package org.util.npci.imps.db;

import org.util.nanolog.Logger;
import org.util.npci.coreconnect.CoreConfig;
import org.util.npci.imps.IMPSDispatcher;
import org.util.npci.imps.cbs.model.AccountDetails;

public abstract class DatabaseService {

	public final IMPSDispatcher dispatcher;
	public final CoreConfig     config;

	public DatabaseService(final IMPSDispatcher dispatcher) {
		this.config     = dispatcher.config;
		this.dispatcher = dispatcher;
	}

	public abstract String getName();

	public abstract AccountDetails getAccountDetails(final String mobile, final String mmid, final Logger logger);
}
