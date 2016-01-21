package main.java.fusiontables;

import java.io.IOException;
import java.io.Serializable;

import com.google.api.client.util.store.AbstractDataStoreFactory;
import com.google.api.client.util.store.DataStore;

public class PreferencesDataStoreFactory extends AbstractDataStoreFactory {

	private final Class<?> carrierNode;
	
	public PreferencesDataStoreFactory(Class<?> carrierNode)
	{
		this.carrierNode = carrierNode;
	}
	
	@Override
	protected <V extends Serializable> DataStore<V> createDataStore(String id) throws IOException {
		return new PreferencesDataStore<V>(this, id, carrierNode);
	}

}
