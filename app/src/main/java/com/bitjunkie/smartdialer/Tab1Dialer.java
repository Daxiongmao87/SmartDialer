package com.bitjunkie.smartdialer;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import static android.R.attr.name;
import static android.content.ContentValues.TAG;
import static com.bitjunkie.smartdialer.R.id.Contacts;
import static com.bitjunkie.smartdialer.R.id.textView;
import static com.bitjunkie.smartdialer.R.id.txtName;



/**
 *
 * FILE NAME: Tab1Dialer.java
 *
 * DESCRIPTION: This java file handles the functionality
 * for the Dialer tab of the Smart Dialer application.
 * This tab contains a T9-style dial pad for making calls
 * similar to other caller apps.
 *
 *
 *
 *   DATE      BY         DESCRIPTION
 * ========   =====       ============
 * 4/4/2017   Omar Q.     Created the class
 * 4/25/2017 Patrick R.   Finished the class
 */



public class Tab1Dialer extends Fragment implements View.OnClickListener{

    int MY_PERMISSION_REQUEST_CALL_PHONE = 0;
    EditText edtPhoneNo;

    Button btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine, btnZero, btnAsterisk,btnHash,btnClear,btnCall;
    ImageButton btnDelete;
    ArrayList<String> numberList;
    ArrayList<String> nameList;

    /**
     * In this method click listeners are established for all buttons,
     * number list and name list arrays used for autocomplete are initialized,
     * and the autocomplete adapter is initialized
     * @param inflater - Brings the layout to view
     * @param container - the container of this fragment
     * @param savedInstanceState - the saved state of this fragment since last pause
     * @return - Returns View to Main Activity
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.tab1dialer, container, false);
        numberList = new ArrayList<>();
        nameList = new ArrayList<>();
        PopulateNumberList();
        edtPhoneNo = (EditText) rootView.findViewById(R.id.edtPhoneNumber);
        //Button listeners
        btnOne = (Button) rootView.findViewById(R.id.btnOne);
        btnOne.setOnClickListener(this);
        btnTwo = (Button) rootView.findViewById(R.id.btnTwo);
        btnTwo.setOnClickListener(this);
        btnThree = (Button) rootView.findViewById(R.id.btnThree);
        btnThree.setOnClickListener(this);
        btnFour = (Button) rootView.findViewById(R.id.btnFour);
        btnFour.setOnClickListener(this);
        btnFive = (Button) rootView.findViewById(R.id.btnFive);
        btnFive.setOnClickListener(this);
        btnSix = (Button) rootView.findViewById(R.id.btnSix);
        btnSix.setOnClickListener(this);
        btnSeven = (Button) rootView.findViewById(R.id.btnSeven);
        btnSeven.setOnClickListener(this);
        btnEight = (Button) rootView.findViewById(R.id.btnEight);
        btnEight.setOnClickListener(this);
        btnNine = (Button) rootView.findViewById(R.id.btnNine);
        btnNine.setOnClickListener(this);
        btnZero = (Button) rootView.findViewById(R.id.btnZero);
        btnZero.setOnClickListener(this);
        btnAsterisk = (Button) rootView.findViewById(R.id.btnAsterisk);
        btnAsterisk.setOnClickListener(this);
        btnHash = (Button) rootView.findViewById(R.id.btnHash);
        btnHash.setOnClickListener(this);
        btnClear = (Button) rootView.findViewById(R.id.btnKeyboard);
        btnClear.setOnClickListener(this);
        btnCall = (Button) rootView.findViewById(R.id.btnCall);
        btnCall.setOnClickListener(this);

        btnDelete = (ImageButton) rootView.findViewById(R.id.btndel);

        //FOR NORMAL CLICK
        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String phoneNo = edtPhoneNo.getText().toString();
                int pos = edtPhoneNo.getSelectionStart();
                if (phoneNo != null && phoneNo.length() > 0) {
                    phoneNo = phoneNo.substring(0,pos-1)+ phoneNo.substring(pos,phoneNo.length());
                    edtPhoneNo.setText(phoneNo);
                    edtPhoneNo.setSelection(pos-1);
                }

            }
        });
        //FOR LONG PRESS
        btnDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                edtPhoneNo.setText("");
                return false;
            }
        });

        String[] NAMES = new String[nameList.size()];
        for(int i = 0; i < nameList.size(); i++) {
            NAMES[i] = nameList.get(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(rootView.getContext(),
                android.R.layout.simple_dropdown_item_1line, nameList);
        AutoCompleteTextView textView = (AutoCompleteTextView) rootView.findViewById(R.id.edtPhoneNumber);
        textView.setThreshold(2);
        textView.setDropDownHeight(320);
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                SetText(numberList.get(pos));

            }
        });
        return rootView;
    }

    /**
     * Used to set the text of a selected autocomplete item into the textview
     * @param text - text to set
     */
    public void SetText(String text){
        edtPhoneNo.setText(text);
        edtPhoneNo.setSelection(edtPhoneNo.getText().length());
    }

    /**
     * This is used to populate the name and number arrays for the autocomplete function
     * to work.  It takes from both the contacts list and listed numbers database
     */
    public void PopulateNumberList() {
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if(cur.getCount() > 0) {
            String name = "";
            String number = "";
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor cur2 = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = ?", new String[]{id}, null);
                    cur2.moveToFirst();
                    number = cur2.getString(cur2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    nameList.add(name + " " + number);
                    numberList.add(number);
                    cur2.close();
                }
                cur.moveToNext();
            }
        }
        cur.close();

        DatabaseOperator dbo = new DatabaseOperator(getActivity());
        SQLiteDatabase db = dbo.getWritableDatabase();
        cur = db.rawQuery("select * from LISTEDNUMBERS",null);
        cur.moveToFirst();
        if(cur.getCount() > 0) {
            while(!cur.isAfterLast()) {
                String name = cur.getString(cur.getColumnIndex("name"));
                String number = cur.getString(cur.getColumnIndex("number"));
                if(!name.equals("")) {
                    nameList.add(name + " " + number);
                    numberList.add(number);
                }
                else {
                    nameList.add(number);
                    numberList.add(number);
                }
                cur.moveToNext();
            }
        }
        cur.close();
    }

    /**
     * Called when the keyboard is closed, sets the dialpad visibility to visible
     */
    public void onKeyboardClose() {
        LinearLayout Layout_hello = (LinearLayout) getActivity().findViewById(R.id.hello);
        Layout_hello.setVisibility(View.VISIBLE);
    }
    /**
     * Called when the keyboard is opened, sets the dialpad visibility to gone
     */
    public void onKeyboardOpen() {
        Log.e("KEYBOARD","OPEN");
        LinearLayout Layout_hello = (LinearLayout) getActivity().findViewById(R.id.hello);
        Layout_hello.setVisibility(View.GONE);
    }

    /**
     * Checks if permission to call is granted
     * @return
     */
    private boolean checkCallPermission(){
        String permission = "android.permission.CALL_PHONE";
        int res = getActivity().getApplicationContext().checkCallingOrSelfPermission(permission);

        return (res == PackageManager.PERMISSION_GRANTED);



    }

    /**
     * Called when the fragment is in view, Layout resize listeners used to detect
     * keyboard state are initialized,
     */
    @Override
    public void onResume() {
        super.onResume();
        View dialerBase = getActivity().findViewById(R.id.dialerBase);

        dialerBase.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
                                       int oldBottom) {
                if (left == 0 && top == 0 && right == 0 && bottom == 0) {
                    return;
                }
                else if(bottom > oldBottom) {
                    onKeyboardClose();
                }
                else if(bottom < oldBottom) {
                    onKeyboardOpen();
                }
            }
        });
    }

    /**
     * Handles all the click listeners and their behaviors.
     * Number pad, call button, delete button, and keyboard button behaviors
     * handled here
     * @param v - View related to the fragment
     */
    @Override
    public void onClick(View v) {

        String phoneNo = edtPhoneNo.getText().toString();
        try {
            int pos = edtPhoneNo.getSelectionStart();

            switch (v.getId()) {
                case R.id.btnAsterisk:
                    Log.e("INFO",edtPhoneNo.getText().toString());

                    phoneNo = phoneNo.substring(0,pos) + "*" + phoneNo.substring(pos,phoneNo.length());
                    edtPhoneNo.setText(phoneNo);
                    edtPhoneNo.setSelection(pos+1);
                    break;
                case R.id.btnHash:

                    phoneNo = phoneNo.substring(0,pos) + "#" + phoneNo.substring(pos,phoneNo.length());
                    edtPhoneNo.setText(phoneNo);
                   edtPhoneNo.setSelection(pos+1);
                    break;
                case R.id.btnZero:

                    phoneNo = phoneNo.substring(0,pos) + "0" + phoneNo.substring(pos,phoneNo.length());
                    edtPhoneNo.setText(phoneNo);
                   edtPhoneNo.setSelection(pos+1);
                    break;
                case R.id.btnOne:

                    phoneNo = phoneNo.substring(0,pos) + "1" + phoneNo.substring(pos,phoneNo.length());
                    edtPhoneNo.setText(phoneNo);
                   edtPhoneNo.setSelection(pos+1);
                    break;
                case R.id.btnTwo:

                    phoneNo = phoneNo.substring(0,pos) + "2" + phoneNo.substring(pos,phoneNo.length());
                    edtPhoneNo.setText(phoneNo);
                   edtPhoneNo.setSelection(pos+1);
                    break;
                case R.id.btnThree:

                    phoneNo = phoneNo.substring(0,pos) + "3" + phoneNo.substring(pos,phoneNo.length());
                    edtPhoneNo.setText(phoneNo);
                    edtPhoneNo.setSelection(pos+1);
                    break;
                case R.id.btnFour:

                    phoneNo = phoneNo.substring(0,pos) + "4" + phoneNo.substring(pos,phoneNo.length());
                    edtPhoneNo.setText(phoneNo);
                   edtPhoneNo.setSelection(pos+1);
                    break;
                case R.id.btnFive:

                    phoneNo = phoneNo.substring(0,pos) + "5" + phoneNo.substring(pos,phoneNo.length());
                    edtPhoneNo.setText(phoneNo);
                   edtPhoneNo.setSelection(pos+1);
                    break;
                case R.id.btnSix:

                    phoneNo = phoneNo.substring(0,pos) + "6" + phoneNo.substring(pos,phoneNo.length());
                    edtPhoneNo.setText(phoneNo);
                   edtPhoneNo.setSelection(pos+1);
                    break;
                case R.id.btnSeven:

                    phoneNo = phoneNo.substring(0,pos) + "7" + phoneNo.substring(pos,phoneNo.length());
                    edtPhoneNo.setText(phoneNo);
                   edtPhoneNo.setSelection(pos+1);
                    break;
                case R.id.btnEight:

                    phoneNo = phoneNo.substring(0,pos) + "8" + phoneNo.substring(pos,phoneNo.length());
                    edtPhoneNo.setText(phoneNo);
                   edtPhoneNo.setSelection(pos+1);
                    break;
                case R.id.btnNine:

                    phoneNo = phoneNo.substring(0,pos) + "9" + phoneNo.substring(pos,phoneNo.length());
                    edtPhoneNo.setText(phoneNo);
                   edtPhoneNo.setSelection(pos+1);
                    break;


                case R.id.btnCall:
                    if(phoneNo.trim().equals(""))
                        Toast.makeText(getActivity().getApplicationContext(),"Enter some digits", Toast.LENGTH_SHORT).show();
                    else if(checkCallPermission()){
                        DatabaseOperator dbo = new DatabaseOperator(getActivity());
                        SQLiteDatabase db = dbo.getWritableDatabase();
                        Lookup lu = new Lookup(db,getActivity(),phoneNo);
                        lu.execute();
                        Log.e("TEST","CHECKING NUMBER");
                        startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo.trim())));
                    }
                    else if(checkCallPermission()==false){


                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSION_REQUEST_CALL_PHONE);
                    }






                    break;
                case R.id.btnKeyboard:
                    //Brings up KeyBoard
                    InputMethodManager imm;
                    imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    //Hides layout
                    break;



            }
        } catch(Exception ex){

        }

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

        /**
         *  doInBackground runs the code in the background (off the main
         *  thread).  Specifically, using the API key, the method sends a
         *  web request to whitepages.com with the phone number and retrieves
         *  a JSONfile which later is parsed to our needs
         * @param params - No params used
         * @return returns the JSONObject retrieved from the website
         */
        protected JSONObject doInBackground(String... params){
            Log.e("TEST","LOOKING UP NuMBER" + phonenumber);
            if(phonenumber !=null) {
                try {
                    String key = "d406a59b6c444b669f42f21d92306923";
                    String url = "https://proapi.whitepages.com/3.0/phone.json?api_key=" + key + "&phone=" + URLEncoder.encode(phonenumber,"UTF-8").toString();
                    JSONParser jsonParser = new JSONParser();
                    JSONObject payload = jsonParser.getJSONFromUrl(url, null);
                    return payload;
                }
                catch (java.io.UnsupportedEncodingException e) {
                    Log.e("URL","Encoding Error: " + e.toString());
                }
            }
            return null;
        }

        /**
         * Parses the data using the JSONParser class.  If the number
         * is not from a business, the info is not used.  We decided
         * this for ethical reasons.
         * @param result - the JSONObject that was retrieved from the
         *               lookup request (whitepages)
         */
        protected void onPostExecute(JSONObject result) {
            String name = "";
            String address = "";
            String city = "";
            String zip = "";
            String state = "";
            String country = "";
            Log.e("TEST","Seeing whats in data");
            try {
                if(result != null) {
                    Log.e("TEST","found something");
                    name = result.getJSONArray("belongs_to").getJSONObject(0).getString("name");
                    address = "";
                    city = "";
                    zip = "";
                    state = "";
                    country = "";/**
                    if (result.getJSONArray("belongs_to").getJSONObject(0).getString("location_type") != null && result.getJSONArray("belongs_to").getJSONObject(0).getString("location_type").equals("Address")) {
                        address = result.getJSONArray("belongs_to").getJSONObject(0).getString("street_line_1");
                        if (result.getJSONArray("belongs_to").getJSONObject(0).getString("street_line_2") != null) {
                            address += ", " + result.getJSONArray("belongs_to").getJSONObject(0).getString("street_line_2");
                            city = result.getJSONArray("belongs_to").getJSONObject(0).getString("city");
                            zip = result.getJSONArray("belongs_to").getJSONObject(0).getString("postal_code");
                            state = result.getJSONArray("belongs_to").getJSONObject(0).getString("state_code");
                            country = result.getJSONArray("belongs_to").getJSONObject(0).getString("countrey_code");
                        }
                    }**/
                    Log.e("TEST","FOUND NUMBER: " + name);
                    PopulateInfo(db, phonenumber, name, address, city, zip, state, country);
                }
            } catch (org.json.JSONException e) {
                Log.e("JSON", "JSON Error: " + e.toString());
            }
        }
    }
    /**
     * PopulateInfo populates the ListedNumbers database with our newfound information
     * to be used in the call log
     * @param db - SQLitedatabase for ListedNumbers
     * @param number - phone number
     * @param name - retrieved name
     * @param address - retrieved address
     * @param city - retrieved city
     * @param zip - retrieved zipcode
     * @param state - retrieved state
     * @param country - retrieved country
     */
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

    }
}

