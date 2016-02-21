package org.cg.ftc.shared.uglySmallThings;

import javax.swing.SwingWorker;

public abstract class SwingWorkerExt<T, V> extends SwingWorker<T, V> {

	public void saveCancel(boolean mayInterruptIfRunning) {
        try {
        	cancel(mayInterruptIfRunning);	
		} catch (Exception e) {
		}
        
    }
	
}
