package org.cg.common.core;

import org.cg.common.core.AbstractLogger;

public class SystemLogger extends AbstractLogger {
	
	private static SystemLogger instance;
	
	public static SystemLogger instance() {
		if (instance == null) 
			instance = new SystemLogger();
		return instance;
	}
	
	@Override
	protected void hdlInfo(String info) {
		System.out.println("INFO: " + info);
	};

	@Override
	protected void hdlError(String error) {
		System.out.println("ERROR: " + error);
	};

}
