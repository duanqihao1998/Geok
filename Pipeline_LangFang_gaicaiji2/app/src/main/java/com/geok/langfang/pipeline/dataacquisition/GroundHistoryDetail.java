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
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.Tools;

import java.util.List;

/**
 * 
 * @author wuchangming 接地电阻的历史记录的本地数据详情
 * 
 */
public class GroundHistoryDetail extends Activity implements View.OnClickListener {

	GroundHistoryData data;
	// GrallyImageView grallyImageView;
	TextView test_date;
	TextView set_value;
	TextView test_value;
	TextView year;
	TextView cpgroundbed;
	TextView halfyear;
	TextView conclusion;
	TextView isupload;
	String guid = "";
	Ground save;
	/*
	 * 标识是否上报
	 */
	int flag = 0;
	List<String> image, imagedes;
	ImageView imageview;
	public Handler MyHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Tools.stopProgressDialog(GroundHistoryDetail.this);
			OperationDB db = new OperationDB(getApplicationContext());
			String str = msg.getData().getString("result");
			if (msg.getData().getInt("flag") == 1 && str.contains("OK")) {
				db.DBupdate(1, guid, Type.GROUND_RESISTANCE);
				Toast.makeText(GroundHistoryDetail.this, "上报成功", 500).show();
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			} else {
				db.DBupdate(0, guid, Type.GROUND_RESISTANCE);
				Toast.makeText(GroundHistoryDetail.this, "上报失败", 500).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grounddetial);
		// 获得values里的数据
		data = (GroundHistoryData) getIntent().getSerializableExtra("values");
		year = (TextView) findViewById(R.id.ground_detial_year);
		halfyear = (TextView) findViewById(R.id.ground_detial_halfyear);
		test_date = (TextView) findViewById(R.id.ground_detial_test_date);
		set_value = (TextView) findViewById(R.id.ground_detial_set_value);
		test_value = (TextView) findViewById(R.id.ground_detial_test_value);
//		temperature = (TextView) findViewById(R.id.ground_detial_temperature);
		cpgroundbed = (TextView) findViewById(R.id.ground_detial_cpgroundbed);
//		weather = (TextView) findViewById(R.id.ground_detial_weather);
		conclusion = (TextView) findViewById(R.id.ground_detial_conclusion);
		isupload = (TextView) findViewById(R.id.ground_detial_isupload);

		year.setText("年份：" + data.getYear());
		halfyear.setText("半年：" + data.getHalfyear());
		String[] check_time = data.getTestdate().split(" ");
		if(check_time.length>1){
			test_date.setText("测试日期：" + check_time[0]);
		}else{
			test_date.setText("测试日期：" + data.getTestdate());
		}

		set_value.setText("规定值(V)：" + data.getSetvalue());
		test_value.setText("测试值(V)：" + data.getTestvalue());
//		temperature.setText("温度：" + data.getTemperature());
		cpgroundbed.setText("地床编号：" + data.getCpgroundbedeventid());
//		weather.setText("天气：" + data.getWeather());
		conclusion.setText("结论：" + data.getConclusion());

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

	// 定义菜单键功能
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
				// 调用接地电阻上报请求，上报数据
				if (Tools.isNetworkAvailable(this, true)) {
					String halfyear = "";
					halfyear = data.getHalfyear();
					if(halfyear.equals("上半年")){
						halfyear = "01";
					}else{
						halfyear = "02";
					}
					Tools.startProgressDialog(GroundHistoryDetail.this, "上报中，请稍后...");
					request.groundingResistanceRequest(data.getCpgroundbedeventid(),
							data.getUserid(), data.getTestdate(), data.getSetvalue(),
							data.getTestvalue(), data.getConclusion(), data.getYear(),
							halfyear, "");
				}
			} else {
				Toast.makeText(getApplicationContext(), "数据已上报", 1000).show();
			}

			break;

		case 2:// 修改

			if (flag !=1) {
				Intent intent = new Intent();
				intent.setClass(GroundHistoryDetail.this, Ground.class);
				intent.putExtra("values", data);
				intent.putExtra("flag", "upgrate");
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			} else {
				Toast.makeText(getApplicationContext(), "数据已上报,不能修改", 1000).show();
			}
			break;

		case 3:// 删除

			Builder builder = new Builder(GroundHistoryDetail.this);
			builder.setTitle("提示");
			builder.setMessage("是否删除本次记录");
			builder.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					OperationDB op = new OperationDB(getApplicationContext());
					ContentValues value = new ContentValues();
					value.put("guid", guid);
					// 根据guid唯一标识，删除记录
					op.DBdelete(value, Type.GROUND_RESISTANCE);
					GroundHistoryDetail.this.finish();
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
