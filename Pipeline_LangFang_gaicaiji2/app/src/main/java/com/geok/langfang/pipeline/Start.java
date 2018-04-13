package com.geok.langfang.pipeline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.geok.langfang.pipeline.map.BaiduMap;
import com.geok.langfang.pipeline.statistics.StatisticsActivity;

public class Start extends Activity {

	Handler handler;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);

		handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Start.this, Login.class);
				startActivity(intent);
				finish();
			}
		}, 1000);

	}

}