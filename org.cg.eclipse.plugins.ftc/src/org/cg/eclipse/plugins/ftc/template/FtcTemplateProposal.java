package org.cg.eclipse.plugins.ftc.template;

import org.cg.common.check.Check;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.swt.graphics.Image;

public class FtcTemplateProposal extends TweakedTemplateProposal {
		
	private static DocumentTemplateContext getDc(TemplateContext context)
	{
		Check.isTrue(context instanceof DocumentTemplateContext);
		return (DocumentTemplateContext)context;
	}
	
	public FtcTemplateProposal(Template template, TemplateContext context, IRegion region, Image image, int relevance) {
		super(template, new FtcDocumentTemplateContext(getDc(context), template), region, image, relevance);
	}
	
	@Override
	public void apply(ITextViewer viewer, char trigger, int stateMask, int offset) {
		super.apply(viewer, trigger, stateMask, offset);
	}

}
