package org.cg.common.core;

import org.cg.common.core.AbstractLogger;

public class DelegatingLogger extends AbstractLogger {

	AbstractLogger logger = null;

	public DelegatingLogger() {
	}

	public DelegatingLogger(AbstractLogger logger) {
		setDelegate(logger);
	}
	
	@Override
	protected void hdlInfo(String info) {
		if (logger != null)
			logger.hdlInfo(info);
	}

	@Override
	protected void hdlError(String error) {

		if (logger != null)
			logger.hdlError(error);

	}

	public void setDelegate(AbstractLogger logger) {
		this.logger = logger;
	}

}
