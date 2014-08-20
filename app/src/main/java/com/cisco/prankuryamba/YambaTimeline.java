package com.cisco.prankuryamba;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
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
                for (YambaClient.Status post : postList) {
                    Log.d(TAG, "Post = " + post.getMessage());
                }

            } catch (YambaClientException e) {
                Log.e(TAG, "error while fetching timeline" + e);

            }

        }
    }


}
