package org.cg.ftceditor;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;

public class FtcTemplateProposal extends TemplateProposal {
	
	public FtcTemplateProposal(Template template, TemplateContext context, IRegion region, Image image, int relevance) {
		super(template, context, region, image, relevance);
	}
	
	@Override
	public void apply(ITextViewer viewer, char trigger, int stateMask, int offset) {
		TemplateApplicationCircumstances.getDefault().setCircumstances(getTemplate(), viewer, offset);
		super.apply(viewer, trigger, stateMask, offset);
	}

}
