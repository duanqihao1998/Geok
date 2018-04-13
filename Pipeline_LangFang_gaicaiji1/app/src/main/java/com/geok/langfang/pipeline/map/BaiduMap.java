package com.geok.langfang.pipeline.map;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MKOLSearchRecord;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;
import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.function.DrawLayer;
import com.geok.langfang.function.MyLocation;
import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.Freqinfo;
import com.geok.langfang.jsonbean.GpsHui;
import com.geok.langfang.jsonbean.GpsLine;
import com.geok.langfang.jsonbean.GpsLocation;
import com.geok.langfang.jsonbean.GpsMarker;
import com.geok.langfang.jsonbean.GpsUser;
import com.geok.langfang.jsonbean.MyTaskQueryBean;
import com.geok.langfang.jsonbean.Taskinfo;
import com.geok.langfang.pipeline.ApplicationApp;
import com.geok.langfang.pipeline.Login;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.statistics.EventupStatisticsActivity;
import com.geok.langfang.pipeline.statistics.TreeElement;
import com.geok.langfang.pipeline.statistics.TreeView;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.util.Calculate;
import com.geok.langfang.util.DisplayUtils;
import com.geok.langfang.util.MyDialog;
import com.geok.langfang.util.MyPopeWindow;
import com.geok.langfang.util.UserDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class BaiduMap extends Activity implements OnClickListener, MKOfflineMapListener {

	public static List<Freqinfo> freqinfos=new ArrayList<>();

	EditText bustext;
	UserDialog busDialog;
	MapController mMapController;
	MKMapViewListener mMapListener = null;
	boolean isUse = false;//是否使用在线地图true为使用，false为不使用

	private MKOfflineMap mOffline = null;
	List<MKOLUpdateElement> localMapList;
	ImageView map_monitor, map_locus, map_search, map_other;
	private MapController mapController = null;
	MyPopeWindow myPopeWindow;
	PopupWindow popupWindow;
	LinearLayout linearLayout;
	MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	String city = "北京";
	TextView map_message;
	String other_type = "";
	Button map_other_move;

	/**
	 * 用来保存屏幕的像素
	 */
	int WindowWidth, WindowHeight;
	MKMapViewListener mapViewListener;

	Calculate calculate;

	LocationClient mLocClient;// 位置
	/**
	 * 功能类引用
	 */
	MyLocation myLocation;
	DrawLayer drawLayer;
	ApplicationApp app;

	Projection projection;
	/** 根据地图的移动刷新数据 */
	private int width = 0;
	private int height = 0;
	View mPopView = null; // 点击mark时弹出的气泡View
	Button image;// pop
	Drawable markerMap; // 得到需要标在地图上的资源
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String uptime = sDateFormat.format(new java.util.Date());
	private static CustomProgressDialog progressDialog = null; // 自定义的进度条的对象

	// ArcGISTiledMapServiceLayer tileLayer;

	static ProgressDialog arcdialog;

	// private ArrayList<Point> points = new ArrayList<Point>();;// 记录全部点
	private int index1;
	private int index2;

	Request request;// 网络请求

	boolean isPile = false;// false为桩，true为管线

	List<GpsUser> gpsUserList = new ArrayList<GpsUser>();// 实时监控在线人员集合
	List<GpsLine> gpsLineList = new ArrayList<GpsLine>();// 查询管线集合
	List<GpsHui> gpsHuis=new ArrayList<>();//轨迹回放点集合
	TextView monitor_map_online_text;// 显示当前在线人数
	TextView monitor_map_offline_text;// 显示当前离线人数

	String type = "桩号";// 点击显示的类型

	TextView locus_person;// 轨迹回放选择人员
	TextView locus_date;// 轨迹回放选择日期

	Calendar c = null;

	Timer timer = new Timer();
	MyTask myTask;
	MyApplication myApplication;
	private MapView mapView;

	private class MyTask extends TimerTask {

		@Override
		public void run() {
			if (Tools.isNetworkAvailable(BaiduMap.this)) {
				request.getUserXY(myApplication.depterid);
				request.getUserNum(myApplication.depterid);
			}

		}

	}

	SharedPreferences spf;// 本地文件
	Editor editor;// 修改本地文件
	GeoPoint initPoint = null;// 部门所在位置
	int offlineNum = 0;
	String offlineCheck = "N";
	Double x = 0.0;
	Double y = 0.0;

	String date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 使用地图sdk前需先初始化BMapManager. BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
		 * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
		 */
		app = (ApplicationApp) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(getApplicationContext());
			/**
			 * 如果BMapManager没有初始化则初始化BMapManager
			 */
			app.mBMapManager.init(ApplicationApp.strKey, new ApplicationApp.MyGeneralListener());
		}
		setContentView(R.layout.baidumap);
		myApplication = new MyApplication(this);

		OperationDB operationDB=new OperationDB(this);
		Cursor cursor = operationDB.DBselect(Type.GPS_UPLOCAD);
		while (cursor.moveToNext()) {
			String lon = cursor.getString(cursor.getColumnIndex("LON"));
			String lat = cursor.getString(cursor.getColumnIndex("LAT"));
			String userid = cursor.getString(cursor.getColumnIndex("USERID"));
			String isupload = cursor.getString(cursor.getColumnIndex("ISUPLOAD"));
			String date = cursor.getString(cursor.getColumnIndex("DATE"));
			Log.i("baiduhhhhhh",lon+"---"+lat+"---"+userid+"---"+isupload+"--日期--"+date);
			GpsHui gpsHui=new GpsHui(lon,lat);
			gpsHuis.add(gpsHui);
		}

		spf = getSharedPreferences("save_password", MODE_PRIVATE);
		editor = spf.edit();
		x = Double.valueOf(spf.getString("x", "0.0"));
		y = Double.valueOf(spf.getString("y", "0.0"));

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		WindowHeight = dm.widthPixels;
		WindowWidth = dm.heightPixels;

		c = Calendar.getInstance();
		request = new Request(myHandler);

		mapView = findViewById(R.id.baidumapView);
		mMapController = mapView.getController();

		ImageView returnhhh = findViewById(R.id.returnhhh);
		returnhhh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
				Toast.makeText(BaiduMap.this, "返回上一级", Toast.LENGTH_SHORT).show();
			}
		});

//		if(spf.getString("offlineCheck", "N").equals("N")){
//			alertOfflineMap();
			mOffline = new MKOfflineMap();
			mOffline.init(mMapController, BaiduMap.this);


			//获取已下过的离线地图信息
			localMapList = mOffline.getAllUpdateInfo();
			if ( localMapList == null ){
				localMapList = new ArrayList<MKOLUpdateElement>();
			}
			if(localMapList.size()==0){
				startProgressDialog("");
				new Thread(new Runnable() {

					@Override
					public void run() {
						int num = mOffline.scan();
						offlineNum = num;
						myHandler.sendEmptyMessage(0);
					}
				}).start();
			}else{
				editor.putString("offlineCheck", "Y");
				editor.commit();
			}

//		}else{
//		}



		/**
		 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
		mMapListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
				/**
				 * 在此处理地图移动完成回调 缩放，平移等操作完成后，此回调被触发
				 */
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * 在此处理底图poi点击事件 显示底图poi名称并移动至该点 设置过：
				 * mMapController.enableClick(true); 时，此回调才能被触发
				 *
				 */
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(BaiduMap.this, title, Toast.LENGTH_SHORT).show();
					mMapController.animateTo(mapPoiInfo.geoPt);
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				/**
				 * 当调用过 mMapView.getCurrentMap()后，此回调会被触发 可在此保存截图至存储设备
				 */
			}

			@Override
			public void onMapAnimationFinish() {
				/**
				 * 地图完成带动画的操作（如: animationTo()）后，此回调被触发
				 */
			}

			/**
			 * 在此处理地图载完成事件
			 */
			@Override
			public void onMapLoadFinish() {
//				Toast.makeText(BaiduMap.this, "地图加载完成", Toast.LENGTH_SHORT).show();

			}
		};
		mapView.regMapViewListener(ApplicationApp.getInstance().mBMapManager, mMapListener);

		if (x != 0) {
			initPoint = new GeoPoint((int) (y * 1E6), (int) (x * 1E6));

			/**
			 * 设置地图缩放级别
			 */
			mMapController.setZoom(10);
			mMapController.animateTo(initPoint);

		}else{
			GeoPoint p ;
//	        double cLat = 39.945 ;
//	        double cLon = 116.404 ;
			double cLat = 43.82;
			double cLon = 87.62;

			//设置中心点为天安门
			p = new GeoPoint((int)(cLat * 1E6), (int)(cLon * 1E6));


			mMapController.setCenter(p);
		}

		IntentFilter filter = new IntentFilter("dialogdismiss");
		registerReceiver(receiver, filter);

		initView();
		// int w=getWindowManager().getDefaultDisplay().getWidth();
		// int h=getWindowManager().getDefaultDisplay().getHeight();
		mLocClient = new LocationClient(this);


		if(gpsHuis.size()>0){
			showLocus1(gpsHuis);
			showSearchMarker1(freqinfos);
		}else {
			showSearchMarker1(freqinfos);
		}
	}

	GeoPoint previousPoint = null;// 上一个点
	GeoPoint nextPoint = null;// 下一个点
	int pointNum = 0;
	double totalLength = 0.0;

	void initView() {
		linearLayout = (LinearLayout) findViewById(R.id.top_background);

		linearLayout.setVisibility(View.GONE);

		map_monitor = (ImageView) findViewById(R.id.map_monitor);
		map_locus = (ImageView) findViewById(R.id.map_locus);
		map_search = (ImageView) findViewById(R.id.map_search);
		map_other = (ImageView) findViewById(R.id.map_other);
		map_message = (TextView) findViewById(R.id.map_message);
		map_message.getBackground().setAlpha(100);
		map_other_move = (Button) findViewById(R.id.map_other_move);
		map_other_move.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (map_other_move.getText().toString().equals("标记")) {
					map_other_move.setText("移动");
				} else {
					map_other_move.setText("标记");
				}
			}
		});
		// mLocClient = new LocationClient(this);
		// initLocation();

		map_monitor.setOnClickListener(this);
		map_locus.setOnClickListener(this);
		map_search.setOnClickListener(this);
		map_other.setOnClickListener(this);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		// 创建点击mark时的弹出泡泡
		mPopView = super.getLayoutInflater().inflate(R.layout.popview, null);
		mapView.addView(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.TOP_LEFT));
		mapView.invalidate();
		image = (Button) mPopView.findViewById(R.id.image);
		mPopView.setVisibility(View.GONE);

		markerMap = BaiduMap.this.getResources().getDrawable(R.drawable.point);
		mapView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				int iAction = event.getAction();
				if (iAction == MotionEvent.ACTION_DOWN) {
					if (other_type.equals("测距") && map_other_move.getText().toString().equals("标记")) {
						previousPoint = nextPoint;
						nextPoint = mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());
						markerMap = BaiduMap.this.getResources().getDrawable(R.drawable.point);
						if (previousPoint != null && nextPoint != null) {

							mapView.getOverlays().add(new OverItemT(markerMap, BaiduMap.this, previousPoint, previousPoint.getLongitudeE6() + "," + previousPoint.getLatitudeE6(), mapView));
							mapView.getOverlays().add(new OverItemT(markerMap, BaiduMap.this, nextPoint, nextPoint.getLongitudeE6() + "," + nextPoint.getLatitudeE6(), mapView));
							DistanceUtil util = new DistanceUtil();
							totalLength = totalLength + util.getDistance(previousPoint, nextPoint);
							String length = String.valueOf(totalLength);
							map_message.setText(length + "米");
							map_message.setVisibility(View.VISIBLE);

							GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mapView);
							mapView.getOverlays().add(graphicsOverlay);

							GeoPoint pt1 = null;
							GeoPoint[] points = new GeoPoint[2];
							points[0] = previousPoint;
							points[1] = nextPoint;
							// 构建线
							Geometry lineGeometry = new Geometry();
							lineGeometry.setPolyLine(points);

							// 设定样式
							Symbol lineSymbol = new Symbol();
							Symbol.Color lineColor = lineSymbol.new Color();
							lineColor.red = 255;
							lineColor.green = 0;
							lineColor.blue = 0;
							lineColor.alpha = 255;
							lineSymbol.setLineSymbol(lineColor, 5);
							// 生成Graphic对象
							Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
							graphicsOverlay.setData(lineGraphic);
							mapView.refresh();
						} else if (nextPoint != null) {

							// mapView.getOverlays().add(new
							// OverItemT(markerMap, BaiduMap.this, nextPoint,
							// nextPoint.getLongitudeE6()+","+nextPoint.getLatitudeE6(),
							// mapView));
							mapView.getOverlays().add(new OverItemT(markerMap, BaiduMap.this, nextPoint, nextPoint.getLongitudeE6() + "," + nextPoint.getLatitudeE6(), mapView));
							mapView.refresh();
						}
					}
				}

				return false;
			}
		});

	}

	public void setCenter() {
		GeoPoint point = null;
		ArrayList<MKOLSearchRecord> records = mOffline.searchCity(city);
		if (records == null || records.size() != 1)
			return;
		MKOLUpdateElement element = mOffline.getUpdateInfo(records.get(0).cityID);
		if (element != null) {
			if (element.geoPt != null)

				mMapController.setCenter(element.geoPt);
			point = element.geoPt;
		}
		if (point != null) {
			mMapController.setCenter(point);
		}

	}

	public void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("gpj02");// 返回国测局经纬度坐标系 coor=gcj02
		option.setProdName("定位Test");
		option.setScanSpan(10000);
		option.setPriority(LocationClientOption.GpsFirst);// GPS定位优先
		mLocClient.setLocOption(option);
		mLocClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceivePoi(BDLocation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location != null) {
					// 显示定位的结果
					showLocation(location);
				}
				return;

			}
		});// 发起定位，异步获取当前位置。

	}

	public void showLocation(BDLocation bdLocation) {

		// mapView.invalidate();
	}

	@Override
	protected void onPause() {
		super.onPause();

		mapView.onPause();
		mLocClient.stop();
		if (myTask != null) {
			myTask.cancel();
		}
	}

	public void alertOfflineMap() {
		AlertDialog.Builder builder = new Builder(BaiduMap.this);
		builder.setTitle("温馨提示");
		builder.setMessage("没有检测到离线数据，在线地图将产生流量消耗，是否切换到在线模式？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
				isUse = true;
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		}).show();
		// final Dialog dialog = new Dialog(BaiduMap.this);
		// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.setContentView(R.layout.map_alert);
		// dialog.show();
		// Button map_alert_y = (Button)dialog.findViewById(R.id.map_alert_y);
		// Button map_alert_d = (Button)dialog.findViewById(R.id.map_alert_d);
		// Button map_alert_qx = (Button)dialog.findViewById(R.id.map_alert_qx);
		// map_alert_y.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// dialog.cancel();
		// GeoPoint p ;
		// double cLat = 39.945 ;
		// double cLon = 116.404 ;
		// Intent intent = getIntent();
		// if ( intent.hasExtra("x") && intent.hasExtra("y") ){
		// //当用intent参数时，设置中心点为指定点
		// Bundle b = intent.getExtras();
		// p = new GeoPoint(b.getInt("y"), b.getInt("x"));
		// }else{
		// //设置中心点为天安门
		// p = new GeoPoint((int)(cLat * 1E6), (int)(cLon * 1E6));
		// }
		//
		// mMapController.setCenter(p);
		//
		// }
		// });
		// map_alert_d.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// mOffline = new MKOfflineMap();
		// mOffline.init(mMapController, BaiduMap.this);
		// startProgressDialog("");
		//
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// int num = mOffline.scan();
		// offlineNum = num;
		// myHandler.sendEmptyMessage(0);
		// }
		// }).start();
		// }
		// });
		// map_alert_qx.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// finish();
		// }
		// });

	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();

		getCity();
		// setCenter();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mOffline != null) {
			mOffline.destroy();
		}

		mapView.destroy();
		// if (app.mBMapManager != null) {
		// app.mBMapManager.destroy();
		// app.mBMapManager = null;
		// }
		mLocClient.stop();
		if (myTask != null) {
			myTask.cancel();
		}
		unregisterReceiver(receiver);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.map_monitor:

				setEmpty();

				if (myTask != null) {
					myTask.cancel();
				}
				linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.top_bg_01));

				myPopeWindow = new MyPopeWindow(getApplicationContext(), R.layout.monitor);
				popupWindow = myPopeWindow.getPopupWindow();
				popupWindow.showAsDropDown(linearLayout);

				View monitorView = myPopeWindow.getView();
				monitor_map_online_text = (TextView) monitorView.findViewById(R.id.monitor_map_online_text);
				monitor_map_offline_text = (TextView) monitorView.findViewById(R.id.monitor_map_offline_text);
				if (Tools.isNetworkAvailable(BaiduMap.this, true)) {
					startProgressDialog();
					myTask = new MyTask();
					timer.schedule(myTask, 0, 30 * 1000);
				}

				break;
			case R.id.map_locus:
				setEmpty();
				if (myTask != null) {
					myTask.cancel();
				}

				linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.top_bg_02));
				myPopeWindow = new MyPopeWindow(getApplicationContext(), R.layout.locus);
				popupWindow = myPopeWindow.getPopupWindow();
				popupWindow.showAsDropDown(linearLayout);

				View locusView = myPopeWindow.getView();
				locus_person = (TextView) locusView.findViewById(R.id.locus_person);
				locus_date = (TextView) locusView.findViewById(R.id.locus_date);
				if (uptime.length() == 19) {
					locus_date.setText(uptime.substring(0, 10));
				}
				ImageView locus_map_start = (ImageView) locusView.findViewById(R.id.locus_map_start);

				locus_person.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (MyApplication.person.equals("") || MyApplication.person.contains("ERR")) {
							if (Tools.isNetworkAvailable(BaiduMap.this, true)) {
								request.SelectPersonRequest(myApplication.userid);
								startProgressDialog();
							}
						} else {
							userData = MyApplication.person;
							showUserDialog();
						}

					}
				});

				locus_date.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						new DatePickerDialog(BaiduMap.this, new OnDateSetListener() {
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
							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
					}
				});

				locus_map_start.setOnClickListener(new OnClickListener() {

					@SuppressLint("WrongConstant")
					@Override
					public void onClick(View v) {
						if (locus_person.getText().toString().equals("") || locus_date.getText().toString().equals("")) {
							Toast.makeText(BaiduMap.this, "人员和日期不能空", 2000).show();
						} else {
							if (Tools.isNetworkAvailable(BaiduMap.this, true)) {
								startProgressDialog();
								request.getLocusByUser(TreeView.getUserNameList(), locus_date.getText().toString());
							}

						}

					}
				});
				break;
			case R.id.map_search:
				setEmpty();
				if (myTask != null) {
					myTask.cancel();
				}
				linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.top_bg_03));

				final Dialog dialog = new Dialog(BaiduMap.this);
				dialog.getWindow().setBackgroundDrawableResource(R.drawable.map_top_bg);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.search);
				dialog.show();
				final RadioButton search_pile = (RadioButton) dialog.findViewById(R.id.search_pile);
				final RadioButton search_line = (RadioButton) dialog.findViewById(R.id.search_line);
				final EditText search_edit = (EditText) dialog.findViewById(R.id.search_edit);
				Button search_button = (Button) dialog.findViewById(R.id.search_button);

				search_button.setOnClickListener(new View.OnClickListener() {

					@SuppressLint("WrongConstant")
					@Override
					public void onClick(View v) {
						if (!(search_edit.getText().toString().trim().equals(""))) {

							if (search_pile.isChecked()) {
								isPile = false;
								if (Tools.isNetworkAvailable(BaiduMap.this, true)) {
									startProgressDialog();
									request.getMarkerXY(search_edit.getText().toString().trim());
								}

							} else if (search_line.isChecked()) {
								isPile = true;
								if (Tools.isNetworkAvailable(BaiduMap.this, true)) {
									startProgressDialog();
									request.getLineXY(search_edit.getText().toString().trim());
								}

							}
						} else {
							Toast.makeText(BaiduMap.this, "搜索内容不能为空", 2000).show();
						}
						dialog.cancel();
					}
				});
				break;
			case R.id.map_other:
				setEmpty();
				if (myTask != null) {
					myTask.cancel();
				}
				linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.top_bg_04));
				myPopeWindow = new MyPopeWindow(getApplicationContext(), R.layout.other);
				popupWindow = myPopeWindow.getPopupWindow();
				popupWindow.showAsDropDown(linearLayout);
				View otherview = myPopeWindow.getView();
				ImageView other_map_distance = (ImageView) otherview.findViewById(R.id.other_map_distance);
				ImageView other_map_area = (ImageView) otherview.findViewById(R.id.other_map_area);
				other_map_area.setVisibility(View.GONE);
				// navi.setClickable(true);
				other_map_distance.setOnTouchListener(new View.OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						other_type = "测距";
						map_other_move.setVisibility(View.VISIBLE);
						return false;
					}
				});

				other_map_area.setOnTouchListener(new View.OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {

						return false;
					}
				});

				break;

		}

	}

	String userData;
	Handler myHandler = new Handler() {

		@SuppressLint("WrongConstant")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			stopProgressDialog();
			if (spf.getString("offlineCheck", "N").equals("N")&&!isUse) {
				if (offlineNum == 0) {
					alertOfflineMap();
					// Toast.makeText(BaiduMap.this, "请下载离线地图包再导入",
					// 2000).show();
				} else {
					editor.putString("offlineCheck", "Y");
					editor.commit();
					if (x != 0) {
						initPoint = new GeoPoint((int) (y * 1E6), (int) (x * 1E6));
						mMapController.animateTo(initPoint);
					}
				}
			}
			switch (msg.arg1) {
				case 42:
					if (msg.getData().getInt("flag") == 1) {
						userData = msg.getData().getString("result");
						if (!userData.contains("ERR")) {
							showUserDialog();
							MyApplication.person = userData;
						} else {
							Toast.makeText(BaiduMap.this, "出现异常，请与管理员联系", 1000).show();
						}
					} else {
						Toast.makeText(BaiduMap.this, "网络连接错误", 1000).show();
					}
					break;
				case 49:
					if (msg.getData().getInt("flag") == 1) {
						String data = msg.getData().getString("result");
						if (data.trim().equals("-1")) {

							Toast.makeText(BaiduMap.this, "暂时没有实时数据", 2000).show();

						} else {
							List<GpsUser> list = Json.getUserXY(data);
							showMonitor(list);
							monitor_map_online_text.setText(list.size() + "");
						}
					} else {
						Toast.makeText(BaiduMap.this, "请检查网络", 2000).show();
					}
					break;
				case 50:
					if (msg.getData().getInt("flag") == 1) {
						String data = msg.getData().getString("result");
						if (data.trim().equals("-1")) {

							Toast.makeText(BaiduMap.this, "暂时没有实时数据", 2000).show();

						} else {
							monitor_map_offline_text.setText(data);
						}
					} else {
						Toast.makeText(BaiduMap.this, "请检查网络", 2000).show();
					}
					break;
				case 52:
					if (msg.getData().getInt("flag") == 1) {
						String data = msg.getData().getString("result");
						if (data.trim().equals("-1")) {
							if (isPile) {
								Toast.makeText(BaiduMap.this, "没有这条管线的数据，请检查输入是否属于正确", 2000).show();
							} else {
								Toast.makeText(BaiduMap.this, "没有这个桩的数据，请检查输入是否属于正确", 2000).show();
							}

						} else {
							List<GpsMarker> marker = Json.getMarkerList(data);
							if (marker.size() > 0) {
								showSearchMarker(marker);
							} else {
								if (isPile) {
									Toast.makeText(BaiduMap.this, "没有这条管线的数据，请检查输入是否属于正确", 2000).show();
								} else {
									Toast.makeText(BaiduMap.this, "没有这个桩的数据，请检查输入是否属于正确", 2000).show();
								}
							}

						}
					} else {
						Toast.makeText(BaiduMap.this, "请检查网络", 2000).show();
					}

					break;
				case 54:
					if (msg.getData().getInt("flag") == 1) {
						String data = msg.getData().getString("result");
						Log.i("桩",data);
						if (data.trim().equals("-1")) {
							if (isPile) {
								Toast.makeText(BaiduMap.this, "没有这条管线的数据，请检查输入是否属于正确", 2000).show();
							} else {
								Toast.makeText(BaiduMap.this, "没有这个桩的数据，请检查输入是否属于正确", 2000).show();
							}

						} else {
							List<GpsLine> list = Json.getLineMarkerList(data);
							if (list.size() > 0) {
								showSearchLine(list);

							} else {
								if (isPile) {
									Toast.makeText(BaiduMap.this, "没有这条管线的数据，请检查输入是否属于正确", 2000).show();
								} else {
									Toast.makeText(BaiduMap.this, "没有这个桩的数据，请检查输入是否属于正确", 2000).show();
								}
							}

						}
					} else {
						Toast.makeText(BaiduMap.this, "请检查网络", 2000).show();
					}
					break;
				case 55:
					if (msg.getData().getInt("flag") == 1) {
						String data = msg.getData().getString("result");
						if (data.trim().equals("-1")) {
							Toast.makeText(BaiduMap.this, "暂时没有这个人员的巡检轨迹", 2000).show();
						} else {
							//换成本地数据
							List<GpsUser> list = Json.getLocusUser(data);
							if (list.get(0).getGpsList().size() > 0) {
								showLocus(list);
							} else {
								Toast.makeText(BaiduMap.this, "暂时没有这个人员的巡检轨迹", 2000).show();
							}

						}
					} else {
						Toast.makeText(BaiduMap.this, "请检查网络", 2000).show();
					}
					break;
			}
		}

	};

	TreeView treeView;
	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			dialog.dismiss();
		}

	};
	Dialog dialog;

	public void showUserDialog() {
		dialog = new Dialog(BaiduMap.this);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.userselection);

		treeView = (TreeView) dialog.findViewById(R.id.userSelectionView);
		getTreeList();
		treeView.initialData("locus");
		dialog.setOnDismissListener(new OnDismissListener() {

			public void onDismiss(DialogInterface dialog) {
				locus_person.setText(TreeView.getUserNameList());
			}
		});
		dialog.show();
	}

	public void getTreeList() {
		try {
			JSONArray ja = new JSONArray(userData);
			TreeElement element = new TreeElement(ja.getJSONObject(0).getString("eventid"), ja.getJSONObject(0).getString("name"));
			element.setNotetype(ja.getJSONObject(0).getString("notetype"));
			addChild(element, ja.getJSONObject(0));
			treeView.mPdfOutlinesCount.add(element);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// TYPE改成notetype,CHILDREDS改成mysubChild,org改成1；notetype为1时时公司，为0时为个人
	public void addChild(TreeElement element, JSONObject ob) {
		try {
			// if ("1".equals(ob.getString("notetype").trim())) {
			JSONArray jsonArray = ob.getJSONArray("mysubChild");
			if (jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject ob1 = jsonArray.getJSONObject(i);
					TreeElement element1 = new TreeElement(ob1.getString("eventid"), ob1.getString("name"));
					element1.setNotetype(ob1.getString("notetype"));
					element.addChild(element1);
					JSONArray jsonArray2 = ob1.getJSONArray("mysubChild");
					for (int j = 0; j < jsonArray2.length(); j++) {
						JSONObject ob2 = jsonArray2.getJSONObject(j);
						TreeElement element2 = new TreeElement(ob2.getString("eventid"), ob2.getString("name"));
						element2.setNotetype(ob2.getString("notetype"));
						element1.addChild(element2);
						JSONArray jsonArray3 = ob2.getJSONArray("mysubChild");
						for (int k = 0; k < jsonArray3.length(); k++) {
							JSONObject ob3 = jsonArray3.getJSONObject(k);
							TreeElement element3 = new TreeElement(ob3.getString("eventid"), ob3.getString("name"));
							element3.setNotetype(ob3.getString("notetype"));
							element2.addChild(element3);
						}
					}
				}
			}
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @功能描述 展示轨迹回放
	 * @author 张龙飞Email:longfeiz@geo-k.cn Tel:13671277587
	 * @param list
	 * @createDate 2013-12-1 下午4:02:15
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void showLocus(List<GpsUser> list) {

		GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mapView);
		mapView.getOverlays().add(graphicsOverlay);

		GeoPoint pt1 = null;
		List<GeoPoint[]> listLinePoint = new ArrayList<GeoPoint[]>();
		List<GeoPoint> startEnd = new ArrayList<GeoPoint>();
		for (int i = 0; i < list.size(); i++) {
			// 构建线
			Geometry lineGeometry = new Geometry();
			// 设定折线点坐标
			GeoPoint[] linePoints = new GeoPoint[list.get(i).getGpsList().size()];
			for (int j = 0; j < list.get(i).getGpsList().size(); j++) {
				GpsLocation gpsMarker = list.get(i).getGpsList().get(j);
				int lon = (int) (Double.valueOf(gpsMarker.getLON()) * 1E6);
				int lat = (int) (Double.valueOf(gpsMarker.getLAT()) * 1E6);
				GeoPoint pt = new GeoPoint(lat, lon);
				linePoints[j] = pt;
				if (i == 0 && j == 0) {
					pt1 = pt;
					startEnd.add(pt);
				}
				if (j == list.get(i).getGpsList().size() - 1) {
					startEnd.add(pt);
				}
			}
			listLinePoint.add(linePoints);
			lineGeometry.setPolyLine(linePoints);

			// 设定样式
			Symbol lineSymbol = new Symbol();
			Symbol.Color lineColor = lineSymbol.new Color();
			lineColor.red = 255;
			lineColor.green = 0;
			lineColor.blue = 0;
			lineColor.alpha = 255;
			lineSymbol.setLineSymbol(lineColor, 10);
			// 生成Graphic对象
			Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
			graphicsOverlay.setData(lineGraphic);

		}
		if (pt1 != null) {
			mMapController.animateTo(pt1);
		}
		Drawable start = BaiduMap.this.getResources().getDrawable(R.drawable.icon_st);
		Drawable end = BaiduMap.this.getResources().getDrawable(R.drawable.icon_en);
		mapView.getOverlays().add(new OverItemT(start, BaiduMap.this, startEnd.get(0), list.get(0).getNAME(), mapView));
		mapView.getOverlays().add(new OverItemT(end, BaiduMap.this, startEnd.get(1), list.get(0).getNAME(), mapView));
		mMapController.setZoom(13);

		mapView.refresh();

	}
	public void showLocus1(List<GpsHui> list) {

		GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mapView);
		mapView.getOverlays().add(graphicsOverlay);

		GeoPoint pt1 = null;
		List<GeoPoint[]> listLinePoint = new ArrayList<GeoPoint[]>();
		List<GeoPoint> startEnd = new ArrayList<GeoPoint>();
		for (int i = 0; i < list.size(); i++) {
			// 构建线
			Geometry lineGeometry = new Geometry();
			// 设定折线点坐标
			GeoPoint[] linePoints = new GeoPoint[list.size()];
			for (int j = 0; j < list.size(); j++) {
				GpsHui gpsMarker = list.get(j);
				int lon = (int) (Double.valueOf(gpsMarker.getLON()) * 1E6);
				int lat = (int) (Double.valueOf(gpsMarker.getLAT()) * 1E6);
				GeoPoint pt = new GeoPoint(lat, lon);
				linePoints[j] = pt;
				if (i == 0 && j == 0) {
					pt1 = pt;
					startEnd.add(pt);
				}
				if (j == list.size() - 1) {
					startEnd.add(pt);
				}
			}
			listLinePoint.add(linePoints);
			lineGeometry.setPolyLine(linePoints);

			// 设定样式
			Symbol lineSymbol = new Symbol();
			Symbol.Color lineColor = lineSymbol.new Color();
			lineColor.red = 255;
			lineColor.green = 0;
			lineColor.blue = 0;
			lineColor.alpha = 255;
			lineSymbol.setLineSymbol(lineColor, 10);
			// 生成Graphic对象
			Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
			graphicsOverlay.setData(lineGraphic);

		}
		if (pt1 != null) {
			mMapController.animateTo(pt1);
		}
		Drawable start = BaiduMap.this.getResources().getDrawable(R.drawable.icon_st);
		Drawable end = BaiduMap.this.getResources().getDrawable(R.drawable.icon_en);
		mapView.getOverlays().add(new OverItemT(start, BaiduMap.this, startEnd.get(0), "", mapView));
		mapView.getOverlays().add(new OverItemT(end, BaiduMap.this, startEnd.get(1), "", mapView));
		mMapController.setZoom(13);

		mapView.refresh();

	}

	/**
	 *
	 * @功能描述 展示搜索的当前在线人员
	 * @author 张龙飞Email:longfeiz@geo-k.cn Tel:13671277587
	 * @param monitorList
	 * @createDate 2013-12-1 下午1:33:15
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void showMonitor(List<GpsUser> monitorList) {
		type = "姓名";
		markerMap = BaiduMap.this.getResources().getDrawable(R.drawable.person);
		mapView.getOverlays().clear();
//		mapView.getOverlays().add(new OverItemT(markerMap, BaiduMap.this, monitorList, mapView));

		mapView.refresh();

	}
	/**
	 *
	 * @功能描述 展示需要搜索的关键点
	 * @author 张龙飞Email:longfeiz@geo-k.cn Tel:13671277587
	 * @param monitorList
	 * @createDate 2018-3-13 上午11:38:15
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void showSearchMarker1(List<Freqinfo> monitorList) {
		spf = getSharedPreferences("pile", Context.MODE_PRIVATE);// 保存关键点id的文件
		editor = spf.edit();
		// 获得eventid集合字符串
		String name = spf.getString("pileList", null);
		String[] idList = null;// eventid集合
		if (name != null) {
			idList = name.split(";");
		}
		// 获得无网保存的eventid集合字符串
		String saveName = spf.getString("savepileList", null);
		String[] saveidList = null;// eventid集合
		if (saveName != null) {
			saveidList = saveName.split(";");
		}
		if (idList != null) {
			for (int i = 0; i < freqinfos.size(); i++) {
				for (int j = 0; j < idList.length; j++) {
					//改变小旗子的颜色
					if (idList[j].equals(freqinfos.get(i).getEVENTID())) {
						markerMap = BaiduMap.this.getResources().getDrawable(R.drawable.greenpoint);
					}else {
						markerMap = BaiduMap.this.getResources().getDrawable(R.drawable.point);
					}
				}
			}
		}
		if (saveidList != null) {
			for (int i = 0; i < freqinfos.size(); i++) {
				for (int j = 0; j < idList.length; j++) {
					// 从保存的本地文件中判断是否已提交,如果已提交修改按钮背景，设置为不可点击
					if (idList[j].equals(freqinfos.get(i).getEVENTID())) {
						//改变小旗子的颜色
						if (idList[j].equals(freqinfos.get(i).getEVENTID())) {
							markerMap = BaiduMap.this.getResources().getDrawable(R.drawable.greenpoint);
						}else {
							markerMap = BaiduMap.this.getResources().getDrawable(R.drawable.point);
						}
					}
				}
			}
		}
		type = "位置";
//		for (int i = 0; i < monitorList.size(); i++) {
//			if(monitorList.get(i).getIsXun()==false){
//				markerMap = BaiduMap.this.getResources().getDrawable(R.drawable.point);
//			}else if(monitorList.get(i).getIsXun()==true){
//				markerMap = BaiduMap.this.getResources().getDrawable(R.drawable.greenpoint);
//			}
//		}
//		mapView.getOverlays().clear();
		mapView.getOverlays().add(new OverItemT(markerMap, BaiduMap.this, monitorList, mapView));

		mapView.refresh();

	}

	/**
	 *
	 * @功能描述 显示搜索到的桩
	 * @author 张龙飞Email:longfeiz@geo-k.cn Tel:13671277587
	 * @createDate 2013-11-29 下午1:57:31
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void showSearchMarker(List<GpsMarker> markerList) {
		type = "桩号";
		mapView.getOverlays().clear();
		markerMap = BaiduMap.this.getResources().getDrawable(R.drawable.point);
		mapView.getOverlays().add(new OverItemT(markerMap, BaiduMap.this, markerList, "", mapView));

		mapView.refresh();
	}

	/**
	 *
	 * @功能描述 展示搜索到的管线
	 * @author 张龙飞 Email:longfeiz@geo-k.cn Tel:13671277587
	 * @param list
	 * @createDate 2013-11-30 下午6:26:51
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void showSearchLine(List<GpsLine> list) {

		GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mapView);
		mapView.getOverlays().add(graphicsOverlay);

		GeoPoint pt1 = null;
		List<GeoPoint[]> listLinePoint = new ArrayList<GeoPoint[]>();
		for (int i = 0; i < list.size(); i++) {
			// 构建线
			Geometry lineGeometry = new Geometry();
			// 设定折线点坐标
			GeoPoint[] linePoints = new GeoPoint[list.get(i).getMarkerLsit().size()];
			for (int j = 0; j < list.get(i).getMarkerLsit().size(); j++) {
				GpsMarker gpsMarker = list.get(i).getMarkerLsit().get(j);
				int lon = (int) (Double.valueOf(gpsMarker.getLon()) * 1E6);
				int lat = (int) (Double.valueOf(gpsMarker.getLat()) * 1E6);
				GeoPoint pt = new GeoPoint(lat, lon);
				linePoints[j] = pt;
				if (i == 0 && j == 0) {
					pt1 = pt;
				}
			}
			listLinePoint.add(linePoints);
			lineGeometry.setPolyLine(linePoints);

			// 设定样式
			Symbol lineSymbol = new Symbol();
			Symbol.Color lineColor = lineSymbol.new Color();
			lineColor.red = 255;
			lineColor.green = 0;
			lineColor.blue = 0;
			lineColor.alpha = 255;
			lineSymbol.setLineSymbol(lineColor, 10);
			// 生成Graphic对象
			Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
			graphicsOverlay.setData(lineGraphic);

		}

		mMapController.setZoom(13);
		if (pt1 != null) {
			mMapController.animateTo(pt1);
		}
		mapView.refresh();
	}

	/**
	 *
	 * @功能描述 清空地图数据
	 * @author 张龙飞Email:longfeiz@geo-k.cn Tel:13671277587
	 * @createDate 2013-11-15 下午2:00:26
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void setEmpty() {
		mapView.getOverlays().clear();

		// points.clear();
		map_message.setVisibility(View.GONE);
//		mMapController.setZoom(13);
		other_type = "";
		mapView.refresh();
		map_other_move.setText("标记");
		map_other_move.setVisibility(View.GONE);
		previousPoint = null;// 上一个点
		nextPoint = null;// 下一个点
	}

	/**
	 *
	 * @功能描述 计算面积
	 * @author 张龙飞[zhanglongfei] Email:longfeiz@geo-k.cn Tel:13671277587
	 * @param dValue
	 * @return
	 * @createDate 2013-11-15 下午1:48:58
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	private String getAreaString(double dValue) {
		long area = Math.abs(Math.round(dValue));
		String sArea = "";
		// 顺时针绘制多边形，面积为正，逆时针绘制，则面积为负
		if (area >= 1000000) {
			double dArea = area / 1000000.0;
			sArea = Double.toString(dArea) + " 平方公里";
		} else
			sArea = Double.toString(area) + " 平方米";
		return sArea;
	}

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
		}
		progressDialog.show();
	}

	private void startProgressDialog(String message) {
		progressDialog = CustomProgressDialog.createDialog(this);
		progressDialog.setMessage("正在导入离线数据包...");
		progressDialog.show();
	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	static public class MyRunnable implements Runnable {
		public void run() {
			arcdialog.dismiss();
		}
	}

	class OverItemT extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();

		public OverItemT(Drawable marker, MapView mMapView) {
			super(marker, mMapView);

		}

		/**
		 * 搜索桩
		 *
		 * @param marker
		 * @param context
		 * @param list
		 * @param title
		 *            没用
		 * @param mMapView
		 */
		public OverItemT(Drawable marker, Context context, List<GpsMarker> list, String title, MapView mMapView) {
			super(marker, mMapView);
			removeAll();
			mGeoList.clear();
			GeoPoint pt1 = null;
			for (int i = 0; i < list.size(); i++) {
				int lon = (int) (Double.valueOf(list.get(i).getLon()) * 1E6);
				int lat = (int) (Double.valueOf(list.get(i).getLat()) * 1E6);
				GeoPoint pt = new GeoPoint(lat, lon);
				if (i == 0) {
					pt1 = pt;
				}
				OverlayItem item = new OverlayItem(pt, type + ":" + list.get(i).getMarkername(), "11");
				mGeoList.add(item);

			}
			// populate();

			mMapController.setZoom(12);
			if (pt1 != null) {
				mMapController.animateTo(pt1);
			}
			this.addItem(mGeoList);

			mMapView.refresh();
		}

		/**
		 * 实时监控
		 *
		 * @param markerm
		 * @param context
		 * @param monitorList
		 * @param mMapView
		 */
//		public OverItemT(Drawable markerm, Context context, List<GpsUser> monitorList, MapView mMapView) {
//			super(markerm, mMapView);
//			removeAll();
//			mGeoList.clear();
//			GeoPoint pt1 = null;
//			for (int i = 0; i < monitorList.size(); i++) {
//				int lon = (int) (Double.valueOf(monitorList.get(i).getLON()) * 1E6);
//				int lat = (int) (Double.valueOf(monitorList.get(i).getLAT()) * 1E6);
//				GeoPoint pt = new GeoPoint(lat, lon);
//
//				if (i == 0) {
//					pt1 = pt;
//				}
//				OverlayItem item = new OverlayItem(pt, type + ":" + monitorList.get(i).getNAME(), null);
//				mGeoList.add(item);
//
//			}
//
//			mMapController.setZoom(10);
//			if (pt1 != null) {
//				mMapController.animateTo(pt1);
//			}
//
//			this.addItem(mGeoList);
//			mMapView.refresh();
//		}
		public OverItemT(Drawable markerm, Context context, List<Freqinfo> monitorList, MapView mMapView) {
			super(markerm, mMapView);
			removeAll();
			mGeoList.clear();
			GeoPoint pt1 = null;
			for (int i = 0; i < monitorList.size(); i++) {
				int lon = (int) (Double.valueOf(monitorList.get(i).getLON()) * 1E6);
				int lat = (int) (Double.valueOf(monitorList.get(i).getLAT()) * 1E6);
				GeoPoint pt = new GeoPoint(lat, lon);

				if (i == 0) {
					pt1 = pt;
				}
				OverlayItem item = new OverlayItem(pt, type + ":" + monitorList.get(i).getPOINTNAME(), null);
				mGeoList.add(item);

			}

			mMapController.setZoom(10);
			if (pt1 != null) {
				mMapController.animateTo(pt1);
			}

			this.addItem(mGeoList);
			mMapView.refresh();
		}

		/**
		 *
		 * @param marker
		 * @param gpsLocation
		 * @param mMapView
		 */
		public OverItemT(Drawable marker, List<GpsLocation> gpsLocation, MapView mMapView) {
			super(marker, mMapView);
			mGeoList.clear();
			GeoPoint pt1 = null;
			for (int i = 0; i < gpsLocation.size(); i++) {
				int lon = (int) (Double.valueOf(gpsLocation.get(i).getLON()) * 1E6);
				int lat = (int) (Double.valueOf(gpsLocation.get(i).getLAT()) * 1E6);
				GeoPoint pt = new GeoPoint(lat, lon);

				if (i == 0) {
					pt1 = pt;
				}
				OverlayItem item = new OverlayItem(pt, type + ":" + gpsLocation.get(i).getText(), null);
				mGeoList.add(item);

			}


			mMapController.setZoom(10);

			if (pt1 != null) {
				mMapController.animateTo(pt1);
			}
			this.addItem(mGeoList);
			mMapView.refresh();
		}

		/**
		 * 轨迹回放起始点
		 *
		 * @param marker
		 * @param context
		 * @param pt
		 * @param title
		 * @param mMapView
		 */
		public OverItemT(Drawable marker, Context context, GeoPoint pt, String title, MapView mMapView) {
			super(marker, mMapView);
			// removeAll();
			OverlayItem item = new OverlayItem(pt, title, null);

			mGeoList.add(item);
			this.addItem(item);
			mMapView.refresh();
		}

		@Override
		protected OverlayItem createItem(int i) {
			System.out.println(i);
			return mGeoList.get(i);
		}

		@Override
		public int size() {
			return mGeoList.size();
		}

		int a;

		@Override
		protected boolean onTap(int i) {

			a = mGeoList.size();
			int b = a;
			// OverlayItem item = getItem(i);
			// item.getPoint();
			// GeoPoint pt = getItem(i).getPoint();
			GeoPoint pt = mGeoList.get(i).getPoint();
			mapView.updateViewLayout(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, pt, MapView.LayoutParams.BOTTOM_CENTER));
			image.setText(mGeoList.get(i).getTitle());
			mPopView.setVisibility(View.VISIBLE);
			return super.onTap(i);
		}

		@Override
		public boolean onTap(GeoPoint arg0, MapView arg1) {

			mPopView.setVisibility(View.GONE);

			return super.onTap(arg0, arg1);

		}

	}

	/**
	 * 获得所在城市
	 */
	public void getCity() {

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		// option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(5); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		mLocClient.setLocOption(option);
		mLocClient.start();
		mLocClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceivePoi(BDLocation arg0) {
				// TODO Auto-generated method stub

			}

			@SuppressLint("WrongConstant")
			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location != null) {
					city = location.getCity();
					double lon = location.getLongitude();
					System.out.println("city--------------" + city);
					mLocClient.stop();
				} else {
					Toast.makeText(BaiduMap.this, "无法获得当前所在城市", 1000).show();
				}
			}
		});
		mLocClient.start();
	}

	@Override
	public void onGetOfflineMapState(int type, int state) {
		switch (type) {
			case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
				// Log.d("OfflineDemo", String.format("cityid:%d update", state));
				MKOLUpdateElement update = mOffline.getUpdateInfo(state);

			}
			break;
			case MKOfflineMap.TYPE_NEW_OFFLINE:
				// Log.d("OfflineDemo", String.format("add offlinemap num:%d",
				// state));

				break;
			case MKOfflineMap.TYPE_VER_UPDATE:
				MKOLUpdateElement e = mOffline.getUpdateInfo(state);
				if (e != null) {
					// Log.d("OfflineDemo",
					// String.format("%d has new offline map: ",e.cityID));
				}
				break;
		}
	}

}
