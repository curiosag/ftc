package manipulations;

import org.antlr.v4.runtime.*;

public class VerboseErrorListener extends BaseErrorListener {

	private final StringBuilder sb = new StringBuilder();
	
	public String getErrors() {
		if (sb.length() == 0)
			return null;
		else
			return sb.toString();
	}
	
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
			String msg, RecognitionException e) {
		String error = "line " + line + ":" + charPositionInLine + " " + msg + '\n'
				+ underlineError(recognizer, (Token) offendingSymbol, line, charPositionInLine);
		sb.append(error);
	}

	protected String underlineError(Recognizer<?, ?> recognizer, Token offendingToken, int line,
			int charPositionInLine) {
		String result = "";
		CommonTokenStream tokens = (CommonTokenStream) recognizer.getInputStream();
		String input = tokens.getTokenSource().getInputStream().toString();
		String[] lines = input.split("\n");
		String errorLine = lines[line - 1];
		result = errorLine + '\n';
		for (int i = 0; i < charPositionInLine; i++)
			result = result + " ";
		int start = offendingToken.getStartIndex();
		int stop = offendingToken.getStopIndex();
		if (start >= 0 && stop >= 0) {
			for (int i = start; i <= stop; i++)
				result = result + '^';
		}
		return result;
	}

}
