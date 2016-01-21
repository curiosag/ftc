package manipulations;

import java.util.HashMap;
import java.util.Map;

import org.cg.common.check.Check;
import org.cg.ftc.shared.uglySmallThings.Const;

public enum NameRecognitionState {

	INITIAL {
		@Override
		public NameRecognitionState next(String input) {
			Check.notEmpty(input);

			NameRecognitionState result;
			if (isSeparator(input))
				result = QUALIFIER;
			else if (expressionOperator(input))
				result = EXPR_EMPTY;
			else
				result = NAME1;

			debug(input, this, result);
			return result;
		}
	},

	NAME1 {
		@Override
		public NameRecognitionState next(String input) {
			Check.notEmpty(input);

			NameRecognitionState result;
			if (isSeparator(input))
				result = QUALIFIER;
			else if (expressionOperator(input))
				result = EXPR_NAME1;
			else
				result = ERROR;

			debug(input, this, result);
			return result;
		}
	},

	QUALIFIER {
		@Override
		public NameRecognitionState next(String input) {
			Check.notEmpty(input);

			NameRecognitionState result;
			if (isSeparator(input))
				result = ERROR;
			else if (expressionOperator(input))
				result = EXPR_QUALIFIER;
			else
				result = NAME2;

			debug(input, this, result);
			return result;
		}

	},

	NAME2 {
		@Override
		public NameRecognitionState next(String input) {
			NameRecognitionState result;

			if (expressionOperator(input))
				result = EXPR_NAME2;
			else
				result = ERROR;
			debug(input, this, result);
			return result;
		}
	},

	EXPR_EMPTY {
		@Override
		public NameRecognitionState next(String input) {
			NameRecognitionState result = EXPR_EMPTY;
			debug(input, this, result);
			return result;
		}
	},

	// this does an extra job of detecting names after oparators, which 
	// does not occur in future tables sql, one can find only values
	EXPR_NAME1 {
		@Override
		public NameRecognitionState next(String input) {
			NameRecognitionState result = EXPR_NAME1;
			debug(input, this, result);
			return result;
		}
	},

	EXPR_QUALIFIER {
		@Override
		public NameRecognitionState next(String input) {
			NameRecognitionState result = EXPR_QUALIFIER;
			debug(input, this, result);
			return result;
		}
	},

	EXPR_NAME2 {
		@Override
		public NameRecognitionState next(String input) {
			NameRecognitionState result = EXPR_NAME2;
			debug(input, this, result);
			return result;
		}
	},

	ERROR {
		@Override
		public NameRecognitionState next(String input) {
			NameRecognitionState result = ERROR;
			debug(input, this, result);
			return result;
		}
	};

	private static boolean debug = Const.debugNameRecognition;
	
	final static Map<String, Integer> leftSideEndIndicators = new HashMap<String, Integer>();

	private static void addIndicators(String... indicators) {
		for (String i : indicators)
			leftSideEndIndicators.put(i, 0);
	}

	static {
		String comma_st_intersects_stmt = ",";
		addIndicators(comma_st_intersects_stmt, "=", "<", ">", "<=", ">=", "like", "matches", "contains", "starts",
				"ends", "does", "not", "in", "between");
	}

	private static boolean expressionOperator(String input) {
		return leftSideEndIndicators.containsKey(input.toLowerCase());
	}

	public abstract NameRecognitionState next(String input);


	private static void debug(String input, NameRecognitionState from, NameRecognitionState to) {
		if (debug)
			System.out.println(String.format("input: %s from: %s to: %s ", input, from.name(), to.name()));
	}

	public boolean in(NameRecognitionState... states) {
		for (NameRecognitionState state : states)
			if (this == state)
				return true;
		return false;
	}

	private static boolean isSeparator(String s) {
		return s != null && (s.equals(".") || s.toUpperCase().equals("AS"));
	}

}
