package test;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;

public class MockToken implements Token {

	private final String text;
	public MockToken(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public int getType() {
		throw new RuntimeException("don't call!");
	}

	@Override
	public int getLine() {
		throw new RuntimeException("don't call!");
	}

	@Override
	public int getCharPositionInLine() {
		throw new RuntimeException("don't call!");
	}

	@Override
	public int getChannel() {
		throw new RuntimeException("don't call!");
	}

	@Override
	public int getTokenIndex() {
		throw new RuntimeException("don't call!");
	}

	@Override
	public int getStartIndex() {
		return 0;
	}

	@Override
	public int getStopIndex() {
		return 0;
	}

	@Override
	public TokenSource getTokenSource() {
		throw new RuntimeException("don't call!");
	}

	@Override
	public CharStream getInputStream() {
		throw new RuntimeException("don't call!");
	}

}
