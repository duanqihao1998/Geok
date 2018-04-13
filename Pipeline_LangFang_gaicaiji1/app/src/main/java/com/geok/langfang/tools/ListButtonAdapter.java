package com.geok.langfang.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.baidu.location.LocationClient;
import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.jsonbean.Freqinfo;
import com.geok.langfang.pipeline.ApplicationApp;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListButtonAdapter extends BaseAdapter {

	private Context context;// 哪个activity
	private int Resource;// xml文件
	List<Freqinfo> data;// 数据源
	Request request;// 联网操作类
	ApplicationApp app;// 全局变量类
	String FREQTEXT;// 巡检频次
	String TASKID;// 任务id
	SharedPreferences spf;// 本地文件保存已提交的关键点EVENTID
	Editor editor;// spf的修改工具提交更新数据
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//	MyLocation location1;
//	LocationClient mLocationClient;

	LocationManager lm;// 位置管理
	public ListButtonAdapter(Context context, List<Freqinfo> data, int Resource, String FREQTEXT,
			String TASKID) {
		this.context = context;
		this.data = data;
		this.Resource = Resource;
		this.FREQTEXT = FREQTEXT;
		this.TASKID = TASKID;
		request = new Request(handler);
		app = (ApplicationApp) context.getApplicationContext();
		myApplication = new MyApplication(context);
		lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	String eventid;// 需要保存的关键点id
	TextView textButton;// 提交按钮
	MyApplication myApplication;
	int position = 0;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		this.position = position;
		spf = context.getSharedPreferences("pile", Context.MODE_PRIVATE);// 保存关键点id的文件
		editor = spf.edit();
		// 获得eventid集合字符串
		String name = spf.getString("pileList", null);
		String[] idList = null;// eventid集合
		if (name != null) {
			idList = name.split(";");
		}
		// 获得无网保存的eventid集合字符串
		String saveName = spf.getString("savepileList", null);
		String[] saveidList = null;// eventid集合
		if (saveName != null) {
			saveidList = saveName.split(";");
		}

		// TODO Auto-generated method stub
		

		LayoutInflater inflater = LayoutInflater.from(context);
		// xml文件转换成view
		convertView = inflater.inflate(Resource, null);

		// 关键点名字
		TextView text = (TextView) convertView.findViewById(R.id.mywork_listitem_text);
		text.setText(data.get(position).getPOINTNAME());

		// 提交按钮
		textButton = (TextView) convertView.findViewById(R.id.mywork_listitem_button);


		if (idList != null) {
			for (int i = 0; i < data.size(); i++) {
				for (int j = 0; j < idList.length; j++) {
					// 从保存的本地文件中判断是否已提交,如果已提交修改按钮背景，设置为不可点击
					if (idList[j].equals(data.get(position).getEVENTID())) {
						textButton.setText("  ");
						// 修改背景
						textButton.setBackgroundDrawable(context.getResources().getDrawable(
								R.drawable.yixunjian));
						// 设置不可点击
						textButton.setClickable(false);
					}
				}
			}
		}
		if (saveidList != null) {
			for (int i = 0; i < data.size(); i++) {
				for (int j = 0; j < idList.length; j++) {
					// 从保存的本地文件中判断是否已提交,如果已提交修改按钮背景，设置为不可点击
					if (idList[j].equals(data.get(position).getEVENTID())) {
						textButton.setText("已保存");
						// 修改背景
						textButton.setBackgroundDrawable(context.getResources().getDrawable(
								R.drawable.xundao));
						// 设置不可点击
						textButton.setClickable(false);
					}
				}
			}
		}
		//我的任务Item项提交的点击事件
		textButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(context, x+"", 1000).show();

				// LocationManager lm=(LocationManager)
				// context.getSystemService(Context.LOCATION_SERVICE);

//				Handler handler = new Handler() {
//
//					@Override
//					public void handleMessage(Message msg) {
//						super.handleMessage(msg);
//
//					}
//
//				};
//				Toast.makeText(context, "点击成功", Toast.LENGTH_SHORT).show();
				getCoordinate();
			}
		});

		return convertView;

	}

	// 上报成功，把enentid保存在本地文件
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			System.out.println("成功======================");
			if (msg.getData().getInt("flag") == 1) {
				String pileList = spf.getString("pileList", "");
				editor.putString("pileList", pileList + eventid + ";");
				editor.commit();
				textButton.setText("已提交");
				textButton.setClickable(false);
				textButton.setBackgroundDrawable(context.getResources().getDrawable(
						R.drawable.circle_cornor_line_gray));
				lm.removeUpdates(locationListener);
			}
		}

	};
	private void getCoordinate() {
		

		try {
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

			// String provider = locationManager.getBestProvider(criteria,
			// true); // 获取GPS信息

			// 设置监听器，自动更新的最小时间为间隔(N*1000)秒或最小位移变化超过N米

			lm.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0,
					locationListener);

//			locationManager.addGpsStatusListener(gpsstatusListener);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private final LocationListener locationListener = new LocationListener() {

		public void onLocationChanged(Location location) {
			if (location == null) {
				Toast.makeText(context, "没有定位到当前位置，请重新提交", 2000).show();
			} 
			else if(location.getLongitude() > 0) {
				lm.removeUpdates(locationListener);
				eventid = data.get(position).getEVENTID();
				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				
				// 有网络提交，没网络保存到本地
				if (Tools.isNetworkAvailable(context, false, false)) {
					boolean isTwo = false;// 是否已提交过

					for (int i = 0; i < MyApplication.pileList.size(); i++) {
						if (eventid.equals(MyApplication.pileList.get(i).getEVENTID())) {
							isTwo = true;
						}
					}
					if (!isTwo) {
						double lon = Double.valueOf(data.get(position).getLON());
						double lat = Double.valueOf(data.get(position).getLAT());
						if (Tools.getDistance(lon, lat, longitude, latitude) <= myApplication.m) {
							// 上报我的任务
							request.MyTaskRequest(data.get(position).getEVENTID(),
									sf.format(new Date()),
									String.valueOf(Tools.getSubDouble(longitude)),
									String.valueOf(Tools.getSubDouble(latitude)),
									"");
							Freqinfo info = new Freqinfo();
							info.setEVENTID(data.get(position).getEVENTID());
							info.setPOINTTYPE(data.get(position).getPOINTTYPE());
							info.setPOINTID(data.get(position).getPOINTID());
							info.setLON(String.valueOf(longitude));
							info.setLAT(String.valueOf(latitude));
							// 将上报的关键点信息保存
							MyApplication.pileList.add(info);
						} else {
							Toast.makeText(context, "请确定是否在当前关键点", 2000).show();
						}

					}

				} else {
					double lon = Double.valueOf(data.get(position).getLON());

					double lat = Double.valueOf(data.get(position).getLAT());
					if (Tools.getDistance(lon, lat, longitude, latitude) <= myApplication.m) {

						String savepileList = spf.getString("savepileList", "");
						editor.putString("savepileList", savepileList + eventid + ";");
						editor.commit();

						OperationDB db = new OperationDB(context);// 数据库操作类
						ContentValues values = new ContentValues();
						values.put("pipeeventid", data.get(position).getEVENTID());
						values.put("arrivetime", sf.format(new Date()));
						values.put("lon", String.valueOf(longitude));
						values.put("lat", String.valueOf(latitude));
						values.put("isupload", "0");
						// 保存关键点信息
						db.DBinsert(values, Type.GPS_INSTASK_UPLOAD);
						textButton.setText("已保存");
						textButton.setClickable(false);
						textButton.setBackgroundDrawable(context.getResources()
								.getDrawable(R.drawable.circle_cornor_line_gray));
					} else {
						Toast.makeText(context, "请确定是否在当前关键点", 2000).show();
					}
				}

			}

		}

		public void onProviderDisabled(String provider) {
			// WriteDB(null);
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		
			// "<locationListener>本地监听状态改变\n");
			switch (status) {

			case LocationProvider.AVAILABLE:
				// "<locationListener>有可用卫星\n");
				break;
			case LocationProvider.OUT_OF_SERVICE:
				// "<locationListener>无可用卫星服务\n");
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				// "<locationListener>暂时无可用卫星服务\n");
				break;
			}

		}

	};
}
