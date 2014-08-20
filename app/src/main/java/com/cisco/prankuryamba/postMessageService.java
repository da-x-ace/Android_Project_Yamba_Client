package com.cisco.prankuryamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class postMessageService extends IntentService {

    private static final String TAG = "prankgup.yamba." + postMessageService.class.getSimpleName();
    public static final int NOTIFICATION_MESSAGE_POSTED = 100;
    private static final int NOTIFICATION_PASSWORD_ERROR = 101;

    public postMessageService() {
        super("postMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleEvent");

        String message = intent.getExtras().getString("message");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        YambaClient client = YambaApp.getInstance().getYambaClient();


        if (client == null){
            notificationManager.cancel(NOTIFICATION_MESSAGE_POSTED);
            Log.w(TAG, "Login Credentials missing");
            Notification.Builder notification = new Notification.Builder(this);
            notification.setSmallIcon(R.drawable.ic_launcher);
            notification.setContentTitle("Yamba");
            notification.setContentText("Username or Password not set");
            notificationManager.notify(NOTIFICATION_PASSWORD_ERROR, notification.getNotification());

        } else {
            notificationManager.cancel(NOTIFICATION_PASSWORD_ERROR);
            try {
                client.postStatus(message);
                Notification.Builder notification = new Notification.Builder(this);
                notification.setSmallIcon(R.drawable.ic_launcher);
                notification.setContentTitle("Yamba");
                notification.setContentText("Posted " + message);
                Intent pIntent = new Intent(this, statusYamba.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, pIntent, 0);
                notification.setContentIntent(pendingIntent);
                notificationManager.notify(NOTIFICATION_MESSAGE_POSTED, notification.getNotification());
            } catch (YambaClientException e) {
                Log.e(TAG, "error while sending " + message, e);
                //e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }
}
