package org.cg.common.threading;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

import org.cg.common.check.*;
import org.cg.common.core.*;
import org.cg.common.threading.Parallel.TaskHandler;

import com.google.common.base.Optional;

public class TaskHandlerExt<V> {

	private TaskHandler<V> handler;

	private TaskHandlerExt(TaskHandler<V> handler) {
		this.handler = handler;
	}

	public static <V> TaskHandlerExt<V> from(TaskHandler<V> handler) {
		Check.notNull(handler);
		return new TaskHandlerExt<V>(handler);
	}

	public Optional<Collection<V>> values() {
		try {
			return Optional.of(handler.values());
		} catch (InterruptedException | ExecutionException e) {
			SystemLogger.instance().Info(e.getMessage() + "\n" + e.getStackTrace().toString());
			return Optional.absent();
		}
	}

	public void awaitCompletion() {
		values();
	}

}
