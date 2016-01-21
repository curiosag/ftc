package org.cg.common.util;

import java.util.List;

import org.cg.common.check.Check;

public class Op {
	
	/**
	 * 
	 * @param lo
	 * @param val
	 * @param hi
	 * @return lo <= val && val <= hi
	 */
	public static boolean between(int lo, int val, int hi){
		return lo <= val && val <= hi;
	}

	private static <T> boolean equals(T v1, T v2) {
		if (v1 instanceof String)
			return v1.equals(v2);
		else
			return v1 == v2;
	}

	@SafeVarargs
	public static <T> boolean in(T comparand, T... comparators) {
		Check.notNull(comparand);
		Check.notNull(comparators);

		for (int i = 0; i < comparators.length; i++) 
			if (equals(comparand, comparators[i]))
				return true;
		
		return false;
	};
	
	@SafeVarargs
	public static boolean inCaseInsensitive(String comparand, String ... comparators) {
		Check.notNull(comparand);
		Check.notNull(comparators);

		for (int i = 0; i < comparators.length; i++) 
			comparators[i] = comparators[i].toLowerCase();
		
		comparand = comparand.toLowerCase();
		
		return in(comparand, comparators);
	};

	@SafeVarargs
	public static <T> boolean eq(List<T> comparand, T... comparators) {
		Check.notNull(comparand);
		Check.notNull(comparators);

		if (comparand.size() != comparators.length)
			return false;
		for (T t : comparators)
			if (comparand.indexOf(t) < 0)
				return false;

		return true;
	};

}
