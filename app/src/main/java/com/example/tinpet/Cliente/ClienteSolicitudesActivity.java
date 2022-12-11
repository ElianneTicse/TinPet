package com.example.tinpet.Cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.tinpet.Administrador.AdmiGestionActivity;
import com.example.tinpet.Administrador.AdmiHomeActivity;
import com.example.tinpet.Administrador.AdmiMiPerfilActivity;
import com.example.tinpet.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ClienteSolicitudesActivity extends AppCompatActivity {

    CardView solicitudesPendientes,solicitudesEnviadas;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_solicitudes);
        solicitudesEnviadas = findViewById(R.id.cvClienteSolicitudEnviada);
        solicitudesPendientes = findViewById(R.id.cvClienteSolicitudPendiente);
        setBottomNavigationView();

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

    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavSolicitudesCliente);
        bottomNavigationView.clearAnimation();
        bottomNavigationView.setSelectedItemId(R.id.btnNavMenuClienteSolicitud);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.btnNavMenuClienteSolicitud:
                        return true;
                    case R.id.btnNavMenuAdmiHome:
                        startActivity(new Intent(getApplicationContext(), AdmiHomeActivity.class));
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