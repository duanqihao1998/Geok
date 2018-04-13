package com.geok.langfang.pipeline.inspection;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.InspectionTaskInfoBean;
import com.geok.langfang.pipeline.Mywork.Mywork;
import com.geok.langfang.pipeline.Mywork.Plan;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.problem.Problem;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.DialogEditActivity;
import com.geok.langfang.tools.MyImageButton;
import com.geok.langfang.tools.MyScrollLayout;
import com.geok.langfang.tools.PanelListAdapter;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.tools.TypeQuest;

import java.util.Calendar;

/**
 * 
 * @author sunshihai
 * 
 *         巡检记录的功能界面
 * 
 */
public class InspectionRecord extends Activity implements OnClickListener {

	Button back, main;
	Button button1;// ,button2;
	LinearLayout viewGroup;
	MyScrollLayout r1, r2;
	LayoutParams params;
	ListView panelList;
	MyImageButton upload, save, reset;
	String eventid;
	String guid = "";

	InsepctionRecordData data;

	PanelListAdapter panelListAdapter;
	int[] image = { R.drawable.menu_mywork_selector, R.drawable.menu_problem_selector,
			R.drawable.menu_plan_selector, R.drawable.menu_record_selector };
	// 定义侧滑栏图片标题
	String[] imageTitle = { "我的工作", "事项上报", "巡检计划", "巡检日志" };
	/*
	 * 用于记录各个字段的变量 weather_text, 天气情况 road_text, 道路情况 inspection_text, 巡检纪事
	 * problem_text, 问题及其处理情况 problem_result_text, 处理结果 send_time_text, 交接时间
	 * send_location_text, 交接地点 car_text, 车辆情况 sender_text, 交班人
	 * receiver_text,接班人 other_text 其他纪事
	 */
	TextView weather_text, road_text, inspection_text, problem_text, problem_result_text,
			send_time_text, send_location_text, car_text, sender_text, receiver_text, other_text;
	TextView begin_time, end_time, instype, inspctortype, tools, inspfreq,// insyield,
			insdevice;
	// ,points
	// ,speed;
	LinearLayout weather, road, record, question, result, time, location, car, sender, receiver,
			other;
	String str_weather_text = "", str_road_text = "", str_inspection_text = "",
			str_problem_text = "", str_problem_result_text = "", str_send_time_text = "",
			str_send_location_text = "", str_car_text = "", str_sender_text = "",
			str_receiver_text = "", str_other_text = "";

	private ViewGroup group;
	// 用来表示每个小圆点的imageView
	private ImageView imageView;
	// 小圆点ImageViews的数组
	private ImageView[] imageViews;
	Request request;
	Tools tool;
	public Handler Myhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			tool.stopProgressDialog(InspectionRecord.this);
			switch (msg.arg1) {
			case 32:
				if (msg.getData().getInt("flag") == 1) {
					String str = msg.getData().getString("result");
					if (!str.equals("-1")) {
						InspectionTaskInfoBean bean = Json.getTaskInfosync(str);
						try {
							begin_time.setText(bean.getBEGINDATETIME().substring(0,
									(bean.getBEGINDATETIME().length() - 3)));
							end_time.setText(bean.getENDDATETIME().substring(0,
									(bean.getBEGINDATETIME().length() - 3)));
							instype.setText(bean.getINSTYPE());
							inspctortype.setText(bean.getINSPECTORTYPE());
							tools.setText(bean.getINSVEHICLE());
							inspfreq.setText(bean.getINSFREQ());
							// insyield.setText(bean.getINSYIELD());
							insdevice.setText(bean.getINSDEVICE());
							// points.setText(bean.getTRACKPOINTS());
							// speed.setText(bean.getAVGSPEED());
							eventid = bean.getEVENTID();
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(InspectionRecord.this, "无数据", 1000).show();
						}

					} else {
						Toast.makeText(InspectionRecord.this, "无数据", 1000).show();
					}
				} else {
					Toast.makeText(InspectionRecord.this, "网络连接错误", 1000).show();
				}
				break;

			case 24:
				if (msg.getData().getInt("flag") == 1) {
					if (msg.getData().getString("result").equals("OK|TRUE")) {
						save(1);
						Toast.makeText(getApplicationContext(), "上报成功", 1000).show();
						getSharedPreferences("save_password", MODE_PRIVATE).edit().putString(
								"info", "");
					} else {
						save(0);
						Toast.makeText(getApplicationContext(), "上报失败，已保存", 1000).show();
					}
				} else {
					Toast.makeText(InspectionRecord.this, "网络连接错误", 1000).show();
				}
				break;
			}
		}
	};
	MyApplication myApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inspectionrecord);
		request = new Request(Myhandler);
		tool = new Tools();
		myApplication = new MyApplication(this);
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		back.setOnClickListener(this);
		main.setOnClickListener(this);
		button1 = (Button) findViewById(R.id.mybutton1);
		// button2=(Button)findViewById(R.id.mybutton2);
		guid = java.util.UUID.randomUUID().toString();
		LayoutInflater inflate = LayoutInflater.from(this);
		r1 = (MyScrollLayout) inflate.inflate(R.layout.tab1, null);
		// r2 = (MyScrollLayout) inflate.inflate(R.layout.tab2, null);
		params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		viewGroup = (LinearLayout) findViewById(R.id.viewgroup);
		viewGroup.addView(r1, params);
		// viewGroup.addView(r2, params);
		button1.setOnClickListener(this);
		// button2.setOnClickListener(this);
		upload = (MyImageButton) findViewById(R.id.inspection_upload);
		upload.setOnClickListener(this);

		save = (MyImageButton) findViewById(R.id.inspection_save);
		save.setOnClickListener(this);

		reset = (MyImageButton) findViewById(R.id.inspection_reset);
		reset.setOnClickListener(this);

		begin_time = (TextView) findViewById(R.id.inspection_start_time);
		end_time = (TextView) findViewById(R.id.inspection_end_time);
		instype = (TextView) findViewById(R.id.inspection_type);
		inspctortype = (TextView) findViewById(R.id.insepction_person);
		tools = (TextView) findViewById(R.id.insepction_tools);
		inspfreq = (TextView) findViewById(R.id.insepction_freq);
		// insyield=(TextView)findViewById(R.id.insepction_hgl);
		insdevice = (TextView) findViewById(R.id.insepction_pda);
		// points=(TextView)findViewById(R.id.insepction_pointNum);
		// speed=(TextView)findViewById(R.id.insepction_speed);
		//

		data = (InsepctionRecordData) getIntent().getSerializableExtra("values");
		initList();
		initPanelList();
		if (data != null) {
			String upgrate = getIntent().getStringExtra("flag");
			if ("upgrate".equals(upgrate)) {

				begin_time.setText(data.getStarttime());
				end_time.setText(data.getEndtime());
				instype.setText(data.getType());
				inspctortype.setText(data.getTester());

				tools.setText(data.getTools());
				inspfreq.setText(data.getInspfreq());
				// insyield.setText(data.getYeild());
				insdevice.setText(data.getDevice());
				// points.setText(data.getPoints());

				// speed.setText(data.getSpeed());

				/*
				 * TextView weather_text,road_text,inspection_text,
				 * problem_text,problem_result_text,
				 * send_time_text,send_location_text,car_text,sender_text,
				 * receiver_text,other_text;
				 */
				weather_text.setText(data.getWeather());
				road_text.setText(data.getRoad());
				inspection_text.setText(data.getRecord());
				problem_text.setText(data.getRecord());
				problem_result_text.setText(data.getResult());
				send_time_text.setText(data.getTime());
				send_location_text.setText(data.getLocation());
				begin_time.setText(data.getStarttime());

				car_text.setText(data.getCar());
				sender_text.setText(data.getSender());
				receiver_text.setText(data.getReceiver());
				other_text.setText(data.getOther());
				begin_time.setText(data.getStarttime());
				guid = data.getGuid();

			}

		} else {
			if (Tools.isNetworkAvailable(InspectionRecord.this, true)) {
				request.TaskInfo(myApplication.userid);
				tool.startProgressDialog(InspectionRecord.this);
			}
			// request.TaskInfo("8f1857a2-adb1-11e2-8702-60672090d36c");
		}

		group = (ViewGroup) findViewById(R.id.pointgroup);
		imageViews = new ImageView[3];
		// 对小圆点的显示初始化
		for (int i = 0; i < 3; i++) {
			imageView = new ImageView(InspectionRecord.this);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(20, 0, 20, 0);
			imageViews[i] = imageView;

			if (i == 0) {
				// 默认是第一张图片 小圆点是
				imageViews[i].setBackgroundResource(R.drawable.circle_o);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.circle_l);
			}
			// 这里循环向LinearLayout中添加了view
			group.addView(imageViews[i]);
		}
		initBroadcast();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(br);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case TypeQuest.PIPELINE_RECORD_WERTHER:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("weather"))) {
					weather_text.setTextColor(Color.RED);
					weather_text.setText("未填写");

				} else {
					weather_text.setTextColor(Color.BLACK);
					weather_text.setText(data.getStringExtra("weather"));
					str_weather_text = data.getStringExtra("weather");
				}
				// weather_text.setText(data.getStringExtra("weather"));
			}

			break;
		case TypeQuest.PIPELINE_RECORD_ROAD:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("road"))) {
					road_text.setTextColor(Color.RED);
					road_text.setText("未填写");

				} else {
					road_text.setTextColor(Color.BLACK);
					road_text.setText(data.getStringExtra("road"));
					str_road_text = data.getStringExtra("road");
				}
				// road_text.setText(data.getStringExtra("road"));
			}

			break;

		case TypeQuest.PIPELINE_RECORD_RECORD:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("record"))) {
					inspection_text.setTextColor(Color.RED);
					inspection_text.setText("未填写");

				} else {
					inspection_text.setTextColor(Color.BLACK);
					inspection_text.setText(data.getStringExtra("record"));
					str_inspection_text = data.getStringExtra("record");
				}
				// inspection_text.setText(data.getStringExtra("record"));
			}

			break;

		case TypeQuest.PIPELINE_RECORD_PROBLEM:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("problem"))) {
					problem_text.setTextColor(Color.RED);
					problem_text.setText("未填写");

				} else {
					problem_text.setTextColor(Color.BLACK);
					problem_text.setText(data.getStringExtra("problem"));
					str_problem_text = data.getStringExtra("problem");
				}
				// problem_text.setText(data.getStringExtra("problem"));
			}

			break;

		case TypeQuest.PIPELINE_RECORD_RESULT:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("result"))) {
					problem_result_text.setTextColor(Color.RED);
					problem_result_text.setText("未填写");

				} else {
					problem_result_text.setTextColor(Color.BLACK);
					problem_result_text.setText(data.getStringExtra("result"));
					str_problem_result_text = data.getStringExtra("result");
				}
				// problem_result_text.setText(data.getStringExtra("result"));
			}

			break;

		case TypeQuest.PIPELINE_RECORD_CAR:
			if (resultCode == Activity.RESULT_OK) {

				if ("".equals(data.getStringExtra("car"))) {
					car_text.setTextColor(Color.RED);
					car_text.setText("未填写");

				} else {
					car_text.setTextColor(Color.BLACK);
					car_text.setText(data.getStringExtra("car"));
					str_car_text = data.getStringExtra("car");
				}
				// car_text.setText(data.getStringExtra("car"));

				break;
			}

			break;
		case TypeQuest.PIPELINE_RECORD_TIME:
			if (resultCode == Activity.RESULT_OK) {

				// if("".equals(data.getStringExtra("time")))
				// {
				// send_time_text.setTextColor(Color.RED);
				// send_time_text.setText("未填写");
				//
				// }else
				// {
				// send_time_text.setTextColor(Color.BLACK);
				// send_time_text.setText(data.getStringExtra("time"));
				// }
				send_time_text.setText(data.getStringExtra("time"));
				str_send_time_text = data.getStringExtra("time");

			}

			break;

		case TypeQuest.PIPELINE_RECORD_STATION:
			if (resultCode == Activity.RESULT_OK) {

				if ("".equals(data.getStringExtra("station"))) {
					send_location_text.setTextColor(Color.RED);
					send_location_text.setText("未填写");

				} else {
					send_location_text.setTextColor(Color.BLACK);
					send_location_text.setText(data.getStringExtra("station"));
					str_send_location_text = data.getStringExtra("station");
				}
				// send_location_text.setText(data.getStringExtra("station"));

			}

			break;
		case TypeQuest.PIPELINE_RECORD_SENDER:
			if (resultCode == Activity.RESULT_OK) {

				if ("".equals(data.getStringExtra("sender"))) {
					sender_text.setTextColor(Color.RED);
					sender_text.setText("未填写");

				} else {
					sender_text.setTextColor(Color.BLACK);
					sender_text.setText(data.getStringExtra("sender"));
					str_sender_text = data.getStringExtra("sender");
				}
				// sender_text.setText(data.getStringExtra("sender"));
			}

			break;
		case TypeQuest.PIPELINE_RECORD_RECEIVER:
			if (resultCode == Activity.RESULT_OK) {

				if ("".equals(data.getStringExtra("receiver"))) {
					receiver_text.setTextColor(Color.RED);
					receiver_text.setText("未填写");

				} else {
					receiver_text.setTextColor(Color.BLACK);
					receiver_text.setText(data.getStringExtra("receiver"));
					str_receiver_text = data.getStringExtra("receiver");
				}
				// receiver_text.setText(data.getStringExtra("receiver"));

			}

			break;
		case TypeQuest.PIPELINE_RECORD_OTHER:
			if (resultCode == Activity.RESULT_OK) {

				if ("".equals(data.getStringExtra("other"))) {
					other_text.setTextColor(Color.RED);
					other_text.setText("未填写");

				} else {
					other_text.setTextColor(Color.BLACK);
					other_text.setText(data.getStringExtra("other"));
					str_other_text = data.getStringExtra("other");
				}
				// other_text.setText(data.getStringExtra("other"));
			}

			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 初始化界面的各个控件
	 */

	private void initList() {

		/*
		 * 用于记录各个字段的变量 weather_text, 天气情况 road_text, 道路情况 inspection_text, 巡检纪事
		 * problem_text, 问题及其处理情况 problem_result_text, 处理结果 send_time_text, 交接时间
		 * send_location_text, 交接地点 car_text, 车辆情况 sender_text, 交班人
		 * receiver_text,接班人 other_text 其他纪事
		 */
		weather_text = (TextView) findViewById(R.id.inspection_weather_text);
		road_text = (TextView) findViewById(R.id.inspection_road_text);
		inspection_text = (TextView) findViewById(R.id.inspection_record_text);
		problem_text = (TextView) findViewById(R.id.inspection_question_text);
		problem_result_text = (TextView) findViewById(R.id.inspection_result_text);
		send_time_text = (TextView) findViewById(R.id.inspection_time_text);
		send_location_text = (TextView) findViewById(R.id.inspection_location_text);
		car_text = (TextView) findViewById(R.id.inspection_car_text);
		sender_text = (TextView) findViewById(R.id.inspection_sender_text);
		receiver_text = (TextView) findViewById(R.id.inspection_receiver_text);
		other_text = (TextView) findViewById(R.id.inspection_other_text);

		// LinearLayout
		// weather,road,record,question,result,time,location,car,sender,receiver,other;
		weather = (LinearLayout) findViewById(R.id.inspection_weather);
		weather.setOnClickListener(this);
		road = (LinearLayout) findViewById(R.id.inspection_road);
		road.setOnClickListener(this);
		record = (LinearLayout) findViewById(R.id.inspection_record);
		record.setOnClickListener(this);
		question = (LinearLayout) findViewById(R.id.inspection_question);
		question.setOnClickListener(this);
		result = (LinearLayout) findViewById(R.id.inspection_result);
		result.setOnClickListener(this);
		time = (LinearLayout) findViewById(R.id.inspection_time);
		time.setOnClickListener(this);
		location = (LinearLayout) findViewById(R.id.inspection_location);
		location.setOnClickListener(this);
		car = (LinearLayout) findViewById(R.id.inspection_car);
		car.setOnClickListener(this);
		sender = (LinearLayout) findViewById(R.id.inspection_sender);
		sender.setOnClickListener(this);
		receiver = (LinearLayout) findViewById(R.id.inspection_receiver);
		receiver.setOnClickListener(this);
		other = (LinearLayout) findViewById(R.id.inspection_other);
		other.setOnClickListener(this);

	}

	/**
	 * 初始化侧拉列表
	 */
	private void initPanelList() {
		panelList = (ListView) findViewById(R.id.inspection_panel_list);
		panelListAdapter = new PanelListAdapter(this, image, imageTitle, 0);
		panelList.setAdapter(panelListAdapter);
		panelList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				switch (arg2) {
				case 0:
					intent.setClass(InspectionRecord.this, Mywork.class);
					break;
				case 1:
					intent.setClass(InspectionRecord.this, Problem.class);
					break;
				case 2:
					intent.setClass(InspectionRecord.this, Plan.class);
					break;
				case 3:
					intent.setClass(InspectionRecord.this, InspectionRecord.class);
					break;
				}
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.inspection_upload:
			if (str_weather_text == "" && str_road_text == "" && str_inspection_text == ""
					&& str_problem_text == "" && str_problem_result_text == ""
					&& str_send_time_text == "" && str_send_location_text == ""
					&& str_car_text == "" && str_sender_text == "" && str_receiver_text == ""
					&& str_other_text == "") {
				Toast.makeText(InspectionRecord.this, "未填写任何内容，上报失败", 1000).show();
			} else {
				if ("填写交班时间".equals(send_time_text.getText().toString())) {
					str_send_time_text = "";
				} else {
					str_send_time_text = send_time_text.getText().toString();
				}
				// if(!(MyApplication.info.equals(""))){

				if (Tools.isNetworkAvailable(getApplicationContext(), true)) {
					tool.startProgressDialog(this);
					request.InspectRecordRequest(myApplication.userid, str_weather_text,
							str_road_text, str_inspection_text, str_problem_text,
							str_problem_result_text, str_send_time_text, str_send_location_text,
							str_car_text, str_sender_text, str_receiver_text, str_other_text,
							myApplication.info, eventid, myApplication.imei, "");
					// "DDF232:01,DE343,23.1,34.1;K230,01,23.1,34.1 #KFDS33:01,DE343,23.1,34.1;K230,01,DE343,23.1,34.1;#",eventid);
					getSharedPreferences("save_password", MODE_PRIVATE).edit()
							.putString("inspecrionEVENTID", eventid).commit();

				} else {
					save(0);
					Toast.makeText(getApplicationContext(), "网络异常，请重试！", 1000).show();
				}
				// }else{
				// Toast.makeText(this, "请先巡检", 2000).show();
				// }
			}
			break;
		case R.id.inspection_save:
			if (str_weather_text == "" && str_road_text == "" && str_inspection_text == ""
					&& str_problem_text == "" && str_problem_result_text == ""
					&& str_send_time_text == "" && str_send_location_text == ""
					&& str_car_text == "" && str_sender_text == "" && str_receiver_text == ""
					&& str_other_text == "") {
				Toast.makeText(InspectionRecord.this, "未填写任何内容，保存失败", 1000).show();
			} else {
				save(0);
			}
			break;
		case R.id.inspection_reset:
			weather_text.setText("填写天气状况");
			road_text.setText("填写道路状况");
			inspection_text.setText("填写巡检记事");
			problem_text.setText("填写问题及情况");
			problem_result_text.setText("填写处理结果");
			send_time_text.setText("填写交班时间");
			send_location_text.setText("填写交班地点");
			car_text.setText("填写车辆状况");
			sender_text.setText("填写交班人");
			receiver_text.setText("填写接班人");
			other_text.setText("填写其他记事");

			weather_text.setTextColor(Color.BLACK);
			road_text.setTextColor(Color.BLACK);
			inspection_text.setTextColor(Color.BLACK);
			problem_text.setTextColor(Color.BLACK);
			problem_result_text.setTextColor(Color.BLACK);
			send_time_text.setTextColor(Color.BLACK);
			send_location_text.setTextColor(Color.BLACK);
			car_text.setTextColor(Color.BLACK);
			sender_text.setTextColor(Color.BLACK);
			receiver_text.setTextColor(Color.BLACK);
			other_text.setTextColor(Color.BLACK);
			break;
		case R.id.mybutton1:
			viewGroup.removeAllViews();
			viewGroup.addView(r1, params);
			break;
		// case R.id.mybutton2:
		// viewGroup.removeAllViews();
		// viewGroup.addView(r2,params);
		// break;
		case R.id.inspection_weather:
			Intent weather = new Intent(InspectionRecord.this, DialogEditActivity.class);
			weather.putExtra("flag", TypeQuest.PIPELINE_RECORD_WERTHER);
			weather.putExtra("Tittle", "天气状况");
			if (!"填写天气状况".equals(weather_text.getText().toString())) {
				weather.putExtra("text", weather_text.getText().toString());
			}
			startActivityForResult(weather, TypeQuest.PIPELINE_RECORD_WERTHER);

			break;
		case R.id.inspection_road:

			Intent road = new Intent(InspectionRecord.this, DialogEditActivity.class);
			road.putExtra("flag", TypeQuest.PIPELINE_RECORD_ROAD);
			road.putExtra("Tittle", "道路状况");
			if (!"填写道路状况".equals(road_text.getText().toString())) {
				road.putExtra("text", road_text.getText().toString());
			}
			startActivityForResult(road, TypeQuest.PIPELINE_RECORD_ROAD);
			break;
		case R.id.inspection_record:
			Intent record = new Intent(InspectionRecord.this, DialogEditActivity.class);
			record.putExtra("flag", TypeQuest.PIPELINE_RECORD_RECORD);
			record.putExtra("Tittle", "巡检纪事");
			if (!"填写巡检记事".equals(inspection_text.getText().toString())) {
				record.putExtra("text", inspection_text.getText().toString());
			}
			startActivityForResult(record, TypeQuest.PIPELINE_RECORD_RECORD);
			break;
		case R.id.inspection_question:
			Intent question = new Intent(InspectionRecord.this, DialogEditActivity.class);
			question.putExtra("flag", TypeQuest.PIPELINE_RECORD_PROBLEM);
			question.putExtra("Tittle", "问题及其处理情况");
			String str = problem_text.getText().toString();
			if (!"填写问题及情况".equals(problem_text.getText().toString())) {
				question.putExtra("text", problem_text.getText().toString());
			}

			startActivityForResult(question, TypeQuest.PIPELINE_RECORD_PROBLEM);
			break;
		case R.id.inspection_result:
			Intent result = new Intent(InspectionRecord.this, DialogEditActivity.class);
			result.putExtra("flag", TypeQuest.PIPELINE_RECORD_RESULT);
			result.putExtra("Tittle", "处理结果");
			if (!"填写处理结果".equals(problem_result_text.getText().toString())) {
				result.putExtra("text", problem_result_text.getText().toString());
			}

			startActivityForResult(result, TypeQuest.PIPELINE_RECORD_RESULT);
			break;
		case R.id.inspection_time:

			Calendar c = Calendar.getInstance();
			// new DatePickerDialog(InspectionRecord.this,
			// new OnDateSetListener() {
			//
			// public void onDateSet(DatePicker view, int year,
			// int monthOfYear, int dayOfMonth) {
			// int n = monthOfYear + 1;
			// send_time_text.setText(year + "-" + n + "-"
			// + dayOfMonth);
			// send_time_text.setGravity(Gravity.CENTER);
			// }
			// }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
			// c.get(Calendar.DAY_OF_MONTH)).show();
			Tools.setDateDialog(this, c, send_time_text);
			break;

		case R.id.inspection_location:
			Intent location = new Intent(InspectionRecord.this, DialogEditActivity.class);
			location.putExtra("flag", TypeQuest.PIPELINE_RECORD_STATION);
			location.putExtra("Tittle", "交接地点");
			if (!"填写交班地点".equals(send_location_text.getText().toString())) {
				location.putExtra("text", send_location_text.getText().toString());
			}
			startActivityForResult(location, TypeQuest.PIPELINE_RECORD_STATION);
			break;
		case R.id.inspection_car:
			Intent car = new Intent(InspectionRecord.this, DialogEditActivity.class);
			car.putExtra("flag", TypeQuest.PIPELINE_RECORD_CAR);
			car.putExtra("Tittle", "车辆状况");
			if (!"填写车辆状况".equals(car_text.getText().toString())) {
				car.putExtra("text", car_text.getText().toString());
			}
			startActivityForResult(car, TypeQuest.PIPELINE_RECORD_CAR);
			break;
		case R.id.inspection_sender:
			Intent sender = new Intent(InspectionRecord.this, DialogEditActivity.class);

			sender.putExtra("flag", TypeQuest.PIPELINE_RECORD_SENDER);
			sender.putExtra("Tittle", "交班人");
			if (!"填写交班人".equals(sender_text.getText().toString())) {
				sender.putExtra("text", sender_text.getText().toString());
			}
			startActivityForResult(sender, TypeQuest.PIPELINE_RECORD_SENDER);
			break;
		case R.id.inspection_receiver:
			Intent receiver = new Intent(InspectionRecord.this, DialogEditActivity.class);
			receiver.putExtra("flag", TypeQuest.PIPELINE_RECORD_RECEIVER);
			receiver.putExtra("Tittle", "接班人");
			if (!"填写接班人".equals(receiver_text.getText().toString())) {
				receiver.putExtra("text", receiver_text.getText().toString());
			}
			startActivityForResult(receiver, TypeQuest.PIPELINE_RECORD_RECEIVER);
			break;
		case R.id.inspection_other:
			Intent other = new Intent(InspectionRecord.this, DialogEditActivity.class);
			other.putExtra("flag", TypeQuest.PIPELINE_RECORD_OTHER);
			other.putExtra("Tittle", "其它纪事");
			if (!"填写其它记事".equals(other_text.getText().toString())) {
				other.putExtra("text", other_text.getText().toString());
			}
			startActivityForResult(other, TypeQuest.PIPELINE_RECORD_OTHER);
			break;

		case R.id.back:
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case R.id.main:
			Tools.backMain(this);
			break;
		}

	}

	public void save(int flag) {
		ContentValues values = new ContentValues();
		values.put("guid", guid);
		values.put("begintime", begin_time.getText().toString());
		values.put("endtime", end_time.getText().toString());
		values.put("type", instype.getText().toString());
		values.put("tester", inspctortype.getText().toString());
		values.put("tools", tools.getText().toString());
		values.put("inspfreq", inspfreq.getText().toString());
		// values.put("yeild", insyield.getText().toString());
		values.put("device", insdevice.getText().toString());
		// values.put("points",points.getText().toString() );
		// values.put("speed", speed.getText().toString());
		// values.put("weather", weather_text.getText().toString());
		values.put("weather", str_weather_text);
		/*
		 * new
		 * Object[]{value.get("guid"),value.get("begintime"),value.get("endtime"
		 * ),value.get("type"),
		 * value.get("tester"),value.get("tools"),value.get(
		 * "inspfreq"),value.get("yeild"),
		 * value.get("device"),value.get("points"
		 * ),value.get("speed"),value.get("weather"),
		 * value.get("road"),value.get
		 * ("record"),value.get("problem"),value.get("result"),
		 * value.get("time")
		 * ,value.get("location"),value.get("car"),value.get("sender"),
		 * value.get("receiver"),value.get("other"),value.get("isupload")});
		 * weather_text
		 * ,road_text,inspection_text,problem_text,problem_result_text
		 * ,send_time_text,send_location_text,car_text,
		 * sender_text,receiver_text,other_text;
		 * begin_time,end_time,instype,inspctortype
		 * ,tools,inspfreq,insyield,insdevice,points,speed;
		 */
		values.put("road", str_road_text);
		values.put("record", str_inspection_text);
		values.put("problem", str_problem_text);
		values.put("result", str_problem_result_text);
		values.put("time", str_send_time_text);
		values.put("location", str_send_location_text);
		values.put("car", str_car_text);
		values.put("sender", str_sender_text);
		values.put("receiver", str_receiver_text);
		values.put("other", str_other_text);
		values.put("isupload", flag);
		values.put("info", myApplication.info);
		values.put("eventid", eventid);

		OperationDB operationDB = new OperationDB(InspectionRecord.this);
		operationDB.InsertOrUpdate(values, Type.LINE_LOG);
		Toast.makeText(getApplicationContext(), "保存成功！", 1000).show();
		finish();
		Intent reset = new Intent();
		reset.setClass(InspectionRecord.this, InspectionRecord.class);
		startActivity(reset);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		menu.add(0, 1, 1, "退出").setIcon(R.drawable.exit);
		menu.add(0, 2, 2, "注销").setIcon(R.drawable.cancel);
		menu.add(0, 3, 3, "信息查询").setIcon(R.drawable.search);
		menu.add(0, 4, 4, "GPS状态").setIcon(R.drawable.gps);
		menu.add(0, 5, 5, "帮助").setIcon(R.drawable.help);
		menu.add(0, 6, 6, "历史记录").setIcon(R.drawable.history);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 1:
			Tools.exit(this);
			break;
		case 2:
			Tools.cancel(this);
			break;
		case 3:
			Toast.makeText(getApplicationContext(), "暂未开放此功能", 1000).show();
			break;
		case 4:
			Tools.gps(this);
			break;
		case 5:
			Tools.help(this);
			break;
		case 6:
			Intent intent = new Intent();
			intent.setClass(InspectionRecord.this, InspectionRecordHistory.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onPageSelected(int arg0) {
		for (int i = 0; i < imageViews.length; i++) {
			imageViews[arg0].setBackgroundResource(R.drawable.circle_o);

			if (arg0 != i) {
				imageViews[i].setBackgroundResource(R.drawable.circle_l);
			}
		}
	}

	/**
	 * 接收广播，选择哪个界面，哪个点亮
	 */
	BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("index")) {
				onPageSelected(intent.getIntExtra("index", 0));
			}
		}

	};

	/**
	 * 
	 * @功能描述 注册广播
	 * @author 张龙飞Email:longfeiz@geo-k.cn Tel:13671277587
	 * @createDate 2013-11-22 上午10:01:21
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void initBroadcast() {
		IntentFilter filter = new IntentFilter("index");
		registerReceiver(br, filter);
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
