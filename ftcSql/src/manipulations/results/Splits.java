package manipulations.results;

import java.util.List;

public class Splits extends ParseResult {
	public final List<String> splits;

	public Splits(List<String> splits, String problemsEncountered) {
		super(problemsEncountered);
		this.splits = splits;
	}
}
