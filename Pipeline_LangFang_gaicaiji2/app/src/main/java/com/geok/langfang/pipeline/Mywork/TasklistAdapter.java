package com.geok.langfang.pipeline.Mywork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.geok.langfang.jsonbean.Freqinfo;
import com.geok.langfang.pipeline.ApplicationApp;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.tools.MyLocation;

import java.util.List;

public class TasklistAdapter extends BaseAdapter {

	private Context context;// 哪个activity
	private int Resource;// xml文件
	List<Freqinfo> data;// 数据源
	ApplicationApp app;// 全局变量类

	MyLocation location1;
	LocationClient mLocationClient;

	public TasklistAdapter(Context context, List<Freqinfo> data, int Resource) {
		this.context = context;
		this.data = data;
		this.Resource = Resource;
		app = (ApplicationApp) context.getApplicationContext();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);
		// xml文件转换成view
		convertView = inflater.inflate(Resource, null);

		// 关键点名字
		TextView text = (TextView) convertView.findViewById(R.id.mywork_listitem_text);
		text.setText(data.get(position).getPOINTNAME());
		TextView textButton = (TextView) convertView.findViewById(R.id.mywork_listitem_button);
		textButton.setBackgroundDrawable(context.getResources().getDrawable(
				R.drawable.circle_cornor_line_gray));
		return convertView;

	}

}
