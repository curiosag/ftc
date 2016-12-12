package org.cg.common.io.logging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.cg.common.io.logging.DelegatingOutputStream;
import org.cg.common.io.logging.OnLineWritten;
import org.cg.common.util.StringUtil;

public class DelegatingOutputStream extends ByteArrayOutputStream {

	private final PrintStream redirect;
	private final OnLineWritten delegate;

	public DelegatingOutputStream(PrintStream redirect, OnLineWritten delegate) {
		super();
		this.delegate = delegate;
		this.redirect = redirect;
	}

	@Override
	public void flush() throws IOException {

		synchronized (this) {
			super.flush();
			passOn(this.toString());
			reset();
		}
	}

	private void passOn(String s) {
		if (! StringUtil.emptyOrNull(s)) {
			if (redirect != null) {
				redirect.print(s);
				redirect.flush();
			}
			if (delegate != null && !s.equals(System.lineSeparator()))
				delegate.notify(s);
		}
	}

	public static PrintStream createPrintStream(PrintStream redirect, OnLineWritten delegate) {
		return new PrintStream(new DelegatingOutputStream(redirect, delegate), true);
	}
}