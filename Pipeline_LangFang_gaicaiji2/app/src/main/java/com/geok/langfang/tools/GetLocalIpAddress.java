package com.geok.langfang.tools;

import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class GetLocalIpAddress {

	/*
	 * public String getLocalIpAddress() { try { for
	 * (Enumeration<NetworkInterface> en = NetworkInterface
	 * .getNetworkInterfaces(); en.hasMoreElements();) { NetworkInterface intf =
	 * en.nextElement(); for (Enumeration<InetAddress> enumIpAddr = intf
	 * .getInetAddresses(); enumIpAddr.hasMoreElements();) { InetAddress
	 * inetAddress = enumIpAddr.nextElement(); if
	 * (!inetAddress.isLoopbackAddress()) { return
	 * inetAddress.getHostAddress().toString(); } } } } catch (SocketException
	 * ex) { Log.e("WifiPreference IpAddress", ex.toString()); } return null; }
	 */

	// ConnectivityManager mConnectivityManager = null;
	// mConnectivityManager = (ConnectivityManager)
	// getSystemService(CONNECTIVITY_SERVICE);
	// String ip = "10.0.0.0";
	// if(mConnectivityManager.getActiveNetworkInfo().getType() ==
	// ConnectivityManager.TYPE_WIFI){
	// ip = getIpAddress();
	// System.out.println("ipppppppppppppppppppppppppp：" + ip);
	// }else if (mConnectivityManager.getActiveNetworkInfo().getType() ==
	// ConnectivityManager.TYPE_MOBILE ){
	// GetLocalIpAddress getip = new GetLocalIpAddress();
	// ip = getip.getLocalIpAddress();
	// System.out.println("ipppppppppppppppppppppppppp：" + ip);
	// }
	// System.out.println("mConnectivityManager.getActiveNetworkInfo().getType() :"
	// + mConnectivityManager.getActiveNetworkInfo().getType() );
	//
	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
					.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
						.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("llll", ex.toString());
		}
		return null;
	}
}
