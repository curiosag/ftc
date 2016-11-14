package org.cg.eclipse.plugins.ftc.glue;

import org.cg.common.core.AbstractLogger;

public class FtcLogger extends AbstractLogger {

	@Override
	protected void hdlInfo(String info) {
		System.out.println(info);
	}

	@Override
	protected void hdlError(String error) {
		System.out.println(error);
	}

}
