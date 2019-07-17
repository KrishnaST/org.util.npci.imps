package org.util.npci.imps.cbs.model;

public class VerificationResponse {

	public String responseCode;
	public String responseMessage;
	public String authCode;
	public String beneficiaryName;

	public VerificationResponse() {}

	public VerificationResponse(String responseCode, String responseMessage) {
		this.responseCode    = responseMessage;
		this.responseMessage = responseMessage;
	}

	public VerificationResponse(IMPSTransactionResponse impsTransactionResponse) {
		if (impsTransactionResponse == null || impsTransactionResponse.errorCode == null) responseCode = "91";
		else {
			responseCode = impsTransactionResponse.errorCode;
			responseMessage = impsTransactionResponse.errorMessage;
			if(impsTransactionResponse.rrnNo != null && impsTransactionResponse.rrnNo.length() == 12 && "00".equals(responseCode)) authCode = impsTransactionResponse.rrnNo.substring(6);
			beneficiaryName = impsTransactionResponse.nickNameCredit;
			if(responseCode == null || responseCode.equals("")) responseCode = "91";
		}
	
	}
}
