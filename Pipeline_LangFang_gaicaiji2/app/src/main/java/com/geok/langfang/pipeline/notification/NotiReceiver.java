package com.geok.langfang.pipeline.notification;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.pipeline.Login;
import com.geok.langfang.request.Request;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 
 * @类描述 自定义接收器 @ 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel: ***********
 * @createDate 2013-6-24 上午09:54:29
 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
 */
public class NotiReceiver extends BroadcastReceiver {
	private static final String TAG = "MyReceiver";
	static String alert;
	static String receive_time;
	OperationDB operationDB;
	String alias = Login.alias;
	// 已读未读的标识，为1则已读，为0则未读
	int flag = 0;
	String data0;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.arg1) {
			case 58:
				data0 = msg.getData().getString("result");
				if (!(data0 == "ok") || !(data0 == "OK")) {
				} else {
				}
				break;
			}
		}
	};

	/**
	 * 
	 * @功能描述 接收消息方法
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel: ***********
	 * @param context
	 * @param intent
	 * @createDate 2013-6-24 下午02:37:28
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		operationDB = new OperationDB(context);
		ContentValues values = new ContentValues();

		// 获取当前时间
		// receive_time = CurrentTime.CurrentTime();

		Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		// 登记注册号
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "接收Registration Id : " + regId);
			// send the Registration Id to your server...
		} else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "接收UnRegistration Id : " + regId);
			// send the UnRegistration Id to your server...
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);
			String receivedtext = bundle.getString(JPushInterface.EXTRA_ALERT);
			String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
			String notititle = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			String notitype = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
			String notiid = String.valueOf(notifactionId);
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String time = sDateFormat.format(new Date());

			values.put("type", display(extra, 3, 4, '['));

			values.put("title", notititle);
			values.put("notifationid", notiid);
			if("".equals(notitype)||notitype==null){
				 values.put("type", "站内信");
			}
			else{
			 values.put("type", notitype);
			}
			values.put("time", time);
			values.put("detail", receivedtext);
			values.put("isnew", flag);
			operationDB.DBinsert(values, Type.NOTIFICATION);

			// // 接收消息时调用webservice，通知服务器
			// JpushRequest jrequest = new JpushRequest();
			// String alias = Login.alias;
			// jrequest.receiveConfirmRequest(display(extra, 2, 3, '['),
			// alias,"1");

			// 接收消息时调用收到消息确认请求，通知服务器
			Request request = new Request(handler);
			// request.ReceiveConfirmRequest(display(extra, 2, 3, '['),
			// alias,"1");
			request.ReceiveConfirmRequest(notiid, alias, "1");

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			Log.d(TAG, "用户点击打开了通知");

			ActivityManager am = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> list = am.getRunningTasks(100);
			boolean isAppRunning = false;
			String MY_PKG_NAME = "com.geok.langfang.pipeline";
			// for (RunningTaskInfo info : list) {
			// if (info.topActivity.getPackageName().equals(MY_PKG_NAME) ||
			// info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
			// isAppRunning = true;
			// Log.i(TAG,info.topActivity.getPackageName() +
			// " info.baseActivity.getPackageName()="+info.baseActivity.getPackageName());
			// break;
			// }
			// }
			// if(isAppRunning){
			// 打开自定义的Activity
			Intent i = new Intent(context, Notification.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			flag = 1;
			operationDB.DBupdateNotification(flag, receive_time);
			context.startActivity(i);

		} else {
			Log.d(TAG, "Unhandled intent - " + intent.getAction());
		}
	}

	/**
	 * 
	 * @功能描述 打印所有的 intent extra 数据
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel:***********
	 * @param bundle
	 * @return
	 * @createDate 2013-7-16 下午2:21:56
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("int1");
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else {
				sb.append("string1");
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
				if (key.equals(JPushInterface.EXTRA_ALERT)) {
					alert = bundle.getString(key);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @功能描述 截取两个号间的字符
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel: ***********
	 * @param str
	 * @param a
	 * @param b
	 * @return
	 * @createDate 2013-6-27 下午12:45:00
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public String display(String str, int a, int b, char c) {
		int count = 0;
		int start = 0;
		int end = 0;
		for (int i = 0; i < str.length(); i++) {
			char d = str.charAt(i);
			if (d == c) {
				count++;
				if (count == a) {
					start = i + 1;
				}
				if (count == b) {
					end = i;
				}
			}
		}
		return str.substring(start, end);
	}
}
