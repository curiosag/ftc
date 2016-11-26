package org.cg.eclipse.plugins.ftc;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.editors.text.TextEditor;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import java.util.List;

import org.cg.common.check.Check;
import org.cg.common.interfaces.OnTextFieldChangedEvent;
import org.cg.common.interfaces.OnValueChanged;
import org.cg.eclipse.plugins.ftc.glue.FtcPluginClient;
import org.cg.eclipse.plugins.ftc.glue.FtcPreferenceStore;
import org.cg.eclipse.plugins.ftc.glue.Unbox;
import org.cg.eclipse.plugins.ftc.syntaxstyle.FtcStyledText;
import org.cg.ftc.shared.interfaces.SyntaxElement;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyledText;

public class FtcEditor extends TextEditor {

	private final FtcSourceViewerConfiguration sourceViewerConfiguration;
	private final MessageConsoleLogger logging = MessageConsoleLogger.getDefault();

	private OnValueChanged<Integer> caretListener = null;
	private OnTextFieldChangedEvent documentChangedListener = null;

	private boolean showCaretPosition;

	@Override
	public void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
	}

	/*
	 * custom implementation to create an FTCSourceViewer
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#createSourceViewer(
	 * Composite, IVerticalRuler, int)
	 * 
	 * @see
	 * org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#createSourceViewer(
	 * Composite, IVerticalRuler, int)
	 */

	@Override
	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {

		fAnnotationAccess = getAnnotationAccess();
		fOverviewRuler = createOverviewRuler(getSharedColors());

		Object input = getEditorInput();
		Assert.isTrue(input instanceof IFileEditorInput);

		ISourceViewer viewer = new FtcSourceViewer(((IFileEditorInput) input).getFile(), parent, ruler,
				getOverviewRuler(), isOverviewRulerVisible(), styles);

		getSourceViewerDecorationSupport(viewer);

		return viewer;
	}

	public int getCaretOffset() {
		return getSourceViewer().getTextWidget().getCaretOffset();
	}

	private FtcStyledText getStyledText() {
		StyledText text = getSourceViewer().getTextWidget();
		Check.isTrue(text instanceof FtcStyledText);
		return (FtcStyledText) text;
	}

	public String getText() {
		return getStyledText().getText();
	}

	public FtcEditor() {
		super();
		sourceViewerConfiguration = new FtcSourceViewerConfiguration();
		setSourceViewerConfiguration(sourceViewerConfiguration);

		IPreferenceStore pref = FtcPluginClient.getDefault().getPreferenceStore();
		showCaretPosition = pref.getBoolean(FtcPreferenceStore.KEY_SHOW_CARETPOS);

		pref.addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(FtcPreferenceStore.KEY_SHOW_CARETPOS)) {
					showCaretPosition = Unbox.asBoolean(event.getNewValue());
					if (!showCaretPosition)
						setStatusLineMessage("");
				}
			}
		});

	}

	protected void initializeEditor() {
		super.initializeEditor();
	}

	public void dispose() {
		super.dispose();
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		addEditorListeners();
		addCaretListenerForUIdisplay();
		logging.reveal();
		Check.isTrue(getSourceViewer() instanceof FtcSourceViewer);

		FtcPluginClient.getDefault().registerEditor(this);
		// not of any use now
		// IContextService contextService = (IContextService)
		// getSite().getService(IContextService.class);
		// contextService.activateContext(PluginConst.FTC_CONTEXT_ID);
	}

	private void internalSetCaretListener(OnValueChanged<Integer> l) {
		getStyledText().addCaretListener(new CaretListener() {

			@Override
			public void caretMoved(CaretEvent event) {
				l.notify(event.caretOffset);
			}
		});
	}

	private void internalSetDocumentChangedListener(OnTextFieldChangedEvent e) {
		getSourceViewer().getDocument().addDocumentListener(new IDocumentListener() {
			@Override
			public void documentAboutToBeChanged(DocumentEvent de) {
			}

			@Override
			public void documentChanged(DocumentEvent de) {
				e.notify(de.getDocument().get());
			}
		});
	}
	
	private void addCaretListenerForUIdisplay() {
		getStyledText().addCaretListener(new CaretListener() {

			@Override
			public void caretMoved(CaretEvent event) {
				showCaretPosition(event);
			}

			private void showCaretPosition(CaretEvent event) {
				if (showCaretPosition)
					setStatusLineMessage(String.valueOf(event.caretOffset));
			}
		});

	}

	public void setCaretListener(OnValueChanged<Integer> onChange) {
		caretListener = onChange;
	}

	public void setDocumentChangedListener(OnTextFieldChangedEvent e) {
		documentChangedListener = e;
	}

	private void hdlDocumentChangedEvent(String value) {
		Check.notNull(documentChangedListener);
		documentChangedListener.notify(getText());
	}

	void hdlCaretChangedEvent(int value) {
		Check.notNull(caretListener);
		caretListener.notify(value);
	}

	private void addEditorListeners() {
		FtcEditor thisEditor = this;
		getSite().getPage().addPartListener(new IPartListener() {

			@Override
			public void partActivated(IWorkbenchPart part) {
				if (part == thisEditor) {
					FtcPluginClient.getDefault().onEditorActivated(thisEditor);
					internalSetDocumentChangedListener(documentChangedListener);
					internalSetCaretListener(caretListener);
					// won't get fired initially from editor or document
					hdlDocumentChangedEvent(thisEditor.getText());
					hdlCaretChangedEvent(getCaretOffset());
				}
			}

			@Override
			public void partBroughtToTop(IWorkbenchPart part) {
			}

			@Override
			public void partDeactivated(IWorkbenchPart part) {
			}

			@Override
			public void partOpened(IWorkbenchPart part) {
			}

			@Override
			public void partClosed(IWorkbenchPart part) {
				if (part == thisEditor)
					FtcPluginClient.getDefault().onEditorClosed(thisEditor);
			}
		});

	}

	public void invalidateTextRepresentation() {
		resetAnythingCached();
		getFtcSourceViewer().invalidateTextPresentation();
		// getFtcSourceViewer().getSyntaxColoring().resetMarkers();
	}

	private List<SyntaxElement> resetAnythingCached() {
		getFtcSourceViewer().resetSyntaxColoring();
		return FtcPluginClient.getDefault().getSyntaxElementSource().get("");
	}

	private FtcSourceViewer getFtcSourceViewer() {
		Check.isTrue(getSourceViewer() instanceof FtcSourceViewer);
		return (FtcSourceViewer) getSourceViewer();
	}

}
