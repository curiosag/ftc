package org.cg.common.core;

import java.util.Observable;

import com.google.common.base.Optional;

import org.cg.common.core.Logging;

public abstract class AbstractLogger extends Observable implements Logging{

	private Optional <String> absent = Optional.absent();
	private Optional <String> lastInfo = absent;
	private Optional <String> lastError = absent;

	abstract protected void hdlInfo(String info);
	abstract protected void hdlError(String error);
	
	@Override
	public void Info(String info) {
		hdlInfo(info);
		lastInfo = Optional.fromNullable(info);
		hdlNotification();
	}

	@Override
	public void Error(String error) {
		hdlError(error);
		lastError = Optional.fromNullable(error);
		hdlNotification();
	}

	private void hdlNotification() {
		setChanged();
		notifyObservers();
		clearChanged();
		lastInfo = absent;
		lastError = absent;
	}
	
	@Override
	public Optional<String> lastInfo() {
		return lastInfo;
	}

	@Override
	public Optional<String> lastError() {
		return lastError;
	}

	
}
