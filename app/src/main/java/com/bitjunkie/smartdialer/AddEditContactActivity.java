package com.bitjunkie.smartdialer;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

/**
 * Created by Patrick on 4/26/2017.
 */

public class AddEditContactActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String type = getIntent().getStringExtra("type");
        Bundle bundle = new Bundle();
        if(type.equals("add")) {
            bundle.putString("type", type);
            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            startActivity(intent);
        }
        else {
            bundle.putString("type", type);
            String name = getIntent().getStringExtra("name");
            String photoURI = getIntent().getStringExtra("photoURI");
            String number = getIntent().getStringExtra("number");
            String email = getIntent().getStringExtra("email");
            String address = getIntent().getStringExtra("address");
            String id = getIntent().getStringExtra("id");
            /*
            bundle.putString("name",name);
            bundle.putString("photoURI",photoURI);
            bundle.putString("number",number);
            bundle.putString("email",email);
            bundle.putString("address",address);
            Intent intent = new Intent(ContactsContract.Intents.Insert.);*/
            Uri mUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                    Long.parseLong(id));
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setDataAndType(mUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);
            intent.putExtra("finishActivityOnSaveCompleted", true);
            startActivityForResult(intent, 0);

        }
        //AddEditContactFragment frag = new AddEditContactFragment();
        //frag.setArguments(bundle);
        //getFragmentManager().beginTransaction().replace(android.R.id.content, frag).commit();



    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
