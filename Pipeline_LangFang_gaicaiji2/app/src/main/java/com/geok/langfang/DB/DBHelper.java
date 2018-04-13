package com.geok.langfang.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class DBHelper {
	private Context context;

	// 获得调用该构造函数的句柄
	public DBHelper(Context context) {
		this.context = context;
	}

	File file = new File("/sdcard/pipeline");// 创建数据库目录
	File f = new File("/sdcard/pipeline/patrol.db");// 创建数据库

	public SQLiteDatabase getDarabase() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			if (!file.exists()) {
				file.mkdirs();
			}
			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(f, null);
			/*
			 * 创建保护点位 ，自然电位，接地电阻，防腐侧漏 四张表 保护电位的字段有：month
			 * ，month_id,year.year_id,line
			 * ,line_id,pilenum,pilenum_id,tester,tester_id
			 * ,provalue,comment,isupload;
			 * 自然电位的字段有：year，year_id,line,line_id,pilenum
			 * ,pilenum_id,tester,tester_id,provalue,comment,isupload;
			 * 接地电阻的字段有：year
			 * ，year_id,half_year,half_year_id,number,number_id,resitance
			 * (电阻值),stated_value,tester,tester_id,comment, isupload; 防腐侧漏 的字段有：
			 */

			// db.execSQL("create table protective_potential(" +
			// "int IDENTITY(1,1) PRIMARY KEY," +//id自增长
			// "year varchar(20)," +
			// "year_id varchar(10)," +
			// "month varchar(20)," +
			// "month_id varchar(10)," +
			// "line varchar(50)," +
			// "line_id varchar(10)," +
			// "pilenum varchar(30)," +
			// "pilenum_id varchar(10)," +
			// "tester varchar(20)," +
			// "tester_id varchar(10)," +
			// "provalue varchar(10)," +
			// "comment varchar(200)," +
			// "isupload boolean" +
			// ")");
			// db.execSQL("create table natural_potential(" +
			// "int IDENTITY(1,1) PRIMARY KEY," +//id自增长
			// "year varchar(20)," +
			// "year_id varchar(10)," +
			// "line varchar(50)," +
			// "line_id varchar(10)," +
			// "pilenum varchar(30)," +
			// "pilenum_id varchar(10)," +
			// "tester varchar(20)," +
			// "tester_id varchar(10)," +
			// "provalue varchar(10)," +
			// "comment varchar(200)," +
			// "isupload boolean" +
			// ")");
			// db.execSQL("create table ground_resistance(" +
			// "int IDENTITY(1,1) PRIMARY KEY," +//id自增长
			// "year varchar(20)," +
			// "year_id varchar(10)," +
			// "half_year varchar(20)," +
			// "half_year_id varchar(10)," +
			// "number varchar(50)," +
			// "number_id varchar(10)," +
			// "resitance varchar(30)," +
			// "stated_value varchar(30)," +
			// "tester varchar(20)," +
			// "tester_id varchar(10)," +
			// "provalue varchar(10)," +
			// "comment varchar(200)," +
			// "isupload boolean" +
			// ")");
			return db;
		}

		else {
			Toast.makeText(context, "SD卡不存在或存在读写错误，请检查", 1000).show();
		}

		return null;

	}

}
