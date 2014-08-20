package com.cisco.prankuryamba;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import static com.cisco.prankuryamba.TimelineContract.Columns.*;

/**
 * Created by prankurgupta on 8/20/14.
 */
public class TimelineHelper extends SQLiteOpenHelper{

    private static final String TAG = "prankgup.yamba." + TimelineHelper.class.getSimpleName();
    public static final String TABLE = "timeline";
    public static final int VERSION = 1;

    public TimelineHelper(Context context) {

        super(context, TABLE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s (%s INT PRIMARY KEY, " +
                                   " %s INT, %s TEXT, %s TEXT, %s INT, %s INT);",
                                   TABLE, ID, TIME_CREATED,USER, MESSAGE, LAT, LON);
        Log.d(TAG, "onCreate "+ sql);
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //If you change the version in build.gradle of app file. Handle it.
    }
}
