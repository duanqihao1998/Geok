package com.geok.langfang.pipeline.Mywork;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.InspectionPlanBean;
import com.geok.langfang.jsonbean.InspectionPlanLineInfo;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Plan extends Activity {
	ListView listview;
	Request request;
	OperationDB operationDB;
	InspectionPlanBean plan;

	private static CustomProgressDialog progressDialog = null;

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
		}
		progressDialog.show();
	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int flag = msg.getData().getInt("flag");
			stopProgressDialog();
			if (flag == 1) {
				 String str = msg.getData().getString("result");
//				String str = "[{\"DEPARTMENT\":\"塔里木输油气分公司\",\"INSTYPE\":\"日常巡检\",\"INSPECTOR\":\"孙浩\",\"DETERMINANT\":\"日常依据\",\"INSPECTLINE\":[{\"RANGEFLAG\":\"管段\",\"LINELOOP\":\"库鄯输油干线\"}],\"INSVEHICLE\":\"车巡\",\"INSBDATE\":\"2013-04-25\",\"INSFREQ\":1,\"PLANNO\":\"20130414102954\",\"INSEDATE\":\"2013-04-25\",\"INSPECTORTYPE\":\"巡线工\",\"INSPROPER\":\"长期\"},{\"DEPARTMENT\":\"塔里木输油气分公司\",\"INSTYPE\":\"日常巡检\",\"INSPECTOR\":\"孙浩\",\"DETERMINANT\":\"日常依据\",\"INSPECTLINE\":[{\"RANGEFLAG\":\"管段\",\"LINELOOP\":\"轮库输气线\"},{\"RANGEFLAG\":\"管段\",\"LINELOOP\":\"轮库输油复线\"}],\"INSVEHICLE\":\"车巡\",\"INSBDATE\":\"2013-04-25\",\"INSFREQ\":1,\"PLANNO\":\"20130414102323\",\"INSEDATE\":\"2013-04-25\",\"INSPECTORTYPE\":\"巡线工\",\"INSPROPER\":\"长期\"}]";

				if ("-2".equals(str)) {
					Cursor cursor = operationDB.DBselect(Type.INSPECTION_PLAN);
					while (cursor.moveToNext()) {
						plan = new InspectionPlanBean();
						plan.setDEPARTMENT(cursor.getString(1));
						plan.setPLANNO(cursor.getString(2));
						plan.setINSTYPE(cursor.getString(3));
						plan.setINSPECTORTYPE(cursor.getString(4));
						plan.setINSPECTOR(cursor.getString(5));
						plan.setINSFREQ(cursor.getString(6));
						plan.setINSVEHICLE(cursor.getString(7));
						plan.setINSPROPER(cursor.getString(8));
						plan.setDETERMINANT(cursor.getString(9));
						plan.setINSBDATE(cursor.getString(10));
						plan.setINSEDATE(cursor.getString(11));

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
						if(plan.getPLANNO().length()>0){
						initList();
						}else{
							Toast.makeText(getApplicationContext(), "暂无巡检计划", 2000).show();
						}
						break;
					}
					cursor.close();operationDB.db.close();
				} else if (str.equals("-1")) {
					Toast.makeText(getApplicationContext(), "暂无巡检计划", 2000).show();
				} else {
					plan = Json.getInspectionPlan(str);
					initList();
					save(plan);
				}

			} else {
				Toast.makeText(getApplicationContext(), "网络异常，请重试", 1000).show();
			}
		}

	};
	Button back, main;
	MyApplication myApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plan);
		myApplication = new MyApplication(this);
		plan = new InspectionPlanBean();
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
				Tools.backMain(Plan.this);
			}
		});

		operationDB = new OperationDB(getApplicationContext());
		request = new Request(myHandler);
		if (Tools.isNetworkAvailable(Plan.this, true)) {
			startProgressDialog();
			request.QueryPlanQuery(myApplication.userid);
		}
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
		listview = (ListView) findViewById(R.id.mywork_plan);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检执行单位:");
		map.put("text", plan.getDEPARTMENT());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检计划编号:");
		map.put("text", plan.getPLANNO());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检类型:");
		map.put("text", plan.getINSTYPE());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检人员类型:");
		map.put("text", plan.getINSPECTORTYPE());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检人员:");
		map.put("text", plan.getINSPECTOR());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检频次:");
		map.put("text", plan.getINSFREQ());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检工具:");
		map.put("text", plan.getINSVEHICLE());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "计划类型:");
		map.put("text", plan.getINSPROPER());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "制定依据:");
		map.put("text", plan.getDETERMINANT());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检开始日期:");
		map.put("text", plan.getINSBDATE());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检结束日期:");
		map.put("text", plan.getINSEDATE());
		list.add(map);
		for (int i = 0; i < plan.getPlanLineInfo().size(); i++) {
			map = new HashMap<String, Object>();
			map.put("tittle", "巡检线路" + (i + 1) + ":");
			map.put("text", plan.getPlanLineInfo().get(i).getLINELOOP());
			list.add(map);
			map = new HashMap<String, Object>();
			map.put("tittle", "巡检范围" + (i + 1) + ":");
			map.put("text", plan.getPlanLineInfo().get(i).getRANGEFLAG());
			list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(Plan.this, list, R.layout.listitem, new String[] {
				"tittle", "text" }, new int[] { R.id.listitem_text1, R.id.listitem_text2 });
		listview.setAdapter(adapter);
	}

	private void save(InspectionPlanBean bean) {
		ContentValues value = new ContentValues();
		value.put("department", bean.getDEPARTMENT());
		value.put("planno", bean.getPLANNO());
		value.put("instype", bean.getINSTYPE());
		value.put("inspectortype", bean.getINSPECTORTYPE());
		value.put("inspector", bean.getINSPROPER());
		value.put("insfreq", bean.getINSFREQ());
		value.put("insvehicle", bean.getINSVEHICLE());
		value.put("insproper", bean.getINSPROPER());
		value.put("determinant", bean.getDETERMINANT());
		value.put("insbdate", bean.getINSBDATE());
		value.put("insedate", bean.getINSEDATE());
		String inspectline = "";
		String rangeflag = "";
		for (int i = 0; i < bean.getPlanLineInfo().size(); i++) {
			inspectline += bean.getPlanLineInfo().get(i).getLINELOOP() + "#";
			rangeflag += bean.getPlanLineInfo().get(i).getRANGEFLAG() + "#";
		}
		value.put("inspectline", inspectline);
		value.put("rangeflag", rangeflag);
		operationDB.DBinsert(value, Type.INSPECTION_PLAN);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		menu.add(0, 1, 1, "退出").setIcon(R.drawable.exit);
		menu.add(0, 2, 2, "注销").setIcon(R.drawable.cancel);
		menu.add(0, 3, 3, "信息查询").setIcon(R.drawable.search);
		menu.add(0, 4, 4, "GPS状态").setIcon(R.drawable.gps);
		menu.add(0, 5, 5, "帮助").setIcon(R.drawable.help);
		menu.add(0, 6, 6, "历史记录").setIcon(R.drawable.history);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 1:
			Tools.exit(this);
			break;
		case 2:
			Tools.cancel(this);
			break;
		case 3:
			break;
		case 4:
			Tools.gps(this);
			break;
		case 5:
			Tools.help(this);
			break;
		case 6:
			Intent intent = new Intent();
			intent.setClass(Plan.this, PlanHistory.class);

			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
