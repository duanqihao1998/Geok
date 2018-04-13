package com.geok.langfang.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geok.langfang.pipeline.R;

public class PanelListAdapter extends BaseAdapter {

	private Context context;
	private int[] list;
	private int resource;
	private String[] listTitle;

	public PanelListAdapter(Context context, int[] list, int resource) {
		this.context = context;
		this.list = list;
		this.resource = resource;
	}

	public PanelListAdapter(Context context, int[] list, String[] listTitle, int resource) {
		this.context = context;
		this.list = list;
		this.listTitle = listTitle;
		this.resource = resource;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LayoutInflater factory = LayoutInflater.from(context);
		View v = factory.inflate(R.layout.panel_item, null);// 绑定自定义的layout
		ImageView iv = (ImageView) v.findViewById(R.id.panel_icon);
		TextView tv = (TextView) v.findViewById(R.id.panel_text);
		iv.setImageResource(list[arg0]);
		tv.setText(listTitle[arg0]);
		return v;
		// ImageView image;
		// if(arg1!=null)
		// {
		// image=(ImageView) arg1;
		// }else
		// {
		// image=new ImageView(context);
		// image.setImageResource(list[arg0]);
		// }
		// return image;
	}

}
