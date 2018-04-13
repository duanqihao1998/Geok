package com.geok.langfang.pipeline.problem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.geok.langfang.pipeline.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EditGallery extends Activity implements OnClickListener {

	ImageView image;
	Bitmap bm;
	GrallyImageView grallyImageView;
	Button back, accept;
	EditText edit;
	/*
	 * 文件描述和图片路径
	 */
	String conment, imagepath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.editgallery);
		grallyImageView = new GrallyImageView();
		imagepath = getIntent().getExtras().getString("image");
		back = (Button) findViewById(R.id.edit_gallery_back);
		accept = (Button) findViewById(R.id.edit_gallery_accept);
		edit = (EditText) findViewById(R.id.edit_gallery);
		back.setOnClickListener(this);
		accept.setOnClickListener(this);

		image = (ImageView) findViewById(R.id.editgallery_image);

//		bm = grallyImageView.getBitmapFromFile(imagepath, 800, 600);
		bm = grallyImageView.getBitmapFromFile(imagepath, 480, 800);
		Date date = new Date();
		date.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
		String markStr = sdf.format(date);

		// 加水印，同时修改图片每个像素以2个字节存储， Bitmap.Config.ARGB_4444
		// bm = mark(bm, markStr, new Point(20, 20), new Color(), 100, 15,
		// true);
		bm = mark(bm, markStr, new Point(20, 20), new Color(), 100, 25, true);
		FileOutputStream b = null;
		try {
			b = new FileOutputStream(imagepath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bm.compress(Bitmap.CompressFormat.JPEG, 50, b);
		image.setImageBitmap(bm);

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
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
				matrix, true);

		return bitmap;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.edit_gallery_accept:
			intent.putExtra("image", imagepath);
			intent.putExtra(imagepath, edit.getText().toString());
			break;

		case R.id.edit_gallery_back:
			break;
		}

		setResult(Activity.RESULT_OK, intent);
		finish();

	}

	// 加水印，同时降低图片每个像素存储大小为2个字节，默认的可能是4个字节
	public static Bitmap mark(Bitmap src, String watermark, Point location, Color color, int alpha,
			int size, boolean underline) {
		int w = src.getWidth();
		int h = src.getHeight();

		// Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
		Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		// Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(result);
		canvas.drawBitmap(src, 0, 0, null);

		Paint paint = new Paint();
		paint.setAlpha(alpha);
		paint.setTextSize(size);
		paint.setAntiAlias(true);
		paint.setUnderlineText(underline);
		canvas.drawText(watermark, location.x, location.y, paint);

		return result;
	}
}
