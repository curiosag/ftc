package org.cg.eclipse.plugins.ftc.glue;

import java.awt.event.ActionEvent;
import java.io.File;

import org.cg.common.core.AbstractLogger;
import org.cg.common.io.StringStorage;
import org.cg.common.util.Op;
import org.cg.eclipse.plugins.ftc.WorkbenchUtil;
import org.cg.ftc.ftcClientJava.ClientController;
import org.cg.ftc.ftcClientJava.ClientModel;
import org.cg.ftc.ftcClientJava.Const;
import org.cg.ftc.ftcClientJava.DelegatingProgress;
import org.cg.ftc.shared.interfaces.Connector;
import org.cg.ftc.shared.structures.ClientSettings;
import org.cg.ftc.shared.structures.RunState;
import org.cg.ftc.shared.uglySmallThings.CSV;
import org.cg.ftc.shared.uglySmallThings.Events;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.IJobFunction;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.google.common.eventbus.Subscribe;

public class EclipseClientController extends ClientController {

	private final IPreferenceStore preferenceStore;
	private final CancellableProgress progress = createProgress();
	private final FtcPluginFrontEnd frontEnd;
	private boolean busy = false;

	public EclipseClientController(FtcPluginFrontEnd frontEnd, IPreferenceStore preferenceStore, ClientModel model,
			AbstractLogger logging, Connector connector, ClientSettings clientSettings, DelegatingProgress progress) {
		super(model, logging, connector, clientSettings, new StringStorage() {

			@Override
			public String read() {
				return null;
			}

			@Override
			public void write(String s) {
			}
		}, progress);
		this.preferenceStore = preferenceStore;
		this.frontEnd = frontEnd;
		progress.setDelegate(progress);
		registerForLongOperationEvent();
	}

	public void doControllerAction(String commandId) {
		actionPerformed(new ActionEvent(this, 0, convertCmd(commandId)));
	}

	private final String eclipseCmdPrefix = "org.cg.eclipse.plugins.ftc.";

	private String convertCmd(String eclipseCmd) {
		return eclipseCmd.replace(eclipseCmdPrefix, "");
	}

	/**
	 * empty implementation, handled by framework
	 */
	@Override
	protected void hdlFileOpen() {
	}

	/**
	 * empty implementation, handled by framework
	 */
	@Override
	protected void hdlFileSave() {
	}

	/**
	 * empty implementation, handled by framework
	 */
	@Override
	protected void hdlFileSaveAs() {
	}

	@Override
	protected void hdlExportCsvAction(ActionEvent e) {
		hdlExportCsv();
	}

	public void hdlExportCsv() {
		if (model.resultData.getValue() == null || model.resultData.getValue().getRowCount() == 0)
			logging.Info("No data to export");
		else {
			String delim = preferenceStore.getString(FtcPreferenceStore.KEY_CSV_DELIMITER);
			String quote = preferenceStore.getString(FtcPreferenceStore.KEY_CSV_QUOTECHAR);

			FileDialog dialog = new FileDialog(WorkbenchUtil.getShell(), SWT.SAVE);

			dialog.setFilterPath(preferenceStore.getString(FtcPreferenceStore.KEY_LAST_EXPORT_PATH));
			dialog.setFilterNames(new String[] { "csv files", "All Files (*.*)" });
			dialog.setFilterExtensions(new String[] { "*.csv", "*.*" });
			String fullPath = dialog.open();

			if (fullPath != null) {
				createCsv(delim, quote).write(model.resultData.getValue(), fullPath);
				preferenceStore.setValue(FtcPreferenceStore.KEY_LAST_EXPORT_PATH, new File(fullPath).getPath());
			}
		}
	}

	private CSV createCsv(String delim, String quote) {
		CSV result = new CSV();

		return result.withDelimiter(limitToFirstChar("delimiter", delim)).withQuote(limitToFirstChar("quote", quote));
	}

	private char limitToFirstChar(String element, String value) {
		if (value == null || value.length() == 0)
			return '\u0000';
		else {
			if (value.length() > 1)
				logging.Info(
						String.format("Export csv: taking only 1st character as %s from value %s", element, value));
			return value.charAt(0);
		}
	}

	public void runCommand(String commandId) {
		if (busy && commandId.equals(Const.cancelExecution)) {
			progress.cancel();
			doControllerAction(commandId);
		} else {
			if (busy)
				logging.Info("Operation in progress...");
			else {
				doControllerAction(commandId);
			}
		}
	}

	private CancellableProgress createProgress() {
		EclipseClientController client = this;

		return new CancellableProgress() {

			int curr;
			SubMonitor m;
			private boolean cancelled;

			@Override
			public void init(int max) {
				cancelled = false;
				curr = 0;
				Job.create("ftc composite query ", new IJobFunction() {

					@Override
					public IStatus run(IProgressMonitor monitor) {

						m = SubMonitor.convert(monitor, max);
						while (!cancelled && curr < max) {
							if (monitor.isCanceled())
								hdlInternallyCancelled(client);

							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
							}
						}
						m.done();
						return Status.OK_STATUS;
					}

					private void hdlInternallyCancelled(EclipseClientController client) {
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								client.runCommand(Const.cancelExecution);
							}
						});
						cancelled = true;
					}
				}).schedule();

			}

			@Override
			public void announce(int progress) {
				if (!cancelled)
					m.worked(progress - curr);
				curr = progress;
			}

			@Override
			public void cancel() {
				cancelled = true;
			}
		};
	}

	private void registerForLongOperationEvent() {
		Events.ui.register(this);
	}

	@Subscribe
	public void eventBusOnLongOperation(RunState state) {
		busy = Op.in(state, RunState.AUTHENTICATION_STARTED, RunState.QUERYEXEC_STARTED);
		if (!busy)
			frontEnd.acivateActiveEditor();
	}

}
