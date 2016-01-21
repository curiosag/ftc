package org.cg.common.io;

import org.cg.common.io.FileUtil;
import org.cg.common.io.StringStorage;

public class FileStringStorage implements StringStorage {

	private final String path;
	
	public FileStringStorage(String path) {
		this.path = path;
	}
	
	@Override
	public String read() {
		return FileUtil.readFromFile(path);
	}

	@Override
	public void write(String s) {
		FileUtil.writeToFile(s, path);
	}

}
