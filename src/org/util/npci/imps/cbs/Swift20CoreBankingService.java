package org.util.npci.imps.cbs;

import org.util.iso8583.ISO8583Message;
import org.util.npci.coreconnect.CoreConfig;

public final class Swift20CoreBankingService extends CoreBankingService {

	public Swift20CoreBankingService(CoreConfig config) {
		super(config);
	}

	@Override
	public final String getName() {
		return "SWIFT20";
	}

	@Override
	public final TansactionResponse transaction(ISO8583Message request) {
		return null;
	}

	@Override
	public final VerificationResponse verification(ISO8583Message request) {
		return null;
	}

}
