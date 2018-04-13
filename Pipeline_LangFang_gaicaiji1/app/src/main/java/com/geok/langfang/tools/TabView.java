package com.geok.langfang.tools;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TabView extends LinearLayout {
	private String text = "";

	public String getText() {
		return text;
	}

	public TabView(Context context, RelativeLayout imageView, TextView textView, TextView textTitle) {
		super(context);
		this.text = textTitle.getText().toString();
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);
		// textTitle.setGravity(Gravity.CENTER);
		// textTitle.setTextColor(Color.BLACK);
		textTitle.setTextSize(13);
		addView(imageView);
		// addView(textTitle);

	}

	public TabView(Context context, ImageView imageView) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);
		addView(imageView);
	}
}