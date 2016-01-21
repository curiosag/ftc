package org.cg.ftc.shared.uglySmallThings;

import java.io.FileWriter;
import java.io.IOException;

import javax.swing.table.TableModel;

import com.opencsv.CSVWriter;

public class CSV {

	public static String write(TableModel model, String fileName) {
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(fileName), ';');
			try {

				String[] row = new String[model.getColumnCount()];

				for (int j = 0; j < model.getColumnCount(); j++)
					row[j] = model.getColumnName(j);
				writer.writeNext(row);

				for (int i = 0; i < model.getRowCount(); i++) {
					for (int j = 0; j < model.getColumnCount(); j++)
						row[j] = (String) model.getValueAt(i, j);

					writer.writeNext(row);
				}
				return (String.format("Exported %d rows", model.getRowCount()));
			} finally {
				writer.close();
			}

		} catch (IOException e) {
			return String.format("Error writing to %s: %s", fileName, e.getMessage());
		}

	}

}
