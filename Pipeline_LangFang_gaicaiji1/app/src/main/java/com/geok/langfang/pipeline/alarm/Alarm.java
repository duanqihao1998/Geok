package com.geok.langfang.pipeline.alarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.pipeline.MainView;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.tools.Tools;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Alarm extends Activity implements OnClickListener {

	ListView listView;// 存放报警信息的列表控件
	OperationDB operationDB; // 数据操作的对象
	List<AlarmData> listdata; // 数据列表
	TextView textview; // text控件
	Cursor cursor; // 数据库操作的游标
	int n;// 用来记录未读信息的条数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
		// operationDB=new OperationDB(this);
		// cursor= operationDB.DBselect(Type.ALARM_INFO);
		System.out.println("alarm oncreat");
		Button back = (Button) findViewById(R.id.back);
		Button main = (Button) findViewById(R.id.main);
		textview = (TextView) findViewById(R.id.alarm_text);
		back.setTag(30);
		main.setTag(31);
		back.setOnClickListener(this);
		main.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		System.out.println("alarm onresume");
		operationDB = new OperationDB(this);
		cursor = operationDB.DBselect(Type.ALARM_INFO);
		initList();
		textview.setText("总共有" + listdata.size() + "条报警信息，未读" + n + "条");
		super.onResume();
	}

	/**
	 * 初始化列表信息，从数据库中读取数据，然后匹配在listview上
	 */
	private void initList() {
		n = 0;
		listdata = new ArrayList<AlarmData>();

		listView = (ListView) findViewById(R.id.alarm_list);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		// while (cursor.moveToNext()) {
		for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {

			/*
			 * 为显示的详细数据赋值
			 */
			AlarmData data = new AlarmData();
			data.setAlarmlocation(cursor.getString(8));

			data.setAlarmtime(cursor.getString(9));

			data.setAlarmtype(cursor.getString(1));

			data.setDroptime(cursor.getString(7));

			data.setMaxoffset(cursor.getString(5));

			data.setRealspeed(cursor.getString(4));

			data.setMaxspeed(cursor.getString(3));
			data.setRealoffset(cursor.getString(6));
			data.setFlag(cursor.getInt(0));
			listdata.add(data);

			/*
			 * 为显示的列表赋值
			 */
			map = new HashMap<String, Object>();
			String type = cursor.getString(1);
			map.put("image1", R.drawable.alarm_flag);
			map.put("tittle", type);
			map.put("time", cursor.getString(9));
			map.put("location", cursor.getString(8));
			if (type.equals("超速")) {
				map.put("detial", "设定最大速度：" + cursor.getString(3) + " 实际速度：" + cursor.getString(4));
			} else if (type.equals("越界")) {
				map.put("detial", "设定最大偏移：" + cursor.getString(5) + " 实际偏移：" + cursor.getString(6));
			} else {
				map.put("detial", "掉线时间间隔:" + cursor.getString(7));
			}
			int flag = cursor.getInt(11);
			if (flag == 1) {
				map.put("alarm_new", R.drawable.alarm_new);
				n++;
			} else {
				map.put("alarm_new", null);
			}
			list.add(map);
		}
		cursor.close();operationDB.db.close();
		SimpleAdapter adapter = new SimpleAdapter(Alarm.this, list, R.layout.alarm_list,
				new String[] { "image1", "tittle", "time", "location", "detial", "alarm_new" },
				new int[] { R.id.alarm_list_image1, R.id.alarm_list_tittle, R.id.alarm_list_time,
						R.id.alarm_list_location, R.id.alarm_list_detial, R.id.alarm_new });
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				intent.setClass(Alarm.this, AlarmDetial.class);
				intent.putExtra("values", listdata.get(arg2));
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

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
	protected void onDestroy() {
		super.onDestroy();
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
			intent2.setClass(Alarm.this, MainView.class);
			startActivity(intent2);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		}

	}

}
