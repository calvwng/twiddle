package com.cpe409.twiddle.model;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class AdventureLocation implements Serializable {

  public double latitude;
  public double longitude;
  public String strAddress;
  public String city;

  public AdventureLocation(LatLng latLng, Address address) {
    this.latitude = latLng.latitude;
    this.longitude = latLng.longitude;

    this.strAddress = getStrAddress(address);
    this.city = getCityName(address);
  }


  private String getStrAddress(Address address) {
    String strAddress = "";

    int maxIndex = address.getMaxAddressLineIndex();
    if (maxIndex >= 0) {
      for (int i = 0; i < maxIndex; i++) {
        strAddress += address.getAddressLine(i);
        strAddress += ", ";
      }
      strAddress += address.getAddressLine(maxIndex);
    }

    return strAddress;
  }

  private String getCityName(Address address) {
    if (address.getLocality() != null) {
      city = address.getLocality();
    } else if (address.getSubAdminArea() != null) {
      city = address.getSubAdminArea();
    } else {
      city = address.getAdminArea();
    }

    return city;
  }
}
