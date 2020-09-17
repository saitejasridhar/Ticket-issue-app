package com.example.ticketissueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final EditText phoneno=(EditText) findViewById(R.id.phone);
        final EditText uname=(EditText) findViewById(R.id.name);
        final EditText email=(EditText) findViewById(R.id.email);
        firebaseAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        final CollectionReference ref = fstore.collection("users");



        getSupportActionBar().hide();
        Button signup_button = (Button) findViewById(R.id.signup);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!uname.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !phoneno.getText().toString().isEmpty() ){

           final String n = uname.getText().toString();
                               final String e = email.getText().toString();
                               final String p = phoneno.getText().toString();
                  final   String finalphone = "+91" + p;

                   ref.whereEqualTo("phone",finalphone).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if(task.isSuccessful())
                           {
                               int c=0;
                               for (QueryDocumentSnapshot document : task.getResult()) {
                                   c++;
                               }
                               if(c==0)
                               {

                                   openNewActivitys(Verifyphonedata.class, finalphone, n, e);

                               }
                               else{
                          Toast.makeText(getApplicationContext(),"User exists",Toast.LENGTH_SHORT).show();

                               }
                       }
                           else{
                             Toast.makeText(getApplicationContext(),"User failer",Toast.LENGTH_SHORT).show();


                           }
                   }

                    });


                }
                else {
                    Toast.makeText(Signup.this,"All fields are required",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }

    private void openNewActivitys( final Class<? extends Activity> ActivityToOpen,String fph,String na,String em)
    {
        Intent i = new Intent(getBaseContext(), ActivityToOpen);
        i.putExtra("fphon", fph);
        i.putExtra("name",na);
        i.putExtra("email",em);
        startActivity(i);
    }

    public void perform_action(View v){
        TextView already=(TextView) findViewById(R.id.already);
        openNewActivity(Login.class);
    }


}
