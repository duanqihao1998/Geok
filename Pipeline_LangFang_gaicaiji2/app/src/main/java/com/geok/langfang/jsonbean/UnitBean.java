package com.geok.langfang.jsonbean;

public class UnitBean {
	/**
	 * EVENTID部门id, DEPARTMENT：部门名称，PARENTDEPARTMENT：上一级部门名称
	 */

	String EVENTID;
	String DEPARTMENT;
	String PARENTDEPARTMENT;

	public String getEVENTID() {
		return EVENTID;
	}

	public void setEVENTID(String eVENTID) {
		EVENTID = eVENTID;
	}

	public String getDEPARTMENT() {
		return DEPARTMENT;
	}

	public void setDEPARTMENT(String dEPARTMENT) {
		DEPARTMENT = dEPARTMENT;
	}

	public String getPARENTDEPARTMENT() {
		return PARENTDEPARTMENT;
	}

	public void setPARENTDEPARTMENT(String pARENTDEPARTMENT) {
		PARENTDEPARTMENT = pARENTDEPARTMENT;
	}

}
