package com.geok.langfang.pipeline.dataacquisition;

import java.io.Serializable;

public class NaturalHistoryData implements Serializable {

	/*
	 * guid userid,year,line,pile,value,test_time,
	 * voltage,temperature,weather,remarks,lineid,pileid
	 */
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getTest_time() {
		return test_time;
	}

	public void setTest_time(String test_time) {
		this.test_time = test_time;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
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

	public String getPile() {
		return pile;
	}

	public void setPile(String pile) {
		this.pile = pile;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTesttime() {
		return test_time;
	}

	public void setTesttime(String test_time) {
		this.test_time = test_time;
	}

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getIsupload() {
		return isupload;
	}

	public void setIsupload(int isupload) {
		this.isupload = isupload;
	}

	private String guid;
	private String userid;
	private String year;
	private String line;
	private String pile;
	private String value;
	private String test_time;
	private String voltage;
	private String temperature;
	private String weather;
	private String remarks;
	private String lineid;
	private String pileid;
	private String month;
	private int isupload;

}
