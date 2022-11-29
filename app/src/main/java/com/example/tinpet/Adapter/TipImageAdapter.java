package com.example.tinpet.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tinpet.R;

public class TipImageAdapter extends RecyclerView.Adapter<TipImageAdapter.ViewHolder> {
    Context context;
    View vista;
    //RecyclerView Items
    int[] tipImgsList;
    int positionMark = 0;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivTipImg);
            linearLayout = itemView.findViewById(R.id.lyTipImg);

        }
    }

    public TipImageAdapter(Context context,int[] tipImgsList){
        this.context = context;
        this.tipImgsList = tipImgsList;
    }

    @NonNull
    @Override
    public TipImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_icon_tip,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TipImageAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageResource(tipImgsList[position]);
        final int pos = position ;
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
                positionMark = pos;
                notifyDataSetChanged();
            }
        });
        if(positionMark == position){
            holder.linearLayout.getBackground().setColorFilter(Color.parseColor("#EA3570"), PorterDuff.Mode.SRC_ATOP);
        }else{
            holder.linearLayout.getBackground().setColorFilter(Color.parseColor("#F27B50"), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public int getSelectedItem(){
        return positionMark;
    }

    public void setSelectedItem(int selectedItem) {
        this.positionMark = selectedItem;
    }

    @Override
    public int getItemCount() {
        return tipImgsList.length;
    }

}
