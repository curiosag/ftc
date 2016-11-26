package org.cg.ftc.ftcClientJava;

import java.util.Observable;

import org.cg.common.check.Check;
import org.cg.common.interfaces.OnTextFieldChangedEvent;

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
	 
	public OnTextFieldChangedEvent getListener() {
		
		return new OnTextFieldChangedEvent() {

			@Override
			public void notify(String valueChanged) {
				if (valueChanged == null)
					value = "";
				else
					value = valueChanged;
				
			}			
		};
	}

	
	public static TextModel getTextModel(Observable o) {
		Check.isTrue(o instanceof TextModel);
		return (TextModel) o;
	}
	
}
