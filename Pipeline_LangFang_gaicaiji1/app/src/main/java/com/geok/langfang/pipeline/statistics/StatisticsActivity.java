package com.geok.langfang.pipeline.statistics;

import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.dataacquisition.Corrosive;
import com.geok.langfang.pipeline.dataacquisition.Ground;
import com.geok.langfang.pipeline.dataacquisition.Natural;
import com.geok.langfang.tools.Tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StatisticsActivity extends Activity implements OnClickListener {

	Button eventup_bt, quipment_bt, qualified_bt, unqualified_bt;
	Button back, main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics);

		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		eventup_bt = (Button) findViewById(R.id.eventup_bt);
		quipment_bt = (Button) findViewById(R.id.quipment_bt);
		qualified_bt = (Button) findViewById(R.id.qualified_bt);
		unqualified_bt = (Button) findViewById(R.id.unqualified_bt);
		eventup_bt.setOnClickListener(this);
		quipment_bt.setOnClickListener(this);
		qualified_bt.setOnClickListener(this);
		unqualified_bt.setOnClickListener(this);

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
				Tools.backMain(StatisticsActivity.this);
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent dataacquisition = new Intent();
		switch (v.getId()) {
		case R.id.eventup_bt:
			dataacquisition.setClass(StatisticsActivity.this, EventupStatisticsActivity.class);
			break;
		case R.id.quipment_bt:
			dataacquisition.setClass(StatisticsActivity.this, StatisticsDevice.class);
			break;
		case R.id.qualified_bt:
			dataacquisition.setClass(StatisticsActivity.this, StatisticsQualifiedRate.class);
			break;
		case R.id.unqualified_bt:
			dataacquisition.setClass(StatisticsActivity.this, StatisticsUnqualifiedPerson.class);
			break;
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
