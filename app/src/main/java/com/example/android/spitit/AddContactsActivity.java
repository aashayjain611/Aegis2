package com.example.android.spitit;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddContactsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button save;
    private FrameLayout photo1,photo2,photo3;
    private int id_name,id_no,id_image;
    private static final int PICK_CONTACT=1;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
    private ArrayList<String> numbers=new ArrayList<>();
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorage=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        getPermissionToReadUserContacts();
        toolbar=(Toolbar)findViewById(R.id.add_contact_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select contacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intialize();
        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContact();
                id_name=R.id.name1;
                id_no=R.id.no1;
                id_image=R.id.image1;
            }
        });
        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContact();
                id_name=R.id.name2;
                id_no=R.id.no2;
                id_image=R.id.image2;
            }
        });
        photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContact();
                id_name=R.id.name3;
                id_no=R.id.no3;
                id_image=R.id.image3;
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numbers.size() == 3)
                {
                    Intent main=new Intent(AddContactsActivity.this,MainActivity.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(main);
                    finish();
                }
                else
                    Toast.makeText(AddContactsActivity.this,"Field(s) cannot be empty",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setContact()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
        Log.e("Hello","2");
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        Log.e("Hello","3");
        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {

                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        String cNumber="";

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                                    null, null);
                            phones.moveToFirst();
                            cNumber = phones.getString(phones.getColumnIndex("data1")).replace(" ","");
                            phones.close();
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
                                .parseLong(id));
                        Uri pic=Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                        System.out.println("number is:"+cNumber);
                        System.out.println("name is:"+name);
                        System.out.println(pic);
                        if(!numbers.contains(cNumber))
                            numbers.add(cNumber);
                        singleContact(name,cNumber,pic);
                    }
                }
                break;
        }
    }

    private void singleContact(String name, String cNumber, Uri pic)
    {
        TextView nameView=(TextView)findViewById(id_name);
        nameView.setText(name);
        TextView number=(TextView)findViewById(id_no);
        number.setText(cNumber);
        ImageView image=(ImageView)findViewById(id_image);
        Picasso.with(this).load(pic).into(image);
        /*if(id_no == R.id.no1)
        {
            mDatabase.child("Person1").child("Name").setValue(name);
            mDatabase.child("Person1").child("Phone").setValue(cNumber);
            mDatabase.child("Person1").child("Photo").setValue(pic);
            mStorage= FirebaseStorage.getInstance().getReference().child("Photos").child(mAuth.getCurrentUser().getUid()).child("Person1");
            mStorage.putFile(pic);
        }
        else if(id_no == R.id.no2)
        {
            mDatabase.child("Person2").child("Name").setValue(name);
            mDatabase.child("Person2").child("Phone").setValue(cNumber);
            mDatabase.child("Person2").child("Photo").setValue(pic);
            mStorage= FirebaseStorage.getInstance().getReference().child("Photos").child(mAuth.getCurrentUser().getUid()).child("Person2");
            mStorage.putFile(pic);
        }
        else if(id_no == R.id.no3)
        {
            mDatabase.child("Person3").child("Name").setValue(name);
            mDatabase.child("Person3").child("Phone").setValue(cNumber);
            mDatabase.child("Person3").child("Photo").setValue(pic);
            mStorage= FirebaseStorage.getInstance().getReference().child("Photos").child(mAuth.getCurrentUser().getUid()).child("Person3");
            mStorage.putFile(pic);
        }*/
    }

    private void intialize()
    {
        photo1=(FrameLayout) findViewById(R.id.photo1);
        photo2=(FrameLayout) findViewById(R.id.photo2);
        photo3=(FrameLayout) findViewById(R.id.photo3);
        save=(Button)findViewById(R.id.save);
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Contacts");
    }

    public void getPermissionToReadUserContacts() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (shouldShowRequestPermissionRationale(
                    android.Manifest.permission.READ_CONTACTS)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
