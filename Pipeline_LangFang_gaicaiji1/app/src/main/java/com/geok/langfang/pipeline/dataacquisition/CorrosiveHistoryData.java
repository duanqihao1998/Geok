package com.geok.langfang.pipeline.dataacquisition;

import java.io.Serializable;

public class CorrosiveHistoryData implements Serializable {

	/*
	 * guid userid line pile offset repair_obj check_date clockposition soil
	 * damage_des area corrosion_des corrosion_area corrosion_num pitdepthmax
	 * pitdepthmin repair_situation repair_date damage_type repair_type
	 * pile_info remarks lineid pileid
	 */
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getPile() {
		return pile;
	}

	public void setPile(String pile) {
		this.pile = pile;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getRepair_obj() {
		return repair_obj;
	}

	public void setRepair_obj(String repair_obj) {
		this.repair_obj = repair_obj;
	}

	public String getCheck_date() {
		return check_date;
	}

	public void setCheck_date(String check_date) {
		this.check_date = check_date;
	}

	public String getClockposition() {
		return clockposition;
	}

	public void setClockposition(String clockposition) {
		this.clockposition = clockposition;
	}

	public String getSoil() {
		return soil;
	}

	public void setSoil(String soil) {
		this.soil = soil;
	}

	public String getDamage_des() {
		return damage_des;
	}

	public void setDamage_des(String damage_des) {
		this.damage_des = damage_des;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCorrosion_des() {
		return corrosion_des;
	}

	public void setCorrosion_des(String corrosion_des) {
		this.corrosion_des = corrosion_des;
	}

	public String getCorrosion_area() {
		return corrosion_area;
	}

	public void setCorrosion_area(String corrosion_area) {
		this.corrosion_area = corrosion_area;
	}

	public String getCorrosion_num() {
		return corrosion_num;
	}

	public void setCorrosion_num(String corrosion_num) {
		this.corrosion_num = corrosion_num;
	}

	public String getPitdepthmax() {
		return pitdepthmax;
	}

	public void setPitdepthmax(String pitdepthmax) {
		this.pitdepthmax = pitdepthmax;
	}

	public String getPitdepthmin() {
		return pitdepthmin;
	}

	public void setPitdepthmin(String pitdepthmin) {
		this.pitdepthmin = pitdepthmin;
	}

	public String getRepair_situation() {
		return repair_situation;
	}

	public void setRepair_situation(String repair_situation) {
		this.repair_situation = repair_situation;
	}

	public String getRepair_date() {
		return repair_date;
	}

	public void setRepair_date(String repair_date) {
		this.repair_date = repair_date;
	}

	public String getDamage_type() {
		return damage_type;
	}

	public void setDamage_type(String damage_type) {
		this.damage_type = damage_type;
	}

	public String getRepair_type() {
		return repair_type;
	}

	public void setRepair_type(String repair_type) {
		this.repair_type = repair_type;
	}

	public String getPile_info() {
		return pile_info;
	}

	public void setPile_info(String pile_info) {
		this.pile_info = pile_info;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getLineid() {
		return lineid;
	}

	public void setLineid(String lineid) {
		this.lineid = lineid;
	}

	public String getPileid() {
		return pileid;
	}

	public void setPileid(String pileid) {
		this.pileid = pileid;
	}

	public int getIsupload() {
		return isupload;
	}

	public void setIsupload(int isupload) {
		this.isupload = isupload;
	}

	private String guid;
	private String userid;
	private String line;
	private String pile;
	private String offset;
	private String repair_obj;
	private String check_date;
	private String clockposition;
	private String soil;
	private String damage_des;
	private String area;
	private String corrosion_des;
	private String corrosion_area;
	private String corrosion_num;
	private String pitdepthmax;
	private String pitdepthmin;
	private String repair_situation;
	private String repair_date;
	private String damage_type;
	private String repair_type;
	private String pile_info;
	private String remarks;
	private String lineid;
	private String pileid;
	private int isupload;

}
