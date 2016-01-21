package org.cg.common.core;

import org.cg.common.core.AbstractLogger;

public class SystemLogger extends AbstractLogger {
	
	@Override
	protected void hdlInfo(String info) {
		System.out.println("INFO: " + info);
	};

	@Override
	protected void hdlError(String error) {
		System.out.println("ERROR: " + error);
	};

}
