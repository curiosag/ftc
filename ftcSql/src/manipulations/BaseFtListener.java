package manipulations;

import org.cg.ftc.parser.FusionTablesSqlBaseListener;

public class BaseFtListener extends FusionTablesSqlBaseListener {

	protected boolean isGenericError(String errorString) {
		return errorString.startsWith("<") && errorString.endsWith(">");
	}

}