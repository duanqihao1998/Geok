package com.geok.langfang.pipeline.toolcase;

import java.util.List;

import com.geok.langfang.Bean.KeypointHistoryBean;
import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.Tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class KeypointHistoryDetail extends Activity implements android.view.View.OnClickListener {

	KeypointHistoryBean data;

	TextView name,lon,lat,time,department,lineid,line,markerid,markername,markerstation,
				locationdes,mileage,offset,buffer,validitystart,validityend,description;
	TextView coordinate, isupload;
	String guid = "";
	/*
	 * 标识是否上报
	 */
	int flag = 0;
	List<String> image, imagedes;
	ImageView imageview;
	Request request;
	MyApplication myApplication;
	public Handler MyHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Tools.stopProgressDialog(KeypointHistoryDetail.this);
			OperationDB db = new OperationDB(getApplicationContext());
			String str = msg.getData().getString("result");
			String emp[] = str.split("\\|");
			if (emp[0].equals("OK")) {
				db.DBupdate(1, guid, Type.KEYPOINTACQUISITION);
				Toast.makeText(KeypointHistoryDetail.this, "上报成功", 500).show();
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			} else {
				db.DBupdate(0, guid, Type.KEYPOINTACQUISITION);
				Toast.makeText(KeypointHistoryDetail.this, "上报失败", 500).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keypoint_history_detial);
		myApplication = new MyApplication(this);
		request = new Request(MyHandler);
		data = (KeypointHistoryBean) getIntent().getSerializableExtra("values");

		name = (TextView) findViewById(R.id.keypoint_detail_name);
		coordinate = (TextView) findViewById(R.id.keypoint_detail_cor);
		line = (TextView) findViewById(R.id.keypoint_detial_line);
		offset = (TextView) findViewById(R.id.keypoint_detial_offset);
		buffer = (TextView) findViewById(R.id.keypoint_detial_buffer);
		validitystart = (TextView) findViewById(R.id.keypoint_detial_validitystart);
		validityend = (TextView) findViewById(R.id.keypoint_detial_validityend);
		description = (TextView) findViewById(R.id.keypoint_detial_description);
		isupload = (TextView) findViewById(R.id.keypoint_detial_isupload);

		name.setText("关键点名称：" + data.getName());
		coordinate.setText("坐标：" + data.getLon() + "," + data.getLat());
		line.setText("管线位置：" + data.getLine() + "," + data.getLocationdes());
		offset.setText("偏移量（m）：" + data.getOffset());
		buffer.setText("缓冲范围（m）：" + data.getBuffer());
		validitystart.setText("有效期起始日期：" + data.getStart());
		validityend.setText("有效期终止日期：" + data.getEnd());
		description.setText("描述：" + data.getDescription());
		

		flag = data.getIsupload();
		if (data.getIsupload() == 1) {
			isupload.setText("是否上报服务器：" + "已上报");
			isupload.setTextColor(Color.BLACK);
		} else {
			isupload.setText("是否上报服务器：" + "未上报");
			isupload.setTextColor(Color.RED);
		}
		guid = data.getGuid();

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
		menu.add(2, 2, 2, "删除");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 1: // 上报
//			String markername1,location;
//			markername1 = data.getMarkername();
//			if (markername1.contains("+")) {
//				String str = markername1.substring(markername1.indexOf("+") + 1,
//						markername1.lastIndexOf("m"));
//				int num = Integer.valueOf(str) + Integer.valueOf(data.getBuffer());
//				location = markername1.substring(0, markername1.indexOf("+")) + "+" + num
//						+ "m";
//			} else {
//				location = markername1 + "+" + data.getBuffer() + "m";
//			}
			if (flag != 1) {
				if (Tools.isNetworkAvailable(this, true)) {
					Tools.startProgressDialog(KeypointHistoryDetail.this, "上报中，请稍后...");
					request.KeypointRequest(data.getUserid(), myApplication.imei, data.getDepartment(),
							data.getName(), data.getLocationdes(), data.getBuffer(), data.getStart(), data.getEnd(), 
							data.getLon(), data.getLat(), data.getDescription(), data.getLineid(), data.getMileage(),"");
				}
			} else {
				Toast.makeText(getApplicationContext(), "数据已上报", 1000).show();
			}

			break;


		case 2:// 删除

			AlertDialog.Builder builder = new Builder(KeypointHistoryDetail.this);
			builder.setTitle("提示");
			builder.setMessage("是否删除本次记录");
			builder.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					OperationDB op = new OperationDB(getApplicationContext());
					ContentValues value = new ContentValues();
					value.put("guid", guid);
					op.DBdelete(value, Type.KEYPOINTACQUISITION);
					KeypointHistoryDetail.this.finish();
					overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;

		case 31:
			Tools.backMain(this);

			break;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
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
