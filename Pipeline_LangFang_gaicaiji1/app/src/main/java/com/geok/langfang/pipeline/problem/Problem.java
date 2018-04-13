package com.geok.langfang.pipeline.problem;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.Freqinfo;
import com.geok.langfang.jsonbean.InformationQueryBean;
import com.geok.langfang.jsonbean.LineSyncBean;
import com.geok.langfang.jsonbean.PileSyncBean;
import com.geok.langfang.jsonbean.PileSyncBeanChild;
import com.geok.langfang.jsonbean.Taskinfo;
import com.geok.langfang.pipeline.Mywork.Mywork;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.search.CurrentLineInfo;
import com.geok.langfang.pipeline.search.LineInfo;
import com.geok.langfang.pipeline.toolcase.KeypointAcquisition;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.DialogActivity;
import com.geok.langfang.tools.DialogEditActivity;
import com.geok.langfang.tools.GetLocation;
import com.geok.langfang.tools.MyImageButton;
import com.geok.langfang.tools.MyLocation;
import com.geok.langfang.tools.PanelListAdapter;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.tools.TreeElement;
import com.geok.langfang.tools.TreeView;
import com.geok.langfang.tools.TypeQuest;
import com.geok.langfang.util.OffnetReceive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author sunshihai 问题上报功能 主要包括问题类型，线路桩号，当前位置，问题描述，发生时间，上报时间 等等
 * 
 */

public class Problem extends Activity implements OnClickListener {

	private static final int CAMERA_WITH_DATA = 3023;
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	private static final int EDIT_IMAGE_WITH_DATA = 3022;
	private static final int GALLERY_WITH_DATA = 3020;
	private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory()
			+ "/ASoohue/CameraCache");
	private File mCurrentPhotoFile;// 照相机拍照得到的图片
	String FileName;
	Bitmap bm, bitimage;
	GetLocation getLocation; //
	ImageView imageview;
	String guid = "";
	ProblemHistoryData Problem_data; // 从历史详细记录跳转过的历史记录对象，用于数据的修改
	Button back, main;
	BMapManager mapManager; // 要用到的百度地图服务的管理器

	MyLocation location1;
	LocationClient mLocationClient;
	/**
	 * 位置服务的对象
	 */
	// MKLocationManager mLocationManager = null;
	// LocationListener mLocationListener = null;
	LocationManager lm;
	private static CustomProgressDialog progressDialog = null; // 自定义的进度条的对象
	/*
	 * lon lat 保存当前位置的经纬度
	 */
	String lon = "", lat = "";

	GrallyImageView grallyImageView;
	/*
	 * lineId 保存线路Id 用于查询相应的桩和上报数据 markId 用于上报数据
	 */
	String lineId;
	String markId;
	RelativeLayout problem_date, problem_time, problem_edit_date, problem_edit_time;
	TextView date_text, time_text, type_text, line_text, pile_text, distance_text, comment_text;
	String str_comment_text, str_location_text, str_plan_text, str_result_text, str_distance_text;
	ListView problem_lineAndpile, problem_tester;
	RelativeLayout problem_type, problem_line, problem_pile, problem_distance;
	PanelListAdapter ListAdapter, ListAdapter1, ListAdapter2;
	Button problem_photo, problem_point_photo, searchLocation, currentLocation;
	MyImageButton upload, save, reset;
	LinearLayout linLayout_comment, linLayout_comment_location, linLayout_comment_plan,
			linLayout_comment_result;
	SimpleAdapter simpleAdapter_type, simpleAdapter_line, simpleAdapter_distance,
			simpleAdapter_tester;
	Map<String, Object> map;
	EditText problem_comment_edit;
	/*
	 * imageList 传递图片数据列表 commentList 传递图片描述数据列表
	 */
	ArrayList<String> imageList, commentList;
	Request request;
	MyApplication myApplication;
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String uptime = sDateFormat.format(new java.util.Date());
	int cor = 0;
	LocationManager locationManager;
	Boolean isUpdate = false;
	List<InformationQueryBean> data;
	String linename = "";
	public Handler Myhandler = new Handler() {

		@SuppressLint("WrongConstant")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			stopProgressDialog();
			if (msg.arg1 == 11) {

				if (msg.getData().getInt("flag") == 1) {
					if (msg.getData().getString("result").equals("OK|TRUE")) {
						save(1);
						Toast.makeText(getApplicationContext(), "上报成功", Toast.LENGTH_LONG).show();
					} else {
						save(0);
						Toast.makeText(getApplicationContext(), "未上报成功，已保存", 1000).show();
					}
				} else {
					save(0);
					Toast.makeText(getApplicationContext(), "未上报成功，已保存", 1000).show();
				}
			}
			if (msg.arg1 == 12) {
				Toast.makeText(getApplicationContext(), "保存成功", 1000).show();
			}
			if (msg.arg1 == 41) {
				String str = msg.getData().getString("result");
				int flag = msg.getData().getInt("flag");
				if (flag == 1) {
					if (!str.equals("-1")) {
						Bundle bundle = new Bundle();
						Intent intent = new Intent();
						intent.setClass(Problem.this, LineInfo.class);
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
			}
			
		}
	};
	private List<PileSyncBean> list;

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

	Calendar c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.problem);
		cor = 0;
		request = new Request(Myhandler);
		String SERVERNAME = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) this.getSystemService(SERVERNAME);
		myApplication = new MyApplication(this);
		c = Calendar.getInstance();
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);

		back.setOnClickListener(this);
		main.setOnClickListener(this);

		main.setVisibility(View.GONE);

		grallyImageView = new GrallyImageView();
		problem_date = (RelativeLayout) findViewById(R.id.problem_date);
		problem_time = (RelativeLayout) findViewById(R.id.problem_time);
		problem_date.setOnClickListener(this);
		problem_time.setOnClickListener(this);

		guid = java.util.UUID.randomUUID().toString();
		imageList = new ArrayList<String>();
		commentList = new ArrayList<String>();

		date_text = (TextView) findViewById(R.id.problem_data_text);
		time_text = (TextView) findViewById(R.id.problem_time_text);
		imageview = (ImageView) findViewById(R.id.problem_imageView);
		comment_text = (TextView) findViewById(R.id.problem_comment);

		problem_comment_edit = (EditText) findViewById(R.id.problem_comment_edit);

		linLayout_comment = (LinearLayout) findViewById(R.id.problem_lin_comment);
		linLayout_comment.setOnClickListener(this);

		if(uptime.length() == 19){
		date_text.setText(uptime.substring(0, 10));
		time_text.setText(uptime.substring(11, 16));
		}
		Problem_data = (ProblemHistoryData) getIntent().getSerializableExtra("values");

		imageview.setOnClickListener(new OnClickListener() {

			@SuppressLint("WrongConstant")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (imageList.size() > 0) {
					System.out.println("imageList不为空");
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putStringArrayList("image", imageList);
					bundle.putStringArrayList("comment", commentList);
					intent.putExtras(bundle);
					intent.setClass(Problem.this, GrallyImage.class);
					startActivityForResult(intent, GALLERY_WITH_DATA);
				} else {
					System.out.println("bm为空");
					Toast.makeText(getApplicationContext(), "无照片", 1000).show();
				}
				//

			}
		});
		problem_photo = (Button) findViewById(R.id.problem_photo);
		problem_point_photo = (Button) findViewById(R.id.problem_point_photo);

		problem_photo.setOnClickListener(this);
		problem_point_photo.setOnClickListener(this);

		// //位置拾取
		// searchLocation=(Button)findViewById(R.id.searchLocation);
		// searchLocation.setOnClickListener(this);

		// 当前位置
		// currentLocation = (Button) findViewById(R.id.currentLocation);
		// currentLocation.setOnClickListener(this);

		/*
		 * 上报
		 */
		upload = (MyImageButton) findViewById(R.id.problem_upload);
		upload.setOnClickListener(this);

		/*
		 * 保存
		 */
		save = (MyImageButton) findViewById(R.id.problem_save);
		save.setOnClickListener(this);
		/*
		 * 重置
		 */
		reset = (MyImageButton) findViewById(R.id.problem_reset);
		reset.setOnClickListener(this);
		initListView();

		if (Problem_data != null) {
			String upgrate = getIntent().getStringExtra("flag");
			if ("upgrate".equals(upgrate)) {
				isUpdate = true;
				guid = Problem_data.getGuid();
				String occurtime = Problem_data.getOccurtime();
				String temp[] = occurtime.split(" ");
				String imagedes = Problem_data.getPhotodes();
				String imagedestemp[] = imagedes.split("#");
				String image = Problem_data.getPhotopath();
				String imagetemp[] = image.split("#");

				for (int i = 0; i < imagetemp.length; i++) {
					imageList.add(imagetemp[i]);
					if (imagedestemp.length > 0) {
						commentList.add(imagedestemp[i]);
					}
				}
				type_text.setText(Problem_data.getType());
				date_text.setText(temp[0]);
				time_text.setText(temp[1].substring(0, temp[1].lastIndexOf(":")));
				line_text.setText(Problem_data.getLine());
				pile_text.setText(Problem_data.getPile());
				distance_text.setText(Problem_data.getOffset());
				lineId=Problem_data.getLineid();
				markId=Problem_data.getPileid();
				problem_comment_edit.setText(Problem_data.getProblemdes());
				lon = Problem_data.getLon();
				lat = Problem_data.getLat();
			}
		}

		if (imageList.size() > 0) {
			bm = getBitmap(imageList.get(0));
			imageview.setImageBitmap(bm);
		}

		if(!isUpdate){
		getCoordinate();
		}

		String line = getSharedPreferences("sync", MODE_PRIVATE).getString("line", null);
		Log.i("line1314",line);
		List<LineSyncBean> pipelineSyncList = Json.getPipelineSyncList(line);
		for (int i = 0; i < pipelineSyncList.size(); i++) {
			LineSyncBean lineSyncBean = pipelineSyncList.get(i);

			if(lineSyncBean.getChildrenList().length>0){
				SaxTree(lineSyncBean.getChildrenList());
			}
		}


		String str = getSharedPreferences("sync", MODE_PRIVATE).getString("pile", "-1");
		Log.i("Problm",str);
		list = Json.getPileSyncList(str);
		for (int i = 0; i < list.size(); i++) {
			PileSyncBean pileSyncBean = list.get(i);
			if(Mywork.pointlineID!=null && Mywork.pointlineID.equals(pileSyncBean.getLINELOOPEVENTID())){
				List<PileSyncBeanChild> childBean = pileSyncBean.getChildBean();
				double poor=0;
				for (int j = 0; j < childBean.size(); j++) {
					String markerstation = childBean.get(i).getMARKERSTATION();
					double parseDouble = Double.parseDouble(markerstation);
					double pointstation = Mywork.pointstation;

					double nub = pointstation - parseDouble;
					double abs = Math.abs(nub);
					if(j==0){
						poor=abs;
					}else if(poor>abs) {
						poor=abs;
						String markername = childBean.get(i).getMARKERNAME();
						String markereventid = childBean.get(i).getMARKEREVENTID();
						pile_text.setText(markername);
						markId=markereventid;
					}
				}
			}
		}
	}

	private void SaxTree(LineSyncBean[] list) {

		for (int i = 0; i< list.length; i++) {
			LineSyncBean bean = list[i];
			Log.i("woaini1","woaini1:"+bean.getLineloopeventid()+"-------"+bean.getLineloopname());

			String lineloopeventid = bean.getLineloopeventid();

			String pointlineID = Mywork.pointlineID;
			if(lineloopeventid.equals(pointlineID)){
				line_text.setText(bean.getLineloopname());
				lineId=lineloopeventid;
			}

			if (bean.getChildrenList() != null) {
				this.SaxTree(bean.getChildrenList());

			}
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

	@SuppressLint("WrongConstant")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.back:
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case R.id.main:
			Tools.backMain(this);
			break;

		case R.id.problem_distance:
			Intent distance = new Intent();
			distance.putExtra("flag", TypeQuest.PROBLEM_DISTANCE);
			distance.putExtra("Tittle", "偏移量(m)");
			if (!"填写偏移量".equals(distance_text.getText().toString())) {
				distance.putExtra("text", distance_text.getText().toString());
			}
			distance.setClass(Problem.this, DialogEditActivity.class);
			startActivityForResult(distance, TypeQuest.PROBLEM_DISTANCE);
			break;
		case R.id.problem_line:
			Intent line = new Intent();
			line.setClass(Problem.this, TreeView.class);
			startActivityForResult(line, TypeQuest.PROBLEM_LINE);
			break;

		case R.id.problem_pile:
			if (lineId == null) {
				Toast.makeText(getApplicationContext(), "请先选择线路", 1000).show();
			} else {
				Intent pile = new Intent();
				pile.setClass(Problem.this, DialogActivity.class);
				pile.putExtra("flag", TypeQuest.PROBLEM_PILE);
				pile.putExtra("lineid", lineId);
				if(!(pile_text.getText().toString().equals("选择桩号"))){
					pile.putExtra("markName", pile_text.getText().toString());
				}
				startActivityForResult(pile, TypeQuest.PROBLEM_PILE);
			}

			break;

		case R.id.problem_type:
			Intent intent = new Intent();
			intent.setClass(Problem.this, DialogActivity.class);
			intent.putExtra("flag", TypeQuest.PROBLEM_TYPE);
			startActivityForResult(intent, TypeQuest.PROBLEM_TYPE);
			break;
		case R.id.problem_date:

			// new DatePickerDialog(Problem.this, new OnDateSetListener() {
			//
			// public void onDateSet(DatePicker view, int year,
			// int monthOfYear, int dayOfMonth) {
			// int n = monthOfYear + 1;
			// date_text.setText(year + "-" + n + "-" + dayOfMonth);
			// date_text.setGravity(Gravity.CENTER);
			// }
			// }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
			// c.get(Calendar.DAY_OF_MONTH)).show();
			Tools.setDateDialog(this, c, date_text);
			date_text.setGravity(Gravity.CENTER);
			break;

		case R.id.problem_time:
			Tools.setTimeDialog(this, c, time_text);
			break;
		case R.id.problem_photo:
			if (imageList.size() < 3) {
				String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
					doTakePhoto();// 用户点击了从照相机获取
				} else {
					Toast.makeText(Problem.this, "没有SD卡", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(Problem.this, "照片不得超三张，请删除后再试", 1000).show();
			}

			break;

		case R.id.problem_point_photo:
			if (imageList.size() < 3) {
				doPickPhotoFromGallery();// 从相册中去获取
			} else {
				Toast.makeText(Problem.this, "照片不得超三张，请删除后再试", 1000).show();
			}
			break;

		// case R.id.currentLocation:
		//
		// // getLocation=new GetLocation(getApplicationContext());
		// // if(getLocation.isGpsEnabled(getApplicationContext()))
		// // {
		// //
		// // lon=getLocation.getLon()+"";
		// // lat=getLocation.getLat()+"";
		// // Toast.makeText(Problem.this, lon+","+lat, 1000).show();
		// // }else
		// // {
		// // Toast.makeText(Problem.this, "当前GPS不可用", 1000).show();
		// // }
		//
		// // final DecimalFormat df = new DecimalFormat("#####0.0000");
		// // mapManager = new BMapManager(getApplication());
		// // mapManager.init("8728BEBD2582C80E5E87FFB8515A2A25EE7F80B5",
		// // null);
		// // mapManager.start();
		// // mLocationManager = mapManager.getLocationManager();
		// //
		// mLocationManager.enableProvider(MKLocationManager.MK_NETWORK_PROVIDER);
		// // mLocationManager.enableProvider((int)
		// // MKLocationManager.MK_GPS_PROVIDER);
		// // // mLocationManager.setNotifyInternal(interval, interval);
		// // mLocationListener = new LocationListener() {
		// //
		// // @Override
		// // public void onLocationChanged(Location arg0) {
		// // // TODO Auto-generated method stub
		// //
		// // if(arg0!=null)
		// // {
		// // lon=arg0.getLongitude()+"";
		// // lat=arg0.getLatitude()+"";
		// //
		// // }
		// // }
		// //
		// // };
		// // Toast.makeText(getApplicationContext(),lat+","+lon, 1000).show();
		// // mLocationManager.requestLocationUpdates(mLocationListener);
		// Handler handler = new Handler() {
		//
		// @Override
		// public void handleMessage(Message msg) {
		// super.handleMessage(msg);
		// if (msg.what == 1) {
		// Toast.makeText(Problem.this, "没有获得定位信息", 1000).show();
		// } else {
		// lon = location1.mylocation.longitude + "";
		// lat = location1.mylocation.latitude + "";
		// // Toast.makeText(Problem.this,lat+","+lon,
		// // 1000).show();
		// Toast.makeText(Problem.this, "成功获取定位信息", 1000).show();
		// // line_text.setText("轮库输气线");
		// // pile_text.setText("LK097km+000m");
		// currentLocation.setTextColor(Color.WHITE);
		// currentLocation.setTextSize(15);
		// currentLocation.setText("已获坐标");
		// currentLocation.setBackgroundResource(R.drawable.button_bg_02);
		// mLocationClient.stop();
		// }
		//
		// }
		//
		// };
		// location1 = new MyLocation(this, handler);
		// mLocationClient = location1.mLocationClient;
		// mLocationClient.start();
		//
		// break;
		/*
		 * case R.id.searchLocation: final Dialog dia=new Dialog(Problem.this,
		 * R.style.CustomActivityDialog); LayoutInflater
		 * inflater=LayoutInflater.from(Problem.this); View
		 * view=inflater.inflate(R.layout.problem_dialog, null);
		 * 
		 * ListView
		 * listview=(ListView)view.findViewById(R.id.problem_searchLocation);
		 * List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		 * Map<String, Object> map;
		 * 
		 * map=new HashMap<String, Object>(); map.put("text", "线路");
		 * map.put("image", R.drawable.arrow); list.add(map); map=new
		 * HashMap<String, Object>(); map.put("text", "桩号"); map.put("image",
		 * R.drawable.arrow); list.add(map);
		 * 
		 * SimpleAdapter adapter=new SimpleAdapter(Problem.this, list,
		 * R.layout.problem_dialog_list, new String[]{"text","image"}, new
		 * int[]{
		 * R.id.problem_dialog_listitem_text,R.id.problem_dialog_listitem_image
		 * }); listview.setAdapter(adapter);
		 * 
		 * Button button
		 * =(Button)view.findViewById(R.id.problem_searchLocation_save);
		 * button.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub dia.dismiss(); } }); dia.setContentView(view); dia.show();
		 * 
		 * break;
		 */

		case R.id.problem_upload:
			//修改的时候
			if (isUpdate) {
				if (type_text.getText().toString().equals("选择问题类型") || line_text.getText().toString().equals("选择线路") || pile_text.getText().toString().equals("选择桩号")
						||distance_text.getText().toString().equals("未填写") || distance_text.getText().toString().equals("填写偏移量")) {
					Toast.makeText(Problem.this, "带*的为必填项，请填写完再试！", 1000).show();
				} else {

//					if (distance_text.getText().toString().equals("未填写") || distance_text.getText().toString().equals("填写偏移量")) {
//						str_distance_text = "";
//					} else {
//						str_distance_text = distance_text.getText().toString();
//					}
					if (Tools.isNetworkAvailable(getApplicationContext(), true)) {
						String uploadtime = null;
						if (date_text.getText().toString() != "日期") {
							uploadtime = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY) + ":"
									+ c.get(Calendar.MINUTE) + ":00";
						} else {
							uploadtime = uptime;
						}
						// String str = date_text.getText().toString();
						str_comment_text = problem_comment_edit.getText().toString();
						startProgressDialog();
						request.problemUploadRequest(guid, myApplication.userid, lineId, uploadtime, str_distance_text, type_text.getText().toString(), str_comment_text, imageList,
								myApplication.depterid, uploadtime, lon, lat, commentList, markId, str_location_text, str_plan_text, str_result_text, "");
					} else {
						save(0);
					}

				}

			} else {
				getCoordinate();
				if (type_text.getText().toString().equals("选择问题类型") || line_text.getText().toString().equals("选择线路") || pile_text.getText().toString().equals("选择桩号")
						||distance_text.getText().toString().equals("未填写") || distance_text.getText().toString().equals("填写偏移量")) {
					Toast.makeText(Problem.this, "带*的为必填项，请填写完再试！", 1000).show();
				} else {
					if ("".equals(lon) || "".equals(lat)) {
						Toast.makeText(Problem.this, "未获取坐标位置，请检查GPS设置", 1000).show();
					} else {
//						if (distance_text.getText().toString().equals("未填写") || distance_text.getText().toString().equals("填写偏移量")) {
//							str_distance_text = "";
//						} else {
//							str_distance_text = distance_text.getText().toString();
//						}
						cor = 2;
					}
				}
			}
			break;
		case R.id.problem_save:
			if (isUpdate) {
				if (type_text.getText().toString().equals("选择问题类型") || line_text.getText().toString().equals("选择线路") || pile_text.getText().toString().equals("选择桩号")
						||distance_text.getText().toString().equals("未填写") || distance_text.getText().toString().equals("填写偏移量")) {
					Toast.makeText(Problem.this, "带*的为必填项，请填写完再试！", 1000).show();
				} else {
						save(0);
				}
			} else {
				getCoordinate();
				if (type_text.getText().toString().equals("选择问题类型") || line_text.getText().toString().equals("选择线路") || pile_text.getText().toString().equals("选择桩号")
						||distance_text.getText().toString().equals("未填写") || distance_text.getText().toString().equals("填写偏移量")) {
					Toast.makeText(Problem.this, "带*的为必填项，请填写完再试！", 1000).show();
				} else {
					if ("".equals(lon) || "".equals(lat)) {
						Toast.makeText(Problem.this, "未获取坐标位置，请检查GPS设置", 1000).show();
					} else {
						cor = 1;
					}
				}
			}
			break;
		case R.id.problem_reset:
			
				AlertDialog.Builder builder = new Builder(Problem.this);
				builder.setTitle("提示");
				builder.setMessage("是否保存信息?");
				builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if (isUpdate) {
						if (type_text.getText().toString().equals("选择问题类型") || line_text.getText().toString().equals("选择线路") || pile_text.getText().toString().equals("选择桩号")
								||distance_text.getText().toString().equals("未填写") || distance_text.getText().toString().equals("填写偏移量")) {
							Toast.makeText(Problem.this, "带*的为必填项，请填写完再试！", 1000).show();
						} else {
							save(0);
						}
					} else {
						getCoordinate();
						if (type_text.getText().toString().equals("选择问题类型") || line_text.getText().toString().equals("选择线路") || pile_text.getText().toString().equals("选择桩号")
								||distance_text.getText().toString().equals("未填写") || distance_text.getText().toString().equals("填写偏移量")) {
							Toast.makeText(Problem.this, "带*的为必填项，请填写完再试！", 1000).show();
						} else {
							cor = 3;
						}
					}
				}
			}).setNegativeButton("否", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						Intent reset = new Intent();
						reset.setClass(Problem.this, Problem.class);
						startActivity(reset);
					}
				}).show();

			
			
			// type_text.setText("选择问题类型");
			// date_text.setText("日期");
			// time_text.setText("时间");
			// line_text.setText("选择线路");
			// pile_text.setText("选择桩号");
			// distance_text.setText("填写偏移量");
			// comment_text.setText("填写问题描述");
			// location_text.setText("填写问题发生地点");
			// plan_text.setText("填写问题解决方案");
			// result_text.setText("填写问题解决情况");
			//
			// distance_text.setTextColor(Color.BLACK);
			// comment_text.setTextColor(Color.BLACK);
			// location_text.setTextColor(Color.BLACK);
			// plan_text.setTextColor(Color.BLACK);
			// result_text.setTextColor(Color.BLACK);
			// imageList = null;
			// commentList = null;
			// lon = "";
			// lat = "";

			// imageview.setImageBitmap(null);
			break;
		// case R.id.problem_lin_comment:
		//
		// Intent comment=new Intent();
		// comment.putExtra("flag", TypeQuest.PROBLEM_COMMENT);
		// comment.putExtra("Tittle", "问题描述");
		// comment.putExtra("text", comment_text.getText().toString());
		// comment.setClass(Problem.this, DialogEditActivity.class);
		// startActivityForResult(comment, TypeQuest.PROBLEM_COMMENT);
		// break;

		}

	}

	/**
	 * 初始化ListView 组件
	 */
	private void initListView() {
		problem_type = (RelativeLayout) findViewById(R.id.problem_type);
		problem_type.setOnClickListener(this);
		type_text = (TextView) findViewById(R.id.problem_type_text);

		problem_line = (RelativeLayout) findViewById(R.id.problem_line);
		problem_line.setOnClickListener(this);
		line_text = (TextView) findViewById(R.id.problem_line_text);

		problem_pile = (RelativeLayout) findViewById(R.id.problem_pile);
		problem_pile.setOnClickListener(this);

		pile_text = (TextView) findViewById(R.id.problem_pile_text);
		problem_distance = (RelativeLayout) findViewById(R.id.problem_distance);
		problem_distance.setOnClickListener(this);
		distance_text = (TextView) findViewById(R.id.problem_distance_text);

		/**
		 * 直接赋值
		 */
//		line_text.setText("段淇皓");
//		pile_text.setText("哈哈哈");
	}

	/**
	 * 拍照获取图片
	 */
	protected void doTakePhoto() {
		try {
			// Launch camera to take photo for selected contact
			if (!PHOTO_DIR.exists()) {
				PHOTO_DIR.mkdirs();// 创建照片的存储目录
			}
			FileName = System.currentTimeMillis() + ".jpg";
			mCurrentPhotoFile = new File(PHOTO_DIR, FileName);
			final Intent intent = getTakePickIntent(mCurrentPhotoFile);
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "photoPickerNotFoundText", Toast.LENGTH_LONG).show();
		}
	}

	public static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

	/**
	 * 从相册获取图片
	 */
	protected void doPickPhotoFromGallery() {
		try {
			final Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "photoPickerNotFoundText", Toast.LENGTH_LONG).show();
		}
	}

	public static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		return intent;
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		/*
		 * 从相册中获取
		 */
		case PHOTO_PICKED_WITH_DATA:
			Uri uri = data.getData();

			String selectedImagePath = getPath(uri);

			Intent intent1 = new Intent();
			intent1.setClass(getApplicationContext(), EditGallery.class);
			intent1.putExtra("image", selectedImagePath);
			startActivityForResult(intent1, EDIT_IMAGE_WITH_DATA);

			break;
		/*
		 * 从照相机中获取
		 */
		case CAMERA_WITH_DATA:

			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), EditGallery.class);
			File f = new File(PHOTO_DIR, FileName);
			intent.putExtra("image", f.getAbsolutePath());
			startActivityForResult(intent, EDIT_IMAGE_WITH_DATA);

			break;
		case EDIT_IMAGE_WITH_DATA:
			if (resultCode == Activity.RESULT_OK) {
				String imagepath = data.getStringExtra("image");
				// Bitmap bitimage = null;
				if (imagepath != null) {
					String comment = data.getStringExtra(imagepath);
					// if(imageList.size()<3){
					imageList.add(imagepath);
					commentList.add(comment);

					bm = getBitmap(imagepath);

					// ByteArrayOutputStream baos = new ByteArrayOutputStream();
					// 把数据写入文件
					// bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					// ByteArrayInputStream isBm = new
					// ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
					// bm = BitmapFactory.decodeStream(isBm, null,
					// null);//把ByteArrayInputStream数据生成图片
					imageview.setImageBitmap(bm);
					// }
					// else{
					// Toast.makeText(Problem.this, "照片数超过三张，请删除后再试",
					// 1000).show();
					// }
				}
			}

			break;
		case GALLERY_WITH_DATA:
			// bundle.putStringArrayList("image", imageList);
			// bundle.putStringArrayList("comment", commentList);
			//
			if (resultCode == Activity.RESULT_OK) {
				imageList = data.getStringArrayListExtra("image");
				commentList = data.getStringArrayListExtra("comment");

				if (imageList.size() > 0) {
					String[] temp = imageList.get(0).split("\\|");

					if (temp[0].equals("1")) // 拍照的文件路径
					{
						bm = getBitmap(temp[1]);
						imageview.setImageBitmap(bm);
					} else if (temp[0].equals("0"))// 从相片中获得的文件路径
					{
						bm = getBitmap(temp[1]);
						imageview.setImageBitmap(bm);
					}
				} else {
					imageview.setImageDrawable(getResources().getDrawable(R.drawable.imageview_bg));
				}
			}
			break;

		case TypeQuest.PROBLEM_TYPE:
			if (resultCode == Activity.RESULT_OK) {
				type_text.setText(data.getStringExtra("problem_type"));
			}
			break;

			//选择线路和桩号
		case TypeQuest.PROBLEM_LINE:
			if (resultCode == Activity.RESULT_OK) {
				line_text.setText(data.getStringExtra("line"));
				lineId = data.getStringExtra("lineId");
				pile_text.setText("选择桩号");
			}
			break;

		case TypeQuest.PROBLEM_PILE:
			if (resultCode == Activity.RESULT_OK) {
				pile_text.setText(data.getStringExtra("problem_pile"));
				markId = data.getStringExtra("markId");
				// lineId=data.getStringExtra("lineId");
			}
			break;

			//偏移量
		case TypeQuest.PROBLEM_DISTANCE:
			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("distance"))) {
					distance_text.setTextColor(Color.RED);
					distance_text.setText("未填写");

				} else {
					distance_text.setTextColor(Color.BLACK);
					distance_text.setText(data.getStringExtra("distance"));

				}
				// distance_text.setText(data.getStringExtra("distance"));
			}
			break;

		case TypeQuest.PROBLEM_COMMENT:

			if (resultCode == Activity.RESULT_OK) {
				if ("".equals(data.getStringExtra("comment"))) {
					comment_text.setTextColor(Color.RED);
					comment_text.setText("问题描述：未填写");

				} else {
					comment_text.setTextColor(Color.BLACK);
					comment_text.setText("问题描述：" + data.getStringExtra("comment"));
					str_comment_text = data.getStringExtra("comment");

				}
				// comment_text.setText("问题描述："+data.getStringExtra("comment"));
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
				linename = line_text.getText().toString();
				if (Tools.isNetworkAvailable(this, true)) {
					request.LineInfoSearchRequest(myApplication.userid, lineId);
					startProgressDialog();
				}
			}
			else{
				Toast.makeText(Problem.this, "请选择线路后查看", 1000).show();
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
			intent.setClass(Problem.this, ProblemHistory.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public Bitmap getBitmap(String path) {
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
		// int w = bitmap.getWidth();
		// int h = bitmap.getHeight();

		// Matrix matrix = new Matrix();
		// matrix.postRotate(90);
		// Bitmap bitmap1 = Bitmap.createBitmap(bitmap,
		// 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		// Bitmap bitmap2 = compressImage(bitmap);
		// int w2 = bitmap2.getWidth();
		// int h2 = bitmap2.getHeight();
		// int v = bitmap2.getRowBytes()*bitmap2.getHeight();
		// System.out.println(w+" "+h + " wwwwwww和hhhhh和vvvvv:" + v); //after
		// zoom
		return bitmap;
	}

	public void save(int flag) {
		
				ContentValues values = new ContentValues();
				OperationDB operationDB = new OperationDB(getApplicationContext());
				String photopath = "";
				String despath = "";
				values.put("guid", guid);
				values.put("problemtype", type_text.getText().toString());

			values.put("lat", lat);
			values.put("lon", lon);
			
			for (int i = 0; i < imageList.size(); i++) {
				photopath = photopath + imageList.get(i) + "#";
				if (commentList.size() > 0) {
					despath = despath + commentList.get(i) + "#";
				}

			}
			String uploadtime = null;
			System.out.println(date_text.getText().toString() + "-----------------时间");
			if (!"日期".equals(date_text.getText().toString())) {
				uploadtime = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
						+ c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY) + ":"
						+ c.get(Calendar.MINUTE) + ":00";
			} else {
				uploadtime = uptime;
			}
			values.put("occurtime",uploadtime);
			values.put("photopath", photopath);
			values.put("photodes", despath);
			values.put("line", line_text.getText().toString());
			values.put("lineid", lineId);
			values.put("pile", pile_text.getText().toString());
			values.put("pileid", markId);
			if (distance_text.getText().toString().equals("未填写")
					|| distance_text.getText().toString().equals("填写偏移量")) {
				values.put("offset", "");
			} else {
				values.put("offset", distance_text.getText().toString());
			}
			values.put("userid", myApplication.userid);
			values.put("departmentid", myApplication.depterid);
			values.put("uploadtime", uploadtime);
			System.out.println("---------------------" + uploadtime + "-----++++++++++++++");

//			int size1 = comment_text.getText().length();
//			int size2 = location_text.getText().length();
//			int size3 = plan_text.getText().length();
//			int size4 = result_text.getText().length();
			// values.put("problemdes", comment_text.getText().toString()
			// .substring(5, size1));
			// values.put("occurplace", location_text.getText().toString()
			// .substring(7, size2));
			// values.put("solution",
			// plan_text.getText().toString().substring(7, size3));
			// values.put("situation",
			// result_text.getText().toString().substring(7, size4));

			values.put("problemdes", problem_comment_edit.getText().toString());
			values.put("isupload", flag);

			// str_comment_text = comment_text.getText().toString()
			// .substring(5, size1);
			// str_location_text = location_text.getText().toString()
			// .substring(7, size2);
			// str_plan_text = plan_text.getText().toString().substring(7,
			// size3);
			// str_result_text = result_text.getText().toString()
			// .substring(7, size4);

			str_comment_text = problem_comment_edit.getText().toString();

				startProgressDialog();
				operationDB.InsertOrUpdate(values, Type.PROBLEM_UPLOAD);
				Message msg = new Message();
				msg.arg1 = 12;
				Myhandler.sendMessage(msg);
				fresh();

	}

	private void fresh(){
		guid = java.util.UUID.randomUUID().toString();
		cor = 0;
		uptime = sDateFormat.format(new java.util.Date());
		if(uptime.length() == 19){
			date_text.setText(uptime.substring(0, 10));
			time_text.setText(uptime.substring(11, 16));
			}
		problem_comment_edit.setText("");
		imageList.clear();
		commentList.clear();
		imageview.setImageBitmap(null);
		lon = "";
		lat = "";
		distance_text.setText("填写偏移量");
	}
	// @Override
	// protected void onPause() {
	// // TODO Auto-generated method stub
	// super.onPause();
	// if(bm!=null)
	// {
	// recycle();
	// }
	//
	// }
	private Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 50;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	// private Bitmap revitionImageSize(String path, int size) throws
	// IOException {
	// // 取得图片
	// InputStream temp = this.getAssets().open(path);
	// BitmapFactory.Options options = new BitmapFactory.Options();
	// // 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
	// options.inJustDecodeBounds = true;
	// // 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
	// BitmapFactory.decodeStream(temp, null, options);
	// // 关闭流
	// temp.close();
	//
	// // 生成压缩的图片
	// int i = 0;
	// Bitmap bitmap = null;
	// while (true) {
	// // 这一步是根据要设置的大小，使宽和高都能满足
	// if ((options.outWidth >> i <= size)
	// && (options.outHeight >> i <= size)) {
	// // 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
	// temp = this.getAssets().open(path);
	// // 这个参数表示 新生成的图片为原始图片的几分之一。
	// options.inSampleSize = (int) Math.pow(2.0D, i);
	// // 这里之前设置为了true，所以要改为false，否则就创建不出图片
	// options.inJustDecodeBounds = false;
	//
	// bitmap = BitmapFactory.decodeStream(temp, null, options);
	// break;
	// }
	// i += 1;
	// }
	// return bitmap;
	// }

	/**
	 * 存储图像的工具
	 *
//	 * @param FileUri
//	 * @param bitmap
	 */
	// public static void saveFile(String FileUri, Bitmap bitmap) {
	// File file = new File(FileUri);
	// if (file.exists()) {
	// file.delete();
	// }
	// try {
	// file.createNewFile();
	// } catch (IOException e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	// FileOutputStream fOut = null;
	// try {
	// fOut = new FileOutputStream(file);
	// } catch (FileNotFoundException e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
	// try {
	// fOut.flush();
	// fOut.close();
	// } catch (IOException e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	// }

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
		// // currentLocation.setTextColor(Color.WHITE);
		// // currentLocation.setTextSize(15);
		// // currentLocation.setText("已获坐标");
		// // currentLocation.setBackgroundResource(R.drawable.button_bg_02);
		// mLocationClient.stop();
		// // Toast.makeText(KeypointAcquisition.this,
		// // "坐标是:" + lon + ";" + lat, 1000).show();
		// }
		//
		// }
		//
		// };
		// location1 = new MyLocation(this, handler);
		// mLocationClient = location1.mLocationClient;
		// mLocationClient.start();
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
				if (cor != 0) {
					if (cor == 1) {
						save(0);
						locationManager.removeUpdates(locationListener);
					} else if (cor == 2) {
						if (Tools.isNetworkAvailable(getApplicationContext(), true)) {
							String uploadtime = null;
							if (date_text.getText().toString() != "日期") {
								uploadtime = c.get(Calendar.YEAR) + "-"
										+ (c.get(Calendar.MONTH) + 1) + "-"
										+ c.get(Calendar.DAY_OF_MONTH) + " "
										+ c.get(Calendar.HOUR_OF_DAY) + ":"
										+ c.get(Calendar.MINUTE) + ":00";
							} else {
								uploadtime = uptime;
							}
							// String str = date_text.getText().toString();
							str_comment_text = problem_comment_edit.getText().toString();
							startProgressDialog();
							request.problemUploadRequest(guid, myApplication.userid, lineId,
									uploadtime, str_distance_text, type_text.getText().toString(),
									str_comment_text, imageList, myApplication.depterid,
									uploadtime, lon, lat, commentList, markId, str_location_text,
									str_plan_text, str_result_text, "");
						} else {
							save(0);
						}
						locationManager.removeUpdates(locationListener);
					}else if(cor == 3){
						save(0);
						finish();
						Intent reset = new Intent();
						reset.setClass(Problem.this, Problem.class);
						startActivity(reset);
						locationManager.removeUpdates(locationListener);
					}
				}else {
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locationManager.removeUpdates(locationListener);
	}

	
}
