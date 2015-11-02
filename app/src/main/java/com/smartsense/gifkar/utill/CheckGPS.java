package com.smartsense.gifkar.utill;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CheckGPS extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {

			Log.i("GPS", "Changed");


		}
	}
}