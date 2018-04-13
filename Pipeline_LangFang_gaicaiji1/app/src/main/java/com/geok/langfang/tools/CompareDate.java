package com.geok.langfang.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @类描述 比较起始日期和终止日期的先后
 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel:***********
 * @createDate 2013-10-11 下午2:38:06
 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
 */
public class CompareDate {
	public static Date StringtoDate(String stringDate) throws ParseException {
		Date date = null;
		String stringdatetemp = stringDate;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		date = sdf.parse(stringdatetemp);
		System.out.println("日期为：" + sdf.format(date));
		return date;
	}

	/**
	 * 
	 * @功能描述 计算两个日期型的时间大小
	 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel:***********
	 * @param startDate开始日期
	 * @param endDate结束日期
	 * @return
	 * @createDate 2013-10-11 下午2:38:52
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static Boolean CompareTwoDate(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return null;
		}
		long timeLong = endDate.getTime() - startDate.getTime();
		if (timeLong < 0)
			return false;
		else {
			return true;
		}
	}
}