package org.cg.common.interfaces;

public interface Continuation<T> {
	void invoke(T value);
}
