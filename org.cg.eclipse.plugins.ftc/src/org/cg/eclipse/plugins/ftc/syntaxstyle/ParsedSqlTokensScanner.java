package org.cg.eclipse.plugins.ftc.syntaxstyle;

import java.util.List;

import org.cg.common.check.Check;
import org.cg.eclipse.plugins.ftc.MessageConsoleLogger;
import org.cg.eclipse.plugins.ftc.preference.SyntaxStyle;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import com.google.common.base.Optional;

public class ParsedSqlTokensScanner implements ITokenScanner {

	private final SyntaxColoring coloring;
	private final boolean debug = false;

	List<StyleRange> styles;
	Optional<StyleRange> lastStyle;

	public ParsedSqlTokensScanner(SyntaxColoring coloring) {
		this.coloring = coloring;
	}

	@Override
	public void setRange(IDocument document, int offset, int length) {
		coloring.setText(document.get());
		styles = coloring.getStyles(offset, offset + length);
	}

	@Override
	public IToken nextToken() {
		if (styles == null || styles.size() == 0) {
			lastStyle = Optional.absent();
			return Token.EOF;
		} else {
			lastStyle = Optional.of(styles.get(0));
			styles.remove(0);
			SyntaxStyle styleDef = lastStyle.get().style;
			Color color = ColorManager.getDefault().getColor(styleDef.color);
			return new Token(new TextAttribute(color, null, getStyleBitmap(styleDef)));
		}
	}

	public static int getStyleBitmap(SyntaxStyle styleDef) {
		int style = SWT.NORMAL;
		if (styleDef.italic)
			style = style + SWT.ITALIC;
		if (styleDef.bold)
			style = style + SWT.BOLD;
		if (styleDef.strikethrough)
			style = style + TextAttribute.STRIKETHROUGH;
		if (styleDef.underline)
			style = style + TextAttribute.UNDERLINE;
		return style;
	}

	@Override
	public int getTokenOffset() {
		Check.isTrue(lastStyle.isPresent());
		debug(String.format("off: %d col: %s", lastStyle.get().start, lastStyle.get().style.color));
		return lastStyle.get().start;
	}

	@Override
	public int getTokenLength() {
		Check.isTrue(lastStyle.isPresent());

		debug(String.format("len: %d", lastStyle.get().length));
		return lastStyle.get().length;
	}

	private void debug(String s) {
		if (debug)
			MessageConsoleLogger.getDefault().Info(s);
	}

}
