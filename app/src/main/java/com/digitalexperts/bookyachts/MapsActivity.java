package com.digitalexperts.bookyachts;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.customClasses.AppConstants;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        Float latitude ;
        Float longitude;

        Log.e("test",AppConstants.yachtsModel.getLongitude());
        Log.e("test",AppConstants.yachtsModel.getLatitude());
        if(AppConstants.yachtsModel.getLongitude() == null || AppConstants.yachtsModel.getLongitude().equals(""))
        {
            latitude = 25.276987f;
            longitude =55.296249f;
        }
        else
        {
            latitude = Float.parseFloat(AppConstants.yachtsModel.getLatitude());
            longitude = Float.parseFloat(AppConstants.yachtsModel.getLongitude());
        }

        // Add a marker in Sydney and move the camera

        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(AppConstants.yachtsModel.getLocation()));


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));


    }
}
