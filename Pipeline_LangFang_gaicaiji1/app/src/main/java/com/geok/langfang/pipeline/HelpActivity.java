package com.geok.langfang.pipeline;

import java.util.ArrayList;
import java.util.List;

import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.VersionBean;
import com.geok.langfang.pipeline.R.style;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomDialog;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.tools.TypeQuest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HelpActivity extends Activity {
	Button back, main, update;
	Tools tools;
	TextView text_help;
	Request request;
	String data0;
	String current_version = "", new_version = "", update_content = "", content = "",
			update_type = "", type = "";
	List<VersionBean> versionlistBean = new ArrayList<VersionBean>();
	MyApplication myApplication;
	String version = "";
	Handler handler = new Handler() {

		@SuppressLint("WrongConstant")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			tools.stopProgressDialog(HelpActivity.this);
			switch (msg.arg1) {
			case 0:
				data0 = msg.getData().getString("result");
				if (data0.equals("-1")) {
					Toast.makeText(HelpActivity.this, "已是最新版本", 1000).show();
				} else {
					if (msg.getData().getInt("flag") == 1) {
						if (!data0.contains("/")) {
							Toast.makeText(HelpActivity.this, "更新错误，请联系管理员", 1000).show();
						} else {
							Intent i = new Intent(HelpActivity.this, UpdateActivity.class);
							i.putExtra("url", data0);
							startActivity(i);
						}
					}
				}
				break;
			case 70:
				String update_str = msg.getData().getString("result");
				if (msg.getData().getInt("flag") == 1) {
					if("-1".equals(update_str)){
						Toast.makeText(HelpActivity.this, "已是最新版本", 1000).show();
					}else if (update_str.contains("ERR") ) {
						 Toast.makeText(HelpActivity.this, "更新出错，请联系管理员",
						 1000).show();
					} else {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("update_str", update_str).commit();
						versionlistBean = Json.getVersionList(update_str);
						if (versionlistBean.size() > 0) {
							HelpActivity.this.showDialog(TypeQuest.CUSTOM_DIALOG);
						} else {
							Toast.makeText(HelpActivity.this, "更新出错，请联系管理员", 1000).show();
						}
					}
				} else {
					 Toast.makeText(HelpActivity.this, "连接错误，请检查ip或网络设置", 1000).show();
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		request = new Request(handler);
		tools = new Tools();
		myApplication = new MyApplication(this);
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		update = (Button) findViewById(R.id.update);
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
				Tools.backMain(HelpActivity.this);
			}
		});
		
		try {
			version = getVersionCode();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Tools.isNetworkAvailable(HelpActivity.this, true)) {
					if (myApplication.ip != null && myApplication.port != null) {
							request.VersionUpdateRequest(myApplication.userid, myApplication.imei,
									version);
					}
				}
			}
		});

		
		text_help = (TextView) findViewById(R.id.text_help);
		text_help.setText("    " + getResources().getString(R.string.app_name) + "Version"
				+ version + "\r\n" + "    " + "此版本适用于Android4.0及以上版本系统，屏幕分辨率不高于1280*720的移动巡检终端。");
		text_help.setTextColor(Color.BLACK);
	}

	/*
	 * 获取当前程序的版本号
	 */
	private String getVersionCode() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		return packInfo.versionName;
	}

	@Override
	public Dialog onCreateDialog(int dialogId) {
		Dialog dialog = null;
		{
			if (versionlistBean.get(0) != null)
				new_version = versionlistBean.get(0).getVERSION();
			update_content = versionlistBean.get(0).getURL();
			content = update_content.replaceAll(";", "\r\n");
			update_type = versionlistBean.get(0).getTYPE();
			if ("0".equals(update_type)) {
				type = "必要更新";
			} else {
				type = "非必要更新";
			}
		}
		switch (dialogId) {
		case TypeQuest.CUSTOM_DIALOG:
			CustomDialog.Builder customBuilder = new CustomDialog.Builder(HelpActivity.this);
			customBuilder.setTitle("升级提醒").setMessage(content);
			customBuilder.setVersion("新版本：" + new_version).setType("(" + type + ")");
			customBuilder.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			customBuilder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					request.sytemUpdateRequest(myApplication.userid,version);
				}
			});
			dialog = customBuilder.create();
			break;
		}
		return dialog;
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
