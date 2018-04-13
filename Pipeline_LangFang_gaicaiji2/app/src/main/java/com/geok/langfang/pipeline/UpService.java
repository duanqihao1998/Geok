package com.geok.langfang.pipeline;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.geok.langfang.DB.OperationDB;

public class UpService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
	}
	
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		OperationDB ob = new OperationDB(UpService.this);
		ob.upload();
		System.out.println("Service------------------");
	}
}
