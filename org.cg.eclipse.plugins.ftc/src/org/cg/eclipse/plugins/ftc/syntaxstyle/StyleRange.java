package org.cg.eclipse.plugins.ftc.syntaxstyle;

import org.cg.eclipse.plugins.ftc.preference.SyntaxStyle;
import org.cg.ftc.shared.interfaces.SyntaxElement;

public class StyleRange {

	public final int start; 
	public final int length;
	public SyntaxElement token;
	public final SyntaxStyle style;
	
	public StyleRange(int start, int length, SyntaxStyle style)
	{
		this.start = start;
		this.length = length;
		this.style = style;
	}
	
	public StyleRange(int start, int length, SyntaxStyle style, SyntaxElement token){
		this(start, length, style);
		this.token = token;
	}
	
}
