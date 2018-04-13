package com.geok.langfang.jsonbean;

import java.util.ArrayList;
import java.util.List;

public class GpsLine {
	String LINENAME;
	String LINEID;
	List<GpsMarker> markerLsit = new ArrayList<GpsMarker>();

	public String getLINENAME() {
		return LINENAME;
	}

	public void setLINENAME(String lINENAME) {
		LINENAME = lINENAME;
	}

	public String getLINEID() {
		return LINEID;
	}

	public void setLINEID(String lINEID) {
		LINEID = lINEID;
	}

	public List<GpsMarker> getMarkerLsit() {
		return markerLsit;
	}

	public void setMarkerLsit(List<GpsMarker> markerLsit) {
		this.markerLsit = markerLsit;
	}

}
