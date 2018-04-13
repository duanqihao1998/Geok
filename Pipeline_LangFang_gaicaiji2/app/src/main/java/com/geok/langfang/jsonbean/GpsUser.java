package com.geok.langfang.jsonbean;

import java.util.ArrayList;
import java.util.List;

public class GpsUser {
	String NAME;
	String UNITID;
	String LON;
	String LAT;

	List<GpsLocation> gpsList = new ArrayList<GpsLocation>();

	public List<GpsLocation> getGpsList() {
		return gpsList;
	}

	public void setGpsList(List<GpsLocation> gpsList) {
		this.gpsList = gpsList;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getUNITID() {
		return UNITID;
	}

	public void setUNITID(String uNITID) {
		UNITID = uNITID;
	}

	public String getLON() {
		return LON;
	}

	public void setLON(String lON) {
		LON = lON;
	}

	public String getLAT() {
		return LAT;
	}

	public void setLAT(String lAT) {
		LAT = lAT;
	}

}
