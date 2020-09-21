package com.example.ticketissueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Editprofile extends AppCompatActivity {
    BottomNavigationView bottomNavigationView ;
    private EditText name;
    private EditText address;
    private EditText email;
    Button update;

    FirebaseAuth firebaseAuth;
    String usid;
    private FirebaseFirestore db;

    users u;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setCustomView(R.layout.actionbarep);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_white));
        }
        update=(Button)findViewById(R.id.fabUploadImage);

        firebaseAuth =FirebaseAuth.getInstance();
        usid=firebaseAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();


        name = findViewById(R.id.editname);
        email = findViewById(R.id.editemail);
        address = findViewById(R.id.editaddress);




        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProduct();
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
                    case R.id.profile:
                        openNewActivity(Profile.class);
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

    private void updateProduct() {
        String aname = name.getText().toString().trim();
        String aemail = email.getText().toString().trim();
        String aaddress = address.getText().toString().trim();

            db.collection("users").document(usid)
                    .update(
                            "name", aname,
                            "email", aemail
                    )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(
                                    Editprofile.this, "User Updated", Toast.LENGTH_LONG).show();
                        }
                    });

    }
}