package com.geok.langfang.pipeline.problem;

import java.io.Serializable;

/**
 * 
 * @author sunshihai 问题上报历史记录的数据Bean 实现Serializable接口是为了在数据的传递上的方便
 * 
 */

public class ProblemHistoryData implements Serializable {

	/*
	 * ID type guid lon lat occurtime,photopath,
	 * photodes,line,lineid,pile,pileid
	 * ,offset,userId,uploadtime,problemdes,isupload
	 */
	private String type;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	private String location;
	private String plan;
	private String result;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
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

	public String getOccurtime() {
		return occurtime;
	}

	public void setOccurtime(String occurtime) {
		this.occurtime = occurtime;
	}

	public String getPhotopath() {
		return photopath;
	}

	public void setPhotopath(String photopath) {
		this.photopath = photopath;
	}

	public String getPhotodes() {
		return photodes;
	}

	public void setPhotodes(String photodes) {
		this.photodes = photodes;
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

	public String getPile() {
		return pile;
	}

	public void setPile(String pile) {
		this.pile = pile;
	}

	public String getPileid() {
		return pileid;
	}

	public void setPileid(String pileid) {
		this.pileid = pileid;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}

	public String getProblemdes() {
		return problemdes;
	}

	public void setProblemdes(String problemdes) {
		this.problemdes = problemdes;
	}

	public int getIsupload() {
		return isupload;
	}

	public void setIsupload(int isupload) {
		this.isupload = isupload;
	}

	private String guid;
	private String lon;
	private String lat;
	private String occurtime;
	private String photopath;
	private String photodes;
	private String line;
	private String lineid;
	private String pile;
	private String pileid;
	private String offset;
	private String userid;
	private String uploadtime;
	private String problemdes;
	private int isupload;

}
