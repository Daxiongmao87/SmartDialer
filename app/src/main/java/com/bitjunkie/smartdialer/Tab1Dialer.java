package com.bitjunkie.smartdialer;

/**
 * Created by Omar on 4/4/2017.
 */


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Tab1Dialer extends Fragment implements View.OnClickListener{
    EditText edtPhoneNo;
    TextView lblinfo;
    Button btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine, btnZero, btnAsterisk,btnHash,btnDelete,btnClear,btnCall;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab1dialer, container, false);
        edtPhoneNo = (EditText) rootView.findViewById(R.id.edtPhoneNumber);
        lblinfo = (TextView) rootView.findViewById(R.id.lblinfo);
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
        btnClear = (Button) rootView.findViewById(R.id.btnClearAll);
        btnClear.setOnClickListener(this);
        btnCall = (Button) rootView.findViewById(R.id.btnCall);
        btnCall.setOnClickListener(this);
        btnDelete = (Button) rootView.findViewById(R.id.btndel);
        btnDelete.setOnClickListener(this);

        //
        return rootView;
    }

    private boolean checkCallPermission(){
        String permission = "android.permission.CALL_PHONE";
        int res = getActivity().getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public void onClick(View v) {

        String phoneNo = edtPhoneNo.getText().toString();
        try {
            switch (v.getId()) {
                case R.id.btnAsterisk:
                    Log.e("INFO",edtPhoneNo.getText().toString());
                    lblinfo.setText("");
                    phoneNo += "*";
                    edtPhoneNo.setText(phoneNo);
                    break;
                case R.id.btnHash:
                    lblinfo.setText("");
                    phoneNo += "#";
                    edtPhoneNo.setText(phoneNo);
                    break;
                case R.id.btnZero:
                    lblinfo.setText("");
                    phoneNo += "0";
                    edtPhoneNo.setText(phoneNo);
                    break;
                case R.id.btnOne:
                    lblinfo.setText("");
                    phoneNo += "1";
                    edtPhoneNo.setText(phoneNo);
                    break;
                case R.id.btnTwo:
                    lblinfo.setText("");
                    phoneNo += "2";
                    edtPhoneNo.setText(phoneNo);
                    break;
                case R.id.btnThree:
                    lblinfo.setText("");
                    phoneNo += "3";
                    edtPhoneNo.setText(phoneNo);
                    break;
                case R.id.btnFour:
                    lblinfo.setText("");
                    phoneNo += "4";
                    edtPhoneNo.setText(phoneNo);
                    break;
                case R.id.btnFive:
                    lblinfo.setText("");
                    phoneNo += "5";
                    edtPhoneNo.setText(phoneNo);
                    break;
                case R.id.btnSix:
                    lblinfo.setText("");
                    phoneNo += "6";
                    edtPhoneNo.setText(phoneNo);
                    break;
                case R.id.btnSeven:
                    lblinfo.setText("");
                    phoneNo += "7";
                    edtPhoneNo.setText(phoneNo);
                    break;
                case R.id.btnEight:
                    lblinfo.setText("");
                    phoneNo += "8";
                    edtPhoneNo.setText(phoneNo);
                    break;
                case R.id.btnNine:
                    lblinfo.setText("");
                    phoneNo += "9";
                    edtPhoneNo.setText(phoneNo);
                    break;
                case R.id.btndel:
                    lblinfo.setText("");
                    if (phoneNo != null && phoneNo.length() > 0) {
                        phoneNo = phoneNo.substring(0, phoneNo.length() - 1);
                    }
                    edtPhoneNo.setText(phoneNo);
                    break;


                case R.id.btnCall:
                    if(phoneNo.trim().equals(""))
                        Toast.makeText(getActivity().getApplicationContext(),"Enter some digits", Toast.LENGTH_SHORT).show();
                    else if(checkCallPermission()){
                        startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNo.trim())));
                    }



                    break;

                case R.id.btnClearAll:
                    lblinfo.setText("");
                    edtPhoneNo.setText("");
                    break;



            }
        } catch(Exception ex){

        }

    }

}

