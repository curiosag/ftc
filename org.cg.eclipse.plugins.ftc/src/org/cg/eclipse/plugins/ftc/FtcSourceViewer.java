package org.cg.eclipse.plugins.ftc;

import org.cg.eclipse.plugins.ftc.syntaxstyle.FtcStyledText;
import org.cg.eclipse.plugins.ftc.syntaxstyle.SyntaxColoring;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

public class FtcSourceViewer extends SourceViewer {

	private final IResource resource;
	private final SyntaxColoring syntaxColoring;

	public FtcSourceViewer(IResource resource, Composite parent, IVerticalRuler ruler, IOverviewRuler overviewRuler,
			boolean overviewRulerVisible, int styles) {

		super(parent, ruler, overviewRuler, overviewRulerVisible, styles);
		this.resource = resource;
		this.syntaxColoring = new SyntaxColoring(this);
	}

	public void resetSyntaxColoring() {
		getSyntaxColoring().reloadStyles();
	}

	/**
	 * @see org.eclipse.jface.text.createTextWidget(Composite parent, int
	 *      styles)
	 */
	@Override
	protected StyledText createTextWidget(Composite parent, int styles) {
		StyledText styledText = new FtcStyledText(parent, styles);
		styledText.setLeftMargin(Math.max(styledText.getLeftMargin(), 2));
		return styledText;
	}

	public IResource getResource() {
		return resource;
	}

	public SyntaxColoring getSyntaxColoring() {
		return syntaxColoring;
	}

}
