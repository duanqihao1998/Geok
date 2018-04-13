package com.geok.langfang.pipeline.setting;

import com.geok.langfang.pipeline.R;

import com.geok.langfang.tools.Tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Setting extends Activity implements OnClickListener {
	Button setting, sync;// 系统设置，数据同步
	Button back, main;// 返回上个界面，返回主界面

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		setting = (Button) findViewById(R.id.setting);
		sync = (Button) findViewById(R.id.sync);
		setting.setOnClickListener(this);
		sync.setOnClickListener(this);

		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		back.setOnClickListener(this);
		main.setOnClickListener(this);

		main.setVisibility(View.GONE);

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
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		// 跳转到系统设置界面
		case R.id.setting:
			intent.setClass(Setting.this, SettingDtial.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		// 跳转到数据同步界面
		case R.id.sync:
			intent.setClass(Setting.this, Sync.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		// 关闭本界面，返回上个界面
		case R.id.back:
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		// 返回主界面
//		case R.id.main:
//			Tools.backMain(this);
//			break;

		}

	}

}
