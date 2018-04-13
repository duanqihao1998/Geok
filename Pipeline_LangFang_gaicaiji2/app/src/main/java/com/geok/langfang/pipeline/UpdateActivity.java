package com.geok.langfang.pipeline;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.geok.langfang.tools.FileDownloadThread;
import com.geok.langfang.tools.MyAdapter;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends Activity implements OnItemClickListener {

	private EditText downloadUrl;
	private EditText downloadFileName;
	private Button downloadBt;
	private Button downloadCancelBt;
	private ProgressBar downloadProgressBar;
	private TextView progressMessage;
	private int downloadedSize = 0;
	private int fileSize = 0;
	String fileName;
	String dowloadDir;

	/*
	 * 对象声明 items：存放显示的名称 paths：存放文件路径 rootPath：起始目录
	 */
	private List<String> items = null;
	private List<String> paths = null;
	private String rootPath = "/";
	private TextView mPath;
	private ListView fileList = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);
		Intent i = getIntent();

		downloadUrl = (EditText) findViewById(R.id.downloadUrl);
		downloadUrl.setText(i.getStringExtra("url"));
		downloadFileName = (EditText) findViewById(R.id.downloadFileName);
		downloadFileName.setText("根目录/PipeLine/PipeLine.apk");
		progressMessage = (TextView) findViewById(R.id.progressMessage);
		downloadBt = (Button) findViewById(R.id.downloadBt);
		downloadCancelBt = (Button) findViewById(R.id.downloadCancelBt);
		downloadProgressBar = (ProgressBar) findViewById(R.id.downloadProgressBar);
		downloadProgressBar.setVisibility(View.VISIBLE);
		downloadProgressBar.setMax(100);
		downloadProgressBar.setProgress(0);

		downloadFileName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 选择下载存储的路径
				// chooseFile();
			}
		});
		downloadBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				download();
			}
		});
		downloadCancelBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void download() {
		// 获取SD卡目录
		dowloadDir = Environment.getExternalStorageDirectory() + "/PipeLine/";
		File file = new File(dowloadDir);
		// 创建下载目录
		if (!file.exists()) {
			file.mkdirs();
		}

		// 读取下载线程数，如果为空，则单线程下载
		int downloadTN = 1;
		// 如果下载文件名为空则获取Url尾为文件名
		int fileNameStart = downloadUrl.getText().toString().lastIndexOf("/");
		fileName = downloadUrl.getText().toString().substring(fileNameStart);
		// 开始下载前把下载按钮设置为不可用
		downloadBt.setClickable(false);
		// 进度条设为0
		downloadProgressBar.setProgress(0);
		// 启动文件下载线程
		new downloadTask(downloadUrl.getText().toString(), Integer.valueOf(downloadTN), dowloadDir
				+ fileName).start();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 当收到更新视图消息时，计算已完成下载百分比，同时更新进度条信息
			int progress = (Double.valueOf((downloadedSize * 1.0 / fileSize * 100))).intValue();
			if (progress == 100) {
				downloadBt.setClickable(true);
				progressMessage.setText("下载完成！");
				Intent i = new Intent();
				i.setAction(Intent.ACTION_VIEW);
				i.setDataAndType(Uri.fromFile(new File(dowloadDir + fileName)),
						"application/vnd.android.package-archive");
				startActivity(i);
			} else {
				progressMessage.setText("当前进度:" + progress + "%");
			}
			downloadProgressBar.setProgress(progress);
		}

	};

	/**
	 * @author ideasandroid 主下载线程
	 */
	public class downloadTask extends Thread {
		private int blockSize, downloadSizeMore;
		private int threadNum = 5;
		String urlStr, threadNo, fileName;

		public downloadTask(String urlStr, int threadNum, String fileName) {
			this.urlStr = urlStr;
			this.threadNum = threadNum;
			this.fileName = fileName;
		}

		@SuppressLint("WrongConstant")
		@Override
		public void run() {
			FileDownloadThread[] fds = new FileDownloadThread[threadNum];
			try {
				URL url = new URL(urlStr);
				URLConnection conn = url.openConnection();
				// 获取下载文件的总大小
				fileSize = conn.getContentLength();
				// 计算每个线程要下载的数据量
				blockSize = fileSize / threadNum;
				// 解决整除后百分比计算误差
				downloadSizeMore = (fileSize % threadNum);
				File file = new File(fileName);
				for (int i = 0; i < threadNum; i++) {
					// 启动线程，分别下载自己需要下载的部分
					FileDownloadThread fdt = new FileDownloadThread(url, file, i * blockSize,
							(i + 1) * blockSize - 1);
					fdt.setName("Thread" + i);
					fdt.start();
					fds[i] = fdt;
				}
				boolean finished = false;
				while (!finished) {
					// 先把整除的余数搞定
					downloadedSize = downloadSizeMore;
					finished = true;
					for (int i = 0; i < fds.length; i++) {
						downloadedSize += fds[i].getDownloadSize();
						if (!fds[i].isFinished()) {
							finished = false;
						}
					}
					// 通知handler去更新视图组件
					handler.sendEmptyMessage(0);
					// 休息1秒后再读取下载进度
					sleep(200);
				}
			} catch (Exception e) {
				Toast.makeText(UpdateActivity.this, "网络连接失败", 1000).show();
				e.printStackTrace();
			}

		}
	}

	public void chooseFile() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.commonsetupfile, null);

		/* 初始化mPath，用以显示目前路径 */
		mPath = (TextView) view.findViewById(R.id.mPath);
		fileList = (ListView) view.findViewById(R.id.fileList);
		getFileDir(rootPath);
		Button fileDetermine = (Button) view.findViewById(R.id.fileDetermine);
		Button fileCancle = (Button) view.findViewById(R.id.fileCancle);
		final AlertDialog dialog = new AlertDialog.Builder(UpdateActivity.this).create();
		dialog.setView(view);
		dialog.show();
		fileDetermine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				downloadFileName.setText(mPath.getText() + "/");

				dialog.cancel();
			}
		});
		fileCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		File file = new File(paths.get(position));
		if (file.canRead()) {
			if (file.isDirectory()) {
				/* 如果是文件夹就运行getFileDir() */
				getFileDir(paths.get(position));
			}
		} else {
			/* 弹出AlertDialog显示权限不足 */
			new AlertDialog.Builder(this).setTitle("Message").setMessage("权限不足!")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}).show();
		}

	}

	/* 取得文件架构的method */
	private void getFileDir(String filePath) {
		/* 设定目前所存路径 */
		mPath.setText(filePath);
		items = new ArrayList<String>();
		paths = new ArrayList<String>();

		File f = new File(filePath);
		File[] files = f.listFiles();

		if (!filePath.equals(rootPath)) {
			/* 第一笔设定为[并到根目录] */
			items.add("b1");
			paths.add(rootPath);
			/* 第二笔设定为[并勺层] */
			items.add("b2");
			paths.add(f.getParent());
		}
		/* 将所有文件放入ArrayList中 */
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				items.add(file.getName());
				paths.add(file.getPath());
			}
		}

		/* 使用自定义的MyAdapter来将数据传入ListActivity */
		fileList.setAdapter(new MyAdapter(this, items, paths));
		fileList.setOnItemClickListener(this);
	}
}
