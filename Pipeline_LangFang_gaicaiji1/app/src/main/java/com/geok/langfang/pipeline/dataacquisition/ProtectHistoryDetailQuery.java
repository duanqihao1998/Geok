package com.geok.langfang.pipeline.dataacquisition;

import java.util.List;

import com.geok.langfang.jsonbean.HistoryDataProtectBean;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.tools.Tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 保护电位的历史记录的查询服务器上的数据详情
 * 
 */
public class ProtectHistoryDetailQuery extends Activity implements
		android.view.View.OnClickListener {

	ProtectHistoryData data;
	HistoryDataProtectBean bean;
	TextView year;
	TextView month;
	TextView line;
	TextView pile;
	TextView value;
	TextView testtime;
	TextView voltage;
	TextView temperature;
	TextView ground;
	TextView remarks;
	TextView natural;
	TextView ir;
	TextView isupload;
	String guid = "";
	/*
	 * 标识是否上报
	 */
	int flag = 0;
	List<String> image, imagedes;
	ImageView imageview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.protectdetial);
		bean = (HistoryDataProtectBean) getIntent().getSerializableExtra("values");

		year = (TextView) findViewById(R.id.protect_detial_year);
		month = (TextView) findViewById(R.id.protect_detial_month);
		line = (TextView) findViewById(R.id.protect_detial_line);
		pile = (TextView) findViewById(R.id.protect_detial_pile);
		value = (TextView) findViewById(R.id.protect_detial_value);

		testtime = (TextView) findViewById(R.id.protect_detial_testtime);
		voltage = (TextView) findViewById(R.id.protect_detial_voltage);
		temperature = (TextView) findViewById(R.id.protect_detial_temperature);
		ground = (TextView) findViewById(R.id.protect_detial_ground);
		natural = (TextView) findViewById(R.id.protect_detial_natural);
		ir = (TextView) findViewById(R.id.protect_detial_ir);
		remarks = (TextView) findViewById(R.id.protect_detial_remarks);
		isupload = (TextView) findViewById(R.id.protect_detial_isupload);
		year.setText("年份：" + bean.getYEAR());
		month.setText("月份：" + bean.getMONTH());
		line.setText("线路：" + bean.getLINELOOP());
		pile.setText("桩号：" + bean.getMARKERNAME());
		value.setText("电位值(V)：" + bean.getVOLTAGE());
		testtime.setText("测试时间时间：" + bean.getTESTDATE());
		voltage.setText("交流电干扰电压(V)：" + bean.getACINTERFERENCEVOLTAGE());
		temperature.setText("温度：" + bean.getTEMPERATURE());
		ground.setText("土壤电阻率：" + bean.getSOILRESISTIVITY());
		natural.setText("自然电位值(V)：" + bean.getNATURAL());
		ir.setText("IR降(V)：" + bean.getIR());
		remarks.setText("备注：" + bean.getREMARK());
		isupload.setText("是否上报服务器：" + "已上报");
		isupload.setTextColor(Color.BLACK);

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

			if (flag == 1) {

			} else {
				Toast.makeText(getApplicationContext(), "数据已上报", 1000).show();
			}
			break;

		case 2:// 修改

			if (flag == 1) {
				Intent intent = new Intent();
				intent.setClass(ProtectHistoryDetailQuery.this, Protect.class);
				intent.putExtra("values", data);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "数据已上报,不能修改", 1000).show();
			}
			break;

		case 3:// 删除

			AlertDialog.Builder builder = new Builder(ProtectHistoryDetailQuery.this);
			builder.setTitle("提示");
			builder.setMessage("是否删除本次记录");
			builder.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
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
		int tag = (Integer) v.getTag();
		switch (tag) {
		case 30:
			this.finish();
			break;
		case 31:
			Tools.backMain(this);
			break;
		}
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
}
