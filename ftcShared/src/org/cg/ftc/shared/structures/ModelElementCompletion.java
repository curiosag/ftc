package org.cg.ftc.shared.structures;

import org.cg.ftc.shared.interfaces.SqlCompletionType;

public class ModelElementCompletion extends AbstractCompletion {

	public ModelElementCompletion(SqlCompletionType completionType, String displayName, AbstractCompletion parent) {
		super(completionType, displayName, parent);
	}

	@Override
	public String getPatch() {
		return displayName;
	}
	
}
