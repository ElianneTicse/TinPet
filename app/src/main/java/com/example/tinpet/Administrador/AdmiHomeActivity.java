package com.example.tinpet.Administrador;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.tinpet.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.time.LocalTime;

public class AdmiHomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView tvSaludo;
    TextView tvNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admi_home);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setBottomNavigationView();
        tvSaludo = findViewById(R.id.tv_welcome_static);

    }

    public void verMasPopulares(View view){
        Intent vistaPopulares = new Intent(this,AdmiHomeActivity.class);
        startActivity(vistaPopulares);
        finish();
    }

    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavMenuAdmiAct);
        bottomNavigationView.clearAnimation();
        bottomNavigationView.setSelectedItemId(R.id.btnNavMenuAdmiHome);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.btnNavMenuAdmiHome:
                        return true;
                    case R.id.btnNavMenuAdmiReportes:
                        startActivity(new Intent(getApplicationContext(), AdmiReportesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.btnNavMenuAdmiGestion:
                        startActivity(new Intent(getApplicationContext(), AdmiGestionActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.btnNavMenuAdmiProfile:
                        startActivity(new Intent(getApplicationContext(), AdmiMiPerfilActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void mostrarSaludo(){
        String saludo = "Buenas noches,";
        LocalTime localTime = LocalTime.now();
        if(localTime.getHour()>=6 && localTime.getHour()<12){
            saludo = "Buenos días,";
        } else if(localTime.getHour() >= 12 && localTime.getHour()<19){
            saludo = "Buenas tardes, ";
        }
        tvSaludo.setText(saludo);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        mostrarSaludo();
    }
}