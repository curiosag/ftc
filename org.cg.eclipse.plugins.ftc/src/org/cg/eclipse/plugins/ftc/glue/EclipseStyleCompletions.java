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
				modelElements.add(adjust((ModelElementCompletion) completion));

			if (completion instanceof CodeSnippetCompletion)
				templates.add(new CodeSnippetCompletion(completion.completionType, completion.displayName,
						adjust(completion.getPatch())));
		}
	}

	private boolean isAsciiName(String value) {
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')|| (c >= '0' && c <= '9')))
				return false;
		}
		return true;
	}

	private ModelElementCompletion adjust(ModelElementCompletion c) {
		if (isAsciiName(c.displayName))
			return c;
		else
			return new ModelElementCompletion(c.completionType, '\'' + c.displayName + '\'', c.parent);
	}

	private String adjust(String patch) {
		return patch.replace("${cursor}", "");
	}
}
