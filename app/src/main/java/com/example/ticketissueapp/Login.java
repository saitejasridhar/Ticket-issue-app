package com.example.ticketissueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       FirebaseFirestore fstore= FirebaseFirestore.getInstance();

        fAuth=FirebaseAuth.getInstance();
        final CollectionReference ref = fstore.collection("users");

        getSupportActionBar().hide();
        Button send=(Button) findViewById(R.id.sendotp);
        final EditText phonenum=(EditText) findViewById(R.id.editTextNumberDecimal);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!phonenum.getText().toString().isEmpty() && phonenum.getText().toString().length()==10)
                        {
                      final String fphone="+91"+phonenum.getText().toString();
                            ref.whereEqualTo("phone",fphone).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        int c=0;
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            c++;
                                        }
                                        if(c!=0)
                                        {
                                            openNewActivitys(Verifyphone.class,fphone);
//                                            finish();
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"User does not exist",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"User failer",Toast.LENGTH_SHORT).show();

                                    }
                                }

                            });

                        }
                        else {
                            phonenum.setError("Phone number is not valid");
                        }

                    }
                });
    }

    private void openNewActivitys( final Class<? extends Activity> ActivityToOpen,String fph)
    {
        Intent i = new Intent(getBaseContext(), ActivityToOpen);
        i.putExtra("fphon", fph);
        startActivity(i);
    }
    private void openNewActivity( final Class<? extends Activity> ActivityToOpen) {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }
    public void perform_action(View v){
        TextView already=(TextView) findViewById(R.id.already);
        openNewActivity(Signup.class);
    }
}