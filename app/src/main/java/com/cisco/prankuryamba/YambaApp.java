package com.cisco.prankuryamba;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;

/**
 * Created by prankurgupta on 8/19/14.
 */
public class YambaApp extends Application {

    private static final String TAG = "prankgup.yamba." + YambaApp.class.getSimpleName();
    private YambaClient yambaClient;
    private static YambaApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        instance = this;
    }

    public YambaClient getYambaClient(){
        if (yambaClient == null){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String username = prefs.getString("username", null);
            String password = prefs.getString("password", null);
            if (username == null || password == null ||
                    username.length() == 0 || password.length() == 0){
                Log.e(TAG, "No username or Password set");
                return null;
            }
            yambaClient = new YambaClient(username, password);
        }
        return yambaClient;
    }
    
    
    public static YambaApp getInstance(){
        return instance;
    }
}
