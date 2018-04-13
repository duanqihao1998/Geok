package com.geok.langfang.pipeline.toolcase;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
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

import com.baidu.location.LocationClient;
import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.pipeline.ApplicationApp;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.dataacquisition.Corrosive;
import com.geok.langfang.pipeline.dataacquisition.Ground;
import com.geok.langfang.pipeline.dataacquisition.Natural;
import com.geok.langfang.pipeline.dataacquisition.Protect;
import com.geok.langfang.pipeline.problem.Problem;
import com.geok.langfang.pipeline.search.LineInfo;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.DialogActivity;
import com.geok.langfang.tools.DialogEditActivity;
import com.geok.langfang.tools.MyImageButton;
import com.geok.langfang.tools.MyLocation;
import com.geok.langfang.tools.PanelListAdapter;
import com.geok.langfang.tools.SIMCardInfo;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.tools.TreeView;
import com.geok.langfang.tools.TypeQuest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 
 * @author wuchangming Ground.java接地电阻类
 * 
 */
public class KeypointAcquisition extends Activity implements OnClickListener {
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
	TextView text_keypoint_name, text_keypoint_coordinate, text_keypoint_department,
			text_keypoint_line, text_keypoint_pile, text_keypoint_offset,text_keypoint_buffer, text_keypoint_start,
			text_keypoint_end, text_keypoint_description;

	private Calendar c = null;
	Boolean isSave = false;
	Boolean update_or_save = true;
	private Request request;
	OperationDB operationDB;
	ApplicationApp app;
	int position = -1;
	private EditText et = null;
	ContentValues values = new ContentValues(); // 声明一个Contentvalues对象用于存储数据
	MyImageButton ground_new, ground_save;
	Button ground_upload;
	RelativeLayout keypoint_name, keypoint_department, keypoint_line, keypoint_pile,
		keypoint_offset,keypoint_buffer, keypoint_start, keypoint_end;
	LinearLayout keypoint_description;
	Button keypoint_coordinate;
	String str_keypoint_name, str_keypoint_coordinate, str_keypoint_department, str_keypoint_line,
			str_keypoint_pile, str_keypoint_offset, str_keypoint_buffer, str_keypoint_start, str_keypoint_end,
			str_keypoint_description, str_keypoint_location,str_keypoint_markername;
	SIMCardInfo simCardInfo;
	// 定义并获取用户id
	String userid;
	MyApplication myApplication;
	// 随机生成码，唯一标识上报信息
	String guid = java.util.UUID.randomUUID().toString();
	Button back, main;
	Tools tools = new Tools();
	MyLocation location1;
	String lon = "", lat = "", time = "";
	LocationClient mLocationClient;
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String uptime = sDateFormat.format(new Date());
	String lineId = "", markername = "", markId = "", markerStation = "", mileage = "";
	private static CustomProgressDialog progressDialog = null;
	double mile = 0;
	double station = 0, offset = 0, buffer = 0;
	LocationManager locationManager;
	int cor = 0;
	String linename = "";
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			tools.stopProgressDialog(KeypointAcquisition.this);

			switch (msg.arg1) {
			case 58:
				if (msg.getData().getInt("flag") == 1) {
					String str1 = msg.getData().getString("result");
					if (str1.equals("OK|TRUE")) {
						save(1, false);
						Toast.makeText(getApplicationContext(), "上报成功", 1000).show();
					} else {
						save(0, false);
						Toast.makeText(getApplicationContext(), "未上报成功，数据已保存", 1000).show();
					}
				} else {
					Toast.makeText(KeypointAcquisition.this, "网络连接错误", 1000).show();
				}
				break;
			case 41:
				String str = msg.getData().getString("result");
				int flag = msg.getData().getInt("flag");
				if (flag == 1) {
					if (!str.equals("-1")) {
						Bundle bundle = new Bundle();
						Intent intent = new Intent();
						intent.setClass(KeypointAcquisition.this, LineInfo.class);
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keypoint);
		myApplication = new MyApplication(this);
		userid = myApplication.userid;
		c = Calendar.getInstance();
		operationDB = new OperationDB(this);
		app = (ApplicationApp) getApplicationContext();
		simCardInfo = new SIMCardInfo(this);
		request = new Request(handler);

		String SERVERNAME = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) this.getSystemService(SERVERNAME);
		initPaneList(); // 显示侧滑栏
		init();
		cor = 0;
		getCoordinate();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
		return super.onKeyDown(keyCode, event);

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
					intent.setClass(KeypointAcquisition.this, Problem.class);
					break;
				case 1:
					intent.setClass(KeypointAcquisition.this, Protect.class);
					break;
				case 2:
					intent.setClass(KeypointAcquisition.this, Natural.class);
					break;
				case 3:
					intent.setClass(KeypointAcquisition.this, Ground.class);
					break;
				case 4:
					intent.setClass(KeypointAcquisition.this, Corrosive.class);
					break;
				}
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
	}

	// 保存
	private void tempsave() {

		if (update_or_save)// 保存
		{

			values.put("userid", userid);

			if ((text_keypoint_name != null)) {
				if (text_keypoint_name.equals("未填写")) {
					values.put("name", "未填写");
				} else {
					values.put("name", text_keypoint_name.getText().toString());
				}
			} else {
				values.put("name", 0);
			}
			// if ((text_keypoint_department != null)) {
			// if (text_keypoint_department.equals("未填写")) {
			// values.put("department", "未填写");
			// } else {
			// values.put("department",
			// text_keypoint_department.getText().toString());
			// }
			// } else {
			// values.put("department", 0);
			// }
			values.put("department", myApplication.depterid);
			if ((text_keypoint_line != null)) {
				if (text_keypoint_line.equals("未填写")) {
					values.put("line", "未填写");
				} else {
					values.put("line", text_keypoint_line.getText().toString());
				}
			} else {
				values.put("line", 0);
			}
			values.put("lineid", lineId);
			values.put("markername", markername);
			values.put("markerid", markId);
			values.put("markerstation", markerStation);
			if ((text_keypoint_offset != null)) {
				if (text_keypoint_offset.equals("未填写")) {
					values.put("offset", "未填写");
				} else {
					values.put("offset", text_keypoint_offset.getText().toString());
				}
			} else {
				values.put("offset", 0);
			}

			if ((text_keypoint_buffer != null)) {
				if (text_keypoint_buffer.equals("未填写")) {
					values.put("buffer", "未填写");
				} else {
					values.put("buffer", text_keypoint_buffer.getText().toString());
				}
			} else {
				values.put("buffer", 0);
			}
			if ((text_keypoint_start != null)) {
				if (text_keypoint_start.equals("未填写")) {
					values.put("start", "未填写");
				} else {
					values.put("start", text_keypoint_start.getText().toString());
				}
			} else {
				values.put("start", 0);
			}
			if ((text_keypoint_end != null)) {
				if (text_keypoint_end.equals("未填写")) {
					values.put("end", "未填写");
				} else {
					values.put("end", text_keypoint_end.getText().toString());
				}
			} else {
				values.put("end", 0);
			}
			if ((text_keypoint_description != null)) {
				if (text_keypoint_description.equals("未填写")) {
					values.put("description", "未填写");
				} else {
					values.put("description", text_keypoint_description.getText().toString());
				}
			} else {
				values.put("description", 0);
			}

			values.put("lat", lat);
			values.put("lon", lon);
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sDateFormat.format(new Date());
			values.put("time", date);

		}
		isSave = true;
		// 赋予中间变量，以便上报
		str_keypoint_name = String.valueOf(values.get("name"));
		str_keypoint_department = String.valueOf(values.get("department"));
		str_keypoint_offset = String.valueOf(values.get("offset"));
		str_keypoint_buffer = String.valueOf(values.get("buffer"));
		str_keypoint_start = String.valueOf(values.get("start"));
		str_keypoint_end = String.valueOf(values.get("end"));
		str_keypoint_description = String.valueOf(values.get("description"));
		str_keypoint_markername = String.valueOf(values.get("markername"));

		if ("".equals(str_keypoint_offset) || "未填写".equals(str_keypoint_offset)
				|| "填写偏移量".equals(str_keypoint_offset)) {
			str_keypoint_offset = "0";
		}
		if ("".equals(str_keypoint_buffer) || "未填写".equals(str_keypoint_buffer)
				|| "填写范围".equals(str_keypoint_buffer)) {
			str_keypoint_buffer = "0";
		}
//		if (markername.contains("+")) {
//			String str = "";
//			if(markername.toLowerCase().contains("m")){
//				str = markername.substring(markername.indexOf("+") + 1,
//						markername.lastIndexOf("m"));
//			}else{
//				str = markername.substring(markername.indexOf("+") + 1,
//						markername.length());
//			}

//			int num = Integer.valueOf(str) + Integer.valueOf(str_keypoint_buffer);
//			str_keypoint_location = markername.substring(0, markername.indexOf("+")) + "+" + num
//					+ "m";
//		} else {
			str_keypoint_location = str_keypoint_markername + "+" + str_keypoint_offset + "m";
//		}

		if (markerStation != "") {
			if(markerStation == "0"){
				mile = Double.valueOf(str_keypoint_offset).doubleValue();
			}else{
			mile = Double.valueOf(markerStation).doubleValue()
					+ Double.valueOf(str_keypoint_offset).doubleValue();
			}
		} else {
			mile = Double.valueOf(str_keypoint_offset).doubleValue();;
		}
		mileage = Double.toString(mile);
		values.put("locationdes", str_keypoint_location);
		values.put("mileage", mileage);
	}

	private void save(int flag, Boolean isToast) {
		guid = java.util.UUID.randomUUID().toString();
		values.put("guid", guid);
		values.put("isupload", flag);
		// 插入或更新数据
		// operationDB.InsertOrUpdate(values, Type.KEYPOINTACQUISITION);
		operationDB.DBinsert(values, Type.KEYPOINTACQUISITION);
		if (isToast) {
			Toast.makeText(KeypointAcquisition.this, "保存成功", 1000).show();
		}
		fresh();
	}

	private void fresh(){
		text_keypoint_description.setText("");
		text_keypoint_offset.setText("填写偏移量");
		cor = 0;
	}
	// 清空
	private void empty() {

		finish();
		Intent reset = new Intent();
		reset.setClass(KeypointAcquisition.this, KeypointAcquisition.class);
		startActivity(reset);
		Toast.makeText(KeypointAcquisition.this, "重置完成", 1000).show();
	}

	private void init() {
		// TODO Auto-generated method stub
		ground_new = (MyImageButton) findViewById(R.id.bnew_ground);
		ground_save = (MyImageButton) findViewById(R.id.bsave_ground);
		ground_upload = (Button) findViewById(R.id.bupload_ground);
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

		keypoint_name = (RelativeLayout) findViewById(R.id.keypoint_name);
		// keypoint_department = (RelativeLayout)
		// findViewById(R.id.keypoint_department);
		keypoint_line = (RelativeLayout) findViewById(R.id.keypoint_line);
		keypoint_pile = (RelativeLayout) findViewById(R.id.keypoint_pile);
		keypoint_offset = (RelativeLayout) findViewById(R.id.keypoint_offset);
		keypoint_buffer = (RelativeLayout) findViewById(R.id.keypoint_buffer);
		keypoint_start = (RelativeLayout) findViewById(R.id.keypoint_start);
		keypoint_end = (RelativeLayout) findViewById(R.id.keypoint_end);
		keypoint_description = (LinearLayout) findViewById(R.id.keypoint_description);

//		keypoint_coordinate = (Button) findViewById(R.id.keypoint_coordinate);
		text_keypoint_name = (TextView) findViewById(R.id.text_keypoint_name);
		// text_keypoint_department = (TextView)
		// findViewById(R.id.text_keypoint_department);
		text_keypoint_line = (TextView) findViewById(R.id.text_keypoint_line);
		text_keypoint_pile = (TextView) findViewById(R.id.text_keypoint_pile);
		text_keypoint_offset = (TextView) findViewById(R.id.text_keypoint_offset);
		text_keypoint_buffer = (TextView) findViewById(R.id.text_keypoint_buffer);
		text_keypoint_start = (TextView) findViewById(R.id.text_keypoint_start);
		text_keypoint_end = (TextView) findViewById(R.id.text_keypoint_end);
		text_keypoint_description = (TextView) findViewById(R.id.text_keypoint_description);

		keypoint_name.setTag(6);
//		keypoint_coordinate.setTag(7);
		// keypoint_department.setTag(8);
		keypoint_line.setTag(9);
		keypoint_buffer.setTag(10);
		keypoint_start.setTag(11);
		keypoint_end.setTag(12);
		keypoint_description.setTag(13);
		keypoint_pile.setTag(14);
		keypoint_offset.setTag(15);

		keypoint_name.setOnClickListener(this);
//		keypoint_coordinate.setOnClickListener(this);
		keypoint_description.setOnClickListener(this);
		// keypoint_department.setOnClickListener(this);
		keypoint_line.setOnClickListener(this);
		keypoint_offset.setOnClickListener(this);
		keypoint_buffer.setOnClickListener(this);
		keypoint_start.setOnClickListener(this);
		keypoint_end.setOnClickListener(this);
		keypoint_pile.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = (Integer) v.getTag();
		Intent intent = new Intent();
		// 跳转到弹出对话框编辑器类
		intent.setClass(KeypointAcquisition.this, DialogEditActivity.class);
		switch (tag) {
		case 30:
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case 31:
			Tools.backMain(this);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case 1:// 重置
			if (!isSave) {
				Builder builder = new Builder(KeypointAcquisition.this);
				builder.setTitle("提示");
				builder.setMessage("是否保存信息?");
				builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						getCoordinate();
						if (text_keypoint_name.getText().toString().equals("填写名称")
								|| text_keypoint_name.getText().toString().equals("未填写")
								// ||text_keypoint_department.getText().toString().equals("填写部门")
								// ||
								// text_keypoint_department.getText().toString().equals("未填写")
								|| text_keypoint_line.getText().toString().equals("选择线路")
								|| text_keypoint_pile.getText().toString().equals("选择桩号")
								|| text_keypoint_offset.getText().toString().equals("填写偏移量")
								|| text_keypoint_offset.getText().toString().equals("未填写")
								|| text_keypoint_start.getText().toString().equals("设置日期")
								|| text_keypoint_end.getText().toString().equals("设置日期")) {
							Toast.makeText(KeypointAcquisition.this, "带*的为必填项，请填写完再试", 300).show();
						}else if (lat == "" || lon == "") {
							Toast.makeText(KeypointAcquisition.this, "未获取坐标,无法保存重置", 300).show();
						} else {				
							tempsave();
							cor = 3;
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
				// if(lon==""||lat==""){
				// tools.startProgressDialog(KeypointAcquisition.this);
				//
				// }
				// }else{
			
			getCoordinate();
			if (text_keypoint_name.getText().toString().equals("填写名称")
					|| text_keypoint_name.getText().toString().equals("未填写")
					// ||text_keypoint_department.getText().toString().equals("填写部门")
					// ||
					// text_keypoint_department.getText().toString().equals("未填写")
					|| text_keypoint_line.getText().toString().equals("选择线路")
					|| text_keypoint_pile.getText().toString().equals("选择桩号")
					|| text_keypoint_offset.getText().toString().equals("填写偏移量")
					|| text_keypoint_offset.getText().toString().equals("未填写")
					|| text_keypoint_start.getText().toString().equals("设置日期")
					|| text_keypoint_end.getText().toString().equals("设置日期")) {
				Toast.makeText(KeypointAcquisition.this, "带*的为必填项，请填写完再试", 300).show();
//			} else if (lat == "" || lon == "") {
//				tools.startProgressDialog(KeypointAcquisition.this);
//				getCoordinate();
//				new Handler().postDelayed(new Runnable() {
//					public void run() {
//						// execute the task
//						tempsave();
//						save(0, true);
//					}
//				}, 2000);
//				tools.stopProgressDialog(KeypointAcquisition.this);
			} else if ("".equals(lat) || "".equals(lon)) {
				Toast.makeText(KeypointAcquisition.this, "未获取坐标", 300).show();
			} else {
				cor = 1;
			}
			// }
			break;
		case 3:// 上报
			
			getCoordinate();
			
			int l = 0;
			Message message = new Message();
			message.arg1 = l;
			if (text_keypoint_name.getText().toString().equals("填写名称")
					|| text_keypoint_name.getText().toString().equals("未填写")
					|| text_keypoint_line.getText().toString().equals("选择线路")
					|| text_keypoint_pile.getText().toString().equals("选择桩号")
					|| text_keypoint_offset.getText().toString().equals("填写偏移量")
					|| text_keypoint_offset.getText().toString().equals("未填写")
					|| text_keypoint_start.getText().toString().equals("设置日期")
					|| text_keypoint_end.getText().toString().equals("设置日期")) {
				Toast.makeText(KeypointAcquisition.this, "带*的为必填项，请填写完再试！", 300).show();
			} else if ("".equals(lat) || "".equals(lon)) {
				Toast.makeText(KeypointAcquisition.this, "未获取坐标", 300).show();
			} else {
				cor = 2;

			}
			// }
			break;

		case 6:
			intent.putExtra("flag", TypeQuest.KEYPOINT_NAME);
			intent.putExtra("Tittle", "关键点名称");
			if (!"填写名称".equals(text_keypoint_name.getText().toString())) {
				intent.putExtra("text", text_keypoint_name.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.KEYPOINT_NAME);
			break;
//		case 7:
//			if ("".equals(lat) || "".equals(lon)) {
//				Toast.makeText(KeypointAcquisition.this, "无GPS信号，无法获取位置信息", 300).show();
//			} 
//				getCoordinate();
//			break;

		case 8:
			intent.putExtra("flag", TypeQuest.KEYPOINT_DEPARTMENT);
			intent.putExtra("Tittle", "所属部门");
			if (!"填写部门".equals(text_keypoint_department.getText().toString())) {
				intent.putExtra("text", text_keypoint_department.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.KEYPOINT_DEPARTMENT);
			break;
		case 9:
			// intent.setClass(KeypointAcquisition.this, DialogActivity.class);
			// intent.putExtra("flag", TypeQuest.KEYPOINT_LINE);
			// intent.putExtra("Tittle", "位置");
			// if (!"填写位置".equals(text_keypoint_line.getText().toString())) {
			// intent.putExtra("text", text_keypoint_line.getText().toString());
			// }
			// startActivityForResult(intent, TypeQuest.KEYPOINT_LINE);
			//
			intent.setClass(KeypointAcquisition.this, TreeView.class);
			intent.putExtra("flag", TypeQuest.KEYPOINT_LINE);
			startActivityForResult(intent, TypeQuest.KEYPOINT_LINE);
			break;

		case 10:
			intent.putExtra("flag", TypeQuest.KEYPOINT_BUFFER);
			intent.putExtra("Tittle", "缓冲范围");
			if (!"填写范围".equals(text_keypoint_buffer.getText().toString())) {
				intent.putExtra("text", text_keypoint_buffer.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.KEYPOINT_BUFFER);
			break;

		case 11:
			// intent.putExtra("flag", TypeQuest.KEYPOINT_START);
			// intent.putExtra("Tittle", "有效起始日期");
			// if (!"填写日期".equals(text_keypoint_start.getText().toString())) {
			// intent.putExtra("text",
			// text_keypoint_start.getText().toString());
			// }
			// startActivityForResult(intent, TypeQuest.KEYPOINT_START);
			Tools.setDateDialog(KeypointAcquisition.this, c, text_keypoint_start);
			break;
		case 12:
			// intent.putExtra("flag", TypeQuest.KEYPOINT_END);
			// intent.putExtra("Tittle", "有效终止日期");
			// if (!"填写日期".equals(text_keypoint_end.getText().toString())) {
			// intent.putExtra("text", text_keypoint_end.getText().toString());
			// }
			// startActivityForResult(intent, TypeQuest.KEYPOINT_END);
			if ("设置日期".equals(text_keypoint_start.getText().toString())) {
				Toast.makeText(KeypointAcquisition.this, "请先选择起始日期！", 1000).show();
			} else {
				str_keypoint_start = text_keypoint_start.getText().toString();
				Tools.setEndDateDialog(KeypointAcquisition.this, c, str_keypoint_start,
						text_keypoint_end);
			}
			break;
		case 14:
			if (lineId == null) {
				Toast.makeText(getApplicationContext(), "请先选择线路", 1000).show();
			} else {
				intent.setClass(KeypointAcquisition.this, DialogActivity.class);
				intent.putExtra("flag", TypeQuest.KEYPOINT_PILE);
				intent.putExtra("lineid", lineId);
				if(!(text_keypoint_pile.getText().toString().equals("选择桩号"))){
					intent.putExtra("markName", text_keypoint_pile.getText().toString());
				}
				startActivityForResult(intent, TypeQuest.KEYPOINT_PILE);
			}
			break;
		case 15:
			intent.putExtra("flag", TypeQuest.KEYPOINT_OFFSET);
			intent.putExtra("Tittle", "偏移量");
			if (!"填写偏移量".equals(text_keypoint_offset.getText().toString())) {
				intent.putExtra("text", text_keypoint_offset.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.KEYPOINT_OFFSET);
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
			if(lineId != null){
				linename = text_keypoint_line.getText().toString();
				if (Tools.isNetworkAvailable(this, true)) {
					request.LineInfoSearchRequest(myApplication.userid, lineId);
					tools.startProgressDialog(this);
				}
			}
			else{
				Toast.makeText(KeypointAcquisition.this, "请选择线路后查看", 1000).show();
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
			intent.setClass(KeypointAcquisition.this, KeypointHistory.class);
			startActivity(intent);
			// Toast.makeText(this, "此功能暂未开放", 1000).show();
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
		case TypeQuest.KEYPOINT_NAME:
			if (resultCode == Activity.RESULT_OK) {

				if ("".equals(data.getStringExtra("keypointname"))) {
					text_keypoint_name.setTextColor(Color.RED);
					text_keypoint_name.setText("未填写");

				} else {
					text_keypoint_name.setTextColor(Color.BLACK);
					text_keypoint_name.setText(data.getStringExtra("keypointname"));
				}
			}
			break;
		case TypeQuest.KEYPOINT_DEPARTMENT:
			if (resultCode == Activity.RESULT_OK) {

				if ("".equals(data.getStringExtra("keypointdepartment"))) {
					text_keypoint_department.setTextColor(Color.RED);
					text_keypoint_department.setText("未填写");

				} else {
					text_keypoint_department.setTextColor(Color.BLACK);
					text_keypoint_department.setText(data.getStringExtra("keypointdepartment"));
				}
			}
			break;
		case TypeQuest.KEYPOINT_LINE:
			if (resultCode == Activity.RESULT_OK) {
				text_keypoint_line.setText(data.getStringExtra("line"));
				lineId = data.getStringExtra("lineId");
				text_keypoint_pile.setText("选择桩号");
			}
			break;

		case TypeQuest.KEYPOINT_PILE:
			if (resultCode == Activity.RESULT_OK) {
				text_keypoint_pile.setText(data.getStringExtra("pile"));
				position = data.getIntExtra("po", -1);
				markername = data.getStringExtra("pile");
				markId = data.getStringExtra("markId");
				markerStation = data.getStringExtra("markerstation");
			}
			break;
		case TypeQuest.KEYPOINT_OFFSET:
			if (resultCode == Activity.RESULT_OK) {
				
				if ("".equals(data.getStringExtra("keypointoffset"))) {
					text_keypoint_offset.setTextColor(Color.RED);
					text_keypoint_offset.setText("未填写");
					
				} else {
					text_keypoint_offset.setTextColor(Color.BLACK);
					text_keypoint_offset.setText(data.getStringExtra("keypointoffset"));
				}
			}
			break;
		case TypeQuest.KEYPOINT_BUFFER:
			if (resultCode == Activity.RESULT_OK) {

				if ("".equals(data.getStringExtra("keypointbuffer"))) {
					text_keypoint_buffer.setTextColor(Color.RED);
					text_keypoint_buffer.setText("未填写");

				} else {
					text_keypoint_buffer.setTextColor(Color.BLACK);
					text_keypoint_buffer.setText(data.getStringExtra("keypointbuffer"));
				}
			}
			break;
		case TypeQuest.KEYPOINT_START:
			// if (resultCode == Activity.RESULT_OK) {
			//
			// if ("".equals(data.getStringExtra("keypointstart"))) {
			// text_keypoint_start.setTextColor(Color.RED);
			// text_keypoint_start.setText("未填写");
			//
			// } else {
			// text_keypoint_start.setTextColor(Color.BLACK);
			// text_keypoint_start.setText(data
			// .getStringExtra("keypointstart"));
			// }
			// }
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("test_time"))) {
					text_keypoint_start.setText("2005-1-1");

				} else {
					text_keypoint_start.setText((CharSequence) et);
				}
			}
			break;
		case TypeQuest.KEYPOINT_END:
			// if (resultCode == Activity.RESULT_OK) {
			//
			// if ("".equals(data.getStringExtra("keypointend"))) {
			// text_keypoint_end.setTextColor(Color.RED);
			// text_keypoint_end.setText("未填写");
			//
			// } else {
			// text_keypoint_end.setTextColor(Color.BLACK);
			// text_keypoint_end.setText(data
			// .getStringExtra("keypointend"));
			// }
			// }
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("test_time"))) {
					text_keypoint_end.setText("2030-12-31");

				} else {
					text_keypoint_end.setText((CharSequence) et);
				}
			}
			break;
		}
	}

	private void getCoordinate() {
		// Handler handler = new Handler() {
		//
		// @Override
		// public void handleMessage(Message msg) {
		// super.handleMessage(msg);
		// if (msg.what == 1) {
		// // Toast.makeText(KeypointAcquisition.this, "没有获得定位信息",
		// // 1000).show();
		// } else {
		// lon = location1.mylocation.longitude + "";
		// lat = location1.mylocation.latitude + "";
		// // Toast.makeText(KeypointAcquisition.this, "成功获取定位信息",
		// // 1000).show();
		// keypoint_coordinate.setTextColor(Color.WHITE);
		// keypoint_coordinate.setTextSize(15);
		// keypoint_coordinate.setText("已获坐标");
		// keypoint_coordinate.setBackgroundResource(R.drawable.button_bg_02);
		// mLocationClient.stop();
		// // Toast.makeText(KeypointAcquisition.this,
		// // "坐标是:" + lon + ";" + lat, 1000).show();
		// values.put("lat", lat);
		// values.put("lon", lon);
		// }
		//
		// }
		//
		// };
		// location1 = new MyLocation(this, handler);
		// mLocationClient = location1.mLocationClient;
		// mLocationClient.start();
		// tools.stopProgressDialog(KeypointAcquisition.this);
		// }
		
		try {
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

			// String provider = locationManager.getBestProvider(criteria,
			// true); // 获取GPS信息

			// 设置监听器，自动更新的最小时间为间隔(N*1000)秒或最小位移变化超过N米

			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					locationListener);

			// locationManager.addGpsStatusListener(gpsstatusListener);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final LocationListener locationListener = new LocationListener() {

		public void onLocationChanged(Location location) {
			if (location != null && location.getLongitude() > 0) {
				lon = location.getLongitude() + "";
				lat = location.getLatitude() + "";
//				 keypoint_coordinate.setTextColor(Color.WHITE);
//				 keypoint_coordinate.setTextSize(15);
//				 keypoint_coordinate.setText("已获坐标");
//				 keypoint_coordinate.setBackgroundResource(R.drawable.button_bg_02);
				
				 if(cor!=0){
					 if(cor == 1){
						 tempsave();
						 save(0,true);
						 locationManager.removeUpdates(locationListener);
					 }else if (cor == 2){
						 if (Tools.isNetworkAvailable(KeypointAcquisition.this, true)) {
								tools.startProgressDialog(KeypointAcquisition.this);
								tempsave();
								request.KeypointRequest(myApplication.userid, myApplication.imei,
										myApplication.depterid, str_keypoint_name, str_keypoint_location,
										str_keypoint_buffer, str_keypoint_start, str_keypoint_end,
										String.valueOf(lon), String.valueOf(lat), str_keypoint_description,
										lineId, mileage, "");
							}else{
								tempsave();
								save(0,true);
							}
						 locationManager.removeUpdates(locationListener);
					 }else if(cor == 3){
						 tempsave();
						 save(0,true);
						 empty();
						 locationManager.removeUpdates(locationListener);
					 }
				 }else{
					 locationManager.removeUpdates(locationListener);
				 }
				
			}
		}

		public void onProviderDisabled(String provider) {
			// WriteDB(null);
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

			// "<locationListener>本地监听状态改变\n");
			switch (status) {

			case LocationProvider.AVAILABLE:
				// "<locationListener>有可用卫星\n");
				break;
			case LocationProvider.OUT_OF_SERVICE:
				// "<locationListener>无可用卫星服务\n");
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				// "<locationListener>暂时无可用卫星服务\n");
				break;
			}

		}

	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		locationManager.removeUpdates(locationListener);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		locationManager.removeUpdates(locationListener);
		super.onPause();
	}

}
