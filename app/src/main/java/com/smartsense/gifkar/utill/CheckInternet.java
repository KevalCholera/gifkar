package com.smartsense.gifkar.utill;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class CheckInternet extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		boolean connection = CommonUtil.isInternetAvailable(context);
		if (connection == true) {

			Log.i("NET", "connected");

		} else {

		}
	}
}