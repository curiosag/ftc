package org.cg.eclipse.plugins.ftc.glue;

import java.awt.event.ActionEvent;
import org.cg.common.check.Check;
import org.cg.common.io.PreferencesStringStorage;
import org.cg.common.util.Op;
import org.cg.common.util.StringUtil;
import org.cg.eclipse.plugins.ftc.FtcEditor;
import org.cg.eclipse.plugins.ftc.MessageConsoleLogger;
import org.cg.eclipse.plugins.ftc.PluginConst;
import org.cg.eclipse.plugins.ftc.preference.PreferenceInitializer;
import org.cg.ftc.ftcClientJava.BaseClient;
import org.cg.ftc.ftcClientJava.Const;
import org.cg.ftc.ftcClientJava.GuiClient;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import com.google.common.eventbus.Subscribe;

public class FtcPluginClient extends BaseClient {

	private static FtcPluginClient _default;
	private boolean debug = false;

	private final ClientSettings clientSettings = ClientSettings.instance(GuiClient.class);
	private final ftcClientModel model = new ftcClientModel(clientSettings);
	private final CancellableProgress progress = createProgress();
	private final ftcClientController controller = new ftcClientController(model, logging, getConnector(),
			clientSettings,
			new PreferencesStringStorage(org.cg.ftc.shared.uglySmallThings.Const.PREF_ID_CMDHISTORY, GuiClient.class),
			progress);

	private final IPreferenceStore preferenceStore = new FtcPreferenceStore(clientSettings);
	private boolean busy;
	private final FtcPluginFrontEnd frontEnd;
	
	public IPreferenceStore getPreferenceStore() {
		return preferenceStore;
	};

	public void reAuthenticate() {
		doControllerAction(Const.authorize);
	}

	public SyntaxElementSource getSyntaxElementSource() {
		return controller;
	}

	public EclipseStyleCompletions getCompletions(String text, int cursorPos) {
		model.caretPositionQueryText = cursorPos;

		Completions completions = controller.get(text, cursorPos);

		if (debug)
			MessageConsoleLogger.getDefault().Info(
					String.format("%d completions at index %d for query:\n->%s", completions.size(), cursorPos, text));

		return new EclipseStyleCompletions(completions);
	}

	public void registerEditor(FtcEditor e) {
		frontEnd.registerEditor(e);
	}

	public void unRegisterEditor(FtcEditor e) {
		frontEnd.unRegisterEditor(e);
	}

	public void onEditorActivated(IWorkbenchPart e) {
		frontEnd.onEditorActivated(e);
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
				controller.authenticate();
		}
	}

	public void onEditorClosed(IWorkbenchPart e) {
		Check.isTrue(e instanceof FtcEditor);
		unRegisterEditor((FtcEditor) e);
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
		frontEnd = new FtcPluginFrontEnd(preferenceStore, progress, logging);
		clientSettings.offline = preferenceStore.getBoolean(FtcPreferenceStore.KEY_OFFLINE);
		
		setUp(clientSettings, model, controller, frontEnd);
		
		logging.setDelegate(MessageConsoleLogger.getDefault());

		registerForLongOperationEvent();

		
		authenticateOnStartup();
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
  	if (frontEnd.activeEditorPresent()) {
		if (busy && commandId.equals(Const.cancelExecution)) {
				progress.cancel();
				doControllerAction(convertCmd(commandId));
			} else {
				if (busy)
					logging.Info("Operation in progress...");
				else {
					if (commandId.equals(PluginConst.CMD_EXPORT_CSV))
						frontEnd.hdlExportCsv();
					else
						doControllerAction(convertCmd(commandId));
				}
			}
		}

	}

	private void doControllerAction(String commandId) {
		controller.actionPerformed(new ActionEvent(this, 0, commandId));
	}

	private void registerForLongOperationEvent() {
		Events.ui.register(this);
	}

	@Subscribe
	public void eventBusOnLongOperation(RunState state) {
		busy = Op.in(state, RunState.AUTHENTICATION_STARTED, RunState.QUERYEXEC_STARTED);
	}



}
