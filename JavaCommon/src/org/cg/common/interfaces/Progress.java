package org.cg.common.interfaces;

public interface Progress {
	void init(int max);
	void announce (int progress);
}
