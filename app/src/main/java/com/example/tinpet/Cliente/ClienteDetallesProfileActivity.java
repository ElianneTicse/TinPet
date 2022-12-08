package com.example.tinpet.Cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.Entity.Solicitudes;
import com.example.tinpet.R;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClienteDetallesProfileActivity extends AppCompatActivity {

    TextView nombreMascota,nickname,direccion,nombreCliente,telefono,sobreMascota,razaMascota,edadMascota,tituloUbicacion;
    ImageView imgSexo;
    ImageButton btnWhatsapp;
    Button btnsolicitud;
    MapView mapView;
    List<String> listaAmigos;
    ImageSlider imgSlider;

    String pendiente1 = "";
    String idsoli;

    FirebaseFirestore firestore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_detalles_profile);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        nombreMascota = findViewById(R.id.tvnombreMascotaCliente);
        nickname = findViewById(R.id.tvnicknameCliente);
        direccion = findViewById(R.id.tvdireccionCliente);
        nombreCliente = findViewById(R.id.tvnombreDuenioCliente);
        telefono = findViewById(R.id.tvtelefonoCliente);
        sobreMascota = findViewById(R.id.tvSobreMascotaCliente);
        edadMascota = findViewById(R.id.tvedadMascotaCliente);
        razaMascota = findViewById(R.id.tvrazaMascotaCliente);
        imgSexo = findViewById(R.id.imageVievSexo);
        btnWhatsapp = findViewById(R.id.imgbtnwassp);
        tituloUbicacion = findViewById(R.id.ubicacionclienteTitulo);
        btnsolicitud = findViewById(R.id.btnenviarSolicitud);
        mapView = findViewById(R.id.mapView2);
        imgSlider = findViewById(R.id.isPetImages);


        String id = getIntent().getStringExtra("id");
        String solicitud = getIntent().getStringExtra("solicitud");
        idsoli = getIntent().getStringExtra("idsoli");


        firestore.collection("mascota").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Mascota mascota = documentSnapshot.toObject(Mascota.class);
                        nombreMascota.setText(mascota.getNombreMascota());
                        System.out.println(mascota.getNombreDuenio());
                        nombreCliente.setText(mascota.getNombreDuenio());
                        nickname.setText(mascota.getNickname());
                        direccion.setText(mascota.getUbicacion());
                        telefono.setText(mascota.getNumeroDuenio());
                        sobreMascota.setText(mascota.getSobreMascota());
                        razaMascota.setText(mascota.getRaza());
                        edadMascota.setText(mascota.getEdad());
                        String sexo = mascota.getSexo();

                        firestore.collection("solicitudes").whereEqualTo("iudMe",mAuth.getUid()).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            Log.d("msg","entroo");
                                            for(QueryDocumentSnapshot document : task.getResult()){
                                                if(document.getString("iudFriend").equals(id)){
                                                    pendiente1 = document.getString("status");
                                                    Log.d("msg",pendiente1);
                                                    if(pendiente1.equals("enviado")){
                                                        Log.d("msg","entroo 2do if");
                                                        idsoli = document.getId();
                                                        btnsolicitud.setText("Eliminar solicitud :(");

                                                    }

                                                }
                                            }
                                        }
                                    }
                                });

                        ArrayList<SlideModel> slideModels = new ArrayList<>();
                        for (String url : mascota.getUrlFotos()){
                            slideModels.add(new SlideModel(url, ScaleTypes.CENTER_CROP));
                        }

                        imgSlider.setImageList(slideModels);
                        List<String> amigos = mascota.getAmigos();
                        if(sexo.equals("Macho")){
                            imgSexo.setImageResource(R.drawable.sexo_macho);
                        }else{
                            imgSexo.setImageResource(R.drawable.sexo_hembra);
                        }
                        if (amigos.contains(mAuth.getUid())){
                            telefono.setVisibility(View.VISIBLE);
                            btnWhatsapp.setVisibility(View.VISIBLE);
                            tituloUbicacion.setVisibility(View.VISIBLE);
                            mapView.setVisibility(View.VISIBLE);
                            btnsolicitud.setVisibility(View.GONE);
                        }
                        if(solicitud.equals("pendiente")){
                            btnsolicitud.setText("Aceptar solicitud ☺");
                        }
                        if(solicitud.equals("enviada")){
                            btnsolicitud.setText("Eliminar solicitud :(");
                        }

                    }
                });

        btnsolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(solicitud.equals("pendiente")){
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("status","aceptado");
                    firestore.collection("solicitudes").document(idsoli).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void unused) {
                            firestore.collection("mascota").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Mascota mascota = documentSnapshot.toObject(Mascota.class);
                                    listaAmigos = mascota.getAmigos();
                                    listaAmigos.add(id);
                                    HashMap<String,Object> map1 = new HashMap<>();
                                    map.put("amigos",listaAmigos);
                                    firestore.collection("mascota").document(mAuth.getUid()).update(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ClienteDetallesProfileActivity.this,"Solicitud aceptada",Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(ClienteDetallesProfileActivity.this,ClienteHomeActivity.class));
                                            finish();
                                        }
                                    });

                                }
                            });


                        }
                    });

                }else if(solicitud.equals("enviada") || pendiente1.equals("enviado")){
                    firestore.collection("solicitudes").document(idsoli).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ClienteDetallesProfileActivity.this,"Solicitud eliminada exitosamente",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ClienteDetallesProfileActivity.this,ClienteHomeActivity.class));
                            finish();
                        }
                    });
                }else {
                    Solicitudes solicitudes = new Solicitudes(id,mAuth.getUid(),"enviado");
                    firestore.collection("solicitudes").add(solicitudes).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(ClienteDetallesProfileActivity.this,"Solicitud enviada exitosamente",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ClienteDetallesProfileActivity.this,ClienteHomeActivity.class));
                            finish();
                        }
                    });
                }
            }
        });



    }

    public void backButtonProfileDetalles(View view){
        onBackPressed();
    }
}