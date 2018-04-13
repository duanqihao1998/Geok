package com.geok.langfang.pipeline.notification;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.tools.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

public class Notification extends Activity {

	ListView listView;// 存放报警信息的列表控件
	OperationDB operationDB; // 数据操作的对象
	List<NotificationData> listdata; // 数据列表
	TextView textview; // text控件
	Cursor cursor; // 数据库操作的游标
	Button back, main;
	int n;// 用来记录未读信息的条数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		textview = (TextView) findViewById(R.id.alarm_text);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});
		main.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Tools.backMain(Notification.this);
			}
		});
		// TextView tv = new TextView(this);
		// tv.setText("用户自定义打开的Activity");
		// addContentView(tv, new LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.FILL_PARENT));
	}

	/**
	 * 
	 * @功能描述 第一次创建activity或重新加载实例时读取数据库数据生成列表
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel:
	 * @createDate 2013-6-24 上午10:29:23
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		System.out.println("notification onresume");
		operationDB = new OperationDB(this);
		cursor = operationDB.DBselect(Type.NOTIFICATION);
		initList();
		textview.setText("总共有" + listdata.size() + "条信息，未读" + n + "条");
		cursor.close();operationDB.db.close();
		super.onResume();
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
		n = 0;
		listdata = new ArrayList<NotificationData>();
		listView = (ListView) findViewById(R.id.notification_list);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		NotiReceiver re = new NotiReceiver();
		int size = cursor.getCount();
		for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
			// for(int i = size ; i >0 ; i--){

			// }
			// while(cursor.moveToNext())
			// {

			/*
			 * 为显示的详细数据赋值
			 */
			// boolean cursor1 = cursor.;
			NotificationData data = new NotificationData();
			data.setNotititle(cursor.getString(1));
			data.setNotitype(cursor.getString(3));
			data.setNotitime(cursor.getString(4));
			data.setNotidetail(cursor.getString(5));
			data.setFlag(cursor.getInt(6));
			listdata.add(data);

			// 为显示 的列表赋值
			map = new HashMap<String, Object>();
			map.put("image1", R.drawable.noti);
			map.put("title", cursor.getString(1));
			map.put("type", cursor.getString(3));
			map.put("time", cursor.getString(4));
			map.put("detail", cursor.getString(5));
			int flag = cursor.getInt(6);
			if (flag == 0) {
				map.put("alarm_new", R.drawable.alarm_new);
				n++;
			} else {
				map.put("alarm_new", null);
			}
			list.add(map);
			
			SimpleAdapter adapter = new SimpleAdapter(Notification.this, list, R.layout.alarm_list,
					new String[] { "image1", "type", "time", "title", "detail", "alarm_new" },
					new int[] { R.id.alarm_list_image1, R.id.alarm_list_tittle,
							R.id.alarm_list_time, R.id.alarm_list_location, R.id.alarm_list_detial,
							R.id.alarm_new });
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(Notification.this, NotificationDetail.class);
					int flag = 1;
					listdata.get(arg2).setFlag(flag);
					intent.putExtra("values", listdata.get(arg2));
					operationDB.DBupdateNotification(flag, listdata.get(arg2).getNotitime());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				}
			});
		}
		cursor.close();operationDB.db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, "恢复推送").setIcon(R.drawable.start_push);
		menu.add(0, 2, 2, "停止推送").setIcon(R.drawable.stop_push);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case 1:
			JPushInterface.resumePush(getApplicationContext());
			break;
		case 2:
			JPushInterface.stopPush(getApplicationContext());
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
