package com.example.tinpet.Administrador;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.tinpet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdmiPeludosSeccionadosActivity extends AppCompatActivity {

    TextView nPerritos, nGatitos;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admi_peludos_seccionados);
        nPerritos = findViewById(R.id.tvAdmiDogsNumber);
        nGatitos = findViewById(R.id.tvAdmiCatsNumber);
        firestore = FirebaseFirestore.getInstance();

        CollectionReference collection = firestore.collection("mascota");
        Query query = collection.whereEqualTo("tipo", "Gato");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                Log.d(TAG, "Count: " + snapshot.getCount());
                nGatitos.setText(Long.toString(snapshot.getCount()));
            } else {
                Log.d(TAG, "Count failed: ", task.getException());
            }
        });

        Query queryP = collection.whereEqualTo("tipo", "Perro");
        AggregateQuery countQueryP = queryP.count();
        countQueryP.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                Log.d(TAG, "Count: " + snapshot.getCount());
                nPerritos.setText(Long.toString(snapshot.getCount()));
            } else {
                Log.d(TAG, "Count failed: ", task.getException());
            }
        });




    }
    public void backButtonA(View view){
        onBackPressed();
    }


}