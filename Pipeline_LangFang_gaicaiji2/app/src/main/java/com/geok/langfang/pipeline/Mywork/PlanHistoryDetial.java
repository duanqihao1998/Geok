package com.geok.langfang.pipeline.Mywork;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.geok.langfang.pipeline.R;
import com.geok.langfang.tools.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanHistoryDetial extends Activity {

	PlanHistoryData plan;
	ListView listview;
	Button back, main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plan);
		plan = (PlanHistoryData) getIntent().getSerializableExtra("values");
		back = (Button) findViewById(R.id.back);
		main = (Button) findViewById(R.id.main);
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
				Tools.backMain(PlanHistoryDetial.this);
			}
		});
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

	private void initList() {
		listview = (ListView) findViewById(R.id.mywork_plan);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检执行单位:");
		map.put("text", plan.getDEPARTMENT());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检计划编号:");
		map.put("text", plan.getPLANNO());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检类型:");
		map.put("text", plan.getINSTYPE());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检人员类型:");
		map.put("text", plan.getINSPECTORTYPE());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检人员:");
		map.put("text", plan.getINSPECTOR());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检频次:");
		map.put("text", plan.getINSFREQ());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检工具:");
		map.put("text", plan.getINSVEHICLE());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "计划类型:");
		map.put("text", plan.getINSPROPER());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "制定依据:");
		map.put("text", plan.getDETERMINANT());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检开始日期:");
		map.put("text", plan.getINSBDATE());
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("tittle", "巡检结束日期:");
		map.put("text", plan.getINSEDATE());
		list.add(map);
		for (int i = 0; i < plan.getPlanLineInfo().size(); i++) {
			map = new HashMap<String, Object>();
			map.put("tittle", "巡检线路" + (i + 1) + ":");
			map.put("text", plan.getPlanLineInfo().get(i).getLINELOOP());
			list.add(map);
			map = new HashMap<String, Object>();
			map.put("tittle", "巡检范围" + (i + 1) + ":");
			map.put("text", plan.getPlanLineInfo().get(i).getRANGEFLAG());
			list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(PlanHistoryDetial.this, list, R.layout.listitem,
				new String[] { "tittle", "text" }, new int[] { R.id.listitem_text1,
						R.id.listitem_text2 });
		listview.setAdapter(adapter);
	}

}
