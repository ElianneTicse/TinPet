package com.example.tinpet.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tinpet.Anonymus.RegisterActivity;
import com.example.tinpet.R;

import java.util.List;

public class ImageUploadAdapter extends RecyclerView.Adapter<ImageUploadAdapter.ViewHolder> {
    private Activity activity;
    private List<String> images;

    public ImageUploadAdapter(Activity activity, List<String> images) {
        this.activity = activity;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_upload, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageUploadAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(activity).load(images.get(position)).placeholder(AppCompatResources.getDrawable(activity,R.drawable.ic_image_placeholder)).into(holder.ivImage);
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivItemImageUpload);
            itemView.findViewById(R.id.btnRemovemImageUpload).setOnClickListener(v ->{
                ((RegisterActivity)  activity).removerFoto(position);
            });
        }
    }
}
