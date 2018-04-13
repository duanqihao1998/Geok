package com.geok.langfang.pipeline.statistics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.achartengine.chart.BarChart.Type;
import org.achartengine.ChartFactory;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONObject;

import com.geok.langfang.json.Json;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.R.color;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.Tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StatisticsQualifiedRate extends Activity implements OnClickListener {

	Button back, main;// 返回，主界面
	LinearLayout startDate_layout, endDate_layout, person_layout;
	RelativeLayout eventup_startdate, eventup_enddate, eventup_person;
	TextView startDate_text, endDate_text, person_text;
	Button eventup_search;// 搜索按钮
	Tools tools = new Tools();
	AbstractDemoChart abstractDemoChart = new AbstractDemoChart();

	Calendar c = null;
	String startDate = null;
	String endDate = null;
	String userId = null;
	Request request;
	String userData, statisticsData;
	TreeView view;
	Map map;
	public static List<String> userCheckList = new ArrayList<String>();// 选择的人员集合

	MyApplication myApplication;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tools.stopProgressDialog(StatisticsQualifiedRate.this);
			switch (msg.arg1) {
			case 42:
				if (msg.getData().getInt("flag") == 1) {
					userData = msg.getData().getString("result");
					if (!(userData.equals("-1"))) {
						myApplication.person = userData;
						showUserDialog();
					} else {
						Toast.makeText(StatisticsQualifiedRate.this, "没有人员数据", 1000).show();
					}

				} else {
					Toast.makeText(StatisticsQualifiedRate.this, "请检查网络", 1000).show();
				}
				break;
			case 45:
				String data = msg.getData().getString("result");
				int flag = msg.getData().getInt("flag");
				if (flag == 1) {
					if (!data.equals("-1")) {
						map = Json.getStatisticsQualifiedRate(data);
						if (map.size() > 0) {
							showCharts(map);
						} else {
							Toast.makeText(getApplicationContext(), "无数据！", 1000).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), "没有查询到所需的数据", 1000).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "查询失败，请重试！", 1000).show();
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics_qualifiedrate);
		myApplication = new MyApplication(this);
		c = Calendar.getInstance();
		request = new Request(handler);
		userId = myApplication.userid;
		initView();
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
				Tools.backMain(StatisticsQualifiedRate.this);
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

	@Override
	public void onClick(View v) {
		String startDate = null;
		switch (v.getId()) {
		case R.id.eventup_startdate:
			Tools.setDateDialog(StatisticsQualifiedRate.this, c, startDate_text);
			break;

		case R.id.eventup_enddate:
			startDate = startDate_text.getText().toString().trim();
			Tools.setEndDateDialog(StatisticsQualifiedRate.this, c, startDate, endDate_text);
			endDate = startDate_text.getText().toString().trim();
			break;

		case R.id.eventup_person:
			if (myApplication.person.equals("")
					||myApplication.person.contains("ERR")) {
				if (Tools.isNetworkAvailable(this, true)) {
					request.SelectPersonRequest(myApplication.userid);
					tools.startProgressDialog(StatisticsQualifiedRate.this);
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
					Toast.makeText(StatisticsQualifiedRate.this, "请选择时间", 1000).show();
				} else {
					if (TreeView.userCheckList.size() > 0) {
						tools.startProgressDialog(StatisticsQualifiedRate.this);
						request.StatisticsQualifiedrateRequest(TreeView.getUserIdList(),
								startDate_text.getText().toString(), endDate_text.getText()
										.toString());
						// 此处查询合格率统计写死了userid
						// request.StatisticsQualifiedrateRequest(
						// "da95a4e0-4001-11e0-9e47-e41f13e34d74",
						// startDate_text.getText().toString(), endDate_text
						// .getText().toString());
					} else {
						Toast.makeText(StatisticsQualifiedRate.this, "没有选择人员", 1000).show();
					}
				}
			}
			break;
		// case R.id.eventup_search:
		// // double[] values = new double[]{14230, 12300, 14240, 15244};
		// List<double[]> values = new ArrayList<double[]>();
		// values.add(new double[]{28,22,132,14,10,13,32,42,15,42,28,35});
		// // values.add(new double[] { 14230, 12300, 14240, 15244});//第一种柱子的数值
		// // values.add(new double[] { 5230, 7300, 9240, 10540});//第二中柱子的数值
		// // showCharts(map);
		// break;
		}
	}

	public void showUserDialog() {
		Dialog dialog = new Dialog(StatisticsQualifiedRate.this);
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

	public void showCharts(Map map) {

		String[] strValues = new String[map.size()];
		// double[] values = new double[map.size()];
		List<double[]> values = new ArrayList<double[]>();
		double[] valuesitem = new double[map.size()];
		Iterator iter = map.entrySet().iterator();
		int n = 0;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			strValues[n] = (String) entry.getKey();
			// values[n] = (Double) entry.getValue();
			valuesitem[n] = (Double) entry.getValue();
			values.add(valuesitem);
			n++;
		}

		String[] titles = new String[] { "\n" + startDate_text.getText().toString().trim() + "~"
				+ endDate_text.getText().toString().trim() + "的合格率" };
		int[] colors = new int[] { Color.BLUE };
		XYMultipleSeriesRenderer renderer = abstractDemoChart.buildBarRenderer(colors);
		abstractDemoChart.setChartSettings(renderer, "合格率统计图", "巡检员", "合格率", 0.5,
				strValues.length + 0.5, 0, 100, Color.GRAY, Color.LTGRAY);
		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
		// renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
		renderer.setXLabels(0);
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setPanEnabled(true, false);
		// renderer.setZoomEnabled(false,false);
		renderer.setApplyBackgroundColor(true);
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0.5f);
		String[] names = { "张三", "李四", "王五", "马六", "七七", "张三", "李四", "王五", "马六", "七七", "马六", "七七" };
		// for(int i=1; i<=names.length; i++)
		// {
		// for(int j=i-1; j<names.length; j++){
		// renderer.addXTextLabel(i, names[j]);
		// break;
		// }
		// }
		for (int i = 1; i <= strValues.length; i++) {
			for (int j = i - 1; j < strValues.length; j++) {
				renderer.addXTextLabel(i, strValues[j]);
				break;
			}
		}
		// Intent intent = ChartFactory.getBarChartIntent(this,
		// abstractDemoChart.buildBarDataset(titles, values), renderer,
		// Type.STACKED);
		Intent intent = ChartFactory.getBarChartIntent(this,
				abstractDemoChart.buildBarDataset(titles, values), renderer, Type.STACKED);
		startActivity(intent);
	}
}
