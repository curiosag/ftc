package org.cg.ftc.ftcClientJava;

import org.cg.common.check.Check;
import org.cg.common.interfaces.Progress;

public class DelegatingProgress implements Progress {

	Progress delegate;
	
	private Progress getDelegate() {
		Check.notNull(delegate);
		return delegate;
	} 
	
	public void setDelegate(Progress delegate)
	{
		Check.notNull(delegate);
		this.delegate = delegate;
	}
	
	@Override
	public void init(int max) {
		getDelegate().init(max);
	}

	@Override
	public void announce(int progress) {
		getDelegate().announce(progress);
	}

}
