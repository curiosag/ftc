package org.cg.ftceditor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.RGB;

public class FtcSourceViewerConfiguration extends SourceViewerConfiguration {
	private FtcScanner scanner;
	private ColorManager colorManager;

	public FtcSourceViewerConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE };
	}

	protected FtcScanner getScanner() {
		if (scanner == null) {
			scanner = new FtcScanner(colorManager);
			scanner.setDefaultReturnToken(colorManager.getColoredToken(IColorConstants.DEFAULT));
		}
		return scanner;
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		// addDamagerRepairer(reconciler, new
		// DefaultDamagerRepairer(getScanner()));

		return reconciler;
	}

	private void addDamagerRepairer(PresentationReconciler reconciler, DefaultDamagerRepairer dr) {
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {

		ContentAssistant assistant = new ContentAssistant();
		assistant.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		assistant.setContentAssistProcessor(new FtcCompletionProcessor(), IDocument.DEFAULT_CONTENT_TYPE);

		assistant.enableAutoActivation(false);
		//assistant.setAutoActivationDelay(500);
		assistant.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
		assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
		assistant.setContextInformationPopupBackground(colorManager.getColor(new RGB(150, 150, 0)));

		return assistant;
	}

}