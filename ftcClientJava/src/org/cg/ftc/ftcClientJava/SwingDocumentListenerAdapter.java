package org.cg.ftc.ftcClientJava;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.cg.common.check.Check;
import org.cg.common.interfaces.OnTextFieldChangedEvent;

public class SwingDocumentListenerAdapter implements DocumentListener {

	private final OnTextFieldChangedEvent adaptee;

	public SwingDocumentListenerAdapter(OnTextFieldChangedEvent adaptee) {
		this.adaptee = adaptee;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		sync(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		sync(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		sync(e);
	}

	private void sync(DocumentEvent e) {
		String value = "";
		Document doc = e.getDocument();
		if (doc.getLength() > 0)
			try {
				value = doc.getText(0, doc.getLength());
			} catch (BadLocationException ex) {
				Check.fail(ex);
			}
		adaptee.notify(value);
	}

}
