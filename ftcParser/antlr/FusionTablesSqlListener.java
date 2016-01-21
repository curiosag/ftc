// Generated from FusionTablesSql.g4 by ANTLR 4.5
package parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FusionTablesSqlParser}.
 */
public interface FusionTablesSqlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#fusionTablesSql}.
	 * @param ctx the parse tree
	 */
	void enterFusionTablesSql(FusionTablesSqlParser.FusionTablesSqlContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#fusionTablesSql}.
	 * @param ctx the parse tree
	 */
	void exitFusionTablesSql(FusionTablesSqlParser.FusionTablesSqlContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#sql_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSql_stmt(FusionTablesSqlParser.Sql_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#sql_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSql_stmt(FusionTablesSqlParser.Sql_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#table_name_in_ddl}.
	 * @param ctx the parse tree
	 */
	void enterTable_name_in_ddl(FusionTablesSqlParser.Table_name_in_ddlContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#table_name_in_ddl}.
	 * @param ctx the parse tree
	 */
	void exitTable_name_in_ddl(FusionTablesSqlParser.Table_name_in_ddlContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#table_name_in_dml}.
	 * @param ctx the parse tree
	 */
	void enterTable_name_in_dml(FusionTablesSqlParser.Table_name_in_dmlContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#table_name_in_dml}.
	 * @param ctx the parse tree
	 */
	void exitTable_name_in_dml(FusionTablesSqlParser.Table_name_in_dmlContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#describe_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDescribe_stmt(FusionTablesSqlParser.Describe_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#describe_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDescribe_stmt(FusionTablesSqlParser.Describe_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#show_tables_stmt}.
	 * @param ctx the parse tree
	 */
	void enterShow_tables_stmt(FusionTablesSqlParser.Show_tables_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#show_tables_stmt}.
	 * @param ctx the parse tree
	 */
	void exitShow_tables_stmt(FusionTablesSqlParser.Show_tables_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#alter_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAlter_table_stmt(FusionTablesSqlParser.Alter_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#alter_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAlter_table_stmt(FusionTablesSqlParser.Alter_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#create_view_stmt}.
	 * @param ctx the parse tree
	 */
	void enterCreate_view_stmt(FusionTablesSqlParser.Create_view_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#create_view_stmt}.
	 * @param ctx the parse tree
	 */
	void exitCreate_view_stmt(FusionTablesSqlParser.Create_view_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#drop_table_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDrop_table_stmt(FusionTablesSqlParser.Drop_table_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#drop_table_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDrop_table_stmt(FusionTablesSqlParser.Drop_table_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#insert_stmt}.
	 * @param ctx the parse tree
	 */
	void enterInsert_stmt(FusionTablesSqlParser.Insert_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#insert_stmt}.
	 * @param ctx the parse tree
	 */
	void exitInsert_stmt(FusionTablesSqlParser.Insert_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#update_stmt}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_stmt(FusionTablesSqlParser.Update_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#update_stmt}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_stmt(FusionTablesSqlParser.Update_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#column_assignment}.
	 * @param ctx the parse tree
	 */
	void enterColumn_assignment(FusionTablesSqlParser.Column_assignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#column_assignment}.
	 * @param ctx the parse tree
	 */
	void exitColumn_assignment(FusionTablesSqlParser.Column_assignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#delete_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDelete_stmt(FusionTablesSqlParser.Delete_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#delete_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDelete_stmt(FusionTablesSqlParser.Delete_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#eq_comparison}.
	 * @param ctx the parse tree
	 */
	void enterEq_comparison(FusionTablesSqlParser.Eq_comparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#eq_comparison}.
	 * @param ctx the parse tree
	 */
	void exitEq_comparison(FusionTablesSqlParser.Eq_comparisonContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#table_name_with_alias}.
	 * @param ctx the parse tree
	 */
	void enterTable_name_with_alias(FusionTablesSqlParser.Table_name_with_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#table_name_with_alias}.
	 * @param ctx the parse tree
	 */
	void exitTable_name_with_alias(FusionTablesSqlParser.Table_name_with_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#select_stmt}.
	 * @param ctx the parse tree
	 */
	void enterSelect_stmt(FusionTablesSqlParser.Select_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#select_stmt}.
	 * @param ctx the parse tree
	 */
	void exitSelect_stmt(FusionTablesSqlParser.Select_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#ordering_term}.
	 * @param ctx the parse tree
	 */
	void enterOrdering_term(FusionTablesSqlParser.Ordering_termContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#ordering_term}.
	 * @param ctx the parse tree
	 */
	void exitOrdering_term(FusionTablesSqlParser.Ordering_termContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#join_clause}.
	 * @param ctx the parse tree
	 */
	void enterJoin_clause(FusionTablesSqlParser.Join_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#join_clause}.
	 * @param ctx the parse tree
	 */
	void exitJoin_clause(FusionTablesSqlParser.Join_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#result_column}.
	 * @param ctx the parse tree
	 */
	void enterResult_column(FusionTablesSqlParser.Result_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#result_column}.
	 * @param ctx the parse tree
	 */
	void exitResult_column(FusionTablesSqlParser.Result_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#qualified_column_name}.
	 * @param ctx the parse tree
	 */
	void enterQualified_column_name(FusionTablesSqlParser.Qualified_column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#qualified_column_name}.
	 * @param ctx the parse tree
	 */
	void exitQualified_column_name(FusionTablesSqlParser.Qualified_column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#aggregate_exp}.
	 * @param ctx the parse tree
	 */
	void enterAggregate_exp(FusionTablesSqlParser.Aggregate_expContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#aggregate_exp}.
	 * @param ctx the parse tree
	 */
	void exitAggregate_exp(FusionTablesSqlParser.Aggregate_expContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(FusionTablesSqlParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(FusionTablesSqlParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#column_name_beginning_expr}.
	 * @param ctx the parse tree
	 */
	void enterColumn_name_beginning_expr(FusionTablesSqlParser.Column_name_beginning_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#column_name_beginning_expr}.
	 * @param ctx the parse tree
	 */
	void exitColumn_name_beginning_expr(FusionTablesSqlParser.Column_name_beginning_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#column_name_in_dml}.
	 * @param ctx the parse tree
	 */
	void enterColumn_name_in_dml(FusionTablesSqlParser.Column_name_in_dmlContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#column_name_in_dml}.
	 * @param ctx the parse tree
	 */
	void exitColumn_name_in_dml(FusionTablesSqlParser.Column_name_in_dmlContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#and_or_or}.
	 * @param ctx the parse tree
	 */
	void enterAnd_or_or(FusionTablesSqlParser.And_or_orContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#and_or_or}.
	 * @param ctx the parse tree
	 */
	void exitAnd_or_or(FusionTablesSqlParser.And_or_orContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#geometry}.
	 * @param ctx the parse tree
	 */
	void enterGeometry(FusionTablesSqlParser.GeometryContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#geometry}.
	 * @param ctx the parse tree
	 */
	void exitGeometry(FusionTablesSqlParser.GeometryContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#circle}.
	 * @param ctx the parse tree
	 */
	void enterCircle(FusionTablesSqlParser.CircleContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#circle}.
	 * @param ctx the parse tree
	 */
	void exitCircle(FusionTablesSqlParser.CircleContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#rectangle}.
	 * @param ctx the parse tree
	 */
	void enterRectangle(FusionTablesSqlParser.RectangleContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#rectangle}.
	 * @param ctx the parse tree
	 */
	void exitRectangle(FusionTablesSqlParser.RectangleContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#coordinate}.
	 * @param ctx the parse tree
	 */
	void enterCoordinate(FusionTablesSqlParser.CoordinateContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#coordinate}.
	 * @param ctx the parse tree
	 */
	void exitCoordinate(FusionTablesSqlParser.CoordinateContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#keyword}.
	 * @param ctx the parse tree
	 */
	void enterKeyword(FusionTablesSqlParser.KeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#keyword}.
	 * @param ctx the parse tree
	 */
	void exitKeyword(FusionTablesSqlParser.KeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(FusionTablesSqlParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(FusionTablesSqlParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(FusionTablesSqlParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(FusionTablesSqlParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#error_message}.
	 * @param ctx the parse tree
	 */
	void enterError_message(FusionTablesSqlParser.Error_messageContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#error_message}.
	 * @param ctx the parse tree
	 */
	void exitError_message(FusionTablesSqlParser.Error_messageContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(FusionTablesSqlParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(FusionTablesSqlParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#column_alias}.
	 * @param ctx the parse tree
	 */
	void enterColumn_alias(FusionTablesSqlParser.Column_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#column_alias}.
	 * @param ctx the parse tree
	 */
	void exitColumn_alias(FusionTablesSqlParser.Column_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#table_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_name(FusionTablesSqlParser.Table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#table_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_name(FusionTablesSqlParser.Table_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#column_name}.
	 * @param ctx the parse tree
	 */
	void enterColumn_name(FusionTablesSqlParser.Column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#column_name}.
	 * @param ctx the parse tree
	 */
	void exitColumn_name(FusionTablesSqlParser.Column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#new_table_name}.
	 * @param ctx the parse tree
	 */
	void enterNew_table_name(FusionTablesSqlParser.New_table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#new_table_name}.
	 * @param ctx the parse tree
	 */
	void exitNew_table_name(FusionTablesSqlParser.New_table_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#view_name}.
	 * @param ctx the parse tree
	 */
	void enterView_name(FusionTablesSqlParser.View_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#view_name}.
	 * @param ctx the parse tree
	 */
	void exitView_name(FusionTablesSqlParser.View_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#table_alias}.
	 * @param ctx the parse tree
	 */
	void enterTable_alias(FusionTablesSqlParser.Table_aliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#table_alias}.
	 * @param ctx the parse tree
	 */
	void exitTable_alias(FusionTablesSqlParser.Table_aliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#numeric_literal}.
	 * @param ctx the parse tree
	 */
	void enterNumeric_literal(FusionTablesSqlParser.Numeric_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#numeric_literal}.
	 * @param ctx the parse tree
	 */
	void exitNumeric_literal(FusionTablesSqlParser.Numeric_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link FusionTablesSqlParser#string_literal}.
	 * @param ctx the parse tree
	 */
	void enterString_literal(FusionTablesSqlParser.String_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link FusionTablesSqlParser#string_literal}.
	 * @param ctx the parse tree
	 */
	void exitString_literal(FusionTablesSqlParser.String_literalContext ctx);
}