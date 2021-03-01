package com.stonefacesoft.ottaa;

/**
 * Created by Hector on 17/02/2016.
 */

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class LocationService extends Service implements LocationListener {

    private LocationManager locationManager;
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;
    Context context;

    public LocationService() {
    }

    public LocationService(Context context) {
        this.context = context;
        locationManager = (LocationManager) context
                .getSystemService(LOCATION_SERVICE);
    }

    public Location getLocation(String provider) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission
                .ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("LocationService_Permis","permission not granted");
            return null;
        }
        locationManager.requestLocationUpdates(provider, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
        Log.e("LocationService_Permis","permission granted");
        if (locationManager != null) {
            Location location = locationManager.getLastKnownLocation(provider);
                return location;
        }else{
            Log.e("LocationService_Permisr","returning null");
            return null;
        }
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
