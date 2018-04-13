package com.geok.langfang.tools;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.List;

public class GetLocation {

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	private double lat;
	private double lon;
	private LocationManager manager;

	public GetLocation(Context context) {
		manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);// 获取精准位置
		criteria.setCostAllowed(true);// 允许产生开销
		criteria.setPowerRequirement(Criteria.POWER_HIGH);// 消耗大的话，获取的频率高
		criteria.setSpeedRequired(true);// 手机位置移动
		criteria.setAltitudeRequired(true);// 海拔
		String BestPrivode = manager.getBestProvider(criteria, true);
		manager.requestLocationUpdates(BestPrivode, 10, 5, new MyLocationLinstener());

	}

	public boolean isGpsEnabled(Context context)

	{

		LocationManager locationManager = ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE));

		List<String> accessibleProviders = locationManager.getProviders(true);

		return accessibleProviders != null && accessibleProviders.size() > 0;

	}

	private class MyLocationLinstener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub

			setLat(location.getLatitude());
			setLon(location.getLongitude());
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

	}

}
