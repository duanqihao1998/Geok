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
import com.geok.langfang.jsonbean.HistoryDataCorrosiveBean;
import com.geok.langfang.pipeline.MainView;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.R.id;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CompareDate;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.DialogActivity;
import com.geok.langfang.tools.MyHorizontalScrollView;
import com.geok.langfang.tools.SizeCallbackForMenu;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.tools.TreeView;
import com.geok.langfang.tools.TypeQuest;

/**
 * 
 * @author wuchangming 防腐层检漏的历史记录类
 * 
 */
public class CorrosiveHistory extends Activity implements OnClickListener {

	List<HistoryDataCorrosiveBean> bean; // 防腐层历史记录数据
	String userid;
	MyApplication myApplication;
	boolean isOnclick = false;
	/**
	 * 异步处理机制 处理其他信息
	 */
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			stopProgressDialog();
			if (msg.arg1 == 17) {
				if (msg.getData().getInt("flag") == 1) {
					String str = msg.getData().getString("result");
					if (!str.contains("ERR")) {
						if (!str.equals("-1")) {
							getSharedPreferences("values", MODE_PRIVATE).edit().putString("values", str).commit();
							bean = Json.HistoryDataCorrosive(str);
							initNetList(str);
						}else{
							Toast.makeText(CorrosiveHistory.this, "无符合的数据，请重新选择查询条件", 1000).show();
						}
					} else {
						Toast.makeText(CorrosiveHistory.this, "返回结果错误，请联系管理员", 1000).show();
					}
				} else {
					Toast.makeText(CorrosiveHistory.this, "与服务器断开，请检查网络或IP", 1000).show();
				}
			}
			if (msg.arg1 == 2000) {
				String str2 = getSharedPreferences("values", MODE_PRIVATE).getString("values", "-1");
				initNetList(str2);
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
	// 防腐层检测历史记录的适配器
	CorrosiveHistoryAdapter corrosiveHistoryAdapter;
	CorrosiveHistoryData corrosiveHistoryData;
	ArrayList<CorrosiveHistoryData> corrosiveHistoryDataList;
	HistoryDataCorrosiveBean corrosiveHistoryBean;
	ArrayList<HistoryDataCorrosiveBean> corrosiveHistoryBeanList;
	private ListView listview;
	OperationDB operationDB;
	Cursor cursor;
	private Calendar c = null;
	private EditText et = null;
	String lineId, markId, markerstation, markerstation2;
	TextView te_search_line, te_search_startpile, te_search_endpile, te_search_startdate,
			te_search_enddate;
	RelativeLayout re_search_line, re_search_startpile, re_search_endpile, re_search_startdate,
			re_search_enddate;
	RelativeLayout data_history_search_r;
	String start_date_string;
	Request request;
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
		request = new Request(handler);
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
		historyList = inflater.inflate(R.layout.dataacquisition_history_list, null);

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

		Button back = (Button) findViewById(R.id.back);
		Button main = (Button) findViewById(R.id.main);
		back.setTag(30);
		main.setTag(31);
		back.setOnClickListener(this);
		main.setOnClickListener(this);
		CompareDate compareDate = new CompareDate();
		init();
	}

	/**
	 * 查询本地数据列表
	 */
	public void initList() {
		startProgressDialog();
		corrosiveHistoryDataList = new ArrayList<CorrosiveHistoryData>();
		operationDB = new OperationDB(getApplicationContext());
		cursor = operationDB.DBselect(Type.CORROSIVE);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		listview = (ListView) findViewById(R.id.data_history_list);
		Map<String, Object> map;
		stopProgressDialog();
		for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
//		while (cursor.moveToNext()) {
			/*
			 * 为每一个listItem详细数据赋值
			 */
			corrosiveHistoryData = new CorrosiveHistoryData();
			corrosiveHistoryData.setGuid(cursor.getString(1));
			corrosiveHistoryData.setUserid(cursor.getString(2));
			corrosiveHistoryData.setLine(cursor.getString(3));
			corrosiveHistoryData.setPile(cursor.getString(4));
			corrosiveHistoryData.setOffset(cursor.getString(5));
			corrosiveHistoryData.setRepair_obj(cursor.getString(6));
			corrosiveHistoryData.setCheck_date(cursor.getString(7));
			corrosiveHistoryData.setClockposition(cursor.getString(8));
			corrosiveHistoryData.setSoil(cursor.getString(9));
			corrosiveHistoryData.setDamage_des(cursor.getString(10));
			corrosiveHistoryData.setArea(cursor.getString(11));
			corrosiveHistoryData.setCorrosion_des(cursor.getString(12));
			corrosiveHistoryData.setCorrosion_area(cursor.getString(13));
			corrosiveHistoryData.setCorrosion_num(cursor.getString(14));
			corrosiveHistoryData.setPitdepthmax(cursor.getString(15));
			corrosiveHistoryData.setPitdepthmin(cursor.getString(16));
			corrosiveHistoryData.setRepair_situation(cursor.getString(17));
			corrosiveHistoryData.setRepair_date(cursor.getString(18));
			corrosiveHistoryData.setDamage_type(cursor.getString(19));
			corrosiveHistoryData.setRepair_type(cursor.getString(20));
			corrosiveHistoryData.setPile_info(cursor.getString(21));
			corrosiveHistoryData.setRemarks(cursor.getString(22));
			corrosiveHistoryData.setLineid(cursor.getString(23));
			corrosiveHistoryData.setPileid(cursor.getString(24));
			corrosiveHistoryData.setIsupload(cursor.getInt(25));
			corrosiveHistoryDataList.add(corrosiveHistoryData);

			/*
			 * 为每一个listItem的标题数据赋值
			 */
			map = new HashMap<String, Object>();
			String[] check_time = cursor.getString(7).split(" ");
			if(check_time.length>1){
				map.put("check_date", "检漏时间：" + check_time[0]);
			}else{
				map.put("check_date", "检漏时间：" + cursor.getString(7));
			}
			
			
			map.put("line_pile", "线桩位置：" + cursor.getString(3) + cursor.getString(4));
			map.put("repair_type", "修复类型：" + cursor.getString(20));
			int flag = cursor.getInt(25);
			map.put("flag", flag);
			list.add(map);
		}
		cursor.close();operationDB.db.close();
		operationDB.db.close();
		corrosiveHistoryAdapter = new CorrosiveHistoryAdapter(getApplicationContext(), list);
		listview.setAdapter(corrosiveHistoryAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// 进入相应的记录详情
				intent.setClass(getApplicationContext(), CorrosiveHistoryDetail.class);
				intent.putExtra("values", corrosiveHistoryDataList.get(arg2));
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}

		});

	}

	/**
	 * 服务器数据查询结果列表
	 * 
	 * @param str
	 */
	public void initNetList(String str) {
		corrosiveHistoryBeanList = new ArrayList<HistoryDataCorrosiveBean>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		listview = (ListView) findViewById(R.id.data_history_list);
		Map<String, Object> map;

		for (int i = 0; i < bean.size(); i++) {
			/*
			 * 为每一个listItem详细数据赋值
			 */
			corrosiveHistoryBean = new HistoryDataCorrosiveBean();
			corrosiveHistoryBean.setREPAIRTARGET(bean.get(i).getREPAIRTARGET());
			corrosiveHistoryBean.setLEAKHUNTINGDATE(bean.get(i).getLEAKHUNTINGDATE());
			corrosiveHistoryBean.setCLOCKPOSITION(bean.get(i).getCLOCKPOSITION());
			corrosiveHistoryBean.setSOIL(bean.get(i).getSOIL());
			corrosiveHistoryBean.setCOATINGFACE(bean.get(i).getCOATINGFACE());
			corrosiveHistoryBean.setCOATINGAREA(bean.get(i).getCOATINGAREA());
			corrosiveHistoryBean.setAPPEARENCEDESC(bean.get(i).getAPPEARENCEDESC());
			corrosiveHistoryBean.setPITAREA(bean.get(i).getPITAREA());
			corrosiveHistoryBean.setPITAMOUNT(bean.get(i).getPITAMOUNT());
			corrosiveHistoryBean.setPITDEPTHMAX(bean.get(i).getPITDEPTHMAX());
			corrosiveHistoryBean.setPITDEPTHMIN(bean.get(i).getPITDEPTHMIN());
			corrosiveHistoryBean.setCOATINGREPAIR(bean.get(i).getCOATINGREPAIR());
			corrosiveHistoryBean.setREPAIRDATE(bean.get(i).getREPAIRDATE());
			corrosiveHistoryBean.setDAMAGETYPE(bean.get(i).getDAMAGETYPE());
			corrosiveHistoryBean.setREPAIRTYPE(bean.get(i).getREPAIRTYPE());
			corrosiveHistoryBean.setREPAIRINFO(bean.get(i).getREPAIRINFO());
			corrosiveHistoryBean.setREMARK(bean.get(i).getREMARK());
			corrosiveHistoryBean.setINSPECTOR(bean.get(i).getINSPECTOR());
			corrosiveHistoryBean.setLINELOOP(bean.get(i).getLINELOOP());
			corrosiveHistoryBean.setMARKERNAME(bean.get(i).getMARKERNAME());
			corrosiveHistoryBean.setOFF(bean.get(i).getOFF());
			corrosiveHistoryBeanList.add(corrosiveHistoryBean);
			/*
			 * 为每一个listItem的标题数据赋值
			 */
			map = new HashMap<String, Object>();
			map.put("check_date", "检漏时间：" + bean.get(i).getLEAKHUNTINGDATE());
			map.put("line_pile", "线桩位置：" + bean.get(i).getLINELOOP() + bean.get(i).getMARKERNAME());
			map.put("repair_type", "修复类型：" + bean.get(i).getREPAIRTYPE());
			// int flag= cursor.getInt(13);
			map.put("flag", 1);
			list.add(map);
		}

		corrosiveHistoryAdapter = new CorrosiveHistoryAdapter(getApplicationContext(), list);
		listview.setAdapter(corrosiveHistoryAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				// 进入相应的记录详情
				intent.setClass(getApplicationContext(), CorrosiveHistoryDetailQuery.class);
				intent.putExtra("values", corrosiveHistoryBeanList.get(arg2));
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
		re_search_line.setClickable(isonclick);
		re_search_startpile.setClickable(isonclick);
		re_search_endpile.setClickable(isonclick);
		re_search_startdate.setClickable(isonclick);
		re_search_enddate.setClickable(isonclick);
	
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
			Tools.backMain(CorrosiveHistory.this);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case 1:
			String searchline = lineId;
			String startpile = markerstation;
			String endpile = markerstation2;
			String startdate = te_search_startdate.getText().toString();
			String enddate = te_search_enddate.getText().toString();
			if (Tools.isNetworkAvailable(this, true)) {
				request.InspectionRecordQuery("1117", userid, searchline, startpile, endpile,
						startdate, enddate);
				startProgressDialog();
				isOnclick = !isOnclick;
				setOnclick(isOnclick);
			}
			scrollView.clickRightButton(search.getMeasuredWidth());
			break;
		case 5:
			Intent line = new Intent();
			line.setClass(CorrosiveHistory.this, TreeView.class);
			line.putExtra("flag", TypeQuest.CORROSIVE_LINE);
			startActivityForResult(line, TypeQuest.CORROSIVE_LINE);
			break;
		case 6:
			if (lineId == null) {
				Toast.makeText(getApplicationContext(), "请先选择线路", 1000).show();
			} else {
				Intent pile = new Intent();
				pile.setClass(CorrosiveHistory.this, DialogActivity.class);
				pile.putExtra("flag", TypeQuest.CORROSIVE_PILE);
				pile.putExtra("lineid", lineId);
				if(!(te_search_startpile.getText().toString().equals("选择桩号"))){
					pile.putExtra("markName", te_search_startpile.getText().toString());
				}
				startActivityForResult(pile, TypeQuest.CORROSIVE_PILE);
			}
			break;
		case 7:
			if (lineId == null) {
				Toast.makeText(getApplicationContext(), "请先选择线路", 1000).show();
			} else {
				Intent pile2 = new Intent();
				pile2.setClass(CorrosiveHistory.this, DialogActivity.class);
				pile2.putExtra("flag", TypeQuest.CORROSIVE_ENDPILE);
				pile2.putExtra("lineid", lineId);
				if(!(te_search_endpile.getText().toString().equals("选择桩号"))){
					pile2.putExtra("markName", te_search_endpile.getText().toString());
				}
				startActivityForResult(pile2, TypeQuest.CORROSIVE_ENDPILE);
			}
			break;
		case 8:
			Tools.setDateDialog(CorrosiveHistory.this, c, te_search_startdate);
			break;
		case 9:
			if ("起始日期".equals(te_search_startdate.getText().toString())) {
				Toast.makeText(CorrosiveHistory.this, "请先选择起始日期！", 1000).show();
			} else {
				start_date_string = te_search_startdate.getText().toString();
				Tools.setEndDateDialog(CorrosiveHistory.this, c, start_date_string,
						te_search_enddate);
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
		if (corrosiveHistoryBeanList != null) {

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
		case TypeQuest.CORROSIVE_LINE:
			if (resultCode == Activity.RESULT_OK) {
				te_search_line.setText(data.getStringExtra("line"));
				lineId = data.getStringExtra("lineId");
				te_search_startpile.setText("起始桩号");
				te_search_endpile.setText("终止桩号");
			}
			break;

		case TypeQuest.CORROSIVE_PILE:
			if (resultCode == Activity.RESULT_OK) {
				te_search_startpile.setText(data.getStringExtra("pile"));
				markId = data.getStringExtra("markId");
				markerstation = data.getStringExtra("markerstation");
			}
			break;
		case TypeQuest.CORROSIVE_ENDPILE:
			if (resultCode == Activity.RESULT_OK) {
				te_search_endpile.setText(data.getStringExtra("pile"));
				markId = data.getStringExtra("markId");
				markerstation2 = data.getStringExtra("markerstation");
			}
			break;
		// case TypeQuest.CORROSIVE_CHECK_DATE:
		// if (resultCode == Activity.RESULT_OK) {
		// if ("".equals(data.getStringExtra("check_date"))) {
		// te_search_startdate.setText("2005-1-1");
		//
		// } else {
		// te_search_startdate.setText((CharSequence) et);
		// }
		// }
		// break;
		// case TypeQuest.CORROSIVE_CHECK_DATE2:
		// if (resultCode == Activity.RESULT_OK) {
		// date = et.toString();
		// if ("".equals(data.getStringExtra("check_date"))) {
		// te_search_enddate.setText("2030-12-31");
		// } else {
		//
		// }
		// }
		// break;
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
