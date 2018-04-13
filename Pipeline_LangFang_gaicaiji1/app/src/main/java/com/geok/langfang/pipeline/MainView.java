package com.geok.langfang.pipeline;

import com.geok.langfang.pipeline.dataacquisition.DataAcquisition;
import com.geok.langfang.pipeline.inspection.InspectionRecord;
import com.geok.langfang.pipeline.map.ArcMap;
import com.geok.langfang.pipeline.map.BaiduMap;
import com.geok.langfang.pipeline.map.MainMap;
import com.geok.langfang.pipeline.notification.Notification;
import com.geok.langfang.pipeline.problem.Problem;
import com.geok.langfang.pipeline.search.InfoSearch;
import com.geok.langfang.pipeline.setting.Setting;
import com.geok.langfang.pipeline.statistics.StatisticsActivity;
import com.geok.langfang.pipeline.toolcase.ToolCase;
import com.geok.langfang.tools.ItemImageAdaper;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.util.OffnetReceive;
import com.geok.langfang.pipeline.Mywork.Mywork;
import com.geok.langfang.pipeline.alarm.*;
import com.geok.langfang.request.MessagePush;
import com.geok.langfang.request.MyApplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainView extends Activity implements OnItemClickListener, OnClickListener {

	GridView gridView;
	int[] upgrate = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private ItemImageAdaper itemImageAdaper;

	/**
	 * 图片命名： "我的工作" menu_mywork_selector , "事项上报" menu_problem_selector, "巡检日志"
	 * menu_record_selector, "紧急报警" menu_sys_alarm_selector, "信息查询"
	 * menu_search_selector, "数据采集" menu_data_selector, "系统配置"
	 * menu_setting_small_selector, "GPS导航" menu_gps_selector, "工具箱"
	 * menu_tools_selector, "统计分析" menu_statistics_selector, "站内信"
	 * menu_notification_selector,"地图浏览"menu_map_selector
	 */
	int[] image = { R.drawable.menu_map_selector, R.drawable.menu_statistics_selector,
			R.drawable.menu_notification_selector, R.drawable.menu_mywork_selector,
			R.drawable.menu_problem_selector, R.drawable.menu_record_selector,
			R.drawable.menu_data_selector, R.drawable.menu_search_selector,
			R.drawable.menu_sys_alarm_selector, R.drawable.menu_gps_selector,
			R.drawable.menu_setting_selector, R.drawable.menu_tools_selector, };
	private String[] title = { "实时监控", "统计分析", "站内信", "我的工作", "事项上报", "巡检日志", "数据采集", "信息查询",
			"系统警告", "GPS导航", "系统配置", "工具箱" };

	private MyBroadcaseReceiver receiver;
	OffnetReceive offnetReceiver;
	LinearLayout netsetting;

	private class MyBroadcaseReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//
			String str = intent.getStringExtra("flag");
			// if(str!=null)
			// {
			upgrate[3]++;
			Toast.makeText(MainView.this, str, 500).show();
			itemImageAdaper.notifyDataSetChanged();
			gridView.setAdapter(itemImageAdaper);
			gridView.invalidate();
			// }

		}

	}

	MyApplication myApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainview);
		gridView = (GridView) findViewById(R.id.gride);
		itemImageAdaper = new ItemImageAdaper(this, image, title, R.layout.mainview_icon, upgrate);
		gridView.setAdapter(itemImageAdaper);
		gridView.setOnItemClickListener(this);
		myApplication = new MyApplication(this);
		Intent intent = new Intent(MainView.this, MessagePush.class);
		startService(intent);
		// netsetting = (LinearLayout)findViewById(R.id.main_offnet);
		// netsetting.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent();
		// intent.setAction(Settings.ACTION_SETTINGS);
		// startActivity(intent);
		// }
		// });
	}

	@Override
	protected void onResume() {
		super.onResume();
		receiver = new MyBroadcaseReceiver();
		IntentFilter filter = new IntentFilter("com.geok.langfang.pipeline.refresh");
		registerReceiver(receiver, filter);
		offnetReceiver = new OffnetReceive(this);
		IntentFilter filter1 = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(offnetReceiver, filter1);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(offnetReceiver);
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
		Tools.stopProgressDialog(MainView.this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Tools.mainBack(MainView.this);
			Tools.cancel(MainView.this);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (arg2) {
		case 3:
			intent.setClass(MainView.this, Mywork.class);
			startActivity(intent);
			break;

		case 4:
			intent.setClass(MainView.this, Problem.class);
			startActivity(intent);
			break;

		case 5:
			intent.setClass(MainView.this, InspectionRecord.class);
			startActivity(intent);
			break;

		case 8:
			intent.setClass(MainView.this, Alarm.class);
			startActivity(intent);
			break;

		case 7:
			intent.setClass(MainView.this, InfoSearch.class);
			startActivity(intent);
			break;

		case 6:
			intent.setClass(MainView.this, DataAcquisition.class);
			startActivity(intent);
			break;

		case 10:
			intent.setClass(MainView.this, Setting.class);
			startActivity(intent);
			break;

		case 9:
			if (isApkAvailable("com.baidu.BaiduMap")) {
				intent = getPackageManager().getLaunchIntentForPackage("com.baidu.BaiduMap");
				startActivity(intent);
			} else {// 未安装，跳转至market下载该程序
				Toast.makeText(MainView.this, "请下载百度地图后重试", 500).show();
				// Uri uri =
				// Uri.parse("market://details?id=com.baidu.BaiduMap");
				// intent = new Intent(Intent.ACTION_VIEW, uri);
				// startActivity(intent);
			}

			break;

		case 11:
			// Toast.makeText(MainView.this, "工具箱暂未开放", 500).show();
			intent.setClass(MainView.this, ToolCase.class);
			startActivity(intent);
			// startActivity(new Intent(Settings.ACTION_SETTINGS));
			break;

		case 1:
			intent.setClass(MainView.this, StatisticsActivity.class);
			startActivity(intent);
			break;

		case 2:
			intent.setClass(MainView.this, Notification.class);
			startActivity(intent);
			break;

		case 0:
			intent.setClass(MainView.this, BaiduMap.class);
			startActivity(intent);
			break;
		}
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, "退出").setIcon(R.drawable.exit);
		menu.add(0, 2, 2, "帮助").setIcon(R.drawable.help);
		menu.add(0, 3, 3, "账号注销").setIcon(R.drawable.cancel);
		menu.add(0, 4, 4, "GPS").setIcon(R.drawable.gps);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 1:
			Tools.exit(MainView.this);
			break;
		case 2:
			Tools.help(MainView.this);
			break;
		case 3:
			Tools.cancel(MainView.this);
			break;
		case 4:
			Tools.gps(MainView.this);
			break;
		}
		// overridePendingTransition(R.anim.push_right_in,
		// R.anim.push_right_out);
		return super.onOptionsItemSelected(item);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	// @Override
	// public void onBackPressed() {
	// Tools.exit(MainView.this);
	// super.onBackPressed();
	// }

}
