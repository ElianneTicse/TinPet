package com.example.tinpet.Cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.tinpet.Adapter.SolicitudAdapter;
import com.example.tinpet.Entity.Solicitudes;
import com.example.tinpet.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ClienteSolicitudesListActivity extends AppCompatActivity {

    TextView tituloSolicitud;
    Query query;
    RecyclerView rvSolicitud;

    FirebaseFirestore firestore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_solicitudes_list);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String type = getIntent().getStringExtra("soli");
        tituloSolicitud = findViewById(R.id.tvTittleSolicitudes);
        rvSolicitud = findViewById(R.id.recycleviewSolicitudes);

        if(type.equals("enviadas")){
            tituloSolicitud.setText("Solicitudes enviadas");
            rvSolicitud.setLayoutManager(new LinearLayoutManager(ClienteSolicitudesListActivity.this));
            query = firestore.collection("solicitudes")
                    .whereEqualTo("iudMe",mAuth.getUid()).whereEqualTo("status","enviado");
        }

        if(type.equals("pendientes")){
            tituloSolicitud.setText("Solicitudes pendientes");
            rvSolicitud.setLayoutManager(new LinearLayoutManager(ClienteSolicitudesListActivity.this));
            query = firestore.collection("solicitudes")
                    .whereEqualTo("iudFriend",mAuth.getUid()).whereEqualTo("status","enviado");
        }

        FirestoreRecyclerOptions<Solicitudes> solicitudesFirestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Solicitudes>().setQuery(query,Solicitudes.class).build();
        SolicitudAdapter solicitudAdapter = new SolicitudAdapter(solicitudesFirestoreRecyclerOptions,type,this);
        solicitudAdapter.notifyDataSetChanged();
        rvSolicitud.setAdapter(solicitudAdapter);
        solicitudAdapter.startListening();
    }

    public void backButtonSolicitudesList(View view){
        onBackPressed();
    }
}