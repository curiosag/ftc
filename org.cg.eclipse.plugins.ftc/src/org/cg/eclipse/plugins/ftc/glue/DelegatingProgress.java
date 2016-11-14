package org.cg.eclipse.plugins.ftc.glue;

import org.cg.common.interfaces.Progress;

public class DelegatingProgress implements Progress {

	private Progress delegate;
	
	public void setDelegate(Progress p){
		delegate = p;
	}
	
	@Override
	public void init(int max) {
		if (delegate != null)
			delegate.init(max);
	}

	@Override
	public void announce(int progress) {
		if (delegate != null)
			delegate.announce(progress);
	}

}
