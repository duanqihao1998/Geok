package com.geok.langfang.jsonbean;

public class AntisepsisQueryBean {

	/**
	 * REPAIRTARGET:修复对象, LEAKHUNTINGDATE:检漏日期, LOCATION:里程位置(管线+桩+偏移量)
	 */
	String REPAIRTARGET;
	String LEAKHUNTINGDATE;
	String LOCATION;

	public AntisepsisQueryBean() {
	}

	public AntisepsisQueryBean(String REPAIRTARGET, String LEAKHUNTINGDATE, String LOCATION) {
		this.REPAIRTARGET = REPAIRTARGET;
		this.LEAKHUNTINGDATE = LEAKHUNTINGDATE;
		this.LOCATION = LOCATION;
	}

	public String getREPAIRTARGET() {
		return REPAIRTARGET;
	}

	public void setREPAIRTARGET(String rEPAIRTARGET) {
		REPAIRTARGET = rEPAIRTARGET;
	}

	public String getLEAKHUNTINGDATE() {
		return LEAKHUNTINGDATE;
	}

	public void setLEAKHUNTINGDATE(String lEAKHUNTINGDATE) {
		LEAKHUNTINGDATE = lEAKHUNTINGDATE;
	}

	public String getLOCATION() {
		return LOCATION;
	}

	public void setLOCATION(String lOCATION) {
		LOCATION = lOCATION;
	}

	// String EVENTID;
	// String DEPARTMENTID;
	// String BREAKLOCATION;
	// String CLOCKPOSITION;
	// String COATINGAREA;
	// String COATINGFACE;
	// String COATINGREPAIR;
	// String LEAKHUNTINGDATE;
	// String LEAKTYPE;
	// String LINELOOPEVENTID;
	// String PIPEDESCRIPTION;
	// String PITAMOUNT;
	// String PITAREA;
	// String PITDEPTHMAX;
	// String PITDEPTHMIN;
	// String REMARK;
	// String REPAIRDATE;
	// String REPAIRTYPE;
	// String SOIL;
	// public AntisepsisQueryBean(){}
	//
	// public AntisepsisQueryBean(String EVENTID, String DEPARTMENTID, String
	// BREAKLOCATION, String CLOCKPOSITION, String COATINGAREA, String
	// COATINGFACE, String COATINGREPAIR,
	// String LEAKHUNTINGDATE, String LEAKTYPE, String LINELOOPEVENTID, String
	// PIPEDESCRIPTION, String PITAMOUNT, String PITAREA, String PITDEPTHMAX,
	// String PITDEPTHMIN, String REMARK, String REPAIRDATE, String REPAIRTYPE,
	// String SOIL){
	//
	// }
	//
	// public String getEVENTID() {
	// return EVENTID;
	// }
	//
	// public void setEVENTID(String eVENTID) {
	// EVENTID = eVENTID;
	// }
	//
	// public String getDEPARTMENTID() {
	// return DEPARTMENTID;
	// }
	//
	// public void setDEPARTMENTID(String DEPARTMENTID) {
	// this.DEPARTMENTID = DEPARTMENTID;
	// }
	//
	// public String getBREAKLOCATION() {
	// return BREAKLOCATION;
	// }
	//
	// public void setBREAKLOCATION(String bREAKLOCATION) {
	// BREAKLOCATION = bREAKLOCATION;
	// }
	//
	// public String getCLOCKPOSITION() {
	// return CLOCKPOSITION;
	// }
	//
	// public void setCLOCKPOSITION(String cLOCKPOSITION) {
	// CLOCKPOSITION = cLOCKPOSITION;
	// }
	//
	// public String getCOATINGAREA() {
	// return COATINGAREA;
	// }
	//
	// public void setCOATINGAREA(String cOATINGAREA) {
	// COATINGAREA = cOATINGAREA;
	// }
	//
	// public String getCOATINGFACE() {
	// return COATINGFACE;
	// }
	//
	// public void setCOATINGFACE(String cOATINGFACE) {
	// COATINGFACE = cOATINGFACE;
	// }
	//
	// public String getCOATINGREPAIR() {
	// return COATINGREPAIR;
	// }
	//
	// public void setCOATINGREPAIR(String cOATINGREPAIR) {
	// COATINGREPAIR = cOATINGREPAIR;
	// }
	//
	// public String getLEAKHUNTINGDATE() {
	// return LEAKHUNTINGDATE;
	// }
	//
	// public void setLEAKHUNTINGDATE(String lEAKHUNTINGDATE) {
	// LEAKHUNTINGDATE = lEAKHUNTINGDATE;
	// }
	//
	// public String getLEAKTYPE() {
	// return LEAKTYPE;
	// }
	//
	// public void setLEAKTYPE(String lEAKTYPE) {
	// LEAKTYPE = lEAKTYPE;
	// }
	//
	// public String getLINELOOPEVENTID() {
	// return LINELOOPEVENTID;
	// }
	//
	// public void setLINELOOPEVENTID(String lINELOOPEVENTID) {
	// LINELOOPEVENTID = lINELOOPEVENTID;
	// }
	//
	// public String getPIPEDESCRIPTION() {
	// return PIPEDESCRIPTION;
	// }
	//
	// public void setPIPEDESCRIPTION(String pIPEDESCRIPTION) {
	// PIPEDESCRIPTION = pIPEDESCRIPTION;
	// }
	//
	// public String getPITAMOUNT() {
	// return PITAMOUNT;
	// }
	//
	// public void setPITAMOUNT(String pITAMOUNT) {
	// PITAMOUNT = pITAMOUNT;
	// }
	//
	// public String getPITAREA() {
	// return PITAREA;
	// }
	//
	// public void setPITAREA(String pITAREA) {
	// PITAREA = pITAREA;
	// }
	//
	// public String getPITDEPTHMAX() {
	// return PITDEPTHMAX;
	// }
	//
	// public void setPITDEPTHMAX(String pITDEPTHMAX) {
	// PITDEPTHMAX = pITDEPTHMAX;
	// }
	//
	// public String getPITDEPTHMIN() {
	// return PITDEPTHMIN;
	// }
	//
	// public void setPITDEPTHMIN(String pITDEPTHMIN) {
	// PITDEPTHMIN = pITDEPTHMIN;
	// }
	//
	// public String getREMARK() {
	// return REMARK;
	// }
	//
	// public void setREMARK(String rEMARK) {
	// REMARK = rEMARK;
	// }
	//
	// public String getREPAIRDATE() {
	// return REPAIRDATE;
	// }
	//
	// public void setREPAIRDATE(String rEPAIRDATE) {
	// REPAIRDATE = rEPAIRDATE;
	// }
	//
	// public String getREPAIRTYPE() {
	// return REPAIRTYPE;
	// }
	//
	// public void setREPAIRTYPE(String rEPAIRTYPE) {
	// REPAIRTYPE = rEPAIRTYPE;
	// }
	//
	// public String getSOIL() {
	// return SOIL;
	// }
	//
	// public void setSOIL(String sOIL) {
	// SOIL = sOIL;
	// }
	//
}
