package main.java.fusiontables.rest;

import java.io.ByteArrayInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Collections;
import java.util.Enumeration;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class Auth {

	// https://www.googleapis.com/auth/drive
	private static final String FUSION_TABLE_OAUTH2_SCOPE = "https://www.googleapis.com/auth/fusiontables";
	private final static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	
	public static Credential authorize(String serviceAccountId, String serviceAccountUser, byte[] p12KeyBytes) {
		GoogleCredential credential = null;

		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			ks.load(new ByteArrayInputStream(p12KeyBytes), "notasecret".toCharArray());

			Key key = null;
			Enumeration<String> aliasEnum = ks.aliases();
			if (aliasEnum.hasMoreElements()) {
				String keyName = (String) aliasEnum.nextElement();
				key = ks.getKey(keyName, "notasecret".toCharArray());
			}

			PrivateKey serviceAccountPrivateKey = (PrivateKey) key;

			credential = new GoogleCredential.Builder().setTransport(getHttpTransport()).setJsonFactory(JSON_FACTORY)
					.setServiceAccountId(serviceAccountId)
					.setServiceAccountUser(serviceAccountUser)
					.setServiceAccountScopes(Collections.singleton(FUSION_TABLE_OAUTH2_SCOPE))
					.setServiceAccountPrivateKey(serviceAccountPrivateKey).build();
			credential.refreshToken();

		} catch (Exception e) {
			System.out.println(e.getClass().getSimpleName() + " " + e.getMessage());
		}

		return credential;
	}
	
	static HttpTransport getHttpTransport() {
		try {
			return GoogleNetHttpTransport.newTrustedTransport();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
