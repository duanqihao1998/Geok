package com.geok.langfang.pipeline.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.tools.Tools;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 
 * @类描述 用来显示新信息的详细信息
 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel:***********
 * @createDate 2013-7-4 上午10:23:29
 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
 */
public class NotificationDetail extends Activity {

	ListView listview; // 用来显示报警信息的详细信息的列表控件
	SimpleAdapter adapter; // 数据匹配器
	List<Map<String, String>> list; // 数据列表
	OperationDB operationDB; // 数据库操作对象
	NotificationData data;
	int flag; // 标识位，用来标识信息已读

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_detial);

		data = (NotificationData) getIntent().getSerializableExtra("values");
		// flag=data.getFlag();
		// String time = data.getNotitime();
		// operationDB=new OperationDB(this);
		// operationDB.DBupdateNotification(flag,time);

		initList();

		Button back = (Button) findViewById(R.id.back);
		Button main = (Button) findViewById(R.id.main);
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
				Tools.backMain(NotificationDetail.this);

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

	/**
	 * 
	 * @功能描述 初始化ListView 控件
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel:***********
	 * @createDate 2013-7-4 上午10:23:49
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	private void initList() {
		listview = (ListView) findViewById(R.id.noti_detail);
		list = new ArrayList<Map<String, String>>();
		Map<String, String> map;

		map = new HashMap<String, String>();
		map.put("tittle", "消息类型：");
		map.put("text", data.getNotititle());
		list.add(map);
		map = new HashMap<String, String>();
		map.put("tittle", "接收时间：");
		map.put("text", data.getNotitime());
		list.add(map);
		map = new HashMap<String, String>();
		map.put("tittle", "消息详情：");
		map.put("text", data.getNotidetail());
		list.add(map);

		adapter = new SimpleAdapter(this, list, R.layout.listitem,
				new String[] { "tittle", "text" }, new int[] { R.id.listitem_text1,
						R.id.listitem_text2 });

		listview.setAdapter(adapter);

	}

}
