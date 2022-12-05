package com.example.tinpet.Adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tinpet.Cliente.ClienteDetallesProfileActivity;
import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;


import java.util.List;

public class RecomendadosAdapter extends RecyclerView.Adapter<RecomendadosAdapter.ViewHolder> {

    Context context;
    private List<Mascota> mascotaList;

    public RecomendadosAdapter(Context context, List<Mascota> mascotaList) {
        this.context = context;
        this.mascotaList = mascotaList;
    }

    @NonNull
    @Override
    public RecomendadosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_friend_sugerencia,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mascota mascota = mascotaList.get(position);
        holder.tvNombre.setText(mascota.getNombreMascota());
        holder.tvNickname.setText(mascota.getNickname());
        holder.tvLocation.setText(mascota.getUbicacion());

        if (mascota.getUrlFotos() != null && !mascota.getUrlFotos().isEmpty()){
            String urlFoto = mascota.getUrlFotos().get(0).toString();
            System.out.println(urlFoto);
            Glide.with(holder.ivUsuario).load(urlFoto).into(holder.ivUsuario);
        }else{
            Glide.with(holder.ivUsuario).load("https://img.freepik.com/foto-gratis/lindo-perrito-haciendose-pasar-persona-negocios_23-2148985938.jpg?w=1060&t=st=1670223600~exp=1670224200~hmac=d5a7356054a901184f5586e5abc6e56551e1a21b40bfc1b5cd6d246b373573d2").into(holder.ivUsuario);
        }
        if(mascota.getSexo().equals("Macho")){
            holder.ivSexo.setImageResource(R.drawable.sexo_macho);
        }else if(mascota.getSexo().equals("Hembra")){
            holder.ivSexo.setImageResource(R.drawable.sexo_hembra);
        }
        holder.mcFriend.setOnClickListener(v -> {
            Intent intent = new Intent(context, ClienteDetallesProfileActivity.class);
            intent.putExtra("mascota",mascota);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mascotaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivUsuario;
        TextView tvNombre,tvNickname,tvLocation;
        ImageView ivSexo;
        MaterialCardView mcFriend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUsuario = itemView.findViewById(R.id.ivFotoSugeridos);
            tvNombre = itemView.findViewById(R.id.tvSugeridosNombre);
            tvNickname = itemView.findViewById(R.id.tvSugeridosNickname);
            tvLocation = itemView.findViewById(R.id.tvSugeridosLocation);
            ivSexo = itemView.findViewById(R.id.ivSugeridosSexo);
            mcFriend = itemView.findViewById(R.id.cardFriendSugerencia);
        }
    }
}
