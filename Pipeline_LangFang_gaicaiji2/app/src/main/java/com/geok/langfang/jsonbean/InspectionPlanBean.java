package com.geok.langfang.jsonbean;

import java.util.ArrayList;
import java.util.List;

public class InspectionPlanBean {

	/*
	 * @auther sunshihai
	 * 
	 * @date 2013-2-18
	 * 
	 * @comment 计划执行部门:DEPARTMENT, 巡检计划编号:PLANNO, 巡检类型:INSTYPE,
	 * 巡检人员类型:INSPECTORTYPE, 巡检人员:INSPECTOR, 频次:INSFREQ, 巡检工具:INSVEHICLE,
	 * 计划类型:INSPROPER, 制定依据:DETERMINANT, 巡检开始日期:INSBDATE, 巡检结束日期:INSEDATE,
	 * 线路信息：InspectionPlanLineInfo
	 */
	private String DEPARTMENT;

	public String getDEPARTMENT() {
		return DEPARTMENT;
	}

	public void setDEPARTMENT(String dEPARTMENT) {
		DEPARTMENT = dEPARTMENT;
	}

	public String getPLANNO() {
		return PLANNO;
	}

	public void setPLANNO(String pLANNO) {
		PLANNO = pLANNO;
	}

	public String getINSTYPE() {
		return INSTYPE;
	}

	public void setINSTYPE(String iNSTYPE) {
		INSTYPE = iNSTYPE;
	}

	public String getINSPECTORTYPE() {
		return INSPECTORTYPE;
	}

	public void setINSPECTORTYPE(String iNSPECTORTYPE) {
		INSPECTORTYPE = iNSPECTORTYPE;
	}

	public String getINSPECTOR() {
		return INSPECTOR;
	}

	public void setINSPECTOR(String iNSPECTOR) {
		INSPECTOR = iNSPECTOR;
	}

	public String getINSFREQ() {
		return INSFREQ;
	}

	public void setINSFREQ(String iNSFREQ) {
		INSFREQ = iNSFREQ;
	}

	public String getINSVEHICLE() {
		return INSVEHICLE;
	}

	public void setINSVEHICLE(String iNSVEHICLE) {
		INSVEHICLE = iNSVEHICLE;
	}

	public String getINSPROPER() {
		return INSPROPER;
	}

	public void setINSPROPER(String iNSPROPER) {
		INSPROPER = iNSPROPER;
	}

	public String getDETERMINANT() {
		return DETERMINANT;
	}

	public void setDETERMINANT(String dETERMINANT) {
		DETERMINANT = dETERMINANT;
	}

	private String PLANNO;
	private String INSTYPE;
	private String INSPECTORTYPE;
	private String INSPECTOR;
	private String INSFREQ;
	private String INSVEHICLE;
	private String INSPROPER;
	private String DETERMINANT;
	private String INSBDATE;

	public String getINSBDATE() {
		return INSBDATE;
	}

	public void setINSBDATE(String iNSBDATE) {
		INSBDATE = iNSBDATE;
	}

	public String getINSEDATE() {
		return INSEDATE;
	}

	public void setINSEDATE(String iNSEDATE) {
		INSEDATE = iNSEDATE;
	}

	private String INSEDATE;
	private List<InspectionPlanLineInfo> planLineInfo = new ArrayList<InspectionPlanLineInfo>();

	public List<InspectionPlanLineInfo> getPlanLineInfo() {
		return planLineInfo;
	}

	public void setPlanLineInfo(List<InspectionPlanLineInfo> planLineInfo) {
		this.planLineInfo = planLineInfo;
	}

}
