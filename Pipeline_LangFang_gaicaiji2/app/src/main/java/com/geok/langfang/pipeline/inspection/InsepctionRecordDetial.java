package com.geok.langfang.pipeline.inspection;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.Tools;

/**
 * 
 * @author sunshihai 巡检记录的详细信息
 * 
 */

public class InsepctionRecordDetial extends Activity implements View.OnClickListener {

	InsepctionRecordData data;

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
			String temp[] = str.split("\\|");
			OperationDB db = new OperationDB(getApplicationContext());
			if (temp[0].equals("OK")) {
				db.DBupdate(1, guid, Type.LINE_LOG);
				Toast.makeText(InsepctionRecordDetial.this, "上报成功", 500).show();
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			} else {
				db.DBupdate(0, guid, Type.LINE_LOG);
				Toast.makeText(InsepctionRecordDetial.this, "上报失败", 500).show();
			}

		}

	};
	MyApplication myApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.inspectiondetial);

		myApplication = new MyApplication(this);
		data = (InsepctionRecordData) getIntent().getSerializableExtra("values");

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

		guid = data.getGuid();

		begintime.setText("巡检开始时间：" + data.getStarttime());
		endtime.setText("巡检结束时间：" + data.getEndtime());
		type.setText("巡检类型：" + data.getType());
		person.setText("巡检人员：" + data.getTester());
		tools.setText("巡检工具：" + data.getTools());
		inspfreq.setText("巡检频次：" + data.getInspfreq());
		yeild.setText("巡检合格率：" + data.getYeild());
		device.setText("巡检仪器：" + data.getDevice());

		points.setText("巡检轨迹点数：" + data.getPoints());
		speed.setText("平均时速：" + data.getSpeed());
		weather.setText("天气状况：" + data.getWeather());
		road.setText("道路状况：" + data.getRoad());
		record.setText("巡检纪事：" + data.getRecord());
		problem.setText("问题及其处理情况：" + data.getProblem());
		result.setText("处理结果：" + data.getResult());
		time.setText("交接班时间：" + data.getTime());

		location.setText("交接班地点：" + data.getLocation());
		sender.setText("交班人：" + data.getSender());
		receiver.setText("接班人:" + data.getReceiver());
		other.setText("其它纪事：" + data.getOther());
		flag = data.getFlag();

		if (flag == 0) {
			isupload.setText("传输状态：保存未上传");
			isupload.setTextColor(Color.RED);
		} else if (flag == 1) {
			isupload.setText("传输状态：已上传");
		} else {
			isupload.setText("传输状态：保存未上传");
			isupload.setTextColor(Color.RED);
		}

		Button back = (Button) findViewById(R.id.back);
		Button main = (Button) findViewById(R.id.main);
		back.setTag(30);
		main.setTag(31);
		back.setOnClickListener(this);
		main.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(1, 1, 1, "上报");
		menu.add(2, 2, 2, "修改");
		menu.add(3, 3, 3, "删除");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 1: // 上报

			if (flag != 1) {
				Request queset = new Request(MyHandler);

				//
				queset.InspectRecordRequest(myApplication.userid, data.getWeather(),
						data.getRoad(), data.getRecord(), data.getProblem(), data.getResult(),
						data.getTime(), data.getLocation(), data.getCar(), data.getSender(),
						data.getReceiver(), data.getOther(), data.getInfo(), data.getEventid(),
						myApplication.imei, "");

			} else {
				Toast.makeText(getApplicationContext(), "数据已上报", 1000).show();
			}

			break;

		case 2:// 修改

			if (flag !=1) {
				Intent intent = new Intent();
				intent.setClass(InsepctionRecordDetial.this, InspectionRecord.class);
				intent.putExtra("values", data);
				intent.putExtra("flag", "upgrate");
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "数据不能修改", 1000).show();
			}
			break;

		case 3:// 删除

			Builder builder = new Builder(InsepctionRecordDetial.this);
			builder.setTitle("提示");
			builder.setMessage("是否删除本次记录");
			builder.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					OperationDB opdb = new OperationDB(getApplicationContext());
					ContentValues value = new ContentValues();
					value.put("guid", guid);
					opdb.DBdelete(value, Type.LINE_LOG);
					InsepctionRecordDetial.this.finish();
					overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
				}
			}).setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			}).show();

			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = (Integer) v.getTag();
		switch (tag) {
		case 30:
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;

		case 31:
			// Intent intent2 = new Intent();
			// intent2.setClass(InsepctionRecordDetial.this, MainView.class);
			// startActivity(intent2);
			Tools.backMain(this);
			break;
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
