package com.example.tinpet.Anonymus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;
import com.google.firebase.storage.StorageReference;

public class RegisterOwnerActivity extends AppCompatActivity {

    EditText nombreDueno,numeroDueno,correoDueno;
    Button btnSiguienteOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_owner);

        nombreDueno = findViewById(R.id.etNombreDuenoRegister);
        numeroDueno = findViewById(R.id.etNumeroDuenoRegister);
        correoDueno = findViewById(R.id.etCorreoDuenoRegister);
        btnSiguienteOwner = findViewById(R.id.btnSiguienteOwner);

        //Recibimos info de la vista previa
        Intent intent = getIntent();
        Mascota mascota = (Mascota) intent.getSerializableExtra("mascota");

        btnSiguienteOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: VALIDAAAAAAAAAAR (:
                mascota.setNombreDuenio(nombreDueno.getText().toString());
                mascota.setNumeroDuenio(Integer.parseInt(numeroDueno.getText().toString()));
                mascota.setCorreoDuenio(correoDueno.getText().toString());

                Intent intent = new Intent(RegisterOwnerActivity.this,RegisterLocationActivity.class);
                intent.putExtra("mascota",mascota);
                startActivity(intent);
                finish();
            }
        });



    }
}