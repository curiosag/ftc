package org.cg.eclipse.plugins.ftc.syntaxstyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import org.cg.common.check.Check;
import org.cg.common.util.Op;
import org.cg.eclipse.plugins.ftc.FtcSourceViewer;
import org.cg.eclipse.plugins.ftc.MessageConsoleLogger;
import org.cg.eclipse.plugins.ftc.PluginConst;
import org.cg.eclipse.plugins.ftc.glue.FtcPluginClient;
import org.cg.eclipse.plugins.ftc.preference.SyntaxStyle;
import org.cg.eclipse.plugins.ftc.preference.SyntaxStyleSettings;
import org.cg.ftc.shared.interfaces.SyntaxElement;
import org.cg.ftc.shared.interfaces.SyntaxElementType;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.texteditor.AbstractMarkerAnnotationModel;

public class SyntaxColoring extends Observable {

	private static final boolean debug = false;
	private List<SyntaxElement> tokens = new ArrayList<SyntaxElement>();
	private HashMap<SyntaxElementType, SyntaxStyle> syntaxStyles = new HashMap<SyntaxElementType, SyntaxStyle>();
	private final String TOKEN_TEXT = "token_text";

	private FtcSourceViewer sourceViewer;
	private String text = "";

	private final SyntaxStyle defaultStyle = new SyntaxStyle("", SyntaxElementType.unknown, true, new RGB(0, 0, 0),
			false, false, false, false);

	public SyntaxColoring(FtcSourceViewer sourceViewer) {
		this.sourceViewer = sourceViewer;
		reloadStyles();
	}

	private IDocument getDocument() {
		IDocument result = sourceViewer.getDocument();
		Check.notNull(result);
		return result;
	};

	private IResource getResource() {
		IResource result = sourceViewer.getResource();
		Check.notNull(result);
		return result;
	};

	private AbstractMarkerAnnotationModel getAnnotationModel() {
		IAnnotationModel m = sourceViewer.getAnnotationModel();
		Check.isTrue(m instanceof AbstractMarkerAnnotationModel);
		return (AbstractMarkerAnnotationModel) m;
	};

	private SyntaxElementType mapType(SyntaxElementType type) {
		if (Op.in(type, SyntaxElementType.alias, SyntaxElementType.viewName))
			return SyntaxElementType.tableName;
		else
			return type;
	}

	public SyntaxStyle getStyle(SyntaxElementType type) {
		SyntaxStyle result = syntaxStyles.get(mapType(type));
		if (result == null || ! result.enable)
			result = defaultStyle;

		return result;
	}

	/**
	 * parses the text and populates caches for style ranges and markers
	 * 
	 * @param text
	 *            the text to parse
	 */
	public synchronized void setText(String text) {
		this.text = text;
		tokens = FtcPluginClient.getDefault().getSyntaxElementSource().get(text);
	}

	/**
	 * return get styles within a range. call setText() before to populate style
	 * caches.
	 * 
	 * @param idxFrom
	 *            range from
	 * @param idxTo
	 *            range to
	 * @return
	 */

	public synchronized List<StyleRange> getStyles(int idxFrom, int idxTo) {
		ArrayList<StyleRange> result = new ArrayList<StyleRange>();

		for (SyntaxElement t : tokens)
			if (t.from >= idxFrom && t.to <= idxTo) {
				SyntaxStyle c = getStyle(t.type);
				result.add(getStyleRange(t, c));
			}

		addStructuralTokens(result, idxFrom, idxTo);

		Collections.sort(result, new Comparator<StyleRange>() {

			@Override
			public int compare(StyleRange o1, StyleRange o2) {
				return o1.start - o2.start;
			}
		});

		if (debug)
			debugRanges(result);

		return result;
	}

	private void debugRanges(ArrayList<StyleRange> result) {
		StringBuilder b = new StringBuilder();
		for (StyleRange r : result)
			b.append(String.format("%d %d %s\n", r.start, r.length, r.token.value));
		MessageConsoleLogger.getDefault().Info(b.toString());
	}

	// "=" as operator is provided as token by the parser
	// except if it appears in join statements
	private static char eqChar = '=';
	private static String structuralChars = "(),.;*" + eqChar;

	/**
	 * augments ranges by structural characters. the clean version would be to
	 * change the parser and the resulting token list, but the regression would
	 * be rather wide spread
	 * 
	 * @param ranges
	 *            current style ranges. get
	 * @param idxFrom
	 * @param idxTo
	 */
	private void addStructuralTokens(ArrayList<StyleRange> ranges, int idxFrom, int idxTo) {
		// usually structuralChars are not contained in the token list
		// except if they appear in a sequence of error tokens
		// so for this case they must not be added in a duplicate fashion
		List<Integer> indices = new ArrayList<Integer>();
		for (StyleRange r : ranges)
			indices.add(r.start);
		Collections.sort(indices);

		for (int i = idxFrom; i < Math.min(idxTo, text.length()); i++) {
			String current = String.valueOf(text.charAt(i));
			if (Collections.binarySearch(indices, i) < 0 && (structuralChars.indexOf(text.charAt(i)) >= 0))
				ranges.add(
						getStyleRange(SyntaxElement.create(current, i, i, 0, SyntaxElementType.unknown), defaultStyle));
		}
	}

	private StyleRange getStyleRange(SyntaxElement t, SyntaxStyle c) {
		return new StyleRange(t.from, (t.to - t.from) + 1, c, t);
	}

	private void createMarker(IResource r, SyntaxElement t) {
		if (t.type == SyntaxElementType.error)
			createMarker(PluginConst.MARKER_TYPE_SYNTAXERROR, r, t, IMarker.SEVERITY_ERROR,
					String.format("Invalid token '%s'", t.value));
		else if (t.hasSemanticError())
			createMarker(PluginConst.MARKER_TYPE_MODELERROR, r, t, IMarker.SEVERITY_WARNING,
					String.format("Invalid name '%s'", t.value));
	}

	private void updateMarkers() {
		try {
			getAnnotationModel().commit(getDocument());
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized void resetMarkers() {

		setText(getDocument().get());
		updateMarkers();

		IResource r = getResource();

		IMarker[] markers;
		try {
			markers = r.findMarkers("org.eclipse.core.resources.problemmarker", true, 0);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}

		hdlDeleted(tokens, markers, r);
		hdlAdded(tokens, markers, r);

	}

	private void hdlDeleted(List<SyntaxElement> tokens, IMarker[] markers, IResource r) {
		HashMap<Integer, SyntaxElement> tokenMap = new HashMap<Integer, SyntaxElement>();
		for (SyntaxElement e : tokens)
			tokenMap.put(e.from, e);

		for (int i = 0; i < markers.length; i++) {
			SyntaxElement token = tokenMap.get(markers[i].getAttribute(IMarker.CHAR_START, -1));
			if (token == null || markerChanged(token, markers[i]))
				try {
					markers[i].delete();
				} catch (CoreException e) {
				}
		}

	}

	private boolean markerChanged(SyntaxElement token, IMarker marker) {
		boolean result = true;
		try {
			result = !token.value.equals(marker.getAttribute(TOKEN_TEXT, ""))
					|| (!getMarkerType(token).equals(marker.getType()));
		} catch (CoreException e) {

		}

		return result;
	}

	private String getMarkerType(SyntaxElement token) {
		if (token.type == SyntaxElementType.error)
			return PluginConst.MARKER_TYPE_SYNTAXERROR;
		if (token.hasSemanticError())
			return PluginConst.MARKER_TYPE_MODELERROR;

		return "no marker";

	}

	private void hdlAdded(List<SyntaxElement> tokens, IMarker[] markers, IResource r) {
		HashMap<Integer, IMarker> markerMap = new HashMap<Integer, IMarker>();
		for (int i = 0; i < markers.length; i++)
			markerMap.put(markers[i].getAttribute(IMarker.CHAR_START, -1), markers[i]);

		for (SyntaxElement e : tokens) {
			IMarker marker = markerMap.get(e.from);

			if (marker == null && (e.type == SyntaxElementType.error || e.hasSemanticError()))
				createMarker(r, e);
		}

	}

	private void createMarker(String markerType, IResource r, SyntaxElement t, int severity, String message) {
		try {
			IMarker m = r.createMarker(markerType);

			m.setAttribute(IMarker.SEVERITY, severity);
			m.setAttribute(IMarker.CHAR_START, t.from);
			m.setAttribute(IMarker.CHAR_END, t.to + 1);
			m.setAttribute(IMarker.MESSAGE, message);
			m.setAttribute(TOKEN_TEXT, t.value);
			m.setAttribute(IMarker.LINE_NUMBER, getDocument().getLineOfOffset(t.from));

		} catch (CoreException e1) {
			e1.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

	}

	/**
	 * reloads styles from SyntaxStyleSettings
	 */
	public void reloadStyles() {
		List<SyntaxStyle> styleSettings = SyntaxStyleSettings.getDefault().get();
		syntaxStyles.clear();
		for (SyntaxStyle s : styleSettings)
			syntaxStyles.put(s.type, s);
		hdlChanged();
	}

	/**
	 * observer logic
	 */
	private void hdlChanged() {
		setChanged();
		notifyObservers(this);
	}

}
