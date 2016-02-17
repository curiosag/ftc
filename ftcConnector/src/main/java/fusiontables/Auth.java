package main.java.fusiontables;

import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Collections;
import java.util.Enumeration;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.fusiontables.FusiontablesScopes;
import com.google.common.base.Optional;

public class Auth {

	private final static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	public static Credential authorize(Optional<AuthInfo> authInfo, Class<?> preferencesNode) throws Exception {
		return authorize(authInfo.get().clientId, authInfo.get().clientSecret, preferencesNode);
	}

	public static Credential authorize(String clientId, String clientSecret, Class<?> preferencesNode) throws Exception {
		String authInfoJSon = "{\"installed\":{\"client_id\":\"%s\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://accounts.google.com/o/oauth2/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"client_secret\":\"%s\",\"redirect_uris\":[\"urn:ietf:wg:oauth:2.0:oob\",\"http://localhost\"]}}";

		Reader authStream = new StringReader(String.format(authInfoJSon, clientId, clientSecret));

		Credential result = authorize(authStream, preferencesNode);
		result.refreshToken();
		return result;
	}

	public static Credential authorize(Reader r, Class<?> preferencesNode) throws Exception {

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, r);

		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret().startsWith("Enter "))
			throw new RuntimeException(
					"Enter Client ID and Secret from https://code.google.com/apis/console/?api=fusiontables "
							+ "into fusiontables-cmdline-sample/src/main/resources/client_secrets.json");

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(getHttpTransport(), JSON_FACTORY,
				clientSecrets, Collections.singleton(FusiontablesScopes.FUSIONTABLES))
						.setDataStoreFactory(getPreferencesStore(preferencesNode)).build();

		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}

	static PreferencesDataStoreFactory getPreferencesStore(Class<?> preferencesNode) {
		return new PreferencesDataStoreFactory(preferencesNode);
	}

	static HttpTransport getHttpTransport() {
		try {
			return GoogleNetHttpTransport.newTrustedTransport();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// https://www.googleapis.com/auth/drive
	private static final String FUSION_TABLE_OAUTH2_SCOPE = "https://www.googleapis.com/auth/fusiontables";

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

			System.out.println(String.format("Algo %s Format %s", key.getAlgorithm(), key.getFormat()));
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

}
