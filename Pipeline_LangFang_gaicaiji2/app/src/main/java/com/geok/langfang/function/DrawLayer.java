package com.geok.langfang.function;

import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Symbol;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.geok.langfang.Bean.CurrentProjectionBean;
import com.geok.langfang.util.Calculate;

public class DrawLayer {

	MapView mapView = null;
	Geometry geometry;

	GraphicsOverlay graphicsOverlay = null;
	Graphic graphic;
	int width, height;
	long lineId;
	long[] pointid;
	double mLat1 = 39.90923;
	double mLon1 = 116.357428;
	double mLat2 = 39.90923;
	double mLon2 = 116.397428;
	double mLat3 = 39.94923;
	double mLon3 = 116.437428;

	double mLat4 = 40.73060;
	double mLon4 = 110.017089;

	/**
	 * 
	 * @param width
	 * @param height
	 * @param mapView
	 */

	public DrawLayer(int width, int height, MapView mapView) {
		this.mapView = mapView;
		this.width = width;
		this.height = height;
		geometry = new Geometry();
		graphicsOverlay = new GraphicsOverlay(mapView);

	}

	/**
	 * 
	 * @功能描述 画线函数
	 * @author 孙士海[sunshihai] Email:shihais@geo-k.cn Tel:
	 * @param point
	 * @createDate 2013-7-3 下午03:53:37
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void DrawLine(GeoPoint[] point) {

		geometry.setPolyLine(point);
		Symbol symbol = new Symbol();
		Symbol.Color linecolor = symbol.new Color();
		linecolor.alpha = 123;
		linecolor.blue = 255;
		linecolor.green = 123;
		linecolor.red = 20;
		symbol.setLineSymbol(linecolor, 12);

		graphic = new Graphic(geometry, symbol);
		lineId = graphicsOverlay.setData(graphic);
		mapView.getOverlays().add(graphicsOverlay);
		mapView.getController().setCenter(Calculate.getCenterGeoPointFromKnow(point));
		int zoomlevel = getCorrectLevel(point);

		mapView.getController().setZoom(zoomlevel);
		mapView.refresh();
	}

	// 清除线
	public void ClearLine() {
		if (lineId != 0)
			graphicsOverlay.removeGraphic(lineId);
		mapView.refresh();
	}

	/**
	 * 
	 * @功能描述 画点的函数
	 * @author 孙士海[sunshihai] Email:shihais@geo-k.cn Tel:
	 * @param point
	 * @createDate 2013-7-3 下午03:52:05
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void DrawPoint(GeoPoint[] point) {
		pointid = new long[point.length];
		for (int i = 0; i < point.length; i++) {
			geometry.setPoint(point[i], 10);

			Symbol pointSymbol = new Symbol();
			Symbol.Color pointColor = pointSymbol.new Color();
			pointColor.red = 0;
			pointColor.green = 0;
			pointColor.blue = 0;
			pointColor.alpha = 126;
			pointSymbol.setPointSymbol(pointColor);

			Graphic pointGraphic = new Graphic(geometry, pointSymbol);
			pointid[i] = graphicsOverlay.setData(graphic);
			graphicsOverlay.setData(pointGraphic);
			mapView.refresh();
		}

	}

	/**
	 * 
	 * @功能描述 清除点
	 * @author 孙士海[sunshihai] Email:shihais@geo-k.cn Tel:
	 * @createDate 2013-7-3 下午03:51:42
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public void ClearPoint() {
		for (int i = 0; i < pointid.length; i++) {
			if (pointid[i] != 0)
				graphicsOverlay.removeGraphic(lineId);
		}

		mapView.refresh();
	}

	/**
	 * 
	 * @功能描述 比较图层是否超出了当前屏幕范围，若超出则放大Zoomlevel 直到图层在当前屏幕范围内为准
	 * @author 孙士海[sunshihai] Email:shihais@geo-k.cn Tel:
	 * @param point
	 * @return
	 * @createDate 2013-7-3 下午03:54:14
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public int getCorrectLevel(GeoPoint[] point) {
		CurrentProjectionBean bean;
		int zoomlevel = 12;
		GeoPoint maxlat = Calculate.getMaxLatFromKnown(point);
		GeoPoint minlat = Calculate.getMinLatFromKnown(point);
		GeoPoint maxlon = Calculate.getMaxLonFromKnown(point);
		GeoPoint minlon = Calculate.getMinLonFromKnown(point);

		int minzoomlevel = mapView.getMinZoomLevel();
		int maxzoomlevel = mapView.getMaxZoomLevel();
		System.out.println(minzoomlevel + "==minzoomlevel");
		System.out.println(maxzoomlevel + "==maxzoomlevel");
		Calculate calculate = new Calculate(mapView, width, height);
		for (; zoomlevel > minzoomlevel; zoomlevel--) {
			bean = calculate.getCurrentProjection(zoomlevel);
			if (maxlat.getLatitudeE6() < bean.getLeftTop().getLatitudeE6()
					&& minlat.getLatitudeE6() > bean.getLeftBottom().getLatitudeE6()
					&& maxlon.getLongitudeE6() < bean.getRightBottom().getLongitudeE6()
					&& minlon.getLongitudeE6() > bean.getLeftBottom().getLongitudeE6()) {
				return zoomlevel;
			}

		}
		return 0;
	}

}
