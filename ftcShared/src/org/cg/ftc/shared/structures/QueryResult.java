package org.cg.ftc.shared.structures;

import javax.swing.table.TableModel;

import org.cg.common.check.Check;
import org.cg.common.http.HttpStatus;

import com.google.common.base.Optional;

public class QueryResult {

	public final HttpStatus status; 
	public final Optional<TableModel> data;
	public final Optional<String> message;
	
	public QueryResult(HttpStatus status, TableModel data, String message)
	{
		Check.notNull(status);
		this.status = status;
		this.data = Optional.fromNullable(data);
		this.message = Optional.fromNullable(message);
	}
	
}
