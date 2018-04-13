package com.geok.langfang.pipeline.Mywork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.jsonbean.InspectionPlanLineInfo;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.tools.Tools;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PlanHistory extends Activity {

	ListView listview;
	OperationDB operationDB;
	List<Map<String, String>> list;
	List<PlanHistoryData> data;
	Map<String, String> map;
	SimpleAdapter adapter;
	Button back, main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.planhistory);
		data = new ArrayList<PlanHistoryData>();

		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});
		main.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Tools.backMain(PlanHistory.this);
			}
		});
		initList();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
		return super.onKeyDown(keyCode, event);

	}

	private void initList() {
		listview = (ListView) findViewById(R.id.planhistorylist);
		operationDB = new OperationDB(getApplicationContext());
		list = new ArrayList<Map<String, String>>();
		Cursor cursor = operationDB.DBselect(Type.INSPECTION_PLAN);
		while (cursor.moveToNext()) {
			/*
			 * 
			 */
			map = new HashMap<String, String>();
			map.put("department", "计划执行部门：" + cursor.getString(1));
			map.put("planno", "巡检计划编号：" + cursor.getString(2));
			map.put("type", "巡检类型：" + cursor.getString(3));
			list.add(map);

			/*
			 * 
			 * 
			 */
			PlanHistoryData plan = new PlanHistoryData();

			plan.setDEPARTMENT(cursor.getString(1));
			plan.setDETERMINANT(cursor.getString(9));
			plan.setINSBDATE(cursor.getString(10));
			plan.setINSEDATE(cursor.getString(11));
			plan.setINSFREQ(cursor.getString(6));
			plan.setINSPECTOR(cursor.getString(5));
			plan.setINSPECTORTYPE(cursor.getString(4));
			plan.setINSPROPER(cursor.getString(8));
			plan.setINSTYPE(cursor.getString(3));
			plan.setINSVEHICLE(cursor.getString(7));
			// plan.setPlanLineInfo(cursor.getString(1));
			plan.setPLANNO(cursor.getString(2));
			List<InspectionPlanLineInfo> list = new ArrayList<InspectionPlanLineInfo>();
			String line = cursor.getString(12);
			String rangeflag = cursor.getString(13);

			String[] templine = line.split("#");
			String[] temprangeflag = rangeflag.split("#");

			if (templine != null) {
				for (int i = 0; i < templine.length; i++) {
					InspectionPlanLineInfo info = new InspectionPlanLineInfo();
					info.setLINELOOP(templine[i]);
					info.setRANGEFLAG(temprangeflag[i]);
					list.add(info);
				}
			}
			plan.setPlanLineInfo(list);

			data.add(plan);
		}
		cursor.close();operationDB.db.close();
		adapter = new SimpleAdapter(PlanHistory.this, list, R.layout.planhistoryitem, new String[] {
				"department", "planno", "type" }, new int[] { R.id.planhistoryitem_text1,
				R.id.planhistoryitem_text2, R.id.planhistoryitem_text3 });

		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), PlanHistoryDetial.class);
				intent.putExtra("values", data.get(arg2));
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

			}
		});

	}

}
