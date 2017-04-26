package com.bitjunkie.smartdialer;


import android.Manifest;

import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.support.design.widget.TabLayout;

import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 *
 * FILE NAME: MainActiviity.java
 *
 * DESCRIPTION: This java file is the Main Activity, and
 * handles all the layouts and fragments.  Anything that needs
 * to be instantiated before anything else is also placed here.
 *
 *   DATE       BY      DESCRIPTION
 * ======== ========== =============
 * 4/4/2017 Omar Q.     Created the class
 * 4/23/2017 Patrick R. Finished the class
 */

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_READ_CALL_LOG = 0;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Context context;

    /**
     * Sets up the PagerAdapter (layout used for the tabulation feature for the
     * app.  Reverse Lookup service is also started here
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.context;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //request Permissions
        String[] permissions = new String[1];
        permissions[0] = Manifest.permission.READ_CALL_LOG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CALL_LOG},MY_PERMISSIONS_READ_CALL_LOG);
        }
        //start reverse lookup service
        startService(new Intent(getBaseContext(), ReverseLookup.class));
    }

    /**
     * Method that handles permission requests.  Not used as much as planned, may utilize
     * it more after refactoring and reorganizing code.
     * @param requestCode - the code of the permission to request
     * @param permissions - the string of permissions (Not Used)
     * @param grantResults - string of results (Not Used)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) {
            case MY_PERMISSIONS_READ_CALL_LOG: {
                return;
            }
        }
    }

    /**
     * Handles the options menu (three dots on top right)
     * @param menu the menu item
     * @return returns true if there are no errors (?)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handles when an item from the options menu is selected
     * @param item - The item selected
     * @return returns true if an item is successfully selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Dummy method for buttonclicks
     * @param v - the related View item
     * @return - always true
     */
    public boolean buttonClickEvent(View v) {
       return true;
    }

    /**
     * This class handles the behaviorof the Pager Adapter,
     * links relevant fragments for viewing.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
           switch(position) {
               case 0:
                   Fragment frag = new Tab1Dialer();
                   return frag;
               case 1:
                   frag = new Tab2Log();
                   return frag;
               default:
                   frag = new Tab3Contacts();
                   return frag;

           }
        }

        /**
         * This method just returns the total pages
         * @return
         */
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        /**
         * This method returns the title of the page (fragment)
         * to be used on the layout
         * @param position - the page position
         * @return - the page title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "DIALER";
                case 1:
                    return "LOG";
                case 2:
                    return "CONTACTS";
            }
            return null;
        }
    }
}
