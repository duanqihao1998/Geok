package com.geok.langfang.pipeline.problem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.geok.langfang.pipeline.R;

import java.util.ArrayList;
import java.util.List;

public class GrallyImage extends Activity implements OnClickListener {
	Gallery layout;
	View re;
	Button back, delete;
	int image_position = 0;
	GrallyImageView grallyImageView;
	ArrayList<String> list, comment;
	GestureDetector mgestureDetector;
	TextView text, text_conment;
	MyImageAdapter imageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grallyimage);

		grallyImageView = new GrallyImageView();
		layout = (Gallery) findViewById(R.id.grallyimage);
		text = (TextView) findViewById(R.id.gallery_text);
		text_conment = (TextView) findViewById(R.id.gallery_comment);

		back = (Button) findViewById(R.id.gallery_back);
		delete = (Button) findViewById(R.id.gallery_delete);
		back.setOnClickListener(this);
		delete.setOnClickListener(this);

		/*
		 * 获得imageList的数据列表
		 */
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			list = (ArrayList<String>) bundle.get("image");
			comment = (ArrayList<String>) bundle.get("comment");
			imageAdapter = new MyImageAdapter(list, getApplicationContext());
			layout.setAdapter(imageAdapter);
			layout.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

				image_position = arg2;
				text.setText(arg2 + 1 + "/" + list.size());
				text_conment.setText(comment.get(arg2));

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

				}
			});
		}
	}

	public Bitmap getBitmap(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回 bm 为空
		options.inJustDecodeBounds = false; // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = (int) (options.outHeight / (float) 320);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be; // 重新读入图片，注意此时已经把 options.inJustDecodeBounds
									// 设回 false 了
		bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}

	class MyImageAdapter extends BaseAdapter {
		List<String> list;
		Context context;

		public MyImageAdapter(List<String> list, Context context) {
			this.list = list;
			this.context = context;
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
			// TODO Auto-generated method stub
			ImageView imageView = new ImageView(context);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			String str = list.get(position);
			String com = comment.get(position);
			Bitmap bm = null;
			bm = getBitmap(str);
			imageView.setImageBitmap(bm);

			text.setText(position + 1 + "/" + list.size());
			text_conment.setText(comment.get(position));

			return imageView;
		}
	}

	public void setResult() {
		Intent intent = new Intent();
		intent.putStringArrayListExtra("image", list);
		intent.putStringArrayListExtra("comment", comment);
		setResult(Activity.RESULT_OK, intent);
		finish();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.gallery_back:
			setResult();
			break;

		case R.id.gallery_delete:

			list.remove(image_position);
			comment.remove(image_position);
			imageAdapter.notifyDataSetChanged();
			layout.setAdapter(imageAdapter);
			layout.invalidate();
			if (list.size() <= 0) {
				setResult();
			}

			break;
		}

	}

}
