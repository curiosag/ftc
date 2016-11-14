package org.cg.eclipse.plugins.ftc.glue;

import org.cg.common.interfaces.Progress;

public interface CancellableProgress extends Progress {
	public void cancel();
}
