package com.cpe409.twiddle.shared;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;


import com.cpe409.twiddle.BuildConfig;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by Michael on 2/17/2015.
 */
public class LocationHelper {
  static LocationHelper instance;

  public static LocationHelper getInstance() {
    if (instance == null) {
      instance = new LocationHelper();
    }
    return instance;
  }

  public Location getLocation(Context context) {
    if (BuildConfig.DEBUG) {
      Location location = new Location("location");
      location.setLatitude(35.281172);
      location.setLongitude(-120.660257);
      return location;
    }

    LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    float bestAccuracy = 0;
    long bestTime = 0;
    long minTime = 0;
    Location bestLocation = null;

    List<String> matchingProviders = locManager.getAllProviders();
    for (String provider : matchingProviders) {
      Location location = locManager.getLastKnownLocation(provider);
      if (location != null) {
        float accuracy = location.getAccuracy();
        long time = location.getTime();
        if (bestAccuracy == 0) {
          bestAccuracy = accuracy;
        }

        if (bestTime == 0) {
          bestTime = time;
        }

        if (minTime == 0) {
          minTime = time;
        }

        if (bestLocation == null) {
          bestLocation = location;
        }

        if ((time > minTime && accuracy < bestAccuracy)) {
          bestLocation = location;
          bestAccuracy = accuracy;
          bestTime = time;
        } else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime) {
          bestLocation = location;
          bestTime = time;
        }
      }
    }

    return bestLocation;
  }

  public Address getAddress(Context context, LatLng latLng)
        throws IOException, IndexOutOfBoundsException {
    Geocoder geocoder = new Geocoder(context);
    Address address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);

    return address;
  }

  public LatLng getCoordinateFromAddress(Context context, String locationAddress) throws IOException {
    Geocoder geocoder = new Geocoder(context);
    Address address = geocoder.getFromLocationName(locationAddress, 1).get(0);

    return new LatLng(address.getLatitude(), address.getLongitude());
  }
}
