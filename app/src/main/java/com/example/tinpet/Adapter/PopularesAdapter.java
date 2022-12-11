package com.example.tinpet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;

import java.util.ArrayList;

public class PopularesAdapter extends RecyclerView.Adapter<PopularesAdapter.ViewHolder> {
    ArrayList<Mascota> mascotaList;
    Context context;

    public PopularesAdapter(ArrayList<Mascota> mascotaList, Context context) {
        this.mascotaList = mascotaList;
        this.context = context;
    }

    @NonNull
    @Override
    public PopularesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_admi_usuarios_populares,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularesAdapter.ViewHolder holder, int position) {
        Mascota mascota = mascotaList.get(position);
        holder.tvNombre.setText(mascota.getNombreMascota());
        holder.tvNickname.setText(mascota.getNickname());
        holder.tvLocacion.setText(mascota.getUbicacion());
        holder.tvCantidadAmigos.setText(mascota.getnAmigos().toString());
    }

    @Override
    public int getItemCount() {
        return mascotaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre,tvNickname,tvLocacion, tvCantidadAmigos;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.ivUsuarioPopularCardNombre);
            tvNickname = itemView.findViewById(R.id.ivUsuarioPopularCardNickname);
            tvLocacion = itemView.findViewById(R.id.ivAdmiUsuarioPopularDireccion);
            tvCantidadAmigos = itemView.findViewById(R.id.tvUsuarioPopularCardNumber);
        }
    }
}
