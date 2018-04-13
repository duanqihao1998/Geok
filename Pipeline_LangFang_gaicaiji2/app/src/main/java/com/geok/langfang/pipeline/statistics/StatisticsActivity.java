package com.geok.langfang.pipeline.statistics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.shuju.BumenBean;
import com.geok.langfang.shuju.SetMessage;
import com.geok.langfang.util.OkHttp3Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 段淇皓
 * 统计页面部门
 * Created by ydb on 2018/4/4.
 */
public class StatisticsActivity extends Activity implements OnClickListener {

	private ListView listview_statistics;
	private List<BumenBean> bumemList=new ArrayList<>();
	private Button back;
	private String path = "http://" + MyApplication.ip + ":" + MyApplication.port
			+ "/pmi/servlet/PdaServlet";

	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String uptime = sDateFormat.format(new java.util.Date());
	Calendar c = null;

	@SuppressLint("HandlerLeak")
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.arg1) {
				case 1:
					String unitsync = msg.getData().getString("result");
					Log.i("diyige",unitsync);
					try {
						JSONArray jsonArray=new JSONArray(unitsync);
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArray.get(i);
							String allpointcount = jsonObject.getString("ALLPOINTCOUNT");
							String dopointcount = jsonObject.getString("DOPOINTCOUNT");
							String eventid = jsonObject.getString("EVENTID");
							String notaskusercount = jsonObject.getString("NOTASKUSERCOUNT");
							String taskusercount = jsonObject.getString("TASKUSERCOUNT");
							String unitid = jsonObject.getString("UNITID");
							String unitname = jsonObject.getString("UNITNAME");
							String usercount = jsonObject.getString("USERCOUNT");

							BumenBean bumenBean=new BumenBean(allpointcount,dopointcount,eventid,notaskusercount,taskusercount,unitid,unitname,usercount);
							bumemList.add(bumenBean);
						}
						listViewAdapter = new ListViewAdapter1(bumemList,StatisticsActivity.this);
						listview_statistics.setAdapter(listViewAdapter);
						listview_statistics.setDividerHeight(0);
						listview_statistics.setOnItemClickListener(new AdapterView.OnItemClickListener(){

							@Override
							public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
								Intent intent=new Intent(StatisticsActivity.this,ItemGRActivity.class);
								intent.putExtra("unitID",bumemList.get(i).getEVENTID());
								String s = locus_date.getText().toString();
								intent.putExtra("data",s);
								startActivity(intent);
//					Toast.makeText(StatisticsActivity.this, bumemList.get(i).getEVENTID()+"点击了第"+i+"个"+"---"+bumemList.get(i).getDEPARTMENT(), Toast.LENGTH_SHORT).show();
							}
						});

					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
			}
		}
	};
	private TextView locus_date;
	private String date;
	private ListViewAdapter1 listViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics);
//		statistics   主界面xml布局
		isitView();

		getData(MyApplication.depterid);
		c = Calendar.getInstance();
		//选择日期
		if (uptime.length() == 19) {
			locus_date.setText(uptime.substring(0, 10));
		}
//		String path = "http://123.124.230.18:8080/pmi/servlet/PdaServlet";
//		Map<String,String> params=new HashMap<>();
//		params.put("reqType", "1126");
//		params.put("arg0", "d3262010-8a42-11df-8860-0026b9593a24");
//		MyApplication myApplication=new MyApplication(this);
//		String userid = myApplication.userid;
//		String depterid = myApplication.depterid;
//		Log.i("qingqiushuju",userid+"-------"+depterid);
//
//		OkHttp3Utils.doPost(path, params,new Callback() {
//			@Override
//			public void onFailure(Call call, IOException e) {
//
//			}
//
//			@Override
//			public void onResponse(Call call, Response response) throws IOException {
//				String string = response.body().string();
//				setMessage(26, string, 1);
//			}
//		});

		//添加适配器

	}
	public void getData(String depterid){
		date = locus_date.getText().toString();
		Map<String,String> params=new HashMap<>();
		params.put("reqType", "11002");
		params.put("arg0",depterid);
		params.put("arg1",date);
		params.put("arg2","");
		OkHttp3Utils.doPost(path, params, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String string = response.body().string();
				SetMessage.setMessage(1,string,1,handler);
			}
		});
	}
	public void isitView(){
		listview_statistics = findViewById(R.id.listview_statistics);
		locus_date = findViewById(R.id.locus_date);
		back = findViewById(R.id.back);

		locus_date.setOnClickListener(this);
		back.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.back:
				finish();//返回上一级
				break;
			case R.id.locus_date:
				new DatePickerDialog(StatisticsActivity.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						int n = monthOfYear + 1;
						String month = "";
						if (n > 9) {
							month = n + "";
						} else {
							month = "0" + n;
						}
						String day = "";
						if (dayOfMonth > 9) {
							day = dayOfMonth + "";
						} else {
							day = "0" + dayOfMonth;
						}
						locus_date.setText(year + "-" + month + "-" + day);
						getData(MyApplication.depterid);
						listViewAdapter.notifyDataSetChanged();
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
				break;
		}
	}
}
