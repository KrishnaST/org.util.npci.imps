package org.util.npci.imps.transaction;

import org.util.iso8583.ISO8583Message;

public class TranUtil {

	public static final void removeNotRequired(ISO8583Message issuerResponse) {
		issuerResponse.remove(18);
		issuerResponse.remove(22);
		issuerResponse.remove(25);
		issuerResponse.remove(42);
		issuerResponse.remove(43);
	}
	
	public static final String truncateString(final String s, final int len) {
		if(s == null) return "";
		if(s.length() > len) return s.substring(0,len);
		return s;
	}
}
