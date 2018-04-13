package com.geok.langfang.pipeline.dataacquisition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.toolcase.KeypointAcquisition;
import com.geok.langfang.tools.Tools;

/**
 * 
 * @类描述 数据采集的菜单页
 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel:***********
 * @createDate 2013-10-12 上午10:21:09
 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
 */
public class DataAcquisition extends Activity implements OnClickListener {

	Button protect_bt, natural_bt, ground_bt, corrosive_bt, keypoint_bt;
	Button back, main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dataacquisition);
		protect_bt = (Button) findViewById(R.id.protect_bt);
		natural_bt = (Button) findViewById(R.id.natural_bt);
		ground_bt = (Button) findViewById(R.id.ground_bt);
		corrosive_bt = (Button) findViewById(R.id.corrosive_bt);
		keypoint_bt = (Button) findViewById(R.id.keypoint);
		protect_bt.setOnClickListener(this);
		natural_bt.setOnClickListener(this);
		ground_bt.setOnClickListener(this);
		corrosive_bt.setOnClickListener(this);
		keypoint_bt.setOnClickListener(this);
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DataAcquisition.this.finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});
		main.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Tools.backMain(DataAcquisition.this);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent dataacquisition = new Intent();
		switch (v.getId()) {
		case R.id.protect_bt:
			//保护电位测试
			dataacquisition.setClass(DataAcquisition.this, Protect.class);
			break;
		case R.id.natural_bt:
			//自然电位测试
			dataacquisition.setClass(DataAcquisition.this, Natural.class);
			break;
		case R.id.ground_bt:
			//接地电阻测试
			dataacquisition.setClass(DataAcquisition.this, Ground.class);
			break;
		case R.id.corrosive_bt:
			//防腐电阻测试
			dataacquisition.setClass(DataAcquisition.this, Corrosive.class);
			break;
		case R.id.keypoint:
			//关键点采集
			dataacquisition.setClass(DataAcquisition.this, KeypointAcquisition.class);
		}
		startActivity(dataacquisition);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
