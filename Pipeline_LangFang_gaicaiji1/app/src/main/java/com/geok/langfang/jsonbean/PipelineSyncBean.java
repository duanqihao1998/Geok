package com.geok.langfang.jsonbean;

public class PipelineSyncBean {

	/**
	 * 
	 * children:下一级管线信息, createdate:管线创建日期, lineloopeventid:管线id,
	 * lineloopname:管线名称, linetype:管线类型 parentlineloopeventid:上一级管线id
	 */
	PipelineSyncBean children;

	public PipelineSyncBean getChildren() {
		return children;
	}

	public void setChildren(PipelineSyncBean children) {
		this.children = children;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getLineloopname() {
		return lineloopname;
	}

	public void setLineloopname(String lineloopname) {
		this.lineloopname = lineloopname;
	}

	public String getLinetype() {
		return linetype;
	}

	public void setLinetype(String linetype) {
		this.linetype = linetype;
	}

	public String getParentlineloopeventid() {
		return parentlineloopeventid;
	}

	public void setParentlineloopeventid(String parentlineloopeventid) {
		this.parentlineloopeventid = parentlineloopeventid;
	}

	String createdate;
	String lineloopname;
	String linetype;
	String parentlineloopeventid;

}
