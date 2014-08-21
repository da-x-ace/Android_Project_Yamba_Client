package com.cisco.prankuryamba;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;


public class statusYamba extends BaseYambaActivity implements TextWatcher {

    private static final String TAG = "prankgup.yamba." + statusYamba.class.getSimpleName();
    private static final int NUM_CHARACTER_ALLOWED = 140;
    private TextView textViewNumLeft;
    private Button buttonPostStatus;
    private EditText editTextStatusMessage;
    private boolean loggedIn;

    private class PostTask extends AsyncTask<String, Integer, Long> {

        @Override
        protected Long doInBackground(String... messages) {
            Log.d(TAG, "doInBackground");
            long start = System.currentTimeMillis();
            for (String message : messages) {
                Log.d(TAG, "sending message " + message);
                YambaClient client = new YambaClient("student", "password");
                try {
                    client.postStatus(message);
                    //Thread.sleep(10000);
                } catch (YambaClientException e) {
                    Log.e(TAG, "error while sending " + message, e);
                } //catch (InterruptedException e) {
                    //e.printStackTrace();
                    //Log.wtf(TAG, "thread error");
                //}
            }
            return System.currentTimeMillis() - start;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            Log.d(TAG, "onPostExecute");
            AlertDialog.Builder builder = new AlertDialog.Builder(statusYamba.this);
            builder.setTitle("Posted Message").setMessage("Took "+aLong+" ms");
            builder.create().show();
            //Clear the editTextStatusMessage
            editTextStatusMessage.getText().clear();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_status_yamba);
        //final EditText editTextStatusMessage = (EditText) findViewById(R.id.editTextStatusMessage);
        editTextStatusMessage = (EditText) findViewById(R.id.editTextStatusMessage);
        editTextStatusMessage.addTextChangedListener(this);
        textViewNumLeft = (TextView) findViewById(R.id.textViewNumLeft);
        buttonPostStatus = (Button) findViewById(R.id.buttonPostStatus);
        buttonPostStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextStatusMessage.getText().toString();
                /*
                // Bad method, creating new thread everytime you post.
                // When you change the layout [from portrait to landscape
                // Then the activity destroys and then starts, so your thread
                // ALso gets destroyed and it may result into unexplainable
                // Results
                PostTask postTask = new PostTask();
                postTask.execute(message);
                */
                Log.d(TAG, "sending message "+ message);
                Intent postIntent = new Intent(statusYamba.this, postMessageService.class);
                postIntent.putExtra("message", message);
                startService(postIntent);

                /*
                //This is not at all recommened and will fail
                //User are not supposed to send messages in that thread itself
                YambaClient client = new YambaClient("student", "password");
                try {
                    client.postStatus(message);
                } catch (YambaClientException e) {
                    Log.e(TAG, "error while sending "+ message, e);
                    //e.printStackTrace();
                }
                */
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String username = prefs.getString("username", null);
        String password = prefs.getString("password", null);

        loggedIn = (username != null && password != null);
        buttonPostStatus.setEnabled(loggedIn);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //Not needed
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //Not needed
    }

    @Override
    public void afterTextChanged(Editable s) {
        int num_left = NUM_CHARACTER_ALLOWED - s.length();
        ColorStateList colorStateList = null;
        //Log.d(TAG, "afterTextChanged " + s.length());
        textViewNumLeft.setText(num_left+ "");
        buttonPostStatus.setEnabled(loggedIn && num_left >= 0);
        if (num_left <= 10){
            textViewNumLeft.setTextColor(getResources().getColor(R.color.error));
            if (num_left < 0) {
                textViewNumLeft.setText(0+"");
            }
        } else {
            textViewNumLeft.setTextColor(getResources().getColor(R.color.valid));
        }
    }
}
