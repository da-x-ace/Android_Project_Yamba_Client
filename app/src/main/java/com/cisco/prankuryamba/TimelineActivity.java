package com.cisco.prankuryamba;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cisco.prankuryamba.R;

public class TimelineActivity extends BaseYambaActivity implements TimelineFragment.TimelineItemSelectionCallback{


    private static final String TAG = "prankgup.yamba." + TimelineActivity.class.getSimpleName();
    private TimelineDetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new TimelineFragment(), "timeline");
        View fragmentDetails = findViewById(R.id.fragment_details);
        if (fragmentDetails != null){
            detailsFragment = new TimelineDetailsFragment();
            ft.replace(R.id.fragment_details, detailsFragment);
        }
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.showPost:
                Intent statusIntent = new Intent(this, statusYamba.class);
                startActivity(statusIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimelineItemSelected(long id) {
        if(detailsFragment == null){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            TimelineDetailsFragment detailsFragment = new TimelineDetailsFragment();
            detailsFragment.setRowId(id);
            ft.replace(R.id.fragment_container, detailsFragment);
            ft.addToBackStack("TimelineDeatils "+ id);
            ft.commit();
        } else {
            detailsFragment.update(id);
        }

    }
}
