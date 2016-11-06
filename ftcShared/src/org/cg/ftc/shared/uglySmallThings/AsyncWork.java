package org.cg.ftc.shared.uglySmallThings;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.cg.common.interfaces.Continuation;
import org.cg.common.interfaces.Progress;
import org.cg.common.threading.Function;
import org.cg.ftc.shared.structures.ConnectionStatus;
import org.cg.common.core.*;

public class AsyncWork {

	private final static Logging log = new SystemLogger();
	private final static ExecutorService pool = Executors.newFixedThreadPool(10);

	public static <T> Future<T> submit(Callable<T> c) {
		return pool.submit(c);
	}

	public static <T, U> SwingWorkerExt<T, U> goUnderground(final Function<T> f, final Continuation<T> onDone,
			final Progress progress) {
		return new SwingWorkerExt<T, U>() {

			@Override
			protected T doInBackground() throws Exception {
				return f.invoke(progress);
			}

			@Override
			public void done() {
				try {
					onDone.invoke(get());
				} catch (InterruptedException ignore) {
				} catch (CancellationException ignore) {
				} catch (java.util.concurrent.ExecutionException e) {
					Throwable cause = e.getCause();
					log.Info(cause != null ? cause.getMessage() : e.getMessage());
				}

			};

		};
	}

	public static SwingWorkerExt<ConnectionStatus, Object> goUnderground(Function<ConnectionStatus> authFunction,
			Continuation<ConnectionStatus> authContinuation) {
		return goUnderground(authFunction, authContinuation, noProgress);
	}
	
	public final static Progress noProgress = new Progress() {
			@Override
			public void announce(int progress) {
			}

			@Override
			public void init(int max) {
			}
		};

	public static <T> SwingWorkerExt<T, Object> createEmptyWorker() {
		return AsyncWork.goUnderground(new Function<T>() {

			@Override
			public T invoke(Progress p) {
				return null;
			}
		}, new Continuation<T>() {

			@Override
			public void invoke(T value) {
			}
		}, noProgress);

	}

}
