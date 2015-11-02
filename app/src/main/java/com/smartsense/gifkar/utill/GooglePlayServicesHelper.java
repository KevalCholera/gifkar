package com.smartsense.gifkar.utill;

import android.app.Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class GooglePlayServicesHelper {

	private Activity mActivity;

	public GooglePlayServicesHelper(Activity activity) {
		mActivity = activity;
	}

	public int getErrorCode() {
		return GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
	}

	public boolean isAvailable() {
		return getErrorCode() == ConnectionResult.SUCCESS;
	}

	public boolean checkPlayServices() {
		if (isAvailable())
			return true;

		final int errorCode = getErrorCode();

		GooglePlayServicesUtil.getErrorDialog(errorCode, mActivity,
				200).show();

		return false;
	}
}