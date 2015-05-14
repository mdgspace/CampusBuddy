package com.example.root.campusbuddy;

import android.app.Fragment;
import android.graphics.RectF;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class map extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }







    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(getApplicationContext(), ""+latLng.latitude, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), ""+latLng.longitude, Toast.LENGTH_LONG).show();
    }
}
