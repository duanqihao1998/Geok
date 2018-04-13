package com.geok.langfang.pipeline.setting;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.InspectionPlanBean;
import com.geok.langfang.jsonbean.InspectionPlanLineInfo;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sync extends Activity implements OnClickListener {

	ListView listView;
	Button back, main;
	SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
	Request request;
	OperationDB operationDB;
	InspectionPlanBean plan;
	Button begin_sync;
	private List<Map<String, Object>> mData;
	Tools tools = new Tools();

	MyApplication myApplication;
	String domain;
	String[] temp;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tools.stopProgressDialog(Sync.this);
			switch (msg.arg1) {
			/*
			 * 处理域字典请求
			 */
			case 3:
				String domainsync = msg.getData().getString("result");

				if (msg.getData().getInt("flag") == 1) {
					if (domainsync.contains("err") || domainsync.contains("ERR")) {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("domainSuccess", "0").commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("domainTime", sf.format(new Date())).commit();
						Toast.makeText(Sync.this, "同步失败", 2000).show();
					} else {
						domain = domainsync;
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("domain", domain).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("domainTime", sf.format(new Date())).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("domainSuccess", "1").commit();
						Toast.makeText(Sync.this, "同步成功", 2000).show();
					}
				} else {
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("domainSuccess", "0").commit();
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("domainTime", sf.format(new Date())).commit();
					Toast.makeText(Sync.this, "同步失败", 2000).show();
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
						Toast.makeText(Sync.this, "同步失败", 2000).show();
					} else {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("line", linesync).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("lineTime", sf.format(new Date())).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("lineSuccess", "1").commit();
						Toast.makeText(Sync.this, "同步成功", 2000).show();
					}
				} else {
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("lineSuccess", "0").commit();
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("lineTime", sf.format(new Date())).commit();
					Toast.makeText(Sync.this, "同步失败", 2000).show();
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
						Toast.makeText(Sync.this, "同步失败", 2000).show();
					} else {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("pile", pilesync).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("pileTime", sf.format(new Date())).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("pileSuccess", "1").commit();
						Toast.makeText(Sync.this, "同步成功", 2000).show();
					}
				} else {
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("pileSuccess", "0").commit();
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("pileTime", sf.format(new Date())).commit();
					Toast.makeText(Sync.this, "同步失败", 2000).show();
				}
				break;
			// 巡检计划同步
			case 30:
				int flag = msg.getData().getInt("flag");
				if (flag == 1) {
					String str = msg.getData().getString("result");
					if ("-1".equals(str) || "-2".equals(str)) {
						Cursor cursor = operationDB.DBselect(Type.INSPECTION_PLAN);
						while (cursor.moveToNext()) {
							plan = new InspectionPlanBean();
							plan.setDEPARTMENT(cursor.getString(1));
							plan.setPLANNO(cursor.getString(2));
							plan.setINSTYPE(cursor.getString(3));
							plan.setINSPECTORTYPE(cursor.getString(4));
							plan.setINSPECTOR(cursor.getString(5));
							plan.setINSFREQ(cursor.getString(6));
							plan.setINSVEHICLE(cursor.getString(7));
							plan.setINSPROPER(cursor.getString(8));
							plan.setDETERMINANT(cursor.getString(9));
							plan.setINSBDATE(cursor.getString(10));
							plan.setINSEDATE(cursor.getString(11));

							List<InspectionPlanLineInfo> list = new ArrayList<InspectionPlanLineInfo>();
							String line = cursor.getString(12);
							String rangeflag = cursor.getString(13);

							String[] templine = line.split("#");
							String[] temprangeflag = rangeflag.split("#");

							if (templine != null) {
								for (int i = 0; i < templine.length; i++) {
									InspectionPlanLineInfo info = new InspectionPlanLineInfo();
									info.setLINELOOP(templine[i]);
									info.setRANGEFLAG(temprangeflag[i]);
									list.add(info);
								}
							}
							plan.setPlanLineInfo(list);
							initList();
							break;
						}
						getSharedPreferences("sync", MODE_PRIVATE).edit()
								.putString("planTime", sf.format(new Date())).commit();
						if ("-1".equals(str)) {
							getSharedPreferences("sync", MODE_PRIVATE).edit()
									.putString("planSuccess", "2").commit();
						} else if ("-2".equals(str)) {
							getSharedPreferences("sync", MODE_PRIVATE).edit()
									.putString("planSuccess", "1").commit();
						}
						Toast.makeText(Sync.this, "同步成功,无新的计划", 2000).show();
						cursor.close();operationDB.db.close();
					} else {
						if (str.contains("ERR")) {
							getSharedPreferences("sync", MODE_PRIVATE).edit()
									.putString("planTime", sf.format(new Date())).commit();
							getSharedPreferences("sync", MODE_PRIVATE).edit()
									.putString("planSuccess", "0").commit();
							Toast.makeText(Sync.this, "同步失败", 2000).show();
						} else {
							plan = Json.getInspectionPlan(str);
							initList();
							save(plan);
							Toast.makeText(Sync.this, "同步成功", 2000).show();
						}
					}
				} else {
					getSharedPreferences("sync", MODE_PRIVATE).edit()
							.putString("planTime", sf.format(new Date())).commit();
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("planSuccess", "0")
							.commit();
					Toast.makeText(Sync.this, "同步失败", 2000).show();
				}
				break;
			case 27:
				String managesync = msg.getData().getString("result");
				if (msg.getData().getInt("flag") == 1) {
					if (managesync.contains("err") || managesync.contains("ERR")) {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("manageSuccess", "0").commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("manageTime", sf.format(new Date())).commit();
						Toast.makeText(Sync.this, "同步失败", 2000).show();
					} else {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("manage", managesync).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("manageTime", sf.format(new Date())).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("manageSuccess", "1").commit();
						Toast.makeText(Sync.this, "同步成功", 2000).show();
					}
				} else {
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("manageTime", sf.format(new Date())).commit();
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("manageSuccess", "0").commit();
					Toast.makeText(Sync.this, "同步失败", 2000).show();
				}
				break;
			case 26:
				String unitsync = msg.getData().getString("result");
				if (msg.getData().getInt("flag") == 1) {
					if (unitsync.contains("err") || unitsync.contains("ERR")) {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("unitTime", sf.format(new Date())).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("unitSuccess", "0").commit();
						Toast.makeText(Sync.this, "同步失败", 2000).show();
					} else {
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("unit", unitsync).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("unitTime", sf.format(new Date())).commit();
						getSharedPreferences("sync", MODE_PRIVATE).edit().putString("unitSuccess", "1").commit();
						Toast.makeText(Sync.this, "同步成功", 2000).show();
					}
				} else {
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("unitTime", sf.format(new Date())).commit();
					getSharedPreferences("sync", MODE_PRIVATE).edit().putString("unitSuccess", "0").commit();
					Toast.makeText(Sync.this, "同步失败", 2000).show();
				}
				break;
			}
			mData = initData();
			initList();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sync);

		myApplication = new MyApplication(this);
		request = new Request(handler);
		operationDB = new OperationDB(getApplicationContext());
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);

		main.setVisibility(View.GONE);
		mData = initData();
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});
		main.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Tools.backMain(Sync.this);
			}
		});
		initData();
		initList();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
		return super.onKeyDown(keyCode, event);

	}

	public List<Map<String, Object>> initData() {
		SharedPreferences sp = getSharedPreferences("sync", MODE_PRIVATE);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		map = new HashMap<String, Object>();
		map.put("image", R.drawable.sync_pile);
		map.put("tittle", "桩信息同步");
		map.put("time", sp.getString("pileTime", ""));
		if (sp.getString("pileSuccess", "0").equals("1")) {
			map.put("flag", "同步成功");
		} else {
			map.put("flag", "同步失败");
		}
		map.put("bt", "开始同步");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("image", R.drawable.sync_line);
		map.put("tittle", "管线信息同步");
		map.put("time", sp.getString("lineTime", ""));
		if (sp.getString("lineSuccess", "0").equals("1")) {
			map.put("flag", "同步成功");
		} else {
			map.put("flag", "同步失败");
		}
		map.put("bt", "开始同步");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("image", R.drawable.sync_plan);
		map.put("tittle", "巡线计划同步");

		if (sp.getString("planSuccess", "0").equals("0")) {
			map.put("flag", "同步失败");
		} else {
			map.put("flag", "同步成功");
		}
		if (sp.getString("planTime", "").equals("")) {
			map.put("time", "暂未同步");
		} else {
			if (sp.getString("planSuccess", "0").equals("2")) {
				map.put("time", sp.getString("planTime", "今天没有巡检任务"));
			} else {
				map.put("time", sp.getString("planTime", ""));
			}
		}

		map.put("bt", "开始同步");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("image", R.drawable.sync_op);
		map.put("tittle", "管理范围同步");
		map.put("time", sp.getString("manageTime", ""));
		if (sp.getString("manageSuccess", "0").equals("1")) {
			map.put("flag", "同步成功");
		} else {
			map.put("flag", "同步失败");
		}
		map.put("bt", "开始同步");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("image", R.drawable.sync_deptment);
		map.put("tittle", "组织机构同步");
		map.put("time", sp.getString("unitTime", ""));
		if (sp.getString("unitSuccess", "0").equals("1")) {
			map.put("flag", "同步成功");
		} else {
			map.put("flag", "同步失败");
		}
		map.put("bt", "开始同步");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("image", R.drawable.sync_pile);
		map.put("tittle", "域字典同步");
		map.put("time", sp.getString("domainTime", ""));
		if (sp.getString("domainSuccess", "0").equals("1")) {
			map.put("flag", "同步成功");
		} else {
			map.put("flag", "同步失败");
		}
		map.put("bt", "开始同步");
		list.add(map);
		return list;
	}

	private void initList() {
		listView = (ListView) findViewById(R.id.sync_list);

		// SimpleAdapter adapter = new SimpleAdapter(Sync.this, initData(),
		// R.layout.sync_list, new String[] { "image", "tittle", "time",
		// "flag" , "bt"}, new int[] { R.id.sync_list_image1,
		// R.id.sync_tittle, R.id.sync_time, R.id.sync_flag, R.id.sync_bt});

		MyAdapter adapter = new MyAdapter(Sync.this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// if (myApplication.userid != "") {
				// if(Tools.isNetworkAvailable(Sync.this, true)){
				switch (arg2) {
				case 0:
					if (Tools.isNetworkAvailable(Sync.this, true)) {
						tools.startProgressDialog(Sync.this);
						request.pileSyncRequest(myApplication.userid, myApplication.imei,
								myApplication.userid, myApplication.depterid);
					}
					// Toast.makeText(Sync.this, "暂未开放此功能", 2000).show();
					break;
				case 1:

					if (Tools.isNetworkAvailable(Sync.this, true)) {
						tools.startProgressDialog(Sync.this);
						request.lineSyncRequest(myApplication.userid, myApplication.imei,
								myApplication.depterid);
					}
					break;
				case 2:
					if (Tools.isNetworkAvailable(Sync.this, true)) {
						tools.startProgressDialog(Sync.this);
						request.QueryPlanQuery(myApplication.userid);
					}
					break;
				case 3:
					// Toast.makeText(Sync.this, "暂未开放此功能", 1000).show();
					if (Tools.isNetworkAvailable(Sync.this, true)) {
						tools.startProgressDialog(Sync.this);
						request.SubSystemRequest(myApplication.userid,myApplication.depterid);
					}
					break;
				case 4:
					// Toast.makeText(Sync.this, "暂未开放此功能", 1000).show();
					if (Tools.isNetworkAvailable(Sync.this, true)) {
						tools.startProgressDialog(Sync.this);
						request.UnitRequest(myApplication.userid);
					}
					break;
				case 5:
					// Toast.makeText(Sync.this, "暂未开放此功能", 1000).show();
					if (Tools.isNetworkAvailable(Sync.this, true)) {
						tools.startProgressDialog(Sync.this);
						request.domianRequest(myApplication.userid, myApplication.imei);
					}
					break;
				}
			}
			// }
		});
	}

	private void save(InspectionPlanBean bean) {
		ContentValues value = new ContentValues();
		value.put("department", bean.getDEPARTMENT());
		value.put("planno", bean.getPLANNO());
		value.put("instype", bean.getINSTYPE());
		value.put("inspectortype", bean.getINSPECTORTYPE());
		value.put("inspector", bean.getINSPROPER());
		value.put("insfreq", bean.getINSFREQ());
		value.put("insvehicle", bean.getINSVEHICLE());
		value.put("insproper", bean.getINSPROPER());
		value.put("determinant", bean.getDETERMINANT());
		value.put("insbdate", bean.getINSBDATE());
		value.put("insedate", bean.getINSEDATE());
		String inspectline = "";
		String rangeflag = "";
		for (int i = 0; i < bean.getPlanLineInfo().size(); i++) {
			inspectline += bean.getPlanLineInfo().get(i).getLINELOOP() + "#";
			rangeflag += bean.getPlanLineInfo().get(i).getRANGEFLAG() + "#";
		}
		value.put("inspectline", inspectline);
		value.put("rangeflag", rangeflag);
		operationDB.DBinsert(value, Type.INSPECTION_PLAN);
		getSharedPreferences("sync", MODE_PRIVATE).edit()
				.putString("planTime", sf.format(new Date())).commit();
		getSharedPreferences("sync", MODE_PRIVATE).edit().putString("planSuccess", "1").commit();
	}

	public final class ViewHolder {
		public ImageView image;
		public TextView tittle;
		public TextView time;
		public TextView flag;
		public Button bt;
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();

		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.sync_list, null);
				holder.image = (ImageView) convertView.findViewById(R.id.sync_list_image1);
				holder.tittle = (TextView) convertView.findViewById(R.id.sync_tittle);
				holder.time = (TextView) convertView.findViewById(R.id.sync_time);
				holder.flag = (TextView) convertView.findViewById(R.id.sync_flag);
				holder.bt = (Button) convertView.findViewById(R.id.sync_bt);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
//			holder.image.setBackgroundResource((Integer) mData.get(position).get("image"));
			holder.image.setImageResource((Integer) mData.get(position).get("image"));
			holder.tittle.setText((String) mData.get(position).get("tittle"));
			holder.time.setText((String) mData.get(position).get("time"));
			holder.flag.setText((String) mData.get(position).get("flag"));

			holder.bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// if (myApplication.userid != "") {
					// if(Tools.isNetworkAvailable(Sync.this, true)){
					switch (position) {
					case 0:
						if (Tools.isNetworkAvailable(Sync.this, true)) {
							tools.startProgressDialog(Sync.this);
							request.pileSyncRequest(myApplication.userid, myApplication.imei,
									myApplication.userid, myApplication.depterid);
						}
						// Toast.makeText(Sync.this, "暂未开放此功能", 2000).show();
						break;
					case 1:
						if (Tools.isNetworkAvailable(Sync.this, true)) {
							tools.startProgressDialog(Sync.this);
							request.lineSyncRequest(myApplication.userid, myApplication.imei,
									myApplication.depterid);
						}
						break;
					case 2:
						if (Tools.isNetworkAvailable(Sync.this, true)) {
							tools.startProgressDialog(Sync.this);
							request.QueryPlanQuery(myApplication.userid);
						}
						break;
					case 3:
						// Toast.makeText(Sync.this, "暂未开放此功能", 1000).show();
						if (Tools.isNetworkAvailable(Sync.this, true)) {
							tools.startProgressDialog(Sync.this);
							request.SubSystemRequest(myApplication.userid,myApplication.depterid);
						}
						break;
					case 4:
						// Toast.makeText(Sync.this, "暂未开放此功能", 1000).show();
						if (Tools.isNetworkAvailable(Sync.this, true)) {
							tools.startProgressDialog(Sync.this);
							request.UnitRequest(myApplication.userid);
						}
						break;
					case 5:
						// Toast.makeText(Sync.this, "暂未开放此功能", 1000).show();
						if (Tools.isNetworkAvailable(Sync.this, true)) {
							tools.startProgressDialog(Sync.this);
							request.domianRequest(myApplication.userid, myApplication.imei);
						}
						break;
					}
				}
				// }
			});

			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
