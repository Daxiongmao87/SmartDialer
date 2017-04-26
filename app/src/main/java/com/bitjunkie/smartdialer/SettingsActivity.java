package com.bitjunkie.smartdialer;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 *
 * FILE NAME: SettingsActivity.java
 *
 * DESCRIPTION: This java file handles the functionality for
 * the Settings page.   This specifically runs the SettingsFragment
 * class
 *
 *   DATE       BY      DESCRIPTION
 * ======== ========== =============
 * 4/11/2017 Patrick R. Created the class
 * 4/17/2017 Patrick R. Finished the class
 */

public class SettingsActivity extends Activity {
    /**
     * this Method establishes the relationship between the activity and fragment
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}

