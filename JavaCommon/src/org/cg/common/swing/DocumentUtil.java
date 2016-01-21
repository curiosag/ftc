package org.cg.common.swing;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

public class DocumentUtil {
		
	public static String getText(Document d) {
        String txt;
        try {
            StringWriter buf = new StringWriter();
            write(buf, d);
            txt = buf.toString();
        } catch (IOException ioe) {
            txt = null;
        }
        return txt;
    }

	private static void write(Writer out, Document doc) throws IOException {
        
        try {
            new DefaultEditorKit().write(out, doc, 0, doc.getLength());
        } catch (BadLocationException e) {
            throw new IOException(e.getMessage());
        }
    }

}
