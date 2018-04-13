package com.geok.langfang.Bean;

import java.io.Serializable;

public class KeypointHistoryBean implements Serializable {

	private String guid;
	private String userid;
	private String department;
	private String name;
	private String lineid;
	private String line;
	private String markerid;
	private String markername;
	private String markerstation;
	private String offset;
	private String buffer;
	private String start;
	private String end;
	private String lon;
	private String lat;
	private String description;
	private int isupload;
	private String time;
	private String locationdes;
	private String mileage;
	private String flag;
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLineid() {
		return lineid;
	}

	public void setLineid(String lineid) {
		this.lineid = lineid;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getMarkerid() {
		return markerid;
	}

	public void setMarkerid(String markerid) {
		this.markerid = markerid;
	}

	public String getMarkername() {
		return markername;
	}

	public void setMarkername(String markername) {
		this.markername = markername;
	}

	public String getMarkerstation() {
		return markerstation;
	}

	public void setMarkerstation(String markerstation) {
		this.markerstation = markerstation;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getBuffer() {
		return buffer;
	}

	public void setBuffer(String buffer) {
		this.buffer = buffer;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIsupload() {
		return isupload;
	}

	public void setIsupload(int isupload) {
		this.isupload = isupload;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time ) {
		this.time = time;
	}
	
	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	
	public String getLocationdes() {
		return locationdes;
	}

	public void setLocationdes(String locationdes) {
		this.locationdes = locationdes;
	}

	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
