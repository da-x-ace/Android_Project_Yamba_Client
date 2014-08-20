package com.cisco.prankuryamba;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class YambaTimeline extends IntentService {


    private static final String TAG = "prankgup.yamba." + YambaTimeline.class.getSimpleName();

    public YambaTimeline() {
        super("YambaTimeline");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onCreate");
        YambaClient client = YambaApp.getInstance().getYambaClient();

        if (client != null){
            try {
                List<YambaClient.Status> postList = client.getTimeline(10);
                //Needs a provider entry in AndroidManifest.xml
                //For any SQL functionality you need a ContentResolver
                final ContentResolver resolver  = getContentResolver();
                Cursor c = resolver.query(TimelineContract.CONTENT_URI, TimelineContract.MAX_TIME_CREATED,
                                          null, null, null);
                final long maxTime = c.moveToFirst() ? c.getLong(0) : Long.MIN_VALUE;

                for (YambaClient.Status post : postList) {
                    Log.d(TAG, "Post = " + post.getMessage());
                    long time = post.getCreatedAt().getTime();
                    if (time > maxTime){
                        ContentValues values = new ContentValues();
                        values.put(TimelineContract.Columns.MESSAGE, post.getMessage());
                        values.put(TimelineContract.Columns.TIME_CREATED, time);
                        values.put(TimelineContract.Columns.USER, post.getUser());
                        values.put(TimelineContract.Columns.ID, post.getId());
                        resolver.insert(TimelineContract.CONTENT_URI, values);
                    }
                }

            } catch (YambaClientException e) {
                Log.e(TAG, "error while fetching timeline" + e);

            }

        }
    }


}
