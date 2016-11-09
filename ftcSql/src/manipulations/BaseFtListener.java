package manipulations;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.cg.ftc.parser.FusionTablesSqlBaseListener;
import org.cg.ftc.parser.FusionTablesSqlParser;

public class BaseFtListener extends FusionTablesSqlBaseListener {

	public final BufferedTokenStream tokens;
	// recognized sql statements
	public final List<Split> splits = new ArrayList<Split>();
	
	protected boolean isGenericError(String errorString) {
		return errorString.startsWith("<") && errorString.endsWith(">");
	}

	public BaseFtListener(BufferedTokenStream tokens) {
		this.tokens = tokens;
	}
	
	@Override
	public void exitSql_stmt(FusionTablesSqlParser.Sql_stmtContext ctx) {
		if (ctx != null && ctx.start != null && ctx.stop != null)
			splits.add(
					new Split(tokens.getText(ctx.start, ctx.stop), ctx.start.getStartIndex(), ctx.stop.getStopIndex()));
	}

}