package com.example.tinpet.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tinpet.Cliente.ClienteDetallesProfileActivity;
import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.Entity.Solicitudes;
import com.example.tinpet.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SolicitudAdapter extends FirestoreRecyclerAdapter<Solicitudes,SolicitudAdapter.ViewHolder> {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    String type;
    Activity actividad;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SolicitudAdapter(@NonNull FirestoreRecyclerOptions<Solicitudes> options, String data, Activity activity) {
        super(options);
        type = data;
        this.actividad = activity;


    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Solicitudes solicitud) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();


        if(type.equals("enviadas")){
            firestore.collection("mascota").document(solicitud.getIudFriend()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Mascota mascota = documentSnapshot.toObject(Mascota.class);
                    holder.nombre.setText(mascota.getNombreMascota());
                    holder.nick.setText(mascota.getNickname());
                    holder.ubicacion.setText(mascota.getUbicacion());
                    holder.time.setText("hola");
                    holder.btnVerPerfil.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(actividad, ClienteDetallesProfileActivity.class);
                            i.putExtra("id",solicitud.getIudFriend());
                            i.putExtra("solicitud","enviada");
                            i.putExtra("idsoli",id);
                            actividad.startActivity(i);
                            actividad.finish();
                        }
                    });
                }
            });
        }else {
            firestore.collection("mascota").document(solicitud.getIudMe()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Mascota mascota = documentSnapshot.toObject(Mascota.class);
                    holder.nombre.setText(mascota.getNombreMascota());
                    holder.nick.setText(mascota.getNickname());
                    holder.ubicacion.setText(mascota.getUbicacion());
                    holder.time.setText("hola");
                    holder.btnVerPerfil.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(actividad, ClienteDetallesProfileActivity.class);
                            i.putExtra("id",solicitud.getIudMe());
                            i.putExtra("solicitud","pendiente");
                            i.putExtra("idsoli",id);
                            actividad.startActivity(i);
                            actividad.finish();
                        }
                    });

                    holder.btnRechazar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("status","rechazado");
                            firestore.collection("solicitudes").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(actividad,"Solicitud Rechazada",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(type.equals("pendientes")){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_solicitudes_pendientes,parent,false);
        }else{
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_solicitudes_enviadas,parent,false);
        }

        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombre,nick,ubicacion,time;
        Button btnVerPerfil;
        Button btnRechazar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvClienteSoliName);
            nick = itemView.findViewById(R.id.tvClienteSoliNickname);
            ubicacion = itemView.findViewById(R.id.tvClienteSoliLocation);
            time = itemView.findViewById(R.id.tvClienteSoliTime);
            btnVerPerfil = itemView.findViewById(R.id.btnSoliVerPerfil);
            btnRechazar = itemView.findViewById(R.id.btnClienteSoliRechazar);

        }
    }



}
