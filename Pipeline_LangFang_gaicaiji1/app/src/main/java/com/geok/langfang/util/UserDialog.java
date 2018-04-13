package com.geok.langfang.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * 
 * @类描述 自定义dialog
 * @author 孙士海[sunshihai] Email:shihais@geo-k.cn Tel:
 * @createDate 2013-7-4 上午09:15:15
 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
 */
public class UserDialog extends Dialog {

	private int mResource;

	public UserDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public UserDialog(Context context, int Resource) {
		super(context, android.R.style.Theme_Dialog);
		// TODO Auto-generated constructor stub
		mResource = Resource;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(mResource);
	}

}
