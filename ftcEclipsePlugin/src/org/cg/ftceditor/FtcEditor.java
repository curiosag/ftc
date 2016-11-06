package org.cg.ftceditor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;

import java.util.ArrayList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.RGB;

public class FtcEditor extends TextEditor {

	private ColorManager colorManager;
	private FtcSourceViewerConfiguration sourceViewerConfiguration;

	private int fCaretOffset;
	
	private static FtcEditor instance = null;

	public static FtcEditor getDefault() {
		return instance;
	}

	public int getCaretOffset() {
		return fCaretOffset;
	}

	public String text(){
		return getSourceViewer().getTextWidget().getText();
	}
	
	public FtcEditor() {
		super();
		colorManager = new ColorManager();
		sourceViewerConfiguration = new FtcSourceViewerConfiguration(colorManager);
		setSourceViewerConfiguration(sourceViewerConfiguration);
		FileDocumentProvider p = new FileDocumentProvider();
		setDocumentProvider(p);

		instance = this;
	}

	protected void initializeEditor() {
		super.initializeEditor();
	}

	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		getSourceViewer().getTextWidget().addLineStyleListener(new LineStyleListener() {

			public void lineGetStyle(LineStyleEvent event) {
				getLineStyle(event);
			}
		});

		ISourceViewer sv = getSourceViewer();

		sv.getTextWidget().addExtendedModifyListener(new ExtendedModifyListener() {

			@Override
			public void modifyText(ExtendedModifyEvent e) {

				StyleRange noStyle = new StyleRange(0, sv.getDocument().getLength(), null, null);

				StyledText w = getSourceViewer().getTextWidget();

				w.setStyleRange(noStyle);
				StyleRange style = new StyleRange(e.start, 1, colorManager.getColor(IColorConstants.COMMENT),
						colorManager.getColor(IColorConstants.NUMBER));

				w.setStyleRange(style);

				style = new StyleRange(0, e.start, colorManager.getColor(IColorConstants.NUMBER),
						colorManager.getColor(IColorConstants.COMMENT));

				w.setStyleRange(style);

				// e.start , e.length give range of new text

			}
		});

		sv.getDocument().addDocumentListener(new IDocumentListener() {

			@Override
			public void documentChanged(DocumentEvent e) {
				
			}

			@Override
			public void documentAboutToBeChanged(DocumentEvent e) {
			}
		});

		Object input = getEditorInput();

		Assert.isTrue(input instanceof IFileEditorInput);
		IResource r = ((IFileEditorInput) input).getFile();
		IMarker m;
		try {
			m = r.createMarker("org.cg.ftcparsermarker");

			m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			m.setAttribute(IMarker.CHAR_START, 0);
			m.setAttribute(IMarker.CHAR_END, 10);
			m.setAttribute(IMarker.MESSAGE, "that's an error");
		} catch (CoreException e1) {

			e1.printStackTrace();
		}

		addCaretListener();
	}

	private void addCaretListener() {
		getSourceViewer().getTextWidget().addCaretListener(new CaretListener() {


			@Override
			public void caretMoved(CaretEvent e) {
				fCaretOffset = e.caretOffset;
			}
		});

	}

	protected void getLineStyle(LineStyleEvent event) {

		// IDocument doc = getDocument();

		try {
			// event.lineOffset ... index im doc, at which line starts

			int startIdx = Math.max(event.lineOffset, 10);

			int endIdx = startIdx + event.lineText.length();

			// red wiggle line style for errors

			// int lineNum = doc.getLineOfOffset(event.lineOffset) + 1;
			// if (lineNum < 1) return; returns -1 even if in valid range

			ArrayList<StyleRange> styles = new ArrayList<StyleRange>();
			//System.out.println("Styling line" + event.lineText);

			// by-foot colorization from parsed tokens
			// VGLColorizer.getInstance().colorizeLine(lineNum,
			// event.lineOffset, startNode, styles);
			RGB white = new RGB(128, 128, 128);

			for (int i = startIdx; i < endIdx - 2; i = i + 2) {
				int col = (i * 20) % 128;
				RGB rgb = new RGB(col, col, col);
				styles.add(new StyleRange(i, 2, colorManager.getColor(rgb), colorManager.getColor(white)));
			}

			if (styles.size() > 0) {
				event.styles = toArray(styles);
			}

		} catch (Exception e) {
		}

	}

	private StyleRange[] toArray(ArrayList<StyleRange> styles) {
		StyleRange[] styleArray = new StyleRange[styles.size()];

		for (int i = 0; i < styles.size(); i++)
			styleArray[i] = styles.get(i);
		return styleArray;
	}

}
