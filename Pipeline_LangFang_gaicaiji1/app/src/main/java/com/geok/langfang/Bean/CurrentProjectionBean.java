package com.geok.langfang.Bean;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class CurrentProjectionBean {

	public int getZoomLevel() {
		return ZoomLevel;
	}

	public void setZoomLevel(int zoomLevel) {
		ZoomLevel = zoomLevel;
	}

	public GeoPoint getLeftTop() {
		return LeftTop;
	}

	public void setLeftTop(GeoPoint leftTop) {
		LeftTop = leftTop;
	}

	public GeoPoint getRightTop() {
		return RightTop;
	}

	public void setRightTop(GeoPoint rightTop) {
		RightTop = rightTop;
	}

	public GeoPoint getLeftBottom() {
		return LeftBottom;
	}

	public void setLeftBottom(GeoPoint leftBottom) {
		LeftBottom = leftBottom;
	}

	public GeoPoint getRightBottom() {
		return RightBottom;
	}

	public void setRightBottom(GeoPoint rightBottom) {
		RightBottom = rightBottom;
	}

	private int ZoomLevel;
	private GeoPoint LeftTop;
	private GeoPoint RightTop;
	private GeoPoint LeftBottom;
	private GeoPoint RightBottom;

}
