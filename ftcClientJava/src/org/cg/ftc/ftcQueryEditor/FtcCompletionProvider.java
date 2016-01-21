package org.cg.ftc.ftcQueryEditor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.JTextComponent;

import org.cg.common.util.Op;
import org.cg.common.util.StringUtil;
import org.cg.ftc.shared.interfaces.CompletionsSource;
import org.cg.ftc.shared.interfaces.SqlCompletionType;
import org.cg.ftc.shared.interfaces.SyntaxElement;
import org.cg.ftc.shared.interfaces.SyntaxElementSource;
import org.cg.ftc.shared.structures.AbstractCompletion;
import org.cg.ftc.shared.structures.Completions;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ParameterizedCompletion;
import org.fife.ui.autocomplete.TemplateCompletion;

public class FtcCompletionProvider extends DefaultCompletionProvider implements SyntaxElementSource {

	public final CompletionsSource completionsSource;
	public final SyntaxElementSource syntaxElementSource;
	private final static List<Completion> noCompletions = new LinkedList<Completion>();
	private SqlCompletionType[] completionTypes;

	private static final SqlCompletionType[] schemaCompletions = { SqlCompletionType.table, SqlCompletionType.column };

	public FtcCompletionProvider(SyntaxElementSource syntaxElementSource, CompletionsSource completionsSource,
			SqlCompletionType... types) {
		this.completionsSource = completionsSource;
		this.syntaxElementSource = syntaxElementSource;
		this.completionTypes = types;
		setListCellRenderer(new FtcListCellRenderer());
	}

	@Override
	public boolean isAutoActivateOkay(JTextComponent tc) {
		return false;
	};

	@Override
	public void clearParameterizedCompletionParams() {
		super.clearParameterizedCompletionParams();
	};

	@Override
	protected List<Completion> getCompletionsImpl(JTextComponent comp) {
		resetCompletions(comp);

		return super.getCompletionsImpl(comp);
	};

	@Override
	public List<Completion> getCompletionsAt(JTextComponent tc, Point p) {
		return noCompletions;
	}

	@Override
	public String getAlreadyEnteredText(JTextComponent comp) {
		return super.getAlreadyEnteredText(comp);
	}

	@Override
	public List<ParameterizedCompletion> getParameterizedCompletions(JTextComponent tc) {
		return super.getParameterizedCompletions(tc);
	}

	private int recentCaretPosition = -1;
	private String recentQuery = null;

	
	
	private void resetCompletions(JTextComponent comp) {
	
		
		String query = comp.getText();
		int caretPosition = comp.getCaretPosition();

		if (StringUtil.emptyOrNull(query) || cursorMoved(caretPosition) || queryChanged(query)) {
			Completions externalCompletions = completionsSource.get(query, caretPosition);
			clear();
			for (AbstractCompletion c : externalCompletions.getAll())
				addCompletion(c);
		}

		recentCaretPosition = caretPosition;
		recentQuery = query;
	}

	private boolean queryChanged(String text) {
		return !text.equals(recentQuery);
	}

	private boolean cursorMoved(int caretPosition) {
		return recentCaretPosition < 0 || (Math.abs(caretPosition - recentCaretPosition)) >= 1;
	}

	private void addCompletion(AbstractCompletion c) {
		if (completionTypes.length == 0 || Op.in(c.completionType, completionTypes))
			addCompletion(createCompletion(this, c));
	}

	public int size() {
		return completions.size();
	}

	public List<Completion> getSchemaValueCompletions(JTextComponent comp) {
		List<Completion> result = new ArrayList<Completion>();

		String text = comp.getText();
		int caretPosition = comp.getCaretPosition();

		for (AbstractCompletion c : completionsSource.get(text, caretPosition).getAll())
			if (Op.in(c.completionType, schemaCompletions))
				result.add(new BasicCompletion(this, c.displayName));

		return result;
	}

	public static org.fife.ui.autocomplete.AbstractCompletion createCompletion(CompletionProvider provider,
			AbstractCompletion c) {
		if (Op.in(c.completionType, schemaCompletions) || !c.hasParameter())
			return new BasicCompletion(provider, c.getPatch());
		else
			return new TemplateCompletion(provider, c.displayName, c.displayName, c.getPatch());
	}

	@Override
	public List<SyntaxElement> get(String query) {
		return syntaxElementSource.get(query);
	}

}
