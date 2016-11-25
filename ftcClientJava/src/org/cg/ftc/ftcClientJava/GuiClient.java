package org.cg.ftc.ftcClientJava;

import javax.swing.SwingUtilities;

import org.cg.common.check.Check;
import org.cg.common.core.AbstractLogger;
import org.cg.common.interfaces.OnValueChanged;
import org.cg.common.io.PreferencesStringStorage;
import org.cg.common.io.logging.DelegatingOutputStream;
import org.cg.common.io.logging.OnLineWritten;
import org.cg.ftc.shared.interfaces.Connector;
import org.cg.ftc.shared.structures.ClientSettings;

public class GuiClient extends BaseClient {

	final DelegatingProgress progress = new DelegatingProgress();
	final ClientSettings clientSettings = ClientSettings.instance(GuiClient.class);
	final ftcClientModel model = new ftcClientModel(clientSettings);
	final Connector connector = getConnector();
	final ftcClientController controller = new ftcClientController(model, logging, connector, clientSettings,
			new PreferencesStringStorage(org.cg.ftc.shared.uglySmallThings.Const.PREF_ID_CMDHISTORY, GuiClient.class),
			progress);
	
	private GuiClient _default = null;

	private GuiClient() {
		Check.isTrue(_default == null);
		_default  = this;
		
		logging.setDelegate(createModelLogger(model.resultText));
		final TextModel systemIoRedirectTarget = model.resultText;
		forkSystemOutput(systemIoRedirectTarget);
		FrontEnd ui = FtcGui.createAndShowGUI(controller, controller, clientSettings);

		setUp(clientSettings, model, controller, ui);

		progress.setDelegate(ui.getProgressMonitor());
		controller.authenticate();
	}

	@Override
	protected OnValueChanged<Integer> createQueryCaretChangedListener(final ftcClientModel model) {
		return new OnValueChanged<Integer>() {

			@Override
			public void notify(Integer value) {
				model.caretPositionQueryText = value;
			}
		};
	}

	private static void forkSystemOutput(final TextModel model) {
		OnLineWritten redirect = new OnLineWritten() {
			@Override
			public void notify(String value) {
				model.append(LogUtil.getLogLine(value));
			}
		};

		System.setOut(DelegatingOutputStream.createPrintStream(System.out, redirect));
		System.setErr(DelegatingOutputStream.createPrintStream(System.err, redirect));

	}

	private static AbstractLogger createModelLogger(final TextModel resultText) {
		return new AbstractLogger() {

			@Override
			protected void hdlInfo(String info) {
				LogUtil.addLogLine(resultText, info);
			}

			@Override
			protected void hdlError(String error) {
				LogUtil.addLogLine(resultText, error);
			}

		};
	}

	public static void start() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GuiClient();
			}
		});
	}

}
