package org.cg.eclipse.plugins.ftc.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import org.cg.eclipse.plugins.ftc.glue.FtcPluginClient;
import org.cg.eclipse.plugins.ftc.glue.FtcPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = FtcPluginClient.getDefault().getPreferenceStore();
		store.setDefault(FtcPreferenceStore.KEY_CSV_DELIMITER, ";");
		store.setDefault(FtcPreferenceStore.KEY_CSV_QUOTECHAR, "\"");
		store.setDefault(FtcPreferenceStore.KEY_CLIENT_AUTHTIMEOUT, 30);
		store.setDefault(FtcPreferenceStore.KEY_CLIENT_QUERYLIMIT, 300);
	}

}
