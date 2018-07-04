package com.example.shubham.notifyme;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback ,GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private String actionBarTitle;
    DbInteract dbi;
      double lat;
    double lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Drawable d=getResources().getDrawable(R.drawable.colored);
        getSupportActionBar().setBackgroundDrawable(d);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toast.makeText(this,"Tap on your location to set it as your Geofence",Toast.LENGTH_LONG).show();
        setActionBarSubTitle("No location selected");
dbi=new DbInteract(this);

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
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(final LatLng latLng) {
       lat=latLng.latitude;
       lon=latLng.longitude;
        Log.d("here", "onMapClick: "+latLng.toString());
        AlertDialog.Builder resultAlert = new AlertDialog.Builder(this);

resultAlert.setMessage("You selected "+latLng);
        resultAlert.setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.addCard(latLng.toString());
                addDataBase();
               MainActivity.addGeoFencelist(lat,lon);
            }
        });
        Log.d("here", "onMapClick: ");
        resultAlert.show();


    }
public void addDataBase(){
    dbi.insertNewlocation(lat,lon);


}
    public void setActionBarSubTitle(String actionBarSubTitle) {
        getSupportActionBar().setSubtitle(actionBarSubTitle);;
    }
}
