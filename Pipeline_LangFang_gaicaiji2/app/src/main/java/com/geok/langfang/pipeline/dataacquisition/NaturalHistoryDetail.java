package com.geok.langfang.pipeline.dataacquisition;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.Tools;

import java.util.List;

/**
 * 
 * 自然电位的历史记录的本地数据详情
 * 
 */
public class NaturalHistoryDetail extends Activity implements View.OnClickListener {

	NaturalHistoryData data;
	TextView year;
	TextView month;
	TextView line;
	TextView pile;
	TextView value;
	TextView testtime;
	TextView voltage;
	TextView temperature;
	TextView weather;
	TextView remarks;
	TextView isupload;
	String guid = "";
	Natural natural;
	MyApplication myApplication;
	/*
	 * 标识是否上报
	 */
	int flag = 0;
	List<String> image, imagedes;
	ImageView imageview;
	Handler MyHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Tools.stopProgressDialog(NaturalHistoryDetail.this);
			OperationDB db = new OperationDB(getApplicationContext());
			String str = msg.getData().getString("result");
			if (msg.getData().getInt("flag") == 1 && str.contains("OK")) {
				db.DBupdate(1, guid, Type.NATURAL_POTENTIAL);
				Toast.makeText(NaturalHistoryDetail.this, "上报成功", 500).show();
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			} else {
				db.DBupdate(0, guid, Type.NATURAL_POTENTIAL);
				Toast.makeText(NaturalHistoryDetail.this, "上报失败", 500).show();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.naturaldetial);
		myApplication = new MyApplication(this);
		data = (NaturalHistoryData) getIntent().getSerializableExtra("values");
		year = (TextView) findViewById(R.id.natural_detial_year);
		month = (TextView) findViewById(R.id.natural_detial_month);
		line = (TextView) findViewById(R.id.natural_detial_line);
		pile = (TextView) findViewById(R.id.natural_detial_pile);
		value = (TextView) findViewById(R.id.natural_detial_value);

		testtime = (TextView) findViewById(R.id.natural_detial_testtime);
		voltage = (TextView) findViewById(R.id.natural_detial_voltage);
		temperature = (TextView) findViewById(R.id.natural_detial_temperature);
		weather = (TextView) findViewById(R.id.natural_detial_weather);
		remarks = (TextView) findViewById(R.id.natural_detial_remarks);
		isupload = (TextView) findViewById(R.id.natural_detial_isupload);
		year.setText("年份：" + data.getYear());
		month.setText("月份：" + data.getMonth());
		line.setText("线路：" + data.getLine());
		pile.setText("桩号：" + data.getPile());
		value.setText("电位值(V)：" + data.getValue());
		String[] check_time = data.getTesttime().split(" ");
		if(check_time.length>1){
			testtime.setText("测试时间时间：" + check_time[0]);
		}else{
			testtime.setText("测试时间时间：" + data.getTesttime());
		}

		voltage.setText("交流电干扰电压(V)：" + data.getVoltage());
		temperature.setText("温度(℃)：" + data.getTemperature());
		weather.setText("天气：" + data.getWeather());
		remarks.setText("备注：" + data.getRemarks());
		flag = data.getIsupload();

		if (data.getIsupload() == 1) {
			isupload.setText("是否上报服务器：" + "已上报");
			isupload.setTextColor(Color.BLACK);
		} else {
			isupload.setText("是否上报服务器：" + "未上报");
			isupload.setTextColor(Color.RED);
		}
		guid = data.getGuid();

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
				Request request = new Request(MyHandler);
				if (Tools.isNetworkAvailable(this, true)) {
					Tools.startProgressDialog(NaturalHistoryDetail.this, "上报中，请稍后...");
					request.naturalPotentialRequest(myApplication.userid, data.getYear(), data.getMonth(),
							data.getLineid(), data.getPileid(), data.getValue(),
							data.getTesttime(), data.getRemarks(), data.getVoltage(),
							data.getTemperature(), data.getWeather(), "");
				}
			} else {
				Toast.makeText(getApplicationContext(), "数据已上报", 1000).show();
			}

			break;

		case 2:// 修改

			if (flag !=1) {
				Intent intent = new Intent();
				intent.setClass(NaturalHistoryDetail.this, Natural.class);
				intent.putExtra("values", data);
				intent.putExtra("flag", "upgrate");
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			} else {
				Toast.makeText(getApplicationContext(), "数据已上报,不能修改", 1000).show();
			}
			break;

		case 3:// 删除

			Builder builder = new Builder(NaturalHistoryDetail.this);
			builder.setTitle("提示");
			builder.setMessage("是否删除本次记录");
			builder.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					OperationDB op = new OperationDB(getApplicationContext());
					ContentValues value = new ContentValues();
					value.put("guid", guid);
					op.DBdelete(value, Type.NATURAL_POTENTIAL);
					NaturalHistoryDetail.this.finish();
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
