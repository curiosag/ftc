package org.cg.common.core;

import com.google.common.base.Optional;

public interface Logging {
	public void Info(String info);
	public void Error(String error);
	
	public Optional<String> lastInfo();
	public Optional<String> lastError();
	
}

