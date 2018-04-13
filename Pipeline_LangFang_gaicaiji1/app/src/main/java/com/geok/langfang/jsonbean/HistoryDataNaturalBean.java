package com.geok.langfang.jsonbean;

import java.io.Serializable;

public class HistoryDataNaturalBean implements Serializable {

	/*
	 * ID type guid lon lat occurtime,photopath,
	 * photodes,line,lineid,pile,pileid
	 * ,offset,userId,uploadtime,problemdes,isupload
	 */
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getINSPECTOR() {
		return INSPECTOR;
	}

	public void setINSPECTOR(String userid) {
		this.INSPECTOR = userid;
	}

	public String getYEAR() {
		return YEAR;
	}

	public void setYEAR(String year) {
		this.YEAR = year;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMONTH() {
		return MONTH;
	}

	public void setMONTH(String mONTH) {
		MONTH = mONTH;
	}

	public String getLINELOOP() {
		return LINELOOP;
	}

	public void setLINELOOP(String line) {
		this.LINELOOP = line;
	}

	public String getMARKERNAME() {
		return MARKERNAME;
	}

	public void setMARKERNAME(String pile) {
		this.MARKERNAME = pile;
	}

	public String getVOLTAGE() {
		return VOLTAGE;
	}

	public void setVOLTAGE(String value) {
		this.VOLTAGE = value;
	}

	public String getTESTDATE() {
		return TESTDATE;
	}

	public void setTESTDATE(String test_time) {
		this.TESTDATE = test_time;
	}

	public String getACINTERFERENCEVOLTAGE() {
		return ACINTERFERENCEVOLTAGE;
	}

	public void setACINTERFERENCEVOLTAGE(String voltage) {
		this.ACINTERFERENCEVOLTAGE = voltage;
	}

	public String getTEMPERATURE() {
		return TEMPERATURE;
	}

	public void setTEMPERATURE(String temperature) {
		this.TEMPERATURE = temperature;
	}

	public String getWEATHER() {
		return WEATHER;
	}

	public void setWEATHER(String weather) {
		this.WEATHER = weather;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String remarks) {
		this.REMARK = remarks;
	}

	public int getIsupload() {
		return isupload;
	}

	public void setIsupload(int isupload) {
		this.isupload = isupload;
	}

	private String guid;
	private String userid;
	private int isupload;

	private String YEAR;
	private String MONTH;
	private String LINELOOP;
	private String MARKERNAME;
	private String VOLTAGE;
	private String TESTDATE;
	private String ACINTERFERENCEVOLTAGE;
	private String TEMPERATURE;
	private String WEATHER;
	private String REMARK;
	private String INSPECTOR;

}
