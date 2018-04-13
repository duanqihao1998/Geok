package com.geok.langfang.request;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.AlarmInformationBean;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.alarm.Alarm;

import java.util.List;

public class MyBroadCastReceive extends BroadcastReceiver {

	OperationDB operationDB;
	Context context;
	String AlarmType = "";
	String AlarmTime = "";
	MyApplication myApplication;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String str = intent.getStringExtra("flag");

		List<AlarmInformationBean> list = Json.getAlarmInformationList(str);
		myApplication = new MyApplication(context);
		if (list.size() > 0) {
			AlarmType = list.get(0).getALARMTYPE();
			AlarmTime = list.get(0).getALARMTIME();

			/*
			 * {value.get("alarmtype"),value.get("deviceid"),value.get("inspectorid"
			 * ),value.get("par"),value.get("lon"),
			 * value.get("lat"),value.get("departmentid"
			 * ),value.get("maxspeed"),value
			 * .get("realspeed"),value.get("maxoffset"),
			 * value.get("realoffset"),
			 * value.get("droppedinterval"),value.get("alarmlocation"
			 * ),value.get("isdown"),value.get("userid"),isupload}
			 */

			operationDB = new OperationDB(context);
			for (int i = 0; i < list.size(); i++) {
				operationDB = new OperationDB(context);
				ContentValues values = new ContentValues();
				values.put("alarmtype", list.get(i).getALARMTYPE());
				values.put("departmentid", myApplication.depterid);
				values.put("maxspeed", list.get(i).getMAXOFFSET());
				values.put("realspeed", list.get(i).getREALSPEED());
				values.put("maxoffset", list.get(i).getMAXOFFSET());
				values.put("realoffset", list.get(i).getREALOFFSET());
				values.put("droppedinterval", list.get(i).getDROPPEDINTERVAL());
				values.put("alarmlocation", list.get(i).getALARMLOCATION());
				values.put("alarmtime", list.get(i).getALARMTIME());
				values.put("userid", myApplication.userid);
				values.put("isnew", true);
				operationDB.DBinsert(values, Type.ALARM_INFO);
			}

			this.context = context;
			addNotificaction();
		}

	}

	private void addNotificaction() {
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// 创建一个Notification
		Notification notification = new Notification();
		// 设置显示在手机最上边的状态栏的图标
		notification.icon = R.drawable.alarm_flag;
		// 当当前的notification被放到状态栏上的时候，提示内容
		notification.tickerText = AlarmType + "报警";

		/***
		 * notification.contentIntent:一个PendingIntent对象，当用户点击了状态栏上的图标时，
		 * 该Intent会被触发 notification.contentView:我们可以不在状态栏放图标而是放一个view
		 * notification.deleteIntent 当当前notification被移除时执行的intent
		 * notification.vibrate 当手机震动时，震动周期设置
		 */
		// 添加声音提示
		notification.defaults = Notification.DEFAULT_SOUND;

		// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
		notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;

		// 下边的两个方式可以添加音乐
		// notification.sound =
		// Uri.parse("file:///sdcard/notification/ringer.mp3");
		// notification.sound =
		// Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
		Intent intent = new Intent(context, Alarm.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_ONE_SHOT);

		manager.cancel(1);
		// 点击状态栏的图标出现的提示信息设置
//		notification.setLatestEventInfo(context, AlarmType + ":", AlarmTime, pendingIntent);

		manager.notify(1, notification);
		notification.flags = Notification.FLAG_AUTO_CANCEL;

	}
}
