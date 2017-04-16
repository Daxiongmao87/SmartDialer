package com.bitjunkie.smartdialer;

/**
 * Created by Omar on 4/4/2017.
 */

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

import static android.R.attr.gravity;
import static android.text.InputType.TYPE_CLASS_PHONE;

public class Tab2Log extends Fragment{
    LinearLayout logList;
    LinearLayout logComponentPrefab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2log, container, false);
        logList = (LinearLayout) rootView.findViewById(R.id.logList);
        logComponentPrefab = (LinearLayout) rootView.findViewById(R.id.logComponentPrefab);

        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        getCallDetails();
    }
    public void getCallDetails() {
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
            //logList.addView(v);

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
        logList.addView(linBase);
        linBase.getLayoutParams().height = dpToPx(64);
        linBase.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        //LayoutInflater.from(getActivity()).inflate(linBase, null);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}

