package com.geok.langfang.pipeline.Mywork;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.geok.langfang.jsonbean.Insdateinfo;
import com.geok.langfang.jsonbean.MyTaskQueryBean;
import com.geok.langfang.jsonbean.Taskinfo;
import com.geok.langfang.pipeline.ApplicationApp;
import com.geok.langfang.pipeline.Login;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.MyImageButton;
import com.geok.langfang.tools.PanelListAdapter;
import com.geok.langfang.tools.Tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyworkhDetailActivity extends Activity implements OnClickListener {

	Request request;// 联网操作类
	ListView panelList;// 左侧导航栏
	PanelListAdapter panelListAdapter;// 导航集合adapter
	int image[] = { R.drawable.menu_mywork_selector, R.drawable.menu_problem_selector,
			R.drawable.menu_plan_selector, R.drawable.menu_record_selector };// 左侧导航图标
	// 定义侧滑栏图片标题
	String[] imageTitle = { "我的工作", "事项上报", "巡检计划", "巡检日志" };
	ViewPager viewGroup;// 任务列表grop
	Button back, main;
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
	MyTaskQueryBean todayTask = new MyTaskQueryBean();// 今天的任务信息
	ApplicationApp app;// 系统全局变量
	LayoutInflater inflater;

	int interval = 5;
	private ViewPager mPager;// 页卡内容
	List<View> listViews; // Tab页面列表
	List<View> listViews1; // Tab页面列表
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度

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
	int l;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workh_detail);

		app = (ApplicationApp) getApplicationContext();

		// mgestureDetector=new GestureDetector(this);
		date = sf.format(new Date());
		inflater = LayoutInflater.from(this);
		interval = Tools.getIntByStr(getSharedPreferences("save_password", MODE_PRIVATE).getString(
				"interval", "5"));
		viewGroup = (ViewPager) findViewById(R.id.viewgroup_mywork);
		// mytask = (ViewPager) findViewById(R.id.mytask);
		Intent intent = getIntent();
		int position = intent.getIntExtra("position", 0);
		todayTask = Login.listTask.get(position);

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

		initButton();
		if (MyApplication.inspectionType.equals("auto")) {
			pause_button.setVisibility(View.VISIBLE);
			start_button.setVisibility(View.GONE);

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
				// 关键点信息列表
				ListView taskInfos = (ListView) views.findViewById(R.id.task_list1);
				TasklistAdapter simpleAdapter = new TasklistAdapter(this, taskInfoList.get(i)
						.getInfoList(), R.layout.mywork_listitem);
				taskInfos.setAdapter(simpleAdapter);
				n = i;
				// 点击某个关键点，弹出这个关键点的详细信息
				taskInfos.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						Dialog dia = new Dialog(MyworkhDetailActivity.this,
								R.style.CustomActivityDialog);
						LayoutInflater inflater = LayoutInflater.from(MyworkhDetailActivity.this);
						View view = inflater.inflate(R.layout.mywork_dialog, null);
						TextView texttittle = (TextView) view
								.findViewById(R.id.mywork_dialog_tittle);
						texttittle.setText(taskInfoList.get(n).getInfoList().get(arg2)
								.getPOINTNAME());
//						TextView texttid = (TextView) view.findViewById(R.id.mywork_dialog_type);
//						texttid.setText("桩类型："
//								+ taskInfoList.get(n).getInfoList().get(arg2).getPOINTTYPE());
						TextView textdetial = (TextView) view
								.findViewById(R.id.mywork_dialog_distance);
						textdetial.setText("经度："
								+ taskInfoList.get(n).getInfoList().get(arg2).getLON());

						TextView texttisusing = (TextView) view
								.findViewById(R.id.mywork_dialog_isUsing);
						texttisusing.setText("纬度："
								+ taskInfoList.get(n).getInfoList().get(arg2).getLAT());
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
		// pagerAdapter.notifyDataSetChanged();
		// myHandler.sendEmptyMessage(0);
		// mytask.setOnPageChangeListener(new MyOnPageChangeListener(false));
		// isTiao = true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.back:
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case R.id.main:
			Tools.backMain(this);
			break;

		}

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

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

}
