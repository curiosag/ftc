package org.cg.common.misc;

import org.cg.common.check.Check;
import org.cg.common.io.StringStorage;

import org.cg.common.misc.CmdDestination;
import org.cg.common.misc.StringList;

public final class CmdHistory {

	private static final int MAX_CMDHISTORY = 50 + 2;
	private final String headSentinel = "\u0001";
	private final String tailSentinel = "\u0000";
	private final int tailSentinelIdx = 0;

	private final int next = 1;
	private final int prev = -1;

	private StringList cmdHistory;
	private int currPos = 0;

	private int headSentinelIdx() {
		return cmdHistory.size() - 1;
	}

	public CmdHistory(StringStorage storage) {
		cmdHistory = createCmdHistory(storage);

		if (cmdHistory.isEmpty()) {
			cmdHistory.add(tailSentinel);
			cmdHistory.add(headSentinel);
		}
		currPos = headSentinelIdx();
	}

	private StringList createCmdHistory(StringStorage storage) {
		StringList result = new StringList(storage);
		if (result.size() < 2
				|| !(result.get(0).equals(tailSentinel) && result.get(result.size() - 1).equals(headSentinel)))
			result.clear();
		return result;
	}

	private String top() {
		return cmdHistory.get(headSentinelIdx() - 1);
	}

	public void add(String cmd) {
		if (!cmd.equals(top())) {
			if (cmdHistory.size() > MAX_CMDHISTORY)
				cmdHistory.remove(tailSentinelIdx + 1);

			cmdHistory.set(headSentinelIdx(), cmd);
			cmdHistory.add(headSentinel);
			cmdHistory.writeToStorage();
			currPos = headSentinelIdx();
		}
	}

	private boolean hitBorder(int i, int navigation) {
		return (navigation > 0 && i >= headSentinelIdx()) || (navigation < 0 && i <= tailSentinelIdx);
	}

	public String navigate(int i) {
		Check.isTrue(Math.abs(i) == 1);

		currPos = currPos + i;

		String result = null;
		if (!hitBorder(currPos, i))
			result = cmdHistory.get(currPos);
		else
			currPos = currPos - i;

		return result;
	}

	private boolean setAny(CmdDestination dest, String value) {

		boolean result = value != null;
		if (result)
			dest.set(value);
		return result;

	}

	public boolean prev(CmdDestination dest) {
		return setAny(dest, navigate(prev));
	}

	public boolean next(CmdDestination dest) {
		return setAny(dest, navigate(next));
	}

	public boolean isEmpty() {
		return cmdHistory.size() <= 2;
	}

	public void rewind() {
		currPos = headSentinelIdx();
	}

}
