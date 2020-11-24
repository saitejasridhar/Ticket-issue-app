package com.example.ticketissueapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.example.ticketissueapp.Helpers.CoreHelper;
import com.example.ticketissueapp.Helpers.CustomModel;

import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.Calendar;


public class Tickets extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Spinner spinner;
    private static final int READ_PERMISSION_CODE = 1;
    private static final int PICK_IMAGE_REQUEST_CODE = 2;
    ImageView no_images;
    Button btnPickImages, btnUploadImages;
    RecyclerView recyclerView;
    List<CustomModel> imagesList;
    List<String> savedImagesUri;
    CoreHelper coreHelper;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    CollectionReference reference;
    int counter;
    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setCustomView(R.layout.actionbart);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_white));
        }
        setContentView(R.layout.activity_tickets);
        spinner = findViewById(R.id.spinner);
        spinner.setPrompt("Title");


        final EditText p = (EditText) findViewById(R.id.editTextPhone2);
        final EditText n = (EditText) findViewById(R.id.editTextTextPersonName3);
        final EditText e = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        final EditText a = (EditText) findViewById(R.id.editTextTextPostalAddress2);
        final EditText i = (EditText) findViewById(R.id.editTextTextPersonName4);

        firebaseAuth = FirebaseAuth.getInstance();


        firestore = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        reference = firestore.collection("tickets");

        savedImagesUri = new ArrayList<>();

        btnPickImages = (Button) findViewById(R.id.fabChooseImage);
        btnUploadImages = (Button) findViewById(R.id.fabUploadImage);
        imagesList = new ArrayList<>();
        coreHelper = new CoreHelper(this);

        String[] value = {"Issue 1", "Issue 2", "Issue 3", "Issue 4"};
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(value));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.style_spinner, arrayList);
        spinner.setAdapter(arrayAdapter);


        btnPickImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPermissionAndPickImage();
            }
        });
        btnUploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!n.getText().toString().isEmpty() && !p.getText().toString().isEmpty() && !e.getText().toString().isEmpty() && !a.getText().toString().isEmpty() && !i.getText().toString().isEmpty()) {
                    uploadImages(view);

                } else {
                    Toast.makeText(Tickets.this, "All fields are required", Toast.LENGTH_SHORT).show();

                }

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
                    case R.id.profile:
                        openNewActivity(Tab.class);
                        break;
                }
                return  true;
            }
        });
    }



    private void uploadImages(View view) {
        if (imagesList.size() != 0) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploaded 0/" + imagesList.size());
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
            final StorageReference storageReference = storage.getReference();
            for (int i = 0; i < imagesList.size(); i++) {
                final int finalI = i;
                storageReference.child("userData/").child(imagesList.get(i).getImageName()).putFile(imagesList.get(i).getImageURI()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            storageReference.child("userData/").child(imagesList.get(finalI).getImageName()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    counter++;
                                    progressDialog.setMessage("Uploaded " + counter + "/" + imagesList.size());
                                    if (task.isSuccessful()) {
                                        savedImagesUri.add(task.getResult().toString());
                                    } else {
                                        storageReference.child("userData/").child(imagesList.get(finalI).getImageName()).delete();
                                        Toast.makeText(Tickets.this, "Couldn't save " + imagesList.get(finalI).getImageName(), Toast.LENGTH_SHORT).show();
                                    }
                                    if (counter == imagesList.size()) {
                                        saveImageDataToFirestore(progressDialog);
                                    }
                                }
                            });
                        } else {
                            progressDialog.setMessage("Uploaded " + counter + "/" + imagesList.size());
                            counter++;
                            Toast.makeText(Tickets.this, "Couldn't upload " + imagesList.get(finalI).getImageName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            coreHelper.createSnackBar(view, "Please add some images first.", "", null, Snackbar.LENGTH_SHORT);
        }
    }

    private void saveImageDataToFirestore(final ProgressDialog progressDialog) {
        String useri = firebaseAuth.getCurrentUser().getUid();
        final EditText n = (EditText) findViewById(R.id.editTextTextPersonName3);
        final EditText p = (EditText) findViewById(R.id.editTextPhone2);
        final EditText e = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        final EditText a = (EditText) findViewById(R.id.editTextTextPostalAddress2);
        final EditText id = (EditText) findViewById(R.id.editTextTextPersonName4);


        final String na = n.getText().toString();
        final String ph = p.getText().toString();
        final String em = e.getText().toString();
        final String ad = a.getText().toString();
        final String is = id.getText().toString();
        final String isu = spinner.getSelectedItem().toString();

        progressDialog.setMessage("Saving uploaded images...");
        Map<String, Object> dataMap = new HashMap<>();
        for (int i = 0; i < savedImagesUri.size(); i++) {
            dataMap.put("image" + i, savedImagesUri.get(i));
        }
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String t = df.format(c);

        dataMap.put("name", na);
        dataMap.put("phone", ph);
        dataMap.put("email", em);
        dataMap.put("taddress", ad);
        dataMap.put("tissue", isu);
        dataMap.put("tissue_desc", is);
        dataMap.put("ttime",t);
        dataMap.put("userid",useri);


        reference.add(dataMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressDialog.dismiss();
                coreHelper.createAlert("Success", "Images uploaded and saved successfully!", "OK", "", null, null, null);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                coreHelper.createAlert("Error", "Images uploaded but we couldn't save them to database.", "OK", "", null, null, null);
                Log.e("MainActivity:SaveData", e.getMessage());

            }
        });

    }

    private void verifyPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //If permission is granted
                pickImage();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);
            }
        } else {
            //no need to check permissions in android versions lower then marshmallow
            pickImage();
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            Uri uri = clipData.getItemAt(i).getUri();
                            imagesList.add(new CustomModel(coreHelper.getFileNameFromUri(uri), uri));
                        }
                    } else {
                        Uri uri = data.getData();
                        imagesList.add(new CustomModel(coreHelper.getFileNameFromUri(uri), uri));
                    }
                }
        }


    }


    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }
}
