package org.cg.eclipse.plugins.ftc.template;

import org.cg.eclipse.plugins.ftc.MessageConsoleLogger;
import org.eclipse.jdt.internal.ui.text.template.contentassist.PositionBasedCompletionProposal;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.link.ProposalPosition;
import org.eclipse.jface.text.templates.TemplateVariable;

@SuppressWarnings("restriction")
public class TweakedProposalPosition extends ProposalPosition {

	private static boolean debug = false;
	private final TemplateVariable variable;
	private final FtcDocumentTemplateContext context;
	private final Position position;

	public TweakedProposalPosition(TemplateVariable templateVariable, String positionCategory,
			FtcDocumentTemplateContext context, int offset, int length, ICompletionProposal[] proposals) {
		super(context.getDocument(), offset, length, proposals);
		this.variable = templateVariable;
		this.context = context;

		position = new Position(offset, length);

		try {
			getDocument().addPosition(positionCategory, position);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public TemplateVariable getTemplateVariable() {
		return variable;
	}

	@Override
	public ICompletionProposal[] getChoices() {
		CompletionProposal[] choices = createCompletionProposals(offset, variable.getType(),
				context.getCurrentTemplate().getPattern(), context.getDocument().get());
		ICompletionProposal[] result = createPositionBasedProposals(offset, variable, choices);
		return result;
	}

	private ICompletionProposal[] createPositionBasedProposals(int offset, TemplateVariable variable,
			ICompletionProposal[] values) {
		int length = variable.getLength();

		ICompletionProposal[] proposals = new ICompletionProposal[values.length];

		for (int j = 0; j < values.length; j++)
			proposals[j] = new PositionBasedCompletionProposal(values[j].getDisplayString(), position, length);

		return proposals;
	}

	public static CompletionProposal[] createCompletionProposals(int completionOffset, String variableType,
			String pattern, String currentText) {
		String patchedText = currentText;
		int variablePosition = completionOffset;

		debug("** create completion proposals **");
		debug("pattern: " + pattern);
		debug(String.format("completion offset %d, variable type %s calculated variable position %d", completionOffset,
				variableType, variablePosition));
		debug("query for parsing: " + patchedText);

		CompletionProposal[] proposals = FtcCompletionProcessor.getModelElementProposals(patchedText, variablePosition,
				0);
		debug(String.format("%d proposals", proposals.length));
		return proposals;
	}

	private static void debug(String s) {
		if (!debug)
			return;
		MessageConsoleLogger.getDefault().Info(s);

	}
}