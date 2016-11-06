package org.cg.ftc.ftcClientJava;

import java.util.Observable;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.cg.common.check.Check;

public class TextModel extends Observable {

	private String value = "";
	private String valueAppended = "";
	
	public String getValue() {
		return value;
	}
	
	public String getValueAppended() {
		return valueAppended;
	}
	
	private void notifyOnChange()
	{
		setChanged();
		notifyObservers();
		clearChanged();
	}
	
	public void setValue(String value) {
		this.value = value;
		valueAppended = value;
		notifyOnChange();
	}

	public void append(String valueToAppend) {
		value = value + valueToAppend;
		valueAppended = valueToAppend;
		notifyOnChange();
	}
	
	public DocumentListener getListener() {
		return new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				sync(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				sync(e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				sync(e);
			}
			
		};
	}

	private void sync(DocumentEvent e) {
		Document doc = e.getDocument();
		if (doc.getLength() > 0)
			try {
				value = doc.getText(0, doc.getLength());
			} catch (BadLocationException ex) {
				Check.fail(ex);
			}
		else
			value = "";
	}
	
	public static TextModel getTextModel(Observable o) {
		Check.isTrue(o instanceof TextModel);
		return (TextModel) o;
	}
	
}
