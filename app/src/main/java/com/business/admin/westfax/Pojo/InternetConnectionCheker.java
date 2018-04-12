package com.business.admin.westfax.Pojo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnectionCheker {

	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;

	private Context _context;

	public InternetConnectionCheker(Context context) {
		this._context = context;
	}

	public boolean isConnectingToInternet() {

		int conn = InternetConnectionCheker.getConnectivityStatus(_context);

		boolean status = false;

		if (conn == InternetConnectionCheker.TYPE_WIFI) {

			status = true;

		} else if (conn == InternetConnectionCheker.TYPE_MOBILE) {

			status = true;

		} else if (conn == InternetConnectionCheker.TYPE_NOT_CONNECTED) {

			status = false;

		}
		return status;
	}

	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		}
		return TYPE_NOT_CONNECTED;
	}
}
