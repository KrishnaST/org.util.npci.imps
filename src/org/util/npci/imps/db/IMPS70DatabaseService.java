package org.util.npci.imps.db;

import org.util.nanolog.Logger;
import org.util.npci.coreconnect.CoreConfig;
import org.util.npci.imps.IMPSDispatcher;
import org.util.npci.imps.cbs.model.AccountDetails;

public class IMPS70DatabaseService extends DatabaseService {

	public IMPS70DatabaseService(final CoreConfig config, final IMPSDispatcher dispatcher) {
		super(config, dispatcher);
	}

	@Override
	public final String getName() {
		return "IMPS70";
	}

	@Override
	public final AccountDetails getAccountDetails(String mobile, String mmid, Logger logger) {
		return null;
	}

}
