package com.geok.langfang.pipeline.dataacquisition;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.pipeline.ApplicationApp;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.problem.Problem;
import com.geok.langfang.pipeline.search.LineInfo;
import com.geok.langfang.pipeline.toolcase.KeypointAcquisition;
import com.geok.langfang.tools.DialogActivity;
import com.geok.langfang.tools.DialogEditActivity;
import com.geok.langfang.pipeline.dataacquisition.Protect;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.MyImageButton;
import com.geok.langfang.tools.PanelListAdapter;
import com.geok.langfang.tools.SIMCardInfo;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.tools.TreeView;
import com.geok.langfang.tools.TypeQuest;
import com.geok.langfang.tools.Pipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Protect extends Activity implements OnClickListener {

	ListView panelList; // 声明侧滑栏List对象
	Map<String, Object> map;
	PanelListAdapter panelListAdapter; // 声明侧滑栏的适配器对象
	SimpleAdapter simpleAdapter1, simpleAdapter2;
	// 定义侧滑栏的图
	int[] image = { R.drawable.menu_problem_selector, R.drawable.sj_menu1_selector,
			R.drawable.sj_menu2_selector, R.drawable.sj_menu3_selector,
			R.drawable.sj_menu4_selector };
	// 定义侧滑栏图片标题
	String[] imageTitle = { "事项上报", "保护电位", "自然电位", "接地电阻", "防腐检漏", };
	boolean isSave = false; // 标识是否保存
	boolean update_or_save = true; // 标识是否更新保存
	private Request request;
	ContentValues values = new ContentValues();
	MyImageButton protect_new, protect_save, protect_upload, protect_previous, protect_next;
	TextView text_year, text_month, text_line, text_pile, text_temperature, text_weather,
			text_value, text_tester, text_test_time, text_ground, text_voltage, text_remarks,text_natural,text_ir;
	private Calendar c = null;
	private EditText et = null;
	OperationDB operationDB;
	int position = -1;
	ApplicationApp app;
	RelativeLayout year, month, line, pile, temperature, weather, value, tester, test_time, ground,
			voltage, remarks, natural,ir;
	// 用于存储各输入的值
	String str_year, str_month, str_line, str_pile, str_temperature, str_weather, str_value,
			str_testtime, str_ground, str_voltage, str_remarks,str_natural,str_ir;
	Context context = this;
	SIMCardInfo simCardInfo;
	String lineId, markId, markerstation; // 定义管线ID、桩ID,用于存储和上报
	String userid;
	MyApplication myApplication;
	String guid = java.util.UUID.randomUUID().toString();
	Button back, main;
	ProtectHistoryData Protect_data;
	LinearLayout protect_remarks_layout;
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String currenttime = sDateFormat.format(new java.util.Date());
	String[] currenttime2 = currenttime.split(" ");
	private static CustomProgressDialog progressDialog = null;
	String linename = "";
	public Handler handler = new Handler() {
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
			case 7:
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
						intent.setClass(Protect.this, LineInfo.class);
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
		setContentView(R.layout.protect);
		myApplication = new MyApplication(this);
		userid = myApplication.userid;
		c = Calendar.getInstance();

		operationDB = new OperationDB(this); // 实例化
		app = (ApplicationApp) getApplicationContext();
		request = new Request(handler);
		simCardInfo = new SIMCardInfo(this);
		// 获得保护电位数据
		Protect_data = (ProtectHistoryData) getIntent().getSerializableExtra("values");
		init();
		if (Protect_data != null) {
			String upgrate = getIntent().getStringExtra("flag");
			if ("upgrate".equals(upgrate)) {
				guid = Protect_data.getGuid();
				text_year.setText(Protect_data.getYear());
				text_month.setText(Protect_data.getMonth());
				text_line.setText(Protect_data.getLine());
				text_pile.setText(Protect_data.getPile());
				text_value.setText(Protect_data.getValue());
				String[] check_time= Protect_data.getTesttime().split(" ");
				text_test_time.setText(check_time[0]);
				text_voltage.setText(Protect_data.getVoltage());
				text_ground.setText(Protect_data.getGround());
				text_temperature.setText(Protect_data.getTemperature());
				text_remarks.setText(Protect_data.getRemarks());
				lineId=Protect_data.getLineid();
				markId=Protect_data.getPileid();
				text_natural.setText(Protect_data.getNatural());
				text_ir.setText(Protect_data.getIr());
				position = Tools.pileIndext(Protect.this, lineId, markId);
			}
		}
		initPaneList();
		// initList();

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
					intent.setClass(Protect.this, Problem.class);
					break;
				case 1:
					intent.setClass(Protect.this, Protect.class);
					break;
				case 2:
					intent.setClass(Protect.this, Natural.class);
					break;
				case 3:
					intent.setClass(Protect.this, Ground.class);
					break;
				case 4:
					intent.setClass(Protect.this, Corrosive.class);
					break;
				}
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
	}

	/*
	 * 保存界面上数据到数据库
	 */
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

			if ((text_month != null)) {
				if (text_month.equals("未填写")) {
					values.put("month", "未填写");
				} else {
					values.put("month", text_month.getText().toString());
				}
			} else {
				values.put("month", 0);
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
					values.put("test_time", text_test_time.getText().toString() + " " + currenttime2[1]);
				}
			} else {
				values.put("test_time", 0);
			}

			if ((text_ground != null)) {
				if (text_ground.equals("未填写")) {
					values.put("ground", "未填写");
				} else {
					values.put("ground", text_ground.getText().toString());
				}
			} else {
				values.put("ground", 0);
			}
			if ((text_natural != null)) {
				if (text_natural.equals("未填写")) {
					values.put("natural", "未填写");
				} else {
					values.put("natural", text_natural.getText().toString());
				}
			} else {
				values.put("natural", 0);
			}
			if ((text_ir != null)) {
				if (text_ir.equals("未填写")) {
					values.put("ir", "未填写");
				} else {
					values.put("ir", text_ir.getText().toString());
				}
			} else {
				values.put("ir", 0);
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

			if ((text_remarks != null)) {
				if (text_remarks.equals("未填写")) {
					values.put("remarks", "未填写");
				} else {
					int size = text_remarks.getText().length();
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
		str_ground = String.valueOf(values.get("ground"));
		str_natural = String.valueOf(values.get("natural"));
		str_ir = String.valueOf(values.get("ir"));
		str_temperature = String.valueOf(values.get("temperature"));

	}

	private void save(int flag, Boolean isToast) {
		values.put("isupload", flag);
		operationDB.InsertOrUpdate(values, Type.PROTECTIVE_POTENTIAL);
		if (isToast) {
			Toast.makeText(Protect.this, "保存成功", 1000).show();
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
	private void empty() {
		guid = java.util.UUID.randomUUID().toString();
		if (text_year != null) {
			text_year.setText("选择年份");
		}

		if (text_month != null) {
			text_month.setText("选择月份");
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

		if (text_value != null) {
			text_value.setText("填写电位值");
			text_value.setTextColor(Color.BLACK);
		}

		if (text_test_time != null) {
			text_test_time.setText("填写测试时间");
			text_test_time.setTextColor(Color.BLACK);
		}

		if (text_ground != null) {
			text_ground.setText("填写电阻率");
			text_ground.setTextColor(Color.BLACK);
		}

		if (text_voltage != null) {
			text_voltage.setText("填写电压");
			text_voltage.setTextColor(Color.BLACK);
		}
		if (text_natural != null) {
			text_natural.setText("填写电位值");
			text_natural.setTextColor(Color.BLACK);
		}
		if (text_ir!= null) {
			text_ir.setText("填写IR降");
			text_ir.setTextColor(Color.BLACK);
		}

		if (text_remarks != null) {
			text_remarks.setText("备注：");
			text_remarks.setTextColor(Color.BLACK);
		}

		Toast.makeText(Protect.this, "清空成功！", 500).show();
		System.out.println("清空成功--------");
	}

	private void init() {
		// TODO Auto-generated method stub
		protect_new = (MyImageButton) findViewById(R.id.bnew_protect);
		protect_save = (MyImageButton) findViewById(R.id.bsave_protect);
		protect_upload = (MyImageButton) findViewById(R.id.bupload_protect);
		protect_previous = (MyImageButton) findViewById(R.id.bprevious_protect);
		protect_next = (MyImageButton) findViewById(R.id.bnext_protect);
		protect_new.setTag(1);
		protect_save.setTag(2);
		protect_upload.setTag(3);
		protect_previous.setTag(4);
		protect_next.setTag(5);
		protect_new.setOnClickListener(this);
		protect_save.setOnClickListener(this);
		protect_upload.setOnClickListener(this);
		protect_previous.setOnClickListener(this);
		protect_next.setOnClickListener(this);

		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		back.setTag(30);
		main.setTag(31);
		back.setOnClickListener(this);
		main.setOnClickListener(this);
		test_time = (RelativeLayout) findViewById(R.id.protect_test_time);
		text_test_time = (TextView) findViewById(R.id.protect_test_time_text);
		test_time.setTag(6);
		test_time.setOnClickListener(this);

		year = (RelativeLayout) findViewById(R.id.protect_year);
		month = (RelativeLayout) findViewById(R.id.protect_month);
		line = (RelativeLayout) findViewById(R.id.protect_line);
		pile = (RelativeLayout) findViewById(R.id.protect_pile);
		value = (RelativeLayout) findViewById(R.id.protect_value);
		voltage = (RelativeLayout) findViewById(R.id.protect_voltage);
		ground = (RelativeLayout) findViewById(R.id.protect_ground);
		temperature = (RelativeLayout) findViewById(R.id.protect_temperature);
		natural = (RelativeLayout) findViewById(R.id.protect_natural);
		ir = (RelativeLayout) findViewById(R.id.protect_ir);

		text_year = (TextView) findViewById(R.id.text_protect_year);
		text_month = (TextView) findViewById(R.id.text_protect_month);
		text_line = (TextView) findViewById(R.id.text_protect_line);
		text_pile = (TextView) findViewById(R.id.text_protect_pile);
		text_value = (TextView) findViewById(R.id.text_protect_value);
		text_voltage = (TextView) findViewById(R.id.text_protect_voltage);
		text_temperature = (TextView) findViewById(R.id.text_protect_temperature);
		text_ground = (TextView) findViewById(R.id.text_protect_ground);
		text_natural = (TextView) findViewById(R.id.text_protect_natural);
		text_ir = (TextView) findViewById(R.id.text_protect_ir);
		text_remarks = (TextView) findViewById(R.id.text_protect_remarks);
		protect_remarks_layout = (LinearLayout) findViewById(R.id.protect_remarks_layout);

		if(currenttime.length() == 19){
			text_test_time.setText(currenttime.substring(0, 10));
			}
		
		year.setTag(7);
		month.setTag(8);
		line.setTag(9);
		pile.setTag(10);
		value.setTag(11);
		voltage.setTag(12);
		temperature.setTag(13);
		ground.setTag(14);
		natural.setTag(16);
		ir.setTag(17);
		// text_remarks.setTag(15);
		protect_remarks_layout.setTag(15);

		year.setOnClickListener(this);
		month.setOnClickListener(this);
		line.setOnClickListener(this);
		pile.setOnClickListener(this);
		value.setOnClickListener(this);
		voltage.setOnClickListener(this);
		temperature.setOnClickListener(this);
		natural.setOnClickListener(this);
		ir.setOnClickListener(this);
		ground.setOnClickListener(this);
		// text_remarks.setOnClickListener(this);
		protect_remarks_layout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = (Integer) v.getTag();
		Intent intent = new Intent();
		// 跳转到弹出对话框编辑器类
		intent.setClass(Protect.this, DialogEditActivity.class);
		switch (tag) {

		case 30:
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case 31:
			Tools.backMain(Protect.this);
			break;
		case 1:// 重置
			if (!isSave) {
				AlertDialog.Builder builder = new Builder(Protect.this);
				builder.setTitle("提示");
				builder.setMessage("是否保存信息?");
				builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						tempsave();
						if (str_year.equals("选择年份") || str_month.equals("选择月份")
								|| str_line.equals("选择线路") || str_pile.equals("选择桩号")
								|| str_testtime.equals("填写测试时间") || str_value.equals("填写电位值")
								|| str_value.equals("未填写") || str_voltage.equals("填写电压")
								|| str_voltage.equals("未填写") || str_temperature.equals("填写温度")
								|| str_temperature.equals("未填写") || str_ground.equals("填写电阻率")
								|| str_ground.equals("未填写") || str_remarks.equals("备注：")
								|| str_remarks.equals("未填写")) {
							Toast.makeText(Protect.this, "带*的为必填项，请填写完再试！", 300).show();
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

			// System.out.println(guid + userid + str_year + str_month +
			// str_line
			// + str_pile + str_value + str_testtime + str_remarks
			// + str_voltage + str_ground + str_temperature
			// + "ohye----------------------");
			if (str_year.equals("选择年份") || str_month.equals("选择月份") || str_line.equals("选择线路")
					|| str_pile.equals("选择桩号") || str_testtime.equals("填写测试时间")
					|| str_value.equals("填写电位值") || str_value.equals("未填写")
					|| str_voltage.equals("填写电压") || str_voltage.equals("未填写")
					|| str_temperature.equals("填写温度") || str_temperature.equals("未填写")
					|| str_ground.equals("填写电阻率") || str_ground.equals("未填写")
					|| str_remarks.equals("备注：") || str_remarks.equals("未填写")) {
				Toast.makeText(Protect.this, "带*的为必填项，请填写完再试！", 300).show();
			} else {
				save(0, true);
			}
			// if(str_year.equals("选择年份")){
			// Toast.makeText(Protect.this, "请选择年份", 300).show();
			// }else if(str_month.equals("选择月份")){
			// Toast.makeText(Protect.this, "请选择月份", 300).show();
			// }else if(str_line.equals("选择线路")){
			// Toast.makeText(Protect.this, "请选择线路", 300).show();
			// }else if(str_pile.equals("选择桩号")){
			// Toast.makeText(Protect.this, "请选择桩号", 300).show();
			// }else if(str_testtime.equals("填写测试时间")){
			// Toast.makeText(Protect.this, "填写测试时间", 300).show();
			// }else if(str_value.equals("填写电位值")||str_value.equals("未填写")){
			// Toast.makeText(Protect.this, "请填写电位值", 300).show();
			// }else
			// if(str_voltage.equals("填写电压")||str_voltage.equals("未填写")){
			// Toast.makeText(Protect.this, "请填写电压", 300).show();
			// }else
			// if(str_temperature.equals("填写温度")||str_temperature.equals("未填写")){
			// Toast.makeText(Protect.this, "请填写温度", 300).show();
			// }else
			// if(str_ground.equals("填写电阻率")||str_ground.equals("未填写")){
			// Toast.makeText(Protect.this, "请填写电阻率", 300).show();
			// }else
			// if(str_remarks.equals("备注：")||str_remarks.equals("未填写")){
			// Toast.makeText(Protect.this, "请填写备注", 300).show();
			//
			// }else{
			// save(1);
			// }
			break;
		case 3:// 上报
			tempsave();
			if (str_year.equals("选择年份") || str_month.equals("选择月份") || str_line.equals("选择线路")
					|| str_pile.equals("选择桩号") || str_testtime.equals("填写测试时间")
					|| str_value.equals("填写电位值") || str_value.equals("未填写")
					|| str_voltage.equals("填写电压") || str_voltage.equals("未填写")
					|| str_temperature.equals("填写温度") || str_temperature.equals("未填写")
					|| str_ground.equals("填写电阻率") || str_ground.equals("未填写")
					|| str_natural.equals("填写电位值") || str_natural.equals("未填写")
					|| str_ir.equals("填写IR降") || str_ir.equals("未填写")
					|| str_remarks.equals("备注：") || str_remarks.equals("未填写")) {
				Toast.makeText(Protect.this, "带*的为必填项，请填写完再试！", 300).show();
			} else {
				// save(1);
				// }
				// if (str_year.equals("选择年份")) {
				// Toast.makeText(Protect.this, "请选择年份", 300).show();
				// } else if (str_month.equals("选择月份")) {
				// Toast.makeText(Protect.this, "请选择月份", 300).show();
				// } else if (str_line.equals("选择线路")) {
				// Toast.makeText(Protect.this, "请选择线路", 300).show();
				// } else if (str_pile.equals("选择桩号")) {
				// Toast.makeText(Protect.this, "请选择桩号", 300).show();
				// } else if (str_testtime.equals("填写测试时间")) {
				// Toast.makeText(Protect.this, "填写测试时间", 300).show();
				// } else if (str_value.equals("填写电位值") ||
				// str_value.equals("未填写")) {
				// Toast.makeText(Protect.this, "请填写电位值", 300).show();
				// } else if (str_voltage.equals("填写电压") ||
				// str_voltage.equals("未填写")) {
				// Toast.makeText(Protect.this, "请填写电压", 300).show();
				// } else if (str_temperature.equals("填写温度")
				// || str_temperature.equals("未填写")) {
				// Toast.makeText(Protect.this, "请填写温度", 300).show();
				// } else if (str_ground.equals("填写电阻率") ||
				// str_ground.equals("未填写")) {
				// Toast.makeText(Protect.this, "请填写电阻率", 300).show();
				// } else if (str_remarks.equals("备注：") ||
				// str_remarks.equals("未填写")) {
				// Toast.makeText(Protect.this, "请填写备注", 300).show();
				// } else {
				System.out.println(guid + userid + str_year + str_month + str_line + str_pile
						+ str_value + str_testtime + str_remarks + str_voltage + str_ground
						+ str_temperature + "ohye----------------------");
				if (Tools.isNetworkAvailable(this, true)) {
					startProgressDialog();
					request.protectPotentialRequest(str_year, str_month, lineId, markId, str_value,
							userid, str_testtime, str_remarks, str_voltage, str_ground,
							str_temperature, str_natural, str_ir, "");
				} else {
					Toast.makeText(Protect.this, "无网络连接，上报失败！", 500).show();
					save(0, true);
				}
			}
			break;

		case 4:// 上一桩
			if (position == -1) {
				Toast.makeText(Protect.this, "您还未选择桩号", 500).show();
				// if(text_pile!=null&&Pipe.listpile.size()!=0)
				// {
				// text_pile.setText(Pipe.listpile.get(0));
				// position=0;
				// }
			} else if (position == 0)
				Toast.makeText(Protect.this, "您已处于第一桩", 500).show();
			else {
				text_pile.setText(Pipe.listpile.get(position - 1));
				position = position - 1;
				System.out.println("上一桩了了" + position);
			}
			break;
		case 5:// 下一桩
			if (position == -1) {
				Toast.makeText(Protect.this, "您还未选择桩号", 500).show();
				// if(text_pile!=null&&Pipe.listpile.size()!=0)
				// {
				// text_pile.setText(Pipe.listpile.get(0));
				// position=0;
				// }
			} else if ((position == (Pipe.listpile.size() - 1))) {
				Toast.makeText(Protect.this, "您处于最后一桩", 500).show();
			} else {
				text_pile.setText(Pipe.listpile.get(position + 1));
				position = position + 1;
			}
			break;
		case 6:
			Tools.setDateDialog(this, c, text_test_time);
			break;
		case 7:
			Intent intent2 = new Intent();
			intent2.setClass(Protect.this, DialogActivity.class);
			intent2.putExtra("flag", TypeQuest.PROTECT_YEAR);
			startActivityForResult(intent2, TypeQuest.PROTECT_YEAR);
			break;
		case 8:
			Intent intent3 = new Intent();
			intent3.setClass(Protect.this, DialogActivity.class);
			intent3.putExtra("flag", TypeQuest.PROTECT_MONTH);
			startActivityForResult(intent3, TypeQuest.PROTECT_MONTH);
			break;

		case 9:
			intent.setClass(Protect.this, TreeView.class);
			intent.putExtra("flag", TypeQuest.PROTECT_LINE);
			startActivityForResult(intent, TypeQuest.PROTECT_LINE);
			break;
		case 10:
			if (lineId == null) {
				Toast.makeText(getApplicationContext(), "请先选择线路", 1000).show();
			} else {
				intent.setClass(Protect.this, DialogActivity.class);
				intent.putExtra("flag", TypeQuest.PROTECT_PILE);
				intent.putExtra("lineid", lineId);
				if(!(text_pile.getText().toString().equals("选择桩号"))){
					intent.putExtra("markName", text_pile.getText().toString());
				}
				startActivityForResult(intent, TypeQuest.PROTECT_PILE);
			}
			break;
		case 11:
			intent.putExtra("flag", TypeQuest.PROTECT_VALUE);
			intent.putExtra("Tittle", "电位值(V)");
			if (!"填写电位值".equals(text_value.getText().toString())) {
				intent.putExtra("text", text_value.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.PROTECT_VALUE);
			break;

		case 12:
			intent.putExtra("flag", TypeQuest.PROTECT_VOLTAGE);
			intent.putExtra("Tittle", "交流电干扰电压(V)");
			if (!"填写电压".equals(text_voltage.getText().toString())) {
				intent.putExtra("text", text_voltage.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.PROTECT_VOLTAGE);
			break;

		case 13:
			intent.putExtra("flag", TypeQuest.PROTECT_TEMPERATURE);
			intent.putExtra("Tittle", "温度(℃)");
			if (!"填写温度".equals(text_temperature.getText().toString())) {
				intent.putExtra("text", text_temperature.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.PROTECT_TEMPERATURE);
			break;

		case 14:
			intent.putExtra("flag", TypeQuest.PROTECT_GROUND);
			intent.putExtra("Tittle", "土壤电阻率");
			if (!"填写电阻率".equals(text_ground.getText().toString())) {
				intent.putExtra("text", text_ground.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.PROTECT_GROUND);
			break;
		// case 15:
		// intent.putExtra("flag", TypeQuest.PROTECT_REMARKS);
		// intent.putExtra("Tittle", "备注");
		// if (!"备注:".equals(text_remarks.getText().toString())) {
		// intent.putExtra("text", text_remarks.getText().toString());
		// }
		// startActivityForResult(intent, TypeQuest.PROTECT_REMARKS);
		// break;
		case 16:
			intent.putExtra("flag", TypeQuest.PROTECT_NATURAL);
			intent.putExtra("Tittle", "自然电位值");
			if (!"填写电位值".equals(text_natural.getText().toString())) {
				intent.putExtra("text", text_natural.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.PROTECT_NATURAL);
			break;
		case 17:
			intent.putExtra("flag", TypeQuest.PROTECT_IR);
			intent.putExtra("Tittle", "IR降");
			if (!"填写IR降".equals(text_ir.getText().toString())) {
				intent.putExtra("text", text_ir.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.PROTECT_IR);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case 1:
			Tools.exit(Protect.this);
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
				Toast.makeText(Protect.this, "请选择线路后查看", 1000).show();
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
			intent.setClass(Protect.this, ProtectHistory.class);
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
		case TypeQuest.PROTECT_YEAR:
			if (resultCode == Activity.RESULT_OK) {
				text_year.setText(data.getStringExtra("year"));
			}
			break;
		case TypeQuest.PROTECT_MONTH:
			if (resultCode == Activity.RESULT_OK) {
				text_month.setText(data.getStringExtra("month"));
			}
			break;
		case TypeQuest.PROTECT_LINE:
			if (resultCode == Activity.RESULT_OK) {
				text_line.setText(data.getStringExtra("line"));
				lineId = data.getStringExtra("lineId");
				text_pile.setText("选择桩号");
			}
			break;
		case TypeQuest.PROTECT_PILE:
			if (resultCode == Activity.RESULT_OK) {
				text_pile.setText(data.getStringExtra("pile"));
				position = data.getIntExtra("po", -1);
				markId = data.getStringExtra("markId");
				markerstation = data.getStringExtra("markerstation");
			}

			break;
		case TypeQuest.PROTECT_TEMPERATURE:
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
		case TypeQuest.PROTECT_WEATHER:
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
		case TypeQuest.PROTECT_VALUE:
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
		case TypeQuest.PROTECT_TESTTIME:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("test_time"))) {
					text_test_time.setTextColor(Color.RED);
					text_test_time.setText("未填写");

				} else {
					text_test_time.setText((CharSequence) et);
				}
			}
			break;

		case TypeQuest.PROTECT_GROUND:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("ground"))) {
					text_ground.setTextColor(Color.RED);
					text_ground.setText("未填写");

				} else {
					text_ground.setTextColor(Color.BLACK);
					text_ground.setText(data.getStringExtra("ground"));
				}
			}
			break;
		case TypeQuest.PROTECT_VOLTAGE:
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
		case TypeQuest.PROTECT_REMARKS:
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
			
		case TypeQuest.PROTECT_NATURAL:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("natural"))) {
					text_natural.setTextColor(Color.RED);
					text_natural.setText("未填写");

				} else {
					text_natural.setTextColor(Color.BLACK);
					text_natural.setText(data.getStringExtra("natural"));
				}
			}
			break;
		case TypeQuest.PROTECT_IR:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("ir"))) {
					text_ir.setTextColor(Color.RED);
					text_ir.setText("未填写");

				} else {
					text_ir.setTextColor(Color.BLACK);
					text_ir.setText(data.getStringExtra("ir"));
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
