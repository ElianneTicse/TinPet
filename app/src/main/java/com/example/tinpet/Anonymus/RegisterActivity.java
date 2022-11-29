package com.example.tinpet.Anonymus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tinpet.Adapter.TipImageAdapter;
import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    EditText inputNickname;
    EditText inputNombre;
    EditText inputEdad;
    EditText inputRaza;
    EditText inputDescripcion;
    Spinner spinnerTipo;
    Spinner spinnerSexo;
    Button btnSiguiente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputNickname = findViewById(R.id.etNicknameRegister);
        inputNombre = findViewById(R.id.etNombreRegister);
        inputEdad = findViewById(R.id.etEdadRegister);
        inputRaza = findViewById(R.id.etRazaRegister);
        inputDescripcion = findViewById(R.id.etDescripciónRegister);
        spinnerTipo = findViewById(R.id.spTipRegister);
        spinnerSexo = findViewById(R.id.spSexoRegister);
        btnSiguiente = findViewById(R.id.btnRegisterPet);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Validaciones
                boolean isInvalid = false;
                String nickname = inputNickname.getText().toString().trim();
                String nombre = inputNombre.getText().toString().trim();
                int edad = Integer.parseInt(inputEdad.getText().toString());
                String raza = inputRaza.getText().toString().trim();
                String tipo = spinnerTipo.getSelectedItem().toString();
                String sexo = spinnerSexo.getSelectedItem().toString();
                String aboutMe = inputDescripcion.getText().toString().trim();


                if(nickname.isEmpty()){
                    inputNickname.setError("El nombre no puede estar vacio.");
                    inputNickname.requestFocus();
                    isInvalid = true;
                }

                if(nickname.length()>8){
                    inputNickname.setError("Solo se admiten 8 caracteres.");
                    inputNickname.requestFocus();
                    isInvalid = true;
                }

                if(nombre.isEmpty()){
                    inputNombre.setError("El nombre no puede estar vacia.");
                    inputNombre.requestFocus();
                    isInvalid = true;
                }

                if(nombre.length()>8){
                    inputNombre.setError("Solo se admiten 8 caracteres.");
                    inputNombre.requestFocus();
                }
                if(raza.isEmpty()){
                    inputRaza.setError("El nombre no puede estar vacia.");
                    inputRaza.requestFocus();
                    isInvalid = true;
                }

                if(raza.length()>10){
                    inputRaza.setError("Solo se admiten 10 caracteres.");
                    inputNombre.requestFocus();
                }
                if(isInvalid) return;


                //Enviamos info
                Intent intent = new Intent(RegisterActivity.this,RegisterOwnerActivity.class);
                Mascota mascota = new Mascota(nickname,tipo,sexo,nombre,edad,aboutMe);
                intent.putExtra("mascota",mascota);
                startActivity(intent);
            }
        });
    }
}