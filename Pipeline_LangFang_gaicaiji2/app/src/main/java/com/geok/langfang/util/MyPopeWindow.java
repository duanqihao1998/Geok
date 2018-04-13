package com.geok.langfang.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class MyPopeWindow {
	private Context mContext;
	private int mResource;
	private View view;
	private PopupWindow popupWindow;

	public MyPopeWindow(Context context, int Resource) {
		mContext = context;
		mResource = Resource;
		init();
	}

	void init() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		view = inflater.inflate(mResource, null);
		if (popupWindow == null) {
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}

		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		popupWindow.update();
		popupWindow.setTouchable(true);

	}

	public View getView() {
		return view;
	}

	public PopupWindow getPopupWindow() {
		return popupWindow;
	}

	public void DisMissPop() {
		popupWindow.dismiss();
	}

}
