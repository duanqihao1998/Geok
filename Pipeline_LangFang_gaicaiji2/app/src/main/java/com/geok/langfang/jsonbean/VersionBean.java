package com.geok.langfang.jsonbean;

/**
 * 
 * @类描述
 * @author 段淇皓[wuchangming] Email:changmingw@geo-k.cn
 * @createDate 2018-4-12 下午4:10:27
 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
 */
public class VersionBean {
	/**
	 * VERSION : 3.0.5
	 * URL : http://123.124.230.18:8080/pmi/version/xbxj.apk
	 * TYPE : 0
	 */

	private String VERSION;
	private String URL;
	private String TYPE;

	public VersionBean(String VERSION, String URL, String TYPE) {
		this.VERSION = VERSION;
		this.URL = URL;
		this.TYPE = TYPE;
	}

	public String getVERSION() {
		return VERSION;
	}

	public void setVERSION(String VERSION) {
		this.VERSION = VERSION;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String TYPE) {
		this.TYPE = TYPE;
	}

//	/**
//	 * VERSION 版本号；REMARK：更新内容；TYPE：更新类型，1为必要更新，0为非必要更新
//	 */
//	String VERSION;
//	String REMARK;
//	String TYPE;
//
//	public String getVERSION() {
//		return VERSION;
//	}
//
//	public void setVERSION(String vERSION) {
//		VERSION = vERSION;
//	}
//
//	public String getREMARK() {
//		return REMARK;
//	}
//
//	public void setREMARK(String rEMARK) {
//		REMARK = rEMARK;
//	}
//
//	public String getTYPE() {
//		return TYPE;
//	}
//
//	public void setTYPE(String tYPE) {
//		TYPE = tYPE;
//	}

}
