package com.geok.langfang.tools;

import java.util.ArrayList;
import java.util.List;

import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.CpgroundbedBean;
import com.geok.langfang.jsonbean.DomainBean;
import com.geok.langfang.jsonbean.DomainBeanChild;
import com.geok.langfang.jsonbean.PileSyncBean;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.tools.TypeQuest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * @author Administrator 弹出数据框工具类
 * 
 */
public class DialogActivity extends Activity {

	@SuppressWarnings("rawtypes")
	private ArrayAdapter adapter;
	List<DomainBean> list;
	List<PileSyncBean> list1;
	List<CpgroundbedBean> list2;
	List<String> listpile;//显示的列表集合
	List<String> listpileId, listmarkerstation, listpilestation;
	List<DomainBeanChild> listtype, listtype1, listtype2;
	private ListView listview;
	String pileflag;
	private int key;
	CalculationYear calculationYear;
	
	EditText search_pile_edit;
	Button search_pile_btn;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		key = getIntent().getIntExtra("flag", 0);
//		TypeQuest.PROBLEM_PILE
//		TypeQuest.PROBLEM_HISTORY_PILE
//		TypeQuest.PROBLEM_HISTORY_PILE_1
//		TypeQuest.PROTECT_PILE
//		TypeQuest.NATURAL_PILE
//		TypeQuest.CORROSIVE_PILE
//		TypeQuest.CORROSIVE_ENDPILE
//		TypeQuest.KEYPOINT_PILE
		switch(key){
		case TypeQuest.PROBLEM_PILE:
		case TypeQuest.PROBLEM_HISTORY_PILE:
		case TypeQuest.PROBLEM_HISTORY_PILE_1:
		case TypeQuest.PROTECT_PILE:
		case TypeQuest.PROTECT_ENDPILE:
		case TypeQuest.NATURAL_PILE:
		case TypeQuest.NATURAL_ENDPILE:
		case TypeQuest.CORROSIVE_PILE:
		case TypeQuest.CORROSIVE_ENDPILE:
		case TypeQuest.KEYPOINT_PILE:
			setContentView(R.layout.dialogactivity_listview_search);
			search_pile_edit = (EditText)findViewById(R.id.search_pile_edit);
			search_pile_btn = (Button)findViewById(R.id.search_pile_btn);
			search_pile_btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					pile(search_pile_edit.getText().toString().trim());
				}
			});
			break;
		default:
			setContentView(R.layout.dialogactivity_listview);
		}
		Tools.startProgressDialog(DialogActivity.this, "加载中...");
		listview = (ListView) findViewById(R.id.dialog_listview);
		listpileId = new ArrayList<String>();
		listmarkerstation = new ArrayList<String>();
		listpilestation = new ArrayList<String>();
		list = new ArrayList<DomainBean>();
		list1 = new ArrayList<PileSyncBean>();
		list2 = new ArrayList<CpgroundbedBean>();
		listtype = new ArrayList<DomainBeanChild>();
		listtype1 = new ArrayList<DomainBeanChild>();
		listtype2 = new ArrayList<DomainBeanChild>();

	

		

		calculationYear = new CalculationYear();
		
		// System.out.println(key");
		switch (key) {

		case TypeQuest.PROBLEM_TYPE:
			domainType();
			List<String> listitem = new ArrayList<String>();
			for (int i = 0; i < listtype.size(); i++) {
				listitem.add(listtype.get(i).getCODENAME());
			}
			adapter = new ArrayAdapter(DialogActivity.this, android.R.layout.simple_list_item_1,
					listitem);
			break;
		case TypeQuest.PROBLEM_HISTORY_TYPE:
			domainType();
			List<String> listitemhistorytype = new ArrayList<String>();
			for (int i = 0; i < listtype.size(); i++) {
				listitemhistorytype.add(listtype.get(i).getCODENAME());
			}
			adapter = new ArrayAdapter(DialogActivity.this, android.R.layout.simple_list_item_1,
					listitemhistorytype);
			break;

		case TypeQuest.PROBLEM_PILE:
			pile();
			break;
		case TypeQuest.PROBLEM_HISTORY_PILE:
			pile();
			break;
		case TypeQuest.PROBLEM_HISTORY_PILE_1:
			pile();
			break;

		case TypeQuest.SETTINGDETIAL_TIME_INTERVAL:
			adapter = ArrayAdapter.createFromResource(DialogActivity.this, R.array.time_interval,
					android.R.layout.simple_list_item_1);
			break;
		case TypeQuest.PROTECT_YEAR:
			adapter = new ArrayAdapter(DialogActivity.this, android.R.layout.simple_list_item_1,
					calculationYear.getYear());
			break;
		case TypeQuest.PROTECT_MONTH:
			adapter = ArrayAdapter.createFromResource(DialogActivity.this, R.array.month,
					android.R.layout.simple_list_item_1);
			break;
		case TypeQuest.PROTECT_LINE:
			break;
		case TypeQuest.PROTECT_PILE:
			pile();
			break;
		case TypeQuest.PROTECT_ENDPILE:
			pile();
			break;

		case TypeQuest.NATURAL_YEAR:
			adapter = new ArrayAdapter(DialogActivity.this, android.R.layout.simple_list_item_1,
					calculationYear.getYear());
			break;
		case TypeQuest.NATURAL_MONTH:
			adapter = ArrayAdapter.createFromResource(DialogActivity.this, R.array.month,
					android.R.layout.simple_list_item_1);
			break;
		case TypeQuest.NATURAL_LINE:
			break;
		case TypeQuest.NATURAL_PILE:
			pile();
			break;
		case TypeQuest.NATURAL_ENDPILE:
			pile();
			break;

		case TypeQuest.GROUND_CPGROUNDBEDEVENTID:
			/*
			 * 地床编号列表
			 */
			String b = getSharedPreferences("sync", MODE_PRIVATE).getString("groundbed", "-1");
			list2 = Json.getCpgroundbedBean(b);
			cpgroundbed();
			break;
		case TypeQuest.GROUND_STARTYEAR:
			adapter = new ArrayAdapter(DialogActivity.this, android.R.layout.simple_list_item_1,
					calculationYear.getYear_search());
			break;
		case TypeQuest.GROUND_ENDYEAR:
			adapter = new ArrayAdapter(DialogActivity.this, android.R.layout.simple_list_item_1,
					calculationYear.getYear_search());
			break;
		case TypeQuest.GROUND_YEAR:
			adapter = new ArrayAdapter(DialogActivity.this, android.R.layout.simple_list_item_1,
					calculationYear.getYear());
			break;
		case TypeQuest.GROUND_HALFYEAR:
			adapter = ArrayAdapter.createFromResource(DialogActivity.this, R.array.halfyear,
					android.R.layout.simple_list_item_1);
			break;

		case TypeQuest.CORROSIVE_LINE:
			break;
		case TypeQuest.CORROSIVE_PILE:
			pile();
			break;
		case TypeQuest.CORROSIVE_ENDPILE:
			pile();
			break;
		case TypeQuest.CORROSIVE_DAMAGE_TYPE:
			domainType();
			List<String> listitem1 = new ArrayList<String>();
			for (int i = 0; i < listtype1.size(); i++) {
				listitem1.add(listtype1.get(i).getCODENAME());
			}
			adapter = new ArrayAdapter(DialogActivity.this, android.R.layout.simple_list_item_1,
					listitem1);
			break;
		case TypeQuest.CORROSIVE_REPAIR_TYPE:
			domainType();
			List<String> listitem2 = new ArrayList<String>();
			for (int i = 0; i < listtype2.size(); i++) {
				listitem2.add(listtype2.get(i).getCODENAME());
			}
			adapter = new ArrayAdapter(DialogActivity.this, android.R.layout.simple_list_item_1,
					listitem2);
			break;
		case TypeQuest.KEYPOINT_LINE:
			break;
		case TypeQuest.KEYPOINT_PILE:
			pile();
			break;
		}
		listview.setAdapter(adapter);
		if(markName!=null){
			listview.setSelection(index);
		}
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				switch (key) {
				case TypeQuest.PROTECT_YEAR:
					intent.putExtra("year", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.PROBLEM_TYPE:
					intent.putExtra("problem_type", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.PROBLEM_HISTORY_TYPE:
					intent.putExtra("problem_history_type", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.PROBLEM_PILE:
					intent.putExtra("problem_pile", adapter.getItem(arg2).toString());
					intent.putExtra("markId", listpileId.get(arg2));
					break;
				case TypeQuest.PROBLEM_HISTORY_PILE:
					intent.putExtra("problem_history_pile", adapter.getItem(arg2).toString());

					intent.putExtra("markId", listpilestation.get(arg2));
					break;
				case TypeQuest.PROBLEM_HISTORY_PILE_1:
					intent.putExtra("problem_history_pile_1", adapter.getItem(arg2).toString());

					intent.putExtra("markId", listpilestation.get(arg2));
					break;

				case TypeQuest.PROTECT_MONTH:
					intent.putExtra("month", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.PROTECT_LINE:
					intent.putExtra("line", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.PROTECT_PILE:
					intent.putExtra("pile", adapter.getItem(arg2).toString());
					intent.putExtra("po", arg2);
					intent.putExtra("markId", listpileId.get(arg2));
					intent.putExtra("markerstation", listmarkerstation.get(arg2));
					break;
				case TypeQuest.PROTECT_ENDPILE:
					intent.putExtra("pile", adapter.getItem(arg2).toString());
					intent.putExtra("po", arg2);
					intent.putExtra("markId", listpileId.get(arg2));
					intent.putExtra("markerstation", listmarkerstation.get(arg2));
					break;

				case TypeQuest.SETTINGDETIAL_TIME_INTERVAL:
					intent.putExtra("time_interval", adapter.getItem(arg2).toString());
					break;

				case TypeQuest.NATURAL_YEAR:
					intent.putExtra("year", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.NATURAL_MONTH:
					intent.putExtra("month", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.NATURAL_LINE:
					intent.putExtra("line", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.NATURAL_PILE:
					intent.putExtra("pile", adapter.getItem(arg2).toString());
					intent.putExtra("po", arg2);
					intent.putExtra("markId", listpileId.get(arg2));
					intent.putExtra("markerstation", listmarkerstation.get(arg2));
					System.out.println("markerstation");
					break;
				case TypeQuest.NATURAL_ENDPILE:
					intent.putExtra("pile", adapter.getItem(arg2).toString());
					intent.putExtra("po", arg2);
					intent.putExtra("markId", listpileId.get(arg2));
					intent.putExtra("markerstation", listmarkerstation.get(arg2));
					break;

				case TypeQuest.GROUND_CPGROUNDBEDEVENTID:
					intent.putExtra("cpgroundbedeventid", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.GROUND_STARTYEAR:
					intent.putExtra("year", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.GROUND_ENDYEAR:
					intent.putExtra("year", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.GROUND_YEAR:
					intent.putExtra("year", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.GROUND_HALFYEAR:
					intent.putExtra("halfyear", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.CORROSIVE_LINE:
					intent.putExtra("line", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.CORROSIVE_PILE:
					intent.putExtra("pile", adapter.getItem(arg2).toString());
					intent.putExtra("po", arg2);
					intent.putExtra("markId", listpileId.get(arg2));
					intent.putExtra("markerstation", listmarkerstation.get(arg2));
					break;
				case TypeQuest.CORROSIVE_ENDPILE:
					intent.putExtra("pile", adapter.getItem(arg2).toString());
					intent.putExtra("po", arg2);
					intent.putExtra("markId", listpileId.get(arg2));
					intent.putExtra("markerstation", listmarkerstation.get(arg2));
					break;
				case TypeQuest.CORROSIVE_DAMAGE_TYPE:
					intent.putExtra("damage_type", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.CORROSIVE_REPAIR_TYPE:
					intent.putExtra("repair_type", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.KEYPOINT_LINE:
					intent.putExtra("line", adapter.getItem(arg2).toString());
					break;
				case TypeQuest.KEYPOINT_PILE:
					intent.putExtra("pile", adapter.getItem(arg2).toString());
					intent.putExtra("po", arg2);
					intent.putExtra("markId", listpileId.get(arg2));
					intent.putExtra("markerstation", listmarkerstation.get(arg2));
					break;
				}
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		Tools.stopProgressDialog(DialogActivity.this);
	}
	int index = 0;
	String markName;
	String lineid;
	public void pile() {
		String str = getSharedPreferences("sync", MODE_PRIVATE).getString("pile", "-1");
		list1 = Json.getPileSyncList(str);
		lineid = getIntent().getStringExtra("lineid");
		if(getIntent().getStringExtra("markName")!=null){
			markName = getIntent().getStringExtra("markName");
		}
		listpile = new ArrayList<String>();
		listpileId = new ArrayList<String>();
		listmarkerstation = new ArrayList<String>();
		listpilestation = new ArrayList<String>();
		for (int i = 0; i < list1.size(); i++) {
			PileSyncBean bean = list1.get(i);
			if (lineid.equals(bean.getLINELOOPEVENTID())) {
				// listpile.add
				for (int j = 0; j < bean.getChildBean().size(); j++) {
					listpile.add(bean.getChildBean().get(j).getMARKERNAME());
					listpileId.add(bean.getChildBean().get(j).getMARKEREVENTID());
					listmarkerstation.add(bean.getChildBean().get(j).getMARKERSTATION());
					listpilestation.add(bean.getChildBean().get(j).getMARKERSTATION());
					if(markName!=null){
						if(markName.equals(bean.getChildBean().get(j).getMARKERNAME())){
							index = j;
						}
					}
				}

			}

		}
		Pipe.listpile = listpile;
		if (listpile.size() != 0) {
			adapter = new ArrayAdapter(DialogActivity.this, android.R.layout.simple_list_item_1,
					listpile);
		} else {
			Toast.makeText(getApplicationContext(), "无数据，请联系管理员", 1000).show();
			finish();
		}
	}

	public void pile(String pileName) {
		String str = getSharedPreferences("sync", MODE_PRIVATE).getString("pile", "-1");
		list1 = Json.getPileSyncList(str);
		lineid = getIntent().getStringExtra("lineid");
		listpile = new ArrayList<String>();
		listpileId = new ArrayList<String>();
		listmarkerstation = new ArrayList<String>();
		listpilestation = new ArrayList<String>();
		if(pileName.equals("")){
			for (int i = 0; i < list1.size(); i++) {
				PileSyncBean bean = list1.get(i);
				if (lineid.equals(bean.getLINELOOPEVENTID())) {
					// listpile.add
					for (int j = 0; j < bean.getChildBean().size(); j++) {
							listpile.add(bean.getChildBean().get(j).getMARKERNAME());
							listpileId.add(bean.getChildBean().get(j).getMARKEREVENTID());
							listmarkerstation.add(bean.getChildBean().get(j).getMARKERSTATION());

					}

				}

			}
		}else{
			for (int i = 0; i < list1.size(); i++) {
				PileSyncBean bean = list1.get(i);
				if (lineid.equals(bean.getLINELOOPEVENTID())) {
					// listpile.add
					for (int j = 0; j < bean.getChildBean().size(); j++) {
						if(bean.getChildBean().get(j).getMARKERNAME().toLowerCase().contains(pileName.toLowerCase())){
							listpile.add(bean.getChildBean().get(j).getMARKERNAME());
							listpileId.add(bean.getChildBean().get(j).getMARKEREVENTID());
							listmarkerstation.add(bean.getChildBean().get(j).getMARKERSTATION());
							listpilestation.add(bean.getChildBean().get(j).getMARKERSTATION());
						}
						
					}

				}

			}
		}
		
		
		Pipe.listpile = listpile;
		if (listpile.size() == 0) {
			Toast.makeText(getApplicationContext(), "没有搜到这个桩", 1000).show();
			
//			adapter.notifyDataSetChanged();
//		} else {
//			
		}
		adapter = new ArrayAdapter(DialogActivity.this, android.R.layout.simple_list_item_1,
				listpile);
		listview.setAdapter(adapter);
	}
	public void cpgroundbed() {
		List<String> listcpgroundbed = new ArrayList<String>();
		for (int i = 0; i < list2.size(); i++) {
			CpgroundbedBean bean = list2.get(i);
			listcpgroundbed.add(bean.getCPGROUNDBEDEVENTID());
		}
		Cpgroundbed.listcpgroundbed = listcpgroundbed;
		if (listcpgroundbed.size() != 0) {
			adapter = new ArrayAdapter(DialogActivity.this, android.R.layout.simple_list_item_1,
					listcpgroundbed);
		} else {
			Toast.makeText(getApplicationContext(), "无数据，请联系管理员", 1000).show();
			finish();
		}
	}
	public void domainType(){
		/*
		 * 域字典列表
		 */
		String a = getSharedPreferences("sync", MODE_PRIVATE).getString("domain", "-1");
		list = Json.getDomainList(getSharedPreferences("sync", MODE_PRIVATE).getString("domain",
				"-1"));
		

		/*
		 * 问题类型列表
		 */
		for (int i = 0; i < list.size(); i++) {
			if ("mattertype".equals(list.get(i).getDOMAINNAME())) {
				listtype = list.get(i).getChidList();
			}
		}
		if (listtype.isEmpty()) {
			Toast.makeText(getApplicationContext(), "无数据，请联系管理员", 1000).show();
			finish();
		}

		/*
		 * 破损类型列表
		 */
		for (int i = 0; i < list.size(); i++) {
			if ("leakType".equals(list.get(i).getDOMAINNAME())) {
				listtype1 = list.get(i).getChidList();
			}
		}
		if (listtype1.isEmpty()) {
			Toast.makeText(getApplicationContext(), "无数据，请联系管理员", 1000).show();
			finish();
		}
		/*
		 * 修复类型列表
		 */
		for (int i = 0; i < list.size(); i++) {
			if ("repairType".equals(list.get(i).getDOMAINNAME())) {
				listtype2 = list.get(i).getChidList();
			}
		}
		if (listtype2.isEmpty()) {
			Toast.makeText(getApplicationContext(), "无数据，请联系管理员", 1000).show();
			finish();
		}
	}
}
