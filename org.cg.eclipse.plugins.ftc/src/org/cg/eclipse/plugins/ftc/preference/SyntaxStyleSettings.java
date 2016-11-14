package org.cg.eclipse.plugins.ftc.preference;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.cg.common.check.Check;
import org.cg.eclipse.plugins.ftc.glue.FtcPluginClient;
import org.cg.eclipse.plugins.ftc.glue.FtcPreferenceStore;
import org.cg.ftc.shared.interfaces.SyntaxElementType;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;

public class SyntaxStyleSettings {

	public List<SyntaxStyle> defaultStyles = new LinkedList<SyntaxStyle>();

	private static SyntaxStyleSettings _default = null;
	private final IPreferenceStore store;
	
	public static SyntaxStyleSettings getDefault() {
		if (_default == null)
			_default = new SyntaxStyleSettings(FtcPluginClient.getDefault().getPreferenceStore());
		return _default;

	}

	private SyntaxStyleSettings(IPreferenceStore store) {
		Check.notNull(store);
		
		this.store = store;
		
		RGB blue = new RGB(0, 0, 255);
		RGB green = new RGB(0, 155, 0);
		RGB red = new RGB(200, 0, 0);
		RGB black = new RGB(0, 0, 0);
		RGB smthg = new RGB(127, 0, 85);

		defaultStyles.add(
				new SyntaxStyle("Sql keyword", SyntaxElementType.sql_keyword, true, smthg, true, false, false, false));
		defaultStyles.add(
				new SyntaxStyle("Column name", SyntaxElementType.columnName, true, blue, false, false, false, false));
		defaultStyles.add(
				new SyntaxStyle("Table, view, alias", SyntaxElementType.tableName, true, blue, false, false, false, false));
		defaultStyles
				.add(new SyntaxStyle("Operator", SyntaxElementType.operator, true, black, false, false, false, false));
		defaultStyles.add(new SyntaxStyle("String literal", SyntaxElementType.stringLiteral, true, green, false, false,
				false, false));
		defaultStyles.add(new SyntaxStyle("Numeric literal", SyntaxElementType.numericLiteral, true, red, false, false,
				false, false));
		defaultStyles
				.add(new SyntaxStyle("Comment", SyntaxElementType.comment, true, green, false, true, false, false));
		setDefaults(store);
	}

	private void setDefaults(IPreferenceStore store) {
		for (SyntaxStyle s : defaultStyles) {
			store.setDefault(getStyleKey(StyleAspect.enable, s.displayName), s.enable);
			String color = StringConverter.asString(s.color);
			store.setDefault(getStyleKey(StyleAspect.color, s.displayName), color);
			store.setDefault(getStyleKey(StyleAspect.bold, s.displayName), s.bold);
			store.setDefault(getStyleKey(StyleAspect.italic, s.displayName), s.italic);
			store.setDefault(getStyleKey(StyleAspect.strikethrough, s.displayName), s.strikethrough);
			store.setDefault(getStyleKey(StyleAspect.underline, s.displayName), s.underline);
		}
	}

	private String getStyleKey(StyleAspect aspect, String name) {
		return FtcPreferenceStore.getStyleKey(aspect, name);
	}

	public List<SyntaxStyle> get() {
		ArrayList<SyntaxStyle> result = new ArrayList<SyntaxStyle>();
		setDefaults(store);
		for (SyntaxStyle s : defaultStyles) {
			boolean enable = store.getBoolean(getStyleKey(StyleAspect.enable, s.displayName));
			RGB color = StringConverter.asRGB(store.getString(getStyleKey(StyleAspect.color, s.displayName)));
			boolean bold = store.getBoolean(getStyleKey(StyleAspect.bold, s.displayName));
			;
			boolean italic = store.getBoolean(getStyleKey(StyleAspect.italic, s.displayName));
			;
			boolean strikethrough = store.getBoolean(getStyleKey(StyleAspect.strikethrough, s.displayName));
			;
			boolean underline = store.getBoolean(getStyleKey(StyleAspect.underline, s.displayName));
			;
			result.add(new SyntaxStyle(s.displayName, s.type, enable, color, bold, italic, strikethrough, underline));
		}
		return result;
	}

}
