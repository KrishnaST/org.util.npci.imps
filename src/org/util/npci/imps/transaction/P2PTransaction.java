package org.util.npci.imps.transaction;

import org.util.iso8583.ISO8583Message;
import org.util.iso8583.npci.MTI;
import org.util.nanolog.Logger;
import org.util.npci.coreconnect.issuer.IssuerTransaction;
import org.util.npci.imps.IMPSDispatcher;
import org.util.npci.imps.cbs.model.TansactionResponse;

public final class P2PTransaction extends IssuerTransaction<IMPSDispatcher> {

	public P2PTransaction(ISO8583Message request, IMPSDispatcher dispatcher) {
		super(request, dispatcher);
	}

	@Override
	protected void execute(Logger logger) {
		try {
			TansactionResponse response = dispatcher.coreBankingService.transaction(request, logger);
			TranUtil.removeNotRequired(request);
			request.put(0, MTI.getCounterMTI(request.get(0)));
			request.put(39, response.responseCode);
			dispatcher.config.coreconnect.sendResponseToNPCI(request, logger);
		} catch (Exception e) {logger.info(e);}
	}

}
