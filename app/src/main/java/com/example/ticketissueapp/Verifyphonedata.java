package com.example.ticketissueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Verifyphonedata extends AppCompatActivity {
    String verificationId;
    PhoneAuthProvider.ForceResendingToken token;
    Boolean vip=false;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
   FirebaseFirestore fstore=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

      TextView  res=(TextView) findViewById(R.id.textView3);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyphonedata);
        Intent intent = getIntent();
        final String phonenumber = intent.getExtras().getString("fphon");
        final String n = intent.getExtras().getString("name");
        final String e = intent.getExtras().getString("email");
        requestotp(phonenumber);
        getSupportActionBar().hide();
        Button veri=(Button) findViewById(R.id.button);
        final EditText code=(EditText) findViewById(R.id.editTextNumberDecimal2);
        veri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vip){
                    String OTP=code.getText().toString();
                    if(OTP.length()==6)
                    {

                        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,OTP);
                        verifyAuth(credential,n,e,phonenumber);
                    }
                    else
                    {
                        code.setError("Valid OTP is required");
                    }
                }
            }
        });
    }

    private void verifyAuth(PhoneAuthCredential credential, final String n, final String e, final String p) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String  userid=firebaseAuth.getCurrentUser().getUid();
                    Map<String,Object> user=new HashMap<>();
                              user.put("name",n);
                              user.put("email",e);
                               user.put("phone",p);
                               Log.v("Hell","Hello");
                          DocumentReference dr=fstore.collection("users").document(userid);
                    dr.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                      if(task.isSuccessful()){
                                          openNewActivity(home.class);
                                          finishAffinity();
                                      }
                                       else {
                                          Toast.makeText(getApplicationContext(),"Data is not inserted",Toast.LENGTH_SHORT).show();
                                      }

                                   }
                               });
                }
                else{
                    Toast.makeText(Verifyphonedata.this,"Authentication failed",Toast.LENGTH_SHORT).show();
                    Log.v("Hello","Hello failed");
                }

            }
        });
    }


    private void requestotp(String fphone) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(fphone, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);


            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId=s;
                token= forceResendingToken;
                vip=true;

            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(Verifyphonedata.this,"Could not verify account"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen) {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }

    public void perform_action(View view) {
        Intent intent = getIntent();
        String phonenumber = intent.getExtras().getString("fphon");

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenumber,
                1  ,
                TimeUnit.MINUTES,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(Verifyphonedata.this,"Could not verify account"+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                },
                token);
        Toast.makeText(Verifyphonedata.this,"OTP resent",Toast.LENGTH_SHORT).show();

    }

}