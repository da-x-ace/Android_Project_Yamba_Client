package com.cisco.prankuryamba;

import android.app.ActionBar;
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
    public static final String SELECTED_TAB = "selectedTab";
    private int selectedTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //Not needed when used with tabs, because it will result in 2
        //Copies of layout on each other.
        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.replace(R.id.fragment_container, new TimelineFragment(), "timeline");
        View fragmentDetails = findViewById(R.id.fragment_details);
        if (fragmentDetails != null){
            detailsFragment = new TimelineDetailsFragment();
            //Comment it when used without tabs
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            //Comment it when used without tabs
            ft.replace(R.id.fragment_details, detailsFragment);
            ft.commit();
        }
        //Comment when used with tabs
        //ft.commit();

        //Example to show values can be passes through intents
        if(getIntent().getBooleanExtra("fromNotification", false)) {
            Log.d(TAG, "From Notification");
        }

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        if (savedInstanceState != null){
            selectedTab = savedInstanceState.getInt(SELECTED_TAB, 0);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        ActionBar actionBar = getActionBar();
        ActionBar.Tab tab = actionBar.newTab();
        tab.setText("Timeline");
        tab.setTabListener(new TabListener<TimelineFragment>(this, "timeline", TimelineFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab();
        tab.setText("Status");
        tab.setTabListener(new TabListener<StatusFragment>(this, "status", StatusFragment.class));
        actionBar.addTab(tab);
        if (selectedTab > -1){
            actionBar.setSelectedNavigationItem(selectedTab);
        }
    }

    @Override
    protected void onPause() {
        getActionBar().removeAllTabs();
        super.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int idx = getActionBar().getSelectedNavigationIndex();
        outState.putInt(SELECTED_TAB, idx);
        super.onSaveInstanceState(outState);
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
