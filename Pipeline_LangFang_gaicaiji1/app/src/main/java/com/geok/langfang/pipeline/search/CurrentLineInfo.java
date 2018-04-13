package com.geok.langfang.pipeline.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.InformationQueryBean;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.toolcase.KeypointAcquisition;

import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.Tools;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * 
 * @author Administrator 管线信息查询
 * 
 */
public class CurrentLineInfo extends Activity implements OnClickListener {

	ListView listview;
	List<InformationQueryBean> data;
	Button back, main;
	MyApplication myApplication;
	Tools tools;
	LocationManager locationManager;
	String lon = "", lat = "";
	Request request;
	public Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			tools.stopProgressDialog(CurrentLineInfo.this);
			if (msg.arg1 == 21) {
				String str = msg.getData().getString("result");
				int flag = msg.getData().getInt("flag");
				if (flag == 1) {
					if (!str.equals("-1")) {
						data = Json.getInformationQueryList(str);
						if (data.size() > 0) {
							initList();
						} else {
							Toast.makeText(getApplicationContext(), "返回结果错误，请联系管理员", 1000).show();
						}

					} else {
						Toast.makeText(getApplicationContext(), "暂无当前管线的信息", 1000).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "管道信息查询失败，请重试！", 1000).show();
				}
				 locationManager.removeUpdates(locationListener);
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.currentlineinfo);
		String SERVERNAME = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) this.getSystemService(SERVERNAME);
		myApplication = new MyApplication(this);
		tools = new Tools();
		request = new Request(myHandler);
		getCoordinate();
		Toast.makeText(CurrentLineInfo.this, "正在获取当前位置，请稍后..", 1000).show();
		tools.startProgressDialog(CurrentLineInfo.this);
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		back.setOnClickListener(this);
		main.setOnClickListener(this);
	}

	private void initList() {

		listview = (ListView) findViewById(R.id.search_lineinfo);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;

		map = new HashMap<String, Object>();
		map.put("tittle", "管线名称");
		map.put("text", data.get(0).getLINELOOPNAME());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "输送介质");
		map.put("text", data.get(0).getMEDIUMTYPE());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "长度(KM)");
		map.put("text", data.get(0).getLENGTH());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "管线类型");
		map.put("text", data.get(0).getLINETYPE());
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("tittle", "管径");
		map.put("text", data.get(0).getDIAMETER());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "壁厚(mm)");
		map.put("text", data.get(0).getWALLTHICKNESS());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "设计压力(MPa)");
		map.put("text", data.get(0).getDESIGNPRESS());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "防腐状况");
		map.put("text", data.get(0).getANTISEPSISCONDITION());
		list.add(map);

		/**
		 * 信息查询
		 * 
		 * @param data
		 * @return 管线详细信息: 管线名称：LINELOOPNAME 输送介质：MEDIUMTYPE 管网类型：干线LINETYPE
		 *         长度(km)：LENGTH 管径：DIAMETER 壁厚(mm)：WALLTHICKNESS
		 *         设计压力(MPa)：DESIGNPRESS 防腐状况：ANTISEPSISCONDITION 管材：PIPE
		 *         站场总数：STATIONNUM 压气站数：GASCOMPRESSION 泵站数:PUMPSTATION
		 *         清管站数：LINETRUNCATIONVALUEROOM 线路截断阀室(座)：PIGSTATIONS
		 *         固定资产原值：ORIGINALFIXEDASSETS
		 */
		map = new HashMap<String, Object>();
		map.put("tittle", "管材");
		map.put("text", data.get(0).getPIPE());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "站场总数");
		map.put("text", data.get(0).getSTATIONNUM());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "压气站数");
		map.put("text", data.get(0).getGASCOMPRESSION());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "泵站数");
		map.put("text", data.get(0).getPUMPSTATION());
		list.add(map);
		/**
		 * 信息查询
		 * 
		 * @param data
		 * @return 管线详细信息: 管线名称：LINELOOPNAME 输送介质：MEDIUMTYPE 管网类型：干线LINETYPE
		 *         长度(km)：LENGTH 管径：DIAMETER 壁厚(mm)：WALLTHICKNESS
		 *         设计压力(MPa)：DESIGNPRESS 防腐状况：ANTISEPSISCONDITION 管材：PIPE
		 *         站场总数：STATIONNUM 压气站数：GASCOMPRESSION 泵站数:PUMPSTATION
		 *         清管站数：LINETRUNCATIONVALUEROOM 线路截断阀室(座)：PIGSTATIONS
		 *         固定资产原值：ORIGINALFIXEDASSETS
		 */
		map = new HashMap<String, Object>();
		map.put("tittle", "清管站数");
		map.put("text", data.get(0).getLINETRUNCATIONVALUEROOM());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "线路截断阀室(座)");
		map.put("text", data.get(0).getPIGSTATIONS());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "固定资产原值");
		map.put("text", data.get(0).getORIGINALFIXEDASSETS());
		list.add(map);
		SimpleAdapter adapter = new SimpleAdapter(CurrentLineInfo.this, list, R.layout.listitem,
				new String[] { "tittle", "text" }, new int[] { R.id.listitem_text1,
						R.id.listitem_text2 });
		listview.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;

		case R.id.main:
			Tools.backMain(CurrentLineInfo.this);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
		return super.onKeyDown(keyCode, event);

	}
	
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

			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					locationListener);
			
			// locationManager.addGpsStatusListener(gpsstatusListener);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final LocationListener locationListener = new LocationListener() {

		public void onLocationChanged(Location location) {
			if (location != null && location.getLongitude() > 0) {
				lon = location.getLongitude() + "";
				lat = location.getLatitude() + "";
				if (Tools.isNetworkAvailable(CurrentLineInfo.this, true)) {
					request.informationQuery("1121", myApplication.userid, lon, lat, "");
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		locationManager.removeUpdates(locationListener);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		locationManager.removeUpdates(locationListener);
		tools.stopProgressDialog(CurrentLineInfo.this);
		super.onPause();
	}
}
