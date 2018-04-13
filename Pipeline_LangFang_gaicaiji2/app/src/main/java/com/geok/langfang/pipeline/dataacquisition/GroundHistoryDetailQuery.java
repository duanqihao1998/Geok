package com.geok.langfang.pipeline.dataacquisition;

import android.app.Activity;
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

import com.geok.langfang.jsonbean.HistoryDataGroundBean;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.tools.Tools;

import java.util.List;

/**
 * 
 * 接地电阻的历史记录的查询服务器上的数据详情
 * 
 */
public class GroundHistoryDetailQuery extends Activity implements View.OnClickListener {

	GroundHistoryData data;
	HistoryDataGroundBean bean;
	TextView test_date;
	TextView set_value;
	TextView test_value;
	TextView year;
	TextView cpgroundbed;
	TextView halfyear;
	TextView conclusion;
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
		setContentView(R.layout.grounddetial);
		bean = (HistoryDataGroundBean) getIntent().getSerializableExtra("values");
		test_date = (TextView) findViewById(R.id.ground_detial_test_date);
		set_value = (TextView) findViewById(R.id.ground_detial_set_value);
		test_value = (TextView) findViewById(R.id.ground_detial_test_value);
		year = (TextView) findViewById(R.id.ground_detial_year);
		cpgroundbed = (TextView) findViewById(R.id.ground_detial_cpgroundbed);
		halfyear = (TextView) findViewById(R.id.ground_detial_halfyear);
		conclusion = (TextView) findViewById(R.id.ground_detial_conclusion);
		isupload = (TextView) findViewById(R.id.ground_detial_isupload);
		test_date.setText("测试日期：" + bean.getTESTDATE());
		set_value.setText("规定值(V)：" + bean.getSETVALUE());
		test_value.setText("测试值(V)：" + bean.getTESTVALUE());
		year.setText("年份：" + bean.getYEAR());
		cpgroundbed.setText("地床编号：" + bean.getCPGROUNDBEDEVENTID());
		halfyear.setText("半年：" + bean.getHALFYEAR());
		conclusion.setText("结论：" + bean.getCONCLUSION());
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
				intent.setClass(GroundHistoryDetailQuery.this, Ground.class);
				intent.putExtra("values", data);
				startActivity(intent);

			} else {
				Toast.makeText(getApplicationContext(), "数据已上报,不能修改", 1000).show();
			}
			break;

		case 3:// 删除
			Builder builder = new Builder(GroundHistoryDetailQuery.this);
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
		// TODO Auto-generated method stub
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
