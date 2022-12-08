package com.example.tinpet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tinpet.Entity.Tip;
import com.example.tinpet.R;

import java.util.List;

public class TipListUsuarioAdapter extends RecyclerView.Adapter<TipListUsuarioAdapter.ViewHolder>{
    Context context;
    private List<Tip> tipList;
    int[] tipImage = {R.drawable.ic_bone,R.drawable.ic_dog_food,R.drawable.ic_star_2,R.drawable.ic_heart};

    public TipListUsuarioAdapter(Context context, List<Tip> tipList) {
        this.context = context;
        this.tipList = tipList;
    }

    @NonNull
    @Override
    public TipListUsuarioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cliente_tips,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TipListUsuarioAdapter.ViewHolder holder, int position) {
        Tip tip = tipList.get(position);
        holder.tittle.setText(tip.getTitulo());
        holder.descripcion.setText(tip.getDescripcion());
        holder.icon.setImageResource(tipImage[tip.getPosition()]);

    }

    @Override
    public int getItemCount() {
        return tipList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tittle,descripcion;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tittle = itemView.findViewById(R.id.tvClienteTipsTittle);
            descripcion = itemView.findViewById(R.id.tvClienteTipsDescription);
            icon = itemView.findViewById(R.id.ivClienteTipIcon);

        }
    }

}
