package com.example.tinpet.Anonymus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.Timestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterPhotosActivity extends AppCompatActivity {

    ProgressBar pbPhoto;
    ProgressBar pbLoading;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_photos);

        //Recibimos info de la vista previa
        Intent intent = getIntent();
        Mascota mascota = (Mascota) intent.getSerializableExtra("mascota");

        StorageReference imageRef = storageReference.child("");

    }

//    public void subirImagenAFirebase(byte[] imageBytes) {
//        StorageReference photoChild = FirebaseStorage.getInstance().getReference().child("mascotasphotos/" + "photo_" + Timestamp.now().getSeconds() + ".jpg");
//        pbPhoto.setVisibility(View.VISIBLE);
//        photoChild.putBytes(imageBytes).addOnSuccessListener(taskSnapshot -> {
//            pbPhoto.setVisibility(View.GONE);
//            photoChild.getDownloadUrl().addOnSuccessListener(uri -> {
//                listFotos.add(uri.toString());
//                fotosAdapter.notifyDataSetChanged();
//                evaluarEmpty();
//            }).addOnFailureListener(e ->{
//                Log.d("msg-test", "error",e);
//                Toast.makeText(RegisterPhotosActivity.this, "Hubo un error al subir la imagen", Toast.LENGTH_SHORT).show();
//            });
//        }).addOnFailureListener(e -> {
//            Log.d("msg-test", "error",e);
//            pbPhoto.setVisibility(View.GONE);
//            Toast.makeText(RegisterPhotosActivity.this, "Hubo un error al subir la imagen", Toast.LENGTH_SHORT).show();
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                long bytesTransferred = snapshot.getBytesTransferred();
//                long totalByteCount = snapshot.getTotalByteCount();
//                double progreso = (100.0 * bytesTransferred) / totalByteCount;
//                Long round = Math.round(progreso);
//                pbPhoto.setProgress(round.intValue());
//            }
//        });
//    }



}