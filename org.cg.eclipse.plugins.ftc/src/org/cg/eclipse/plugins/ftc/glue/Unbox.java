package org.cg.eclipse.plugins.ftc.glue;

import org.cg.common.check.Check;

public class Unbox {

	public static boolean asBoolean(Object o){
		Check.isTrue(o instanceof Boolean);
		return ((Boolean) o).booleanValue();
	}
	
	public static String asString(Object o){
		Check.isTrue(o instanceof String);
		return (String)o;
	}
	
	public static int asInt(Object o){
		Check.isTrue(o instanceof Integer);
		return ((Integer) o).intValue();
	}
	
}
