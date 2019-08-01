package org.util.npci.imps.cbs;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

import org.util.datautil.TLV;
import org.util.iso8583.ISO8583Message;
import org.util.iso8583.npci.constants.IMPSTransactionType;
import org.util.nanolog.Logger;
import org.util.npci.api.ConfigurationNotFoundException;
import org.util.npci.api.PropertyName;
import org.util.npci.coreconnect.CoreConfig;
import org.util.npci.coreconnect.util.RetroClientBuilder;
import org.util.npci.imps.IMPSDispatcher;
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

		@POST("InwardImpsTransaction")
		Call<IMPSTransactionResponse> transaction(@Body IMPSTransactionRequest request, @Tag Logger logger);

		@POST("impsBeneficiaryVerification")
		Call<IMPSTransactionResponse> verification(@Body IMPSTransactionRequest request);
	}

	private final Retrofit retrofit;

	public Swift63CoreBankingService(final CoreConfig config, final IMPSDispatcher dispatcher) throws ConfigurationNotFoundException {
		super(config, dispatcher);
		System.out.println(PropertyName.CBS_IP+" : "+config.getString(PropertyName.CBS_IP));
		retrofit = RetroClientBuilder.newBuilder().baseURL(config.getString(PropertyName.CBS_IP))
				.withLogging(config.getStringSupressException(PropertyName.RETROFIT_LOGGING_LEVEL))
				.readTimeout(config.getIntSupressException(PropertyName.RETROFIT_READ_TIMEOUT_SEC), TimeUnit.SECONDS)
				.writeTimeout(config.getIntSupressException(PropertyName.RETROFIT_WRITE_TIMEOUT_SEC), TimeUnit.SECONDS)
				.connectTimeout(config.getIntSupressException(PropertyName.RETROFIT_CONNECT_TIMEOUT_SEC), TimeUnit.SECONDS).build();
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
			TLV                    de120   = TLV.parse(message.get(120));
			IMPSTransactionRequest request = new IMPSTransactionRequest();

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
			RetroCoreBankingService           service  = retrofit.create(RetroCoreBankingService.class);
			Call<IMPSTransactionResponse>     call     = service.transaction(request, logger);
			Response<IMPSTransactionResponse> response = call.execute();
			impsTransactionResponse = response.body();
			return new TansactionResponse(impsTransactionResponse, request.benfAccNo, message.get(11));
		} catch (ConnectException e) {
			logger.error(e);
			impsTransactionResponse.errorCode = "08";
			return new TansactionResponse(impsTransactionResponse);
		} catch (Exception e) {
			logger.error(e);
			impsTransactionResponse.errorCode = "91";
			return new TansactionResponse(impsTransactionResponse);
		}

	}

	@Override
	public final VerificationResponse verification(final ISO8583Message message, final Logger logger) {
		IMPSTransactionResponse impsTransactionResponse = new IMPSTransactionResponse();
		try {
			TLV                    de120   = TLV.parse(message.get(120));
			IMPSTransactionRequest request = new IMPSTransactionRequest();

			if (de120.get("001").equals(IMPSTransactionType.P2A_VERIFICATION)) {
				request.transType = IMPSTransactionType.P2A_VERIFICATION;
				request.benfAccNo = de120.get("062");
				request.benfIFSC  = de120.get("059");

				request.accountNo = de120.get("062");
				request.ifscCode  = de120.get("059");
			} else {
				request.transType = IMPSTransactionType.P2P_VERIFICATION;
				final String   mmid           = message.get(2).substring(0, 4) + de120.get("049");
				final String   mobile         = message.get(2).substring(9);
				AccountDetails accountDetails = dispatcher.databaseService.getAccountDetails(mobile, mmid, logger);
				;
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
			RetroCoreBankingService           service  = retrofit.create(RetroCoreBankingService.class);
			Call<IMPSTransactionResponse>     call     = service.verification(request);
			Response<IMPSTransactionResponse> response = call.execute();
			impsTransactionResponse = response.body();
			return new VerificationResponse(impsTransactionResponse, request.benfAccNo, message.get(11));
		} catch (ConnectException e) {
			e.printStackTrace();
			impsTransactionResponse.errorCode = "08";
			return new VerificationResponse(impsTransactionResponse);
		} catch (Exception e) {
			e.printStackTrace();
			impsTransactionResponse.errorCode = "91";
			return new VerificationResponse(impsTransactionResponse);
		}
	}

}
