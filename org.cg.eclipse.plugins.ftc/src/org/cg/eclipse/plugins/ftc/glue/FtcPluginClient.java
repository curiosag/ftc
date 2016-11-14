package org.cg.eclipse.plugins.ftc.glue;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import org.cg.common.check.Check;
import org.cg.common.io.FileUtil;
import org.cg.common.io.PreferencesStringStorage;
import org.cg.common.util.Op;
import org.cg.common.util.StringUtil;
import org.cg.eclipse.plugins.ftc.FtcEditor;
import org.cg.eclipse.plugins.ftc.MessageConsoleLogger;
import org.cg.eclipse.plugins.ftc.PluginConst;
import org.cg.eclipse.plugins.ftc.WorkbenchUtil;
import org.cg.eclipse.plugins.ftc.preference.PreferenceInitializer;
import org.cg.eclipse.plugins.ftc.view.ResultView;
import org.cg.ftc.ftcClientJava.BaseClient;
import org.cg.ftc.ftcClientJava.Const;
import org.cg.ftc.ftcClientJava.GuiClient;
import org.cg.ftc.ftcClientJava.Observism;
import org.cg.ftc.ftcClientJava.ftcClientController;
import org.cg.ftc.ftcClientJava.ftcClientModel;
import org.cg.ftc.shared.interfaces.SyntaxElementSource;
import org.cg.ftc.shared.structures.ClientSettings;
import org.cg.ftc.shared.structures.Completions;
import org.cg.ftc.shared.structures.RunState;
import org.cg.ftc.shared.uglySmallThings.Events;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.IJobFunction;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import com.google.common.eventbus.Subscribe;

import com.google.common.base.Optional;

public class FtcPluginClient extends BaseClient {

	private static FtcPluginClient _default;

	private final ClientSettings clientSettings = ClientSettings.instance(GuiClient.class);
	private final ftcClientModel model = new ftcClientModel(clientSettings);
	private final CancellableProgress progress = createProgress();
	private final ftcClientController controller = new ftcClientController(model, logging, getConnector(),
			clientSettings,
			new PreferencesStringStorage(org.cg.ftc.shared.uglySmallThings.Const.PREF_ID_CMDHISTORY, GuiClient.class),
			progress);

	private Optional<FtcEditor> activeEditor = Optional.absent();
	private final IPreferenceStore preferenceStore = new FtcPreferenceStore(clientSettings);
	private boolean busy;
	private boolean offline;

	private final List<FtcEditor> editors = new LinkedList<FtcEditor>();

	public IPreferenceStore getPreferenceStore() {
		return preferenceStore;
	};

	public void reAuthenticate() {
		doControllerAction(Const.authorize);
	}

	private void authenticate() {
		if (!offline) {
			controller.authenticate();
		} else
			logging.Info("working offline");
	}

	public SyntaxElementSource getSyntaxElementSource() {
		return controller;
	}

	public EclipseStyleCompletions getCompletions(String text, int cursorPos) {
		model.caretPositionQueryText = cursorPos;

		Completions completions = controller.get(text, cursorPos);
		return new EclipseStyleCompletions(completions);
	}

	public void registerEditor(FtcEditor e) {
		if (editors.indexOf(e) < 0)
			editors.add(e);
	}

	public void unRegisterEditor(FtcEditor e) {
		editors.remove(e);
	}

	public void onEditorActivated(IWorkbenchPart e) {
		activeEditor = Optional.of((FtcEditor) e);
	}

	private void authenticateOnStartup() {
		String id = model.clientId.getValue();
		String secret = model.clientSecret.getValue();
		String storedCredential = preferenceStore.getString("StoredCredential");
		if (StringUtil.emptyOrNull(id) || StringUtil.emptyOrNull(secret))
			logging.Info(
					"Login credentials to access google data are not set.\n Use the Fusion Tables Console preference page\nto set them and to authenticate.");
		// a valid credential has > 1Kb size
		else if (storedCredential == null || storedCredential.length() < 1000)
			logging.Info(
					"Authentication is required to access google data.\n Use the Fusion Tables Console preference page to authenticate.");

		if (model.clientId.getValue() != null && model.clientSecret.getValue() != null) {

			if (storedCredential != null && storedCredential.length() > 1000)
				authenticate();
		}
	}

	public void onEditorClosed(IWorkbenchPart e) {
		Check.isTrue(e instanceof FtcEditor);
		unRegisterEditor((FtcEditor) e);
		activeEditor = Optional.absent();
	}

	public static FtcPluginClient getDefault() {
		if (_default == null) {
			_default = new FtcPluginClient();
			// wont get called from framework?
			// can't happen in constructor
			(new PreferenceInitializer()).initializeDefaultPreferences();
		}
		return _default;
	}

	private FtcPluginClient() {
		model.resultData.addObserver(createResultDataObserver());
		model.resultText.addObserver(createOpResultObserver());

		model.clientId.setValue(clientSettings.clientId);
		model.clientSecret.setValue(clientSettings.clientSecret);

		logging.setDelegate(MessageConsoleLogger.getDefault());

		registerForLongOperationEvent();

		initOfflineStatus();
		addPreferencesListeners();
		authenticateOnStartup();
	}

	private void addPreferencesListeners() {
		preferenceStore.addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(FtcPreferenceStore.KEY_OFFLINE))
					resetOfflineStatus(Unbox.asBoolean(event.getNewValue()));
				if (event.getProperty().equals(FtcPreferenceStore.KEY_CLIENT_ID))
					model.clientId.setValue(Unbox.asString(event.getNewValue()));
				if (event.getProperty().equals(FtcPreferenceStore.KEY_CLIENT_SECRET))
					model.clientSecret.setValue(Unbox.asString(event.getNewValue()));
			}
		});

	}

	private void initOfflineStatus() {
		offline = preferenceStore.getBoolean(FtcPreferenceStore.KEY_OFFLINE);
		controller.setOffline(offline);
	}

	private void resetOfflineStatus(boolean isOffline) {
		boolean recent = offline;
		offline = isOffline;
		if (recent != offline) {
			if (!offline)
				authenticate();
			controller.setOffline(offline);
			refreshEditors();
		}
	}

	private void refreshEditors() {
		for (FtcEditor e : editors)
			e.invalidateTextRepresentation();
	}

	private CancellableProgress createProgress() {
		FtcPluginClient client = this;

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

					private void hdlInternallyCancelled(FtcPluginClient client) {
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

	private final String eclipseCmdPrefix = "org.cg.eclipse.plugins.ftc.";

	private String convertCmd(String eclipseCmd) {
		return eclipseCmd.replace(eclipseCmdPrefix, "");
	}

	public void runCommand(String commandId) {
		if (activeEditor.isPresent()) {
			model.caretPositionQueryText = activeEditor.get().getCaretOffset();
			model.queryText.setValue(activeEditor.get().getText());

			if (busy && commandId.equals(Const.cancelExecution)) {
				progress.cancel();
				doControllerAction(convertCmd(commandId));
			} else {
				if (busy)
					logging.Info("Operation in progress...");
				else {
					if (commandId.equals(PluginConst.CMD_EXPORT_CSV))
						hdlExportCsv();
					else
						doControllerAction(convertCmd(commandId));
				}
			}
		}

	}

	private void doControllerAction(String commandId) {
		controller.actionPerformed(new ActionEvent(this, 0, commandId));
	}

	public void hdlExportCsv() {
		String delim = preferenceStore.getString(FtcPreferenceStore.KEY_CSV_DELIMITER);
		String quote = preferenceStore.getString(FtcPreferenceStore.KEY_CSV_QUOTECHAR);
		String csvData = getResultView().getCsv(delim, quote);

		FileDialog dialog = new FileDialog(WorkbenchUtil.getShell(), SWT.SAVE);

		dialog.setFilterPath(preferenceStore.getString(FtcPreferenceStore.KEY_LAST_EXPORT_PATH));
		dialog.setFilterNames(new String[] { "csv files", "All Files (*.*)" });
		dialog.setFilterExtensions(new String[] { "*.csv", "*.*" });
		String fullPath = dialog.open();

		if (fullPath != null) {
			File f = new File(fullPath);
			String path = f.getPath();
			FileUtil.writeToFile(csvData, fullPath);
			preferenceStore.setValue(FtcPreferenceStore.KEY_LAST_EXPORT_PATH, path);
		}
	}

	private Observer createOpResultObserver() {
		return new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						logging.Info(Observism.decodeTextModelObservable(o));
					}
				});

			}
		};
	}

	private Observer createResultDataObserver() {
		return new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						getResultView().displayTable(Observism.decodeTableModelObservable(o));
					}
				});
			}
		};

	}

	private ResultView getResultView() {
		IViewPart view = WorkbenchUtil.showView(PluginConst.RESULT_VIEW_ID);
		Check.isTrue(view instanceof ResultView);
		return (ResultView) view;
	}

	private void registerForLongOperationEvent() {
		Events.ui.register(this);
	}

	@Subscribe
	public void eventBusOnLongOperation(RunState state) {
		busy = Op.in(state, RunState.AUTHENTICATION_STARTED, RunState.QUERYEXEC_STARTED);
	}
}
