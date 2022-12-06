package com.example.tinpet.Anonymus;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tinpet.Adapter.ImageUploadAdapter;
import com.example.tinpet.Adapter.TipImageAdapter;
import com.example.tinpet.Decorations.ImageSelectorMargin;
import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    final int IMAGE_SELECTOR_MARGIN = 8;

    EditText inputNickname;
    EditText inputNombre;
    EditText inputEdad;
    EditText inputRaza;
    EditText inputDescripcion;
    Spinner spinnerTipo;
    Spinner spinnerSexo;

    Button btnSiguiente;
    ImageButton btnBack;

    ProgressBar pbPhoto;
    ProgressBar pbLoading;

    ImageButton btnPhotoAttach;
    ImageButton btnPhotoCam;

    RecyclerView rvFotos;
    GridLayout glFotos;

    boolean isBusy = false;
    private Uri cameraUri;
    private List<String> listFotos = new ArrayList<>();
    ImageUploadAdapter fotosAdapter;
    ActivityResultLauncher<Intent> launcherPhotoDocument = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = result.getData().getData();
                    compressImageAndUpload(uri,50);
                } else {
                    Toast.makeText(RegisterActivity.this, "Debe seleccionar un archivo", Toast.LENGTH_SHORT).show();
                }
            }
    );

    ActivityResultLauncher<Intent> launcherPhotoCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    compressImageAndUpload(cameraUri,25);
                } else {
                    Toast.makeText(RegisterActivity.this, "Debe seleccionar un archivo", Toast.LENGTH_SHORT).show();
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rvFotos = findViewById(R.id.rvCreatePetFotos);
        glFotos = findViewById(R.id.glCreatePet);

        inputNickname = findViewById(R.id.etNicknameRegister);
        inputNombre = findViewById(R.id.etNombreRegister);
        inputEdad = findViewById(R.id.etEdadRegister);
        inputRaza = findViewById(R.id.etRazaRegister);
        inputDescripcion = findViewById(R.id.etDescripciónRegister);
        spinnerTipo = findViewById(R.id.spTipRegister);
        spinnerSexo = findViewById(R.id.spSexoRegister);
        btnSiguiente = findViewById(R.id.btnRegisterPet);

        btnBack = findViewById(R.id.ibBackRegister1);

        pbPhoto = findViewById(R.id.pbCreatePetPhotoRegister);
        pbLoading = findViewById(R.id.pbCreatePetLoading);

        btnPhotoAttach = findViewById(R.id.ibCreatePetPhotoAttach);
        btnPhotoCam = findViewById(R.id.ibCreatePetPhotoCam);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pbPhoto.getVisibility()==View.VISIBLE){
                    Toast.makeText(RegisterActivity.this, "Espera a que se termine de subir la foto", Toast.LENGTH_SHORT).show();
                    return;
                }
                String nickname = inputNickname.getText().toString().trim();
                String nombre = inputNombre.getText().toString().trim();
                String edadStr = inputEdad.getText().toString();
                String raza = inputRaza.getText().toString().trim();
                String tipo = spinnerTipo.getSelectedItem().toString();
                String sexo = spinnerSexo.getSelectedItem().toString();
                String aboutMe = inputDescripcion.getText().toString().trim();

                if(nickname.isEmpty()||nombre.isEmpty()||edadStr.isEmpty()||raza.isEmpty()||tipo.isEmpty()||sexo.isEmpty()||aboutMe.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Los campos no pueden estar vacio.",Toast.LENGTH_SHORT).show();
                }else{
//                    mostrarCargando();
                    registrarMascota(nombre,nickname,raza,aboutMe,tipo,edadStr,sexo,listFotos);
                }
            }
        });

        //Setea adapters
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        fotosAdapter = new ImageUploadAdapter(this, listFotos);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // 7 is the sum of items in one repeated section
                switch (position % 5) {
                    // first three items span 3 columns each
                    case 0:
                    case 1:
                    case 2:
                        return 2;
                    // next two items span 2 columns each
                    case 3:
                    case 4:
                        return 3;
                }
                throw new IllegalStateException("internal error");
            }
        });
        //Implementa adapters
        rvFotos.setAdapter(fotosAdapter);
        rvFotos.setLayoutManager(gridLayoutManager);
        evaluarEmpty();
    }

    public  void registrarMascota(String nombre, String nickname,String raza,String aboutMe,String tipo,String edadStr,String sexo, List<String> listFotos){
        if(nickname.length()>8) {
            inputNickname.setError("Solo se admiten 8 caracteres.");
            inputNickname.requestFocus();
        }else if(nombre.length()>8){
            inputNombre.setError("Solo se admiten como máximo 8 caracteres.");
            inputNombre.requestFocus();
        }else if(raza.length()>10){
            inputRaza.setError("Solo se admiten como máximo 10 caracteres.");
            inputNombre.requestFocus();
        }else if(aboutMe.length()>250){
            inputRaza.setError("Solo se admiten como máximo 250 caracteres.");
            inputNombre.requestFocus();
        }else if(listFotos.size()<3 || listFotos.size()>6){
                rvFotos.requestFocus();
                Toast.makeText(RegisterActivity.this, "Se deben subir entre 3 a 6 fotos", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(RegisterActivity.this,RegisterOwnerActivity.class);
            Mascota mascota = new Mascota(nickname,tipo,sexo,nombre,edadStr,aboutMe,listFotos.get(0),listFotos);
            intent.putExtra("mascota",mascota);
            startActivity(intent);
            finish();
        }
    }

    public void removerFoto(int position){
        listFotos.remove(position);
        fotosAdapter.notifyDataSetChanged();
        evaluarEmpty();
    }

    public void evaluarEmpty(){
        if (listFotos.size()>0){
            rvFotos.setVisibility(View.VISIBLE);
            glFotos.setVisibility(View.GONE);
        }else{
            rvFotos.setVisibility(View.GONE);
            glFotos.setVisibility(View.VISIBLE);
        }
    }

    public void uploadPhotoFromDocument(View view) {
        if (pbPhoto.getVisibility()==View.VISIBLE){
            Toast.makeText(RegisterActivity.this, "Espera a que se termine de subir la foto", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            launcherPhotoDocument.launch(intent);
        }
    }

    public void compressImageAndUpload(Uri uri, int quality){
        try{
            Bitmap originalImage = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            originalImage.compress(Bitmap.CompressFormat.JPEG,quality,stream);
            subirImagenAFirebase(stream.toByteArray());
        }catch (Exception e){
            Log.d("msg","error",e);
        }
    }

    public void subirImagenAFirebase(byte[] imageBytes) {
        StorageReference photoChild = FirebaseStorage.getInstance().getReference().child("mascotasphotos/" + "photo_" + Timestamp.now().getSeconds() + ".jpg");
        pbPhoto.setVisibility(View.VISIBLE);
        photoChild.putBytes(imageBytes).addOnSuccessListener(taskSnapshot -> {
            pbPhoto.setVisibility(View.GONE);
            photoChild.getDownloadUrl().addOnSuccessListener(uri -> {
                listFotos.add(uri.toString());
                fotosAdapter.notifyDataSetChanged();
                evaluarEmpty();
            }).addOnFailureListener(e ->{
                Log.d("msg-test", "error",e);
                Toast.makeText(RegisterActivity.this, "Hubo un error al subir la imagen", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            Log.d("msg-test", "error",e);
            pbPhoto.setVisibility(View.GONE);
            Toast.makeText(RegisterActivity.this, "Hubo un error al subir la imagen", Toast.LENGTH_SHORT).show();
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                long bytesTransferred = snapshot.getBytesTransferred();
                long totalByteCount = snapshot.getTotalByteCount();
                double progreso = (100.0 * bytesTransferred) / totalByteCount;
                Long round = Math.round(progreso);
                pbPhoto.setProgress(round.intValue());
            }
        });
    }

    public void uploadPhotoFromCamera(View view) {
        if (pbPhoto.getVisibility()==View.VISIBLE){
            Toast.makeText(RegisterActivity.this, "Espera a que se termine de subir la foto", Toast.LENGTH_SHORT).show();
        }else{
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
            cameraUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            launcherPhotoCamera.launch(cameraIntent);
        }
    }

    public void backButtonRegister(View view){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (!isBusy){
            super.onBackPressed();
        }
    }



}