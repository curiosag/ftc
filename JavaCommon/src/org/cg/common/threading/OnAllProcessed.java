package org.cg.common.threading;

public interface OnAllProcessed<T> {
	void announce(T results);
}
