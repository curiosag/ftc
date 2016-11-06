package org.cg.ftceditor;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.templates.Template;

public class TemplateApplicationCircumstances {

	private Template template;
	private ITextViewer viewer;
	private int offset;
	
	private static TemplateApplicationCircumstances _default;
	
	public static TemplateApplicationCircumstances getDefault()
	{
		if (_default == null)
			_default = new TemplateApplicationCircumstances();
		
		return _default;
	}
	
	private TemplateApplicationCircumstances()
	{
		this.template = null;
		this.viewer = null;
		this.offset = 0;
	}
	
	public void setCircumstances(Template template, ITextViewer viewer, int offset)
	{
		this.template = template;
		this.viewer = viewer;
		this.offset = offset;
	}
	
	public Template getTemplate() {
		return template;
	}

	public ITextViewer getViewer() {
		return viewer;
	}

	public int getOffset() {
		return offset;
	}

}
