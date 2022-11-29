package com.example.tinpet.Anonymus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tinpet.R;

public class RegisterWelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_welcome);
    }

    public void sigIn(View view){
        startActivity(new Intent(RegisterWelcomeActivity.this,LoginActivity.class));
    }

}