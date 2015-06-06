package mobileDevelopment.com.root.campusbuddy;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.VisibleRegion;

public class map4 extends SupportMapFragment{

    private final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(41.8138,12.3891), new LatLng(41.9667, 12.5938));
    private final int MAX_ZOOM = 18;
    private final int MIN_ZOOM = 14;

    Context mContext;
    GoogleMap mMap;
    private OverscrollHandler mOverscrollHandler;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
     /*   mContext = getActivity();
        mMap = getMap();
         mOverscrollHandler = new OverscrollHandler();
        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(new  VexLocalTileProvider(getResources().getAssets())));
        CameraUpdate upd = CameraUpdateFactory.newLatLngZoom(new LatLng(41.87145, 12.52849), 14);
        mMap.moveCamera(upd);
        mOverscrollHandler.sendEmptyMessageDelayed(0,100);*/
    }

    private LatLng getLatLngCorrection(LatLngBounds cameraBounds) {
        double latitude=0, longitude=0;
        if(cameraBounds.southwest.latitude < BOUNDS.southwest.latitude) {
            latitude = BOUNDS.southwest.latitude - cameraBounds.southwest.latitude;
        }
        if(cameraBounds.southwest.longitude < BOUNDS.southwest.longitude) {
            longitude = BOUNDS.southwest.longitude - cameraBounds.southwest.longitude;
        }
        if(cameraBounds.northeast.latitude > BOUNDS.northeast.latitude) {
            latitude = BOUNDS.northeast.latitude - cameraBounds.northeast.latitude;
        }
        if(cameraBounds.northeast.longitude > BOUNDS.northeast.longitude) {
            longitude = BOUNDS.northeast.longitude - cameraBounds.northeast.longitude;
        }
        return new LatLng(latitude, longitude);
    }

    private class OverscrollHandler extends Handler {


        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            CameraPosition position = mMap.getCameraPosition();
            VisibleRegion region = mMap.getProjection().getVisibleRegion();
            float zoom = 0;
            if(position.zoom < MIN_ZOOM) zoom = MIN_ZOOM;
            if(position.zoom > MAX_ZOOM) zoom = MAX_ZOOM;
            LatLng correction = getLatLngCorrection(region.latLngBounds);
            if(zoom != 0 || correction.latitude != 0 || correction.longitude != 0) {
                zoom = (zoom==0)?position.zoom:zoom;
                double lat = position.target.latitude + correction.latitude;
                double lon = position.target.longitude + correction.longitude;
                CameraPosition newPosition = new CameraPosition(new LatLng(lat,lon), zoom, position.tilt, position.bearing);
                CameraUpdate update = CameraUpdateFactory.newCameraPosition(newPosition);
                mMap.moveCamera(update);
            }
        /* Recursively call handler every 100ms */
            sendEmptyMessageDelayed(0,100);
        }
    }
}

