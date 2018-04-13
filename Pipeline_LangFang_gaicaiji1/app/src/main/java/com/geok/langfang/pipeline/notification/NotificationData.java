package com.geok.langfang.pipeline.notification;

import java.io.Serializable;

/**
 * 
 * @类描述 用来保存新消息的数据bean
 * @author 吴长明[wuchangming] Email:changmingw@geo-k.cn Tel:***********
 * @createDate 2013-7-4 上午10:24:20
 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
 */
public class NotificationData implements Serializable {

	private String notitype;
	private String notitime;
	private String notititle;
	private String notidetail;
	private int flag;

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getNotititle() {
		return notititle;
	}

	public void setNotititle(String notititle) {
		this.notititle = notititle;
	}

	public String getNotitype() {
		return notitype;
	}

	public void setNotitype(String notitype) {
		this.notitype = notitype;
	}

	public String getNotitime() {
		return notitime;
	}

	public void setNotitime(String notitime) {
		this.notitime = notitime;
	}

	public String getNotidetail() {
		return notidetail;
	}

	public void setNotidetail(String notidetail) {
		this.notidetail = notidetail;
	}

}
