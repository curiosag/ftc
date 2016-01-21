package manipulations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.cg.common.check.*;
import org.cg.common.core.Logging;
import org.cg.common.util.StringUtil;
import org.cg.ftc.parser.FusionTablesSqlLexer;
import org.cg.ftc.parser.FusionTablesSqlParser;

public class Util {

	private static Map<String, StatementType> types = new HashMap<String, StatementType>();

	Logging log;

	static {
		types.put("alter_table_stmt", StatementType.ALTER);
		types.put("create_view_stmt", StatementType.CREATE_VIEW);
		types.put("delete_stmt", StatementType.DELETE);
		types.put("insert_stmt", StatementType.INSERT);
		types.put("select_stmt", StatementType.SELECT);
		types.put("update_stmt", StatementType.UPDATE);
		types.put("drop_table_stmt", StatementType.DROP);
	}

	public static class Stuff {
		public final FusionTablesSqlLexer lexer;
		public final CommonTokenStream tokenStream;
		public final FusionTablesSqlParser parser;

		public Stuff(FusionTablesSqlLexer lexer, CommonTokenStream tokenStream, FusionTablesSqlParser parser) {
			this.lexer = lexer;
			this.tokenStream = tokenStream;
			this.parser = parser;
		}

	}

	private static StatementType getSqlStatementType(String rule_name) {
		Check.notNull(rule_name);
		StatementType result = StatementType.UNKNOWN;

		if (types.containsKey(rule_name))
			result = types.get(rule_name);

		return result;
	}

	public static String getRuleName(ParseTree ruleContext, FusionTablesSqlParser parser) {
		Check.isTrue((ruleContext instanceof RuleContext));
		RuleContext r = (RuleContext) ruleContext;
		return parser.getRuleNames()[r.getRuleIndex()];
	}

	public static String getTerminalValue(ParseTree t, FusionTablesSqlParser parser) {
		TerminalNode terminal = getTerminal(t, parser);
		
		String result = null;
		
		if (terminal != null) 
			result = terminal.getText();
		
		return result;
	}
	
	public static TerminalNode getTerminal(ParseTree t, FusionTablesSqlParser parser) {
		Check.isTrue(t.getChildCount() == 1);
		ParseTree c = t.getChild(0);
		TerminalNode result = null;
		
		if (c != null){ 
			Check.isTrue(c instanceof TerminalNode);
			result = (TerminalNode) c;
		}
			
		return result;
	}

	public static StatementType getSqlStatementType(ParseTree t, FusionTablesSqlParser parser) {
		StatementType result = StatementType.UNKNOWN;

		if (t instanceof RuleContext && t.getChildCount() > 0 && t.getChild(0) instanceof RuleContext) {
			ParseTree c = t.getChild(0);
			RuleContext r = (RuleContext) c;
			result = getSqlStatementType((parser.getRuleNames()[r.getRuleIndex()]));
		}
		return result;
	}

	public static ANTLRInputStream getFileInput(String filePath) {
		ANTLRInputStream input = null;
		try {
			input = new ANTLRFileStream(filePath);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return input;
	}

	public static ANTLRInputStream getInput(String source) {
		ANTLRInputStream input;

		input = new ANTLRInputStream(StringUtil.nonNull(source));

		return input;
	}

	public static String pathToRoot(ParserRuleContext ctx) {
		if (ctx != null)
			return pathToRoot(ctx.getParent()) + '/' + ctx.getTokens(0).get(0).getText();
		else
			return "";
	}

	public static Util.Stuff getParser(String input) {
		return getParser(getInput(input));
	}
	
	public static Util.Stuff getParser(ANTLRInputStream input) {
		FusionTablesSqlLexer lexer = new FusionTablesSqlLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		FusionTablesSqlParser parser = new FusionTablesSqlParser(tokens);
		parser.setBuildParseTree(true);
		return new Util.Stuff(lexer, tokens, parser);
	}

	public static void addDebugListeners(FusionTablesSqlParser parser) {
		parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
		parser.removeErrorListeners();
		parser.addErrorListener(new DiagnosticErrorListener());
		parser.addErrorListener(new VerboseErrorListener());
	}		
	
	public static VerboseErrorListener addVerboseErrorListener(FusionTablesSqlParser parser)
	{
		VerboseErrorListener verbose = new VerboseErrorListener();
		parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
		parser.removeErrorListeners();
		parser.addErrorListener(verbose);
		return verbose;
	}
	
	public static ParserRuleContext climb(ParserRuleContext ctx, int levels) {
		if (ctx == null)
			return null;

		if (levels == 0)
			return ctx;
		else
			return climb(ctx.getParent(), levels - 1);
	}

	/**
	 * descends on leftmost children
	 * @param t
	 * @return
	 */
	public static TerminalNode digTerminal(ParseTree t) {
		if (t instanceof TerminalNode)
			return (TerminalNode) t;

		if (t.getChildCount() == 0)
			return null;
		
		return digTerminal(t.getChild(0));
	}
	
	public static boolean isTableName(FusionTablesSqlParser.String_literalContext ctx) {
		int stepsToClimb = 2; // string_literal -> identifier -> table_name
		ParserRuleContext maybeTableCtx = climb(ctx, stepsToClimb);
		return maybeTableCtx != null && maybeTableCtx.getClass().equals(FusionTablesSqlParser.Table_nameContext.class);
	}
	
	public static boolean withinContext(CursorContext context, ParserRuleContext... candidates) {
		for (ParserRuleContext e : context.getContextStack())
			for (int i = 0; i < candidates.length; i++)
				if (candidates[i].getClass().getName().equals(e.getClass().getName()))
					return true;

		return false;
	}

	
}
