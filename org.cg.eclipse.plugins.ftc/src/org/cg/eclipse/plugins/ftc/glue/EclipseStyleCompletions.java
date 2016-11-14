package org.cg.eclipse.plugins.ftc.glue;

import java.util.ArrayList;
import java.util.List;

import org.cg.ftc.shared.structures.AbstractCompletion;
import org.cg.ftc.shared.structures.CodeSnippetCompletion;
import org.cg.ftc.shared.structures.Completions;
import org.cg.ftc.shared.structures.ModelElementCompletion;

public class EclipseStyleCompletions {

	public final List<ModelElementCompletion> modelElements = new ArrayList<ModelElementCompletion>();
	public final List<CodeSnippetCompletion> templates = new ArrayList<CodeSnippetCompletion>();

	public EclipseStyleCompletions(Completions c) {
		for (AbstractCompletion completion : c.getAll()) {
			if (completion instanceof ModelElementCompletion)
				modelElements.add((ModelElementCompletion) completion);
			if (completion instanceof CodeSnippetCompletion)
				templates.add(new CodeSnippetCompletion(completion.completionType, completion.displayName,
						adjust(completion.getPatch())));
		}
	}

	private String adjust(String patch) {
		String result = patch.replace("${cursor}", "");
		// if (result.indexOf("${t}") >= 0)
		// return result.replace("${c}", "colname");
		// else
		return result;
	}
}
