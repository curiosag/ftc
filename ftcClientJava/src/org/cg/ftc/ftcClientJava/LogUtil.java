package org.cg.ftc.ftcClientJava;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {

	public static void addLogLine(TextModel resultText, String info) {
		resultText.setValue(addLogLine(resultText.getValue(), info));
	}

	public static String addLogLine(String currentLog, String info)
	{
		Format formatter = new SimpleDateFormat("HH:mm:ss");
		String date = formatter.format(new Date());

		return prune(currentLog) + "\n" + date + " " + info;
	}
	
	public static String prune(String value) {
		if (!value.endsWith("\n"))
			value = value + "\n";

		return value.substring(Math.max(0, value.length() - org.cg.ftc.shared.uglySmallThings.Const.MAX_LOGSIZE));
	}
	
}
