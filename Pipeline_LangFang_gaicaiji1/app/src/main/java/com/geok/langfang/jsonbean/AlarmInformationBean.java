package com.geok.langfang.jsonbean;

public class AlarmInformationBean {

	String ALARMTYPE;
	String ALARMTIME;
	String ALARMLOCATION;
	String MAXOFFSET;
	String REALOFFSET;
	String MAXSPEED;
	String REALSPEED;
	String DROPPEDINTERVAL;
	boolean isNew = false;

	public AlarmInformationBean() {
	}

	/**
	 * ALARMTYPE:报警类型,ALARMTIME:报警时间,ALARMLOCATION:报警位置,MAXOFFSET:设定最大偏移,
	 * REALOFFSET:实际偏移 ,MAXSPEED:设定最大速度,REALSPEED:实际速度,DROPPEDINTERVAL:掉线时间间隔,
	 * isNew是否是新
	 * 
	 * @param ALARMTYPE
	 * @param ALARMTIME
	 * @param ALARMLOCATION
	 * @param MAXOFFSET
	 * @param REALOFFSET
	 * @param MAXSPEED
	 * @param REALSPEED
	 * @param DROPPEDINTERVAL
	 */
	public AlarmInformationBean(String ALARMTYPE, String ALARMTIME, String ALARMLOCATION,
			String MAXOFFSET, String REALOFFSET, String MAXSPEED, String REALSPEED,
			String DROPPEDINTERVAL, boolean isNew) {
		this.ALARMTYPE = ALARMTYPE;
		this.ALARMTIME = ALARMTIME;
		this.ALARMLOCATION = ALARMLOCATION;
		this.MAXOFFSET = MAXOFFSET;
		this.REALOFFSET = REALOFFSET;
		this.MAXSPEED = MAXSPEED;
		this.REALSPEED = REALSPEED;
		this.DROPPEDINTERVAL = DROPPEDINTERVAL;
		this.isNew = isNew;
	}

	public String getALARMTYPE() {
		return ALARMTYPE;
	}

	public void setALARMTYPE(String aLARMTYPE) {
		ALARMTYPE = aLARMTYPE;
	}

	public String getALARMTIME() {
		return ALARMTIME;
	}

	public void setALARMTIME(String aLARMTIME) {
		ALARMTIME = aLARMTIME;
	}

	public String getALARMLOCATION() {
		return ALARMLOCATION;
	}

	public void setALARMLOCATION(String aLARMLOCATION) {
		ALARMLOCATION = aLARMLOCATION;
	}

	public String getMAXOFFSET() {
		return MAXOFFSET;
	}

	public void setMAXOFFSET(String mAXOFFSET) {
		MAXOFFSET = mAXOFFSET;
	}

	public String getREALOFFSET() {
		return REALOFFSET;
	}

	public void setREALOFFSET(String rEALOFFSET) {
		REALOFFSET = rEALOFFSET;
	}

	public String getMAXSPEED() {
		return MAXSPEED;
	}

	public void setMAXSPEED(String mAXSPEED) {
		MAXSPEED = mAXSPEED;
	}

	public String getREALSPEED() {
		return REALSPEED;
	}

	public void setREALSPEED(String rEALSPEED) {
		REALSPEED = rEALSPEED;
	}

	public String getDROPPEDINTERVAL() {
		return DROPPEDINTERVAL;
	}

	public void setDROPPEDINTERVAL(String dROPPEDINTERVAL) {
		DROPPEDINTERVAL = dROPPEDINTERVAL;
	}

	public boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

}
