package com.geok.langfang.pipeline.dataacquisition;

import java.io.Serializable;

public class GroundHistoryData implements Serializable {

	/*
	 * guid,cpgroundbedeventid,userid,test_date,set_value,test_value,
	 * temperature,temperature,weather,conclusion,lineid,pileid
	 */
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getCpgroundbedeventid() {
		return cpgroundbedeventid;
	}

	public void setCpgroundbedeventid(String cpgroundbedeventid) {
		this.cpgroundbedeventid = cpgroundbedeventid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTestdate() {
		return test_date;
	}

	public void setTestdate(String test_date) {
		this.test_date = test_date;
	}

	public String getSetvalue() {
		return set_value;
	}

	public void setSetvalue(String set_value) {
		this.set_value = set_value;
	}

	public String getTestvalue() {
		return test_value;
	}

	public void setTestvalue(String test_value) {
		this.test_value = test_value;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getLineid() {
		return lineid;
	}

	public void setLineid(String lineid) {
		this.lineid = lineid;
	}

	public String getPileid() {
		return pileid;
	}

	public void setPileid(String pileid) {
		this.pileid = pileid;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getHalfyear() {
		return halfyear;
	}

	public void setHalfyear(String halfyear) {
		this.halfyear = halfyear;
	}

	public int getIsupload() {
		return isupload;
	}

	public void setIsupload(int isupload) {
		this.isupload = isupload;
	}

	private String guid;
	private String cpgroundbedeventid;
	private String userid;
	private String test_date;
	private String set_value;
	private String test_value;
//	private String temperature;
//	private String weather;
	private String conclusion;
	private String lineid;
	private String pileid;
	private String year;
	private String halfyear;
	private int isupload;

}
