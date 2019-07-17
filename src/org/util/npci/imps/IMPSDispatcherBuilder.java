package org.util.npci.imps;

import java.util.List;

import org.util.npci.api.ConfigurationNotFoundException;
import org.util.npci.coreconnect.CoreConfig;
import org.util.npci.coreconnect.issuer.IssuerDispatcher;
import org.util.npci.coreconnect.issuer.IssuerDispatcherBuilder;

public final class IMPSDispatcherBuilder extends IssuerDispatcherBuilder {

	@Override
	public final List<String> getDispatcherTypes() {
		return List.of("IMPS");
	}

	@Override
	public final IssuerDispatcher build(CoreConfig config) throws ConfigurationNotFoundException {
		return new IMPSDispatcher(config);
	}

}
