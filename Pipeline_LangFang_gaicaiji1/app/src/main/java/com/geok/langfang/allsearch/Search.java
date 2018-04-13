package com.geok.langfang.allsearch;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.geok.langfang.DB.Type;
import com.geok.langfang.DB.OperationDB;

public class Search {
	OperationDB operationab;
	List<ContentValues> list;

	public List<ContentValues> search_protect(Context c) {
		operationab = new OperationDB(c);
		Cursor cursor = operationab.DBselect(Type.PROTECTIVE_POTENTIAL);
		list = new ArrayList<ContentValues>();
		while (cursor.moveToNext()) {
			ContentValues values = new ContentValues();
			values.put("id", cursor.getInt(0));
			values.put("year", cursor.getString(1));
			values.put("month", cursor.getString(2));
			values.put("line", cursor.getString(3));
			values.put("pile", cursor.getString(4));
			values.put("temperature", cursor.getString(5));
			values.put("value", cursor.getString(6));
			values.put("tester", cursor.getString(7));
			values.put("test_time", cursor.getString(8));
			values.put("ground", cursor.getString(9));
			values.put("voltage", cursor.getString(10));
			values.put("remarks", cursor.getString(11));
			values.put("isupload", cursor.getString(12));
			list.add(values);
			values.clear();
		}
		return list;
	}

	public List<ContentValues> search_natural(Context c) {
		operationab = new OperationDB(c);
		Cursor cursor = operationab.DBselect(Type.NATURAL_POTENTIAL);
		list = new ArrayList<ContentValues>();
		while (cursor.moveToNext()) {
			ContentValues values = new ContentValues();
			values.put("id", cursor.getInt(0));
			values.put("year", cursor.getString(1));
			values.put("line", cursor.getString(2));
			values.put("pile", cursor.getString(3));
			values.put("temperature", cursor.getString(4));
			values.put("value", cursor.getString(5));
			values.put("tester", cursor.getString(6));
			values.put("test_time", cursor.getString(7));
			values.put("voltage", cursor.getString(8));
			values.put("remarks", cursor.getString(9));
			values.put("isupload", cursor.getString(10));
			list.add(values);
			values.clear();
		}
		return list;
	}

	public List<ContentValues> search_ground(Context c) {
		operationab = new OperationDB(c);
		Cursor cursor = operationab.DBselect(Type.GROUND_RESISTANCE);
		list = new ArrayList<ContentValues>();
		while (cursor.moveToNext()) {
			ContentValues values = new ContentValues();
			values.put("id", cursor.getInt(0));
			values.put("year", cursor.getString(1));
			values.put("halfyear", cursor.getString(2));
			values.put("line", cursor.getString(3));
			values.put("pile", cursor.getString(4));
			values.put("ground", cursor.getString(5));
			values.put("specifiedground", cursor.getString(6));
			values.put("tester", cursor.getString(7));
			values.put("test_time", cursor.getString(8));
			values.put("remarks", cursor.getString(9));
			values.put("isupload", cursor.getString(10));
			list.add(values);
			values.clear();
		}
		return list;
	}

	public List<ContentValues> search_corrosive(Context c) {
		operationab = new OperationDB(c);
		Cursor cursor = operationab.DBselect(Type.CORROSIVE);
		list = new ArrayList<ContentValues>();
		while (cursor.moveToNext()) {
			ContentValues values = new ContentValues();
			values.put("id", cursor.getInt(0));
			values.put("line", cursor.getString(1));
			values.put("pile", cursor.getString(2));
			values.put("damage_type", cursor.getString(3));
			values.put("repair_type", cursor.getString(4));
			values.put("damage_des", cursor.getString(5));
			values.put("offset", cursor.getString(6));
			values.put("soil", cursor.getString(7));
			values.put("area", cursor.getString(8));
			values.put("corrosion_des", cursor.getString(9));
			values.put("corrosion_area", cursor.getString(10));
			values.put("corrosion_num", cursor.getString(11));
			values.put("repair_obj", cursor.getString(12));
			values.put("check_date", cursor.getString(13));
			values.put("mile_location", cursor.getString(14));
			values.put("appear_des", cursor.getString(15));
			values.put("repair_date", cursor.getString(16));
			values.put("repair_situation", cursor.getString(17));
			values.put("pile_info", cursor.getString(18));
			values.put("remarks", cursor.getString(19));
			values.put("isupload", cursor.getString(20));
			list.add(values);
			values.clear();
		}
		return list;
	}

	public List<ContentValues> search_problem(Context c) {
		operationab = new OperationDB(c);
		Cursor cursor = operationab.DBselect(Type.PROBLEM_UPLOAD);
		list = new ArrayList<ContentValues>();
		while (cursor.moveToNext()) {
			ContentValues values = new ContentValues();
			values.put("id", cursor.getInt(0));
			values.put("type", cursor.getString(1));
			values.put("current_location", cursor.getString(2));
			values.put("location", cursor.getString(3));
			values.put("date", cursor.getString(4));
			values.put("time", cursor.getString(5));
			values.put("photo_path", cursor.getString(6));
			values.put("line", cursor.getString(7));
			values.put("pile", cursor.getString(9));
			values.put("offset", cursor.getString(10));
			values.put("report_time", cursor.getString(11));
			values.put("tester", cursor.getString(12));
			values.put("problem_des", cursor.getString(11));
			values.put("isupload", cursor.getString(12));
			list.add(values);
			values.clear();
		}
		return list;
	}

	public List<ContentValues> search_linelog(Context c) {
		operationab = new OperationDB(c);
		Cursor cursor = operationab.DBselect(Type.LINE_LOG);
		list = new ArrayList<ContentValues>();
		while (cursor.moveToNext()) {
			ContentValues values = new ContentValues();
			values.put("id", cursor.getInt(0));
			values.put("line", cursor.getString(1));
			values.put("pile", cursor.getString(2));
			values.put("offset", cursor.getString(3));
			values.put("tester", cursor.getString(4));
			values.put("problem_type", cursor.getString(5));
			values.put("time", cursor.getString(6));
			values.put("problem_des", cursor.getString(7));
			values.put("informant", cursor.getString(8));
			values.put("report_date", cursor.getString(9));
			values.put("photo", cursor.getString(10));
			values.put("start_time", cursor.getString(11));
			values.put("finish_time", cursor.getString(12));
			values.put("pipeline_type", cursor.getString(13));
			values.put("car", cursor.getString(14));
			values.put("frequency", cursor.getString(15));
			values.put("weather", cursor.getString(16));
			values.put("start_location", cursor.getString(17));
			values.put("note", cursor.getString(18));
			values.put("handle_situation", cursor.getString(19));
			values.put("result", cursor.getString(20));
			values.put("isupload", cursor.getString(21));
			list.add(values);
			values.clear();
		}
		return list;
	}
}
