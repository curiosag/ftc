package org.cg.ftc.shared.structures;

import java.util.prefs.Preferences;

public class ClientSettings {
	public String clientId;
	public String clientSecret;
	public String pathScriptFile;
	public String pathCsvFile;
	public int x;
	public int y;
	public int width;
	public int height;
	public int dividerLocationH;
	public int dividerLocationV;
	public int defaultQueryLimit;
	public int authTimeout;
	public boolean lineNumbersEnabled;
	public boolean highlightCurrentLine;
	public boolean tabLines;
	public String editorThemeXml;

	public final static String keyClientId = "clientId";
	public final static String keyClientSecret = "clientSecret";
	public final static String keyPathScriptFile = "pathScriptFile";
	public final static String keyPathCsvFile = "pathCsvFile";
	public final static String keyX = "x";
	public final static String keyY = "y";
	public final static String keyWidth = "width";
	public final static String keyHeight = "height";
	public final static String keySeparatorH = "separatorH";
	public final static String keySeparatorV = "separatorV";
	public final static String keyDefaultQueryLimit = "defaultQueryLimit";
	public final static String keyAuthTimeout = "authTimeout";
	public final static String keyLineNumbers = "lineNumbers";
	public final static String keyLineHighlight = "lineHighlight";
	public final static String keyTabLines = "tabLines";
	public final static String keyEditorThemeXml = "editorThemeXml";

	private final Preferences prefs;
	
	private static ClientSettings instance = null;

	public static ClientSettings instance(Class<?> carrierNode) {
		if (instance == null)
			instance = new ClientSettings(carrierNode);
		return instance;
	}

	public void write() {
		prefs.put(keyClientId, clientId);
		prefs.put(keyClientSecret, clientSecret);
		prefs.put(keyPathScriptFile, pathScriptFile);
		prefs.put(keyPathCsvFile, pathCsvFile);
		prefs.putInt(keyX, x);
		prefs.putInt(keyY, y);
		prefs.putInt(keyWidth, width);
		prefs.putInt(keyHeight, height);
		prefs.putInt(keySeparatorH, dividerLocationH);
		prefs.putInt(keySeparatorV, dividerLocationV);
		prefs.putInt(keyDefaultQueryLimit, defaultQueryLimit);
		prefs.putInt(keyAuthTimeout, authTimeout);
		prefs.putBoolean(keyLineNumbers, lineNumbersEnabled);
		prefs.putBoolean(keyLineHighlight, highlightCurrentLine);
		prefs.putBoolean(keyTabLines, tabLines);
		prefs.put(keyEditorThemeXml, editorThemeXml);
	}

	private ClientSettings(Class<?> carrierNode) {
		prefs = Preferences.userNodeForPackage(carrierNode);
		clientId = prefs.get(keyClientId, "");
		clientSecret = prefs.get(keyClientSecret, "");
		pathScriptFile = prefs.get(keyPathScriptFile, "");
		pathCsvFile = prefs.get(keyPathCsvFile, "");
		x = prefs.getInt(keyX, 5);
		y = prefs.getInt(keyY, 5);
		width = prefs.getInt(keyWidth, 1000);
		height = prefs.getInt(keyHeight, 700);
		dividerLocationH = prefs.getInt(keySeparatorH, 450);
		dividerLocationV = prefs.getInt(keySeparatorV, 300);
		defaultQueryLimit = prefs.getInt(keyDefaultQueryLimit, 100);
		authTimeout = prefs.getInt(keyAuthTimeout, 25);
		lineNumbersEnabled = prefs.getBoolean(keyLineNumbers, false);
		highlightCurrentLine = prefs.getBoolean(keyLineHighlight, false);
		tabLines = prefs.getBoolean(keyTabLines, false);
		editorThemeXml = prefs.get(keyEditorThemeXml, "eclipse.xml");
	}

}
