package com.geok.langfang.jsonbean;

public class JobEvaluateBean {
	/**
	 * YEAR:年, MONTH月, ARTIFICIALSCORE: 人工评分, SYSSCORE: 系统得分, OPINIONS: 考评意见,
	 * NAME:姓名
	 */
	String YEAR;
	String MONTH;
	String ARTIFICIALSCORE;
	String SYSSCORE;
	String OPINIONS;
	String NAME;

	public String getYEAR() {
		return YEAR;
	}

	public void setYEAR(String yEAR) {
		YEAR = yEAR;
	}

	public String getMONTH() {
		return MONTH;
	}

	public void setMONTH(String mONTH) {
		MONTH = mONTH;
	}

	public String getARTIFICIALSCORE() {
		return ARTIFICIALSCORE;
	}

	public void setARTIFICIALSCORE(String aRTIFICIALSCORE) {
		ARTIFICIALSCORE = aRTIFICIALSCORE;
	}

	public String getSYSSCORE() {
		return SYSSCORE;
	}

	public void setSYSSCORE(String sYSSCORE) {
		SYSSCORE = sYSSCORE;
	}

	public String getOPINIONS() {
		return OPINIONS;
	}

	public void setOPINIONS(String oPINIONS) {
		OPINIONS = oPINIONS;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

}
