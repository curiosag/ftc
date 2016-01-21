package org.cg.ftc.shared.interfaces;

import java.util.List;
import java.util.Observable;

public class SyntaxElements extends Observable {

	private List<SyntaxElement> elements;
	
	public SyntaxElements() {
	}

	private void notifyOnChange()
	{
		setChanged();
		notifyObservers();
		clearChanged();
	}
	
	public List<SyntaxElement> get() {
		return elements;
	}

	public void set(List<SyntaxElement> elements) {
		this.elements = elements;
		notifyOnChange();
	}

}
