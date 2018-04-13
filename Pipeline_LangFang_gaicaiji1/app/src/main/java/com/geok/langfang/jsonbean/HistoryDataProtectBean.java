package com.geok.langfang.jsonbean;

import java.io.Serializable;

public class HistoryDataProtectBean implements Serializable {

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

	public String getMONTH() {
		return MONTH;
	}

	public void setMONTH(String month) {
		this.MONTH = month;
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

	public String getSOILRESISTIVITY() {
		return SOILRESISTIVITY;
	}

	public void setSOILRESISTIVITY(String ground) {
		this.SOILRESISTIVITY = ground;
	}

	public String getTEMPERATURE() {
		return TEMPERATURE;
	}

	public void setTEMPERATURE(String temperature) {
		this.TEMPERATURE = temperature;
	}

	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getNATURAL() {
		return NATURAL;
	}

	public void setNATURAL(String nATURAL) {
		NATURAL = nATURAL;
	}

	public String getIR() {
		return IR;
	}

	public void setIR(String iR) {
		IR = iR;
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
	private String YEAR;
	private String MONTH;
	private String LINELOOP;
	private String MARKERNAME;
	private String VOLTAGE;
	private String TESTDATE;
	private String ACINTERFERENCEVOLTAGE;
	private String SOILRESISTIVITY;
	private String TEMPERATURE;
	private String REMARK;
	private String INSPECTOR;
	private String NATURAL;
	private String IR;
	private int isupload;

}
