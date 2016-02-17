package main.java.fusiontables;

import org.apache.http.NameValuePair;

public class BaseNameValuePair implements NameValuePair {

	private final String name; 
	private final String value;
	
	public BaseNameValuePair(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String getName(){
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

}
