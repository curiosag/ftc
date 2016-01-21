package org.cg.ftc.shared.interfaces;

public enum SqlCompletionType {
	categorySnippet, table, column {

		/**
		 * parse rule result_column is mapped to this element so it always
		 * signifies a piece of sql that potentially exists of 
		 *  >>table_name "." column_name<<
		 */
		@SuppressWarnings("unused")
		public void semantics() {
		}

	},
	unknown, ftSql, aggregate, columnConditionExpr, columnConditionExprAfterColumn, orderBy, groupBy, keywordWhere
}