package manipulations;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.cg.common.check.Check;
import org.cg.ftc.parser.FusionTablesSqlBaseListener;
import org.cg.ftc.parser.FusionTablesSqlParser;

public class StatementSplitter extends FusionTablesSqlBaseListener {

	private final BufferedTokenStream tokens;
	public final List<Split> splits = new ArrayList<Split>();
	
	public StatementSplitter(BufferedTokenStream tokens) {
		Check.notNull(tokens);
		this.tokens = tokens;
	}

	@Override
	public void exitSql_stmt(FusionTablesSqlParser.Sql_stmtContext ctx) {
		splits.add(new Split(tokens.getText(ctx.start, ctx.stop), ctx.start.getStartIndex(), ctx.stop.getStopIndex()));
	}

}
