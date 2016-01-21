package org.cg.common.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

public class UnderlineHighlightPainter extends DefaultHighlightPainter {

	public UnderlineHighlightPainter(Color c) {
		super(c);
	}

	@Override
	public void paint(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c) {
		try {
			TextUI mapper = c.getUI();
			Rectangle p0 = mapper.modelToView(c, offs0);
			Rectangle p1 = mapper.modelToView(c, offs1);

			Color color = getColor();

			if (color == null) 
				g.setColor(c.getSelectionColor());
			else 
				g.setColor(color);

			if (sameLine(p0, p1)) {
				Rectangle r = p0.union(p1);
				underline(g, r);
			}
		} catch (BadLocationException e) {
		}
	}

	private void underline(Graphics g, Rectangle r) {
		int stepX = 1;
		int stepY = stepX;

		int y = r.y + r.height - 1;
		int x1 = r.x;
		int x2 = r.x + r.width;

		int yCurr = y;
		int xCurr = x1;
		int yNext = 0;
		int xNext = 0;

		while (xCurr <= x2) {
			stepY = -1 * stepY;
			xNext = xCurr + stepX;
			yNext = yCurr + stepY;
			g.drawLine(xCurr, yCurr, xNext, yNext);
			xCurr = xNext;
			yCurr = yNext;
		}
	}

	private boolean sameLine(Rectangle p0, Rectangle p1) {
		return p0.y == p1.y;
	}

}
