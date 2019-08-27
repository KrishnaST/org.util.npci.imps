package org.util.npci.imps.transaction;

import org.util.datautil.TLV;
import org.util.iso8583.ISO8583Message;
import org.util.iso8583.npci.MTI;
import org.util.iso8583.npci.NPCIUtil;
import org.util.nanolog.Logger;
import org.util.npci.coreconnect.issuer.IssuerTransaction;
import org.util.npci.imps.IMPSDispatcher;
import org.util.npci.imps.cbs.model.TansactionResponse;
import org.util.npci.imps.internals.TranUtil;

public final class P2PTransaction extends IssuerTransaction<IMPSDispatcher> {

	public P2PTransaction(final ISO8583Message request, final IMPSDispatcher dispatcher) {
		super(request, dispatcher);
	}

	@Override
	protected boolean execute(final Logger logger) {
		try {
			final TLV DE120 = TLV.parse(request.get(120));
 			logger.info("Request DE120", DE120.toString());
			TansactionResponse response = dispatcher.coreBankingService.transaction(request, logger);
			request.put(0, MTI.getResponseMTI(request.get(0)));
			request.put(38, response.authCode);
			request.put(39, response.responseCode);
			request.put(103, response.beneficiaryAccount);
			request.put(120, DE120.put("046", TranUtil.truncateString(response.beneficiaryName, 20)).build());
			logger.info("Response DE120", DE120.toString());
			NPCIUtil.removeNotRequiredElements(request);
			return config.coreconnect.sendResponseToNPCI(request, logger);
		} catch (Exception e) {logger.info(e);}
		return false;
	}

}
