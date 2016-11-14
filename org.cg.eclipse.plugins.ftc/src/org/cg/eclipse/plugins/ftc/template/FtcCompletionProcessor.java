package org.cg.eclipse.plugins.ftc.template;

import java.util.List;

import org.cg.common.check.Check;
import org.cg.eclipse.plugins.ftc.FtcContextType;
import org.cg.eclipse.plugins.ftc.FtcPlugin;
import org.cg.eclipse.plugins.ftc.PluginConst;
import org.cg.eclipse.plugins.ftc.glue.EclipseStyleCompletions;
import org.cg.eclipse.plugins.ftc.glue.FtcPluginClient;
import org.cg.ftc.shared.structures.CodeSnippetCompletion;
import org.cg.ftc.shared.structures.ModelElementCompletion;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class FtcCompletionProcessor extends TweakedTemplateCompletionProcessor {

	private static final String TEMPLATE_IMAGE = "$nl$/icons/template.gif"; //$NON-NLS-1$
	private TemplateContextType templateContextType = null;
	private EclipseStyleCompletions currentCompletions = null;
	private final ISourceViewer sourceViewer;
	private final static char[] autoActivationChar = new char[]{'.'};
	
	public FtcCompletionProcessor(ISourceViewer sourceViewer) {
		this.sourceViewer = sourceViewer;
	}

	private TemplateContextType getTemplateContextType() {
		ContributionContextTypeRegistry registry = new ContributionContextTypeRegistry();
		registry.addContextType(FtcContextType.TYPE);
		return registry.getContextType(FtcContextType.TYPE);
	}
	
	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return autoActivationChar;
	}
	
	@Override
	protected ICompletionProposal createProposal(Template template, TemplateContext context, IRegion region, int relevance) {
		return new FtcTemplateProposal(template, context, region, getImage(template), relevance);
	}
	
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int documentOffset) {
	
		currentCompletions = getFtcCompletions(viewer.getTextWidget().getText(), documentOffset);
		
		ICompletionProposal[] templates = super.computeCompletionProposals(viewer, documentOffset);
		ICompletionProposal[] completions = getModelElementProposals(currentCompletions.modelElements, documentOffset, sourceViewer.getSelectedRange().y);

		ICompletionProposal[] result = new ICompletionProposal[templates.length + completions.length];
		
		int idx = 0;
		for (int i = 0; i < templates.length; i++) {
			result[idx] = templates[i];
			idx++;
		}
		for (int i = 0; i < completions.length; i++) {
			result[idx] = completions[i];
			idx++;
		}

		return result;
	}

	private static EclipseStyleCompletions getFtcCompletions(String text, int documentOffset) {
		return FtcPluginClient.getDefault().getCompletions(text, documentOffset);
	}

	private static CompletionProposal[] getModelElementProposals(List<ModelElementCompletion> modelElements, int replacementOffset, int replacementLength) {
		CompletionProposal[] result = new CompletionProposal[modelElements.size()];
		
		int i = 0;
		for (ModelElementCompletion e : modelElements) {
			result[i] = new CompletionProposal(e.getPatch(), replacementOffset, replacementLength, e.getPatch().length());
			i++;
		}
		
		return result;
	}
	
	/**
	 * calculates a list of completion proposals based on the provided information
	 *
	 * @param text the complete current editor text
	 * @param replacementOffset the offset of the text to be replaced by completions
	 * @param replacementLength the length of the text to be replaced by completions
	 */

	public static CompletionProposal[] getModelElementProposals(String text, int replacementOffset, int replacementLength)
	{
		return getModelElementProposals(getFtcCompletions(text, replacementOffset).modelElements, replacementOffset, replacementLength);
	}
	
	@Override
	protected Template[] getTemplates(String contextTypeId) {
		
		Template[] result = new Template[getCurrentCompletions().templates.size()];
		
		int i = 0;
		for (CodeSnippetCompletion template : getCurrentCompletions().templates) {
			result[i] = new Template(template.displayName, "", FtcContextType.TYPE, template.snippet, false);
			i++;
		}
		
		return result;
	}

	@Override
	protected TemplateContextType getContextType(ITextViewer viewer, IRegion region) {
		if (templateContextType == null)
			templateContextType = getTemplateContextType();
		return templateContextType;
	}

	@Override
	protected Image getImage(Template template) {
		ImageRegistry registry = FtcPlugin.getDefault().getImageRegistry();
		Image image = registry.get(TEMPLATE_IMAGE);
		if (image == null) {
			ImageDescriptor desc = AbstractUIPlugin.imageDescriptorFromPlugin(PluginConst.PLUGIN_NAME, TEMPLATE_IMAGE);
			registry.put(TEMPLATE_IMAGE, desc);
			image = registry.get(TEMPLATE_IMAGE);

		}
		return image;
	}

	public EclipseStyleCompletions getCurrentCompletions() {
		Check.notNull(currentCompletions);
		return currentCompletions;
	}

}
