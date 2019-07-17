package org.util.npci.imps.db;

import java.util.List;

import org.util.npci.api.ConfigurationNotFoundException;
import org.util.npci.coreconnect.CoreConfig;
import org.util.npci.imps.IMPSDispatcher;

public final class InternalDatabaseServiceBuilder extends DatabaseServiceBuilder {

	@Override
	public final List<String> getDatabaseServices() {
		return List.of("IMPS70");
	}

	@Override
	public final DatabaseService build(final CoreConfig config, final IMPSDispatcher dispatcher) throws ConfigurationNotFoundException {
		return new IMPS70DatabaseService(config, dispatcher);
	}

}
