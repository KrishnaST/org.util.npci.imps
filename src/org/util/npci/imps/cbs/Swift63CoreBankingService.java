package org.util.npci.imps.cbs;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

import org.util.datautil.TLV;
import org.util.iso8583.ISO8583Message;
import org.util.iso8583.npci.IMPSResponseCode;
import org.util.iso8583.npci.imps.IMPSTransactionType;
import org.util.nanolog.Logger;
import org.util.npci.api.ConfigurationNotFoundException;
import org.util.npci.coreconnect.CoreConfig;
import org.util.npci.coreconnect.util.RetroClientBuilder;
import org.util.npci.imps.IMPSDispatcher;
import org.util.npci.imps.POSPropertyName;
import org.util.npci.imps.cbs.model.AccountDetails;
import org.util.npci.imps.cbs.model.IMPSTransactionRequest;
import org.util.npci.imps.cbs.model.IMPSTransactionResponse;
import org.util.npci.imps.cbs.model.TansactionResponse;
import org.util.npci.imps.cbs.model.VerificationResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Tag;

public final class Swift63CoreBankingService extends CoreBankingService {

	public interface RetroCoreBankingService {

		@POST("transaction/InwardImpsTransaction")
		Call<IMPSTransactionResponse> transaction(@Body IMPSTransactionRequest request, @Tag Logger logger);

		@POST("transaction/impsBeneficiaryVerification")
		Call<IMPSTransactionResponse> verification(@Body IMPSTransactionRequest request, @Tag Logger logger);
	}

	private final Retrofit retrofit;

	public Swift63CoreBankingService(final CoreConfig config, final IMPSDispatcher dispatcher) throws ConfigurationNotFoundException {
		super(dispatcher);
		retrofit = RetroClientBuilder.newBuilder().baseURL(config.getString(POSPropertyName.CBS_IP))
				.withLogging(config.getStringSupressException(POSPropertyName.CBS_LOGGING_LEVEL))
				.readTimeout(config.issuerTimeout, TimeUnit.SECONDS).build();
		config.corelogger.info("retrofit initialized : " + retrofit);
	}

	@Override
	public final String getName() {
		return "SWIFT63";
	}

	@Override
	public final TansactionResponse transaction(final ISO8583Message message, final Logger logger) {
		logger.info("processing transaction at core banking.");
		IMPSTransactionResponse impsTransactionResponse = new IMPSTransactionResponse();
		try {
			final TLV                    de120   = TLV.parse(message.get(120));
			final IMPSTransactionRequest request = new IMPSTransactionRequest();
			if (de120.get("001").equals(IMPSTransactionType.P2A_TRANSACTION)) {
				request.transType = "P2A";
				request.benfAccNo = de120.get("062");
				request.benfIFSC  = de120.get("059");
				request.accountNo = de120.get("062");
				request.ifscCode  = de120.get("059");
			} else {
				request.transType = "P2P";
				final String   mmid           = message.get(2).substring(0, 4) + de120.get("049");
				final String   mobile         = message.get(2).substring(9);
				AccountDetails accountDetails = dispatcher.databaseService.getAccountDetails(mobile, mmid, logger);
				;
				if (accountDetails == null) {
					logger.info("account details not found.");
					return new TansactionResponse("M0", "Account Not found.");
				}
				request.benfAccNo = accountDetails.accNo15;
				request.accountNo = accountDetails.accNo15;
				//Not sent by earlier versions.
				//request.benfIFSC = de120.get("059");
				request.benfMMID   = message.get(2).substring(0, 7);
				request.benfMobile = "91" + message.get(2).substring(9);
			}

			request.narration      = de120.get("051");
			request.remitterAccNo  = message.get(102);
			request.remitterMMID   = de120.get("050").substring(0, 7);
			request.remitterMobile = "91" + de120.get("050").substring(7);
			request.RRNNo          = message.get(37);
			request.transAmt       = Double.parseDouble(message.get(4)) / 100.0;
			final RetroCoreBankingService           service  = retrofit.create(RetroCoreBankingService.class);
			final Call<IMPSTransactionResponse>     call     = service.transaction(request, logger);
			final Response<IMPSTransactionResponse> response = call.execute();
			impsTransactionResponse = response.body();
			return new TansactionResponse(impsTransactionResponse, request.benfAccNo, message.get(11));
		} catch (ConnectException e) {
			logger.error(e);
			impsTransactionResponse.errorCode = IMPSResponseCode.ISSUER_NODE_OFFLINE;
			return new TansactionResponse(impsTransactionResponse);
		} catch (Exception e) {
			logger.error(e);
			impsTransactionResponse.errorCode = IMPSResponseCode.PROCESSOR_DOWN;
			return new TansactionResponse(impsTransactionResponse);
		}

	}

	@Override
	public final VerificationResponse verification(final ISO8583Message message, final Logger logger) {
		logger.info("processing verification at core banking.");
		IMPSTransactionResponse impsTransactionResponse = new IMPSTransactionResponse();
		try {
			final TLV                    de120   = TLV.parse(message.get(120));
			final IMPSTransactionRequest request = new IMPSTransactionRequest();
			if (de120.get("001").equals(IMPSTransactionType.P2A_VERIFICATION)) {
				request.transType = IMPSTransactionType.P2A_VERIFICATION;
				request.benfAccNo = de120.get("062");
				request.benfIFSC  = de120.get("059");

				request.accountNo = de120.get("062");
				request.ifscCode  = de120.get("059");
			} else {
				request.transType = IMPSTransactionType.P2P_VERIFICATION;
				final String         mmid           = message.get(2).substring(0, 4) + de120.get("049");
				final String         mobile         = message.get(2).substring(9);
				final AccountDetails accountDetails = dispatcher.databaseService.getAccountDetails(mobile, mmid, logger);
				if (accountDetails == null) {
					logger.info("account details not found.");
					return new VerificationResponse("M0", "Account Not found.");
				}
				request.benfAccNo = accountDetails.accNo15;
				request.accountNo = accountDetails.accNo15;
				//Not sent by earlier versions.
				//request.benfIFSC = de120.get("059");
				request.benfMMID   = message.get(2).substring(0, 7);
				request.benfMobile = "91" + message.get(2).substring(9);
			}

			request.narration      = de120.get("051");
			request.remitterAccNo  = message.get(102);
			request.remitterMMID   = de120.get("050").substring(0, 7);
			request.remitterMobile = "91" + de120.get("050").substring(7);
			request.RRNNo          = message.get(37);
			request.transAmt       = Double.parseDouble(message.get(4)) / 100.0;
			final RetroCoreBankingService           service  = retrofit.create(RetroCoreBankingService.class);
			final Call<IMPSTransactionResponse>     call     = service.verification(request, logger);
			final Response<IMPSTransactionResponse> response = call.execute();
			impsTransactionResponse = response.body();
			return new VerificationResponse(impsTransactionResponse, request.benfAccNo, message.get(11));
		} catch (ConnectException e) {
			e.printStackTrace();
			impsTransactionResponse.errorCode = IMPSResponseCode.ISSUER_NODE_OFFLINE;
			return new VerificationResponse(impsTransactionResponse);
		} catch (Exception e) {
			e.printStackTrace();
			impsTransactionResponse.errorCode = IMPSResponseCode.PROCESSOR_DOWN;
			return new VerificationResponse(impsTransactionResponse);
		}
	}

}
