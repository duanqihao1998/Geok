package com.geok.langfang.request;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class MessagePush extends Service {

	private MyThread myThread;
	boolean ThreadisColsed = true;
	private Thread thread;
	MyApplication myApplication;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.arg1) {
			case 20:

				if (msg.getData().getInt("flag") == 1) {
					Intent intent = new Intent("MyBroadCastReceive");
					intent.putExtra("flag", msg.getData().getString("result"));
					sendBroadcast(intent);
				}

				break;

			}
		}

	};

	Request request;
	String str = "";

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		myApplication = new MyApplication(this);
		myThread = new MyThread();
		request = new Request(handler);
		thread = new Thread(myThread);
		thread.start();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	class MyThread implements Runnable {

		@Override
		public void run() {

			// while (ThreadisColsed) {
			// request.AlarmRequest(myApplication.imei);
			// System.out.println("service start!!");
			// try {
			// Thread.sleep(5000);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }

		}

	}

}
