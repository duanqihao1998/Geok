package com.geok.langfang.pipeline.alarm;

import java.io.Serializable;

/**
 * 
 * @author sunshihai 用来保存报警信息的数据bean
 * 
 */

public class AlarmData implements Serializable {

	private String alarmtype;
	private String alarmtime;
	private String alarmlocation;
	private String maxspeed;
	private String maxoffset;
	private String realspeed;
	private String realoffset;
	private int flag;

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getAlarmtype() {
		return alarmtype;
	}

	public void setAlarmtype(String alarmtype) {
		this.alarmtype = alarmtype;
	}

	public String getAlarmtime() {
		return alarmtime;
	}

	public void setAlarmtime(String alarmtime) {
		this.alarmtime = alarmtime;
	}

	public String getAlarmlocation() {
		return alarmlocation;
	}

	public void setAlarmlocation(String alarmlocation) {
		this.alarmlocation = alarmlocation;
	}

	public String getMaxspeed() {
		return maxspeed;
	}

	public void setMaxspeed(String maxspeed) {
		this.maxspeed = maxspeed;
	}

	public String getMaxoffset() {
		return maxoffset;
	}

	public void setMaxoffset(String maxoffset) {
		this.maxoffset = maxoffset;
	}

	public String getRealspeed() {
		return realspeed;
	}

	public void setRealspeed(String realspeed) {
		this.realspeed = realspeed;
	}

	public String getRealoffset() {
		return realoffset;
	}

	public void setRealoffset(String realoffset) {
		this.realoffset = realoffset;
	}

	public String getDroptime() {
		return droptime;
	}

	public void setDroptime(String droptime) {
		this.droptime = droptime;
	}

	private String droptime;

}
