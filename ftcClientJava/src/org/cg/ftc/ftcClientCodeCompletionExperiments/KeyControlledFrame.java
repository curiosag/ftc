package org.cg.ftc.ftcClientCodeCompletionExperiments;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import org.cg.common.check.Check;

public class KeyControlledFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private class Dispatcher implements KeyEventDispatcher {

		KeyEventHandler onKeyEvent;
		private Object owner;

		public Dispatcher(KeyEventHandler onKeyEvent, Object owner) {
			Check.notNull(onKeyEvent);
			this.onKeyEvent = onKeyEvent;
			this.owner = owner;
		}

		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			onKeyEvent.onKeyEvent(e, owner);
			return true;
		}
	}

	public KeyControlledFrame(String caption, KeyEventHandler onKeyEvent) {
		super(caption);
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new Dispatcher(onKeyEvent, this));
	}
}
