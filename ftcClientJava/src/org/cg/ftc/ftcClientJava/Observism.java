package org.cg.ftc.ftcClientJava;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.cg.common.interfaces.OnValueChangedEvent;

public class Observism {

	public static Observer createObserver(final JTextArea f)
	{
		return new Observer() {
			@Override
			public void update(final Observable o, Object arg) {
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run() {
						f.setText(TextModel.fromObservable(o));
					}});
			}
		};
	}

	public static Observer createObserver(final JTextField f)
	{
		return new Observer() {
			@Override
			public void update(final Observable o, Object arg) {
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run() {
						f.setText(TextModel.fromObservable(o));
					}});
			}
		};
	}
	
	public static Observer createObserver(final JEditorPane f)
	{
		return new Observer() {
			@Override
			public void update(final Observable o, Object arg) {
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run() {
						f.setText(TextModel.fromObservable(o));
					}});
			}
		};
	}

	private static FocusListener createValueChangedListener(final JTextField textField, final OnValueChangedEvent delegate)
	{
		FocusListener result = new FocusListener(){
			
			OnValueChangedEvent onFocus = delegate;
			String value = textField.getText();
			
			@Override
			public void focusGained(FocusEvent e) {
				value = textField.getText();
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (!value.equals(textField.getText()))
					onFocus.notify(textField);
			}};
		
		return result;
	}
	
	public static void addValueChangedListener(JTextField f, OnValueChangedEvent delegate)
	{
		f.addFocusListener(createValueChangedListener(f, delegate));
	}

	
}
