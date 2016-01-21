package org.cg.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.cg.common.check.Check;
import org.cg.common.structures.Tuple;

public class CollectionUtil {

	public static <T> T last(List<T> l) {
		if (l.size() == 0)
			return null;
		else
			return l.get(l.size() - 1);
	}

	public static <T> List<T> toList(Collection<T> c) {
		return Collections.list(Collections.enumeration(c));
	}

	public static <T extends Comparable<? super T>> List<T> sort(List<T> c) {
		Collections.sort(c);
		return c;
	}

	public static <T> List<T> toList(T[] c) {
		List<T> result = new ArrayList<T>();
		for (T t : c)
			result.add(t);

		return result;
	}

	public static <T> List<T> toList(Enumeration<T> c) {
		return Collections.list(c);
	}

	public static <T> List<T> toList(Iterator<T> c) {
		Check.notNull(c);

		List<T> result = new ArrayList<T>();

		while (c.hasNext())
			result.add(c.next());

		return result;
	};

	/**
	 * 
	 * @param partitionAt
	 * @return Tuple<List<T>> (e1, e2)
	 * 	e1 contains all elements with index < partitionAt
	 *  e2 all with index >= partitionAt
	 *  
	 *  if partitionAt < 0 || partitionAt >= list.length then both e1, e2 are empty lists
	 *  
	 */
	
	public static <T> Tuple<List<T>> partition(int partitionAt, List<T> list) {
		Check.notNull(list);
		if (partitionAt >= 0 && partitionAt < list.size())
			return new Tuple<List<T>>(list.subList(0, partitionAt), list.subList(partitionAt, list.size()));
		else {
			List<T> emptyList = new LinkedList<T>();
			return new Tuple<List<T>>(emptyList, emptyList);
		}
		
	};

}