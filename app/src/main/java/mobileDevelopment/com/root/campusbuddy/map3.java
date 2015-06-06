package mobileDevelopment.com.root.campusbuddy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class map3 extends Activity implements OnMapReadyCallback {

    // Google Map
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map3);

        try {
            // Loading map
            initilizeMap();
        } catch (Exception e) {
            Log.i("App", "initialization error" + e.toString());
        }

        googleMap.addMarker(new MarkerOptions().position(new LatLng(-18.142, 178.431))
                .title("Marker").draggable(true));

        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap arg0) {
        // TODO Auto-generated method stub
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(-18.142, 178.431), 2));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

}

