package com.cpe409.twiddle.activities;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
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

import java.io.IOException;


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

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);

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
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
            String locationAddress = intent.getStringExtra(SearchManager.QUERY);
            LatLng latLng = this.getCoordinateFromAddress(locationAddress);

            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
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
        Location location = new Location(latLng, getAddress(latLng));

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_LOCATION, location);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private Address getAddress(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this);
        Address address = null;

        try {
            address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    private LatLng getCoordinateFromAddress(String locationAddress) {
        Geocoder geocoder = new Geocoder(this);
        LatLng latLng = null;

        try {
            Address address = geocoder.getFromLocationName(locationAddress, 1).get(0);
            latLng = new LatLng(address.getLatitude(), address.getLongitude());
        } catch (IOException e) {
            latLng = new LatLng(0, 0);
            e.printStackTrace();
        }

        return latLng;
    }
}