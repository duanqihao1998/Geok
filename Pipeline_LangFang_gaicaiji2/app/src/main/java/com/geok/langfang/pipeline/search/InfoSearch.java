package com.geok.langfang.pipeline.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.geok.langfang.pipeline.R;
import com.geok.langfang.tools.Tools;

public class InfoSearch extends Activity implements OnClickListener {

	Button infosearch_assessment, current_line_info, line_info;
	Button back, main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infosearch);
		infosearch_assessment = (Button) findViewById(R.id.infosearch_assessment);
		infosearch_assessment.setOnClickListener(this);
		current_line_info = (Button) findViewById(R.id.infosearch_info);
		current_line_info.setOnClickListener(this);
		line_info = (Button) findViewById(R.id.line_info);
		line_info.setOnClickListener(this);
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		back.setOnClickListener(this);
		main.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();

		switch (v.getId()) {
		case R.id.line_info:

			intent.setClass(this, LineInfo.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

			break;

		case R.id.infosearch_assessment:
			intent.setClass(InfoSearch.this, Assessment.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

			break;

		case R.id.infosearch_info:
			intent.setClass(InfoSearch.this, CurrentLineInfo.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

			break;

		case R.id.back:
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case R.id.main:
			Tools.backMain(this);
			break;

		}

		// }
		// public void uriToActivity(String name, String uri) {
		// Intent intent = new Intent();
		// intent.setClass(InfoSearch.this, Assessment.class);
		// Bundle bundle = new Bundle(); bundle.putString("name", name);
		// bundle.putString("uri", uri); intent.putExtras(bundle);
		// startActivity(intent);
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
