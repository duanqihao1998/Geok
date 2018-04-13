package com.geok.langfang.pipeline.dataacquisition;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.problem.Problem;
import com.geok.langfang.pipeline.search.LineInfo;
import com.geok.langfang.pipeline.toolcase.KeypointAcquisition;
import com.geok.langfang.tools.DialogActivity;
import com.geok.langfang.tools.DialogEditActivity;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Corrosive.java类是防腐层检漏的类
 */
public class Corrosive extends Activity implements OnClickListener {
	ListView panelList; // 声明侧滑栏List对象
	PanelListAdapter panelListAdapter; // 声明侧滑栏的适配器对象
	// 定义侧滑栏的图片
	int[] image = { R.drawable.menu_problem_selector, R.drawable.sj_menu1_selector,
			R.drawable.sj_menu2_selector, R.drawable.sj_menu3_selector,
			R.drawable.sj_menu4_selector };
	// 定义侧滑栏图片标题
	String[] imageTitle = { "事项上报", "保护电位", "自然电位", "接地电阻", "防腐检漏", };
	// 声明界面中的文本框
	TextView text_line, text_pile, text_damage_des, text_damage_type, text_repair_type,
			text_corrosion_des, text_offset, text_soil, text_area, text_corrosion_area,
			text_corrosion_num, text_repair_obj, text_clockposition, text_clockposition1,
			text_clockposition2, text_pitdepthmax, text_pitdepthmin, text_check_date,
			text_repair_date, text_repair_situation, text_pile_info, text_remarks;
	// 声明界面中每一条字段的布局
	RelativeLayout line, pile, damage_des, damage_type, repair_type, corrosion_des, offset, soil,
			area, corrosion_area, corrosion_num, repair_obj, clockposition, check_date,
			repair_date, repair_situation, pile_info, remarks, clockposition1, clockposition2,
			pitdepthmax, pitdepthmin;
	// 用于存储各输入的值
	String str_line, str_pile, str_damage_des, str_damage_type, str_repair_type, str_corrosion_des,
			str_offset, str_soil, str_area, str_corrosion_area, str_corrosion_num, str_repair_obj,
			str_clockposition, str_check_date, str_repair_date, str_repair_situation,
			str_pile_info, str_remarks, str_clockposition1, str_clockposition2, str_pitdepthmax,
			str_pitdepthmin;
	private EditText et = null; // 年月日赋值
	private Calendar c = null; // 测试时间选择
	int position = -1; // 选桩号的位置变量
	// 声明界面中五个按钮
	MyImageButton ntiseptic_new, ntiseptic_save, ntiseptic_upload, ntiseptic_previous,
			ntiseptic_next;
	OperationDB operationDB; // 声明一个操作数据库对象
	ContentValues values = new ContentValues(); // 声明一个Contentvalues对象用于存储数据
	Boolean isSave = false; // 判断是否已经存储
	// Boolean update_or_save=true;
	Request request;
	// ApplicationApp app;
	SIMCardInfo simCardInfo; // 声明SIM卡信息对象
	String lineId, markId; // 定义管线ID、桩ID,用于存储和上报
	MyApplication myApplication; // 声明对象，用于获取手机信息
	// 随机生成码，唯一标识上报信息
	String guid = java.util.UUID.randomUUID().toString();
	// 定义并获取用户id
	String userid;
	Button back, main; // 声明标题栏的返回键和主页键
	CorrosiveHistoryData Corrosive_data; // 声明历史记录数据
	LinearLayout corrosive_soil_layout, damage_des_layout, corrosion_des_layout,
			text_remarks_layout;
	Tools tools = new Tools();
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String currenttime = sDateFormat.format(new java.util.Date());
	String[] currenttime2 = currenttime.split(" ");
	String linename = "";
	// 消息队列中的消息由当前线程内部进行处理
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			tools.stopProgressDialog(Corrosive.this);
			switch (msg.arg1) {
			case 2:
				String str1 = msg.getData().getString("result");
				if (str1.equals("OK|TRUE")) {
					finish();
				} else {
					finish();
				}
				break;
			case 10:
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
						intent.setClass(Corrosive.this, LineInfo.class);
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
		setContentView(R.layout.corrosive); // 获取界面
		c = Calendar.getInstance(); // 实例化年月日
		operationDB = new OperationDB(this); // /实例化数据库操作
		// app=(ApplicationApp) getApplicationContext();
		simCardInfo = new SIMCardInfo(this);
		request = new Request(handler);
		myApplication = new MyApplication(this);
		userid = myApplication.userid;
		initPaneList(); // 显示侧滑栏
		Corrosive_data = (CorrosiveHistoryData) getIntent().getSerializableExtra("values");
		init();
		if (Corrosive_data != null) {
			String upgrate = getIntent().getStringExtra("flag");
			// 升级
			if ("upgrate".equals(upgrate)) {
				guid = Corrosive_data.getGuid();
				text_line.setText(Corrosive_data.getLine());
				text_pile.setText(Corrosive_data.getPile());
				text_offset.setText(Corrosive_data.getOffset());
				text_repair_obj.setText(Corrosive_data.getRepair_obj());
				String[] check_time= Corrosive_data.getCheck_date().split(" ");
				text_check_date.setText(check_time[0]);
				String str = Corrosive_data.getClockposition();
				int one = str.lastIndexOf(",");
				if(one>1){
				String c1 = Corrosive_data.getClockposition().substring(0, (one - 1));
				text_clockposition1.setText(c1);
				String c2 = Corrosive_data.getClockposition().substring((one + 1), str.length());
				text_clockposition2.setText(c2);
				}
				text_soil.setText(Corrosive_data.getSoil());
				text_damage_des.setText(Corrosive_data.getDamage_des());
				text_area.setText(Corrosive_data.getArea());
				text_corrosion_des.setText(Corrosive_data.getCorrosion_des());
				text_corrosion_area.setText(Corrosive_data.getCorrosion_area());
				text_corrosion_num.setText(Corrosive_data.getCorrosion_num());
				text_pitdepthmax.setText(Corrosive_data.getPitdepthmax());
				text_pitdepthmin.setText(Corrosive_data.getPitdepthmin());
				text_repair_situation.setText(Corrosive_data.getRepair_situation());
				String[] check_time2= Corrosive_data.getRepair_date().split(" ");
				text_repair_date.setText(check_time2[0]);
				text_damage_type.setText(Corrosive_data.getDamage_type());
				text_repair_type.setText(Corrosive_data.getRepair_type());
				text_pile_info.setText(Corrosive_data.getPile_info());
				text_remarks.setText(Corrosive_data.getRemarks());
				lineId = Corrosive_data.getLineid();
				markId = Corrosive_data.getPileid();
				position = Tools.pileIndext(Corrosive.this, lineId, markId);
			}

		}

	}

	/**
	 * 导航栏方法
	 */
	private void initPaneList() {
		// 获取侧滑栏List
		panelList = (ListView) findViewById(R.id.corrosive_panel_list);
		panelListAdapter = new PanelListAdapter(this, image, imageTitle, 0);
		panelList.setAdapter(panelListAdapter);
		panelList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				switch (arg2) {
				case 0:
					intent.setClass(Corrosive.this, Problem.class);
					break;
				case 1:
					intent.setClass(Corrosive.this, Protect.class);
					break;
				case 2:
					intent.setClass(Corrosive.this, Natural.class);
					break;
				case 3:
					intent.setClass(Corrosive.this, Ground.class);
					break;
				case 4:
					intent.setClass(Corrosive.this, Corrosive.class);
					break;
				}
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
	}

	/**
	 * 暂时保存数据方法
	 */
	private void tempsave() {

		values.put("guid", guid);
		values.put("lineid", lineId);
		values.put("pileid", markId);

		String userid = myApplication.userid;

		values.put("userid", userid);

		if ((text_line != null)) {
			values.put("line", text_line.getText().toString());
		} else {
			values.put("line", 0);
		}

		if ((text_pile != null)) {

			values.put("pile", text_pile.getText().toString());
		} else {
			values.put("pile", 0);
		}

		if ((text_damage_type != null)) {
			values.put("damage_type", text_damage_type.getText().toString());
		} else {
			values.put("damage_type", 0);
		}

		if ((text_repair_type != null)) {
			values.put("repair_type", text_repair_type.getText().toString());
		} else {
			values.put("repair_type", 0);
		}

		if ((text_damage_des != null)) {
			// 若未填写信息，则填入空
			if (text_damage_des.getText().toString().equals("破损外观描述：")
					|| text_damage_des.getText().toString().equals("破损外观描述：未填写")) {
				values.put("damage_des", "");
			} else {
				int size = text_damage_des.getText().length();
				// values.put("damage_des", text_damage_des.getText().toString()
				// .substring(7, size));
				values.put("damage_des", text_damage_des.getText().toString());
			}
		} else {
			values.put("damage_des", 0);
		}

		if ((text_offset != null)) {
			if (text_offset.getText().toString().equals("未填写")
					|| text_offset.getText().toString().equals("填写偏移量")) {
				values.put("offset", "0");
			} else {
				values.put("offset", text_offset.getText().toString());
			}
		} else {
			values.put("offset", 0);
		}

		if ((text_soil != null)) {
			if (text_soil.getText().toString().equals("漏点土壤环境描述：")
					|| text_soil.getText().toString().equals("漏点土壤环境描述：未填写")) {
				values.put("soil", "");
			} else {
				int size = text_soil.getText().length();
				// values.put("soil",text_soil.getText().toString().substring(9,
				// size));
				values.put("soil", text_soil.getText().toString());
			}
		} else {
			values.put("soil", 0);
		}

		if ((text_area != null)) {
			if (text_area.getText().toString().equals("未填写")
					|| text_area.getText().toString().equals("填写破损面积")) {
				values.put("area", "");
			} else {
				values.put("area", text_area.getText().toString());
			}
		} else {
			values.put("area", 0);
		}

		if ((text_repair_obj != null)) {
			if (text_repair_obj.getText().toString().equals("未填写")
					|| text_repair_obj.getText().toString().equals("填写修复对象")) {
				values.put("repair_obj", "");
			} else {
				values.put("repair_obj", text_repair_obj.getText().toString());
			}
		} else {
			values.put("repair_obj", 0);
		}
		if ((text_check_date != null)) {
			if (text_check_date.getText().toString().equals("填写检漏日期")) {
				values.put("check_date", "");
			} else {
				values.put("check_date", text_check_date.getText().toString() + " " + currenttime2[1]);
			}
		} else {
			values.put("check_date", 0);
		}

		if (text_clockposition1 != null || text_clockposition2 != null) {
			if (text_clockposition1.getText().toString().equals("起始")
					|| text_clockposition2.getText().toString().equals("终止")) {
				values.put("clockposition", "");
			} else {
				values.put("clockposition", text_clockposition1.getText().toString() + ","
						+ text_clockposition2.getText().toString());
			}
		} else {
			values.put("clockposition", 0);
		}

		if ((text_pitdepthmax != null)) {
			if (text_pitdepthmax.getText().toString().equals("未填写")
					|| text_pitdepthmax.getText().toString().equals("起始")) {
				values.put("pitdepthmax", "");
			} else {
				values.put("pitdepthmax", text_pitdepthmax.getText().toString());
			}
		} else {
			values.put("pitdepthmax", 0);
		}
		if ((text_pitdepthmin != null)) {
			if (text_pitdepthmin.getText().toString().equals("未填写")
					|| text_pitdepthmin.getText().toString().equals("终止")) {
				values.put("pitdepthmin", "");
			} else {
				values.put("pitdepthmin", text_pitdepthmin.getText().toString());
			}
		} else {
			values.put("pitdepthmin", 0);
		}

		if ((text_repair_date != null)) {
			if (text_repair_date.getText().toString().equals("填写补漏日期")) {
				values.put("repair_date", "填写补漏日期");
			} else {
				values.put("repair_date", text_repair_date.getText().toString() + " " + currenttime2[1]);
			}
		} else {
			values.put("repair_date", 0);
		}

		if ((text_corrosion_des != null)) {
			if (text_corrosion_des.getText().toString().equals("金属腐蚀表面描述：")
					|| text_corrosion_des.getText().toString().equals("金属腐蚀表面描述：未填写")) {
				values.put("corrosion_des", "");
			} else {
				int size = text_corrosion_des.getText().length();
				// values.put("corrosion_des",
				// text_corrosion_des.getText().toString().substring(9, size));
				values.put("corrosion_des", text_corrosion_des.getText().toString());
			}
		} else {
			values.put("corrosion_des", 0);
		}

		if ((text_corrosion_area != null)) {
			if (text_corrosion_area.getText().toString().equals("未填写")
					|| text_corrosion_area.getText().toString().equals("填写腐蚀面积")) {
				values.put("corrosion_area", "");
			} else {
				values.put("corrosion_area", text_corrosion_area.getText().toString());
			}
		} else {
			values.put("corrosion_area", 0);
		}

		if ((text_corrosion_num != null)) {
			if (text_corrosion_num.getText().toString().equals("未填写")
					|| text_corrosion_num.getText().toString().equals("填写腐蚀个数")) {
				values.put("corrosion_num", "");
			} else {
				values.put("corrosion_num", text_corrosion_num.getText().toString());
			}
		} else {
			values.put("corrosion_num", 0);
		}
		if ((text_repair_situation != null)) {
			if (text_repair_situation.getText().toString().equals("未填写")
					|| text_repair_situation.getText().toString().equals("填写处理情况")) {
				values.put("repair_situation", "");
			} else {
				values.put("repair_situation", text_repair_situation.getText().toString());
			}
		} else {
			values.put("repair_situation", 0);
		}

		if ((text_pile_info != null)) {
			if (text_pile_info.getText().toString().equals("未填写")
					|| text_pile_info.getText().toString().equals("填写修复信息")) {
				values.put("pile_info", "");
			} else {
				values.put("pile_info", text_pile_info.getText().toString());
			}
		} else {
			values.put("pile_info", 0);
		}

		if ((text_remarks != null)) {
			if (text_remarks.getText().toString().equals("备注：")
					|| text_remarks.getText().toString().equals("备注：未填写")) {
				values.put("remarks", "");
			} else {
				int size = text_remarks.getText().length();
				// values.put("remarks",
				// text_remarks.getText().toString().substring(3, size));
				values.put("remarks", text_remarks.getText().toString());
			}
		} else {
			values.put("remarks", 0);
		}
		// 将数据赋予中间变量，以备上报
		str_line = String.valueOf(values.get("line"));
		str_pile = String.valueOf(values.get("pile"));
		str_offset = String.valueOf(values.get("offset"));
		str_clockposition = String.valueOf(values.get("clockposition"));
		str_clockposition1 = text_clockposition1.getText().toString();
		str_clockposition2 = text_clockposition2.getText().toString();
		str_soil = String.valueOf(values.get("soil"));
		str_damage_des = String.valueOf(values.get("damage_des"));
		str_damage_type = String.valueOf(values.get("damage_type"));
		str_repair_type = String.valueOf(values.get("repair_type"));
		str_area = String.valueOf(values.get("area"));
		str_corrosion_des = String.valueOf(values.get("corrosion_des"));
		str_corrosion_area = String.valueOf(values.get("corrosion_area"));
		str_corrosion_num = String.valueOf(values.get("corrosion_num"));
		str_pitdepthmax = String.valueOf(values.get("pitdepthmax"));// arg15
																	// 腐蚀深度范围(起始)
		str_pitdepthmin = String.valueOf(values.get("pitdepthmin"));// arg16
																	// 腐蚀深度范围(终止)
		str_repair_obj = String.valueOf(values.get("repair_obj"));
		str_check_date = String.valueOf(values.get("check_date"));
		str_repair_situation = String.valueOf(values.get("repair_situation"));
		str_repair_date = String.valueOf(values.get("repair_date"));
		str_pile_info = String.valueOf(values.get("pile_info"));
		str_remarks = String.valueOf(values.get("remarks"));
	}

	/**
	 * 将数据存入数据库
	 * 
	 * @param flag
	 */
	private void save(int flag, Boolean isToast) {
		values.put("isupload", flag);
		// 插入或更新数据
		operationDB.InsertOrUpdate(values, Type.CORROSIVE);
		if (isToast) {
			Toast.makeText(Corrosive.this, "保存成功", 1000).show();
		}
		fresh();
	}

	private void fresh(){
		guid = java.util.UUID.randomUUID().toString();
		
		if (text_damage_des != null) {
			// text_damage_des.setText("破损外观描述：");
			text_damage_des.setText("");
			text_damage_des.setTextColor(Color.BLACK);
		}

		if (text_offset != null) {
			text_offset.setText("填写偏移量");
			text_offset.setTextColor(Color.BLACK);
		}

		if (text_soil != null) {
			// text_soil.setText("漏点土壤环境描述：");
			text_soil.setText("");
			text_soil.setTextColor(Color.BLACK);
		}

		if (text_area != null) {
			text_area.setText("填写破损面积");
			text_area.setTextColor(Color.BLACK);
		}

		if (text_repair_obj != null) {
			text_repair_obj.setText("填写修复对象");
			text_repair_obj.setTextColor(Color.BLACK);
		}

		if (text_clockposition1 != null) {
			text_clockposition1.setText("起始");
			text_clockposition1.setTextColor(Color.BLACK);
		}

		if (text_clockposition2 != null) {
			text_clockposition2.setText("终止");
			text_clockposition2.setTextColor(Color.BLACK);
		}

		if (text_pitdepthmax != null) {
			text_pitdepthmax.setText("起始");
			text_pitdepthmax.setTextColor(Color.BLACK);
		}

		if (text_pitdepthmin != null) {
			text_pitdepthmin.setText("终止");
			text_pitdepthmin.setTextColor(Color.BLACK);
		}

		if (text_corrosion_des != null) {
			// text_corrosion_des.setText("金属腐蚀表面描述：");
			text_corrosion_des.setText("");
			text_corrosion_des.setTextColor(Color.BLACK);
		}

		if (text_corrosion_area != null) {
			text_corrosion_area.setText("填写腐蚀面积");
			text_corrosion_area.setTextColor(Color.BLACK);
		}

		if (text_corrosion_num != null) {
			text_corrosion_num.setText("填写腐蚀个数");
			text_corrosion_num.setTextColor(Color.BLACK);
		}

		if (text_repair_situation != null) {
			text_repair_situation.setText("填写处理情况");
			text_repair_situation.setTextColor(Color.BLACK);
		}

		if (text_pile_info != null) {
			text_pile_info.setText("填写修复信息");
			text_pile_info.setTextColor(Color.BLACK);
		}

		if (text_remarks != null) {
			text_remarks.setText("");
			// text_remarks.setText("备注：");
			text_remarks.setTextColor(Color.BLACK);
		}
	}
	
	/**
	 * 清空界面数据
	 */
	private void empty() {
		guid = java.util.UUID.randomUUID().toString();
		if (text_line != null) {
			text_line.setText("选择线路");
		}

		if (text_pile != null) {
			text_pile.setText("选择桩号");
		}

		if (text_damage_type != null) {
			text_damage_type.setText("选择破损类型");
		}

		if (text_repair_type != null) {
			text_repair_type.setText("选择修复类型");
		}

		if (text_damage_des != null) {
			// text_damage_des.setText("破损外观描述：");
			text_damage_des.setText("");
			text_damage_des.setTextColor(Color.BLACK);
		}

		if (text_offset != null) {
			text_offset.setText("填写偏移量");
			text_offset.setTextColor(Color.BLACK);
		}

		if (text_soil != null) {
			// text_soil.setText("漏点土壤环境描述：");
			text_soil.setText("");
			text_soil.setTextColor(Color.BLACK);
		}

		if (text_area != null) {
			text_area.setText("填写破损面积");
			text_area.setTextColor(Color.BLACK);
		}

		if (text_repair_obj != null) {
			text_repair_obj.setText("填写修复对象");
			text_repair_obj.setTextColor(Color.BLACK);
		}

		if (text_check_date != null) {
			text_check_date.setText("填写检漏日期");
			text_check_date.setTextColor(Color.BLACK);
		}

		if (text_clockposition1 != null) {
			text_clockposition1.setText("起始");
			text_clockposition1.setTextColor(Color.BLACK);
		}

		if (text_clockposition2 != null) {
			text_clockposition2.setText("终止");
			text_clockposition2.setTextColor(Color.BLACK);
		}

		if (text_pitdepthmax != null) {
			text_pitdepthmax.setText("起始");
			text_pitdepthmax.setTextColor(Color.BLACK);
		}

		if (text_pitdepthmin != null) {
			text_pitdepthmin.setText("终止");
			text_pitdepthmin.setTextColor(Color.BLACK);
		}

		if (text_repair_date != null) {
			text_repair_date.setText("填写补漏日期");
			text_repair_date.setTextColor(Color.BLACK);
		}

		if (text_corrosion_des != null) {
			// text_corrosion_des.setText("金属腐蚀表面描述：");
			text_corrosion_des.setText("");
			text_corrosion_des.setTextColor(Color.BLACK);
		}

		if (text_corrosion_area != null) {
			text_corrosion_area.setText("填写腐蚀面积");
			text_corrosion_area.setTextColor(Color.BLACK);
		}

		if (text_corrosion_num != null) {
			text_corrosion_num.setText("填写腐蚀个数");
			text_corrosion_num.setTextColor(Color.BLACK);
		}

		if (text_repair_situation != null) {
			text_repair_situation.setText("填写处理情况");
			text_repair_situation.setTextColor(Color.BLACK);
		}

		if (text_pile_info != null) {
			text_pile_info.setText("填写修复信息");
			text_pile_info.setTextColor(Color.BLACK);
		}

		if (text_remarks != null) {
			text_remarks.setText("");
			// text_remarks.setText("备注：");
			text_remarks.setTextColor(Color.BLACK);
		}

		Toast.makeText(Corrosive.this, "重置完成", 1000).show();

	}

	/**
	 * 初始化界面
	 */
	private void init() {
		// TODO Auto-generated method stub
		// 获取界面上保存上报等按钮
		ntiseptic_new = (MyImageButton) findViewById(R.id.bnew_corrosive);
		ntiseptic_save = (MyImageButton) findViewById(R.id.bsave_corrosive);
		ntiseptic_upload = (MyImageButton) findViewById(R.id.bupload_corrosive);
		ntiseptic_previous = (MyImageButton) findViewById(R.id.bprevious_corrosive);
		ntiseptic_next = (MyImageButton) findViewById(R.id.bnext_corrosive);
		ntiseptic_new.setTag(1);
		ntiseptic_save.setTag(2);
		ntiseptic_upload.setTag(3);
		ntiseptic_previous.setTag(4);
		ntiseptic_next.setTag(5);
		// 设置点击事件
		ntiseptic_new.setOnClickListener(this);
		ntiseptic_save.setOnClickListener(this);
		ntiseptic_upload.setOnClickListener(this);
		ntiseptic_previous.setOnClickListener(this);
		ntiseptic_next.setOnClickListener(this);
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		back.setTag(40);
		main.setTag(41);
		back.setOnClickListener(this);
		main.setOnClickListener(this);
		// 获取布局
		check_date = (RelativeLayout) findViewById(R.id.corrosive_check_date);
		offset = (RelativeLayout) findViewById(R.id.corrosive_offset);
		area = (RelativeLayout) findViewById(R.id.corrosive_area);
		corrosion_area = (RelativeLayout) findViewById(R.id.corrosion_area);
		corrosion_num = (RelativeLayout) findViewById(R.id.corrosive_corrosion_num);
		repair_obj = (RelativeLayout) findViewById(R.id.corrosive_repair_obj);
		repair_situation = (RelativeLayout) findViewById(R.id.corrosive_repair_situation);
		repair_date = (RelativeLayout) findViewById(R.id.corrosive_repair_date);
		pile_info = (RelativeLayout) findViewById(R.id.corrosive_pile_info);
		clockposition1 = (RelativeLayout) findViewById(R.id.corrosive_clockposition1);
		clockposition2 = (RelativeLayout) findViewById(R.id.corrosive_clockposition2);
		pitdepthmax = (RelativeLayout) findViewById(R.id.corrosive_pitdepthmax);
		pitdepthmin = (RelativeLayout) findViewById(R.id.corrosive_pitdepthmin);

		line = (RelativeLayout) findViewById(R.id.corrosive_line);
		pile = (RelativeLayout) findViewById(R.id.corrosive_pile);
		damage_type = (RelativeLayout) findViewById(R.id.corrosive_damage_type);
		repair_type = (RelativeLayout) findViewById(R.id.corrosive_repair_type);

		text_check_date = (TextView) findViewById(R.id.text_check_date);
		text_offset = (TextView) findViewById(R.id.text_offset);
		text_soil = (TextView) findViewById(R.id.text_soil);
		text_damage_des = (TextView) findViewById(R.id.text_damage_des);
		text_area = (TextView) findViewById(R.id.text_area);
		text_corrosion_des = (TextView) findViewById(R.id.text_corrosion_des);
		text_corrosion_area = (TextView) findViewById(R.id.text_corrosion_area);
		text_corrosion_num = (TextView) findViewById(R.id.text_corrosion_num);
		text_repair_obj = (TextView) findViewById(R.id.text_repair_obj);
		text_repair_situation = (TextView) findViewById(R.id.text_repair_situation);
		text_repair_date = (TextView) findViewById(R.id.text_repair_date);
		text_pile_info = (TextView) findViewById(R.id.text_pile_info);
		text_remarks = (TextView) findViewById(R.id.text_remarks);
		text_clockposition1 = (TextView) findViewById(R.id.text_clockposition1);
		text_clockposition2 = (TextView) findViewById(R.id.text_clockposition2);
		text_pitdepthmax = (TextView) findViewById(R.id.text_pitdepthmax);
		text_pitdepthmin = (TextView) findViewById(R.id.text_pitdepthmin);

		text_line = (TextView) findViewById(R.id.text_corrosive_line);
		text_pile = (TextView) findViewById(R.id.text_corrosive_pile);
		text_damage_type = (TextView) findViewById(R.id.text_corrosive_damage_type);
		text_repair_type = (TextView) findViewById(R.id.text_corrosive_repair_type);

		corrosive_soil_layout = (LinearLayout) findViewById(R.id.corrosive_soil_layout);
		damage_des_layout = (LinearLayout) findViewById(R.id.damage_des_layout);
		corrosion_des_layout = (LinearLayout) findViewById(R.id.corrosion_des_layout);
		text_remarks_layout = (LinearLayout) findViewById(R.id.text_remarks_layout);

		if(currenttime.length() == 19){
			text_check_date.setText(currenttime.substring(0, 10));
			}
		
		check_date.setTag(10);
		offset.setTag(11);
		text_soil.setTag(12);
		text_damage_des.setTag(13);
		area.setTag(14);
		text_corrosion_des.setTag(15);
		corrosion_area.setTag(16);
		corrosion_num.setTag(17);
		repair_obj.setTag(18);
		repair_situation.setTag(19);
		repair_date.setTag(20);
		pile_info.setTag(21);
		text_remarks.setTag(22);
		// clockposition.setTag(23);
		clockposition1.setTag(24);
		clockposition2.setTag(25);
		pitdepthmax.setTag(26);
		pitdepthmin.setTag(27);

		line.setTag(28);
		pile.setTag(29);
		damage_type.setTag(30);
		repair_type.setTag(31);

		check_date.setOnClickListener(this);
		offset.setOnClickListener(this);
		// clockposition.setOnClickListener(this);
		text_soil.setOnClickListener(this);
		text_damage_des.setOnClickListener(this);
		area.setOnClickListener(this);
		text_corrosion_des.setOnClickListener(this);
		corrosion_area.setOnClickListener(this);
		corrosion_num.setOnClickListener(this);
		repair_obj.setOnClickListener(this);
		repair_situation.setOnClickListener(this);
		repair_date.setOnClickListener(this);
		pile_info.setOnClickListener(this);
		text_remarks.setOnClickListener(this);
		clockposition1.setOnClickListener(this);
		clockposition2.setOnClickListener(this);
		pitdepthmax.setOnClickListener(this);
		pitdepthmin.setOnClickListener(this);
		line.setOnClickListener(this);
		pile.setOnClickListener(this);
		damage_type.setOnClickListener(this);
		repair_type.setOnClickListener(this);

//		corrosive_soil_layout.setTag(12);
//		damage_des_layout.setTag(13);
//		corrosion_des_layout.setTag(15);
//		text_remarks_layout.setTag(22);
//		corrosive_soil_layout.setOnClickListener(this);
//		damage_des_layout.setOnClickListener(this);
//		corrosion_des_layout.setOnClickListener(this);
//		text_remarks_layout.setOnClickListener(this);
	}

	/**
	 * 实现点击事件
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = (Integer) v.getTag();
		Intent intent = new Intent();
		intent.setClass(Corrosive.this, DialogEditActivity.class);
		switch (tag) {
		case 40:
			this.finish();
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case 41:
			Tools.backMain(this);
			break;
		case 1:// 重置
			if (!isSave) {
				// 点击重置若未保存过
				AlertDialog.Builder builder = new Builder(Corrosive.this);
				builder.setTitle("提示");
				builder.setMessage("是否保存信息?");
				builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						tempsave();
						if (str_check_date.equals("填写检漏日期") || str_line.equals("选择线路")
								|| str_pile.equals("选择桩号") || str_damage_type.equals("选择类型")
								|| str_repair_type.equals("选择类型")
								|| str_repair_date.equals("填写补漏日期")
								|| str_repair_date.equals("未填写")) {
							Toast.makeText(Corrosive.this, "带*的为必填项，请填写完再试！", 300).show();
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
			if (str_check_date.equals("填写检漏日期") || str_line.equals("选择线路")
					|| str_pile.equals("选择桩号") || str_damage_type.equals("选择类型")
					|| str_repair_type.equals("选择类型") || str_repair_date.trim().equals("填写补漏日期")
					|| str_repair_date.equals("未填写")) {
				Toast.makeText(Corrosive.this, "带*的为必填项，请填写完再试！", 300).show();
			} else {
				save(0, true);
			}
			break;
		case 3:// 上报
			tempsave();
			if (str_check_date.equals("填写检漏日期") || str_line.equals("选择线路")
					|| str_pile.equals("选择桩号") || str_damage_type.equals("选择类型")
					|| str_repair_type.equals("选择类型") || str_repair_date.trim().equals("填写补漏日期")
					|| str_repair_date.equals("未填写")) {
				Toast.makeText(Corrosive.this, "带*的为必填项，请填写完再试！", 300).show();
			} else {
				if (Tools.isNetworkAvailable(this, false)) {
					tools.startProgressDialog(Corrosive.this);
					request.ntisepticRequest(userid, lineId, markId, str_offset, str_repair_obj,
							str_check_date, str_clockposition, str_soil, str_damage_des, str_area,
							str_corrosion_des, str_corrosion_area, str_corrosion_num,
							str_pitdepthmax, str_pitdepthmin, str_repair_situation,
							str_repair_date, str_damage_type, str_repair_type, str_pile_info,
							str_remarks, "");
				} else {
					Toast.makeText(Corrosive.this, "无网络连接，上报失败", 500).show();
					save(0, true);
				}
			}

			break;
		case 4:// 上一桩
			if (position == -1) {

				Toast.makeText(Corrosive.this, "您还未选择桩号", 500).show();
				// if(text_pile!=null&&Pipe.listpile.size()!=0)
				// {
				// text_pile.setText(Pipe.listpile.get(0));
				// position=0;
				// }
			} else if (position == 0)
				Toast.makeText(Corrosive.this, "您已处于第一桩", 500).show();
			else {
				
				text_pile.setText(Pipe.listpile.get(position - 1));
				position = position - 1;
				// System.out.println("上一桩了了"+ position);
			}
			break;
		case 5:// 下一桩
			if (position == -1) {
				Toast.makeText(Corrosive.this, "您还未选择桩号", 500).show();
				// if(text_pile!=null&&Pipe.listpile.size()!=0)
				// {
				// text_pile.setText(Pipe.listpile.get(0));
				// position=0;
				// }
			} else if ((position == (Pipe.listpile.size() - 1))) {
				Toast.makeText(Corrosive.this, "您处于最后一桩", 500).show();
			} else {
				text_pile.setText(Pipe.listpile.get(position + 1));
				position = position + 1;
			}
			break;

		case 10: // 检测时间
			Tools.setDateDialog(this, c, text_check_date);
			break;
		case 11:
			System.out.println("bbbbbbbbbbbbb");
			intent.putExtra("flag", TypeQuest.CORROSIVE_OFFSET);
			intent.putExtra("Tittle", "偏移量");
			if (!"填写偏移量".equals(text_offset.getText().toString())) {
				intent.putExtra("text", text_offset.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.CORROSIVE_OFFSET);
			break;
		// case 12:
		// intent.putExtra("flag", TypeQuest.CORROSIVE_SOIL);
		// intent.putExtra("Tittle", "漏点土壤环境描述");
		// if (!"漏点土壤环境描述：".equals(text_soil.getText().toString())) {
		// intent.putExtra("text", text_soil.getText().toString());
		// }
		// startActivityForResult(intent, TypeQuest.CORROSIVE_SOIL);
		// break;
		// case 13:
		// intent.putExtra("flag", TypeQuest.CORROSIVE_DAMAGE_DES);
		// intent.putExtra("Tittle", "破损外观描述");
		// if (!"破损外观描述：".equals(text_damage_des.getText().toString())) {
		// intent.putExtra("text", text_damage_des.getText().toString());
		// }
		// startActivityForResult(intent, TypeQuest.CORROSIVE_DAMAGE_DES);
		// break;
		case 14:
			intent.putExtra("flag", TypeQuest.CORROSIVE_AREA);
			intent.putExtra("Tittle", "破损面积(㎡)");
			if (!"填写破损面积".equals(text_area.getText().toString())) {
				intent.putExtra("text", text_area.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.CORROSIVE_AREA);
			break;
		// case 15:
		// intent.putExtra("flag", TypeQuest.CORROSIVE_CORROSION_DES);
		// intent.putExtra("Tittle", "金属腐蚀表面描述");
		// if (!"金属腐蚀表面描述：".equals(text_corrosion_des.getText().toString())) {
		// intent.putExtra("text", text_corrosion_des.getText().toString());
		// }
		// startActivityForResult(intent, TypeQuest.CORROSIVE_CORROSION_DES);
		// break;
		case 16:
			intent.putExtra("flag", TypeQuest.CORROSIVE_CORROSION_AREA);
			intent.putExtra("Tittle", "金属腐蚀面积(㎡)");
			if (!"填写腐蚀面积".equals(text_corrosion_area.getText().toString())) {
				intent.putExtra("text", text_corrosion_area.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.CORROSIVE_CORROSION_AREA);
			break;
		case 17:
			intent.putExtra("flag", TypeQuest.CORROSIVE_CORROSION_NUM);
			intent.putExtra("Tittle", "金属腐蚀个数");
			if (!"填写腐蚀个数".equals(text_corrosion_num.getText().toString())) {
				intent.putExtra("text", text_corrosion_num.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.CORROSIVE_CORROSION_NUM);
			break;
		case 18:
			intent.putExtra("flag", TypeQuest.CORROSIVE_REPAIR_OBJ);
			intent.putExtra("Tittle", "修复对象");
			if (!"填写修复对象".equals(text_repair_obj.getText().toString())) {
				intent.putExtra("text", text_repair_obj.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.CORROSIVE_REPAIR_OBJ);
			break;
		case 19:
			intent.putExtra("flag", TypeQuest.CORROSIVE_REPAIR_SITUATION);
			intent.putExtra("Tittle", "防腐层修补情况");
			if (!"填写处理情况".equals(text_repair_situation.getText().toString())) {
				intent.putExtra("text", text_repair_situation.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.CORROSIVE_REPAIR_SITUATION);
			break;
		case 20:
			Tools.setDateDialog(this, c, text_repair_date);
			break;
		case 21:
			intent.putExtra("flag", TypeQuest.CORROSIVE_PILE_INFO);
			intent.putExtra("Tittle", "管道修复信息");
			if (!"填写修复信息".equals(text_pile_info.getText().toString())) {
				intent.putExtra("text", text_pile_info.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.CORROSIVE_PILE_INFO);
			break;
		// case 22:
		// intent.putExtra("flag", TypeQuest.CORROSIVE_REMARKS);
		// intent.putExtra("Tittle", "备注");
		// if (!"备注：".equals(text_remarks.getText().toString())) {
		// intent.putExtra("text", text_remarks.getText().toString());
		// }
		// startActivityForResult(intent, TypeQuest.CORROSIVE_REMARKS);
		// break;
		// case 23:
		// break;
		case 24:
			Tools.setTimeDialog(this, c, text_clockposition1);
			break;
		case 25:
			Tools.setTimeDialog(this, c, text_clockposition2);
			break;
		case 26:
			intent.putExtra("flag", TypeQuest.CORROSIVE_PITDEPTHMAX);
			intent.putExtra("Tittle", "腐蚀深度起始");
			if (!"起始".equals(text_pitdepthmax.getText().toString())) {
				intent.putExtra("text", text_pitdepthmax.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.CORROSIVE_PITDEPTHMAX);
			break;
		case 27:
			intent.putExtra("flag", TypeQuest.CORROSIVE_PITDEPTHMIN);
			intent.putExtra("Tittle", "腐蚀深度终止");
			if (!"终止".equals(text_pitdepthmin.getText().toString())) {
				intent.putExtra("text", text_pitdepthmin.getText().toString());
			}
			startActivityForResult(intent, TypeQuest.CORROSIVE_PITDEPTHMIN);
			break;

		case 28:
			intent.setClass(Corrosive.this, TreeView.class);
			intent.putExtra("flag", TypeQuest.CORROSIVE_LINE);
			startActivityForResult(intent, TypeQuest.CORROSIVE_LINE);
			break;

		case 29:
			if (lineId == null) {
				Toast.makeText(getApplicationContext(), "请先选择线路", 1000).show();
			} else {
				intent.setClass(Corrosive.this, DialogActivity.class);
				intent.putExtra("flag", TypeQuest.CORROSIVE_PILE);
				intent.putExtra("lineid", lineId);
				if(!(text_pile.getText().toString().equals("选择桩号"))){
					intent.putExtra("markName", text_pile.getText().toString());
				}
				startActivityForResult(intent, TypeQuest.CORROSIVE_PILE);
			}
			break;
		case 30:
			Intent intent2 = new Intent();
			intent2.setClass(Corrosive.this, DialogActivity.class);
			intent2.putExtra("flag", TypeQuest.CORROSIVE_DAMAGE_TYPE);
			startActivityForResult(intent2, TypeQuest.CORROSIVE_DAMAGE_TYPE);
			break;
		case 31:
			Intent intent3 = new Intent();
			intent3.setClass(Corrosive.this, DialogActivity.class);
			intent3.putExtra("flag", TypeQuest.CORROSIVE_REPAIR_TYPE);
			startActivityForResult(intent3, TypeQuest.CORROSIVE_REPAIR_TYPE);
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
					tools.startProgressDialog(this);
				}
			}
			else{
				Toast.makeText(Corrosive.this, "请选择线路后查看", 1000).show();
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
			intent.setClass(Corrosive.this, CorrosiveHistory.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 返回结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TypeQuest.CORROSIVE_LINE:
			if (resultCode == Activity.RESULT_OK) {
				text_line.setText(data.getStringExtra("line"));
				lineId = data.getStringExtra("lineId");
				text_pile.setText("选择桩号");
			}
			break;
		case TypeQuest.CORROSIVE_PILE:
			if (resultCode == Activity.RESULT_OK) {
				text_pile.setText(data.getStringExtra("pile"));
				position = data.getIntExtra("po", -1);
				markId = data.getStringExtra("markId");
			}
			break;

		case TypeQuest.CORROSIVE_DAMAGE_TYPE:
			if (resultCode == Activity.RESULT_OK) {
				text_damage_type.setText(data.getStringExtra("damage_type"));
			}
			break;
		case TypeQuest.CORROSIVE_REPAIR_TYPE:
			if (resultCode == Activity.RESULT_OK) {
				text_repair_type.setText(data.getStringExtra("repair_type"));
			}
			break;

		case TypeQuest.CORROSIVE_DAMAGE_DES:
			if (resultCode == Activity.RESULT_OK) {

				if ("".equals(data.getStringExtra("damage_des"))) {
					text_damage_des.setTextColor(Color.RED);
					text_damage_des.setText("破损外观描述：未填写");

				} else {
					text_damage_des.setTextColor(Color.BLACK);
					text_damage_des.setText("破损外观描述：" + data.getStringExtra("damage_des"));
				}
			}
			break;
		case TypeQuest.CORROSIVE_OFFSET:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("offset"))) {
					text_offset.setTextColor(Color.RED);
					text_offset.setText("未填写");

				} else {
					text_offset.setTextColor(Color.BLACK);
					text_offset.setText(data.getStringExtra("offset"));
				}
			}
			break;
		case TypeQuest.CORROSIVE_SOIL:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("soil"))) {
					text_soil.setTextColor(Color.RED);
					text_soil.setText("漏点土壤环境描述：未填写");

				} else {
					text_soil.setTextColor(Color.BLACK);
					text_soil.setText("漏点土壤环境描述：" + data.getStringExtra("soil"));
				}
			}
			break;
		case TypeQuest.CORROSIVE_AREA:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("area"))) {
					text_area.setTextColor(Color.RED);
					text_area.setText("未填写");
				} else {
					text_area.setTextColor(Color.BLACK);
					text_area.setText(data.getStringExtra("area"));
				}
			}
			break;

		case TypeQuest.CORROSIVE_REPAIR_OBJ:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("repair_obj"))) {
					text_repair_obj.setTextColor(Color.RED);
					text_repair_obj.setText("未填写");

				} else {
					text_repair_obj.setTextColor(Color.BLACK);
					text_repair_obj.setText(data.getStringExtra("repair_obj"));
				}
			}
			break;
		case TypeQuest.CORROSIVE_CHECK_DATE:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("check_date"))) {
					text_check_date.setTextColor(Color.RED);
					text_check_date.setText("未填写");

				} else {
					// text_check_date.setText(data.getStringExtra("check_date"));
					text_check_date.setText((CharSequence) et);
				}
			}
			break;
		case TypeQuest.CORROSIVE_MILE_LOCATION:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("clockposition"))) {
					text_clockposition.setTextColor(Color.RED);
					text_clockposition.setText("未填写");

				} else {
					text_clockposition.setTextColor(Color.BLACK);
					text_clockposition.setText(data.getStringExtra("clockposition"));
				}
			}
			break;

		case TypeQuest.CORROSIVE_CLOCKPOSITION1:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("clockposition1"))) {
					text_clockposition1.setTextColor(Color.RED);
					text_clockposition1.setText("未填写");

				} else {
					text_clockposition1.setTextColor(Color.BLACK);
					text_clockposition1.setText(data.getStringExtra("clockposition1"));
				}
			}
			break;

		case TypeQuest.CORROSIVE_CLOCKPOSITION2:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("clockposition2"))) {
					text_clockposition2.setTextColor(Color.RED);
					text_clockposition2.setText("未填写");

				} else {
					text_clockposition2.setTextColor(Color.BLACK);
					text_clockposition2.setText(data.getStringExtra("clockposition2"));
				}
			}
			break;

		case TypeQuest.CORROSIVE_PITDEPTHMAX:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("pitdepthmax"))) {
					text_pitdepthmax.setTextColor(Color.RED);
					text_pitdepthmax.setText("未填写");

				} else {
					text_pitdepthmax.setTextColor(Color.BLACK);
					text_pitdepthmax.setText(data.getStringExtra("pitdepthmax"));
				}
			}
			break;

		case TypeQuest.CORROSIVE_PITDEPTHMIN:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("pitdepthmin"))) {
					text_pitdepthmin.setTextColor(Color.RED);
					text_pitdepthmin.setText("未填写");

				} else {
					text_pitdepthmin.setTextColor(Color.BLACK);
					text_pitdepthmin.setText(data.getStringExtra("pitdepthmin"));
				}
			}
			break;

		case TypeQuest.CORROSIVE_REPAIR_DATE:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("repair_date"))) {
					text_repair_date.setTextColor(Color.RED);
					text_repair_date.setText("未填写");

				} else {
					text_repair_date.setText((CharSequence) et);
				}
			}
			break;
		case TypeQuest.CORROSIVE_CORROSION_DES:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("corrosion_des"))) {
					text_corrosion_des.setTextColor(Color.RED);
					text_corrosion_des.setText("金属腐蚀表面描述：未填写");

				} else {
					text_corrosion_des.setTextColor(Color.BLACK);
					text_corrosion_des.setText("金属腐蚀表面描述：" + data.getStringExtra("corrosion_des"));

				}
			}
			break;
		case TypeQuest.CORROSIVE_CORROSION_AREA:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("corrosion_area"))) {
					text_corrosion_area.setTextColor(Color.RED);
					text_corrosion_area.setText("未填写");

				} else {
					text_corrosion_area.setTextColor(Color.BLACK);
					text_corrosion_area.setText(data.getStringExtra("corrosion_area"));
				}
			}
			break;
		case TypeQuest.CORROSIVE_CORROSION_NUM:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("corrosion_num"))) {
					text_corrosion_num.setTextColor(Color.RED);
					text_corrosion_num.setText("未填写");

				} else {
					text_corrosion_num.setTextColor(Color.BLACK);
					text_corrosion_num.setText(data.getStringExtra("corrosion_num"));
				}
			}
			break;

		// case TypeQuest.CORROSIVE_DAMAGE_DES:
		// if(resultCode==Activity.RESULT_OK)
		// {
		// if("".equals(data.getStringExtra("damage_des")))
		// {
		// text_damage_des.setTextColor(Color.RED);
		// text_damage_des.setText("未填写");
		//
		// }else
		// {
		// text_damage_des.setText(data.getStringExtra("appear_des"));
		// }
		// }
		// break;

		case TypeQuest.CORROSIVE_REPAIR_SITUATION:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("repair_situation"))) {
					text_repair_situation.setTextColor(Color.RED);
					text_repair_situation.setText("未填写");

				} else {
					text_repair_situation.setTextColor(Color.BLACK);
					text_repair_situation.setText(data.getStringExtra("repair_situation"));
				}
			}
			break;
		case TypeQuest.CORROSIVE_PILE_INFO:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("pile_info"))) {
					text_pile_info.setTextColor(Color.RED);
					text_pile_info.setText("未填写");

				} else {
					text_pile_info.setTextColor(Color.BLACK);
					text_pile_info.setText(data.getStringExtra("pile_info"));
				}
			}
			break;
		case TypeQuest.CORROSIVE_REMARKS:
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
