package com.example.tinpet.Administrador;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

public class AdmiHomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView tvSaludo;
    TextView tvNombre;
    TextView tvNumero;
    TextView nombreUsuarioPopular,nombreUsuarioNickname,nombreUsuarioLocation,numeroAmigos;
    ArrayList<Mascota> mascota_friends = new ArrayList<>();
    ShapeableImageView image;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admi_home);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setBottomNavigationView();
        tvSaludo = findViewById(R.id.tv_welcome_static);
        tvNombre = findViewById(R.id.tv_welcome_admi);
        tvNumero = findViewById(R.id.tvAdmiNuevosUsuariosNumber);
        nombreUsuarioPopular = findViewById(R.id.tvAdmiUsuarioPopularName);
        nombreUsuarioNickname = findViewById(R.id.tvAdmiUsuarioPopularNickname);
        nombreUsuarioLocation = findViewById(R.id.tvAdmiUsuarioPopularLocation);
        numeroAmigos = findViewById(R.id.tvAdmiUsuarioPopularFriends);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firestore.collection("mascota").document(firebaseAuth.getUid()).get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Mascota mascota = documentSnapshot.toObject(Mascota.class);
                    //Seteamos los datos del administrador
                    tvNombre.setText(mascota.getNombreDuenio());
                }
            });

        CollectionReference collection = firestore.collection("mascota");
        Query query = collection.whereEqualTo("rol", "usuario");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                Log.d(TAG, "Count: " + snapshot.getCount());
                tvNumero.setText(Long.toString(snapshot.getCount()));
            } else {
                Log.d(TAG, "Count failed: ", task.getException());
            }
        });

        //Obtenemos usuario más popular

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://us-central1-tinpet-dde08.cloudfunctions.net/populares";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Map<String,Double> usuario_amigos = new Gson().fromJson(response, Map.class);
                        System.out.println(usuario_amigos);
                        Log.d("msg",usuario_amigos.toString());
                        FirebaseFirestore db = firestore.getInstance();

                        //Obtenemos mascotas
                        usuario_amigos.forEach((k,v)->{
                            System.out.println("Key:" +k);
                            System.out.println("Value:" +v);

                            DocumentReference docRef = db.collection("mascota").document(k);
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Mascota mascota = documentSnapshot.toObject(Mascota.class);
                                    if (mascota != null) {
                                        mascota.setnAmigos((Double) v);
                                        mascota_friends.add(mascota);
                                    }
                                }
                            });
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(stringRequest);
    }

    public void verMasPopulares(View view){
        Intent vistaPopulares = new Intent(this,AdmiUsuariosPopularesActivity.class);
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