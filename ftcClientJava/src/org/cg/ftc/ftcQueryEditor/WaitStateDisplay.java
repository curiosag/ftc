package org.cg.ftc.ftcQueryEditor;

import java.awt.Cursor;
import java.util.Timer;
import java.util.TimerTask;

import org.cg.ftc.shared.interfaces.Event;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class WaitStateDisplay {

	private final RSyntaxTextArea comp;
	private final Timer parseWaitCursorTimer = new Timer();
	private Thread.State longOpExecutionState = Thread.State.TERMINATED;
	private final Cursor backupCursor;
	private final Object lock = new Object();
	private final int delay = 100;

	public WaitStateDisplay(RSyntaxTextArea comp)
	{
		this.comp = comp;
		backupCursor = comp.getCursor();
	}
	
	public void enterWaitState() {
		synchronized (lock) {
			if (longOpExecutionState != Thread.State.RUNNABLE) {
				longOpExecutionState = Thread.State.RUNNABLE;
				scheduleDelayedWaitCursorDisplay();
			}
		}
	}

	private void scheduleDelayedWaitCursorDisplay() {
		parseWaitCursorTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				synchronized (lock) {
					if (longOpExecutionState == Thread.State.RUNNABLE)
						comp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				}
			}
		}, delay);
	}

	public synchronized void exitWaitState() {
		synchronized (lock) {
			if (longOpExecutionState == Thread.State.RUNNABLE && backupCursor != null) {
				comp.setCursor(backupCursor);
				longOpExecutionState = Thread.State.TERMINATED;
			}
		}
	}

}
