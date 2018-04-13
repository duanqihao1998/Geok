package com.geok.langfang.jsonbean;

import java.util.ArrayList;
import java.util.List;

public class QueryPlanBean {
	/**
	 * INSPECTORID：巡检人员eventid，EVENTID:巡检计划EVENTID，INSPROPER：巡检性质，INSTYPE，巡检类型，
	 * INSPECTLINE：巡检管线信息， DETERMINANT：制作依据，INSPECTOR：巡检人员，INSVEHICLE：巡检工具，
	 * INSFREQ：巡检频次，PLANNO：巡检任务编号
	 */

	String INSPECTORID;
	String EVENTID;
	String INSPROPER;
	String INSTYPE;
	String INSPECTLINE;

	String DETERMINANT;
	String INSPECTOR;
	String INSVEHICLE;
	String INSFREQ;
	String PLANNO;
	List<Inspectline> lineList = new ArrayList<Inspectline>();

	public List<Inspectline> getLineList() {
		return lineList;
	}

	public void setLineList(List<Inspectline> lineList) {
		this.lineList = lineList;
	}

	public String getINSPECTORID() {
		return INSPECTORID;
	}

	public void setINSPECTORID(String iNSPECTORID) {
		INSPECTORID = iNSPECTORID;
	}

	public String getEVENTID() {
		return EVENTID;
	}

	public void setEVENTID(String eVENTID) {
		EVENTID = eVENTID;
	}

	public String getINSPROPER() {
		return INSPROPER;
	}

	public void setINSPROPER(String iNSPROPER) {
		INSPROPER = iNSPROPER;
	}

	public String getINSTYPE() {
		return INSTYPE;
	}

	public void setINSTYPE(String iNSTYPE) {
		INSTYPE = iNSTYPE;
	}

	public String getINSPECTLINE() {
		return INSPECTLINE;
	}

	public void setINSPECTLINE(String iNSPECTLINE) {
		INSPECTLINE = iNSPECTLINE;
	}

	public String getDETERMINANT() {
		return DETERMINANT;
	}

	public void setDETERMINANT(String dETERMINANT) {
		DETERMINANT = dETERMINANT;
	}

	public String getINSPECTOR() {
		return INSPECTOR;
	}

	public void setINSPECTOR(String iNSPECTOR) {
		INSPECTOR = iNSPECTOR;
	}

	public String getINSVEHICLE() {
		return INSVEHICLE;
	}

	public void setINSVEHICLE(String iNSVEHICLE) {
		INSVEHICLE = iNSVEHICLE;
	}

	public String getINSFREQ() {
		return INSFREQ;
	}

	public void setINSFREQ(String iNSFREQ) {
		INSFREQ = iNSFREQ;
	}

	public String getPLANNO() {
		return PLANNO;
	}

	public void setPLANNO(String pLANNO) {
		PLANNO = pLANNO;
	}

}
