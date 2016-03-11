package org.cg.ftc.ftcClientJava;

import java.util.HashMap;
import java.util.Map;

import org.cg.common.check.Check;
import org.cg.common.core.DelegatingLogger;
import org.cg.common.core.SystemLogger;
import org.cg.ftc.shared.interfaces.Connector;

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

}