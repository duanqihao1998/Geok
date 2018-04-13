package com.geok.langfang.util;

import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;
import com.geok.langfang.Bean.CurrentProjectionBean;

/**
 * 
 * @类描述 用于一些基本的关于GeoPoint的计算
 * @author 孙士海[sunshihai] Email:shihais@geo-k.cn Tel:
 * @createDate 2013-7-3 下午02:10:46
 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
 */

public class Calculate {

	private MapView mapView;
	private int width, height;

	public Calculate(MapView mapView, int width, int height) {
		this.mapView = mapView;
		this.width = width;
		this.height = height;
	}

	/**
	 * 
	 * @功能描述 计算当前屏幕四角所对应的经纬度
	 * @author 孙士海[sunshihai] Email:shihais@geo-k.cn Tel:
	 * @param zoomlevel
	 * @return
	 * @createDate 2013-7-3 下午02:11:36
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */

	public CurrentProjectionBean getCurrentProjection(int zoomlevel) {
		CurrentProjectionBean bean = new CurrentProjectionBean();
		mapView.getController().setZoom(zoomlevel);
		mapView.refresh();

		Projection projection = mapView.getProjection();
		bean.setZoomLevel(zoomlevel);
		bean.setLeftTop(projection.fromPixels(0, 0));
		bean.setRightTop(projection.fromPixels(width, 0));
		bean.setLeftBottom(projection.fromPixels(0, height));
		bean.setRightBottom(projection.fromPixels(width, height));
		return bean;

	}

	/**
	 * 获得最大的纬度的GeoPoint
	 * 
	 * @param GeoPoint
	 *            []
	 * @return GeoPoint
	 */
	public static GeoPoint getMaxLatFromKnown(GeoPoint[] point) {
		GeoPoint geoPoint = point[0];

		for (int i = 0; i <= point.length - 1; i++) {
			if (i < point.length) {
				if (geoPoint.getLatitudeE6() < point[i].getLatitudeE6()) {
					geoPoint = point[i];
				}
			}

		}
		return geoPoint;
	}

	/**
	 * 获得最小纬度的GeoPoint
	 * 
	 * @param point
	 * @return
	 */

	public static GeoPoint getMinLatFromKnown(GeoPoint[] point) {
		GeoPoint geoPoint = point[0];

		for (int i = 0; i <= point.length - 1; i++) {
			if (i < point.length) {

				if (geoPoint.getLatitudeE6() > point[i].getLatitudeE6()) {
					geoPoint = point[i];
				}
			}

		}
		return geoPoint;
	}

	/**
	 * 获得最大的纬度的GeoPoint
	 * 
	 * @param point
	 * @return
	 */
	public static GeoPoint getMaxLonFromKnown(GeoPoint[] point) {
		GeoPoint geoPoint = point[0];
		for (int i = 0; i <= point.length - 1; i++) {
			if (i < point.length) {
				if (geoPoint.getLongitudeE6() < point[i].getLongitudeE6()) {
					geoPoint = point[i];
				}
			}

		}
		return geoPoint;
	}

	/**
	 * 获得最小纬度的GeoPoint
	 * 
	 * @param point
	 * @return
	 */

	public static GeoPoint getMinLonFromKnown(GeoPoint[] point) {
		GeoPoint geoPoint = point[0];
		int length = point.length;
		System.out.println(length + "  ==   length");
		for (int i = 0; i <= length - 1; i++) {
			if (i < length) {
				if (geoPoint.getLongitudeE6() > point[i].getLongitudeE6()) {
					geoPoint = point[i];
				}
			}

		}
		return geoPoint;
	}

	/**
	 * 根据给定的GeoPoint 集合 求出这些点的中心点
	 * 
	 * @param point
	 * @return
	 */
	public static GeoPoint getCenterGeoPointFromKnow(GeoPoint[] point) {
		GeoPoint centerPoint = new GeoPoint(
				(getMaxLatFromKnown(point).getLatitudeE6() + getMinLatFromKnown(point)
						.getLatitudeE6()) / 2,
				(getMaxLonFromKnown(point).getLongitudeE6() + getMinLonFromKnown(point)
						.getLongitudeE6()) / 2);

		return centerPoint;
	}

}
