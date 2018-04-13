package com.geok.langfang.pipeline.problem;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.jsonbean.HistoryProblemImageLoadBean;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.tools.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author sunshihai
 * 
 *         事项上报的历史详细信息 这个是从网络请求的历史记录的详细信息
 * 
 */
public class ProblemHistoryDetialNet extends Activity implements View.OnClickListener {

	HistoryProblemImageLoadBean data;
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
	Button back, main;
	/*
	 * 标识是否上报
	 */
	int flag = 0;
	List<String> image, imagedes;
	ImageView imageview;

	public Handler MyHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			OperationDB db = new OperationDB(getApplicationContext());
			String str = msg.getData().getString("result");
			String emp[] = str.split("\\|");
			if (emp[0].equals("OK")) {
				db.DBupdate(1, guid, Type.PROBLEM_UPLOAD);
			} else {
				db.DBupdate(0, guid, Type.PROBLEM_UPLOAD);
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.problemdetial);

		data = (HistoryProblemImageLoadBean) getIntent().getSerializableExtra("values");
		image = new ArrayList<String>();
		imagedes = new ArrayList<String>();
		grallyImageView = new GrallyImageView();

		type = (TextView) findViewById(R.id.problem_detial_type);
		type.setText("问题类型：" + data.getTYPE());
		lonlat = (TextView) findViewById(R.id.problem_detial_latlon);
		lonlat.setText("经纬度：" + data.getLAT() + "," + data.getLON());
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
		Location.setText("问题发生地点：" + data.getADDRESS());
		Plan.setText("问题解决方案：" + data.getDEALPLAN());
		if (data.getDEALDESC() == null) {
			Result.setText("问题解决情况：");
		} else {
			Result.setText("问题解决情况：" + data.getDEALDESC());
		}
		line.setText("线路：" + data.getLINELOOP());
		pile.setText("桩号：" + data.getMARKERNAME());
		offset.setText("偏移量(m)：" + data.getOFF());
		occurtime.setText("发生时间：" + data.getOCCURRENCETIME());
		// user.setText("巡检人员："+data.getUserid());
		uploadTime.setText("上报时间：" + data.getREPORTDATE());
		problemdes.setText("问题描述：" + data.getDESCRIPTION());

		isupload.setText("是否上报服务器：" + "已上报");
		isupload.setTextColor(Color.BLACK);

		// String photopath=data.getPhotopath();
		// String photodes=data.getPhotodes();
		// String[] temppath= photopath.split("#");
		// String[] tempdes=photodes.split("#");
		// for(int i=0;i<temppath.length;i++)
		// {
		// image.add(temppath[i]);
		// }
		// for(int j=0;j<tempdes.length;j++)
		// {
		// imagedes.add(tempdes[j]);
		// }
		imageview = (ImageView) findViewById(R.id.problem_detial_image);
		int i = getIntent().getIntExtra("image", 2);
		// if(i==2)
		// {
		// Bitmap bitmap =BitmapFactory.decodeFile("/sdcard/myBitmap.png");
		// imageview.setImageBitmap(bitmap);
		// }
		String path = data.getPATH();
		if(path.length()>1){
		new ImageLoader(getApplicationContext()).DisplayImage(path, imageview, false);
		}else{
			imageview.setVisibility(View.GONE);
		}
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		main = (Button) findViewById(R.id.main);
		main.setOnClickListener(this);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(Activity.RESULT_OK);
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case R.id.main:
			Tools.backMain(this);
			break;
		}
	}

}
