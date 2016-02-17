package org.cg.common.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Request {

	private final String url, params;

	private final Map<String, String> props = new HashMap<String, String>(); 
	
	public Request(String url, String params) {
		this.url = url;
		this.params = params;
	}

	public HttpResult post()
	{
		try {
			return internalPost();
		} catch (Exception e) {
			return new HttpResult(HttpStatus.SC_METHOD_FAILURE, e.getClass().getSimpleName() + " " + e.getMessage());
		}
	}
	
	public void setRequestProperty(String key, String value)
	{
		props.put(key, value);
	}
	
	private HttpResult internalPost() throws Exception {
		String USER_AGENT = "Mozilla/5.0";
		URL obj = new URL(url);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		for (Entry<String, String> e : props.entrySet()) 
			con.setRequestProperty(e.getKey(), e.getValue());
		
		String urlParameters = params;
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());

		try {
			wr.writeBytes(urlParameters);
			wr.flush();
		} finally {
			wr.close();
		}

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		StringBuffer response = new StringBuffer();
		String inputLine;
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		try {
			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);
		} finally {
			in.close();
		}

		return new HttpResult(HttpStatus.decode(responseCode).or(HttpStatus.SC_METHOD_FAILURE), response.toString());
	}

}
