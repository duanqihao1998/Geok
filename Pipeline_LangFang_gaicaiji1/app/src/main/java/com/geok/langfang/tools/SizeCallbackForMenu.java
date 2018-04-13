package com.geok.langfang.tools;

import com.geok.langfang.tools.MyHorizontalScrollView.SizeCallback;

import android.widget.ImageButton;

public class SizeCallbackForMenu implements SizeCallback {
	private ImageButton leftButton;
	private int leftButtonWidth;

	public SizeCallbackForMenu(ImageButton leftButton) {
		super();
		this.leftButton = leftButton;

	}

	@Override
	public void onGlobalLayout() {
		leftButtonWidth = leftButton.getMeasuredWidth() + 20;

	}

	@Override
	public void getViewSize(int idx, int w, int h, int[] dims) {
		dims[0] = w;
		dims[1] = h;
		if (idx != 1) {
			// 当视图不是中间的视图
			dims[0] = w - leftButtonWidth;
		}
	}
}
