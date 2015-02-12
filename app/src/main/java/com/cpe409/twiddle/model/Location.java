package com.cpe409.twiddle.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Location implements Serializable {

    public double latitude;
    public double longitude;

    public Location(LatLng latLng) {
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
    }
}
