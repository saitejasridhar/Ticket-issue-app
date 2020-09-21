package com.example.ticketissueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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
import com.google.protobuf.StringValue;

public class Profile extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Button editpro;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usid = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
    private CollectionReference notebookRef = db.collection("users").document(usid).collection("tickets");
    private RecyclerView recyclerView;
    private static FirebaseFirestore mDatabase;
    private FirestoreRecyclerAdapter<ticket, DataViewHolder> adapter;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbarp);

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseFirestore.getInstance();
        final CollectionReference datasRef = mDatabase.collection("users").document(usid).collection("tickets");
        query = datasRef;


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

    @Override
    protected void onStart(){
        super.onStart();
        FirestoreRecyclerOptions<ticket> options = new FirestoreRecyclerOptions.Builder<ticket>()
                .setQuery(query, ticket.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<ticket, DataViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DataViewHolder dataViewHolder, int i, @NonNull ticket data) {
                dataViewHolder.setData(data.getTissue(),data.getTtime(),data.getTaddress(),data.getTissue_desc());
            }
            @NonNull
            @Override
            public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_item, parent, false);
                return new DataViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }
    private class DataViewHolder extends RecyclerView.ViewHolder {
        private View view;
        DataViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
        void setData(String dataname, String datatime,String dataaddress,String datadesc) {
            TextView name = view.findViewById(R.id.tname);
            TextView time = view.findViewById(R.id.ttime);
            TextView address=view.findViewById(R.id.taddress);
            TextView desc=view.findViewById(R.id.tissuedesc);
            name.setText(dataname);
            time.setText(datatime);
            address.setText(dataaddress);
            desc.setText(datadesc);
        }
    }

}