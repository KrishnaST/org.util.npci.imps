package org.util.npci.imps.cbs;

import org.util.iso8583.ISO8583Message;
import org.util.nanolog.Logger;
import org.util.npci.coreconnect.CoreConfig;
import org.util.npci.imps.IMPSDispatcher;
import org.util.npci.imps.cbs.model.TansactionResponse;
import org.util.npci.imps.cbs.model.VerificationResponse;

public abstract class CoreBankingService {

	public final IMPSDispatcher dispatcher;
	public final CoreConfig     config;

	public CoreBankingService(final CoreConfig config, final IMPSDispatcher dispatcher) {
		this.config     = config;
		this.dispatcher = dispatcher;
	}

	public abstract String getName();

	public abstract TansactionResponse transaction(final ISO8583Message request, final Logger logger);

	public abstract VerificationResponse verification(final ISO8583Message request, final Logger logger);

}
