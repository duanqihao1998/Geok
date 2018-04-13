package com.geok.langfang.jsonbean;

import java.io.Serializable;

public class HistoryDataGroundBean implements Serializable {

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

	public String getCPGROUNDBEDEVENTID() {
		return CPGROUNDBEDEVENTID;
	}

	public void setCPGROUNDBEDEVENTID(String cpgroundbedeventid) {
		this.CPGROUNDBEDEVENTID = cpgroundbedeventid;
	}

	public String getINSPECTOR() {
		return INSPECTOR;
	}

	public void setINSPECTOR(String userid) {
		this.INSPECTOR = userid;
	}

	public String getTESTDATE() {
		return TESTDATE;
	}

	public void setTESTDATE(String test_date) {
		this.TESTDATE = test_date;
	}

	public String getSETVALUE() {
		return SETVALUE;
	}

	public void setSETVALUE(String set_value) {
		this.SETVALUE = set_value;
	}

	public String getTESTVALUE() {
		return TESTVALUE;
	}

	public void setTESTVALUE(String test_value) {
		this.TESTVALUE = test_value;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getYEAR() {
		return YEAR;
	}

	public void setYEAR(String yEAR) {
		YEAR = yEAR;
	}

	public String getHALFYEAR() {
		return HALFYEAR;
	}

	public void setHALFYEAR(String hALFYEAR) {
		HALFYEAR = hALFYEAR;
	}

	public String getCONCLUSION() {
		return CONCLUSION;
	}

	public void setCONCLUSION(String conclusion) {
		this.CONCLUSION = conclusion;
	}

	public int getIsupload() {
		return isupload;
	}

	public void setIsupload(int isupload) {
		this.isupload = isupload;
	}

	private String guid;
	private String CPGROUNDBEDEVENTID;
	private String userid;
	private String TESTDATE;
	private String SETVALUE;
	private String TESTVALUE;
	private String YEAR;
	private String HALFYEAR;
	private String CONCLUSION;
	private String INSPECTOR;
	private int isupload;

}
