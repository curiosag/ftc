package org.cg.common.structures;

import org.cg.common.structures.IntTuple;

public class IntTuple {

	public final int i1;
	public final int i2;

	public static IntTuple instance(int i1, int i2)
	{
		return new IntTuple(i1, i2);
	}
	
	protected IntTuple(int i1, int i2) {
		this.i1 = i1;
		this.i2 = i2;
	}

}