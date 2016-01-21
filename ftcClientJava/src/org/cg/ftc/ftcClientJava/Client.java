package org.cg.ftc.ftcClientJava;

import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;

import org.cg.common.check.Check;
import org.cg.common.core.AbstractLogger;
import org.cg.common.core.DelegatingLogger;
import org.cg.common.core.SystemLogger;
import org.cg.common.interfaces.OnValueChangedEvent;
import org.cg.common.io.PreferencesStringStorage;
import org.cg.common.io.logging.DelegatingOutputStream;
import org.cg.common.io.logging.OnLineWritten;
import org.cg.ftc.shared.interfaces.Connector;
import org.cg.ftc.shared.structures.ClientSettings;
import com.google.common.base.Optional;

import main.java.fusiontables.AuthInfo;
import main.java.fusiontables.FusionTablesConnector;
import test.MockConnector;

public class Client {

	private final static ConnectorTypes connectorType = ConnectorTypes.gftConnector;

	private final static DelegatingLogger logging = new DelegatingLogger(new SystemLogger());

	private static Map<ConnectorTypes, Connector> connectors = new HashMap<ConnectorTypes, Connector>();

	static {
		connectors.put(ConnectorTypes.mockConnector, MockConnector.instance());
		Optional<AuthInfo> noAuth = Optional.absent();
		connectors.put(ConnectorTypes.gftConnector,
				new FusionTablesConnector(logging, noAuth, Client.class));
	}

	private static void interlace(Document d, Observer textModelObserver, TextModel m) {
		d.addDocumentListener(m.getListener());
		m.addObserver(textModelObserver);
	}
	
	private static Connector getConnector() {
		Connector result = connectors.get(connectorType);
		Check.notNull(result);
		return result;
	}

	private static Runnable startup() {
		
		final ClientSettings clientSettings = ClientSettings.instance(Client.class);
		final ftcClientModel model = new ftcClientModel(clientSettings);
		final Connector connector = getConnector();
		final ftcClientController controller = new ftcClientController(model, logging, connector, clientSettings,
				new PreferencesStringStorage(org.cg.ftc.shared.uglySmallThings.Const.PREF_ID_CMDHISTORY, Client.class));

		logging.setDelegate(createModelLogger(model.resultText));
		final TextModel systemIoRedirectTarget = model.resultText;
		forkSystemOutput(systemIoRedirectTarget);

		return new Runnable() {

			@Override
			public void run() {
				FtcGui ui = FtcGui.createAndShowGUI(controller, controller, clientSettings);

				ui.setActionListener(controller);
				model.resultData.addObserver(ui.createResultDataObserver());
				model.clientId.addObserver(ui.createClientIdObserver());
				model.clientSecret.addObserver(ui.createClientSecretObserver());
				ui.addClientIdChangedListener(createOnValueChangedEvent(model.clientId));
				ui.addClientSecretChangedListener(createOnValueChangedEvent(model.clientSecret));

				interlace(ui.opResultDocument(), ui.createOpResultObserver(), model.resultText);
				interlace(ui.queryTextDocument(), ui.createQueryObserver(), model.queryText);

				model.clientId.setValue(clientSettings.clientId);
				model.clientSecret.setValue(clientSettings.clientSecret);

				controller.authenticate();
			}
		};
	}

	private static void forkSystemOutput(final TextModel model) {
		OnLineWritten redirect = new OnLineWritten() {
			@Override
			public void notify(String value) {
				// there's a java 7 issue where the TextModel itself can not be passed to addLogLines
				model.setValue(LogUtil.addLogLine(model.getValue(), value));
			}
		};

		System.setOut(DelegatingOutputStream.createPrintStream(System.out, redirect));
		System.setErr(DelegatingOutputStream.createPrintStream(System.err, redirect));

	}

	private static OnValueChangedEvent createOnValueChangedEvent(final TextModel target) {
		return new OnValueChangedEvent() {

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

	public static void main(String[] args) {
		SwingUtilities.invokeLater(startup());
	}
}
