package org.cg.ftceditor;

import org.eclipse.jface.text.rules.*;

public class FtcScanner extends RuleBasedScanner {

	public FtcScanner(ColorManager manager) {
		IToken stringToken = manager.getColoredToken(IColorConstants.STRING);
		IToken numberToken = manager.getColoredToken(IColorConstants.NUMBER);
		IToken commentToken = manager.getColoredToken(IColorConstants.COMMENT);
		
		IRule[] rules = new IRule[6];

		rules[0] = new EndOfLineRule("#", commentToken, '\\');
		rules[1] = new MultiLineRule("/*", "*/", commentToken, '\\');
		rules[2] = new SingleLineRule("\"", "\"", stringToken, '\\');
		rules[3] = new SingleLineRule("'", "'", stringToken, '\\');
		rules[4] = new NumberRule(numberToken);
		rules[5] = new WhitespaceRule(new WhitespaceDetector());
		
		setRules(rules);
		
		this.setDefaultReturnToken(manager.getColoredToken(IColorConstants.DEFAULT));
	}
}
