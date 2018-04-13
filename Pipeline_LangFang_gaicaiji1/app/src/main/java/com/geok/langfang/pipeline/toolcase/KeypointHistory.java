package com.geok.langfang.pipeline.toolcase;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.geok.langfang.Bean.KeypointHistoryBean;
import com.geok.langfang.DB.OperationDB;
import com.geok.langfang.DB.Type;
import com.geok.langfang.pipeline.MainView;
//import com.geok.langfang.jsonbean.HistoryDataProtectBean;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.tools.Tools;

/**
 * 
 * @author wuchangming 保护电位的历史记录类
 * 
 */
public class KeypointHistory extends Activity implements OnClickListener {

	String userid;
	MyApplication myApplication;
	Context context = this;
	Button confirm;
	KeypointHistoryAdapter keypointHistoryAdapter;
	KeypointHistoryBean keypointHistoryData;
	ArrayList<KeypointHistoryBean> keypointHistoryDataList;
	private ListView listview;
	OperationDB operationDB;
	Cursor cursor;
	Button back, main, data_history_up, data_history_del, data_history_check;

	private Calendar c = null;
	private EditText et = null;
	String lineId, markId, markerstation, markerstation2;
	String start_date_string;
	Tools tools;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	Request request;
	String guid;
	int noHave = 0;
	private static CustomProgressDialog progressDialog = null;
	int num = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keypoint_history);
		c = Calendar.getInstance();
		request = new Request(handler);
		myApplication = new MyApplication(this);
		tools = new Tools();
		userid = myApplication.userid;
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		data_history_up = (Button) findViewById(R.id.data_history_up);
		data_history_del = (Button) findViewById(R.id.data_history_del);
		data_history_check = (Button) findViewById(R.id.data_history_check);
		data_history_up.setOnClickListener(this);
		data_history_del.setOnClickListener(this);
		data_history_check.setOnClickListener(this);
		
		
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});
		main.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Tools.backMain(KeypointHistory.this);
			}
		});
		tools.startProgressDialog(KeypointHistory.this);
		initList();
	}
	
	public void initList() {
		data_history_check.setText("全选");
		keypointHistoryDataList = new ArrayList<KeypointHistoryBean>();
		operationDB = new OperationDB(getApplicationContext());
		cursor = operationDB.DBselect(Type.KEYPOINTACQUISITION);

		listview = (ListView) findViewById(R.id.data_history_list);
		Map<String, Object> map;
		tools.stopProgressDialog(KeypointHistory.this);
		for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
//		while (cursor.moveToNext()) {
			/*
			 * 为每一个listItem详细数据赋值
			 */
			keypointHistoryData = new KeypointHistoryBean();
			keypointHistoryData.setGuid(cursor.getString(1));
			keypointHistoryData.setUserid(cursor.getString(2));
			keypointHistoryData.setName(cursor.getString(3));
			keypointHistoryData.setLon(cursor.getString(4));
			keypointHistoryData.setLat(cursor.getString(5));
			keypointHistoryData.setDepartment(cursor.getString(7));
			keypointHistoryData.setLineid(cursor.getString(8));
			keypointHistoryData.setLine(cursor.getString(9));
			keypointHistoryData.setMarkerid(cursor.getString(10));
			keypointHistoryData.setMarkername(cursor.getString(11));
			keypointHistoryData.setMarkerstation(cursor.getString(12));
			keypointHistoryData.setLocationdes(cursor.getString(13));
			keypointHistoryData.setBuffer(cursor.getString(15));
			keypointHistoryData.setStart(cursor.getString(16));
			keypointHistoryData.setEnd(cursor.getString(17));
			keypointHistoryData.setDescription(cursor.getString(18));
			keypointHistoryData.setIsupload(cursor.getInt(19));
			keypointHistoryData.setOffset(cursor.getString(20));
			keypointHistoryDataList.add(keypointHistoryData);
			/*
			 * 为每一个listItem的标题数据赋值
<<<<<<< .mine
			 */
//			map = new HashMap<String, Object>();
//			 map.put("guid", cursor.getString(1));
//			map.put("name", "关键点名称：" + cursor.getString(3));
//			map.put("location", "位置：" + cursor.getString(9) + cursor.getString(13));
//			map.put("validity", "有效期日期：" + cursor.getString(16) + "至" + cursor.getString(17));
//			int flag = cursor.getInt(13);
//			map.put("flag", flag);
//			list.add(map);
		}
		cursor.close();operationDB.db.close();
		keypointHistoryAdapter = new KeypointHistoryAdapter(getApplicationContext(), keypointHistoryDataList);
		listview.setAdapter(keypointHistoryAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				 Intent intent = new Intent();
				 intent.setClass(getApplicationContext(),
				 KeypointHistoryDetail.class);
				 intent.putExtra("values", keypointHistoryDataList.get(arg2));
				 startActivity(intent);
				 overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
	}
	int upNum = 0;//批量上报成功的数量
	int upFailureNum = 0;//批量上报失败的数量
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			stopProgressDialog();
			switch (msg.arg1) {
			case 58:
				if (msg.getData().getInt("flag") == 1) {
					String str1 = msg.getData().getString("result");
					if (str1.equals("OK|TRUE")) {
						ContentValues values = new ContentValues();
						values.put("guid", msg.getData().getString("guid"));
						values.put("isupload", 1);
						operationDB.DBupdate(values, Type.KEYPOINTACQUISITION);
//						Toast.makeText(getApplicationContext(), "上报成功", 1000).show();
						upNum++;
//						keypointHistoryAdapter.upList.get(num).setIsupload(1);
						for(int i=0;i<keypointHistoryAdapter.upList.size();i++){
							if(keypointHistoryAdapter.upList.get(i).getGuid().equals(msg.getData().getString("guid"))){
								keypointHistoryAdapter.upList.get(i).setIsupload(1);
							}
						}
					} else {
						upFailureNum++;
						Toast.makeText(getApplicationContext(), upFailureNum+"失败，请稍后再次选择上报", 1000).show();
					}
					if(upNum==noHave){
						keypointHistoryAdapter.upList.clear();
						initList();
						keypointHistoryAdapter.notifyDataSetChanged();
						Toast.makeText(getApplicationContext(), upNum+"条数据上报成功", 1000).show();
					}
					 
				} else {	
					keypointHistoryAdapter.upList.clear();
					initList();
					keypointHistoryAdapter.notifyDataSetChanged();
					Toast.makeText(KeypointHistory.this, "网络连接错误"+upNum+"条数据上报成功", 1000).show();
				}
				break;
			}
		}
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		int tag = (Integer) v.getTag();
//		switch (tag) {
//		}
		switch(v.getId()){
		case R.id.data_history_up:
			upFailureNum=0;
			upNum=0;
			noHave=0;
			if(keypointHistoryAdapter.upList.size()==0){
				Toast.makeText(this, "请先选择要操作的数据", 1000).show();
			}else{
				if (Tools.isNetworkAvailable(KeypointHistory.this, true)) {
					
					for(int i=0;i<keypointHistoryAdapter.upList.size();i++){
					
						KeypointHistoryBean bean = new KeypointHistoryBean();
						bean = keypointHistoryAdapter.upList.get(i);
						if(keypointHistoryAdapter.upList.get(i).getIsupload() == 0){
							num = i;
							startProgressDialog("正在上报，请稍后。。。");
							request.KeypointRequest(myApplication.userid, myApplication.imei,
									myApplication.depterid, bean.getName(), bean.getLocationdes(),
									bean.getBuffer(), bean.getStart(), bean.getEnd(),
									bean.getLon(), bean.getLat(), bean.getDescription(),
									bean.getLineid(), bean.getMileage(), bean.getGuid(),"");
							noHave++;
						}
						
					}	
					if(noHave==0){
						Toast.makeText(this, "没有未上报的数据", 1000).show();
					}
					
				}
				
			}
			
			break;
		case R.id.data_history_del:
			
			if(keypointHistoryAdapter.upList.size()==0){
				Toast.makeText(this, "请先选择要操作的数据", 1000).show();
			}else{
				AlertDialog.Builder builder = new Builder(KeypointHistory.this);
				builder.setTitle("提示");
				builder.setMessage("请确定是否删除所选择的"+keypointHistoryAdapter.upList.size()+"条数据！");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						startProgressDialog("正在删除，请稍后。。。");
						for(int i=0;i<keypointHistoryAdapter.upList.size();i++){
							ContentValues values = new ContentValues();
							values.put("guid", keypointHistoryAdapter.upList.get(i).getGuid());
							operationDB.DBdelete(values, Type.KEYPOINTACQUISITION);
						}
						keypointHistoryAdapter.upList.clear();
						stopProgressDialog();
						Toast.makeText(KeypointHistory.this, "删除成功", 1000).show();
						initList();
						keypointHistoryAdapter.notifyDataSetChanged();
					}
					
				});
				builder.setNegativeButton("取消", null);
				builder.show();
			}
			break;
		case R.id.data_history_check:
			if(data_history_check.getText().toString().equals("全选")){
				data_history_check.setText("全不选");
				keypointHistoryAdapter.isAll = true;
				keypointHistoryAdapter.isCheckAll = 1;
				keypointHistoryAdapter.notifyDataSetChanged();
			}else if(data_history_check.getText().toString().equals("全不选")){
				data_history_check.setText("全选");
				keypointHistoryAdapter.isAll = false;
				keypointHistoryAdapter.isCheckAll = 2;
				keypointHistoryAdapter.notifyDataSetChanged();
			}
			break;
			
		}
	}

	Runnable resumelist = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			initList();
			// stopProgressDialog();
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// startProgressDialog();
		super.onResume();
		data_history_check.setText("全选");
		initList();
		keypointHistoryAdapter.upList.clear();
		keypointHistoryAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
		return super.onKeyDown(keyCode, event);

	}

	public static final String[] FIELD_ARR = { "userid", "department", "name", "lineid", "line",
			"markerid", "markername", "markerstation", "buffer", "start", "end", "lon", "lat",
			"description", "isupload" };// 要写入exceld的字段
	// 要写入exceld的字段,表头
	public static final String[] FIELD_ARR1 = { "序号", "用户ID", "归属部门ID", "关键点名称", "管线ID", "管线",
			"桩ID", "桩名", "桩Station", "缓冲范围", "有效期起始日期", "有效期终止日期", "经度", "纬度", "描述", "是否上报" };
	public static final int[] COLWIDTH_ARR = { 5, 15, 30 };// 单元格宽度

	public static void makeDir(File dir) {
		if (!dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdir();
	}

	File file;

	public void exportData() {
		file = new File(getSDPath() + "/Keypoint");
		makeDir(file);
		JXLUtil.initExcel(file.toString() + "/Keypoint.xls", FIELD_ARR1, COLWIDTH_ARR);

		//
		// List<KeypointHistoryBean> list = new
		// ArrayList<KeypointHistoryBean>();
		// for(int i=0;i<25;i++){
		// KeypointHistoryBean bean = new KeypointHistoryBean();
		// bean.setGuid(i+"");
		// bean.setIsupload(0);
		// bean.setLine("北京管线");
		// bean.setLineid("北京管线"+i);
		// bean.setPile("KK"+i);
		// bean.setRemarks("北京KK"+i);
		// bean.setTemperature("星球");
		// bean.setTesttime("2014-1-3");
		// bean.setUserid("小明");
		// bean.setValue("222"+i);
		// bean.setVoltage("1111"+i);
		// bean.setWeather("晴天");
		// bean.setYear("2014");
		// list.add(bean);
		// }
		JXLUtil.writeObjListToExcel(keypointHistoryDataList,
				getSDPath() + "/Keypoint/Keypoint.xls", FIELD_ARR, KeypointHistory.this);
	}

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		String dir = sdDir.toString();
		return dir;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, "导出xls").setIcon(R.drawable.exit);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 1:
			exportData();
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	private void startProgressDialog(String message) {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this).setMessage(message);
		}
		progressDialog.show();
	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}
