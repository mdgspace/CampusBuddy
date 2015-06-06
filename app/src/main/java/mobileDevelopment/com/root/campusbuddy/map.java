package mobileDevelopment.com.root.campusbuddy;
//
//
//
//
//         public class map extends FragmentActivity
//              implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
//             @Override
//             protected void onCreate(Bundle savedInstanceState) {
//                 super.onCreate(savedInstanceState);
//                 //  setContentView(R.layout.activity_map);
//                 try {
//                     setContentView(R.layout.activity_map);
//                 } catch (Exception e) {
//                     Toast.makeText(map.this, e.toString(), Toast.LENGTH_LONG).show();
//                     MapFragment mapFragment = (MapFragment) getFragmentManager()
//                             .findFragmentById(R.id.mapview);
//                     //  mapFragment.getMapAsync(this);
//                 }
//             }
//                 @Override
//                 public void onMapReady (GoogleMap map){
//                     map.addMarker(new MarkerOptions()
//                             .position(new LatLng(0, 0))
//                             .title("Marker"));
//                     map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//                 }
//                 @Override
//                 public void onMapClick (LatLng latLng){
//                     Toast.makeText(getApplicationContext(), "" + latLng.latitude, Toast.LENGTH_LONG).show();
//                     Toast.makeText(getApplicationContext(), "" + latLng.longitude, Toast.LENGTH_LONG).show();
//                 }
//
//         }
