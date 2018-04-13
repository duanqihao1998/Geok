package com.geok.langfang.pipeline.toolcase;

import com.geok.langfang.pipeline.MainView;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.notification.Notification;
import com.geok.langfang.tools.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * 
 * @类描述 数据采集的菜单页
 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel:***********
 * @createDate 2013-10-12 上午10:21:09
 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
 */
public class ToolCase extends Activity implements OnClickListener {

	Button compass_bt, barograph_bt, calculator_bt, flashlight_bt;
	Button back, main;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.toolcase);
		compass_bt = (Button) findViewById(R.id.compass_bt);
		barograph_bt = (Button) findViewById(R.id.barograph_bt);
		calculator_bt = (Button) findViewById(R.id.calculator_bt);
		flashlight_bt = (Button) findViewById(R.id.flashlight_bt);
		
		compass_bt.setOnClickListener(this);
		barograph_bt.setOnClickListener(this);
		calculator_bt.setOnClickListener(this);
		flashlight_bt.setOnClickListener(this);
		

		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});
		main.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Tools.backMain(ToolCase.this);
			}
		});
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
		case R.id.compass_bt:
			if (isApkAvailable("com.ee772compass")) {
				intent = getPackageManager().getLaunchIntentForPackage("com.ee772compass");
				startActivity(intent);
			} else {
				Toast.makeText(ToolCase.this, "请下载电子罗盘后重试", 500).show();
				// Uri uri = Uri.parse("market://details?id=com.ee772compass");
				// intent = new Intent(Intent.ACTION_VIEW, uri);
			}
			break;
		case R.id.barograph_bt:
			LocationManager locationManager = (LocationManager) ToolCase.this
					.getSystemService(Context.LOCATION_SERVICE);
			if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
				Toast.makeText(this, "已开启GPS", 1000).show();
				// intent.setClassName("com.unistrong.ee772tool",
				// "com.unistrong.ee772tool.BarographActivity");
				if (isApkAvailable("com.unistrong.ee772tool")) {
					intent = getPackageManager().getLaunchIntentForPackage(
							"com.unistrong.ee772tool");
					startActivity(intent);
				} else if (isApkAvailable("com.unistrong.barometer")) {
					intent = getPackageManager().getLaunchIntentForPackage(
							"com.unistrong.barometer");
					startActivity(intent);
				} else {
					Toast.makeText(ToolCase.this, "请下载气压计后重试", 500).show();
				}
			} else {
				Intent opengps = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				Toast.makeText(ToolCase.this, "请开启GPS", 1000).show();
				this.startActivity(opengps);
			}
			break;
		case R.id.calculator_bt:
			if (isApkAvailable("com.android.calculator2")) {
				intent = getPackageManager().getLaunchIntentForPackage("com.android.calculator2");
				startActivity(intent);
			} else {// 未安装，跳转至market下载该程序
				Toast.makeText(ToolCase.this, "请下载计算器后重试", 500).show();
				Uri uri = Uri.parse("market://details?id=com.android.calculator2");
				intent = new Intent(Intent.ACTION_VIEW, uri);
			}
			break;
		case R.id.flashlight_bt:
			if (isApkAvailable("com.ee772flashlight")) {
				intent = getPackageManager().getLaunchIntentForPackage("com.ee772flashlight");
				startActivity(intent);
			} else {
				Toast.makeText(ToolCase.this, "请下载手电筒后重试", 500).show();
				// Uri uri =
				// Uri.parse("market://details?id=com.ee772flashlight");
				// intent = new Intent(Intent.ACTION_VIEW, uri);
			}
			break;
		}

	}

	private boolean isApkAvailable(String packagename) {
		PackageInfo packageInfo;
		try {
			packageInfo = this.getPackageManager().getPackageInfo(packagename, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
		}
		if (packageInfo == null) {
			return false;
		} else {
			return true;
		}
	}
}
