package org.cg.ftceditor;

import org.eclipse.jface.text.rules.*;

public class FtcCommentScanner extends RuleBasedScanner {

	public FtcCommentScanner(ColorManager manager) {
		
		IRule[] rules = new IRule[2];
		rules[0] = new MultiLineRule("/*", "*/", manager.getColoredToken(IColorConstants.COMMENT), '/');
		rules[1] = new EndOfLineRule("#", manager.getColoredToken(IColorConstants.COMMENT));
		
		setRules(rules);
	}
}
