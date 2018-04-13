package com.geok.langfang.jsonbean;

import java.util.ArrayList;
import java.util.List;

public class Insdateinfo {

	/**
	 * NAME:任务名称, FREQTEXT:巡检频次, TASKLOCATION:巡检线路, TASKINFO: 需要巡检的详细信息,
	 * POINTNAME:桩或关键点的名称, TASKID:任务EVENTID, POINTID:桩或关键点的EVENTID, EVENTID:
	 * 巡检点EVENTID，TASKINFO：任务信息， INSPECTOR：巡检人员，
	 * 
	 * @author Administrator
	 * 
	 */
	String INSPECTOR;
	String NAME;
	String FREQTEXT;
	String TASKLOCATION;
	String TASKINFO;
	String TASKID;
	String INSTACTTIME;

	List<Taskinfo> taskInfoList = new ArrayList<Taskinfo>();
	Taskinfo taskInfo = new Taskinfo();

	public List<Taskinfo> getTaskInfoList() {
		return taskInfoList;
	}

	public void setTaskInfoList(List<Taskinfo> taskInfoList) {
		this.taskInfoList = taskInfoList;
	}

	public Taskinfo getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(Taskinfo taskInfo) {
		this.taskInfo = taskInfo;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getFREQTEXT() {
		return FREQTEXT;
	}

	public void setFREQTEXT(String fREQTEXT) {
		FREQTEXT = fREQTEXT;
	}

	public String getTASKLOCATION() {
		return TASKLOCATION;
	}

	public void setTASKLOCATION(String tASKLOCATION) {
		TASKLOCATION = tASKLOCATION;
	}

	public String getTASKINFO() {
		return TASKINFO;
	}

	public void setTASKINFO(String tASKINFO) {
		TASKINFO = tASKINFO;
	}

	public String getTASKID() {
		return TASKID;
	}

	public void setTASKID(String tASKID) {
		TASKID = tASKID;
	}

	public String getINSPECTOR() {
		return INSPECTOR;
	}

	public void setINSPECTOR(String iNSPECTOR) {
		INSPECTOR = iNSPECTOR;
	}

	public String getINSTACTTIME() {
		return INSTACTTIME;
	}

	public void setINSTACTTIME(String iNSTACTTIME) {
		INSTACTTIME = iNSTACTTIME;
	}

}
