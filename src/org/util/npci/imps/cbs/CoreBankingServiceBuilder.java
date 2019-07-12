package org.util.npci.imps.cbs;

import java.util.List;
import java.util.ServiceLoader;

import org.util.npci.api.ConfigurationNotFoundException;
import org.util.npci.coreconnect.CoreConfig;

public abstract class CoreBankingServiceBuilder {

	public abstract List<String> getCoreBankingServices();

	public abstract CoreBankingService build(CoreConfig coreConfig) throws ConfigurationNotFoundException;

	public static final CoreBankingService getIssuerDispatcher(final CoreConfig config) throws ConfigurationNotFoundException {
		final ServiceLoader<CoreBankingServiceBuilder> serviceLoader = ServiceLoader.load(CoreBankingServiceBuilder.class, CoreBankingServiceBuilder.class.getClassLoader());
		for (CoreBankingServiceBuilder builder : serviceLoader) { if (builder.getCoreBankingServices().contains(config.coreBankingType)) return builder.build(config); }
		throw new ConfigurationNotFoundException("could not find core banking service with name : "+config.coreBankingType);
	}
}
