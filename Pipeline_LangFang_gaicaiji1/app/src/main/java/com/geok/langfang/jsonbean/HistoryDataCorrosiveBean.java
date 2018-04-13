package com.geok.langfang.jsonbean;

import java.io.Serializable;

public class HistoryDataCorrosiveBean implements Serializable {

	/*
	 * ID type guid lon lat occurtime,photopath,
	 * photodes,line,lineid,pile,pileid
	 * ,offset,userId,uploadtime,problemdes,isupload
	 */
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getINSPECTOR() {
		return INSPECTOR;
	}

	public void setINSPECTOR(String userid) {
		this.INSPECTOR = userid;
	}

	public String getLINELOOP() {
		return LINELOOP;
	}

	public void setLINELOOP(String line) {
		this.LINELOOP = line;
	}

	public String getMARKERNAME() {
		return MARKERNAME;
	}

	public void setMARKERNAME(String pile) {
		this.MARKERNAME = pile;
	}

	public String getOFF() {
		return OFF;
	}

	public void setOFF(String offset) {
		this.OFF = offset;
	}

	public String getREPAIRTARGET() {
		return REPAIRTARGET;
	}

	public void setREPAIRTARGET(String repair_obj) {
		this.REPAIRTARGET = repair_obj;
	}

	public String getLEAKHUNTINGDATE() {
		return LEAKHUNTINGDATE;
	}

	public void setLEAKHUNTINGDATE(String check_date) {
		this.LEAKHUNTINGDATE = check_date;
	}

	public String getCLOCKPOSITION() {
		return CLOCKPOSITION;
	}

	public void setCLOCKPOSITION(String clockposition) {
		this.CLOCKPOSITION = clockposition;
	}

	public String getSOIL() {
		return SOIL;
	}

	public void setSOIL(String soil) {
		this.SOIL = soil;
	}

	public String getCOATINGFACE() {
		return COATINGFACE;
	}

	public void setCOATINGFACE(String damage_des) {
		this.COATINGFACE = damage_des;
	}

	public String getCOATINGAREA() {
		return COATINGAREA;
	}

	public void setCOATINGAREA(String area) {
		this.COATINGAREA = area;
	}

	public String getAPPEARENCEDESC() {
		return APPEARENCEDESC;
	}

	public void setAPPEARENCEDESC(String corrosion_des) {
		this.APPEARENCEDESC = corrosion_des;
	}

	public String getPITAREA() {
		return PITAREA;
	}

	public void setPITAREA(String corrosion_area) {
		this.PITAREA = corrosion_area;
	}

	public String getPITAMOUNT() {
		return PITAMOUNT;
	}

	public void setPITAMOUNT(String corrosion_num) {
		this.PITAMOUNT = corrosion_num;
	}

	public String getPITDEPTHMAX() {
		return PITDEPTHMAX;
	}

	public void setPITDEPTHMAX(String pitdepthmax) {
		this.PITDEPTHMAX = pitdepthmax;
	}

	public String getPITDEPTHMIN() {
		return PITDEPTHMIN;
	}

	public void setPITDEPTHMIN(String pitdepthmin) {
		this.PITDEPTHMIN = pitdepthmin;
	}

	public String getCOATINGREPAIR() {
		return COATINGREPAIR;
	}

	public void setCOATINGREPAIR(String repair_situation) {
		this.COATINGREPAIR = repair_situation;
	}

	public String getREPAIRDATE() {
		return REPAIRDATE;
	}

	public void setREPAIRDATE(String repair_date) {
		this.REPAIRDATE = repair_date;
	}

	public String getDAMAGETYPE() {
		return DAMAGETYPE;
	}

	public void setDAMAGETYPE(String damage_type) {
		this.DAMAGETYPE = damage_type;
	}

	public String getREPAIRTYPE() {
		return REPAIRTYPE;
	}

	public void setREPAIRTYPE(String repair_type) {
		this.REPAIRTYPE = repair_type;
	}

	public String getREPAIRINFO() {
		return REPAIRINFO;
	}

	public void setREPAIRINFO(String pile_info) {
		this.REPAIRINFO = pile_info;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String remarks) {
		this.REMARK = remarks;
	}

	public int getIsupload() {
		return isupload;
	}

	public void setIsupload(int isupload) {
		this.isupload = isupload;
	}

	private String guid;
	private String userid;
	private String LINELOOP;
	private String MARKERNAME;
	private String OFF;
	private String REPAIRTARGET;
	private String LEAKHUNTINGDATE;
	private String CLOCKPOSITION;
	private String SOIL;
	private String COATINGFACE;
	private String COATINGAREA;
	private String APPEARENCEDESC;
	private String PITAREA;
	private String PITAMOUNT;
	private String PITDEPTHMAX;
	private String PITDEPTHMIN;
	private String COATINGREPAIR;
	private String REPAIRDATE;
	private String DAMAGETYPE;
	private String REPAIRTYPE;
	private String REPAIRINFO;
	private String REMARK;
	private String INSPECTOR;
	private int isupload;

}
