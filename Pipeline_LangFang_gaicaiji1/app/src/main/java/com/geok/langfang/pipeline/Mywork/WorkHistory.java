package com.geok.langfang.pipeline.Mywork;

import com.geok.langfang.pipeline.Login;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.tools.Tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class WorkHistory extends Activity {

	Button back, main;// 返回，主界面
	TextView workh_null;// 没有数据时显示
	TextView workh_num;// 共有几条数据
	ListView workh_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrokhistory);
		initView();
		initData();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
		return super.onKeyDown(keyCode, event);

	}

	public void initView() {
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		workh_null = (TextView) findViewById(R.id.workh_null);
		workh_num = (TextView) findViewById(R.id.workh_num);
		workh_list = (ListView) findViewById(R.id.workh_list);

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});
		main.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Tools.backMain(WorkHistory.this);
			}
		});

		workh_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(WorkHistory.this, MyworkhDetailActivity.class);
				intent.putExtra("position", arg2);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

	}

	public void initData() {
		if (Login.listTask.size() == 0) {
			workh_null.setVisibility(View.VISIBLE);
		} else {
			WorkhAdapter adapter = new WorkhAdapter(WorkHistory.this, Login.listTask);
			workh_list.setAdapter(adapter);
		}
	}

}
