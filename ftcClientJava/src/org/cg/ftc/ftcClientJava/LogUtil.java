package org.cg.ftc.ftcClientJava;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {

	private final static Format formatter = new SimpleDateFormat("HH:mm:ss"); 
	
	public static void addLogLine(TextModel resultText, String info) {
		resultText.append(getLogLine(info));
	}

	public static String getLogLine(String info)
	{
		String date = formatter.format(new Date());

		return date + " " + info + "\n";
	}
	
	
}
