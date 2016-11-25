package org.cg.ftc.ftcClientJava;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextField;

import org.cg.common.check.Check;
import org.cg.common.core.DelegatingLogger;
import org.cg.common.core.SystemLogger;
import org.cg.common.interfaces.OnTextFieldChangedEvent;
import org.cg.common.interfaces.OnValueChanged;
import org.cg.common.interfaces.Progress;
import org.cg.ftc.shared.interfaces.Connector;
import org.cg.ftc.shared.structures.ClientSettings;

import com.google.common.base.Optional;

import main.java.fusiontables.AuthInfo;
import main.java.fusiontables.FusionTablesConnector;
import test.MockConnector;

public abstract class BaseClient {

	protected static final ConnectorTypes connectorType = ConnectorTypes.gftConnector;
	protected static Map<ConnectorTypes, Connector> connectors = new HashMap<ConnectorTypes, Connector>();

	protected final static DelegatingLogger logging = new DelegatingLogger(new SystemLogger());

	static {
		connectors.put(ConnectorTypes.mockConnector, MockConnector.instance());
		Optional<AuthInfo> noAuth = Optional.absent();
		connectors.put(ConnectorTypes.gftConnector,
				new FusionTablesConnector(logging, noAuth, GuiClient.class));
	}
	
	protected static Connector getConnector() {
		Connector result = connectors.get(connectorType);
		Check.notNull(result);
		return result;
	}

	public BaseClient() {
		super();
	}
	
	protected static OnTextFieldChangedEvent createOnTextFieldChangedEvent(final TextModel target) {
		return new OnTextFieldChangedEvent() {

			@Override
			public void notify(String valueChanged) {
				target.setValue(valueChanged);
			}
		};
	}
		
	protected abstract OnValueChanged<Integer> createQueryCaretChangedListener(final ftcClientModel model);

	protected void setUp(final ClientSettings clientSettings, final ftcClientModel model, final ftcClientController controller, FrontEnd ui) {
		
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
		
		// model values must be set after all listeners are in place
		model.clientId.setValue(clientSettings.clientId);
		model.clientSecret.setValue(clientSettings.clientSecret);
	}
	
}