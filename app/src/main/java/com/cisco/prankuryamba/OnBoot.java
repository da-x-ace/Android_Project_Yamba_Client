package com.cisco.prankuryamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by prankurgupta on 8/20/14.
 */
public class OnBoot extends BroadcastReceiver{

    private static final String TAG = "prankgup.yamba." + OnBoot.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        //It will call YambaApp.oncreate function after rebooting
        //Need a receiver entry in AndroidManifest.xml
        Log.d(TAG, "onReceive");
    }
}
