package main.java.fusiontables;

import org.cg.common.util.StringUtil;

public class AuthInfo {
	public final String clientId;
	public final String clientSecret;

	public AuthInfo(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public boolean credentialsPlausible() {
		return !(StringUtil.emptyOrNull(clientId) || StringUtil.emptyOrNull(clientSecret));
	}

}
