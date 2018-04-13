package com.geok.langfang.jsonbean;

public class InspectionHistoryDetile2 {

	/*
	 * 应巡检起始时间:BEGINTIME 应巡线终止时间:ENDTIME 未巡检人员:NOTPECTOR 未巡检原因:NOTREASON
	 * 未巡检原因描述:NOTREMARK
	 */

	private String BEGINTIME;

	public String getBEGINTIME() {
		return BEGINTIME;
	}

	public void setBEGINTIME(String bEGINTIME) {
		BEGINTIME = bEGINTIME;
	}

	public String getENDTIME() {
		return ENDTIME;
	}

	public void setENDTIME(String eNDTIME) {
		ENDTIME = eNDTIME;
	}

	public String getNOTPECTOR() {
		return NOTPECTOR;
	}

	public void setNOTPECTOR(String nOTPECTOR) {
		NOTPECTOR = nOTPECTOR;
	}

	public String getNOTREASON() {
		return NOTREASON;
	}

	public void setNOTREASON(String nOTREASON) {
		NOTREASON = nOTREASON;
	}

	public String getNOTREMARK() {
		return NOTREMARK;
	}

	public void setNOTREMARK(String nOTREMARK) {
		NOTREMARK = nOTREMARK;
	}

	private String ENDTIME;
	private String NOTPECTOR;
	private String NOTREASON;
	private String NOTREMARK;
}
