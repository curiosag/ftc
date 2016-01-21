package manipulations;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.IntervalSet;

public interface OnError {

		public void notifyOnError(Token offendingToken, Token missingToken, IntervalSet tokensExpected);
	
}
