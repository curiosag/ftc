package org.cg.eclipse.plugins.ftc;

import org.cg.eclipse.plugins.ftc.syntaxstyle.SyntaxColoring;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;

public class FtcDamagerRepairer extends DefaultDamagerRepairer {

	SyntaxColoring coloring;
	
	public FtcDamagerRepairer(SyntaxColoring coloring, ITokenScanner scanner) {
		super(scanner);
		this.coloring = coloring;
	}

	@Override
	public void createPresentation(TextPresentation presentation, ITypedRegion damage){
		super.createPresentation(presentation, damage);
		coloring.resetMarkers();
	}
	
}
