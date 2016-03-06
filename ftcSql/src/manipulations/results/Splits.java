package manipulations.results;

import java.util.List;

import manipulations.Split;

public class Splits extends ParseResult {
	public final List<Split> splits;

	public Splits(List<Split> splits, String problemsEncountered) {
		super(problemsEncountered);
		this.splits = splits;
	}
}
