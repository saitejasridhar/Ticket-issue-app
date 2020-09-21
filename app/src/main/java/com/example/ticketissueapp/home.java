package com.example.ticketissueapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Profile1> list;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        setContentView(R.layout.activity_home);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient2));
        }


        bottomNavigationView=(BottomNavigationView) findViewById(R.id.navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.tickets:
                        openNewActivity(Tickets.class);
                        break;
                    case R.id.profile:
                        openNewActivity(Profile.class);
                        break;
                }
                return  true;
            }
        });

        Button logout_button = (Button) findViewById(R.id.logout);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                finish();
                openNewActivity(MainActivity.class);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));


        reference = FirebaseDatabase.getInstance().getReference().child("Profiles");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<Profile1>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Profile1 p = dataSnapshot1.getValue(Profile1.class);
                    list.add(p);
                }
                adapter = new MyAdapter(home.this,list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(home.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }

    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {

        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user == null) {
                startActivity(new Intent(home.this, Login.class));
                finish();
            }
        }
    };
}