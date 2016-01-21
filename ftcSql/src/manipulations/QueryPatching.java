package manipulations;

import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.cg.common.structures.OrderedIntTuple;
import org.cg.common.util.StringUtil;
import org.cg.ftc.shared.interfaces.SqlCompletionType;
import org.cg.ftc.shared.structures.AbstractCompletion;
import org.cg.ftc.shared.structures.ColumnInfo;
import org.cg.ftc.shared.structures.Completions;
import org.cg.ftc.shared.structures.ModelElementCompletion;
import org.cg.ftc.shared.structures.TableInfo;

import com.google.common.base.Optional;

import manipulations.results.TableInfoResolver;

public class QueryPatching {
	
	public final CursorContext cursorContext;
	public final Optional<ParserRuleContext> parserRuleContext;
	public final int cursorPosition;
	public Optional<Integer> newCursorPosition = Optional.absent();
	private final String query;
	private final TableInfoResolver tableInfoResolver;

	private boolean bareSelect(String q) {
		return q.toLowerCase().replace("select", "").replace(" ", "").length() == 0;
	}

	public QueryPatching(TableInfoResolver tableInfoResolver, CursorContext context, int cursorPosition, String query) {
		this.cursorContext = context;
		parserRuleContext = context.getParserRuleContext();

		this.cursorPosition = cursorPosition;
		this.query = query;
		this.tableInfoResolver = tableInfoResolver;
	}

	private void setNewCursorPosition(int i) {
		newCursorPosition = Optional.of(Integer.valueOf(i));
	}

	public String patch(AbstractCompletion completion) {
		String result = query;
		boolean bareSelect = bareSelect(query);

		String patch = completion.getPatch();
		Optional<OrderedIntTuple> boundaries = cursorContext.boundaries;

		if (completion.completionType == SqlCompletionType.columnConditionExprAfterColumn)
			result = appendCompletion(patch.replace("{column_name}", ""));
		else {
			if (boundaries.isPresent()) {
				result = StringUtil.replace(query, boundaries.get(), patch);
				
				int offsetNewCursor = patch.length() - 1 - (boundaries.get().hi() - boundaries.get().lo());
				setNewCursorPosition(cursorPosition + offsetNewCursor);
			} else
				result = appendCompletion(patch);
		}

		if (bareSelect && completion.completionType == SqlCompletionType.column && completion.parent != null)
			result = result + "\nFROM " + completion.parent.displayName + ";";

		return result;
	}

	private String appendCompletion(String value) {
		String result;
		result = StringUtil.insert(query, cursorPosition, value);
		setNewCursorPosition(cursorPosition + value.length());
		return result;
	}

	private final static boolean addColumnDetails = true;

	public Completions getCompletions() {
		Completions result = new Completions(cursorContext.boundaries);	
		
		final boolean isExprContext = cursorContext.completionOptions.indexOf(SqlCompletionType.columnConditionExpr) >= 0;
		
		for (SqlCompletionType c : cursorContext.completionOptions)
			switch (c) {
			case table:
				result.addAll(toCompletions(tableInfoResolver.listTables(), !addColumnDetails));
				break;

			case column:
				if (! isExprContext && cursorContext.underlyingTableName.isPresent()) {
					Optional<TableInfo> i = tableInfoResolver.getTableInfo(cursorContext.underlyingTableName.get());
					if (i.isPresent()) 
						result.addAll(columnsToCompletions(i.get()));
				}
				break;

			case unknown:
				;

			default:
				result.addAll(Snippets.instance().get(c));
			}

		return result;
	}

	private Completions toCompletions(List<TableInfo> tableInfo, boolean addDetails) {
		Completions result = new Completions(cursorContext.boundaries);
		for (TableInfo t : tableInfo)
			result.add(fromTable(t, addDetails));

		return result;
	}

	private Completions columnsToCompletions(TableInfo tableInfo) {
		Completions result = new Completions(cursorContext.boundaries);
		for (ColumnInfo c : tableInfo.columns)
			result.add(new ModelElementCompletion(SqlCompletionType.column, c.name, null));

		return result;
	}

	private ModelElementCompletion fromTable(TableInfo t, boolean addDetails) {
		ModelElementCompletion result = new ModelElementCompletion(SqlCompletionType.table, t.name, null);

		if (addDetails)
			for (ColumnInfo c : t.columns)
				result.addChild(new ModelElementCompletion(SqlCompletionType.column, c.name, result));

		return result;
	}

}
