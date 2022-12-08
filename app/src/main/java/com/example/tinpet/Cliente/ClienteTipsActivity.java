package com.example.tinpet.Cliente;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.tinpet.Adapter.RecomendadosAdapter;
import com.example.tinpet.Adapter.TipListUsuarioAdapter;
import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.Entity.Tip;
import com.example.tinpet.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ClienteTipsActivity extends AppCompatActivity {

    RecyclerView rvTipList;
    FirebaseFirestore db;
    ArrayList<Tip> tipArrayList = new ArrayList<>();
    TipListUsuarioAdapter tipListUsuarioAdapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_tips);
        rvTipList = findViewById(R.id.rvClienteTipsList);
        setBottomNavigationView();

        rvTipList.setHasFixedSize(true);
        rvTipList.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        EventChangeListener();
    }

    private void EventChangeListener() {
        db.collection("tip")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            Log.d("Ha ocurrido un error",error.getMessage());
                            return;
                        }
                        for(DocumentChange doc : value.getDocumentChanges()){
                            if(doc.getType() == DocumentChange.Type.ADDED){
                                Tip tip = doc.getDocument().toObject(Tip.class);
                                tipArrayList.add(tip);

                            }
                        }
                        tipListUsuarioAdapter = new TipListUsuarioAdapter(ClienteTipsActivity.this,tipArrayList);
                        rvTipList.setAdapter(tipListUsuarioAdapter);
                    }
                });
    }

    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavTipsCliente);
        bottomNavigationView.clearAnimation();
        bottomNavigationView.setSelectedItemId(R.id.btnNavMenuClienteTips);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.btnNavMenuClienteTips:
                        return true;
                    case R.id.btnNavMenuClienteProfile:
                        startActivity(new Intent(getApplicationContext(), ClienteProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.btnNavMenuClienteSolicitud:
                        startActivity(new Intent(getApplicationContext(), ClienteSolicitudesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.btnNavMenuClienteHome:
                        startActivity(new Intent(getApplicationContext(), ClienteHomeActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }


}