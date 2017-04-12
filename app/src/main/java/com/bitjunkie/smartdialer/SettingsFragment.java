package com.bitjunkie.smartdialer;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Patrick on 4/11/2017.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}