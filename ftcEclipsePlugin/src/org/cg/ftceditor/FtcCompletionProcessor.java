package org.cg.ftceditor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class FtcCompletionProcessor extends TemplateCompletionProcessor {

	private static final String DEFAULT_IMAGE = "$nl$/icons/sample.gif"; //$NON-NLS-1$
	private TemplateContextType templateContextType = null;

	private TemplateContextType getTemplateContextType() {
		ContributionContextTypeRegistry registry = new ContributionContextTypeRegistry();
		registry.addContextType(FtcContextType.TYPE);
		return registry.getContextType(FtcContextType.TYPE);
	}
	
	@Override
	protected ICompletionProposal createProposal(Template template, TemplateContext context, IRegion region, int relevance) {
		return new FtcTemplateProposal(template, context, region, getImage(template), relevance);
	}

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int documentOffset) {
		ICompletionProposal[] templates = super.computeCompletionProposals(viewer, documentOffset);
		ICompletionProposal[] completions = addCompletionProposals(viewer, documentOffset);

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

	private ICompletionProposal[] addCompletionProposals(ITextViewer viewer, int documentOffset) {
		String[] fgProposalNames = { "abstract", "boolean", "break" };
		String[] fgProposals = { "abstract", "boolean", "break" };

		ICompletionProposal[] result = new ICompletionProposal[fgProposals.length];
		for (int i = 0; i < fgProposals.length; i++) {
			IContextInformation info = new ContextInformation(fgProposals[i], fgProposals[i]);
			result[i] = new CompletionProposal(fgProposals[i], documentOffset, 0, fgProposals[i].length(), null,
					fgProposalNames[i], info, "additional info");
		}
		return result;
	}

	/**
	 * Return the XML context type that is supported by this plug-in.
	 *
	 * @param viewer
	 *            the viewer, ignored in this implementation
	 * @param region
	 *            the region, ignored in this implementation
	 * @return the supported XML context type
	 */
	@Override
	protected TemplateContextType getContextType(ITextViewer viewer, IRegion region) {
		if (templateContextType == null)
			templateContextType = getTemplateContextType();
		return templateContextType;
	}

	@Override
	protected Template[] getTemplates(String contextTypeId) {
		Template[] result = new Template[1];
		result[0] = new Template("a template", "template description", FtcContextType.TYPE, "a ${t} b ${f} c", true);
		return result;
	}

	@Override
	protected Image getImage(Template template) {
		ImageRegistry registry = Activator.getDefault().getImageRegistry();
		Image image = registry.get(DEFAULT_IMAGE);
		if (image == null) {
			ImageDescriptor desc = AbstractUIPlugin.imageDescriptorFromPlugin("org.cg.ftceditor.FtcEditor", //$NON-NLS-1$
					DEFAULT_IMAGE);
			registry.put(DEFAULT_IMAGE, desc);
			image = registry.get(DEFAULT_IMAGE);
		}
		return image;
	}

}
