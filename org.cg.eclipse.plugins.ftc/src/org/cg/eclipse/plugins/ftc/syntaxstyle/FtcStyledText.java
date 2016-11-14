package org.cg.eclipse.plugins.ftc.syntaxstyle;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

public class FtcStyledText extends StyledText {

	public FtcStyledText(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void setStyleRange(StyleRange range) {
		super.setStyleRange(range);
	}

	@Override
	public void replaceStyleRanges(int start, int length, StyleRange[] ranges) {
		super.replaceStyleRanges(start, length, ranges);
	}

}
