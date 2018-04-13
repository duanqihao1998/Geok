package com.geok.langfang.jsonbean;

public class ResistanceQueryBean {
	/**
	 * EVENTID:唯一标识,UNITEVENTID:部门编号,HALF：填报年份(上半年/下半年) ,INSTRUMENTNAME：名称型号
	 * ,INSTRUMENTTYPE：仪器编号 ,REGISTERMAN：录入人 ,RETESTMAN：复核人 ,TEMPERATURE：温度
	 * ,TESTMAN：测试人 ,WEATHER：天气 ,YEAR：年度 ,CONCLUSION：结论 ,CPGROUNDBEDEVENTID：地床编号
	 * ,SETVALUE：规定值 ,TESTDATE：测试日期 ,TESTVALUE：测试值
	 */

	/**
	 * EVENTID:唯一标识,CPGROUNDBEDEVENTID:地床编号,TESTDATE:测试日期,SETVALUE:规定值,TESTVALUE
	 * ;测试值,CONCLUSION:结论,WEATHER:天气
	 * TEMPERATURE;温度,INSTRUMENTNAME:接地电阻测试仪器型号,INSTRUMENTTYPE
	 * :接地电阻测试仪器编号,USERID:测试人员
	 */
	String EVENTID;
	String CPGROUNDBEDEVENTID;
	String TESTDATE;
	String SETVALUE;
	String TESTVALUE;
	String CONCLUSION;
	String WEATHER;
	String TEMPERATURE;
	String INSTRUMENTNAME;
	String INSTRUMENTTYPE;
	String USERID;

	public ResistanceQueryBean() {
	}

	public ResistanceQueryBean(String EVENTID, String CPGROUNDBEDEVENTID, String TESTDATE,
			String SETVALUE, String TESTVALUE, String CONCLUSION, String WEATHER,
			String TEMPERATURE, String INSTRUMENTNAME, String INSTRUMENTTYPE, String USERID) {
		this.EVENTID = EVENTID;
		this.CPGROUNDBEDEVENTID = CPGROUNDBEDEVENTID;
		this.TESTDATE = TESTDATE;
		this.SETVALUE = SETVALUE;
		this.TESTVALUE = TESTVALUE;
		this.CONCLUSION = CONCLUSION;
		this.WEATHER = WEATHER;
		this.TEMPERATURE = TEMPERATURE;
		this.INSTRUMENTNAME = INSTRUMENTNAME;
		this.INSTRUMENTTYPE = INSTRUMENTTYPE;
		this.USERID = USERID;
	}

	public String getEVENTID() {
		return EVENTID;
	}

	public void setEVENTID(String eVENTID) {
		EVENTID = eVENTID;
	}

	public String getCPGROUNDBEDEVENTID() {
		return CPGROUNDBEDEVENTID;
	}

	public void setCPGROUNDBEDEVENTID(String cPGROUNDBEDEVENTID) {
		CPGROUNDBEDEVENTID = cPGROUNDBEDEVENTID;
	}

	public String getTESTDATE() {
		return TESTDATE;
	}

	public void setTESTDATE(String tESTDATE) {
		TESTDATE = tESTDATE;
	}

	public String getSETVALUE() {
		return SETVALUE;
	}

	public void setSETVALUE(String sETVALUE) {
		SETVALUE = sETVALUE;
	}

	public String getTESTVALUE() {
		return TESTVALUE;
	}

	public void setTESTVALUE(String tESTVALUE) {
		TESTVALUE = tESTVALUE;
	}

	public String getCONCLUSION() {
		return CONCLUSION;
	}

	public void setCONCLUSION(String cONCLUSION) {
		CONCLUSION = cONCLUSION;
	}

	public String getWEATHER() {
		return WEATHER;
	}

	public void setWEATHER(String wEATHER) {
		WEATHER = wEATHER;
	}

	public String getTEMPERATURE() {
		return TEMPERATURE;
	}

	public void setTEMPERATURE(String tEMPERATURE) {
		TEMPERATURE = tEMPERATURE;
	}

	public String getINSTRUMENTNAME() {
		return INSTRUMENTNAME;
	}

	public void setINSTRUMENTNAME(String iNSTRUMENTNAME) {
		INSTRUMENTNAME = iNSTRUMENTNAME;
	}

	public String getINSTRUMENTTYPE() {
		return INSTRUMENTTYPE;
	}

	public void setINSTRUMENTTYPE(String iNSTRUMENTTYPE) {
		INSTRUMENTTYPE = iNSTRUMENTTYPE;
	}

	public String getUSERID() {
		return USERID;
	}

	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}

}
