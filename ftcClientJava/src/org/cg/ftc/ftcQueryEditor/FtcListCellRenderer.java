package org.cg.ftc.ftcQueryEditor;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.cg.common.check.Check;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.TemplateCompletion;

@SuppressWarnings("serial")
public class FtcListCellRenderer extends DefaultListCellRenderer {
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {
		
		Check.isTrue(value instanceof Completion);
		Completion c = (Completion) value;
		
		if (c instanceof TemplateCompletion) 
			list.setBackground(new Color(230,255,204));
			
		// super uses value to set an Icon or not depending on the class of value.
		// either icon or text. Null will cause nothing to be displayed.
		return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

	}


}
