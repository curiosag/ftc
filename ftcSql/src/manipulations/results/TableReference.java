package manipulations.results;

import java.util.List;

import com.google.common.base.Optional;

public class TableReference {

	public final String tableName;
	public final Optional<String> tableAlias;
	public final String tableId;
	public final List<String> columnNames;
	
	public TableReference(String tableName, Optional<String> tableAlias, String tableId, List<String> columnNames) {
		this.tableName = tableName;
		this.tableAlias = tableAlias;
		this.tableId = tableId;
		this.columnNames = columnNames;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s - %s - %s\n", tableName, tableAlias, tableId));
		for (String s : columnNames) 
			sb.append("   " + s + "\n");
		return sb.toString();
	}
}
