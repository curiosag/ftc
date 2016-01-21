package main.java.fusiontables;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.prefs.Preferences;

import com.google.api.client.util.IOUtils;
import com.google.api.client.util.Maps;
import com.google.api.client.util.store.DataStoreFactory;

public class PreferencesDataStore<V extends Serializable> extends CustomDataStore<V> {

	private final Preferences prefs;

	protected PreferencesDataStore(DataStoreFactory dataStoreFactory, String id,
			@SuppressWarnings("rawtypes") Class carrier) {
		super(dataStoreFactory, id);

		prefs = Preferences.userNodeForPackage(carrier);

		ByteArrayInputStream s = new ByteArrayInputStream(prefs.getByteArray(getId(), new byte[0]));

		if (s.available() <= 0)
			keyValueMap = Maps.newHashMap();
		else
			try {
				keyValueMap = IOUtils.deserialize(s);
				try {
					save();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
	}

	@Override
	void save() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		IOUtils.serialize(keyValueMap, buffer);
		prefs.putByteArray(getId(), buffer.toByteArray());
	}

	@Override
	public PreferencesDataStoreFactory getDataStoreFactory() {
		return (PreferencesDataStoreFactory) super.getDataStoreFactory();
	}
	
	public void Clear()
	{
		prefs.remove(getId());
	}

}
