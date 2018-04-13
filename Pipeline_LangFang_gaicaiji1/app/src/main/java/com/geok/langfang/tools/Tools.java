package com.geok.langfang.tools;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baidu.mapapi.map.LocationData;
import com.esri.core.tasks.ags.na.StopGraphic;
import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.PileSyncBean;
import com.geok.langfang.pipeline.HelpActivity;
import com.geok.langfang.pipeline.Login;
import com.geok.langfang.pipeline.MainView;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.UpdateActivity;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.LocalActivityManager;
import android.app.TimePickerDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.renderscript.Sampler.Value;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.os.Process;

public class Tools {

	public static void showOffnet(final Activity activity, boolean check) {
		LinearLayout layout = (LinearLayout) activity.findViewById(R.id.main_offnet);
		if (check) {
			layout.setVisibility(View.GONE);
		} else {
			layout.setVisibility(View.VISIBLE);
		}
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction(Settings.ACTION_SETTINGS);
				activity.startActivity(intent);
			}
		});
	}

	/**
	 * 判断是否为数字
	 */
	public static boolean checkDouble(String str) {
		try {
			Pattern pattern = Pattern.compile("\\d+\\.\\d+|\\d+");
			Matcher matcher = pattern.matcher(str);
			boolean b = matcher.matches();
			// Double.parseDouble(str);
			return b;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 计算两点坐标距离(小数点后保留2位)，单位：m;
	 * 
	 * @param lon1
	 * @param lat1
	 * @param lon2
	 * @param lat2
	 * @return
	 */
	public static double getDistance(double lon1, double lat1, double lon2, double lat2) {
		double R = 6378137.0; // 地球半径，单位：m
		double x1 = lon1 * Math.PI / 180;
		double y1 = lat1 * Math.PI / 180;
		double x2 = lon2 * Math.PI / 180;
		double y2 = lat2 * Math.PI / 180;
		double deltaX = x1 - x2;
		double deltaY = y1 - y2;
		double step = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(deltaY / 2), 2) + Math.cos(y1)
				* Math.cos(y2) * Math.pow(Math.sin(deltaX / 2), 2)));
		double s = Math.round((step * R) * 100) / 100.0;
		return s;
	}

	/**
	 * double截取出需要的格式，小数点后8位
	 * 
	 * @param value
	 * @return
	 */
	public static double getSubDouble(double value) {
		String str = String.valueOf(value);
		int index = str.indexOf(".");
		String subStr = str.substring(0, index + 9);
		double a = Double.valueOf(subStr);
		return a;
	}

	/**
	 * 打开gps设置界面
	 * 
	 * @param c
	 */
	public static void gps(Activity c) {
		Intent intent4 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		c.startActivityForResult(intent4, 0);
		c.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/**
	 * 注销，返回到登陆界面
	 * 
	 * @param c
	 */
	public static void cancel(final Context c) {
		// Intent intent3 = new Intent();
		// intent3.setClass(c, Login.class);
		// intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
		// c.startActivity(intent3);
		// ((Activity)c).overridePendingTransition(R.anim.push_right_in,
		// R.anim.push_right_out);
		final MyApplication myApplication = new MyApplication(c);
		final int myProcessID = Process.myPid();
		final String processid = String.valueOf(myProcessID);
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				stopProgressDialog(c);
				switch (msg.arg1) {
				case 2:
					String data0 = msg.getData().getString("result");
					mainBack(c, "main");
					if (msg.getData().getInt("flag") == 1) {
						if (data0.contains("ok") || data0.contains("OK")) {

							Toast.makeText(c, "已注销", 1000).show();
						} else {

							Toast.makeText(c, "注销失败，请联系管理员", 1000).show();
						}
					} else {
						Toast.makeText(c, "注销失败，请联系管理员", 1000).show();
					}
					break;
				}
			}
		};
		AlertDialog.Builder builder = new Builder(c);
		builder.setTitle("提示");
		if (c.getSharedPreferences("save_password", c.MODE_PRIVATE)
				.getString("loginCheck", "offline").equals("online")) {
			if (!(isNetworkAvailable(c))) {
				builder.setMessage("没有网络，无法通知服务器，请确定是否退出系统?");
			} else {
				builder.setMessage("是否退出系统?  退出之后无法正常巡检！！！！");
			}
		} else {
			builder.setMessage("是否退出系统?");
		}

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Request request = new Request(handler);
				if (c.getSharedPreferences("save_password", c.MODE_PRIVATE)
						.getString("loginCheck", "offline").equals("offline")) {
					mainBack(c, "main");
					Toast.makeText(c, "已退出", 1000).show();
				} else {
					if (isNetworkAvailable(c, true, false)) {
						startProgressDialog(c, "正在退出。。。。");
						request.logoutRequest(myApplication.userid, processid, myApplication.imei);
					} else {
						mainBack(c, "main");
						Toast.makeText(c, "已退出", 1000).show();
					}
				}
			}
		});
		builder.setNegativeButton("取消", null);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * 返回到主界面
	 * 
	 * @param c
	 */
	public static void backMain(Context c) {
		Intent intent3 = new Intent();
		intent3.setClass(c, MainView.class);
		intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
		c.startActivity(intent3);
		((Activity) c).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	/**
	 * 跳转到帮助界面
	 * 
	 * @param c
	 */
	public static void help(Context c) {
		Intent intent2 = new Intent();
		intent2.setClass(c, HelpActivity.class);
		c.startActivity(intent2);
		((Activity) c).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/**
	 * 
	 * @功能描述 退出到登录界面
	 * @author 张龙飞[zhanglongfei] Email:longfeiz@geo-k.cn Tel:13671277587
	 * @param c
	 * @param type
	 * @createDate 2014-1-17 下午12:40:05
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static void mainBack(Context c, String type) {
		Intent intent3 = new Intent();
		intent3.setClass(c, Login.class);
		intent3.putExtra("exit", type);
		intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
		c.startActivity(intent3);
		((Activity) c).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	public static void mainBack(Context c) {
		Intent intent3 = new Intent();
		intent3.setClass(c, Login.class);
		intent3.putExtra("exit", "mainback");
		intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置
		c.startActivity(intent3);
		((Activity) c).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	/**
	 * 退出系统
	 * 
	 * @param c
	 */
	public static void exit(final Context c) {

		final MyApplication myApplication = new MyApplication(c);
		final int myProcessID = Process.myPid();
		final String processid = String.valueOf(myProcessID);
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.arg1) {
				case 2:
					String data0 = msg.getData().getString("result");
					mainBack(c, "exit");
					if (msg.getData().getInt("flag") == 1) {
						if (data0.contains("ok") || data0.contains("OK")) {

							Toast.makeText(c, "已退出管道巡检系统", 1000).show();
						} else {

							Toast.makeText(c, "已退出，但注销系统失败，请联系管理员", 1000).show();
						}
					} else {
						Toast.makeText(c, "已退出，但注销系统失败，请联系管理员", 1000).show();
					}
					break;
				}
			}
		};
		AlertDialog.Builder builder = new Builder(c);
		builder.setTitle("提示");
		if (c.getSharedPreferences("save_password", c.MODE_PRIVATE)
				.getString("loginCheck", "offline").equals("online")) {
			if (!(isNetworkAvailable(c))) {
				builder.setMessage("没有网络，无法通知服务器，请确定是否退出系统?");
			} else {
				builder.setMessage("是否退出系统?");
			}
		} else {

		}

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Request request = new Request(handler);
				if (c.getSharedPreferences("save_password", c.MODE_PRIVATE)
						.getString("loginCheck", "offline").equals("offline")) {
					mainBack(c, "exit");

				} else {
					if (isNetworkAvailable(c, true, false)) {
						request.logoutRequest(myApplication.userid, processid, myApplication.imei);
					} else {
						mainBack(c, "exit");
					}
				}

			}
		});
		builder.setNegativeButton("取消", null);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	static LocationData retLocation = null;// 位置信息

	// public static MKLocationManager mLocationManager = null;// 位置管理设置
	// public static LocationListener mLocationListener = null;// 位置改变事件

	/**
	 * 获得位置
	 * 
	 * @param c
	 * @return
	 */
	// public static LocationData getLocation(Context c, Handler handler) {
	// //
	// // // final DecimalFormat df = new DecimalFormat("#####0.0000");
	// // mapManager = new BMapManager(c);
	// // mapManager.init("8728BEBD2582C80E5E87FFB8515A2A25EE7F80B5", null);//
	// 初始化地图key，需要联网，在网上注册
	// // mapManager.start();
	// // mLocationManager = mapManager.getLocationManager();
	// //
	// mLocationManager.enableProvider(MKLocationManager.MK_NETWORK_PROVIDER);//
	// 设置通过网络定位
	// // mLocationManager
	// // .enableProvider((int) MKLocationManager.MK_GPS_PROVIDER);// 设置通过gps定位
	// // mLocationManager.setNotifyInternal(5, 5);// 5秒更新下位置
	// // mLocationListener = new LocationListener() {
	// //
	// // @Override
	// // public void onLocationChanged(Location location) {
	// // System.out.println("zouleme============");
	// // if (location != null) {
	// // retLocation = location;
	// // // 注销位置改变事件
	// // mLocationManager.removeUpdates(mLocationListener);
	// // }
	// // }
	// // };
	// // // 注册位置改变事件
	// // mLocationManager.requestLocationUpdates(mLocationListener);
	// MyLocation location = new MyLocation(c, handler);
	// LocationClient mLocClient = location.mLocationClient;
	// mLocClient.start();
	// if(location.mylocation.latitude>0){
	// retLocation = location.mylocation;
	// }
	//
	// return retLocation;
	// }

	// public static Location getLocationProvider(LocationManager lm){
	// Location retLocation = null;
	// try{
	// // Criteria criteria = new Criteria();
	// // criteria.setAccuracy(Criteria.ACCURACY_COARSE);
	// // criteria.setAltitudeRequired(false);
	// //
	// // criteria.setBearingRequired(false);
	// //
	// // criteria.setCostAllowed(false);
	// //
	// // criteria.setPowerRequirement(Criteria.POWER_LOW);
	// //
	// // String provider = lm.getBestProvider(criteria, true);
	// // retLocation = lm.getLastKnownLocation(provider);
	// retLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	//
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	//
	// return retLocation;
	//
	// }
	public static ArrayAdapter adapter(Context c, int rId) {
		ArrayAdapter adapter1 = ArrayAdapter.createFromResource(c, rId,
				android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter1;
	}

	public static void getLayView(Class className, LinearLayout layout, Context c,
			LocalActivityManager lam) {
		Intent intent = new Intent(c, className);
		Window subActivity = lam.startActivity("activity", intent);
		View view = subActivity.getDecorView();
		layout.addView(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	}

	public static TabView[] initNavigator(Context context, int[] imagelist, int[] params) {
		TabView[] tabView = new TabView[imagelist.length];
		for (int i = 0; i < tabView.length; i++) {
			ImageView imageView = new ImageView(context);
			imageView.setAdjustViewBounds(false);
			imageView.setLayoutParams(new LayoutParams(params[0], params[1]));
			// imgItems[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
			// imageView.setPadding(2, 2, 2, 2);
			imageView.setImageResource(imagelist[i]);

			tabView[i] = new TabView(context, imageView);
			tabView[i].setPadding(10, 0, 10, 0);
		}
		return tabView;
	}

	public static TabView[] initNavigator(Context context, int[] imagelist, String[] titlelist,
			int Reaource, int[] upgrate) {
		TabView[] tabView = new TabView[imagelist.length];
		for (int i = 0; i < tabView.length; i++) {

			LayoutInflater infalter = LayoutInflater.from(context);
			RelativeLayout relativeLayout = (RelativeLayout) infalter.inflate(Reaource, null);
			ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.mainview_icon_image);
			TextView text = (TextView) relativeLayout.findViewById(R.id.mainview_icon_upgrate);
			text.setVisibility(Tools.setVisibility(upgrate[i]));
			text.setText(upgrate[i] + "");
			// RelativeLayout imageView = new ;
			// imageView.setAdjustViewBounds(false);
			// imageView.setLayoutParams(new LayoutParams(params[0],
			// params[1]));
			// imgItems[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
			// imageView.setPadding(2, 2, 2, 2);
			imageView.setImageResource(imagelist[i]);
			// TextView textView = new TextView(context);
			// textView.setText(titlelist[i]);
			TextView text2 = (TextView) relativeLayout.findViewById(R.id.mainview_icon_title);
			text2.setText(titlelist[i]);
			tabView[i] = new TabView(context, relativeLayout, null, text2);
			// tabView[i].setPadding(10, 0, 10, 0);

		}
		return tabView;
	}

	/**
	 * 判断view是否显示
	 * 
	 * @param flag
	 * @return
	 */
	public static int setVisibility(int flag) {
		int result = View.VISIBLE;
		if (flag > 0) {
			result = View.VISIBLE;
		} else {
			result = View.GONE;
		}
		return result;
	}

	public static boolean isRun = false;//正在运行为true，运行完成为false
	/**
	 * 判断是否连接网络，判断是否登录、离线
	 * 
	 * @param context
	 * @param isToast
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context, boolean isToast) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.i("NetWorkState", "Unavailabel");
			return false;
		} else {
			if(isRun){
				Toast.makeText(context, "上一次联网操作未结束", 2000).show();
				return !isRun;
			}
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						Log.i("NetWorkState", "Availabel");
						if (context.getSharedPreferences("save_password", context.MODE_PRIVATE)
								.getString("loginCheck", "offline").equals("offline")) {
							Toast.makeText(context, "请先登录再进行操作", 2000).show();
							return false;
						}
						return true;
					}
				}
			}
		}
		if (isToast) {
			Toast.makeText(context, "请检查网络", 2000).show();
		}
		return false;
	}

	/**
	 * 判断是否连接网络
	 * 
	 * @param context
	 * @param isToast
	 * @param check
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context, boolean isToast, boolean check) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.i("NetWorkState", "Unavailabel");
			return false;
		} else {
			if(isRun){
				Toast.makeText(context, "上一次联网操作未结束", 2000).show();
				return !isRun;
			}
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						Log.i("NetWorkState", "Availabel");
						return true;
					}
				}
			}
		}
		if (isToast) {
			Toast.makeText(context, "请检查网络", 2000).show();
		}
		return false;
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.i("NetWorkState", "Unavailabel");
			return false;
		} else {

			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						Log.i("NetWorkState", "Availabel");
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * 字符串里取出数字，放在一起转化为int
	 * 
	 * @param a
	 * @return
	 */
	public static int getIntByStr(String a) {
		String str = "";
		for (int i = 0; i < a.length(); i++) {
			if (a.charAt(i) <= '9' && a.charAt(i) >= '0') {
				str += a.substring(i, i + 1);
			}
		}
		return Integer.valueOf(str);
	}

	/**
	 * 
	 * @功能描述 设置日期
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel:***********
	 * @param context
	 * @param c
	 * @param dateTextView
	 * @createDate 2013-10-11 上午10:49:12
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static void setDateDialog(Context context, Calendar c, final TextView dateTextView) {
		new DatePickerDialog(context, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				int n = monthOfYear + 1;
				dateTextView.setText(year + "-" + n + "-" + dayOfMonth);
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

	}

	/**
	 * 
	 * @功能描述 设置终止日期
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel:***********
	 * @param context
	 * @param c
	 * @param startDate
	 * @param endDateTextView
	 * @createDate 2013-10-11 上午10:49:34
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static void setEndDateDialog(final Context context, Calendar c, final String startDate,
			final TextView endDateTextView) {

		if (startDate == null) {
			Toast.makeText(context, "请选择开始时间", 1000).show();
		} else if (startDate.length() == 0) {
			Toast.makeText(context, "请选择开始时间", 1000).show();
		} else {
			new DatePickerDialog(context, new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					int n = monthOfYear + 1;

					String endDate = year + "-" + n + "-" + dayOfMonth;
					try {
						Date startdate = CompareDate.StringtoDate(startDate);
						Date enddate = CompareDate.StringtoDate(endDate);
						if (CompareDate.CompareTwoDate(startdate, enddate)) {
							endDateTextView.setText(year + "-" + n + "-" + dayOfMonth);
						} else {
							Toast.makeText(context, "时间选择错误", 1000).show();
							endDateTextView.setText("日期错误，请重选");
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
		}
	}

	public static void setTimeDialog(Context context, Calendar c, final TextView timeTextView) {
		new TimePickerDialog(context, new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				timeTextView.setText(hourOfDay + ":" + minute);
			}
		}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
	}

	public static CustomProgressDialog progressDialog = null;

	public static void startProgressDialog(Context context, String message) {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(context).setMessage(message);
		}
		progressDialog.show();
	}
	public void startProgressDialog(Context context) {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(context).setMessage("加载中...");
		}
		progressDialog.show();
	}

	public static void stopProgressDialog(Context context) {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	public static int pileIndext(Context c, String lineid, String markId) {
		int position = 0;
		String str = c.getSharedPreferences("sync", c.MODE_PRIVATE).getString("pile", "-1");
		List<PileSyncBean> list1 = Json.getPileSyncList(str);
		ArrayList<String> listpile = new ArrayList<String>();
		ArrayList<String> listpileId = new ArrayList<String>();
	
		for (int i = 0; i < list1.size(); i++) {
			PileSyncBean bean = list1.get(i);
			if (lineid.equals(bean.getLINELOOPEVENTID())) {
				// listpile.add
				for (int j = 0; j < bean.getChildBean().size(); j++) {
					if(bean.getChildBean().get(j).getMARKEREVENTID().equals(markId)){
						position = j;
					}
				}

			}

		}
		return position;

	}
}
