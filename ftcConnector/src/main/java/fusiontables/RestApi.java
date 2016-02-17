package main.java.fusiontables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.cg.common.check.Check;
import org.cg.ftc.shared.uglySmallThings.Const;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;

public class RestApi {

	public static final String FUSION_TABLE_QUERY_API = "https://www.googleapis.com/fusiontables/v2/query";
	public static final String FUSION_TABLE_TABLE_API = "https://www.googleapis.com/fusiontables/v2/tables";

	public final static String methodPost = "POST";
	
	private final HttpTransport httpTransport;
	private final Credential credential;
	
	public RestApi(Credential credential) {
		this.credential = credential;
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public String executeStmt(String method, String urlString, String statement) throws IOException
	{
		return executeStmt(method, urlString, statement, null);
	}
	
	public String executeStmt(String method, String urlString, String statement, List<NameValuePair> qparams)
			throws IOException {
		Check.notNull(statement);

		GenericUrl url = setParams(new GenericUrl(urlString), qparams);
		UrlEncodedContent urlEntity = setMediaType(getUrlEncodedSql(statement));

		HttpRequestFactory rf = httpTransport.createRequestFactory(credential);

		HttpResponse response = rf.buildRequest(method, url, urlEntity).execute();
		String result = readGoogleResponse(response);

		if (response.getStatusCode() != HttpServletResponse.SC_OK)
			throw new RuntimeException(result.toString() + statement);

		return result;
	}

	private GenericUrl setParams(GenericUrl url, List<NameValuePair> qparams) {
		if (qparams != null)
			for (NameValuePair param : qparams)
				url.set(param.getName(), param.getValue());
		return url;
	}

	private UrlEncodedContent setMediaType(UrlEncodedContent urlEntity) {
		HttpMediaType t = urlEntity.getMediaType();
		if (t != null)
			t.setCharsetParameter(Charset.forName(Const.UTF8_ENCODE));
		else {
			t = new HttpMediaType("application", "x-www-form-urlencoded");
			t.setCharsetParameter(Charset.forName(Const.UTF8_ENCODE));
			urlEntity.setMediaType(t);
		}

		return urlEntity;
	}

	private UrlEncodedContent getUrlEncodedSql(String statement) {
		Map<String, String> content = new HashMap<String, String>();
		content.put("sql", statement);
		return new UrlEncodedContent(content);
	}

	public static String readGoogleResponse(com.google.api.client.http.HttpResponse resp) throws IOException {
		if (resp != null)
			return readResponse(resp.getContent());
		else
			return "";
	}

	private static String readResponse(InputStream content) {
		if (content == null)
			return "";

		StringBuffer response = new StringBuffer();

		// TODO: this section of code is possibly causing 'WARNING: Going to
		// buffer response body of large or unknown size. Using
		// getResponseBodyAsStream instead is recommended.'
		// The WARNING is most likely only happening when running appengine
		// locally, but we should investigate to make sure
		BufferedReader reader = null;
		InputStreamReader isr = null;

		try {
			reader = new BufferedReader(isr = new InputStreamReader(content, Const.UTF8_ENCODE));
			String responseLine;
			while ((responseLine = reader.readLine()) != null)
				response.append(responseLine);
		} catch (IllegalStateException | IOException e) {
			throw new RuntimeException(e);
		} finally {
			tryClose(reader);
			tryClose(isr);
		}

		return response.toString();
	}

	private static void tryClose(Reader reader) {
		if (reader != null)
			try {
				reader.close();
			} catch (IOException ex) {
			}
	}

	
}
