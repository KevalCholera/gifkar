package com.smartsense.gifkar.utill;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;

import java.util.List;

public class LocationFinderService extends Service implements LocationListener {

	protected LocationManager locationManager;
	Location location;
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;
	// flag for GPS status
	boolean canGetLocation = false;

	double latitude; // latitude
	double longitude; // longitude

	public LocationFinderService(Context context) {
		locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
		getLastKnownLocation();

	}

	public Location getLocation(String provider) {
		if (locationManager.isProviderEnabled(provider)) {

			if (locationManager != null) {
				location = locationManager.getLastKnownLocation(provider);
				return location;
			}
		}
		return null;
	}

	public boolean isGPSAvilabel(String provider) {
		return locationManager.isProviderEnabled(provider);
	}

	public void stopUpdateRequest() {
		try {
			locationManager.removeUpdates(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Location getLastKnownLocation() {
		List<String> providers = locationManager.getProviders(true);
		Location bestLocation = null;

		for (String provider : providers) {

			// if (locationManager.isProviderEnabled(provider)) {
			// locationManager.requestLocationUpdates(provider,
			// MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
			// } else {
			// buildAlertMessageNoGps();
			// }

			Location l = locationManager.getLastKnownLocation(provider);

			if (l == null) {
				continue;
			}
			if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
				// Found best last known location: %s", l);
				bestLocation = l;
			}
		}

		return bestLocation;
	}

	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(LocationFinderService.this);
		}
	}

	public void buildAlertMessageNoGps() {

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Your GPS seems to be disabled, do you want to enable it?").setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Function to check GPS/wifi enabled
	 *
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}
		return longitude;
	}

	/**
	 * Function to get latitude
	 * */
	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}
		return latitude;
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}