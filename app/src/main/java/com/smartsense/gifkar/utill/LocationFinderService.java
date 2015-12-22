package com.smartsense.gifkar.utill;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

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

    public boolean isGPSAvilabel(String provider) {
        return locationManager.isProviderEnabled(provider);
    }

    public Location getLastKnownLocation1() {
        try {
            return locationManager.getLastKnownLocation
                    (LocationManager.PASSIVE_PROVIDER);
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            try {

                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
//


        }

        return bestLocation;
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