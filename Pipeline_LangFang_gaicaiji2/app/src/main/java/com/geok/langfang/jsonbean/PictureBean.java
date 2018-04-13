package com.geok.langfang.jsonbean;

import java.io.Serializable;

public class PictureBean implements Serializable {
	/*
	 * PATH:图片数据 PICTUREDESCRIPTION:图片描述
	 */
	private String PATH;

	public String getPATH() {
		return PATH;
	}

	public void setPATH(String pATH) {
		PATH = pATH;
	}

	public String getPICTUREDESCRIPTION() {
		return PICTUREDESCRIPTION;
	}

	public void setPICTUREDESCRIPTION(String pICTUREDESCRIPTION) {
		PICTUREDESCRIPTION = pICTUREDESCRIPTION;
	}

	private String PICTUREDESCRIPTION;

}
