package com.geok.langfang.jsonbean;

import java.io.Serializable;

public class InspectionPlanLineInfo implements Serializable {

	/*
	 * @auther sunshihai
	 * 
	 * @DATE 2013-2-18
	 * 
	 * @comment 巡检范围:RANGEFLAG, 巡检线路信息:INSPECTLINE
	 */

	private String RANGEFLAG;

	public String getRANGEFLAG() {
		return RANGEFLAG;
	}

	public void setRANGEFLAG(String rANGEFLAG) {
		RANGEFLAG = rANGEFLAG;
	}

	public String getLINELOOP() {
		return LINELOOP;
	}

	public void setLINELOOP(String iLINELOOP) {
		LINELOOP = iLINELOOP;
	}

	private String LINELOOP;
}
