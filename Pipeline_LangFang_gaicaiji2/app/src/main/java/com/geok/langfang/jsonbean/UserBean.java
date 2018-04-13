package com.geok.langfang.jsonbean;

import java.util.ArrayList;
import java.util.List;

public class UserBean {
	String id;
	String parentId;
	String type;// 表示部门org表示人员emp
	List<UserBean> childrenList = new ArrayList<UserBean>();// 部门才有
	String text;// 人名、部门名称

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<UserBean> getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(List<UserBean> childrenList) {
		this.childrenList = childrenList;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
