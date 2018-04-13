package com.geok.langfang.jsonbean;

public class InspectPlanBean {
	/*
	 * INSPECTOR:巡检人员,INSTIME:巡检时间,INSDATE:巡检日期
	 */
	String INSPECTOR;
	String INSTIME;
	String INSDATE;

	public InspectPlanBean() {
	}

	public InspectPlanBean(String INSPECTOR, String INSTIME, String INSDATE) {
		this.INSPECTOR = INSPECTOR;
		this.INSTIME = INSTIME;
		this.INSDATE = INSDATE;
	}

	public String getINSPECTOR() {
		return INSPECTOR;
	}

	public void setINSPECTOR(String iNSPECTOR) {
		INSPECTOR = iNSPECTOR;
	}

	public String getINSTIME() {
		return INSTIME;
	}

	public void setINSTIME(String iNSTIME) {
		INSTIME = iNSTIME;
	}

	public String getINSDATE() {
		return INSDATE;
	}

	public void setINSDATE(String iNSDATE) {
		INSDATE = iNSDATE;
	}

}
