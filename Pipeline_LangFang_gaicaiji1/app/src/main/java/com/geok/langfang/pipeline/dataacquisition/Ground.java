package com.geok.langfang.pipeline.dataacquisition;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import com.geok.langfang.DB.Type;
import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.pipeline.ApplicationApp;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.problem.Problem;
import com.geok.langfang.pipeline.search.LineInfo;
import com.geok.langfang.pipeline.toolcase.KeypointAcquisition;
import com.geok.langfang.tools.DialogActivity;
import com.geok.langfang.tools.DialogEditActivity;
import com.geok.langfang.pipeline.dataacquisition.Ground;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.MyImageButton;
import com.geok.langfang.tools.PanelListAdapter;
import com.geok.langfang.tools.SIMCardInfo;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.tools.TypeQuest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @author wuchangming Ground.java接地电阻类
 * 
 */
public class Ground extends Activity implements OnClickListener {
	ListView panelList; // 声明侧滑栏List对象
	TextView protect_test_time_text;
	Map<String, Object> map;
	PanelListAdapter panelListAdapter; // 声明侧滑栏的适配器对象
	SimpleAdapter simpleAdapter1, simpleAdapter2;
	// 定义侧滑栏的图片
	int[] image = { R.drawable.menu_problem_selector, R.drawable.sj_menu1_selector,
			R.drawable.sj_menu2_selector, R.drawable.sj_menu3_selector,
			R.drawable.sj_menu4_selector };
	// 定义侧滑栏图片标题
	String[] imageTitle = { "事项上报", "保护电位", "自然电位", "接地电阻", "防腐检漏", };
	// 声明界面中的文本框
	TextView text_year, text_halfyear, text_line, text_pile, text_test_date, text_set_value,
			text_test_value, text_conclusion, text_weather, text_temperature, text_cpgroundbed;

	private Calendar c = null;
	Boolean isSave = false;
	Boolean update_or_save = true;
	private Request request;
	OperationDB operationDB;
	ApplicationApp app;
	int position = -1;
	private EditText et = null;
	ContentValues values = new ContentValues(); // 声明一个Contentvalues对象用于存储数据
	MyImageButton ground_new, ground_save, ground_upload, ground_previous, ground_next;
	RelativeLayout test_date, set_value, test_value, conclusion, weather, temperature, cpgroundbed, year, halfyear;
	String str_test_date, str_set_value, str_test_value, str_conclusion, str_weather,
			str_temperature, str_cpgroundbedeventid, str_year, str_halfyear;
	SIMCardInfo simCardInfo;
	// 定义并获取用户id
	String userid;
	// 随机生成码，唯一标识上报信息
	String guid = java.util.UUID.randomUUID().toString();
	Button back, main;
	String groundbed; // 地床编号
	GroundHistoryData Ground_data;
	private static CustomProgressDialog progressDialog = null;
	MyApplication myApplication;
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String currenttime = sDateFormat.format(new java.util.Date());
	String[] currenttime2 = currenttime.split(" ");
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			stopProgressDialog();
			switch (msg.arg1) {
			case 2:
				String str1 = msg.getData().getString("result");
				if (str1.equals("OK|TRUE")) {
					finish();
				} else {
					finish();
				}
				break;
			case 9:
				String str2 = msg.getData().getString("result");
				if (msg.getData().getInt("flag") == 1 && str2.contains("OK")) {
					save(1, false);
					Toast.makeText(getApplicationContext(), "上报成功", 1000).show();
				} else {
					save(0, false);
					Toast.makeText(getApplicationContext(), "上报失败，数据已保存", 1000).show();
				}
				break;
			// case 31:
			// // String str=msg.getData().getString("result");
			// // if(str.equals("-1")){
			// // // if(msg.getData().getInt("flag")==1){
			// // Toast.makeText(Ground.this, groundbed, 1000).show();
			// // }
			// // else{
			// // groundbed=str;
			// // getSharedPreferences("sync",
			// // MODE_PRIVATE).edit().putString("groundbed",
			// // groundbed).commit();
			// // // Json.getCpgroundbedBean(str);
			// // }
			// String str = msg.getData().getString("result");
			// // if(str.equals("-1")){
			// if (msg.getData().getInt("flag") == 1) {
			// groundbed = str;
			// getSharedPreferences("sync", MODE_PRIVATE).edit()
			// .putString("groundbed", groundbed).commit();
			// // Json.getCpgroundbedBean(str);
			//
			// } else {
			// Toast.makeText(Ground.this, groundbed, 1000).show();
			// }
			// break;
			}
		}
	};

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
		}
		progressDialog.show();
	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ground);
		myApplication = new MyApplication(this);
		userid = myApplication.userid;
		c = Calendar.getInstance();
		operationDB = new OperationDB(this);
		app = (ApplicationApp) getApplicationContext();
		simCardInfo = new SIMCardInfo(this);
		request = new Request(handler);
		// // 地床编号请求，获取地床编号
		// if (Tools.isNetworkAvailable(this, true)) {
		// request.CpgroundbedQuery(userid);
		// }
		// request.CpgroundbedQuery("f97e17e0-adc1-11e2-912a-002219d25412");
		Ground_data = (GroundHistoryData) getIntent().getSerializableExtra("values");
		init();
		if (Ground_data != null) {
			String upgrate = getIntent().getStringExtra("flag");
			if ("upgrate".equals(upgrate)) {
				guid = Ground_data.getGuid();
				String[] check_time= Ground_data.getTestdate().split(" ");
				text_test_date.setText(check_time[0]);
				text_set_value.setText(Ground_data.getSetvalue());
				text_test_value.setText(Ground_data.getTestvalue());
//				text_temperature.setText(Ground_data.getTemperature());
				text_cpgroundbed.setText(Ground_data.getCpgroundbedeventid());
//				text_weather.setText(Ground_data.getWeather());
				text_conclusion.setText(Ground_data.getConclusion());
				text_year.setText(Ground_data.getYear());
				text_halfyear.setText(Ground_data.getHalfyear());

			}
		}
		initPaneList();

	}

	/**
	 * 导航栏方法
	 */
	private void initPaneList() {
		panelList = (ListView) findViewById(R.id.protect_panel_list);
		panelListAdapter = new PanelListAdapter(this, image, imageTitle, 0);
		panelList.setAdapter(panelListAdapter);
		panelList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				switch (arg2) {
				case 0:
					intent.setClass(Ground.this, Problem.class);
					break;
				case 1:
					intent.setClass(Ground.this, Protect.class);
					break;
				case 2:
					intent.setClass(Ground.this, Natural.class);
					break;
				case 3:
					intent.setClass(Ground.this, Ground.class);
					break;
				case 4:
					intent.setClass(Ground.this, Corrosive.class);
					break;
				}
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
	}

	// 保存
	private void tempsave() {

		// if(!isSave)
		// {
		if (update_or_save)// 保存
		{
			values.put("guid", guid);

			values.put("userid", userid);

			if ((text_set_value != null)) {
				if (text_set_value.equals("未填写")) {
					values.put("set_value", "未填写");
				} else {
					values.put("set_value", text_set_value.getText().toString());
				}
			} else {
				values.put("set_value", 0);
			}

			if ((text_test_value != null)) {
				if (text_test_value.equals("未填写")) {
					values.put("test_value", "未填写");
				} else {
					values.put("test_value", text_test_value.getText().toString());
				}
			} else {
				values.put("test_value", 0);
			}

//			if ((text_weather != null)) {
//				if (text_weather.equals("未填写")) {
//					values.put("weather", "未填写");
//				} else {
//					values.put("weather", text_weather.getText().toString());
//				}
//			} else {
//				values.put("weather", 0);
//			}

			if ((text_test_date != null)) {
				if (text_test_date.equals("未填写")) {
					values.put("test_date", "未填写");
				} else {
					values.put("test_date", text_test_date.getText().toString() + " " + currenttime2[1]);
				}
			} else {
				values.put("test_date", 0);
			}

			if ((text_conclusion != null)) {
				if (text_conclusion.equals("未填写")) {
					values.put("conclusion", "未填写");
				} else {
					values.put("conclusion", text_conclusion.getText().toString());
				}
			} else {
				values.put("conclusion", 0);
			}

//			if ((text_temperature != null)) {
//				if (text_temperature.equals("未填写")) {
//					values.put("temperature", "未填写");
//				} else {
//					values.put("temperature", text_temperature.getText().toString());
//				}
//			} else {
//				values.put("temperature", 0);
//			}

			if ((text_cpgroundbed != null)) {
				if (text_cpgroundbed.equals("未填写")) {
					values.put("cpgroundbedeventid", "未填写");
				} else {
					values.put("cpgroundbedeventid", text_cpgroundbed.getText().toString());
				}
			} else {
				values.put("cpgroundbedeventid", 0);
			}
			
			if ((text_year != null)) {
				if (text_year.equals("未填写")) {
					values.put("year", "未填写");
				} else {
					values.put("year", text_year.getText().toString());
				}
			} else {
				values.put("year", 0);
			}
			if ((text_halfyear != null)) {
				if (text_halfyear.equals("未填写")) {
					values.put("halfyear", "未填写");
				} else {
					values.put("halfyear", text_halfyear.getText().toString());
				}
			} else {
				values.put("halfyear", 0);
			}

		}
		isSave = true;
		// 赋予中间变量，以便上报
		str_set_value = String.valueOf(values.get("set_value"));
		str_test_value = String.valueOf(values.get("test_value"));
//		str_weather = String.valueOf(values.get("weather"));
		str_test_date = String.valueOf(values.get("test_date"));
		str_conclusion = String.valueOf(values.get("conclusion"));
//		str_temperature = String.valueOf(values.get("temperature"));
		str_cpgroundbedeventid = String.valueOf(values.get("cpgroundbedeventid"));
		str_year = String.valueOf(values.get("year"));
		str_halfyear = String.valueOf(values.get("halfyear"));
	}

	private void save(int flag, Boolean isToast) {
		values.put("isupload", flag);
		// 插入或更新数据
		operationDB.InsertOrUpdate(values, Type.GROUND_RESISTANCE);
		if (isToast) {
			Toast.makeText(Ground.this, "保存成功", 1000).show();
		}
		fresh();
	}

	private void fresh() {
		guid = java.util.UUID.randomUUID().toString();
	}

	// 清空
	private void empty() {
		guid = java.util.UUID.randomUUID().toString();
		if (text_year != null) {
			text_year.setText("选择年份");
			text_year.setTextColor(Color.BLACK);
			}
			
			if (text_halfyear != null) {
				text_halfyear.setText("选择半年");
				text_halfyear.setTextColor(Color.BLACK);
			}
		
		if (text_set_value != null) {
			text_set_value.setText("填写规定值");
			text_set_value.setTextColor(Color.BLACK);
		}

		if (text_test_value != null) {
			text_test_value.setText("填写测试值");
			text_test_value.setTextColor(Color.BLACK);
		}

//		if (text_weather != null) {
//			text_weather.setText("填写天气");
//			text_weather.setTextColor(Color.BLACK);
//		}

		if (text_test_date != null) {
			text_test_date.setText("填写测试时间");
			text_test_date.setTextColor(Color.BLACK);
		}

		if (text_conclusion != null) {
			text_conclusion.setText("填写结论");
			text_conclusion.setTextColor(Color.BLACK);
		}

//		if (text_temperature != null) {
//			text_temperature.setText("填写温度");
//			text_temperature.setTextColor(Color.BLACK);
//		}

		if (text_cpgroundbed != null) {
			text_cpgroundbed.setText("选择地床编号");
			text_cpgroundbed.setTextColor(Color.BLACK);
		}

		Toast.makeText(Ground.this, "重置完成", 1000).show();
	}

	private void init() {
		// TODO Auto-generated method stub
		ground_new = (MyImageButton) findViewById(R.id.bnew_ground);
		ground_save = (MyImageButton) findViewById(R.id.bsave_ground);
		ground_upload = (MyImageButton) findViewById(R.id.bupload_ground);
		ground_new.setTag(1);
		ground_save.setTag(2);
		ground_upload.setTag(3);
		ground_new.setOnClickListener(this);
		ground_save.setOnClickListener(this);
		ground_upload.setOnClickListener(this);
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		back.setTag(30);
		main.setTag(31);
		back.setOnClickListener(this);
		main.setOnClickListener(this);

		test_date = (RelativeLayout) findViewById(R.id.ground_test_time);
		set_value = (RelativeLayout) findViewById(R.id.ground_set_value);
		test_value = (RelativeLayout) findViewById(R.id.ground_test_value);
		conclusion = (RelativeLayout) findViewById(R.id.ground_conclusion);
//		weather = (RelativeLayout) findViewById(R.id.ground_weather);
//		temperature = (RelativeLayout) findViewById(R.id.ground_temperature);
		cpgroundbed = (RelativeLayout) findViewById(R.id.ground_cpgroundbed);
		year = (RelativeLayout) findViewById(R.id.ground_year);
		halfyear = (RelativeLayout) findViewById(R.id.ground_halfyear);

		text_test_date = (TextView) findViewById(R.id.ground_test_time_text);
		text_set_value = (TextView) findViewById(R.id.text_set_value);
		text_test_value = (TextView) findViewById(R.id.text_test_value);
		text_conclusion = (TextView) findViewById(R.id.text_conclusion);
//		text_weather = (TextView) findViewById(R.id.text_weather);
//		text_temperature = (TextView) findViewById(R.id.text_temperature);
		text_cpgroundbed = (TextView) findViewById(R.id.text_cpgroundbed);
		text_year = (TextView) findViewById(R.id.text_year);
		text_halfyear = (TextView) findViewById(R.id.text_halfyear);

		test_date.setTag(6);
		set_value.setTag(7);
		test_value.setTag(8);
		conclusion.setTag(9);
//		weather.setTag(10);
//		temperature.setTag(11);
		cpgroundbed.setTag(12);
		year.setTag(4);
		halfyear.setTag(5);

		test_date.setOnClickListener(this);
		set_value.setOnClickListener(this);
		test_value.setOnClickListener(this);
		conclusion.setOnClickListener(this);
		year.setOnClickListener(this);
		halfyear.setOnClickListener(this);
		cpgroundbed.setOnClickListener(this);
		
		if(currenttime.length() == 19){
			text_test_date.setText(currenttime.substring(0, 10));
			}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = (Integer) v.getTag();
		Intent intent = new Intent();
		// 跳转到弹出对话框编辑器类
		intent.setClass(Ground.this, DialogEditActivity.class);
		switch (tag) {
		case 30:
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case 31:
			Tools.backMain(this);
			break;
		case 1:// 重置
			if (!isSave) {
				AlertDialog.Builder builder = new Builder(Ground.this);
				builder.setTitle("提示");
				builder.setMessage("是否保存信息?");
				builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						tempsave();
						if (str_test_date.equals("填写测试时间") || str_set_value.equals("填写规定值")
								|| str_set_value.equals("未填写") || str_test_value.equals("填写测试值")
								|| str_test_value.equals("未填写") || str_conclusion.equals("填写结论")
								|| str_conclusion.equals("未填写") 
//								|| str_weather.equals("填写天气")
//								|| str_weather.equals("未填写") || str_temperature.equals("填写温度")
//								|| str_temperature.equals("未填写")
								|| str_year.equals("选择年份") || str_halfyear.equals("选择半年")
								|| str_cpgroundbedeventid.equals("选择地床编号")) {
							Toast.makeText(Ground.this, "带*的为必填项，请填写完再试！", 300).show();
						} else {
							save(0, true);
							empty();
						}
					}
				}).setNegativeButton("否", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						empty();
					}
				}).show();
			}

			isSave = false;

			break;
		case 2:// 保存
			tempsave();
			if (str_test_date.equals("填写测试时间") || str_set_value.equals("填写规定值")
					|| str_set_value.equals("未填写") || str_test_value.equals("填写测试值")
					|| str_test_value.equals("未填写") || str_conclusion.equals("填写结论")
					|| str_year.equals("选择年份") || str_halfyear.equals("选择半年")
					|| str_conclusion.equals("未填写") || str_cpgroundbedeventid.equals("选择地床编号")) {
				Toast.makeText(Ground.this, "带*的为必填项，请填写完再试！", 300).show();
			} else {
				save(0, true);
			}
			break;
		case 3:// 上报
			tempsave();
			if (str_test_date.equals("填写测试时间") || str_set_value.equals("填写规定值")
					|| str_set_value.equals("未填写") || str_test_value.equals("填写测试值")
					|| str_test_value.equals("未填写") || str_conclusion.equals("填写结论")
					|| str_conclusion.equals("未填写") || str_year.equals("选择年份") || str_halfyear.equals("选择半年")
					|| str_cpgroundbedeventid.equals("选择地床编号")) {
				Toast.makeText(Ground.this, "带*的为必填项，请填写完再试！", 300).show();
			} else {
				if (Tools.isNetworkAvailable(this, false)) {
					startProgressDialog();
					if(str_halfyear.equals("上半年")){
						str_halfyear = "01";
					}else{
						str_halfyear = "02";
					}
					request.groundingResistanceRequest(str_cpgroundbedeventid, userid,
							str_test_date, str_set_value, str_test_value, str_conclusion,
							str_year, str_halfyear, "");
				} else {
					Toast.makeText(Ground.this, "无网络连接，上报失败", 500).show();
					save(0, true);
				}
			}
			break;
		case 4:
			Intent year = new Intent();
			year.setClass(Ground.this, DialogActivity.class);
			year.putExtra("flag", TypeQuest.GROUND_YEAR);
			startActivityForResult(year, TypeQuest.GROUND_YEAR);
			break;
		case 5:
			Intent halfyear = new Intent();
			halfyear.setClass(Ground.this, DialogActivity.class);
			halfyear.putExtra("flag", TypeQuest.GROUND_HALFYEAR);
			startActivityForResult(halfyear, TypeQuest.GROUND_HALFYEAR);
			break;
			
		case 6:
			Tools.setDateDialog(this, c, text_test_date);
			break;

		case 7:
			intent.putExtra("flag", TypeQuest.GROUND_SETVALUE);
			intent.putExtra("Tittle", "规定值(R)");
			if (!"填写规定值".equals(text_set_value.getText().toString())) {
				intent.putExtra("text", text_set_value.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.GROUND_SETVALUE);
			break;

		case 8:
			intent.putExtra("flag", TypeQuest.GROUND_TESTVALUE);
			intent.putExtra("Tittle", "测试值(R)");
			if (!"填写测试值".equals(text_test_value.getText().toString())) {
				intent.putExtra("text", text_test_value.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.GROUND_TESTVALUE);
			break;
		case 9:
			intent.putExtra("flag", TypeQuest.GROUND_CONCLUSION);
			intent.putExtra("Tittle", "结论");
			if (!"填写结论".equals(text_conclusion.getText().toString())) {
				intent.putExtra("text", text_conclusion.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.GROUND_CONCLUSION);
			break;

//		case 10:
//			intent.putExtra("flag", TypeQuest.GROUND_WEATHER);
//			intent.putExtra("Tittle", "天气");
//			if (!"填写天气".equals(text_weather.getText().toString())) {
//				intent.putExtra("text", text_weather.getText().toString());
//			}
//			startActivityForResult(intent, TypeQuest.GROUND_WEATHER);
//			break;
//		case 11:
//			intent.putExtra("flag", TypeQuest.GROUND_TEMPERATURE);
//			intent.putExtra("Tittle", "温度(℃)");
//			if (!"填写温度".equals(text_temperature.getText().toString())) {
//				intent.putExtra("text", text_temperature.getText().toString());
//			}
//			startActivityForResult(intent, TypeQuest.GROUND_TEMPERATURE);
//			break;
		case 12:
			Intent intent2 = new Intent();
			intent2.setClass(Ground.this, DialogActivity.class);
			intent2.putExtra("flag", TypeQuest.GROUND_CPGROUNDBEDEVENTID);
			intent2.putExtra("Tittle", "地床编号");
			startActivityForResult(intent2, TypeQuest.GROUND_CPGROUNDBEDEVENTID);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		menu.add(0, 1, 1, "退出").setIcon(R.drawable.exit);
		menu.add(0, 2, 2, "注销").setIcon(R.drawable.cancel);
		menu.add(0, 3, 3, "信息查询").setIcon(R.drawable.search);
		menu.add(0, 4, 4, "GPS状态").setIcon(R.drawable.gps);
		menu.add(0, 5, 5, "帮助").setIcon(R.drawable.help);
		menu.add(0, 6, 6, "历史记录").setIcon(R.drawable.history);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 菜单键方法
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {

		case 1:
			Tools.exit(this);
			break;
		case 2:
			Tools.cancel(this);
			break;
		case 3:
			Intent info = new Intent();
			info.setClass(this, LineInfo.class);
			startActivity(info);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case 4:
			Tools.gps(this);
			break;
		case 5:
			Tools.help(this);
			break;
		case 6:
			Intent intent = new Intent();
			intent.setClass(Ground.this, GroundHistory.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * 返回值
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		//
		case TypeQuest.GROUND_SETVALUE:
			if (resultCode == Activity.RESULT_OK) {

				if ("".equals(data.getStringExtra("set_value"))) {
					text_set_value.setTextColor(Color.RED);
					text_set_value.setText("未填写");

				} else {
					text_set_value.setTextColor(Color.BLACK);
					text_set_value.setText(data.getStringExtra("set_value"));
				}
			}
			break;
		case TypeQuest.GROUND_TESTVALUE:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("test_value"))) {
					text_test_value.setTextColor(Color.RED);
					text_test_value.setText("未填写");

				} else {
					text_test_value.setTextColor(Color.BLACK);
					text_test_value.setText(data.getStringExtra("test_value"));
				}
			}
			break;
//		case TypeQuest.GROUND_WEATHER:
//			if (resultCode == Activity.RESULT_OK) {
//				if ("".equals(data.getStringExtra("weather"))) {
//					text_weather.setTextColor(Color.RED);
//					text_weather.setText("未填写");
//
//				} else {
//					text_weather.setTextColor(Color.BLACK);
//					text_weather.setText(data.getStringExtra("weather"));
//				}
//			}
//			break;
		case TypeQuest.GROUND_TESTDATE:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("test_date"))) {
					text_test_date.setTextColor(Color.RED);
					text_test_date.setText("未填写");

				} else {
					text_test_date.setText((CharSequence) et);
				}
			}
			break;

		case TypeQuest.GROUND_CONCLUSION:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("conclusion"))) {
					text_conclusion.setTextColor(Color.RED);
					text_conclusion.setText("未填写");

				} else {
					text_conclusion.setTextColor(Color.BLACK);
					text_conclusion.setText(data.getStringExtra("conclusion"));
				}
			}
			break;
//		case TypeQuest.GROUND_TEMPERATURE:
//			if (resultCode == Activity.RESULT_OK) {
//				if ("".equals(data.getStringExtra("temperature"))) {
//					text_temperature.setTextColor(Color.RED);
//					text_temperature.setText("未填写");
//
//				} else {
//					text_temperature.setTextColor(Color.BLACK);
//					text_temperature.setText(data.getStringExtra("temperature"));
//				}
//			}
//			break;
		case TypeQuest.GROUND_CPGROUNDBEDEVENTID:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("cpgroundbedeventid"))) {
					text_cpgroundbed.setTextColor(Color.RED);
					text_cpgroundbed.setText("未填写");

				} else {
					text_cpgroundbed.setTextColor(Color.BLACK);
					text_cpgroundbed.setText(data.getStringExtra("cpgroundbedeventid"));
				}
			}
			break;
		case TypeQuest.GROUND_YEAR:
			if (resultCode == Activity.RESULT_OK) {
				text_year.setText(data.getStringExtra("year"));
			}
			break;
			
		case TypeQuest.GROUND_HALFYEAR:
			if (resultCode == Activity.RESULT_OK) {
				text_halfyear.setText(data.getStringExtra("halfyear"));
			}
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
