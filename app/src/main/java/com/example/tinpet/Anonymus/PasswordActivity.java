package com.example.tinpet.Anonymus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PasswordActivity extends AppCompatActivity {

    EditText password,passwordRepeat;
    Button btnRegister;

    CollectionReference ref;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        password = findViewById(R.id.etPasswordRegistro);
        passwordRepeat = findViewById(R.id.etPasswordRepeatRegistro);
        btnRegister = findViewById(R.id.btnRegister_final);


        ref = FirebaseFirestore.getInstance().collection("mascota");
        fAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        Mascota mascota = (Mascota) intent.getSerializableExtra("mascota");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:VERIFICAR
                fAuth.createUserWithEmailAndPassword(mascota.getCorreoDuenio(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            ref.document(fAuth.getCurrentUser().getUid()).set(mascota)
                                    .addOnSuccessListener(documentReference -> {
                                        fAuth.getCurrentUser().sendEmailVerification();
                                        startActivity(new Intent(PasswordActivity.this,RegisterWelcomeActivity.class));
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(PasswordActivity.this, "No se pudo completar el registro", Toast.LENGTH_LONG).show();
                                    });

                        }else{
                            Toast.makeText(PasswordActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}