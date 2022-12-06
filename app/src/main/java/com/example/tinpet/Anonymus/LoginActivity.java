package com.example.tinpet.Anonymus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tinpet.Administrador.AdmiHomeActivity;
import com.example.tinpet.Cliente.ClienteHomeActivity;
import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText etCorreo;
    EditText etContrasena;
    Button btnLogin;
    Button btnRegister;
    Button btnForgotPassw;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etCorreo = findViewById(R.id.etLoginCorreo);
        etContrasena = findViewById(R.id.etLoginContrasena);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.btn_login);
        btnForgotPassw = findViewById(R.id.btn_forgetpwd);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        btnLogin.setOnClickListener(v -> {
            String email = etCorreo.getText().toString();
            String password = etContrasena.getText().toString();
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(LoginActivity.this,"Complete todos los datos requeridos.",Toast.LENGTH_SHORT).show();
            }else {
                login(email,password);
            }
        });
    }

    public void login(String email,String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    firestore.collection("mascota").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(snapshot -> {
                        Mascota mascota = snapshot.toObject(Mascota.class);
                        if(mascota.getRol().equals("usuario")){
                            //Cliente
                            startActivity(new Intent(LoginActivity.this, ClienteHomeActivity.class));
                            finish();
                        }else if(mascota.getRol().equals("admi")){
                            startActivity(new Intent(LoginActivity.this, AdmiHomeActivity.class));
                            finish();
                        }
                    });
                }else{
                    startActivity(new Intent(LoginActivity.this, NonVerifiedActivity.class));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void goRegister(View view){
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }

    public void goRecoveryPwsd(View view){
        startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
    }

}