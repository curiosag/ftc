package org.cg.ftc.ftcClientJava;

public class Client {

	public static void main(String[] args) {	
		switch (args.length) {
		case 0:
			GuiClient.start();
			break;

		case 1:
			System.exit(CmdLineClient.start(args[0], null));
	
		case 2:
			System.exit(CmdLineClient.start(args[0], args[1]));
			
		default:
			System.err.println("usage: client.jar <input file name> [<output file name>]");
			System.exit(1);
		}

	}

}
