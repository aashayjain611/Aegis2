package com.example.android.spitit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmergencyActivity extends AppCompatActivity {

    private RecyclerView mEmergencyList;
    private DatabaseReference mDatabase;
    View myView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        Toolbar toolbar=(Toolbar)findViewById(R.id.emergency_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Emergency");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mEmergencyList=(RecyclerView)findViewById(R.id.emergency_list);
        mEmergencyList.setHasFixedSize(false);
        mEmergencyList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Emergency");
        mDatabase.keepSynced(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Emergency,EmergencyViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Emergency, EmergencyViewHolder>(Emergency.class,R.layout.emergency_row,EmergencyViewHolder.class,mDatabase) {


            @Override
            protected void populateViewHolder(EmergencyViewHolder viewHolder, Emergency model, int position) {
                final String uid=getRef(position).getKey();
                viewHolder.setLocation(model.getLocation());
                viewHolder.setType(model.getType());
                viewHolder.setTip(model.getTip());
                viewHolder.setPeople(model.getPeople());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        };
        mEmergencyList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class EmergencyViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public EmergencyViewHolder(View itemView)
        {
            super(itemView);
            mView=itemView;
        }
        public void setLocation(String location)
        {
            TextView locationTextView=(TextView)mView.findViewById(R.id.location_card_view);
            locationTextView.setText(location);
        }
        public void setTip(String tip)
        {
            TextView tipTextView=(TextView)mView.findViewById(R.id.tip_card_view);
            tipTextView.setText(tip);
        }
        public void setType(String emergency_type)
        {
            TextView emergencyTextView=(TextView)mView.findViewById(R.id.emergency_card_view);
            emergencyTextView.setText(emergency_type);
        }
        public void setPeople(String people)
        {
            TextView peopleTextView=(TextView)mView.findViewById(R.id.people_left_card_view);
            peopleTextView.setText(people);
        }
    }
}
