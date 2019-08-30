package org.util.npci.imps.db;

import org.util.nanolog.Logger;
import org.util.npci.coreconnect.CoreConfig;
import org.util.npci.coreconnect.CoreDatabaseService;
import org.util.npci.imps.IMPSDispatcher;
import org.util.npci.imps.cbs.model.AccountDetails;

public abstract class DatabaseService extends CoreDatabaseService<IMPSDispatcher> {

	public final IMPSDispatcher dispatcher;
	public final CoreConfig     config;

	public DatabaseService(final CoreConfig config, final IMPSDispatcher dispatcher) {
		super(dispatcher);
		this.config     = config;
		this.dispatcher = dispatcher;
	}

	public abstract String getName();

	public abstract AccountDetails getAccountDetails(final String mobile, final String mmid, final Logger logger);
}
