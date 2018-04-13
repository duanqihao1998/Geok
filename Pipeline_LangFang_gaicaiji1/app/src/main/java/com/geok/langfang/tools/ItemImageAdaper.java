package com.geok.langfang.tools;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 类ImageAdapter,用于为GridView和Gallery提供图标
 * 
 * @author Hu
 * 
 */

public class ItemImageAdaper extends BaseAdapter {
	Context mContext;
	TabView[] tabView = null;
	int[] imagelist;
	String[] titlelist;
	int Resource;

	int[] upgrate;

	// construct
	public ItemImageAdaper(Context c, int[] imagelist, String[] titlelist, int Resource,
			int[] upgrate) {
		mContext = c;

		this.Resource = Resource;
		this.titlelist = titlelist;
		this.imagelist = imagelist;
		this.upgrate = upgrate;

	}

	@Override
	public int getCount() {
		return imagelist.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		// return tabView[position];

		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		tabView = Tools.initNavigator(mContext, imagelist, titlelist, Resource, upgrate);
		TabView tabiew;
		if (convertView == null) {
			tabiew = tabView[position];
		} else {
			tabiew = (TabView) convertView;
		}

		return tabiew;
	}

}