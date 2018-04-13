package com.geok.langfang.jsonbean;

public class GpsMarker {

	/**
	 * comment on table GPS_MARKER is '桩坐标关系表'; LINELOOPID is '管线EVENTID';
	 * MARKERID is '桩EVENTID'; MARKERNAME is '桩名称'; MARKERSTATION is '桩里程值';
	 * MARKERTYPE is '桩类型'; LON is '经度'; LAT is '纬度'; REMARK is '备注';
	 */
	private String markerid;
	private String lineloopid;
	private String markername;
	private String markerstation;
	private String markertype;
	private String lon;
	private String lat;
	private String remark;
	private String active;

	public GpsMarker() {
	}

	public String getMarkerid() {
		return markerid;
	}

	public void setMarkerid(String markerid) {
		this.markerid = markerid;
	}

	public String getLineloopid() {
		return lineloopid;
	}

	public void setLineloopid(String lineloopid) {
		this.lineloopid = lineloopid;
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

	public String getMarkertype() {
		return markertype;
	}

	public void setMarkertype(String markertype) {
		this.markertype = markertype;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

}
