package com.geok.langfang.pipeline.problem;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author sunshihai 问题上报历史详细记录 这个是本地数据的检索的详细记录
 * 
 */
public class ProblemHistoryDetial extends Activity implements View.OnClickListener {

	ProblemHistoryData data;
	GrallyImageView grallyImageView;
	TextView type;
	TextView lonlat;
	TextView occurtime;
	TextView line;
	TextView pile;
	TextView offset;
	TextView user;
	TextView uploadTime;
	TextView problemdes;
	TextView Location;
	TextView Plan;
	TextView Result;
	TextView isupload;
	String guid = "";
	/*
	 * 标识是否上报
	 */
	int flag = 0;
	List<String> image, imagedes;
	ImageView imageview;

	MyApplication myApplication;
	// 异步处理数据请求 分别是处理数据上报成功 和点击上报因网络原因 实际上未成功的操作
	public Handler MyHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Tools.stopProgressDialog(ProblemHistoryDetial.this);
			OperationDB db = new OperationDB(getApplicationContext());
			String str = msg.getData().getString("result");
			String emp[] = str.split("\\|");
			if (emp[0].equals("OK")) {
				db.DBupdate(1, guid, Type.PROBLEM_UPLOAD);
				Toast.makeText(ProblemHistoryDetial.this, "上报成功", 500).show();
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			} else {
				db.DBupdate(0, guid, Type.PROBLEM_UPLOAD);
				Toast.makeText(ProblemHistoryDetial.this, "上报失败", 500).show();
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.problemdetial);
		myApplication = new MyApplication(this);
		data = (ProblemHistoryData) getIntent().getSerializableExtra("values");
		image = new ArrayList<String>();
		imagedes = new ArrayList<String>();
		grallyImageView = new GrallyImageView();

		type = (TextView) findViewById(R.id.problem_detial_type);
		type.setText("问题类型：" + data.getType());
		lonlat = (TextView) findViewById(R.id.problem_detial_latlon);
		lonlat.setText("经纬度：" + data.getLat() + "," + data.getLon());
		occurtime = (TextView) findViewById(R.id.problem_detial_occurtime);
		line = (TextView) findViewById(R.id.problem_detial_line);
		pile = (TextView) findViewById(R.id.problem_detial_pile);
		offset = (TextView) findViewById(R.id.problem_detial_offset);
		// user = (TextView) findViewById(R.id.problem_detial_userid);
		uploadTime = (TextView) findViewById(R.id.problem_detial_uploadtime);
		problemdes = (TextView) findViewById(R.id.problem_detial_problemdes);
		isupload = (TextView) findViewById(R.id.problem_detial_isupload);
		Location = (TextView) findViewById(R.id.problem_detial_location);
		Plan = (TextView) findViewById(R.id.problem_detial_plan);
		Result = (TextView) findViewById(R.id.problem_detial_result);

		Location.setText("问题发生地点：" + data.getLocation());
		Plan.setText("问题解决方案：" + data.getPlan());
		Result.setText("问题解决情况：" + data.getResult());

		line.setText("线路：" + data.getLine());
		pile.setText("桩号：" + data.getPile());
		offset.setText("偏移量（m）：" + data.getOffset());
		occurtime.setText("发生时间：" + data.getOccurtime());
		// user.setText("巡检人员："+data.getUserid());
		uploadTime.setText("上报时间：" + data.getUploadtime());
		problemdes.setText("问题描述：" + data.getProblemdes());
		flag = data.getIsupload();
		if (data.getIsupload() == 1) {
			isupload.setText("是否上报服务器：" + "已上报");
			isupload.setTextColor(Color.BLACK);

		} else {
			isupload.setText("是否上报服务器：" + "未上报");
			isupload.setTextColor(Color.RED);
		}
		guid = data.getGuid();
		String photopath = data.getPhotopath();
		String photodes = data.getPhotodes();
		String[] temppath = photopath.split("#");
		String[] tempdes = photodes.split("#");
		for (int i = 0; i < temppath.length; i++) {
			image.add(temppath[i]);
		}
		for (int j = 0; j < tempdes.length; j++) {
			imagedes.add(tempdes[j]);
		}
		imageview = (ImageView) findViewById(R.id.problem_detial_image);
		if(image.size()>0){
		imageview.setImageBitmap(getBitmap(image.get(0)));
		}

	}

	public Bitmap getBitmap(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回 bm 为空
		options.inJustDecodeBounds = false; // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = (int) (options.outHeight / (float) 320);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be; // 重新读入图片，注意此时已经把 options.inJustDecodeBounds
									// 设回 false 了
		bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
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

			if (flag != 1) {
				Request queset = new Request(MyHandler);
				List<String> image = new ArrayList<String>();
				List<String> imagedes = new ArrayList<String>();
				String temp[] = data.getPhotopath().split("#");
				for (int i = 0; i < temp.length; i++) {
					image.add(temp[i]);
				}
				String temp1[] = data.getPhotodes().split("#");
				for (int i = 0; i < temp.length; i++) {
					imagedes.add(temp1[i]);
				}
				if (Tools.isNetworkAvailable(this, true)) {
					Tools.startProgressDialog(ProblemHistoryDetial.this, "上报中，请稍后...");
					queset.problemUploadRequest(guid, myApplication.userid, data.getLineid(),
							data.getOccurtime(), data.getOffset(), data.getType(),
							data.getProblemdes(), image, myApplication.depterid,
							data.getUploadtime(), data.getLat(), data.getLon(), imagedes,
							data.getPileid(), data.getLocation(), data.getPlan(), data.getResult(), "");
				}

				// webservice请求
				// queset.problemUploadRequest(guid, MyApplication.userid,
				// data.getLineid(), data.getPileid(), data.getOffset(),
				// data.getType(), data.getOccurtime(), image, imagedes,
				// data.getProblemdes(), data.getUploadtime(),
				// data.getLon(), data.getLat(), MyApplication.depterid,
				// data.getLocation(), data.getPlan(), data.getResult());

			} else {
				Toast.makeText(getApplicationContext(), "数据已上报", 1000).show();
			}

			break;

		case 2:// 修改

			if (flag !=1) {
				Intent intent = new Intent();
				intent.setClass(ProblemHistoryDetial.this, Problem.class);
				intent.putExtra("values", data);
				intent.putExtra("flag", "upgrate");
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

			} else {
				Toast.makeText(getApplicationContext(), "数据已上报,不能修改", 1000).show();
			}
			break;

		case 3:// 删除

			Builder builder = new Builder(ProblemHistoryDetial.this);
			builder.setTitle("提示");
			builder.setMessage("是否删除本次记录");
			builder.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					OperationDB op = new OperationDB(getApplicationContext());
					ContentValues value = new ContentValues();
					value.put("guid", guid);
					op.DBdelete(value, Type.PROBLEM_UPLOAD);
					ProblemHistoryDetial.this.finish();
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
			// Intent intent=new Intent();
			// intent.setClass(ProblemHistoryDetial.this, ProblemHistory.class);
			// startActivity(intent);
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case 31:
			// Intent intent2=new Intent();
			// intent2.setClass(ProblemHistoryDetial.this, MainView.class);
			// startActivity(intent2);
			Tools.backMain(this);
			break;
		}

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
