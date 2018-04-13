package com.geok.langfang.jsonbean;

public class Freqinfo {
	/**
	 * POINTNAME:桩或关键点的名称 POINTID:桩或关键点的EVENTID, EVENTID: 巡检点EVENTID
	 * LON:经度,LAT:纬度，POINTTYPE:巡检点类型,isXun是否已巡过
	 */

	String POINTNAME;
	String EVENTID;
	String POINTID;
	String LON;
	String LAT;
	String POINTTYPE;
	String LineID;
	String Station;

	public String getLineID() {
		return LineID;
	}

	public void setLineID(String lineID) {
		LineID = lineID;
	}

	public String getStation() {
		return Station;
	}

	public void setStation(String station) {
		Station = station;
	}

	boolean isXun = false;

	public boolean getIsXun() {
		return isXun;
	}

	public void setIsXun(boolean isXun) {
		this.isXun = isXun;
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

	public String getPOINTTYPE() {
		return POINTTYPE;
	}

	public void setPOINTTYPE(String pOINTTYPE) {
		POINTTYPE = pOINTTYPE;
	}

	public String getPOINTNAME() {
		return POINTNAME;
	}

	public void setPOINTNAME(String pOINTNAME) {
		POINTNAME = pOINTNAME;
	}

	public String getEVENTID() {
		return EVENTID;
	}

	public void setEVENTID(String eVENTID) {
		EVENTID = eVENTID;
	}

	public String getPOINTID() {
		return POINTID;
	}

	public void setPOINTID(String pOINTID) {
		POINTID = pOINTID;
	}

}
