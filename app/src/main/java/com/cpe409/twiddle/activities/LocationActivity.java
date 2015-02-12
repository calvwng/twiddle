package com.cpe409.twiddle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.model.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.Serializable;


public class LocationActivity extends ActionBarActivity implements OnMapReadyCallback {

    public final static String EXTRA_LOCATION = "LOCATION";

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        MapFragment mapFragment = (MapFragment) this.getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_create:
                this.sendLocation();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLngBounds boundsUSA = new LatLngBounds(new LatLng(23, -125), new LatLng(47, -66));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(boundsUSA.getCenter(), 2));

        googleMap.setMyLocationEnabled(true);

        this.googleMap = googleMap;
    }

    private void sendLocation() {
        LatLng latLng = this.googleMap.getCameraPosition().target;
        Location location = new Location(latLng);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_LOCATION, location);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}