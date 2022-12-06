package com.example.tinpet.Cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ClienteProfileActivity extends AppCompatActivity {


    EditText nombreMascota,edadMascota,razaMascota,sexoMascota,sobreMascota,direccion;
    TextView nickname;
    GridLayout fotoeditar;
    ImageSlider imageSlider;
    BottomNavigationView bottomNavigationView;

    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    Button btnactualizar,btnlogout,btnEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_profile);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        nombreMascota = findViewById(R.id.etNombreMiPerfil);
        edadMascota = findViewById(R.id.etEdadMiPerfil);
        razaMascota = findViewById(R.id.etRazaMiPerfil);
        sexoMascota = findViewById(R.id.etSexoMiPerfil);
        sobreMascota = findViewById(R.id.etDescripcionMiPerfil);
        direccion = findViewById(R.id.etUbicacionMiPerfil);
        btnactualizar = findViewById(R.id.btnActualizar);
        btnlogout = findViewById(R.id.btnLogout);
        btnEditar = findViewById(R.id.btnEditar);
        imageSlider = findViewById(R.id.isPetImagesMiPerfil);
        fotoeditar = findViewById(R.id.glFotosEditar);

        firestore.collection("mascota").document(mAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Mascota mascota = documentSnapshot.toObject(Mascota.class);
                        nombreMascota.setText(mascota.getNombreMascota());
                        //nickname.setText(mascota.getNickname());
                        direccion.setText(mascota.getUbicacion());
                        sobreMascota.setText(mascota.getSobreMascota());
                        razaMascota.setText(mascota.getRol());
                        edadMascota.setText(mascota.getEdad());
                        sexoMascota.setText(mascota.getSexo());

                        nombreMascota.setFocusable(false);
                        nombreMascota.setTextIsSelectable(false);
                        direccion.setFocusable(false);
                        direccion.setTextIsSelectable(false);
                        sobreMascota.setFocusable(false);
                        sobreMascota.setTextIsSelectable(false);
                        razaMascota.setFocusable(false);
                        razaMascota.setTextIsSelectable(false);
                        edadMascota.setFocusable(false);
                        edadMascota.setTextIsSelectable(false);
                        sexoMascota.setFocusable(false);
                        sexoMascota.setTextIsSelectable(false);

                    }
                });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombreMascota.setFocusable(true);
                nombreMascota.setTextIsSelectable(true);
                direccion.setFocusable(true);
                direccion.setTextIsSelectable(true);
                sobreMascota.setFocusable(true);
                sobreMascota.setTextIsSelectable(true);
                razaMascota.setFocusable(true);
                razaMascota.setTextIsSelectable(true);
                edadMascota.setFocusable(true);
                edadMascota.setTextIsSelectable(true);
                sexoMascota.setFocusable(true);
                sexoMascota.setTextIsSelectable(true);
                imageSlider.setVisibility(View.GONE);
                fotoeditar.setVisibility(View.VISIBLE);
                btnactualizar.setVisibility(View.VISIBLE);
                btnlogout.setVisibility(View.GONE);
                btnEditar.setVisibility(View.GONE);
            }
        });


        btnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = nombreMascota.getText().toString();
                String ubicacion = direccion.getText().toString();
                String descripcion = sobreMascota.getText().toString();
                String raza = razaMascota.getText().toString();
                String edad = edadMascota.getText().toString();
                String sexo = sexoMascota.getText().toString();

                HashMap<String,Object> map = new HashMap<>();
                map.put("edad",edad);
                map.put("nombreMascota",nombre);
                map.put("rol",raza);
                map.put("sexo",sexo);
                map.put("sobreMascota",descripcion);
                map.put("ubicacion",ubicacion);
                firestore.collection("mascota").document(mAuth.getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        nombreMascota.setFocusable(false);
                        nombreMascota.setTextIsSelectable(false);
                        direccion.setFocusable(false);
                        direccion.setTextIsSelectable(false);
                        sobreMascota.setFocusable(false);
                        sobreMascota.setTextIsSelectable(false);
                        razaMascota.setFocusable(false);
                        razaMascota.setTextIsSelectable(false);
                        edadMascota.setFocusable(false);
                        edadMascota.setTextIsSelectable(false);
                        sexoMascota.setFocusable(false);
                        sexoMascota.setTextIsSelectable(false);
                        imageSlider.setVisibility(View.VISIBLE);
                        fotoeditar.setVisibility(View.GONE);
                        btnactualizar.setVisibility(View.GONE);
                        btnlogout.setVisibility(View.VISIBLE);
                        btnEditar.setVisibility(View.VISIBLE);
                        Toast.makeText(ClienteProfileActivity.this,"Actualizado correctamente",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        setBottomNavigationView();


    }

    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavProfileCliente);
        bottomNavigationView.clearAnimation();
        bottomNavigationView.setSelectedItemId(R.id.btnNavMenuClienteProfile);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.btnNavMenuClienteHome:
                        startActivity(new Intent(getApplicationContext(), ClienteHomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.btnNavMenuClienteProfile:
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
}