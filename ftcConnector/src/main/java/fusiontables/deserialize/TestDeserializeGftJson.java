package main.java.fusiontables.deserialize;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import javax.swing.table.TableModel;

import org.cg.common.http.HttpStatus;
import org.cg.common.io.FileUtil;
import org.cg.ftc.shared.structures.QueryResult;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

import main.java.fusiontables.FusionTablesConnector;


public class TestDeserializeGftJson {

	@Test
	public void testDeserializer() {

		// that's in jsonData.txt
		// {
		// "columns" : [ "Date", "willhaben", "bazar", "jobwohnen", "total" ],
		// "kind" : "fusiontables#sqlresponse",
		// "rows" : [ [ "2015-05-20 00:00:00", "0", "1", "0", "1" ], [
		// "2015-05-21 00:00:00", "8", "8", "4", "20" ] ]
		// }

		String json = FileUtil.readFromStream(FusionTablesConnector.class.getResourceAsStream("/jsonData.txt"));
		QueryResult dsResult = FusionTablesConnector.deserializeGftJson(json);
		assertNotNull(dsResult.data);
		assertEquals(HttpStatus.SC_OK, dsResult.status);
		assertFalse(dsResult.message.isPresent());
		assertTrue(dsResult.data.isPresent());
		TableModel data = dsResult.data.get();

		assertEquals(2, data.getRowCount());
		assertEquals(5, data.getColumnCount());
		assertEquals("Date", data.getColumnName(0));
		assertEquals("20", data.getValueAt(1, 4));

	}

	@Test
	public void testDeserializerErrorResult() {

		// that's in badRequestJson.txt
		// 400 Bad Request
		// {
		// "code" : 400,
		// "errors" : [ {
		// "domain" : "fusiontables",
		// "location" : "q",
		// "locationType" : "parameter",
		// "message" : "Invalid query: Column `daatum' does not exist",
		// "reason" : "badQueryCouldNotParse"
		// } ],
		// "message" : "Invalid query: Column `daatum' does not exist"
		// }

		String json = FileUtil.readFromStream(FusionTablesConnector.class.getResourceAsStream("/badRequestJson.txt"));
		QueryResult dsResult = FusionTablesConnector.deserializeGftJson(json);
		assertNotNull(dsResult.data);
		assertEquals(HttpStatus.SC_BAD_REQUEST, dsResult.status);
		assertTrue(dsResult.message.isPresent());
		assertFalse(dsResult.data.isPresent());
	}
	
	@Test
	public void testDeserializeNoContent() {

		QueryResult dsResult = FusionTablesConnector.deserializeGftJson(null);
		checkNoContent(dsResult);
		dsResult = FusionTablesConnector.deserializeGftJson("");
		checkNoContent(dsResult);
		dsResult = FusionTablesConnector.deserializeGftJson("001");
		checkNoContent(dsResult);
		dsResult = FusionTablesConnector.deserializeGftJson("lalalala");
		checkNoContent(dsResult);
	}

	private void checkNoContent(QueryResult dsResult) {
		assertNotNull(dsResult.data);
		assertEquals(HttpStatus.SC_NO_CONTENT, dsResult.status);
		// error info could be present or not
		assertFalse(dsResult.data.isPresent());
	}

	// inner class must be static
	static class BasicJson {
		// that's an alternative way to deserialize
		// @JsonCreator(mode=JsonCreator.Mode.PROPERTIES)
		// public BasicJson(String kind)
		// {
		// this.kind = kind;
		// }

		public List<String> columns; // List, not arrays
		public String kind;

	}

	// @Test
	public void basicTest() {

		QueryResult result = null;
		String json = FileUtil.readFromStream(FusionTablesConnector.class.getResourceAsStream("/simpleJsonData.txt"));
		ObjectMapper jsonmapper = new ObjectMapper();
		try {
			BasicJson data = jsonmapper.readValue(json, BasicJson.class);
			result = new QueryResult(HttpStatus.SC_OK, null, data.toString());
		} catch (IOException e) {
			System.out.println(Throwables.getStackTraceAsString(e));
			result = new QueryResult(HttpStatus.SC_METHOD_FAILURE, null, e.getMessage());
		}

	}
}
