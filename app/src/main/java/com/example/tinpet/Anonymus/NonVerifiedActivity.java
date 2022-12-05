package com.example.tinpet.Anonymus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tinpet.Cliente.ClienteHomeActivity;
import com.example.tinpet.R;

public class NonVerifiedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_verified);
    }

    public void backLogin(View view){
        startActivity(new Intent(NonVerifiedActivity.this, LoginActivity.class));
    }
}