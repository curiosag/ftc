package org.cg.ftc.shared.interfaces;

import java.util.List;

import org.antlr.v4.runtime.Token;
import org.cg.common.check.Check;

public class SyntaxElement implements Comparable {

	public final int from;
	public final int to;
	public final int tokenIndex;
	public final SyntaxElementType type;
	public final String value;
	private boolean semanticError = false;

	private SyntaxElement(String value, int from, int to, int tokenIndex, SyntaxElementType type) {
		this.from = from;
		this.to = to;
		this.tokenIndex = tokenIndex;
		this.type = type;
		this.value = value;
	}

	public static SyntaxElement create(Token t, SyntaxElementType type) {
		return new SyntaxElement(t.getText(), t.getStartIndex(), t.getStopIndex(), t.getTokenIndex(), type);
	}
	
	public static SyntaxElement create(String value, int from, int to, int tokenIndex, SyntaxElementType type) {
		return new SyntaxElement(value, from, to, tokenIndex, type);
	}

	public static boolean findCaseInsensitive(List<SyntaxElement> l, String value) {
		String lcValue = value.toLowerCase();
		for (SyntaxElement e : l)
			if (e.value.toLowerCase().equals(lcValue))
				return true;
		return false;
	}

	public boolean hasSemanticError() {
		return semanticError;
	}

	public void setSemanticError(boolean semanticError) {
		this.semanticError = semanticError;

	}

	@Override
	public int compareTo(Object o) {
		Check.isTrue(o instanceof SyntaxElement);
		return tokenIndex -  ((SyntaxElement) o).tokenIndex;
	}

}
