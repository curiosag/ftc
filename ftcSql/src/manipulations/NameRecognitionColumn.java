package manipulations;

import org.cg.common.structures.OrderedIntTuple;

import com.google.common.base.Optional;

public class NameRecognitionColumn extends NameRecognition {
	
	public Optional<String> ColumnName() {
		if (colNameIsName1())
			return getName1();
		else if (colNameIsName2())
			return getName2();
		else
			return Optional.absent();
		
	}
	
	public Optional<OrderedIntTuple> BoundariesColumnName()
	{
		if (colNameIsName1())
			return getBoundaries1();
		else if (colNameIsName2())
			return getBoundaries2();
		else
			return Optional.absent();
	}
	
	public Optional<String> TableName() {
		if (hasTableName()) {
			return getName1();
		} else
			return Optional.absent();
	}
	
	public Optional<OrderedIntTuple> BoundariesTableName() {
		if (hasTableName()) {
			return getBoundaries1();
		} else
			return Optional.absent();
	}

	private boolean hasTableName() {
		return state.in(NameRecognitionState.NAME2, NameRecognitionState.QUALIFIER, NameRecognitionState.EXPR_NAME2, NameRecognitionState.EXPR_QUALIFIER);
	}
	

	private boolean colNameIsName2() {
		return state.in(NameRecognitionState.NAME2, NameRecognitionState.EXPR_NAME2);
	}

	private boolean colNameIsName1() {
		return state.in(NameRecognitionState.NAME1, NameRecognitionState.EXPR_NAME1);
	}
	
}
