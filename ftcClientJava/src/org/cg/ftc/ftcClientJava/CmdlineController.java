package org.cg.ftc.ftcClientJava;

import java.awt.event.ActionEvent;

import org.cg.common.core.AbstractLogger;
import org.cg.common.interfaces.Progress;
import org.cg.common.io.StringStorage;
import org.cg.ftc.shared.interfaces.Connector;
import org.cg.ftc.shared.structures.ClientSettings;

public class CmdlineController extends ClientController {

	public CmdlineController(ClientModel model, AbstractLogger logging, Connector connector,
			ClientSettings clientSettings, StringStorage cmdHistoryStorage, Progress progress) {
		super(model, logging, connector, clientSettings, cmdHistoryStorage, progress);
	}

	@Override
	protected void hdlFileOpen() {
	}

	@Override
	protected void hdlFileSave() {
	}

	@Override
	protected void hdlFileSaveAs() {
	}

	@Override
	protected void hdlExportCsvAction(ActionEvent e) {
	}

	
	
}
