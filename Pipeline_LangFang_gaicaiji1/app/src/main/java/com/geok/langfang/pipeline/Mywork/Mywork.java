package com.geok.langfang.pipeline.Mywork;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.Freqinfo;
import com.geok.langfang.jsonbean.GpsHui;
import com.geok.langfang.jsonbean.GpsMarker;
import com.geok.langfang.jsonbean.Insdateinfo;
import com.geok.langfang.jsonbean.MyTaskQueryBean;
import com.geok.langfang.jsonbean.Taskinfo;
import com.geok.langfang.pipeline.ApplicationApp;
import com.geok.langfang.pipeline.Login;
import com.geok.langfang.pipeline.MainView;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.inspection.InspectionRecord;
import com.geok.langfang.pipeline.map.BaiduMap;
import com.geok.langfang.pipeline.problem.Problem;
import com.geok.langfang.pipeline.setting.Setting;
import com.geok.langfang.pipeline.statistics.StatisticsActivity;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.ListButtonAdapter;
import com.geok.langfang.tools.MyImageButton;
import com.geok.langfang.tools.PanelListAdapter;
import com.geok.langfang.tools.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static com.geok.langfang.tools.Tools.progressDialog;
import static java.lang.String.valueOf;

public class Mywork extends Activity implements OnClickListener {

	public static String pointlineID;
	public static double pointstation;
	public static double offsetpoor;
	private Vibrator vibrator;//手机震动

	Request request;// 联网操作类
	ListView panelList;// 左侧导航栏
	PanelListAdapter panelListAdapter;// 导航集合adapter
	int image[] = { R.drawable.menu_mywork_selector, R.drawable.menu_problem_selector,
			R.drawable.menu_plan_selector, R.drawable.menu_record_selector };// 左侧导航图标
	// 定义侧滑栏图片标题
	String[] imageTitle = { "我的工作", "事项上报", "巡检计划", "巡检日志" };
	ViewPager viewGroup;// 任务列表grop
	Button back, main;
	ImageView workList;// 我的任务历史列表按钮
	MyImageButton accept_button, start_button, pause_button;// 确定巡检，开始巡检，暂停巡检
	// GestureDetector mgestureDetector;
	ImageView[] imageCircle;// 点图片集合
	ImageView[] imageCircle1;// 点图片集合
	// TextView[] textButton;
	// View view,view1;
	int index = 0;// 表示哪条任务
	int index1 = 0;// 表示哪次巡线
	LinearLayout imageGroup;// 任务，点的布局
	LinearLayout imageGroup1;// 具体任务，点的布局
	// List<Map<String, Object>> list;
	// List<Pile> listpile;
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	List<MyTaskQueryBean> listTask = new ArrayList<MyTaskQueryBean>();// 全部的任务信息集合，有很多天
	MyTaskQueryBean todayTask = new MyTaskQueryBean();// 今天的任务信息
	ApplicationApp app;// 系统全局变量
	LayoutInflater inflater;

	int interval = 30;
	private ViewPager mPager;// 页卡内容
	List<View> listViews; // Tab页面列表
	List<View> listViews1; // Tab页面列表
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	// boolean isTiao = true;

	BMapManager mapManager;// 百度地图
	LocationManager lm;// 位置管理
//	LocationClient mLocClient;// 位置
	SharedPreferences spf;
	Editor editor;

	MyApplication myApplication;

	/*
	 * handler 用于更新界面，我的任务中任务与标识页面转动的图片进行联动
	 */
	public Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			initTaskList(true);
			pagerAdapter.notifyDataSetChanged();
			// mytask.invalidate();
			// mytask.notify();
		}
	};
	String date;
	String dateRequest;
	Timer timer = new Timer();
	MyTimerTask myTimerTask = new MyTimerTask();
	int l;
	public static Taskinfo taskinfo;

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			System.out.println("-----" + Thread.currentThread().getName());
			Message message = new Message();
			message.arg1 = l;
			l++;
			dateRequest = sf.format(new Date());
			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Toast.makeText(Mywork.this, "请开启GPS...", Toast.LENGTH_SHORT).show();
				myTimerTask.cancel();
				pause_button.setVisibility(View.GONE);
				start_button.setVisibility(View.VISIBLE);
				// 返回开启GPS导航设置界面
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivityForResult(intent, 0);
			} else {
				// Location locaion = getLocation();
				//
				// if(locaion!=null){
				// // Toast.makeText(Mywork.this, locaion.getLatitude()+"-----",
				// 1000).show();
				// double latitude = locaion.getLatitude();
				// double longitude = locaion.getLongitude();
				// if(Tools.isNetworkAvailable(Mywork.this, false)){
				// try{
				// request.GPSUploadRequest(app.getUserEventid(), app.getImei(),
				// String.valueOf(longitude),
				// String.valueOf(latitude), dateRequest, String.valueOf(l));
				// }catch(Exception e){
				// e.printStackTrace();
				// }
				//
				// }else{
				// }
				//
				// }
			}

		}
	}

	@SuppressLint("WrongConstant")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mywork1);
		//浮标跳转地图
		findViewById(R.id.hhhhh).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(Mywork.this, BaiduMap.class);
				startActivity(intent);
			}
		});
		//事件上报
		findViewById(R.id.shangbao).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(Mywork.this,Problem.class);
				startActivity(intent);
			}
		});

		myApplication = new MyApplication(this);
		spf = getSharedPreferences("pile", MODE_PRIVATE);
		editor = spf.edit();
		app = (ApplicationApp) getApplicationContext();

//		mLocClient = new LocationClient(this);

		// mgestureDetector=new GestureDetector(this);
		date = sf.format(new Date());
		inflater = LayoutInflater.from(this);
		request = new Request(handler);
		interval = Tools.getIntByStr(getSharedPreferences("save_password", MODE_PRIVATE).getString(
				"interval", "30"));
		viewGroup = (ViewPager) findViewById(R.id.viewgroup_mywork);
		// mytask = (ViewPager) findViewById(R.id.mytask);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		initData();
		for (int i = 0; i < listTask.size(); i++) {
			MyTaskQueryBean bean = listTask.get(i);

			Log.i("shujuData",date+"---------------"+bean.getINSDATE());
			if (date.equals(bean.getINSDATE())) {
				todayTask = bean;
			}
		}
		if (todayTask.getInsdateinfoList().size() == 0) {
			Toast.makeText(this, "暂时没有任务", 2000).show();
		}
		// Intent intent = getIntent();
		// index = intent.getIntExtra("index", 0);
		// isTiao = intent.getBooleanExtra("isTiao", true);

		getListTask();
		// 初始化任务信息布局，并展示任务信息列表
		initViewGroup();
		imageCircle = new ImageView[listViews.size()];
		TextView tittle_num = (TextView) findViewById(R.id.tittle_num);
		tittle_num.setText("我的任务(" + listViews.size() + ")");
		imageGroup = (LinearLayout) findViewById(R.id.image_group);
		imageGroup1 = (LinearLayout) findViewById(R.id.image_group1);
		// 初始化点布局
		initImage();
		// 初始化任务关键点信息列表
		initTaskList(false);
		// 初始化导航栏
		initPanelList();

		initButton();

		//直接开始巡检
		start_xunjian();
	}

	public void initData(){
		String workinfo = getSharedPreferences("sync", MODE_PRIVATE).getString("Data19", null);
		if(workinfo!=null){
			Login.listTask = Json.getMyTaskQueryList(workinfo);
			listTask = Login.listTask;
		}
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
            Tools.cancel(Mywork.this);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
		return super.onKeyDown(keyCode, event);

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.arg1) {
			case 19:
				String data19 = msg.getData().getString("result");
				if (data19.equals("-1")) {

				} else {
					listTask = Json.getMyTaskQueryList(data19);
					sendBroadcast(new Intent("listTask"));
				}
			}
		}

	};

	/*
	 * 判断我的任务得个数并做初始化
	 */

	private void initViewGroup() {
		listViews = new ArrayList<View>();
		listViews.clear();
		if (todayTask.getInsdateinfoList().size() > 0) {
			for (int i = 0; i < todayTask.getInsdateinfoList().size(); i++) {
				Insdateinfo insdateInfo = todayTask.getInsdateinfoList().get(i);
				View view = inflater.inflate(R.layout.pipeline_work, null);
				TextView work_tittle_text = (TextView) view.findViewById(R.id.work_tittle_text);
				work_tittle_text.setText(insdateInfo.getNAME());

				TextView work_time_text = (TextView) view.findViewById(R.id.work_time_text);
				work_time_text.setText(insdateInfo.getINSTACTTIME());

				TextView work_line_text = (TextView) view.findViewById(R.id.work_line_text);
				work_line_text.setText(insdateInfo.getTASKLOCATION());

				TextView work_fre_text = (TextView) view.findViewById(R.id.work_fre_text);
				work_fre_text.setText(insdateInfo.getFREQTEXT());
				view.setVisibility(View.VISIBLE);
				listViews.add(view);
			}
		}

		// viewGroup.setOnTouchListener(this);
		viewGroup.setAdapter(new MyPagerAdapter(listViews));
		// viewGroup.setOnPageChangeListener(new MyOnPageChangeListener(true));
		viewGroup.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				index = arg0;
				initImage();
				// initTaskList(true);
				myHandler.sendEmptyMessage(0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		// index=currIndex;
		viewGroup.setCurrentItem(index);
	}

	/**
	 * 初始化页面按钮
	 */
	private void initButton() {
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		back.setOnClickListener(this);
		main.setOnClickListener(this);

		workList = (ImageView) findViewById(R.id.workList);
		workList.setOnClickListener(this);
		workList.setVisibility(View.GONE);

		accept_button = (MyImageButton) findViewById(R.id.accept_button);

		accept_button.setOnClickListener(this);
		start_button = (MyImageButton) findViewById(R.id.start_button);
		start_button.setOnClickListener(this);
		pause_button = (MyImageButton) findViewById(R.id.pause_button);
		pause_button.setOnClickListener(this);

//		accept_button.setVisibility(View.GONE);
		start_button.setVisibility(View.GONE);
		pause_button.setVisibility(View.GONE);

		Button panelHandle = findViewById(R.id.panelHandle);
		panelHandle.setVisibility(View.GONE);


		if (todayTask.getInsdateinfoList().size() > 0) {
			accept_button.setClickable(true);
			start_button.setClickable(true);
			pause_button.setClickable(true);
		} else {
			accept_button.setClickable(false);
			start_button.setClickable(false);
			pause_button.setClickable(false);
		}

	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(locationListener);
	}
	/**
	 * 导航栏
	 */
	private void initPanelList() {
		panelList = (ListView) findViewById(R.id.mywork_panel_list);
		panelListAdapter = new PanelListAdapter(this, image, imageTitle, 0);
		panelList.setAdapter(panelListAdapter);

		panelList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
				switch (arg2) {
				case 0:
					intent.setClass(Mywork.this, Mywork.class);
					break;
				case 1:
					intent.setClass(Mywork.this, Problem.class);
					break;
				case 2:
					intent.setClass(Mywork.this, Plan.class);
					break;
				case 3:
					intent.setClass(Mywork.this, InspectionRecord.class);
					break;
				}
				startActivity(intent);

				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

	}

	int n;
	MyPagerAdapter pagerAdapter;// 关键点集合的adapter

	/*
	 * 处理任务列表，并初始化
	 */
	private void initTaskList(boolean isNotify) {
		// mytask = null;
		listViews1 = null;
		ViewPager mytask = (ViewPager) findViewById(R.id.mytask);// 初始化关键点列表布局
		mytask.removeAllViews();// 移除上次任务关键点列表view
		listViews1 = new ArrayList<View>();
		listViews1.clear();// 清空上次任务关键点列表信息
		MyPagerAdapter pagerAdapter1 = new MyPagerAdapter();
		// 如果数据需要更新，先清空mytask的adapter
		if (isNotify) {
			mytask.setAdapter(pagerAdapter1);
			mytask.invalidate();
			mytask.getAdapter().notifyDataSetChanged();
		} else {
			mytask.setAdapter(pagerAdapter);
			// pagerAdapter.notifyDataSetChanged();
		}
		// mytask.removeAllViews();
		if (todayTask.getInsdateinfoList().size() > 0) {
			final List<Taskinfo> taskInfoList = todayTask.getInsdateinfoList().get(index)
					.getTaskInfoList();

			for (int i = 0; i < taskInfoList.size(); i++) {
				View views = inflater.inflate(R.layout.task1, null);
				TextView work_tittle_text = (TextView) views.findViewById(R.id.task_index);
				work_tittle_text.setText("第" + (i + 1) + "次巡线");

				Log.i("taskInfoList",taskInfoList.get(i).getFREQINDEX());

				BaiduMap.freqinfos=taskInfoList.get(i).getInfoList();

				Log.i("shujurenwu",taskInfoList.get(i).getInfoList().get(i).getLAT());


				// 关键点信息列表
				ListView taskInfos = (ListView) views.findViewById(R.id.task_list1);
				ListButtonAdapter simpleAdapter = new ListButtonAdapter(this, taskInfoList.get(i)
						.getInfoList(), R.layout.mywork_listitem, todayTask.getInsdateinfoList()
						.get(index).getFREQTEXT(), todayTask.getInsdateinfoList().get(index)
						.getTASKID());
				//添加我的任务适配器
				taskInfos.setAdapter(simpleAdapter);


				Log.i("xxxxx",taskInfoList.get(i).getInfoList().size()+"-----------"+taskInfoList.get(i).getInfoList().get(i).getLAT()+"-----------------------"+taskInfoList.get(i).getInfoList().get(i).getLON()+"------"+taskInfoList.get(i).getInfoList().get(i).getIsXun());

				taskinfo = taskInfoList.get(i);
				// 点击某个关键点，弹出这个关键点的详细信息
				taskInfos.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						Dialog dia = new Dialog(Mywork.this, R.style.CustomActivityDialog);
						LayoutInflater inflater = LayoutInflater.from(Mywork.this);
						View view = inflater.inflate(R.layout.mywork_dialog, null);
						TextView texttittle = (TextView) view
								.findViewById(R.id.mywork_dialog_tittle);
						texttittle.setText(taskinfo.getInfoList().get(arg2)
								.getPOINTNAME());
//						TextView texttid = (TextView) view.findViewById(R.id.mywork_dialog_type);
//						texttid.setText("桩类型："
//								+ taskInfoList.get(n).getInfoList().get(arg2).getPOINTTYPE());
						TextView textdetial = (TextView) view
								.findViewById(R.id.mywork_dialog_distance);
						textdetial.setText("经度："
								+ taskinfo.getInfoList().get(arg2).getLON());

						TextView texttisusing = (TextView) view
								.findViewById(R.id.mywork_dialog_isUsing);
						texttisusing.setText("纬度："
								+ taskinfo.getInfoList().get(arg2).getLAT());
						// TextView
						// texttline=(TextView)view.findViewById(R.id.mywork_dialog_line);
						// texttline.setText("所属线路："+listpile.get(arg2).getLine());
						// TextView
						// textdetelephone=(TextView)view.findViewById(R.id.mywork_dialog_telephone);
						// textdetelephone.setText("是否有联系方式："+listpile.get(arg2).getTelephone());
						// TextView
						// textdate=(TextView)view.findViewById(R.id.mywork_dialog_useDate);
						// textdate.setText("投入使用日期："+listpile.get(arg2).getUserdate());
						dia.setContentView(view);
						dia.setCanceledOnTouchOutside(true);
						dia.show();
					}
				});
				listViews1.add(views);
			}
		}
		// 更新mytask
		pagerAdapter = new MyPagerAdapter(listViews1);

		if (isNotify) {
			pagerAdapter.notifyDataSetChanged();
		} else {
			mytask.setAdapter(pagerAdapter);
		}
		mytask.setAdapter(pagerAdapter);
		pagerAdapter.notifyDataSetChanged();

		mytask.getAdapter().notifyDataSetChanged();

		mytask.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				index1 = arg0;
				initImage1();

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		// 展示第一页的关键点信息列表
		mytask.setCurrentItem(0);
		imageCircle1 = new ImageView[listViews1.size()];
		initImage1();
		 pagerAdapter.notifyDataSetChanged();
		// myHandler.sendEmptyMessage(0);
		// mytask.setOnPageChangeListener(new MyOnPageChangeListener(false));
		// isTiao = true;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.back:
//			Tools.backMain(this);
			Intent intenthhhh=new Intent(Mywork.this,Login.class);
			startActivity(intenthhhh);
			finish();
			break;
		case R.id.main:
//			Tools.backMain(this);
			//跳转配置页面
			Intent intentSet=new Intent(Mywork.this,Setting.class);
			startActivity(intentSet);

			break;
		case R.id.workList:
			Intent intent = new Intent(Mywork.this, WorkHistory.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		// 点击确定任务按钮事件
		case R.id.accept_button:
			// 移除百度地图位置更新事件，停止百度地图
			// if(mLocationListener!=null){
			// mLocationManager.removeUpdates(mLocationListener);
			// mapManager.stop();
			// }
			
			new AlertDialog.Builder(Mywork.this).setTitle("提示").setMessage("请确认今天的任务是否全部完成")
			.setNegativeButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					lm.removeUpdates(locationListener);
		//			mLocClient.stop();
					// 将转换的上传关键点信息集合字符串保存在系统全局变量
					getSharedPreferences("save_password", MODE_PRIVATE).edit()
							.putString("info", getPlanStr()).commit();
					pagerAdapter.notifyDataSetChanged();
					int pileListsize = MyApplication.pileList.size();
					for (int k = 0; k < pileListsize; k++) {
                        request.MyTaskRequest(MyApplication.pileList.get(k).getEVENTID(),sdf.format(new Date()),valueOf(MyApplication.pileList.get(k).getLON()), valueOf(MyApplication.pileList.get(k).getLAT()),"");
						startProgressDialog(Mywork.this, "正在上报。。。。");
                    }
					Toast.makeText(Mywork.this, "上报完成", Toast.LENGTH_SHORT).show();


					// if(Tools.mapManager!=null){
					// Tools.mLocationManager.requestLocationUpdates(Tools.mLocationListener);
					// Tools.mapManager.stop();
					// }
//					getSharedPreferences("pile", MODE_PRIVATE).edit().clear().commit();// 清空本地文件里保存的上传关键点信息集合
					List<Freqinfo> pileList = new ArrayList<Freqinfo>();
					MyApplication.pileList = pileList;// 清空系统全局变量里保存的上传关键点信息集合
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
					
				}
			}).setPositiveButton("取消", null).show();
			
			// mytask.getAdapter().notifyDataSetChanged();
			break;
		// 开始按钮点击事件
		case R.id.start_button:
			Toast.makeText(this, "开始巡检", Toast.LENGTH_SHORT).show();
			pause_button.setVisibility(View.VISIBLE);
			start_button.setVisibility(View.GONE);
			// myTimerTask = new MyTimerTask();
			// timer.schedule(myTimerTask, 0, interval);
			if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Toast.makeText(Mywork.this, "请开启GPS...", Toast.LENGTH_SHORT).show();
				pause_button.setVisibility(View.GONE);
				start_button.setVisibility(View.VISIBLE);
				// 返回开启GPS导航设置界面
				Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivityForResult(intent1, 0);
			} else {
				getLocation();
			}
			break;
		// 暂停按钮点击事件
		case R.id.pause_button:
			pause_button.setVisibility(View.GONE);
			start_button.setVisibility(View.VISIBLE);
			// myTimerTask.cancel();
			// mLocationManager.removeUpdates(mLocationListener);
			// mapManager.stop();
//			mLocClient.stop();
			lm.removeUpdates(locationListener);
			break;

		}

	}
	public static void startProgressDialog(Context context, String message) {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(context).setMessage(message);
		}
		progressDialog.show();
	}

	/**
	 * 初始化点布局，展示第几条任务，第几个点就亮
	 */
	private void initImage() {
		imageGroup.removeAllViews();

		for (int i = 0; i < imageCircle.length; i++) {
			imageCircle[i] = new ImageView(this);
			// 展示第几条任务，第几个点就亮，其他的变暗
			if (i == index) {
				imageCircle[i].setImageDrawable(getResources().getDrawable(R.drawable.circle_o));
				imageGroup.addView(imageCircle[i]);
			} else {
				imageCircle[i].setImageDrawable(getResources().getDrawable(R.drawable.circle_l));
				imageGroup.addView(imageCircle[i]);
			}

		}
	}

	private void initImage1() {
		imageGroup1.removeAllViews();

		for (int i = 0; i < imageCircle1.length; i++) {
			imageCircle1[i] = new ImageView(this);
			// 展示第几条任务，第几个点就亮，其他的变暗
			if (i == index1) {
				imageCircle1[i].setImageDrawable(getResources().getDrawable(R.drawable.circle_o));
				imageGroup1.addView(imageCircle1[i]);
			} else {
				imageCircle1[i].setImageDrawable(getResources().getDrawable(R.drawable.circle_l));
				imageGroup1.addView(imageCircle1[i]);
			}

		}
	}


	private void getLocation() {
		
		 String SERVERNAME = Context.LOCATION_SERVICE;
		try {
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

			// String provider = locationManager.getBestProvider(criteria,
			// true); // 获取GPS信息

			lm.addGpsStatusListener(gpsListener);
			// 设置监听器，自动更新的最小时间为间隔(N*1000)秒或最小位移变化超过N米

			lm.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, interval * 1000, 10,
					locationListener);


//			locationManager.addGpsStatusListener(gpsstatusListener);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private final LocationListener locationListener = new LocationListener() {

		public void onLocationChanged(Location location) {
			if (location != null&& location.getLongitude() > 0) {

				final double lat = location.getLatitude();
				final double lon = location.getLongitude();
				Log.i("xxx","跑了");

				if (Tools.isNetworkAvailable(Mywork.this)) {
					try {
						boolean isTwo = false;
//						dateRequest = sf.format(new Date());
						SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String dateRequest = sDateFormat.format(new java.util.Date());
						// 上传gps位置信息
						request.GPSUploadRequest(myApplication.userid, myApplication.imei,
								valueOf(lon), valueOf(lat), dateRequest,
								valueOf(l));

						OperationDB operationDB=new OperationDB(Mywork.this);
						ContentValues contentValues=new ContentValues();
						contentValues.put("guid",java.util.UUID.randomUUID().toString());
						contentValues.put("userid",myApplication.userid);
						contentValues.put("lon", valueOf(lon));
						contentValues.put("lat", valueOf(lat));
						contentValues.put("date",dateRequest);
						contentValues.put("pointcount",1);
						contentValues.put("isupload",2);

						operationDB.DBinsert(contentValues, Type.GPS_UPLOCAD);

//						Cursor cursor = operationDB.DBselect(Type.GPS_UPLOCAD);
//						while (cursor.moveToNext()){
//							String date = cursor.getString(cursor.getColumnIndex("DATE"));
//								operationDB.DBdelete(contentValues,9,date);
//						}


						for (int i = 0; i < todayTask.getInsdateinfoList().size(); i++) {
							final List<Taskinfo> taskInfoList = todayTask.getInsdateinfoList()
									.get(i).getTaskInfoList();

							for (int j = 0; j < taskInfoList.size(); j++) {
								boolean isComplete = false;//false为这一频次巡检完成，true为未完成
								if(j>0){
								  for(int r = 0; r < taskInfoList.get(j-1).getInfoList().size(); r++){
									if(!taskInfoList.get(j-1).getInfoList().get(r).getIsXun()){
										if(j>0){
											isComplete = true;
											break;
										}
									}
								  }
								}
								if(isComplete){
									break;
								}

								int size2 = taskInfoList.get(j).getInfoList().size();
								for (int m = 0; m < size2; m++) {
									if(m==0){
										editor.putString("pileList"+j, getListPileStr(taskInfoList.get(j).getInfoList()));
										editor.commit();
									}
									double latPile = Double.valueOf(taskInfoList.get(j)
											.getInfoList().get(m).getLAT());
									double lonPile = Double.valueOf(taskInfoList.get(j)
											.getInfoList().get(m).getLON());

									double distance = Tools.getDistance(lon, lat, lonPile, latPile);
									if(m>1){
										if(distance<offsetpoor){
											pointlineID=taskInfoList.get(j).getInfoList().get(m).getLineID();
											String station = taskInfoList.get(j).getInfoList().get(m).getStation();
											pointstation = Double.parseDouble(station);
											offsetpoor=distance;
										}
									}
									if(m==0){
										pointlineID=taskInfoList.get(j).getInfoList().get(m).getLineID();
										String station = taskInfoList.get(j).getInfoList().get(m).getStation();
										pointstation = Double.parseDouble(station);
										offsetpoor=distance;
									}

									// 判断当前位置是否是任务关键点位置，是的话就上传，并保存在系统全局变量和本地文件里
									if (distance <= myApplication.m) {// ((latPile-lat)<=0.00000005
																											// &&
																											// (latPile-lat)>=-0.00000005)
																											// &&
																											// ((lonPile-lon)<=0.00000005
																											// &&
																											// (lonPile-lon)>=-0.00000005)){
										vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
										long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启
										vibrator.vibrate(pattern,-1);           //重复两次上面的pattern 如果只想震动一次，index设为-1

										for (int p = 0; p < MyApplication.pileList.size(); p++) {
											if (MyApplication.pileList
													.get(p)
													.getEVENTID()
													.equals(taskInfoList.get(j).getInfoList()
															.get(m).getEVENTID())) {
												isTwo = true;
												taskInfoList.get(j).getInfoList().get(m).setIsXun(true);
											}
										}
										
										if (!isTwo) {
											String pile = spf.getString("pileList", "");
											editor.putString("pileList", pile
													+ taskInfoList.get(j).getInfoList().get(m)
															.getEVENTID() + ";");
											editor.commit();
											// 更新关键点信息列表
											initTaskList(true);
											Freqinfo info = new Freqinfo();
											info.setEVENTID(taskInfoList.get(j).getInfoList()
													.get(m).getEVENTID());
											info.setPOINTID(taskInfoList.get(j).getInfoList()
													.get(m).getPOINTID());
											info.setLON(valueOf(lon));
											info.setLAT(valueOf(lat));
//											info.setPOINTTYPE(taskInfoList.get(j).getInfoList()
//													.get(m).getPOINTTYPE());
											// 将关键点信息保存在系统全局变量
											MyApplication.pileList.add(info);

											// 上传任务关键点信息
											request.MyTaskRequest(taskInfoList.get(j)
													.getInfoList().get(m).getEVENTID(),
													sdf.format(new Date()),
													valueOf(lon), valueOf(lat),"");

											int size = MyApplication.pileList.size();
											int size1 = taskInfoList.get(j).getInfoList().size();
											if(size == size1){
												AlertDialog.Builder builder = new AlertDialog.Builder(Mywork.this);
												builder.setTitle("温馨提示");
												builder.setMessage("巡检关键点完成是否提交?");
												builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
													@Override
													public void onClick(DialogInterface dialog, int which) {
														// TODO Auto-generated method stub
														Toast.makeText(Mywork.this, "今天所有关键点完成", Toast.LENGTH_SHORT).show();
                                                        for (int k = 0; k < MyApplication.pileList.size(); k++) {
                                                            request.MyTaskRequest(MyApplication.pileList.get(k).getEVENTID(),sdf.format(new Date()),valueOf(MyApplication.pileList.get(k).getLON()), valueOf(MyApplication.pileList.get(k).getLAT()),"");
                                                        }
													}
												}).setNegativeButton("否",null).show();
											}else {

											}
										}
									}
								}


							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					OperationDB operationDB=new OperationDB(Mywork.this);
					ContentValues contentValues=new ContentValues();
					contentValues.put("guid",java.util.UUID.randomUUID().toString());
					contentValues.put("userid",myApplication.userid);
					contentValues.put("lon", valueOf(lon));
					contentValues.put("lat", valueOf(lat));
					contentValues.put("date",dateRequest);
					contentValues.put("pointcount",1);
					contentValues.put("isupload",2);

					operationDB.DBinsert(contentValues, Type.GPS_UPLOCAD);
				}

			}
			// System.out.println(myLocation);
		}
		

		public void onProviderDisabled(String provider) {
			// WriteDB(null);
		}

		public void onProviderEnabled(String provider) {
		}

		@SuppressLint("WrongConstant")
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
				Toast.makeText(Mywork.this, "没有卫星服务", 2000).show();
				break;
			}

		}

	};
	GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
		private GpsStatus gpsStatus;

		@SuppressLint("WrongConstant")
		public void onGpsStatusChanged(int event) {

			gpsStatus = lm.getGpsStatus(null);

			switch (event) {
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				// GPS时间
				break;

			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:


				break;

			case GpsStatus.GPS_EVENT_STARTED:
				// Event sent when the GPS system has started.

				// "<gpsstatusListener>开始信号GPS_EVENT_STARTED\n");
				break;

			case GpsStatus.GPS_EVENT_STOPPED:
				// Event sent when the GPS system has stopped.
				// "<gpsstatusListener>结束信号GPS_EVENT_STOPPED\n");
				break;

			default:
				Toast.makeText(Mywork.this, "没有卫星服务", 2000).show();
				// "<gpsstatusListener>没有获取到状态信息\n");
				break;
			}
		}
	};

	@Override
	public void finish() {
		super.finish();
		// if(mLocationListener!=null){
		// System.out.println("结束。。。。。。。");
		// mLocationManager.removeUpdates(mLocationListener);
		// mapManager.stop();
		// }
		lm.removeUpdates(locationListener);
//		mLocClient.stop();
	}

	StringBuffer sb = new StringBuffer();

	/**
	 * 将保存的上传的关键点信息集合转为字符串
	 * 
	 * @return
	 */
	public String getPlanStr() {
		String planStr = null;
		for (int i = 0; i < todayTask.getInsdateinfoList().size(); i++) {
			sb.append(todayTask.getInsdateinfoList().get(i).getTASKID() + ":");
			final List<Taskinfo> taskInfoList = todayTask.getInsdateinfoList().get(i)
					.getTaskInfoList();

			for (int j = 0; j < taskInfoList.size(); j++) {
				for (int m = 0; m < taskInfoList.get(j).getInfoList().size(); m++) {
					for (int n = 0; n < MyApplication.pileList.size(); n++) {
						if (MyApplication.pileList.get(n).getEVENTID()
								.equals(taskInfoList.get(j).getInfoList().get(m).getEVENTID())) {
//							sb.append(MyApplication.pileList.get(n).getPOINTTYPE() + ",");
							sb.append(MyApplication.pileList.get(n).getPOINTID() + ",");
							sb.append(MyApplication.pileList.get(n).getLON() + ",");
							sb.append(MyApplication.pileList.get(n).getLAT() + ";");
						}
					}
				}
			}
			sb.append("#");
		}
		planStr = sb.toString();
		return planStr;
	}
	/**
	 * 
	  * @功能描述	
	  * @author 张龙飞 Email:longfeiz@geo-k.cn Tel:13671277587
	  * @param list
	  * @return
	  * @createDate 2014-3-16 下午2:28:07
	 	* @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public String getListPileStr(List<Freqinfo> list){
		StringBuffer sbStr = new StringBuffer();
		for(int i=0;i<list.size();i++){
			sbStr.append(list.get(i).getEVENTID()+";");
		}
		return sbStr.toString();
	}
	/**
	 * 获得上传保存的关键点信息，保存到系统全局变量里
	 */
	public void getListTask() {
		MyApplication.pileList.clear();
		String pile = getSharedPreferences("pile", MODE_PRIVATE).getString("pileList", "");
		if (!(pile.equals(""))) {
			String[] idList = null;
			if (pile != null) {
				idList = pile.split(";");
			}
			if (idList != null) {
				if (todayTask.getInsdateinfoList().size() > 0) {
					for (int i = 0; i < idList.length; i++) {
						for (int j = 0; j < todayTask.getInsdateinfoList().size(); j++) {
							List<Taskinfo> taskInfoList = todayTask.getInsdateinfoList().get(j)
									.getTaskInfoList();
							for (int m = 0; m < taskInfoList.size(); m++) {
								for (int n = 0; n < taskInfoList.get(m).getInfoList().size(); n++) {
									if (idList[i].equals(taskInfoList.get(m).getInfoList().get(n)
											.getEVENTID())) {
										MyApplication.pileList.add(taskInfoList.get(m)
												.getInfoList().get(n));
									}
								}

							}
						}
					}
				}

			}
		}
	}

	public void start_xunjian(){
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			new AlertDialog.Builder(Mywork.this).setTitle("提示").setMessage("你未开启GPS是否开始？")
					.setNegativeButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(Mywork.this, "请开启GPS...", Toast.LENGTH_SHORT).show();
							// 返回开启GPS导航设置界面
							Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivityForResult(intent1, 0);

						}
					}).setPositiveButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					finish();
				}
			}).show();

		} else {
			getLocation();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
//		vibrator.cancel();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case 1:
				Tools.exit(Mywork.this);
				break;
			case 2:
				Tools.help(Mywork.this);
				break;
			case 3:
				Tools.cancel(Mywork.this);
				break;
			case 4:
				Tools.gps(Mywork.this);
				break;
		}
		// overridePendingTransition(R.anim.push_right_in,
		// R.anim.push_right_out);
		return super.onOptionsItemSelected(item);
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add(0, 1, 1, "退出").setIcon(R.drawable.exit);
        menu.add(0, 2, 2, "帮助").setIcon(R.drawable.help);
        menu.add(0, 3, 3, "账号注销").setIcon(R.drawable.cancel);
        menu.add(0, 4, 4, "GPS").setIcon(R.drawable.gps);
        return super.onCreateOptionsMenu(menu);
    }

}
