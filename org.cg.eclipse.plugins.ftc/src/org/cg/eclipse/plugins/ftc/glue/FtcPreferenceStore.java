package org.cg.eclipse.plugins.ftc.glue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.cg.eclipse.plugins.ftc.preference.StyleAspect;
import org.cg.ftc.shared.structures.ClientSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * 
 * a hybrid monster
 *
 */
public class FtcPreferenceStore extends Observable implements IPreferenceStore {

	public final static String KEY_CLIENT_ID = "org.cg.eclipse.plugins.ftc.client_id";
	public final static String KEY_CLIENT_SECRET = "org.cg.eclipse.plugins.ftc.client_secret";
	public final static String KEY_CLIENT_QUERYLIMIT = "org.cg.eclipse.plugins.ftc.querylimit";
	public final static String KEY_CLIENT_AUTHTIMEOUT = "org.cg.eclipse.plugins.ftc.authtimeout";
	public final static String KEY_SHOW_CARETPOS = "org.cg.eclipse.plugins.ftc.shopCursorpos";
	public final static String KEY_OFFLINE = "org.cg.eclipse.plugins.ftc.offline";
	public final static String KEY_LAST_EXPORT_PATH = "org.cg.eclipse.plugins.ftc.lastExportPath";
	public static final String KEY_CSV_DELIMITER = "org.cg.eclipse.plugins.ftc.csvDelimiter";
	public static final String KEY_CSV_QUOTECHAR = "org.cg.eclipse.plugins.ftc.csvQuoteChar";
	
	private final ClientSettings clientSettings;

	private final Map<String, Object> defaults = new HashMap<String, Object>();
	private final List<IPropertyChangeListener> propertyChangeListeners = new LinkedList<IPropertyChangeListener>();

	private static final String prefixStylePreference = "styleKey";
	

	public static boolean isStyleKey(String preferenceKey) {
		return preferenceKey != null && preferenceKey.startsWith(prefixStylePreference);
	}

	public static String getStyleKey(StyleAspect aspect, String tokenName) {
		return prefixStylePreference + ";" + aspect.name() + ";" + tokenName;
	}

	public FtcPreferenceStore(ClientSettings clientSettings) {
		this.clientSettings = clientSettings;
	}

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		propertyChangeListeners.add(listener);
	}

	@Override
	public boolean contains(String name) {
		throw new NotImplementedException();
	}

	@Override
	public void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
		for (IPropertyChangeListener l : propertyChangeListeners)
			l.propertyChange(new PropertyChangeEvent(this, name, oldValue, newValue));
	}

	@Override
	public boolean getBoolean(String name) {
		return clientSettings.getPreferences().getBoolean(name, getDefaultBoolean(name));
	}

	@Override
	public boolean getDefaultBoolean(String name) {
		Object o = defaults.get(name);
		if (o == null)
			return false;
		else
			return Unbox.asBoolean(o);
	}

	@Override
	public double getDefaultDouble(String name) {
		throw new NotImplementedException();
	}

	@Override
	public float getDefaultFloat(String name) {
		throw new NotImplementedException();
	}

	@Override
	public int getDefaultInt(String name) {
		Object o = defaults.get(name);
		if (o == null)
			return 0;
		else
			return Unbox.asInt(o);
	}

	@Override
	public long getDefaultLong(String name) {
		throw new NotImplementedException();
	}

	@Override
	public String getDefaultString(String name) {
		Object o = defaults.get(name);
		if (o == null)
			return null;
		else
			return Unbox.asString(o);
	}

	@Override
	public double getDouble(String name) {
		throw new NotImplementedException();
	}

	@Override
	public float getFloat(String name) {
		throw new NotImplementedException();
	}

	@Override
	public int getInt(String name) {
		switch (name) {
		case KEY_CLIENT_AUTHTIMEOUT:
			return clientSettings.authTimeout;

		case KEY_CLIENT_QUERYLIMIT:
			return clientSettings.defaultQueryLimit;

		default: {
			return clientSettings.getPreferences().getInt(name, getDefaultInt(name));
		}

		}
	}

	@Override
	public long getLong(String name) {
		throw new NotImplementedException();
	}

	@Override
	public String getString(String name) {
		switch (name) {
		case KEY_CLIENT_ID:
			return clientSettings.clientId;

		case KEY_CLIENT_SECRET:
			return clientSettings.clientSecret;

		default: 
			return clientSettings.getPreferences().get(name, getDefaultString(name));
		}
	}

	@Override
	public boolean isDefault(String name) {
		throw new NotImplementedException();
	}

	@Override
	public boolean needsSaving() {
		return true;
	}

	@Override
	public void putValue(String name, String value) {
		clientSettings.getPreferences().put(name, value);
	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		propertyChangeListeners.remove(listener);
	}

	@Override
	public void setDefault(String name, double value) {
		throw new NotImplementedException();
	}

	@Override
	public void setDefault(String name, float value) {
		throw new NotImplementedException();
	}

	@Override
	public void setDefault(String name, int value) {
		defaults.put(name, Integer.valueOf(value));
	}

	@Override
	public void setDefault(String name, long value) {
		throw new NotImplementedException();
	}

	@Override
	public void setDefault(String name, String value) {
		defaults.put(name, value);
	}

	@Override
	public void setDefault(String name, boolean value) {
		defaults.put(name, Boolean.valueOf(value));
	}

	@Override
	public void setToDefault(String name) {
		throw new NotImplementedException();
	}

	@Override
	public void setValue(String name, double value) {
		throw new NotImplementedException();
	}

	@Override
	public void setValue(String name, float value) {
		throw new NotImplementedException();
	}

	@Override
	public void setValue(String name, int value) {
		int old = getInt(name);
		switch (name) {
		case KEY_CLIENT_AUTHTIMEOUT:
			clientSettings.authTimeout = value;
			break;

		case KEY_CLIENT_QUERYLIMIT:
			clientSettings.defaultQueryLimit = value;
			break;

		default:
			clientSettings.getPreferences().putInt(name, value);
		}
		clientSettings.write();
		firePropertyChangeEvent(name, Integer.valueOf(old), Integer.valueOf(value));
	}

	@Override
	public void setValue(String name, long value) {
		throw new NotImplementedException();
	}

	@Override
	public void setValue(String name, String value) {
		String old = getString(name);
		switch (name) {
		case KEY_CLIENT_ID:
			clientSettings.clientId = value;
			break;

		case KEY_CLIENT_SECRET:
			clientSettings.clientSecret = value;
			break;

		default:
			clientSettings.getPreferences().put(name, value);
		}
		clientSettings.write();
		firePropertyChangeEvent(name, old, value);
	}

	@Override
	public void setValue(String name, boolean value) {
		boolean old = getBoolean(name);
		clientSettings.getPreferences().putBoolean(name, value);
		firePropertyChangeEvent(name, Boolean.valueOf(old), Boolean.valueOf(value));
	}

}
