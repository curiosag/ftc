package org.cg.ftc.shared.uglySmallThings;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.SwingWorker;

import org.cg.common.interfaces.Continuation;
import org.cg.common.threading.Function;

public class AsyncWork {

	private final static ExecutorService pool = Executors.newFixedThreadPool(10);
	
	public static <T> Future<T> submit(Callable<T> c){
		return pool.submit(c);
	}
	
	public static <T, U> SwingWorker<T, U> goUnderground(final Function<T> f, final Continuation<T> onDone) {
		return new SwingWorker<T, U>() {

			private T result;

			@Override
			protected T doInBackground() throws Exception {
				result = f.invoke();
				return result;
			}

			@Override
			public void done() {
				if (!isCancelled())
					onDone.invoke(result);
			};

		};
	}

	public static <T> SwingWorker<T, Object> createEmptyWorker() {
		return AsyncWork.goUnderground(new Function<T>() {

			@Override
			public T invoke() {
				return null;
			}
		}, new Continuation<T>() {

			@Override
			public void invoke(T value) {
			}
		});

	}

}
