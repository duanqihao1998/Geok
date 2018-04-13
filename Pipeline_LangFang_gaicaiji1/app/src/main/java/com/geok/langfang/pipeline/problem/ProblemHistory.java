package com.geok.langfang.pipeline.problem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.HistoryProblemImageLoadBean;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.DialogActivity;
import com.geok.langfang.tools.MyHorizontalScrollView;
import com.geok.langfang.tools.SizeCallbackForMenu;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.tools.TreeView;
import com.geok.langfang.tools.TypeQuest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author sunshihai 问题上报的详细信息
 * 
 */

public class ProblemHistory extends Activity implements OnClickListener {

	List<String> listString; // 存储数据的列表
	
	boolean isOnclick = false;
	/**
	 * 异步处理机制 处理其他信息
	 */
	public Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			stopProgressDialog();
			if (msg.arg1 == 0) {
				String str = msg.getData().getString("result");
				getSharedPreferences("values", MODE_PRIVATE).edit().putString("values", str)
						.commit();
				// List<HistoryDataProblemBean> bean=
				// Json.HistoryDataProblem(str);
				initNetList(str);
			}
			if (msg.arg1 == 20000) {
				String str = getSharedPreferences("values", MODE_PRIVATE).getString("values", "-1");
				initNetList(str);
			}

		}

	};
	private static CustomProgressDialog progressDialog = null;
	private MyHorizontalScrollView scrollView;
	ProblemHistoryAdapter problemHistoryAdapter;
	ProblemHistoryAdapter1 problemHistoryAdapter1;
	ProblemHistoryData problemHistoryData;
	HistoryProblemImageLoadBean problemHistoryData1;
	ArrayList<ProblemHistoryData> problemHistoryDataList;
	ArrayList<HistoryProblemImageLoadBean> problemHistoryDataList1;
	private View leftMenu;
	private View rightMenu;
	private ListView listview;
	private View histortList;
	Button confirm;
	TextView type_text, line_text, startpile_text, endpile_text, starttime_text, endtime_text;
	RelativeLayout type, line, startpile, endpile, starttime, endtime;
	RelativeLayout history_search_r;
	private ImageButton search;
	OperationDB operationDB;
	String lineid;
	String startpileid, endpileid; // 起始和终止桩号
	Cursor cursor;
	Button back, main;
	Calendar c;
	String start_date_string;

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

	MyApplication myApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		c = Calendar.getInstance();

		myApplication = new MyApplication(this);
		LayoutInflater inflater = LayoutInflater.from(this);

		setContentView(inflater.inflate(R.layout.problem_history, null));

		listString = new ArrayList<String>();

		scrollView = (MyHorizontalScrollView) findViewById(R.id.myScrollView);
		leftMenu = findViewById(R.id.leftmenu);
		rightMenu = findViewById(R.id.rightmenu);
		histortList = inflater.inflate(R.layout.problemhistory, null);

		back = (Button) histortList.findViewById(R.id.back);
		main = (Button) histortList.findViewById(R.id.main);

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});
		main.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Tools.backMain(ProblemHistory.this);
			}
		});

		history_search_r = (RelativeLayout) histortList.findViewById(R.id.history_search_r);
		search = (ImageButton) histortList.findViewById(R.id.problemhistory_search);
		history_search_r.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scrollView.clickRightButton(search.getMeasuredWidth());
				isOnclick = !isOnclick;
				setOnclick(isOnclick);
				
			}
		});
		View leftView = new View(this);// 左边透明视图
		View rightView = new View(this);// 右边透明视图
		leftView.setBackgroundColor(Color.TRANSPARENT);
		rightView.setBackgroundColor(Color.TRANSPARENT);
		final View[] children = new View[] { leftView, histortList, rightView };
		// 初始化滚动布局
		scrollView.initViews(children, new SizeCallbackForMenu(search), leftMenu, rightMenu);

		// Button back = (Button)findViewById(R.id.back);
		// Button main = (Button)findViewById(R.id.main);
		// back.setOnClickListener(this);
		// main.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		listview = (ListView) findViewById(R.id.problem_history_list);
		initList();
		init();
		super.onResume();
	}

	/**
	 * 
	 * @param str
	 *            网络上获取的数据字符串 主要是处理网络获取的信息然后解析 匹配到Listview 上
	 * 
	 */
	public void initNetList(String str) {

		final List<HistoryProblemImageLoadBean> bean = Json.HistoryDataProblem(str);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		problemHistoryDataList1 = new ArrayList<HistoryProblemImageLoadBean>();

		Map<String, Object> map;
		/*
		 * cursor的格式 ID type guid lon lat occurtime,photopath,
		 * photodes,line,lineid
		 * ,pile,pileid,offset,userId,uploadtime,problemdes,isupload
		 */
		for (int i = 0; i < bean.size(); i++) {
			map = new HashMap<String, Object>();
			map.put("time", "发生时间：" + bean.get(i).getOCCURRENCETIME());
			map.put("location", "线路位置：" + bean.get(i).getLINELOOP() + bean.get(i).getMARKERNAME());
			map.put("type", "问题类型：" + bean.get(i).getTYPE());
			// if(bean.get(i).getPicturebean().size()==0)
			// {
			// map.put("flag", 1);
			// map.put("image", null);
			// }else
			// {
			String photopath = bean.get(i).getPATH();
			if (photopath != null) {

				map.put("flag", 1);
				map.put("image", photopath);
				listString.add(photopath);
			} else {
				map.put("flag", 1);
				map.put("image", null);
				listString.add("");
			}

			list.add(map);

			problemHistoryData1 = new HistoryProblemImageLoadBean();
			problemHistoryData1.setTYPE(bean.get(i).getTYPE());
			problemHistoryData1.setLON(bean.get(i).getLON());
			problemHistoryData1.setLAT(bean.get(i).getLAT());
			problemHistoryData1.setOCCURRENCETIME(bean.get(i).getOCCURRENCETIME());
			problemHistoryData1.setPATH(bean.get(i).getPATH());
			problemHistoryData1.setLINELOOP(bean.get(i).getLINELOOP());
			problemHistoryData1.setMARKERNAME(bean.get(i).getMARKERNAME());
			problemHistoryData1.setOFF(bean.get(i).getOFF());
			problemHistoryData1.setREPORTDATE(bean.get(i).getREPORTDATE());
			problemHistoryData1.setDESCRIPTION(bean.get(i).getDESCRIPTION());
			problemHistoryData1.setADDRESS(bean.get(i).getADDRESS());
			problemHistoryData1.setDEALPLAN(bean.get(i).getDEALPLAN());
			problemHistoryData1.setINSPECTOR(bean.get(i).getINSPECTOR());
			problemHistoryDataList1.add(problemHistoryData1);
		}
		problemHistoryAdapter1 = new ProblemHistoryAdapter1(getApplicationContext(), list);
		listview.setAdapter(problemHistoryAdapter1);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ProblemHistory.this, ProblemHistoryDetialNet.class);

				// HistoryDataProblemBean data= bean.get(arg2);
				// intent.putExtra("values",data);
				// Bitmap bm;
				// if(bean.get(arg2).getPicturebean().size()==0)
				// {
				// // saveMyBitmap("myBitmap", null);
				// intent.putExtra("image", 1);
				// }else
				// {
				//
				// String photopath
				// =bean.get(arg2).getPicturebean().get(0).getPATH();
				// // bm=getBitmap(photopath);
				// // saveMyBitmap("myBitmap", bm);
				// intent.putExtra("image", 2);
				// // bm.recycle();
				// }
				// String photopath
				// =bean.get(arg2).getPicturebean().get(0).getCONTENTINFO();
				// Bitmap bm=getBitmap(photopath);
				// saveMyBitmap("myBitmap", bm);
				HistoryProblemImageLoadBean data = problemHistoryDataList1.get(arg2);

				intent.putExtra("values", data);
				/*
				 * flag 1表示本地数据 2 表示是网络数据
				 */
				startActivityForResult(intent, 20000);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

			}

		});

	}

	public static Bitmap getBitmap(String imgStr)

	{
		Bitmap btm = null;
		// 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return null;

		try {
			// Base64解码
			// byte[] b = decoder.decodeBuffer(imgStr);
			byte[] b = Base64.decode(imgStr, 0);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成jpeg图片
			if (b.length != 0) {
				// btm =BitmapFactory.decodeByteArray(b, 0, b.length);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = false;
				options.inSampleSize = 10;
				btm = BitmapFactory.decodeByteArray(b, 0, b.length, options);
			}

			return btm;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 从数据库读取数据 然后匹配到ListView 上
	 */

	public void initList() {
		startProgressDialog();
		GrallyImageView grallyImageView = new GrallyImageView();
		problemHistoryDataList = new ArrayList<ProblemHistoryData>();
		operationDB = new OperationDB(getApplicationContext());
		cursor = operationDB.DBselect(Type.PROBLEM_UPLOAD);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// listview=(ListView)findViewById(R.id.problem_history_list);
		Map<String, Object> map;
		for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
//		while (cursor.moveToNext()) {
			/*
			 * 为每一个listItem详细数据赋值
			 */
			problemHistoryData = new ProblemHistoryData();
			problemHistoryData.setGuid(cursor.getString(2));
			problemHistoryData.setType(cursor.getString(1));
			problemHistoryData.setLon(cursor.getString(3));
			problemHistoryData.setLat(cursor.getString(4));
			problemHistoryData.setOccurtime(cursor.getString(5));
			problemHistoryData.setPhotopath(cursor.getString(6));
			problemHistoryData.setPhotodes(cursor.getString(7));
			problemHistoryData.setLine(cursor.getString(8));
			problemHistoryData.setLineid(cursor.getString(9));
			problemHistoryData.setPile(cursor.getString(10));
			problemHistoryData.setPileid(cursor.getString(11));
			problemHistoryData.setOffset(cursor.getString(12));
			problemHistoryData.setUserid(cursor.getString(13));
			problemHistoryData.setUploadtime(cursor.getString(15));
			problemHistoryData.setProblemdes(cursor.getString(16));
			problemHistoryData.setLocation(cursor.getString(17));
			problemHistoryData.setPlan(cursor.getString(18));
			problemHistoryData.setResult(cursor.getString(19));
			problemHistoryData.setIsupload(cursor.getInt(20));
			problemHistoryDataList.add(problemHistoryData);

			/*
			 * 为每一个listItem的标题数据赋值
			 */
			map = new HashMap<String, Object>();
			map.put("time", "发生时间：" + cursor.getString(5));
			map.put("location", "线路位置：" + cursor.getString(8) + cursor.getString(10));
			map.put("type", "问题类型：" + cursor.getString(1));
			int flag = cursor.getInt(20);
			String photopath = cursor.getString(6);
			Bitmap bm = null;
			if(photopath.length()>1){
			String temp[] = photopath.split("#");
			bm = getBitmap1(temp[0]);
			}else{
			Resources res=getResources(); 
			// 读取InputStream并得到位图
			@SuppressLint("ResourceType") InputStream is=res.openRawResource(R.drawable.problem_photo);
			BitmapDrawable bmpDraw=new BitmapDrawable(is);
			bm=bmpDraw.getBitmap();
			}
			map.put("flag", flag);
			map.put("image", bm);
			list.add(map);
		}
		cursor.close();operationDB.db.close();
		problemHistoryAdapter = new ProblemHistoryAdapter(getApplicationContext(), list);
		listview.setAdapter(problemHistoryAdapter);
		stopProgressDialog();
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ProblemHistoryDetial.class);
				intent.putExtra("values", problemHistoryDataList.get(arg2));
				/*
				 * flag 1表示本地数据 2 表示是网络数据
				 */
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}

		});

	}

	public Bitmap getBitmap1(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回 bm 为空
		options.inJustDecodeBounds = false; // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = (int) (options.outHeight / (float) 320);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be; // 重新读入图片，注意此时已经把 options.inJustDecodeBounds
									// 设回 false 了
		bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}

	/**
	 * 初始化组件
	 */
	private void init() {
		confirm = (Button) findViewById(R.id.problem_history_confirm);
		confirm.setOnClickListener(this);

		/*
		 * TextView
		 * type_text,line_text,startpile_text,endpile_text,starttime_text
		 * ,endtime_text; RelativeLayout
		 * type,line,startpile,endpile,starttime,endtime;
		 */
		type = (RelativeLayout) findViewById(R.id.problem_history_type);
		type.setOnClickListener(this);
		line = (RelativeLayout) findViewById(R.id.problem_history_line);
		line.setOnClickListener(this);
		startpile = (RelativeLayout) findViewById(R.id.problem_history_startpile);
		startpile.setOnClickListener(this);
		endpile = (RelativeLayout) findViewById(R.id.problem_history_endpile);
		endpile.setOnClickListener(this);
		starttime = (RelativeLayout) findViewById(R.id.problem_history_starttime);
		starttime.setOnClickListener(this);
		endtime = (RelativeLayout) findViewById(R.id.problem_history_endtime);
		endtime.setOnClickListener(this);
		type_text = (TextView) findViewById(R.id.problem_history_type_text);
		line_text = (TextView) findViewById(R.id.problem_history_line_text);
		startpile_text = (TextView) findViewById(R.id.problem_history_startpile_text);
		endpile_text = (TextView) findViewById(R.id.problem_history_endpile_text);
		starttime_text = (TextView) findViewById(R.id.problem_history_starttime_text);
		endtime_text = (TextView) findViewById(R.id.problem_history_endtime_text);
		setOnclick(isOnclick);
	}

	public void setOnclick(boolean isonclick){
			confirm.setClickable(isonclick);
			type.setClickable(isonclick);
			line.setClickable(isonclick);
			startpile.setClickable(isonclick);
			endpile.setClickable(isonclick);
			starttime.setClickable(isonclick);
			endtime.setClickable(isonclick);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.problem_history_confirm:
			Request request = new Request(myHandler);
			if (Tools.isNetworkAvailable(this, true)) {
				request.ProblemHistoryQuest(myApplication.userid, type_text.getText().toString(),
						lineid, startpileid, endpileid, starttime_text.getText().toString(),
						endtime_text.getText().toString());
				startProgressDialog();
				isOnclick = !isOnclick;
				setOnclick(isOnclick);
			}
			scrollView.clickRightButton(search.getMeasuredWidth());
			break;

		// case R.id.back:
		// Intent intent=new Intent();
		// intent.setClass(ProblemHistory.this, Problem.class);
		// startActivity(intent);
		// break;
		// case R.id.main:
		// Intent intent2=new Intent();
		// intent2.setClass(ProblemHistory.this, MainView.class);
		// startActivity(intent2);
		// break;
		case R.id.problem_history_type:
			Intent type = new Intent();
			type.setClass(getApplicationContext(), DialogActivity.class);
			type.putExtra("flag", TypeQuest.PROBLEM_HISTORY_TYPE);
			startActivityForResult(type, TypeQuest.PROBLEM_HISTORY_TYPE);
			break;
		case R.id.problem_history_line:
			Intent line = new Intent();
			line.setClass(getApplicationContext(), TreeView.class);
			line.putExtra("flag", TypeQuest.PROBLEM_HISTORY_LINE);
			startActivityForResult(line, TypeQuest.PROBLEM_HISTORY_LINE);
			break;

		case R.id.problem_history_startpile:
			if (lineid == null) {
				Toast.makeText(getApplicationContext(), "请先选择线路", 1000).show();
			} else {
				Intent startpile = new Intent();
				startpile.setClass(getApplicationContext(), DialogActivity.class);
				startpile.putExtra("flag", TypeQuest.PROBLEM_HISTORY_PILE);
				startpile.putExtra("lineid", lineid);
				startActivityForResult(startpile, TypeQuest.PROBLEM_HISTORY_PILE);
			}
			break;

		case R.id.problem_history_endpile:
			if (lineid == null) {
				Toast.makeText(getApplicationContext(), "请先选择线路", 1000).show();
			} else {
				Intent endpile = new Intent();
				endpile.setClass(getApplicationContext(), DialogActivity.class);
				endpile.putExtra("flag", TypeQuest.PROBLEM_HISTORY_PILE_1);
				endpile.putExtra("lineid", lineid);
				startActivityForResult(endpile, TypeQuest.PROBLEM_HISTORY_PILE_1);
			}
			break;

		case R.id.problem_history_starttime:
			Tools.setDateDialog(ProblemHistory.this, c, starttime_text);
			break;

		case R.id.problem_history_endtime:
			if ("起始日期".equals(starttime_text.getText().toString())) {
				Toast.makeText(ProblemHistory.this, "请先选择起始日期！", 1000).show();
			} else {
				start_date_string = starttime_text.getText().toString();
				Tools.setEndDateDialog(ProblemHistory.this, c, start_date_string, endtime_text);
			}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {

		case TypeQuest.PROBLEM_HISTORY_TYPE:
			if (resultCode == Activity.RESULT_OK) {
				type_text.setText(data.getStringExtra("problem_history_type"));
			}
			break;

		case TypeQuest.PROBLEM_HISTORY_PILE:
			if (resultCode == Activity.RESULT_OK) {

				startpile_text.setText(data.getStringExtra("problem_history_pile"));
				startpileid = data.getStringExtra("markId");

				// lineId=data.getStringExtra("lineId");
			}
			break;
		case TypeQuest.PROBLEM_HISTORY_PILE_1:
			if (resultCode == Activity.RESULT_OK) {

				endpile_text.setText(data.getStringExtra("problem_history_pile_1"));
				endpileid = data.getStringExtra("markId");

				// lineId=data.getStringExtra("lineId");
			}
			break;
		case TypeQuest.PROBLEM_HISTORY_LINE:
			if (resultCode == Activity.RESULT_OK) {
				line_text.setText(data.getStringExtra("line"));
				lineid = data.getStringExtra("lineId");
				startpile_text.setText("起始桩号");
				endpile_text.setText("终止桩号");
			}
			break;
		case 20000:

			// List<HistoryDataProblemBean> bean= Json.HistoryDataProblem(str);
			Message message = new Message();
			message.arg1 = 20000;
			myHandler.sendMessage(message);
			// initNetList(str);
		}
	}

	public void saveMyBitmap(String bitName, Bitmap mBitmap) {
		File f = new File("/sdcard/" + bitName + ".png");
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
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
