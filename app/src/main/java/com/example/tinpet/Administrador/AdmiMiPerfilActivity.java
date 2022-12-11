package com.example.tinpet.Administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tinpet.Anonymus.LoginActivity;
import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.common.subtyping.qual.Bottom;

public class AdmiMiPerfilActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView nombre,correo;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admi_mi_perfil);
        setBottomNavigationView();

        nombre = findViewById(R.id.tvNombreAdmiPerfil);
        correo = findViewById(R.id.tvCorreoAdmiPerfil);
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firestore.collection("mascota").document(firebaseAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Mascota mascota = documentSnapshot.toObject(Mascota.class);
                        //Seteamos los datos del administrador
                        nombre.setText(mascota.getNombreDuenio());
                        correo.setText(mascota.getCorreoDuenio());
                    }
                });
    }

    public void cerrarSesion(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(AdmiMiPerfilActivity.this, "Has cerrado sesión", Toast.LENGTH_SHORT).show();
        sharedPreferences.edit().remove("user").apply();
        startActivity(new Intent(AdmiMiPerfilActivity.this, LoginActivity.class));
        ActivityCompat.finishAffinity(AdmiMiPerfilActivity.this);
        finish();
    }

    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavPerfilAdmiAct);
        bottomNavigationView.clearAnimation();
        bottomNavigationView.setSelectedItemId(R.id.btnNavMenuAdmiProfile);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.btnNavMenuAdmiProfile:
                        return true;
                    case R.id.btnNavMenuAdmiHome:
                        startActivity(new Intent(getApplicationContext(), AdmiHomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.btnNavMenuAdmiGestion:
                        startActivity(new Intent(getApplicationContext(), AdmiGestionActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.btnNavMenuAdmiReportes:
                        startActivity(new Intent(getApplicationContext(), AdmiReportesActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }


}