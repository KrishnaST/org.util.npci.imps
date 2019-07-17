package org.util.npci.imps.transaction;

import org.util.iso8583.ISO8583Message;
import org.util.iso8583.npci.MTI;
import org.util.nanolog.Logger;
import org.util.npci.coreconnect.issuer.IssuerTransaction;
import org.util.npci.imps.IMPSDispatcher;
import org.util.npci.imps.cbs.model.VerificationResponse;

public final class P2PVerification extends IssuerTransaction<IMPSDispatcher> {

	public P2PVerification(ISO8583Message request, IMPSDispatcher dispatcher) {
		super(request, dispatcher);
	}

	@Override
	protected void execute(Logger logger) {
		try {
			VerificationResponse response = dispatcher.coreBankingService.verification(request, logger);
			TranUtil.removeNotRequired(request);
			request.put(0, MTI.getCounterMTI(request.get(0)));
			request.put(39, response.responseCode);
			dispatcher.config.coreconnect.sendResponseToNPCI(request, logger);
		} catch (Exception e) {logger.info(e);}
	
	}

}
