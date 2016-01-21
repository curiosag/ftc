package org.cg.ftc.parser;

import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.cg.common.check.Check;

import com.google.common.base.Optional;

public class Ctx {

	public final static ParserRuleContext Table_name_with_alias = new FusionTablesSqlParser.Table_name_with_aliasContext(
			null, -1);
	public final static ParserRuleContext Column = new FusionTablesSqlParser.Result_columnContext(null, -1);
	public final static ParserRuleContext Column_name = new FusionTablesSqlParser.Column_nameContext(null, -1);
	public final static ParserRuleContext FusionTablesSql = new FusionTablesSqlParser.FusionTablesSqlContext(null, -1);
	public final static ParserRuleContext Expr = new FusionTablesSqlParser.ExprContext(null, -1);
	public final static ParserRuleContext Aggregate = new FusionTablesSqlParser.Aggregate_expContext(null, -1);
			
	public final static ParserRuleContext ResultColumn = new FusionTablesSqlParser.Result_columnContext(null, -1);
	/**/ public final static ParserRuleContext Qualified_column_name = new FusionTablesSqlParser.Qualified_column_nameContext(
			null, -1);
	public final static ParserRuleContext Column_name_beginning_expression = new FusionTablesSqlParser.Column_name_beginning_exprContext(
			null, -1);

	public static boolean is(ParserRuleContext c1, ParserRuleContext c2) {
		Check.isFalse((c1 == null || c2 == null));

		return c1.getClass().getCanonicalName().equals(c2.getClass().getCanonicalName());
	}

	public static Optional<ParserRuleContext> findInScope(ParserRuleContext c, Stack<ParserRuleContext> contexts) {
		Check.isFalse((c == null || contexts == null));

		for (ParserRuleContext ci : contexts)
			if (is(ci, c))
				return Optional.of(ci);

		return Optional.absent();
	}
}
