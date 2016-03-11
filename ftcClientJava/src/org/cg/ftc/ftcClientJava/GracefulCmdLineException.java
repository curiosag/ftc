package org.cg.ftc.ftcClientJava;

public class GracefulCmdLineException extends RuntimeException {
	private static final long serialVersionUID = 5240258989372421335L;

	public GracefulCmdLineException(String reason) {
		super(reason);
	}

}
