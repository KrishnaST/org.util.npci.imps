package org.util.npci.imps.cbs;

import org.util.iso8583.ISO8583Message;
import org.util.npci.coreconnect.CoreConfig;

public abstract class CoreBankingService {

	public final CoreConfig config;

	public CoreBankingService(CoreConfig config) {
		this.config = config;
	}

	public abstract String getName();

	public abstract TansactionResponse transaction(final ISO8583Message request);

	public abstract VerificationResponse verification(final ISO8583Message request);

}
