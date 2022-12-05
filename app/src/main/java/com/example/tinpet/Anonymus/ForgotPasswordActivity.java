package com.example.tinpet.Anonymus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tinpet.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etCorreo;
    Button btnSiguienteRecovery;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        etCorreo = findViewById(R.id.etCorreoRecovery);
        btnSiguienteRecovery = findViewById(R.id.btnSiguienteRecovery);

        firebaseAuth = FirebaseAuth.getInstance();

        btnSiguienteRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = etCorreo.getText().toString().trim();
                firebaseAuth.sendPasswordResetEmail(correo)
                    .addOnCompleteListener( task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPasswordActivity.this, "Se ha enviado un enlace de recuperación a su correo", Toast.LENGTH_SHORT).show();
                            Log.d("pwdRecover", "Envío de enlace de recuperación exitoso");
                            startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(ForgotPasswordActivity.this, "No existe ningun usuario registrado con este correo", Toast.LENGTH_SHORT).show();
                            Log.e("pwdRecover", task.getException().getMessage());
                        }
                    });
            }
        });
    }

    public void goRegisterPswd(View view){
        startActivity(new Intent(ForgotPasswordActivity.this,RegisterActivity.class));
        finish();
    }



}