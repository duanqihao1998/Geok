package com.geok.langfang.pipeline.dataacquisition;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geok.langfang.pipeline.R;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author wuchangming 防腐层检漏数据适配器
 * 
 */
public class CorrosiveHistoryAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, Object>> list;

	public CorrosiveHistoryAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		String check_date = (String) list.get(arg0).get("check_date");
		String[] check_time = check_date.split(" ");
		if(check_time.length>1){
			check_date = check_time[0];
		}
		String line_pile = (String) list.get(arg0).get("line_pile");
		String repair_type = (String) list.get(arg0).get("repair_type");
		int flag = (Integer) list.get(arg0).get("flag");
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.data_history_list, null);
		// 获取文本框
		TextView time_text = (TextView) view.findViewById(R.id.data_history_list_1);
		time_text.setText(check_date);
		TextView pile_text = (TextView) view.findViewById(R.id.data_history_list_2);
		pile_text.setText(line_pile);
		TextView weather_text = (TextView) view.findViewById(R.id.data_history_list_3);
		weather_text.setText(repair_type);
		TextView upload_text = (TextView) view.findViewById(R.id.data_history_list_4);

		// 若为1，则显示已上报，若为其他，则显示未上报
		if (flag == 1) {
			upload_text.setTextColor(Color.BLACK);
			upload_text.setText("已上报");
		} else {
			upload_text.setTextColor(Color.RED);
			upload_text.setText("未上报");
		}

		return view;
	}

}
