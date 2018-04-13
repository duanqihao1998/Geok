package com.geok.langfang.pipeline.dataacquisition;

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
import com.geok.langfang.jsonbean.HistoryDataNaturalBean;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.R.id;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.DialogActivity;
import com.geok.langfang.tools.MyHorizontalScrollView;
import com.geok.langfang.tools.SizeCallbackForMenu;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.tools.TreeView;
import com.geok.langfang.tools.TypeQuest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author wuchangming 自然电位的历史记录类
 * 
 */
public class NaturalHistory extends Activity implements OnClickListener {

	List<HistoryDataNaturalBean> bean;
	String userid;
	MyApplication myApplication;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.arg1 == 15) {
				String str = msg.getData().getString("result");
				getSharedPreferences("values", MODE_PRIVATE).edit().putString("values", str)
						.commit();
				bean = Json.HistoryDataNatural(str);
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
	String lineId, markId, markerstation, markerstation2;
	NaturalHistoryAdapter naturalHistoryAdapter;
	NaturalHistoryData naturalHistoryData;
	ArrayList<NaturalHistoryData> naturalHistoryDataList;
	HistoryDataNaturalBean naturalHistoryBean;
	ArrayList<HistoryDataNaturalBean> naturalHistoryBeanList;
	private ListView listview;
	OperationDB operationDB;
	Cursor cursor;
	Button back, main;
	Button sure;
	boolean isOnclick = false;

	private Calendar c = null;
	private EditText et = null;
	TextView te_search_line, te_search_startpile, te_search_endpile, te_search_startdate,
			te_search_enddate;
	RelativeLayout re_search_line, re_search_startpile, re_search_endpile, re_search_startdate,
			re_search_enddate;
	RelativeLayout data_history_search_r;
	String start_date_string;
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
		setContentView(inflater.inflate(R.layout.dataacquisition_history, null));

		myApplication = new MyApplication(this);
		userid = myApplication.userid;
		scrollView = (MyHorizontalScrollView) findViewById(R.id.myScrollView);
		leftMenu = findViewById(R.id.leftmenu);
		re_search_line = (RelativeLayout) findViewById(R.id.re_search_line);
		te_search_line = (TextView) findViewById(R.id.te_search_line);
		re_search_startpile = (RelativeLayout) findViewById(R.id.re_search_startpile);
		te_search_startpile = (TextView) findViewById(R.id.te_search_startpile);
		re_search_endpile = (RelativeLayout) findViewById(R.id.re_search_endpile);
		te_search_endpile = (TextView) findViewById(R.id.te_search_endpile);
		re_search_startdate = (RelativeLayout) findViewById(R.id.re_search_startdate);
		te_search_startdate = (TextView) findViewById(R.id.te_search_startdate);
		re_search_enddate = (RelativeLayout) findViewById(R.id.re_search_enddate);
		te_search_enddate = (TextView) findViewById(R.id.te_search_enddate);
		re_search_line.setTag(5);
		re_search_startpile.setTag(6);
		re_search_endpile.setTag(7);
		re_search_startdate.setTag(8);
		re_search_enddate.setTag(9);
		re_search_line.setOnClickListener(this);
		re_search_startpile.setOnClickListener(this);
		re_search_endpile.setOnClickListener(this);
		re_search_startdate.setOnClickListener(this);
		re_search_enddate.setOnClickListener(this);
		rightMenu = findViewById(R.id.rightmenu);
		sure = (Button) findViewById(id.sure);
		setOnclick(isOnclick);
		historyList = inflater.inflate(R.layout.dataacquisition_history_list, null);
		back = (Button) historyList.findViewById(R.id.back);
		main = (Button) historyList.findViewById(R.id.main);
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
				Tools.backMain(NaturalHistory.this);
			}
		});
		data_history_search_r = (RelativeLayout) historyList
				.findViewById(R.id.data_history_search_r);
		search = (ImageButton) historyList.findViewById(R.id.data_history_search);
		data_history_search_r.setOnClickListener(new OnClickListener() {
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
		final View[] children = new View[] { leftView, historyList, rightView };
		// 初始化滚动布局
		scrollView.initViews(children, new SizeCallbackForMenu(search), leftMenu, rightMenu);
		init();
	}

	public void initList() {
		startProgressDialog();
		naturalHistoryDataList = new ArrayList<NaturalHistoryData>();
		operationDB = new OperationDB(getApplicationContext());
		cursor = operationDB.DBselect(Type.NATURAL_POTENTIAL);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		listview = (ListView) findViewById(R.id.data_history_list);
		Map<String, Object> map;
		stopProgressDialog();
		for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
//		while (cursor.moveToNext()) {
			/*
			 * 为每一个listItem详细数据赋值
			 */
			naturalHistoryData = new NaturalHistoryData();
			naturalHistoryData.setGuid(cursor.getString(1));
			naturalHistoryData.setUserid(cursor.getString(2));
			naturalHistoryData.setYear(cursor.getString(3));
			naturalHistoryData.setMonth(cursor.getString(4));
			naturalHistoryData.setLine(cursor.getString(5));
			naturalHistoryData.setPile(cursor.getString(6));
			naturalHistoryData.setValue(cursor.getString(7));
			naturalHistoryData.setTesttime(cursor.getString(8));
			naturalHistoryData.setVoltage(cursor.getString(10));
			naturalHistoryData.setTemperature(cursor.getString(11));
			naturalHistoryData.setWeather(cursor.getString(12));
			naturalHistoryData.setRemarks(cursor.getString(9));
			naturalHistoryData.setLineid(cursor.getString(13));
			naturalHistoryData.setPileid(cursor.getString(14));
			naturalHistoryData.setIsupload(cursor.getInt(15));
			naturalHistoryDataList.add(naturalHistoryData);

			/*
			 * 为每一个listItem的标题数据赋值
			 */
			map = new HashMap<String, Object>();
			String[] check_time = cursor.getString(8).split(" ");
			if(check_time.length>1){
				map.put("test_time", "测试时间：" + check_time[0]);
			}else{
				map.put("test_time", "测试时间：" + cursor.getString(8));
			}
			map.put("line_pile", "线桩位置：" + cursor.getString(5) + cursor.getString(6));
			map.put("weather", "天气：" + cursor.getString(12));
			int flag = cursor.getInt(15);
			map.put("flag", flag);
			list.add(map);
		}
		cursor.close();operationDB.db.close();
		naturalHistoryAdapter = new NaturalHistoryAdapter(getApplicationContext(), list);
		listview.setAdapter(naturalHistoryAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), NaturalHistoryDetail.class);
				intent.putExtra("values", naturalHistoryDataList.get(arg2));
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}

		});

	}

	public void initNetList(String str) {
		naturalHistoryBeanList = new ArrayList<HistoryDataNaturalBean>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		listview = (ListView) findViewById(R.id.data_history_list);
		Map<String, Object> map;

		for (int i = 0; i < bean.size(); i++) {
			/*
			 * 为每一个listItem详细数据赋值
			 */

			naturalHistoryBean = new HistoryDataNaturalBean();
			naturalHistoryBean.setYEAR(bean.get(i).getYEAR());
			naturalHistoryBean.setMONTH(bean.get(i).getMONTH());
			naturalHistoryBean.setACINTERFERENCEVOLTAGE(bean.get(i).getACINTERFERENCEVOLTAGE());
			naturalHistoryBean.setWEATHER(bean.get(i).getWEATHER());
			naturalHistoryBean.setLINELOOP(bean.get(i).getLINELOOP());
			naturalHistoryBean.setMARKERNAME(bean.get(i).getMARKERNAME());
			naturalHistoryBean.setREMARK(bean.get(i).getREMARK());
			naturalHistoryBean.setTEMPERATURE(bean.get(i).getTEMPERATURE());
			naturalHistoryBean.setTESTDATE(bean.get(i).getTESTDATE());
			naturalHistoryBean.setINSPECTOR(bean.get(i).getINSPECTOR());
			naturalHistoryBean.setVOLTAGE(bean.get(i).getVOLTAGE());
			naturalHistoryBeanList.add(naturalHistoryBean);
			/*
			 * 为每一个listItem的标题数据赋值
			 */
			map = new HashMap<String, Object>();
			map.put("test_time", "测试时间：" + bean.get(i).getTESTDATE());
			map.put("line_pile", "线桩位置：" + bean.get(i).getLINELOOP() + bean.get(i).getMARKERNAME());
			map.put("weather", "天气：" + bean.get(i).getWEATHER());
			map.put("flag", 1);
			list.add(map);
		}
		naturalHistoryAdapter = new NaturalHistoryAdapter(getApplicationContext(), list);
		listview.setAdapter(naturalHistoryAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), NaturalHistoryDetailQuery.class);
				intent.putExtra("values", naturalHistoryBeanList.get(arg2));
				startActivityForResult(intent, 2000);
			}
		});
	}

	private void init() {
		
		sure.setTag(1);
		sure.setOnClickListener(this);
		setOnclick(isOnclick);
	}

	public void setOnclick(boolean isonclick){
		sure.setClickable(isonclick);
		re_search_startdate.setClickable(isonclick);
		re_search_enddate.setClickable(isonclick);
		re_search_line.setClickable(isonclick);
		re_search_startpile.setClickable(isonclick);
		re_search_endpile.setClickable(isonclick);
	
}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = (Integer) v.getTag();

		switch (tag) {
		case 1:
			String searchline = lineId;
			String startpile = markerstation;
			String endpile = markerstation2;
			String startdate = te_search_startdate.getText().toString();
			String enddate = te_search_enddate.getText().toString();
			Request request = new Request(handler);
			// request.InspectionRecordQuery("1115", userid,
			// "75199391-c3f3-48c5-a1cb-c5096cd14208", "178384.0254",
			// "182130.4184", "2013-3-3", "2013-3-3");
			if (Tools.isNetworkAvailable(this, true)) {
				request.InspectionRecordQuery("1115", userid, searchline, startpile, endpile,
						startdate, enddate);
				startProgressDialog();
				isOnclick = !isOnclick;
				setOnclick(isOnclick);
			}
			scrollView.clickRightButton(search.getMeasuredWidth());
			break;
		case 5:
			Intent line = new Intent();
			line.setClass(NaturalHistory.this, TreeView.class);
			line.putExtra("flag", TypeQuest.NATURAL_LINE);
			startActivityForResult(line, TypeQuest.NATURAL_LINE);
			break;
		case 6:
			if (lineId == null) {
				Toast.makeText(getApplicationContext(), "请先选择线路", 1000).show();
			} else {
				Intent pile = new Intent();
				pile.setClass(NaturalHistory.this, DialogActivity.class);
				pile.putExtra("flag", TypeQuest.NATURAL_PILE);
				pile.putExtra("lineid", lineId);
				startActivityForResult(pile, TypeQuest.NATURAL_PILE);
			}
			break;
		case 7:
			if (lineId == null) {
				Toast.makeText(getApplicationContext(), "请先选择线路", 1000).show();
			} else {
				Intent pile2 = new Intent();
				pile2.setClass(NaturalHistory.this, DialogActivity.class);
				pile2.putExtra("flag", TypeQuest.NATURAL_ENDPILE);
				pile2.putExtra("lineid", lineId);
				startActivityForResult(pile2, TypeQuest.NATURAL_ENDPILE);
			}
			break;
		case 8:
			Tools.setDateDialog(NaturalHistory.this, c, te_search_startdate);
			break;
		case 9:
			if ("起始日期".equals(te_search_startdate.getText().toString())) {
				Toast.makeText(NaturalHistory.this, "请先选择起始日期！", 1000).show();
			} else {
				start_date_string = te_search_startdate.getText().toString();
				Tools.setEndDateDialog(NaturalHistory.this, c, start_date_string, te_search_enddate);
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
		if (naturalHistoryBeanList != null) {
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
		case TypeQuest.NATURAL_LINE:
			if (resultCode == Activity.RESULT_OK) {
				te_search_line.setText(data.getStringExtra("line"));
				lineId = data.getStringExtra("lineId");
				te_search_startpile.setText("起始桩号");
				te_search_endpile.setText("终止桩号");
			}
			break;

		case TypeQuest.NATURAL_PILE:
			if (resultCode == Activity.RESULT_OK) {
				te_search_startpile.setText(data.getStringExtra("pile"));
				markId = data.getStringExtra("markId");
				markerstation = data.getStringExtra("markerstation");
			}
			break;
		case TypeQuest.NATURAL_ENDPILE:
			if (resultCode == Activity.RESULT_OK) {
				te_search_endpile.setText(data.getStringExtra("pile"));
				markId = data.getStringExtra("markId");
				markerstation2 = data.getStringExtra("markerstation");
			}
			break;
		case TypeQuest.NATURAL_TESTTIME:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("test_time"))) {
					te_search_startdate.setText("2005-1-1");

				} else {
					te_search_startdate.setText((CharSequence) et);
				}
			}
			break;
		case TypeQuest.NATURAL_TESTTIME2:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("test_time"))) {
					te_search_enddate.setText("2030-12-31");

				} else {
					te_search_enddate.setText((CharSequence) et);
				}
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
