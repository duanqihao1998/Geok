package com.geok.langfang.jsonbean;

public class LineSyncBean {
	/**
	 * "children":[],"createdate":"2012-12-20 22:59:26","lineloopeventid":
	 * "0562ca8e-859c-42b5-9328-
	 * 
	 * b92f8631a680","lineloopname":"库鄯输油干线","linetype":"管道占压","
	 * parentlineloopeventid":"ebe2dae9-9e73-4df0-a547-39c5579bc654"
	 */
	LineSyncBean[] childrenList;
	String createdate;
	String lineloopeventid;
	String lineloopname;
	String linetype;
	String parentlineloopeventid;

	public LineSyncBean[] getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(int num) {
		this.childrenList = new LineSyncBean[num];
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getLineloopeventid() {
		return lineloopeventid;
	}

	public void setLineloopeventid(String lineloopeventid) {
		this.lineloopeventid = lineloopeventid;
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

}
