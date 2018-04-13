package com.geok.langfang.pipeline.map;

import java.util.Random;

/**
 * 
 * @类描述 天地图url
 * @author 张龙飞 Email:longfeiz@geo-k.cn Tel:13671277587
 * @createDate 2013-11-19 下午2:09:59
 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
 */
public class TDTUrl {
	private TianDiTuTiledMapServiceType _tiandituMapServiceType;
	private int _level;
	private int _col;
	private int _row;

	public TDTUrl(int level, int col, int row, TianDiTuTiledMapServiceType tiandituMapServiceType) {
		this._level = level;
		this._col = col;
		this._row = row;
		this._tiandituMapServiceType = tiandituMapServiceType;
	}

	public String generatUrl() {
		/**
		 * 天地图矢量、影像
		 * */
		StringBuilder url = new StringBuilder("http://t");
		Random random = new Random();
		int subdomain = (random.nextInt(6) + 1);
		url.append(subdomain);
		switch (this._tiandituMapServiceType) {
		case VEC_C:
			url.append(".tianditu.com/DataServer?T=vec_c&X=").append(this._col).append("&Y=")
					.append(this._row).append("&L=").append(this._level);
			// url.append(".tianditu.com/vec_c/wmts?request=GetTile&service=wmts&version=1.0.0&layer=vec&style=default&format=tiles&TileMatrixSet=c&TILECOL=").append(this._col).append("&TILEROW=").append(this._row).append("&TILEMATRIX=").append(this._level);
			// url.append(".tianditu.com/DataServer?T=vec_w&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
			break;
		case CVA_C:
			url.append(".tianditu.com/DataServer?T=cva_c&X=").append(this._col).append("&Y=")
					.append(this._row).append("&L=").append(this._level);
			// url.append(".tianditu.com/DataServer?T=cva_w&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
			break;
		case CIA_C:
			url.append(".tianditu.com/DataServer?T=cia_c&X=").append(this._col).append("&Y=")
					.append(this._row).append("&L=").append(this._level);
			break;
		case IMG_C:
			url.append(".tianditu.com/DataServer?T=img_c&X=").append(this._col).append("&Y=")
					.append(this._row).append("&L=").append(this._level);
			break;
		default:
			return null;
		}
		return url.toString();
	}

}
