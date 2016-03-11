package org.cg.ftc.ftcClientJava;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.cg.common.core.AbstractLogger;
import org.cg.common.interfaces.OnTextFieldChangedEvent;
import org.cg.common.interfaces.OnValueChanged;
import org.cg.common.io.PreferencesStringStorage;
import org.cg.common.io.logging.DelegatingOutputStream;
import org.cg.common.io.logging.OnLineWritten;
import org.cg.ftc.shared.interfaces.Connector;
import org.cg.ftc.shared.structures.ClientSettings;

public class GuiClient extends BaseClient {
	
	private static Runnable startup() {
		final DelegatingProgress progress = new DelegatingProgress();
		final ClientSettings clientSettings = ClientSettings.instance(GuiClient.class);
		final ftcClientModel model = new ftcClientModel(clientSettings);
		final Connector connector = getConnector();
		final ftcClientController controller = new ftcClientController(model, logging, connector, clientSettings,
				new PreferencesStringStorage(org.cg.ftc.shared.uglySmallThings.Const.PREF_ID_CMDHISTORY, GuiClient.class), progress);

		logging.setDelegate(createModelLogger(model.resultText));
		final TextModel systemIoRedirectTarget = model.resultText;
		forkSystemOutput(systemIoRedirectTarget);

		return new Runnable() {

			@Override
			public void run() {
				FrontEnd ui = FtcGui.createAndShowGUI(controller, controller, clientSettings);

				model.resultData.addObserver(ui.createResultDataObserver());
				
				model.clientId.addObserver(ui.createClientIdObserver());
				model.clientSecret.addObserver(ui.createClientSecretObserver());
				model.resultText.addObserver(ui.createOpResultObserver());
				model.queryText.addObserver(ui.createQueryObserver());
				
				ui.setActionListener(controller);
				ui.addClientIdChangedListener(createOnTextFieldChangedEvent(model.clientId));
				ui.addClientSecretChangedListener(createOnTextFieldChangedEvent(model.clientSecret));
				ui.addResultTextChangedListener(model.resultText.getListener());
				ui.addQueryTextChangedListener(model.queryText.getListener());
				ui.addQueryCaretChangedListener(createQueryCaretChangedListener(model));
				
				model.clientId.setValue(clientSettings.clientId);
				model.clientSecret.setValue(clientSettings.clientSecret);

				progress.setDelegate(ui.getProgressMonitor());
				controller.authenticate();
			}

			private OnValueChanged<Integer> createQueryCaretChangedListener(final ftcClientModel model) {
				return new OnValueChanged<Integer>(){

					@Override
					public void notify(Integer value) {
						model.caretPositionQueryText = value;
					}};
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

	private static OnTextFieldChangedEvent createOnTextFieldChangedEvent(final TextModel target) {
		return new OnTextFieldChangedEvent() {

			@Override
			public void notify(JTextField field) {
				target.setValue(field.getText());
			}
		};
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
		SwingUtilities.invokeLater(startup());
	}
	
}
