package org.cg.common.util;

import java.io.File;

public class FileUtil {

	public static String getPathOnly(File f) {
		if (f.isDirectory())
			return f.getPath();

		else {
			String fullPath = f.getPath();
			return fullPath.substring(0, fullPath.lastIndexOf(f.separator));
		}
	}

}
