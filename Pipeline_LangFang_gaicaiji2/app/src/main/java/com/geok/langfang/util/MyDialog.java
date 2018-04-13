package com.geok.langfang.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

public class MyDialog {
	
	Context c = null;
	Dialog dialog;
	public MyDialog(Context c, String message){
		this.c = c;
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setMessage(message);
		dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		
	}
	public void cancel(){
		dialog.cancel();
	}
	public void show(){
		dialog.show();
	}
}
