
package org.cg.eclipse.plugins.ftc.template;

import org.cg.common.check.Check;
import org.cg.common.util.StringUtil;
import org.cg.eclipse.plugins.ftc.MessageConsoleLogger;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateVariableResolver;

/**
 * proposes
 */
public class FtcVariableResolver extends TemplateVariableResolver {

	private static final String DUMMY_COLNAME = "colname";
	private static final String LEADING_CHAR_COLUMNVAR = "c";
	private static boolean debug = true;

	@Override
	protected String[] resolveAll(TemplateContext context) {

		Check.isTrue(context instanceof FtcDocumentTemplateContext);
		FtcDocumentTemplateContext ftcContext = (FtcDocumentTemplateContext) context;

		String pattern = ftcContext.getCurrentTemplate().getPattern();
		Check.notNull(pattern);
		String currentText = ftcContext.getDocument().get();
		ICompletionProposal[] proposals = createCompletionProposals(ftcContext.getCompletionOffset(), getType(),
				pattern, currentText);

		String[] result = new String[proposals.length];
		for (int i = 0; i < proposals.length; i++)
			result[i] = proposals[i].getDisplayString();
		return result;
	}

	public static CompletionProposal[] createCompletionProposals(int completionOffset, String variableType,
			String pattern, String currentText) {
		debug("* variable resolver *");
		int replacementLength = 0; // TODO length of selected text actually
		CompletionProposal[] proposals;

		String patchedText = StringUtil.insert(currentText, completionOffset, prepareForParsing(pattern));
		// + 2 : in prepareForParsing e.g. "${t}" gets changed to " t "
		// so the index must be at "t" rather than "$"
		int variablePosition = completionOffset + (pattern.indexOf(String.format("${%s}", variableType))) + 2;

		if (variableType.startsWith(LEADING_CHAR_COLUMNVAR)) {
			String patch = DUMMY_COLNAME;
			
			CompletionProposal dummyProposal = new CompletionProposal(patch, variablePosition, replacementLength,
					patch.length());
			// 2 dummy proposals to trigger use of a TweakedProposalPosition in TweakedTemplateProposal
			// no handling of this kind for tables, though, to cover the case where there's one table only too
			proposals = new CompletionProposal[] { dummyProposal, dummyProposal };
			debug(String.format("column at %d ", variablePosition));

		} else {

			debug("pattern: " + pattern);
			debug(String.format("completion offset %d, variable type %s calculated variable position %d",
					completionOffset, variableType, variablePosition));
			debug("patched query for parsing: " + patchedText);

			proposals = FtcCompletionProcessor.getModelElementProposals(patchedText, variablePosition,
					replacementLength);
			debug(String.format("%d proposals", proposals.length));
		}
		return proposals;
	}

	private static void debug(String s) {
		if (!debug)
			return;
		MessageConsoleLogger.getDefault().Info(s);
	}

	private static String prepareForParsing(String pattern) {
		return pattern.replace("${", "  ").replace("}", " ");
	}
}
