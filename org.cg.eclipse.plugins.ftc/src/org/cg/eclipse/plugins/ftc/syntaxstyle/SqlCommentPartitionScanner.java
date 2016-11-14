package org.cg.eclipse.plugins.ftc.syntaxstyle;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
public class SqlCommentPartitionScanner extends RuleBasedPartitionScanner {

	public final static String SQL_COMMENT = "sql_comment";
	public final static String[] partition_types = new String[] { SQL_COMMENT };

	static class EmptyCommentDetector implements IWordDetector {

		@Override
		public boolean isWordStart(char c) {
			return (c == '/');
		}

		@Override
		public boolean isWordPart(char c) {
			return (c == '*' || c == '/');
		}
	}
	
	static class WordPredicateRule extends WordRule implements IPredicateRule {

		private IToken fSuccessToken;

		public WordPredicateRule(IToken successToken) {
			super(new EmptyCommentDetector());
			fSuccessToken= successToken;
			addWord("/**/", fSuccessToken); //$NON-NLS-1$
		}

		@Override
		public IToken evaluate(ICharacterScanner scanner, boolean resume) {
			return super.evaluate(scanner);
		}

		@Override
		public IToken getSuccessToken() {
			return fSuccessToken;
		}
	}

	private static SqlCommentPartitionScanner fPartitionScanner;
	
	public static SqlCommentPartitionScanner getDefault() {
		if (fPartitionScanner == null)
			fPartitionScanner= new SqlCommentPartitionScanner();
		return fPartitionScanner;
	}
	
	private SqlCommentPartitionScanner() {
		IToken commentToken = new Token(SQL_COMMENT);

		List<IPredicateRule> rules= new ArrayList<>();

		rules.add(new EndOfLineRule("--", commentToken, '\\'));
		rules.add(new MultiLineRule("/*", "*/", commentToken, '\\'));
		
		rules.add(new WordPredicateRule(commentToken));
		rules.add(new SingleLineRule("\"", "\"", Token.UNDEFINED, '\\')); 
		rules.add(new SingleLineRule("'", "'", Token.UNDEFINED, '\\'));

		IPredicateRule[] result= new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}

}
