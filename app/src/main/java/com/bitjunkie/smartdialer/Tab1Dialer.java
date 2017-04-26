package com.bitjunkie.smartdialer;

/**
 * Created by Omar on 4/4/2017.
 */


import android.app.Activity;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import static android.content.ContentValues.TAG;


public class Tab1Dialer extends Fragment implements View.OnClickListener{

    int MY_PERMISSION_REQUEST_CALL_PHONE = 0;
    EditText edtPhoneNo;

    Button btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine, btnZero, btnAsterisk,btnHash,btnClear,btnCall;
    ImageButton btnDelete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.tab1dialer, container, false);
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
                }
                edtPhoneNo.setText(phoneNo);
                edtPhoneNo.setSelection(pos-1);
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

        return rootView;
    }

    public void onKeyboardClose() {
        Log.e("KEYBOARD","CLOSED");
        LinearLayout Layout_hello = (LinearLayout) getActivity().findViewById(R.id.hello);
        Layout_hello.setVisibility(View.VISIBLE);
    }
    public void onKeyboardOpen() {
        Log.e("KEYBOARD","OPEN");
        LinearLayout Layout_hello = (LinearLayout) getActivity().findViewById(R.id.hello);
        Layout_hello.setVisibility(View.GONE);
    }

    private boolean checkCallPermission(){
        String permission = "android.permission.CALL_PHONE";
        int res = getActivity().getApplicationContext().checkCallingOrSelfPermission(permission);

        return (res == PackageManager.PERMISSION_GRANTED);



    }
    @Override
    public void onResume() {
        super.onResume();
        View dialerBase = getActivity().findViewById(R.id.dialerBase);

        dialerBase.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
                                       int oldBottom) {
                // its possible that the layout is not complete in which case
                // we will get all zero values for the positions, so ignore the event
                if (left == 0 && top == 0 && right == 0 && bottom == 0) {
                    return;
                }
                else if(bottom > oldBottom) {
                    onKeyboardClose();
                }
                else if(bottom < oldBottom) {
                    onKeyboardOpen();
                }
                // Do what you need to do with the height/width since they are now set
            }
        });
    }
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


}

