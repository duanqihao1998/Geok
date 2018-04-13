package com.geok.langfang.pipeline.map;

import android.app.Activity;

public class MainMap extends Activity {
	// /** Called when the activity is first created. */
	// final static String TAG = "MainMap";
	// EditText bustext;
	// UserDialog busDialog;
	// private MapView mapView = null;
	// ImageView map_locate, map_layer, map_search, map_other;
	// private MapController mapController = null;
	// MyPopeWindow myPopeWindow;
	// PopupWindow popupWindow;
	// LinearLayout linearLayout;
	// MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	// String city = "北京";
	// /**
	// * 用来保存屏幕的像素
	// */
	// int WindowWidth, WindowHeight;
	// MKMapViewListener mapViewListener;
	//
	// Calculate calculate;
	//
	// LocationClient mLocClient;// 位置
	// /**
	// * 功能类引用
	// */
	// MyLocation myLocation;
	// DrawLayer drawLayer;
	// ApplicationApp app;
	//
	// Projection projection;
	// /** 根据地图的移动刷新数据 */
	// private int width = 0;
	// private int height = 0;
	// View mPopView = null; // 点击mark时弹出的气泡View
	// Button image;// pop
	// Drawable marker; // 得到需要标在地图上的资源
	//
	// private static CustomProgressDialog progressDialog = null; // 自定义的进度条的对象
	//
	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	//
	// app = ApplicationApp.getInstance();
	// if (app.mBMapManager == null) {
	// app.mBMapManager = new BMapManager(this);
	// app.mBMapManager.init(ApplicationApp.strKey,
	// new ApplicationApp.MyGeneralListener());
	// }
	// setContentView(R.layout.main);
	//
	// DisplayMetrics dm = new DisplayMetrics();
	// getWindowManager().getDefaultDisplay().getMetrics(dm);
	// WindowHeight = dm.widthPixels;
	// WindowWidth = dm.heightPixels;
	// // int w=getWindowManager().getDefaultDisplay().getWidth();
	// // int h=getWindowManager().getDefaultDisplay().getHeight();
	// initView();
	// mLocClient = new LocationClient(this);
	// getCity();
	// mSearch = new MKSearch();
	// mSearch.setDrivingPolicy(MKSearch.ECAR_DIS_FIRST);
	// // 初始化搜索模块，注册事件监听
	// mSearch = new MKSearch();
	// mSearch.init(app.mBMapManager, new MKSearchListener() {
	//
	// @Override
	// public void onGetPoiDetailSearchResult(int type, int error) {
	// }
	//
	// @Override
	// public void onGetDrivingRouteResult(MKDrivingRouteResult res,
	// int error) {
	// stopProgressDialog();
	// // 错误号可参考MKEvent中的定义
	// if (error != 0 || res == null) {
	// Toast.makeText(MainMap.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
	// .show();
	// return;
	// }
	// RouteOverlay routeOverlay = new RouteOverlay(MainMap.this,
	// mapView);
	// // 此处仅展示一个方案作为示例
	// routeOverlay.setData(res.getPlan(0).getRoute(0));
	// mapView.getOverlays().clear();
	// mapView.getOverlays().add(routeOverlay);
	// mapView.refresh();
	// // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
	// mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
	// routeOverlay.getLonSpanE6());
	// mapView.getController().animateTo(res.getStart().pt);
	// }
	//
	// @Override
	// public void onGetTransitRouteResult(MKTransitRouteResult res,
	// int error) {
	// stopProgressDialog();
	// if (error != 0 || res == null) {
	// Toast.makeText(MainMap.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
	// .show();
	// return;
	// }
	// TransitOverlay routeOverlay = new TransitOverlay(MainMap.this,
	// mapView);
	// // 此处仅展示一个方案作为示例
	// routeOverlay.setData(res.getPlan(0));
	// mapView.getOverlays().clear();
	// mapView.getOverlays().add(routeOverlay);
	// mapView.refresh();
	// // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
	// mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
	// routeOverlay.getLonSpanE6());
	// mapView.getController().animateTo(res.getStart().pt);
	// }
	//
	// @Override
	// public void onGetWalkingRouteResult(MKWalkingRouteResult res,
	// int error) {
	// stopProgressDialog();
	// if (error != 0 || res == null) {
	// Toast.makeText(MainMap.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
	// .show();
	// return;
	// }
	// RouteOverlay routeOverlay = new RouteOverlay(MainMap.this,
	// mapView);
	// // 此处仅展示一个方案作为示例
	// routeOverlay.setData(res.getPlan(0).getRoute(0));
	// mapView.getOverlays().clear();
	// mapView.getOverlays().add(routeOverlay);
	// mapView.refresh();
	// // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
	// mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
	// routeOverlay.getLonSpanE6());
	// mapView.getController().animateTo(res.getStart().pt);
	//
	// }
	//
	// @Override
	// public void onGetAddrResult(MKAddrInfo res, int error) {
	// stopProgressDialog();
	// /*
	// * * 返回地址信息搜索结果。 参数： arg0 - 搜索结果 arg1 -
	// * 错误号，0表示结果正确，result中有相关结果信息；100表示结果正确，无相关地址信息
	// */
	// String Location = null;
	// if (res == null) {
	// Location = "没有搜索到该地址";
	// } else {
	// try {
	// // 获得搜索地址的经纬度
	// Location = "纬度：" + res.geoPt.getLatitudeE6() / 1E6
	// + "\n" + "经度：" + res.geoPt.getLongitudeE6()
	// / 1E6 + "\n";
	// mapController.setZoom(14);
	// mapController.animateTo(res.geoPt);
	//
	// marker.setBounds(0, 0, marker.getIntrinsicWidth(),
	// marker.getIntrinsicHeight()); // 为maker定义位置和边界
	// mapView.getOverlays().clear();
	// mapView.getOverlays().add(
	// new OverItemT(marker, MainMap.this, res.geoPt,
	// res.strAddr, mapView));
	//
	// mapView.refresh();
	// } catch (Exception e) {
	// e.printStackTrace();
	// Location = "没有搜索到该地址";
	// }
	//
	// }
	// AlertDialog.Builder builder = new AlertDialog.Builder(
	// MainMap.this);
	// builder.setTitle("搜索结果");
	// builder.setMessage(Location);
	// builder.setPositiveButton("关闭",
	// new android.content.DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// dialog.dismiss();
	// }
	// });
	// builder.show();
	//
	// }
	//
	// @Override
	// public void onGetPoiResult(MKPoiResult res, int type, int error) {
	// stopProgressDialog();
	// // 错误号可参考MKEvent中的定义
	// if (error != 0 || res == null) {
	// Toast.makeText(MainMap.this, "抱歉，未找到结果", Toast.LENGTH_LONG)
	// .show();
	// return;
	// }
	//
	// // 找到公交路线poi node
	// MKPoiInfo curPoi = null;
	// int totalPoiNum = res.getCurrentNumPois();
	// for (int idx = 0; idx < totalPoiNum; idx++) {
	// if (2 == res.getPoi(idx).ePoiType) {
	// // poi类型，0：普通点，1：公交站，2：公交线路，3：地铁站，4：地铁线路
	// curPoi = res.getPoi(idx);
	// mSearch.busLineSearch(city, curPoi.uid);
	// break;
	// }
	// }
	//
	// // 没有找到公交信息
	// if (curPoi == null) {
	// Toast.makeText(MainMap.this, "抱歉，未找到结果", Toast.LENGTH_LONG)
	// .show();
	// return;
	// }
	// }
	//
	// @Override
	// public void onGetBusDetailResult(MKBusLineResult result, int iError) {
	// stopProgressDialog();
	// if (iError != 0 || result == null) {
	// Toast.makeText(MainMap.this, "抱歉，未找到结果", Toast.LENGTH_LONG)
	// .show();
	// return;
	// }
	// RouteOverlay routeOverlay = new RouteOverlay(MainMap.this,
	// mapView); // 此处仅展示一个方案作为示例
	// routeOverlay.setData(result.getBusRoute());
	// mapView.getOverlays().clear();
	// mapView.getOverlays().add(routeOverlay);
	// mapView.refresh();
	// mapView.getController().animateTo(
	// result.getBusRoute().getStart());
	// }
	//
	// @Override
	// public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
	// }
	//
	// });
	//
	// }
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	//
	// switch (v.getId()) {
	// case R.id.map_locate:
	// linearLayout.setBackgroundDrawable(getResources().getDrawable(
	// R.drawable.top_bg_01));
	// myLocation = new MyLocation(MainMap.this, mapView, mapController);
	// myLocation.setLocation();
	// break;
	// case R.id.map_layer:
	// linearLayout.setBackgroundDrawable(getResources().getDrawable(
	// R.drawable.top_bg_02));
	// myPopeWindow = new MyPopeWindow(getApplicationContext(),
	// R.layout.layer);
	// popupWindow = myPopeWindow.getPopupWindow();
	// popupWindow.showAsDropDown(linearLayout);
	// View view = myPopeWindow.getView();
	// CheckBox check_pile = (CheckBox) view.findViewById(R.id.layer_pile);
	// CheckBox check_pipeline = (CheckBox) view
	// .findViewById(R.id.layer_pipeline);
	// boolean PipelineIsChecked = getSharedPreferences("Layer",
	// MODE_PRIVATE).getBoolean("pipeline", false);
	// check_pipeline.setChecked(PipelineIsChecked);
	// boolean PileIsChecked = getSharedPreferences("Layer", MODE_PRIVATE)
	// .getBoolean("pile", false);
	// check_pile.setChecked(PileIsChecked);
	// drawLayer = new DrawLayer(WindowWidth, WindowHeight, mapView);
	// calculate = new Calculate(mapView, WindowWidth, WindowHeight);
	// /**
	// * 设置处理CheckBox监听器
	// */
	// // getWindowManager().getDefaultDisplay().getWidth();
	//
	// check_pipeline
	// .setOnCheckedChangeListener(new OnCheckedChangeListener() {
	//
	// @Override
	// public void onCheckedChanged(CompoundButton buttonView,
	// boolean isChecked) {
	// // TODO Auto-generated method stub
	// if (isChecked) {
	// getSharedPreferences("Layer", MODE_PRIVATE)
	// .edit().putBoolean("pipeline", true)
	// .commit();
	// // drawLayer.DrawLine();
	// popupWindow.dismiss();
	// Toast.makeText(getApplicationContext(),
	// "被选中！！", 1000).show();
	// } else {
	// getSharedPreferences("Layer", MODE_PRIVATE)
	// .edit().putBoolean("pipeline", false)
	// .commit();
	// drawLayer.ClearLine();
	// popupWindow.dismiss();
	// Toast.makeText(getApplicationContext(),
	// "被取消！！", 1000).show();
	// }
	//
	// }
	// });
	//
	// check_pile
	// .setOnCheckedChangeListener(new OnCheckedChangeListener() {
	//
	// @Override
	// public void onCheckedChanged(CompoundButton buttonView,
	// boolean isChecked) {
	// // TODO Auto-generated method stub
	// if (isChecked) {
	// getSharedPreferences("Layer", MODE_PRIVATE)
	// .edit().putBoolean("pile", true)
	// .commit();
	// // drawLayer.DrawLine();
	// popupWindow.dismiss();
	// } else {
	// getSharedPreferences("Layer", MODE_PRIVATE)
	// .edit().putBoolean("pile", false)
	// .commit();
	// // drawLayer.ClearLine();
	// popupWindow.dismiss();
	// }
	// }
	// });
	// break;
	// case R.id.map_search:
	//
	// linearLayout.setBackgroundDrawable(getResources().getDrawable(
	// R.drawable.top_bg_03));
	//
	// final Dialog dialog = new Dialog(MainMap.this);
	// dialog.getWindow().setBackgroundDrawableResource(
	// R.drawable.map_top_bg);
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog.setContentView(R.layout.search);
	// dialog.show();
	// final RadioButton search_pile = (RadioButton) dialog
	// .findViewById(R.id.search_pile);
	// final RadioButton search_line = (RadioButton) dialog
	// .findViewById(R.id.search_line);
	// final EditText search_edit = (EditText) findViewById(R.id.search_edit);
	// Button search_button = (Button) findViewById(R.id.search_button);
	//
	// search_button.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// if (search_edit.getText().length() > 0) {
	// if (search_pile.isChecked()) {
	//
	// String lon = Login.listTask.get(0)
	// .getInsdateinfoList().get(0)
	// .getTaskInfoList().get(0).getInfoList()
	// .get(0).getLON();
	// String lat = Login.listTask.get(0)
	// .getInsdateinfoList().get(0)
	// .getTaskInfoList().get(0).getInfoList()
	// .get(0).getLAT();
	// int a = (int) (Double.valueOf(lon) * 1E6);
	// int b = (int) (Double.valueOf(lat) * 1E6);
	// GeoPoint gp = new GeoPoint(b, a);
	// mapController.setZoom(14);
	// mapController.animateTo(gp);
	// Drawable marker = getResources().getDrawable(
	// R.drawable.wu1); // 得到需要标在地图上的资源
	// marker.setBounds(0, 0, marker.getIntrinsicWidth(),
	// marker.getIntrinsicHeight()); // 为maker定义位置和边界
	// mapView.getOverlays().clear();
	// mapView.getOverlays().add(
	// new OverItemT(marker, MainMap.this, gp, "",
	// mapView));
	//
	// mapView.refresh();
	// } else if (search_line.isChecked()) {
	//
	// }
	// }
	// dialog.cancel();
	// }
	// });
	// break;
	// case R.id.map_other:
	// linearLayout.setBackgroundDrawable(getResources().getDrawable(
	// R.drawable.top_bg_04));
	// myPopeWindow = new MyPopeWindow(getApplicationContext(),
	// R.layout.other);
	// popupWindow = myPopeWindow.getPopupWindow();
	// popupWindow.showAsDropDown(linearLayout);
	// View otherview = myPopeWindow.getView();
	// ImageView navi = (ImageView) otherview
	// .findViewById(R.id.other_navi);
	// ImageView placename = (ImageView) otherview
	// .findViewById(R.id.other_placename);
	// ImageView bus = (ImageView) otherview.findViewById(R.id.other_bus);
	// // navi.setClickable(true);
	// navi.setOnTouchListener(new View.OnTouchListener() {
	//
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// // TODO Auto-generated method stub
	//
	// showNavigation();
	// return false;
	// }
	// });
	//
	// placename.setOnTouchListener(new View.OnTouchListener() {
	//
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	//
	// final Dialog dialog = new Dialog(MainMap.this);
	// dialog.getWindow().setBackgroundDrawableResource(
	// R.drawable.map_top_bg);
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog.setContentView(R.layout.placename);
	// bustext = (EditText) dialog.findViewById(R.id.placename);
	// Button busbutton = (Button) dialog
	// .findViewById(R.id.navi_button);
	//
	// busbutton.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// if (bustext.getText().length() > 0) {
	// mSearch.geocode(bustext.getText().toString(),
	// city);
	// dialog.dismiss();
	// startProgressDialog();
	// }
	//
	// }
	// });
	// dialog.show();
	//
	// return false;
	// }
	// });
	//
	// bus.setOnTouchListener(new View.OnTouchListener() {
	//
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// // TODO Auto-generated method stub
	// // busDialog=new UserDialog(MainMap.this, R.layout.bus);
	// // busDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// final Dialog dialog = new Dialog(MainMap.this);
	// dialog.getWindow().setBackgroundDrawableResource(
	// R.drawable.map_top_bg);
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog.setContentView(R.layout.bus);
	// bustext = (EditText) dialog.findViewById(R.id.bus);
	// Button busbutton = (Button) dialog
	// .findViewById(R.id.bus_button);
	//
	// busbutton.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// mSearch.poiSearchInCity(city, bustext.getText()
	// .toString());
	// dialog.dismiss();
	// startProgressDialog();
	// }
	// });
	// dialog.show();
	//
	// return false;
	// }
	// });
	// break;
	//
	// }
	//
	// }
	//
	// void initView() {
	// linearLayout = (LinearLayout) findViewById(R.id.top_background);
	// mapView = (MapView) findViewById(R.id.bmapView);
	// map_locate = (ImageView) findViewById(R.id.map_locate);
	// map_layer = (ImageView) findViewById(R.id.map_layer);
	// map_search = (ImageView) findViewById(R.id.map_search);
	// map_other = (ImageView) findViewById(R.id.map_other);
	// map_locate.setOnClickListener(this);
	// map_layer.setOnClickListener(this);
	// map_search.setOnClickListener(this);
	// map_other.setOnClickListener(this);
	// mapController = mapView.getController();
	//
	// mapView.getController().enableClick(true);
	// mapView.getController().setZoom(12);
	// mapView.setBuiltInZoomControls(true);
	// mapView.setDoubleClickZooming(true);
	//
	// DisplayMetrics dm = new DisplayMetrics();
	// getWindowManager().getDefaultDisplay().getMetrics(dm);
	// width = dm.widthPixels;
	// height = dm.heightPixels;
	// marker = getResources().getDrawable(R.drawable.wu1); // 得到需要标在地图上的资源
	// // 创建点击mark时的弹出泡泡
	// mPopView = super.getLayoutInflater().inflate(R.layout.popview, null);
	// mapView.addView(mPopView, new MapView.LayoutParams(
	// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null,
	// MapView.LayoutParams.TOP_LEFT));
	// mapView.invalidate();
	// image = (Button) mPopView.findViewById(R.id.image);
	// mPopView.setVisibility(View.GONE);
	// mapViewListener = new MKMapViewListener() {
	// /**
	// * 移动地图 异步加载数据
	// */
	// @Override
	// public void onMapMoveFinish() {
	// // TODO Auto-generated method stub
	//
	// projection = mapView.getProjection();
	// GeoPoint pointLeftTop = DisplayUtils.jwBypx(projection, 0, 0);//
	// 最小精度、最大维度
	// GeoPoint pointBottom = DisplayUtils.jwBypx(projection, width,
	// height);// 最大维度最小精度
	// // int leftLon = pointLeftTop.getLongitudeE6();
	// // int leftLat = pointLeftTop.getLatitudeE6();
	// // int bottomLon = pointBottom.getLongitudeE6();
	// // int bottomLat = pointBottom.getLatitudeE6();
	// // Rect rect = new Rect(leftLon, leftLat, bottomLon, bottomLat);
	// // // rect.contains(x, y)
	// //
	// // int level=mapView.getZoomLevel();
	// // Toast.makeText(MainMap.this, level+"", 1000).show();
	// OperationDB db = new OperationDB(MainMap.this);
	// List<GpsMarker> list = db.getGpsMarker(pointLeftTop,
	// pointBottom);
	// mapView.getOverlays().clear();
	// mapView.getOverlays().add(
	// new OverItemT(marker, null, list, null, mapView));
	// mapView.refresh();
	// }
	//
	// @Override
	// public void onMapAnimationFinish() {
	// // TODO Auto-generated method stub
	// projection = mapView.getProjection();
	// GeoPoint pointLeftTop = DisplayUtils.jwBypx(projection, 0, 0);//
	// 最小精度、最大维度
	// GeoPoint pointBottom = DisplayUtils.jwBypx(projection, width,
	// height);// 最大维度最小精度
	// // int leftLon = pointLeftTop.getLongitudeE6();
	// // int leftLat = pointLeftTop.getLatitudeE6();
	// // int bottomLon = pointBottom.getLongitudeE6();
	// // int bottomLat = pointBottom.getLatitudeE6();
	// // Rect rect = new Rect(leftLon, leftLat, bottomLon, bottomLat);
	// // // rect.contains(x, y)
	// //
	// // int level=mapView.getZoomLevel();
	// // Toast.makeText(MainMap.this, level+"", 1000).show();
	// OperationDB db = new OperationDB(MainMap.this);
	// List<GpsMarker> list = db.getGpsMarker(pointLeftTop,
	// pointBottom);
	// mapView.getOverlays().clear();
	// mapView.getOverlays().add(
	// new OverItemT(marker, null, list, null, mapView));
	// mapView.refresh();
	// }
	//
	// @Override
	// public void onGetCurrentMap(Bitmap arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onClickMapPoi(MapPoi arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// };
	//
	// mapView.regMapViewListener(ApplicationApp.getInstance().mBMapManager,
	// mapViewListener);
	// //
	// //
	// // mapView.refresh();
	//
	// }
	//
	// @Override
	// protected void onResume() {
	// // TODO Auto-generated method stub
	// super.onResume();
	// mapView.onResume();
	// }
	//
	// @Override
	// protected void onDestroy() {
	// // TODO Auto-generated method stub
	// super.onDestroy();
	// mapView.destroy();
	// if (app.mBMapManager != null) {
	// app.mBMapManager.destroy();
	// app.mBMapManager = null;
	// }
	// mLocClient.stop();
	// }
	//
	// public void showNavigation() {
	//
	// final Dialog dialog = new Dialog(this);
	// dialog.getWindow().setBackgroundDrawableResource(R.drawable.map_top_bg);
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog.setContentView(R.layout.navi);
	//
	// final EditText navi_from = (EditText) dialog
	// .findViewById(R.id.navi_from);
	// final EditText navi_to = (EditText) dialog.findViewById(R.id.navi_to);
	// navi_from.setText("五道口");
	// navi_to.setText("天安门");
	// Button navi_button = (Button) dialog.findViewById(R.id.navi_button);
	// navi_button.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	//
	// boolean isNull = false;
	// String start = null;
	// String end = null;
	// if (navi_from.getText() != null
	// || navi_from.getText().length() != 0) {
	// start = navi_from.getText().toString();
	// isNull = true;
	// } else {
	// Toast.makeText(MainMap.this, "起点不能为空", 1000).show();
	// isNull = false;
	// }
	// if (navi_to.getText() != null
	// || navi_to.getText().length() != 0) {
	// end = navi_to.getText().toString();
	// isNull = true;
	// } else {
	// Toast.makeText(MainMap.this, "终点不能为空", 1000).show();
	// isNull = false;
	// }
	//
	// if (isNull) {
	// // 设置起始地（当前位置）
	//
	// MKPlanNode startNode = new MKPlanNode();
	// startNode.name = start;
	//
	// // 设置目的地
	// MKPlanNode endNode = new MKPlanNode();
	// endNode.name = end;
	// // 展开搜索的城市
	//
	// mSearch.drivingSearch(city, startNode, city, endNode);
	// startProgressDialog();
	//
	// // 步行路线
	//
	// // searchModel.walkingSearch(city, startNode, city,
	// // endNode);
	//
	// // 公交路线
	// // searchModel.transitSearch(city, startNode, endNode);
	// }
	//
	// dialog.dismiss();
	// }
	// });
	// dialog.show();
	// }
	//
	// class OverItemT extends ItemizedOverlay<OverlayItem> {
	// private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
	//
	// public OverItemT(Drawable marker, Context context,
	// List<GpsMarker> list, String title, MapView mMapView) {
	// super(marker, mMapView);
	// removeAll();
	// mGeoList.clear();
	// for (int i = 0; i < list.size(); i++) {
	// int lon = (int) (Double.valueOf(list.get(i).getLon()) * 1E6);
	// int lat = (int) (Double.valueOf(list.get(i).getLat()) * 1E6);
	// GeoPoint pt = new GeoPoint(lat, lon);
	// OverlayItem item = new OverlayItem(pt, list.get(i)
	// .getMarkername(), null);
	// mGeoList.add(item);
	// addItem(item);
	// }
	//
	// // populate();
	// }
	//
	// public OverItemT(Drawable marker, Context context, GeoPoint pt,
	// String title, MapView mMapView) {
	// super(marker, mMapView);
	// mGeoList.clear();
	// removeAll();
	// OverlayItem item = new OverlayItem(pt, title, null);
	//
	// mGeoList.add(item);
	// addItem(item);
	// // populate();
	// }
	//
	// @Override
	// protected OverlayItem createItem(int i) {
	// return mGeoList.get(i);
	// }
	//
	// @Override
	// public int size() {
	// return mGeoList.size();
	// }
	//
	// int a;
	//
	// @Override
	// protected boolean onTap(int i) {
	//
	// a = mGeoList.size();
	// int b = a;
	// // OverlayItem item = getItem(i);
	// // item.getPoint();
	// // GeoPoint pt = getItem(i).getPoint();
	// GeoPoint pt = mGeoList.get(i).getPoint();
	// mapView.updateViewLayout(mPopView, new MapView.LayoutParams(
	// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, pt,
	// MapView.LayoutParams.BOTTOM_CENTER));
	// image.setText(mGeoList.get(i).getTitle());
	// mPopView.setVisibility(View.VISIBLE);
	// return super.onTap(i);
	// }
	//
	// @Override
	// public boolean onTap(GeoPoint arg0, MapView arg1) {
	//
	// mPopView.setVisibility(View.GONE);
	//
	// return super.onTap(arg0, arg1);
	//
	// }
	//
	// }
	//
	// public void getCity() {
	//
	// LocationClientOption option = new LocationClientOption();
	// option.setOpenGps(true);
	// option.setAddrType("all");// 返回的定位结果包含地址信息
	// option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
	// // option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
	// option.disableCache(true);// 禁止启用缓存定位
	// option.setPoiNumber(5); // 最多返回POI个数
	// option.setPoiDistance(1000); // poi查询距离
	// option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
	// mLocClient.setLocOption(option);
	//
	// mLocClient.registerLocationListener(new BDLocationListener() {
	//
	// @Override
	// public void onReceivePoi(BDLocation arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onReceiveLocation(BDLocation location) {
	// if (location != null) {
	// city = location.getCity();
	// double lon = location.getLongitude();
	// System.out.println("city--------------" + city);
	// mLocClient.stop();
	// } else {
	// Toast.makeText(MainMap.this, "无法获得当前所在城市", 1000).show();
	// }
	// }
	// });
	// mLocClient.start();
	// }
	//
	// private void startProgressDialog() {
	// if (progressDialog == null) {
	// progressDialog = CustomProgressDialog.createDialog(this);
	// }
	// progressDialog.show();
	// }
	//
	// private void stopProgressDialog() {
	// if (progressDialog != null) {
	// progressDialog.dismiss();
	// progressDialog = null;
	// }
	// }
}