package org.cg.common.util;

import com.google.common.base.Optional;

public class OptionalUtil {

	public static <T> Optional<T> of(T value){
			return Optional.fromNullable(value);
	} 

}
