package org.cg.common.misc;

public interface CmdDestination {
	/**
	 * 
	 * @param cmd
	 *            value to set for destination
	 * @return true if cmdHistory wasn't empty and command could be set
	 */
	void set(String cmd);
}