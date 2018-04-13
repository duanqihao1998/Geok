package com.geok.langfang.jsonbean;

import java.util.List;

public class PileSyncBean {

	// MARKERVALUE:桩的值 ,MARKERTYPE:桩的类型,STACKNAME:桩的名称,STACKCODE:桩的代码,
	// MARKEREVENTID:桩EVENTID,MARKERNAME:桩名称,MARKERSTATION:桩里程值,LINELOOPEVENTID;管网EVENTID

	String LINELOOPEVENTID;

	public String getLINELOOPEVENTID() {
		return LINELOOPEVENTID;
	}

	public void setLINELOOPEVENTID(String lINELOOPEVENTID) {
		LINELOOPEVENTID = lINELOOPEVENTID;
	}

	public List<PileSyncBeanChild> getChildBean() {
		return ChildBean;
	}

	public void setChildBean(List<PileSyncBeanChild> childBean) {
		ChildBean = childBean;
	}

	List<PileSyncBeanChild> ChildBean;

}
