package org.cg.common.util;

import java.net.URISyntaxException;

public class AppUtil {

	public static String getClassWorkingDirectory(Class<?> c) {

		try {
			return c.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException e1) {
			return "";
		}
	}
}
