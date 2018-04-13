package com.geok.langfang.jsonbean;

public class InspectionHistoryBean {

	/*
	 * EVENTID:唯一标识 BEGINDATETIME:出发时间 ENDDATETIME:返回时间 INSPECTOR:巡检人员
	 * TYPE:巡检记录类型,0:未巡检记录,1:巡检记录
	 * {"EVENTID":"","BEGINDATETIME":"","ENDDATETIME":
	 * "","INSPECTOR":"","INSTYPE":""}
	 */
	private String EVENTID;

	public String getEVENTID() {
		return EVENTID;
	}

	public void setEVENTID(String eVENTID) {
		EVENTID = eVENTID;
	}

	public String getBEGINDATETIME() {
		return BEGINDATETIME;
	}

	public void setBEGINDATETIME(String bEGINDATETIME) {
		BEGINDATETIME = bEGINDATETIME;
	}

	public String getENDDATETIME() {
		return ENDDATETIME;
	}

	public void setENDDATETIME(String eNDDATETIME) {
		ENDDATETIME = eNDDATETIME;
	}

	public String getINSPECTOR() {
		return INSPECTOR;
	}

	public void setINSPECTOR(String iNSPECTOR) {
		INSPECTOR = iNSPECTOR;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	private String BEGINDATETIME;
	private String ENDDATETIME;
	private String INSPECTOR;
	private String TYPE;

}
