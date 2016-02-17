package org.cg.common.io.logging;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.util.logging.Level;

import org.cg.common.io.logging.StdOutErrLevel;

/**
 * Class defining 2 new Logging levels, one for STDOUT, one for STDERR, used
 * when multiplexing STDOUT and STDERR into the same rolling log file via the
 * Java Logging APIs.
 * 
 * credits to Nick Stephen at https://blogs.oracle.com/nickstephen/entry/java_redirecting_system_out_and
 */
public class StdOutErrLevel extends Level {

	private static final long serialVersionUID = 1L;

	private StdOutErrLevel(String name, int value) {
		super(name, value);
	}

	public static Level STDOUT = new StdOutErrLevel("STDOUT", Level.INFO.intValue() + 53);

	public static Level STDERR = new StdOutErrLevel("STDERR", Level.INFO.intValue() + 54);

	/**
	 * Method to avoid creating duplicate instances when deserializing the
	 * object.
	 * 
	 * @return the singleton instance of this <code>Level</code> value in this
	 *         classloader
	 * @throws ObjectStreamException
	 *             If unable to deserialize
	 */
	protected Object readResolve() throws ObjectStreamException {
		if (this.intValue() == STDOUT.intValue())
			return STDOUT;
		if (this.intValue() == STDERR.intValue())
			return STDERR;
		throw new InvalidObjectException("Unknown instance :" + this);
	}

}
