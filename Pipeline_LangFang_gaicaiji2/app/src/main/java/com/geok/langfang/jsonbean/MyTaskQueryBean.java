package com.geok.langfang.jsonbean;

import java.util.ArrayList;
import java.util.List;

public class MyTaskQueryBean {
	/**
	 * INSDATE：巡检日期, INSDATEINFO:具体某天任务信息,INSPECTOR：巡检人员
	 */
	String INSPECTOR;
	String INSDATE;
	String INSDATEINFO;
	Insdateinfo insdateinfo = new Insdateinfo();
	List<Insdateinfo> insdateinfoList = new ArrayList<Insdateinfo>();

	public List<Insdateinfo> getInsdateinfoList() {
		return insdateinfoList;
	}

	public void setInsdateinfoList(List<Insdateinfo> insdateinfoList) {
		this.insdateinfoList = insdateinfoList;
	}

	public Insdateinfo getInsdateinfo() {
		return insdateinfo;
	}

	public void setInsdateinfo(Insdateinfo insdateinfo) {
		this.insdateinfo = insdateinfo;
	}

	public String getINSPECTOR() {
		return INSPECTOR;
	}

	public void setINSPECTOR(String iNSPECTOR) {
		INSPECTOR = iNSPECTOR;
	}

	public String getINSDATE() {
		return INSDATE;
	}

	public void setINSDATE(String iNSDATE) {
		INSDATE = iNSDATE;
	}

	public String getINSDATEINFO() {
		return INSDATEINFO;
	}

	public void setINSDATEINFO(String iNSDATEINFO) {
		INSDATEINFO = iNSDATEINFO;
	}

}
