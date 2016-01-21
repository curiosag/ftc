package org.cg.common.io.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.cg.common.check.Check;
import org.cg.common.io.logging.OnLineWritten;

public class CallbackHandler extends Handler {

	private OnLineWritten callback;

	int count = 0;
	
	public CallbackHandler(OnLineWritten callback){
		Check.notNull(callback);
		this.callback = callback;
	}
	
	@Override
	public void publish(LogRecord record) {
		callback.notify(record.getMessage());
	}

	@Override
	public void flush() {
		count++;
	}

	@Override
	public void close() throws SecurityException {
		
	}

}
