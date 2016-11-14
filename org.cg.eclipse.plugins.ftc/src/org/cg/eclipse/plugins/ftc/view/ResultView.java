package org.cg.eclipse.plugins.ftc.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.table.TableModel;

import org.cg.eclipse.plugins.ftc.PluginConst;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import com.opencsv.CSVWriter;

public class ResultView extends ViewPart {

	Clipboard clipboard;
	public static final String ID = PluginConst.RESULT_VIEW_ID;
	private Table table;

	public ResultView() {
		TableViewer t;
	}

	public void createPartControl(Composite parent) {
		table = new Table(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;

		table.setLayoutData(data);

		clipboard = new Clipboard(Display.getDefault());
		table.addKeyListener(createKeyListener());
	}

	public void displayTable(TableModel model) {

		table.setRedraw(false);
		table.removeAll();
		while (table.getColumnCount() > 0) {
			table.getColumns()[0].dispose();
		}
		table.setRedraw(true);

		int colCount = model.getColumnCount();

		for (int i = 0; i < colCount; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(model.getColumnName(i));
		}

		for (int i = 0; i < model.getRowCount(); i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			for (int j = 0; j < colCount; j++)
				item.setText(j, model.getValueAt(i, j).toString());
		}

		for (int i = 0; i < colCount; i++)
			table.getColumn(i).pack();
	}

	public String getCsv(String delimiter, String quote) {
		return getCsv(table.getItems(), delimiter, quote);
	}

	private String getCsv(TableItem[] tableItems, String delimiter, String quote) {
		StringBuffer result = new StringBuffer();

		for (int i = 0; i < tableItems.length; i++)
			appendLine(result, tableItems[i], delimiter, quote);

		return result.toString();
	}

	private void appendLine(StringBuffer result, TableItem item, String delimiter, String quote) {
		String escapedQuote = quote + quote;
		for (int j = 0; j < table.getColumnCount(); j++) {
			result.append(quote);
			
			String current = item.getText(j);
			if (current != null) {
				if (quote != null && quote.length() > 0)
					current = current.replace(quote, escapedQuote);
				result.append(current);
			}
			
			result.append(quote);
			if (j < table.getColumnCount() - 1)
				result.append(quote);
		}
		result.append("\n");
	}

	private KeyListener createKeyListener() {

		return new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 'c'))
					clipboard.setContents(new Object[] { getCsv(table.getSelection(), " ", "") },
							new Transfer[] { TextTransfer.getInstance() });
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		};
	}

	@Override
	public void setFocus() {
	}

}
