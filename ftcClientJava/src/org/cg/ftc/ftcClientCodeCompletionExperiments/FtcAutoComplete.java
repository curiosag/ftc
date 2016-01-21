package org.cg.ftc.ftcClientCodeCompletionExperiments;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.JTextComponent;

import org.cg.common.util.Op;
import org.cg.ftc.ftcQueryEditor.FtcCompletionProvider;
import org.cg.ftc.shared.interfaces.CompletionsSource;
import org.cg.ftc.shared.interfaces.SqlCompletionType;
import org.cg.ftc.shared.interfaces.SyntaxElementSource;
import org.cg.ftc.shared.uglySmallThings.Const;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ParameterChoicesProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;
import org.fife.ui.autocomplete.TemplateCompletion;

import org.fife.ui.autocomplete.ParameterizedCompletion.Parameter;
import org.fife.ui.autocomplete.RoundRobinAutoCompletion;

public class FtcAutoComplete {

	private final FtcCompletionProvider schemaElementProvider;
	private final FtcCompletionProvider nonSchemaElementProvider;

	private RoundRobinAutoCompletion ac = null;

	private final static SqlCompletionType[] schemaElementTypes = { SqlCompletionType.table, SqlCompletionType.column };
	private final static SqlCompletionType[] nonSchemaElementTypes = getNonSchemaElements();
	private final static SqlCompletionType[] emptySchemaElementTypes = {};

	private final static List<Completion> emptyCompletions = new LinkedList<Completion>();

	public FtcAutoComplete(final SyntaxElementSource syntaxElementSource, final CompletionsSource completionsSource) {

		schemaElementProvider = new FtcCompletionProvider(syntaxElementSource, completionsSource, schemaElementTypes);
		nonSchemaElementProvider = new FtcCompletionProvider(syntaxElementSource, completionsSource, nonSchemaElementTypes);

//		ac = new AutoCompletion(createCompletionProvider());
//		ac.setAutoCompleteEnabled(true);
//		ac.setParameterAssistanceEnabled(true);
		
		ac = new RoundRobinAutoCompletion(schemaElementProvider);
		ac.addCompletionProvider(nonSchemaElementProvider);

		ac.setParameterAssistanceEnabled(true);
		ac.setAutoCompleteEnabled(true);

		ParameterChoicesProvider pcp = new ParameterChoicesProvider() {

			@Override
			public List<Completion> getParameterChoices(JTextComponent tc, Parameter param) {

				List<Completion> result;
				
				if (isSchemaParameter(param.getName()))
					result = new FtcCompletionProvider(syntaxElementSource, completionsSource, emptySchemaElementTypes).getSchemaValueCompletions(tc);
				else
					result = emptyCompletions;
				
				return result;
			}

			private boolean isSchemaParameter(String name) {
				return name.equals(Const.paramNameTable) || name.startsWith(Const.paramNameColumn);
			}
		};

		nonSchemaElementProvider.setParameterChoicesProvider(pcp);

	}

	public void install(JTextComponent c) {
		ac.install(c);
	}

	private CompletionProvider createCompletionProvider() {

		// A DefaultCompletionProvider is the simplest concrete implementation
		// of CompletionProvider. This provider has no understanding of
		// language semantics. It simply checks the text entered up to the
		// caret position for a match against known completions. This is all
		// that is needed in the majority of cases.

		final DefaultCompletionProvider provider = new DefaultCompletionProvider();

		ParameterChoicesProvider pcp = new ParameterChoicesProvider() {

			@Override
			public List<Completion> getParameterChoices(JTextComponent tc, Parameter param) {
				ArrayList<Completion> p = new ArrayList<Completion>();
				p.add(new BasicCompletion(provider, "p1"));
				p.add(new BasicCompletion(provider, "p2"));
				p.add(new BasicCompletion(provider, "p3"));
				return p;
			}
		};

		provider.setParameterChoicesProvider(pcp);

		ArrayList<Completion> subs = new ArrayList<Completion>();

		subs.add(new TemplateCompletion(provider, "xX", "forlp",
				"for (int ${i} = 0; ${i} &lt; ${array}.length; ${i}++)"));
		subs.add(new BasicCompletion(provider, "b"));
		subs.add(new BasicCompletion(provider, "s2"));
		subs.add(new BasicCompletion(provider, "s3"));
		provider.addCompletion(new BasicCompletion(provider, "s", "", null));

		// Add completions for all Java keywords. A BasicCompletion is just
		// a straightforward word completion.
		TemplateCompletion tc = new TemplateCompletion(provider, "y", "y",
				"for (int ${i} = 0; ${i} &lt; ${array}.length; ${i}++)");

		provider.addCompletion(tc);

		provider.addCompletion(new BasicCompletion(provider, "assert"));
		provider.addCompletion(new BasicCompletion(provider, "break"));
		provider.addCompletion(new BasicCompletion(provider, "case"));
		// ... etc ...
		provider.addCompletion(new BasicCompletion(provider, "transient"));
		provider.addCompletion(new BasicCompletion(provider, "try"));
		provider.addCompletion(new BasicCompletion(provider, "void"));
		provider.addCompletion(new BasicCompletion(provider, "volatile"));
		provider.addCompletion(new BasicCompletion(provider, "while"));
		
		// Add a couple of "shorthand" completions. These completions don't
		// require the input text to be the same thing as the replacement text.
		provider.addCompletion(
				new ShorthandCompletion(provider, "sysout", "System.out.println(", "System.out.println("));
		provider.addCompletion(
				new ShorthandCompletion(provider, "syserr", "System.err.println(", "System.err.println("));

		return provider;

	}

	private static SqlCompletionType[] getNonSchemaElements() {
		SqlCompletionType[] v = SqlCompletionType.values();
		List<SqlCompletionType> result = new LinkedList<SqlCompletionType>();
		for (SqlCompletionType t : v)
			if (!Op.in(t, schemaElementTypes))
				result.add(t);
		return result.toArray(new SqlCompletionType[result.size()]);
	}

}
