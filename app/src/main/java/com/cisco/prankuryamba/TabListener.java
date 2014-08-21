package com.cisco.prankuryamba;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;

/**
 * Created by prankurgupta on 8/21/14.
 */
public class TabListener<T extends Fragment> implements ActionBar.TabListener  {
    private static final String TAG = "prankgup.yamba." + TabListener.class.getSimpleName();
    private Activity activity;
    private String tag;
    private Class<T> clazz;
    private Fragment fragment;

    public TabListener(Activity activity, String tag, Class<T> clz) {
        Log.d(TAG, "TabListener " + tag);
        this.activity = activity;
        this.tag = tag;
        this.clazz = clz;
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        Log.d(TAG, "onTabSelected " + tab.getText());
        if(this.fragment == null) {
            fragment = Fragment.instantiate(activity, clazz.getName());
            ft.add(R.id.fragment_container, fragment, tag);
        }
        else {
            ft.attach(fragment);
        }
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        Log.d(TAG, "onTabUnselected " + tab.getText());
        if(fragment != null) {
            ft.detach(fragment);
        }
    }

}
