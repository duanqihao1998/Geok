package com.geok.langfang.request;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.geok.langfang.jsonbean.Freqinfo;

public class MyApplication {

	public String userid = "";
	public String depterid = "";
	public int AlarmUpgrate = 0;
	public String imei = "";
	public static String ip = "11";
	public static String port = "11";
	public String inspecrionEVENTID = "";
	public String info = "";
	public static String inspectionType = "self";
	public static List<Freqinfo> pileList = new ArrayList<Freqinfo>();// 上报的关键点信息
	public Double m = 100.0;
	public static int checkIp = 0;// 判断是否进入过设置ip界面
	public static String person = "";

	public MyApplication(Context c) {
		SharedPreferences spf = c.getSharedPreferences("save_password", c.MODE_PRIVATE);// 本地文件
		Editor editor = spf.edit();// 修改本地文件
		userid = spf.getString("userId", "");
		depterid = spf.getString("depterid", "");
		imei = spf.getString("imei", "");
		inspectionType = spf.getString("inspectionType", "");
		m = Double.valueOf(spf.getString("m", "100.0"));
		inspecrionEVENTID = spf.getString("inspecrionEVENTID", "");
		info = spf.getString("info", "");
		ip = spf.getString("ip", "");
		port = spf.getString("port", "8080");
	}

}
