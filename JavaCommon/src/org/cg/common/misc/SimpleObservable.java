package org.cg.common.misc;

import java.util.Observable;

import org.cg.common.check.Check;

import org.cg.common.misc.SimpleObservable;

public class SimpleObservable<T> extends Observable {

	private T value;
	
	public static Object getValue(Observable o)
	{
		Check.isTrue(o instanceof SimpleObservable);
		return ((SimpleObservable<?>) o).getValue();
	}
	
	public SimpleObservable(T value){
		this.value = value;
	}
	
	public void setValue(T value)
	{
		this.value = value;
		notifyOnChange();
	}
	
	public T getValue()
	{
		return value;
	}
	
	public void notifyOnChange()
	{
		setChanged();
		notifyObservers();
		clearChanged();
	}
	
}
