package com.geok.langfang.pipeline.map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.platform.comapi.map.Projection;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.ags.geocode.Locator;
import com.esri.core.tasks.ags.geocode.LocatorFindParameters;
import com.esri.core.tasks.ags.geocode.LocatorGeocodeResult;
import com.geok.langfang.function.DrawLayer;
import com.geok.langfang.function.MyLocation;
import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.GpsLine;
import com.geok.langfang.jsonbean.GpsLocation;
import com.geok.langfang.jsonbean.GpsMarker;
import com.geok.langfang.jsonbean.GpsUser;
import com.geok.langfang.pipeline.ApplicationApp;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.statistics.TreeElement;
import com.geok.langfang.pipeline.statistics.TreeView;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.util.Calculate;
import com.geok.langfang.util.MyPopeWindow;
import com.geok.langfang.util.UserDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.com.esrichina.tianditu.TianDiTuLayer;
import cn.com.esrichina.tianditu.TianDiTuLayerTypes;

public class ArcMap extends Activity implements OnClickListener {

	EditText bustext;
	UserDialog busDialog;
	private MapView mapView = null;
	ImageView map_monitor, map_locus, map_search, map_other;
	private MapController mapController = null;
	MyPopeWindow myPopeWindow;
	PopupWindow popupWindow;
	LinearLayout linearLayout;
	MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	String city = "北京";
	TextView map_message;
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
	Drawable marker; // 得到需要标在地图上的资源
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String uptime = sDateFormat.format(new java.util.Date());
	private static CustomProgressDialog progressDialog = null; // 自定义的进度条的对象

	GraphicsLayer glLayer = null;
	// ArcGISTiledMapServiceLayer tileLayer;
	Locator locator;
	static Handler handler;

	static ProgressDialog arcdialog;

	private Geometry.Type geoType = null;// 用于判定当前选择的几何图形类型
	private ArrayList<Point> points = new ArrayList<Point>();;// 记录全部点
	private int index1;
	private int index2;
	private Graphic tempGraphic;
	private Point ptStart = null;// 起点
	private Point ptPrevious = null;// 上一点
	private Polygon tempPolygon = null;

	TianDiTuTiledMapServiceLayer t_vec;// 矢量
	TianDiTuTiledMapServiceLayer t_cva;// 标注

	Double totallength = 0.0;//测量总长度
	Layer mapLayer;
	Layer annotationLayer;

	Request request;// 网络请求

	boolean isPile = false;// false为桩，true为管线

	List<GpsUser> gpsUserList = new ArrayList<GpsUser>();// 实时监控在线人员集合
	List<GpsLine> gpsLineList = new ArrayList<GpsLine>();// 查询管线集合
	TextView monitor_map_online_text;// 显示当前在线人数
	TextView monitor_map_offline_text;// 显示当前离线人数

	String type = "桩号";// 点击显示的类型

	TextView locus_person;// 轨迹回放选择人员
	TextView locus_date;// 轨迹回放选择日期

	Calendar c = null;

	Callout callout;// 气泡

	Timer timer = new Timer();
	MyTask myTask;
	MyApplication myApplication;

	private class MyTask extends TimerTask {

		@Override
		public void run() {
			if (Tools.isNetworkAvailable(ArcMap.this)) {
				request.getUserXY(myApplication.depterid);
				request.getUserNum(myApplication.depterid);
			}

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.arcmap);
		myApplication = new MyApplication(this);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		WindowHeight = dm.widthPixels;
		WindowWidth = dm.heightPixels;
		handler = new Handler();
		c = Calendar.getInstance();
		request = new Request(myHandler);

		mapView = (MapView) findViewById(R.id.map);

		// t_vec = new
		// TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.VEC_C);
		// mapView.addLayer(t_vec);
		// t_cva = new
		// TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.CVA_C);
		// mapView.addLayer(t_cva);

		mapLayer = new TianDiTuLayer(TianDiTuLayerTypes.TIANDITU_VECTOR_MERCATOR);

		annotationLayer = new TianDiTuLayer(
				TianDiTuLayerTypes.TIANDITU_VECTOR_ANNOTATION_CHINESE_MERCATOR);
		mapView.addLayer(mapLayer);
		mapView.addLayer(annotationLayer);

		glLayer = new GraphicsLayer();
		/* create a @ArcGISTiledMapServiceLayer */
		// tileLayer = new ArcGISTiledMapServiceLayer(
		// "http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineStreetCold/MapServer");
		// // Add tiled layer to MapView
		// mapView.addLayer(tileLayer);
		mapView.addLayer(glLayer);

		IntentFilter filter = new IntentFilter("dialogdismiss");
		registerReceiver(receiver, filter);
		
		initView();

	}

	void initView() {
		linearLayout = (LinearLayout) findViewById(R.id.top_background);
		map_monitor = (ImageView) findViewById(R.id.map_monitor);
		map_locus = (ImageView) findViewById(R.id.map_locus);
		map_search = (ImageView) findViewById(R.id.map_search);
		map_other = (ImageView) findViewById(R.id.map_other);
		map_message = (TextView) findViewById(R.id.map_message);
		map_message.getBackground().setAlpha(100);
		
		
		mLocClient = new LocationClient(this);
		initLocation();

		map_monitor.setOnClickListener(this);
		map_locus.setOnClickListener(this);
		map_search.setOnClickListener(this);
		map_other.setOnClickListener(this);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;

		mapView.setOnZoomListener(new OnZoomListener() {

			@Override
			public void preAction(float paramFloat1, float paramFloat2, double paramDouble) {
			}

			@Override
			public void postAction(float paramFloat1, float paramFloat2, double paramDouble) {

			}
		});
		mapView.setOnSingleTapListener(new OnSingleTapListener() {

			@Override
			public void onSingleTap(float x, float y) {

				Point location = mapView.toMapPoint(x, y);
				points.add(location);
				if (callout != null && callout.isShowing()) {
					callout.hide();
				}
				int[] graphicIDs = glLayer.getGraphicIDs(x, y, 25);
				if (graphicIDs != null && graphicIDs.length > 0) {
					LayoutInflater inflater = LayoutInflater.from(ArcMap.this);
					View view = inflater.inflate(R.layout.callout, null);
					Graphic gr = glLayer.getGraphic(graphicIDs[0]);
					Map<String, Object> map = gr.getAttributes();
					if (map.get("name") != null) {
						callout = mapView.getCallout();
						// callout.setStyle(R.xml.calloutstyle);
						callout.setOffset(0, -15);
						TextView text = (TextView) view.findViewById(R.id.calloutText);
						text.setText(type + ":" + map.get("name"));
						callout.show(location, view);
					}
					// com.esri.core.geometry.Point location =
					// mapView.toMapPoint(
					// x, y);

				}

				if (geoType != null) {
					PictureMarkerSymbol locationPH = new PictureMarkerSymbol(ArcMap.this
							.getResources().getDrawable(R.drawable.point));
					SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.RED, 2,
							SimpleLineSymbol.STYLE.DASH);

					SimpleFillSymbol fillSymbol = new SimpleFillSymbol(Color.RED);
					fillSymbol.setAlpha(33);

					Line line = new Line();
					Graphic graphic1 = new Graphic(location, locationPH);
					glLayer.addGraphic(graphic1);
					if (ptStart == null) {
						ptStart = location;
//						Graphic graphic = new Graphic(location, locationPH);
//						glLayer.addGraphic(graphic);
					} else {
						// 生成当前线段（由当前点和上一个点构成）

						line.setStart(ptPrevious);
						line.setEnd(location);

						if (geoType == Geometry.Type.POLYLINE) {

							Graphic graphic = new Graphic(location, locationPH);
							glLayer.addGraphic(graphic);
							if (tempGraphic != null) {
								glLayer.removeGraphic(index1);
							}
							Polyline polyline = new Polyline();
							Point startPoint = null;
							Point endPoint = null;
							// 绘制完整的线段
							for (int i = 1; i < points.size(); i++) {
								startPoint = points.get(i - 1);
								endPoint = points.get(i);

								Line line1 = new Line();
								line1.setStart(startPoint);
								line1.setEnd(endPoint);

								polyline.addSegment(line1, false);
							}
							tempGraphic = new Graphic(polyline, lineSymbol);
							index1 = glLayer.addGraphic(tempGraphic);
							totallength = Math.round(line.calculateLength2D())+ totallength;
							String length = Double.toString(totallength)
									+ " 米";
//							MyDialog dialog = new MyDialog(ArcMap.this, length);
//							dialog.show();
							map_message.setText(length);
							map_message.setVisibility(View.VISIBLE);
//							Toast.makeText(ArcMap.this, length, Toast.LENGTH_LONG).show();

						} else if (geoType == Geometry.Type.POLYGON) {
							if (tempGraphic != null) {
								glLayer.removeGraphic(index2);
							}
							// 绘制临时多边形
							if (tempPolygon == null)
								tempPolygon = new Polygon();
							tempPolygon.addSegment(line, false);

							Polygon polygon = new Polygon();
							Point startPoint = null;
							Point endPoint = null;
							// 绘制完整的多边形
							for (int i = 1; i < points.size(); i++) {
								startPoint = points.get(i - 1);
								endPoint = points.get(i);

								Line line1 = new Line();
								line1.setStart(startPoint);
								line1.setEnd(endPoint);

								polygon.addSegment(line1, false);
							}
							tempGraphic = new Graphic(polygon, fillSymbol);
							index2 = glLayer.addGraphic(tempGraphic);
							// 计算当前面积

							String sArea = getAreaString(tempPolygon.calculateArea2D());
//							MyDialog dialog = new MyDialog(ArcMap.this, sArea);
							if(points.size()>2){
//								dialog.show();
								map_message.setText(sArea);
								map_message.setVisibility(View.VISIBLE);
							}
							
//							Toast.makeText(ArcMap.this, sArea, Toast.LENGTH_LONG).show();

						}
					}

					ptPrevious = location;

				}

			}
		});
		// marker = getResources().getDrawable(R.drawable.wu1); // 得到需要标在地图上的资源
		// // 创建点击mark时的弹出泡泡
		// mPopView = super.getLayoutInflater().inflate(R.layout.popview, null);
		// mapView.addView(mPopView, new MapView.LayoutParams(
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null,
		// MapView.LayoutParams.));
		// mapView.invalidate();
		// image = (Button) mPopView.findViewById(R.id.image);
		// mPopView.setVisibility(View.GONE);

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

		PictureMarkerSymbol locationPH = new PictureMarkerSymbol(ArcMap.this.getResources()
				.getDrawable(R.drawable.wu1));
		double locy = bdLocation.getLatitude();
		double locx = bdLocation.getLongitude();
		Point wgspoint = new Point(locx, locy);
		Point mapPoint = (Point) GeometryEngine.project(wgspoint, SpatialReference.create(4326),
				mapView.getSpatialReference());
		glLayer.removeAll();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "长城大厦");
		map.put("address", "保福寺桥南");
		Graphic gp = new Graphic(mapPoint, locationPH, map, 0);

		glLayer.addGraphic(gp);
		mapView.centerAt(mapPoint, true);// 漫游到当前位置
		mapView.zoomToResolution(mapPoint, 2 * 2 * 2);
		if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
			System.out.println("locationType:  Gps");
		}
		// mapView.invalidate();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.unpause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

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

			geoType = null;
			glLayer.removeAll();
			setEmpty();

			if (callout != null && callout.isShowing()) {
				callout.hide();
			}
			if (myTask != null) {
				myTask.cancel();
			}
			linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.top_bg_01));

			myPopeWindow = new MyPopeWindow(getApplicationContext(), R.layout.monitor);
			popupWindow = myPopeWindow.getPopupWindow();
			popupWindow.showAsDropDown(linearLayout);

			View monitorView = myPopeWindow.getView();
			monitor_map_online_text = (TextView) monitorView
					.findViewById(R.id.monitor_map_online_text);
			monitor_map_offline_text = (TextView) monitorView
					.findViewById(R.id.monitor_map_offline_text);
			if (Tools.isNetworkAvailable(ArcMap.this, true)) {
				startProgressDialog();
				myTask = new MyTask();
				timer.schedule(myTask, 0, 30 * 1000);
			}

			break;
		case R.id.map_locus:
			geoType = null;
			glLayer.removeAll();
			setEmpty();
			if (myTask != null) {
				myTask.cancel();
			}
			if (callout != null && callout.isShowing()) {
				callout.hide();
			}

			linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.top_bg_02));
			myPopeWindow = new MyPopeWindow(getApplicationContext(), R.layout.locus);
			popupWindow = myPopeWindow.getPopupWindow();
			popupWindow.showAsDropDown(linearLayout);

			View locusView = myPopeWindow.getView();
			locus_person = (TextView) locusView.findViewById(R.id.locus_person);
			locus_date = (TextView) locusView.findViewById(R.id.locus_date);
			if(uptime.length() == 19){
				locus_date.setText(uptime.substring(0, 10));
				}
			ImageView locus_map_start = (ImageView) locusView.findViewById(R.id.locus_map_start);

			locus_person.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (MyApplication.person.equals("")
							|| MyApplication.person.contains("ERR")) {
						if (Tools.isNetworkAvailable(ArcMap.this, true)) {
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

					new DatePickerDialog(ArcMap.this, new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear,
								int dayOfMonth) {
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
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
							.show();
				}
			});

			locus_map_start.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (locus_person.getText().toString().equals("")
							|| locus_date.getText().toString().equals("")) {
						Toast.makeText(ArcMap.this, "人员和日期不能空", 2000).show();
					} else {
						if (Tools.isNetworkAvailable(ArcMap.this, true)) {
							startProgressDialog();
							request.getLocusByUser(TreeView.getUserNameList(), locus_date.getText()
									.toString());
						}

					}

				}
			});
			break;
		case R.id.map_search:
			geoType = null;
			glLayer.removeAll();
			setEmpty();
			if (myTask != null) {
				myTask.cancel();
			}
			if (callout != null && callout.isShowing()) {
				callout.hide();
			}
			linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.top_bg_03));

			final Dialog dialog = new Dialog(ArcMap.this);
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.map_top_bg);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.search);
			dialog.show();
			final RadioButton search_pile = (RadioButton) dialog.findViewById(R.id.search_pile);
			final RadioButton search_line = (RadioButton) dialog.findViewById(R.id.search_line);
			final EditText search_edit = (EditText) dialog.findViewById(R.id.search_edit);
			Button search_button = (Button) dialog.findViewById(R.id.search_button);

			search_button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!(search_edit.getText().toString().trim().equals(""))) {

						if (search_pile.isChecked()) {
							isPile = false;
							if (Tools.isNetworkAvailable(ArcMap.this, true)) {
								startProgressDialog();
								request.getMarkerXY(search_edit.getText().toString().trim());
							}

						} else if (search_line.isChecked()) {
							isPile = true;
							if (Tools.isNetworkAvailable(ArcMap.this, true)) {
								startProgressDialog();
								request.getLineXY(search_edit.getText().toString().trim());
							}

						}
					} else {
						Toast.makeText(ArcMap.this, "搜索内容不能为空", 2000).show();
					}
					dialog.cancel();
				}
			});
			break;
		case R.id.map_other:
			geoType = null;
			glLayer.removeAll();
			setEmpty();
			if (myTask != null) {
				myTask.cancel();
			}
			if (callout != null && callout.isShowing()) {
				callout.hide();
			}
			linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.top_bg_04));
			myPopeWindow = new MyPopeWindow(getApplicationContext(), R.layout.other);
			popupWindow = myPopeWindow.getPopupWindow();
			popupWindow.showAsDropDown(linearLayout);
			View otherview = myPopeWindow.getView();
			ImageView other_map_distance = (ImageView) otherview
					.findViewById(R.id.other_map_distance);
			ImageView other_map_area = (ImageView) otherview.findViewById(R.id.other_map_area);

			// navi.setClickable(true);
			other_map_distance.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					geoType = Geometry.Type.POLYLINE;
					return false;
				}
			});

			other_map_area.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {

					geoType = Geometry.Type.POLYGON;
					return false;
				}
			});

			break;

		}

	}

	String userData;
	Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			stopProgressDialog();
			switch (msg.arg1) {
			case 42:
				if (msg.getData().getInt("flag") == 1) {
					userData = msg.getData().getString("result");
					if (!userData.contains("ERR")) {
						showUserDialog();
						MyApplication.person = userData;
					} else {
						Toast.makeText(ArcMap.this, "出现异常，请与管理员联系", 1000).show();
					}
				} else {
					Toast.makeText(ArcMap.this, "网络连接错误", 1000).show();
				}
				break;
			case 49:
				if (msg.getData().getInt("flag") == 1) {
					String data = msg.getData().getString("result");
					if (data.trim().equals("-1")) {

						Toast.makeText(ArcMap.this, "暂时没有实时数据", 2000).show();

					} else {
						List<GpsUser> list = Json.getUserXY(data);
						showMonitor(list);
						monitor_map_online_text.setText(list.size() + "");
					}
				} else {
					Toast.makeText(ArcMap.this, "请检查网络", 2000).show();
				}
				break;
			case 50:
				if (msg.getData().getInt("flag") == 1) {
					String data = msg.getData().getString("result");
					if (data.trim().equals("-1")) {

						Toast.makeText(ArcMap.this, "暂时没有实时数据", 2000).show();

					} else {
						monitor_map_offline_text.setText(data);
					}
				} else {
					Toast.makeText(ArcMap.this, "请检查网络", 2000).show();
				}
				break;
			case 52:
				if (msg.getData().getInt("flag") == 1) {
					String data = msg.getData().getString("result");
					if (data.trim().equals("-1")) {
						if (isPile) {
							Toast.makeText(ArcMap.this, "没有这条管线的数据，请检查输入是否属于正确", 2000).show();
						} else {
							Toast.makeText(ArcMap.this, "没有这个桩的数据，请检查输入是否属于正确", 2000).show();
						}

					} else {
						List<GpsMarker> marker = Json.getMarkerList(data);
						if(marker.size()>0){
							showSearchMarker(marker);
						}else{
							if (isPile) {
								Toast.makeText(ArcMap.this, "没有这条管线的数据，请检查输入是否属于正确", 2000).show();
							} else {
								Toast.makeText(ArcMap.this, "没有这个桩的数据，请检查输入是否属于正确", 2000).show();
							}
						}
						
					}
				} else {
					Toast.makeText(ArcMap.this, "请检查网络", 2000).show();
				}

				break;
			case 54:
				if (msg.getData().getInt("flag") == 1) {
					String data = msg.getData().getString("result");
					if (data.trim().equals("-1")) {
						if (isPile) {
							Toast.makeText(ArcMap.this, "没有这条管线的数据，请检查输入是否属于正确", 2000).show();
						} else {
							Toast.makeText(ArcMap.this, "没有这个桩的数据，请检查输入是否属于正确", 2000).show();
						}

					} else {
						List<GpsLine> list = Json.getLineMarkerList(data);
						if(list.size()>0){
							showSearchLine(list);
						}else{
							if (isPile) {
								Toast.makeText(ArcMap.this, "没有这条管线的数据，请检查输入是否属于正确", 2000).show();
							} else {
								Toast.makeText(ArcMap.this, "没有这个桩的数据，请检查输入是否属于正确", 2000).show();
							}
						}
						
					}
				} else {
					Toast.makeText(ArcMap.this, "请检查网络", 2000).show();
				}
				break;
			case 55:
				if (msg.getData().getInt("flag") == 1) {
					String data = msg.getData().getString("result");
					if (data.trim().equals("-1")) {
						Toast.makeText(ArcMap.this, "暂时没有这个人员的巡检轨迹", 2000).show();
					} else {
						List<GpsUser> list = Json.getLocusUser(data);
						if(list.get(0).getGpsList().size()>0){
							showLocus(list);
						}else{
							Toast.makeText(ArcMap.this, "暂时没有这个人员的巡检轨迹", 2000).show();
						}
						
					}
				} else {
					Toast.makeText(ArcMap.this, "请检查网络", 2000).show();
				}
				break;
			}
		}

	};

	TreeView treeView;
	BroadcastReceiver receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			dialog.dismiss();
		}
		
	};
	Dialog dialog;
	public void showUserDialog() {
		dialog = new Dialog(ArcMap.this);

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
			if(ja.length()>0){
				for(int i=ja.length()-1;i>=0;i--){
					TreeElement element = new TreeElement(ja.getJSONObject(i).getString("eventid"), ja
							.getJSONObject(i).getString("name"));
					element.setNotetype(ja.getJSONObject(0).getString("notetype"));
					addChild(element, ja.getJSONObject(0));
					treeView.mPdfOutlinesCount.add(element);
				}
			}
			
			

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
				for (int i = jsonArray.length()-1; i >=0; i--) {
					JSONObject ob1 = jsonArray.getJSONObject(i);
					TreeElement element1 = new TreeElement(ob1.getString("eventid"),
							ob1.getString("name"));
					element1.setNotetype(ob1.getString("notetype"));
					
					JSONArray jsonArray2 = ob1.getJSONArray("mysubChild");
					if(jsonArray2.length()>0){
						addChild(element1, ob1);
					}
					element.addChild(element1);
					
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
		Point showPoint = new Point();
		for (int i = 0; i < list.size(); i++) {
			Polyline polyline = new Polyline();
			List<GpsLocation> locationList = list.get(i).getGpsList();
			for (int j = 0; j < locationList.size(); j++) {

				double x = Double.valueOf(locationList.get(j).getLON());
				double y = Double.valueOf(locationList.get(j).getLAT());
				Point point = new Point(x, y);

				Point mapPoint = (Point) GeometryEngine.project(point,
						SpatialReference.create(4326), mapView.getSpatialReference());
				if (j == 0) {
					if (i == 0) {
						showPoint = mapPoint;
						PictureMarkerSymbol locationPH = new PictureMarkerSymbol(ArcMap.this.getResources()
								.getDrawable(R.drawable.b));
						Graphic graphic = new Graphic(mapPoint, locationPH);
						glLayer.addGraphic(graphic);
					}
					polyline.startPath(mapPoint);

					Graphic graphic = new Graphic(mapPoint, new SimpleLineSymbol(Color.RED, 5));
					glLayer.addGraphic(graphic);
				}
				if(j==(locationList.size()-1)){
					PictureMarkerSymbol locationPH = new PictureMarkerSymbol(ArcMap.this.getResources()
							.getDrawable(R.drawable.e));
					Graphic graphic = new Graphic(mapPoint, locationPH);
					glLayer.addGraphic(graphic);
				}
				polyline.lineTo(mapPoint);
			}
			glLayer.addGraphic(new Graphic(polyline, new SimpleLineSymbol(Color.BLUE, 5)));
		}
		if (!(showPoint.isEmpty())) {
			mapView.setResolution(291.49165192588737);
			mapView.centerAt(showPoint, true);// 漫游到当前位置
			mapView.zoomTo(showPoint, 2 * 2 * 2 * 2);
		}

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
		PictureMarkerSymbol locationPH = new PictureMarkerSymbol(ArcMap.this.getResources()
				.getDrawable(R.drawable.person));
		for (int i = 0; i < monitorList.size(); i++) {
			GpsUser user = monitorList.get(i);
			double locy = Double.valueOf(user.getLAT());
			double locx = Double.valueOf(user.getLON());
			Point wgspoint = new Point(locx, locy);
			Point mapPoint = (Point) GeometryEngine.project(wgspoint,
					SpatialReference.create(4326), mapView.getSpatialReference());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", user.getNAME());
			Graphic gp = new Graphic(mapPoint, locationPH, map, 0);
			glLayer.addGraphic(gp);
			if (i == 0) {

				// mapView.zoomToResolution(mapPoint, 2*2*2);
//				mapView.zoomTo(mapPoint, 2 * 2 * 2 * 2 * 2 * 2);
				mapView.setResolution(291.49165192588737);
				mapView.centerAt(mapPoint, true);// 漫游到当前位置
			}

		}
	}

	/**
	 * 
	 * @功能描述 显示搜索到的桩
	 * @author 张龙飞Email:longfeiz@geo-k.cn Tel:13671277587
	 * @createDate 2013-11-29 下午1:57:31
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void showSearchMarker(List<GpsMarker> markerList) {
		
		double scale = mapView.getScale();
		type = "桩号";
		PictureMarkerSymbol locationPH = new PictureMarkerSymbol(ArcMap.this.getResources()
				.getDrawable(R.drawable.wu1));
		for (int i = 0; i < markerList.size(); i++) {
			GpsMarker marker = markerList.get(i);
			double locy = Double.valueOf(marker.getLat());
			double locx = Double.valueOf(marker.getLon());
			Point wgspoint = new Point(locx, locy);
			Point mapPoint = (Point) GeometryEngine.project(wgspoint,
					SpatialReference.create(4326), mapView.getSpatialReference());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", marker.getMarkername());
			// Graphic gp = new Graphic(mapPoint, locationPH, map, 0);
			Graphic gp = new Graphic(mapPoint, locationPH, map, 0);
			glLayer.addGraphic(gp);
			if (i == 0) {
				mapView.centerAt(mapPoint, true);// 漫游到当前位置
				mapView.zoomToResolution(mapPoint, 2 * 2 * 2*2*2);
			}

		}
		double scale1 = mapView.getScale();
		double s = scale1;
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
		double scale = mapView.getScale();
		Point showPoint = new Point();
		for (int i = 0; i < list.size(); i++) {
			Polyline polyline = new Polyline();
			List<GpsMarker> markerList = list.get(i).getMarkerLsit();
			for (int j = 0; j < markerList.size(); j++) {
				double x = Double.valueOf(markerList.get(j).getLon());
				double y = Double.valueOf(markerList.get(j).getLat());
				Point point = new Point(x, y);

				Point mapPoint = (Point) GeometryEngine.project(point,
						SpatialReference.create(4326), mapView.getSpatialReference());
				if (j == 0) {
					if (i == 0) {
						showPoint = mapPoint;
					}
					polyline.startPath(mapPoint);

					Graphic graphic = new Graphic(mapPoint, new SimpleLineSymbol(Color.RED, 5));
					glLayer.addGraphic(graphic);
				}
				polyline.lineTo(mapPoint);
			}
			glLayer.addGraphic(new Graphic(polyline, new SimpleLineSymbol(Color.BLUE, 5)));
		}
		if (!(showPoint.isEmpty())) {
			mapView.setResolution(291.49165192588737);
			mapView.centerAt(showPoint, true);// 漫游到当前位置
			
//			mapView.zoomTo(showPoint, 2 * 2);
		}

	}

	/**
	 * 
	 * @功能描述 清空地图数据
	 * @author 张龙飞Email:longfeiz@geo-k.cn Tel:13671277587
	 * @createDate 2013-11-15 下午2:00:26
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void setEmpty() {
		ptStart = null;
		ptPrevious = null;
		points.clear();
		tempPolygon = null;
		tempGraphic = null;
		map_message.setVisibility(View.GONE);
		totallength = 0.0;
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

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	/*
	 * Convert input address into geocoded point
	 */
	private void address2Pnt(String address) {
		try {
			// create Locator parameters from single line address string
			LocatorFindParameters findParams = new LocatorFindParameters(address);
			// set the search country to USA
			findParams.setSourceCountry("china");
			// limit the results to 2
			findParams.setMaxLocations(2);
			// set address spatial reference to match map
			findParams.setOutSR(mapView.getSpatialReference());
			// execute async task to geocode address
			new Geocoder().execute(findParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public class MyRunnable implements Runnable {
		public void run() {
			arcdialog.dismiss();
		}
	}

	/*
	 * AsyncTask to geocode an address to a point location Draw resulting point
	 * location on the map with matching address
	 */
	private class Geocoder extends
			AsyncTask<LocatorFindParameters, Void, List<LocatorGeocodeResult>> {
		// The result of geocode task is passed as a parameter to map the
		// results
		protected void onPostExecute(List<LocatorGeocodeResult> result) {
			if (result == null || result.size() == 0) {
				// update UI with notice that no results were found
				Toast toast = Toast.makeText(ArcMap.this, "No result found.", Toast.LENGTH_LONG);
				toast.show();
			} else {
				// show progress dialog while geocoding address
				arcdialog = ProgressDialog.show(mapView.getContext(), "Geocoder", "搜索中 ...");
				// get return geometry from geocode result
				Geometry resultLocGeom = result.get(0).getLocation();
				// create marker symbol to represent location
				SimpleMarkerSymbol resultSymbol = new SimpleMarkerSymbol(Color.BLUE, 20,
						SimpleMarkerSymbol.STYLE.CIRCLE);
				PictureMarkerSymbol marker = new PictureMarkerSymbol(ArcMap.this.getResources()
						.getDrawable(R.drawable.wu1));
				// create graphic object for resulting location
				Graphic resultLocation = new Graphic(resultLocGeom, marker);
				// add graphic to location layer
				glLayer.addGraphic(resultLocation);
				// create text symbol for return address
				TextSymbol resultAddress = new TextSymbol(12, result.get(0).getAddress(),
						Color.BLACK);
				// create offset for text
				resultAddress.setOffsetX(10);
				resultAddress.setOffsetY(50);
				// create a graphic object for address text
				Graphic resultText = new Graphic(resultLocGeom, resultAddress);
				// add address text graphic to location graphics layer
				glLayer.addGraphic(resultText);
				// zoom to geocode result
				mapView.zoomToResolution(result.get(0).getLocation(), 2);
				// create a runnable to be added to message queue
				handler.post(new MyRunnable());
			}
		}

		// invoke background thread to perform geocode task
		@Override
		protected List<LocatorGeocodeResult> doInBackground(LocatorFindParameters... params) {
			// get spatial reference from map
			SpatialReference sr = mapView.getSpatialReference();
			// create results object and set to null
			List<LocatorGeocodeResult> results = null;
			// set the geocode servicehttp:
			locator = new Locator(
					"http://geocode.arcgis.com/arcgis/rest/services/ChinaOnlineStreetCold/GeocodeServer");
			try {
				// pass address to find method to return point representing
				// address
				results = locator.find(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// return the resulting point(s)
			return results;
		}
	}
}
