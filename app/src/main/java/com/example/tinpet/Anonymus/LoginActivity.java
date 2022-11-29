package com.example.tinpet.Anonymus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.tinpet.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText etCorreo;
    EditText etContrasena;
    Button btnLogin;
    Button btnRegister;
    Button btnForgotPassw;

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


    }
}