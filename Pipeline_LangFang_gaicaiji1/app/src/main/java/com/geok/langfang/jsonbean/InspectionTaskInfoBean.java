package com.geok.langfang.jsonbean;

public class InspectionTaskInfoBean {

	/*
	 * 巡检出发时间:BEGINDATETIME, 巡线返回时间:ENDDATETIME, 巡检类型:INSTYPE,
	 * 巡检人员(类型):INSPECTORTYPE, 巡检工具:INSVEHICLE, 巡检频次:INSFREQ, 巡检合格率:INSYIELD,
	 * 巡检仪:INSDEVICE, 巡检轨迹点数:TRACKPOINTS, 平均时速:AVGSPEED EVENTID 唯一标识
	 */
	private String BEGINDATETIME;
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

	public String getINSTYPE() {
		return INSTYPE;
	}

	public void setINSTYPE(String iNSTYPE) {
		INSTYPE = iNSTYPE;
	}

	public String getINSPECTORTYPE() {
		return INSPECTORTYPE;
	}

	public void setINSPECTORTYPE(String iNSPECTORTYPE) {
		INSPECTORTYPE = iNSPECTORTYPE;
	}

	public String getINSVEHICLE() {
		return INSVEHICLE;
	}

	public void setINSVEHICLE(String iNSVEHICLE) {
		INSVEHICLE = iNSVEHICLE;
	}

	public String getINSFREQ() {
		return INSFREQ;
	}

	public void setINSFREQ(String iNSFREQ) {
		INSFREQ = iNSFREQ;
	}

	public String getINSYIELD() {
		return INSYIELD;
	}

	public void setINSYIELD(String iNSYIELD) {
		INSYIELD = iNSYIELD;
	}

	public String getINSDEVICE() {
		return INSDEVICE;
	}

	public void setINSDEVICE(String iNSDEVICE) {
		INSDEVICE = iNSDEVICE;
	}

	public String getTRACKPOINTS() {
		return TRACKPOINTS;
	}

	public void setTRACKPOINTS(String tRACKPOINTS) {
		TRACKPOINTS = tRACKPOINTS;
	}

	public String getAVGSPEED() {
		return AVGSPEED;
	}

	public void setAVGSPEED(String aVGSPEED) {
		AVGSPEED = aVGSPEED;
	}

	private String ENDDATETIME;
	private String INSTYPE;
	private String INSPECTORTYPE;
	private String INSVEHICLE;
	private String INSFREQ;
	private String INSYIELD;
	private String INSDEVICE;
	private String TRACKPOINTS;
	private String AVGSPEED;

}
