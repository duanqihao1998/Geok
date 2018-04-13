package com.geok.langfang.util;

import com.geok.langfang.tools.Tools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OffnetReceive extends BroadcastReceiver {

	Activity c;

	public OffnetReceive(Activity c) {
		this.c = c;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
			boolean check = (Tools.isNetworkAvailable(c));
			Tools.showOffnet(c, check);

		}
	}

}
