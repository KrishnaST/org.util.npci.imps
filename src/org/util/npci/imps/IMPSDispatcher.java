package org.util.npci.imps;

import org.util.iso8583.ISO8583Message;
import org.util.npci.coreconnect.CoreConfig;
import org.util.npci.coreconnect.issuer.LogonDispatcher;

public final class IMPSDispatcher extends LogonDispatcher {

	public IMPSDispatcher(CoreConfig config) {
		super(config);
	}

	@Override
	public final boolean dispatch(ISO8583Message request) {
		return super.dispatch(request);
	}

}
