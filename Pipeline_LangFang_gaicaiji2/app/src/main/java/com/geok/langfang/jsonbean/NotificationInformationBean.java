package com.geok.langfang.jsonbean;

public class NotificationInformationBean {

	String NOTIFICATIONTYPE;
	String SENDTIME;
	String NAME;
	String DETAIL;

	boolean isNew = false;

	public NotificationInformationBean() {
	}

	/**
	 * NOTIFICATIONTYPE:消息类型,SENDTIME:发送时间,NAME:名称,DETAIL:详情, isNew是否是新
	 * 
	 * @param NOTIFICATIONTYPE
	 * @param SENDTIME
	 * @param NAME
	 * @param DETAIL
	 * @param isNew
	 */
	public NotificationInformationBean(String NOTIFICATIONTYPE, String SENDTIME, String NAME,
			String DETAIL, boolean isNew) {
		this.NOTIFICATIONTYPE = NOTIFICATIONTYPE;
		this.SENDTIME = SENDTIME;
		this.NAME = NAME;
		this.DETAIL = DETAIL;
		this.isNew = isNew;
	}

	public String getNOTIFICATIONTYPE() {
		return NOTIFICATIONTYPE;
	}

	public void setNOTIFICATIONTYPE(String nOTIFICATIONTYPE) {
		NOTIFICATIONTYPE = nOTIFICATIONTYPE;
	}

	public String getSENDTIME() {
		return SENDTIME;
	}

	public void setSENDTIME(String sENDTIME) {
		SENDTIME = sENDTIME;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getDETAIL() {
		return DETAIL;
	}

	public void setDETAIL(String dETAIL) {
		DETAIL = dETAIL;
	}

	public boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

}
