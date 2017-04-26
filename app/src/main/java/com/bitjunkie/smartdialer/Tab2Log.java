package com.bitjunkie.smartdialer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import android.database.Cursor;
import android.provider.CallLog;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONObject;

import static android.R.attr.gravity;
import static android.text.InputType.TYPE_CLASS_PHONE;

/**
 *
 * FILE NAME: Tab2Log.java
 *
 * DESCRIPTION: This java file handles the functionality for
 * the Call Log tab of the Smart Dialer application. This tab
 * stores and organizes all calls received and made by the user's
 * Android mobile device.
 *
 *   DATE       BY      DESCRIPTION
 * ======== ========== =============
 * 4/4/2017 Omar Q.     Created the class
 * 4/25/2017 Patrick R. Finished the class
 */

public class Tab2Log extends Fragment{
    LinearLayout logList;
    ArrayList<LinearLayout> logItems;
    ArrayList<String> logIDs;
    boolean bIncoming = true;
    boolean bOutgoing = true;
    boolean bMissed = true;
    DatabaseOperator dbo;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        dbo = new DatabaseOperator(getActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2log, container, false);
        logList = (LinearLayout) rootView.findViewById(R.id.logList);
        logItems = new ArrayList<>();
        logIDs = new ArrayList<>();
        ToggleButton btnOutgoing = (ToggleButton) rootView.findViewById(R.id.btnOutgoing);
        btnOutgoing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bOutgoing = isChecked;
                FilterLog();
            }
        });
        ToggleButton btnIncoming = (ToggleButton) rootView.findViewById(R.id.btnIncoming);
        btnIncoming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bIncoming = isChecked;
                FilterLog();
            }
        });
        ToggleButton btnMissed = (ToggleButton) rootView.findViewById(R.id.btnMissed);
        btnMissed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bMissed = isChecked;
                FilterLog();
            }
        });
        //Check if our ListedNumbers database exists, if not, create it

        //SQLiteDatabase db = dbo.getWritableDatabase();
        //getContactDetails(db);
        return rootView;
    }
    int MY_PERMISSION_REQUEST_READ_CALL_LOG = 1;
    int MY_PERMISSION_REQUEST_CALL_PHONE = 4;
    public void getCallDetails(SQLiteDatabase db) {
        if( ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CALL_LOG},MY_PERMISSION_REQUEST_READ_CALL_LOG);
            if( ContextCompat.checkSelfPermission(this.getActivity(),
                    Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int id = managedCursor.getColumnIndex(CallLog.Calls._ID);

        while (managedCursor.moveToNext()) {
            //View v = LayoutInflater.from(this.getActivity()).inflate(R.layout.calllog, null);
            //logList.addView(v);

            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            String call_id = managedCursor.getString(id);
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
            for(int i = 0; i < logIDs.size(); i++) {
                if(logIDs.get(i) == call_id) {
                    updateLogItem(logItems.get(i),db, phNumber,callType,callDayTime,callDuration,dir);
                    break;
                }
            }
            createLogItem(db, phNumber,callType,callDayTime,callDuration,dir, call_id);
        }
    }
    //IF the log item exists, update it
    public void updateLogItem(LinearLayout logItem, SQLiteDatabase dbase, String phNumber, String callType, Date callDayTime, String callDuration, String dir){
        final SQLiteDatabase db = dbase;
        final String number = phNumber;
        String contactName = FindContactName(getActivity(),phNumber);
        LinearLayout linBase = logItem;
        ImageView imgPhoto = (ImageView) linBase.getChildAt(0);
        LinearLayout linCaller = (LinearLayout) linBase.getChildAt(1);
        LinearLayout linTimeInfo = (LinearLayout) linBase.getChildAt(2);
        TextView txtName = (TextView) linCaller.getChildAt(0);
        TextView txtNumber = (TextView) linCaller.getChildAt(1);
        TextView txtState = (TextView) linTimeInfo.getChildAt(0);
        TextView txtTime = (TextView) linTimeInfo.getChildAt(1);
        String callDate = Integer.toString(callDayTime.getDay()) + "/" + Integer.toString(callDayTime.getMonth()) + "/" + Integer.toString(callDayTime.getYear()).substring(1,3);
        if(contactName == null) {
            //if contactName = null, check if its in our listednumbers database
            String[] projection = {
                    "name",
                    "address",
                    "city",
                    "zip",
                    "state",
                    "city"
            };
            String selection = "number = ?";
            String[] selectionArgs = { number };
            String sortOrder = "number DESC";
            Cursor cursor = db.query(
                    "LISTEDNUMBERS",
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
           // Log.e("TESTING","DB TEST: Checking " + number);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                txtName.setText(cursor.getString(0));
                //Log.e("TESTING","DB TEST: Found! " + number + ": " + cursor.getString(0));
            } else{
                txtName.setVisibility(View.GONE);
                //Log.e("TESTING","DB TEST: NOT FOUND :( " + number);
            }
            cursor.close();
            //txtName.setVisibility(View.GONE);
        }
        else {
            txtName.setText(contactName);
        }
        txtNumber.setInputType(TYPE_CLASS_PHONE);
        txtNumber.setText(phNumber);
        txtState.setText(dir);

        txtTime.setText(callDate);

        //imgPhoto.setImageURI(null);
        //imgPhoto.setImageDrawable(Drawable.createFromPath(photoUri.getPath()));
        imgPhoto.setImageResource(R.drawable.default_photo);
        String contactPhotoURI = FindContactPhoto(getActivity(),phNumber);
        if(contactPhotoURI != null) {
            imgPhoto.setImageURI(Uri.parse(contactPhotoURI));
        }
    }
    //Programmatically add call log item
    public void createLogItem(SQLiteDatabase dbase, String phNumber, String callType, Date callDayTime, String callDuration, String dir, String id){
        final SQLiteDatabase db = dbase;
        final String number = phNumber;
        String contactName = FindContactName(getActivity(),phNumber);
        LinearLayout linBase = new LinearLayout(getActivity());
        ImageView imgPhoto = new ImageView(getActivity());
        LinearLayout linCaller = new LinearLayout(getActivity());
        LinearLayout linTimeInfo = new LinearLayout(getActivity());
        TextView txtName = new TextView(getActivity());
        TextView txtNumber = new TextView(getActivity());
        TextView txtState = new TextView(getActivity());
        TextView txtTime = new TextView(getActivity());

        String callDate = Integer.toString(callDayTime.getDay()) + "/" + Integer.toString(callDayTime.getMonth()) + "/" + Integer.toString(callDayTime.getYear()).substring(1,3);
        if(contactName == null) {
            //if contactName = null, check if its in our listednumbers database
            String[] projection = {
                    "name",
                    "address",
                    "city",
                    "zip",
                    "state",
                    "city"
            };
            String selection = "number = ?";
            String[] selectionArgs = { number };
            String sortOrder = "number DESC";
            Cursor cursor = db.query(
                    "LISTEDNUMBERS",
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
            //\Log.e("TESTING","DB TEST: Checking " + number);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                txtName.setText(cursor.getString(0));
                //Log.e("TESTING","DB TEST: Found! " + number + ": " + cursor.getString(0));
            } else{
                txtName.setVisibility(View.GONE);
              //  Log.e("TESTING","DB TEST: NOT FOUND :( " + number);
            }
            cursor.close();
            //txtName.setVisibility(View.GONE);
        }
        else {
            txtName.setText(contactName);
        }
        txtNumber.setInputType(TYPE_CLASS_PHONE);
        txtNumber.setText(phNumber);
        txtState.setText(dir);

        txtTime.setText(callDate);

        //imgPhoto.setImageURI(null);
        //imgPhoto.setImageDrawable(Drawable.createFromPath(photoUri.getPath()));
        imgPhoto.setImageResource(R.drawable.default_photo);
        String contactPhotoURI = FindContactPhoto(getActivity(),phNumber);
        if(contactPhotoURI != null) {
            imgPhoto.setImageURI(Uri.parse(contactPhotoURI));
        }
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
        logList.addView(linBase,0);
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
                    Lookup lu = new Lookup(db,getActivity(),number);
                    lu.execute();
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
        logItems.add(linBase);
        logIDs.add(id);
        //LayoutInflater.from(getActivity()).inflate(linBase, null);
    }
    class Lookup extends AsyncTask<String, Integer, JSONObject> {
        private Context context;
        private String phonenumber;
        SQLiteDatabase db;
        public Lookup(SQLiteDatabase dbase, Context c, String pn) {
            context = c;
            phonenumber = pn;
            db = dbase;
        }
        protected JSONObject doInBackground(String... params){
            Log.e("NUMBER!",phonenumber);
            if(phonenumber !=null) {
                try {
                    String key = "d406a59b6c444b669f42f21d92306923";
                    String url = "https://proapi.whitepages.com/3.0/phone.json?api_key=" + key + "&phone=" + URLEncoder.encode(phonenumber,"UTF-8").toString();
                    JSONParser jsonParser = new JSONParser();
                    JSONObject payload = jsonParser.getJSONFromUrl(url, null);
                    Log.e("TESTING","TEST2: " + payload.getJSONArray("belongs_to").getJSONObject(0).getString("name"));
                    return payload;
                }
                catch (org.json.JSONException e) {
                    Log.e("JSON","JSON Error: " + e.toString());
                }
                catch (java.io.UnsupportedEncodingException e) {
                    Log.e("URL","Encoding Error: " + e.toString());
                }
            }
            return null;
        }
        protected void onPostExecute(JSONObject result) {
            Log.e("TESTJSON",result.toString());
            if(result != null) {

                try {
                    String name = result.getJSONArray("belongs_to").getJSONObject(0).getString("name");
                    String address = "";
                    String city = "";
                    String zip = "";
                    String state = "";
                    String country = "";
                    if (result.get("is_commercial").equals("true")) {
                        if (result.getJSONArray("belongs_to").getJSONObject(0).getString("location_type").equals("Address")) {
                            address = result.getJSONArray("belongs_to").getJSONObject(0).getString("street_line_1");
                            if (result.getJSONArray("belongs_to").getJSONObject(0).getString("street_line_2") != null) {
                                address += ", " + result.getJSONArray("belongs_to").getJSONObject(0).getString("street_line_2");
                                city = result.getJSONArray("belongs_to").getJSONObject(0).getString("city");
                                zip = result.getJSONArray("belongs_to").getJSONObject(0).getString("postal_code");
                                state = result.getJSONArray("belongs_to").getJSONObject(0).getString("state_code");
                                country = result.getJSONArray("belongs_to").getJSONObject(0).getString("countrey_code");
                            }
                        }
                    }
                    PopulateInfo(db, phonenumber, name, address, city, zip, state, country);
                } catch (org.json.JSONException e) {
                    Log.e("JSON", "JSON Error: " + e.toString());
                }
            }
        }
    }
    public void PopulateInfo(SQLiteDatabase db, String number,String name, String address, String city, String zip, String state, String country){

        ContentValues values = new ContentValues();
        values.put("number",number);
        values.put("name",name);
        values.put("address",address);
        values.put("city",city);
        values.put("zip",zip);
        values.put("state",state);
        values.put("country",country);
        db.insert("LISTEDNUMBERS",null,values);
        //db.close();
        ///dbo.close();
    }
    public static String FindContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode("tel:"+phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }

    @Override
    public void onPause() {
        super.onPause();
        logList.removeAllViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TESTONRESUME","RESUME");
        SQLiteDatabase db = dbo.getWritableDatabase();
        getCallDetails(db);
    }
    public void FilterLog() {
        for(int i = 0; i < logItems.size(); i++) {
            LinearLayout infoSection = (LinearLayout) logItems.get(i).getChildAt(2);
            TextView callTypeSection = (TextView) infoSection.getChildAt(0);
            String callType = callTypeSection.getText().toString();
            if(callType.equals("Outgoing"))
                logItems.get(i).setVisibility((bOutgoing ? View.VISIBLE : View.GONE));
            else if(callType.equals("Incoming"))
                logItems.get(i).setVisibility((bIncoming ? View.VISIBLE : View.GONE));
            else
                logItems.get(i).setVisibility((bMissed ? View.VISIBLE : View.GONE));
        }
    }

    public static String FindContactPhoto(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode("tel:"+phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactPhoto = null;
        if(cursor.moveToFirst()) {
            contactPhoto = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI));
        }
        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactPhoto;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}

