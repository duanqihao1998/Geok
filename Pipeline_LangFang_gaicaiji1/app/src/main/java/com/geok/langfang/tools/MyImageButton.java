package com.geok.langfang.tools;

import com.geok.langfang.pipeline.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyImageButton extends LinearLayout {

	Paint paint;

	public MyImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyButton);
		Drawable image = typedArray.getDrawable(R.styleable.MyButton_src);
		String string = (String) typedArray.getText(R.styleable.MyButton_text);
		int textSize = (int) typedArray.getDimension(R.styleable.MyButton_textSize, 15);

		TextView text = new TextView(context);
		ImageView imageview = new ImageView(context);
		text.setText(string);
		imageview.setImageDrawable(image);

		LayoutParams imageparams = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		LayoutParams textparams = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		imageparams.gravity = Gravity.CENTER_VERTICAL;
		imageparams.leftMargin = 10;
		imageparams.weight = 2;
		textparams.gravity = Gravity.CENTER_VERTICAL;
		textparams.leftMargin = 5;
		textparams.rightMargin = 10;
		textparams.weight = 5;
		text.setTextSize(textSize);
		text.setTextColor(Color.WHITE);
		setClickable(true); // 可点击
		setFocusable(true); // 可聚焦
		// setPressed(true);
		setOrientation(LinearLayout.HORIZONTAL); // 垂直布局
		this.addView(imageview, imageparams);
		this.addView(text, textparams);

	}

}
