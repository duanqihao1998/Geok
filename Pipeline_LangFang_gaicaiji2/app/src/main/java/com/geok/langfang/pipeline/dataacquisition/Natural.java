package com.geok.langfang.pipeline.dataacquisition;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.pipeline.ApplicationApp;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.problem.Problem;
import com.geok.langfang.pipeline.search.LineInfo;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.DialogActivity;
import com.geok.langfang.tools.DialogEditActivity;
import com.geok.langfang.tools.MyImageButton;
import com.geok.langfang.tools.Panel;
import com.geok.langfang.tools.PanelListAdapter;
import com.geok.langfang.tools.Pipe;
import com.geok.langfang.tools.SIMCardInfo;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.tools.TreeView;
import com.geok.langfang.tools.TypeQuest;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 * @author wuchangming 自然电位测试类
 * 
 */
public class Natural extends Activity implements OnClickListener {
	ListView panelList;
	// Map<String, Object> map;
	PanelListAdapter panelListAdapter; // 声明侧滑栏的适配器对象
	SimpleAdapter simpleAdapter1, simpleAdapter2;
	// 定义侧滑栏的图片
	int[] image = { R.drawable.menu_problem_selector, R.drawable.sj_menu1_selector,
			R.drawable.sj_menu2_selector, R.drawable.sj_menu3_selector,
			R.drawable.sj_menu4_selector };
	// 定义侧滑栏图片标题
	String[] imageTitle = { "事项上报", "保护电位", "自然电位", "接地电阻", "防腐检漏", };
	MyImageButton natural_new, natural_save;
	Button natural_previous, natural_next, natural_upload;
	Panel panel;
	TextView text_year,text_month, text_line, text_pile, text_value, text_tester, text_test_time,
			text_remarks, text_voltage, text_temperature, text_weather;
	private EditText et = null;
	private Calendar c = null;
	OperationDB operationDB;
	int position = -1; // 选桩号的位置变量
	int po;
	int i = 1;
	Boolean isSave = false;
	Boolean update_or_save = true;
	// 声明一个Contentvalues对象用于存储数据
	ContentValues values = new ContentValues();
	private Request request;
	ApplicationApp app;
	// 声明界面中每一条字段的布局
	RelativeLayout test_time, year,month, line, pile, value, remarks, voltage, temperature, weather;
	String str_year, str_month, str_line, str_pile, str_temperature, str_weather, str_value,
			str_testtime, str_ground, str_voltage, str_remarks;
	SIMCardInfo simCardInfo;
	String lineId, markId;
	String userid; // 用户ID
	MyApplication myApplication;
	String guid = java.util.UUID.randomUUID().toString();
	Button back, main;
	NaturalHistoryData Natural_data; // 声明历史记录数据
	LinearLayout natural_remarks_layout;
	private static CustomProgressDialog progressDialog = null;
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String currenttime = sDateFormat.format(new java.util.Date());
	String[] currenttime2 = currenttime.split(" ");
	String linename= "";
	// int flag;
	Handler handler = new Handler() {

		@SuppressLint("WrongConstant")
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

			case 8:
				String str2 = msg.getData().getString("result");
				if (msg.getData().getInt("flag") == 1 && str2.contains("OK")) {
					save(1, false);
					Toast.makeText(getApplicationContext(), "上报成功", 1000).show();
				} else {
					save(0, false);
					Toast.makeText(getApplicationContext(), "上报失败，数据已保存", 1000).show();
				}
				break;
				
			case 41:
				String str = msg.getData().getString("result");
				int flag = msg.getData().getInt("flag");
				if (flag == 1) {
					if (!str.equals("-1")) {
						Bundle bundle = new Bundle();
						Intent intent = new Intent();
						intent.setClass(Natural.this, LineInfo.class);
						bundle.putString("lineinfo", str);
						bundle.putString("linename",linename);
						intent.putExtras(bundle);
						startActivity(intent);
						overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					} else {
						Toast.makeText(getApplicationContext(), "暂无当前管线的信息", 1000).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "管道信息查询失败，请重试！", 1000).show();
				}
				break;
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
		setContentView(R.layout.natural);

		myApplication = new MyApplication(this);
		userid = myApplication.userid;
		c = Calendar.getInstance();
		// 实例化操作数据库对象
		operationDB = new OperationDB(this);
		request = new Request(handler);
		simCardInfo = new SIMCardInfo(this);
		app = (ApplicationApp) getApplicationContext();
		initPaneList();
		// initList();
		// 获得自然电位数据
		Natural_data = (NaturalHistoryData) getIntent().getSerializableExtra("values");
		init();

		if (Natural_data != null) {
			String upgrate = getIntent().getStringExtra("flag");
			if ("upgrate".equals(upgrate)) {
				guid = Natural_data.getGuid();
				text_year.setText(Natural_data.getYear());
				text_line.setText(Natural_data.getLine());
				text_pile.setText(Natural_data.getPile());
				text_value.setText(Natural_data.getValue());
				String[] check_time= Natural_data.getTesttime().split(" ");
				text_test_time.setText(check_time[0]);
				text_voltage.setText(Natural_data.getVoltage());
				text_temperature.setText(Natural_data.getTemperature());
				text_weather.setText(Natural_data.getWeather());
				text_remarks.setText(Natural_data.getRemarks());
				lineId=Natural_data.getLineid();
				markId=Natural_data.getPileid();
				text_month.setText(Natural_data.getMonth());
				position = Tools.pileIndext(Natural.this, lineId, markId);
			}
		}
	}

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
					intent.setClass(Natural.this, Problem.class);
					break;
				case 1:
					intent.setClass(Natural.this, Protect.class);
					break;
				case 2:
					intent.setClass(Natural.this, Natural.class);
					break;
				case 3:
					intent.setClass(Natural.this, Ground.class);
					break;
				case 4:
					intent.setClass(Natural.this, Corrosive.class);
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
			values.put("lineid", lineId);
			values.put("pileid", markId);

			if ((text_year != null)) {
				if (text_year.equals("未填写")) {
					values.put("year", "未填写");
				} else {
					values.put("year", text_year.getText().toString());
				}
			} else {
				values.put("year", 0);
			}

			if ((text_line != null)) {
				if (text_line.equals("未填写")) {
					values.put("line", "未填写");
				} else {
					values.put("line", text_line.getText().toString());
				}
			} else {
				values.put("line", 0);
			}

			if ((text_pile != null)) {
				if (text_pile.equals("未填写")) {
					values.put("pile", "未填写");
				} else {
					values.put("pile", text_pile.getText().toString());
				}
			} else {
				values.put("pile", 0);
			}

			if ((text_temperature != null)) {
				if (text_temperature.equals("未填写")) {
					values.put("temperature", "未填写");
				} else {
					values.put("temperature", text_temperature.getText().toString());
				}
			} else {
				values.put("temperature", 0);
			}

			if ((text_weather != null)) {
				if (text_weather.equals("未填写")) {
					values.put("weather", "未填写");
				} else {
					values.put("weather", text_weather.getText().toString());
				}
			} else {
				values.put("weather", 0);
			}

			if ((text_value != null)) {
				if (text_value.equals("未填写")) {
					values.put("value", "未填写");
				} else {
					values.put("value", text_value.getText().toString());
				}
			} else {
				values.put("value", 0);
			}

			if ((text_test_time != null)) {
				if (text_test_time.equals("未填写")) {
					values.put("test_time", "未填写");
				} else {
					values.put("test_time", text_test_time.getText().toString()+ " " + currenttime2[1]);
				}
			} else {
				values.put("test_time", 0);
			}

			if ((text_voltage != null)) {
				if (text_voltage.equals("未填写")) {
					values.put("voltage", "未填写");
				} else {
					values.put("voltage", text_voltage.getText().toString());
				}
			} else {
				values.put("voltage", 0);
			}

			if ((text_month != null)) {
				if (text_month.equals("未填写")) {
					values.put("month", "未填写");
				} else {
					values.put("month", text_month.getText().toString());
				}
			} else {
				values.put("month", 0);
			}
			
			if ((text_remarks != null)) {
				if (text_remarks.equals("未填写")) {
					values.put("remarks", "未填写");
				} else {
					int size = text_remarks.getText().length();
					// values.put("remarks", text_remarks.getText().toString()
					// .substring(3, size));
					values.put("remarks", text_remarks.getText().toString().substring(0, size));
				}
			} else {
				values.put("remarks", 0);
			}

		}
		isSave = true;

		str_year = String.valueOf(values.get("year"));
		str_month = String.valueOf(values.get("month"));
		str_line = String.valueOf(values.get("line"));
		str_pile = String.valueOf(values.get("pile"));
		str_value = String.valueOf(values.get("value"));
		str_testtime = String.valueOf(values.get("test_time"));
		str_remarks = String.valueOf(values.get("remarks"));
		str_voltage = String.valueOf(values.get("voltage"));
		str_weather = String.valueOf(values.get("weather"));
		str_temperature = String.valueOf(values.get("temperature"));
	}

	@SuppressLint("WrongConstant")
	public void save(int flag, Boolean isToast) {
		Builder builder = new Builder(Natural.this);
		values.put("isupload", flag);
		operationDB.InsertOrUpdate(values, Type.NATURAL_POTENTIAL);
		if (isToast) {
			Toast.makeText(Natural.this, "保存成功", 1000).show();
		}
		fresh();
	}

	private void fresh() {
		guid = java.util.UUID.randomUUID().toString();
		if (text_remarks != null) {
			text_remarks.setText("");
		}
	}

	// 清空
	@SuppressLint("WrongConstant")
	private void empty() {
     	guid = java.util.UUID.randomUUID().toString();
		if (text_year != null) {
			text_year.setText("选择年份");
		}
		if (text_year != null) {
			text_year.setText("选择月份");
		}

		if (text_line != null) {
			text_line.setText("选择线路");
		}

		if (text_pile != null) {
			text_pile.setText("选择桩号");
		}

		if (text_temperature != null) {
			text_temperature.setText("填写温度");
			text_temperature.setTextColor(Color.BLACK);
		}

		if (text_weather != null) {
			text_weather.setText("填写天气");
			text_weather.setTextColor(Color.BLACK);
		}

		if (text_value != null) {
			text_value.setText("填写电位值");
			text_value.setTextColor(Color.BLACK);
		}

		if (text_test_time != null) {
			text_test_time.setText("填写测试时间");
			text_test_time.setTextColor(Color.BLACK);
		}

		if (text_voltage != null) {
			text_voltage.setText("填写电压");
			text_voltage.setTextColor(Color.BLACK);
		}

		if (text_remarks != null) {
			text_remarks.setText("");
			text_remarks.setTextColor(Color.BLACK);
		}

		Toast.makeText(Natural.this, "清空完成！", 500).show();
		System.out.println("重置成功--------");
	}

	private void init() {
		// TODO Auto-generated method stub
		natural_new = (MyImageButton) findViewById(R.id.bnew_natural);
		natural_save = (MyImageButton) findViewById(R.id.bsave_natural);
		natural_upload = (Button) findViewById(R.id.bupload_natural);
		natural_previous = (Button) findViewById(R.id.bprevious_natural);
		natural_next = (Button) findViewById(R.id.bnext_natural);
		natural_new.setTag(1);
		natural_save.setTag(2);
		natural_upload.setTag(3);
		natural_previous.setTag(4);
		natural_next.setTag(5);
		natural_new.setOnClickListener(this);
		natural_save.setOnClickListener(this);
		natural_upload.setOnClickListener(this);
		natural_previous.setOnClickListener(this);
		natural_next.setOnClickListener(this);
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		back.setTag(30);
		main.setTag(31);
		back.setOnClickListener(this);
		main.setOnClickListener(this);

		test_time = (RelativeLayout) findViewById(R.id.natural_test_time);
		text_test_time = (TextView) findViewById(R.id.natural_test_time_text);
		test_time.setTag(6);
		test_time.setOnClickListener(this);

		year = (RelativeLayout) findViewById(R.id.natural_year);
		month = (RelativeLayout) findViewById(R.id.natural_month);
		line = (RelativeLayout) findViewById(R.id.natural_line);
		pile = (RelativeLayout) findViewById(R.id.natural_pile);
		value = (RelativeLayout) findViewById(R.id.natural_value);
		voltage = (RelativeLayout) findViewById(R.id.natural_voltage);
		weather = (RelativeLayout) findViewById(R.id.natural_weather);
		temperature = (RelativeLayout) findViewById(R.id.natural_temperature);

		text_year = (TextView) findViewById(R.id.text_natural_year);
		text_month = (TextView) findViewById(R.id.text_natural_month);
		text_line = (TextView) findViewById(R.id.text_natural_line);
		text_pile = (TextView) findViewById(R.id.text_natural_pile);
		text_value = (TextView) findViewById(R.id.text_natural_value);
		text_voltage = (TextView) findViewById(R.id.text_natural_voltage);
		text_temperature = (TextView) findViewById(R.id.text_natural_temperature);
		text_weather = (TextView) findViewById(R.id.text_natural_weather);
		text_remarks = (TextView) findViewById(R.id.text_natural_remarks);
		natural_remarks_layout = (LinearLayout) findViewById(R.id.natural_remarks_layout);

		if(currenttime.length() == 19){
			text_test_time.setText(currenttime.substring(0, 10));
			}

		value.setTag(7);
		voltage.setTag(8);
		temperature.setTag(9);
		weather.setTag(10);
		text_remarks.setTag(11);
		year.setTag(12);
		line.setTag(13);
		pile.setTag(14);
		month.setTag(15);
//		natural_remarks_layout.setTag(15);

		value.setOnClickListener(this);
		voltage.setOnClickListener(this);
		temperature.setOnClickListener(this);
		weather.setOnClickListener(this);
		text_remarks.setOnClickListener(this);
		year.setOnClickListener(this);
		line.setOnClickListener(this);
		pile.setOnClickListener(this);
		month.setOnClickListener(this);
//		natural_remarks_layout.setOnClickListener(this);
	}

	@SuppressLint("WrongConstant")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = (Integer) v.getTag();
		Intent intent = new Intent();
		// 跳转到弹出对话框编辑器类
		intent.setClass(Natural.this, DialogEditActivity.class);

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
				Builder builder = new Builder(Natural.this);
				builder.setTitle("提示");
				builder.setMessage("是否保存信息?");
				builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

					@SuppressLint("WrongConstant")
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						tempsave();
						if (str_year.equals("选择年份") || str_year.equals("选择月份")||str_line.equals("选择线路")
								|| str_pile.equals("选择桩号") || str_testtime.equals("填写测试时间")
								|| str_value.equals("填写电位值") || str_value.equals("未填写")
								|| str_voltage.equals("填写电压") || str_voltage.equals("未填写")
								|| str_temperature.equals("填写温度") || str_temperature.equals("未填写")
								|| str_weather.equals("填写天气") || str_weather.equals("未填写")
								|| str_remarks.equals("备注：") || str_remarks.equals("未填写")) {
							Toast.makeText(Natural.this, "带*的为必填项，请填写完再试！", 300).show();
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
			if (str_year.equals("选择年份") || str_year.equals("选择月份")|| str_line.equals("选择线路") || str_pile.equals("选择桩号")
					|| str_testtime.equals("填写测试时间") || str_value.equals("填写电位值")
					|| str_value.equals("未填写") || str_voltage.equals("填写电压")
					|| str_voltage.equals("未填写") || str_temperature.equals("填写温度")
					|| str_temperature.equals("未填写") || str_weather.equals("填写天气")
					|| str_weather.equals("未填写") || str_remarks.equals("备注：")
					|| str_remarks.equals("未填写")) {
				Toast.makeText(Natural.this, "带*的为必填项，请填写完再试！", 300).show();
			} else {
				save(0, true);
			}
			break;
		case 3:// 上报
			tempsave();
			if (str_year.equals("选择年份") || str_year.equals("选择月份") ||str_line.equals("选择线路") || str_pile.equals("选择桩号")
					|| str_testtime.equals("填写测试时间") || str_value.equals("填写电位值")
					|| str_value.equals("未填写") || str_voltage.equals("填写电压")
					|| str_voltage.equals("未填写") || str_temperature.equals("填写温度")
					|| str_temperature.equals("未填写") || str_weather.equals("填写天气")
					|| str_weather.equals("未填写") || str_remarks.equals("备注：")
					|| str_remarks.equals("未填写")) {
				Toast.makeText(Natural.this, "带*的为必填项，请填写完再试！", 300).show();
			} else {
				if (Tools.isNetworkAvailable(this, false)) {
					startProgressDialog();
					request.naturalPotentialRequest(userid, str_year, str_month, lineId, markId, str_value,
							str_testtime, str_remarks, str_voltage, str_temperature, str_weather, "");
				} else {
					Toast.makeText(Natural.this, "无网络连接，上报失败！", 500).show();
					save(0, true);
				}

			}
			break;
		case 4:// 上一桩
			if (position == -1) {
				Toast.makeText(Natural.this, "您还未选择桩号", 500).show();
			} else if (position == 0)
				Toast.makeText(Natural.this, "您已处于第一桩", 500).show();
			else {
				text_pile.setText(Pipe.listpile.get(position - 1));
				position = position - 1;
				// System.out.println("上一桩了了"+ position);
			}
			break;
		case 5:// 下一桩
			if (position == -1) {
				Toast.makeText(Natural.this, "您还未选择桩号", 500).show();
			} else if ((position == (Pipe.listpile.size() - 1))) {
				Toast.makeText(Natural.this, "您处于最后一桩", 500).show();
			} else {
				text_pile.setText(Pipe.listpile.get(position + 1));
				System.out.println(i);
				position = position + 1;
			}
			break;
		case 6:
			Tools.setDateDialog(this, c, text_test_time);
			break;
		case 7:
			intent.putExtra("flag", TypeQuest.NATURAL_VALUE);
			intent.putExtra("Tittle", "电位值(V)");
			if (!"填写电位值".equals(text_value.getText().toString())) {
				intent.putExtra("text", text_value.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.NATURAL_VALUE);

			break;
		case 8:
			intent.putExtra("flag", TypeQuest.NATURAL_VOLTAGE);
			intent.putExtra("Tittle", "交流电干扰电压(V)");
			if (!"填写电压".equals(text_voltage.getText().toString())) {
				intent.putExtra("text", text_voltage.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.NATURAL_VOLTAGE);
			break;

		case 9:
			intent.putExtra("flag", TypeQuest.NATURAL_TEMPERATURE);
			intent.putExtra("Tittle", "温度(℃)");
			if (!"填写温度".equals(text_temperature.getText().toString())) {
				intent.putExtra("text", text_temperature.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.NATURAL_TEMPERATURE);
			break;

		case 10:
			intent.putExtra("flag", TypeQuest.NATURAL_WEATHER);
			intent.putExtra("Tittle", "天气");
			if (!"填写天气".equals(text_weather.getText().toString())) {
				intent.putExtra("text", text_weather.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.NATURAL_WEATHER);
			break;

		// case 11:
		// intent.putExtra("flag", TypeQuest.NATURAL_REMARKS);
		// intent.putExtra("Tittle", "备注");
		// if (!"填写备注".equals(text_remarks.getText().toString())) {
		// intent.putExtra("text", text_remarks.getText().toString());
		// }
		// startActivityForResult(intent, TypeQuest.NATURAL_REMARKS);
		// break;
		case 12:
			Intent intent2 = new Intent();
			intent2.setClass(Natural.this, DialogActivity.class);
			intent2.putExtra("flag", TypeQuest.NATURAL_YEAR);
			startActivityForResult(intent2, TypeQuest.NATURAL_YEAR);
			break;

		case 15:
			Intent intent3 = new Intent();
			intent3.setClass(Natural.this, DialogActivity.class);
			intent3.putExtra("flag", TypeQuest.NATURAL_MONTH);
			startActivityForResult(intent3, TypeQuest.NATURAL_MONTH);
			break;
		case 13:
			intent.setClass(Natural.this, TreeView.class);
			intent.putExtra("flag", TypeQuest.NATURAL_LINE);
			startActivityForResult(intent, TypeQuest.NATURAL_LINE);
			break;
		case 14:
			if (lineId == null) {
				Toast.makeText(getApplicationContext(), "请先选择线路", 1000).show();
			} else {
				intent.setClass(Natural.this, DialogActivity.class);
				intent.putExtra("flag", TypeQuest.NATURAL_PILE);
				intent.putExtra("lineid", lineId);
				if(!(text_pile.getText().toString().equals("选择桩号"))){
					intent.putExtra("markName", text_pile.getText().toString());
				}
				startActivityForResult(intent, TypeQuest.NATURAL_PILE);
			}
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

	@SuppressLint("WrongConstant")
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
			if(lineId != null){
				linename = text_line.getText().toString();
				if (Tools.isNetworkAvailable(this, true)) {
					request.LineInfoSearchRequest(myApplication.userid, lineId);
					startProgressDialog();
				}
			}
			else{
				Toast.makeText(Natural.this, "请选择线路后查看", 1000).show();
			}
			break;
		case 4:
			Tools.gps(this);
			break;
		case 5:
			Tools.help(this);
			break;
		case 6:
			Intent intent = new Intent();
			intent.setClass(Natural.this, NaturalHistory.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TypeQuest.NATURAL_YEAR:
			if (resultCode == Activity.RESULT_OK) {
				text_year.setText(data.getStringExtra("year"));
			}
			break;
			
		case TypeQuest.NATURAL_MONTH:
			if (resultCode == Activity.RESULT_OK) {
				text_month.setText(data.getStringExtra("month"));
			}
			break;

		case TypeQuest.NATURAL_LINE:
			if (resultCode == Activity.RESULT_OK) {
				text_line.setText(data.getStringExtra("line"));
				lineId = data.getStringExtra("lineId");
				text_pile.setText("选择桩号");
			}
			break;

		case TypeQuest.NATURAL_PILE:
			if (resultCode == Activity.RESULT_OK) {
				text_pile.setText(data.getStringExtra("pile"));
				position = data.getIntExtra("po", -1);
				markId = data.getStringExtra("markId");
			}
			break;
		case TypeQuest.NATURAL_TEMPERATURE:
			if (resultCode == Activity.RESULT_OK) {

				if ("".equals(data.getStringExtra("temperature"))) {
					text_temperature.setTextColor(Color.RED);
					text_temperature.setText("未填写");

				} else {
					text_temperature.setTextColor(Color.BLACK);
					text_temperature.setText(data.getStringExtra("temperature"));
				}
			}
			break;
		case TypeQuest.NATURAL_WEATHER:
			if (resultCode == Activity.RESULT_OK) {

				if ("".equals(data.getStringExtra("weather"))) {
					text_weather.setTextColor(Color.RED);
					text_weather.setText("未填写");

				} else {
					text_weather.setTextColor(Color.BLACK);
					text_weather.setText(data.getStringExtra("weather"));
				}
			}
			break;
		case TypeQuest.NATURAL_VALUE:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("value"))) {
					text_value.setTextColor(Color.RED);
					text_value.setText("未填写");

				} else {
					text_value.setTextColor(Color.BLACK);
					text_value.setText(data.getStringExtra("value"));
				}
			}
			break;
		case TypeQuest.NATURAL_TESTER:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("tester"))) {
					text_tester.setTextColor(Color.RED);
					text_tester.setText("未填写");

				} else {
					text_tester.setTextColor(Color.BLACK);
					text_tester.setText(data.getStringExtra("tester"));
				}
			}
			break;
		case TypeQuest.NATURAL_TESTTIME:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("test_time"))) {
					text_test_time.setTextColor(Color.RED);
					text_test_time.setText("未填写");

				} else {
					// text_test_time.setText(data.getStringExtra("test_time"));
					text_test_time.setText((CharSequence) et);
				}
			}
			break;

		case TypeQuest.NATURAL_VOLTAGE:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("voltage"))) {
					text_voltage.setTextColor(Color.RED);
					text_voltage.setText("未填写");

				} else {
					text_voltage.setTextColor(Color.BLACK);
					text_voltage.setText(data.getStringExtra("voltage"));
				}
			}
			break;
		case TypeQuest.NATURAL_REMARKS:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("remarks"))) {
					text_remarks.setTextColor(Color.RED);
					text_remarks.setText("备注：未填写");

				} else {
					text_remarks.setTextColor(Color.BLACK);
					text_remarks.setText("备注：" + data.getStringExtra("remarks"));
				}
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
