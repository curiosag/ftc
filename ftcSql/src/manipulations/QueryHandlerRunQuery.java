package manipulations;

import org.cg.common.check.Check;
import org.cg.ftc.shared.interfaces.Connector;

public class QueryHandlerRunQuery {

	public QueryHandlerRunQuery(Connector c, String query, QueryHandlerRunQueryCallback onFinished) {
		Check.notNull(onFinished);
		c.fetch(query);
		onFinished.onFinished();
	}
	
}
