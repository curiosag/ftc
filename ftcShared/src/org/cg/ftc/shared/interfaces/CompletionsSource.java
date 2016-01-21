package org.cg.ftc.shared.interfaces;

import org.cg.ftc.shared.structures.Completions;

public interface CompletionsSource {
	public Completions get(String query, int cursorPos);
}
