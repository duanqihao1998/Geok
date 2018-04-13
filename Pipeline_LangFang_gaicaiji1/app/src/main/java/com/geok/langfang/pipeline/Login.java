package com.geok.langfang.pipeline;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.JobEvaluateBean;
import com.geok.langfang.jsonbean.MyTaskQueryBean;
import com.geok.langfang.jsonbean.VersionBean;
import com.geok.langfang.pipeline.Mywork.Mywork;
import com.geok.langfang.pipeline.dataacquisition.Ground;
import com.geok.langfang.pipeline.setting.SettingDtial;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomDialog;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.GetLocalIpAddress;
import com.geok.langfang.tools.MyImageButton;
import com.geok.langfang.tools.PlaySoundPool;
import com.geok.langfang.tools.SIMCardInfo;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.tools.TypeQuest;

/**
 * 巡检工
 */
public class Login extends Activity implements OnClickListener, SensorEventListener,
		OnCheckedChangeListener {

	public static int syncnum;
	/** Called when the activity is first created. */
	MyImageButton btn_online, btn_offline;
	// , delete_user, delete_password;
	EditText username, password;
	CheckBox save_passwd, auto_login;
	private SensorManager sensorManager;// 传感器管理器
	private Vibrator vibrator;// 震动
	PlaySoundPool playSoundPool;
	SIMCardInfo simCardInfo;
	String[] temp;
	Request request;
	String data0;
	SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
	private static CustomProgressDialog progressDialog = null;
	public static List<MyTaskQueryBean> listTask = new ArrayList<MyTaskQueryBean>();
	PopupWindow window;// 自动提示框
	String mailType = "ptr";

	SharedPreferences spf;// 本地文件
	Editor editor;// 修改本地文件
	/*
	 * departID 部门ID departName部门名称 userID 用户ID userName 用户中文名称 processID 登录进程ID
	 * IMEI 手持端唯一编码 telNum 电话号码 domain 域字典同步数据 line 线路同步数据 pile 桩信息同步数据
	 */
	String departID, departName, userId, userName, IMEI, telNum, domain, line, pile;
	public static String alias;
	public static String processId;
	String imei;
	MyApplication myApplication;
	String groundbed; // 地床编号
	RadioGroup group;
	String current_version = "", new_version = "", update_content = "", content = "",
			update_type = "" , type = "非必要更新";
	List<VersionBean> versionlistBean = new ArrayList<VersionBean>();
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	String logintime = sDateFormat.format(new java.util.Date());
	SharedPreferences spflogintime, spfsync;
	RadioButton radiobt_ptr, radiobt_cnpc;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@SuppressLint("WrongConstant")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			//stopProgressDialog();
			switch (msg.arg1) {
			/*
			 * 处理登录请求 返回格式：OK|部门EID|部门名称|用户EID|用户中文名称|登录进程EID
			 */
			case 0:
				data0 = msg.getData().getString("result");
				if (data0.equals("-1")) {
					Toast.makeText(Login.this, "已是最新版本", 1000).show();
				} else {
					if (msg.getData().getInt("flag") == 1) {
						if (!data0.contains("/")) {
							Toast.makeText(Login.this, "更新错误，请联系管理员", 1000).show();
						} else {
							Intent i = new Intent(Login.this, UpdateActivity.class);
							i.putExtra("url", data0);
							startActivity(i);
						}
					}
				}
				break;
			case 1:
				String str = msg.getData().getString("result");
				if (msg.getData().getInt("flag") == 1) {
					if (str.contains("|")) {
						temp = str.split("\\|");
						if (temp[0].contains("err") || temp[0].contains("ERR")) {
							Toast.makeText(Login.this, temp[1], 1000).show();
						} else if (temp[0].contains("ok") || temp[0].contains("OK")) {
							CheckBoxInfo();
							if (msg.getData().getInt("flag") == 1) {
//								syncnum=0;
								startProgressDialog2();
								departID = temp[1];
								departName = temp[2];
								userId = temp[3];
								userName = temp[4];
								processId = temp[5];

								String s = spf.getString("depterid", null);
								Log.i("xxxx","xxx:"+s+"====="+departID);

								if(s!=null&&!departID.equals(s)){
									SharedPreferences.Editor edit = spfsync.edit();
									SharedPreferences.Editor edit1 = spflogintime.edit();
									edit.clear();
									edit1.clear();
								}
								String olduserId = spf.getString("userId", null);
								if(userId!=null&& !userId.equals(olduserId)){
									getSharedPreferences("pile", MODE_PRIVATE).edit().clear().commit();
								}

								editor.putString("userId", Login.this.userId);
								editor.putString("imei", IMEI);
								editor.putString("depterid", departID);
								Log.i("MyIDname", Login.this.userId +"-----"+IMEI);
								editor.putString("loginCheck", "online");

								if(temp.length>6){
								editor.putString("x", temp[6]);
								editor.putString("y", temp[7]);
								}
								editor.commit();
								// MyApplication.userid = userId;
								// MyApplication.imei = IMEI;
								// MyApplication.depterid =
								// "7ced5320-fb5b-11e2-a8cd-e41f13e36064";
								// MyApplication.inspectionType =
								// getSharedPreferences("save_password",MODE_PRIVATE).getString("inspectionType",
								// "self");
								// MyApplication.m =
								// Double.valueOf(getSharedPreferences("save_password",MODE_PRIVATE).getString("m",
								// "100.0"));
								//
								if (spfsync.getString("domain", null) == null) {
									/// 域字典同步请求
									request.domianRequest(Login.this.userId, IMEI);
								} else if (spfsync.getString("domain", null).contains("ERR")) {
									request.domianRequest(Login.this.userId, IMEI);
								} else {
									/// 请求是否更新

								}
								if (spfsync.getString("line", null) == null) {
									//管线同步
									request.lineSyncRequest(Login.this.userId, IMEI, departID);
								} else if (spfsync.getString("line", null).contains("ERR")) {
									request.lineSyncRequest(Login.this.userId, IMEI, departID);
								} else {
									// 请求是否更新

								}
								if (spfsync.getString("pile", null) == null) {
									///管线桩同步
									request.pileSyncRequest(Login.this.userId, IMEI, Login.this.userId, departID);

								} else if (spfsync.getString("pile", null).contains("ERR")) {
									request.pileSyncRequest(Login.this.userId, IMEI, Login.this.userId, departID);
								} else {
									// 请求是否更新
								}
								if (spfsync.getString("manage", null) == null) {
									///管理范围
									request.SubSystemRequest(Login.this.userId);
								} else if (spfsync.getString("manage", null).contains("ERR")) {
									request.SubSystemRequest(Login.this.userId);
								} else {
									// 请求是否更新
								}
								if (spfsync.getString("unit", null) == null) {
									///组织机构
									request.UnitRequest(Login.this.userId);
								} else if (spfsync.getString("unit", null).contains("ERR")) {
									request.UnitRequest(Login.this.userId);
								} else {
									// 请求是否更新
									syncnum=120;
								}




							if (spfsync.getString("groundbed", null) == null) {
								// 地床编号请求，获取地床编号
								request.CpgroundbedQuery(Login.this.userId);
							} else if (spfsync.getString("groundbed", null).contains("ERR")) {
								request.CpgroundbedQuery(Login.this.userId);
							} else {
								// 请求是否更新
							}

							// request.getUserXY(departID);
							// request.getMarkerXY("2194");
							// request.getLineXY("乌兰成品油干线");

							// request.getLocusByUser("陈龙喜;江正虎;", "2013-08-01");
							// request.getUserXyByName("张生良");

							// request.StatisticsUnqualifiedPersonRequest("76d13e20-8f34-11df-9349-0026b9593a24",
							// "2013-10-1", "2013-11-27");
							// request.InspectionRecordQuery("1119",
							// "8f183082-adb1-11e2-8702-60672090d36c", "", "",
							// "", "", "");
							// 若登陆时间和数据库里的时间相同
							if (logintime.equals(spflogintime.getString("inspection_time", null))) {

							} else {// 若登陆时间和数据库里的时间不同，包括数据库的时间为空，或者与登录时间不同
								getSharedPreferences("pile", MODE_PRIVATE).edit().clear().commit();// 清空本地文件里保存的上传关键点信息集合
								request.InspectionRecordQuery("1119", Login.this.userId, "", "", "", "", "");

								OperationDB operationDB=new OperationDB(Login.this);
								operationDB.DBdelete("date",9,logintime);
							}
							// userid:用这个才有我的任务
							// d68f48k0-ae1b-11e1-84c0-5cff92345733
							// request.InspectionRecordQuery("1119",
							// "8f183082-adb1-11e2-8702-60672090d36c", "", "",
							// "", "", "");
							Intent intent = new Intent("com.geok.langfang.pipeline.UpService");
							startService(intent);

							// alias = username.getText().toString()
							// .substring(0,
							// username.getText().toString().indexOf("@"));
							alias = username.getText().toString();

								JPushInterface.setAliasAndTags(getApplicationContext(), alias, null);
								String ip = "0.0.0.0";
								// if (getSystemService(WIFI_SERVICE) != null) {
								// ip = getIpAddress();
								// } else {
								// GetLocalIpAddress getip = new
								// GetLocalIpAddress();
								// ip = getip.getLocalIpAddress();
								// }

								//登陆数据更新遮罩判断消失

								while (syncnum<120){
									try {
										Thread.currentThread().sleep(5000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									syncnum=Login.syncnum;
									Log.i("syncnum",syncnum+"");
									continue;

								}
								forward();
								stopProgressDialog();
							}

//							SharedPreferences sp = getSharedPreferences("sync", MODE_PRIVATE);
//							if(sp.getString("unitSuccess", "0").equals("1")){
//								forward();
//
//							}else if(!sp.getString("domainSuccess", "0").equals("1")){
//								startProgressDialog2();
////								loginHandler.sendEmptyMessage(1);
//
//							}

						} else {
							Toast.makeText(Login.this, str, 1000).show();
						}
					}
				} else {
					stopProgressDialog();
					Toast.makeText(Login.this, "连接失败，请检查IP设置或联系管理员", 1000).show();
				}
				break;
			// 处理退出请求
			case 2:

				break;

			/*
			 * 处理域字典请求
			 */
			case 3:
				String domainsync = msg.getData().getString("result");

				if (msg.getData().getInt("flag") == 1) {
					if (domainsync.contains("err") || domainsync.contains("ERR")) {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("domainSuccess", "0").commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("domainTime", sf.format(new Date())).commit();
					} else {
						domain = domainsync;
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("domain", domain).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("domainTime", sf.format(new Date())).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("domainSuccess", "1").commit();
//						loginHandler.sendEmptyMessage(2);
					}
				} else {
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("domainSuccess", "0").commit();
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("domainTime", sf.format(new Date())).commit();
				}
				break;
			/*
			 * 处理线路同步请求
			 */
			case 4:
				String linesync = msg.getData().getString("result");

				if (msg.getData().getInt("flag") == 1) {
					if (linesync.contains("err") || linesync.contains("ERR")) {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("lineSuccess", "0").commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("lineTime", sf.format(new Date())).commit();
					} else {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("line", linesync).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("lineTime", sf.format(new Date())).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("lineSuccess", "1").commit();
//						loginHandler.sendEmptyMessage(3);
					}

				} else {
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("lineSuccess", "0").commit();
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("lineTime", sf.format(new Date())).commit();
				}
				break;
			/*
			 * 处理桩信息同步请求
			 */
			case 5:
				String pilesync = msg.getData().getString("result");
				if (msg.getData().getInt("flag") == 1) {

					if (pilesync.contains("err") || pilesync.contains("ERR")) {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("pileSuccess", "0").commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("pileTime", sf.format(new Date())).commit();
					} else {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("pile", pilesync).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("pileTime", sf.format(new Date())).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("pileSuccess", "1").commit();
//						loginHandler.sendEmptyMessage(4);
					}
				} else {
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("pileSuccess", "0").commit();
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("pileTime", sf.format(new Date())).commit();
				}
				break;
			case 19:
				String data19 = msg.getData().getString("result");
				if (msg.getData().getInt("flag") == 1) {
					spflogintime.edit().putString("inspection_time", logintime).commit();
					String userId = spf.getString("userId", null);
					if (data19.equals("-1")) {

					} else {
						listTask = Json.getMyTaskQueryList(data19);
						getSharedPreferences("sync", MODE_PRIVATE).edit()
								.putString("Data19", data19).commit();
					}
				}

				break;
			case 27:
				String managesync = msg.getData().getString("result");
				if (msg.getData().getInt("flag") == 1) {
					if (managesync.contains("err") || managesync.contains("ERR")) {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("manageSuccess", "0").commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("manageTime", sf.format(new Date())).commit();
					} else {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("manage", managesync).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("manageTime", sf.format(new Date())).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("manageSuccess", "1").commit();
//						loginHandler.sendEmptyMessage(5);
					}
				} else {
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("manageTime", sf.format(new Date())).commit();
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("manageSuccess", "0").commit();
				}
				break;
			case 26:
				String unitsync = msg.getData().getString("result");
				if (msg.getData().getInt("flag") == 1) {
					if (unitsync.contains("err") || unitsync.contains("ERR")) {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("unitTime", sf.format(new Date())).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("unitSuccess", "0").commit();
					} else {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("unit", unitsync).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("unitTime", sf.format(new Date())).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("unitSuccess", "1").commit();
//						loginHandler.sendEmptyMessage(6);
					}
				} else {
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("unitTime", sf.format(new Date())).commit();
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("unitSuccess", "0").commit();
				}
				break;
			case 31:
				String bed = msg.getData().getString("result");
				if (msg.getData().getInt("flag") == 1) {
					if (bed.contains("ERR") || bed.contains("err")) {
					} else {
						groundbed = bed;
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("groundbed", groundbed).commit();
					}

				} else {
				}
				break;
			case 70:
				String update_str = msg.getData().getString("result");
				Log.i("woaini",update_str);
				if (msg.getData().getInt("flag") == 1) {
					spflogintime.edit().putString("login_time", logintime).commit();
					if (update_str.contains("ERR") || "-1".equals(update_str)) {
						// Toast.makeText(Login.this, "更新错误，请联系管理员",
						// 1000).show();
					} else {
						getSharedPreferences("sync", MODE_PRIVATE).edit()
								.putString("update_str", update_str).commit();
						versionlistBean = Json.getVersionList(update_str);
						Log.i("woaini",versionlistBean.size()+"");
						if (versionlistBean.size() > 0) {
							Login.this.showDialog(TypeQuest.CUSTOM_DIALOG);
						} else {
							Toast.makeText(Login.this, "更新错误，请联系管理员", 1000).show();
						}
					}
				} else {
					// Toast.makeText(Login.this, "此版本即为最新版本", 1000).show();
				}
				break;
			}

		}

	};

	String exit = null;// 是否退出系统
	//LoginHandler单挑线程
//	Handler loginHandler=new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what){
//				case 1:
//					if (spfsync.getString("domain", null) == null) {
//						startProgressDialog();
//						/// 域字典同步请求
//						request.domianRequest(userId, IMEI);
//					} else if (spfsync.getString("domain", null).contains("ERR")) {
//						startProgressDialog();
//						request.domianRequest(userId, IMEI);
//					} else {
//						/// 请求是否更新
//					}
//					break;
//				case 2:
//					if (spfsync.getString("line", null) == null) {
//						startProgressDialog();
//						//管线同步
//						request.lineSyncRequest(userId, IMEI, departID);
//					} else if (spfsync.getString("line", null).contains("ERR")) {
//						startProgressDialog();
//						request.lineSyncRequest(userId, IMEI, departID);
//					} else {
//						// 请求是否更新
//
//					}
//
//					break;
//				case 3:
//					if (spfsync.getString("pile", null) == null) {
//						startProgressDialog();
//						///管线桩同步
//						request.pileSyncRequest(userId, IMEI, userId, departID);
//
//					} else if (spfsync.getString("pile", null).contains("ERR")) {
//						startProgressDialog();
//						request.pileSyncRequest(userId, IMEI, userId, departID);
//					} else {
//						// 请求是否更新
//					}
//					break;
//				case 4:
//					if (spfsync.getString("manage", null) == null) {
//						startProgressDialog();
//						///管理范围
//						request.SubSystemRequest(userId);
//					} else if (spfsync.getString("manage", null).contains("ERR")) {
//						startProgressDialog();
//						request.SubSystemRequest(userId);
//					} else {
//						// 请求是否更新
//					}
//
//					break;
//				case 5:
//					if (spfsync.getString("unit", null) == null) {
//						startProgressDialog();
//						///组织机构
//						request.UnitRequest(userId);
//					} else if (spfsync.getString("unit", null).contains("ERR")) {
//						startProgressDialog();
//						request.UnitRequest(userId);
//					} else {
//						// 请求是否更新
//					}
//					break;
//				case 6:
//						forward();
//					break;
//			}
//		}
//	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		// 根据ID找到RadioGroup实例
		group = (RadioGroup) this.findViewById(R.id.radio_mail);
		group.setOnCheckedChangeListener(Login.this);
		group.setVisibility(View.GONE);
		radiobt_ptr = (RadioButton) findViewById(R.id.radiobt_ptr);
		radiobt_cnpc = (RadioButton) findViewById(R.id.radiobt_cnpc);
		MyApplication.person = "";
		// radiobt.setChecked(true);
		Intent i = getIntent();
		exit = i.getStringExtra("exit");
		if (exit != null) {
			if (exit.equals("exit")) {
				this.finish();
			}
		}
		spflogintime = getSharedPreferences("login_time", MODE_PRIVATE);
		spfsync = getSharedPreferences("sync", MODE_PRIVATE);
		spf = getSharedPreferences("save_password", MODE_PRIVATE);
		editor = spf.edit();
		myApplication = new MyApplication(this);
		simCardInfo = new SIMCardInfo(this);
		// MyApplication.ip =
		// getSharedPreferences("save_password",MODE_PRIVATE).getString("ip",
		// "");
		// MyApplication.port =
		// getSharedPreferences("save_password",MODE_PRIVATE).getString("port",
		// "");
		request = new Request(handler);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		playSoundPool = new PlaySoundPool(Login.this);
		playSoundPool.loadSfx(R.raw.ring, 1);

		init();

	}

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this).setMessage("正在登录，请稍后...");
		}
		progressDialog.show();
	}
	private void startProgressDialog2() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this).setMessage("第一次登陆正在更新数据，请稍后...");
		}
		progressDialog.show();
	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	@SuppressLint("WrongConstant")
	@Override
	protected void onResume() {
		super.onResume();
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		// 加速度传感器
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		myApplication = new MyApplication(this);
		request = new Request(handler);
		if (exit == null) {
			// 自动登录
			if (auto_login.isChecked()) {
				if (password.getText().toString().length() > 0) {
					if (username.getText().toString() != null) {
						if (Tools.isNetworkAvailable(Login.this, true, true)) {
							request.loginRequest(username.getText().toString(), password.getText()
									.toString(), telNum, IMEI, mailType);
						}

					} else {
						Toast.makeText(this, "邮箱类型输入不正确", 2000).show();
					}
				}
			}
		}
		
	}

	@Override
	protected void onStop() {
		sensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	protected void onPause() {
		sensorManager.unregisterListener(this);
		super.onPause();

	}

	@SuppressLint("WrongConstant")
	private void init() {
//		JPushInterface.init(getApplicationContext());
		btn_online = (MyImageButton) findViewById(R.id.login_online);
		btn_offline = (MyImageButton) findViewById(R.id.login_offline);
		// delete_user=(ImageButton)findViewById(R.id.delete_user);
		// delete_password=(ImageButton)findViewById(R.id.delete_password);

		// delete_user.setOnClickListener(this);
		// delete_password.setOnClickListener(this);

		btn_online.setOnClickListener(this);
		btn_offline.setOnClickListener(this);
		username = (EditText) findViewById(R.id.login_username);
		password = (EditText) findViewById(R.id.login_password);
		save_passwd = (CheckBox) findViewById(R.id.checkbox_passwd);
		save_passwd.setChecked(getSharedPreferences("save_password", MODE_PRIVATE).getBoolean(
				"savepassword", false));
		auto_login = (CheckBox) findViewById(R.id.checkbox_auto);
		// username.addTextChangedListener(new TextWatcher() {

		// @Override
		// public void onTextChanged(CharSequence s, int start, int before,
		// int count) {
		// if (!username.getText().toString().isEmpty()) {
		// // delete_user.setVisibility(View.VISIBLE);
		// int n = username.getText().toString().length();
		// if (username.getText().toString().subSequence(n - 1, n)
		// .equals("@")) {
		// if (username.getText().toString()
		// .contains("@cnpc.com.cn")
		// || username.getText().toString()
		// .contains("@ptr.petrochina")) {
		// return;
		// }
		// getPopupWindow(username);
		// }
		// } else if (username.getText().toString().isEmpty()) {
		// // delete_user.setVisibility(View.GONE);
		// }
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		// // TODO Auto-generated method stub
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// // TODO Auto-generated method stub
		// }
		// });

		password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// if(!password.getText().toString().isEmpty()){
				// delete_password.setVisibility(View.VISIBLE);
				// }else if(password.getText().toString().isEmpty()){
				// delete_password.setVisibility(View.GONE);
				// }
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		if (myApplication.ip.equals("") || myApplication.port.equals("")) {
			if (MyApplication.checkIp == 0) {
				Toast.makeText(this, "请先设置ip和端口号", 2000).show();
				Intent intent = new Intent(this, SettingDtial.class);
				startActivity(intent);
			}

		}
		if (getSharedPreferences("save_password", MODE_PRIVATE).getBoolean("savepassword", false)) {
			username.setText(getSharedPreferences("save_password", MODE_PRIVATE).getString(
					"username", null));
			password.setText(getSharedPreferences("save_password", MODE_PRIVATE).getString(
					"password", null));
			mailType = getSharedPreferences("save_password", MODE_PRIVATE).getString("mailType",
					"ptr");
		} else {
			username.setText(getSharedPreferences("save_password", MODE_PRIVATE).getString(
					"username", null));
			password.setText("");
			mailType = getSharedPreferences("save_password", MODE_PRIVATE).getString("mailType",
					"ptr");
		}
		auto_login.setChecked(getSharedPreferences("auto_login", MODE_PRIVATE).getBoolean(
				"autologin", false));
		if (mailType.equals("cnpc")) {
			radiobt_cnpc.setChecked(true);
		} else {
			radiobt_ptr.setChecked(true);
		}
		if (!myApplication.ip.equals("")) {
			// 若登陆时间和数据库里的时间相同
			if (logintime.equals(spflogintime.getString("login_time", null))) {

			} else {// 若登陆时间和数据库里的时间不同，包括数据库的时间为空，或者与登录时间不同
				if (Tools.isNetworkAvailable(this, true, true)) {
					if (!(myApplication.ip.equals("")) && myApplication.port != null) {
						try {
							// request.sytemUpdateRequest(getVersionCode());
							request.VersionUpdateRequest(myApplication.userid, myApplication.imei, getVersionCode());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			}
		}
		try {
			current_version = getVersionCode();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	// 界面跳转
	private void forward() {
		Intent intent = new Intent();
//			intent.setClass(Login.this,MainView.class);
		intent.setClass(Login.this, Mywork.class);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/*
	 * 复选框判断 save_passwd 保存密码 auto_login 自动登录
	 */
	private void CheckBoxInfo() {
		// 自动登录
		if (auto_login.isChecked()) {
			getSharedPreferences("auto_login", MODE_PRIVATE).edit()

			.putBoolean("autologin", true).commit();
		} else {
			getSharedPreferences("auto_login", MODE_PRIVATE).edit()

			.putBoolean("autologin", false).commit();
		}

		// 保存密码
		if (save_passwd.isChecked()) {
			getSharedPreferences("save_password", MODE_PRIVATE).edit()
					.putString("username", username.getText().toString())
					.putString("password", password.getText().toString())
					.putString("mailType", mailType).putBoolean("savepassword", true).commit();
		} else {
			getSharedPreferences("save_password", MODE_PRIVATE).edit()
					.putString("username", username.getText().toString())
					.putBoolean("savepassword", false).putString("mailType", mailType).commit();
		}
	}

	@SuppressLint("WrongConstant")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.login_online:
			CheckBoxInfo();
			/*
			 * 
			 * userName 用户名 passWord 密码
			 */
			telNum = simCardInfo.getPhoneNum();
			IMEI = simCardInfo.getIMEI();
			String userName = username.getText().toString().trim();
			String passWord = password.getText().toString().trim();
			AlertDialog.Builder b  = new Builder(Login.this);
//			b.setMessage(passWord);
//			b.create().show();
			if(userName.equals("")){
				Toast.makeText(this, "请输入用户名", 1000).show();
			}else if(passWord.equals("")){
				Toast.makeText(this, "请输入密码", 2000).show();
			}else{
				if (Tools.isNetworkAvailable(Login.this, true, true)) {
					startProgressDialog2();
					// request.loginRequest(userName.substring(0,
					// userName.indexOf("@")),passWord, telNum, IMEI, mailType);
					request.loginRequest(userName, passWord, telNum, IMEI, mailType);
					// Toast.makeText(this, "mailType:" + mailType, 1000).show();
				}
			}
			// if (userName.contains("@")) {


			// } else {
			// Toast.makeText(this, "邮箱类型输入不正确", 2000).show();
			// }

			break;

		case R.id.login_offline:

			String data19 = getSharedPreferences("sync", MODE_PRIVATE).getString("Data19", "");
			if (!(data19.equals(""))) {
				listTask = Json.getMyTaskQueryList(data19);
			}

			editor.putString("loginCheck", "offline");
			editor.commit();
			CheckBoxInfo();
			forward();
			break;

		// case R.id.delete_user:
		// username.setText("");
		// case R.id.delete_password:
		// password.setText("");
		}

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		int sensorType = event.sensor.getType();
		float[] value = event.values;

//		if (sensorType == Sensor.TYPE_ACCELEROMETER) {
//			if (Math.abs(value[0]) > 15.0 || Math.abs(value[1]) > 15.0 || Math.abs(value[2]) > 20.0) {
//				vibrator.vibrate(500);
//				playSoundPool.play(1, 0);
//				// forward();
//			}
//
//		}
	}

	/*
	 * 获取当前程序的版本号
	 */
	private String getVersionCode() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		return packInfo.versionName;
	}

	@Override
	public Dialog onCreateDialog(int dialogId) {
		Dialog dialog = null;
		{
			if (versionlistBean.size() > 0) {
				if (versionlistBean.get(0) != null)
					new_version = versionlistBean.get(0).getVERSION();
				update_content = versionlistBean.get(0).getURL();
				content = update_content.replaceAll(";", "\r\n");
				update_type = versionlistBean.get(0).getTYPE();
				if ("0".equals(update_type)) {
					type = "必要更新";
				} else {
					type = "非必要更新";
				}
			}

		}
		switch (dialogId) {
		case TypeQuest.CUSTOM_DIALOG:
			CustomDialog.Builder customBuilder = new CustomDialog.Builder(Login.this);
			customBuilder.setTitle("升级提醒").setMessage(content);
			customBuilder.setVersion("新版本：" + new_version).setType("(" + type + ")");
			customBuilder.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			customBuilder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					request.sytemUpdateRequest(myApplication.userid,current_version);
				}
			});
			dialog = customBuilder.create();
			break;
		}
		return dialog;
	}

	public void getPopupWindow(View parent) {
		if (window == null) {
			LayoutInflater inflater = LayoutInflater.from(this);
			View view = inflater.inflate(R.layout.popupwindow_item, null);
			final TextView cnpcText = (TextView) view.findViewById(R.id.cnpcText);
			final TextView ptrText = (TextView) view.findViewById(R.id.ptrText);
			cnpcText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int index = username.getText().toString().indexOf("@");
					String cnpc = username.getText().toString().substring(0, index + 1);
					username.setText(cnpc + cnpcText.getText().toString());
					mailType = "cnpc";
					window.dismiss();
				}
			});
			ptrText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int index = username.getText().toString().indexOf("@");
					String ptr = username.getText().toString().substring(0, index + 1);
					username.setText(ptr + ptrText.getText().toString());
					mailType = "ptr";
					window.dismiss();
				}
			});
			window = new PopupWindow(view, android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int[] location = new int[2];
		parent.getLocationOnScreen(location);
		window.setFocusable(true);
		window.setOutsideTouchable(false);
		window.update();
		window.showAtLocation(parent, Gravity.TOP, location[0] + (location[0] / 2) + 30,
				location[1] - window.getHeight() - window.getHeight());
		// window.showAsDropDown(parent);
	}

	private String getIpAddress() {
		@SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		return intToIp(wifiInfo.getIpAddress());
	}

	private String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
				+ (i >> 24 & 0xFF);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "系统配置").setIcon(R.drawable.sys_setting);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Intent intent = new Intent();
			intent.setClass(Login.this, SettingDtial.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkedId) {
		// // TODO Auto-generated method stub
		// 获取变更后的选中项的ID
		int radioButtonId = arg0.getCheckedRadioButtonId();
		// 根据ID获取RadioButton的实例
		RadioButton rb = (RadioButton) Login.this.findViewById(radioButtonId);
		// 更新文本内容，以符合选中项
		mailType = rb.getText().toString();
		getSharedPreferences("save_password", MODE_PRIVATE).edit().putString("mailType", mailType)
				.commit();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}