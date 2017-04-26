package com.bitjunkie.smartdialer;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 *  
 * FILE NAME: ReverseLookup.java
 * 
 * DESCRIPTION: This java file handles the Reverse Lookup feature for our
 * Smart Dialer application, including utilizing the API and performing
 * background checks to capture incoming calls and insert them into
 * the Smart Dialer app for reverse lookup.
 *
 *   DATE       BY      DESCRIPTION
 * ======== ========== =============
 * 4/19/2017 Patrick R. Created the class
 * 
 */

public class ReverseLookup extends Service 
{

    BroadcastReceiver br;
    private static boolean ringing = false;
    private static boolean incoming = false;

    private final BroadcastReceiver receiver = new BroadcastReceiver() 
    {

        String prevState = null;
        
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String number;

            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING))
             {

                if(prevState == null || !prevState.equals(state))
                 {

                    number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    Log.e("TESTING","TEST0: CHECKING NUMBERS");
                    
                    if (!CheckKnownNumbers(context, number))
                    {

                        Log.e("TESTING","TEST1: INCOMING CALL");
                        Lookup lu = new Lookup(context, number);
                        lu.execute();
                    }
                }
            }

            prevState = state;
        }
        
        class Lookup extends AsyncTask<String, Integer, JSONObject> 
        {

            private Context context;
            private String phonenumber;

            public Lookup(Context c, String pn) 
            {

                context = c;
                phonenumber = pn;
            }

            protected JSONObject doInBackground(String... params)
            {
                Log.e("NUMBER!",phonenumber);

                if(phonenumber !=null) 
                {

                    try 
                    {

                        String key = "d406a59b6c444b669f42f21d92306923";
                        String url = "https://proapi.whitepages.com/3.0/phone.json?api_key=" + key + "&phone=" + URLEncoder.encode(phonenumber,"UTF-8").toString();
                        JSONParser jsonParser = new JSONParser();
                        JSONObject payload = jsonParser.getJSONFromUrl(url, null);
                        Log.e("TESTING","TEST2: " + payload.getJSONArray("belongs_to").getJSONObject(0).getString("name"));
                        return payload;
                    }

                    catch (org.json.JSONException e) 
                    {

                        Log.e("JSON","JSON Error: " + e.toString());
                    }

                    catch (java.io.UnsupportedEncodingException e)
                    {
                        Log.e("URL","Encoding Error: " + e.toString());
                    }
                }

                return null;
            }

            protected void onPostExecute(JSONObject result) 
            {

                try 
                {

                    String name = result.getJSONArray("belongs_to").getJSONObject(0).getString("name");
                    String address = "";
                    String city = "";
                    String zip = "";
                    String state = "";
                    String country = "";

                    if(result.get("is_commercial").equals("true")) 
                    {

                        if(result.getJSONArray("belongs_to").getJSONObject(0).getString("location_type").equals("Address"))
                        {

                            address = result.getJSONArray("belongs_to").getJSONObject(0).getString("street_line_1");
                            
                            if(result.getJSONArray("belongs_to").getJSONObject(0).getString("street_line_2") != null) 
                            {

                                address += ", " + result.getJSONArray("belongs_to").getJSONObject(0).getString("street_line_2");
                                city = result.getJSONArray("belongs_to").getJSONObject(0).getString("city");
                                zip = result.getJSONArray("belongs_to").getJSONObject(0).getString("postal_code");
                                state = result.getJSONArray("belongs_to").getJSONObject(0).getString("state_code");
                                country = result.getJSONArray("belongs_to").getJSONObject(0).getString("countrey_code");
                            }
                        }
                    }

                    Log.e("TESTING","TEST3: POPULATING INFO");
                    PopulateInfo(context,phonenumber,name,address,city,zip,state,country);
                }

                catch (org.json.JSONException e) 
                {

                    Log.e("JSON","JSON Error: " + e.toString());
                }

            }
        }
    };

    public void PopulateInfo(Context context,String number,String name, String address, String city, String zip, String state, String country)
    {

        DatabaseOperator dbo = new DatabaseOperator(context);
        SQLiteDatabase db = dbo.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number",number);
        values.put("name",name);
        values.put("address",address);
        values.put("city",city);
        values.put("zip",zip);
        values.put("state",state);
        values.put("country",country);
        db.insert("LISTEDNUMBERS",null,values);
        Log.e("TESTING","TEST4: INFO POPuLATED");
        //db.close();
    }

    public boolean CheckKnownNumbers(Context context,String number) 
    {

        DatabaseOperator dbo = new DatabaseOperator(context);
        SQLiteDatabase db = dbo.getReadableDatabase();
        String[] projection = 
        {

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

        if(cursor.getCount() > 0) 
        {

            cursor.close();
            Log.e("TESTING","TEST0A: EXISTS");
            //db.close();
            return true;
        }

        cursor.close();
        //db.close();
        Log.e("TESTING","TEST0A: DOES NOT EXIST");
        return false;
    }

    @Override
    public void onCreate()
    {

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        filter.addAction("your_action_strings"); //further more
        filter.addAction("your_action_strings"); //further more

        registerReceiver(receiver, filter);
        //Toast.makeText(getApplicationContext(),"Service started", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }
}
