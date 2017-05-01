package com.bitjunkie.smartdialer;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 *
 * FILE NAME: Tab3Contacts.java
 *
 * DESCRIPTION: This java file handles the functionality
 * for the Contacts tab of the Smart Dialer application.
 * This tab contains a tab for each contact stored by the user,
 * which includes their phone number and information collected
 * by the White Pages API.
 *
 *
 *
 *   DATE      BY         DESCRIPTION
 * ========   =====       ============
 * 4/4/2017   Omar Q.     Created the class
 * 4/25/2017 Patrick R.   Finished the class
 */

public class Tab3Contacts extends Fragment{

    LinearLayout contactList;
    ArrayList<LinearLayout> contactItems;
    ArrayList<String> contactIDs;
    boolean bIncoming = true;
    boolean bOutgoing = true;
    boolean bMissed = true;

    /**
     * called when the fragment is instantiated.  Not used
     * @param savedInstanceState - Latest saved paused state
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    /**
     * Called when the fragment view is created.  Contact List arrays
     * are initialized.
     * @param inflater - Inflates the layout
     * @param container - The layout container this fragment belongs to
     * @param savedInstanceState - Saved state of fragment before pause
     * @return returns the View to the Main Activity
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3contacts, container, false);
        contactList = (LinearLayout) rootView.findViewById(R.id.contactList);
        contactItems = new ArrayList<>();
        contactIDs = new ArrayList<>();
        getContactDetails();
        FloatingActionButton addContactButton = (FloatingActionButton) rootView.findViewById(R.id.fbAddContact);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEditContactActivity.class);
                intent.putExtra("type","add");
                startActivity(intent);

            }
        });
        return rootView;
    }
    //Some permissions variables
    int MY_PERMISSION_REQUEST_READ_CONTACTS = 2;
    int MY_PERMISSION_REQUEST_WRITE_CONTACTS = 3;
    int MY_PERMISSION_REQUEST_CALL_PHONE = 4;

    /**
     * This method pulls the needed info from the built-in Android contacts
     * database.  Firstly it asks for the appropriate permissions
     */
    public void getContactDetails() {
        if( ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS},MY_PERMISSION_REQUEST_READ_CONTACTS);
            if( ContextCompat.checkSelfPermission(this.getActivity(),
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        if( ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_CONTACTS},MY_PERMISSION_REQUEST_WRITE_CONTACTS);
            if( ContextCompat.checkSelfPermission(this.getActivity(),
                    Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,ContactsContract.Contacts.DISPLAY_NAME + " DESC");
        if(managedCursor != null) {
            int nameColumn = managedCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int photoUriColumn = managedCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI);
            int idColumn = managedCursor.getColumnIndex(ContactsContract.Contacts._ID);
            while (managedCursor.moveToNext()) {
                String name = managedCursor.getString(nameColumn);
                String id = managedCursor.getString(idColumn);
                Cursor managedCursor2 = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                String number = "";
                if (managedCursor2 != null) {
                    if (managedCursor2.moveToFirst()) {
                        int numberColumn = managedCursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        number = managedCursor2.getString(numberColumn);
                    }
                }
                managedCursor2 = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                String email = "";
                if (managedCursor2 != null) {
                    if (managedCursor2.moveToFirst()) {
                        int emailColumn = managedCursor2.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                        email = managedCursor2.getString(emailColumn);
                    }
                }
                managedCursor2 = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?", new String[]{id}, null);
                String address = "";
                if (managedCursor2 != null) {
                    if (managedCursor2.moveToFirst()) {
                        int addressColumn = managedCursor2.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS);
                        address = managedCursor2.getString(addressColumn);
                    }
                    managedCursor2.close();
                }

                String photoUri = managedCursor.getString(photoUriColumn);
                createContactItem(number, name, photoUri, email, address, id);

            }
            managedCursor.close();
        }
    }

    /**
     * Programatically adds the contact layout items to the scroll view
     * @param phNumber - Phone number used for dialing a number
     * @param name - Name to display
     * @param photoURI - Photo URI to display image
     */
    public void createContactItem(String phNumber, String name, String photoURI, String email, String address, String id){
        final String number = phNumber;
        String contactName = name;
        LinearLayout linBase = new LinearLayout(getActivity());
        ImageView imgPhoto = new ImageView(getActivity());
        TextView txtName = new TextView(getActivity());
        txtName.setText(name);
        imgPhoto.setImageResource(R.drawable.default_photo);
        if(photoURI != null) {
            imgPhoto.setImageURI(Uri.parse(photoURI));
        }
        linBase.addView(imgPhoto);
        linBase.addView(txtName);
        linBase.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams loparams = (LinearLayout.LayoutParams) imgPhoto.getLayoutParams();
        loparams.width = 0;
        loparams.weight = 0.15f;
        loparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        loparams = (LinearLayout.LayoutParams) txtName.getLayoutParams();
        loparams.width = 0;
        loparams.weight = 0.85f;
        loparams.height = ViewGroup.LayoutParams.MATCH_PARENT;

        loparams = (LinearLayout.LayoutParams) imgPhoto.getLayoutParams();
        imgPhoto.getLayoutParams().height = dpToPx(48);
        loparams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        //linTimeInfo.getLayoutParams().width = dpToPx(256);

        //View v = LayoutInflater.from(this.getActivity()).inflate(R.layout.calllog, null);
        contactList.addView(linBase,0);
        linBase.getLayoutParams().height = dpToPx(64);
        linBase.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        linBase.setClickable(true);
        linBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TabHost host = (TabHost) getActivity().findViewById(android.R.id.tabhost);
                //host.setCurrentTab(0);
                String permission = "android.permission.CALL_PHONE";

                int res = getActivity().getApplicationContext().checkCallingOrSelfPermission(permission);
                if (res == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number.trim())));
                }
                else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSION_REQUEST_CALL_PHONE);
                    if (res == PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number.trim())));
                    }
                }
            }
        });
        final String passName = name;
        final String passPhoto= photoURI;
        final String passNumber = phNumber;
        final String passEmail = email;
        final String passAddress = address;
        final String passId = id;
        linBase.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), AddEditContactActivity.class);
                intent.putExtra("type","edit");
                intent.putExtra("id",passId);
                intent.putExtra("name",passName);
                intent.putExtra("photoURI",passPhoto);
                intent.putExtra("number",passNumber);
                intent.putExtra("email",passEmail);
                intent.putExtra("address",passAddress);
                startActivity(intent);
                return false;
            }
        });
    }

    /**
     * called when fragment is not visible, removes all the contacft items
     */
    @Override
    public void onPause() {
        super.onPause();
        contactList.removeAllViews();
    }

    /**
     * Not used yet, hopefully will be able to use when
     * we figure out when to better remove all views
     */
    public void onUpdated() {
        contactList.removeAllViews();
        getContactDetails();
    }

    /**
     * called when the fragment is visible again
     */
    @Override
    public void onResume() {
        super.onResume();
        getContactDetails();

    }
    /**
     * DpToPx Used to convert DP measurements to pixel measurements
     * @param dp - dp to convert
     * @return returns the pixel measurement
     */
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
