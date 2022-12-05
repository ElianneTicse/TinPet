package com.example.tinpet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class UsuariosListAdapter extends RecyclerView.Adapter<UsuariosListAdapter.ViewHolder> {
    Context context;
    ArrayList<Mascota> mascotaArrayList;

    public UsuariosListAdapter(Context context, ArrayList<Mascota> mascotaArrayList) {
        this.context = context;
        this.mascotaArrayList = mascotaArrayList;
    }

    @NonNull
    @Override
    public UsuariosListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_admi_usuarios_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosListAdapter.ViewHolder holder, int position) {
        Mascota mascota = mascotaArrayList.get(position);
        holder.tvUsuarioNombreList.setText(mascota.getNombreMascota());
        holder.tvUsuarioNicknameList.setText(mascota.getNickname());
        holder.tvUsuarioDireccion.setText(mascota.getUbicacion());
//        List<String> listFotos = mascota.getUrlFotos();
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
    }

    @Override
    public int getItemCount() {
        return mascotaArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivUsuario;
        TextView tvUsuarioNombreList,tvUsuarioNicknameList,tvUsuarioDireccion;
        ImageView ivLocation, ivSexo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUsuario = itemView.findViewById(R.id.ivAdmiUsuarioFoto);
            tvUsuarioNombreList = itemView.findViewById(R.id.tvAdmiUsuarioNombre);
            tvUsuarioNicknameList = itemView.findViewById(R.id.tvAdmiUsuarioNickname);
            tvUsuarioDireccion = itemView.findViewById(R.id.tvAdmiUsuarioDireccion);
            ivLocation = itemView.findViewById(R.id.ivAdmiUserLocation);
            ivSexo = itemView.findViewById(R.id.ivAdmiUsuarioSexo);
        }
    }
}
