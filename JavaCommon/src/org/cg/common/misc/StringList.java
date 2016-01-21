package org.cg.common.misc;

import java.util.ArrayList;

import org.cg.common.check.Check;
import org.cg.common.io.StringStorage;
import org.cg.common.util.StringUtil;

public class StringList extends ArrayList<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String itemSeparator = " öüö\n";

	private final StringStorage storage;

	public StringList() {
		storage = null;
	}

	public StringList(StringStorage storage) {
		Check.notNull(storage);
		this.storage = storage;

		String val = storage.read();
		
		if (!StringUtil.emptyOrNull(val)) {
			String[] splits = val.split(itemSeparator);
			for (String s : splits)
				add(s);
		}
	}

	public void writeToStorage() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < size(); i++) {
			String sep = "";
			if (i < size() - 1)
				sep = itemSeparator;
			sb.append(get(i) + sep);
		}

		storage.write(sb.toString());
	}

}
