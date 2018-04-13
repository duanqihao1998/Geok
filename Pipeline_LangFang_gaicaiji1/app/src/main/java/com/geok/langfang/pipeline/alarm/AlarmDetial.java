package com.geok.langfang.pipeline.alarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.pipeline.MainView;
import com.geok.langfang.pipeline.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 
 * @author sunshihai 用来显示报警信息的详细信息
 */
public class AlarmDetial extends Activity implements OnClickListener {

	ListView listview; // 用来显示报警信息的详细信息的列表控件
	SimpleAdapter adapter; // 数据匹配器
	List<Map<String, String>> list; // 数据列表
	OperationDB operationDB; // 数据库操作对象
	AlarmData data;
	int flag; // 标识位，用来标识信息已读

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_detial);

		data = (AlarmData) getIntent().getSerializableExtra("values");
		flag = data.getFlag();
		operationDB = new OperationDB(this);
		operationDB.DBupdateAlarm(flag);

		initList();

		Button back = (Button) findViewById(R.id.back);
		Button main = (Button) findViewById(R.id.main);
		back.setTag(30);
		main.setTag(31);
		back.setOnClickListener(this);
		main.setOnClickListener(this);
	}

	/**
	 * 初始化ListView 控件
	 */
	private void initList() {
		listview = (ListView) findViewById(R.id.alarm_ditial);
		list = new ArrayList<Map<String, String>>();
		Map<String, String> map;

		map = new HashMap<String, String>();
		map.put("tittle", "报警类型：");
		map.put("text", data.getAlarmtype());
		list.add(map);
		map = new HashMap<String, String>();
		map.put("tittle", "报警时间：");
		map.put("text", data.getAlarmtime());
		list.add(map);
		map = new HashMap<String, String>();
		map.put("tittle", "报警位置：");
		map.put("text", data.getAlarmlocation());
		list.add(map);
		map = new HashMap<String, String>();
		map.put("tittle", "掉线间隔：");
		map.put("text", data.getDroptime());
		list.add(map);
		map = new HashMap<String, String>();
		map.put("tittle", "设定最大偏移：");
		map.put("text", data.getMaxoffset());
		list.add(map);
		map = new HashMap<String, String>();
		map.put("tittle", "实际偏移：");
		map.put("text", data.getRealoffset());
		list.add(map);
		map = new HashMap<String, String>();
		map.put("tittle", "设定最大速度：");
		map.put("text", data.getMaxspeed());
		list.add(map);
		map = new HashMap<String, String>();
		map.put("tittle", "实际速度：");
		map.put("text", data.getRealspeed());
		list.add(map);
		adapter = new SimpleAdapter(this, list, R.layout.listitem,
				new String[] { "tittle", "text" }, new int[] { R.id.listitem_text1,
						R.id.listitem_text2 });

		listview.setAdapter(adapter);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = (Integer) v.getTag();
		switch (tag) {
		case 30:
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;

		case 31:
			Intent intent2 = new Intent();
			intent2.setClass(AlarmDetial.this, MainView.class);
			startActivity(intent2);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
