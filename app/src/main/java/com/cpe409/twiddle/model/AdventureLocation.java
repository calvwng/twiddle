package com.cpe409.twiddle.model;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class AdventureLocation implements Serializable {

  public double latitude;
  public double longitude;
  public String strAddress;

  public AdventureLocation(LatLng latLng, Address address) {
    this.latitude = latLng.latitude;
    this.longitude = latLng.longitude;

    this.setAddress(address);
  }

  private void setAddress(Address address) {
    String featureName = checkString(address.getFeatureName(), ", ");
    String adminArea = checkString(address.getAdminArea(), ", ");
    String subAdminArea = checkString(address.getSubAdminArea(), ", ");
    String countryName = checkString(address.getCountryName(), "");

    this.strAddress = featureName + adminArea + subAdminArea + countryName;
  }

  private String checkString(String str, String separator) {
    if (str != null) {
      str = str + separator;
    } else {
      str = "";
    }

    return str;
  }
}
