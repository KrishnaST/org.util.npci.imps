package org.util.npci.imps.cbs;

import java.util.concurrent.TimeUnit;

import org.util.iso8583.ISO8583Message;
import org.util.nanolog.Logger;
import org.util.npci.api.ConfigurationNotFoundException;
import org.util.npci.api.PropertyName;
import org.util.npci.coreconnect.CoreConfig;
import org.util.npci.coreconnect.util.RetroClientBuilder;
import org.util.npci.imps.IMPSDispatcher;
import org.util.npci.imps.cbs.model.TansactionResponse;
import org.util.npci.imps.cbs.model.VerificationResponse;

import retrofit2.Retrofit;

public final class Swift20CoreBankingService extends CoreBankingService {

	private final Retrofit retrofit;

	public Swift20CoreBankingService(final CoreConfig config, final IMPSDispatcher dispatcher) throws ConfigurationNotFoundException {
		super(config, dispatcher);
		retrofit = RetroClientBuilder.newBuilder().baseURL(config.getString(PropertyName.CBS_IP))
				.withLogging(config.getStringSupressException(PropertyName.RETROFIT_LOGGING_LEVEL))
				.readTimeout(config.getIntSupressException(PropertyName.RETROFIT_READ_TIMEOUT_SEC), TimeUnit.SECONDS)
				.writeTimeout(config.getIntSupressException(PropertyName.RETROFIT_WRITE_TIMEOUT_SEC), TimeUnit.SECONDS)
				.connectTimeout(config.getIntSupressException(PropertyName.RETROFIT_CONNECT_TIMEOUT_SEC), TimeUnit.SECONDS)
				.build();
		config.corelogger.info("retrofit initialized : " + retrofit);
	}

	@Override
	public final String getName() {
		return "SWIFT20";
	}

	@Override
	public final TansactionResponse transaction(final ISO8583Message request, final Logger logger) {
		return null;
	}

	@Override
	public final VerificationResponse verification(final ISO8583Message request, final Logger logger) {
		return null;
	}

}
