package com.bitjunkie.smartdialer;

/**
 * Created by Omar on 4/4/2017.
 */


//import android.support.v4.app.Fragment;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Date;
import android.database.Cursor;
import android.provider.CallLog;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import android.content.ContentResolver;

import static android.R.attr.gravity;
import static android.text.InputType.TYPE_CLASS_PHONE;



Public class Tab3Contacts extends ListActivity {
    private static final int CONTACT_CREATE = 0;
    private static final int CONTACT_EDIT = 1;

    //select the second one, Android view menu
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private DBHelper dbHelper;
    private Cursor c;

    
public class Tab3Contacts extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3contacts, container, false);
        return rootView;
    }

    LinearLayout contactList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3contacts, container, false);
        contactList = (LinearLayout) rootView.findViewById(R.id.contactList);

        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        getCallDetails();
    }
    public void getCallDetails() {

        // Call list retrieval example from Stack Overflow
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Toast.makeText(NativeContentProvider.this, "Name: " + name
                                + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
                    }
                    pCur.close();
                }
            }
        }

        // Pat's code
        if( ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            System.exit(0);
        }
        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        while (managedCursor.moveToNext()) {
            //View v = LayoutInflater.from(this.getActivity()).inflate(R.layout.calllog, null);
            //contactList.addView(v);

            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "Outgoing";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "Incoming";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "Missed";
                    break;
            }
            createLogItem(phNumber,callType,callDayTime,callDuration,dir);
        }
    }
    //Programmatically add call log item
    public void createLogItem(String phNumber, String callType, Date callDayTime, String callDuration, String dir){
        LinearLayout linBase = new LinearLayout(getActivity());
        ImageView imgPhoto = new ImageView(getActivity());
        LinearLayout linCaller = new LinearLayout(getActivity());
        LinearLayout linTimeInfo = new LinearLayout(getActivity());
        TextView txtName = new TextView(getActivity());
        TextView txtNumber = new TextView(getActivity());
        TextView txtState = new TextView(getActivity());
        TextView txtTime = new TextView(getActivity());

        String callDate = Integer.toString(callDayTime.getDay()) + "/" + Integer.toString(callDayTime.getMonth()) + "/" + Integer.toString(callDayTime.getYear()).substring(1,3);
        txtName.setText("Test");
        txtNumber.setInputType(TYPE_CLASS_PHONE);
        txtNumber.setText(phNumber);
        txtState.setText(dir);

        txtTime.setText(callDate);
        imgPhoto.setImageResource(R.drawable.default_photo);


        linBase.addView(imgPhoto);
        linBase.addView(linCaller);
        linBase.addView(linTimeInfo);
        linCaller.addView(txtName);
        linCaller.addView(txtNumber);
        linTimeInfo.addView(txtState);
        linTimeInfo.addView(txtTime);

        linBase.setOrientation(LinearLayout.HORIZONTAL);
        linCaller.setOrientation(LinearLayout.VERTICAL);
        linTimeInfo.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams loparams = (LinearLayout.LayoutParams) imgPhoto.getLayoutParams();
        loparams.width = 0;
        loparams.weight = 0.15f;
        loparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        loparams = (LinearLayout.LayoutParams) linCaller.getLayoutParams();
        loparams.width = 0;
        loparams.weight = 0.50f;
        loparams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        loparams = (LinearLayout.LayoutParams) linTimeInfo.getLayoutParams();
        loparams.width = 0;
        loparams.weight = 0.35f;
        loparams.height = ViewGroup.LayoutParams.MATCH_PARENT;

        loparams = (LinearLayout.LayoutParams) txtState.getLayoutParams();
        loparams.gravity = Gravity.RIGHT;
        loparams = (LinearLayout.LayoutParams) txtTime.getLayoutParams();
        loparams.gravity = Gravity.RIGHT;

        loparams = (LinearLayout.LayoutParams) imgPhoto.getLayoutParams();
        imgPhoto.getLayoutParams().height = dpToPx(48);
        loparams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        //linTimeInfo.getLayoutParams().width = dpToPx(256);

        //View v = LayoutInflater.from(this.getActivity()).inflate(R.layout.calllog, null);
        contactList.addView(linBase);
        linBase.getLayoutParams().height = dpToPx(64);
        linBase.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        //LayoutInflater.from(getActivity()).inflate(linBase, null);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
