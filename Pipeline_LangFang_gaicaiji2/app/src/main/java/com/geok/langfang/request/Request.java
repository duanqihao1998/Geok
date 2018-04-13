package com.geok.langfang.request;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.geok.langfang.pipeline.Login;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.util.ZipUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

public class Request {

	// String
	// path="http://"+MyApplication.ip+":"+MyApplication.port+"/PipelineMobileInspection/servlet/PdaServlet";
	String path = "http://" + MyApplication.ip + ":" + MyApplication.port
			+ "/pmi/servlet/PdaServlet";
	String result = null;
	private Handler handler;

	public Request(Handler handler) {
		this.handler = handler;
		// path="http://"+MyApplication.ip+":"+MyApplication.port+"/PipelineMobileInspection/servlet/PdaServlet";
		path = "http://" + MyApplication.ip + ":" + MyApplication.port + "/pmi/servlet/PdaServlet";
		// path="http://192.168.1.104:8080/PipelineMobileInspection/servlet/PdaServlet";
		// path="http://1.202.91.206:8088/pmi/servlet/PdaServlet";
	}

	/**
	 *
	 * @param username
	 * @param password
	 * @param telnum
	 * @param imei
	 * @param emiltype
	 *            reqType 请求类型 1171 arg0 用户英文名 arg1 密码 Base64编码 arg2 手机号码
	 *            string可为空 arg3 IMEI string（15）唯一编码 arg4 邮箱类型 String(cnpc/ptr)
	 *
	 *            1101：管理人员登录接口
	 */
	// 登录请求（httpservice）
	public void loginRequest(final String username, final String password, final String telnum,
							 final String imei, final String mailType) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1101"));
				params.add(new BasicNameValuePair("arg0", username));
				params.add(new BasicNameValuePair("arg1", password));
				params.add(new BasicNameValuePair("arg2", telnum));
				params.add(new BasicNameValuePair("arg3", imei));
				params.add(new BasicNameValuePair("arg4", mailType));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					int code = httpResponse.getStatusLine().getStatusCode();
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						String str = EntityUtils.toString(httpResponse.getEntity());
						// result = new
						// String(EntityUtils.toString(httpResponse.getEntity())
						// .getBytes("utf-8"), "utf-8");
						result = str;
						setMessage(1, result, 1);
					} else {
						setMessage(1, "网络异常，请检查网络！", 2);
					}

				} catch (Exception e) {
					e.printStackTrace();
					setMessage(1, "网络异常，请检查网络！", 2);
				}
			}
		}).start();

	}

	// //登录请求(webservice)
	// public void loginRequset(final String username,final String
	// password,final String telnum,final String imei, final String mailType)
	// {
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	//
	//
	// // HttpPost httpRequest = new HttpPost(path);
	// String methodName = "userLogin";
	// WebServiceUtils webservice = new WebServiceUtils(
	// WebServiceInfo.nameSpace, WebServiceInfo.url,
	// methodName);
	// // List<NameValuePair> params = new ArrayList<NameValuePair>();
	// webservice.addSimpleParam("reqtype", "1101");
	// webservice.addSimpleParam("userid", username);
	// webservice.addSimpleParam("password", password);
	// webservice.addSimpleParam("phonenumber", telnum);
	// webservice.addSimpleParam("imei", imei);
	// webservice.addSimpleParam("emailtype", mailType);
	// SoapObject soapObject = webservice.getContent();
	// System.out.println("soapObject:......................... " +
	// soapObject.toString());
	// // HttpEntity httpEntity;
	// // try {
	// // httpEntity = new UrlEncodedFormEntity(params, "utf-8");
	// // httpRequest.setEntity(httpEntity);
	// // HttpClient httpClient=new DefaultHttpClient();
	// // //连接超时
	// //
	// httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
	// 3000*10);
	// // //读取超时
	// // httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
	// 3000*10);
	// // HttpResponse httpResponse = httpClient.execute(httpRequest);
	// // int code = httpResponse.getStatusLine().getStatusCode();
	// if(null != soapObject){
	// result= soapObject.toString();
	// setMessage(1, result, 1);
	// }else{
	// setMessage(1, "网络异常，请检查网络！", 2);
	// }
	// }
	// }).start();
	//
	// }
	/**
	 *
	 * @param userid
	 * @param proceeid
	 * @param imei
	 *            reqType 请求类型 1102 arg0 用户EID string arg1 登录进程EID string arg2
	 *            IMEI唯一编码
	 */
	// 退出请求
	public void logoutRequest(final String userid, final String proceeid, final String imei) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1102"));
				params.add(new BasicNameValuePair("arg0", userid));
				params.add(new BasicNameValuePair("arg1", proceeid));
				params.add(new BasicNameValuePair("arg2", imei));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);

					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);

					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(2, result, 1);

					} else {
						setMessage(2, "网络异常，请检查网络！", 2);
					}

				} catch (Exception e) {
					e.printStackTrace();
					setMessage(2, "网络异常，请检查网络！", 2);
				}
			}
		}).start();

	}

	/**
	 *
	 * @param userid
	 * @param imei
	 *            reqType 请求类型 1103 arg0 用户EID string arg1 IMEI唯一编码 string
	 */
	// 域字典同步请求
	public void domianRequest(final String userid, final String imei) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1103"));
				params.add(new BasicNameValuePair("arg0", userid));
				params.add(new BasicNameValuePair("arg1", imei));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						Login.syncnum+=20;
						setMessage(3, result, 1);
					} else {
						setMessage(3, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(3, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @param userid
	 * @param imei
	 * @param departId
	 *            reqType 请求类型 1104 arg0 用户EID string arg1 IMEI唯一编码 string arg2
	 *            部门EID string
	 */
	// 管线连线同步请求
	public void lineSyncRequest(final String userid, final String imei, final String departId) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1104"));
				params.add(new BasicNameValuePair("arg0", userid));
				params.add(new BasicNameValuePair("arg1", imei));
				params.add(new BasicNameValuePair("arg2", departId));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						Login.syncnum+=20;
						setMessage(4, result, 1);
					} else {
						setMessage(4, "网络异常，请检查网络！", 2);
					}
				} catch (IOException e) {
					e.printStackTrace();
					setMessage(4, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @param userid
	 * @param imei
	 * @param lineid
	 * @param departId
	 *
	 *            reqType 请求类型 1105 arg0 用户EID string arg1 IMEI唯一编码 string arg2
	 *            线路EID string arg3 部门EID string
	 */
	// 管线桩请求
	public void pileSyncRequest(final String userid, final String imei, final String lineid,
								final String departId) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1105"));
				params.add(new BasicNameValuePair("arg0", userid));
				params.add(new BasicNameValuePair("arg1", imei));
				params.add(new BasicNameValuePair("arg2", lineid));
				params.add(new BasicNameValuePair("arg3", departId));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						Login.syncnum+=20;
						setMessage(5, result, 1);
					} else {
						setMessage(5, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(5, "网络异常，请检查网络！", 2);
				}
			}
		}).start();

	}

	public void pileSyncRequest1(final String beginstation, final String endstation, final String lineloopeventid,
								final String departId) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1105"));
				params.add(new BasicNameValuePair("arg0", beginstation));
				params.add(new BasicNameValuePair("arg1", endstation));
				params.add(new BasicNameValuePair("arg2", lineloopeventid));
				params.add(new BasicNameValuePair("arg3", departId));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						Login.syncnum+=20;
						setMessage(5, result, 1);
					} else {
						setMessage(5, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(5, "网络异常，请检查网络！", 2);
				}
			}
		}).start();

	}

	/**
	 *
	 * @param userid
	 * @param imei
	 * @param lon
	 * @param lat
	 * @param time
	 *
	 *            reqType 请求类型 1106 arg0 用户EID string arg1 IMEI唯一编码 string arg2
	 *            实时经度 double arg3 实时纬度 double arg4 时间 “2011-01-01 09:00:00”
	 */
	// GPS实时上报请求
	public void GPSUploadRequest(final String userid, final String imei, final String lon,
								 final String lat, final String time, final String arg5) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1106"));
				params.add(new BasicNameValuePair("arg0", userid));
				params.add(new BasicNameValuePair("arg1", imei));
				params.add(new BasicNameValuePair("arg2", lon));
				params.add(new BasicNameValuePair("arg3", lat));
				params.add(new BasicNameValuePair("arg4", time));
				params.add(new BasicNameValuePair("arg5", arg5));

				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(6, result, 1);
					} else {
						setMessage(6, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(6, "网络异常，请检查网络！", 2);
				}
			}
		}).start();

	}

	/**
	 *
	 * @param imei
	 * @param userid
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 * @param arg8
	 * @param arg9
	 * @param arg10
	 * @param arg11
	 * @param arg12
	 *            reqType 请求类型 1107 arg1 年份 如:2012 arg2 月份 如:12 arg3 线路eid
	 *            string arg4 桩号ID String arg5 电位值（V） double arg6
	 *            用户eid，即测试人eventid arg7 测试时间 Date(2012-12-12 ) arg8 备注 arg9
	 *            交流电干扰电压（V） Int arg10 土壤电阻率 Int arg11 温度 Int
	 */
	// 保护电位上报请求
	public void protectPotentialRequest(final String arg0, final String arg1, final String arg2,
										final String arg3, final String arg4, final String arg5, final String arg6,
										final String arg7, final String arg8, final String arg9, final String arg10,
										final String arg11, final String arg12, final String type) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				if(!(type.equals("autoup"))){
					Tools.isRun = true;
				}
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1107"));
				params.add(new BasicNameValuePair("arg0", arg0));
				params.add(new BasicNameValuePair("arg1", arg1));
				params.add(new BasicNameValuePair("arg2", arg2));
				params.add(new BasicNameValuePair("arg3", arg3));
				params.add(new BasicNameValuePair("arg4", arg4));
				params.add(new BasicNameValuePair("arg5", arg5));
				params.add(new BasicNameValuePair("arg6", arg6));
				params.add(new BasicNameValuePair("arg7", arg7));
				params.add(new BasicNameValuePair("arg8", arg8));
				params.add(new BasicNameValuePair("arg9", arg9));
				params.add(new BasicNameValuePair("arg10", arg10));
				params.add(new BasicNameValuePair("arg11", arg11));
				params.add(new BasicNameValuePair("arg12", arg12));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(7, result, 1);
					} else {
						setMessage(7, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(7, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @param imei
	 * @param userid
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 * @param arg8
	 * @param arg9
	 * @param arg10
	 *            reqType 请求类型 1108 arg1 用户EID（即测试人） arg2 年份 Int (如2012) arg3
	 *            线路id arg4 桩号ID arg5 电位值 Int arg6 测试时间 Date(如2012-12-12) arg7
	 *            备注 arg8 交流电干扰电压 Int arg9 温度 int arg10 天气
	 */
	// 自然电位上报请求
	public void naturalPotentialRequest(final String arg0, final String arg1, final String arg2,
										final String arg3, final String arg4, final String arg5, final String arg6,
										final String arg7, final String arg8, final String arg9, final String arg10, final String type) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if(!(type.equals("autoup"))){
					Tools.isRun = true;
				}
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1108"));
				params.add(new BasicNameValuePair("arg0", arg0));
				params.add(new BasicNameValuePair("arg1", arg1));
				params.add(new BasicNameValuePair("arg2", arg2));
				params.add(new BasicNameValuePair("arg3", arg3));
				params.add(new BasicNameValuePair("arg4", arg4));
				params.add(new BasicNameValuePair("arg5", arg5));
				params.add(new BasicNameValuePair("arg6", arg6));
				params.add(new BasicNameValuePair("arg7", arg7));
				params.add(new BasicNameValuePair("arg8", arg8));
				params.add(new BasicNameValuePair("arg9", arg9));
				params.add(new BasicNameValuePair("arg10", arg10));

				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(8, result, 1);
					} else {
						setMessage(8, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(8, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @param imei
	 * @param userid
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 * @param arg8
	 * @param arg9
	 * @param arg10
	 *            reqType 请求类型 1109 arg1 地床编号eventid arg2 用户EID (即测试人EVENTID)
	 *            string arg3 测试日期 Date（2012-12-12） arg4 规定值 double arg5 测试值
	 *            Double arg6 结论 string arg7 年份 arg8 半年（01表示上半年，02表示下半年） int
	 */

	// 接地电阻上报请求
	public void groundingResistanceRequest(final String arg0, final String arg1,
										   final String userid, final String arg3, final String arg4, final String arg5,
										   final String arg6, final String arg7 , final String type) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if(!(type.equals("autoup"))){
					Tools.isRun = true;
				}
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1109"));
				params.add(new BasicNameValuePair("arg0", arg0));
				params.add(new BasicNameValuePair("arg1", arg1));
				params.add(new BasicNameValuePair("arg2", userid));
				params.add(new BasicNameValuePair("arg3", arg3));
				params.add(new BasicNameValuePair("arg4", arg4));
				params.add(new BasicNameValuePair("arg5", arg5));
				params.add(new BasicNameValuePair("arg6", arg6));
				params.add(new BasicNameValuePair("arg7", arg7));

				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(9, result, 1);
					} else {
						setMessage(9, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(9, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @param imei
	 * @param userid
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 * @param arg8
	 * @param arg9
	 * @param arg10
	 * @param arg11
	 * @param arg12
	 * @param arg13
	 * @param arg14
	 * @param arg15
	 * @param arg16
	 * @param arg17
	 * @param arg18
	 * @param arg19
	 * @param arg20
	 * @param arg21
	 *
	 *            reqType 请求类型 1110 arg1 测试人eid,即用户EID string arg2 漏点所在管线ID arg3
	 *            桩号ID string arg4 偏移量 double米 arg5 修复对象 arg6 检漏日期
	 *            Date(2012-12-12) arg7 检漏环周位置 arg8 漏点处土壤环境描述 arg9 防腐层破损情况--外观描述
	 *            arg10 防腐层破损情况--破损面积 double arg11 管道金属腐蚀情况--表面描述 arg12
	 *            面积(管道金属腐蚀情况--腐蚀坑形状和腐蚀坑深度) double arg13
	 *            个数(管道金属腐蚀情况--腐蚀坑形状和腐蚀坑深度) int arg14 最大(管道金属腐蚀情况--腐蚀坑形状和腐蚀坑深度)
	 *            int arg15 最小(管道金属腐蚀情况--腐蚀坑形状和腐蚀坑深度) mm int arg16 腐蚀层补修处理情况
	 *            arg17 补漏漏日期 date(2012-12-21) arg18 破损类型 (01 机械损伤 02人为损伤 03 薄弱
	 *            04 老化 05 其他)，传给后台的是具体的值，如“人为损伤” arg19 补漏类型 (01 防腐蚀修补 02
	 *            管道补焊加强) 传给后台的是具体的值，如“防腐蚀修补” arg20 管体修复信息 arg21 备注
	 */

	// 防腐侧漏上报请求
	public void ntisepticRequest(final String arg0, final String arg1, final String arg2,
								 final String arg3, final String arg4, final String arg5, final String arg6,
								 final String arg7, final String arg8, final String arg9, final String arg10,
								 final String arg11, final String arg12, final String arg13, final String arg14,
								 final String arg15, final String arg16, final String arg17, final String arg18,
								 final String arg19, final String arg20, final String type) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				if(!(type.equals("autoup"))){
					Tools.isRun = true;
				}
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1110"));
				params.add(new BasicNameValuePair("arg0", arg0));
				params.add(new BasicNameValuePair("arg1", arg1));
				params.add(new BasicNameValuePair("arg2", arg2));
				params.add(new BasicNameValuePair("arg3", arg3));
				params.add(new BasicNameValuePair("arg4", arg4));
				params.add(new BasicNameValuePair("arg5", arg5));
				params.add(new BasicNameValuePair("arg6", arg6));
				params.add(new BasicNameValuePair("arg7", arg7));
				params.add(new BasicNameValuePair("arg8", arg8));
				params.add(new BasicNameValuePair("arg9", arg9));
				params.add(new BasicNameValuePair("arg10", arg10));
				params.add(new BasicNameValuePair("arg11", arg11));
				params.add(new BasicNameValuePair("arg12", arg12));
				params.add(new BasicNameValuePair("arg13", arg13));
				params.add(new BasicNameValuePair("arg14", arg14));
				params.add(new BasicNameValuePair("arg15", arg15));
				params.add(new BasicNameValuePair("arg16", arg16));
				params.add(new BasicNameValuePair("arg17", arg17));
				params.add(new BasicNameValuePair("arg18", arg18));
				params.add(new BasicNameValuePair("arg19", arg19));
				params.add(new BasicNameValuePair("arg20", arg20));

				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(10, result, 1);
					} else {
						setMessage(10, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(10, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	// //管道巡线请求
	// public void pipeRequest()
	// {
	//
	// }

	// 问题上报
	public void problemUploadRequest(final String guid, final String userid, final String lineid,
									 final String occurtime, final String offset, final String type,
									 final String problemdes, final List<String> photopath, final String departId,
									 final String uploadTime, final String lon, final String lat,
									 final List<String> imagedes, final String pileid, final String location,
									 final String plan, final String queresult, final String type1) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if(!(type1.equals("autoup"))){
					Tools.isRun = true;
				}
				String uploadimageBuffer = "";
				String uploadimagedes = "";
				try {
					for (int i = 0; i < photopath.size(); i++) {
						FileInputStream fis = new FileInputStream(photopath.get(i));
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						byte[] buffer = new byte[1024];
						int count = 0;
						while ((count = fis.read(buffer)) >= 0) {
							baos.write(buffer, 0, count);
						}
						uploadimageBuffer = uploadimageBuffer
								+ new String(Base64.encode(baos.toByteArray(), 1)) + ";";

						uploadimagedes = uploadimagedes + imagedes.get(i) + "###";
						fis.close();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1111"));
				params.add(new BasicNameValuePair("arg0", guid));
				params.add(new BasicNameValuePair("arg1", userid));
				params.add(new BasicNameValuePair("arg2", lineid));
				params.add(new BasicNameValuePair("arg3", pileid));
				params.add(new BasicNameValuePair("arg4", offset));
				params.add(new BasicNameValuePair("arg5", type));
				params.add(new BasicNameValuePair("arg6", occurtime));
				params.add(new BasicNameValuePair("arg7", uploadimageBuffer)); // 图片数据
				params.add(new BasicNameValuePair("arg8", uploadimagedes)); // 图片描述
				params.add(new BasicNameValuePair("arg9", problemdes));
				params.add(new BasicNameValuePair("arg10", uploadTime));
				params.add(new BasicNameValuePair("arg11", lon));
				params.add(new BasicNameValuePair("arg12", lat));
				params.add(new BasicNameValuePair("arg13", departId));
				params.add(new BasicNameValuePair("arg14", location));
				params.add(new BasicNameValuePair("arg15", plan));
				params.add(new BasicNameValuePair("arg16", queresult));

				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(11, result, 1);
					} else {
						setMessage(11, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(11, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	// /**
	// *
	// * @param imei
	// * @param userid
	// * @param arg2
	// * @param arg3
	// * @param arg4
	// * @param arg5
	// * @param arg6
	// * @param arg7
	// * @param arg8
	// * @param arg9
	// * @param arg10
	// * @param arg11
	// * @param arg12
	// reqType 请求类型 1111
	// arg0 EID 唯一编号
	// arg1 用户id
	// arg2 线路eid
	// arg3 问题发生时间
	// arg4 问题发生地点 int 米
	// arg5 问题类型
	// arg6 问题描述
	// arg7 图片数据 base64 不大于20K
	// arg8 上报单位
	// arg9 问题解决时间
	// arg10 管线位置
	// arg11 问题解决方案
	// arg12 问题解决情况
	// arg13 经度
	// arg14纬度
	// arg15图片名称
	// */
	// //问题上报请求
	// public void problemUploadRequest(final String guid,final String
	// userid,final String lineid,final String occurtime,final String offset,
	// final String type,final String problemdes,final List<String> photopath,
	// final String departId,final String uploadTime,final String lat,
	// final String lon,final List<String> imagedes,final String pileid)
	// {
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// String uploadimageBuffer = "";
	// String uploadimagedes="";
	// try{
	// for(int i=0;i<photopath.size();i++)
	// {
	// FileInputStream fis = new FileInputStream(photopath.get(i));
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// byte[] buffer = new byte[1024];
	// int count = 0;
	// while((count = fis.read(buffer)) >= 0){
	// baos.write(buffer, 0, count);
	// }
	// uploadimageBuffer=uploadimageBuffer+new
	// String(Base64.encode(baos.toByteArray(), 1))+";";
	//
	// uploadimagedes=uploadimagedes+imagedes.get(i)+"###";
	// fis.close();
	// }
	//
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	// HttpPost httpRequest = new HttpPost(path);
	// List<NameValuePair> params =new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("reqType", "1111"));
	// params.add(new BasicNameValuePair("arg0", guid));
	// params.add(new BasicNameValuePair("arg1", userid));
	// params.add(new BasicNameValuePair("arg2", lineid));
	// params.add(new BasicNameValuePair("arg3", pileid));
	// params.add(new BasicNameValuePair("arg4", offset));
	// params.add(new BasicNameValuePair("arg5", type));
	// params.add(new BasicNameValuePair("arg6", occurtime));
	// params.add(new BasicNameValuePair("arg7", uploadimageBuffer)); //图片数据
	// params.add(new BasicNameValuePair("arg8", uploadimagedes)); //图片描述
	// params.add(new BasicNameValuePair("arg9", problemdes));
	// params.add(new BasicNameValuePair("arg10", uploadTime));
	// params.add(new BasicNameValuePair("arg11", lon));
	// params.add(new BasicNameValuePair("arg12", lat));
	// params.add(new BasicNameValuePair("arg13", departId));
	//
	// HttpEntity httpEntity;
	// try {
	// httpEntity = new UrlEncodedFormEntity(params, "utf-8");
	// httpRequest.setEntity(httpEntity);
	// HttpClient httpClient=new DefaultHttpClient();
	// //连接超时
	// httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
	// 3000*10);
	// //读取超时
	// httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
	// 3000*10);
	// HttpResponse httpResponse = httpClient.execute(httpRequest);
	// if(httpResponse.getStatusLine().getStatusCode()==200)
	// {
	// result= EntityUtils.toString(httpResponse.getEntity());
	// setMessage(11, result, 1);
	// }else
	// {
	// setMessage(11, "网络异常，请检查网络！", 2);
	// }
	// }catch (Exception e) {
	// e.printStackTrace();
	// setMessage(11, "网络异常，请检查网络！", 2);
	// }
	// }
	// }).start();
	// }
	/**
	 *
	 * @param imei
	 * @param userid
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param arg6
	 * @param arg7
	 * @param arg8
	 * @param arg9
	 * @param arg10
	 * @param arg11
	 * @param arg12
	 * @param arg13
	 * @param arg14
	 * @param arg15
	 * @param arg16
	 * @param arg17
	 * @param arg18
	 * @param arg19
	 * @param arg20
	 * @param arg21
	 *            reqType 请求类型 arg0 EID arg1 用户id arg2 管线ID arg3 桩号 arg4 偏移量
	 *            arg5 测试人 arg6 问题类型 arg7 发生时间 arg8 事件描述 arg9 填报人 arg10 填报日期
	 *            arg11 照片 arg12 巡线出发时间 arg13 巡线返回时间 arg14 巡线类型 arg15 车辆 arg16
	 *            巡检频次 arg17 天气状况 arg18 起始位置 arg19 巡线记事 arg20 问题及处理情况 arg21 处理结果
	 */
	// 巡线日志请求 (废弃，巡检日志上报即是巡检记录上报)
	public void lineLogRequest(final String imei, final String userid, final String arg2,
							   final String arg3, final String arg4, final String arg5, final String arg6,
							   final String arg7, final String arg8, final String arg9, final String arg10,
							   final String arg11, final String arg12, final String arg13, final String arg14,
							   final String arg15, final String arg16, final String arg17, final String arg18,
							   final String arg19, final String arg20, final String arg21) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1112"));
				params.add(new BasicNameValuePair("arg0", imei));
				params.add(new BasicNameValuePair("arg1", userid));
				params.add(new BasicNameValuePair("arg2", arg2));
				params.add(new BasicNameValuePair("arg3", arg3));
				params.add(new BasicNameValuePair("arg4", arg4));
				params.add(new BasicNameValuePair("arg5", arg5));
				params.add(new BasicNameValuePair("arg6", arg6));
				params.add(new BasicNameValuePair("arg7", arg7));
				params.add(new BasicNameValuePair("arg8", arg8));
				params.add(new BasicNameValuePair("arg9", arg9));
				params.add(new BasicNameValuePair("arg10", arg10));
				params.add(new BasicNameValuePair("arg11", arg11));
				params.add(new BasicNameValuePair("arg12", arg12));
				params.add(new BasicNameValuePair("arg13", arg13));
				params.add(new BasicNameValuePair("arg14", arg14));
				params.add(new BasicNameValuePair("arg15", arg15));
				params.add(new BasicNameValuePair("arg16", arg16));
				params.add(new BasicNameValuePair("arg17", arg17));
				params.add(new BasicNameValuePair("arg18", arg18));
				params.add(new BasicNameValuePair("arg19", arg19));
				params.add(new BasicNameValuePair("arg20", arg20));
				params.add(new BasicNameValuePair("arg21", arg21));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
							6000 * 10);
					// 读取超时
//					httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000 * 10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(12, result, 1);
					} else {
						setMessage(12, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(12, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @param imei
	 * @param userid
	 * @param arg2
	 *            reqType 请求类型 1100 arg0 用户EID string arg1 当前版本号 string
	 */
	// 系统更新请求
	public void sytemUpdateRequest(final String userId, final String version) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1100"));
				params.add(new BasicNameValuePair("arg0", userId));
				params.add(new BasicNameValuePair("arg1", version));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(0, result, 1);
					} else {
						setMessage(0, "-1", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(0, "-1", 2);
				}
			}
		}).start();
	}

	/*
	 * 问题上报历史记录查询
	 */

	public void ProblemHistoryQuest(final String userid, final String type, final String lineid,
									final String startpile, final String endpile, final String starttime,
									final String endtime) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1133"));
				params.add(new BasicNameValuePair("arg0", userid));
				params.add(new BasicNameValuePair("arg1", type));
				params.add(new BasicNameValuePair("arg2", lineid));
				params.add(new BasicNameValuePair("arg3", startpile));
				params.add(new BasicNameValuePair("arg4", endpile));
				params.add(new BasicNameValuePair("arg5", starttime));
				params.add(new BasicNameValuePair("arg6", endtime));

				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(0, result, 1);
					} else {
						setMessage(0, "-1", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(0, "-1", 2);
				}
			}
		}).start();
	}

	// /**
	// * 列表查询（巡检记录查询1113，计划查询1114 ,自然电位记录查询1115 ,保护电位记录查询1116 ,防腐侧漏查询1117
	// ,接地电阻记录查询1118，我的任务1119
	// * 报警信息1120）
	// * @param arg0
	// * @param arg1
	// * reqType 查询类型
	// arg0 起始条 String
	// arg1 条数 String
	//
	// */
	// public void InspectionRecordQuery(final String reqType, final String
	// userId, final String startDate, final String endDate){
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// HttpPost httpRequest = new HttpPost(path);
	// List<NameValuePair> params =new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("reqType", reqType));
	// if(reqType.equals("1120")){
	// params.add(new BasicNameValuePair("arg0", userId));
	// params.add(new BasicNameValuePair("arg1", startDate));
	// }else{
	// params.add(new BasicNameValuePair("arg0", userId));
	// params.add(new BasicNameValuePair("arg1", startDate));
	// params.add(new BasicNameValuePair("arg2", endDate));
	// }
	// int arg = Integer.valueOf(reqType.substring(2, reqType.length()));
	// HttpEntity httpEntity;
	// try {
	// httpEntity = new UrlEncodedFormEntity(params, "utf-8");
	// httpRequest.setEntity(httpEntity);
	// HttpClient httpClient=new DefaultHttpClient();
	// //连接超时
	// httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
	// 3000*10);
	// //读取超时
	// httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
	// 3000*10);
	// HttpResponse httpResponse = httpClient.execute(httpRequest);
	//
	// if(httpResponse.getStatusLine().getStatusCode()==200)
	// {
	// result= EntityUtils.toString(httpResponse.getEntity());
	//
	// setMessage(arg, result, 1);
	// }else
	// {
	// setMessage(arg, "网络异常，请检查网络！", 2);
	// }
	// }catch (Exception e) {
	// e.printStackTrace();
	// setMessage(arg, "网络异常，请检查网络！", 2);
	// }
	// }
	// }).start();
	// }

	/**
	 * 列表查询（巡检记录查询1113，计划查询1114 ,自然电位记录查询1115 ,保护电位记录查询1116 ,防腐侧漏查询1117
	 * ,接地电阻记录查询1118，我的任务11191 报警信息1120）
	 *
	 * @param arg0
	 * @param arg1
	 *            reqType 查询类型 arg0 用户id String arg1 线路 String arg2 起始桩号 String
	 *            arg3 终止桩号 String arg4 起始日期 String arg5 终止日期 String
	 */
	public void InspectionRecordQuery(final String reqType, final String userId,
									  final String lineId, final String startPile, final String endPile,
									  final String startDate, final String endDate) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", reqType));
				if (reqType.equals("1119")) {
					params.add(new BasicNameValuePair("arg0", userId));
					params.add(new BasicNameValuePair("arg1", lineId)); /* 这里的lineId实际传起始日期 */
				} else if (reqType.equals("1115") || reqType.equals("1116")
						|| reqType.equals("1117")) {
					params.add(new BasicNameValuePair("arg0", userId));
					params.add(new BasicNameValuePair("arg1", lineId));
					params.add(new BasicNameValuePair("arg2", startPile));
					params.add(new BasicNameValuePair("arg3", endPile));
					params.add(new BasicNameValuePair("arg4", startDate));
					params.add(new BasicNameValuePair("arg5", endDate));
				} else {
					params.add(new BasicNameValuePair("arg0", userId));
					params.add(new BasicNameValuePair("arg1", lineId)); /* 这里的lineId实际传起始日期 */
					params.add(new BasicNameValuePair("arg2", startPile)); /* 这里的startPile实际传终止日期 */
				}

				int arg = Integer.valueOf(reqType.substring(2, reqType.length()));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);

					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						Login.syncnum+=20;
						setMessage(arg, result, 1);
					} else {
						setMessage(arg, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(arg, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	// /**
	// * 信息查询1121 arg1 lon arg2 lat
	// */
	// public void informationQuery(final String userId, final String lon,
	// final String lat) {
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// HttpPost httpRequest = new HttpPost(path);
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("reqType", "1121"));
	//
	// params.add(new BasicNameValuePair("arg0", userId));
	// params.add(new BasicNameValuePair("arg1", lon));
	// params.add(new BasicNameValuePair("arg2", lat));
	//
	// HttpEntity httpEntity;
	// try {
	// httpEntity = new UrlEncodedFormEntity(params, "utf-8");
	// httpRequest.setEntity(httpEntity);
	// HttpClient httpClient = new DefaultHttpClient();
	// // 连接超时
	// httpClient.getParams().setParameter(
	// CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
	// // 读取超时
	// httpClient.getParams().setParameter(
	// CoreConnectionPNames.SO_TIMEOUT, 3000*10);
	// HttpResponse httpResponse = httpClient.execute(httpRequest);
	//
	// if (httpResponse.getStatusLine().getStatusCode() == 200) {
	// result = EntityUtils.toString(httpResponse.getEntity());
	// setMessage(23, result, 1);
	// } else {
	// setMessage(23, "网络异常，请检查网络！", 2);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// setMessage(23, "网络异常，请检查网络！", 2);
	// }
	// }
	// }).start();
	// }

	/**
	 * 信息查询1121,事项上报1122
	 */
	/**
	 *
	 * @功能描述
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn
	 * @param reqType
	 * @param userId
	 *            用户ID
	 * @param arg1
	 *            经度
	 * @param arg2
	 *            纬度
	 * @param arg3
	 *            管线id
	 * @createDate 2013-11-29 上午11:10:20
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void informationQuery(final String reqType, final String userId, final String arg1,
								 final String arg2, final String arg3) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", reqType));
				if (reqType.equals("1121")) {
					params.add(new BasicNameValuePair("arg0", userId));
					params.add(new BasicNameValuePair("arg1", arg1));
					params.add(new BasicNameValuePair("arg2", arg2));
					params.add(new BasicNameValuePair("arg3", arg3));
				}
				HttpEntity httpEntity;
				int arg = Integer.valueOf(reqType.substring(2, reqType.length()));
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);

					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(arg, result, 1);
					} else {
						setMessage(arg, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(arg, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 * 巡检日志(列表)
	 *
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public void InspectionPlanQuery(final String arg0, final String arg1, final String arg2) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1123"));
				params.add(new BasicNameValuePair("arg0", arg0));
				params.add(new BasicNameValuePair("arg1", arg1));
				params.add(new BasicNameValuePair("arg2", arg2));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(23, result, 1);
					} else {
						setMessage(23, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(23, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 reqType 请求类型 1124 arg1 巡检人员EVENTID，即用户eid 1 arg2 出发时间 Date(2012-12-12
	 * 12:12:12) 1 arg3 返回时间 Date(2012-12-12 12:12:12) 1 arg4 巡检类型 域值(日常巡检,
	 * 雨后巡检, 雪后巡检, 第三方施工巡检, 灾害预防巡检, 其他) 返回给后台的是具体值，如“灾害预防巡检” 1 arg5 巡检人员类型
	 * 域值（01巡线工，02管道工），返回给后台的是具体值，如“巡线工” arg6 巡检工具 域值(步巡, 车巡), 返回给后台的是具体值 arg7
	 * 巡检频次 arg8 巡检合格率 int arg9 巡检轨迹点数 int arg10 巡检仪 域值(低端无屏幕巡检仪, 低端有屏幕巡检仪,
	 * 智能巡检仪), 返回给后台的是具体值 arg11 平均时速 Double,单位是公里/小时 arg12 天气情况 arg13 道路情况 arg14
	 * 巡检记事 arg15 问题及处理情况 arg16 处理结果 arg17 交班时间 Date(2012-12-12) arg18 交班地点
	 * arg19 车辆状况 arg20 交班人 arg21 接班人 arg22 其他记事
	 */
	public void InspectRecordRequest(final String userid, final String weather, final String road,
									 final String record, final String problem, final String result1, final String time,
									 final String location, final String car, final String sender, final String receiver,
									 final String other, final String info, final String eventid, final String imei, final String type

	) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if(!(type.equals("autoup"))){
					Tools.isRun = true;
				}
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1124"));
				params.add(new BasicNameValuePair("arg0", userid));
				params.add(new BasicNameValuePair("arg1", weather));
				params.add(new BasicNameValuePair("arg2", road));
				params.add(new BasicNameValuePair("arg3", record));
				params.add(new BasicNameValuePair("arg4", problem));
				params.add(new BasicNameValuePair("arg5", result1));
				params.add(new BasicNameValuePair("arg6", time));
				params.add(new BasicNameValuePair("arg7", location));
				params.add(new BasicNameValuePair("arg8", car));
				params.add(new BasicNameValuePair("arg9", sender));
				params.add(new BasicNameValuePair("arg10", receiver));
				params.add(new BasicNameValuePair("arg11", other));
				params.add(new BasicNameValuePair("arg12", info));
				params.add(new BasicNameValuePair("arg13", eventid));
				params.add(new BasicNameValuePair("arg14", imei));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(24, result, 1);
					} else {
						setMessage(24, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(24, "网络异常，请检查网络！", 2);
				}
			}
		}).start();

	}

	/**
	 * 巡检日志(详细)
	 *
	 * @param arg0
	 * @param arg1
	 */
	public void InspectionInforQuery(final String arg0, final String arg1) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1125"));
				params.add(new BasicNameValuePair("arg0", arg0));
				params.add(new BasicNameValuePair("arg1", arg1));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(25, result, 1);
					} else {
						setMessage(25, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(25, "网络异常，请检查网络！", 2);
				}
			}
		}).start();

	}

	/**
	 * 组织机构同步
	 */
	public void UnitRequest(final String userId) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1126"));
				params.add(new BasicNameValuePair("arg0", userId));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						Login.syncnum+=20;
						setMessage(26, result, 1);
					} else {
						setMessage(26, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(26, "网络异常，请检查网络！", 2);
				}
			}
		}).start();

	}

	/**
	 * 管理范围同步
	 */
	public void SubSystemRequest(final String userId,final String unitId) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1127"));
				params.add(new BasicNameValuePair("arg0", userId));
				params.add(new BasicNameValuePair("arg1", unitId));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						Login.syncnum+=20;
						setMessage(27, result, 1);
					} else {
						setMessage(27, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(27, "网络异常，请检查网络！", 2);
				}

			}
		}).start();

	}

	/**
	 * 我的任务上报
	 *
	 * @param arg0
	 *            巡检点或桩的EVENTID
	 * @param arg1
	 *            到达时间 2012-12-12 12:12:12
	 * @param arg2
	 *            到 达时经度 double
	 * @param arg3
	 *            到达时纬度 Double
	 */
	public void MyTaskRequest(final String arg0, final String arg1, final String arg2,
							  final String arg3, final String type) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if(!(type.equals("autoup"))){
					Tools.isRun = true;
				}
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1128"));
				params.add(new BasicNameValuePair("arg0", arg0));
				params.add(new BasicNameValuePair("arg1", arg1));
				params.add(new BasicNameValuePair("arg2", arg2));
				params.add(new BasicNameValuePair("arg3", arg3));

				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(28, result, 1);
					} else {
						setMessage(28, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(28, "网络异常，请检查网络！", 2);
				}
			}
		}).start();

	}

	/**
	 * 人员考评
	 *
	 * @param userId
	 */
	public void JobEvaluateQuery(final String userId, final String departId) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1129"));
				params.add(new BasicNameValuePair("arg0", userId));
				params.add(new BasicNameValuePair("arg1", departId));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(29, result, 1);
					} else {
						setMessage(29, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(29, "网络异常，请检查网络！", 2);
				}
			}
		}).start();

	}

	/**
	 * 巡检计划
	 *
	 * @param userId
	 */
	public void QueryPlanQuery(final String userId) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1130"));
				params.add(new BasicNameValuePair("arg0", userId));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(30, result, 1);
					} else {
						setMessage(30, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(30, "网络异常，请检查网络！", 2);
				}
			}
		}).start();

	}

	/**
	 * 地床编号同步
	 *
	 * @param userId
	 */
	public void CpgroundbedQuery(final String userId) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1131"));
				params.add(new BasicNameValuePair("arg0", userId));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(31, result, 1);
					} else {
						setMessage(31, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(31, "网络异常，请检查网络！", 2);
				}
			}
		}).start();

	}

	/**
	 * 根据用户id获得报警信息
	 *
	 * @param userId
	 */
	public void AlarmRequest(final String userId) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1120"));
				params.add(new BasicNameValuePair("arg0", userId));

				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);

					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());

						setMessage(20, result, 1);
					} else {
						setMessage(20, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(20, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 * 获得某人的任务信息
	 *
	 * @param userid
	 */
	public void TaskInfo(final String userid) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1132"));

				params.add(new BasicNameValuePair("arg0", userid));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(32, result, 1);
					} else {
						setMessage(32, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(32, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @功能描述 管线信息查询
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn
	 * @param userId
	 * @param lineId
	 * @createDate 2013-11-19 上午10:42:20
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void LineInfoSearchRequest(final String userId, final String lineId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1141"));
				params.add(new BasicNameValuePair("arg0", userId));
				params.add(new BasicNameValuePair("arg1", lineId));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);

					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(41, result, 1);
					} else {
						setMessage(41, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(41, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @功能描述 选择人员
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn
	 * @param userId
	 * @createDate 2013-11-19 上午10:39:02
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void SelectPersonRequest(final String userId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1142"));
				params.add(new BasicNameValuePair("arg0", userId));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);

					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(42, result, 1);
					} else {
						setMessage(42, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(42, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @功能描述 事项上报统计
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @createDate 2013-11-19 上午10:46:35
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void StatisticsProblemRequest(final String userId, final String startDate,
										 final String endDate) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1143"));
				params.add(new BasicNameValuePair("arg0", userId));
				params.add(new BasicNameValuePair("arg1", startDate));
				params.add(new BasicNameValuePair("arg2", endDate));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);

					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(43, result, 1);
					} else {
						setMessage(43, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(43, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @功能描述 设备使用状况统计
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @createDate 2013-11-19 上午10:42:58
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void StatisticsDeviceRequest(final String userId, final String startDate,
										final String endDate) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1144"));
				params.add(new BasicNameValuePair("arg0", userId));
				params.add(new BasicNameValuePair("arg1", startDate));
				params.add(new BasicNameValuePair("arg2", endDate));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);

					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(44, result, 1);
					} else {
						setMessage(44, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(44, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @功能描述 巡检合格率统计
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @createDate 2013-11-19 上午10:43:06
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void StatisticsQualifiedrateRequest(final String userId, final String startDate,
											   final String endDate) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1145"));
				params.add(new BasicNameValuePair("arg0", userId));
				params.add(new BasicNameValuePair("arg1", startDate));
				params.add(new BasicNameValuePair("arg2", endDate));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);

					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(45, result, 1);
					} else {
						setMessage(45, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(45, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @功能描述 不合格人员统计
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @createDate 2013-11-19 上午10:43:15
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void StatisticsUnqualifiedPersonRequest(final String userId, final String startDate,
												   final String endDate) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1146"));
				params.add(new BasicNameValuePair("arg0", userId));
				params.add(new BasicNameValuePair("arg1", startDate));
				params.add(new BasicNameValuePair("arg2", endDate));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);

					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(46, result, 1);
					} else {
						setMessage(46, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(46, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	// /**
	// *
	// * @功能描述 消息推送登陆
	// * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn
	// * @param alias
	// * @param ip
	// * @param imei
	// * @param onlinestate
	// * @createDate 2013-11-19 上午10:43:28
	// * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	// */
	// public void PushLoginRequest(final String alias, final String ip, final
	// String imei,
	// final String onlinestate) {
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	// HttpPost httpRequest = new HttpPost(path);
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("reqType", "1156"));
	// params.add(new BasicNameValuePair("arg0", alias));
	// params.add(new BasicNameValuePair("arg1", ip));
	// params.add(new BasicNameValuePair("arg2", imei));
	// params.add(new BasicNameValuePair("arg3", onlinestate));
	//
	// HttpEntity httpEntity;
	// try {
	// httpEntity = new UrlEncodedFormEntity(params, "utf-8");
	// httpRequest.setEntity(httpEntity);
	// HttpClient httpClient = new DefaultHttpClient();
	// // 连接超时
	// httpClient.getParams().setParameter(
	// CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
	// // 读取超时
	// httpClient.getParams().setParameter(
	// CoreConnectionPNames.SO_TIMEOUT, 3000*10);
	// HttpResponse httpResponse = httpClient.execute(httpRequest);
	// if (httpResponse.getStatusLine().getStatusCode() == 200) {
	// result = EntityUtils.toString(httpResponse.getEntity());
	// setMessage(56, result, 1);
	// } else {
	// setMessage(56, "网络异常，请检查网络！", 2);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// setMessage(56, "网络异常，请检查网络！", 2);
	// }
	// }
	// }).start();
	// }
	//
	// /**
	// *
	// * @功能描述 消息推送退出
	// * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn
	// * @param alias
	// * @param onlinestate
	// * @createDate 2013-11-19 上午10:43:33
	// * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	// */
	// public void PushLogoutRequest(final String alias, final String
	// onlinestate) {
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	// HttpPost httpRequest = new HttpPost(path);
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("reqType", "1157"));
	// params.add(new BasicNameValuePair("arg0", alias));
	// params.add(new BasicNameValuePair("arg1", onlinestate));
	// HttpEntity httpEntity;
	// try {
	// httpEntity = new UrlEncodedFormEntity(params, "utf-8");
	// httpRequest.setEntity(httpEntity);
	// HttpClient httpClient = new DefaultHttpClient();
	// // 连接超时
	// httpClient.getParams().setParameter(
	// CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
	// // 读取超时
	// httpClient.getParams().setParameter(
	// CoreConnectionPNames.SO_TIMEOUT, 3000*10);
	// HttpResponse httpResponse = httpClient.execute(httpRequest);
	// if (httpResponse.getStatusLine().getStatusCode() == 200) {
	// result = EntityUtils.toString(httpResponse.getEntity());
	// setMessage(57, result, 1);
	// } else {
	// setMessage(57, "网络异常，请检查网络！", 2);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// setMessage(57, "网络异常，请检查网络！", 2);
	// }
	// }
	// }).start();
	// }

	/**
	 *
	 * @功能描述 消息推送收到消息确认
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn
	 * @param msgId
	 * @param receiverId
	 * @param receiveFlag
	 * @createDate 2013-11-19 上午10:43:40
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void ReceiveConfirmRequest(final String msgId, final String receiverId,
									  final String receiveFlag) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1156"));
				params.add(new BasicNameValuePair("arg0", msgId));
				params.add(new BasicNameValuePair("arg1", receiverId));
				params.add(new BasicNameValuePair("arg2", receiveFlag));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(56, result, 1);
					} else {
						setMessage(56, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(56, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	public void AlarmImmediateRequest(final String userId, final String time, final String imei,
									  final String lon, final String lat, final String departmentId, final String alarmType) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1157"));
				params.add(new BasicNameValuePair("arg0", userId));
				params.add(new BasicNameValuePair("arg1", time));
				params.add(new BasicNameValuePair("arg2", imei));
				params.add(new BasicNameValuePair("arg3", lon));
				params.add(new BasicNameValuePair("arg4", lat));
				params.add(new BasicNameValuePair("arg5", departmentId));
				params.add(new BasicNameValuePair("arg6", alarmType));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(57, result, 1);
					} else {
						setMessage(57, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(57, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @功能描述 获得当前在线人员位置
	 * @author 张龙飞Email:longfeiz@geo-k.cn Tel:13671277587
	 * @param userId
	 *            人员id（单个或多个用;分开）
	 * @createDate 2013-11-22 下午2:55:37
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void getUserXY(final String departID) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1149"));
				params.add(new BasicNameValuePair("arg0", departID));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(49, result, 1);
					} else {
						setMessage(49, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(49, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @功能描述 获得当前离线人数数量
	 * @author 张龙飞Email:longfeiz@geo-k.cn Tel:13671277587
	 * @createDate 2013-11-22 下午2:57:17
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void getUserNum(final String departID) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1150"));
				params.add(new BasicNameValuePair("arg0", departID));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(50, result, 1);
					} else {
						setMessage(50, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(50, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @功能描述 查询桩坐标
	 * @author 张龙飞 Email:longfeiz@geo-k.cn Tel:13671277587
	 * @param markerName
	 *            桩名称
	 * @createDate 2013-11-22 下午2:59:12
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void getMarkerXY(final String markerName) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1152"));
				params.add(new BasicNameValuePair("arg0", markerName));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// // 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(52, result, 1);
					} else {
						setMessage(52, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(52, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @功能描述 查询人员当前所在位置
	 * @author 张龙飞 Email:longfeiz@geo-k.cn Tel:13671277587
	 * @param userName
	 *            人员名称
	 * @createDate 2013-11-22 下午3:01:45
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void getUserXyByName(final String userName) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1153"));
				params.add(new BasicNameValuePair("arg0", userName));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(53, result, 1);
					} else {
						setMessage(53, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(53, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @功能描述 查询管线位置
	 * @author 张龙飞Email:longfeiz@geo-k.cn Tel:13671277587
	 * @param lineName
	 *            管线名称
	 * @createDate 2013-11-22 下午3:04:07
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void getLineXY(final String lineName) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1154"));
				params.add(new BasicNameValuePair("arg0", lineName));

				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(54, result, 1);
					} else {
						setMessage(54, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(54, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	/**
	 *
	 * @功能描述 轨迹回放
	 * @author 张龙飞[wuchangming] Email:longfeiz@geo-k.cn Tel:13671277587
	 * @param userId
	 *            选择的人员Id（单个或多个用;分开）
	 * @param date
	 *            日期
	 * @createDate 2013-11-22 下午3:09:14
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void getLocusByUser(final String userId, final String date) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1155"));
				params.add(new BasicNameValuePair("arg0", userId));
				params.add(new BasicNameValuePair("arg1", date));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// // 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(55, result, 1);
					} else {
						setMessage(55, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(55, "网络异常，请检查网络！", 2);
				}
			}
		}).start();
	}

	// 批量关键点上报请求
	public void KeypointRequest(final String userid, final String imei, final String department,
								final String name, final String location, final String buffer, final String start,
								final String end, final String lon, final String lat, final String description,
								final String lineid, final String mileage, final String guid, final String type) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if(!(type.equals("autoup"))){
					Tools.isRun = true;
				}
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1158"));
				params.add(new BasicNameValuePair("arg0", userid));
				params.add(new BasicNameValuePair("arg1", imei));
				params.add(new BasicNameValuePair("arg2", department));
				params.add(new BasicNameValuePair("arg3", name));
				params.add(new BasicNameValuePair("arg4", location));
				params.add(new BasicNameValuePair("arg5", buffer));
				params.add(new BasicNameValuePair("arg6", start));
				params.add(new BasicNameValuePair("arg7", end));
				params.add(new BasicNameValuePair("arg8", lon));
				params.add(new BasicNameValuePair("arg9", lat));
				params.add(new BasicNameValuePair("arg10", description));
				params.add(new BasicNameValuePair("arg11", lineid));
				params.add(new BasicNameValuePair("arg12", mileage));

				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//						 httpClient.getParams().setParameter(
//						 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(58, result, 1, guid);
					} else {
						setMessage(58, "网络异常，请检查网络！", 2, guid);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(58, "网络异常，请检查网络！", 2, guid);
				}
			}
		}).start();

	}

	// 关键点上报请求
	public void KeypointRequest(final String userid, final String imei, final String department,
								final String name, final String location, final String buffer, final String start,
								final String end, final String lon, final String lat, final String description,
								final String lineid, final String mileage, final String type) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if(!(type.equals("autoup"))){
					Tools.isRun = true;
				}
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1158"));
				params.add(new BasicNameValuePair("arg0", userid));
				params.add(new BasicNameValuePair("arg1", imei));
				params.add(new BasicNameValuePair("arg2", department));
				params.add(new BasicNameValuePair("arg3", name));
				params.add(new BasicNameValuePair("arg4", location));
				params.add(new BasicNameValuePair("arg5", buffer));
				params.add(new BasicNameValuePair("arg6", start));
				params.add(new BasicNameValuePair("arg7", end));
				params.add(new BasicNameValuePair("arg8", lon));
				params.add(new BasicNameValuePair("arg9", lat));
				params.add(new BasicNameValuePair("arg10", description));
				params.add(new BasicNameValuePair("arg11", lineid));
				params.add(new BasicNameValuePair("arg12", mileage));

				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						setMessage(58, result, 1);
					} else {
						setMessage(58, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(58, "网络异常，请检查网络！", 2);
				}
			}
		}).start();

	}

	/**
	 *
	 * @功能描述
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn
	 * @param userid
	 *            用户id
	 * @param imei
	 *            设备编号
	 * @param version
	 *            当前版本
	 * @createDate 2014-1-16 上午10:55:26
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void VersionUpdateRequest(final String userid, final String imei, final String version) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Tools.isRun = true;
				HttpPost httpRequest = new HttpPost(path);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("reqType", "1170"));
				params.add(new BasicNameValuePair("arg0", userid));
				params.add(new BasicNameValuePair("arg1", imei));
				params.add(new BasicNameValuePair("arg2", version));
				HttpEntity httpEntity;
				try {
					httpEntity = new UrlEncodedFormEntity(params, "utf-8");
					httpRequest.setEntity(httpEntity);
					HttpClient httpClient = new DefaultHttpClient();
					// 连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT, 3000*10);
					// 读取超时
//					 httpClient.getParams().setParameter(
//					 CoreConnectionPNames.SO_TIMEOUT, 3000*10);
					HttpResponse httpResponse = httpClient.execute(httpRequest);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = EntityUtils.toString(httpResponse.getEntity());
						// result =
						// "[{\"VERSION\":\"3.1.1\",\"REMARK\":\"1.侧拉抽屉透明化;2.登陆时加载提示;3.登陆界面添加系统设置入口\",\"TYPE\":\"0\"}]";
						setMessage(70, result, 1);
					} else {
						setMessage(70, "网络异常，请检查网络！", 2);
					}
				} catch (Exception e) {
					e.printStackTrace();
					setMessage(70, "网络异常，请检查网络！", 2);
				}
			}
		}).start();

	}



	/**
	 * arg表示哪个方法，result连接返回的内容，flag判断连接是否成功，1为成功，2为失败
	 *
	 * @param arg
	 * @param result
	 * @param flaglogout
	 */
//	 public void setMessage(int arg, String result, int flaglogout) {
//	 //
//	 Message message = new Message();
//	 message.arg1 = arg;
//	 Bundle bundle = new Bundle();
//	 bundle.putString("result", result);
//	 bundle.putInt("flag", flaglogout);
//	 message.setData(bundle);
//	 handler.sendMessage(message);
//	 }

	// 压缩时用
	public void setMessage(int arg, String result, int flaglogout) {
		Tools.isRun = false;
		//
		Message message = new Message();
		message.arg1 = arg;
		String str = "连不上服务器，请联系管理员";
		try {
			// String a = new String(result.getBytes("utf-8"), "utf-8");
			str = ZipUtil.uncompress(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bundle bundle = new Bundle();
		bundle.putString("result", str);
		bundle.putInt("flag", flaglogout);
		message.setData(bundle);
		handler.sendMessage(message);
	}

	// 压缩时用,批量上报时用
	public void setMessage(int arg, String result, int flaglogout, String guid) {
		Tools.isRun = false;
		//
		Message message = new Message();
		message.arg1 = arg;
		String str = "连不上服务器，请联系管理员";
		try {
			// String a = new String(result.getBytes("utf-8"), "utf-8");
			str = ZipUtil.uncompress(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bundle bundle = new Bundle();
		bundle.putString("result", str);
		bundle.putInt("flag", flaglogout);
		bundle.putString("guid", guid);
		message.setData(bundle);
		handler.sendMessage(message);
	}
	//
}
