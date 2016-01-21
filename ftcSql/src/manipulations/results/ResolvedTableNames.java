package manipulations.results;

import com.google.common.base.Optional;

public class ResolvedTableNames extends ParseResult {
	public final String nameFrom;
	public final Optional<String> idFrom;
	public final String nameTo;

	public ResolvedTableNames(String nameFrom, Optional<String> idFrom, String nameTo, String problemsEncountered) {
		super(problemsEncountered);
		this.nameFrom = nameFrom;
		this.idFrom = idFrom;
		this.nameTo = nameTo;
	}

}