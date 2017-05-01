package com.bitjunkie.smartdialer;

import android.app.Fragment;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.bitjunkie.smartdialer.R.id.Contacts;
import static com.bitjunkie.smartdialer.R.id.container;
import static com.bitjunkie.smartdialer.R.id.imgPhoto;

/**
 * Created by Patrick on 4/26/2017.
 */

public class AddEditContactFragment extends Fragment {

    Button addButton;
    Button delButton;
    EditText contactName, contactNumber, contactEmail, contactAddress;
    ImageView image;
    String type;
    Uri uri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_contact, container, false);
        image = (ImageView)rootView.findViewById(R.id.imgViewContactImage);
        contactName = (EditText)rootView.findViewById(R.id.txtName);
        contactNumber = (EditText)rootView.findViewById(R.id.txtPhone);
        contactEmail = (EditText)rootView.findViewById(R.id.txtEmail);
        contactAddress = (EditText)rootView.findViewById(R.id.txtAddress);
        addButton = (Button)rootView.findViewById(R.id.btnAddEdit);
        delButton = (Button)rootView.findViewById(R.id.btnDelete);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent,"Select Photo"),1);
            }
        });
        return rootView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Toast.makeText(getActivity(),"TEST",Toast.LENGTH_LONG).show();
            uri = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(uri,filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            image.setImageURI(uri);
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstaneState) {
        super.onActivityCreated(savedInstaneState);
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            type = bundle.getString("type");
            if (type.equals("add")) {
                image.setImageResource(R.drawable.default_photo);
                delButton.setVisibility(View.GONE);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!contactName.getText().toString().equals("")) {
                            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                            intent.putExtra(ContactsContract.Intents.Insert.EMAIL,contactEmail.getText().toString())
                            .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                            .putExtra(ContactsContract.Intents.Insert.PHONE, contactNumber.getText().toString())
                            .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                            .putExtra(ContactsContract.Intents.Insert.POSTAL,contactAddress.getText().toString())
                            .putExtra(ContactsContract.Intents.Insert.POSTAL_TYPE,ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME)
                            .putExtra(ContactsContract.Intents.Insert.NAME,contactName.getText().toString())
                            .putExtra(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                            .putExtra(ContactsContract.CommonDataKinds.Photo.PHOTO_URI, uri);

                            startActivity(intent);
                        }
                        else
                            Toast.makeText(getActivity(),"Name is Empty!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                image.setImageURI(Uri.parse(bundle.getString("photoURI")));
                contactName.setText(bundle.getString("name"));
                contactNumber.setText(bundle.getString("number"));
                contactEmail.setText(bundle.getString("email"));
                contactAddress.setText(bundle.getString("address"));
                delButton.setVisibility(View.VISIBLE);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!contactName.getText().equals("")) {

                            Toast.makeText(getActivity(), "Contact Saved", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getActivity(),"Name is Empty!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
