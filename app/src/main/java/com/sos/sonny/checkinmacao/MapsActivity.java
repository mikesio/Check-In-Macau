package com.sos.sonny.checkinmacao;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Double Lat;
    Double Lng;
    String CityName;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = this.getIntent().getExtras();
        Lat = bundle.getDouble("currentLat");
        Lng = bundle.getDouble("currentLng");

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Lat, Lng, 1);
            CityName = addresses.get(0).getLocality();
            if (CityName == null) CityName = addresses.get(0).getCountryName();
            //Toast.makeText(this, CityName,Toast.LENGTH_SHORT).show();
            //Log.d("getLocality", "CityName:" + CityName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //'22.198745', '113.543873' Macao

        LatLng currLoc = new LatLng(Lat, Lng);
        mMap.addMarker(new MarkerOptions().position(currLoc).title(CityName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
        mMap.setMinZoomPreference(17.0f);
        mMap.setMaxZoomPreference(25.0f);
    }
}
