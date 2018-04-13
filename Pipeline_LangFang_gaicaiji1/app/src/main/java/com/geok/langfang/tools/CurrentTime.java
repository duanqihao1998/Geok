package com.geok.langfang.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentTime {
	static String current_time;

	public static String CurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 获取当前时间
		Date curDate = new Date(System.currentTimeMillis());
		current_time = formatter.format(curDate);
		return current_time;
	}
}
