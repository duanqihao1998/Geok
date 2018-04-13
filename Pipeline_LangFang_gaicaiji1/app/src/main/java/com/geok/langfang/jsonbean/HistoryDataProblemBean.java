package com.geok.langfang.jsonbean;

import java.io.Serializable;
import java.util.List;

public class HistoryDataProblemBean implements Serializable {

	/*
	 * INSPECTOR:巡检人员 LINELOOP:管线 MARKERNAME:桩 OFF:偏移量 TYPE:问题类型
	 * OCCURRENCETIME:问题发生时间 DESCRIPTION:问题描述 REPORTDATE:上报时间 LON:经度 LAT:纬度
	 * DEPARTMENTID:部门 DEALPLAN:问题解决方案 DEALDESC:问题解决情况 ADDRESS:问题发生地点
	 * PICTURE:图片信息 CONTENTINFO:图片数据 PICTUREDESCRIPTION:图片描述
	 */
	private String INSPECTOR;
	private String LINELOOP;
	private String MARKERNAME;
	private String OFF;
	private String TYPE;
	private String OCCURRENCETIME;

	public String getINSPECTOR() {
		return INSPECTOR;
	}

	public void setINSPECTOR(String iNSPECTOR) {
		INSPECTOR = iNSPECTOR;
	}

	public String getLINELOOP() {
		return LINELOOP;
	}

	public void setLINELOOP(String lINELOOP) {
		LINELOOP = lINELOOP;
	}

	public String getMARKERNAME() {
		return MARKERNAME;
	}

	public void setMARKERNAME(String mARKERNAME) {
		MARKERNAME = mARKERNAME;
	}

	public String getOFF() {
		return OFF;
	}

	public void setOFF(String oFF) {
		OFF = oFF;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public String getOCCURRENCETIME() {
		return OCCURRENCETIME;
	}

	public void setOCCURRENCETIME(String oCCURRENCETIME) {
		OCCURRENCETIME = oCCURRENCETIME;
	}

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}

	public String getREPORTDATE() {
		return REPORTDATE;
	}

	public void setREPORTDATE(String rEPORTDATE) {
		REPORTDATE = rEPORTDATE;
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

	public String getDEPARTMENTID() {
		return DEPARTMENTID;
	}

	public void setDEPARTMENTID(String dEPARTMENTID) {
		DEPARTMENTID = dEPARTMENTID;
	}

	public String getDEALPLAN() {
		return DEALPLAN;
	}

	public void setDEALPLAN(String dEALPLAN) {
		DEALPLAN = dEALPLAN;
	}

	public String getDEALDESC() {
		return DEALDESC;
	}

	public void setDEALDESC(String dEALDESC) {
		DEALDESC = dEALDESC;
	}

	public String getADDRESS() {
		return ADDRESS;
	}

	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}

	public String getPICTURE() {
		return PICTURE;
	}

	public void setPICTURE(String pICTURE) {
		PICTURE = pICTURE;
	}

	public List<PictureBean> getPicturebean() {
		return picturebean;
	}

	public void setPicturebean(List<PictureBean> picturebean) {
		this.picturebean = picturebean;
	}

	private String DESCRIPTION;
	private String REPORTDATE;
	private String LON;
	private String LAT;
	private String DEPARTMENTID;
	private String DEALPLAN;
	private String DEALDESC;
	private String ADDRESS;
	private String PICTURE;
	private List<PictureBean> picturebean;

}
