package com.geok.langfang.jsonbean;

public class InspectionRecordQueryBean {

	String INSPECTOR;
	String INSTYPE;
	String INSDATE;

	public InspectionRecordQueryBean() {
	}

	/**
	 * INSPECTOR:巡检人员,INSTYPE:巡检类型,INSDATE:巡检日期
	 */
	public InspectionRecordQueryBean(String INSPECTOR, String INSTYPE, String INSDATE) {
		this.INSPECTOR = INSPECTOR;
		this.INSTYPE = INSTYPE;
		this.INSDATE = INSDATE;
	}

	public String getINSPECTOR() {
		return INSPECTOR;
	}

	public void setINSPECTOR(String iNSPECTOR) {
		INSPECTOR = iNSPECTOR;
	}

	public String getINSTYPE() {
		return INSTYPE;
	}

	public void setINSTYPE(String iNSTYPE) {
		INSTYPE = iNSTYPE;
	}

	public String getINSDATE() {
		return INSDATE;
	}

	public void setINSDATE(String iNSDATE) {
		INSDATE = iNSDATE;
	}

}
