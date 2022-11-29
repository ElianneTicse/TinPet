package com.example.tinpet.Administrador;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;

import com.example.tinpet.Adapter.TipImageAdapter;
import com.example.tinpet.Adapter.TipListAdapter;
import com.example.tinpet.Entity.Tip;
import com.example.tinpet.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdmiGestionActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    TipListAdapter tipListAdapter;

    public AdmiGestionActivity() {
        super();
    }

    @Override
    protected void onStart() {
        super.onStart();
        tipListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        tipListAdapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admi_gestion);
        recyclerView = findViewById(R.id.rvAdmiTipList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = db.collection("tip");
        FirestoreRecyclerOptions<Tip> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Tip>().setQuery(query,Tip.class).build();
        tipListAdapter = new TipListAdapter(firestoreRecyclerOptions);
        tipListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(tipListAdapter);


    }

    public void addNewTip(View view){
        startActivity(new Intent(AdmiGestionActivity.this,AdmiNuevoTipActivity.class));
        finish();
    }

    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavTipsAdmiAct);
        bottomNavigationView.clearAnimation();
        bottomNavigationView.setSelectedItemId(R.id.btnNavMenuAdmiGestion);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.btnNavMenuAdmiHome:
                        return true;
                    case R.id.btnNavMenuAdmiReportes:
                        startActivity(new Intent(getApplicationContext(), AdmiReportesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.btnNavMenuAdmiGestion:
                        startActivity(new Intent(getApplicationContext(), AdmiGestionActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.btnNavMenuAdmiProfile:
                        startActivity(new Intent(getApplicationContext(), AdmiMiPerfilActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
}