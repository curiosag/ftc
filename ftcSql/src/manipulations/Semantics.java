package manipulations;

import java.util.List;

import org.cg.common.check.Check;
import org.cg.common.util.Op;
import org.cg.common.util.StringUtil;
import org.cg.ftc.shared.interfaces.SyntaxElement;
import org.cg.ftc.shared.interfaces.SyntaxElementType;
import com.google.common.base.Optional;

import manipulations.results.TableReference;

public class Semantics {

	List<NameRecognition> columnReferences;
	private final Optional<TableReference> tableReference;

	public Semantics(Optional<TableReference> tableReference, List<NameRecognition> names) {

		Check.notNull(names);
		Check.notNull(tableReference);

		this.columnReferences = names;
		this.tableReference = tableReference;
	}

	/**
	 * 
	 * @param tokens
	 * @param allNames2
	 * @return tokens mutated
	 */
	public List<SyntaxElement> setSemanticAttributes(List<SyntaxElement> tokens) {

		for (SyntaxElement e : tokens)
			e.setSemanticError(hasSemanticError(e));

		return tokens;
	}

	private boolean hasSemanticError(SyntaxElement e) {
		if (!Op.in(e.type, SyntaxElementType.columnName, SyntaxElementType.tableName))
			return false;

		if (!tableReference.isPresent())
			return true;
		
		TableReference tableRef = tableReference.get();

		String strippedValue = StringUtil.stripQuotes(e.value);
		if (e.type == SyntaxElementType.columnName)
			return ! findColumnName(strippedValue, tableRef.columnNames);
		
		// tableName may occur also in qualified column names as <tableName>.<columnName>
		// where it also may be an alias or the table Id
		if (e.type == SyntaxElementType.tableName)
			return ! Op.in(strippedValue, tableRef.tableName, tableRef.tableAlias.or(""), tableRef.tableId);

		return false;
	}
	
	private boolean findColumnName(String value, List<String> columnNames) {
		for (String s : columnNames)
			if (s.equals(value))
				return true;
		
		return false;
	}

}
