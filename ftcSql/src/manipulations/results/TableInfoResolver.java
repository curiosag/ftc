package manipulations.results;

import java.util.List;

import org.cg.ftc.shared.structures.TableInfo;

import com.google.common.base.Optional;

public interface TableInfoResolver {
	Optional<TableInfo> getTableInfo(String nameOrId);

	List<TableInfo> listTables();
}
