package org.cg.common.structures;

import org.cg.common.structures.IntTuple;
import org.cg.common.structures.OrderedIntTuple;

public class OrderedIntTuple extends IntTuple {

	public final boolean swap;
	public final boolean isDefined;

	public static OrderedIntTuple create() {
		return new OrderedIntTuple();
	}
	
	public static OrderedIntTuple create(int lo, int hi) {
		return new OrderedIntTuple(lo, hi);
	}

	private OrderedIntTuple(int lo, int hi) {
		super(lo <= hi ? lo : hi, hi > lo ? hi : lo);
		this.swap = lo > hi;
		this.isDefined = true;
	}

	private OrderedIntTuple() {
		super(0, 0);
		this.swap = false;
		this.isDefined = false;
	}
	
	public int lo(){
		return i1;
	}
	
	public int hi(){
		return i2;
	}
}
