package org.cg.common.structures;

import java.util.List;

import org.cg.common.check.Check;

public class StackLight<T> {
	
	private final List<T> inner;
	private int current = 0;
	
	public StackLight(List<T> items){
		Check.notNull(items);
		inner = items;
	};
	
	public T peek(){
		Check.isFalse(empty());
		return inner.get(current);
	};
	
	public T pop(){
		T result = peek();
		current ++;
		return result;
	};
	
	public boolean empty(){
		return current >= inner.size();
	};
	
}
