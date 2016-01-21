package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.PrintException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.pattern.ParseTreeMatch;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.antlr.v4.runtime.tree.xpath.XPath;
import org.cg.common.check.Check;
import org.cg.common.core.Logging;
import org.cg.common.core.SystemLogger;
import org.cg.common.util.*;
import org.cg.ftc.parser.FusionTablesSqlLexer;
import org.cg.ftc.parser.FusionTablesSqlParser;
import org.cg.ftc.parser.FusionTablesSqlParser.FusionTablesSqlContext;
import org.cg.ftc.shared.interfaces.Connector;
import org.cg.ftc.shared.interfaces.SyntaxElement;
import org.junit.Test;

import manipulations.QueryManipulator;
import manipulations.Util;
import manipulations.results.*;
import manipulations.results.RefactoredSql;
import manipulations.results.ResolvedTableNames;
import manipulations.results.Splits;

@SuppressWarnings("unused")
public class TestQueryManipulation {

	private static void showTree(FusionTablesSqlParser parser) {
		RuleContext tree = parser.fusionTablesSql();

		// tree.inspect(parser);
	}

	private static ParseTreeWalker walker = new ParseTreeWalker();


	private void log(String msg) {
		System.out.println(msg);
	}

	
	private void getStatementType(FusionTablesSqlParser p) {
		FusionTablesSqlContext x = p.fusionTablesSql();

	}

	private static Util.Stuff getParserStuff() {
		String filePath = "/home/sp/fucg/qmix.txt";
		System.out.println("running on " + filePath);
		return Util.getParser(Util.getFileInput(filePath));
	}
	
	// @Test
	public void treeXpath() {
		FusionTablesSqlParser parser = getParserStuff().parser;
		String xpath = "//sql_stmt";

		for (ParseTree t : XPath.findAll(parser.fusionTablesSql(), xpath, parser))
			System.out.println(Util.getSqlStatementType(t, parser).name());

	}

	// @Test
	public void testResolveAlterTableIdentifiers() {
		QueryManipulator ftr = test.Util.getManipulator("alter table table1 rename to tableB;");
		ResolvedTableNames names = ftr.getAlterTableIdentifiers();
		assertTrue(names.idFrom.isPresent());
		assertFalse(names.problemsEncountered.isPresent());
		assertEquals("table1", names.nameFrom);
		assertEquals("ID1", names.idFrom.get());
		assertEquals("tableB", names.nameTo);

		ftr = test.Util.getManipulator("alter table x rename to tableB;");
		names = ftr.getAlterTableIdentifiers();
		assertFalse(names.idFrom.isPresent());
		assertTrue(names.problemsEncountered.isPresent());
		log(names.problemsEncountered.get());
		assertEquals("x", names.nameFrom);
		assertEquals("tableB", names.nameTo);

		ftr = test.Util.getManipulator("alter table table1 rename toooo tableB;");
		names = ftr.getAlterTableIdentifiers();
		assertTrue(names.problemsEncountered.isPresent());
		log(names.problemsEncountered.get());
	}

	// @Test
	public void testResolveDropTableIdentifiers() {
		QueryManipulator ftr = test.Util.getManipulator("drop table table1;");
		ResolvedTableNames names = ftr.getTableNameToDrop();
		assertTrue(names.idFrom.isPresent());
		assertEquals("table1", names.nameFrom);
		assertEquals("ID1", names.idFrom.get());

		ftr = test.Util.getManipulator("drop table x;");
		names = ftr.getTableNameToDrop();
		assertFalse(names.idFrom.isPresent());
		assertTrue(names.problemsEncountered.isPresent());
		log(names.problemsEncountered.get());
		assertEquals("x", names.nameFrom);

		ftr = test.Util.getManipulator("drop x;");
		names = ftr.getTableNameToDrop();
		assertTrue(names.problemsEncountered.isPresent());
		log(names.problemsEncountered.get());
	}

	 @Test
	public void testResolveSelectIdentifiers() {
		QueryManipulator ftr = test.Util.getManipulator("Select * from table1;");
		RefactoredSql r = ftr.refactorQuery();
		assertFalse(r.problemsEncountered.isPresent());
		log("o: " + r.original);
		log("r: " + r.refactored);

		String joinQuery = "SELECT A.a1, A.*, table2.b1, Average(table2.b2), Average(A.a2)\n"
				+ "FROM table1 as A LEFT OUTER JOIN table2 as B ON A.id = table2.id\n"
				+ "LEFT OUTER JOIN table3 ON B.id = table3.id;";

		ftr = test.Util.getManipulator(joinQuery);
		r = ftr.refactorQuery();
		assertFalse(r.problemsEncountered.isPresent());
		log(r.refactored);
		log("o: " + joinQuery);
		log("r: " + r.refactored);

		joinQuery = "SELECT A.a1, A.*, table2.b1, Average(table2.b2), Average(A.a2)\n"
				+ "FROM table1 as A LEFT UUUUTER JOIN table2 as B ON A.id = table2.id\n" // <----
																						// syntax
																						// error
				+ "LEFT OUTER JOIN table3 ON B.id = table3.id;";

		ftr = test.Util.getManipulator(joinQuery);
		r = ftr.refactorQuery();
		assertTrue(r.problemsEncountered.isPresent());
		log(r.refactored);
		log("O: " + joinQuery);
		log("R: " + r.refactored);
		log("P" + r.problemsEncountered.get());

	}

	@Test
	public void testStatemntSplit() {
		QueryManipulator m = test.Util.getManipulator("Select * from table1; Select a,b from table2;");
		Splits s = m.splitStatements();
		assertFalse(s.problemsEncountered.isPresent());
		assertEquals(2, s.splits.size());
		assertEquals("Select * from table1;", s.splits.get(0));
		assertEquals("Select a,b from table2;", s.splits.get(1));

		m = test.Util.getManipulator("Select * from table1; Sälect a,b fromm from table2;Select * from table3;");
		s = m.splitStatements();
		assertTrue(s.problemsEncountered.isPresent());
		log(s.problemsEncountered.get());
	}
	
	@Test
	public void testSyntaxElements() {
		QueryManipulator m = test.Util.getManipulator("select A.a, loc_col from ah where F = -1 and ST_INTERSECTS(loc_col,  CIRCLE(LATLNG(1, 1), 1)) and ST_INTERSECTS(loc_col, RECTANGLE( LATLNG(2, 2), LATLNG(2, 2)) ) order by st_distance(loc_col, LATLNG(0, 0));");
		List<SyntaxElement> l = m.getSyntaxElements();
		for (SyntaxElement e : l) 
			System.out.println(String.format("%d-%d %s %s", e.from, e.to, e.type.name(), e.value));
	}
}
