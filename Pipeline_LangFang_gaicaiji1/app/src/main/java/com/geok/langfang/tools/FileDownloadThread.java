package com.geok.langfang.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

/**
 * 
 * 单个下载线程
 */
public class FileDownloadThread extends Thread {
	private static final int BUFFER_SIZE = 1024;
	private URL url;
	private File file;
	private int startPosition;
	private int endPosition;
	private int curPosition;
	// 用于标识当前线程是否下载完成
	private boolean finished = false;
	private int downloadSize = 0;

	public FileDownloadThread(URL url, File file, int startPosition, int endPosition) {
		this.url = url;
		this.file = file;
		this.startPosition = startPosition;
		this.curPosition = startPosition;
		this.endPosition = endPosition;
	}

	@Override
	public void run() {
		BufferedInputStream bis = null;
		RandomAccessFile fos = null;
		byte[] buf = new byte[BUFFER_SIZE];
		URLConnection con = null;
		try {
			con = url.openConnection();
			con.setAllowUserInteraction(true);
			// 设置当前线程下载的起点，终点
			con.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);
			// 使用java中的RandomAccessFile 对文件进行随机读写操作
			fos = new RandomAccessFile(file, "rw");
			// 设置开始写文件的位置
			fos.seek(startPosition);
			bis = new BufferedInputStream(con.getInputStream());
			// 开始循环以流的形式读写文件
			while (curPosition < endPosition) {
				int len = bis.read(buf, 0, BUFFER_SIZE);
				if (len == -1) {
					break;
				}
				fos.write(buf, 0, len);
				curPosition = curPosition + len;
				if (curPosition > endPosition) {
					downloadSize += len - (curPosition - endPosition) + 1;
				} else {
					downloadSize += len;
				}
			}
			// 下载完成设为true
			this.finished = true;
			bis.close();
			fos.close();
		} catch (IOException e) {
			Log.d(getName() + " Error:", e.getMessage());
		}
	}

	public static void down(String URL, long nPos, String savePathAndFile) {
		HttpURLConnection conn = null;
		try {
			/*
			 * String content="<?xml version=/"1.0/" encoding=/"utf-8/" ?>"
			 * +"<xmlRequest>" +"<header>04</header>" +"<body>"
			 * +"<user userName=/"
			 * 222/" password=/"222222/" currentVision=/"20101020165216/" >"
			 * +"</user>" +"</body>" +"</xmlRequest>";
			 */
			conn = (HttpURLConnection) new URL(URL).openConnection();
			/*
			 * conn.setRequestProperty("content-type", "text/html");
			 * conn.setRequestProperty("User-Agent", "NetFox");// 设置User-Agent
			 * conn.setRequestProperty("RANGE", "bytes=" + nPos);// 设置断点续传的开始位置
			 * conn.setRequestMethod("POST"); //设置请求方法为POST, 也可以为GET
			 * conn.setDoInput(true); conn.setDoOutput(true);
			 * 
			 * OutputStream outStream = conn.getOutputStream(); PrintWriter out
			 * = new PrintWriter(outStream); out.print(content); out.flush();
			 * out.close();
			 */
			// 获得输入流
			InputStream input = conn.getInputStream();
			RandomAccessFile oSavedFile = new RandomAccessFile(savePathAndFile, "rw");
			// 定位文件指针到nPos位置
			oSavedFile.seek(nPos);
			byte[] b = new byte[1024 * 5];
			int nRead;
			// 从输入流中读入字节流，然后写到文件中
			while ((nRead = input.read(b, 0, 1024 * 5)) > 0) {
				(oSavedFile).write(b, 0, nRead);
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isFinished() {
		return finished;
	}

	public int getDownloadSize() {
		return downloadSize;
	}
}
