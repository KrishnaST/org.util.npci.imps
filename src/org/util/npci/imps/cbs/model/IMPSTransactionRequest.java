package org.util.npci.imps.cbs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class IMPSTransactionRequest {

	public String remitterMobile;
	public String remitterMMID;
	public String benfMobile;
	public String benfMMID;
	public String benfAccNo;
	public String benfIFSC;
	public Double transAmt;
	public String transType;
	public String narration;
	public String remitterAccNo;
	public String RRNNo;
	
	public String accountNo;
	public String ifscCode;
	
}
