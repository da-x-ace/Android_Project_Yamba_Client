package com.cisco.prankuryamba;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;


/**
 * Created by prankurgupta on 8/20/14.
 */
public class TimelineProvider extends ContentProvider{

    private static final String TAG = "prankgup.yamba." + TimelineProvider.class.getSimpleName();
    private static final int STATUS_DIR = 1;
    private static final int STATUS_ITEM = 2;
    private TimelineHelper timelineHelper;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(TimelineContract.AUTHORITY, TimelineContract.PATH.substring(1),
                           STATUS_DIR);
        URI_MATCHER.addURI(TimelineContract.AUTHORITY, TimelineContract.PATH.substring(1)
                           + "/#", STATUS_ITEM);
    }



    @Override
    public boolean onCreate() {
        //Here we connect to database
        timelineHelper = new TimelineHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = timelineHelper.getReadableDatabase();

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TimelineHelper.TABLE);

        Cursor c = qb.query(db, projection, selection,selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert "+ uri + " values: "+ values);
        if(URI_MATCHER.match(uri) != STATUS_DIR){
            throw new IllegalArgumentException("Unsupported URI "+ uri);
        }
        Uri result = null;
        SQLiteDatabase db = timelineHelper.getWritableDatabase();
        long rowId = db.insert(TimelineHelper.TABLE, null, values);

        if (rowId >= 0){
            result = ContentUris.withAppendedId(TimelineContract.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
            Log.e(TAG, "Unable to insert " + values);
        }


        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}