package com.geok.langfang.jsonbean;

public class InspectionPlanQueryBean {

	/**
	 * 字段说明 TYPE:巡检类型 1代表已巡检记录,0代表未巡检记录; EVENTID:巡检eventid, LINELOOPNAME:管线名称;
	 * BEGINDATETIME:巡检开始时间, ENDDATETIME:巡检结束时间
	 */
	String TYPE;
	String EVENTID;
	String LINELOOPNAME;
	String BEGINDATETIME;
	String ENDDATETIME;

	public void InspectionPlanQueryBean() {
	}

	public void InspectionPlanQueryBean(String TYPE, String EVENTID, String LINELOOPNAME,
			String BEGINDATETIME, String ENDDATETIME) {
		this.TYPE = TYPE;
		this.EVENTID = EVENTID;
		this.LINELOOPNAME = LINELOOPNAME;
		this.BEGINDATETIME = BEGINDATETIME;
		this.ENDDATETIME = ENDDATETIME;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public String getEVENTID() {
		return EVENTID;
	}

	public void setEVENTID(String eVENTID) {
		EVENTID = eVENTID;
	}

	public String getLINELOOPNAME() {
		return LINELOOPNAME;
	}

	public void setLINELOOPNAME(String lINELOOPNAME) {
		LINELOOPNAME = lINELOOPNAME;
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

}
