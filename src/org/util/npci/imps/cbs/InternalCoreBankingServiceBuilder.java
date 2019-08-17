package org.util.npci.imps.cbs;

import java.util.List;

import org.util.npci.api.ConfigurationNotFoundException;
import org.util.npci.coreconnect.CoreConfig;
import org.util.npci.imps.IMPSDispatcher;

public final class InternalCoreBankingServiceBuilder extends CoreBankingServiceBuilder {

	@Override
	public final List<String> getCoreBankingServices() {
		return List.of("SWIFT20", "SWIFT63");
	}

	@Override
	public final CoreBankingService build(final CoreConfig config, final IMPSDispatcher dispatcher) throws ConfigurationNotFoundException {
		if ("SWIFT20".equals(config.coreBankingType)) return new Swift20CoreBankingService(config, dispatcher);
		else if ("SWIFT63".equals(config.coreBankingType)) return new Swift63CoreBankingService(config, dispatcher);
		throw new ConfigurationNotFoundException("");
	}
}
