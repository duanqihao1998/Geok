package com.geok.langfang.json;

import android.util.Log;

import com.geok.langfang.jsonbean.AlarmInformationBean;
import com.geok.langfang.jsonbean.AntisepsisQueryBean;
import com.geok.langfang.jsonbean.CpgroundbedBean;
import com.geok.langfang.jsonbean.CppotentialQueryBean;
import com.geok.langfang.jsonbean.DomainBean;
import com.geok.langfang.jsonbean.DomainBeanChild;
import com.geok.langfang.jsonbean.Freqinfo;
import com.geok.langfang.jsonbean.GpsHui;
import com.geok.langfang.jsonbean.GpsLine;
import com.geok.langfang.jsonbean.GpsLocation;
import com.geok.langfang.jsonbean.GpsMarker;
import com.geok.langfang.jsonbean.GpsUser;
import com.geok.langfang.jsonbean.HistoryDataCorrosiveBean;
import com.geok.langfang.jsonbean.HistoryDataGroundBean;
import com.geok.langfang.jsonbean.HistoryDataNaturalBean;
import com.geok.langfang.jsonbean.HistoryDataProtectBean;
import com.geok.langfang.jsonbean.HistoryProblemImageLoadBean;
import com.geok.langfang.jsonbean.InformationQueryBean;
import com.geok.langfang.jsonbean.Insdateinfo;
import com.geok.langfang.jsonbean.InspectPlanBean;
import com.geok.langfang.jsonbean.InspectionHistoryBean;
import com.geok.langfang.jsonbean.InspectionHistoryDetile1;
import com.geok.langfang.jsonbean.InspectionHistoryDetile2;
import com.geok.langfang.jsonbean.InspectionInforQueryBean;
import com.geok.langfang.jsonbean.InspectionInforQueryBean1;
import com.geok.langfang.jsonbean.InspectionPlanBean;
import com.geok.langfang.jsonbean.InspectionPlanLineInfo;
import com.geok.langfang.jsonbean.InspectionPlanQueryBean;
import com.geok.langfang.jsonbean.InspectionRecordQueryBean;
import com.geok.langfang.jsonbean.InspectionTaskInfoBean;
import com.geok.langfang.jsonbean.Inspectline;
import com.geok.langfang.jsonbean.JobEvaluateBean;
import com.geok.langfang.jsonbean.LineSyncBean;
import com.geok.langfang.jsonbean.MyTaskBean;
import com.geok.langfang.jsonbean.MyTaskQueryBean;
import com.geok.langfang.jsonbean.NatureVoltageQueryBean;
import com.geok.langfang.jsonbean.NotificationInformationBean;
import com.geok.langfang.jsonbean.PileSyncBean;
import com.geok.langfang.jsonbean.PileSyncBeanChild;
import com.geok.langfang.jsonbean.QueryPlanBean;
import com.geok.langfang.jsonbean.ResistanceQueryBean;
import com.geok.langfang.jsonbean.SubSystemBean;
import com.geok.langfang.jsonbean.Taskinfo;
import com.geok.langfang.jsonbean.UnitBean;
import com.geok.langfang.jsonbean.UnqualifiedPersonBean;
import com.geok.langfang.jsonbean.UserBean;
import com.geok.langfang.jsonbean.VersionBean;
import com.geok.langfang.pipeline.Login;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Json {
	public static List<GpsHui> gpsHuis=new ArrayList<>();//轨迹回放点集合
	/**
	 * 域字典同步解析
	 * 
	 * @param data
	 * @return List<List<JsonBean>>
	 *         DOMAINNAME:域值类型名,DOMAINVAL:域值,CODENAME:域值名称,CODEVAL:域值代码
	 */
	public static List<DomainBean> getDomainList(String data) {
		List<DomainBean> beans = new ArrayList<DomainBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);// new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				DomainBean bean = new DomainBean();
				bean.setDOMAINNAME(getStr(jb.getString("DOMAINNAME")));
				beans.add(bean);
				List<DomainBeanChild> listchild = new ArrayList<DomainBeanChild>();
				JSONArray array = jb.getJSONArray("DOMAINVAL");

				for (int j = 0; j < array.length(); j++) {
					JSONObject object = array.getJSONObject(j);
					DomainBeanChild jb1 = new DomainBeanChild();
					jb1.setCODEVALUE(getStr(object.getString("CODEVALUE")));
					jb1.setCODENAME(getStr(object.getString("CODENAME")));
					listchild.add(jb1);
				}
				bean.setChidList(listchild);
				beans.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return beans;
	}

	/**
	 * 解析问题上报历史信息集合
	 * 
	 * @param data
	 * @return
	 */
	public static List<HistoryProblemImageLoadBean> HistoryDataProblem(String data) {
		List<HistoryProblemImageLoadBean> beans = new ArrayList<HistoryProblemImageLoadBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);// new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jb = jsonArray.getJSONObject(i);
				HistoryProblemImageLoadBean bean = new HistoryProblemImageLoadBean();
				/*
				 * INSPECTOR:巡检人员 LINELOOP:管线 MARKERNAME:桩 OFF:偏移量 TYPE:问题类型
				 * OCCURRENCETIME:问题发生时间 DESCRIPTION:问题描述 REPORTDATE:上报时间 LON:经度
				 * LAT:纬度 DEPARTMENTID:部门 DEALPLAN:问题解决方案 DEALDESC:问题解决情况
				 * ADDRESS:问题发生地点 PICTURE:图片信息 CONTENTINFO:图片数据
				 * PICTUREDESCRIPTION:图片描述
				 */
				bean.setINSPECTOR(getStr(jb.getString("INSPECTOR").equals("null") ? "" : jb
						.getString("INSPECTOR")));
				bean.setLINELOOP(getStr(jb.getString("LINELOOP").equals("null") ? "" : jb
						.getString("LINELOOP")));
				bean.setMARKERNAME(getStr(jb.getString("MARKERNAME").equals("null") ? "" : jb
						.getString("MARKERNAME")));
				bean.setOFF(getStr(jb.getString("OFF").equals("null") ? "" : getStr(jb.getString("OFF"))));
				bean.setTYPE(getStr(jb.getString("TYPE").equals("null") ? "" : getStr(jb.getString("TYPE"))));
				bean.setOCCURRENCETIME(getStr(jb.getString("OCCURRENCETIME").equals("null") ? "" : jb
						.getString("OCCURRENCETIME")));
				bean.setDESCRIPTION(getStr(jb.getString("DESCRIPTION").equals("null") ? "" : jb
						.getString("DESCRIPTION")));
				bean.setREPORTDATE(getStr(jb.getString("REPORTDATE").equals("null") ? "" : jb
						.getString("REPORTDATE")));
				bean.setLON(getStr(jb.getString("LON").equals("null") ? "" : getStr(jb.getString("LON"))));
				bean.setLAT(getStr(jb.getString("LAT").equals("null") ? "" : getStr(jb.getString("LAT"))));
				bean.setDEPARTMENTID(getStr(jb.getString("DEPARTMENTID").equals("null") ? "" : jb
						.getString("DEPARTMENTID")));
				bean.setDEALPLAN(getStr(jb.getString("DEALPLAN").equals("null") ? "" : jb
						.getString("DEALPLAN")));
				bean.setDEALDESC(getStr(jb.getString("DEALDESC")));
				bean.setADDRESS(getStr(jb.getString("ADDRESS").equals("null") ? "" : jb
						.getString("ADDRESS")));
				String path = getStr(jb.optString("PATH","null"));
				if(path.equals("null")){
					bean.setPATH("");
				}else{
					bean.setPATH(path);
				}
//				bean.setPATH(getStr(jb.getString("PATH").equals("null") ? "" : getStr(jb.getString("PATH"));
				//
				// List<PictureBean> listchild=new ArrayList<PictureBean>();
				// JSONArray array = getStr(jb.getJSONArray("PICTURE");
				//
				// for(int j=0;j<array.length();j++){
				// JSONObject object = array.getJSONObject(j);
				// PictureBean jb1 = new PictureBean();
				// jb1.setCONTENTINFO(object.getString("CONTENTINFO").equals("null")?"":getStr(jb.getString("CONTENTINFO"));
				// jb1.setPICTUREDESCRIPTION(object.getString("PICTUREDESCRIPTION").equals("null")?"":getStr(jb.getString("PICTUREDESCRIPTION"));
				// listchild.add(jb1);
				// }
				// bean.setPicturebean(listchild);
				beans.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return beans;
	}

	/**
	 * 解析巡检集合历史信息: 巡检计划 计划执行部门:DEPARTMENT, 巡检计划编号:PLANNO, 巡检类型:INSTYPE,
	 * 巡检人员类型:INSPECTORTYPE, 巡检人员:INSPECTOR, 频次:INSFREQ, 巡检工具:INSVEHICLE,
	 * 计划类型:INSPROPER, 制定依据:DETERMINANT, 巡检日期:INSPECTDATE, 巡检管线:LINELOOP,
	 * 线路信息：InspectionPlanLineInfo
	 */
	public static List<InspectionHistoryBean> getInspectionHistory(String data) {
		List<InspectionHistoryBean> info = new ArrayList<InspectionHistoryBean>();
		try {
			JSONArray jarray = new JSONArray(data);
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject jb = jarray.getJSONObject(i);
				InspectionHistoryBean bean = new InspectionHistoryBean();
				bean.setBEGINDATETIME(getStr(jb.getString("BEGINDATETIME")));
				bean.setENDDATETIME(getStr(jb.getString("ENDDATETIME")));
				bean.setEVENTID(getStr(jb.getString("EVENTID")));
				bean.setINSPECTOR(getStr(jb.getString("INSPECTOR")));
				bean.setTYPE(getStr(jb.getString("TYPE")));
				info.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return info;
	}

	/**
	 * 解析巡检计划集合
	 * 
	 * @param data
	 * @return
	 */
	public static InspectionPlanBean getInspectionPlan(String data) {
		InspectionPlanBean info = new InspectionPlanBean();
		try {
			JSONArray jarray = new JSONArray(data);
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject jb = jarray.getJSONObject(0);
				info.setDEPARTMENT(getStr(jb.getString("DEPARTMENT")));
				info.setPLANNO(getStr(jb.getString("PLANNO")));
				info.setINSTYPE(getStr(jb.getString("INSTYPE")));
				info.setINSPECTORTYPE(getStr(jb.getString("INSPECTORTYPE")));
				info.setINSPECTOR(getStr(jb.getString("INSPECTOR")));
				info.setINSFREQ(getStr(jb.getString("INSFREQ")));
				info.setINSVEHICLE(getStr(jb.getString("INSVEHICLE")));
				info.setINSPROPER(getStr(jb.getString("INSPROPER")));
				info.setINSBDATE(getStr(jb.getString("INSBDATE")));
				info.setINSEDATE(getStr(jb.getString("INSEDATE")));
				info.setDETERMINANT(getStr(jb.getString("DETERMINANT")));

				JSONArray array = jb.getJSONArray("INSPECTLINE");
				List<InspectionPlanLineInfo> list = new ArrayList<InspectionPlanLineInfo>();
				for (int j = 0; j < array.length(); j++) {
					InspectionPlanLineInfo linfo = new InspectionPlanLineInfo();
					JSONObject job = array.getJSONObject(j);
					linfo.setLINELOOP(getStr(job.getString("LINELOOP")));
					linfo.setRANGEFLAG(getStr(job.getString("RANGEFLAG")));
					list.add(linfo);
				}
				info.setPlanLineInfo(list);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return info;
	}

	/**
	 * 解析保护电位集合
	 * 
	 * @param data
	 * @return
	 */
	public static List<HistoryDataProtectBean> HistoryDataProtect(String data) {
		List<HistoryDataProtectBean> beans = new ArrayList<HistoryDataProtectBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);// new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jb = jsonArray.getJSONObject(i);
				HistoryDataProtectBean bean = new HistoryDataProtectBean();
				/*
				 * YEAR,年份 ACINTERFERENCEVOLTAGE 交流干扰电压(V) LINELOOP,管线
				 * MARKERNAME 桩 REMARK 备注 SOILRESISTIVITY,土壤电阻率(Ωom) TEMPERATURE
				 * 温度 TESTDATE 测试日期 INSPECTOR 巡检人员 VOLTAGE 电位值 MONTH 月份
				 */
				bean.setYEAR(getStr(jb.getString("YEAR")));
				bean.setACINTERFERENCEVOLTAGE(getStr(jb.getString("ACINTERFERENCEVOLTAGE")));
				bean.setLINELOOP(getStr(jb.getString("LINELOOP")));
				bean.setMARKERNAME(getStr(jb.getString("MARKERNAME")));
				bean.setREMARK(getStr(jb.getString("REMARK")));
				bean.setSOILRESISTIVITY(getStr(jb.getString("SOILRESISTIVITY")));
				bean.setTEMPERATURE(getStr(jb.getString("TEMPERATURE")));
				bean.setTESTDATE(getStr(jb.getString("TESTDATE")));
				bean.setINSPECTOR(getStr(jb.getString("INSPECTOR")));
				bean.setVOLTAGE(getStr(jb.getString("VOLTAGE")));
				bean.setMONTH(getStr(jb.getString("MONTH")));
				bean.setNATURAL(getStr(jb.getString("NATUREVOLTAGE")));
				bean.setIR(getStr(jb.getString("IR")));

				beans.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return beans;
	}

	/**
	 * 解析自然电位信息集合
	 * 
	 * @param data
	 * @return
	 */
	public static List<HistoryDataNaturalBean> HistoryDataNatural(String data) {
		List<HistoryDataNaturalBean> beans = new ArrayList<HistoryDataNaturalBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);// new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jb = jsonArray.getJSONObject(i);
				HistoryDataNaturalBean bean = new HistoryDataNaturalBean();
				/*
				 * YEAR 年份 ACINTERFERENCEVOLTAGE,交流干扰电压(V) WEATHER,天气
				 * LINELOOP,管线 MARKERNAME,桩 REMARK,备注 TEMPERATURE,温度
				 * TESTDATE,测试日期 INSPECTOR,巡检人员 VOLTAGE 电位值
				 */
				bean.setYEAR(getStr(jb.getString("YEAR")));
				bean.setMONTH(getStr(jb.getString("MONTH")));
				bean.setACINTERFERENCEVOLTAGE(getStr(jb.getString("ACINTERFERENCEVOLTAGE")));
				bean.setWEATHER(getStr(jb.getString("WEATHER")));
				bean.setLINELOOP(getStr(jb.getString("LINELOOP")));
				bean.setMARKERNAME(getStr(jb.getString("MARKERNAME")));
				bean.setREMARK(getStr(jb.getString("REMARK")));
				bean.setTEMPERATURE(getStr(jb.getString("TEMPERATURE")));
				bean.setTESTDATE(getStr(jb.getString("TESTDATE")));
				bean.setINSPECTOR(getStr(jb.getString("INSPECTOR")));
				bean.setVOLTAGE(getStr(jb.getString("VOLTAGE")));

				beans.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return beans;
	}

	/**
	 * 解析接地电阻历史信息集合
	 * 
	 * @param data
	 * @return
	 */
	public static List<HistoryDataGroundBean> HistoryDataGround(String data) {
		List<HistoryDataGroundBean> beans = new ArrayList<HistoryDataGroundBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);// new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jb = jsonArray.getJSONObject(i);
				HistoryDataGroundBean bean = new HistoryDataGroundBean();
				/*
				 * CPGROUNDBEDEVENTID,地床编号 TESTDATE,测试日期 SETVALUE,规定值
				 * TESTVALUE,测试值 CONCLUSION,结论 WEATHER,天气 TEMPERATURE,温度
				 * INSPECTOR 巡检人员
				 */
				bean.setCPGROUNDBEDEVENTID(getStr(jb.getString("CPGROUNDBEDEVENTID")));
				bean.setTESTDATE(getStr(jb.getString("TESTDATE")));
				bean.setSETVALUE(getStr(jb.getString("SETVALUE")));
				bean.setTESTVALUE(getStr(jb.getString("TESTVALUE")));
				bean.setCONCLUSION(getStr(jb.getString("CONCLUSION")));
				bean.setYEAR(getStr(jb.getString("YEAR")));
				String halfyear = getStr(jb.getString("HALFYEAR"));
				if(halfyear!=""){
					if(halfyear == "01"){
						bean.setHALFYEAR("上半年");
					}else{
						bean.setHALFYEAR("上半年");
					}
				}
				bean.setHALFYEAR(halfyear);
				bean.setINSPECTOR(getStr(jb.getString("INSPECTOR")));

				beans.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return beans;
	}

	/**
	 * 防腐层检测历史信息集合
	 * 
	 * @param data
	 * @return
	 */
	public static List<HistoryDataCorrosiveBean> HistoryDataCorrosive(String data) {
		List<HistoryDataCorrosiveBean> beans = new ArrayList<HistoryDataCorrosiveBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);// new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jb = jsonArray.getJSONObject(i);
				HistoryDataCorrosiveBean bean = new HistoryDataCorrosiveBean();
				/*
				 * REPAIRTARGET 修复对象 LEAKHUNTINGDATE 检漏日期 CLOCKPOSITION
				 * 检漏位置--检漏环周位置 SOIL 漏点处土壤环境描述 COATINGFACE 防腐层破损情况--外观描述
				 * COATINGAREA 防腐层破损情况--破损面积 APPEARENCEDESC 管道金属腐蚀情况--表面描述
				 * PITAREA 面积(管道金属腐蚀情况--腐蚀坑形状和腐蚀坑深度)mm^2 PITAMOUNT
				 * 个数(管道金属腐蚀情况--腐蚀坑形状和腐蚀坑深度) PITDEPTHMAX
				 * 最大(管道金属腐蚀情况--腐蚀坑形状和腐蚀坑深度)mm PITDEPTHMIN
				 * 最小(管道金属腐蚀情况--腐蚀坑形状和腐蚀坑深度) mm COATINGREPAIR 腐蚀层补修处理情况
				 * REPAIRDATE 补漏漏日期 DAMAGETYPE 破损类型(01 机械损伤 02人为损伤 03 薄弱 04 老化
				 * 05 其他) REPAIRTYPE 补漏类型(01 防腐蚀修补 02 管道补焊加强) REPAIRINFO 管体修复信息
				 * REMARK 备注 INSPECTOR 巡检人员 LINELOOP 管线 MARKERNAME 桩 OFF 偏移量
				 */
				bean.setREPAIRTARGET(getStr(jb.getString("REPAIRTARGET")));
				bean.setLEAKHUNTINGDATE(getStr(jb.getString("LEAKHUNTINGDATE")));
				bean.setCLOCKPOSITION(getStr(jb.getString("CLOCKPOSITION")));
				bean.setSOIL(getStr(jb.getString("SOIL").equals("null") ? "" : getStr(jb.getString("SOIL"))));
				bean.setCOATINGFACE(getStr(jb.getString("COATINGFACE").equals("null") ? "" : jb
						.getString("COATINGFACE")));
				bean.setCOATINGAREA(getStr(jb.getString("COATINGAREA")));
				bean.setAPPEARENCEDESC(getStr(jb.getString("APPEARENCEDESC").equals("null") ? "" : jb
						.getString("APPEARENCEDESC")));
				bean.setPITAREA(getStr(jb.getString("PITAREA")));
				bean.setPITAMOUNT(getStr(jb.getString("PITAMOUNT")));
				bean.setPITDEPTHMAX(getStr(jb.getString("PITDEPTHMAX")));
				bean.setPITDEPTHMIN(getStr(jb.getString("PITDEPTHMIN")));
				bean.setCOATINGREPAIR(getStr(jb.getString("COATINGREPAIR")));
				bean.setREPAIRDATE(getStr(jb.getString("REPAIRDATE")));
				bean.setDAMAGETYPE(getStr(jb.getString("DAMAGETYPE")));
				bean.setREPAIRTYPE(getStr(jb.getString("REPAIRTYPE")));
				bean.setREPAIRINFO(getStr(jb.getString("REPAIRINFO")));
				bean.setREMARK(getStr(jb.getString("REMARK").equals("null") ? "" : getStr(jb.getString("REMARK"))));
				bean.setINSPECTOR(getStr(jb.getString("INSPECTOR")));
				bean.setLINELOOP(getStr(jb.getString("LINELOOP")));
				bean.setMARKERNAME(getStr(jb.getString("MARKERNAME")));
				bean.setOFF(getStr(jb.getString("OFF")));

				beans.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return beans;
	}

	/**
	 * 解析巡检信息集合
	 * 
	 * @param data
	 * @return
	 */
	public static InspectionHistoryDetile1 getInspectionHistoryDetial1(String data) {
		InspectionHistoryDetile1 info = new InspectionHistoryDetile1();
		try {
			JSONArray jarray = new JSONArray(data);
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject jb = jarray.getJSONObject(0);
				info.setADVERSARIA(getStr(jb.getString("ADVERSARIA").equals("null") ? "" : jb
						.getString("ADVERSARIA")));
				info.setAVGSPEED(getStr(jb.getString("AVGSPEED").equals("null") ? "" : jb
						.getString("AVGSPEED")));
				info.setBEGINDATETIME(getStr(jb.getString("BEGINDATETIME").equals("null") ? "" : jb
						.getString("BEGINDATETIME")));
				info.setDEALWITH(getStr(jb.getString("DEALWITH").equals("null") ? "" : jb
						.getString("DEALWITH")));
				info.setENDDATETIME(getStr(jb.getString("ENDDATETIME").equals("null") ? "" : jb
						.getString("ENDDATETIME")));
				info.setINSDEVICE(getStr(jb.getString("INSDEVICE").equals("null") ? "" : jb
						.getString("INSDEVICE")));
				info.setINSFREQ(getStr(jb.getString("INSFREQ").equals("null") ? "" : jb
						.getString("INSFREQ")));
				info.setINSPECTOR(getStr(jb.getString("INSPECTOR").equals("null") ? "" : jb
						.getString("INSPECTOR")));
				info.setINSPECTORTYPE(getStr(jb.getString("INSPECTORTYPE").equals("null") ? "" : jb
						.getString("INSPECTORTYPE")));
				info.setINSTYPE(getStr(jb.getString("INSTYPE").equals("null") ? "" : jb
						.getString("INSTYPE")));
				info.setINSVEHICLE(getStr(jb.getString("INSVEHICLE").equals("null") ? "" : jb
						.getString("INSVEHICLE")));
				info.setINSYIELD(getStr(jb.getString("INSYIELD").equals("null") ? "" : jb
						.getString("INSYIELD")));
				info.setOTHERADVERSARIA(getStr(jb.getString("OTHERADVERSARIA").equals("null") ? "" : jb
						.getString("OTHERADVERSARIA")));
				info.setPROBLEM(getStr(jb.getString("PROBLEM").equals("null") ? "" : jb
						.getString("PROBLEM")));
				info.setRODESTATUS(getStr(jb.getString("RODESTATUS").equals("null") ? "" : jb
						.getString("RODESTATUS")));
				info.setSHIFTFROM(getStr(jb.getString("SHIFTFROM").equals("null") ? "" : jb
						.getString("SHIFTFROM")));
				info.setSHIFTTO(getStr(jb.getString("ADVERSARIA").equals("null") ? "" : jb
						.getString("ADVERSARIA")));
				info.setTRACKPOINTS(getStr(jb.getString("TRACKPOINTS").equals("null") ? "" : jb
						.getString("TRACKPOINTS")));
				info.setSHIFTPLACE(getStr(jb.getString("SHIFTPLACE").equals("null") ? "" : jb
						.getString("SHIFTPLACE")));
				info.setSHIFTTIME(getStr(jb.getString("SHIFTTIME").equals("null") ? "" : jb
						.getString("SHIFTTIME")));
				info.setVEHICLE(getStr(jb.getString("VEHICLE").equals("null") ? "" : jb
						.getString("VEHICLE")));
				info.setWEATHER(getStr(jb.getString("WEATHER").equals("null") ? "" : jb
						.getString("WEATHER")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return info;
	}

	/**
	 * 解析未巡检信息集合
	 * 
	 * @param data
	 * @return
	 */
	public static InspectionHistoryDetile2 getInspectionHistoryDetial2(String data) {
		InspectionHistoryDetile2 info = new InspectionHistoryDetile2();
		try {
			JSONArray jarray = new JSONArray(data);
			for (int i = 0; i < jarray.length(); i++) {				
				JSONObject jb =  jarray.getJSONObject(0);

				info.setBEGINTIME(getStr(jb.getString("BEGINTIME").equals("null") ? "" : jb
						.getString("BEGINTIME")));
				info.setENDTIME(getStr(jb.getString("ENDTIME").equals("null") ? "" : jb
						.getString("ENDTIME")));
				info.setNOTPECTOR(getStr(jb.getString("NOTPECTOR").equals("null") ? "" : jb
						.getString("NOTPECTOR")));
				info.setNOTREASON(getStr(jb.getString("NOTREASON").equals("null") ? "" : jb
						.getString("NOTREASON")));
				info.setNOTREMARK(getStr(jb.getString("NOTREMARK").equals("null") ? "" : jb
						.getString("NOTREMARK")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return info;
	}

	/**
	 * 桩同步
	 * 
	 * @param data
	 * @return List<List<PileSyncBean>> MARKERVALUE:桩的值
	 *         ,MARKERTYPE:桩的类型,STACKNAME:桩的名称,STACKCODE:桩的代码,
	 *         MARKEREVENTID:桩EVENTID
	 *         ,MARKERNAME:桩名称,MARKERSTATION:桩里程值,LINELOOPEVENTID;管网EVENTID
	 */

	public static List<PileSyncBean> getPileSyncList(String data) {
		List<PileSyncBean> list = new ArrayList<PileSyncBean>();

		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jb = jsonArray.getJSONObject(i);
				PileSyncBean bean = new PileSyncBean();
				List<PileSyncBeanChild> listchild = new ArrayList<PileSyncBeanChild>();
				bean.setLINELOOPEVENTID(getStr(jb.getString("LINELOOPEVENTID")));

				JSONArray array = jb.getJSONArray("MARKERVALUE");
				for (int j = 0; j < array.length(); j++) {
					JSONObject object = array.getJSONObject(j);
					PileSyncBeanChild beanchild = new PileSyncBeanChild();

					beanchild.setMARKEREVENTID(getStr(object.getString("MARKEREVENTID")));
					beanchild.setMARKERNAME(getStr(object.getString("MARKERNAME")));
					beanchild.setMARKERSTATION(getStr(object.getString("MARKERSTATION")));
					beanchild.setMARKERTYPE(getStr(object.getString("MARKERTYPE")));
					listchild.add(beanchild);
					// bean.setChildBean(childBean);
				}
				bean.setChildBean(listchild);
				list.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 任务描述 巡检出发时间:BEGINDATETIME, 巡线返回时间:ENDDATETIME, 巡检类型:INSTYPE,
	 * 巡检人员(类型):INSPECTORTYPE, 巡检工具:INSVEHICLE, 巡检频次:INSFREQ, 巡检合格率:INSYIELD,
	 * 巡检仪:INSDEVICE, 巡检轨迹点数:TRACKPOINTS, 平均时速:AVGSPEED 唯一标识 EVENTID
	 */
	public static InspectionTaskInfoBean getTaskInfosync(String data) {
		InspectionTaskInfoBean bean = new InspectionTaskInfoBean();

		try {
			JSONArray jarr = new JSONArray(data);
			for (int i = 0; i < jarr.length(); i++) {
				JSONObject jb = jarr.getJSONObject(i);
				bean.setAVGSPEED(getStr(jb.getString("AVGSPEED").equals("null") ? "" : jb
						.getString("AVGSPEED")));
				bean.setTRACKPOINTS(getStr(jb.getString("TRACKPOINTS").equals("null") ? "" : jb
						.getString("TRACKPOINTS")));
				bean.setINSDEVICE(getStr(jb.getString("INSDEVICE").equals("null") ? "" : jb
						.getString("INSDEVICE")));
				bean.setBEGINDATETIME(getStr(jb.getString("BEGINDATETIME").equals("null") ? "" : jb
						.getString("BEGINDATETIME")));
				bean.setENDDATETIME(getStr(jb.getString("ENDDATETIME").equals("null") ? "" : jb
						.getString("ENDDATETIME")));
				bean.setINSTYPE(getStr(jb.getString("INSTYPE").equals("null") ? "" : jb
						.getString("INSTYPE")));
				bean.setINSPECTORTYPE(getStr(jb.getString("INSPECTORTYPE").equals("null") ? "" : jb
						.getString("INSPECTORTYPE")));
				bean.setINSVEHICLE(getStr(jb.getString("INSVEHICLE").equals("null") ? "" : jb
						.getString("INSVEHICLE")));
				bean.setINSFREQ(getStr(jb.getString("INSFREQ").equals("null") ? "" : jb
						.getString("INSFREQ")));
				bean.setINSYIELD(getStr(jb.getString("INSYIELD").equals("null") ? "" : jb
						.getString("INSYIELD")));
				bean.setEVENTID(getStr(jb.getString("EVENTID").equals("null") ? "" : jb
						.getString("EVENTID")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bean;
	}

	/**
	 * 管线同步
	 * 
	 * @param data
	 * @return List<List<PipelineSyncBean>> LINEID:线路代码,LINENAME:线路
	 *         名,OILSECTIONNAME输油段名,OILSECTIONCODE:输油段域值代码
	 */
	public static List<LineSyncBean> getPipelineSyncList(String data) {

		List<LineSyncBean> list = new ArrayList<LineSyncBean>();// 管线同步集合

		try {
			JSONArray jsonArray = new JSONArray(data);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				LineSyncBean bean = new LineSyncBean();
				getChildrenList(jb, bean);
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public static LineSyncBean getChildrenList(JSONObject object, LineSyncBean bean) {
		try {
			JSONArray childrenList = object.getJSONArray("children");
			if (childrenList.length() > 0) {
				bean.setChildrenList(childrenList.length());
				for (int i = 0; i < childrenList.length(); i++) {
					LineSyncBean bean1 = new LineSyncBean();
					bean.getChildrenList()[i] = getChildrenList(childrenList.getJSONObject(i),
							bean1);
				}
			}
			bean.setCreatedate(getStr(object.getString("createdate")));
			bean.setLineloopeventid(getStr(object.getString("lineloopeventid")));
			bean.setLineloopname(getStr(object.getString("lineloopname")));
			bean.setLinetype(getStr(object.getString("linetype")));
			bean.setParentlineloopeventid(getStr(object.getString("parentlineloopeventid")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 报警信息
	 * 
	 * @param data
	 * @return List<List<AlarmInformationBean>>
	 *         报警类型分为超速,越界,掉线;返回结果时三种类型只能出现其中一种,另两种的值会设定为空.
	 *         ALARMTYPE:报警类型,ALARMTIME
	 *         :报警时间,ALARMLOCATION:报警位置,MAXOFFSET:设定最大偏移,
	 *         REALOFFSET:实际偏移,MAXSPEED
	 *         :设定最大速度,REALSPEED:实际速度,DROPPEDINTERVAL:掉线时间间隔,
	 * 
	 */
	public static List<AlarmInformationBean> getAlarmInformationList(String data) {
		List<AlarmInformationBean> list = new ArrayList<AlarmInformationBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				AlarmInformationBean bean = new AlarmInformationBean();
				bean.setALARMTYPE(getStr(jb.getString("ALARMTYPE")));
				bean.setALARMTIME(getStr(jb.getString("ALARMTIME")));

				bean.setALARMLOCATION(getStr(jb.getString("ALARMLOCATION")));
				bean.setMAXOFFSET(getStr(jb.getString("MAXOFFSET")));
				bean.setREALOFFSET(getStr(jb.getString("REALOFFSET")));
				bean.setMAXSPEED(getStr(jb.getString("MAXSPEED")));
				bean.setREALSPEED(getStr(jb.getString("REALSPEED")));
				bean.setDROPPEDINTERVAL(getStr(jb.getString("DROPPEDINTERVAL")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 巡检记录查询
	 * 
	 * @param data
	 * @return INSPECTOR:巡检人员,INSTYPE:巡检类型,INSDATE:巡检日期
	 */
	public static List<InspectionRecordQueryBean> getInspectionRecordQueryList(String data) {
		List<InspectionRecordQueryBean> list = new ArrayList<InspectionRecordQueryBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				InspectionRecordQueryBean bean = new InspectionRecordQueryBean();
				bean.setINSPECTOR(getStr(jb.getString("INSPECTOR")));
				bean.setINSTYPE(getStr(jb.getString("INSTYPE")));
				bean.setINSDATE(getStr(jb.getString("INSDATE")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 计划查询
	 * 
	 * @param data
	 * @return INSPECTOR:巡检人员,INSTIME:巡检时间,INSDATE:巡检日期
	 */
	public static List<InspectPlanBean> getInspectPlanBeanList(String data) {
		List<InspectPlanBean> list = new ArrayList<InspectPlanBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				InspectPlanBean bean = new InspectPlanBean();
				bean.setINSPECTOR(getStr(jb.getString("INSPECTOR")));
				bean.setINSTIME(getStr(jb.getString("INSTIME")));
				bean.setINSDATE(getStr(jb.getString("INSDATE")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 自然电位记录查询
	 * 
	 * @param data
	 * @return 
	 *         EVENTID:唯一标识,DEPARTMENTID:部门编号,YEAR:年份,ACINTERFERENCEVOLTAGE:交流干扰电压
	 *         (V)
	 *         ,MARKEREVENTID;桩REMARK:备注,TEMPERATURE:温度,TESTDATE:测试日期,USERID:
	 *         测试人员VOLTAGE:电位值
	 */
	public static List<NatureVoltageQueryBean> getNatureVoltageQueryBeanList(String data) {
		List<NatureVoltageQueryBean> list = new ArrayList<NatureVoltageQueryBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				NatureVoltageQueryBean bean = new NatureVoltageQueryBean();
				bean.setEVENTID(getStr(jb.getString("EVENTID")));
				bean.setDEPARTMENTID(getStr(jb.getString("DEPARTMENTID")));
				bean.setYEAR(getStr(jb.getString("YEAR")));

				bean.setACINTERFERENCEVOLTAGE(getStr(jb.getString("ACINTERFERENCEVOLTAGE")));
				bean.setMARKEREVENTID(getStr(jb.getString("MARKEREVENTID")));
				bean.setREMARK(getStr(jb.getString("REMARK")));
				bean.setTEMPERATURE(getStr(jb.getString("TEMPERATURE")));
				bean.setTESTDATE(getStr(jb.getString("TESTDATE")));
				bean.setUSERID(getStr(jb.getString("USERID")));
				bean.setVOLTAGE(getStr(jb.getString("VOLTAGE")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 保护电位记录查询
	 * 
	 * @param data
	 * @return 
	 *         EVENTID:唯一标识,DEPARTMENTID:部门编号,WEATHER:天气,ACINTERFERENCEVOLTAGE:交流干扰电压
	 *         (V),
	 *         MARKEREVENTID;桩,REMARK:备注,SOILRESISTIVITY:土壤电阻率(Ωom),USERID:测试人员
	 *         ,VOLTAGE:电位值,TEMPERATURE:温度,TESTDATE:测试日期
	 */
	public static List<CppotentialQueryBean> getCppotentialQueryList(String data) {
		List<CppotentialQueryBean> list = new ArrayList<CppotentialQueryBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				CppotentialQueryBean bean = new CppotentialQueryBean();
				bean.setEVENTID(getStr(jb.getString("EVENTID")));
				bean.setDEPARTMENTID(getStr(jb.getString("DEPARTMENTID")));
				bean.setWEATHER(getStr(jb.getString("WEATHER")));

				bean.setACINTERFERENCEVOLTAGE(getStr(jb.getString("ACINTERFERENCEVOLTAGE")));
				bean.setMARKEREVENTID(getStr(jb.getString("MARKEREVENTID")));
				bean.setREMARK(getStr(jb.getString("REMARK")));
				bean.setSOILRESISTIVITY(getStr(jb.getString("SOILRESISTIVITY")));
				bean.setTEMPERATURE(getStr(jb.getString("TEMPERATURE")));
				bean.setTESTDATE(getStr(jb.getString("TESTDATE")));
				bean.setUSERID(getStr(jb.getString("USERID")));
				bean.setVOLTAGE(getStr(jb.getString("VOLTAGE")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 防腐侧漏查询
	 * 
	 * @param data
	 * @return REPAIRTARGET:修复对象, LEAKHUNTINGDATE:检漏日期, LOCATION:里程位置(管线+桩+偏移量)
	 */
	public static List<AntisepsisQueryBean> getAntisepsisQueryBeanList(String data) {
		List<AntisepsisQueryBean> list = new ArrayList<AntisepsisQueryBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				AntisepsisQueryBean bean = new AntisepsisQueryBean();
				bean.setREPAIRTARGET(getStr(jb.getString("REPAIRTARGET")));
				bean.setLEAKHUNTINGDATE(getStr(jb.getString("LEAKHUNTINGDATE")));
				bean.setLOCATION(getStr(jb.getString("LOCATION")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 接地电阻记录查询
	 * 
	 * @param data
	 * 
	 * @return *
	 *         EVENTID:唯一标识,CPGROUNDBEDEVENTID:地床编号,TESTDATE:测试日期,SETVALUE:规定值,
	 *         TESTVALUE;测试值,CONCLUSION:结论,WEATHER:天气
	 *         TEMPERATURE;温度,INSTRUMENTNAME
	 *         :接地电阻测试仪器型号,INSTRUMENTTYPE:接地电阻测试仪器编号,USERID:测试人员
	 */
	public static List<ResistanceQueryBean> getResistanceQueryList(String data) {
		List<ResistanceQueryBean> list = new ArrayList<ResistanceQueryBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				ResistanceQueryBean bean = new ResistanceQueryBean();
				bean.setEVENTID(getStr(jb.getString("EVENTID")));
				bean.setCPGROUNDBEDEVENTID(getStr(jb.getString("CPGROUNDBEDEVENTID")));
				bean.setTESTDATE(getStr(jb.getString("TESTDATE")));
				bean.setSETVALUE(getStr(jb.getString("SETVALUE")));
				bean.setTESTVALUE(getStr(jb.getString("TESTVALUE")));

				bean.setCONCLUSION(getStr(jb.getString("CONCLUSION")));
				bean.setWEATHER(getStr(jb.getString("WEATHER")));
				bean.setTEMPERATURE(getStr(jb.getString("TEMPERATURE")));
				bean.setINSTRUMENTNAME(getStr(jb.getString("INSTRUMENTNAME")));
				bean.setINSTRUMENTTYPE(getStr(jb.getString("INSTRUMENTTYPE")));
				bean.setUSERID(getStr(jb.getString("USERID")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 我的任务
	 * 
	 * @param data
	 * @return
	 */
	public static List<List<MyTaskBean>> getMyTaskList(String data) {
		List<List<MyTaskBean>> domainList = new ArrayList<List<MyTaskBean>>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				List<MyTaskBean> beans = new ArrayList<MyTaskBean>();
				JSONObject jb = jsonArray.getJSONObject(i);
				MyTaskBean bean = new MyTaskBean();
				bean.setINSPECTTIME(getStr(jb.getString("INSPECTTIME")));
				bean.setINSPECTLINE(getStr(jb.getString("INSPECTLINE")));
				bean.setINSFREQ(getStr(jb.getString("INSFREQ")));
				beans.add(bean);
				JSONArray array = jb.getJSONArray("MARKER");
				for (int j = 0; j < array.length(); j++) {
					JSONObject object = array.getJSONObject(j);
					MyTaskBean jb1 = new MyTaskBean();
					jb1.setMARKEREVENTID(getStr(object.getString("MARKEREVENTID")));
					beans.add(jb1);
				}
				domainList.add(beans);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return domainList;
	}

	/**
	 * 我的任务列表
	 * 
	 * @param data
	 * @return
	 */
	public static List<MyTaskQueryBean> getMyTaskQueryList(String data) {
		List<MyTaskQueryBean> list1 = new ArrayList<MyTaskQueryBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				MyTaskQueryBean bean = new MyTaskQueryBean();
				bean.setINSPECTOR(getStr(jb.getString("INSPECTOR")));
				bean.setINSDATE(getStr(jb.getString("INSDATE")));
				JSONArray array = jb.getJSONArray("INSDATEINFO");
				if (array.length() > 0) {
					for (int j = 0; j < array.length(); j++) {
						Insdateinfo insdateInfo = new Insdateinfo();
						JSONObject object = array.getJSONObject(j);
						insdateInfo.setNAME(getStr(object.getString("NAME")));
						insdateInfo.setFREQTEXT(getStr(object.getString("FREQTEXT")));
						insdateInfo.setTASKLOCATION(getStr(object.getString("TASKLOCATION")));
						insdateInfo.setTASKID(getStr(object.getString("TASKID")));
						insdateInfo.setINSTACTTIME(getStr(object.getString("INSTACTTIME")));
						JSONArray array1 = object.getJSONArray("TASKINFO");
						if (array1.length() > 0) {
							for (int n = 0; n < array1.length(); n++) {
								Taskinfo taskInfo = new Taskinfo();
								JSONObject ob = array1.getJSONObject(n);
								taskInfo.setFREQINDEX(getStr(ob.getString("FREQINDEX")));
								JSONArray array2 = ob.getJSONArray("FREQINFO");
								for (int m = 0; m < array2.length(); m++) {
									Freqinfo freqinfo = new Freqinfo();
									JSONObject ob1 = array2.getJSONObject(m);
									freqinfo.setEVENTID(getStr(ob1.getString("EVENTID")));
									freqinfo.setPOINTID(getStr(ob1.getString("POINTID")));
									freqinfo.setPOINTNAME(getStr(ob1.getString("POINTNAME")));
									freqinfo.setLON(getStr(ob1.getString("LON")));
									freqinfo.setLAT(getStr(ob1.getString("LAT")));
									freqinfo.setLineID(getStr(ob1.getString("LINEID")));
									freqinfo.setStation(getStr(ob1.getString("STATION")));
//									freqinfo.setPOINTTYPE(getStr(ob1.getString("POINTTYPE")));
									taskInfo.getInfoList().add(freqinfo);
								}
								insdateInfo.getTaskInfoList().add(taskInfo);
							}
						}
						bean.getInsdateinfoList().add(insdateInfo);
					}
				}

				list1.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list1;
	}

	/**
	 * 信息查询
	 * 
	 * @param data
	 * @return 管线详细信息: 管线名称：LINELOOPNAME 输送介质：MEDIUMTYPE 管网类型：干线LINETYPE
	 *         长度(km)：LENGTH 管径：DIAMETER 壁厚(mm)：WALLTHICKNESS
	 *         设计压力(MPa)：DESIGNPRESS 防腐状况：ANTISEPSISCONDITION 管材：PIPE
	 *         站场总数：STATIONNUM 压气站数：GASCOMPRESSION 泵站数:PUMPSTATION
	 *         清管站数：LINETRUNCATIONVALUEROOM 线路截断阀室(座)：PIGSTATIONS
	 *         固定资产原值：ORIGINALFIXEDASSETS
	 */
	public static List<InformationQueryBean> getInformationQueryList(String data) {
		List<InformationQueryBean> list = new ArrayList<InformationQueryBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				InformationQueryBean bean = new InformationQueryBean();
				bean.setLINELOOPNAME(getStr(jb.getString("LINELOOPNAME")));
				bean.setMEDIUMTYPE(getStr(jb.getString("MEDIUMTYPE")));
				bean.setLINETYPE(getStr(jb.getString("LINETYPE")));

				bean.setLENGTH(getStr(jb.getString("LENGTH")));
				bean.setDIAMETER(getStr(jb.getString("DIAMETER")));
				bean.setWALLTHICKNESS(getStr(jb.getString("WALLTHICKNESS")));
				bean.setDESIGNPRESS(getStr(jb.getString("DESIGNPRESS")));
				bean.setANTISEPSISCONDITION(getStr(jb.getString("ANTISEPSISCONDITION")));
				bean.setPIPE(getStr(jb.getString("PIPE")));
				bean.setSTATIONNUM(getStr(jb.getString("STATIONNUM")));
				bean.setGASCOMPRESSION(getStr(jb.getString("GASCOMPRESSION")));
				bean.setPUMPSTATION(getStr(jb.getString("PUMPSTATION")));
				bean.setLINETRUNCATIONVALUEROOM(getStr(jb.getString("LINETRUNCATIONVALUEROOM")));
				bean.setPIGSTATIONS(getStr(jb.getString("PIGSTATIONS")));
				bean.setORIGINALFIXEDASSETS(getStr(jb.getString("ORIGINALFIXEDASSETS")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 巡检日志(列表)解析
	 * 
	 * @param data
	 * @return
	 */
	public static List<InspectionPlanQueryBean> getInspectionPlanQueryList(String data) {
		List<InspectionPlanQueryBean> list = new ArrayList<InspectionPlanQueryBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				InspectionPlanQueryBean bean = new InspectionPlanQueryBean();
				bean.setTYPE(getStr(jb.getString("TYPE")));
				bean.setEVENTID(getStr(jb.getString("EVENTID")));
				bean.setLINELOOPNAME(getStr(jb.getString("LINELOOPNAME")));

				bean.setBEGINDATETIME(getStr(jb.getString("BEGINDATETIME")));
				bean.setENDDATETIME(getStr(jb.getString("ENDDATETIME")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 巡检日志(详细)
	 * 
	 * @param data
	 * @return
	 */
	public static List<InspectionInforQueryBean> getInspectionInforQueryList(String data) {
		List<InspectionInforQueryBean> list = new ArrayList<InspectionInforQueryBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				InspectionInforQueryBean bean = new InspectionInforQueryBean();
				bean.setEVENTID(getStr(jb.getString("EVENTID")));
				bean.setDEPARTMENT(getStr(jb.getString("DEPARTMENT")));
				bean.setBEGINDATETIME(getStr(jb.getString("BEGINDATETIME")));
				bean.setENDDATETIME(getStr(jb.getString("ENDDATETIME")));
				bean.setINSTYPE(getStr(jb.getString("INSTYPE")));
				bean.setINSPECTOR(getStr(jb.getString("INSPECTOR")));
				bean.setINSVEHICLE(getStr(jb.getString("INSVEHICLE")));
				bean.setINSFREQ(getStr(jb.getString("INSFREQ")));
				bean.setINSYIELD(getStr(jb.getString("INSYIELD")));
				bean.setTRACKPOINTS(getStr(jb.getString("TRACKPOINTS")));
				bean.setINSDEVICE(getStr(jb.getString("INSDEVICE")));
				bean.setAVGSPEED(getStr(jb.getString("AVGSPEED")));
				bean.setWEATHER(getStr(jb.getString("WEATHER")));
				bean.setRODESTATUS(getStr(jb.getString("RODESTATUS")));
				bean.setADVERSARIA(getStr(jb.getString("ADVERSARIA")));
				bean.setPROBLEM(getStr(jb.getString("PROBLEM")));
				bean.setDEALWITH(getStr(jb.getString("DEALWITH")));
				bean.setSHIFTTIME(getStr(jb.getString("SHIFTTIME")));
				bean.setVEHICLE(getStr(jb.getString("VEHICLE")));
				bean.setSHIFTFROM(getStr(jb.getString("SHIFTFROM")));
				bean.setSHIFTTO(getStr(jb.getString("SHIFTTO")));
				bean.setOTHERADVERSARIA(getStr(jb.getString("OTHERADVERSARIA")));
				bean.setREPORTBY(getStr(jb.getString("REPORTBY")));
				bean.setPLANID(getStr(jb.getString("PLANID")));
				bean.setCREATEDATE(getStr(jb.getString("CREATEDATE")));
				bean.setLINELOOPEVENID(getStr(jb.getString("LINELOOPEVENID")));
				bean.setRANGEFLAG(getStr(jb.getString("RANGEFLAG")));
				bean.setBEGINSTATION(getStr(jb.getString("BEGINSTATION")));
				bean.setENDSTATION(getStr(jb.getString("ENDSTATION")));
				bean.setSLENGTH(getStr(jb.getString("SLENGTH")));
				bean.setRLENGTH(getStr(jb.getString("RLENGTH")));
				bean.setSKEYPOINT(getStr(jb.getString("SKEYPOINT")));
				bean.setRKEYPOINT(getStr(jb.getString("RKEYPOINT")));
				bean.setBEGINLOCATION(getStr(jb.getString("BEGINLOCATION")));
				bean.setENDLOCATION(getStr(jb.getString("ENDLOCATION")));
				bean.setFLAG(getStr(jb.getString("FLAG")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 未巡检日志(详细)
	 * 
	 * @param data
	 * @return
	 */
	public static List<InspectionInforQueryBean1> getInspectionInforQueryList1(String data) {
		List<InspectionInforQueryBean1> list = new ArrayList<InspectionInforQueryBean1>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				InspectionInforQueryBean1 bean = new InspectionInforQueryBean1();
				bean.setEVENTID(getStr(jb.getString("EVENTID")));
				bean.setBEGINTIME(getStr(jb.getString("BEGINTIME")));
				bean.setENDTIME(getStr(jb.getString("ENDTIME")));
				bean.setNOTPECTOR(getStr(jb.getString("NOTPECTOR")));
				bean.setNOTREASON(getStr(jb.getString("NOTREASON")));
				bean.setNOTREMARK(getStr(jb.getString("NOTREMARK")));
				bean.setREPORTDATE(getStr(jb.getString("REPORTDATE")));
				bean.setCREATEBY(getStr(jb.getString("CREATEBY")));
				bean.setCREATEDATE(getStr(jb.getString("CREATEDATE")));
				bean.setMODIFYBY(getStr(jb.getString("MODIFYBY")));
				bean.setMODIFYDATE(getStr(jb.getString("MODIFYDATE")));
				bean.setLINELOOPEVENTID(getStr(jb.getString("LINELOOPEVENTID")));
				bean.setRANGEFLAG(getStr(jb.getString("RANGEFLAG")));
				bean.setBEGINSTATION(getStr(jb.getString("BEGINSTATION")));
				bean.setENDSTATION(getStr(jb.getString("ENDSTATION")));
				bean.setSLENGTH(getStr(jb.getString("SLENGTH")));
				bean.setRLENGTH(getStr(jb.getString("RLENGTH")));
				bean.setBEGINLOCATION(getStr(jb.getString("BEGINLOCATION")));
				bean.setENDLOCATION(getStr(jb.getString("ENDLOCATION")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 组织机构同步
	 * 
	 * @param data
	 * @return
	 */
	public static List<UnitBean> getUnitList(String data) {
		List<UnitBean> list = new ArrayList<UnitBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				UnitBean bean = new UnitBean();
				bean.setEVENTID(getStr(jb.getString("EVENTID")));
				bean.setDEPARTMENT(getStr(jb.getString("DEPARTMENT")));
				bean.setPARENTDEPARTMENT(getStr(jb.getString("PARENTDEPARTMENT")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 管理范围同步
	 * 
	 * @param data
	 * @return
	 */
	public static List<SubSystemBean> getSubSystemList(String data) {
		List<SubSystemBean> list = new ArrayList<SubSystemBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				SubSystemBean bean = new SubSystemBean();
				bean.setDEPARTMENT(getStr(jb.getString("DEPARTMENT")));
				bean.setLINELOOPNAME(getStr(jb.getString("LINELOOPNAME")));
				bean.setBEGINSTATION(getStr(jb.getString("BEGINSTATION")));
				bean.setENDSTATION(getStr(jb.getString("ENDSTATION")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 人员考评
	 * 
	 * @param data
	 * @return
	 */
	public static List<JobEvaluateBean> getJobEvaluateList(String data) {
		List<JobEvaluateBean> list = new ArrayList<JobEvaluateBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				JobEvaluateBean bean = new JobEvaluateBean();
				bean.setYEAR(getStr(jb.getString("YEAR")));
				bean.setMONTH(getStr(jb.getString("MONTH")));
				bean.setARTIFICIALSCORE(getStr(jb.getString("ARTIFICIALSCORE")));
				bean.setSYSSCORE(getStr(jb.getString("SYSSCORE")));
				bean.setOPINIONS(getStr(jb.getString("OPINIONS")));
				bean.setNAME(getStr(jb.getString("NAME")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 解析查询的巡检计划信息
	 * 
	 * @param data
	 * @return
	 */
	public static List<QueryPlanBean> getQueryPlanBean(String data) {
		List<QueryPlanBean> list = new ArrayList<QueryPlanBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				QueryPlanBean bean = new QueryPlanBean();
				bean.setINSPECTORID(getStr(jb.getString("INSPECTORID")));
				bean.setEVENTID(getStr(jb.getString("EVENTID")));
				bean.setINSPROPER(getStr(jb.getString("INSPROPER")));
				bean.setINSTYPE(getStr(jb.getString("INSTYPE")));
				bean.setINSPECTLINE(getStr(jb.getString("INSPECTLINE")));
				bean.setDETERMINANT(getStr(jb.getString("DETERMINANT")));
				bean.setINSPECTOR(getStr(jb.getString("INSPECTOR")));
				bean.setINSVEHICLE(getStr(jb.getString("INSVEHICLE")));
				bean.setINSFREQ(getStr(jb.getString("INSFREQ")));
				bean.setPLANNO(getStr(jb.getString("PLANNO")));
				JSONArray jsonArray1 = jb.getJSONArray("INSPECTLINE");
				for (int n = 0; n < jsonArray1.length(); n++) {
					Inspectline bean1 = new Inspectline();
					JSONObject jb1 = jsonArray1.getJSONObject(n);
					bean1.setLINELOOP(getStr(jb1.getString("lINELOOP")));
					bean1.setRANGEFLAG(getStr(jb1.getString("RANGEFLAG")));
					bean.getLineList().add(bean1);
				}
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 解析地床编号信息
	 * 
	 * @param data
	 * @return
	 */
	public static List<CpgroundbedBean> getCpgroundbedBean(String data) {
		List<CpgroundbedBean> list = new ArrayList<CpgroundbedBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				CpgroundbedBean bean = new CpgroundbedBean();
				bean.setEVENTID(getStr(jb.getString("EVENTID")));
				bean.setCPGROUNDBEDEVENTID(getStr(jb.getString("CPGROUNDBEDEVENTID")));
				bean.setDEPARTMENTID(getStr(jb.getString("DEPARTMENTID")));

				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 消息推送信息
	 * 
	 * @param data
	 * @return List<List<NotificationInformationBean>>
	 *         消息类型分为站内信，;返回结果时三种类型只能出现其中一种,另两种的值会设定为空.
	 *         ALARMTYPE:报警类型,ALARMTIME:
	 *         报警时间,ALARMLOCATION:报警位置,MAXOFFSET:设定最大偏移,
	 *         REALOFFSET:实际偏移,MAXSPEED:
	 *         设定最大速度,REALSPEED:实际速度,DROPPEDINTERVAL:掉线时间间隔,
	 * 
	 */
	public static List<NotificationInformationBean> getNotificationInformationList(String data) {
		List<NotificationInformationBean> list = new ArrayList<NotificationInformationBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				NotificationInformationBean bean = new NotificationInformationBean();
				bean.setNOTIFICATIONTYPE(getStr(jb.getString("NOTIFICATIONTYPE")));
				bean.setSENDTIME(getStr(jb.getString("SENDTIME")));
				bean.setNAME(getStr(jb.getString("NAME")));
				bean.setDETAIL(getStr(jb.getString("DETAIL")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 同步解析桩
	 * 
	 * @param data
	 * @return
	 */
	public static List<GpsMarker> getGpsMarkerList(String data) {
		List<GpsMarker> list = new ArrayList<GpsMarker>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				GpsMarker bean = new GpsMarker();
				bean.setActive(getStr(jb.getString("active")));
				bean.setLat(getStr(jb.getString("lat")));
				bean.setLineloopid(getStr(jb.getString("lineloopid")));
				bean.setLon(getStr(jb.getString("lon")));
				bean.setMarkerid(getStr(jb.getString("markerid")));
				bean.setMarkername(getStr(jb.getString("markername")));
				bean.setMarkerstation(getStr(jb.getString("markerstation")));
				bean.setMarkertype(getStr(jb.getString("markertype")));
				bean.setRemark(getStr(jb.getString("remark")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<UserBean> getUserList(String data) {
		List<UserBean> list = new ArrayList<UserBean>();
		try {
			JSONObject ob = new JSONObject(data);
			UserBean bean = new UserBean();
			getChildren(bean, ob);
			list.add(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ok---------------11111111111");
		return list;
	}

	public static UserBean getChildren(UserBean bean, JSONObject ob) {
		try {
			if ("1".equals(ob.getString("notetype").trim())) {
				JSONArray jsonArray = ob.getJSONArray("mysubChild");
				if (jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject ob1 = jsonArray.getJSONObject(i);
						UserBean bean1 = new UserBean();
						bean.getChildrenList().add(getChildren(bean1, ob1));
					}
				}

			}
			bean.setId(getStr(ob.getString("eventid")));
			bean.setText(getStr(ob.getString("name")));
			bean.setType(getStr(ob.getString("notetype")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bean;
	}

	/**
	 * 
	 * @功能描述 解析事项上报统计
	 * @author 张龙飞 Email:longfeiz@geo-k.cn Tel:13671277587
	 * @param data
	 * @return
	 * @createDate 2013-11-26 下午1:21:15
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static Map getStatisticsProblem(String data) {
		Map map = new HashMap<String, Double>();
		try {
			JSONArray ob = new JSONArray(data);
			for (int i = 0; i < ob.length(); i++) {
				map.put(getStr(ob.getJSONObject(i).getString("TYPE")),
						Double.valueOf(getStr((ob.getJSONObject(i).getString("SUM")))));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 
	 * @功能描述 解析设备使用状况统计
	 * @author 张龙飞Email:longfeiz@geo-k.cn Tel:13671277587
	 * @param data
	 * @return
	 * @createDate 2013-11-26 下午2:15:27
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static Map getStatisticsDevice(String data) {
		Map map = new HashMap<String, Double>();
		try {
			JSONArray ob = new JSONArray(data);
			for (int i = 0; i < ob.length(); i++) {
				map.put(getStr(ob.getJSONObject(i).getString("CODENAME")),
						Double.valueOf(getStr((ob.getJSONObject(i).getString("SUM")))));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 
	 * @功能描述 解析桩数据
	 * @author 张龙飞Email:longfeiz@geo-k.cn Tel:13671277587
	 * @return
	 * @createDate 2013-11-29 下午1:48:40
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static List<GpsMarker> getMarkerList(String data) {
		List<GpsMarker> list = new ArrayList<GpsMarker>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				GpsMarker marker = new GpsMarker();
				JSONObject jb = jsonArray.getJSONObject(i);
				marker.setMarkername(getStr(jb.getString("NAME")));
				marker.setLon(getStr(jb.getString("X")));
				marker.setLat(getStr(jb.getString("Y")));
				list.add(marker);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 解析搜索管线的桩数据
	 * 
	 * @功能描述
	 * @author 张龙飞[wuchangming] Email:longfeiz@geo-k.cn Tel:13671277587
	 * @param data
	 * @return
	 * @createDate 2013-11-30 下午12:26:45
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static List<GpsLine> getLineMarkerList(String data) {
		List<GpsLine> list = new ArrayList<GpsLine>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				GpsLine line = new GpsLine();
				JSONObject jb = jsonArray.getJSONObject(i);
				line.setLINEID(getStr(jb.getString("LINEID")));
				line.setLINENAME(getStr(jb.getString("LINENAME")));
				int a = 0;
				if(!(jb.optString("MARKERLIST").equals(""))){
					a = jb.optJSONArray("MARKERLIST").length();
				}
				
				if (a > 0) {
					JSONArray json = jb.optJSONArray("MARKERLIST");
					for (int j = 0; j < json.length(); j++) {
						GpsMarker marker = new GpsMarker();
						JSONObject jb1 = json.getJSONObject(j);
						marker.setMarkerid(getStr(jb1.getString("MARKERID")));
						marker.setMarkername(getStr(jb1.getString("MARKERNAME")));
						marker.setLon(getStr(jb1.getString("X")));
						marker.setLat(getStr(jb1.getString("Y")));
						line.getMarkerLsit().add(marker);
					}
				}
				if(!(jb.optString("MARKERLIST").equals(""))){
					list.add(line);
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 
	 * @功能描述 解析轨迹回放
	 * @author 张龙飞Email:longfeiz@geo-k.cn Tel:13671277587
	 * @param data
	 * @return
	 * @createDate 2013-12-1 下午3:53:50
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static List<GpsUser> getLocusUser(String data) {
		List<GpsUser> list = new ArrayList<GpsUser>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				GpsUser user = new GpsUser();
				JSONObject jb = jsonArray.getJSONObject(i);
				user.setNAME(getStr(jb.getString("NAME")));
				int a = 0;
				if(!(jb.getString("POINTLIST").equals(""))){
					a = jb.optJSONArray("POINTLIST").length();
				}				
				
				if (a > 0) {
					JSONArray json = jb.optJSONArray("POINTLIST");
					for (int j = 0; j < json.length(); j++) {
						GpsLocation location = new GpsLocation();
						JSONObject jb1 = json.getJSONObject(j);
						location.setLON(getStr(jb1.getString("X")));
						location.setLAT(getStr(jb1.getString("Y")));
						user.getGpsList().add(location);
					}
				}

				list.add(user);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public static List<GpsUser> getLocusUser1(String data) {
		List<GpsUser> list = new ArrayList<GpsUser>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				GpsUser user = new GpsUser();
				JSONObject jb = jsonArray.getJSONObject(i);
				user.setNAME(getStr(jb.getString("NAME")));
				int a = 0;
				if(!(jb.getString("POINTLIST").equals(""))){
					a = jb.optJSONArray("POINTLIST").length();
				}

				if (a > 0) {
					JSONArray json = jb.optJSONArray("POINTLIST");
					int i1 = json.length() / 5;
					Log.i("geren",i1+"");
					for (int j = 0; j < json.length()/3; j++) {
						GpsLocation location = new GpsLocation();
						JSONObject jb1 = json.getJSONObject(j);
						location.setLON(getStr(jb1.getString("X")));
						location.setLAT(getStr(jb1.getString("Y")));
						user.getGpsList().add(location);
						String x = jb1.getString("X");
						String y = jb1.getString("Y");
						GpsHui gpsHui=new GpsHui(x,y);
						gpsHuis.add(gpsHui);
					}
				}

				list.add(user);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 
	 * @功能描述解析实时监控在线人员
	 * @author 张龙飞 Email:longfeiz@geo-k.cn Tel:13671277587
	 * @param data
	 * @return
	 * @createDate 2013-11-30 下午1:39:39
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static List<GpsUser> getUserXY(String data) {
		List<GpsUser> list = new ArrayList<GpsUser>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				GpsUser user = new GpsUser();
				JSONObject jb = jsonArray.getJSONObject(i);
				user.setNAME(getStr(jb.getString("NAME")));
				user.setUNITID(getStr(jb.getString("UNITID")));
				user.setLON(getStr(jb.getString("X")));
				user.setLAT(getStr(jb.getString("Y")));
				list.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 
	 * @功能描述
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn
	 * @param data
	 * @return
	 * @createDate 2013-11-30 下午5:00:49
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static Map getStatisticsQualifiedRate(String data) {
		Map map = new HashMap<String, Double>();
		try {
			JSONArray ob = new JSONArray(data);
			for (int i = 0; i < ob.length(); i++) {
				map.put(getStr(ob.getJSONObject(i).getString("USERNAME")),
						Double.valueOf(getStr((ob.getJSONObject(i).getString("PASSRATE")))));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 
	 * @功能描述
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn
	 * @param data
	 * @return
	 * @createDate 2013-11-30 下午5:00:42
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static List<UnqualifiedPersonBean> getStatisticsUnqualifiedPerson(String data) {
		List<UnqualifiedPersonBean> list = new ArrayList<UnqualifiedPersonBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				UnqualifiedPersonBean bean = new UnqualifiedPersonBean();
				bean.setUSERNAME(getStr(jb.getString("USERNAME")));
				bean.setDEPARTMENT(getStr(jb.getString("DEPARTMENT")));
				bean.setUNQUALIFIEDRATE(getStr(jb.getString("FAILURERATE")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<VersionBean> getVersionList(String data) {
		List<VersionBean> list = new ArrayList<VersionBean>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jb = jsonArray.getJSONObject(i);
				String version = jb.getString("VERSION");
				String url = jb.getString("URL");
				String type = jb.getString("TYPE");
				VersionBean bean = new VersionBean(version,url,type);
//				bean.setVERSION(getStr(jb.getString("VERSION")));
//				bean.setURL(getStr(jb.getString("REMARK")));
//				bean.setTYPE(getStr(jb.getString("TYPE")));

				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	public static String getStr(String name){
		if(name.equals("null")){
			return "";
		}
		return name;
	}
}
