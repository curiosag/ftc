// Generated from FusionTablesSql.g4 by ANTLR 4.5
package org.cg.ftc.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FusionTablesSqlParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, K_ALTER=5, K_AND=6, K_OR=7, K_AS=8, K_ASC=9, 
		K_AVERAGE=10, K_BY=11, K_BETWEEN=12, K_CASE=13, K_CIRCLE=14, K_COLUMN=15, 
		K_CONTAINS=16, K_COUNT=17, K_CREATE=18, K_DELETE=19, K_DESC=20, K_DESCRIBE=21, 
		K_DOES=22, K_CONTAIN=23, K_DROP=24, K_ENDS=25, K_FROM=26, K_GROUP=27, 
		K_HAVING=28, K_IGNORING=29, K_IN=30, K_INSERT=31, K_INTO=32, K_JOIN=33, 
		K_LATLNG=34, K_LEFT=35, K_LIKE=36, K_LIMIT=37, K_MATCHES=38, K_MAXIMUM=39, 
		K_MINIMUM=40, K_NOT=41, K_EQUAL=42, K_OF=43, K_OFFSET=44, K_ON=45, K_ORDER=46, 
		K_OUTER=47, K_RECTANGLE=48, K_RENAME=49, K_ST_DISTANCE=50, K_SELECT=51, 
		K_ST_INTERSECTS=52, K_SUM=53, K_SET=54, K_SHOW=55, K_STARTS=56, K_TABLE=57, 
		K_TABLES=58, K_TO=59, K_UPDATE=60, K_VALUES=61, K_VIEW=62, K_WHERE=63, 
		K_WITH=64, LT_EQ=65, GT_EQ=66, GT=67, EQ=68, LT=69, LPAR=70, RPAR=71, 
		NUMERIC_LITERAL=72, STRING_LITERAL=73, STRING=74, QUOTED_STRING=75, SINGLELINE_COMMENT=76, 
		MULTILINE_COMMENT=77, WHITESPACE=78;
	public static final int
		RULE_fusionTablesSql = 0, RULE_sql_stmt = 1, RULE_table_name_in_ddl = 2, 
		RULE_table_name_in_dml = 3, RULE_describe_stmt = 4, RULE_show_tables_stmt = 5, 
		RULE_alter_table_stmt = 6, RULE_create_view_stmt = 7, RULE_drop_table_stmt = 8, 
		RULE_insert_stmt = 9, RULE_update_stmt = 10, RULE_column_assignment = 11, 
		RULE_delete_stmt = 12, RULE_eq_comparison = 13, RULE_table_name_with_alias = 14, 
		RULE_select_stmt = 15, RULE_ordering_term = 16, RULE_join_clause = 17, 
		RULE_result_column = 18, RULE_qualified_column_name = 19, RULE_aggregate_exp = 20, 
		RULE_expr = 21, RULE_column_name_beginning_expr = 22, RULE_column_name_in_dml = 23, 
		RULE_and_or_or = 24, RULE_geometry = 25, RULE_circle = 26, RULE_rectangle = 27, 
		RULE_coordinate = 28, RULE_keyword = 29, RULE_operator = 30, RULE_literal = 31, 
		RULE_error_message = 32, RULE_identifier = 33, RULE_column_alias = 34, 
		RULE_table_name = 35, RULE_column_name = 36, RULE_new_table_name = 37, 
		RULE_view_name = 38, RULE_table_alias = 39, RULE_numeric_literal = 40, 
		RULE_string_literal = 41;
	public static final String[] ruleNames = {
		"fusionTablesSql", "sql_stmt", "table_name_in_ddl", "table_name_in_dml", 
		"describe_stmt", "show_tables_stmt", "alter_table_stmt", "create_view_stmt", 
		"drop_table_stmt", "insert_stmt", "update_stmt", "column_assignment", 
		"delete_stmt", "eq_comparison", "table_name_with_alias", "select_stmt", 
		"ordering_term", "join_clause", "result_column", "qualified_column_name", 
		"aggregate_exp", "expr", "column_name_beginning_expr", "column_name_in_dml", 
		"and_or_or", "geometry", "circle", "rectangle", "coordinate", "keyword", 
		"operator", "literal", "error_message", "identifier", "column_alias", 
		"table_name", "column_name", "new_table_name", "view_name", "table_alias", 
		"numeric_literal", "string_literal"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "';'", "','", "'*'", "'.'", null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, "'<='", "'>='", "'>'", "'='", "'<'", 
		"'('", "')'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, "K_ALTER", "K_AND", "K_OR", "K_AS", "K_ASC", 
		"K_AVERAGE", "K_BY", "K_BETWEEN", "K_CASE", "K_CIRCLE", "K_COLUMN", "K_CONTAINS", 
		"K_COUNT", "K_CREATE", "K_DELETE", "K_DESC", "K_DESCRIBE", "K_DOES", "K_CONTAIN", 
		"K_DROP", "K_ENDS", "K_FROM", "K_GROUP", "K_HAVING", "K_IGNORING", "K_IN", 
		"K_INSERT", "K_INTO", "K_JOIN", "K_LATLNG", "K_LEFT", "K_LIKE", "K_LIMIT", 
		"K_MATCHES", "K_MAXIMUM", "K_MINIMUM", "K_NOT", "K_EQUAL", "K_OF", "K_OFFSET", 
		"K_ON", "K_ORDER", "K_OUTER", "K_RECTANGLE", "K_RENAME", "K_ST_DISTANCE", 
		"K_SELECT", "K_ST_INTERSECTS", "K_SUM", "K_SET", "K_SHOW", "K_STARTS", 
		"K_TABLE", "K_TABLES", "K_TO", "K_UPDATE", "K_VALUES", "K_VIEW", "K_WHERE", 
		"K_WITH", "LT_EQ", "GT_EQ", "GT", "EQ", "LT", "LPAR", "RPAR", "NUMERIC_LITERAL", 
		"STRING_LITERAL", "STRING", "QUOTED_STRING", "SINGLELINE_COMMENT", "MULTILINE_COMMENT", 
		"WHITESPACE"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "FusionTablesSql.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public FusionTablesSqlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class FusionTablesSqlContext extends ParserRuleContext {
		public List<Sql_stmtContext> sql_stmt() {
			return getRuleContexts(Sql_stmtContext.class);
		}
		public Sql_stmtContext sql_stmt(int i) {
			return getRuleContext(Sql_stmtContext.class,i);
		}
		public FusionTablesSqlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fusionTablesSql; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterFusionTablesSql(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitFusionTablesSql(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitFusionTablesSql(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FusionTablesSqlContext fusionTablesSql() throws RecognitionException {
		FusionTablesSqlContext _localctx = new FusionTablesSqlContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_fusionTablesSql);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << K_ALTER) | (1L << K_CREATE) | (1L << K_DELETE) | (1L << K_DESCRIBE) | (1L << K_DROP) | (1L << K_INSERT) | (1L << K_SELECT) | (1L << K_SHOW) | (1L << K_UPDATE))) != 0)) {
				{
				{
				setState(84);
				sql_stmt();
				}
				}
				setState(89);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Sql_stmtContext extends ParserRuleContext {
		public Alter_table_stmtContext alter_table_stmt() {
			return getRuleContext(Alter_table_stmtContext.class,0);
		}
		public Select_stmtContext select_stmt() {
			return getRuleContext(Select_stmtContext.class,0);
		}
		public Create_view_stmtContext create_view_stmt() {
			return getRuleContext(Create_view_stmtContext.class,0);
		}
		public Delete_stmtContext delete_stmt() {
			return getRuleContext(Delete_stmtContext.class,0);
		}
		public Drop_table_stmtContext drop_table_stmt() {
			return getRuleContext(Drop_table_stmtContext.class,0);
		}
		public Insert_stmtContext insert_stmt() {
			return getRuleContext(Insert_stmtContext.class,0);
		}
		public Update_stmtContext update_stmt() {
			return getRuleContext(Update_stmtContext.class,0);
		}
		public Describe_stmtContext describe_stmt() {
			return getRuleContext(Describe_stmtContext.class,0);
		}
		public Show_tables_stmtContext show_tables_stmt() {
			return getRuleContext(Show_tables_stmtContext.class,0);
		}
		public Sql_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sql_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterSql_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitSql_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitSql_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Sql_stmtContext sql_stmt() throws RecognitionException {
		Sql_stmtContext _localctx = new Sql_stmtContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_sql_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			switch (_input.LA(1)) {
			case K_ALTER:
				{
				setState(90);
				alter_table_stmt();
				}
				break;
			case K_SELECT:
				{
				setState(91);
				select_stmt();
				}
				break;
			case K_CREATE:
				{
				setState(92);
				create_view_stmt();
				}
				break;
			case K_DELETE:
				{
				setState(93);
				delete_stmt();
				}
				break;
			case K_DROP:
				{
				setState(94);
				drop_table_stmt();
				}
				break;
			case K_INSERT:
				{
				setState(95);
				insert_stmt();
				}
				break;
			case K_UPDATE:
				{
				setState(96);
				update_stmt();
				}
				break;
			case K_DESCRIBE:
				{
				setState(97);
				describe_stmt();
				}
				break;
			case K_SHOW:
				{
				setState(98);
				show_tables_stmt();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(101);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Table_name_in_ddlContext extends ParserRuleContext {
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public Table_name_in_ddlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_table_name_in_ddl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterTable_name_in_ddl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitTable_name_in_ddl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitTable_name_in_ddl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Table_name_in_ddlContext table_name_in_ddl() throws RecognitionException {
		Table_name_in_ddlContext _localctx = new Table_name_in_ddlContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_table_name_in_ddl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			table_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Table_name_in_dmlContext extends ParserRuleContext {
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public Table_name_in_dmlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_table_name_in_dml; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterTable_name_in_dml(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitTable_name_in_dml(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitTable_name_in_dml(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Table_name_in_dmlContext table_name_in_dml() throws RecognitionException {
		Table_name_in_dmlContext _localctx = new Table_name_in_dmlContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_table_name_in_dml);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			table_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Describe_stmtContext extends ParserRuleContext {
		public TerminalNode K_DESCRIBE() { return getToken(FusionTablesSqlParser.K_DESCRIBE, 0); }
		public Table_name_in_ddlContext table_name_in_ddl() {
			return getRuleContext(Table_name_in_ddlContext.class,0);
		}
		public Describe_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_describe_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterDescribe_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitDescribe_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitDescribe_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Describe_stmtContext describe_stmt() throws RecognitionException {
		Describe_stmtContext _localctx = new Describe_stmtContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_describe_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			match(K_DESCRIBE);
			setState(108);
			table_name_in_ddl();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Show_tables_stmtContext extends ParserRuleContext {
		public TerminalNode K_SHOW() { return getToken(FusionTablesSqlParser.K_SHOW, 0); }
		public TerminalNode K_TABLES() { return getToken(FusionTablesSqlParser.K_TABLES, 0); }
		public Show_tables_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_show_tables_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterShow_tables_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitShow_tables_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitShow_tables_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Show_tables_stmtContext show_tables_stmt() throws RecognitionException {
		Show_tables_stmtContext _localctx = new Show_tables_stmtContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_show_tables_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			match(K_SHOW);
			setState(111);
			match(K_TABLES);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Alter_table_stmtContext extends ParserRuleContext {
		public TerminalNode K_ALTER() { return getToken(FusionTablesSqlParser.K_ALTER, 0); }
		public TerminalNode K_TABLE() { return getToken(FusionTablesSqlParser.K_TABLE, 0); }
		public List<Table_name_in_ddlContext> table_name_in_ddl() {
			return getRuleContexts(Table_name_in_ddlContext.class);
		}
		public Table_name_in_ddlContext table_name_in_ddl(int i) {
			return getRuleContext(Table_name_in_ddlContext.class,i);
		}
		public TerminalNode K_RENAME() { return getToken(FusionTablesSqlParser.K_RENAME, 0); }
		public TerminalNode K_TO() { return getToken(FusionTablesSqlParser.K_TO, 0); }
		public Alter_table_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alter_table_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterAlter_table_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitAlter_table_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitAlter_table_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Alter_table_stmtContext alter_table_stmt() throws RecognitionException {
		Alter_table_stmtContext _localctx = new Alter_table_stmtContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_alter_table_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			match(K_ALTER);
			setState(114);
			match(K_TABLE);
			setState(115);
			table_name_in_ddl();
			{
			setState(116);
			match(K_RENAME);
			setState(117);
			match(K_TO);
			setState(118);
			table_name_in_ddl();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Create_view_stmtContext extends ParserRuleContext {
		public TerminalNode K_CREATE() { return getToken(FusionTablesSqlParser.K_CREATE, 0); }
		public TerminalNode K_VIEW() { return getToken(FusionTablesSqlParser.K_VIEW, 0); }
		public View_nameContext view_name() {
			return getRuleContext(View_nameContext.class,0);
		}
		public TerminalNode K_AS() { return getToken(FusionTablesSqlParser.K_AS, 0); }
		public TerminalNode K_SELECT() { return getToken(FusionTablesSqlParser.K_SELECT, 0); }
		public List<Result_columnContext> result_column() {
			return getRuleContexts(Result_columnContext.class);
		}
		public Result_columnContext result_column(int i) {
			return getRuleContext(Result_columnContext.class,i);
		}
		public TerminalNode K_FROM() { return getToken(FusionTablesSqlParser.K_FROM, 0); }
		public Table_name_with_aliasContext table_name_with_alias() {
			return getRuleContext(Table_name_with_aliasContext.class,0);
		}
		public List<Join_clauseContext> join_clause() {
			return getRuleContexts(Join_clauseContext.class);
		}
		public Join_clauseContext join_clause(int i) {
			return getRuleContext(Join_clauseContext.class,i);
		}
		public TerminalNode K_WHERE() { return getToken(FusionTablesSqlParser.K_WHERE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Create_view_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_create_view_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterCreate_view_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitCreate_view_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitCreate_view_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Create_view_stmtContext create_view_stmt() throws RecognitionException {
		Create_view_stmtContext _localctx = new Create_view_stmtContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_create_view_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			match(K_CREATE);
			setState(121);
			match(K_VIEW);
			setState(122);
			view_name();
			setState(123);
			match(K_AS);
			setState(124);
			match(LPAR);
			setState(125);
			match(K_SELECT);
			setState(126);
			result_column();
			setState(131);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(127);
				match(T__1);
				setState(128);
				result_column();
				}
				}
				setState(133);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(134);
			match(K_FROM);
			{
			{
			setState(135);
			table_name_with_alias();
			setState(138);
			_la = _input.LA(1);
			if (_la==K_WHERE) {
				{
				setState(136);
				match(K_WHERE);
				setState(137);
				expr();
				}
			}

			}
			setState(143);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==K_LEFT) {
				{
				{
				setState(140);
				join_clause();
				}
				}
				setState(145);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(146);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Drop_table_stmtContext extends ParserRuleContext {
		public TerminalNode K_DROP() { return getToken(FusionTablesSqlParser.K_DROP, 0); }
		public TerminalNode K_TABLE() { return getToken(FusionTablesSqlParser.K_TABLE, 0); }
		public Table_name_in_ddlContext table_name_in_ddl() {
			return getRuleContext(Table_name_in_ddlContext.class,0);
		}
		public Drop_table_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_drop_table_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterDrop_table_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitDrop_table_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitDrop_table_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Drop_table_stmtContext drop_table_stmt() throws RecognitionException {
		Drop_table_stmtContext _localctx = new Drop_table_stmtContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_drop_table_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(K_DROP);
			setState(149);
			match(K_TABLE);
			setState(150);
			table_name_in_ddl();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Insert_stmtContext extends ParserRuleContext {
		public TerminalNode K_INSERT() { return getToken(FusionTablesSqlParser.K_INSERT, 0); }
		public TerminalNode K_INTO() { return getToken(FusionTablesSqlParser.K_INTO, 0); }
		public Table_name_in_dmlContext table_name_in_dml() {
			return getRuleContext(Table_name_in_dmlContext.class,0);
		}
		public List<Column_name_in_dmlContext> column_name_in_dml() {
			return getRuleContexts(Column_name_in_dmlContext.class);
		}
		public Column_name_in_dmlContext column_name_in_dml(int i) {
			return getRuleContext(Column_name_in_dmlContext.class,i);
		}
		public TerminalNode K_VALUES() { return getToken(FusionTablesSqlParser.K_VALUES, 0); }
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public Insert_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insert_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterInsert_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitInsert_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitInsert_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Insert_stmtContext insert_stmt() throws RecognitionException {
		Insert_stmtContext _localctx = new Insert_stmtContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_insert_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
			match(K_INSERT);
			setState(153);
			match(K_INTO);
			setState(154);
			table_name_in_dml();
			{
			setState(155);
			match(LPAR);
			setState(156);
			column_name_in_dml();
			setState(161);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(157);
				match(T__1);
				setState(158);
				column_name_in_dml();
				}
				}
				setState(163);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(164);
			match(RPAR);
			}
			{
			setState(166);
			match(K_VALUES);
			setState(167);
			match(LPAR);
			setState(168);
			literal();
			setState(173);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(169);
				match(T__1);
				setState(170);
				literal();
				}
				}
				setState(175);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(176);
			match(RPAR);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Update_stmtContext extends ParserRuleContext {
		public TerminalNode K_UPDATE() { return getToken(FusionTablesSqlParser.K_UPDATE, 0); }
		public Table_name_in_dmlContext table_name_in_dml() {
			return getRuleContext(Table_name_in_dmlContext.class,0);
		}
		public TerminalNode K_SET() { return getToken(FusionTablesSqlParser.K_SET, 0); }
		public List<Column_assignmentContext> column_assignment() {
			return getRuleContexts(Column_assignmentContext.class);
		}
		public Column_assignmentContext column_assignment(int i) {
			return getRuleContext(Column_assignmentContext.class,i);
		}
		public TerminalNode K_WHERE() { return getToken(FusionTablesSqlParser.K_WHERE, 0); }
		public Eq_comparisonContext eq_comparison() {
			return getRuleContext(Eq_comparisonContext.class,0);
		}
		public Update_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_update_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterUpdate_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitUpdate_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitUpdate_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Update_stmtContext update_stmt() throws RecognitionException {
		Update_stmtContext _localctx = new Update_stmtContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_update_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			match(K_UPDATE);
			setState(179);
			table_name_in_dml();
			setState(180);
			match(K_SET);
			setState(181);
			column_assignment();
			setState(186);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(182);
				match(T__1);
				setState(183);
				column_assignment();
				}
				}
				setState(188);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(189);
			match(K_WHERE);
			setState(190);
			eq_comparison();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Column_assignmentContext extends ParserRuleContext {
		public Column_name_in_dmlContext column_name_in_dml() {
			return getRuleContext(Column_name_in_dmlContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public Column_assignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column_assignment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterColumn_assignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitColumn_assignment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitColumn_assignment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Column_assignmentContext column_assignment() throws RecognitionException {
		Column_assignmentContext _localctx = new Column_assignmentContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_column_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(192);
			column_name_in_dml();
			setState(193);
			match(EQ);
			setState(194);
			literal();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Delete_stmtContext extends ParserRuleContext {
		public TerminalNode K_DELETE() { return getToken(FusionTablesSqlParser.K_DELETE, 0); }
		public TerminalNode K_FROM() { return getToken(FusionTablesSqlParser.K_FROM, 0); }
		public Table_name_in_dmlContext table_name_in_dml() {
			return getRuleContext(Table_name_in_dmlContext.class,0);
		}
		public TerminalNode K_WHERE() { return getToken(FusionTablesSqlParser.K_WHERE, 0); }
		public Column_name_in_dmlContext column_name_in_dml() {
			return getRuleContext(Column_name_in_dmlContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public Delete_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_delete_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterDelete_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitDelete_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitDelete_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Delete_stmtContext delete_stmt() throws RecognitionException {
		Delete_stmtContext _localctx = new Delete_stmtContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_delete_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(196);
			match(K_DELETE);
			setState(197);
			match(K_FROM);
			setState(198);
			table_name_in_dml();
			setState(204);
			_la = _input.LA(1);
			if (_la==K_WHERE) {
				{
				setState(199);
				match(K_WHERE);
				setState(200);
				column_name_in_dml();
				setState(201);
				match(EQ);
				setState(202);
				literal();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Eq_comparisonContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode EQ() { return getToken(FusionTablesSqlParser.EQ, 0); }
		public String_literalContext string_literal() {
			return getRuleContext(String_literalContext.class,0);
		}
		public Eq_comparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eq_comparison; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterEq_comparison(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitEq_comparison(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitEq_comparison(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Eq_comparisonContext eq_comparison() throws RecognitionException {
		Eq_comparisonContext _localctx = new Eq_comparisonContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_eq_comparison);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			identifier();
			setState(207);
			match(EQ);
			setState(208);
			string_literal();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Table_name_with_aliasContext extends ParserRuleContext {
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public TerminalNode K_AS() { return getToken(FusionTablesSqlParser.K_AS, 0); }
		public Table_aliasContext table_alias() {
			return getRuleContext(Table_aliasContext.class,0);
		}
		public Table_name_with_aliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_table_name_with_alias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterTable_name_with_alias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitTable_name_with_alias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitTable_name_with_alias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Table_name_with_aliasContext table_name_with_alias() throws RecognitionException {
		Table_name_with_aliasContext _localctx = new Table_name_with_aliasContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_table_name_with_alias);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			table_name();
			setState(213);
			_la = _input.LA(1);
			if (_la==K_AS) {
				{
				setState(211);
				match(K_AS);
				setState(212);
				table_alias();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Select_stmtContext extends ParserRuleContext {
		public TerminalNode K_SELECT() { return getToken(FusionTablesSqlParser.K_SELECT, 0); }
		public List<Result_columnContext> result_column() {
			return getRuleContexts(Result_columnContext.class);
		}
		public Result_columnContext result_column(int i) {
			return getRuleContext(Result_columnContext.class,i);
		}
		public TerminalNode K_FROM() { return getToken(FusionTablesSqlParser.K_FROM, 0); }
		public Table_name_with_aliasContext table_name_with_alias() {
			return getRuleContext(Table_name_with_aliasContext.class,0);
		}
		public List<Join_clauseContext> join_clause() {
			return getRuleContexts(Join_clauseContext.class);
		}
		public Join_clauseContext join_clause(int i) {
			return getRuleContext(Join_clauseContext.class,i);
		}
		public TerminalNode K_WHERE() { return getToken(FusionTablesSqlParser.K_WHERE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode K_GROUP() { return getToken(FusionTablesSqlParser.K_GROUP, 0); }
		public List<TerminalNode> K_BY() { return getTokens(FusionTablesSqlParser.K_BY); }
		public TerminalNode K_BY(int i) {
			return getToken(FusionTablesSqlParser.K_BY, i);
		}
		public List<Qualified_column_nameContext> qualified_column_name() {
			return getRuleContexts(Qualified_column_nameContext.class);
		}
		public Qualified_column_nameContext qualified_column_name(int i) {
			return getRuleContext(Qualified_column_nameContext.class,i);
		}
		public TerminalNode K_ORDER() { return getToken(FusionTablesSqlParser.K_ORDER, 0); }
		public List<Ordering_termContext> ordering_term() {
			return getRuleContexts(Ordering_termContext.class);
		}
		public Ordering_termContext ordering_term(int i) {
			return getRuleContext(Ordering_termContext.class,i);
		}
		public TerminalNode K_OFFSET() { return getToken(FusionTablesSqlParser.K_OFFSET, 0); }
		public List<Numeric_literalContext> numeric_literal() {
			return getRuleContexts(Numeric_literalContext.class);
		}
		public Numeric_literalContext numeric_literal(int i) {
			return getRuleContext(Numeric_literalContext.class,i);
		}
		public TerminalNode K_LIMIT() { return getToken(FusionTablesSqlParser.K_LIMIT, 0); }
		public Select_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_select_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterSelect_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitSelect_stmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitSelect_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Select_stmtContext select_stmt() throws RecognitionException {
		Select_stmtContext _localctx = new Select_stmtContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_select_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(215);
			match(K_SELECT);
			setState(216);
			result_column();
			setState(221);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(217);
				match(T__1);
				setState(218);
				result_column();
				}
				}
				setState(223);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(224);
			match(K_FROM);
			setState(225);
			table_name_with_alias();
			setState(229);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==K_LEFT) {
				{
				{
				setState(226);
				join_clause();
				}
				}
				setState(231);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(234);
			_la = _input.LA(1);
			if (_la==K_WHERE) {
				{
				setState(232);
				match(K_WHERE);
				setState(233);
				expr();
				}
			}

			setState(246);
			_la = _input.LA(1);
			if (_la==K_GROUP) {
				{
				setState(236);
				match(K_GROUP);
				setState(237);
				match(K_BY);
				setState(238);
				qualified_column_name();
				setState(243);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(239);
					match(T__1);
					setState(240);
					qualified_column_name();
					}
					}
					setState(245);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(258);
			_la = _input.LA(1);
			if (_la==K_ORDER) {
				{
				setState(248);
				match(K_ORDER);
				setState(249);
				match(K_BY);
				setState(250);
				ordering_term();
				setState(255);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(251);
					match(T__1);
					setState(252);
					ordering_term();
					}
					}
					setState(257);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(266);
			_la = _input.LA(1);
			if (_la==K_OFFSET) {
				{
				{
				setState(260);
				match(K_OFFSET);
				setState(261);
				numeric_literal();
				setState(264);
				_la = _input.LA(1);
				if (_la==K_LIMIT) {
					{
					setState(262);
					match(K_LIMIT);
					setState(263);
					numeric_literal();
					}
				}

				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Ordering_termContext extends ParserRuleContext {
		public Qualified_column_nameContext qualified_column_name() {
			return getRuleContext(Qualified_column_nameContext.class,0);
		}
		public TerminalNode K_ST_DISTANCE() { return getToken(FusionTablesSqlParser.K_ST_DISTANCE, 0); }
		public CoordinateContext coordinate() {
			return getRuleContext(CoordinateContext.class,0);
		}
		public TerminalNode K_ASC() { return getToken(FusionTablesSqlParser.K_ASC, 0); }
		public TerminalNode K_DESC() { return getToken(FusionTablesSqlParser.K_DESC, 0); }
		public Ordering_termContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ordering_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterOrdering_term(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitOrdering_term(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitOrdering_term(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Ordering_termContext ordering_term() throws RecognitionException {
		Ordering_termContext _localctx = new Ordering_termContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_ordering_term);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(276);
			switch (_input.LA(1)) {
			case STRING_LITERAL:
				{
				setState(268);
				qualified_column_name();
				}
				break;
			case K_ST_DISTANCE:
				{
				setState(269);
				match(K_ST_DISTANCE);
				setState(270);
				match(LPAR);
				setState(271);
				qualified_column_name();
				setState(272);
				match(T__1);
				setState(273);
				coordinate();
				setState(274);
				match(RPAR);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(279);
			_la = _input.LA(1);
			if (_la==K_ASC || _la==K_DESC) {
				{
				setState(278);
				_la = _input.LA(1);
				if ( !(_la==K_ASC || _la==K_DESC) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Join_clauseContext extends ParserRuleContext {
		public TerminalNode K_LEFT() { return getToken(FusionTablesSqlParser.K_LEFT, 0); }
		public TerminalNode K_OUTER() { return getToken(FusionTablesSqlParser.K_OUTER, 0); }
		public TerminalNode K_JOIN() { return getToken(FusionTablesSqlParser.K_JOIN, 0); }
		public Table_name_with_aliasContext table_name_with_alias() {
			return getRuleContext(Table_name_with_aliasContext.class,0);
		}
		public TerminalNode K_ON() { return getToken(FusionTablesSqlParser.K_ON, 0); }
		public List<Qualified_column_nameContext> qualified_column_name() {
			return getRuleContexts(Qualified_column_nameContext.class);
		}
		public Qualified_column_nameContext qualified_column_name(int i) {
			return getRuleContext(Qualified_column_nameContext.class,i);
		}
		public Join_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_join_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterJoin_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitJoin_clause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitJoin_clause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Join_clauseContext join_clause() throws RecognitionException {
		Join_clauseContext _localctx = new Join_clauseContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_join_clause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(281);
			match(K_LEFT);
			setState(282);
			match(K_OUTER);
			setState(283);
			match(K_JOIN);
			setState(284);
			table_name_with_alias();
			setState(285);
			match(K_ON);
			setState(286);
			qualified_column_name();
			setState(287);
			match(EQ);
			setState(288);
			qualified_column_name();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Result_columnContext extends ParserRuleContext {
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public Qualified_column_nameContext qualified_column_name() {
			return getRuleContext(Qualified_column_nameContext.class,0);
		}
		public Aggregate_expContext aggregate_exp() {
			return getRuleContext(Aggregate_expContext.class,0);
		}
		public Result_columnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_result_column; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterResult_column(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitResult_column(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitResult_column(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Result_columnContext result_column() throws RecognitionException {
		Result_columnContext _localctx = new Result_columnContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_result_column);
		try {
			setState(297);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(290);
				match(T__2);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(291);
				table_name();
				setState(292);
				match(T__3);
				setState(293);
				match(T__2);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(295);
				qualified_column_name();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(296);
				aggregate_exp();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Qualified_column_nameContext extends ParserRuleContext {
		public Column_nameContext column_name() {
			return getRuleContext(Column_nameContext.class,0);
		}
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public Qualified_column_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualified_column_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterQualified_column_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitQualified_column_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitQualified_column_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Qualified_column_nameContext qualified_column_name() throws RecognitionException {
		Qualified_column_nameContext _localctx = new Qualified_column_nameContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_qualified_column_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(299);
				table_name();
				setState(300);
				match(T__3);
				}
				break;
			}
			setState(304);
			column_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Aggregate_expContext extends ParserRuleContext {
		public TerminalNode LPAR() { return getToken(FusionTablesSqlParser.LPAR, 0); }
		public Qualified_column_nameContext qualified_column_name() {
			return getRuleContext(Qualified_column_nameContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(FusionTablesSqlParser.RPAR, 0); }
		public TerminalNode K_SUM() { return getToken(FusionTablesSqlParser.K_SUM, 0); }
		public TerminalNode K_COUNT() { return getToken(FusionTablesSqlParser.K_COUNT, 0); }
		public TerminalNode K_AVERAGE() { return getToken(FusionTablesSqlParser.K_AVERAGE, 0); }
		public TerminalNode K_MAXIMUM() { return getToken(FusionTablesSqlParser.K_MAXIMUM, 0); }
		public TerminalNode K_MINIMUM() { return getToken(FusionTablesSqlParser.K_MINIMUM, 0); }
		public Aggregate_expContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aggregate_exp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterAggregate_exp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitAggregate_exp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitAggregate_exp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Aggregate_expContext aggregate_exp() throws RecognitionException {
		Aggregate_expContext _localctx = new Aggregate_expContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_aggregate_exp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(306);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << K_AVERAGE) | (1L << K_COUNT) | (1L << K_MAXIMUM) | (1L << K_MINIMUM) | (1L << K_SUM))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(307);
			match(LPAR);
			setState(308);
			qualified_column_name();
			setState(309);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public Column_name_beginning_exprContext column_name_beginning_expr() {
			return getRuleContext(Column_name_beginning_exprContext.class,0);
		}
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public And_or_orContext and_or_or() {
			return getRuleContext(And_or_orContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<String_literalContext> string_literal() {
			return getRuleContexts(String_literalContext.class);
		}
		public String_literalContext string_literal(int i) {
			return getRuleContext(String_literalContext.class,i);
		}
		public TerminalNode K_LIKE() { return getToken(FusionTablesSqlParser.K_LIKE, 0); }
		public TerminalNode K_MATCHES() { return getToken(FusionTablesSqlParser.K_MATCHES, 0); }
		public TerminalNode K_STARTS() { return getToken(FusionTablesSqlParser.K_STARTS, 0); }
		public TerminalNode K_WITH() { return getToken(FusionTablesSqlParser.K_WITH, 0); }
		public TerminalNode K_ENDS() { return getToken(FusionTablesSqlParser.K_ENDS, 0); }
		public TerminalNode K_CONTAINS() { return getToken(FusionTablesSqlParser.K_CONTAINS, 0); }
		public TerminalNode K_IGNORING() { return getToken(FusionTablesSqlParser.K_IGNORING, 0); }
		public TerminalNode K_CASE() { return getToken(FusionTablesSqlParser.K_CASE, 0); }
		public TerminalNode K_DOES() { return getToken(FusionTablesSqlParser.K_DOES, 0); }
		public TerminalNode K_NOT() { return getToken(FusionTablesSqlParser.K_NOT, 0); }
		public TerminalNode K_CONTAIN() { return getToken(FusionTablesSqlParser.K_CONTAIN, 0); }
		public TerminalNode K_EQUAL() { return getToken(FusionTablesSqlParser.K_EQUAL, 0); }
		public TerminalNode K_TO() { return getToken(FusionTablesSqlParser.K_TO, 0); }
		public TerminalNode K_IN() { return getToken(FusionTablesSqlParser.K_IN, 0); }
		public TerminalNode K_BETWEEN() { return getToken(FusionTablesSqlParser.K_BETWEEN, 0); }
		public TerminalNode K_AND() { return getToken(FusionTablesSqlParser.K_AND, 0); }
		public TerminalNode K_ST_INTERSECTS() { return getToken(FusionTablesSqlParser.K_ST_INTERSECTS, 0); }
		public TerminalNode LPAR() { return getToken(FusionTablesSqlParser.LPAR, 0); }
		public Qualified_column_nameContext qualified_column_name() {
			return getRuleContext(Qualified_column_nameContext.class,0);
		}
		public GeometryContext geometry() {
			return getRuleContext(GeometryContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(FusionTablesSqlParser.RPAR, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_expr);
		int _la;
		try {
			setState(382);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(311);
				column_name_beginning_expr();
				{
				setState(312);
				operator();
				}
				setState(313);
				literal();
				setState(317);
				_la = _input.LA(1);
				if (_la==K_AND || _la==K_OR) {
					{
					setState(314);
					and_or_or();
					setState(315);
					expr();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(319);
				column_name_beginning_expr();
				setState(336);
				switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
				case 1:
					{
					setState(320);
					match(K_LIKE);
					}
					break;
				case 2:
					{
					setState(321);
					match(K_MATCHES);
					}
					break;
				case 3:
					{
					setState(322);
					match(K_STARTS);
					setState(323);
					match(K_WITH);
					}
					break;
				case 4:
					{
					setState(324);
					match(K_ENDS);
					setState(325);
					match(K_WITH);
					}
					break;
				case 5:
					{
					setState(326);
					match(K_CONTAINS);
					}
					break;
				case 6:
					{
					setState(327);
					match(K_CONTAINS);
					setState(328);
					match(K_IGNORING);
					setState(329);
					match(K_CASE);
					}
					break;
				case 7:
					{
					setState(330);
					match(K_DOES);
					setState(331);
					match(K_NOT);
					setState(332);
					match(K_CONTAIN);
					}
					break;
				case 8:
					{
					setState(333);
					match(K_NOT);
					setState(334);
					match(K_EQUAL);
					setState(335);
					match(K_TO);
					}
					break;
				}
				setState(338);
				string_literal();
				setState(342);
				_la = _input.LA(1);
				if (_la==K_AND || _la==K_OR) {
					{
					setState(339);
					and_or_or();
					setState(340);
					expr();
					}
				}

				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(344);
				column_name_beginning_expr();
				setState(345);
				match(K_IN);
				setState(346);
				match(LPAR);
				setState(347);
				string_literal();
				setState(352);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__1) {
					{
					{
					setState(348);
					match(T__1);
					setState(349);
					string_literal();
					}
					}
					setState(354);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(355);
				match(RPAR);
				setState(359);
				_la = _input.LA(1);
				if (_la==K_AND || _la==K_OR) {
					{
					setState(356);
					and_or_or();
					setState(357);
					expr();
					}
				}

				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(361);
				column_name_beginning_expr();
				setState(362);
				match(K_BETWEEN);
				setState(363);
				literal();
				setState(364);
				match(K_AND);
				setState(365);
				literal();
				setState(369);
				_la = _input.LA(1);
				if (_la==K_AND || _la==K_OR) {
					{
					setState(366);
					and_or_or();
					setState(367);
					expr();
					}
				}

				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(371);
				match(K_ST_INTERSECTS);
				setState(372);
				match(LPAR);
				setState(373);
				qualified_column_name();
				setState(374);
				match(T__1);
				setState(375);
				geometry();
				setState(376);
				match(RPAR);
				setState(380);
				_la = _input.LA(1);
				if (_la==K_AND || _la==K_OR) {
					{
					setState(377);
					and_or_or();
					setState(378);
					expr();
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Column_name_beginning_exprContext extends ParserRuleContext {
		public Qualified_column_nameContext qualified_column_name() {
			return getRuleContext(Qualified_column_nameContext.class,0);
		}
		public Column_name_beginning_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column_name_beginning_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterColumn_name_beginning_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitColumn_name_beginning_expr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitColumn_name_beginning_expr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Column_name_beginning_exprContext column_name_beginning_expr() throws RecognitionException {
		Column_name_beginning_exprContext _localctx = new Column_name_beginning_exprContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_column_name_beginning_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(384);
			qualified_column_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Column_name_in_dmlContext extends ParserRuleContext {
		public Column_nameContext column_name() {
			return getRuleContext(Column_nameContext.class,0);
		}
		public Column_name_in_dmlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column_name_in_dml; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterColumn_name_in_dml(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitColumn_name_in_dml(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitColumn_name_in_dml(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Column_name_in_dmlContext column_name_in_dml() throws RecognitionException {
		Column_name_in_dmlContext _localctx = new Column_name_in_dmlContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_column_name_in_dml);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(386);
			column_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class And_or_orContext extends ParserRuleContext {
		public TerminalNode K_AND() { return getToken(FusionTablesSqlParser.K_AND, 0); }
		public TerminalNode K_OR() { return getToken(FusionTablesSqlParser.K_OR, 0); }
		public And_or_orContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_and_or_or; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterAnd_or_or(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitAnd_or_or(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitAnd_or_or(this);
			else return visitor.visitChildren(this);
		}
	}

	public final And_or_orContext and_or_or() throws RecognitionException {
		And_or_orContext _localctx = new And_or_orContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_and_or_or);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(388);
			_la = _input.LA(1);
			if ( !(_la==K_AND || _la==K_OR) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GeometryContext extends ParserRuleContext {
		public CircleContext circle() {
			return getRuleContext(CircleContext.class,0);
		}
		public RectangleContext rectangle() {
			return getRuleContext(RectangleContext.class,0);
		}
		public GeometryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_geometry; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterGeometry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitGeometry(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitGeometry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GeometryContext geometry() throws RecognitionException {
		GeometryContext _localctx = new GeometryContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_geometry);
		try {
			setState(392);
			switch (_input.LA(1)) {
			case K_CIRCLE:
				enterOuterAlt(_localctx, 1);
				{
				setState(390);
				circle();
				}
				break;
			case K_RECTANGLE:
				enterOuterAlt(_localctx, 2);
				{
				setState(391);
				rectangle();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CircleContext extends ParserRuleContext {
		public TerminalNode K_CIRCLE() { return getToken(FusionTablesSqlParser.K_CIRCLE, 0); }
		public CoordinateContext coordinate() {
			return getRuleContext(CoordinateContext.class,0);
		}
		public Numeric_literalContext numeric_literal() {
			return getRuleContext(Numeric_literalContext.class,0);
		}
		public CircleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_circle; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterCircle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitCircle(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitCircle(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CircleContext circle() throws RecognitionException {
		CircleContext _localctx = new CircleContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_circle);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(394);
			match(K_CIRCLE);
			setState(395);
			match(LPAR);
			setState(396);
			coordinate();
			setState(397);
			match(T__1);
			setState(398);
			numeric_literal();
			setState(399);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RectangleContext extends ParserRuleContext {
		public TerminalNode K_RECTANGLE() { return getToken(FusionTablesSqlParser.K_RECTANGLE, 0); }
		public List<CoordinateContext> coordinate() {
			return getRuleContexts(CoordinateContext.class);
		}
		public CoordinateContext coordinate(int i) {
			return getRuleContext(CoordinateContext.class,i);
		}
		public RectangleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rectangle; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterRectangle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitRectangle(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitRectangle(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RectangleContext rectangle() throws RecognitionException {
		RectangleContext _localctx = new RectangleContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_rectangle);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
			match(K_RECTANGLE);
			setState(402);
			match(LPAR);
			setState(403);
			coordinate();
			setState(404);
			match(T__1);
			setState(405);
			coordinate();
			setState(406);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CoordinateContext extends ParserRuleContext {
		public TerminalNode K_LATLNG() { return getToken(FusionTablesSqlParser.K_LATLNG, 0); }
		public List<Numeric_literalContext> numeric_literal() {
			return getRuleContexts(Numeric_literalContext.class);
		}
		public Numeric_literalContext numeric_literal(int i) {
			return getRuleContext(Numeric_literalContext.class,i);
		}
		public CoordinateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_coordinate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterCoordinate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitCoordinate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitCoordinate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CoordinateContext coordinate() throws RecognitionException {
		CoordinateContext _localctx = new CoordinateContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_coordinate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(408);
			match(K_LATLNG);
			setState(409);
			match(LPAR);
			setState(410);
			numeric_literal();
			setState(411);
			match(T__1);
			setState(412);
			numeric_literal();
			setState(413);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeywordContext extends ParserRuleContext {
		public TerminalNode K_ALTER() { return getToken(FusionTablesSqlParser.K_ALTER, 0); }
		public TerminalNode K_AND() { return getToken(FusionTablesSqlParser.K_AND, 0); }
		public TerminalNode K_OR() { return getToken(FusionTablesSqlParser.K_OR, 0); }
		public TerminalNode K_AS() { return getToken(FusionTablesSqlParser.K_AS, 0); }
		public TerminalNode K_ASC() { return getToken(FusionTablesSqlParser.K_ASC, 0); }
		public TerminalNode K_AVERAGE() { return getToken(FusionTablesSqlParser.K_AVERAGE, 0); }
		public TerminalNode K_BY() { return getToken(FusionTablesSqlParser.K_BY, 0); }
		public TerminalNode K_BETWEEN() { return getToken(FusionTablesSqlParser.K_BETWEEN, 0); }
		public TerminalNode K_CASE() { return getToken(FusionTablesSqlParser.K_CASE, 0); }
		public TerminalNode K_CIRCLE() { return getToken(FusionTablesSqlParser.K_CIRCLE, 0); }
		public TerminalNode K_COLUMN() { return getToken(FusionTablesSqlParser.K_COLUMN, 0); }
		public TerminalNode K_CONTAIN() { return getToken(FusionTablesSqlParser.K_CONTAIN, 0); }
		public TerminalNode K_CONTAINS() { return getToken(FusionTablesSqlParser.K_CONTAINS, 0); }
		public TerminalNode K_COUNT() { return getToken(FusionTablesSqlParser.K_COUNT, 0); }
		public TerminalNode K_CREATE() { return getToken(FusionTablesSqlParser.K_CREATE, 0); }
		public TerminalNode K_DELETE() { return getToken(FusionTablesSqlParser.K_DELETE, 0); }
		public TerminalNode K_DESC() { return getToken(FusionTablesSqlParser.K_DESC, 0); }
		public TerminalNode K_DOES() { return getToken(FusionTablesSqlParser.K_DOES, 0); }
		public TerminalNode K_DROP() { return getToken(FusionTablesSqlParser.K_DROP, 0); }
		public TerminalNode K_ENDS() { return getToken(FusionTablesSqlParser.K_ENDS, 0); }
		public TerminalNode K_EQUAL() { return getToken(FusionTablesSqlParser.K_EQUAL, 0); }
		public TerminalNode K_FROM() { return getToken(FusionTablesSqlParser.K_FROM, 0); }
		public TerminalNode K_GROUP() { return getToken(FusionTablesSqlParser.K_GROUP, 0); }
		public TerminalNode K_HAVING() { return getToken(FusionTablesSqlParser.K_HAVING, 0); }
		public TerminalNode K_IGNORING() { return getToken(FusionTablesSqlParser.K_IGNORING, 0); }
		public TerminalNode K_IN() { return getToken(FusionTablesSqlParser.K_IN, 0); }
		public TerminalNode K_INSERT() { return getToken(FusionTablesSqlParser.K_INSERT, 0); }
		public TerminalNode K_INTO() { return getToken(FusionTablesSqlParser.K_INTO, 0); }
		public TerminalNode K_JOIN() { return getToken(FusionTablesSqlParser.K_JOIN, 0); }
		public TerminalNode K_LATLNG() { return getToken(FusionTablesSqlParser.K_LATLNG, 0); }
		public TerminalNode K_LEFT() { return getToken(FusionTablesSqlParser.K_LEFT, 0); }
		public TerminalNode K_LIKE() { return getToken(FusionTablesSqlParser.K_LIKE, 0); }
		public TerminalNode K_LIMIT() { return getToken(FusionTablesSqlParser.K_LIMIT, 0); }
		public TerminalNode K_MATCHES() { return getToken(FusionTablesSqlParser.K_MATCHES, 0); }
		public TerminalNode K_MAXIMUM() { return getToken(FusionTablesSqlParser.K_MAXIMUM, 0); }
		public TerminalNode K_MINIMUM() { return getToken(FusionTablesSqlParser.K_MINIMUM, 0); }
		public TerminalNode K_NOT() { return getToken(FusionTablesSqlParser.K_NOT, 0); }
		public TerminalNode K_OF() { return getToken(FusionTablesSqlParser.K_OF, 0); }
		public TerminalNode K_OFFSET() { return getToken(FusionTablesSqlParser.K_OFFSET, 0); }
		public TerminalNode K_ON() { return getToken(FusionTablesSqlParser.K_ON, 0); }
		public TerminalNode K_ORDER() { return getToken(FusionTablesSqlParser.K_ORDER, 0); }
		public TerminalNode K_OUTER() { return getToken(FusionTablesSqlParser.K_OUTER, 0); }
		public TerminalNode K_RECTANGLE() { return getToken(FusionTablesSqlParser.K_RECTANGLE, 0); }
		public TerminalNode K_RENAME() { return getToken(FusionTablesSqlParser.K_RENAME, 0); }
		public TerminalNode K_SELECT() { return getToken(FusionTablesSqlParser.K_SELECT, 0); }
		public TerminalNode K_SET() { return getToken(FusionTablesSqlParser.K_SET, 0); }
		public TerminalNode K_STARTS() { return getToken(FusionTablesSqlParser.K_STARTS, 0); }
		public TerminalNode K_ST_DISTANCE() { return getToken(FusionTablesSqlParser.K_ST_DISTANCE, 0); }
		public TerminalNode K_ST_INTERSECTS() { return getToken(FusionTablesSqlParser.K_ST_INTERSECTS, 0); }
		public TerminalNode K_SUM() { return getToken(FusionTablesSqlParser.K_SUM, 0); }
		public TerminalNode K_TABLE() { return getToken(FusionTablesSqlParser.K_TABLE, 0); }
		public TerminalNode K_TO() { return getToken(FusionTablesSqlParser.K_TO, 0); }
		public TerminalNode K_UPDATE() { return getToken(FusionTablesSqlParser.K_UPDATE, 0); }
		public TerminalNode K_VALUES() { return getToken(FusionTablesSqlParser.K_VALUES, 0); }
		public TerminalNode K_VIEW() { return getToken(FusionTablesSqlParser.K_VIEW, 0); }
		public TerminalNode K_WHERE() { return getToken(FusionTablesSqlParser.K_WHERE, 0); }
		public TerminalNode K_WITH() { return getToken(FusionTablesSqlParser.K_WITH, 0); }
		public KeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyword; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterKeyword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitKeyword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitKeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeywordContext keyword() throws RecognitionException {
		KeywordContext _localctx = new KeywordContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_keyword);
		try {
			setState(473);
			switch (_input.LA(1)) {
			case EOF:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;
			case K_ALTER:
				enterOuterAlt(_localctx, 2);
				{
				setState(416);
				match(K_ALTER);
				}
				break;
			case K_AND:
				enterOuterAlt(_localctx, 3);
				{
				setState(417);
				match(K_AND);
				}
				break;
			case K_OR:
				enterOuterAlt(_localctx, 4);
				{
				setState(418);
				match(K_OR);
				}
				break;
			case K_AS:
				enterOuterAlt(_localctx, 5);
				{
				setState(419);
				match(K_AS);
				}
				break;
			case K_ASC:
				enterOuterAlt(_localctx, 6);
				{
				setState(420);
				match(K_ASC);
				}
				break;
			case K_AVERAGE:
				enterOuterAlt(_localctx, 7);
				{
				setState(421);
				match(K_AVERAGE);
				}
				break;
			case K_BY:
				enterOuterAlt(_localctx, 8);
				{
				setState(422);
				match(K_BY);
				}
				break;
			case K_BETWEEN:
				enterOuterAlt(_localctx, 9);
				{
				setState(423);
				match(K_BETWEEN);
				}
				break;
			case K_CASE:
				enterOuterAlt(_localctx, 10);
				{
				setState(424);
				match(K_CASE);
				}
				break;
			case K_CIRCLE:
				enterOuterAlt(_localctx, 11);
				{
				setState(425);
				match(K_CIRCLE);
				}
				break;
			case K_COLUMN:
				enterOuterAlt(_localctx, 12);
				{
				setState(426);
				match(K_COLUMN);
				}
				break;
			case K_CONTAIN:
				enterOuterAlt(_localctx, 13);
				{
				setState(427);
				match(K_CONTAIN);
				}
				break;
			case K_CONTAINS:
				enterOuterAlt(_localctx, 14);
				{
				setState(428);
				match(K_CONTAINS);
				}
				break;
			case K_COUNT:
				enterOuterAlt(_localctx, 15);
				{
				setState(429);
				match(K_COUNT);
				}
				break;
			case K_CREATE:
				enterOuterAlt(_localctx, 16);
				{
				setState(430);
				match(K_CREATE);
				}
				break;
			case K_DELETE:
				enterOuterAlt(_localctx, 17);
				{
				setState(431);
				match(K_DELETE);
				}
				break;
			case K_DESC:
				enterOuterAlt(_localctx, 18);
				{
				setState(432);
				match(K_DESC);
				}
				break;
			case K_DOES:
				enterOuterAlt(_localctx, 19);
				{
				setState(433);
				match(K_DOES);
				}
				break;
			case K_DROP:
				enterOuterAlt(_localctx, 20);
				{
				setState(434);
				match(K_DROP);
				}
				break;
			case K_ENDS:
				enterOuterAlt(_localctx, 21);
				{
				setState(435);
				match(K_ENDS);
				}
				break;
			case K_EQUAL:
				enterOuterAlt(_localctx, 22);
				{
				setState(436);
				match(K_EQUAL);
				}
				break;
			case K_FROM:
				enterOuterAlt(_localctx, 23);
				{
				setState(437);
				match(K_FROM);
				}
				break;
			case K_GROUP:
				enterOuterAlt(_localctx, 24);
				{
				setState(438);
				match(K_GROUP);
				}
				break;
			case K_HAVING:
				enterOuterAlt(_localctx, 25);
				{
				setState(439);
				match(K_HAVING);
				}
				break;
			case K_IGNORING:
				enterOuterAlt(_localctx, 26);
				{
				setState(440);
				match(K_IGNORING);
				}
				break;
			case K_IN:
				enterOuterAlt(_localctx, 27);
				{
				setState(441);
				match(K_IN);
				}
				break;
			case K_INSERT:
				enterOuterAlt(_localctx, 28);
				{
				setState(442);
				match(K_INSERT);
				}
				break;
			case K_INTO:
				enterOuterAlt(_localctx, 29);
				{
				setState(443);
				match(K_INTO);
				}
				break;
			case K_JOIN:
				enterOuterAlt(_localctx, 30);
				{
				setState(444);
				match(K_JOIN);
				}
				break;
			case K_LATLNG:
				enterOuterAlt(_localctx, 31);
				{
				setState(445);
				match(K_LATLNG);
				}
				break;
			case K_LEFT:
				enterOuterAlt(_localctx, 32);
				{
				setState(446);
				match(K_LEFT);
				}
				break;
			case K_LIKE:
				enterOuterAlt(_localctx, 33);
				{
				setState(447);
				match(K_LIKE);
				}
				break;
			case K_LIMIT:
				enterOuterAlt(_localctx, 34);
				{
				setState(448);
				match(K_LIMIT);
				}
				break;
			case K_MATCHES:
				enterOuterAlt(_localctx, 35);
				{
				setState(449);
				match(K_MATCHES);
				}
				break;
			case K_MAXIMUM:
				enterOuterAlt(_localctx, 36);
				{
				setState(450);
				match(K_MAXIMUM);
				}
				break;
			case K_MINIMUM:
				enterOuterAlt(_localctx, 37);
				{
				setState(451);
				match(K_MINIMUM);
				}
				break;
			case K_NOT:
				enterOuterAlt(_localctx, 38);
				{
				setState(452);
				match(K_NOT);
				}
				break;
			case K_OF:
				enterOuterAlt(_localctx, 39);
				{
				setState(453);
				match(K_OF);
				}
				break;
			case K_OFFSET:
				enterOuterAlt(_localctx, 40);
				{
				setState(454);
				match(K_OFFSET);
				}
				break;
			case K_ON:
				enterOuterAlt(_localctx, 41);
				{
				setState(455);
				match(K_ON);
				}
				break;
			case K_ORDER:
				enterOuterAlt(_localctx, 42);
				{
				setState(456);
				match(K_ORDER);
				}
				break;
			case K_OUTER:
				enterOuterAlt(_localctx, 43);
				{
				setState(457);
				match(K_OUTER);
				}
				break;
			case K_RECTANGLE:
				enterOuterAlt(_localctx, 44);
				{
				setState(458);
				match(K_RECTANGLE);
				}
				break;
			case K_RENAME:
				enterOuterAlt(_localctx, 45);
				{
				setState(459);
				match(K_RENAME);
				}
				break;
			case K_SELECT:
				enterOuterAlt(_localctx, 46);
				{
				setState(460);
				match(K_SELECT);
				}
				break;
			case K_SET:
				enterOuterAlt(_localctx, 47);
				{
				setState(461);
				match(K_SET);
				}
				break;
			case K_STARTS:
				enterOuterAlt(_localctx, 48);
				{
				setState(462);
				match(K_STARTS);
				}
				break;
			case K_ST_DISTANCE:
				enterOuterAlt(_localctx, 49);
				{
				setState(463);
				match(K_ST_DISTANCE);
				}
				break;
			case K_ST_INTERSECTS:
				enterOuterAlt(_localctx, 50);
				{
				setState(464);
				match(K_ST_INTERSECTS);
				}
				break;
			case K_SUM:
				enterOuterAlt(_localctx, 51);
				{
				setState(465);
				match(K_SUM);
				}
				break;
			case K_TABLE:
				enterOuterAlt(_localctx, 52);
				{
				setState(466);
				match(K_TABLE);
				}
				break;
			case K_TO:
				enterOuterAlt(_localctx, 53);
				{
				setState(467);
				match(K_TO);
				}
				break;
			case K_UPDATE:
				enterOuterAlt(_localctx, 54);
				{
				setState(468);
				match(K_UPDATE);
				}
				break;
			case K_VALUES:
				enterOuterAlt(_localctx, 55);
				{
				setState(469);
				match(K_VALUES);
				}
				break;
			case K_VIEW:
				enterOuterAlt(_localctx, 56);
				{
				setState(470);
				match(K_VIEW);
				}
				break;
			case K_WHERE:
				enterOuterAlt(_localctx, 57);
				{
				setState(471);
				match(K_WHERE);
				}
				break;
			case K_WITH:
				enterOuterAlt(_localctx, 58);
				{
				setState(472);
				match(K_WITH);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperatorContext extends ParserRuleContext {
		public TerminalNode LT() { return getToken(FusionTablesSqlParser.LT, 0); }
		public TerminalNode LT_EQ() { return getToken(FusionTablesSqlParser.LT_EQ, 0); }
		public TerminalNode GT() { return getToken(FusionTablesSqlParser.GT, 0); }
		public TerminalNode GT_EQ() { return getToken(FusionTablesSqlParser.GT_EQ, 0); }
		public TerminalNode EQ() { return getToken(FusionTablesSqlParser.EQ, 0); }
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(475);
			_la = _input.LA(1);
			if ( !(((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (LT_EQ - 65)) | (1L << (GT_EQ - 65)) | (1L << (GT - 65)) | (1L << (EQ - 65)) | (1L << (LT - 65)))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public Numeric_literalContext numeric_literal() {
			return getRuleContext(Numeric_literalContext.class,0);
		}
		public String_literalContext string_literal() {
			return getRuleContext(String_literalContext.class,0);
		}
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_literal);
		try {
			setState(479);
			switch (_input.LA(1)) {
			case NUMERIC_LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(477);
				numeric_literal();
				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(478);
				string_literal();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Error_messageContext extends ParserRuleContext {
		public String_literalContext string_literal() {
			return getRuleContext(String_literalContext.class,0);
		}
		public Error_messageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_error_message; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterError_message(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitError_message(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitError_message(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Error_messageContext error_message() throws RecognitionException {
		Error_messageContext _localctx = new Error_messageContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_error_message);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(481);
			string_literal();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierContext extends ParserRuleContext {
		public String_literalContext string_literal() {
			return getRuleContext(String_literalContext.class,0);
		}
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(483);
			string_literal();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Column_aliasContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public Column_aliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column_alias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterColumn_alias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitColumn_alias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitColumn_alias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Column_aliasContext column_alias() throws RecognitionException {
		Column_aliasContext _localctx = new Column_aliasContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_column_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(485);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Table_nameContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public Table_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_table_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterTable_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitTable_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitTable_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Table_nameContext table_name() throws RecognitionException {
		Table_nameContext _localctx = new Table_nameContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_table_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(487);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Column_nameContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public Column_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_column_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterColumn_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitColumn_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitColumn_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Column_nameContext column_name() throws RecognitionException {
		Column_nameContext _localctx = new Column_nameContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_column_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(489);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class New_table_nameContext extends ParserRuleContext {
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public New_table_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_new_table_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterNew_table_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitNew_table_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitNew_table_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final New_table_nameContext new_table_name() throws RecognitionException {
		New_table_nameContext _localctx = new New_table_nameContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_new_table_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(491);
			table_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class View_nameContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public View_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_view_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterView_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitView_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitView_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final View_nameContext view_name() throws RecognitionException {
		View_nameContext _localctx = new View_nameContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_view_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(493);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Table_aliasContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public Table_aliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_table_alias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterTable_alias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitTable_alias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitTable_alias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Table_aliasContext table_alias() throws RecognitionException {
		Table_aliasContext _localctx = new Table_aliasContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_table_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(495);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Numeric_literalContext extends ParserRuleContext {
		public TerminalNode NUMERIC_LITERAL() { return getToken(FusionTablesSqlParser.NUMERIC_LITERAL, 0); }
		public Numeric_literalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numeric_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterNumeric_literal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitNumeric_literal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitNumeric_literal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Numeric_literalContext numeric_literal() throws RecognitionException {
		Numeric_literalContext _localctx = new Numeric_literalContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_numeric_literal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(497);
			match(NUMERIC_LITERAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class String_literalContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL() { return getToken(FusionTablesSqlParser.STRING_LITERAL, 0); }
		public String_literalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).enterString_literal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FusionTablesSqlListener ) ((FusionTablesSqlListener)listener).exitString_literal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FusionTablesSqlVisitor ) return ((FusionTablesSqlVisitor<? extends T>)visitor).visitString_literal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final String_literalContext string_literal() throws RecognitionException {
		String_literalContext _localctx = new String_literalContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_string_literal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(499);
			match(STRING_LITERAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3P\u01f8\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\3"+
		"\2\7\2X\n\2\f\2\16\2[\13\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3f\n"+
		"\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\7\t\u0084\n\t\f\t\16\t\u0087"+
		"\13\t\3\t\3\t\3\t\3\t\5\t\u008d\n\t\3\t\7\t\u0090\n\t\f\t\16\t\u0093\13"+
		"\t\3\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\7\13\u00a2"+
		"\n\13\f\13\16\13\u00a5\13\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\7\13\u00ae"+
		"\n\13\f\13\16\13\u00b1\13\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\7\f\u00bb"+
		"\n\f\f\f\16\f\u00be\13\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\5\16\u00cf\n\16\3\17\3\17\3\17\3\17\3\20\3\20"+
		"\3\20\5\20\u00d8\n\20\3\21\3\21\3\21\3\21\7\21\u00de\n\21\f\21\16\21\u00e1"+
		"\13\21\3\21\3\21\3\21\7\21\u00e6\n\21\f\21\16\21\u00e9\13\21\3\21\3\21"+
		"\5\21\u00ed\n\21\3\21\3\21\3\21\3\21\3\21\7\21\u00f4\n\21\f\21\16\21\u00f7"+
		"\13\21\5\21\u00f9\n\21\3\21\3\21\3\21\3\21\3\21\7\21\u0100\n\21\f\21\16"+
		"\21\u0103\13\21\5\21\u0105\n\21\3\21\3\21\3\21\3\21\5\21\u010b\n\21\5"+
		"\21\u010d\n\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u0117\n\22"+
		"\3\22\5\22\u011a\n\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u012c\n\24\3\25\3\25\3\25\5\25\u0131"+
		"\n\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\5\27\u0140\n\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u0153\n\27\3\27\3\27\3\27\3\27\5\27"+
		"\u0159\n\27\3\27\3\27\3\27\3\27\3\27\3\27\7\27\u0161\n\27\f\27\16\27\u0164"+
		"\13\27\3\27\3\27\3\27\3\27\5\27\u016a\n\27\3\27\3\27\3\27\3\27\3\27\3"+
		"\27\3\27\3\27\5\27\u0174\n\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\5\27\u017f\n\27\5\27\u0181\n\27\3\30\3\30\3\31\3\31\3\32\3\32\3"+
		"\33\3\33\5\33\u018b\n\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\5\37\u01dc\n\37\3 \3 \3!\3!\5!\u01e2\n!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&"+
		"\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3+\3+\2\2,\2\4\6\b\n\f\16\20\22\24\26"+
		"\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRT\2\6\4\2\13\13\26\26\6"+
		"\2\f\f\23\23)*\67\67\3\2\b\t\3\2CG\u0239\2Y\3\2\2\2\4e\3\2\2\2\6i\3\2"+
		"\2\2\bk\3\2\2\2\nm\3\2\2\2\fp\3\2\2\2\16s\3\2\2\2\20z\3\2\2\2\22\u0096"+
		"\3\2\2\2\24\u009a\3\2\2\2\26\u00b4\3\2\2\2\30\u00c2\3\2\2\2\32\u00c6\3"+
		"\2\2\2\34\u00d0\3\2\2\2\36\u00d4\3\2\2\2 \u00d9\3\2\2\2\"\u0116\3\2\2"+
		"\2$\u011b\3\2\2\2&\u012b\3\2\2\2(\u0130\3\2\2\2*\u0134\3\2\2\2,\u0180"+
		"\3\2\2\2.\u0182\3\2\2\2\60\u0184\3\2\2\2\62\u0186\3\2\2\2\64\u018a\3\2"+
		"\2\2\66\u018c\3\2\2\28\u0193\3\2\2\2:\u019a\3\2\2\2<\u01db\3\2\2\2>\u01dd"+
		"\3\2\2\2@\u01e1\3\2\2\2B\u01e3\3\2\2\2D\u01e5\3\2\2\2F\u01e7\3\2\2\2H"+
		"\u01e9\3\2\2\2J\u01eb\3\2\2\2L\u01ed\3\2\2\2N\u01ef\3\2\2\2P\u01f1\3\2"+
		"\2\2R\u01f3\3\2\2\2T\u01f5\3\2\2\2VX\5\4\3\2WV\3\2\2\2X[\3\2\2\2YW\3\2"+
		"\2\2YZ\3\2\2\2Z\3\3\2\2\2[Y\3\2\2\2\\f\5\16\b\2]f\5 \21\2^f\5\20\t\2_"+
		"f\5\32\16\2`f\5\22\n\2af\5\24\13\2bf\5\26\f\2cf\5\n\6\2df\5\f\7\2e\\\3"+
		"\2\2\2e]\3\2\2\2e^\3\2\2\2e_\3\2\2\2e`\3\2\2\2ea\3\2\2\2eb\3\2\2\2ec\3"+
		"\2\2\2ed\3\2\2\2fg\3\2\2\2gh\7\3\2\2h\5\3\2\2\2ij\5H%\2j\7\3\2\2\2kl\5"+
		"H%\2l\t\3\2\2\2mn\7\27\2\2no\5\6\4\2o\13\3\2\2\2pq\79\2\2qr\7<\2\2r\r"+
		"\3\2\2\2st\7\7\2\2tu\7;\2\2uv\5\6\4\2vw\7\63\2\2wx\7=\2\2xy\5\6\4\2y\17"+
		"\3\2\2\2z{\7\24\2\2{|\7@\2\2|}\5N(\2}~\7\n\2\2~\177\7H\2\2\177\u0080\7"+
		"\65\2\2\u0080\u0085\5&\24\2\u0081\u0082\7\4\2\2\u0082\u0084\5&\24\2\u0083"+
		"\u0081\3\2\2\2\u0084\u0087\3\2\2\2\u0085\u0083\3\2\2\2\u0085\u0086\3\2"+
		"\2\2\u0086\u0088\3\2\2\2\u0087\u0085\3\2\2\2\u0088\u0089\7\34\2\2\u0089"+
		"\u008c\5\36\20\2\u008a\u008b\7A\2\2\u008b\u008d\5,\27\2\u008c\u008a\3"+
		"\2\2\2\u008c\u008d\3\2\2\2\u008d\u0091\3\2\2\2\u008e\u0090\5$\23\2\u008f"+
		"\u008e\3\2\2\2\u0090\u0093\3\2\2\2\u0091\u008f\3\2\2\2\u0091\u0092\3\2"+
		"\2\2\u0092\u0094\3\2\2\2\u0093\u0091\3\2\2\2\u0094\u0095\7I\2\2\u0095"+
		"\21\3\2\2\2\u0096\u0097\7\32\2\2\u0097\u0098\7;\2\2\u0098\u0099\5\6\4"+
		"\2\u0099\23\3\2\2\2\u009a\u009b\7!\2\2\u009b\u009c\7\"\2\2\u009c\u009d"+
		"\5\b\5\2\u009d\u009e\7H\2\2\u009e\u00a3\5\60\31\2\u009f\u00a0\7\4\2\2"+
		"\u00a0\u00a2\5\60\31\2\u00a1\u009f\3\2\2\2\u00a2\u00a5\3\2\2\2\u00a3\u00a1"+
		"\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a6\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a6"+
		"\u00a7\7I\2\2\u00a7\u00a8\3\2\2\2\u00a8\u00a9\7?\2\2\u00a9\u00aa\7H\2"+
		"\2\u00aa\u00af\5@!\2\u00ab\u00ac\7\4\2\2\u00ac\u00ae\5@!\2\u00ad\u00ab"+
		"\3\2\2\2\u00ae\u00b1\3\2\2\2\u00af\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0"+
		"\u00b2\3\2\2\2\u00b1\u00af\3\2\2\2\u00b2\u00b3\7I\2\2\u00b3\25\3\2\2\2"+
		"\u00b4\u00b5\7>\2\2\u00b5\u00b6\5\b\5\2\u00b6\u00b7\78\2\2\u00b7\u00bc"+
		"\5\30\r\2\u00b8\u00b9\7\4\2\2\u00b9\u00bb\5\30\r\2\u00ba\u00b8\3\2\2\2"+
		"\u00bb\u00be\3\2\2\2\u00bc\u00ba\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd\u00bf"+
		"\3\2\2\2\u00be\u00bc\3\2\2\2\u00bf\u00c0\7A\2\2\u00c0\u00c1\5\34\17\2"+
		"\u00c1\27\3\2\2\2\u00c2\u00c3\5\60\31\2\u00c3\u00c4\7F\2\2\u00c4\u00c5"+
		"\5@!\2\u00c5\31\3\2\2\2\u00c6\u00c7\7\25\2\2\u00c7\u00c8\7\34\2\2\u00c8"+
		"\u00ce\5\b\5\2\u00c9\u00ca\7A\2\2\u00ca\u00cb\5\60\31\2\u00cb\u00cc\7"+
		"F\2\2\u00cc\u00cd\5@!\2\u00cd\u00cf\3\2\2\2\u00ce\u00c9\3\2\2\2\u00ce"+
		"\u00cf\3\2\2\2\u00cf\33\3\2\2\2\u00d0\u00d1\5D#\2\u00d1\u00d2\7F\2\2\u00d2"+
		"\u00d3\5T+\2\u00d3\35\3\2\2\2\u00d4\u00d7\5H%\2\u00d5\u00d6\7\n\2\2\u00d6"+
		"\u00d8\5P)\2\u00d7\u00d5\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8\37\3\2\2\2"+
		"\u00d9\u00da\7\65\2\2\u00da\u00df\5&\24\2\u00db\u00dc\7\4\2\2\u00dc\u00de"+
		"\5&\24\2\u00dd\u00db\3\2\2\2\u00de\u00e1\3\2\2\2\u00df\u00dd\3\2\2\2\u00df"+
		"\u00e0\3\2\2\2\u00e0\u00e2\3\2\2\2\u00e1\u00df\3\2\2\2\u00e2\u00e3\7\34"+
		"\2\2\u00e3\u00e7\5\36\20\2\u00e4\u00e6\5$\23\2\u00e5\u00e4\3\2\2\2\u00e6"+
		"\u00e9\3\2\2\2\u00e7\u00e5\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00ec\3\2"+
		"\2\2\u00e9\u00e7\3\2\2\2\u00ea\u00eb\7A\2\2\u00eb\u00ed\5,\27\2\u00ec"+
		"\u00ea\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed\u00f8\3\2\2\2\u00ee\u00ef\7\35"+
		"\2\2\u00ef\u00f0\7\r\2\2\u00f0\u00f5\5(\25\2\u00f1\u00f2\7\4\2\2\u00f2"+
		"\u00f4\5(\25\2\u00f3\u00f1\3\2\2\2\u00f4\u00f7\3\2\2\2\u00f5\u00f3\3\2"+
		"\2\2\u00f5\u00f6\3\2\2\2\u00f6\u00f9\3\2\2\2\u00f7\u00f5\3\2\2\2\u00f8"+
		"\u00ee\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9\u0104\3\2\2\2\u00fa\u00fb\7\60"+
		"\2\2\u00fb\u00fc\7\r\2\2\u00fc\u0101\5\"\22\2\u00fd\u00fe\7\4\2\2\u00fe"+
		"\u0100\5\"\22\2\u00ff\u00fd\3\2\2\2\u0100\u0103\3\2\2\2\u0101\u00ff\3"+
		"\2\2\2\u0101\u0102\3\2\2\2\u0102\u0105\3\2\2\2\u0103\u0101\3\2\2\2\u0104"+
		"\u00fa\3\2\2\2\u0104\u0105\3\2\2\2\u0105\u010c\3\2\2\2\u0106\u0107\7."+
		"\2\2\u0107\u010a\5R*\2\u0108\u0109\7\'\2\2\u0109\u010b\5R*\2\u010a\u0108"+
		"\3\2\2\2\u010a\u010b\3\2\2\2\u010b\u010d\3\2\2\2\u010c\u0106\3\2\2\2\u010c"+
		"\u010d\3\2\2\2\u010d!\3\2\2\2\u010e\u0117\5(\25\2\u010f\u0110\7\64\2\2"+
		"\u0110\u0111\7H\2\2\u0111\u0112\5(\25\2\u0112\u0113\7\4\2\2\u0113\u0114"+
		"\5:\36\2\u0114\u0115\7I\2\2\u0115\u0117\3\2\2\2\u0116\u010e\3\2\2\2\u0116"+
		"\u010f\3\2\2\2\u0117\u0119\3\2\2\2\u0118\u011a\t\2\2\2\u0119\u0118\3\2"+
		"\2\2\u0119\u011a\3\2\2\2\u011a#\3\2\2\2\u011b\u011c\7%\2\2\u011c\u011d"+
		"\7\61\2\2\u011d\u011e\7#\2\2\u011e\u011f\5\36\20\2\u011f\u0120\7/\2\2"+
		"\u0120\u0121\5(\25\2\u0121\u0122\7F\2\2\u0122\u0123\5(\25\2\u0123%\3\2"+
		"\2\2\u0124\u012c\7\5\2\2\u0125\u0126\5H%\2\u0126\u0127\7\6\2\2\u0127\u0128"+
		"\7\5\2\2\u0128\u012c\3\2\2\2\u0129\u012c\5(\25\2\u012a\u012c\5*\26\2\u012b"+
		"\u0124\3\2\2\2\u012b\u0125\3\2\2\2\u012b\u0129\3\2\2\2\u012b\u012a\3\2"+
		"\2\2\u012c\'\3\2\2\2\u012d\u012e\5H%\2\u012e\u012f\7\6\2\2\u012f\u0131"+
		"\3\2\2\2\u0130\u012d\3\2\2\2\u0130\u0131\3\2\2\2\u0131\u0132\3\2\2\2\u0132"+
		"\u0133\5J&\2\u0133)\3\2\2\2\u0134\u0135\t\3\2\2\u0135\u0136\7H\2\2\u0136"+
		"\u0137\5(\25\2\u0137\u0138\7I\2\2\u0138+\3\2\2\2\u0139\u013a\5.\30\2\u013a"+
		"\u013b\5> \2\u013b\u013f\5@!\2\u013c\u013d\5\62\32\2\u013d\u013e\5,\27"+
		"\2\u013e\u0140\3\2\2\2\u013f\u013c\3\2\2\2\u013f\u0140\3\2\2\2\u0140\u0181"+
		"\3\2\2\2\u0141\u0152\5.\30\2\u0142\u0153\7&\2\2\u0143\u0153\7(\2\2\u0144"+
		"\u0145\7:\2\2\u0145\u0153\7B\2\2\u0146\u0147\7\33\2\2\u0147\u0153\7B\2"+
		"\2\u0148\u0153\7\22\2\2\u0149\u014a\7\22\2\2\u014a\u014b\7\37\2\2\u014b"+
		"\u0153\7\17\2\2\u014c\u014d\7\30\2\2\u014d\u014e\7+\2\2\u014e\u0153\7"+
		"\31\2\2\u014f\u0150\7+\2\2\u0150\u0151\7,\2\2\u0151\u0153\7=\2\2\u0152"+
		"\u0142\3\2\2\2\u0152\u0143\3\2\2\2\u0152\u0144\3\2\2\2\u0152\u0146\3\2"+
		"\2\2\u0152\u0148\3\2\2\2\u0152\u0149\3\2\2\2\u0152\u014c\3\2\2\2\u0152"+
		"\u014f\3\2\2\2\u0153\u0154\3\2\2\2\u0154\u0158\5T+\2\u0155\u0156\5\62"+
		"\32\2\u0156\u0157\5,\27\2\u0157\u0159\3\2\2\2\u0158\u0155\3\2\2\2\u0158"+
		"\u0159\3\2\2\2\u0159\u0181\3\2\2\2\u015a\u015b\5.\30\2\u015b\u015c\7 "+
		"\2\2\u015c\u015d\7H\2\2\u015d\u0162\5T+\2\u015e\u015f\7\4\2\2\u015f\u0161"+
		"\5T+\2\u0160\u015e\3\2\2\2\u0161\u0164\3\2\2\2\u0162\u0160\3\2\2\2\u0162"+
		"\u0163\3\2\2\2\u0163\u0165\3\2\2\2\u0164\u0162\3\2\2\2\u0165\u0169\7I"+
		"\2\2\u0166\u0167\5\62\32\2\u0167\u0168\5,\27\2\u0168\u016a\3\2\2\2\u0169"+
		"\u0166\3\2\2\2\u0169\u016a\3\2\2\2\u016a\u0181\3\2\2\2\u016b\u016c\5."+
		"\30\2\u016c\u016d\7\16\2\2\u016d\u016e\5@!\2\u016e\u016f\7\b\2\2\u016f"+
		"\u0173\5@!\2\u0170\u0171\5\62\32\2\u0171\u0172\5,\27\2\u0172\u0174\3\2"+
		"\2\2\u0173\u0170\3\2\2\2\u0173\u0174\3\2\2\2\u0174\u0181\3\2\2\2\u0175"+
		"\u0176\7\66\2\2\u0176\u0177\7H\2\2\u0177\u0178\5(\25\2\u0178\u0179\7\4"+
		"\2\2\u0179\u017a\5\64\33\2\u017a\u017e\7I\2\2\u017b\u017c\5\62\32\2\u017c"+
		"\u017d\5,\27\2\u017d\u017f\3\2\2\2\u017e\u017b\3\2\2\2\u017e\u017f\3\2"+
		"\2\2\u017f\u0181\3\2\2\2\u0180\u0139\3\2\2\2\u0180\u0141\3\2\2\2\u0180"+
		"\u015a\3\2\2\2\u0180\u016b\3\2\2\2\u0180\u0175\3\2\2\2\u0181-\3\2\2\2"+
		"\u0182\u0183\5(\25\2\u0183/\3\2\2\2\u0184\u0185\5J&\2\u0185\61\3\2\2\2"+
		"\u0186\u0187\t\4\2\2\u0187\63\3\2\2\2\u0188\u018b\5\66\34\2\u0189\u018b"+
		"\58\35\2\u018a\u0188\3\2\2\2\u018a\u0189\3\2\2\2\u018b\65\3\2\2\2\u018c"+
		"\u018d\7\20\2\2\u018d\u018e\7H\2\2\u018e\u018f\5:\36\2\u018f\u0190\7\4"+
		"\2\2\u0190\u0191\5R*\2\u0191\u0192\7I\2\2\u0192\67\3\2\2\2\u0193\u0194"+
		"\7\62\2\2\u0194\u0195\7H\2\2\u0195\u0196\5:\36\2\u0196\u0197\7\4\2\2\u0197"+
		"\u0198\5:\36\2\u0198\u0199\7I\2\2\u01999\3\2\2\2\u019a\u019b\7$\2\2\u019b"+
		"\u019c\7H\2\2\u019c\u019d\5R*\2\u019d\u019e\7\4\2\2\u019e\u019f\5R*\2"+
		"\u019f\u01a0\7I\2\2\u01a0;\3\2\2\2\u01a1\u01dc\3\2\2\2\u01a2\u01dc\7\7"+
		"\2\2\u01a3\u01dc\7\b\2\2\u01a4\u01dc\7\t\2\2\u01a5\u01dc\7\n\2\2\u01a6"+
		"\u01dc\7\13\2\2\u01a7\u01dc\7\f\2\2\u01a8\u01dc\7\r\2\2\u01a9\u01dc\7"+
		"\16\2\2\u01aa\u01dc\7\17\2\2\u01ab\u01dc\7\20\2\2\u01ac\u01dc\7\21\2\2"+
		"\u01ad\u01dc\7\31\2\2\u01ae\u01dc\7\22\2\2\u01af\u01dc\7\23\2\2\u01b0"+
		"\u01dc\7\24\2\2\u01b1\u01dc\7\25\2\2\u01b2\u01dc\7\26\2\2\u01b3\u01dc"+
		"\7\30\2\2\u01b4\u01dc\7\32\2\2\u01b5\u01dc\7\33\2\2\u01b6\u01dc\7,\2\2"+
		"\u01b7\u01dc\7\34\2\2\u01b8\u01dc\7\35\2\2\u01b9\u01dc\7\36\2\2\u01ba"+
		"\u01dc\7\37\2\2\u01bb\u01dc\7 \2\2\u01bc\u01dc\7!\2\2\u01bd\u01dc\7\""+
		"\2\2\u01be\u01dc\7#\2\2\u01bf\u01dc\7$\2\2\u01c0\u01dc\7%\2\2\u01c1\u01dc"+
		"\7&\2\2\u01c2\u01dc\7\'\2\2\u01c3\u01dc\7(\2\2\u01c4\u01dc\7)\2\2\u01c5"+
		"\u01dc\7*\2\2\u01c6\u01dc\7+\2\2\u01c7\u01dc\7-\2\2\u01c8\u01dc\7.\2\2"+
		"\u01c9\u01dc\7/\2\2\u01ca\u01dc\7\60\2\2\u01cb\u01dc\7\61\2\2\u01cc\u01dc"+
		"\7\62\2\2\u01cd\u01dc\7\63\2\2\u01ce\u01dc\7\65\2\2\u01cf\u01dc\78\2\2"+
		"\u01d0\u01dc\7:\2\2\u01d1\u01dc\7\64\2\2\u01d2\u01dc\7\66\2\2\u01d3\u01dc"+
		"\7\67\2\2\u01d4\u01dc\7;\2\2\u01d5\u01dc\7=\2\2\u01d6\u01dc\7>\2\2\u01d7"+
		"\u01dc\7?\2\2\u01d8\u01dc\7@\2\2\u01d9\u01dc\7A\2\2\u01da\u01dc\7B\2\2"+
		"\u01db\u01a1\3\2\2\2\u01db\u01a2\3\2\2\2\u01db\u01a3\3\2\2\2\u01db\u01a4"+
		"\3\2\2\2\u01db\u01a5\3\2\2\2\u01db\u01a6\3\2\2\2\u01db\u01a7\3\2\2\2\u01db"+
		"\u01a8\3\2\2\2\u01db\u01a9\3\2\2\2\u01db\u01aa\3\2\2\2\u01db\u01ab\3\2"+
		"\2\2\u01db\u01ac\3\2\2\2\u01db\u01ad\3\2\2\2\u01db\u01ae\3\2\2\2\u01db"+
		"\u01af\3\2\2\2\u01db\u01b0\3\2\2\2\u01db\u01b1\3\2\2\2\u01db\u01b2\3\2"+
		"\2\2\u01db\u01b3\3\2\2\2\u01db\u01b4\3\2\2\2\u01db\u01b5\3\2\2\2\u01db"+
		"\u01b6\3\2\2\2\u01db\u01b7\3\2\2\2\u01db\u01b8\3\2\2\2\u01db\u01b9\3\2"+
		"\2\2\u01db\u01ba\3\2\2\2\u01db\u01bb\3\2\2\2\u01db\u01bc\3\2\2\2\u01db"+
		"\u01bd\3\2\2\2\u01db\u01be\3\2\2\2\u01db\u01bf\3\2\2\2\u01db\u01c0\3\2"+
		"\2\2\u01db\u01c1\3\2\2\2\u01db\u01c2\3\2\2\2\u01db\u01c3\3\2\2\2\u01db"+
		"\u01c4\3\2\2\2\u01db\u01c5\3\2\2\2\u01db\u01c6\3\2\2\2\u01db\u01c7\3\2"+
		"\2\2\u01db\u01c8\3\2\2\2\u01db\u01c9\3\2\2\2\u01db\u01ca\3\2\2\2\u01db"+
		"\u01cb\3\2\2\2\u01db\u01cc\3\2\2\2\u01db\u01cd\3\2\2\2\u01db\u01ce\3\2"+
		"\2\2\u01db\u01cf\3\2\2\2\u01db\u01d0\3\2\2\2\u01db\u01d1\3\2\2\2\u01db"+
		"\u01d2\3\2\2\2\u01db\u01d3\3\2\2\2\u01db\u01d4\3\2\2\2\u01db\u01d5\3\2"+
		"\2\2\u01db\u01d6\3\2\2\2\u01db\u01d7\3\2\2\2\u01db\u01d8\3\2\2\2\u01db"+
		"\u01d9\3\2\2\2\u01db\u01da\3\2\2\2\u01dc=\3\2\2\2\u01dd\u01de\t\5\2\2"+
		"\u01de?\3\2\2\2\u01df\u01e2\5R*\2\u01e0\u01e2\5T+\2\u01e1\u01df\3\2\2"+
		"\2\u01e1\u01e0\3\2\2\2\u01e2A\3\2\2\2\u01e3\u01e4\5T+\2\u01e4C\3\2\2\2"+
		"\u01e5\u01e6\5T+\2\u01e6E\3\2\2\2\u01e7\u01e8\5D#\2\u01e8G\3\2\2\2\u01e9"+
		"\u01ea\5D#\2\u01eaI\3\2\2\2\u01eb\u01ec\5D#\2\u01ecK\3\2\2\2\u01ed\u01ee"+
		"\5H%\2\u01eeM\3\2\2\2\u01ef\u01f0\5D#\2\u01f0O\3\2\2\2\u01f1\u01f2\5D"+
		"#\2\u01f2Q\3\2\2\2\u01f3\u01f4\7J\2\2\u01f4S\3\2\2\2\u01f5\u01f6\7K\2"+
		"\2\u01f6U\3\2\2\2$Ye\u0085\u008c\u0091\u00a3\u00af\u00bc\u00ce\u00d7\u00df"+
		"\u00e7\u00ec\u00f5\u00f8\u0101\u0104\u010a\u010c\u0116\u0119\u012b\u0130"+
		"\u013f\u0152\u0158\u0162\u0169\u0173\u017e\u0180\u018a\u01db\u01e1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}