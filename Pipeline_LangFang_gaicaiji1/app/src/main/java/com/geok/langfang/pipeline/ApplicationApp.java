package com.geok.langfang.pipeline;


import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class ApplicationApp extends Application {
	String alarmType = "超速";
	boolean isRead = false;
	String imei;
	String userEventid;
	String ip;
	String part;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public String getUserEventid() {
		return userEventid;
	}

	public void setUserEventid(String userEventid) {
		this.userEventid = userEventid;
	}

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(boolean isRead) {
		this.isRead = isRead;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	private static ApplicationApp mInstance = null;
	public boolean m_bKeyRight = true;
	public BMapManager mBMapManager = null;

//	public static final String strKey = "cPfSb2sQbasoEcOfjGvul8z9";
	public static final String strKey = "yOG73gaGyGCZQeObCbWICSeNgUqGTmN5";
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
//		initEngineManager(this);
	}

	@Override
	// 建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
		super.onTerminate();
	}
	public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(strKey,new MyGeneralListener())) {
            Toast.makeText(ApplicationApp.getInstance().getApplicationContext(), 
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
	}

	public static ApplicationApp getInstance() {
		return mInstance;
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			  if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
	                Toast.makeText(ApplicationApp.getInstance().getApplicationContext(), "您的网络出错啦！",
	                    Toast.LENGTH_LONG).show();
	            }
	            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
	                Toast.makeText(ApplicationApp.getInstance().getApplicationContext(), "输入正确的检索条件！",
	                        Toast.LENGTH_LONG).show();
	            }
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			//非零值表示key验证未通过
            if (iError != 0) {
                //授权Key错误：
//                Toast.makeText(ApplicationApp.getInstance().getApplicationContext(), 
//                        "地图认证Key出错,检查您的网络连接是否正常,网络正常请联系管理员error: "+iError, Toast.LENGTH_LONG).show();
                ApplicationApp.getInstance().m_bKeyRight = false;
                
            }
            else{
            	ApplicationApp.getInstance().m_bKeyRight = true;
//            	Toast.makeText(ApplicationApp.getInstance().getApplicationContext(), 
//                        "地图key认证成功", Toast.LENGTH_LONG).show();
            }
		}
	}
}
