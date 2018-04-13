package com.geok.langfang.pipeline.statistics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.UnqualifiedPersonBean;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.map.ArcMap;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.Tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class StatisticsUnqualifiedPerson extends Activity implements OnClickListener {

	Button back, main;// 返回，主界面
	LinearLayout startDate_layout, endDate_layout, person_layout;
	RelativeLayout eventup_startdate, eventup_enddate, eventup_person;
	TextView startDate_text, endDate_text, person_text;
	Button eventup_search;// 搜索按钮
	Tools tools = new Tools();
	Request request;
	Calendar c = null;
	String startDate = null;
	String endDate = null;
	String userId = null;
	Map map;
	public static List<String> userCheckList = new ArrayList<String>();// 选择的人员集合
	String userData, statisticsData;
	TreeView view;
	List<UnqualifiedPersonBean> listBean = new ArrayList<UnqualifiedPersonBean>();
	UnqualifiedPersonBean bean = new UnqualifiedPersonBean();
	TextView numtext, department, name, rate;
	LinearLayout tablelinear;
	TableLayout table;
	MyApplication myApplication;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tools.stopProgressDialog(StatisticsUnqualifiedPerson.this);
			switch (msg.arg1) {
			case 42:
				if (msg.getData().getInt("flag") == 1) {
					userData = msg.getData().getString("result");
					if (!(userData.equals("-1"))) {
						myApplication.person = userData;
						showUserDialog();
					} else {
						Toast.makeText(StatisticsUnqualifiedPerson.this, "没有人员数据", 1000).show();
					}

				} else {
					Toast.makeText(StatisticsUnqualifiedPerson.this, "请检查网络", 1000).show();
				}
				break;
			case 46:
				String str = msg.getData().getString("result");
				int flag = msg.getData().getInt("flag");
				if (flag == 1) {
					if (!str.equals("-1")) {
						listBean = Json.getStatisticsUnqualifiedPerson(str);
						if (listBean.size() > 0) {
							showCharts();
						} else {
							Toast.makeText(getApplicationContext(), "无数据！", 1000).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), "没有查询到所需的数据", 1000).show();
						// StatisticsUnqualifiedPerson.this.finish();
					}
				} else {
					Toast.makeText(getApplicationContext(), "查询失败，请重试！", 1000).show();
					// StatisticsUnqualifiedPerson.this.finish();
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics_unqualifiedperson);
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
				Tools.backMain(StatisticsUnqualifiedPerson.this);
			}
		});
		tablelinear = (LinearLayout) findViewById(R.id.unqualifiedperson_include);
		table = (TableLayout) findViewById(R.id.unqualified_table1);
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

		// numtext = (TextView)findViewById(R.id.unqualified_num);
		// department = (TextView)findViewById(R.id.unqualified_department);
		// name = (TextView)findViewById(R.id.unqualified_inspector);
		// rate = (TextView)findViewById(R.id.unqualified_rate);
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
		switch (v.getId()) {
		case R.id.eventup_startdate:
			Tools.setDateDialog(StatisticsUnqualifiedPerson.this, c, startDate_text);
			break;

		case R.id.eventup_enddate:
			startDate = startDate_text.getText().toString().trim();
			Tools.setEndDateDialog(StatisticsUnqualifiedPerson.this, c, startDate, endDate_text);
			endDate = startDate_text.getText().toString().trim();
			break;

		case R.id.eventup_person:
			if (myApplication.person.equals("")
					||myApplication.person.contains("ERR")) {
				if (Tools.isNetworkAvailable(this, true)) {
					request.SelectPersonRequest(myApplication.userid);
					tools.startProgressDialog(StatisticsUnqualifiedPerson.this);
				}
			} else {
				userData = myApplication.person;
				showUserDialog();
			}
			break;

		case R.id.eventup_search:
			if (table.getChildCount() > 1) {
				table.removeViews(1, table.getChildCount() - 1);
			}
			// table.removeAllViews();
			if (Tools.isNetworkAvailable(this, true)) {
				if (startDate_text.getText().toString().equals("")
						|| endDate_text.getText().equals("")) {
					Toast.makeText(StatisticsUnqualifiedPerson.this, "请选择时间", 1000).show();
				} else {
					if (TreeView.userCheckList.size() > 0) {
						tools.startProgressDialog(StatisticsUnqualifiedPerson.this);
						request.StatisticsUnqualifiedPersonRequest(TreeView.getUserIdList(),
								startDate_text.getText().toString(), endDate_text.getText()
										.toString());
						// 此处查询设备使用状况统计写死了userid
						// request.StatisticsUnqualifiedPersonRequest(
						// "da95a4e0-4001-11e0-9e47-e41f13e34d74",
						// startDate_text.getText().toString(), endDate_text
						// .getText().toString());
					} else {
						Toast.makeText(StatisticsUnqualifiedPerson.this, "没有选择人员", 1000).show();
					}
				}
			}
			break;
		}
	}

	public void showUserDialog() {
		Dialog dialog = new Dialog(StatisticsUnqualifiedPerson.this);
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

	public void showCharts() {
		tablelinear.setVisibility(View.VISIBLE);
		table.setStretchAllColumns(true);
		for (int i = 0; i < listBean.size(); i++) {
			bean = listBean.get(i);
			TableRow tablerow = new TableRow(this);
			tablerow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			numtext = new TextView(this);
			department = new TextView(this);
			name = new TextView(this);
			rate = new TextView(this);
			// numtext.setLayoutParams(new
			// LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT
			// ));
			// department.setLayoutParams(new
			// LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT
			// ));
			// name.setLayoutParams(new
			// LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT
			// ));
			// rate.setLayoutParams(new
			// LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT
			// ));

			numtext.setText(i + 1 + "");
			department.setText(bean.getDEPARTMENT());
			name.setText(bean.getUSERNAME());
			rate.setText(bean.getUNQUALIFIEDRATE());

			// numtext.setLines(3);
			numtext.setGravity(Gravity.CENTER);
			numtext.setBackgroundColor(Color.WHITE);// 背景黑色
			numtext.setTextSize(13);
			numtext.setPadding(1, 1, 1, 1);
			numtext.setTextColor(Color.BLACK);

			// department.setLines(3);
			department.setGravity(Gravity.CENTER);
			department.setBackgroundColor(Color.WHITE);// 背景黑色
			department.setTextSize(13);
			// department.setWidth(CONTEXT_RESTRICTED);
			department.setPadding(1, 1, 1, 1);
			department.setTextColor(Color.BLACK);

			// name.setLines(3);
			name.setGravity(Gravity.CENTER);
			name.setBackgroundColor(Color.WHITE);// 背景黑色
			name.setTextSize(13);
			name.setPadding(1, 1, 1, 1);
			name.setTextColor(Color.BLACK);

			// rate.setLines(3);
			rate.setGravity(Gravity.CENTER);
			rate.setBackgroundColor(Color.WHITE);// 背景黑色
			rate.setTextSize(13);
			// rate.setWidth(CONTEXT_RESTRICTED);
			rate.setPadding(1, 1, 1, 1);
			rate.setTextColor(Color.BLACK);

			tablerow.addView(numtext);
			tablerow.addView(department);
			tablerow.addView(name);
			tablerow.addView(rate);
			tablerow.setBackgroundColor(Color.BLACK);
			tablerow.setPadding(2, 1, 2, 1);

			table.addView(tablerow);
		}

	}
}
