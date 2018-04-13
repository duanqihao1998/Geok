package com.geok.langfang.pipeline.setting;

import com.geok.langfang.pipeline.ApplicationApp;
import com.geok.langfang.pipeline.Login;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.tools.DialogActivity;
import com.geok.langfang.tools.DialogEditActivity;
import com.geok.langfang.tools.DialogEditIpActivity;
import com.geok.langfang.tools.MyImageButton;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.tools.TypeQuest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingDtial extends Activity implements OnClickListener {

	RelativeLayout relativeLayout;
	RadioButton auto_radiaoButton;// 自动巡检选项
	RadioButton self_radiaoButton;// 手动巡检选项
	TextView ip_address, server_port;// ip和端口设置
	TextView time_interval, server_name;// gps实时上报间隔和服务器地址
	MyImageButton settingDetial_save;// 保存按钮
	ApplicationApp app;// 系统全局变量
	SharedPreferences spf;// 本地文件
	Editor editor;// 修改本地文件
	Button back, main;
	TextView error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingdetial);
		app = (ApplicationApp) getApplicationContext();
		spf = getSharedPreferences("save_password", MODE_PRIVATE);
		editor = spf.edit();
		MyApplication.checkIp = 2;
		init();
	}

	/**
	 * 初始化界面
	 */
	private void init() {
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		back.setOnClickListener(this);
		main.setOnClickListener(this);

		main.setVisibility(View.GONE);

		relativeLayout = (RelativeLayout) findViewById(R.id.setting_interval_relativeLayout);
		auto_radiaoButton = (RadioButton) findViewById(R.id.auto_radiaoButton);
		self_radiaoButton = (RadioButton) findViewById(R.id.self_radiaoButton);

		relativeLayout.setOnClickListener(this);
		time_interval = (TextView) findViewById(R.id.setting_interval_textview);
		ip_address = (TextView) findViewById(R.id.setting_ip);
		if(getSharedPreferences("save_password", MODE_PRIVATE).getString("ip",null) == null){
		editor.putString("ip", "123.124.230.18").commit();
		}
		ip_address.setText(getSharedPreferences("save_password", MODE_PRIVATE).getString("ip",
				"123.124.230.18"));
		ip_address.setOnClickListener(this);
		server_port = (TextView) findViewById(R.id.setting_port);
		server_port.setText(getSharedPreferences("save_password", MODE_PRIVATE).getString("port",
				"8080"));
		server_port.setOnClickListener(this);
		server_name = (TextView) findViewById(R.id.setting_serverName);
		// server_name.setOnClickListener(this);

		settingDetial_save = (MyImageButton) findViewById(R.id.settingDetial_save);
		settingDetial_save.setOnClickListener(this);
		error = (TextView) findViewById(R.id.setting_m);
		error.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
		return super.onKeyDown(keyCode, event);

	}

	@SuppressLint("WrongConstant")
	@Override
	public void onClick(View v) {
		// // TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		// 设置两个点之间可允许的误差距离(关键点位置和当前位置)
		case R.id.setting_m:
			intent.setClass(SettingDtial.this, DialogEditActivity.class);
			intent.putExtra("flag", TypeQuest.SETTING_M);
			intent.putExtra("Tittle", "误差距离");
			startActivityForResult(intent, TypeQuest.SETTING_M);
			break;
		// 设置gps实时上报的间隔时间
		case R.id.setting_interval_relativeLayout:
			intent.setClass(SettingDtial.this, DialogActivity.class);
			intent.putExtra("flag", TypeQuest.SETTINGDETIAL_TIME_INTERVAL);
			startActivityForResult(intent, TypeQuest.SETTINGDETIAL_TIME_INTERVAL);
			break;
		// 保存按钮事件
		case R.id.settingDetial_save:
			// 判断选择自动或手动巡检，保存到系统全局变量
			if (auto_radiaoButton.isChecked()) {
				MyApplication.inspectionType = "auto";
			} else {
				MyApplication.inspectionType = "self";
			}
			String ip = ip_address.getText().toString().trim();
			String port = server_port.getText().toString().trim();
			String interval = time_interval.getText().toString();
			if (!(Tools.checkDouble(error.getText().toString().trim()))) {
				Toast.makeText(SettingDtial.this, "请填写数字,否则按100设置", 2000).show();
			}
			// 判断服务器ip和端口号是否为空，不为空，将选择的系统设置信息保存在本地
			if (!(ip.equals("")) && !(port.equals(""))) {

				editor.putString("ip", ip);
				editor.putString("port", port);
				editor.putString("interval", interval);
				editor.putString("inspectionType", MyApplication.inspectionType);
				editor.putString("m", Tools.checkDouble(error.getText().toString().trim()) ? error
						.getText().toString().trim() : "100.0");
				editor.commit();
				Toast.makeText(SettingDtial.this, "保存成功", 2000).show();
				Intent intent1=new Intent(SettingDtial.this, Login.class);
				startActivity(intent1);
			} else {
				Toast.makeText(SettingDtial.this, "填写ip和端口号", 2000).show();
			}
			break;
		case R.id.setting_ip:
			intent.setClass(SettingDtial.this, DialogEditIpActivity.class);
			intent.putExtra("flag", TypeQuest.SETTING_IP_ADDRESS);
			intent.putExtra("Tittle", "ip地址");
			MyApplication myApplication = new MyApplication(SettingDtial.this);
			if (myApplication.ip != "") {
				intent.putExtra("ip", myApplication.ip);
			}
			// 跳转到设置ip界面，根据TypeQuest.SETTING_IP_ADDRESS判断返回
			startActivityForResult(intent, TypeQuest.SETTING_IP_ADDRESS);
			break;
		case R.id.setting_port:
			intent.setClass(SettingDtial.this, DialogEditActivity.class);
			intent.putExtra("flag", TypeQuest.SETTING_PORT);
			intent.putExtra("Tittle", "端口号");
			if (!"".equals(server_port.getText().toString())) {
				intent.putExtra("text", server_port.getText().toString());
			}
			// 跳转到设置端口号界面，根据TypeQuest.SETTING_PORT判断返回
			startActivityForResult(intent, TypeQuest.SETTING_PORT);
			break;
		case R.id.setting_serverName:
			intent.setClass(SettingDtial.this, DialogEditActivity.class);
			intent.putExtra("flag", TypeQuest.SETTING_SERVER_NAME);
			intent.putExtra("Tittle", "服务名称");
			if (!"".equals(server_name.getText().toString())) {
				intent.putExtra("text", server_name.getText().toString());
			}
			// 跳转到设置服务名称界面，根据TypeQuest.SETTING_SERVER_NAME判断返回
			startActivityForResult(intent, TypeQuest.SETTING_SERVER_NAME);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TypeQuest.SETTINGDETIAL_TIME_INTERVAL:
			if (resultCode == Activity.RESULT_OK) {
				time_interval.setText(data.getStringExtra("time_interval"));
			}
			break;
		case TypeQuest.SETTING_IP_ADDRESS:
			if (resultCode == Activity.RESULT_OK) {
				ip_address.setText(data.getStringExtra("ip_address"));
			}
			break;
		case TypeQuest.SETTING_PORT:
			if (resultCode == Activity.RESULT_OK) {
				server_port.setText(data.getStringExtra("port"));
			}
			break;
		case TypeQuest.SETTING_M:
			if (resultCode == Activity.RESULT_OK) {
				error.setText(data.getStringExtra("SETTING_M"));
			}
			break;
		case TypeQuest.SETTING_SERVER_NAME:
			if (resultCode == Activity.RESULT_OK) {
				server_name.setText(data.getStringExtra("server_name"));
			}
			break;
		}
	}

}
