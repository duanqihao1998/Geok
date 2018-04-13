package com.geok.langfang.jsonbean;

public class NatureVoltageQueryBean {
	/**
	 * EVENTID:唯一标识,DEPARTMENTID:部门编号,YEAR:年份,ACINTERFERENCEVOLTAGE:交流干扰电压(V)
	 * ,MARKEREVENTID
	 * ;桩REMARK:备注,TEMPERATURE:温度,TESTDATE:测试日期,USERID:测试人员VOLTAGE:电位值
	 */
	String EVENTID;
	String DEPARTMENTID;
	String YEAR;
	String ACINTERFERENCEVOLTAGE;
	String MARKEREVENTID;
	String REMARK;
	String TEMPERATURE;
	String TESTDATE;
	String USERID;
	String VOLTAGE;

	public NatureVoltageQueryBean() {
	}

	public NatureVoltageQueryBean(String EVENTID, String DEPARTMENTID, String YEAR,
			String ACINTERFERENCEVOLTAGE, String MARKEREVENTID, String REMARK, String TEMPERATURE,
			String TESTDATE, String USERID, String VOLTAGE) {
		this.EVENTID = EVENTID;
		this.DEPARTMENTID = DEPARTMENTID;
		this.YEAR = YEAR;
		this.ACINTERFERENCEVOLTAGE = ACINTERFERENCEVOLTAGE;
		this.MARKEREVENTID = MARKEREVENTID;
		this.REMARK = REMARK;
		this.TEMPERATURE = TEMPERATURE;
		this.TESTDATE = TESTDATE;
		this.USERID = USERID;
		this.VOLTAGE = VOLTAGE;
	}

	public String getEVENTID() {
		return EVENTID;
	}

	public void setEVENTID(String eVENTID) {
		EVENTID = eVENTID;
	}

	public String getDEPARTMENTID() {
		return DEPARTMENTID;
	}

	public void setDEPARTMENTID(String DEPARTMENTID) {
		this.DEPARTMENTID = DEPARTMENTID;
	}

	public String getYEAR() {
		return YEAR;
	}

	public void setYEAR(String yEAR) {
		YEAR = yEAR;
	}

	public String getACINTERFERENCEVOLTAGE() {
		return ACINTERFERENCEVOLTAGE;
	}

	public void setACINTERFERENCEVOLTAGE(String aCINTERFERENCEVOLTAGE) {
		ACINTERFERENCEVOLTAGE = aCINTERFERENCEVOLTAGE;
	}

	public String getMARKEREVENTID() {
		return MARKEREVENTID;
	}

	public void setMARKEREVENTID(String mARKEREVENTID) {
		MARKEREVENTID = mARKEREVENTID;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}

	public String getTEMPERATURE() {
		return TEMPERATURE;
	}

	public void setTEMPERATURE(String tEMPERATURE) {
		TEMPERATURE = tEMPERATURE;
	}

	public String getTESTDATE() {
		return TESTDATE;
	}

	public void setTESTDATE(String tESTDATE) {
		TESTDATE = tESTDATE;
	}

	public String getUSERID() {
		return USERID;
	}

	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}

	public String getVOLTAGE() {
		return VOLTAGE;
	}

	public void setVOLTAGE(String vOLTAGE) {
		VOLTAGE = vOLTAGE;
	}

}
