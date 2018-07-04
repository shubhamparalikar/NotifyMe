package com.example.shubham.notifyme;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RemoveClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {
android.support.v7.app.ActionBar ab;
    private RecyclerView mRecyclerView;
    private static RecyclerAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    static ArrayList<RecyclerData> myList = new ArrayList<>();
    GoogleApiClient obj;
    static List<Geofence> geofencelist=new ArrayList<Geofence>();
    PendingIntent mGeofencePendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("here", "onCreate of mainactivity");
        Drawable d=getResources().getDrawable(R.drawable.colored);
        getSupportActionBar().setBackgroundDrawable(d);
        ab=getSupportActionBar();
        setActionBarSubTitle("No location selected");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.appicon);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerAdapter = new RecyclerAdapter(myList,this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        obj=new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
            obj.connect();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==R.id.addIcon){
            Intent i=new Intent(MainActivity.this,MapsActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);

    }
    public void setActionBarSubTitle(String actionBarSubTitle) {
        getSupportActionBar().setSubtitle(actionBarSubTitle);;
    }


    @Override
    public void OnRemoveClick(int index) {
        myList.remove(index);
        mRecyclerAdapter.notifyData(myList);
    }

    public static void addCard(String s) {
        RecyclerData mLog = new RecyclerData();
        mLog.setTitle(s);


        myList.add(mLog);
        mRecyclerAdapter.notifyData(myList);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("here", "onConnected: ");
        try {
            if (!geofencelist.isEmpty()) {
                Log.d("here", "onConnected: calling request");
                LocationServices.GeofencingApi.addGeofences(obj, createGeofencingRequest(), getGeoFencePendingIntent()).setResultCallback(this);
            }
            else
                Log.d("here", "onConnected: geofencelist is null");
        }
        catch (SecurityException ex){
            Log.d("here", "onConnected: security exception"+ex);
        }
    }

public GeofencingRequest createGeofencingRequest(){
    Log.d("here", "createGeofencingRequest: ");
    GeofencingRequest.Builder builder=new GeofencingRequest.Builder();
    builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER|GeofencingRequest.INITIAL_TRIGGER_EXIT);
    builder.addGeofences(getGeoFenceList());
    if (!geofencelist.isEmpty()) {
        return builder.build();
    }

    return null;

}

    public List getGeoFenceList(){
        if(geofencelist!=null) {
            return geofencelist;
        }
        Toast.makeText(this,"no fences added",Toast.LENGTH_SHORT).show();
        return null;
    }
    public static void addGeoFencelist(double lat,double lon){
String id= String.valueOf(geofencelist.size());
        Log.d("here", "id is  "+id);
        Log.d("here", "in createGeoFencelist: ");
        geofencelist.add(new Geofence.Builder().setRequestId(id)
                .setCircularRegion(lat ,lon,100).setLoiteringDelay(10000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER|Geofence.GEOFENCE_TRANSITION_EXIT).build());

        Log.d("here", "creategeoFencelist: "+geofencelist);



    }

    public PendingIntent getGeoFencePendingIntent(){
        Log.d("here", "getGeoFencePendingIntent: ");
        if(mGeofencePendingIntent!=null){
            Log.d("here", "getGeoFencePendingIntent: "+mGeofencePendingIntent);
            return mGeofencePendingIntent;
        }
        Log.d("here", "getGeoFencePendingIntent: intent created "+mGeofencePendingIntent);
        Intent intent = new Intent(this, GeofenceIntentService.class);
        Log.d("here", "getGeoFencePendingIntent: "+intent);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("here", "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("here", "onConnectionFailed: ");
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.d("here", "onResult: "+status.toString());

        Toast.makeText(this,"onResult() called with data:"+status.getStatus().toString(), Toast.LENGTH_LONG).show();

    }

}
