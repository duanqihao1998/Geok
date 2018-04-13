package com.geok.langfang.pipeline.dataacquisition;

import java.util.List;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.pipeline.R;
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

/**
 * 
 * @author wuchangming 防腐层检漏的历史记录的本地数据详情
 * 
 */
public class CorrosiveHistoryDetail extends Activity implements android.view.View.OnClickListener {

	CorrosiveHistoryData data;
	// GrallyImageView grallyImageView;

	TextView line;
	TextView pile;
	TextView offset;
	TextView repair_obj;
	TextView check_date;
	TextView clockposition;
	TextView soil;
	TextView damage_des;
	TextView area;
	TextView corrosion_des;
	TextView corrosion_area;
	TextView corrosion_num;
	TextView pitdepthmax;
	TextView pitdepthmin;
	TextView repair_situation;
	TextView repair_date;
	TextView damage_type;
	TextView repair_type;
	TextView pile_info;
	TextView remarks;
	TextView isupload;
	String guid = "";
	String[] check_time, repair_time;
	/*
	 * 标识是否上报
	 */
	int flag = 0;
	List<String> image, imagedes;
	ImageView imageview;
	Request request;
	public Handler MyHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Tools.stopProgressDialog(CorrosiveHistoryDetail.this);
			OperationDB db = new OperationDB(getApplicationContext());
			String str = msg.getData().getString("result");
			if (msg.getData().getInt("flag") == 1 && str.contains("OK")) {
				db.DBupdate(1, guid, Type.CORROSIVE);
				Toast.makeText(CorrosiveHistoryDetail.this, "上报成功", 500).show();
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			} else {
				db.DBupdate(0, guid, Type.CORROSIVE);
				Toast.makeText(CorrosiveHistoryDetail.this, "上报失败", 500).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.corrosivedetial);
		request = new Request(MyHandler);
		data = (CorrosiveHistoryData) getIntent().getSerializableExtra("values");
		// System.out.println(data.toString());

		// image=new ArrayList<String>();
		// imagedes=new ArrayList<String>();
		// grallyImageView=new GrallyImageView();

		line = (TextView) findViewById(R.id.corrosive_detial_line);
		pile = (TextView) findViewById(R.id.corrosive_detial_pile);
		offset = (TextView) findViewById(R.id.corrosive_detial_offset);
		repair_obj = (TextView) findViewById(R.id.corrosive_detial_repair_obj);
		check_date = (TextView) findViewById(R.id.corrosive_detial_check_date);
		clockposition = (TextView) findViewById(R.id.corrosive_detial_clockposition);
		soil = (TextView) findViewById(R.id.corrosive_detial_soil);
		damage_des = (TextView) findViewById(R.id.corrosive_detial_damage_des);
		area = (TextView) findViewById(R.id.corrosive_detial_area);
		corrosion_des = (TextView) findViewById(R.id.corrosive_detial_corrosion_des);
		corrosion_area = (TextView) findViewById(R.id.corrosive_detial_corrosion_area);
		corrosion_num = (TextView) findViewById(R.id.corrosive_detial_corrosion_num);
		pitdepthmax = (TextView) findViewById(R.id.corrosive_detial_pitdepthmax);
		pitdepthmin = (TextView) findViewById(R.id.corrosive_detial_pitdepthmin);
		repair_situation = (TextView) findViewById(R.id.corrosive_detial_repair_situation);
		repair_date = (TextView) findViewById(R.id.corrosive_detial_repair_date);
		damage_type = (TextView) findViewById(R.id.corrosive_detial_damage_type);
		repair_type = (TextView) findViewById(R.id.corrosive_detial_repair_type);
		pile_info = (TextView) findViewById(R.id.corrosive_detial_pile_info);
		remarks = (TextView) findViewById(R.id.corrosive_detial_remarks);
		isupload = (TextView) findViewById(R.id.corrosive_detial_isupload);

		line.setText("线路：" + data.getLine());
		pile.setText("桩号：" + data.getPile());
		offset.setText("偏移量(m)：" + data.getOffset());
		repair_obj.setText("修复对象：" + data.getRepair_obj());
		check_time = data.getCheck_date().split(" ");
		if(check_time.length>1){
			check_date.setText("捡漏日期：" + check_time[0]); 
		}else{
			check_date.setText("捡漏日期：" + data.getCheck_date());
		}
		
		clockposition.setText("环周位置：" + data.getClockposition());
		soil.setText("漏点土壤环境描述：" + data.getSoil());
		damage_des.setText("破损外观描述：" + data.getDamage_des());
		area.setText("破损面积(㎡)：" + data.getArea());
		corrosion_des.setText("金属腐蚀表面描述：" + data.getCorrosion_des());
		corrosion_area.setText("金属腐蚀面积(㎡)：" + data.getCorrosion_area());
		corrosion_num.setText("金属腐蚀个数：" + data.getCorrosion_num());
		pitdepthmax.setText("腐蚀深度起始：" + data.getPitdepthmax());
		pitdepthmin.setText("腐蚀深度终止：" + data.getPitdepthmin());
		repair_situation.setText("防腐层修补处理情况：" + data.getRepair_situation());
		repair_date.setText("补漏日期：" + data.getRepair_date());
		damage_type.setText("破损类型：" + data.getDamage_type());
		repair_type.setText("修复类型：" + data.getRepair_type());
		pile_info.setText("管道修复信息：" + data.getPile_info());
		remarks.setText("备注：" + data.getRemarks());

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
		// imageview=(ImageView)findViewById(R.id.corrosive_detial_image);
		// imageview.setImageBitmap(grallyImageView.getBitmapFromFile(image.get(0),
		// 800, 600));
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
				if (Tools.isNetworkAvailable(this, true)) {
					Tools.startProgressDialog(CorrosiveHistoryDetail.this, "上报中，请稍后...");
					request.ntisepticRequest(data.getUserid(), data.getUserid(), data.getPileid(),
							data.getOffset(), data.getRepair_obj(), data.getCheck_date(),
							data.getClockposition(), data.getSoil(), data.getDamage_des(),
							data.getArea(), data.getCorrosion_des(), data.getCorrosion_area(),
							data.getCorrosion_num(), data.getPitdepthmax(), data.getPitdepthmin(),
							data.getRepair_situation(), data.getRepair_date(),
							data.getDamage_type(), data.getRepair_type(), data.getPile_info(),
							data.getRemarks(), "");
				}
			} else {
				Toast.makeText(getApplicationContext(), "数据已上报", 1000).show();
			}

			break;

		case 2:// 修改

			if (flag !=1) {
				Intent intent = new Intent();
				intent.setClass(CorrosiveHistoryDetail.this, Corrosive.class);
				intent.putExtra("values", data);
				intent.putExtra("flag", "upgrate");
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

			} else {
				Toast.makeText(getApplicationContext(), "数据已上报,不能修改", 1000).show();
			}
			break;

		case 3:// 删除

			AlertDialog.Builder builder = new Builder(CorrosiveHistoryDetail.this);
			builder.setTitle("提示");
			builder.setMessage("是否删除本次记录");
			builder.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					OperationDB op = new OperationDB(getApplicationContext());
					ContentValues value = new ContentValues();
					value.put("guid", guid);
					op.DBdelete(value, Type.CORROSIVE);
					CorrosiveHistoryDetail.this.finish();
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
			// Intent intent = new Intent();
			// intent.setClass(CorrosiveHistoryDetailQuery.this,
			// CorrosiveHistory.class);
			// startActivity(intent);
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;

		case 31:
			// Intent intent2 = new Intent();
			// intent2.setClass(CorrosiveHistoryDetailQuery.this,
			// MainView.class);
			// startActivity(intent2);
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
