package org.cg.common.http;

public class HttpResult {

	public final HttpStatus responseCode;
	public final String resultData;
	
	public HttpResult(HttpStatus responseCode, String resultData){
		this.responseCode = responseCode;
		this.resultData = resultData;
	}
}
