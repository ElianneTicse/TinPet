package com.example.tinpet.Administrador;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.example.tinpet.Adapter.UsuariosListAdapter;
import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminUsuariosListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Mascota> mascotaArrayList;
    UsuariosListAdapter usuariosListAdapter;
    FirebaseFirestore db;
    boolean isBusy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_usuarios_list);
        recyclerView = findViewById(R.id.rvAdmiUsuariosList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        mascotaArrayList = new ArrayList<>();
        usuariosListAdapter = new UsuariosListAdapter(AdminUsuariosListActivity.this,mascotaArrayList);
        recyclerView.setAdapter(usuariosListAdapter);
        EventChangeListener();
    }

    private void EventChangeListener() {
        db.collection("mascota").whereNotEqualTo("rol","admi")
        .orderBy("nombreMascota", Query.Direction.ASCENDING)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error!=null){
                        Log.d("Ha ocurrido un error",error.getMessage());
                        return;
                    }
                    for(DocumentChange doc : value.getDocumentChanges()){
                        if(doc.getType() == DocumentChange.Type.ADDED){
                            mascotaArrayList.add(doc.getDocument().toObject(Mascota.class));
                        }
                        usuariosListAdapter.notifyDataSetChanged();
                    }
                }
            });
    }

    public void backButtonUsuariosList(View view){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (!isBusy){
            super.onBackPressed();
        }
    }

}