package com.geok.langfang.tools;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;

public class MyLocation {

	public LocationClient mLocationClient = null;
	// public LocationClient locationClient = null;
	// public LocationClient LocationClient = null;
	private String mData;
	public MyLocationListenner myListener = new MyLocationListenner();
	// public MyLocationListenner listener = new MyLocationListenner();
	// public MyLocationListenner locListener = new MyLocationListenner();
	public TextView mTv;
	public NotifyLister mNotifyer = null;
	public Vibrator mVibrator01;
	public static String TAG = "LocTestDemo";
	public LocationData mylocation;
	Handler handler;

	public MyLocation(Context c, Handler handler) {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("gpj02");// 返回国测局经纬度坐标系 coor=gcj02
		option.setPriority(LocationClientOption.GpsFirst);// GPS定位优先
		option.disableCache(true);// 禁止启用缓存定位
		mLocationClient = new LocationClient(c);
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(myListener);
		mylocation = new LocationData();
		this.handler = handler;
	}

	/**
	 * 显示字符串
	 * 
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			mData = str;
			if (mTv != null)
				mTv.setText(mData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				handler.sendEmptyMessage(1);
				return;
			}
			mylocation.longitude = location.getLongitude();
			mylocation.latitude = location.getLatitude();
			mylocation.satellitesNum = location.getLocType();
			handler.sendEmptyMessage(0);
			// StringBuffer sb = new StringBuffer(256);
			// sb.append("time : ");
			// sb.append(location.getTime());
			// sb.append("\nerror code : ");
			// sb.append(location.getLocType());
			// sb.append("\nlatitude : ");
			// sb.append(location.getLatitude());
			// sb.append("\nlontitude : ");
			// sb.append(location.getLongitude());
			// sb.append("\nradius : ");
			// sb.append(location.getRadius());
			// if (location.getLocType() == BDLocation.TypeGpsLocation){
			// sb.append("\nspeed : ");
			// sb.append(location.getSpeed());
			// sb.append("\nsatellite : ");
			// sb.append(location.getSatelliteNumber());
			// } else if (location.getLocType() ==
			// BDLocation.TypeNetWorkLocation){
			// // sb.append("\n省：");
			// // sb.append(location.getProvince());
			// // sb.append("\n市：");
			// // sb.append(location.getCity());
			// // sb.append("\n区/县：");
			// // sb.append(location.getDistrict());
			// sb.append("\naddr : ");
			// sb.append(location.getAddrStr());
			// }
			// sb.append("\nsdk version : ");
			// sb.append(mLocationClient.getVersion());
			// sb.append("\nisCellChangeFlag : ");
			// sb.append(location.isCellChangeFlag());
			// logMsg(sb.toString());
			// Log.i(TAG, sb.toString());
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				handler.sendEmptyMessage(1);
				return;
			}
			mylocation.longitude = poiLocation.getLongitude();
			mylocation.latitude = poiLocation.getLatitude();
			handler.sendEmptyMessage(0);
			// StringBuffer sb = new StringBuffer(256);
			// sb.append("Poi time : ");
			// sb.append(poiLocation.getTime());
			// sb.append("\nerror code : ");
			// sb.append(poiLocation.getLocType());
			// sb.append("\nlatitude : ");
			// sb.append(poiLocation.getLatitude());
			// sb.append("\nlontitude : ");
			// sb.append(poiLocation.getLongitude());
			// sb.append("\nradius : ");
			// sb.append(poiLocation.getRadius());
			// if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
			// sb.append("\naddr : ");
			// sb.append(poiLocation.getAddrStr());
			// }
			// if(poiLocation.hasPoi()){
			// sb.append("\nPoi:");
			// sb.append(poiLocation.getPoi());
			// }else{
			// sb.append("noPoi information");
			// }
			// logMsg(sb.toString());
		}
	}

	public class NotifyLister extends BDNotifyListener {
		@Override
		public void onNotify(BDLocation mlocation, float distance) {
			mVibrator01.vibrate(1000);
		}
	}
}