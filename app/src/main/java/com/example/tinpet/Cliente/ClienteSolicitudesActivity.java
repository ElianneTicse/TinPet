package com.example.tinpet.Cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tinpet.R;

public class ClienteSolicitudesActivity extends AppCompatActivity {

    CardView solicitudesPendientes,solicitudesEnviadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_solicitudes);
        solicitudesEnviadas = findViewById(R.id.cvClienteSolicitudEnviada);
        solicitudesPendientes = findViewById(R.id.cvClienteSolicitudPendiente);

        solicitudesPendientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ClienteSolicitudesActivity.this,ClienteSolicitudesListActivity.class);
                i.putExtra("soli","pendientes");
                startActivity(i);
                finish();
            }
        });

        solicitudesEnviadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  i = new Intent(ClienteSolicitudesActivity.this,ClienteSolicitudesListActivity.class);
                i.putExtra("soli","enviadas");
                startActivity(i);
                finish();
            }
        });

    }
}