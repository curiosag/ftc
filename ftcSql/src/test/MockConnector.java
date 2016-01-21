package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cg.common.check.Check;
import org.cg.common.http.HttpStatus;
import org.cg.ftc.shared.interfaces.Connector;
import org.cg.ftc.shared.structures.ColumnInfo;
import org.cg.ftc.shared.structures.ConnectionStatus;
import org.cg.ftc.shared.structures.QueryResult;
import org.cg.ftc.shared.structures.TableInfo;

import static org.junit.Assert.*;

public class MockConnector implements Connector {

	private Map<String, String> tableNameToIdMap;
	private List<TableInfo> tableInfo = new ArrayList<TableInfo>();

	private static MockConnector instance = null;

	public static MockConnector instance() {
		if (instance == null)
			instance = new MockConnector();

		return instance;
	}

	public MockConnector() {
		tableNameToIdMap = new HashMap<String, String>();

		for (Integer i = 0; i < 10; i++) {
			String name = "table" + i.toString();
			String id = "ID" + i.toString();
			tableNameToIdMap.put(name, id);
			TableInfo t = new TableInfo(name, id, "table: " + name + " id: " + id, getCols(i));
			tableInfo.add(t);
		}

	}

	private static List<ColumnInfo> getCols(Integer i) {
		List<ColumnInfo> result = new ArrayList<ColumnInfo>();
		for (int j = 0; j < i + 1; j++) {
			result.add(new ColumnInfo("C" + Integer.toString(j), "", ""));
		}
		return result;
	}

	@Override
	public List<TableInfo> getTableInfo() {
		return tableInfo;
	}

	@Override
	public String executeSql(String query) throws IOException {
		fail("no call expected");
		return null;
	}

	@Override
	public String execSql(String query) {
		fail("no call expected");
		return null;
	}

	@Override
	public void deleteTable(String tableId) throws IOException {
		fail("no call expected");
	}

	@Override
	public String renameTable(String tableId, String newName) {
		fail("no call expected");
		return null;
	}

	@Override
	public QueryResult fetch(String query) {
		return new QueryResult(HttpStatus.SC_FORBIDDEN, null, null);
	}

	@Override
	public ConnectionStatus reset(Dictionary<String, String> connectionInfo) {
		return new ConnectionStatus(HttpStatus.SC_OK);
	}

	@Override
	public void clearStoredLoginData() {
	}

}
