package org.cg.common.threading;

import org.cg.common.interfaces.Progress;

public class SynchronizedProgressDecorator implements Progress {

	private final Progress inner;
	
	public SynchronizedProgressDecorator(Progress inner) {
		this.inner = inner;
	}
	
	@Override
	public synchronized void init(int max) {
		inner.init(max);
	}

	@Override
	public synchronized void announce(int progress) {
		inner.announce(progress);
	}

}
