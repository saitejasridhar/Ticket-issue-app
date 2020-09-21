package com.example.ticketissueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.core.Query;

public class Profile extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Button editpro;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usid = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
    private CollectionReference notebookRef = db.collection("users").document(usid).collection("tickets");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbarp);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient2));
        }

        editpro=(Button)findViewById(R.id.editprofile);
        editpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openNewActivity(Editprofile.class);
            }
        });



        bottomNavigationView=(BottomNavigationView) findViewById(R.id.navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.home:
                        openNewActivity(home.class);
                        break;
                    case R.id.tickets:
                        openNewActivity(Tickets.class);
                        break;

                }
                return  true;
            }
        });
    }

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }

}