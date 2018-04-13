package com.geok.langfang.pipeline.search;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.JobEvaluateBean;
import com.geok.langfang.pipeline.ApplicationApp;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.Tools;

import android.app.Activity;
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
 * 人员考评信息详情
 * 
 */
public class Assessment extends Activity implements OnClickListener {

	ListView listView;
	Request request;
	String userid,departid;
	MyApplication myApplication;
	// 定义考评信息数据列
	List<JobEvaluateBean> listBean = new ArrayList<JobEvaluateBean>();
	Tools tools = new Tools();

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// switch (msg.arg1) {
			// case 29:
			// String data = msg.getData().getString("result");
			// if (data.equals("-1")) {
			// Toast.makeText(getApplicationContext(), "暂没有考核信息！",
			// 1000).show();
			// Assessment.this.finish();
			// } else {
			// listBean = Json.getJobEvaluateList(data);
			// initList();
			// }
			// tools.stopProgressDialog(Assessment.this);
			// break;
			// }
			// }
			tools.stopProgressDialog(Assessment.this);
			if (msg.arg1 == 29) {
				String str = msg.getData().getString("result");
				int flag = msg.getData().getInt("flag");
				if (flag == 1) {
					if (!str.equals("-1")) {

						listBean = Json.getJobEvaluateList(str);
						initList();
					} else {
						Toast.makeText(getApplicationContext(), "暂无考核信息", 1000).show();
						Assessment.this.finish();
					}
				} else {
					Toast.makeText(getApplicationContext(), "考评信息查询失败，请重试！", 1000).show();
					Assessment.this.finish();
				}
			}
		}
	};

	ApplicationApp app;
	Button back, main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assessment);
		myApplication = new MyApplication(this);
		userid = myApplication.userid;
		departid = myApplication.depterid;
		app = (ApplicationApp) getApplicationContext();
		if (Tools.isNetworkAvailable(this, true)) {
			request = new Request(handler);
			// 请求查询
			request.JobEvaluateQuery(userid,departid);
			// request.JobEvaluateQuery("8f1857a2-adb1-11e2-8702-60672090d36c");
			// 开始进度条
			tools.startProgressDialog(Assessment.this);

		}
		// 获取界面返回和主页键
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		back.setOnClickListener(this);
		main.setOnClickListener(this);

	}

	private void initList() {
		Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		JobEvaluateBean bean = new JobEvaluateBean();

		for (int i = 0; i < listBean.size(); i++) {
			int m;
			m = Integer.parseInt(listBean.get(i).getMONTH());
			if (listBean.get(i).getYEAR().equals(String.valueOf(year)) && m == month) {
				bean = listBean.get(i);
			}
		}

		listView = (ListView) findViewById(R.id.search_assessment);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;

		map = new HashMap<String, Object>();
		map.put("tittle", "考评年份");
		map.put("text", bean.getYEAR());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "考评月份");
		map.put("text", bean.getMONTH());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "人工评分");
		map.put("text", bean.getARTIFICIALSCORE());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "系统得分");
		map.put("text", bean.getSYSSCORE());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "考评意见");
		map.put("text", bean.getOPINIONS());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "姓名");
		map.put("text", bean.getNAME());
		list.add(map);
		// 使用SimpleAdapter
		SimpleAdapter adapter = new SimpleAdapter(Assessment.this, list, R.layout.listitem,
				new String[] { "tittle", "text" }, new int[] { R.id.listitem_text1,
						R.id.listitem_text2 });
		// 根据适配器显示数据
		listView.setAdapter(adapter);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// int tag=(Integer)v.getTag();
		switch (v.getId()) {
		// 返回上一层
		case R.id.back:
			finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		// 返回主页
		case R.id.main:
			Tools.backMain(Assessment.this);
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
}
