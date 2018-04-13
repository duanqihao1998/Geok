package com.geok.langfang.request;

import com.geok.langfang.tools.GetLocation;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class LocationService extends Service {

	GetLocation getLocation;
	Mythread mythread;
	Thread thread;
	boolean ThreadisColsed = true;
	public MyBinder binder = new MyBinder();
	private double lat, lon;

	public class MyBinder extends Binder {
		public String getInfo() {
			return "lat=" + lat + ",lon=" + lon;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return binder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		getLocation = new GetLocation(this);
		mythread = new Mythread();
		thread = new Thread(mythread);
		thread.start();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		if (thread != null) {
			ThreadisColsed = false;
			thread.interrupt();
			thread = null;
		}
		return super.onUnbind(intent);
	}

	class Mythread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			try {

				while (ThreadisColsed) {
					lat = getLocation.getLat();
					lon = getLocation.getLon();
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

}
