package main.java.fusiontables;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.table.DefaultTableModel;

import org.cg.common.check.Check;
import org.cg.common.core.Logging;
import org.cg.common.http.HttpStatus;
import org.cg.ftc.shared.interfaces.*;
import org.cg.ftc.shared.structures.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.Fusiontables.Query.Sql;
import com.google.api.services.fusiontables.Fusiontables.Table.Delete;
import com.google.api.services.fusiontables.FusiontablesScopes;
import com.google.api.services.fusiontables.model.Column;
import com.google.api.services.fusiontables.model.Sqlresponse;
import com.google.api.services.fusiontables.model.Table;
import com.google.common.base.Optional;

import main.java.fusiontables.deserialize.GftResponseJson;

public class FusionTablesConnector implements Connector {

	private final Map<String, String> tableNamesToIds = new HashMap<String, String>();

	private final String APPLICATION_NAME = "fusion tables console";
	private final String NOT_CONNECTED = "not connected";

	private PreferencesDataStoreFactory dataStoreFactory = null;
	private final Class<?> dataStoreCarrierNode;
	private HttpTransport httpTransport;
	private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private Optional<Fusiontables> fusiontables;

	private final Logging logger;

	public FusionTablesConnector(Logging logger, Optional<AuthInfo> authInfo, Class<?> dataStoreCarrierNode) {
		Check.notNull(authInfo);
		Check.notNull(logger);
		this.logger = logger;
		this.dataStoreCarrierNode = dataStoreCarrierNode;
		try {

			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			reset(authInfo);

		} catch (Exception e) {
			log(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}

	}

	public ConnectionStatus reset(Dictionary<String, String> connectionInfo) {
		return reset(Optional.of(new AuthInfo(connectionInfo.get(ClientSettings.keyClientId),
				connectionInfo.get(ClientSettings.keyClientSecret))));
	}

	public ConnectionStatus reset(Optional<AuthInfo> authInfo) {
		String authInfoJSon = "{\"installed\":{\"client_id\":\"%s\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://accounts.google.com/o/oauth2/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"client_secret\":\"%s\",\"redirect_uris\":[\"urn:ietf:wg:oauth:2.0:oob\",\"http://localhost\"]}}";

		if (dataStoreFactory == null)
			dataStoreFactory = new PreferencesDataStoreFactory(dataStoreCarrierNode);

		if (!authInfo.isPresent() || authInfo.isPresent() && !authInfo.get().credentialsPlausible()) {
			fusiontables = Optional.absent();
			return new ConnectionStatus(HttpStatus.SC_BAD_REQUEST, "incomplete credentials");
		}

		Reader authStream;

		authStream = new StringReader(
				String.format(authInfoJSon, authInfo.get().clientId, authInfo.get().clientSecret));

		Credential credential = null;
		ConnectionStatus result;
		try {
			credential = authorize(authStream);
			result = new ConnectionStatus(HttpStatus.SC_OK);
		} catch (Exception e) {
			logger.Error("Failed to authorize: " + e.getMessage());
			fusiontables = Optional.absent();
			result = new ConnectionStatus(e);
		}

		fusiontables = Optional.of(new Fusiontables.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build());

		return result;
	}

	private void log(String msg) {
		if (logger != null)
			logger.Info(msg);
	}

	private Credential authorize(Reader r) throws Exception {

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, r);

		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
			throw new RuntimeException(
					"Enter Client ID and Secret from https://code.google.com/apis/console/?api=fusiontables "
							+ "into fusiontables-cmdline-sample/src/main/resources/client_secrets.json");
		}

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY,
				clientSecrets, Collections.singleton(FusiontablesScopes.FUSIONTABLES))
						.setDataStoreFactory(dataStoreFactory).build();

		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}

	@Override
	public List<TableInfo> getTableInfo() {
		ArrayList<TableInfo> result = new ArrayList<TableInfo>();

		if (!fusiontables.isPresent())
			return result;

		try {
			for (Table t : fusiontables.get().table().list().execute().getItems())
				result.add(new TableInfo(t.getName(), t.getTableId(), t.getDescription(), getColumns(t)));
		} catch (IOException ex) {
			log("IOException: " + ex.getMessage());
		} catch (NullPointerException ex) {
			// there is no obvious way to determine if this succeeded
			// execute() returns a TableList t where t.isEmpty() == false
			// and getItems() still throws a NullPointerException
			// behaves the same, if the network connection is gone
			// or network is ok, but there are no tables at all
			log("no network connection or there are no tables at all");
		}

		reportDuplicates(result);
		return result;
	}

	private List<ColumnInfo> getColumns(Table t) {
		List<ColumnInfo> columns = new ArrayList<ColumnInfo>();
		for (Column c : t.getColumns())
			columns.add(new ColumnInfo(c.getName(), c.getType(), c.getKind()));
		return columns;
	}

	private void reportDuplicates(ArrayList<TableInfo> result) {
		String fuckedUp = "";
		tableNamesToIds.clear();
		for (TableInfo i : result) {
			if (tableNamesToIds.containsKey(i.name))
				fuckedUp = fuckedUp + "ambiguous table name '" + i.name + "'\r\n";
			tableNamesToIds.put(i.name, i.id);

		}
		if (fuckedUp.length() > 0) {
			log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\r\n"
					+ " ambiguous table names found - one name for more than one table ID."
					+ " Name to ID replacement has a good chance to produce \r\n"
					+ " invalid queries if those tables are involved. \r\n"
					+ " Better to change names, a unique one for each table. \r\n"
					+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\r\n" + fuckedUp);
		}
	}

	@Override
	public String executeSql(String query) throws IOException {

		if (!fusiontables.isPresent())
			return NOT_CONNECTED;

		Sql sql = fusiontables.get().query().sql(query);
		Sqlresponse response = null;

		response = sql.execute();

		getTableInfo();
		return response.toPrettyString();
	}

	@Override
	public String execSql(String query) {
		try {
			String result = executeSql(query);
			getTableInfo();
			return result;
		} catch (IOException e) {
			return e.getMessage();
		}
	}

	@Override
	public void deleteTable(String tableId) throws IOException {
		if (!fusiontables.isPresent()) {
			logger.Info(NOT_CONNECTED);
			return;
		}

		Delete delete = fusiontables.get().table().delete(tableId);
		delete.execute();
		getTableInfo();
	}

	public String createSampleTable() throws IOException {
		if (!fusiontables.isPresent())
			return NOT_CONNECTED;

		View.header("Create Sample Table");

		Table table = new Table();
		table.setName(UUID.randomUUID().toString());
		table.setIsExportable(false);
		table.setDescription("Sample Table");

		table.setColumns(Arrays.asList(new Column().setName("Text").setType("STRING"),
				new Column().setName("Number").setType("NUMBER"), new Column().setName("Location").setType("LOCATION"),
				new Column().setName("Date").setType("DATETIME")));

		Fusiontables.Table.Insert t = fusiontables.get().table().insert(table);
		Table r = t.execute();

		return r.getTableId();
	}

	@Override
	public String renameTable(String tableId, String newName) {
		if (!fusiontables.isPresent())
			return NOT_CONNECTED;

		Table table = new Table();
		table.setTableId(tableId);
		table.setName(newName);
		try {
			fusiontables.get().table().patch(tableId, table).execute();
		} catch (IOException e) {
			return e.getMessage();
		}

		getTableInfo();

		return "renamed " + tableId + " to " + newName;
	}

	@Override
	public QueryResult fetch(String query) {

		try {
			return deserializeGftJson(executeSql(query));
		} catch (Exception e) {
			return createErrorResult(HttpStatus.SC_METHOD_FAILURE, e.getMessage());
		}
	}

	private static HttpStatus getHttpStatus(String json) {
		HttpStatus errorStatus = HttpStatus.SC_NO_CONTENT;

		if (json == null)
			return errorStatus;

		json = json.trim();

		if (json.length() < 3)
			return errorStatus;

		if (json.charAt(0) == '{')
			return HttpStatus.SC_OK;

		int code = strToInt(json.substring(0, 3));
		if (code < 0)
			return errorStatus;

		Optional<HttpStatus> status = HttpStatus.decode(code);
		if (!status.isPresent())
			return errorStatus;

		return status.get();
	}

	private static int strToInt(String s) {
		int result = -1;
		try {
			result = Integer.valueOf(s);
		} catch (NumberFormatException e) {
		}
		return result;
	}

	private static QueryResult createErrorResult(HttpStatus status, String errorMsg) {
		return new QueryResult(status, null, errorMsg);
	}

	public static QueryResult deserializeGftJson(String json) {

		HttpStatus status = getHttpStatus(json);

		if (status != HttpStatus.SC_OK)
			return createErrorResult(status, json);

		ObjectMapper jsonmapper = new ObjectMapper();
		try {
			GftResponseJson data = jsonmapper.readValue(json, GftResponseJson.class);
			return new QueryResult(HttpStatus.SC_OK, new DefaultTableModel(data.rows, data.columns), null);
		} catch (IOException e) {
			return createErrorResult(HttpStatus.SC_METHOD_FAILURE, e.getMessage());
		}
	}

	@Override
	public void clearStoredLoginData() {
		try {
			// that's a hack. As a matter of fact the google machinerey will use
			// "StoredCredential" as key
			dataStoreFactory.createDataStore("StoredCredential").clear();
			dataStoreFactory = null;
		} catch (IOException e) {
			logger.Error(e.getMessage());
		}
	}

}
