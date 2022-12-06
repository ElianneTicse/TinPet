package com.example.tinpet.Cliente;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.tinpet.Adapter.RecomendadosAdapter;
import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalTime;
import java.util.ArrayList;

public class ClienteHomeActivity extends AppCompatActivity {

    TextView tv_welcome_static;
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    ArrayList<Mascota> mascotaArrayList;
    FirebaseFirestore db;
    RecomendadosAdapter recomendadosAdapter;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_home);
        setBottomNavigationView();
        tv_welcome_static = findViewById(R.id.tv_welcome_static);
        recyclerView = findViewById(R.id.rvClienteHomeSugerencias);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        EventChangeListener();
    }

    private void EventChangeListener() {
        db.collection("mascota").whereEqualTo("rol","usuario")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error!=null){
                        Log.d("Ha ocurrido un error",error.getMessage());
                        return;
                    }
                    for(DocumentChange doc : value.getDocumentChanges()){
                        if(doc.getType() == DocumentChange.Type.ADDED){
                            if(!doc.getDocument().getId().equals(firebaseAuth.getCurrentUser().getUid())){
                                Mascota mascota = doc.getDocument().toObject(Mascota.class);
                                mascota.setUid(doc.getDocument().getId());
                                mascotaArrayList.add(mascota);
                            }
                        }
                    }
                    recomendadosAdapter = new RecomendadosAdapter(ClienteHomeActivity.this,mascotaArrayList);
                    recyclerView.setAdapter(recomendadosAdapter);
                }
            });
    }

    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavMenuCliente);
        bottomNavigationView.clearAnimation();
        bottomNavigationView.setSelectedItemId(R.id.btnNavMenuClienteHome);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.btnNavMenuClienteHome:
                        return true;
                    case R.id.btnNavMenuClienteProfile:
                        startActivity(new Intent(getApplicationContext(), ClienteProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.btnNavMenuClienteSolicitud:
                        startActivity(new Intent(getApplicationContext(), ClienteSolicitudesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.btnNavMenuClienteTips:
                        startActivity(new Intent(getApplicationContext(), ClienteTipsActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void mostrarSaludo(){
        String saludo = "Buenas noches,";
        LocalTime localTime = LocalTime.now();
        if(localTime.getHour()>=6 && localTime.getHour()<12){
            saludo = "Buenos días,";
        } else if(localTime.getHour() >= 12 && localTime.getHour()<19){
            saludo = "Buenas tardes, ";
        }
        tv_welcome_static.setText(saludo);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        mostrarSaludo();
    }


}


