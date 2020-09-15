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

import java.util.concurrent.TimeUnit;

public class Verifyphone extends AppCompatActivity {
    String verificationId;
    PhoneAuthProvider.ForceResendingToken token;
    Boolean vip=false;
    FirebaseAuth fAuth;
    TextView res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyphone);
        Intent intent = getIntent();
        final String phonenumber = intent.getExtras().getString("fphon");
        requestotp(phonenumber);
        getSupportActionBar().hide();
        fAuth= FirebaseAuth.getInstance();
        Button veri=(Button) findViewById(R.id.button);
        res=(TextView) findViewById(R.id.textView3);
        final EditText code=(EditText) findViewById(R.id.editTextNumberDecimal2);
        veri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vip){
                    String OTP=code.getText().toString();
                    if(OTP.length()==6)
                    {
                        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,OTP);
                        verifyAuth(credential);
                    }
                    else
                    {
                        code.setError("Valid OTP is required");
                    }
                }
            }
        });
    }

    public void verifyAuth(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    openNewActivity(home.class);
                    finishAffinity();
                    Toast.makeText(Verifyphone.this,"Authentication sucessful",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Verifyphone.this,"Authentication failed",Toast.LENGTH_SHORT).show();
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
                res.setVisibility(View.VISIBLE);

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
                Toast.makeText(Verifyphone.this,"Could not verify account"+e.getMessage(),Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Verifyphone.this,"Could not verify account"+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                },
                token);
        Toast.makeText(Verifyphone.this,"OTP resent",Toast.LENGTH_SHORT).show();

    }
}