package org.cg.common.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

// following org.apache.http.HttpStatus
public enum HttpStatus {

	SC_ACCEPTED, SC_BAD_GATEWAY, SC_BAD_REQUEST, SC_CONFLICT, SC_CONTINUE, SC_CREATED, SC_EXPECTATION_FAILED, SC_FAILED_DEPENDENCY, SC_FORBIDDEN, SC_GATEWAY_TIMEOUT, SC_GONE, SC_HTTP_VERSION_NOT_SUPPORTED, SC_INSUFFICIENT_SPACE_ON_RESOURCE, SC_INSUFFICIENT_STORAGE, SC_INTERNAL_SERVER_ERROR, SC_LENGTH_REQUIRED, SC_LOCKED, SC_METHOD_FAILURE, SC_METHOD_NOT_ALLOWED, SC_MOVED_PERMANENTLY, SC_MOVED_TEMPORARILY, SC_MULTIPLE_CHOICES, SC_MULTI_STATUS, SC_NO_CONTENT, SC_NON_AUTHORITATIVE_INFORMATION, SC_NOT_ACCEPTABLE, SC_NOT_FOUND, SC_NOT_IMPLEMENTED, SC_NOT_MODIFIED, SC_OK, SC_PARTIAL_CONTENT, SC_PAYMENT_REQUIRED, SC_PRECONDITION_FAILED, SC_PROCESSING, SC_PROXY_AUTHENTICATION_REQUIRED, SC_REQUESTED_RANGE_NOT_SATISFIABLE, SC_REQUEST_TIMEOUT, SC_REQUEST_TOO_LONG, SC_REQUEST_URI_TOO_LONG, SC_RESET_CONTENT, SC_SEE_OTHER, SC_SERVICE_UNAVAILABLE, SC_SWITCHING_PROTOCOLS, SC_TEMPORARY_REDIRECT, SC_UNAUTHORIZED, SC_UNPROCESSABLE_ENTITY, SC_UNSUPPORTED_MEDIA_TYPE, SC_USE_PROXY;

	static{
		createMap();
	}

	private final static BiMap<Integer, HttpStatus> statusMap = createMap();

	private static BiMap<Integer, HttpStatus> createMap() {
		final Map<Integer, HttpStatus> entries = new HashMap<Integer, HttpStatus>();
		
	    put(entries, SC_ACCEPTED, 202);
	    put(entries, SC_BAD_GATEWAY, 502);
	    put(entries, SC_BAD_REQUEST, 400);
	    put(entries, SC_CONFLICT, 409);
	    put(entries, SC_CONTINUE, 100);
	    put(entries, SC_CREATED, 201);
	    put(entries, SC_EXPECTATION_FAILED, 417);
	    put(entries, SC_FAILED_DEPENDENCY, 424);
	    put(entries, SC_FORBIDDEN, 403);
	    put(entries, SC_GATEWAY_TIMEOUT, 504);
	    put(entries, SC_GONE, 410);
	    put(entries, SC_HTTP_VERSION_NOT_SUPPORTED, 505);
	    put(entries, SC_INSUFFICIENT_SPACE_ON_RESOURCE, 419);
	    put(entries, SC_INSUFFICIENT_STORAGE, 507);
	    put(entries, SC_INTERNAL_SERVER_ERROR, 500);
	    put(entries, SC_LENGTH_REQUIRED, 411);
	    put(entries, SC_LOCKED, 423);
	    put(entries, SC_METHOD_FAILURE, 420);
	    put(entries, SC_METHOD_NOT_ALLOWED, 405);
	    put(entries, SC_MOVED_PERMANENTLY, 301);
	    put(entries, SC_MOVED_TEMPORARILY, 302);
	    put(entries, SC_MULTIPLE_CHOICES, 300);
	    put(entries, SC_MULTI_STATUS, 207);
	    put(entries, SC_NO_CONTENT, 204);
	    put(entries, SC_NON_AUTHORITATIVE_INFORMATION, 203);
	    put(entries, SC_NOT_ACCEPTABLE, 406);
	    put(entries, SC_NOT_FOUND, 404);
	    put(entries, SC_NOT_IMPLEMENTED, 501);
	    put(entries, SC_NOT_MODIFIED, 304);
	    put(entries, SC_OK, 200);
	    put(entries, SC_PARTIAL_CONTENT, 206);
	    put(entries, SC_PAYMENT_REQUIRED, 402);
	    put(entries, SC_PRECONDITION_FAILED, 412);
	    put(entries, SC_PROCESSING, 102);
	    put(entries, SC_PROXY_AUTHENTICATION_REQUIRED, 407);
	    put(entries, SC_REQUESTED_RANGE_NOT_SATISFIABLE, 416);
	    put(entries, SC_REQUEST_TIMEOUT, 408);
	    put(entries, SC_REQUEST_TOO_LONG, 413);
	    put(entries, SC_REQUEST_URI_TOO_LONG, 414);
	    put(entries, SC_RESET_CONTENT, 205);
	    put(entries, SC_SEE_OTHER, 303);
	    put(entries, SC_SERVICE_UNAVAILABLE, 503);
	    put(entries, SC_SWITCHING_PROTOCOLS, 101);
	    put(entries, SC_TEMPORARY_REDIRECT, 307);
	    put(entries, SC_UNAUTHORIZED, 401);
	    put(entries, SC_UNPROCESSABLE_ENTITY, 422);
	    put(entries, SC_UNSUPPORTED_MEDIA_TYPE, 415);
	    put(entries, SC_USE_PROXY, 305);
	    
		return ImmutableBiMap.copyOf(Collections.unmodifiableMap(entries));
	}

	private static void put(Map<Integer, HttpStatus> entries, HttpStatus status, Integer code) {
		entries.put(code, status);
	}

	public static Optional<HttpStatus> decode(int statusCode) {
		return Optional.fromNullable(statusMap.get(statusCode));
	}

	public static Optional<Integer> encode(HttpStatus httpStatus) {
		return Optional.fromNullable(statusMap.inverse().get(httpStatus));
	}
}
