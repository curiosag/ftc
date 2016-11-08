package org.cg.ftc.ftcClientJava;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
import org.cg.ftc.shared.interfaces.*;
import org.cg.ftc.shared.structures.*;
import org.cg.ftc.shared.uglySmallThings.*;

import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import com.google.common.eventbus.Subscribe;

import manipulations.QueryHandler;
import manipulations.Split;

public class ftcClientController implements ActionListener, SyntaxElementSource, CompletionsSource {

	public final ftcClientModel model;
	private final QueryHandler queryHandler;
	private final AbstractLogger logging;
	private final ClientSettings clientSettings;
	private final Connector connector;
	private final Progress progress;
	private final Queue<String> sqlStatementQueue = new LinkedList<String>();
	private Event allQueriesProcessed = null;

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
	private Optional<File> lastFileUsed = Optional.absent();

	public ftcClientController(ftcClientModel model, AbstractLogger logging, Connector connector,
			ClientSettings clientSettings, StringStorage cmdHistoryStorage, Progress progress) {
		this.model = model;
		this.queryHandler = new QueryHandler(logging, connector, clientSettings);
		this.logging = logging;
		this.clientSettings = clientSettings;
		this.connector = connector;
		history = new CmdHistory(cmdHistoryStorage);
		this.progress = progress;
		registerForQueryFinishedEvent();
	}

	private synchronized void setStateQueryIsExecuting(boolean value) {
		Check.isFalse(isExecuting && value);
		if (value) {
			Events.ui.post(RunState.QUERYEXEC_STARTED);
			executionStopwatch.reset();
			executionStopwatch.start();
		} else if (isExecuting) {
			executionStopwatch.stop();
			Events.ui.post(RunState.QUERYEXEC_FINISHED);
		}
		isExecuting = value;
	}

	private boolean getStateIsExecuting() {
		return isExecuting;
	}

	private final static boolean EXECUTE_ALL = true;
	private static final boolean RETURN_SINGLE_QUERY_ANYWAY = true;

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {
		case Const.execSql:
			if (!getStateIsExecuting())
				hdlExecSql(!EXECUTE_ALL);
			break;

		case Const.execAllSql:
			if (!getStateIsExecuting())
				hdlExecSql(EXECUTE_ALL);
			break;

		case Const.cancelExecution:
			hdlCancelExecution();
			break;

		case Const.listTables:
			model.resultData.setValue(queryHandler.getTableInfo());
			break;

		case Const.viewPreprocessedQuery:
			hdlPreview();
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

		case Const.fileSaveAs:
			hdlFileSaveAs();
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

	private void hdlPreview() {
		Optional<QueryAtHand> query = queryHandler.getQueryAtCaretPosition(model.queryText.getValue(), model.caretPositionQueryText,
				RETURN_SINGLE_QUERY_ANYWAY);
		if (query.isPresent())
			logging.Info(queryHandler.previewExecutedSql(query.get().query));
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

	private Continuation<QueryResult> onQueryExecutionFinished = new Continuation<QueryResult>() {
		@Override
		public void invoke(QueryResult result) {

			try {
				if (result.data.isPresent())
					model.resultData.setValue(result.data.get());

				String msg;
				if (result.status == HttpStatus.SC_CONTINUE)
					msg = "Composite queries being processed.";
				else {
					float elapsed = (float) executionStopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000;
					msg = String.format("Executed query in %.3f seconds. %d records returned", elapsed,
							result.data.isPresent() ? result.data.get().getRowCount() : 0);
				}

				logging.Info(msg);
				if (result.message.isPresent())
					logging.Info(result.message.get());

			} finally {
				if (result.status != HttpStatus.SC_CONTINUE)
					setStateQueryIsExecuting(false);
			}

		}
	};

	private Function<QueryResult> createExecuteSqlFunction(final String query) {
		return new Function<QueryResult>() {
			@Override
			public QueryResult invoke(Progress progress) {
				setStateQueryIsExecuting(true);
				try {
					history.add(query);
					return queryHandler.getQueryResult(query, progress, onQueryExecutionFinished);

				} catch (Exception e) {
					return new QueryResult(HttpStatus.SC_METHOD_FAILURE, null,
							"Exception occured: " + e.getClass().getSimpleName() + " " + e.getMessage());
				}
			}
		};
	}

	/**
	 * Using event bus Events.ui for "Execute all Sql statements" Each single
	 * query execution will trigger an event to be fired at its begin
	 * (Thread.State.RUNNABLE) and at its end (Thread.State.TERMINATED), the
	 * latter triggering the processing of the next query if there is any
	 * 
	 * 
	 * the sequence is:
	 * 
	 * executeNextSql()
	 * 
	 * createExecuteSqlFunction()- setStateIsExecuting(true) // -> Events.ui
	 * Thread.State.RUNNABLE
	 * 
	 * onQueryExecutionFinished - setStateIsExecuting(false) // -> Events.ui
	 * Thread.State.TERMINATED
	 * 
	 * eventBusQueryFinishedListener(Thread.State opState)
	 * 
	 * executeNextSql() // if Thread.State.TERMINATED .
	 * 
	 * 
	 * @param executeAll
	 */

	private void hdlExecSql(boolean executeAll) {
		String text = model.queryText.getValue();

		if (!executeAll) {
			Optional<QueryAtHand> query = queryHandler.getQueryAtCaretPosition(text, model.caretPositionQueryText,
					RETURN_SINGLE_QUERY_ANYWAY);
			if (query.isPresent())
				sqlStatementQueue.add(query.get().query);
		} else
			for (Split split : queryHandler.getQueries(text))
				sqlStatementQueue.add(split.text);

		if (sqlStatementQueue.size() > 1)
			logging.Info(String.format("Queued %d queries for execution", sqlStatementQueue.size()));

		if (sqlStatementQueue.isEmpty())
			logging.Info("no query at caret position");

		probeExecuteNextQuery();
	}

	private void registerForQueryFinishedEvent() {
		Events.ui.register(this);
	}

	@Subscribe
	public void eventBusListener(RunState state) {
		if (indicatesCurrentQueryFinished(state))
			probeExecuteNextQuery();
	}

	private boolean indicatesCurrentQueryFinished(RunState state) {
		return state == RunState.QUERYEXEC_FINISHED;
	}

	private void probeExecuteNextQuery() {
		if (sqlStatementQueue.isEmpty())
			triggerAllQueriesProcessed();
		else {
			Continuation<QueryResult> onSingularExecutionFinished = onQueryExecutionFinished;
			AsyncWork.goUnderground(createExecuteSqlFunction(sqlStatementQueue.poll()), onSingularExecutionFinished,
					progress).execute();
		}
	}

	private void hdlCancelExecution() {
		sqlStatementQueue.clear();
		cancelSingularQueryProcessing();
		cancelCompositeQueryProcessing();
		logging.Info("execution cancelled");
		setStateQueryIsExecuting(false);
	}

	private void cancelSingularQueryProcessing() {
		if (getStateIsExecuting())
			if (!(executionWorker.isDone() || executionWorker.isCancelled()))
				executionWorker.saveCancel(true);
	}

	private void cancelCompositeQueryProcessing() {
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
		Events.ui.post(RunState.AUTHENTICATION_STARTED);
		try {
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
		} finally {
			Events.ui.post(RunState.AUTHENTICATION_FINISHED);
		}
	}

	private void hdlFileOpen() {
		new FileAction(FileAction.FILE_OPEN, clientSettings.pathScriptFile, null, new OnFileAction() {

			@Override
			public void onFileAction(ActionEvent e, Optional<File> file) {
				if (file.isPresent()) {
					model.queryText.setValue(FileUtil.readFromFile(file.get().getPath()));
					clientSettings.pathScriptFile = FileUtil.getPathOnly(file.get());
					lastFileUsed = file;
				}
			}

		}).actionPerformed(null);
	}

	private void hdlFileSave() {
		if (lastFileUsed.isPresent())
			FileUtil.writeToFile(model.queryText.getValue(), lastFileUsed.get().getPath());
		else
			hdlFileSaveAs();
	}

	private void hdlFileSaveAs() {
		new FileAction(FileAction.FILE_SAVE_AS, clientSettings.pathScriptFile, null, new OnFileAction() {

			@Override
			public void onFileAction(ActionEvent e, Optional<File> file) {
				if (file.isPresent()) {
					FileUtil.writeToFile(model.queryText.getValue(), file.get().getPath());
					lastFileUsed = file;
					clientSettings.pathScriptFile = FileUtil.getPathOnly(file.get());
				}
			}
		}).actionPerformed(null);
	}

	private void hdlExportCsvAction(ActionEvent e) {
		if (tablePopulated()) {
			new FileAction(FileAction.EXPORT_FILE, clientSettings.pathCsvFile, null, new OnFileAction() {

				@Override
				public void onFileAction(ActionEvent e, Optional<File> file) {
					if (file.isPresent()) {
						clientSettings.pathCsvFile = FileUtil.getPathOnly(file.get());
						logging.Info(CSV.write(model.resultData.getValue(), file.get().getPath()));
					}
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
	public Completions get(String text, int cursorPos) {
		Optional<QueryAtHand> split = queryHandler.getQueryAtCaretPosition(text, model.caretPositionQueryText,
				!RETURN_SINGLE_QUERY_ANYWAY);

		String query = "";
		int caretPosition = 0;
		if (split.isPresent()) {
			query = split.get().query;
			caretPosition = split.get().caretPositon;
		}

		return queryHandler.getPatcher(query, caretPosition).getCompletions();
	}

	private ConnectionStatus resetConnector() {
		Dictionary<String, String> credentials = new Hashtable<String, String>();
		credentials.put(ClientSettings.keyClientSecret, model.clientSecret.getValue());
		credentials.put(ClientSettings.keyClientId, model.clientId.getValue());
		return queryHandler.reset(credentials);
	}

	private void triggerAllQueriesProcessed() {
		if (allQueriesProcessed != null)
			allQueriesProcessed.fire();
	}

	public void setOnAllQueriesProcessed(Event e) {
		this.allQueriesProcessed = e;
	}

}
