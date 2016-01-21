package manipulations;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.cg.common.check.Check;

public class RecognitionErrorStrategy extends DefaultErrorStrategy {

	private final OnError onErrorCallback;

	public RecognitionErrorStrategy(OnError onErrorCallback) {
		Check.notNull(onErrorCallback);
		this.onErrorCallback = onErrorCallback;
	}

	@Override
	public void reportInputMismatch(Parser recognizer, InputMismatchException e) throws RecognitionException {
		onErrorCallback.notifyOnError(e.getOffendingToken(), null, e.getExpectedTokens());
	}

	@Override
	public void reportMissingToken(Parser recognizer) {
		onErrorCallback.notifyOnError(null, recognizer.getCurrentToken(), getExpectedTokens(recognizer));
	}

}
