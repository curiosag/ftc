package org.cg.eclipse.plugins.ftc.handlers;

import org.cg.eclipse.plugins.ftc.glue.FtcPluginClient;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class FtcCommandHandler extends AbstractHandler {

	public FtcCommandHandler() {
	}

	public Object execute(ExecutionEvent e) throws ExecutionException {
		FtcPluginClient.getDefault().runCommand(e.getCommand().getId());
		return null;
	}
}
