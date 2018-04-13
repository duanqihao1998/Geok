package com.geok.langfang.pipeline.inspection;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.InspectionHistoryBean;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.MyHorizontalScrollView;
import com.geok.langfang.tools.SizeCallbackForMenu;
import com.geok.langfang.tools.Tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author sunshihai 巡检记录的历史信息
 * 
 */
public class InspectionRecordHistory extends Activity implements OnClickListener {

	private static CustomProgressDialog progressDialog = null;
	private MyHorizontalScrollView scrollView;
	List<InspectionHistoryBean> bean;
	SimpleAdapter adapter;
	Request request;
	InsepctionRecordData insepctionRecordData;
	ArrayList<InsepctionRecordData> InsepctionRecordDataList;
	private View leftMenu;
	private View rightMenu;
	private ListView listview;
	private View histortList;
	private Button confirm;
	private ImageButton search;
	OperationDB operationDB;
	Button back, main;
	RelativeLayout starttime, endtime;
	RelativeLayout history_search_r;
	TextView starttime_text, endtime_text;
	Cursor cursor;
	Calendar c;
	String start_date_string;
	boolean isOnclick = false;

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

	public Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			stopProgressDialog();
			if (msg.arg1 == 23) {
				String str = msg.getData().getString("result");
				if (!str.equals("-1")) {
					getSharedPreferences("values", MODE_PRIVATE).edit().putString("values", str)
							.commit();
					bean = Json.getInspectionHistory(str);
					initNetList(str);
					System.out.println(bean);
				} else {
					Toast.makeText(getApplicationContext(), "暂无该时间段的历史记录", 1000).show();
				}
			}
			if (msg.arg1 == 2000) {
				String str = getSharedPreferences("values", MODE_PRIVATE).getString("values", "-1");
				initNetList(str);
			}

		}

	};
	MyApplication myApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		c = Calendar.getInstance();
		LayoutInflater inflater = LayoutInflater.from(this);
		request = new Request(myHandler);
		setContentView(inflater.inflate(R.layout.insepction_history, null));

		myApplication = new MyApplication(this);
		scrollView = (MyHorizontalScrollView) findViewById(R.id.myScrollView);
		leftMenu = findViewById(R.id.leftmenu);
		rightMenu = findViewById(R.id.rightmenu);
		histortList = inflater.inflate(R.layout.insepctionrecordhistory, null);
		starttime = (RelativeLayout) findViewById(R.id.inseption_history_starttime);
		starttime.setOnClickListener(this);
		endtime = (RelativeLayout) findViewById(R.id.inseption_history_endtime);
		endtime.setOnClickListener(this);
		starttime_text = (TextView) findViewById(R.id.inseption_history_starttime_text);
		endtime_text = (TextView) findViewById(R.id.inseption_history_endtime_text);
		confirm = (Button) findViewById(R.id.inseption_history_confirm);
		back = (Button) histortList.findViewById(R.id.back);
		main = (Button) histortList.findViewById(R.id.main);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});
		main.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Tools.backMain(InspectionRecordHistory.this);
			}
		});

		history_search_r = (RelativeLayout) histortList.findViewById(R.id.history_search_r);
		search = (ImageButton) histortList.findViewById(R.id.insepctionrecordhistory_search);
		history_search_r.setOnClickListener(new OnClickListener() {
			// search = (ImageButton)
			// histortList.findViewById(R.id.insepctionrecordhistory_search);
			// search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scrollView.clickRightButton(search.getMeasuredWidth());
				isOnclick = !isOnclick;
				setOnclick(isOnclick);
			}
		});
		View leftView = new View(this);// 左边透明视图
		View rightView = new View(this);// 右边透明视图
		leftView.setBackgroundColor(Color.TRANSPARENT);
		rightView.setBackgroundColor(Color.TRANSPARENT);
		final View[] children = new View[] { leftView, histortList, rightView };
		// 初始化滚动布局
		scrollView.initViews(children, new SizeCallbackForMenu(search), leftMenu, rightMenu);
		// startProgressDialog();
		 init();
if (Tools.isNetworkAvailable(InspectionRecordHistory.this, true)) {
			request.InspectionPlanQuery(myApplication.userid, "2000-1-1", "2020-1-1");
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		startProgressDialog();
		// listview=(ListView)findViewById(R.id.problem_history_list);
		initList();
		init();
		super.onResume();
	}

	private void init() {
		
		confirm.setOnClickListener(this);
		setOnclick(isOnclick);
	}
	public void setOnclick(boolean isonclick){
		confirm.setClickable(isonclick);
		starttime.setClickable(isonclick);
		endtime.setClickable(isonclick);
}

	/**
	 * 初始化ListView匹配从网络上获取的数据
	 * 
	 * @param str
	 *            从网络上请求的数据 从网络上请求数据，进行解析，然后匹配到ListView上
	 */
	public void initNetList(String str) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		listview = (ListView) findViewById(R.id.insepctionrecordhistory_list);
		Map<String, Object> map;

		for (int i = 0; i < bean.size(); i++) {

			map = new HashMap<String, Object>();
			map.put("starttime", "开始巡检时间:" + bean.get(i).getBEGINDATETIME());
			map.put("endtime", "结束巡检时间:" + bean.get(i).getENDDATETIME());
			map.put("type", "巡检人员：" + bean.get(i).getINSPECTOR());
			list.add(map);
		}

		adapter = new SimpleAdapter(getApplicationContext(), list,
				R.layout.insepctionrecordhistoryitem,
				new String[] { "starttime", "endtime", "type" }, new int[] { R.id.insepction_text1,
						R.id.insepction_text2, R.id.insepction_text3 });
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (bean.get(arg2).getTYPE().equals("1")) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), InsepctionRecordDetialNet1.class);
					intent.putExtra("eventid", bean.get(arg2).getEVENTID());
					// startActivity(intent);
					startActivityForResult(intent, 2000);

				} else if (bean.get(arg2).getTYPE().equals("0")) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), InsepctionRecordDetialNet2.class);
					intent.putExtra("eventid", bean.get(arg2).getEVENTID());
					// startActivity(intent);
					startActivityForResult(intent, 2000);
				}
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

			}
		});
	}

	/**
	 * 初始化ListView匹配从从数据库查询数据
	 */

	public void initList() {
		InsepctionRecordDataList = new ArrayList<InsepctionRecordData>();
		operationDB = new OperationDB(getApplicationContext());
		cursor = operationDB.DBselect(Type.LINE_LOG);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		listview = (ListView) findViewById(R.id.insepctionrecordhistory_list);
		Map<String, Object> map;
		stopProgressDialog();
		/*
		 * cusor 的格式
		 */
		for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
//		while (cursor.moveToNext()) {
			insepctionRecordData = new InsepctionRecordData();
			insepctionRecordData.setGuid(cursor.getString(1));
			insepctionRecordData.setStarttime(cursor.getString(2));
			insepctionRecordData.setEndtime(cursor.getString(3));
			insepctionRecordData.setType(cursor.getString(4));
			insepctionRecordData.setTester(cursor.getString(5));
			insepctionRecordData.setTools(cursor.getString(6));
			insepctionRecordData.setInspfreq(cursor.getString(7));
			insepctionRecordData.setYeild(cursor.getString(8));
			insepctionRecordData.setDevice(cursor.getString(9));
			insepctionRecordData.setPoints(cursor.getString(10));
			insepctionRecordData.setSpeed(cursor.getString(11));
			insepctionRecordData.setWeather(cursor.getString(12));
			insepctionRecordData.setRoad(cursor.getString(13));
			insepctionRecordData.setRecord(cursor.getString(14));
			insepctionRecordData.setProblem(cursor.getString(15));
			insepctionRecordData.setResult(cursor.getString(16));
			insepctionRecordData.setTime(cursor.getString(17));
			insepctionRecordData.setLocation(cursor.getString(18));
			insepctionRecordData.setCar(cursor.getString(19));
			insepctionRecordData.setSender(cursor.getString(20));
			insepctionRecordData.setReceiver(cursor.getString(21));
			insepctionRecordData.setOther(cursor.getString(22));
			insepctionRecordData.setFlag(cursor.getInt(23));
			insepctionRecordData.setInfo(cursor.getString(24));
			insepctionRecordData.setEventid(cursor.getString(25));

			InsepctionRecordDataList.add(insepctionRecordData);
			//
			/*
			 * 为每一个listItem的标题数据赋值
			 */
			map = new HashMap<String, Object>();
			map.put("starttime", "开始巡检时间:" + cursor.getString(2));
			map.put("endtime", "结束巡检时间:" + cursor.getString(3));
			map.put("type", "巡检类型：" + cursor.getString(4));
			list.add(map);
		}
		cursor.close();operationDB.db.close();
		adapter = new SimpleAdapter(getApplicationContext(), list,
				R.layout.insepctionrecordhistoryitem,
				new String[] { "starttime", "endtime", "type" }, new int[] { R.id.insepction_text1,
						R.id.insepction_text2, R.id.insepction_text3 });
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), InsepctionRecordDetial.class);
				intent.putExtra("values", InsepctionRecordDataList.get(arg2));
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.inseption_history_confirm:
			Request request = new Request(myHandler);
			if (Tools.isNetworkAvailable(this, true)) {
			request.InspectionPlanQuery(myApplication.userid, starttime_text.getText().toString(),
					endtime_text.getText().toString());
			isOnclick = !isOnclick;
			setOnclick(isOnclick);
			}
			scrollView.clickRightButton(search.getMeasuredWidth());
			break;
		case R.id.inseption_history_starttime:
			Tools.setDateDialog(InspectionRecordHistory.this, c, starttime_text);
			break;
		case R.id.inseption_history_endtime:
			if ("起始日期".equals(starttime_text.getText().toString())) {
				Toast.makeText(InspectionRecordHistory.this, "请先选择起始日期！", 1000).show();
			} else {
				start_date_string = starttime_text.getText().toString();
				Tools.setEndDateDialog(InspectionRecordHistory.this, c, start_date_string,
						endtime_text);
			}
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {

		case 2000:
			Message message = new Message();
			message.arg1 = 2000;
			myHandler.sendMessage(message);
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
}
