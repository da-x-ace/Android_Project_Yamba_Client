package com.cisco.prankuryamba;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by prankurgupta on 8/19/14.
 */
public class YambaPrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
