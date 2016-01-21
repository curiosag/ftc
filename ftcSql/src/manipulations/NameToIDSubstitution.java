package manipulations;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.cg.common.check.Check;
import org.cg.common.util.StringUtil;
import org.cg.ftc.parser.FusionTablesSqlBaseListener;
import org.cg.ftc.parser.FusionTablesSqlParser;

import com.google.common.base.Optional;

public class NameToIDSubstitution extends FusionTablesSqlBaseListener {

	private final TokenStreamRewriter rewriter;
	private final TableNameToIdMapper mapper;
	public List<String> problems = new LinkedList<String>();

	public NameToIDSubstitution(FusionTablesSqlParser parser, BufferedTokenStream tokens,
			TableNameToIdMapper namesToIds, Map<String, String> tableAliasToName) {
		Check.notNull(tokens);
		Check.notNull(namesToIds);
		Check.notNull(tableAliasToName);

		this.mapper = namesToIds;
		rewriter = new TokenStreamRewriter(tokens);
	}

	public String tuted() {
		return rewriter.getText();
	}

	@Override
	public void enterString_literal(FusionTablesSqlParser.String_literalContext ctx) {

		if (Util.isTableName(ctx)) {			
			String tableName = StringUtil.stripQuotes(ctx.getText());	
			Optional<String> tableId = mapper.resolveTableId(tableName);

			if (tableId.isPresent()) {
				rewriter.delete(ctx.start, ctx.stop);
				rewriter.insertAfter(ctx.start, tableId.get());
			} else
				problems.add("unknown table name: " + tableName);
		}

	}

}
