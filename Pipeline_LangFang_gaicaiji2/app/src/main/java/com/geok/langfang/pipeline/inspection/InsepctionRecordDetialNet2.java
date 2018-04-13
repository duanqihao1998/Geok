package com.geok.langfang.pipeline.inspection;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.InspectionHistoryDetile2;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.Tools;

public class InsepctionRecordDetialNet2 extends Activity implements OnClickListener {

	Request request;

	String eventid;
	String eventtype;
	TextView begintime;
	TextView endtime;
	TextView type;
	TextView person;
	TextView tools;
	TextView inspfreq;
	TextView yeild;
	TextView device;
	TextView points;
	TextView speed;
	TextView weather;
	TextView road;
	TextView record;
	TextView problem;
	TextView result;
	TextView time;
	TextView location;
	TextView sender;
	TextView receiver;
	TextView other;
	TextView car;

	TextView isupload;
	String guid = "";
	Button back, main;
	/*
	 * 标识是否上报
	 */
	int flag = 0;

	public Handler MyHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String str = msg.getData().getString("result");
			InspectionHistoryDetile2 bean = Json.getInspectionHistoryDetial2(str);
			if (bean != null) {
				begintime.setText("巡检起始时间:" + bean.getBEGINTIME());
				endtime.setText("巡检结束时间:" + bean.getENDTIME());
				type.setText("未巡检人员:" + bean.getNOTPECTOR());
				person.setText("未巡检原因:" + bean.getNOTREASON());
				tools.setText("未巡检原因描述:" + bean.getNOTREMARK());
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inspectiondetial);
		eventid = getIntent().getStringExtra("eventid");
		request = new Request(MyHandler);
		request.InspectionInforQuery("0", eventid);
		begintime = (TextView) findViewById(R.id.inspectiondetial_begintime);
		endtime = (TextView) findViewById(R.id.inspectiondetial_endtime);
		type = (TextView) findViewById(R.id.inspectiondetial_type);
		person = (TextView) findViewById(R.id.inspectiondetial_tester);
		tools = (TextView) findViewById(R.id.inspectiondetial_tools);
		inspfreq = (TextView) findViewById(R.id.inspectiondetial_inspfreq);
		yeild = (TextView) findViewById(R.id.inspectiondetial_yeild);
		device = (TextView) findViewById(R.id.inspectiondetial_device);
		points = (TextView) findViewById(R.id.inspectiondetial_points);
		speed = (TextView) findViewById(R.id.inspectiondetial_speed);
		weather = (TextView) findViewById(R.id.inspectiondetial_weather);
		road = (TextView) findViewById(R.id.inspectiondetial_road);
		record = (TextView) findViewById(R.id.inspectiondetial_record);
		problem = (TextView) findViewById(R.id.inspectiondetial_problem);
		result = (TextView) findViewById(R.id.inspectiondetial_result);
		time = (TextView) findViewById(R.id.inspectiondetial_time);
		car = (TextView) findViewById(R.id.inspectiondetial_car);
		location = (TextView) findViewById(R.id.inspectiondetial_location);
		sender = (TextView) findViewById(R.id.inspectiondetial_sender);
		receiver = (TextView) findViewById(R.id.inspectiondetial_receiver);
		other = (TextView) findViewById(R.id.inspectiondetial_other);
		isupload = (TextView) findViewById(R.id.inspectiondetial_isupload);
		/*
		 * begintime.setText("巡检开始时间："+data.getStarttime());
		 * endtime.setText("巡检结束时间："+data.getEndtime());
		 * type.setText("巡检类型："+data.getType());
		 * person.setText("巡检人员："+data.getTester());
		 * tools.setText("巡检工具："+data.getTools());
		 * inspfreq.setText("巡检频次："+data.getInspfreq());
		 * yeild.setText("巡检合格率："+data.getYeild());
		 * device.setText("巡检仪器："+data.getDevice());
		 * points.setText("巡检轨迹点数："+data.getPoints());
		 * speed.setText("平均时速："+data.getSpeed());
		 * weather.setText("天气状况："+data.getWeather());
		 * road.setText("道路状况："+data.getRoad());
		 * record.setText("巡检纪事："+data.getRecord());
		 * problem.setText("问题及其处理情况："+data.getProblem());
		 * result.setText("处理结果："+data.getResult());
		 * time.setText("交接班时间："+data.getTime());
		 * 
		 * location.setText("交接班地点："+data.getLocation());
		 * sender.setText("交班人："+data.getSender());
		 * receiver.setText("接班人:"+data.getReceiver());
		 * other.setText("其它纪事："+data.getOther());
		 */
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		main = (Button) findViewById(R.id.main);
		main.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(Activity.RESULT_OK);
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.main:
			Tools.backMain(this);
			break;
		}
	}

}
