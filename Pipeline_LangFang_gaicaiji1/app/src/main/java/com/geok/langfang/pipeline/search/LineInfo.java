package com.geok.langfang.pipeline.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.InformationQueryBean;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.tools.Tools;
import com.geok.langfang.tools.TreeView;
import com.geok.langfang.tools.TypeQuest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LineInfo extends Activity implements OnClickListener {
	RelativeLayout select_line;
	TextView select_line_text;
	String lineId;
	Button back, main;
	ListView listview;
	List<InformationQueryBean> data;
	CurrentLineInfo currentLineInfo = new CurrentLineInfo();
	Tools tools = new Tools();
	Request request;
	MyApplication myApplication;
	public Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.arg1 == 41) {
				String str = msg.getData().getString("result");
				int flag = msg.getData().getInt("flag");
				if (flag == 1) {
					if (!str.equals("-1")) {

						data = Json.getInformationQueryList(str);
						if (data.size() > 0) {
							listview = (ListView) findViewById(R.id.search_lineinfo);
							listview.setVisibility(0);
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
				tools.stopProgressDialog(LineInfo.this);
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lineinfo);
		myApplication = new MyApplication(this);
		select_line = (RelativeLayout) findViewById(R.id.select_line);
		select_line_text = (TextView) findViewById(R.id.select_line_text);
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
		back.setOnClickListener(this);
		main.setOnClickListener(this);
		select_line.setOnClickListener(this);
		request = new Request(myHandler);
		Bundle bundle = getIntent().getExtras();
		String lineinfo = "", linename = "";
		if(bundle!=null){
		lineinfo = bundle.getString("lineinfo");
		linename = bundle.getString("linename");
		}
		if(!lineinfo.equals("")){
			data = Json.getInformationQueryList(lineinfo);
			if (data.size() > 0) {
				listview = (ListView) findViewById(R.id.search_lineinfo);
				listview.setVisibility(0);
				select_line_text.setText(linename);
				initList();
			} else {
				Toast.makeText(getApplicationContext(), "查询错误，请重试", 1000).show();
			}
		}else{
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TypeQuest.NATURAL_LINE:
			if (resultCode == Activity.RESULT_OK) {
				select_line_text.setText(data.getStringExtra("line"));
				lineId = data.getStringExtra("lineId");
				if (Tools.isNetworkAvailable(this, true)) {
					request.LineInfoSearchRequest(myApplication.userid, lineId);
					// request.LineInfoSearchRequest("8f183082-adb1-11e2-8702-60672090d36c",
					// "1e4d4bd4-1002-4eeb-8fa8-766561f7d092");
					tools.startProgressDialog(this);
				}
			}
			break;
		}
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
			Tools.backMain(this);
			break;

		case R.id.select_line:
			Intent line = new Intent();
			line.setClass(LineInfo.this, TreeView.class);
			line.putExtra("flag", TypeQuest.NATURAL_LINE);
			startActivityForResult(line, TypeQuest.NATURAL_LINE);
			break;
		}
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
		map.put("tittle", "长度(mm)");
		map.put("text", data.get(0).getLENGTH());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "管线类型");
		map.put("text", data.get(0).getLINETYPE());
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("tittle", "管径(mm)");
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
		map.put("tittle", "固定资产原值(万元)");
		map.put("text", data.get(0).getORIGINALFIXEDASSETS());
		list.add(map);
		SimpleAdapter adapter = new SimpleAdapter(LineInfo.this, list, R.layout.listitem,
				new String[] { "tittle", "text" }, new int[] { R.id.listitem_text1,
						R.id.listitem_text2 });
		listview.setAdapter(adapter);
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
