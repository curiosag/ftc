package manipulations;

import java.util.HashMap;
import java.util.Map;

import org.cg.common.structures.OrderedIntTuple;
import org.cg.ftc.shared.interfaces.SqlCompletionType;
import org.cg.ftc.shared.structures.Completions;

import com.google.common.base.Optional;


public class Snippets {

	private final static boolean extended_dml = false;
	
	private static Optional<OrderedIntTuple> noBoundaries = Optional.absent();
	private static Snippets instance = null;
	private Map<SqlCompletionType, Completions> completionMap = new HashMap<SqlCompletionType, Completions>();

	public Completions get(SqlCompletionType t)
	{
		Completions c = completionMap.get(t);
		if (c == null)
			c = new Completions(noBoundaries);
		
		return c;
	}
	
	private Completions getAggregateExpressions() {
		Completions result = new Completions(noBoundaries);

		result.addSnippet(SqlCompletionType.aggregate, "sum", "SUM(${c}${cursor})");
		result.addSnippet(SqlCompletionType.aggregate, "count", "COUNT(${c}${cursor})");
		result.addSnippet(SqlCompletionType.aggregate, "average", "AVERAGE(${c}${cursor})");
		result.addSnippet(SqlCompletionType.aggregate, "maximum", "MAXIMUM(${c}${cursor})");
		result.addSnippet(SqlCompletionType.aggregate, "minimum", "MINIMUM(${c}${cursor})");

		return result;
	}

	/**
	 * every parameters starting with "c" are used for columns,
	 * triggering column retrieval in completions, "t"s are for tables
	 * @return
	 */
	private Completions getSqlStatementExpressions() {
		Completions result = new Completions(noBoundaries);

		result.addSnippet(SqlCompletionType.ftSql, "alter table", "ALTER TABLE ${t} RENAME TO ${name}${cursor}; ");
		result.addSnippet(SqlCompletionType.ftSql, "drop table", "DROP TABLE ${t}${cursor}; ");
		 //${cursor} logic doesen't really work here, because it can't be placed after $t. first table gets chosen, then column
		result.addSnippet(SqlCompletionType.ftSql, "select", "SELECT ${c} FROM ${t};");
		result.addSnippet(SqlCompletionType.ftSql, "insert single", "INSERT INTO ${t} (${c}) \nVALUES (${value}${cursor});");
		result.addSnippet(SqlCompletionType.ftSql, "insert multi",
				"INSERT INTO ${t} (${c1}, ${c2}) \nVALUES (${value1}, ${value2}${cursor});");
		
		result.addSnippet(SqlCompletionType.ftSql, "delete",
				"DELETE FROM ${t} WHERE ${c}=${value}${cursor}; ");
		result.addSnippet(SqlCompletionType.ftSql, "delete all",
				"DELETE FROM ${t}${cursor}; ");
		if (extended_dml) {
			result.addSnippet(SqlCompletionType.ftSql, "update single", "UPDATE ${t} SET ${c} = ${value}${cursor};");
			result.addSnippet(SqlCompletionType.ftSql, "update multi",
					"UPDATE ${t} SET ${c1} = ${value1}, ${c2} = ${value2}${cursor}; ");	
		}
		
		result.addSnippet(SqlCompletionType.ftSql, "describe table", "DESCRIBE ${t}${cursor};");
		
		return result;
	}
		
	private Completions getColumnConditionExpressions(SqlCompletionType t) {
		Completions result = new Completions(noBoundaries);
		
		result.addSnippet(t, "=", "${c} = ${value}${cursor}");
		result.addSnippet(t, ">", "${c} > ${value}${cursor} ");
		result.addSnippet(t, "<", "${c} < ${value}${cursor} ");
		result.addSnippet(t, ">=", "${c} => ${value}${cursor} ");
		result.addSnippet(t, "<=", "${c} <= ${value}${cursor} ");

		result.addSnippet(t, "in", "${c} IN('${value1}', '${value2}')${cursor}");
		result.addSnippet(t, "between", "${c} BETWEEN '${value1}' AND '${value2}'${cursor}");
		result.addSnippet(t, "like", "${c} LIKE '${value}'${cursor}");
		result.addSnippet(t, "matches", "${c} MATCHES '${value}'");
		result.addSnippet(t, "starts with", "${c} STARTS WITH '${value}'${cursor}");
		result.addSnippet(t, "ends with", "${c} ENDS WITH '${value}'${cursor}");
		result.addSnippet(t, "contains", "${c} CONTAINS '${value}'${cursor}");
		result.addSnippet(t, "contains ignoring case", "${c} CONTAINS IGNORING CASE '${value}'${cursor}");
		result.addSnippet(t, "does not contain", "${c} DOES NOT CONTAIN '${value}'${cursor}");
		result.addSnippet(t, "not equal to", "${c} NOT EQUAL TO '${value}'${cursor}");

		if (t == SqlCompletionType.columnConditionExpr){
			result.addSnippet(t, "st_intersects circle", "ST_INTERSECTS(${c}, CIRCLE(LATLNG(${number1}, ${number2}), ${number3})${cursor} ");
			result.addSnippet(t, "st_intersects rectangle", "ST_INTERSECTS(${c}, RECTANGLE(LATLNG(${number1}, ${number2}), LATLNG(${number3}, ${number4})))${cursor} ");
		}
			
		return result;
	}

	private Completions getOrderByExpressions() {
		Completions result =  new Completions(noBoundaries);
		SqlCompletionType t = SqlCompletionType.orderBy;
		result.addSnippet(t, "order by", "ORDER BY ${c}${cursor}");
		result.addSnippet(t, "order by, descending", "ORDER BY ${c}${cursor} DESC${cursor}");
		result.addSnippet(t, "order by spatial distance", "ORDER BY ST_DISTANCE(${c}, LATLNG(${number1}, ${number2}${cursor})) ");
		
		return result;
	}

	private Completions getGroupByExpressions() {
		Completions result =  new Completions(noBoundaries);
		SqlCompletionType t = SqlCompletionType.groupBy;
		result.addSnippet(t, "group by", "GROUP BY ${c}${cursor} ");
		return result;
	}
	
	private Snippets() {
		completionMap.put(SqlCompletionType.ftSql, getSqlStatementExpressions());
		completionMap.put(SqlCompletionType.columnConditionExpr, getColumnConditionExpressions(SqlCompletionType.columnConditionExpr));
		completionMap.put(SqlCompletionType.columnConditionExprAfterColumn, getColumnConditionExpressions(SqlCompletionType.columnConditionExprAfterColumn));
		completionMap.put(SqlCompletionType.aggregate, getAggregateExpressions());
		completionMap.put(SqlCompletionType.groupBy, getGroupByExpressions());
		completionMap.put(SqlCompletionType.orderBy, getOrderByExpressions());
		completionMap.put(SqlCompletionType.keywordWhere, Completions.create(noBoundaries, SqlCompletionType.keywordWhere, "where - keyword", "WHERE "));
	}
	
	public static Snippets instance()
	{
		if (Snippets.instance == null)
			Snippets.instance = new Snippets();
		
		return instance;
	}
}
