package com.example.tinpet.Administrador;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tinpet.Adapter.TipImageAdapter;
import com.example.tinpet.Entity.Tip;
import com.example.tinpet.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class AdmiNuevoTipActivity extends AppCompatActivity {
    //Para realizar el adapter
    RecyclerView rvSelectedTipIcon;
    RecyclerView.Adapter itemTipImageAdapter;
    RecyclerView.LayoutManager layoutManager;

    int[] tipImage = {R.drawable.ic_bone,R.drawable.ic_dog_food,R.drawable.ic_star_2,R.drawable.ic_heart};
    EditText tituloTip;
    EditText descripcionTip;
    Button btnAgregar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admi_nuevo_tip);
        //Inicio
        rvSelectedTipIcon = findViewById(R.id.rvTipImgNew);
        tituloTip = findViewById(R.id.etTittleTip);
        descripcionTip = findViewById(R.id.etDescripcionTip);
        btnAgregar = findViewById(R.id.btnAddTip);

        //Recibimos Tip de intent
        Intent intenteTip = getIntent();
        String idTip = intenteTip.getStringExtra("tip_id");

        /* TODO: Si esta vacio se creará nuevo tip, si no, se setearán los valores pasados*/
        if(idTip!=null){
            btnAgregar.setText("Actualizar");
            obtenerTip(idTip);
            btnAgregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isInvalid = false;
                    int selectedItem = ((TipImageAdapter) rvSelectedTipIcon.getAdapter()).getSelectedItem();
                    String titulo = tituloTip.getText().toString().trim();
                    String descripcion = descripcionTip.getText().toString().trim();

                    if(titulo.isEmpty()){
                        tituloTip.setError("El titulo no puede estar vacio");
                        tituloTip.requestFocus();
                        isInvalid = true;
                    }

                    if(titulo.length()>40){
                        tituloTip.setError("El titulo no puede contener más de 40 caracteres.");
                        tituloTip.requestFocus();
                        isInvalid = true;
                    }

                    if(descripcion.isEmpty()){
                        descripcionTip.setError("La descripcion no puede estar vacia.");
                        descripcionTip.requestFocus();
                        isInvalid = true;
                    }

                    if(descripcion.length()>300){
                        descripcionTip.setError("Solo se aceptan 300 caracteres.");
                        descripcionTip.requestFocus();
                    }
                    if(isInvalid) return;

                    editarTipFirestore(new Tip(titulo,descripcion,selectedItem),idTip);
                }
            });

        }

        //RecyclerView ItemTip
        rvSelectedTipIcon.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rvSelectedTipIcon.setLayoutManager(layoutManager);
        itemTipImageAdapter = new TipImageAdapter(this,tipImage);
        rvSelectedTipIcon.setAdapter(itemTipImageAdapter);
    }

    public void addTip(View view){
        boolean isInvalid = false;
        int selectedItem = ((TipImageAdapter) rvSelectedTipIcon.getAdapter()).getSelectedItem();
        String titulo = tituloTip.getText().toString().trim();
        String descripcion = descripcionTip.getText().toString().trim();

        if(titulo.isEmpty()){
            tituloTip.setError("El titulo no puede estar vacio");
            tituloTip.requestFocus();
            isInvalid = true;
        }

        if(titulo.length()>40){
            tituloTip.setError("El titulo no puede contener más de 40 caracteres.");
            tituloTip.requestFocus();
            isInvalid = true;
        }

        if(descripcion.isEmpty()){
            descripcionTip.setError("La descripcion no puede estar vacia.");
            descripcionTip.requestFocus();
            isInvalid = true;
        }

        if(descripcion.length()>300){
            descripcionTip.setError("Solo se aceptan 300 caracteres.");
            descripcionTip.requestFocus();
        }
        if(isInvalid) return;

        crearTipFirestore(new Tip(titulo,descripcion,selectedItem));
    }

    public void backButton(View view){
        onBackPressed();
    }

    public void crearTipFirestore(Tip tip){
        FirebaseFirestore.getInstance().collection("tip").add(tip).addOnSuccessListener(unused -> {
//            ocultarCargando();
            Toast.makeText(AdmiNuevoTipActivity.this, "Se añadió el tip: "+ tip.getTitulo(), Toast.LENGTH_SHORT).show();
            Intent intentList = new Intent(AdmiNuevoTipActivity.this,AdmiGestionActivity.class);
            intentList.putExtra("tip",tip);
            startActivity(intentList);
            finish();

        }).addOnFailureListener(e->{
//            ocultarCargando();
            Log.d("msg",e.getMessage());
            Toast.makeText(AdmiNuevoTipActivity.this, "Ocurrió un error en el servidor", Toast.LENGTH_LONG).show();
        });
    }

    public void editarTipFirestore(Tip tip,String id){
        FirebaseFirestore.getInstance().collection("tip").document(id).update(
                "titulo",tip.getTitulo(),
                "descripcion",tip.getDescripcion()
        ).addOnSuccessListener(unused -> {
            Toast.makeText(AdmiNuevoTipActivity.this, "Actualizado exitosamente.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(AdmiNuevoTipActivity.this,AdmiGestionActivity.class));
        }).addOnFailureListener(e -> {
            Toast.makeText(AdmiNuevoTipActivity.this, "Ocurrió un error en el servidor", Toast.LENGTH_LONG).show();
        });

    }

    public void obtenerTip(String id){
        FirebaseFirestore.getInstance().collection("tip").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String titulo = documentSnapshot.getString("titulo");
                String descripcion = documentSnapshot.getString("descripcion");

                tituloTip.setText(titulo);
                descripcionTip.setText(descripcion);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdmiNuevoTipActivity.this, "Ocurrió un error en el servidor", Toast.LENGTH_LONG).show();
            }
        });

    }

}