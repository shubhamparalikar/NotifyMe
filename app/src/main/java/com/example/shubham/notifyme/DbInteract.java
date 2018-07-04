package com.example.shubham.notifyme;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by shubham on 9/8/2016.
 */
public class DbInteract extends SQLiteOpenHelper {
    private Context ct;
    private static  String DB_NAME="GeoFenceDetails";
    private static  int DB_VERSION=1;

    public DbInteract(Context context) {
        super(context, DB_NAME  , null, DB_VERSION);

    }




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String str="create table geofencedetails(lat DOUBLE, long DOUBLE, bluetooth TEXT,Wifi TEXT,profile TEXT)";
        sqLiteDatabase.execSQL(str);
        Log.d("here", "onCreate: table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String str="drop table geofencedetails";
        sqLiteDatabase.execSQL(str);
        onCreate(sqLiteDatabase);

    }
    public void insertNewlocation(double lat,double lon){

        SQLiteDatabase db=super.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put("lat",lat);
        cv.put("long",lon);
        long res=db.insert("geofencedetails",null,cv);
        if(res!=-1)
            Log.d("here", "insertNewlocation: rec inserted ");
        else
            Log.d("here", "insertNewlocation: rec NOT inserted ");

        db.close();





    }
}
