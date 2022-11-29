package com.example.tinpet.Anonymus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;

public class RegisterLocationActivity extends AppCompatActivity {

    EditText location;
    Button btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_location);

        location = findViewById(R.id.etLocationRegister);
        btnSiguiente = findViewById(R.id.btnSiguienteLocation);

        Intent intent = getIntent();
        Mascota mascota = (Mascota) intent.getSerializableExtra("mascota");

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: VALIDAR
                mascota.setUbicacion(location.getText().toString());
                Intent intent = new Intent(RegisterLocationActivity.this,PasswordActivity.class);
                intent.putExtra("mascota",mascota);
                startActivity(intent);
            }
        });

    }

}