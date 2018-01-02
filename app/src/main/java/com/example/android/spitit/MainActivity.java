package com.example.android.spitit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private DatabaseReference mDatabase;
    private ArrayList<String> admins;
    private FragmentManager fragmentManager;
    static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Aegis");
        toolbar.inflateMenu(R.menu.main);

        mAuth=FirebaseAuth.getInstance();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(mAuth.getCurrentUser() != null)
        {
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            View hView =  navigationView.getHeaderView(0);
            TextView nav_user = (TextView)hView.findViewById(R.id.textView);
            if(!TextUtils.isEmpty(personGivenName) && !TextUtils.isEmpty(personFamilyName))
                nav_user.setText(personGivenName+" "+personFamilyName);
            else
                nav_user.setText(acct.getEmail());
        }
        admins=getAdmin();
        if(admins.contains(mAuth.getCurrentUser().getEmail()));
        {
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_emergency).setVisible(true);
        }
    }

    private ArrayList<String> getAdmin()
    {
        final ArrayList<String> admins=new ArrayList<>();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Admin");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Admin",dataSnapshot.getValue().toString());
                HashMap<String,String> admin=(HashMap<String,String>)dataSnapshot.getValue();
                ArrayList<String> keySet=new ArrayList<>(admin.keySet());
                for(String key:keySet)
                {
                    admins.add(admin.get(key));
                    Log.e("Admin",admin.get(key));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return admins;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_add)
        {
            Intent addContact=new Intent(this,AddContactsActivity.class);
            startActivity(addContact);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {
            // Handle the camera action
        }
        else if (id == R.id.nav_static)
        {
            toolbar.setTitle("Static information");
        }
        else if (id == R.id.nav_emergency)
        {
            toolbar.setTitle("Emergency");
            //fragmentManager=getSupportFragmentManager();
            //fragmentManager.beginTransaction().replace(R.id.app_bar_main,new EmergencyFragment()).commit();
            startActivity(new Intent(this,EmergencyActivity.class));
        }
        else if (id == R.id.nav_instructions)
        {
            toolbar.setTitle("Instructions");
        }
        else if (id == R.id.nav_sos)
        {
            toolbar.setTitle("SOS no.");
        }
        else if (id == R.id.nav_portal)
        {
            toolbar.setTitle("Emergency portal");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
