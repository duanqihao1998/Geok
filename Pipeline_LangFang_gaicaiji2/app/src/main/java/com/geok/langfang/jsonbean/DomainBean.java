package com.geok.langfang.jsonbean;

import java.util.List;

public class DomainBean {
	/**
	 * DOMAINNAME:域值类型名,DOMAINVAL:域值,CODENAME:域值名称,CODEVAL:域值代码
	 */
	String DOMAINNAME;
	List<DomainBeanChild> chidList;

	public List<DomainBeanChild> getChidList() {
		return chidList;
	}

	public void setChidList(List<DomainBeanChild> chidList) {
		this.chidList = chidList;
	}

	public String getDOMAINNAME() {
		return DOMAINNAME;
	}

	public void setDOMAINNAME(String dOMAINNAME) {
		DOMAINNAME = dOMAINNAME;
	}

}
