package org.cg.eclipse.plugins.ftc.preference;

import org.cg.ftc.shared.interfaces.SyntaxElementType;
import org.eclipse.swt.graphics.RGB;

public class SyntaxStyle {
	public final SyntaxElementType type;
	
	public final String displayName;
	public final boolean enable;
	public final RGB color;
	public final boolean bold;
	public final boolean italic;
	public final boolean strikethrough;
	public final boolean underline;
	
	public SyntaxStyle(String displayName, SyntaxElementType type, boolean enable, RGB color, boolean bold, boolean italic, boolean strikethrough, boolean underline){
		this.displayName = displayName;
		this.type = type;
		this.enable = enable;
		this.color = color;
		this.bold = bold;
		this.italic = italic;
		this.strikethrough = strikethrough;
		this.underline = underline;
	}
}