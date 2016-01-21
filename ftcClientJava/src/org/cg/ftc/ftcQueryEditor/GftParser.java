package org.cg.ftc.ftcQueryEditor;

import java.util.List;

import org.cg.common.check.Check;
import org.cg.common.swing.DocumentUtil;
import org.cg.ftc.shared.interfaces.Event;
import org.cg.ftc.shared.interfaces.SyntaxElement;
import org.cg.ftc.shared.interfaces.SyntaxElementSource;
import org.cg.ftc.shared.interfaces.SyntaxElementType;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParserNotice;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
import org.fife.ui.rsyntaxtextarea.parser.ParserNotice.Level;

public class GftParser extends AbstractParser {

	private SyntaxElementSource syntaxElementSource;

	private final Event onStartParsing;
	private final Event onFinishParsing;

	public GftParser(SyntaxElementSource syntaxElementSource, Event onStartParsing, Event onFinishParsing) {
		Check.notNull(syntaxElementSource);
		this.onStartParsing = onStartParsing;
		this.onFinishParsing = onFinishParsing;
		this.syntaxElementSource = syntaxElementSource;
		setEnabled(true);
	}

	@Override
	public ParseResult parse(RSyntaxDocument doc, String style) {
		DefaultParseResult result = new DefaultParseResult(this);
		onStartParsing();
		List<SyntaxElement> elements = syntaxElementSource.get(DocumentUtil.getText(doc));
		
		for (SyntaxElement e : elements)
			maybeAddNotice(result, e);
		
		onFinishParsing();
		
		return result;
	}

	private void maybeAddNotice(DefaultParseResult result, SyntaxElement e) {
		Level level;
		if (e.type == SyntaxElementType.error)
			level = Level.ERROR;
		else if (e.hasSemanticError())
			level = Level.WARNING;
		else
			return;
		DefaultParserNotice notice = new DefaultParserNotice(this, null, -1, e.from, e.value.length());
		notice.setLevel(level);
		result.addNotice(notice);
	}

	public void onStartParsing() {
		if (onStartParsing != null)
			onStartParsing.fire();
	}

	public void onFinishParsing() {
		if (onFinishParsing != null)
			onFinishParsing.fire();
	}

}
