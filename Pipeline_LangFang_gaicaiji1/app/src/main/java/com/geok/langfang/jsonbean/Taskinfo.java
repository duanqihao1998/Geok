package com.geok.langfang.jsonbean;

import java.util.ArrayList;
import java.util.List;

public class Taskinfo {

	String FREQINFO;
	String FREQINDEX;

	Freqinfo info = new Freqinfo();
	List<Freqinfo> infoList = new ArrayList<Freqinfo>();

	public Freqinfo getInfo() {
		return info;
	}

	public void setInfo(Freqinfo info) {
		this.info = info;
	}

	public List<Freqinfo> getInfoList() {
		return infoList;
	}

	public void setInfoList(List<Freqinfo> infoList) {
		this.infoList = infoList;
	}

	public String getFREQINFO() {
		return FREQINFO;
	}

	public void setFREQINFO(String fREQINFO) {
		FREQINFO = fREQINFO;
	}

	public String getFREQINDEX() {
		return FREQINDEX;
	}

	public void setFREQINDEX(String fREQINDEX) {
		FREQINDEX = fREQINDEX;
	}
}
