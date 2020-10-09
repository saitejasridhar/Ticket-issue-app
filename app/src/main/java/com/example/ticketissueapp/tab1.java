package com.example.ticketissueapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;



public class tab1 extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private static FirebaseFirestore mDatabase;
    private FirestoreRecyclerAdapter<ticket, tab1.DataViewHolder> adapter;
    private Query query;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab1,container,false);


        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mDatabase = FirebaseFirestore.getInstance();
        final CollectionReference datasRef = mDatabase.collection("tickets");
        query = datasRef;








        return view;
    }

    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getActivity().getBaseContext(), ActivityToOpen));
    }

    @Override
    public void onStart(){
        super.onStart();
        FirestoreRecyclerOptions<ticket> options = new FirestoreRecyclerOptions.Builder<ticket>()
                .setQuery(query, ticket.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<ticket, tab1.DataViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull tab1.DataViewHolder dataViewHolder, int i, @NonNull ticket data) {
                String usid1 = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (data.getUserid().equals(usid1)) {
                    dataViewHolder.setData(data.getTissue(), data.getTtime(), data.getTaddress(), data.getTissue_desc());
                }
                else{
                    dataViewHolder.rootView.setVisibility(View.GONE);
                    dataViewHolder.rotView.setVisibility(View.GONE);
                    dataViewHolder.rtView.setVisibility(View.GONE);


                }


            }

            @NonNull
            @Override
            public tab1.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_item, parent, false);

                return new tab1.DataViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    public void onStop(){
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }
    private class DataViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout.LayoutParams params;
        public LinearLayout rootView;
        public LinearLayout rotView;
        public LinearLayout rtView;



        private View view;
        DataViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            rotView=itemView.findViewById(R.id.rootView2);
            rootView = itemView.findViewById(R.id.rootView1);
            rtView = itemView.findViewById(R.id.rootView3);


        }
        void setData(String dataname, String datatime,String dataaddress,String datadesc) {

            TextView name = view.findViewById(R.id.tname);
            TextView time = view.findViewById(R.id.ttime);
            TextView address = view.findViewById(R.id.taddress);
            TextView desc = view.findViewById(R.id.tissuedesc);


            name.setText(dataname);
            time.setText(datatime + "  .");
            address.setText(dataaddress);
            desc.setText(datadesc);


        }
    }
}





