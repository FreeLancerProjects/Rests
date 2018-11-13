package com.semicolon.rests.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.semicolon.rests.R;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    private final int error_dialog =9001;
    private Marker marker;
    private GoogleMap mMap;
    private double lat=0.0,lng=0.0;
    private float zoom = 16f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getDataFromIntent();
        if (isServiceOk())
        {
            initMap();
        }
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent !=null)
        {
            lat = intent.getDoubleExtra("lat",0.0);
            lng = intent.getDoubleExtra("lng",0.0);

            Log.e("lat",lat+"");
            Log.e("lng",lng+"");

        }
    }

    private void initMap()
    {
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }
    private boolean isServiceOk() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (available== ConnectionResult.SUCCESS)
        {
            return true;
        }else if (GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this,available,error_dialog);
            dialog.show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap!=null)
        {
            mMap = googleMap;
            googleMap.setIndoorEnabled(true);
            googleMap.setTrafficEnabled(false);
            googleMap.setBuildingsEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.maps));
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),zoom));


        }

    }
}
