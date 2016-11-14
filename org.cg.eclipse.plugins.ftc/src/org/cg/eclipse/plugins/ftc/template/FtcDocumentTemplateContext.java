package org.cg.eclipse.plugins.ftc.template;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContextType;

public class FtcDocumentTemplateContext extends DocumentTemplateContext {

	private Template template;	
	
	public FtcDocumentTemplateContext(TemplateContextType type, IDocument document, int offset, int length, Template template) {
		super(type, document, offset, length);
		this.template = template;		
	}

	public FtcDocumentTemplateContext(DocumentTemplateContext context, Template template) {
		this(context.getContextType(), context.getDocument(), context.getStart(), context.getCompletionLength(), template);
	}

	/**
	 * FtcVariableResolver (TemplateVariableResolver) needs a reference 
	 * to the template being processed in TemplateProposal.apply
	 * 
	 * @return the processed template in TemplateProposal.apply, if properly set
	 */
	
	public Template getCurrentTemplate() {
		return template;
	}

}
