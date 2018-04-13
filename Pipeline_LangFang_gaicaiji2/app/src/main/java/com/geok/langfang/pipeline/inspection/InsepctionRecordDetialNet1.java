package com.geok.langfang.pipeline.inspection;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.InspectionHistoryDetile1;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.Tools;

public class InsepctionRecordDetialNet1 extends Activity implements
		View.OnClickListener {

	InsepctionRecordData data;
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
	 * 巡检人员姓名:INSPECTOR 巡检出发时间:BEGINDATETIME 巡线返回时间:ENDDATETIME 巡检类型:INSTYPE
	 * 巡检人员(类型):INSPECTORTYPE 巡检工具:INSVEHICLE 巡检频次:INSFREQ 巡检合格率:INSYIELD
	 * 巡检仪:INSDEVICE 巡检轨迹点数:TRACKPOINTS 平均时速:AVGSPEED 天气状况:WEATHER
	 * 道路状况:RODESTATUS 巡检记事:ADVERSARIA 问题及处理情况:PROBLEM 处理结果:DEALWITH
	 * 交班时间:SHIFTTIME 交班地点:SHIFTPLACE 车辆状况:VEHICLE 交班人:SHIFTFROM 接班人:SHIFTTO
	 * 其他记事:OTHERADVERSARIA
	 */
	/*
	 * 标识是否上报
	 */
	int flag = 0;
	Request request;
	public Handler MyHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String str = msg.getData().getString("result");
			InspectionHistoryDetile1 bean = Json.getInspectionHistoryDetial1(str);
			if (bean != null) {
				begintime.setText("巡检开始时间：" + bean.getBEGINDATETIME());
				endtime.setText("巡检结束时间：" + bean.getENDDATETIME());
				type.setText("巡检类型：" + bean.getINSTYPE());
				person.setText("巡检人员：" + bean.getINSPECTOR());
				tools.setText("巡检工具：" + bean.getINSVEHICLE());
				inspfreq.setText("巡检频次：" + bean.getINSFREQ());
				yeild.setText("巡检合格率：" + bean.getINSYIELD());
				device.setText("巡检仪器：" + bean.getINSDEVICE());
				points.setText("轨迹点数：" + bean.getTRACKPOINTS());
				speed.setText("巡检速度：" + bean.getAVGSPEED());
				weather.setText("天气情况：" + bean.getWEATHER());
				road.setText("道路状况：" + bean.getRODESTATUS());
				record.setText("巡检记录：" + bean.getADVERSARIA());
				problem.setText("问题情况：" + bean.getPROBLEM());
				result.setText("问题解决情况：" + bean.getDEALWITH());
				time.setText("交接班时间：" + bean.getSHIFTTIME());
				car.setText("车辆情况：" + bean.getVEHICLE());
				location.setText("交接班地点：" + bean.getSHIFTPLACE());
				sender.setText("交班人：" + bean.getSHIFTFROM());
				receiver.setText("接班人：" + bean.getSHIFTTO());
				other.setText("其他情况：" + bean.getOTHERADVERSARIA());

			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		eventid = getIntent().getStringExtra("eventid");
		request = new Request(MyHandler);
		request.InspectionInforQuery("1", eventid);
		setContentView(R.layout.inspectiondetial);
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
		// TODO Auto-generated method stub
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
