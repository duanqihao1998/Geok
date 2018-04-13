package com.geok.langfang.pipeline.statistics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.achartengine.ChartFactory;
import org.achartengine.renderer.DefaultRenderer;
import org.json.JSONArray;
import org.json.JSONObject;

import com.geok.langfang.json.Json;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.Tools;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EventupStatisticsActivity extends Activity implements OnClickListener {

	Button back, main;// 返回，主界面
	LinearLayout startDate_layout, endDate_layout, person_layout;
	RelativeLayout eventup_startdate, eventup_enddate, eventup_person;
	TextView startDate_text, endDate_text, person_text;
	Button eventup_search;// 搜索按钮

	Calendar c = null;

	private static CustomProgressDialog progressDialog = null; // 自定义的进度条的对象
	/**
	 * 统计数据
	 */
	Map map;
	/**
	 * 数据请求
	 */
	Request request;

	MyApplication myApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventupstatistics);
		myApplication = new MyApplication(this);
		c = Calendar.getInstance();
		initView();

		request = new Request(handler);
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
				Tools.backMain(EventupStatisticsActivity.this);
			}
		});
	}

	public void initView() {
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);

		startDate_layout = (LinearLayout) findViewById(R.id.startDate_layout);
		endDate_layout = (LinearLayout) findViewById(R.id.endDate_layout);
		person_layout = (LinearLayout) findViewById(R.id.person_layout);

		eventup_startdate = (RelativeLayout) findViewById(R.id.eventup_startdate);
		eventup_enddate = (RelativeLayout) findViewById(R.id.eventup_enddate);
		eventup_person = (RelativeLayout) findViewById(R.id.eventup_person);

		eventup_startdate.setOnClickListener(this);
		eventup_enddate.setOnClickListener(this);
		eventup_person.setOnClickListener(this);

		startDate_text = (TextView) findViewById(R.id.startDate_text);
		endDate_text = (TextView) findViewById(R.id.endDate_text);
		person_text = (TextView) findViewById(R.id.person_text);

		eventup_search = (Button) findViewById(R.id.eventup_search);
		eventup_search.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
		return super.onKeyDown(keyCode, event);

	}

	@SuppressWarnings("unused")
	@Override
	public void onClick(View v) {
		String startDate = null;
		switch (v.getId()) {
		case R.id.eventup_startdate:

			Tools.setDateDialog(EventupStatisticsActivity.this, c, startDate_text);

			break;
		case R.id.eventup_enddate:
			startDate = startDate_text.getText().toString().trim();
			Tools.setEndDateDialog(EventupStatisticsActivity.this, c, startDate, endDate_text);
			break;
		case R.id.eventup_person:
			if (myApplication.person.equals("")
					||myApplication.person.contains("ERR")) {
				if (Tools.isNetworkAvailable(this, true)) {
					request.SelectPersonRequest(myApplication.userid);
					startProgressDialog();
				}
			} else {
				userData = myApplication.person;
				showUserDialog();
			}

			break;
		case R.id.eventup_search:
			if (Tools.isNetworkAvailable(this, true)) {
				if (startDate_text.getText().toString().equals("")
						|| endDate_text.getText().equals("")) {
					Toast.makeText(EventupStatisticsActivity.this, "请选择时间", 2000).show();
				} else {
					if (TreeView.userCheckList.size() > 0) {
						startProgressDialog();
						request.StatisticsProblemRequest(TreeView.getUserIdList(), startDate_text
								.getText().toString(), endDate_text.getText().toString());
					} else {
						Toast.makeText(EventupStatisticsActivity.this, "没有选择人员", 2000).show();
					}
				}
			}
			break;
		}
	}

	String userData;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			stopProgressDialog();
			switch (msg.arg1) {
			case 42:
				if (msg.getData().getInt("flag") == 1) {
					userData = msg.getData().getString("result");
					if (!userData.contains("ERR")) {
						myApplication.person = userData;
						showUserDialog();
					} else {
						Toast.makeText(EventupStatisticsActivity.this, "出现异常，请与管理员联系", 1000).show();
					}
				} else {
					Toast.makeText(EventupStatisticsActivity.this, "网络连接错误", 1000).show();
				}
				break;
			case 43:

				String data = msg.getData().getString("result");
				int flag = msg.getData().getInt("flag");
				if (flag == 1) {
					if (!data.equals("-1")) {
						map = Json.getStatisticsProblem(data);
						if (map.size() > 0) {
							showCharts(map);
						} else {
							Toast.makeText(getApplicationContext(), "无数据", 1000).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), "没有查询到所需的数据", 1000).show();
						// StatisticsUnqualifiedPerson.this.finish();
					}
				} else {
					Toast.makeText(getApplicationContext(), "查询失败，请重试", 1000).show();
					// StatisticsUnqualifiedPerson.this.finish();
				}
				break;
			}

		}

	};
	TreeView view;

	public void showUserDialog() {
		Dialog dialog = new Dialog(EventupStatisticsActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.userselection);
		view = (TreeView) dialog.findViewById(R.id.userSelectionView);
		getTreeList();
		view.initialData();
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				person_text.setText(TreeView.getUserNameList());
			}
		});
		dialog.show();
	}

	public void getTreeList() {
		try {
			JSONArray ja = new JSONArray(userData);
			if(ja.length()>0){
				for(int i=ja.length()-1;i>=0;i--){
					TreeElement element = new TreeElement(ja.getJSONObject(i).getString("eventid"), ja
							.getJSONObject(i).getString("name"));
					element.setNotetype(ja.getJSONObject(0).getString("notetype"));
					addChild(element, ja.getJSONObject(0));
					view.mPdfOutlinesCount.add(element);
				}
			}
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// TYPE改成notetype,CHILDREDS改成mysubChild,org改成1；notetype为1时时公司，为0时为个人
	public void addChild(TreeElement element, JSONObject ob) {
		try {
			// if ("1".equals(ob.getString("notetype").trim())) {
			JSONArray jsonArray = ob.getJSONArray("mysubChild");
			if (jsonArray.length() > 0) {
				for (int i = jsonArray.length()-1; i >=0; i--) {
					JSONObject ob1 = jsonArray.getJSONObject(i);
					TreeElement element1 = new TreeElement(ob1.getString("eventid"),
							ob1.getString("name"));
					element1.setNotetype(ob1.getString("notetype"));
					
					JSONArray jsonArray2 = ob1.getJSONArray("mysubChild");
					if(jsonArray2.length()>0){
						addChild(element1, ob1);
					}
					element.addChild(element1);
					
				}
			}
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this).setMessage("加载中...");
		}
		progressDialog.show();
	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	public void showCharts(Map map) {

		String[] strValues = new String[map.size()];// new
													// String[]{"管道占压","第三方施工",
													// "反打孔盗油气", "站场阀室设备损坏",
													// "线路设施损坏", "地质灾害", "其它"};
		double[] values = new double[map.size()];

		Iterator iter = map.entrySet().iterator();
		int i = 0;
		int[] colors = new int[map.size()];
		int[] colors1 = new int[] { Color.BLUE, Color.GREEN, Color.CYAN, Color.YELLOW,
				Color.MAGENTA, Color.RED, Color.LTGRAY, Color.WHITE };
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			strValues[i] = (String) entry.getKey();
			values[i] = (Double) entry.getValue();
			colors[i] = colors1[i];
			i++;

		}
		AbstractDemoChart dc = new AbstractDemoChart();
		DefaultRenderer renderer = dc.buildCategoryRenderer(colors, "事件上报统计图");
		renderer.setZoomButtonsVisible(true);
		renderer.setZoomEnabled(true);
		renderer.setChartTitle("事件上报统计图");
		renderer.setChartTitleTextSize(25);
		renderer.setDisplayValues(true);
		renderer.setShowLabels(true);
		renderer.setLabelsTextSize(20);
		Intent intent = ChartFactory.getPieChartIntent(this,
				dc.buildCategoryDataset(strValues, values), renderer, "统计图");
		startActivity(intent);
	}
}
