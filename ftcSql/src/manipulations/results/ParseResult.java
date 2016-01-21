package manipulations.results;

import org.cg.common.util.OptionalUtil;

import com.google.common.base.Optional;

public class ParseResult {
	public final Optional<String> problemsEncountered;

	public ParseResult(String problemsEncountered) {
		this.problemsEncountered = OptionalUtil.of(problemsEncountered);
	}
}