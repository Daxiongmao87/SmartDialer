package com.bitjunkie.smartdialer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class addContact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        Button confirm = (Button) findViewById(R.id.btnAdd);
    }


}
