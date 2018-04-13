package com.geok.langfang.DB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.geok.langfang.DB.Type;
import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.GpsMarker;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;

/**
 * 
 * @author Administrator PipeLineDB.java数据库操作 类 定义数据库的插入，更新，删除，查询等操作
 * 
 */

public class OperationDB extends Type {

	// 数据库操作的辅助类
	private PipeLineDB dbhelp;
	public SQLiteDatabase db;
	Context context;
	MyApplication myApplication;

	public OperationDB(Context context) {
		dbhelp = new PipeLineDB(context);
		this.context = context;
		myApplication = new MyApplication(context);
	}

	// select * from gps_marker t where (t.lon<103.78 and t.lon>103.6) and
	// (t.lat<36.94 and t.lat>36.9)
	public List<GpsMarker> getGpsMarker(GeoPoint leftTop, GeoPoint rightBottom) {

		db = dbhelp.getWritableDatabase();
		List<GpsMarker> list = new ArrayList<GpsMarker>();
		String sql = "select * from gps_marker where (lon<" + (rightBottom.getLongitudeE6() / 1E6)
				+ " and lon>" + (leftTop.getLongitudeE6() / 1E6) + ") and (lat>"
				+ (rightBottom.getLatitudeE6() / 1E6) + " and lat<"
				+ (leftTop.getLatitudeE6() / 1E6) + ")";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			GpsMarker marker = new GpsMarker();
			marker.setActive(cursor.getString(cursor.getColumnIndex("active")));
			marker.setLat(cursor.getString(cursor.getColumnIndex("lat")));
			marker.setLon(cursor.getString(cursor.getColumnIndex("lon")));
			marker.setLineloopid(cursor.getString(cursor.getColumnIndex("lineloopid")));
			marker.setMarkerid(cursor.getString(cursor.getColumnIndex("markerid")));
			marker.setMarkername(cursor.getString(cursor.getColumnIndex("markername")));
			marker.setMarkerstation(cursor.getString(cursor.getColumnIndex("markerstation")));
			marker.setMarkertype(cursor.getString(cursor.getColumnIndex("markertype")));
			marker.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
			list.add(marker);
		}
		db.close();
		return list;
	}

	/**
	 * 插入gps桩数据
	 * 
	 * @param data
	 */
	public void insertGpsMarker(String data) {
		db = dbhelp.getWritableDatabase();
		List<GpsMarker> values = Json.getGpsMarkerList(data);
		String sql = "INSERT INTO gps_marker (markerid, lineloopid, markername, "
				+ "markerstation, markertype, lon, lat, remark, active) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		db.beginTransaction();

		SQLiteStatement stmt = db.compileStatement(sql);
		for (int i = 0; i < values.size(); i++) {
			stmt.bindString(1, values.get(i).getMarkerid());
			stmt.bindString(2, values.get(i).getLineloopid());
			stmt.bindString(3, values.get(i).getMarkername());
			stmt.bindString(4, values.get(i).getMarkerstation());
			stmt.bindString(5, values.get(i).getMarkertype());
			stmt.bindString(6, values.get(i).getLon());
			stmt.bindString(7, values.get(i).getLat());
			stmt.bindString(8, values.get(i).getRemark());
			stmt.bindString(9, values.get(i).getActive());
			stmt.execute();
			stmt.clearBindings();
		}

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/*
	 * 数据的添加用于保存操作 参数 表名，指定保存在哪个表中，数据列表 isupload=0,未上传 isupload=1,已上传
	 * isupload=2,保存
	 */

	public void DBinsert(ContentValues value, int type) {
		db = dbhelp.getWritableDatabase();

		int isupload;
		boolean isnew = true;
		switch (type) {
		case PROTECTIVE_POTENTIAL:
			db.execSQL(
					"insert into  PROTECTIVE_POTENTIAL (guid,userid,year,month,line,pile,value,test_time,"
							+ "remarks,voltage,ground,temperature,lineid,pileid,natural,ir,isupload) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("guid"), value.get("userid"), value.get("year"),
							value.get("month"), value.get("line"), value.get("pile"),
							value.get("value"), value.get("test_time"), value.get("remarks"),
							value.get("voltage"), value.get("ground"), value.get("temperature"),
							value.get("lineid"), value.get("pileid"), value.get("natural"),
							value.get("ir"), value.get("isupload") });
			break;

		case NATURAL_POTENTIAL:

			db.execSQL(
					"insert into  NATURAL_POTENTIAL (guid,userid,year,month,line,pile,value,test_time,"
							+ "remarks,voltage,temperature,weather,lineid,pileid,isupload) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("guid"), value.get("userid"), value.get("year"), value.get("month"),
							value.get("line"), value.get("pile"), value.get("value"),
							value.get("test_time"), value.get("remarks"), value.get("voltage"),
							value.get("temperature"), value.get("weather"), value.get("lineid"),
							value.get("pileid"), value.get("isupload") });
			break;

		case GROUND_RESISTANCE:

			db.execSQL(
					"insert into  GROUND_RESISTANCE (guid,cpgroundbedeventid,userid,test_date,set_value,test_value,conclusion,year,"
							+ "halfyear,isupload) values(?,?,?,?,?,?,?,?,?,?)", new Object[] {
							value.get("guid"), value.get("cpgroundbedeventid"),
							value.get("userid"), value.get("test_date"), value.get("set_value"),
							value.get("test_value"), value.get("conclusion"), value.get("year"),
							value.get("halfyear"), value.get("isupload") });
			break;

		case CORROSIVE:
			/*
			 * GUID,USERID,LINELOOPEVENTID,MARKEREVENTID,OFF,REPAIRTARGET," +
			 * "LEAKHUNTINGDATE,CLOCKPOSITION,SOIL,COATINGFACE,COATINGAREA," +
			 * "APPEARENCEDESC,PITAREA,PITAMOUNT,PITDEPTHMAX,PITDEPTHMIN," +
			 * "COATINGREPAIR,REPAIRDATE,DAMAGETYPE,REPAIRTYPE,REPAIRINFO," +
			 * "REMARK,ISUPLOAD
			 */
			db.execSQL(
					"insert into  CORROSIVE(guid,userid,line,pile,offset,repair_obj,"
							+ "check_date,clockposition,soil,damage_des,area,"
							+ "corrosion_des,corrosion_area,corrosion_num,pitdepthmax,pitdepthmin,"
							+ "repair_situation,repair_date,damage_type,repair_type,pile_info,"
							+ "remarks,lineid,pileid,isupload) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("guid"), value.get("userid"), value.get("line"),
							value.get("pile"), value.get("offset"), value.get("repair_obj"),
							value.get("check_date"), value.get("clockposition"), value.get("soil"),
							value.get("damage_des"), value.get("area"), value.get("corrosion_des"),
							value.get("corrosion_area"), value.get("corrosion_num"),
							value.get("pitdepthmax"), value.get("pitdepthmin"),
							value.get("repair_situation"), value.get("repair_date"),
							value.get("damage_type"), value.get("repair_type"),
							value.get("pile_info"), value.get("remarks"), value.get("lineid"),
							value.get("pileid"), value.get("isupload") });
			break;

		case PROBLEM_UPLOAD:

			db.execSQL(
					"insert into  PROBLEM_UPLOAD(PROBLEMTYPE,GUID,LON,LAT,OCCURTIME,PHOTOPATH,"
							+ "PHOTODES,LINE,LINEID,PILE,PILEID,"
							+ "OFFSET,USERID,DEPARTMENTID,UPLOADTIME,PROBLEMDES,OCCURPLACE,SOLUTION,SITUATION,ISUPLOAD) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("problemtype"), value.get("guid"), value.get("lon"),
							value.get("lat"), value.get("occurtime"), value.get("photopath"),
							value.get("photodes"), value.get("line"), value.get("lineid"),
							value.get("pile"), value.get("pileid"), value.get("offset"),
							value.get("userid"), value.get("departmentid"),
							value.get("uploadtime"), value.get("problemdes"),
							value.get("occurplace"), value.get("solution"), value.get("situation"),
							value.get("isupload") });
			break;

		case LINE_LOG:

			db.execSQL(
					"insert into  LINE_LOG(GUID,BEGINTIME,ENDTIME,TYPE,TESTER,TOOLS,"
							+ "INSPFREQ,YEILD,DEVICE,POINTS,SPEED,"
							+ "WEATHER,ROAD,RECORD,PROBLEM,RESULT,TIME,LOCATION,CAR,SENDER,RECEIVER,OTHER,ISUPLOAD,INFO,EVENTID) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("guid"), value.get("begintime"), value.get("endtime"),
							value.get("type"), value.get("tester"), value.get("tools"),
							value.get("inspfreq"), value.get("yeild"), value.get("device"),
							value.get("points"), value.get("speed"), value.get("weather"),
							value.get("road"), value.get("record"), value.get("problem"),
							value.get("result"), value.get("time"), value.get("location"),
							value.get("car"), value.get("sender"), value.get("receiver"),
							value.get("other"), value.get("isupload"), value.get("info"),
							value.get("eventid") });
			break;

		case GPS_UPLOCAD:

			db.execSQL(
					"insert into  GPS_UPLOAD (GUID,USERID,LON,LAT,DATE,POINTCOUNT,ISUPLOAD) values(?,?,?,?,?,?,?)",
					new Object[] { value.get("guid"), value.get("userid"), value.get("lon"),
							value.get("lat"), value.get("date"), value.get("pointcount"),
							value.get("isupload") });
			Log.i("baidu","添加数据");
			break;

		case ALARM_INFO:

			db.execSQL(
					"insert into  ALARM_INFO(ALARMTYPE,"
							+ "DEPARTMENTID,MAXSPEED,REALSPEED,MAXOFFSET,REALOFFSET,DROPPEDINTERVAL,"
							+ "ALARMLOCATION,ALARMTIME,USERID,ISNEW) values(?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("alarmtype"), value.get("departmentid"),
							value.get("maxspeed"), value.get("realspeed"), value.get("maxoffset"),
							value.get("realoffset"), value.get("droppedinterval"),
							value.get("alarmlocation"), value.get("alarmtime"),
							value.get("userid"), value.get("isnew") });

			break;

		case PIS_LINELOOP_INFO:

			db.execSQL(
					"insert into  PIS_LINELOOP_INFO(LINELOOPID,MEDIUMTYPE,LINETYPE,LENGTH,DIAMETER,WALLTHICKNESS,"
							+ "DESIGNPRESS,ANTISEPSISCONDITION,PIPE,STATIONNUM,GASCOMPRESSION,"
							+ "PUMPSTATION,LINETRUNCATIONVALUEROOM,PIGSTATIONS,ORIGINALFIXEDASSETS,LINENUMBER,LON,"
							+ "LAT,USERID,ISUPLOAD) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("lineloopid"), value.get("mediumtype"),
							value.get("linetype"), value.get("length"), value.get("diameter"),
							value.get("wallthickness"), value.get("designpress"),
							value.get("antisepsiscondition"), value.get("pipe"),
							value.get("stationnum"), value.get("gascompression"),
							value.get("pumpstation"), value.get("linetruncationvalueroom"),
							value.get("pigstations"), value.get("originalfixedassets"),
							value.get("linenumber"), value.get("lon"), value.get("lat"),
							value.get("userid"), value.get("isupload") });
			break;

		case GPS_INSTASK_DAY_MAIN:

			db.execSQL(
					"insert into  GPS_INSTASK_DAY_MAIN(INSDATE,INSPECTORID,BEGININSBT,ENDINSET,TASKCOUNT,ACTIVE,"
							+ "CREATEBY,CREATEDATE,MODIFYBY,MODIFYDATE,ISUPLOAD) values(?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("instate"), value.get("inspectorid"),
							value.get("begininsbt"), value.get("endinset"), value.get("taskcount"),
							value.get("active"), value.get("createby"), value.get("createdate"),
							value.get("modifyby"), value.get("modifydate"), value.get("isupload") });
			break;

		case GPS_INSTASK_DAY:

			db.execSQL(
					"insert into  GPS_INSTASK_DAY(MAINID,PLANID,NAME,TASKINDEX,LINELOOPEVENTID,BEGINSTATION,"
							+ "ENDSTATION,TASKLOCATION,BEGINTIME,ENDTIME,FREQVALUE,FREQTEXT,ISUPLOAD) values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("mainid"), value.get("planid"), value.get("name"),
							value.get("taskindex"), value.get("lineloopeventid"),
							value.get("beginstation"), value.get("endstation"),
							value.get("tasklocation"), value.get("begintime"),
							value.get("endtime"), value.get("freqvalue"), value.get("freqtext"),
							value.get("isupload") });
			break;

		case GPS_INSTASK_DAY_POINT:

			db.execSQL(
					"insert into  GPS_INSTASK_DAY_POINT(FREQINDEX,TASKID,POINTID,POINTNAME,POINTTYPE,RULETIME,"
							+ "ARRTIME,LON,LAT,ARRLON,ARRLAT,BUFFERRANGE,ISUPLOAD) values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("freqindex"), value.get("taskid"),
							value.get("pointin"), value.get("pointname"), value.get("pointtype"),
							value.get("ruletime"), value.get("arrtime"), value.get("lon"),
							value.get("lat"), value.get("arrlon"), value.get("arrlat"),
							value.get("bufferrange"), value.get("isupload") });
			break;

		case GPS_INSTASK_UPLOAD:

			db.execSQL(
					"insert into  GPS_INSTASK_UPLOAD(GUID,PIPEEVENTID,ARRIIVETIME,LON,LAT,ISUPLOAD) values(?,?,?,?,?,?)",
					new Object[] { value.get("guid"), value.get("pipeeventid"),
							value.get("arrivetime"), value.get("lon"), value.get("lat"),
							value.get("isupload") });
			break;

		case GPS_INSRECORD:

			db.execSQL(
					"insert into  GPS_INSRECORD(DEPARTMENTID,BEGINDATETIME,ENDDATETIME,INSTYPE,INSPECTORID,"
							+ "INSVEHICLE,INSFREQ,INSYIELD,TRACKPOINTS,INSDEVICE"
							+ "AVGSPEED,WEATHER,RODESTATUS,ADVERSARIA,PROBLEM,"
							+ "DEALWITH,SHIFTTIME,VEHICLE,SHIFTFROM,SHIFTTO,OTHERADVERSARIA,"
							+ "REPORTBY,PLANID,FLAG,ACTIVE,CREATEDATE,ISUPLOAD) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("departmentid"), value.get("begindatetime"),
							value.get("enddatetime"), value.get("instype"),
							value.get("inspectorid"), value.get("insvehicle"),
							value.get("insfreq"), value.get("insyield"), value.get("trackpoints"),
							value.get("insdevice"), value.get("avgspeed"), value.get("weather"),
							value.get("rodestatus"), value.get("adversaria"), value.get("problem"),
							value.get("dealwith"), value.get("shifttime"), value.get("vehicle"),
							value.get("shiftfrom"), value.get("shiftto"),
							value.get("otheradversaria"), value.get("reportby"),
							value.get("planid"), value.get("flag"), value.get("active"),
							value.get("createdate"), value.get("isupload") });
			break;

		case GPS_INSRECORD_LINE:

			db.execSQL(
					"insert into  GPS_INSRECORD_LINE(LINELOOPEVENID,RANGEFLAG,BEGINSTATION,ENDSTATION,SLENGTH,RLENGTH,"
							+ "SKEYPOINT,RKEYPOINT,BEGINLOCATION,ENDLOCATION,ISUPLOAD) values(?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("lineloopevenid"), value.get("rangeflag"),
							value.get("beginstation"), value.get("endstation"),
							value.get("slength"), value.get("rlength"), value.get("skeypoint"),
							value.get("rkeypoint"), value.get("beginlocation"),
							value.get("endlocation"), value.get("isupload") });
			break;

		case GPS_UNINSRECORD:

			db.execSQL(
					"insert into  GPS_UNINSRECORD(BEGINTIME,ENDTIME,NOTPECTOR,NOTREASON,NOTREMARK,REPORTDATE,"
							+ "REPORTBY,DEPARTMENTID,ACTIVE,CREATEBY,CREATEDATE,"
							+ "MODIFYBY,MODIFYDATE,ISUPLOAD) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("begintime"), value.get("endtime"),
							value.get("notpector"), value.get("notreason"), value.get("notremark"),
							value.get("reportdate"), value.get("reportby"),
							value.get("departmentid"), value.get("active"), value.get("createby"),
							value.get("createdate"), value.get("modifyby"),
							value.get("modifydate"), value.get("isupload") });
			break;

		case GPS_UNINSRECORD_LINE:

			db.execSQL(
					"insert into  GPS_UNINSRECORD_LINE(PLANEVENTID,LINELOOPEVENID,RANGEFLAG,BEGINSTATION,ENDSTATION,SLENGTH,RLENGTH,"
							+ "SKEYPOINT,RKEYPOINT,BEGINLOCATION,ENDLOCATION,ISUPLOAD) values(?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("planeventid"), value.get("lineloopevenid"),
							value.get("rangeflag"), value.get("beginstation"),
							value.get("endstation"), value.get("slength"), value.get("rlength"),
							value.get("skeypoint"), value.get("rkeypoint"),
							value.get("beginlocation"), value.get("endlocation"),
							value.get("isupload") });
			break;

		case PIS_PLAN_INFO:

			db.execSQL(
					"insert into  PIS_PLAN_INFO(PLANNO,INSTYPE,EXECUNITID,INSPECTORTYPE,"
							+ "INSFREQ,INSFREQUNIT,INSFREQUNITVAL,INSVEHICLE,INSPECTORID,"
							+ "INSPECTOR,INSPROPER,BEGININSBDATE,ENDINSEDATE,DETERMINANT,REPEALDATE,"
							+ "PLANFLAG,DEPARTMENTID,ACTIVE,CREATEDATE,ISUPLOAD) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("planno"), value.get("instype"),
							value.get("execunitid"), value.get("inspectortype"),
							value.get("insfreq"), value.get("insfrequnit"),
							value.get("insfrequnitval"), value.get("insvehicle"),
							value.get("inspectorid"), value.get("inspector"),
							value.get("insproper"), value.get("begininsbdate"),
							value.get("endinsedate"), value.get("determinant"),
							value.get("repealdate"), value.get("planflag"),
							value.get("departmentid"), value.get("active"),
							value.get("createdate"), value.get("isupload") });
			break;

		case PIS_PLAN_LINE:

			db.execSQL(
					"insert into  PIS_PLAN_LINE(LINELOOPEVENID,RANGEFLAG,BEGINSTATION,ENDSTATION,BEGINLOCATION,ENDLOCATION,ISUPLOAD) values(?,?,?,?,?,?,?)",
					new Object[] { value.get("lineloopevenid"), value.get("rangeflag"),
							value.get("beginstation"), value.get("endstation"),
							value.get("beginlocation"), value.get("endlocation"),
							value.get("isupload") });
			break;
		/*
		 * 计划执行部门:DEPARTMENT, 巡检计划编号:PLANNO, 巡检类型:INSTYPE, 巡检人员类型:INSPECTORTYPE,
		 * 巡检人员:INSPECTOR, 频次:INSFREQ, 巡检工具:INSVEHICLE, 计划类型:INSPROPER,
		 * 制定依据:DETERMINANT, 巡检开始日期:INSBDATE, 巡检结束日期:INSEDATE,
		 */
		case INSPECTION_PLAN:
			db.execSQL(
					"insert into  INSPECTION_PLAN(DEPARTMENT,PLANNO,INSTYPE,INSPECTORTYPE,INSPECTOR,INSFREQ,INSVEHICLE,INSPROPER,DETERMINANT,INSBDATE,INSEDATE,INSPECTLINE,RANGEFLAG) values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("department"), value.get("planno"),
							value.get("instype"), value.get("inspectortype"),
							value.get("inspector"), value.get("insfreq"), value.get("insvehicle"),
							value.get("insproper"), value.get("determinant"),
							value.get("insbdate"), value.get("insedate"), value.get("inspectline"),
							value.get("rangeflag") });
			break;

		case NOTIFICATION:
			db.execSQL(
					"insert into  NOTIFICATION(TITLE,NOTIFATIONID,TYPE,TIME,DETAIL,ISNEW) values(?,?,?,?,?,?)",
					new Object[] { value.get("title"), value.get("notifationid"),
							value.get("type"), value.get("time"), value.get("detail"),
							value.get("isnew") });
			break;

		case KEYPOINTACQUISITION:
			db.execSQL(
					"insert into  KEYPOINTACQUISITION(GUID,USERID,NAME,LON,LAT,TIME,DEPARTMENT,LINEID,LINE,MARKERID,MARKERNAME,MARKERSTATION,"
							+ "LOCATIONDES,MILEAGE,BUFFER,VALIDITYSTART,VALIDITYEND,DESCRIPTION,ISUPLOAD,OFFSET) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { value.get("guid"), value.get("userid"), value.get("name"),
							value.get("lon"), value.get("lat"), value.get("time"),
							value.get("department"), value.get("lineid"), value.get("line"),
							value.get("markerid"), value.get("markername"),
							value.get("markerstation"), value.get("locationdes"),
							value.get("mileage"), value.get("buffer"), value.get("start"),
							value.get("end"), value.get("description"), value.get("isupload"),value.get("offset") });
			break;

		}
		db.close();
	}

	// 数据删除

	public void DBdelete(ContentValues value, int type) {
		db = dbhelp.getWritableDatabase();
		db.delete(getTableName(type), "guid=?", new String[] { value.getAsString("guid") });
	}
	public void DBdelete(String columnname, int type,String value) {
		db = dbhelp.getWritableDatabase();
		db.delete(getTableName(type), columnname+"<?", new String[] { value });
	}


	/*
	 * flag用于标识上报(upload)和更新(update),上报只修改isupload字段，更新修改数据 type定义了再哪一个表中进行修改
	 * id定义了更新数据的唯一标示 List 用于更新的数据
	 */

	public void InsertOrUpdate(ContentValues value, int type) {
		db = dbhelp.getWritableDatabase();
		String guid = value.getAsString("guid");
		String str = getTableName(type);
		Cursor cursor = db
				.rawQuery("select * from " + str + " where guid=?", new String[] { guid });
		if (cursor.getCount() > 0) {
			DBupdate(value, type);
		} else {
			DBinsert(value, type);
		}
	}

	/**
	 * 数据更新
	 * 
	 * @param value
	 * @param type
	 */
	public void DBupdate(ContentValues value, int type) {
		// TODO Auto-generated method stub
		db = dbhelp.getWritableDatabase();
		db.update(getTableName(type), value, "guid=?", new String[] { value.getAsString("guid") });
		db.close();
	}

	/**
	 * 数据更新
	 * 
	 * @param flag
	 * @param guid
	 * @param Type
	 */
	public void DBupdate(int flag, String guid, int Type) {
		db = dbhelp.getWritableDatabase();
		if (getTableName(Type) == "PROTECTIVE_POTENTIAL"
				|| getTableName(Type) == "NATURAL_POTENTIAL"
				|| getTableName(Type) == "GROUND_RESISTANCE" || getTableName(Type) == "CORROSIVE") {
			db.execSQL("UPDATE " + getTableName(Type) + " SET isupload=? where guid=?",
					new String[] { flag + "", guid });
		} else {
			db.execSQL("UPDATE " + getTableName(Type) + " SET ISUPLOAD=? where GUID=?",
					new String[] { flag + "", guid });
		}
	}

	/**
	 * 数据更新修改已寻关键点状态
	 *
	 * 姓名：段淇皓
	 * 时间：2018-3-14  15:10:10
	 * @param type
	 * @return
	 */
//	public void DBupdate(String lat ,String lon ,int type ,){
//
//	}



	public String getTableName(int type) {
		String str = "";
		switch (type) {
		case Type.PROTECTIVE_POTENTIAL:
			str = "PROTECTIVE_POTENTIAL";
			break;
		case Type.NATURAL_POTENTIAL:
			str = "NATURAL_POTENTIAL";
			break;
		case Type.GROUND_RESISTANCE:
			str = "GROUND_RESISTANCE";
			break;
		case Type.PROBLEM_UPLOAD:
			str = "PROBLEM_UPLOAD";
			break;
		case Type.CORROSIVE:
			str = "CORROSIVE";
			break;
		case Type.LINE_LOG:
			str = "LINE_LOG";
			break;
		case Type.GPS_UPLOCAD:
			str = "GPS_UPLOAD";
			break;
		case Type.ALARM_INFO:
			str = "ALARM_INFO";
			break;
		case Type.PIS_LINELOOP_INFO:
			str = "PIS_LINELOOP_INFO";
			break;
		case Type.GPS_INSTASK_DAY_MAIN:
			str = "GPS_INSTASK_DAY_MAIN";
			break;
		case Type.GPS_INSTASK_DAY:
			str = "GPS_INSTASK_DAY";
			break;
		case Type.GPS_INSTASK_DAY_POINT:
			str = "GPS_INSTASK_DAY_POINT";
			break;

		case Type.GPS_INSRECORD:
			str = "GPS_INSRECORD";
			break;
		case Type.GPS_INSRECORD_LINE:
			str = "GPS_INSRECORD_LINE";
			break;

		case Type.GPS_UNINSRECORD:
			str = "GPS_UNINSRECORD";
			break;
		case Type.GPS_UNINSRECORD_LINE:
			str = "GPS_UNINSRECORD_LINE";
			break;

		case Type.PIS_PLAN_INFO:
			str = "PIS_PLAN_INFO";
			break;
		case Type.PIS_PLAN_LINE:
			str = "PIS_PLAN_LINE";
			break;
		case Type.GPS_INSTASK_UPLOAD:
			str = "GPS_INSTASK_UPLOAD";
			break;

		case Type.KEYPOINTACQUISITION:
			str = "KEYPOINTACQUISITION";
			break;

		default:
			break;
		}
		return str;
	}

	/*
	 * 数据查询 参数是要查询的数据表 返回的是一个List数据
	 */

	public ContentValues DBselect(String str) {
		return null;
	}

	public Cursor DBselect(int type) {
		db = dbhelp.getReadableDatabase();
		Cursor cursor = null;
		switch (type) {

		case INSPECTION_PLAN:
			cursor = db.rawQuery("select * from INSPECTION_PLAN", null);
			break;
		case PROTECTIVE_POTENTIAL:

			cursor = db.rawQuery("select * from PROTECTIVE_POTENTIAL", null);
			break;

		case NATURAL_POTENTIAL:
			cursor = db.rawQuery("select * from NATURAL_POTENTIAL", null);
			break;

		case GROUND_RESISTANCE:
			// cursor=db.query("GroundResistance", new
			// String[]{"UserID,Year,Halfyear,Line,Pile,Ground,Specifiedground,Tester,Testtime,Remarks,ISUPLOAD"},
			// "Year like ? and Line like ?", new String[]{"2015%", "%二道%"},
			// null, null, "Line");
			cursor = db.rawQuery("select * from GROUND_RESISTANCE", null);
			break;

		case CORROSIVE:
			cursor = db.rawQuery("select * from CORROSIVE", null);
			break;

		case PROBLEM_UPLOAD:
			cursor = db.rawQuery("select * from PROBLEM_UPLOAD", null);
			break;

		case LINE_LOG:
			cursor = db.rawQuery("select * from LINE_LOG", null);
			break;

		case GPS_UPLOCAD:
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String thisdata = sf.format(new Date());
			Log.i("baiduhhhhhh","获取当前日期："+thisdata);
			cursor = db.rawQuery("select * from GPS_UPLOAD where DATE like '"+thisdata+"%'", null);
			break;

		case ALARM_INFO:
			cursor = db.rawQuery("select * from ALARM_INFO order by ISNEW desc", null);
			break;

		case PIS_LINELOOP_INFO:
			cursor = db.rawQuery("select * from PIS_LINELOOP_INFO", null);
			break;

		case GPS_INSTASK_DAY_MAIN:
			cursor = db.rawQuery("select * from GPS_INSTASK_DAY_MAIN", null);
			break;

		case GPS_INSTASK_DAY:
			cursor = db.rawQuery("select * from GPS_INSTASK_DAY", null);
			break;

		case GPS_INSTASK_DAY_POINT:
			cursor = db.rawQuery("select * from GPS_INSTASK_DAY_POINT", null);
			break;

		case GPS_INSTASK_UPLOAD:
			cursor = db.rawQuery("select * from GPS_INSTASK_UPLOAD", null);
			break;

		case GPS_INSRECORD:
			cursor = db.rawQuery("select * from GPS_INSRECORD", null);
			break;

		case GPS_INSRECORD_LINE:
			cursor = db.rawQuery("select * from GPS_INSRECORD_LINE", null);
			break;

		case GPS_UNINSRECORD:
			cursor = db.rawQuery("select * from GPS_UNINSRECORD", null);
			break;

		case GPS_UNINSRECORD_LINE:
			cursor = db.rawQuery("select * from GPS_UNINSRECORD_LINE", null);
			break;

		case PIS_PLAN_INFO:
			cursor = db.rawQuery("select * from PIS_PLAN_INFO", null);
			break;

		case PIS_PLAN_LINE:
			cursor = db.rawQuery("select * from PIS_PLAN_LINE", null);
			break;
		case NOTIFICATION:
			cursor = db.rawQuery("select * from NOTIFICATION", null);
			break;
		case KEYPOINTACQUISITION:
			cursor = db.rawQuery("select * from KEYPOINTACQUISITION", null);
			break;
		}
		return cursor;
	}

	public void DBupdateAlarm(int flag) {
		db = dbhelp.getWritableDatabase();

		db.execSQL("update ALARM_INFO set ISNEW = 0 where ID=?", new String[] { flag + "" });

	}

	public void DBupdateNotification(int flag, String time) {
		db = dbhelp.getWritableDatabase();
		db.execSQL("update NOTIFICATION set ISNEW = ? where TIME=?",
				new String[] { flag + "", time });
		db.close();
	}

	public Cursor DBselectByIsupload(int type) {
		db = dbhelp.getReadableDatabase();
		Cursor cursor = null;
		switch (type) {

		case PROTECTIVE_POTENTIAL:
			cursor = db.rawQuery("select * from PROTECTIVE_POTENTIAL where isupload=0", null);
			break;

		case NATURAL_POTENTIAL:
			cursor = db.rawQuery("select * from NATURAL_POTENTIAL where isupload=0", null);
			break;

		case GROUND_RESISTANCE:
			// cursor=db.query("GroundResistance", new
			// String[]{"UserID,Year,Halfyear,Line,Pile,Ground,Specifiedground,Tester,Testtime,Remarks,ISUPLOAD"},
			// "Year like ? and Line like ?", new String[]{"2015%", "%二道%"},
			// null, null, "Line");
			cursor = db.rawQuery("select * from GROUND_RESISTANCE where isupload=0", null);
			break;

		case CORROSIVE:
			cursor = db.rawQuery("select * from CORROSIVE where isupload=0", null);
			break;

		case PROBLEM_UPLOAD:
			cursor = db.rawQuery("select * from PROBLEM_UPLOAD where isupload=0", null);
			break;

		case GPS_UPLOCAD:
			// cursor=db.rawQuery("select * from GPS_UPLOCAD", null);
			cursor = db.rawQuery("select * from GPS_UPLOCAD where isupload=0", null);
			break;

		case GPS_INSTASK_UPLOAD:
			// cursor=db.rawQuery("select * from GPS_INSTASK_UPLOAD", null);
			cursor = db.rawQuery("select * from GPS_INSTASK_UPLOAD where isupload=0", null);
			break;

		case KEYPOINTACQUISITION:
			cursor = db.rawQuery("select * from KEYPOINTACQUISITION where isupload=0", null);
			break;

		}

		return cursor;
	}

	List<String> guidList = new ArrayList<String>();
	List<String> guidList1 = new ArrayList<String>();
	List<String> guidList2 = new ArrayList<String>();
	List<String> guidList3 = new ArrayList<String>();
	List<String> guidList4 = new ArrayList<String>();
	List<String> guidList5 = new ArrayList<String>();
	List<String> guidList6 = new ArrayList<String>();
	List<String> guidList7 = new ArrayList<String>();

	String PIPEEVENTID;

	public void upload() {
		db = dbhelp.getReadableDatabase();
		Request request = new Request(handler);

		Cursor cursor = db.rawQuery("select * from PROTECTIVE_POTENTIAL where isupload=?",
				new String[] { "0" });

		while (cursor.moveToNext()) {
			String guid = cursor.getString(cursor.getColumnIndex("guid"));
			String year = cursor.getString(cursor.getColumnIndex("year"));
			String month = cursor.getString(cursor.getColumnIndex("month"));
			String lineId = cursor.getString(cursor.getColumnIndex("lineid"));
			String markId = cursor.getString(cursor.getColumnIndex("pileid"));
			String value = cursor.getString(cursor.getColumnIndex("value"));
			String userid = cursor.getString(cursor.getColumnIndex("userid"));
			String testtime = cursor.getString(cursor.getColumnIndex("test_time"));
			String remarks = cursor.getString(cursor.getColumnIndex("remarks"));
			String voltage = cursor.getString(cursor.getColumnIndex("voltage"));
			String ground = cursor.getString(cursor.getColumnIndex("ground"));
			String temperature = cursor.getString(cursor.getColumnIndex("temperature"));
			String natural = cursor.getString(cursor.getColumnIndex("natural"));
			String ir = cursor.getString(cursor.getColumnIndex("ir"));
			guidList.add(guid);
			request.protectPotentialRequest(year, month, lineId, markId, value, userid, testtime,
					remarks, voltage, ground, temperature, natural, ir, "autoup");
		}
		// cursor.close();operationDB.db.close();
		// db.close();

		Cursor cursor1 = db.rawQuery("select * from NATURAL_POTENTIAL where isupload=0", null);

		while (cursor1.moveToNext()) {
			String guid = cursor1.getString(cursor1.getColumnIndex("guid"));
			String year = cursor1.getString(cursor1.getColumnIndex("year"));
			String month = cursor1.getString(cursor1.getColumnIndex("month"));
			String lineId = cursor1.getString(cursor1.getColumnIndex("lineid"));
			String markId = cursor1.getString(cursor1.getColumnIndex("pileid"));
			String value = cursor1.getString(cursor1.getColumnIndex("value"));
			String userid = cursor1.getString(cursor1.getColumnIndex("userid"));
			String testtime = cursor1.getString(cursor1.getColumnIndex("test_time"));
			String remarks = cursor1.getString(cursor1.getColumnIndex("remarks"));
			String voltage = cursor1.getString(cursor1.getColumnIndex("voltage"));
			String weather = cursor1.getString(cursor1.getColumnIndex("weather"));
			String temperature = cursor1.getString(cursor1.getColumnIndex("temperature"));
			guidList1.add(guid);
			request.naturalPotentialRequest(userid, year,month, lineId, markId, value, testtime, remarks,
					voltage, temperature, weather, "autoup");
		}
		// cursor1.close();
		// db.close();
		Cursor cursor2 = db.rawQuery("select * from GROUND_RESISTANCE where isupload=0", null);

		while (cursor2.moveToNext()) {
			String guid = cursor2.getString(cursor2.getColumnIndex("guid"));
			String cpgroundbedeventid = cursor2.getString(cursor2
					.getColumnIndex("cpgroundbedeventid"));
			String userid = cursor2.getString(cursor2.getColumnIndex("userid"));
			String test_date = cursor2.getString(cursor2.getColumnIndex("test_date"));
			String set_value = cursor2.getString(cursor2.getColumnIndex("set_value"));
			String test_value = cursor2.getString(cursor2.getColumnIndex("test_value"));
			String conclusion = cursor2.getString(cursor2.getColumnIndex("conclusion"));
			String year = cursor2.getString(cursor2.getColumnIndex("year"));
			String halfyear = cursor2.getString(cursor2.getColumnIndex("halfyear"));
			guidList2.add(guid);
			if(halfyear.equals("上半年")){
				halfyear = "01";
			}else{
				halfyear = "02";
			}
			request.groundingResistanceRequest(cpgroundbedeventid, userid, test_date, set_value,
					test_value, conclusion, year, halfyear, "autoup");
		}
		// cursor2.close();
		// db.close();
		Cursor cursor3 = db.rawQuery("select * from CORROSIVE where isupload=0", null);

		while (cursor3.moveToNext()) {
			String guid = cursor3.getString(cursor3.getColumnIndex("guid"));
			String userid = cursor3.getString(cursor3.getColumnIndex("userid"));
			String lineId = cursor3.getString(cursor3.getColumnIndex("lineid"));
			String markId = cursor3.getString(cursor3.getColumnIndex("pileid"));
			String offset = cursor3.getString(cursor3.getColumnIndex("offset"));
			String repair_obj = cursor3.getString(cursor3.getColumnIndex("repair_obj"));
			String check_date = cursor3.getString(cursor3.getColumnIndex("check_date"));
			String clockposition = cursor3.getString(cursor3.getColumnIndex("clockposition"));
			String soil = cursor3.getString(cursor3.getColumnIndex("soil"));
			String damage_des = cursor3.getString(cursor3.getColumnIndex("damage_des"));
			String area = cursor3.getString(cursor3.getColumnIndex("area"));
			String corrosion_des = cursor3.getString(cursor3.getColumnIndex("corrosion_des"));
			String corrosion_area = cursor3.getString(cursor3.getColumnIndex("corrosion_area"));
			String corrosion_num = cursor3.getString(cursor3.getColumnIndex("corrosion_num"));
			String pitdepthmax = cursor3.getString(cursor3.getColumnIndex("pitdepthmax"));
			String pitdepthmin = cursor3.getString(cursor3.getColumnIndex("pitdepthmin"));
			String repair_situation = cursor3.getString(cursor3.getColumnIndex("repair_situation"));
			String repair_date = cursor3.getString(cursor3.getColumnIndex("repair_date"));
			String damage_type = cursor3.getString(cursor3.getColumnIndex("damage_type"));
			String repair_type = cursor3.getString(cursor3.getColumnIndex("repair_type"));
			String pile_info = cursor3.getString(cursor3.getColumnIndex("pile_info"));
			String remarks = cursor3.getString(cursor3.getColumnIndex("remarks"));

			guidList3.add(guid);
			request.ntisepticRequest(userid, lineId, markId, offset, repair_obj, check_date,
					clockposition, soil, damage_des, area, corrosion_des, corrosion_area,
					corrosion_num, pitdepthmax, pitdepthmin, repair_situation, repair_date,
					damage_type, repair_type, pile_info, remarks, "autoup");
		}
		// cursor3.close();
		// db.close();
		Cursor cursor4 = db.rawQuery("select * from PROBLEM_UPLOAD where isupload=0", null);

		// public void problemUploadRequest(final String guid,final String
		// userid,final String lineid,
		// final String occurtime,final String offset,
		// final String type,final String problemdes,final List<String>
		// photopath,
		// final String departId,final String uploadTime,final String lat,
		// final String lon,final List<String> imagedes,
		// final String pileid,final String location,final String plan,final
		// String queresult)

		// String userid =
		// cursor3.getString(cursor3.getColumnIndex("PROBLEMTYPE"));
		while (cursor4.moveToNext()) {
			String guid = cursor4.getString(cursor4.getColumnIndex("GUID"));
			String userid = cursor4.getString(cursor4.getColumnIndex("USERID"));

			String occurtime = cursor4.getString(cursor4.getColumnIndex("OCCURTIME"));
			String photopathStr = cursor4.getString(cursor4.getColumnIndex("PHOTOPATH"));
			String[] paths = photopathStr.split("#");
			List<String> photopath = new ArrayList<String>();
			for (int i = 0; i < paths.length; i++) {
				photopath.add(paths[i]);
			}
			String imagedesStr = cursor4.getString(cursor4.getColumnIndex("PHOTODES"));
			String[] desc = imagedesStr.split("#");
			List<String> imagedes = new ArrayList<String>();
			for (int i = 0; i < desc.length; i++) {
				imagedes.add(desc[i]);
			}

			String lineid = cursor4.getString(cursor4.getColumnIndex("LINEID"));
			String pileid = cursor4.getString(cursor4.getColumnIndex("PILEID"));
			String offset = cursor4.getString(cursor4.getColumnIndex("OFFSET"));
			String departId = cursor4.getString(cursor4.getColumnIndex("DEPARTMENTID"));
			String uploadTime = cursor4.getString(cursor4.getColumnIndex("UPLOADTIME"));
			String problemdes = cursor4.getString(cursor4.getColumnIndex("PROBLEMDES"));
			String location = cursor4.getString(cursor4.getColumnIndex("OCCURPLACE"));
			String plan = cursor4.getString(cursor4.getColumnIndex("SOLUTION"));
			String queresult = cursor4.getString(cursor4.getColumnIndex("SITUATION"));
			String lon = cursor4.getString(cursor4.getColumnIndex("LON"));
			String lat = cursor4.getString(cursor4.getColumnIndex("LAT"));
			String type = cursor4.getString(cursor4.getColumnIndex("PROBLEMTYPE"));

			guidList4.add(guid);
			request.problemUploadRequest(guid, userid, lineid, occurtime, offset, type, problemdes,
					photopath, departId, uploadTime, lat, lon, imagedes, pileid, location, plan,
					queresult, "autoup");
			// webservice请求
			// request.problemUploadRequest(guid, userid, lineid, pileid,
			// offset,
			// type, occurtime, photopath, imagedes, problemdes,
			// uploadTime, lon, lat, departId, departId, plan, queresult);

		}

		//
		// cursor4.close();
		// db.close();
		Cursor cursor5 = db.rawQuery("select * from GPS_INSTASK_UPLOAD where isupload=0", null);

		while (cursor5.moveToNext()) {
			String guid = cursor5.getString(cursor5.getColumnIndex("GUID"));
			PIPEEVENTID = cursor5.getString(cursor5.getColumnIndex("PIPEEVENTID"));
			String ARRIIVETIME = cursor5.getString(cursor5.getColumnIndex("ARRIIVETIME"));
			String lon = cursor5.getString(cursor5.getColumnIndex("LON"));
			String lat = cursor5.getString(cursor5.getColumnIndex("LAT"));

			guidList5.add(guid);
			request.MyTaskRequest(PIPEEVENTID, ARRIIVETIME, lon, lat, "autoup");

		}
		// cursor5.close();
		// db.close();
		Cursor cursor6 = db.rawQuery("select * from LINE_LOG where isupload=0", null);

		while (cursor6.moveToNext()) {
			String guid = cursor6.getString(cursor6.getColumnIndex("GUID"));
			String weather = cursor6.getString(cursor6.getColumnIndex("WEATHER"));
			String road = cursor6.getString(cursor6.getColumnIndex("ROAD"));
			String record = cursor6.getString(cursor6.getColumnIndex("RECORD"));
			String problem = cursor6.getString(cursor6.getColumnIndex("PROBLEM"));
			String result1 = cursor6.getString(cursor6.getColumnIndex("RESULT"));
			String time = cursor6.getString(cursor6.getColumnIndex("TIME"));
			String location = cursor6.getString(cursor6.getColumnIndex("LOCATION"));
			String car = cursor6.getString(cursor6.getColumnIndex("CAR"));
			String sender = cursor6.getString(cursor6.getColumnIndex("SENDER"));
			String receiver = cursor6.getString(cursor6.getColumnIndex("RECEIVER"));
			String other = cursor6.getString(cursor6.getColumnIndex("OTHER"));
			String info = cursor6.getString(cursor6.getColumnIndex("INFO"));
			String eventid = cursor6.getString(cursor6.getColumnIndex("EVENTID"));

			guidList6.add(guid);

			request.InspectRecordRequest(guid, weather, road, record, problem, result1, time,
					location, car, sender, receiver, other, info, eventid, myApplication.imei, "autoup");

			
		}
		Cursor cursor7 = db
				.rawQuery("select * from KEYPOINTACQUISITION where isupload=0", null);

		while (cursor7.moveToNext()) {
			String guid2 = cursor7.getString(cursor7.getColumnIndex("GUID"));
			String userid = cursor7.getString(cursor7.getColumnIndex("USERID"));
			String name = cursor7.getString(cursor7.getColumnIndex("NAME"));
			String lon = cursor7.getString(cursor7.getColumnIndex("LON"));
			String lat = cursor7.getString(cursor7.getColumnIndex("LAT"));
			String time2 = cursor7.getString(cursor7.getColumnIndex("TIME"));
			String department = cursor7.getString(cursor7.getColumnIndex("DEPARTMENT"));
			String lineid = cursor7.getString(cursor7.getColumnIndex("LINEID"));
			String line = cursor7.getString(cursor7.getColumnIndex("LINE"));
			String markerid = cursor7.getString(cursor7.getColumnIndex("MARKERID"));
			String markername = cursor7.getString(cursor7.getColumnIndex("MARKERNAME"));
			String markerstation = cursor7.getString(cursor7.getColumnIndex("MARKERSTATION"));
			String locationdes = cursor7.getString(cursor7.getColumnIndex("LOCATIONDES"));
			String mileage = cursor7.getString(cursor7.getColumnIndex("MILEAGE"));
			String buffer = cursor7.getString(cursor7.getColumnIndex("BUFFER"));
			String validitystart = cursor7.getString(cursor7.getColumnIndex("VALIDITYSTART"));
			String validityend = cursor7.getString(cursor7.getColumnIndex("VALIDITYEND"));
			String description = cursor7.getString(cursor7.getColumnIndex("DESCRIPTION"));

			guidList7.add(guid2);
			request.KeypointRequest(userid, myApplication.imei, department, name, markername
					+ buffer, buffer, validitystart, validityend, String.valueOf(lon),
					String.valueOf(lat), description, lineid, mileage, "autoup");
		}
		// cursor6.close();
		db.close();

	}

	int a = 0;
	int a1 = 0;
	int a2 = 0;
	int a3 = 0;
	int a4 = 0;
	int a5 = 0;
	int a6 = 0;
	int a7 = 0;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String result = msg.getData().getString("result");
			switch (msg.arg1) {
			case 7:

				if (result.equals("OK|TRUE")) {
					ContentValues values = new ContentValues();
					values.put("guid", guidList.get(a));
					values.put("isupload", 1);
					DBupdate(values, Type.PROTECTIVE_POTENTIAL);
				}
				a++;
				break;
			case 8:

				if (result.equals("OK|TRUE")) {
					ContentValues values = new ContentValues();
					values.put("guid", guidList1.get(a1));
					values.put("isupload", 1);
					DBupdate(values, Type.NATURAL_POTENTIAL);
				}
				a1++;
				break;
			case 9:

				if (result.equals("OK|TRUE")) {
					ContentValues values = new ContentValues();
					values.put("guid", guidList2.get(a2));
					values.put("isupload", 1);
					DBupdate(values, Type.GROUND_RESISTANCE);
				}
				a2++;
				break;
			case 10:

				if (result.equals("OK|TRUE")) {
					ContentValues values = new ContentValues();
					values.put("guid", guidList3.get(a3));
					values.put("isupload", 1);
					DBupdate(values, Type.CORROSIVE);
				}
				a3++;
				break;
			case 11:

				if (result.equals("OK|TRUE")) {
					ContentValues values = new ContentValues();
					values.put("guid", guidList4.get(a4));
					values.put("isupload", 1);
					DBupdate(values, Type.PROBLEM_UPLOAD);
				}
				a4++;
				break;
			case 24:

				if (result.equals("OK|TRUE")) {
					ContentValues values = new ContentValues();
					values.put("guid", guidList6.get(a6));
					values.put("isupload", 1);
					DBupdate(values, Type.LINE_LOG);
				}
				a6++;
				break;
			case 28:

				if (result.equals("OK|TRUE")) {
					ContentValues values = new ContentValues();
					values.put("guid", guidList5.get(a5));
					values.put("isupload", 1);
					DBupdate(values, Type.GPS_INSTASK_UPLOAD);
					SharedPreferences spf;// 本地文件保存已提交的关键点EVENTID
					Editor editor;// spf的修改工具提交更新数据
					spf = context.getSharedPreferences("pile", Context.MODE_PRIVATE);// 保存关键点id的文件
					editor = spf.edit();
					String pileList = spf.getString("pileList", "");
					editor.putString("pileList", pileList + PIPEEVENTID + ";");
					editor.commit();
					String a = PIPEEVENTID + ";";
					String savepileList = spf.getString("savepileList", "");
					if (!(savepileList.equals(""))) {
						int b = a.length();
						int ab = savepileList.indexOf(a);
						if (ab != -1) {
							String c = savepileList.substring(0, ab);
							String d = savepileList.substring(ab + b, savepileList.length());
							editor.putString("savepileList", c + d);
							editor.commit();
						}
					}

				}
				a5++;
				break;

			case 58:

				if (result.equals("OK|TRUE")) {
					ContentValues values = new ContentValues();
					values.put("guid", guidList7.get(a7));
					values.put("isupload", 1);
					DBupdate(values, Type.KEYPOINTACQUISITION);
				}
				a7++;
				break;

			}
		}

	};

}
