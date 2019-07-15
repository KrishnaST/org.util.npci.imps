package org.util.npci.imps.cbs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IMPSTransactionResponse {

	public String  response;
	public String  errorMessage;
	public String  errorCode;
	public String  rrnNo;
	public String  stan;
	public String  nickNameDebit;
	public String  nickNameCredit;
	public String  category;
	public String  URL;
	public boolean isValid;
	public String  nBin;
	public String  bankName;
	public String  refNo;
	public String  custNo;;
	public String  lbrCode0;
	public String  mobileNo;
	public String  accountNo;
}
