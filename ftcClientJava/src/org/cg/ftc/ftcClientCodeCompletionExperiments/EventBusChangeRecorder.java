package org.cg.ftc.ftcClientCodeCompletionExperiments;

import javax.swing.event.ChangeEvent;

import com.google.common.eventbus.Subscribe;

class EventBusChangeRecorder {
	@Subscribe
	public void recordCustomerChange(ChangeEvent e) {
		System.out.println("change recorded: " +  e.getSource().toString());
	}
}
