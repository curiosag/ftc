package manipulations;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.cg.common.util.Op;
import org.cg.common.util.StringUtil;
import org.cg.ftc.shared.interfaces.SyntaxElement;
import org.cg.ftc.shared.interfaces.SyntaxElementType;
import manipulations.results.TableReference;

public class Semantics {

	private final List<TableReference> tableReferences = new ArrayList<TableReference>();
	private final Dictionary<String, Integer> columns = new Hashtable<String, Integer>();
	private final Dictionary<String, Integer> tables = new Hashtable<String, Integer>();

	public void addReference(TableReference ref) {
		tableReferences.add(ref);

		// tableName may occur also in qualified column names as
		// <tableName>.<columnName>
		// where it also may be an alias or the table Id
		tables.put(ref.tableId, 0);
		tables.put(ref.tableAlias.or(""), 0);
		tables.put(ref.tableName, 0);

		for (String n : ref.columnNames)
			columns.put(n, 0);
	}

	/**
	 * 
	 * @param tokens
	 * @param allNames2
	 * @return tokens mutated
	 */
	public List<SyntaxElement> setSemanticAttributes(List<SyntaxElement> tokens) {

		for (int i = 0; i < tokens.size(); i++)
			tokens.get(i).setSemanticError(hasSemanticError(tokens, i));

		return tokens;
	}

	private boolean hasSemanticError(List<SyntaxElement> tokens, int current) {
		SyntaxElement e = tokens.get(current);
		if (!Op.in(e.type, SyntaxElementType.columnName, SyntaxElementType.tableName))
			return false;

		if (tableReferences.isEmpty())
			return true;

		String strippedValue = StringUtil.stripQuotes(e.value);

		return ((e.type == SyntaxElementType.columnName)
				&& (columns.get(strippedValue) == null || columnFollowsInvalidAlias(tokens, current)))
				|| ((e.type == SyntaxElementType.tableName) && tables.get(strippedValue) == null);

	}

	private boolean columnFollowsInvalidAlias(List<SyntaxElement> tokens, int current) {
		// seems we don't see the "." thing here
		return current >= 1 && tokens.get(current - 1).hasSemanticError();
	}

}