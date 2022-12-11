package com.example.tinpet.Administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tinpet.Adapter.PopularesAdapter;
import com.example.tinpet.Adapter.TipListAdapter;
import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdmiUsuariosPopularesActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    PopularesAdapter popularesAdapter;
    RecyclerView recyclerView;
    ArrayList<Mascota> mascota_friends = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admi_usuarios_populares);

        usuariosPopulares();

        recyclerView = findViewById(R.id.rvAdmiUsuariosPopulares);
        //Seteamos Adapters
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdmiUsuariosPopularesActivity.this));
        popularesAdapter = new PopularesAdapter(mascota_friends,AdmiUsuariosPopularesActivity.this);
        recyclerView.setAdapter(popularesAdapter);

    }

    public void usuariosPopulares(){
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
                                popularesAdapter.notifyDataSetChanged();
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

    public void backButtonUsuariosPopulares(View view){
        onBackPressed();
    }


}