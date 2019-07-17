package org.util.npci.imps;

import org.util.datautil.TLV;
import org.util.iso8583.ISO8583Message;
import org.util.iso8583.npci.constants.IMPSTransactionType;
import org.util.npci.api.ConfigurationNotFoundException;
import org.util.npci.coreconnect.CoreConfig;
import org.util.npci.coreconnect.issuer.LogonDispatcher;
import org.util.npci.imps.cbs.CoreBankingService;
import org.util.npci.imps.cbs.CoreBankingServiceBuilder;
import org.util.npci.imps.db.DatabaseService;
import org.util.npci.imps.db.DatabaseServiceBuilder;
import org.util.npci.imps.transaction.P2ATransaction;
import org.util.npci.imps.transaction.P2AVerification;
import org.util.npci.imps.transaction.P2PTransaction;
import org.util.npci.imps.transaction.P2PVerification;

public final class IMPSDispatcher extends LogonDispatcher {

	public final DatabaseService    databaseService;
	public final CoreBankingService coreBankingService;

	public IMPSDispatcher(CoreConfig config) throws ConfigurationNotFoundException {
		super(config);
		databaseService    = DatabaseServiceBuilder.getDatabaseService(config, this);
		coreBankingService = CoreBankingServiceBuilder.getCoreBankingService(config, this);
	}

	@Override
	public final boolean dispatch(ISO8583Message request) {
		boolean isDispatched = false;
		isDispatched = super.dispatch(request);
		if (!isDispatched) {
			final String transactionType = TLV.parse(request.get(120)).get("001");
			if (IMPSTransactionType.P2A_TRANSACTION.equals(transactionType)) isDispatched = config.schedular.execute(new P2ATransaction(request, this));
			else if (IMPSTransactionType.P2A_VERIFICATION.equals(transactionType)) isDispatched = config.schedular.execute(new P2AVerification(request, this));
			else if (IMPSTransactionType.P2P_TRANSACTION.equals(transactionType)) isDispatched = config.schedular.execute(new P2PTransaction(request, this));
			else if (IMPSTransactionType.P2P_VERIFICATION.equals(transactionType)) isDispatched = config.schedular.execute(new P2PVerification(request, this));
		}
		return isDispatched;
	}

}
