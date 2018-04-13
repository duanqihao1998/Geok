package com.geok.langfang.jsonbean;

public class CppotentialQueryBean {
	/*
	 * EVENTID:唯一标识,DEPARTMENTID:部门编号,WEATHER:天气,ACINTERFERENCEVOLTAGE:交流干扰电压(V),
	 * MARKEREVENTID
	 * ;桩,REMARK:备注,SOILRESISTIVITY:土壤电阻率(Ωom),USERID:测试人员,VOLTAGE:电位值
	 * ,TEMPERATURE:温度,TESTDATE:测试日期
	 */
	String EVENTID;
	String DEPARTMENTID;
	String WEATHER;
	String ACINTERFERENCEVOLTAGE;
	String MARKEREVENTID;
	String REMARK;
	String SOILRESISTIVITY;
	String USERID;
	String VOLTAGE;
	String TEMPERATURE;
	String TESTDATE;

	public CppotentialQueryBean() {
	}

	public CppotentialQueryBean(String EVENTID, String DEPARTMENTID, String WEATHER,
			String ACINTERFERENCEVOLTAGE, String MARKEREVENTID, String REMARK,
			String SOILRESISTIVITY, String USERID, String VOLTAGE, String TEMPERATURE,
			String TESTDATE) {
		this.EVENTID = EVENTID;
		this.DEPARTMENTID = DEPARTMENTID;
		this.WEATHER = WEATHER;
		this.ACINTERFERENCEVOLTAGE = ACINTERFERENCEVOLTAGE;
		this.MARKEREVENTID = MARKEREVENTID;
		this.REMARK = REMARK;
		this.SOILRESISTIVITY = SOILRESISTIVITY;
		this.USERID = USERID;
		this.VOLTAGE = VOLTAGE;
		this.TEMPERATURE = TEMPERATURE;
		this.TESTDATE = TESTDATE;
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

	public String getWEATHER() {
		return WEATHER;
	}

	public void setWEATHER(String wEATHER) {
		WEATHER = wEATHER;
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

	public String getSOILRESISTIVITY() {
		return SOILRESISTIVITY;
	}

	public void setSOILRESISTIVITY(String sOILRESISTIVITY) {
		SOILRESISTIVITY = sOILRESISTIVITY;
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

}
