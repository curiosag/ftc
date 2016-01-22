// Generated from FusionTablesSql.g4 by ANTLR 4.5
package org.cg.ftc.parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FusionTablesSqlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FusionTablesSqlVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#fusionTablesSql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFusionTablesSql(FusionTablesSqlParser.FusionTablesSqlContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#sql_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_stmt(FusionTablesSqlParser.Sql_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#table_name_in_ddl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_name_in_ddl(FusionTablesSqlParser.Table_name_in_ddlContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#table_name_in_dml}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_name_in_dml(FusionTablesSqlParser.Table_name_in_dmlContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#describe_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescribe_stmt(FusionTablesSqlParser.Describe_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#show_tables_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShow_tables_stmt(FusionTablesSqlParser.Show_tables_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#alter_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_table_stmt(FusionTablesSqlParser.Alter_table_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#create_view_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreate_view_stmt(FusionTablesSqlParser.Create_view_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#drop_table_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_table_stmt(FusionTablesSqlParser.Drop_table_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#insert_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_stmt(FusionTablesSqlParser.Insert_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#update_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_stmt(FusionTablesSqlParser.Update_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#column_assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_assignment(FusionTablesSqlParser.Column_assignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#delete_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete_stmt(FusionTablesSqlParser.Delete_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#eq_comparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEq_comparison(FusionTablesSqlParser.Eq_comparisonContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#table_name_with_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_name_with_alias(FusionTablesSqlParser.Table_name_with_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#select_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_stmt(FusionTablesSqlParser.Select_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#ordering_term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrdering_term(FusionTablesSqlParser.Ordering_termContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#join_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoin_clause(FusionTablesSqlParser.Join_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#result_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResult_column(FusionTablesSqlParser.Result_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#qualified_column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualified_column_name(FusionTablesSqlParser.Qualified_column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#aggregate_exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggregate_exp(FusionTablesSqlParser.Aggregate_expContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(FusionTablesSqlParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#column_name_beginning_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_name_beginning_expr(FusionTablesSqlParser.Column_name_beginning_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#column_name_in_dml}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_name_in_dml(FusionTablesSqlParser.Column_name_in_dmlContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#and_or_or}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd_or_or(FusionTablesSqlParser.And_or_orContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#geometry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometry(FusionTablesSqlParser.GeometryContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#circle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCircle(FusionTablesSqlParser.CircleContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#rectangle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRectangle(FusionTablesSqlParser.RectangleContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#coordinate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCoordinate(FusionTablesSqlParser.CoordinateContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyword(FusionTablesSqlParser.KeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(FusionTablesSqlParser.OperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(FusionTablesSqlParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#error_message}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError_message(FusionTablesSqlParser.Error_messageContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(FusionTablesSqlParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#column_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_alias(FusionTablesSqlParser.Column_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#table_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_name(FusionTablesSqlParser.Table_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_name(FusionTablesSqlParser.Column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#new_table_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew_table_name(FusionTablesSqlParser.New_table_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#view_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitView_name(FusionTablesSqlParser.View_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#table_alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_alias(FusionTablesSqlParser.Table_aliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#numeric_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumeric_literal(FusionTablesSqlParser.Numeric_literalContext ctx);
	/**
	 * Visit a parse tree produced by {@link FusionTablesSqlParser#string_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_literal(FusionTablesSqlParser.String_literalContext ctx);
}