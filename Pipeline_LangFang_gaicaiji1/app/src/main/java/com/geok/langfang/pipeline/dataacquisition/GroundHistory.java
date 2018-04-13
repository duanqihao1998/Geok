package com.geok.langfang.pipeline.dataacquisition;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.allsearch.Search;
import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.HistoryDataGroundBean;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.R.id;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.MyHorizontalScrollView;
import com.geok.langfang.tools.SizeCallbackForMenu;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.tools.TypeQuest;

/**
 * 
 * @author wuchangming 接地电阻的历史记录类
 * 
 */
public class GroundHistory extends Activity implements OnClickListener {

	List<HistoryDataGroundBean> bean;
	String userid;
	MyApplication myApplication;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.arg1 == 18) {
				String str = msg.getData().getString("result");
				getSharedPreferences("values", MODE_PRIVATE).edit().putString("values", str)
						.commit();
				bean = Json.HistoryDataGround(str);
				initNetList(str);
				stopProgressDialog();
			}
			if (msg.arg1 == 2000) {
				String str2 = getSharedPreferences("values", MODE_PRIVATE)
						.getString("values", "-1");
				initNetList(str2);
				stopProgressDialog();
			}
		}

	};
	private MyHorizontalScrollView scrollView;
	private SizeCallbackForMenu callback;
	private View leftMenu;
	private View rightMenu;
	private View historyList;

	private Button test, print;
	private ImageButton search;
	Context context = this;
	Search search2;

	GroundHistoryAdapter groundHistoryAdapter;
	GroundHistoryData groundHistoryData;
	HistoryDataGroundBean groundHistoryBean;
	ArrayList<HistoryDataGroundBean> groundHistoryBeanList;
	ArrayList<GroundHistoryData> groundHistoryDataList;
	private ListView listview;
	OperationDB operationDB;
	Cursor cursor;

	Button back, main;

	String lineId, markId, markerstation, markerstation2;
	private Calendar c = null;
	private EditText et = null;
	TextView te_search_startdate, te_search_enddate;
	RelativeLayout re_search_startdate, re_search_enddate;
	RelativeLayout data_history_search_r;
	String start_date_string;
	boolean isOnclick = false;
	Button sure;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		c = Calendar.getInstance();
		LayoutInflater inflater = LayoutInflater.from(this);
		setContentView(inflater.inflate(R.layout.dataacquisition_history_noline, null));

		myApplication = new MyApplication(this);
		userid = myApplication.userid;
		scrollView = (MyHorizontalScrollView) findViewById(R.id.myScrollView);
		leftMenu = findViewById(R.id.leftmenu);

		rightMenu = findViewById(R.id.rightmenu);
		historyList = inflater.inflate(R.layout.dataacquisition_history_list, null);
		re_search_startdate = (RelativeLayout) findViewById(R.id.re_search_startdate);
		te_search_startdate = (TextView) findViewById(R.id.te_search_startdate);
		re_search_enddate = (RelativeLayout) findViewById(R.id.re_search_enddate);
		te_search_enddate = (TextView) findViewById(R.id.te_search_enddate);
		re_search_startdate.setTag(8);
		re_search_enddate.setTag(9);
		re_search_startdate.setOnClickListener(this);
		re_search_enddate.setOnClickListener(this);
		sure = (Button) findViewById(id.sure);

		back = (Button) historyList.findViewById(R.id.back);
		main = (Button) historyList.findViewById(R.id.main);
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
				Tools.backMain(GroundHistory.this);
			}
		});

		data_history_search_r = (RelativeLayout) historyList
				.findViewById(R.id.data_history_search_r);
		search = (ImageButton) historyList.findViewById(R.id.data_history_search);
		data_history_search_r.setOnClickListener(new OnClickListener() {
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
		final View[] children = new View[] { leftView, historyList, rightView };
		// 初始化滚动布局
		scrollView.initViews(children, new SizeCallbackForMenu(search), leftMenu, rightMenu);
		init();
	}

	public void setOnclick(boolean isonclick){
		sure.setClickable(isonclick);
		re_search_startdate.setClickable(isonclick);
		re_search_enddate.setClickable(isonclick);
	
}
	
	public void initList() {
		startProgressDialog();
		groundHistoryDataList = new ArrayList<GroundHistoryData>();
		operationDB = new OperationDB(getApplicationContext());
		cursor = operationDB.DBselect(Type.GROUND_RESISTANCE);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		listview = (ListView) findViewById(R.id.data_history_list);
		Map<String, Object> map;
		stopProgressDialog();
		for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
//		while (cursor.moveToNext()) {
			/*
			 * 为每一个listItem详细数据赋值
			 */
			groundHistoryData = new GroundHistoryData();
			groundHistoryData.setGuid(cursor.getString(1));
			groundHistoryData.setCpgroundbedeventid(cursor.getString(2));
			groundHistoryData.setUserid(cursor.getString(3));
			groundHistoryData.setTestdate(cursor.getString(4));
			groundHistoryData.setSetvalue(cursor.getString(5));
			groundHistoryData.setTestvalue(cursor.getString(6));
			groundHistoryData.setYear(cursor.getString(8));
			groundHistoryData.setHalfyear(cursor.getString(9));
			groundHistoryData.setConclusion(cursor.getString(7));
			groundHistoryData.setIsupload(cursor.getInt(10));
			groundHistoryDataList.add(groundHistoryData);
			/*
			 * 为每一个listItem的标题数据赋值
			 */
			map = new HashMap<String, Object>();
			String[] check_time = cursor.getString(4).split(" ");
			if(check_time.length>1){
				map.put("test_time", "测试日期：" + check_time[0]);
			}else{
				map.put("test_time", "测试日期：" + cursor.getString(4));
			}
			map.put("test_value", "测试值：" + cursor.getString(5));
			map.put("conclusion", "结论：" + cursor.getString(8));
			int flag = cursor.getInt(10);
			map.put("flag", flag);
			list.add(map);
		}
		cursor.close();operationDB.db.close();
		groundHistoryAdapter = new GroundHistoryAdapter(getApplicationContext(), list);
		listview.setAdapter(groundHistoryAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), GroundHistoryDetail.class);
				intent.putExtra("values", groundHistoryDataList.get(arg2));
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}

		});

	}

	public void initNetList(String str) {
		groundHistoryBeanList = new ArrayList<HistoryDataGroundBean>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		listview = (ListView) findViewById(R.id.data_history_list);
		Map<String, Object> map;

		for (int i = 0; i < bean.size(); i++) {
			/*
			 * 为每一个listItem详细数据赋值
			 */
			groundHistoryBean = new HistoryDataGroundBean();
			groundHistoryBean.setCPGROUNDBEDEVENTID(bean.get(i).getCPGROUNDBEDEVENTID());
			groundHistoryBean.setTESTDATE(bean.get(i).getTESTDATE());
			groundHistoryBean.setSETVALUE(bean.get(i).getSETVALUE());
			groundHistoryBean.setTESTVALUE(bean.get(i).getTESTVALUE());
			groundHistoryBean.setCONCLUSION(bean.get(i).getCONCLUSION());
			groundHistoryBean.setYEAR(bean.get(i).getYEAR());
			groundHistoryBean.setHALFYEAR(bean.get(i).getHALFYEAR());
			groundHistoryBean.setINSPECTOR(bean.get(i).getINSPECTOR());
			groundHistoryBeanList.add(groundHistoryBean);
			/*
			 * 为每一个listItem的标题数据赋值
			 */
			map = new HashMap<String, Object>();
			map.put("test_time", "测试时间：" + bean.get(i).getTESTDATE());
			map.put("test_value", "测试值：" + bean.get(i).getTESTVALUE());
			map.put("conclusion", "结论：" + bean.get(i).getCONCLUSION());
			map.put("flag", 1);
			list.add(map);
		}

		groundHistoryAdapter = new GroundHistoryAdapter(getApplicationContext(), list);
		listview.setAdapter(groundHistoryAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), GroundHistoryDetailQuery.class);
				intent.putExtra("values", groundHistoryBeanList.get(arg2));
				startActivityForResult(intent, 2000);

			}

		});

	}

	private void init() {
		
		sure.setTag(1);
		sure.setOnClickListener(this);
		setOnclick(isOnclick);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = (Integer) v.getTag();
		switch (tag) {
		case 1:
			String startdate = te_search_startdate.getText().toString();
			String enddate = te_search_enddate.getText().toString();
			Request request = new Request(handler);
			if (Tools.isNetworkAvailable(this, true)) {
				request.InspectionRecordQuery("1118", userid, startdate, enddate, "", "", "");
				startProgressDialog();
				isOnclick = !isOnclick;
				setOnclick(isOnclick);
			}
			scrollView.clickRightButton(search.getMeasuredWidth());
			break;
		case 8:
			// new DatePickerDialog(GroundHistory.this, new OnDateSetListener()
			// {
			//
			// public void onDateSet(DatePicker view, int year,
			// int monthOfYear, int dayOfMonth) {
			// int n = monthOfYear + 1;
			// te_search_startdate.setText(year + "-" + n + "-"
			// + dayOfMonth);
			// }
			// }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
			// c.get(Calendar.DAY_OF_MONTH)).show();
			Tools.setDateDialog(GroundHistory.this, c, te_search_startdate);
			break;
		case 9:
			if ("起始日期".equals(te_search_startdate.getText().toString())) {
				Toast.makeText(GroundHistory.this, "请先选择起始日期！", 1000).show();
			} else {
				start_date_string = te_search_startdate.getText().toString();
				Tools.setEndDateDialog(GroundHistory.this, c, start_date_string, te_search_enddate);
			}
			break;
		}
	}

	Runnable resumelist = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			initList();
			stopProgressDialog();
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
//		startProgressDialog();
		if (groundHistoryBeanList != null) {

		} else {
			handler.post(resumelist);
			init();
		}
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TypeQuest.GROUND_STARTYEAR:
			if (resultCode == Activity.RESULT_OK) {
				te_search_startdate.setText((CharSequence) et);
			}
			break;
		case TypeQuest.GROUND_ENDYEAR:
			if (resultCode == Activity.RESULT_OK) {
				te_search_enddate.setText((CharSequence) et);
			}
			break;
		case 2000:
			Message message = new Message();
			message.arg1 = 2000;
			handler.sendMessage(message);
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
