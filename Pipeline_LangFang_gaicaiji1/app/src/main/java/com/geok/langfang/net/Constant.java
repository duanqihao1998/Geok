package com.geok.langfang.net;

/**
 * 
 * @类描述
 * @author 孙士海[sunshihai] Email:shihais@geo-k.cn Tel:
 * @createDate 2013-7-2 下午03:23:30
 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
 */

public class Constant {
	/**
	 * 
	 */
	public static final String NAMESPACE = "http://WebXml.com.cn/";
	public static String URL = "http://www.webxml.com.cn/webservices/weatherwebservice.asmx";
	/**
	 * 获取所有的GeoPoint
	 */
	public static String getGeoPointAll = "getGeoPointAll";
	/**
	 * 根据level的级别 获得显示一定数量的GeoPoint
	 */
	public static String getGeoPointByLevel = "getGeoPointByLevel";
	/**
	 * 根据拖拽显示GeoPoint
	 */
	public static String getGeoPointByDrag = "getGeoPointByDrag";

	public static final int ALL = 1;
	public static final int LEVEL = 2;
	public static final int DRAG = 3;

}
