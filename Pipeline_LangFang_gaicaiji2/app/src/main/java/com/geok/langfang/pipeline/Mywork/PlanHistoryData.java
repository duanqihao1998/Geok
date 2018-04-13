package com.geok.langfang.pipeline.Mywork;

import com.geok.langfang.jsonbean.InspectionPlanLineInfo;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class PlanHistoryData implements Serializable {

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

	public List<InspectionPlanLineInfo> getPlanLineInfo() {
		return planLineInfo;
	}

	public void setPlanLineInfo(List<InspectionPlanLineInfo> planLineInfo) {
		this.planLineInfo = planLineInfo;
	}

	private String DEPARTMENT;
	private String PLANNO;
	private String INSTYPE;
	private String INSPECTORTYPE;
	private String INSPECTOR;
	private String INSFREQ;
	private String INSVEHICLE;
	private String INSPROPER;
	private String DETERMINANT;
	private String INSBDATE;
	private String INSEDATE;
	private List<InspectionPlanLineInfo> planLineInfo;

}
