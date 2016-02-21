package org.cg.common.threading;

import org.cg.common.interfaces.Progress;

public interface Function<T> {
	T invoke(Progress progress);
}
