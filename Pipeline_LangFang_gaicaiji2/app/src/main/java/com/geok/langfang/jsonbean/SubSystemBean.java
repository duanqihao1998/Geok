package com.geok.langfang.jsonbean;

public class SubSystemBean {
	/**
	 * DEPARTMENT:部门名称，LINELOOPNAME：管线名称，BEGINSTATION：起始位置，ENDSTATION：终止位置
	 */
	String DEPARTMENT;
	String LINELOOPNAME;
	String BEGINSTATION;
	String ENDSTATION;

	public String getDEPARTMENT() {
		return DEPARTMENT;
	}

	public void setDEPARTMENT(String dEPARTMENT) {
		DEPARTMENT = dEPARTMENT;
	}

	public String getLINELOOPNAME() {
		return LINELOOPNAME;
	}

	public void setLINELOOPNAME(String lINELOOPNAME) {
		LINELOOPNAME = lINELOOPNAME;
	}

	public String getBEGINSTATION() {
		return BEGINSTATION;
	}

	public void setBEGINSTATION(String bEGINSTATION) {
		BEGINSTATION = bEGINSTATION;
	}

	public String getENDSTATION() {
		return ENDSTATION;
	}

	public void setENDSTATION(String eNDSTATION) {
		ENDSTATION = eNDSTATION;
	}

}
