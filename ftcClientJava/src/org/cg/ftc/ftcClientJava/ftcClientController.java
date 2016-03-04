package org.cg.ftc.ftcClientJava;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.SwingWorker;

import org.cg.common.check.Check;
import org.cg.common.core.AbstractLogger;
import org.cg.common.http.HttpStatus;
import org.cg.common.interfaces.Continuation;
import org.cg.common.interfaces.Progress;
import org.cg.common.io.FileUtil;
import org.cg.common.io.StringStorage;
import org.cg.common.misc.CmdDestination;
import org.cg.common.misc.CmdHistory;
import org.cg.common.threading.Function;
import org.cg.ftc.shared.interfaces.CompletionsSource;
import org.cg.ftc.shared.interfaces.Connector;
import org.cg.ftc.shared.interfaces.OnFileAction;
import org.cg.ftc.shared.interfaces.SyntaxElement;
import org.cg.ftc.shared.interfaces.SyntaxElementSource;
import org.cg.ftc.shared.structures.ClientSettings;
import org.cg.ftc.shared.structures.Completions;
import org.cg.ftc.shared.structures.ConnectionStatus;
import org.cg.ftc.shared.structures.QueryResult;
import org.cg.ftc.shared.uglySmallThings.AsyncWork;
import org.cg.ftc.shared.uglySmallThings.CSV;
import org.cg.ftc.shared.uglySmallThings.Events;
import org.cg.ftc.shared.uglySmallThings.SwingWorkerExt;

import com.google.common.base.Stopwatch;

import manipulations.QueryHandler;

public class ftcClientController implements ActionListener, SyntaxElementSource, CompletionsSource {

	public final ftcClientModel model;
	private final QueryHandler queryHandler;
	private final AbstractLogger logging;
	private final ClientSettings clientSettings;
	private final Connector connector;
	private final Progress progress;

	private SwingWorkerExt<QueryResult, Object> executionWorker = AsyncWork.createEmptyWorker();
	private final CmdHistory history;
	private final CmdDestination historyScrollDestination = new CmdDestination() {
		@Override
		public void set(String cmd) {
			model.queryText.setValue(cmd);
		}
	};

	private Stopwatch executionStopwatch = Stopwatch.createUnstarted();
	private boolean isExecuting = false;
	private boolean refreshTablesOngoing;

	public ftcClientController(ftcClientModel model, AbstractLogger logging, Connector connector,
			ClientSettings clientSettings, StringStorage cmdHistoryStorage, Progress progress) {
		this.model = model;
		this.queryHandler = new QueryHandler(logging, connector, clientSettings);
		this.logging = logging;
		this.clientSettings = clientSettings;
		this.connector = connector;
		history = new CmdHistory(cmdHistoryStorage);
		this.progress = progress;
	}

	private synchronized void setStateIsExecuting(boolean value) {
		Check.isFalse(isExecuting && value);
		if (value) {
			Events.ui.post(Thread.State.RUNNABLE);
			executionStopwatch.reset();
			executionStopwatch.start();
		} else if (isExecuting) {
			executionStopwatch.stop();
			Events.ui.post(Thread.State.TERMINATED);
		}
		isExecuting = value;
	}

	private boolean getStateIsExecuting() {
		return isExecuting;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {
		case Const.execSql:
			if (!getStateIsExecuting())
				hdlExecSql();
			break;

		case Const.cancelExecution:
			hdlCancelExecution();
			break;

		case Const.listTables:
			model.resultData.setValue(queryHandler.getTableInfo());
			break;

		case Const.viewPreprocessedQuery:
			logging.Info(queryHandler.previewExecutedSql(model.queryText.getValue()));
			break;

		case Const.memorizeQuery:
			hdlRememberCommand();
			break;

		case Const.previousCommand:
			history.prev(historyScrollDestination);
			break;

		case Const.nextCommand:
			history.next(historyScrollDestination);
			break;

		case Const.fileOpen:
			hdlFileOpen();
			break;

		case Const.fileSave:
			hdlFileSave();
			break;

		case Const.exportCsv:
			hdlExportCsvAction(e);
			break;

		case Const.authorize:
			if (!getStateIsExecuting())
				hdlAuthenticate();
			break;

		case Const.refreshTables:
			hdlRefreshTables();

		default:
			break;
		}
	}

	private void hdlRefreshTables() {
		if (!getRefreshTablesOngoing())
			AsyncWork.goUnderground(new Function<Void>() {
				@Override
				public synchronized Void invoke(Progress progress) {
					try {
						setRefreshTablesOngoing(true);
						queryHandler.reloadTableList();
						return null;
					} catch (Exception e) {
						return null;
					}
				}
			}, new Continuation<Void>() {
				@Override
				public void invoke(Void value) {
					setRefreshTablesOngoing(false);
				}
			}, AsyncWork.noProgress).execute();
	}

	private synchronized void setRefreshTablesOngoing(boolean b) {
		refreshTablesOngoing = b;
	}

	private synchronized boolean getRefreshTablesOngoing() {
		return refreshTablesOngoing;
	}

	private void hdlRememberCommand() {
		history.add(model.queryText.getValue());
		logging.Info("command memorized");
	}

	private void hdlExecSql() {
		AsyncWork.goUnderground(new Function<QueryResult>() {
			@Override
			public QueryResult invoke(Progress progress) {
				setStateIsExecuting(true);
				try {
					return getQueryResult(progress);
				} catch (Exception e) {
					return new QueryResult(HttpStatus.SC_METHOD_FAILURE, null,
							"Exception occured: " + e.getClass().getSimpleName() + " " + e.getMessage());
				}
			}
		}, new Continuation<QueryResult>() {
			@Override
			public void invoke(QueryResult value) {
				onQueryResult(value);
				setStateIsExecuting(false);
			}
		}, progress).execute();
	}

	private QueryResult getQueryResult(Progress progress) {
		String sql = model.queryText.getValue();
		history.add(sql);
		return queryHandler.getQueryResult(sql, progress);
	}

	private void onQueryResult(QueryResult result) {
		if (result.data.isPresent())
			model.resultData.setValue(result.data.get());

		String msg;
		if (result.data.isPresent())
			msg = String.format("Read %d records ", result.data.get().getRowCount());
		else
			msg = "Executed query ";

		float elapsed = (float) executionStopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000;
		msg = msg + String.format("in %.3f seconds \n", elapsed);
		logging.Info(msg);
		if (result.message.isPresent())
			logging.Info(result.message.get());
	}

	private void hdlCancelExecution() {
		cancelSynchronousQueryProcessing();
		cancelAsynchronousQueryProcessing();
		logging.Info("execution cancelled");	
		setStateIsExecuting(false);
	}

	private void cancelSynchronousQueryProcessing() {
		if (getStateIsExecuting())
			if (!(executionWorker.isDone() || executionWorker.isCancelled()))
			{
				executionWorker.saveCancel(true);
				setStateIsExecuting(false);
			}
	}

	private void cancelAsynchronousQueryProcessing() {
		// if it seems odd to call this asynchronous: it is meant for the
		// composite query cases
		// where the execution worker terminates immediately and queryHandler
		// goes on processing the composites multi-threaded
		queryHandler.cancelRequest();
	}

	private final Function<ConnectionStatus> authFunction = new Function<ConnectionStatus>() {
		@Override
		public ConnectionStatus invoke(Progress progress) {
			return resetConnector();
		}
	};

	private final Continuation<ConnectionStatus> authContinuation = new Continuation<ConnectionStatus>() {
		@Override
		public void invoke(ConnectionStatus value) {
		}
	};

	private void hdlAuthenticate() {
		connector.clearStoredLoginData();
		authenticate();
	}

	public void authenticate() {
		SwingWorker<ConnectionStatus, Object> connectionWorker = AsyncWork.goUnderground(authFunction,
				authContinuation);
		setStateIsExecuting(true);
		connectionWorker.execute();
		logging.Info("attempting to authorize");
		String msgAuthFailed = "authorization failed: ";
		try {
			ConnectionStatus result = connectionWorker.get(clientSettings.authTimeout, TimeUnit.SECONDS);
			if (result.status == HttpStatus.SC_OK)
				logging.Info("authorization succeeded");
			else
				logging.Info(msgAuthFailed + result.message.or("unknown reason"));
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			connectionWorker.cancel(true);
			logging.Info(String.format("%s %s %s", msgAuthFailed, e.getClass().getSimpleName(), e.getMessage()));
		}
		setStateIsExecuting(false);
	}

	private void hdlFileOpen() {
		new FileAction("Open", clientSettings.pathScriptFile, null, new OnFileAction() {

			@Override
			public void onFileAction(ActionEvent e, File file) {
				model.queryText.setValue(FileUtil.readFromFile(file.getPath()));
				clientSettings.pathScriptFile = FileUtil.getPathOnly(file);
			}
		}).actionPerformed(null);
	}

	private void hdlFileSave() {

		new FileAction("Save", clientSettings.pathScriptFile, null, new OnFileAction() {

			@Override
			public void onFileAction(ActionEvent e, File file) {
				FileUtil.writeToFile(model.queryText.getValue(), file.getPath());
				clientSettings.pathScriptFile = FileUtil.getPathOnly(file);
			}
		}).actionPerformed(null);
	}

	private void hdlExportCsvAction(ActionEvent e) {
		if (tablePopulated()) {
			new FileAction("Export", clientSettings.pathCsvFile, null, new OnFileAction() {

				@Override
				public void onFileAction(ActionEvent e, File file) {
					clientSettings.pathCsvFile = FileUtil.getPathOnly(file);
					logging.Info(CSV.write(model.resultData.getValue(), file.getPath()));
				}
			}).actionPerformed(e);
		} else
			logging.Info("no data to export");
	}

	private boolean tablePopulated() {
		return model.resultData.getValue() != null && model.resultData.getValue().getRowCount() > 0;
	}

	@Override
	public List<SyntaxElement> get(String query) {
		return queryHandler.getHighlighting(query);
	}

	@Override
	public Completions get(String query, int cursorPos) {
		return queryHandler.getPatcher(query, cursorPos).getCompletions();
	}

	private ConnectionStatus resetConnector() {
		Dictionary<String, String> credentials = new Hashtable<String, String>();
		credentials.put(ClientSettings.keyClientSecret, model.clientSecret.getValue());
		credentials.put(ClientSettings.keyClientId, model.clientId.getValue());
		return queryHandler.reset(credentials);
	}

}