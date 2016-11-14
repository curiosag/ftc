package org.cg.eclipse.plugins.ftc;

import java.util.Observable;
import java.util.Observer;

import org.cg.common.check.Check;
import org.cg.eclipse.plugins.ftc.preference.SyntaxStyle;
import org.cg.eclipse.plugins.ftc.syntaxstyle.ColorManager;
import org.cg.eclipse.plugins.ftc.syntaxstyle.ParsedSqlTokensScanner;
import org.cg.eclipse.plugins.ftc.syntaxstyle.SqlCommentPartitionScanner;
import org.cg.eclipse.plugins.ftc.syntaxstyle.SyntaxColoring;
import org.cg.eclipse.plugins.ftc.template.FtcCompletionProcessor;
import org.cg.ftc.shared.interfaces.SyntaxElementType;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class FtcSourceViewerConfiguration extends SourceViewerConfiguration {
	private ColorManager colorManager;

	static class CommentTokenScanner extends BufferedRuleBasedScanner implements Observer 
	{
		
		private SyntaxColoring coloring;
		
		@Override
		public IToken nextToken() {
			return super.nextToken();
		}
		
		public CommentTokenScanner(SyntaxColoring coloring) {
			this.coloring = coloring;
			coloring.addObserver(this);
			setDefaultReturnToken(getCommentToken());
		}
		
		private IToken getCommentToken() {
			SyntaxStyle style = coloring.getStyle(SyntaxElementType.comment);
			return new Token(new TextAttribute(getColor(style), null, ParsedSqlTokensScanner.getStyleBitmap(style)));
		}

		public Color getColor(SyntaxStyle style){
			return ColorManager.getDefault().getColor(style.color);
		} 
		
		@Override
		public void update(Observable o, Object arg) {
			setDefaultReturnToken(getCommentToken());
		}

		@Override
		protected void finalize() throws Throwable {
			coloring.deleteObserver(this);
		}
	}
	
	public FtcSourceViewerConfiguration() {
		this.colorManager = ColorManager.getDefault();
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE, SqlCommentPartitionScanner.SQL_COMMENT };
	}
	
	@Override
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return FtcPlugin.SQL_PARTITIONING;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		
		Check.isTrue(sourceViewer instanceof FtcSourceViewer);
		SyntaxColoring coloring = ((FtcSourceViewer) sourceViewer).getSyntaxColoring();
		
		addDamagerRepairer(reconciler, new FtcDamagerRepairer(coloring, new ParsedSqlTokensScanner(coloring)), IDocument.DEFAULT_CONTENT_TYPE);
		
		DefaultDamagerRepairer commentDr = new DefaultDamagerRepairer(new CommentTokenScanner(coloring));	
		addDamagerRepairer(reconciler, commentDr, SqlCommentPartitionScanner.SQL_COMMENT);
		
		return reconciler;
	}

	private void addDamagerRepairer(PresentationReconciler reconciler, DefaultDamagerRepairer dr, String contentType) {
		reconciler.setDamager(dr, contentType);
		reconciler.setRepairer(dr, contentType);
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {

		ContentAssistant assistant = new ContentAssistant();
		assistant.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		assistant.setContentAssistProcessor(new FtcCompletionProcessor(sourceViewer), IDocument.DEFAULT_CONTENT_TYPE);

		
		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(300);
		assistant.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
		assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
		assistant.setContextInformationPopupBackground(colorManager.getColor(new RGB(150, 150, 0)));

		return assistant;
	}

}