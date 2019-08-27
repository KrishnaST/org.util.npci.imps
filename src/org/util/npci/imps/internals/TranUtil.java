package org.util.npci.imps.internals;

public final class TranUtil {

	public static final String truncateString(final String s, final int len) {
		if(s == null) return "";
		if(s.length() > len) return s.substring(0,len);
		return s;
	}
}
