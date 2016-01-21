package org.cg.ftc.ftcQueryEditor;

import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.TemplateCompletion;

public class GftParamCompletion extends TemplateCompletion {


	public GftParamCompletion(CompletionProvider provider,	String shortDescription) {
		super(provider, shortDescription, shortDescription, shortDescription, shortDescription, shortDescription);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString()
	{
		return getInputText();
	}

}
