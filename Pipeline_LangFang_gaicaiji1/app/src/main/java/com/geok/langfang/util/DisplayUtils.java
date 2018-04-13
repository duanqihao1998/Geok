package com.geok.langfang.util;

import android.graphics.Point;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;

/**
 * Android大小单位转换工具类
 * 
 * @author yangzhiwei
 */
public class DisplayUtils {
	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(float pxValue, float scale) {
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(float dipValue, float scale) {
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(float pxValue, float fontScale) {
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(float spValue, float fontScale) {
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 讲像素转换为经纬坐标
	 * 
	 * @author zhong
	 * @param projection
	 * @param widht
	 * @param height
	 * @return
	 */
	public static GeoPoint jwBypx(Projection projection, int widht, int height) {
		return projection.fromPixels(widht, height);
	}

	/**
	 * 讲经纬坐标转为px
	 * 
	 * @author zhongDDDDD
	 * @param projection
	 * @param point
	 * @return
	 */
	public static Point pxByjw(Projection projection, GeoPoint point) {
		Point p = projection.toPixels(point, null);
		return p;
	}

	public static GeoPoint jwBysPxePx(int swidht, int sheight, int ewidht, int eheight) {
		// eheight-sheight;?
		return null;
	}
}
