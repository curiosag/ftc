package org.cg.ftc.shared.uglySmallThings;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import javax.swing.table.TableModel;
import org.cg.common.util.StringUtil;
import com.opencsv.CSVWriter;

public class CSV {

	private  char DELIMITER = ';';
	private  char QUOTE = CSVWriter.DEFAULT_QUOTE_CHARACTER;
	
	public CSV(){
		super();
	}

	public CSV withDelimiter(char delimiter){
		DELIMITER = delimiter;
		return this;
	}
	
	public CSV withQuote(char quote){
		QUOTE = quote;
		return this;
	}
	
	public String write(TableModel model, String fileName) {

		try {
			Writer w = new FileWriter(fileName);
			try {
				return processCsv(model, w);
			} finally {
				w.close();
			}
		} catch (IOException e) {
			return String.format("Error writing to %s: %s", fileName, e.getMessage());
		}
	}

	public void write(TableModel model, Writer dest) {
		try {
			processCsv(model, dest);
		} catch (IOException e) {
			System.err.println(StringUtil.getStackTrace(e));
			throw new RuntimeException(e);
		}
	}

	private String processCsv(TableModel model, Writer dest) throws IOException {
		
		CSVWriter writer = new CSVWriter(dest, DELIMITER, QUOTE);
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
			writer.flush();
			return (String.format("Exported %d rows", model.getRowCount()));
		} finally {
			writer.close();
		}

	}

	
}
