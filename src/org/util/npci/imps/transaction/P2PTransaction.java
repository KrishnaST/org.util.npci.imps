package org.util.npci.imps.transaction;

import org.util.iso8583.ISO8583Message;
import org.util.nanolog.Logger;
import org.util.npci.coreconnect.issuer.IssuerTransaction;
import org.util.npci.imps.IMPSDispatcher;

public final class P2PTransaction extends IssuerTransaction<IMPSDispatcher> {

	public P2PTransaction(ISO8583Message request, IMPSDispatcher dispatcher) {
		super(request, dispatcher);
	}

	@Override
	protected void execute(Logger logger) {

	}

}
