package org.cg.common.io;

import java.util.prefs.Preferences;

public class PreferencesStringStorage implements StringStorage {

	private final Preferences prefs;
	private final String key;
	
	public PreferencesStringStorage(String key, Class<?> carrierNode)
	{
		this.key = key;
		prefs = Preferences.userNodeForPackage(carrierNode);
	}
	
	@Override
	public String read() {
		// simply reading/writing strings resulted in malformatted preferences xml file
		byte[] val = prefs.getByteArray(key, null);
		if (val == null)
			return "";
		else
			return new String(val);
	}

	@Override
	public void write(String s) {
		prefs.putByteArray(key, s.getBytes());
	}

}
