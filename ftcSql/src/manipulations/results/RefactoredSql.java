package manipulations.results;

import java.util.List;

public class RefactoredSql extends ParseResult {
	public final String original;
	public final String refactored;

	public final int keywordWhereStartIndex;
	public final List<String> tableIds;

	public RefactoredSql(List<String> tableIds, String original, String refactored, int kWhereStartIndex,
			String problemsEncountered) {
		super(problemsEncountered);

		this.tableIds = tableIds;
		this.original = original;
		this.refactored = refactored;
		this.keywordWhereStartIndex = considerShiftBroughtByTableId(original, refactored, kWhereStartIndex);
	}

	private int considerShiftBroughtByTableId(String original, String refactored, int kWhereStartIndex) {
		return kWhereStartIndex + (refactored.length() - original.length());
	}
}