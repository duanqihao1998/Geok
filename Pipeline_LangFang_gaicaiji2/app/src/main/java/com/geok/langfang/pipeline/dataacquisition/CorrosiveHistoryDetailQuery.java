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

import com.geok.langfang.jsonbean.HistoryDataCorrosiveBean;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.tools.Tools;

import java.util.List;

/**
 * 
 * 防腐层检漏的历史记录的查询服务器上的数据详情
 * 
 */
public class CorrosiveHistoryDetailQuery extends Activity implements
		View.OnClickListener {

	CorrosiveHistoryData data;
	HistoryDataCorrosiveBean bean;
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
		setContentView(R.layout.corrosivedetial);
		// data=(CorrosiveHistoryData)
		// getIntent().getSerializableExtra("values");
		bean = (HistoryDataCorrosiveBean) getIntent().getSerializableExtra("values");
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

		line.setText("线路：" + bean.getLINELOOP());
		pile.setText("桩号：" + bean.getMARKERNAME());
		offset.setText("偏移量(m)：" + bean.getOFF());
		repair_obj.setText("修复对象：" + bean.getREPAIRTARGET());
		check_date.setText("捡漏日期：" + bean.getLEAKHUNTINGDATE());
		clockposition.setText("环周位置：" + bean.getCLOCKPOSITION());
		soil.setText("漏点土壤环境描述：" + bean.getSOIL());
		damage_des.setText("破损外观描述：" + bean.getCOATINGFACE());
		area.setText("破损面积(㎡)：" + bean.getCOATINGAREA());
		corrosion_des.setText("金属腐蚀表面描述：" + bean.getAPPEARENCEDESC());
		corrosion_area.setText("金属腐蚀面积(㎡)：" + bean.getPITAREA());
		corrosion_num.setText("金属腐蚀个数：" + bean.getPITAMOUNT());
		pitdepthmax.setText("腐蚀深度起始：" + bean.getPITDEPTHMAX());
		pitdepthmin.setText("腐蚀深度终止：" + bean.getPITDEPTHMIN());
		repair_situation.setText("防腐层修补处理情况：" + bean.getCOATINGREPAIR());
		repair_date.setText("补漏日期：" + bean.getREPAIRDATE());
		damage_type.setText("破损类型：" + bean.getDAMAGETYPE());
		repair_type.setText("修复类型：" + bean.getREPAIRTYPE());
		pile_info.setText("管道修复信息：" + bean.getREPAIRINFO());
		remarks.setText("备注：" + bean.getREMARK());
		isupload.setText("是否上报服务器：" + "已上报");
		isupload.setTextColor(Color.BLACK);

		// flag=data.getIsupload();
		// if(data.getIsupload()==1)
		// {
		// isupload.setText("是否上报服务器："+"已上报");
		// isupload.setTextColor(Color.BLACK);
		// }else
		// {
		// isupload.setText("是否上报服务器："+"未上报");
		// isupload.setTextColor(Color.RED);
		// }
		// guid=data.getGuid();

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

			if (flag == 1) {

			} else {
				Toast.makeText(getApplicationContext(), "数据已上报", 1000).show();
			}

			break;

		case 2:// 修改

			if (flag == 1) {
				Intent intent = new Intent();
				intent.setClass(CorrosiveHistoryDetailQuery.this, Corrosive.class);
				intent.putExtra("values", data);
				startActivity(intent);

			} else {
				Toast.makeText(getApplicationContext(), "数据已上报,不能修改", 1000).show();
			}
			break;

		case 3:// 删除

			Builder builder = new Builder(CorrosiveHistoryDetailQuery.this);
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
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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
