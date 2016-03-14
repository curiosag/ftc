package org.cg.common.io;

import java.io.IOException;
import java.io.Writer;

import org.cg.common.check.Check;

public class CustomCloseActionWriterDecorator extends Writer {

	private final Writer inner;
	WriterCloseAction closeAction;

	public CustomCloseActionWriterDecorator(Writer inner, WriterCloseAction closeAction) {
		Check.notNull(inner);
		this.inner = inner;
		this.closeAction = closeAction;
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		inner.write(cbuf, off, len);
	}

	@Override
	public void flush() throws IOException {
		inner.flush();		
	}

	@Override
	public void close() throws IOException {
		if (closeAction != null)
			closeAction.invoke(inner);
	}

}
