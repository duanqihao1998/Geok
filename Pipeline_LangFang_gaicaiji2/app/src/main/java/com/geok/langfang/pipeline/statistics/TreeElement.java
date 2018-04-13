package com.geok.langfang.pipeline.statistics;

import java.util.ArrayList;

public class TreeElement {
	private String id;
	private String outlineTitle;
	public boolean hasParent;
	public boolean hasChild;
	private boolean isChoose;
	private TreeElement parent;
	private int level;
	private ArrayList<TreeElement> childList = new ArrayList<TreeElement>();
	private ArrayList<TreeElement> totalChildList = new ArrayList<TreeElement>();
	private ViewHolder holder;
	String notetype = "1";

	public String getNotetype() {
		return notetype;
	}

	public void setNotetype(String notetype) {
		this.notetype = notetype;
	}

	public ViewHolder getHolder() {
		return holder;
	}

	public void setHolder(ViewHolder holder) {
		this.holder = holder;
	}

	public boolean isChoose() {
		return isChoose;
	}

	public void setChoose(boolean isChoose) {
		this.isChoose = isChoose;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOutlineTitle() {
		return outlineTitle;
	}

	public void setOutlineTitle(String outlineTitle) {
		this.outlineTitle = outlineTitle;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public ArrayList<TreeElement> getChildList() {
		return childList;
	}

	public TreeElement getParent() {
		return parent;
	}

	public void setParent(TreeElement parent) {
		this.parent = parent;
	}

	// private OutlineElement outlineElement;
	private boolean expanded;

	public void onClickIsChoose(boolean checked) {
		this.isChoose = checked;
		if (isChoose && hasChild) {
			for (int i = 0; i < totalChildList.size(); i++) {
				TreeElement childTE = totalChildList.get(i);
				childTE.isChoose = true;
				if (!(childTE.hasChild) && childTE.getNotetype().equals("0")) {
					TreeView.userCheckList.add(childTE.getId());
					TreeView.userNameCheckList.add(childTE.getOutlineTitle());
				}else if((childTE.hasChild) && !(childTE.getNotetype().equals("0"))){
					childTE.onClickIsChoose(checked);
				}

			}
		} else if (!isChoose && hasChild) {
			for (int i = 0; i < totalChildList.size(); i++) {
				TreeElement childTE = totalChildList.get(i);
				childTE.isChoose = false;
				if (!(childTE.hasChild)) {
					TreeView.userCheckList.remove(childTE.getId());
					TreeView.userNameCheckList.remove(childTE.getOutlineTitle());
				}else if(childTE.hasChild){
					childTE.onClickIsChoose(checked);
				}

			}
		}
	}

	public void addChild(TreeElement c) {
		this.childList.add(c);
		this.hasParent = false;
		this.hasChild = true;
		this.isChoose = false;
		c.parent = this;
		c.level = this.level + 1;
		this.totalChildList.add(c);
		setTotalChildList(this.level, c);

	}

	public ArrayList<TreeElement> getTotalChildList() {
		return totalChildList;
	}

	public void setTotalChildList(ArrayList<TreeElement> totalChildList) {
		this.totalChildList = totalChildList;
	}

	private void setTotalChildList(int templevel, TreeElement c) {
		if (templevel > 0) {
			this.parent.totalChildList.add(c);
			templevel--;
			c = this.parent;
			setTotalChildList(templevel, c);
		}
	}

	public TreeElement(String id, String title) {
		super();
		this.id = id;
		this.outlineTitle = title;
		this.level = 0;
		this.hasParent = true;
		this.hasChild = false;
		this.isChoose = false;
		this.parent = null;
	}

	public TreeElement(String id, String outlineTitle, boolean mhasParent, boolean mhasChild,
                       TreeElement parent, int level, boolean expanded) {
		super();
		this.id = id;
		this.outlineTitle = outlineTitle;
		this.hasParent = mhasParent;
		this.hasChild = mhasChild;
		this.parent = parent;

		if (parent != null) {
			this.parent.getChildList().add(this);
		}
		this.level = level;
		this.expanded = expanded;
	}
	// public static TreeElement elementToTreeElement(Element node,String
	// idName,String outString){
	// TreeElement treeElement=new TreeElement(node.attributeValue(idName),
	// node.attributeValue(outString));
	// return treeElement;
	// }

}
