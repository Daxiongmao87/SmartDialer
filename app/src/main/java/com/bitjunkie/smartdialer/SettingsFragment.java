package com.bitjunkie.smartdialer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 *
 * FILE NAME: SettingsFragment.java
 *
 * DESCRIPTION: This java file handles the functionality for
 * the Settings fragment.  This associates the xml layout to the
 * settings activity.
 *
 *   DATE       BY      DESCRIPTION
 * ======== ========== =============
 * 4/11/2017 Patrick R. Created the class
 * 4/17/2017 Patrick R. Finished the class
 */

public class SettingsFragment extends PreferenceFragment {
    /**
     * Associates the xml preferences layout to the Settings
     * Activity
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
       // getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener();
    }
}