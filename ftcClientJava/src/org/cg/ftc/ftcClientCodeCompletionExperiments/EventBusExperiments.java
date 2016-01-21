package org.cg.ftc.ftcClientCodeCompletionExperiments;

import javax.swing.event.ChangeEvent;

import org.junit.Test;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.DeadEvent;

public class EventBusExperiments {

	static EventBus eventBus = new EventBus();
	static DeadEventSubscriber deadEventSubscriber;

	static {
		eventBus.register(DeadEventSubscriber.create());
		eventBus.register(new EventBusSimpleListener());
		eventBus.register(new EventBusChangeRecorder());
	}

	@Test
	public void test() {
		eventBus.post("simply hallo");
		eventBus.post(new ChangeEvent("hallo by change event"));
		eventBus.post(false);
	}

	private static class DeadEventSubscriber {
		@Subscribe
		public void handleDeadEvent(DeadEvent deadEvent) {
			System.out.println("dead event: " + deadEvent.getSource().toString());
		}

		public static DeadEventSubscriber create(){
			return new DeadEventSubscriber(); 
		}
	}

}
