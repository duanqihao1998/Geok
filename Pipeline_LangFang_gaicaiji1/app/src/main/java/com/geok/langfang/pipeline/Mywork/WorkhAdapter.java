package com.geok.langfang.pipeline.Mywork;

import java.util.List;

import com.geok.langfang.jsonbean.MyTaskQueryBean;
import com.geok.langfang.pipeline.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WorkhAdapter extends BaseAdapter {

	Context c;
	List<MyTaskQueryBean> list;

	public WorkhAdapter(Context c, List<MyTaskQueryBean> data) {
		this.c = c;
		this.list = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder view;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(c);
			convertView = inflater.inflate(R.layout.workhlist_item, null);
			view = new ViewHolder();
			view.num = (TextView) convertView.findViewById(R.id.num);
			view.work_day = (TextView) convertView.findViewById(R.id.work_date);
			view.work_info = (TextView) convertView.findViewById(R.id.work_info);
		} else {
			view = (ViewHolder) convertView.getTag();
		}
		view.num.setText(position + 1 + "");
		view.work_day.setText(list.get(position).getINSDATE());
		view.work_info.setText(list.get(position).getInsdateinfoList().size() + "条任务");
		return convertView;
	}

	class ViewHolder {
		TextView num;
		TextView work_day;
		TextView work_info;
	}

}
