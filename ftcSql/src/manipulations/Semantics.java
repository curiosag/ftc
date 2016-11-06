package manipulations;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.cg.common.util.StringUtil;
import org.cg.ftc.shared.interfaces.SyntaxElement;
import org.cg.ftc.shared.interfaces.SyntaxElementType;

import com.google.common.base.Optional;

import manipulations.results.TableReference;

public class Semantics {

	private final List<TableReference> tableReferences = new ArrayList<TableReference>();
	private final Dictionary<String, Integer> columns = new Hashtable<String, Integer>();
	private final Dictionary<String, Integer> tables = new Hashtable<String, Integer>();

	/**
	 * add references to existing fu tables to check against later
	 * 
	 * @param ref
	 */
	public void addReference(TableReference ref) {
		if (tables.get(ref.tableId) == null && tables.get(ref.tableName) == null) {
			tableReferences.add(ref);
			for (String n : ref.columnNames)
				columns.put(n, 0);
		}

		if (tables.get(ref.tableId) == null)
			tables.put(ref.tableId, tableReferences.size() - 1);

		if (tables.get(ref.tableName) == null)
			tables.put(ref.tableName, tableReferences.size() - 1);
	}

	/**
	 * sets semantic information for column and table tokens, i.e. if the
	 * element referred to exists or not
	 * 
	 * @param stop
	 *            start of the token range to consider
	 * @param start
	 *            stop - " -
	 * 
	 * @param tokens
	 * @param tableList
	 */
	public List<SyntaxElement> setSemanticAttributes(Split s, List<SyntaxElement> tokens,
			List<NameRecognitionTable> tableList) {
		List<NameRecognitionTable> tablesInSplit = filterTables(s, tableList);
		for (int i = 0; i < tokens.size(); i++) {
			SyntaxElement current = tokens.get(i);
			if (current.from >= s.start && current.to <= s.stop)
				current.setSemanticError(hasSemanticError(tokens, i, tablesInSplit));
		}
		return tokens;
	}

	/**
	 * reduce all tables in tableList to those referenced in query
	 * 
	 * @param s
	 * @param tableList
	 * @return
	 */
	private List<NameRecognitionTable> filterTables(Split s, List<NameRecognitionTable> tableList) {
		List<NameRecognitionTable> result = new ArrayList<NameRecognitionTable>();
		for (NameRecognitionTable r : tableList) {
			if (r.stmtStartIndex >= s.start && r.stmtEndIndex <= s.stop)
				result.add(r);
		}
		return result;
	}

	private boolean hasSemanticError(List<SyntaxElement> tokens, int current, List<NameRecognitionTable> tableList) {
		SyntaxElement e = tokens.get(current);

		String strippedValue = StringUtil.stripQuotes(e.value);

		switch (e.type) {
		case tableName:
			return invalidTableName(strippedValue, tableList);
		case alias:
			return !getTableForAlias(e.value, tableList).isPresent();
		case columnName:
			return invalidColumn(tokens, current, tableList);
		default:
			return false;
		}

	}

	private boolean invalidColumn(List<SyntaxElement> tokens, int current, List<NameRecognitionTable> tableList) {
		String columnName = tokens.get(current).value;
		Optional<TableReference> table = Optional.absent();

		Optional<SyntaxElement> aliasToken = getAlias(tokens, current);

		if (aliasToken.isPresent()){
			table = getTableForAlias(aliasToken.get().value, tableList);
			if (! table.isPresent())
				return true;
			
			return !hasColumn(columnName, table.get());
		}
		
		return !anyHasColumn(columnName, tableList);
	}

	private boolean anyHasColumn(String column, List<NameRecognitionTable> tableList) {
		for (NameRecognitionTable t : tableList) {
			String tableName = t.TableName().get();
			Integer tId = tables.get(tableName);
			if (tId != null && hasColumn(column, tableReferences.get(tId)))
				return true;
		}
		return false;
	}

	private boolean hasColumn(String column, TableReference ref) {
		return ref.columnNames.indexOf(column) >= 0;
	}

	private Optional<TableReference> getTableForAlias(String alias, List<NameRecognitionTable> tableList) {
		Optional<NameRecognitionTable> aliased = getForAlias(alias, tableList);

		if (aliased.isPresent()) {
			Integer refId = tables.get(aliased.get().TableName().or(""));
			if (refId != null)
				return Optional.of(tableReferences.get(refId));
		}
		return Optional.absent();
	}

	private Optional<NameRecognitionTable> getForAlias(String alias, List<NameRecognitionTable> tableList) {
		for (NameRecognitionTable n : tableList)
			if (n.TableAlias().isPresent() && n.TableAlias().get().equals(alias)) {
				return Optional.of(n);
			}
		return Optional.absent();

	}

	private boolean invalidTableName(String identifier, List<NameRecognitionTable> tableList) {
		Optional<NameRecognitionTable> aliased = getForAlias(identifier, tableList);

		String tableName;
		if (aliased.isPresent())
			tableName = aliased.get().TableName().or("");
		else
			tableName = identifier;

		return tables.get(tableName) == null;
	}

	private Optional<SyntaxElement> getAlias(List<SyntaxElement> tokens, int columnTokenIndex) {
		if (columnTokenIndex >= 1 && tokens.get(columnTokenIndex - 1).type == SyntaxElementType.tableName)
			return Optional.of(tokens.get(columnTokenIndex - 1));
		return Optional.absent();
	}

}
