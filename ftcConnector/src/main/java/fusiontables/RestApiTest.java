package main.java.fusiontables;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.junit.Test;

public class RestApiTest {

	private static String apiUrl = "https://www.googleapis.com/fusiontables/v2/query";

	@Test
	public void testOAuth2() {

		// https://www.googleapis.com/fusiontables/v2/query?sql=select+*+from+15UBo6tWiMpYP7X0K_iIsqJJYGpzURZwigmruYR80&key=AIzaSyClqG2GMmAYv_IlpvbovGo920-zbtKgGYY

		String clientId = "1002359378366-ipnetharogqs3pmhf9q35ov4m14l6014.apps.googleusercontent.com";
		String clientKey = "wJWwr-FbTNtyCLhHvXE5mCo6";

		RestApi api = null;
		try {
			api = new RestApi(Auth.authorize(clientId, clientKey, this.getClass()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		checkReturnedData(null, api);
		checkDelete(null, api);
	}

	@Test
	public void testServiceKey() {
		// neue service keys funktionieren nicht sofort??
		// oder nur in der Nacht nicht, Zeitzonenscheiss??
		
		// base64-ed content of sniffMore-aa4e3e5dc588.p12
		String p12key = "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\nFNcRWn+a6MPOVKp6sxNHaAgMhfHQAgIEAA==";
		Charset utf = Charset.forName("UTF-8");

		// Domain-weite Google Apps-Delegation nicht nötig
		String serviceAccountId = "ftc-294@futconsole.iam.gserviceaccount.com"; // alone
																				// sufficient
																				// for
																				// read
																				// only
																				// access
		String serviceAccountUser = "curiosa.globunznik@gmail.com"; // needed
																	// for write
																	// access

		//byte[] keyBytes = FileUtil.readToByteArray(new File("/home/sp/Downloads/sniffMore-e00d95c8dc74.p12"));
	    byte[] keyBytes = Base64.decodeBase64(p12key.getBytes(utf));

		RestApi api = new RestApi(Auth.authorize(serviceAccountId, serviceAccountUser, keyBytes));

		checkReturnedData(null, api);
	    checkDelete(null, api);

	}

	private void checkDelete(Object object, RestApi api) {
		try {
			System.out
					.println(api.executeStmt("POST", apiUrl, "delete from 1LU-S2JpGitGmzU6x-nepnFDPZHy5L_X8jii-ZwZ2"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void testApplicationKey() {
		List<NameValuePair> key = new LinkedList<NameValuePair>();
		// Browserschlüssel 1 von curiosa.globunznik@gmail.com
		key.add(new BaseNameValuePair("key", "AIzaSyClqG2GMmAYv_IlpvbovGo920-zbtKgGYY"));

		RestApi api = new RestApi(null);
		checkReturnedData(key, api);

	}

	private void checkReturnedData(List<NameValuePair> key, RestApi api) {
		try {
			String r = executeQuery(null, api);
			assertEquals(true, r.indexOf("fusiontables#sqlresponse") >= 0);
			System.out.println(r);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String executeQuery(List<NameValuePair> key, RestApi api) throws IOException {
		return api.executeStmt("POST", apiUrl, "select * from 15UBo6tWiMpYP7X0K_iIsqJJYGpzURZwigmruYR80", key);
	}

}
