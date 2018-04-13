package com.geok.langfang.function;

import android.content.Context;
import android.os.Handler;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 
 * @author yanfa 实现定位的类
 */

public class MyLocation {
	MapView mapView = null;
	MapController mapController = null;
	LocationClient locationClient = null;
	LocationData locationData = null;
	MyLocationOverlay myLocationOverlay = null;
	Handler myHandler = new Handler() {

	};
	BDLocationListener MyLocationListener = new BDLocationListener() {

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			locationData.latitude = location.getLatitude();
			locationData.accuracy = location.getRadius();
			locationData.longitude = location.getLongitude();
			locationData.direction = location.getDerect();
			myLocationOverlay.setData(locationData);
			mapView.refresh();
			mapController.animateTo(new GeoPoint((int) (locationData.latitude * 1e6),
					(int) (locationData.longitude * 1e6)), myHandler.obtainMessage(1));

		}
	};

	/**
	 * 构造函数
	 * 
	 * @param mapView
	 * @param controller
	 */
	public MyLocation(Context context, MapView mapView, MapController controller) {
		this.mapView = mapView;
		this.mapController = controller;
		init(context);
	}

	void init(Context context) {
		locationClient = new LocationClient(context);
		locationClient.registerLocationListener(MyLocationListener);
		LocationClientOption clientOption = new LocationClientOption();
		clientOption.setOpenGps(true);
		clientOption.setCoorType("bd09ll");
		clientOption.setScanSpan(500);
		locationClient.setLocOption(clientOption);
		locationClient.start();
		myLocationOverlay = new MyLocationOverlay(mapView);
		locationData = new LocationData();
		myLocationOverlay.setData(locationData);
		myLocationOverlay.enableCompass();
		mapView.getOverlays().add(myLocationOverlay);
		mapView.getController().setZoom(19);
		mapView.refresh();

	}

	public void setLocation() {
		locationClient.requestLocation();
	}

}
