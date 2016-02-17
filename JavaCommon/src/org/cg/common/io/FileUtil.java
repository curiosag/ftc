package org.cg.common.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class FileUtil {

	public static String getPathOnly(File f) {
		if (f.isDirectory())
			return f.getPath();

		else {
			String fullPath = f.getPath();
			return fullPath.substring(0, fullPath.lastIndexOf(File.separator));
		}
	}
	
	public static String pwd() {
		return System.getProperty("user.dir");
	}

	public static boolean writeToFile(String s, String fileName) {
		try {
			try (PrintWriter w = new PrintWriter(fileName)) {
				w.write(s);
				w.close();
			}
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}

	private static void throwRuntimeException(String msg) {
		throw new RuntimeException(msg);
	}

	/**
	 * 
	 * @param path
	 * @return
	 * 
	 * 		Since it is used in GWT context Java.nio can not be used and
	 *         guava comes in the wrong flavor
	 * 
	 */
	public static String readFromFile(String path) {
		File file = new File(path);

		if (!file.exists())
			return "";

		if (file.isDirectory())
			throwRuntimeException("file expected, this is a directory: " + path);

		try {
			FileInputStream fin = new FileInputStream(file);
			return readFromStream(fin);
		} catch (IOException e) {
			throwRuntimeException(e.getMessage());
		}

		return "";
	}

	public static String readFromStream(InputStream input) {
		StringBuilder sb = new StringBuilder();

		BufferedInputStream bin = null;
		try {
			bin = new BufferedInputStream(input);
			byte[] contents = new byte[1024];

			int bytesRead = 0;
			String strFileContents;

			while ((bytesRead = bin.read(contents)) != -1) {

				strFileContents = new String(contents, 0, bytesRead);
				sb.append(strFileContents);
			}

		} catch (IOException e) {
			throwRuntimeException(e.getMessage());
		} finally {
			try {
				if (bin != null)
					bin.close();
			} catch (IOException ioe) {
			}
		}
		return sb.toString();

	}

	public static byte[] readToByteArray(File file) {
		byte[] bFile = new byte[(int) file.length()];
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return bFile;
	}

}