package org.cg.ftc.ftcClientJava;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.cg.common.check.Check;
import org.cg.common.interfaces.OnTextFieldChangedEvent;
import org.cg.common.misc.SimpleObservable;

public class Observism {

	public static Observer createObserver(final JTextArea f) {
		return new Observer() {
			@Override
			public void update(final Observable o, Object arg) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						f.setText(TextModel.getTextModel(o).getValue());
					}
				});
			}
		};
	}

	public static Observer createObserver(final JTextField f) {
		return new Observer() {
			@Override
			public void update(final Observable o, Object arg) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						f.setText(TextModel.getTextModel(o).getValue());
					}
				});
			}
		};
	}

	public static Observer createObserver(final JEditorPane f) {
		return new Observer() {
			@Override
			public void update(final Observable o, Object arg) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						f.setText(TextModel.getTextModel(o).getValue());
					}
				});
			}
		};
	}

	public static Observer createAppendingObserver(final JEditorPane f) {
		return new Observer() {
			@Override
			public void update(final Observable o, Object arg) {
				Document doc = f.getDocument();
				String value = decodeTextModelObservable(o);
				try {
					doc.insertString(doc.getLength(), value, null);
					f.setCaretPosition(doc.getLength());
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		};
	}
	
	public static String decodeTextModelObservable(Observable o)
	{
		return TextModel.getTextModel(o).getValueAppended();
	}

	public static TableModel decodeTableModelObservable(Observable o)
	{
		Object value = SimpleObservable.getValue(o);
		Check.isTrue(value instanceof TableModel);
		return (TableModel) value;
	}
	
	
	private static FocusListener createValueChangedListener(final JTextField textField,
			final OnTextFieldChangedEvent delegate) {
		FocusListener result = new FocusListener() {

			OnTextFieldChangedEvent onFocus = delegate;
			String value = textField.getText();

			@Override
			public void focusGained(FocusEvent e) {
				value = textField.getText();
			}

			@Override
			public void focusLost(FocusEvent e) {
				String val = textField.getText();
				if (!value.equals(val))
					onFocus.notify(val);
			}
		};

		return result;
	}

	public static void addValueChangedListener(JTextField f, OnTextFieldChangedEvent delegate) {
		f.addFocusListener(createValueChangedListener(f, delegate));
	}

	public static Observer unObserver = new Observer() {
		@Override
		public void update(Observable o, Object arg) {
		}
	};

}
