package com.geok.langfang.jsonbean;

public class MyTaskBean {
	/**
	 * INSPECTTIME:巡检时间,INSPECTLINE:巡检线路,MARKER:桩,MARKEREVENTID:桩id, INSFREQ
	 */
	String INSPECTTIME;
	String INSPECTLINE;
	String INSFREQ;
	String MARKEREVENTID;

	public MyTaskBean() {
	}

	public MyTaskBean(String INSPECTTIME, String INSPECTLINE, String INSFREQ, String MARKEREVENTID) {
		this.INSPECTTIME = INSPECTTIME;
		this.INSPECTLINE = INSPECTLINE;
		this.INSFREQ = INSFREQ;
		this.MARKEREVENTID = MARKEREVENTID;
	}

	public String getINSPECTTIME() {
		return INSPECTTIME;
	}

	public void setINSPECTTIME(String iNSPECTTIME) {
		INSPECTTIME = iNSPECTTIME;
	}

	public String getINSPECTLINE() {
		return INSPECTLINE;
	}

	public void setINSPECTLINE(String iNSPECTLINE) {
		INSPECTLINE = iNSPECTLINE;
	}

	public String getINSFREQ() {
		return INSFREQ;
	}

	public void setINSFREQ(String iNSFREQ) {
		INSFREQ = iNSFREQ;
	}

	public String getMARKEREVENTID() {
		return MARKEREVENTID;
	}

	public void setMARKEREVENTID(String mARKEREVENTID) {
		MARKEREVENTID = mARKEREVENTID;
	}

}
