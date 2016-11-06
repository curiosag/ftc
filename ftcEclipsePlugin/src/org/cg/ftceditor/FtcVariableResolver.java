/*******************************************************************************
*
 *******************************************************************************/
package org.cg.ftceditor;

import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateVariableResolver;

/**
 * proposes
 */
public class FtcVariableResolver extends TemplateVariableResolver {

	@Override
	protected String[] resolveAll(TemplateContext context) {
		System.out.println("caret offset: " + FtcEditor.getDefault().getCaretOffset() + " ");
		System.out.println(getType());

		Template template = TemplateApplicationCircumstances.getDefault().getTemplate();
		if (template != null)
			System.out.println(template.getPattern());
		// TemplateProposal.apply triggers all this and knows everything.
		// Override to supply template text here
		// isn't included in editor text yet at resoveAll

		if (getType().equals("t"))
			return new String[] { "T1", "T2" };
		else
			return new String[] { "C1", "C2" };

	}
}
