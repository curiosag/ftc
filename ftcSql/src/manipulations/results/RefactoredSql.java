package manipulations.results;

public class RefactoredSql extends ParseResult {
	public final String original;
	public final String refactored;

	public RefactoredSql(String original, String refactored, String problemsEncountered) {
		super(problemsEncountered);

		this.original = original;
		this.refactored = refactored;
	}

}