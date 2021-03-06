package com.geok.langfang.pipeline.problem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geok.langfang.pipeline.R;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author sunshihai 自定义数据匹配器
 * 
 */
public class ProblemHistoryAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, Object>> list;

	public ProblemHistoryAdapter(Context context, List<Map<String, Object>> list) {
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

		Bitmap bm = (Bitmap) list.get(arg0).get("image");
		String time = (String) list.get(arg0).get("time");
		String location = (String) list.get(arg0).get("location");
		String type = (String) list.get(arg0).get("type");
		int flag = (Integer) list.get(arg0).get("flag");

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.problem_history_list, null);
		ImageView image = (ImageView) view.findViewById(R.id.problem_history_list_image);
		image.setImageBitmap(bm);
		TextView time_text = (TextView) view.findViewById(R.id.problem_history_list_time);
		time_text.setText(time);
		TextView location_text = (TextView) view.findViewById(R.id.problem_history_list_location);
		location_text.setText(location);
		TextView type_text = (TextView) view.findViewById(R.id.problem_history_list_person);
		type_text.setText(type);
		TextView upload_text = (TextView) view.findViewById(R.id.problem_history_list_flag);

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
