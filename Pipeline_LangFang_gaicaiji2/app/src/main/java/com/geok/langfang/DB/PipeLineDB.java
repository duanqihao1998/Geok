package com.geok.langfang.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author Administrator PipeLineDB.java数据库 类 新建数据库，定义数据库各个表结构及字段属性等
 * 
 */
public class PipeLineDB extends SQLiteOpenHelper {

	SQLiteDatabase db;

	public PipeLineDB(Context context) {
		super(context, SQLiteDateBaseConfig.GetDataBaseName(), null, SQLiteDateBaseConfig
				.GetVersion());
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建gps桩
		StringBuffer gps_marker = new StringBuffer();
		gps_marker.append("		Create  TABLE GPS_MARKER(");
		gps_marker.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		gps_marker.append("				,[markerid] varchar(50)");
		gps_marker.append("				,[lineloopid] varchar(50)");
		gps_marker.append("				,[markername] varchar(100)");
		gps_marker.append("				,[markerstation] varchar(50)");
		gps_marker.append("				,[markertype] varchar(50)");
		gps_marker.append("				,[lon] varchar(50)");
		gps_marker.append("				,[lat] varchar(50)");
		gps_marker.append("				,[remark] varchar(500)");
		gps_marker.append("				,[active] varchar(50)");
		gps_marker.append("				)");
		db.execSQL(gps_marker.toString());

		// 创建保护电位的数据库
		StringBuffer _protectivepotential = new StringBuffer();
		_protectivepotential.append("		Create  TABLE PROTECTIVE_POTENTIAL(");
		_protectivepotential.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_protectivepotential.append("				,[guid] varchar(50)");
		_protectivepotential.append("				,[userid] varchar(50)");
		_protectivepotential.append("				,[year] varchar(50)");
		_protectivepotential.append("				,[month] varchar(50)");
		_protectivepotential.append("				,[line] varchar(50)");
		_protectivepotential.append("				,[pile] varchar(50)");
		_protectivepotential.append("				,[value] varchar(50)");
		_protectivepotential.append("				,[test_time] varchar(50)");
		_protectivepotential.append("				,[remarks] varchar(200)");
		_protectivepotential.append("				,[voltage] varchar(50)");
		_protectivepotential.append("				,[ground] varchar(50)");
		_protectivepotential.append("				,[temperature] varchar(50)");
		_protectivepotential.append("				,[lineid] varchar(50)");
		_protectivepotential.append("				,[pileid] varchar(50)");
		_protectivepotential.append("				,[natural] varchar(50)");
		_protectivepotential.append("				,[ir] varchar(50)");
		_protectivepotential.append("				,[isupload] int(50)");
		_protectivepotential.append("				)");
		db.execSQL(_protectivepotential.toString());

		// 创建自然电位的数据表
		StringBuffer _naturalpotential = new StringBuffer();
		_naturalpotential.append("		Create  TABLE NATURAL_POTENTIAL(");
		_naturalpotential.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_naturalpotential.append("				,[guid] varchar(50)");
		_naturalpotential.append("				,[userid] varchar(50)");
		_naturalpotential.append("				,[year] varchar(50)");
		_naturalpotential.append("				,[month] varchar(50)");
		_naturalpotential.append("				,[line] varchar(50)");
		_naturalpotential.append("				,[pile] varchar(50)");
		_naturalpotential.append("				,[value] varchar(50)");
		_naturalpotential.append("				,[test_time] varchar(50)");
		_naturalpotential.append("				,[remarks] varchar(200)");
		_naturalpotential.append("				,[voltage] varchar(50)");
		_naturalpotential.append("				,[temperature] varchar(50)");
		_naturalpotential.append("				,[weather] varchar(50)");
		_naturalpotential.append("				,[lineid] varchar(50)");
		_naturalpotential.append("				,[pileid] varchar(50)");
		_naturalpotential.append("				,[isupload] int(50)");
		_naturalpotential.append("				)");
		db.execSQL(_naturalpotential.toString());

		// 接地电阻的数据表
		StringBuffer _groundresistance = new StringBuffer();
		_groundresistance.append("		Create  TABLE GROUND_RESISTANCE(");
		_groundresistance.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_groundresistance.append("				,[guid] varchar(50)");
		_groundresistance.append("				,[cpgroundbedeventid] varchar(50)");
		_groundresistance.append("				,[userid] varchar(50)");
		_groundresistance.append("              ,[test_date] varchar(50)");
		_groundresistance.append("				,[set_value] number(50)");
		_groundresistance.append("				,[test_value] number(50)");
		_groundresistance.append("				,[conclusion] varchar(100)");
		_groundresistance.append("				,[year] varchar(50)");
		_groundresistance.append("				,[halfyear] varchar(50)");
		_groundresistance.append("				,[isupload] int(50)");
		_groundresistance.append("				)");
		db.execSQL(_groundresistance.toString());

		// 防腐侧漏的数据表
		StringBuffer _corrosive = new StringBuffer();
		_corrosive.append("		Create  TABLE CORROSIVE(");
		_corrosive.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_corrosive.append("				,[guid] varchar(50)");
		_corrosive.append("				,[userid] varchar(50)");
		_corrosive.append("             ,[line] varchar(50)");
		_corrosive.append("				,[pile] varchar(50)");
		_corrosive.append("				,[offset] varchar(50)");
		_corrosive.append("				,[repair_obj] varchar(50)");
		_corrosive.append("				,[check_date] varchar(50)");
		_corrosive.append("				,[clockposition] varchar(50)");
		_corrosive.append("				,[soil] varchar(50)");
		_corrosive.append("				,[damage_des] varchar(50)");
		_corrosive.append("				,[area] varchar(50)");
		_corrosive.append("				,[corrosion_des] varchar(50)");
		_corrosive.append("				,[corrosion_area] varchar(50)");
		_corrosive.append("             ,[corrosion_num] varchar(50)");
		_corrosive.append("				,[pitdepthmax] varchar(50)");
		_corrosive.append("				,[pitdepthmin] varchar(50)");
		_corrosive.append("				,[repair_situation] varchar(50)");
		_corrosive.append("				,[repair_date] varchar(50)");
		_corrosive.append("				,[damage_type] varchar(50)");
		_corrosive.append("				,[repair_type] varchar(200)");
		_corrosive.append("				,[pile_info] varchar(50)");
		_corrosive.append("				,[remarks] varchar(50)");
		_corrosive.append("				,[lineid] varchar(50)");
		_corrosive.append("				,[pileid] varchar(50)");
		_corrosive.append("				,[isupload] int(50)");
		_corrosive.append("				)");
		db.execSQL(_corrosive.toString());

		/*
		 * guid 问题类型 50 经度 50 维度 50 发生时间和日期 100 照片地址 500 照片描述 100 线路 50 线路id 50
		 * 桩 50 桩id 50 偏移量 50 用户id 50 上报时间 和日期 100 问题描述 200
		 */

		// 事项(问题)上报数据表
		StringBuffer _problemupload = new StringBuffer();
		_problemupload.append("		Create  TABLE PROBLEM_UPLOAD(");
		_problemupload.append("				[id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_problemupload.append("				,[PROBLEMTYPE] varchar(50)");
		_problemupload.append("				,[GUID] varchar(50)");
		_problemupload.append("				,[LON] varchar(50)");
		_problemupload.append("				,[LAT] varchar(50)");
		_problemupload.append("				,[OCCURTIME] varchar(100)");
		_problemupload.append("				,[PHOTOPATH] varchar(500)");
		_problemupload.append("				,[PHOTODES] varchar(100)");
		_problemupload.append("				,[LINE] varchar(50)");
		_problemupload.append("				,[LINEID] varchar(50)");
		_problemupload.append("				,[PILE] varchar(50)");
		_problemupload.append("				,[PILEID] varchar(50)");
		_problemupload.append("				,[OFFSET] varchar(50)");
		_problemupload.append("				,[USERID] varchar(50)");
		_problemupload.append("				,[DEPARTMENTID] varchar(50)");
		_problemupload.append("				,[UPLOADTIME] varchar(100)");
		_problemupload.append("				,[PROBLEMDES] varchar(50)");
		_problemupload.append("				,[OCCURPLACE] varchar(50)");
		_problemupload.append("				,[SOLUTION] varchar(50)");
		_problemupload.append("				,[SITUATION] varchar(50)");
		_problemupload.append("				,[ISUPLOAD] int(50)");
		_problemupload.append("				)");
		db.execSQL(_problemupload.toString());

		// 巡线日志上报数据表
		StringBuffer _linelog = new StringBuffer();
		_linelog.append("		Create  TABLE LINE_LOG(");
		_linelog.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_linelog.append("				,[GUID] varchar(50)");
		_linelog.append("				,[BEGINTIME] varchar(50)");
		_linelog.append("            	,[ENDTIME] varchar(50)");
		_linelog.append("				,[TYPE] varchar(50) ");
		_linelog.append("				,[TESTER] varchar(50) ");
		_linelog.append("				,[TOOLS] varchar(50) ");
		_linelog.append("				,[INSPFREQ] varchar(50) ");
		_linelog.append("				,[YEILD] varchar(50) ");
		_linelog.append("				,[DEVICE] varchar(50)");
		_linelog.append("				,[POINTS] varchar(50) ");
		_linelog.append("				,[SPEED] varchar(100) ");
		_linelog.append("				,[WEATHER] varchar(50) ");
		_linelog.append("				,[ROAD] varchar(50) ");
		_linelog.append("				,[RECORD] varchar(50) ");
		_linelog.append("				,[PROBLEM] varchar(100) ");
		_linelog.append("				,[RESULT] varchar(100) ");
		_linelog.append("				,[TIME] varchar(50) ");
		_linelog.append("				,[LOCATION] varchar(50)");
		_linelog.append("				,[CAR] varchar(50) ");
		_linelog.append("				,[SENDER] varchar(50)");
		_linelog.append("				,[RECEIVER] varchar(50)");
		_linelog.append("				,[OTHER] varchar(50) ");
		_linelog.append("				,[ISUPLOAD] int(50) ");
		_linelog.append("				,[INFO] int(1000) ");
		_linelog.append("				,[EVENTID] int(50) ");
		_linelog.append("				)");
		db.execSQL(_linelog.toString());

		// GPS实时上报的数据表
		StringBuffer _gpsupload = new StringBuffer();
		_gpsupload.append("		Create  TABLE GPS_UPLOAD(");
		_gpsupload.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_gpsupload.append("				,[GUID] varchar(50)");
		_gpsupload.append("				,[USERID] varchar(50)");
		_gpsupload.append("				,[LON] varchar(50)");
		_gpsupload.append("				,[LAT] varchar(50)");
		_gpsupload.append("				,[DATE] varchar(50)");
		_gpsupload.append("				,[POINTCOUNT] varchar(50)");
		_gpsupload.append("				,[ISUPLOAD] int(50)");
		_gpsupload.append("				)");
		db.execSQL(_gpsupload.toString());
		// 报警信息的数据表
		StringBuffer _alarm_info = new StringBuffer();
		_alarm_info.append("		Create  TABLE ALARM_INFO(");
		_alarm_info.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_alarm_info.append("				,[ALARMTYPE] varchar(100)");
		_alarm_info.append("				,[DEPARTMENTID] varchar(100)");
		_alarm_info.append("				,[MAXSPEED] varchar(100)");
		_alarm_info.append("				,[REALSPEED] varchar(100)");
		_alarm_info.append("				,[MAXOFFSET] varchar(100)");
		_alarm_info.append("          	 	,[REALOFFSET] varchar(100)");
		_alarm_info.append("				,[DROPPEDINTERVAL] varchar(50)");
		_alarm_info.append("				,[ALARMLOCATION] varchar(200)");
		_alarm_info.append("				,[ALARMTIME] varchar(100)");
		_alarm_info.append("				,[USERID] varchar(50)");
		_alarm_info.append("				,[ISNEW] int(50)");
		_alarm_info.append("				)");
		db.execSQL(_alarm_info.toString());

		// //报警信息的数据表
		// StringBuffer _alarm_info=new StringBuffer();
		// _alarm_info.append("		Create  TABLE ALARM_INFO(");
		// _alarm_info.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		// _alarm_info.append("				,[ALARMTYPE] varchar(50)");
		// _alarm_info.append("           	    ,[DEVICEID] varchar(50)");
		// _alarm_info.append("				,[INSPECTORID] varchar(50)");
		// _alarm_info.append("				,[PAR] varchar(50)");
		// _alarm_info.append("				,[LON] varchar(50)");
		// _alarm_info.append("				,[LAT] varchar(50)");
		// _alarm_info.append("				,[DEPARTMENTID] varchar(50)");
		// _alarm_info.append("				,[MAXSPEED] varchar(50)");
		// _alarm_info.append("				,[REALSPEED] varchar(50)");
		// _alarm_info.append("				,[MAXOFFSET] varchar(50)");
		// _alarm_info.append("          	 	,[REALOFFSET] varchar(50)");
		// _alarm_info.append("				,[DROPPEDINTERVAL] varchar(50)");
		// _alarm_info.append("				,[ALARMLOCATION] varchar(50)");
		// _alarm_info.append("				,[ISDOWN] varchar(50)");
		// _alarm_info.append("				,[USERID] varchar(50)");
		// _alarm_info.append("				,[ISUPLOAD] int(50)");
		// _alarm_info.append("				)");
		// db.execSQL(_alarm_info.toString());

		// 管线信息查询表
		StringBuffer _pis_lineloop_info = new StringBuffer();
		_pis_lineloop_info.append("		Create  TABLE PIS_LINELOOP_INFO(");
		_pis_lineloop_info.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_pis_lineloop_info.append("				,[LINELOOPID] varchar(50)");
		_pis_lineloop_info.append("           	,[MEDIUMTYPE] varchar(50)");
		_pis_lineloop_info.append("				,[LINETYPE] varchar(50)");
		_pis_lineloop_info.append("				,[LENGTH] varchar(50)");
		_pis_lineloop_info.append("				,[DIAMETER] varchar(50)");
		_pis_lineloop_info.append("				,[WALLTHICKNESS] varchar(50)");
		_pis_lineloop_info.append("				,[DESIGNPRESS] varchar(50)");
		_pis_lineloop_info.append("				,[ANTISEPSISCONDITION] varchar(50)");
		_pis_lineloop_info.append("				,[PIPE] varchar(50)");
		_pis_lineloop_info.append("				,[STATIONNUM] varchar(50)");
		_pis_lineloop_info.append("          	,[GASCOMPRESSION] varchar(50)");
		_pis_lineloop_info.append("				,[PUMPSTATION] varchar(50)");
		_pis_lineloop_info.append("				,[LINETRUNCATIONVALUEROOM] varchar(50)");
		_pis_lineloop_info.append("				,[PIGSTATIONS] varchar(50)");
		_pis_lineloop_info.append("          	,[ORIGINALFIXEDASSETS] varchar(50)");
		_pis_lineloop_info.append("				,[LINENUMBER] varchar(50)");
		_pis_lineloop_info.append("          	,[LON] varchar(50)");
		_pis_lineloop_info.append("				,[LAT] varchar(50)");
		_pis_lineloop_info.append("				,[USERID] varchar(50)");
		_pis_lineloop_info.append("				,[ISUPLOAD] int(50)");
		_pis_lineloop_info.append("				)");
		db.execSQL(_pis_lineloop_info.toString());

		// 每日巡检任务主表
		StringBuffer _gps_instask_day_main = new StringBuffer();
		_gps_instask_day_main.append("		Create  TABLE GPS_INSTASK_DAY_MAIN(");
		_gps_instask_day_main.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_gps_instask_day_main.append("				,[INSDATE] varchar(50)");
		_gps_instask_day_main.append("           	,[INSPECTORID] varchar(50)");
		_gps_instask_day_main.append("				,[BEGININSBT] varchar(50)");
		_gps_instask_day_main.append("				,[ENDINSET] varchar(50)");
		_gps_instask_day_main.append("				,[TASKCOUNT] varchar(50)");
		_gps_instask_day_main.append("				,[ACTIVE] varchar(50)");
		_gps_instask_day_main.append("				,[CREATEBY] varchar(50)");
		_gps_instask_day_main.append("				,[CREATEDATE] varchar(50)");
		_gps_instask_day_main.append("				,[MODIFYBY] varchar(50)");
		_gps_instask_day_main.append("				,[MODIFYDATE] varchar(50)");
		_gps_instask_day_main.append("				,[ISUPLOAD] int(50)");
		_gps_instask_day_main.append("				)");
		db.execSQL(_gps_instask_day_main.toString());

		// 每日巡检任务表
		StringBuffer _gps_instask_day = new StringBuffer();
		_gps_instask_day.append("		Create  TABLE GPS_INSTASK_DAY(");
		_gps_instask_day.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_gps_instask_day.append("				,[MAINID] varchar(50)");
		_gps_instask_day.append("           	,[PLANID] varchar(50)");
		_gps_instask_day.append("				,[NAME] varchar(50)");
		_gps_instask_day.append("				,[TASKINDEX] varchar(50)");
		_gps_instask_day.append("				,[LINELOOPEVENTID] varchar(50)");
		_gps_instask_day.append("				,[BEGINSTATION] varchar(50)");
		_gps_instask_day.append("				,[ENDSTATION] varchar(50)");
		_gps_instask_day.append("				,[TASKLOCATION] varchar(50)");
		_gps_instask_day.append("				,[BEGINTIME] varchar(50)");
		_gps_instask_day.append("				,[ENDTIME] varchar(50)");
		_gps_instask_day.append("          		,[FREQVALUE] varchar(50)");
		_gps_instask_day.append("				,[FREQTEXT] varchar(50)");
		_gps_instask_day.append("				,[ISUPLOAD] int(50)");
		_gps_instask_day.append("				)");
		db.execSQL(_gps_instask_day.toString());

		// 每日巡检任务巡检点子表
		StringBuffer _gps_instask_day_point = new StringBuffer();
		_gps_instask_day_point.append("		Create  TABLE GPS_INSTASK_DAY_POINT(");
		_gps_instask_day_point.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_gps_instask_day_point.append("				,[FREQINDEX] varchar(50)");
		_gps_instask_day_point.append("           	,[TASKID] varchar(50)");
		_gps_instask_day_point.append("				,[POINTID] varchar(50)");
		_gps_instask_day_point.append("				,[POINTNAME] varchar(50)");
		_gps_instask_day_point.append("				,[POINTTYPE] varchar(50)");
		_gps_instask_day_point.append("				,[RULETIME] varchar(50)");
		_gps_instask_day_point.append("				,[ARRTIME] varchar(50)");
		_gps_instask_day_point.append("				,[LON] varchar(50)");
		_gps_instask_day_point.append("				,[LAT] varchar(50)");
		_gps_instask_day_point.append("				,[ARRLON] varchar(50)");
		_gps_instask_day_point.append("          	,[ARRLAT] varchar(50)");
		_gps_instask_day_point.append("				,[BUFFERRANGE] varchar(50)");
		_gps_instask_day_point.append("				,[ISUPLOAD] int(50)");
		_gps_instask_day_point.append("				)");
		db.execSQL(_gps_instask_day_point.toString());

		// 巡检任务提交表
		StringBuffer _gps_instask_upload = new StringBuffer();
		_gps_instask_upload.append("		Create  TABLE GPS_INSTASK_UPLOAD(");
		_gps_instask_upload.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_gps_instask_upload.append("				,[GUID] varchar(50)");
		_gps_instask_upload.append("				,[PIPEEVENTID] varchar(50)");
		_gps_instask_upload.append("           		,[ARRIIVETIME] varchar(50)");
		_gps_instask_upload.append("				,[LON] varchar(50)");
		_gps_instask_upload.append("				,[LAT] varchar(50)");
		_gps_instask_upload.append("				,[ISUPLOAD] int(50)");
		_gps_instask_upload.append("				)");
		db.execSQL(_gps_instask_upload.toString());

		// 巡检记录的数据表
		StringBuffer _gps_insrecord = new StringBuffer();
		_gps_insrecord.append("		Create  TABLE GPS_INSRECORD(");
		_gps_insrecord.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_gps_insrecord.append("				,[DEPARTMENTID] varchar(50)");
		_gps_insrecord.append("              ,[BEGINDATETIME] varchar(50)");
		_gps_insrecord.append("				,[ENDDATETIME] varchar(50)");
		_gps_insrecord.append("				,[INSTYPE] varchar(50)");
		_gps_insrecord.append("				,[INSPECTORID] varchar(50)");
		_gps_insrecord.append("				,[INSVEHICLE] varchar(50)");
		_gps_insrecord.append("				,[INSFREQ] varchar(50)");
		_gps_insrecord.append("				,[INSYIELD] varchar(50)");
		_gps_insrecord.append("				,[TRACKPOINTS] varchar(50)");
		_gps_insrecord.append("				,[INSDEVICE] varchar(50)");
		_gps_insrecord.append("             ,[AVGSPEED] varchar(50)");
		_gps_insrecord.append("				,[WEATHER] varchar(50)");
		_gps_insrecord.append("				,[RODESTATUS] varchar(50)");
		_gps_insrecord.append("				,[ADVERSARIA] varchar(50)");
		_gps_insrecord.append("				,[PROBLEM] varchar(50)");
		_gps_insrecord.append("				,[DEALWITH] varchar(50)");
		_gps_insrecord.append("				,[SHIFTTIME] varchar(50)");
		_gps_insrecord.append("				,[VEHICLE] varchar(50)");
		_gps_insrecord.append("				,[SHIFTFROM] varchar(50)");
		_gps_insrecord.append("              ,[SHIFTTO] varchar(50)");
		_gps_insrecord.append("				,[OTHERADVERSARIA] varchar(50)");
		_gps_insrecord.append("				,[REPORTBY] varchar(50)");
		_gps_insrecord.append("				,[PLANID] varchar(50)");
		_gps_insrecord.append("				,[FLAG] varchar(50)");
		_gps_insrecord.append("				,[ACTIVE] varchar(50)");
		_gps_insrecord.append("				,[CREATEDATE] varchar(50)");
		_gps_insrecord.append("				,[ISUPLOAD] int(50)");
		_gps_insrecord.append("				)");
		db.execSQL(_gps_insrecord.toString());

		// 巡检记录线路子表
		StringBuffer _gps_insrecord_line = new StringBuffer();
		_gps_insrecord_line.append("		Create  TABLE GPS_INSRECORD_LINE(");
		_gps_insrecord_line.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_gps_insrecord_line.append("				,[LINELOOPEVENID] varchar(50)");
		_gps_insrecord_line.append("           	    ,[RANGEFLAG] varchar(50)");
		_gps_insrecord_line.append("				,[BEGINSTATION] varchar(50)");
		_gps_insrecord_line.append("				,[ENDSTATION] varchar(50)");
		_gps_insrecord_line.append("				,[SLENGTH] varchar(50)");
		_gps_insrecord_line.append("				,[RLENGTH] varchar(50)");
		_gps_insrecord_line.append("				,[SKEYPOINT] varchar(50)");
		_gps_insrecord_line.append("				,[RKEYPOINT] varchar(50)");
		_gps_insrecord_line.append("				,[BEGINLOCATION] varchar(50)");
		_gps_insrecord_line.append("				,[ENDLOCATION] varchar(50)");
		_gps_insrecord_line.append("				,[ISUPLOAD] int(50)");
		_gps_insrecord_line.append("				)");
		db.execSQL(_gps_insrecord_line.toString());

		// 未巡检记录表
		StringBuffer _gps_uninsrecord = new StringBuffer();
		_gps_uninsrecord.append("		Create  TABLE GPS_UNINSRECORD(");
		_gps_uninsrecord.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_gps_uninsrecord.append("				,[BEGINTIME] varchar(50)");
		_gps_uninsrecord.append("           	,[ENDTIME] varchar(50)");
		_gps_uninsrecord.append("				,[NOTPECTOR] varchar(50)");
		_gps_uninsrecord.append("				,[NOTREASON] varchar(50)");
		_gps_uninsrecord.append("				,[NOTREMARK] varchar(50)");
		_gps_uninsrecord.append("				,[REPORTDATE] varchar(50)");
		_gps_uninsrecord.append("				,[REPORTBY] varchar(50)");
		_gps_uninsrecord.append("				,[DEPARTMENTID] varchar(50)");
		_gps_uninsrecord.append("				,[ACTIVE] varchar(50)");
		_gps_uninsrecord.append("				,[CREATEBY] varchar(50)");
		_gps_uninsrecord.append("          		,[CREATEDATE] varchar(50)");
		_gps_uninsrecord.append("				,[MODIFYBY] varchar(50)");
		_gps_uninsrecord.append("				,[MODIFYDATE] varchar(50)");
		_gps_uninsrecord.append("				,[ISUPLOAD] int(50)");
		_gps_uninsrecord.append("				)");
		db.execSQL(_gps_uninsrecord.toString());

		// 未巡检记录线路子表
		StringBuffer _gps_uninsrecord_line = new StringBuffer();
		_gps_uninsrecord_line.append("		Create  TABLE GPS_UNINSRECORD_LINE(");
		_gps_uninsrecord_line.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_gps_uninsrecord_line.append("				,[PLANEVENTID] varchar(50)");
		_gps_uninsrecord_line.append("				,[LINELOOPEVENTID] varchar(50)");
		_gps_uninsrecord_line.append("           	,[RANGEFLAG] varchar(50)");
		_gps_uninsrecord_line.append("				,[BEGINSTATION] varchar(50)");
		_gps_uninsrecord_line.append("				,[ENDSTATION] varchar(50)");
		_gps_uninsrecord_line.append("				,[SLENGTH] varchar(50)");
		_gps_uninsrecord_line.append("				,[RLENGTH] varchar(50)");
		_gps_uninsrecord_line.append("				,[BEGINLOCATION] varchar(50)");
		_gps_uninsrecord_line.append("				,[ENDLOCATION] varchar(50)");
		_gps_uninsrecord_line.append("				,[ISUPLOAD] int(50)");
		_gps_uninsrecord_line.append("				)");
		db.execSQL(_gps_uninsrecord_line.toString());

		// 巡检计划表
		StringBuffer _pis_plan_info = new StringBuffer();
		_pis_plan_info.append("		Create  TABLE PIS_PLAN_INFO(");
		_pis_plan_info.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_pis_plan_info.append("				,[PLANNO] varchar(50)");
		_pis_plan_info.append("             ,[INSTYPE] varchar(50)");
		_pis_plan_info.append("				,[EXECUNITID] varchar(50)");
		_pis_plan_info.append("				,[INSPECTORTYPE] varchar(50)");
		_pis_plan_info.append("				,[INSFREQ] varchar(50)");
		_pis_plan_info.append("				,[INSFREQUNIT] varchar(50)");
		_pis_plan_info.append("				,[INSFREQUNITVAL] varchar(50)");
		_pis_plan_info.append("				,[INSVEHICLE] varchar(50)");
		_pis_plan_info.append("             ,[INSPECTORID] varchar(50)");
		_pis_plan_info.append("				,[INSPECTOR] varchar(50)");
		_pis_plan_info.append("				,[INSPROPER] varchar(50)");
		_pis_plan_info.append("				,[BEGININSBDATE] varchar(50)");
		_pis_plan_info.append("				,[ENDINSEDATE] varchar(50)");
		_pis_plan_info.append("				,[DETERMINANT] varchar(50)");
		_pis_plan_info.append("				,[REPEALDATE] varchar(50)");
		_pis_plan_info.append("				,[PLANFLAG] varchar(50)");
		_pis_plan_info.append("				,[DEPARTMENTID] varchar(50)");
		_pis_plan_info.append("             ,[ACTIVE] varchar(50)");
		_pis_plan_info.append("				,[CREATEDATE] varchar(50)");
		_pis_plan_info.append("				,[ISUPLOAD] int(50)");
		_pis_plan_info.append("				)");
		db.execSQL(_pis_plan_info.toString());

		// 巡检计划线路子表
		StringBuffer _pis_plan_line = new StringBuffer();
		_pis_plan_line.append("		Create  TABLE PIS_PLAN_LINE(");
		_pis_plan_line.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_pis_plan_line.append("				,[LINELOOPEVENTID] varchar(50)");
		_pis_plan_line.append("             ,[RANGEFLAG] varchar(50)");
		_pis_plan_line.append("				,[BEGINSTATION] varchar(50)");
		_pis_plan_line.append("				,[ENDSTATION] varchar(50)");
		_pis_plan_line.append("				,[BEGINLOCATION] varchar(50)");
		_pis_plan_line.append("				,[ENDLOCATION] varchar(50)");
		_pis_plan_line.append("				,[ISUPLOAD] int(50)");
		_pis_plan_line.append("				)");
		db.execSQL(_pis_plan_line.toString());

		/*
		 * @auther sunshihai
		 * 
		 * @date 2013-2-20 巡检计划数据库 计划执行部门:DEPARTMENT, 巡检计划编号:PLANNO,
		 * 巡检类型:INSTYPE, 巡检人员类型:INSPECTORTYPE, 巡检人员:INSPECTOR, 频次:INSFREQ,
		 * 巡检工具:INSVEHICLE, 计划类型:INSPROPER, 制定依据:DETERMINANT, 巡检开始日期:INSBDATE,
		 * 巡检结束日期:INSEDATE, 线路信息：InspectionPlanLineInfo
		 */
		StringBuffer __inspection_plan = new StringBuffer();
		__inspection_plan.append("		Create  TABLE INSPECTION_PLAN(");
		__inspection_plan.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		__inspection_plan.append("				,[DEPARTMENT] varchar(50)");
		__inspection_plan.append("              ,[PLANNO] varchar(50)");
		__inspection_plan.append("				,[INSTYPE] varchar(50)");
		__inspection_plan.append("				,[INSPECTORTYPE] varchar(50)");
		__inspection_plan.append("				,[INSPECTOR] varchar(50)");
		__inspection_plan.append("				,[INSFREQ] varchar(50)");
		__inspection_plan.append("				,[INSVEHICLE] varchar(50)");
		__inspection_plan.append("				,[INSPROPER] varchar(50)");
		__inspection_plan.append("				,[DETERMINANT] varchar(50)");
		__inspection_plan.append("				,[INSBDATE] varchar(50)");
		__inspection_plan.append("				,[INSEDATE] varchar(50)");
		__inspection_plan.append("				,[INSPECTLINE] varchar(1000)");
		__inspection_plan.append("				,[RANGEFLAG] varchar(1000)");
		__inspection_plan.append("				)");
		db.execSQL(__inspection_plan.toString());

		/*
		 * @auther sunshihai
		 * 
		 * @date 2013-2-20 巡检计划巡检线路数据库 巡检计划编号:PLANNO, 巡检范围:RANGEFLAG,
		 * 巡检线路信息:INSPECTLINE
		 */

		StringBuffer __inspection_plan_line = new StringBuffer();
		__inspection_plan_line.append("		Create  TABLE INSPECTION_PLAN_LINE(");
		__inspection_plan_line.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		__inspection_plan_line.append("				,[PLANNO] varchar(50)");
		__inspection_plan_line.append("             ,[RANGEFLAG] varchar(50)");
		__inspection_plan_line.append("				,[INSPECTLINE] varchar(50)");
		__inspection_plan_line.append("				)");
		db.execSQL(__inspection_plan_line.toString());

		StringBuffer __notification = new StringBuffer();
		__notification.append("		Create TABLE NOTIFICATION(");
		__notification.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		__notification.append(" ,[TITLE] varchar(50)");
		__notification.append(" ,[NOTIFATIONID] varchar(50)");
		__notification.append(" ,[TYPE] varchar(50)");
		__notification.append(" ,[TIME] varchar(50)");
		__notification.append(" ,[DETAIL] varchar(50)");
		__notification.append(" ,[ISNEW] varchar(50)");
		__notification.append("				)");
		db.execSQL(__notification.toString());

		StringBuffer _keypoint = new StringBuffer();
		_keypoint.append("Create TABLE KEYPOINTACQUISITION(");
		_keypoint.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		_keypoint.append(", [GUID] varchar(50)");
		_keypoint.append(", [USERID] varchar(50)");
		_keypoint.append(", [NAME] varchar(50)");
		_keypoint.append(", [LON] varchar(30)");// 经度
		_keypoint.append(", [LAT] varchar(30)");// 纬度
		_keypoint.append(", [TIME] varchar(50)");
		_keypoint.append(", [DEPARTMENT] varchar(50)");
		_keypoint.append(", [LINEID] varchar(50)");
		_keypoint.append(", [LINE] varchar(50)");
		_keypoint.append(", [MARKERID] varchar(50)");
		_keypoint.append(", [MARKERNAME] varchar(50)");
		_keypoint.append(", [MARKERSTATION] varchar(50)");
		_keypoint.append(", [LOCATIONDES] varchar(50)");
		_keypoint.append(", [MILEAGE] varchar(50)");
		_keypoint.append(", [BUFFER] varchar(50)");
		_keypoint.append(", [VALIDITYSTART] varchar(50)");// 有效期起始日期
		_keypoint.append(", [VALIDITYEND] varchar(50)");
		_keypoint.append(", [DESCRIPTION] varchar(50)");
		_keypoint.append(", [ISUPLOAD] varchar(10)");
		_keypoint.append(", [OFFSET] varchar(50)");
		_keypoint.append(" )");
		db.execSQL(_keypoint.toString());
		// StringBuffer _keypoint = new StringBuffer();
		// _keypoint.append("Create TABLE KEYPOINT(");
		// _keypoint.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		// _keypoint.append(", [USERID] varchar(50)");
		// _keypoint.append(", [NAME] varchar(50)");
		// _keypoint.append(", [LON] varchar(30)");//经度
		// _keypoint.append(", [LAT] varchar(30)");//纬度
		// _keypoint.append(", [TIME] varchar(50)");
		// _keypoint.append(", [DESCRIPTION] varchar(50)");
		// _keypoint.append(", [ISUPLOAD] varchar(10)");
		// _keypoint.append(" )");
		// db.execSQL(_keypoint.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1) {
			StringBuffer _keypoint = new StringBuffer();
			_keypoint.append("Create TABLE KEYPOINTACQUISITION(");
			_keypoint.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
			_keypoint.append(", [GUID] varchar(50)");
			_keypoint.append(", [USERID] varchar(50)");
			_keypoint.append(", [NAME] varchar(50)");
			_keypoint.append(", [LON] varchar(30)");// 经度
			_keypoint.append(", [LAT] varchar(30)");// 纬度
			_keypoint.append(", [TIME] varchar(50)");
			_keypoint.append(", [DEPARTMENT] varchar(50)");
			_keypoint.append(", [LINEID] varchar(50)");
			_keypoint.append(", [LINE] varchar(50)");
			_keypoint.append(", [MARKERID] varchar(50)");
			_keypoint.append(", [MARKERNAME] varchar(50)");
			_keypoint.append(", [MARKERSTATION] varchar(50)");
			_keypoint.append(", [LOCATIONDES] varchar(50)");
			_keypoint.append(", [MILEAGE] varchar(50)");
			_keypoint.append(", [BUFFER] varchar(50)");
			_keypoint.append(", [VALIDITYSTART] varchar(50)");// 有效期起始日期
			_keypoint.append(", [VALIDITYEND] varchar(50)");
			_keypoint.append(", [DESCRIPTION] varchar(50)");
			_keypoint.append(", [ISUPLOAD] varchar(10)");
			_keypoint.append(" )");
			db.execSQL(_keypoint.toString());
			db.execSQL("alter table KEYPOINT rename KEYPOINT_TEST");
			db.execSQL("insert into KEYPOINTACQUISITION select *,'' KEYPOINT_TEST");
			db.execSQL("drop table KEYPOINT_TEST");
			oldVersion=2;
		}
		if (oldVersion == 2) {
			db.execSQL("alter table KEYPOINTACQUISITION add column OFFSET VARCHAR(50)");
			oldVersion=3;
		}
		if (oldVersion == 3){
			db.execSQL("alter table PROTECTIVE_POTENTIAL rename to PROTECTIVE_POTENTIAL_TEMP");
			// 创建保护电位的新的数据表
			StringBuffer _protectivepotential = new StringBuffer();
			_protectivepotential.append("		Create  TABLE PROTECTIVE_POTENTIAL(");
			_protectivepotential.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
			_protectivepotential.append("				,[guid] varchar(50)");
			_protectivepotential.append("				,[userid] varchar(50)");
			_protectivepotential.append("				,[year] varchar(50)");
			_protectivepotential.append("				,[month] varchar(50)");
			_protectivepotential.append("				,[line] varchar(50)");
			_protectivepotential.append("				,[pile] varchar(50)");
			_protectivepotential.append("				,[value] varchar(50)");
			_protectivepotential.append("				,[test_time] varchar(50)");
			_protectivepotential.append("				,[remarks] varchar(200)");
			_protectivepotential.append("				,[voltage] varchar(50)");
			_protectivepotential.append("				,[ground] varchar(50)");
			_protectivepotential.append("				,[temperature] varchar(50)");
			_protectivepotential.append("				,[lineid] varchar(50)");
			_protectivepotential.append("				,[pileid] varchar(50)");
			_protectivepotential.append("				,[natural] varchar(50)");
			_protectivepotential.append("				,[ir] varchar(50)");
			_protectivepotential.append("				,[isupload] int(50)");
			_protectivepotential.append("				)");
			db.execSQL(_protectivepotential.toString());
			db.execSQL("insert into PROTECTIVE_POTENTIAL select id,guid,userid,year,month,line,pile,value," +
					"test_time,remarks,voltage,ground,temperature,lineid,pileid,'','',isupload from PROTECTIVE_POTENTIAL_TEMP");
			db.execSQL("drop table PROTECTIVE_POTENTIAL_TEMP");
			
			db.execSQL("alter table NATURAL_POTENTIAL rename to NATURAL_POTENTIAL_TEMP");
			// 创建自然电位的新的数据表
			StringBuffer _naturalpotential = new StringBuffer();
			_naturalpotential.append("		Create  TABLE NATURAL_POTENTIAL(");
			_naturalpotential.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
			_naturalpotential.append("				,[guid] varchar(50)");
			_naturalpotential.append("				,[userid] varchar(50)");
			_naturalpotential.append("				,[year] varchar(50)");
			_naturalpotential.append("				,[month] varchar(50)");
			_naturalpotential.append("				,[line] varchar(50)");
			_naturalpotential.append("				,[pile] varchar(50)");
			_naturalpotential.append("				,[value] varchar(50)");
			_naturalpotential.append("				,[test_time] varchar(50)");
			_naturalpotential.append("				,[remarks] varchar(200)");
			_naturalpotential.append("				,[voltage] varchar(50)");
			_naturalpotential.append("				,[temperature] varchar(50)");
			_naturalpotential.append("				,[weather] varchar(50)");
			_naturalpotential.append("				,[lineid] varchar(50)");
			_naturalpotential.append("				,[pileid] varchar(50)");
			_naturalpotential.append("				,[isupload] int(50)");
			_naturalpotential.append("				)");
			db.execSQL(_naturalpotential.toString());
			db.execSQL("insert into NATURAL_POTENTIAL select id,guid,userid,year,'',line,pile,value,test_time," +
					"remarks,voltage,temperature,weather,lineid,pileid,isupload from NATURAL_POTENTIAL_TEMP");
			db.execSQL("drop table NATURAL_POTENTIAL_TEMP");
			
			db.execSQL("alter table GROUND_RESISTANCE rename to GROUND_RESISTANCE_TEMP");
			// 接地电阻的新的数据表
			StringBuffer _groundresistance = new StringBuffer();
			_groundresistance.append("		Create  TABLE GROUND_RESISTANCE(");
			_groundresistance.append("				 [id] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
			_groundresistance.append("				,[guid] varchar(50)");
			_groundresistance.append("				,[cpgroundbedeventid] varchar(50)");
			_groundresistance.append("				,[userid] varchar(50)");
			_groundresistance.append("              ,[test_date] varchar(50)");
			_groundresistance.append("				,[set_value] number(50)");
			_groundresistance.append("				,[test_value] number(50)");
			_groundresistance.append("				,[conclusion] varchar(100)");
			_groundresistance.append("				,[year] varchar(50)");
			_groundresistance.append("				,[halfyear] varchar(50)");
			_groundresistance.append("				,[isupload] int(50)");
			_groundresistance.append("				)");
			db.execSQL(_groundresistance.toString());
			db.execSQL("insert into GROUND_RESISTANCE select id,guid,cpgroundbedeventid,userid,test_date," +
					"set_value,test_value,conclusion,'','',isupload from GROUND_RESISTANCE_TEMP");
			db.execSQL("drop table GROUND_RESISTANCE_TEMP");
			oldVersion=4;
		}
	}

}
