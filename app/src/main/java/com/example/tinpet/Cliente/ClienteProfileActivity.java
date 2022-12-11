package com.example.tinpet.Cliente;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.tinpet.Adapter.ImageUploadAdapter;
import com.example.tinpet.Anonymus.LoginActivity;
import com.example.tinpet.Anonymus.PasswordActivity;
import com.example.tinpet.Anonymus.RegisterLocationActivity;
import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ClienteProfileActivity extends AppCompatActivity {


    EditText nombreMascota,edadMascota,razaMascota,sobreMascota,direccion;
    Spinner sexoMascota;
    TextView nickname;
    GridLayout glFotos;
    ImageSlider imageSlider;
    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedPreferences;
    RecyclerView rvFotos;
    ProgressBar pbPhoto;
    LinearLayout linearLayout;
    ShapeableImageView ivPerfil;
    ImageButton ibSearch;
    GoogleMap googleMap;
    Boolean isPer;

    private Uri cameraUri;
    ImageUploadAdapter fotosAdapter;
    private List<String> listFotos;

    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    Button btnactualizar,btnEditar;

    ActivityResultLauncher<Intent> launcherPhotoDocument = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri uri = result.getData().getData();
                    compressImageAndUpload(uri,50);
                } else {
                    Toast.makeText(ClienteProfileActivity.this, "Debe seleccionar un archivo", Toast.LENGTH_SHORT).show();
                }
            }
    );

    ActivityResultLauncher<Intent> launcherPhotoCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    compressImageAndUpload(cameraUri,25);
                } else {
                    Toast.makeText(ClienteProfileActivity.this, "Debe seleccionar un archivo", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_profile);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        nombreMascota = findViewById(R.id.etNombreMiPerfil);
        edadMascota = findViewById(R.id.etEdadMiPerfil);
        razaMascota = findViewById(R.id.etRazaMiPerfil);
        sexoMascota = findViewById(R.id.etSexoMiPerfil);
        sobreMascota = findViewById(R.id.etDescripcionMiPerfil);
        direccion = findViewById(R.id.etUbicacionMiPerfil);
        btnactualizar = findViewById(R.id.btnActualizar);
        btnEditar = findViewById(R.id.btnEditar);
        imageSlider = findViewById(R.id.isPetImagesMiPerfil);
        glFotos = findViewById(R.id.glFotosEditar);
        rvFotos = findViewById(R.id.rvProfilePet);
        pbPhoto = findViewById(R.id.pbUpdateClientePhoto);
        linearLayout = findViewById(R.id.llAtachUploadPhoto);
        ivPerfil = findViewById(R.id.ivClienteProfilePerfil);
        ibSearch = findViewById(R.id.ibSearchMapPerfil);
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);


        firestore.collection("mascota").document(mAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Mascota mascota = documentSnapshot.toObject(Mascota.class);
                        nombreMascota.setText(mascota.getNombreMascota());
                        //nickname.setText(mascota.getNickname());
                        direccion.setText(mascota.getUbicacion());
                        sobreMascota.setText(mascota.getSobreMascota());
                        razaMascota.setText(mascota.getRol());
                        edadMascota.setText(mascota.getEdad());
                        sexoMascota.setPrompt(mascota.getSexo());
                        Glide.with(ivPerfil).load(mascota.getUrlFotoPrincipal()).into(ivPerfil);

                        ArrayList<SlideModel> slideModels = new ArrayList<>();
                        for (String url : mascota.getUrlFotos()){
                            slideModels.add(new SlideModel(url, ScaleTypes.CENTER_CROP));
                        }
                        imageSlider.setImageList(slideModels);



                        listFotos = mascota.getUrlFotos();
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ClienteProfileActivity.this, 2);
                        fotosAdapter = new ImageUploadAdapter(ClienteProfileActivity.this, listFotos);

                        //Implementa adapters
                        rvFotos.setAdapter(fotosAdapter);
                        rvFotos.setLayoutManager(gridLayoutManager);
                        evaluarEmpty();

                        nombreMascota.setFocusable(false);
                        nombreMascota.setTextIsSelectable(false);
                        direccion.setFocusable(false);
                        direccion.setTextIsSelectable(false);
                        sobreMascota.setFocusable(false);
                        sobreMascota.setTextIsSelectable(false);
                        razaMascota.setFocusable(false);
                        razaMascota.setTextIsSelectable(false);
                        edadMascota.setFocusable(false);
                        edadMascota.setTextIsSelectable(false);
                        sexoMascota.setFocusable(false);
                        sexoMascota.setEnabled(false);
                        rvFotos.setVisibility(View.GONE);
                        ibSearch.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.GONE);

                    }
                });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombreMascota.setFocusable(true);
                nombreMascota.setTextIsSelectable(true);
                direccion.setFocusable(true);
                direccion.setTextIsSelectable(true);
                sobreMascota.setFocusable(true);
                sobreMascota.setTextIsSelectable(true);
                razaMascota.setFocusable(true);
                razaMascota.setTextIsSelectable(true);
                edadMascota.setFocusable(true);
                edadMascota.setTextIsSelectable(true);
                sexoMascota.setFocusable(true);
                sexoMascota.setEnabled(true);
                imageSlider.setVisibility(View.GONE);
                glFotos.setVisibility(View.VISIBLE);
                btnactualizar.setVisibility(View.VISIBLE);
                btnEditar.setVisibility(View.GONE);
                rvFotos.setVisibility(View.VISIBLE);
                ibSearch.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });


        btnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = nombreMascota.getText().toString();
                String ubicacion = direccion.getText().toString();
                editLocation(ubicacion);
                String descripcion = sobreMascota.getText().toString();
                String raza = razaMascota.getText().toString();
                String edad = edadMascota.getText().toString();
                String sexo = sexoMascota.getSelectedItem().toString();

                HashMap<String,Object> map = new HashMap<>();
                map.put("edad",edad);
                map.put("nombreMascota",nombre);
                map.put("rol",raza);
                map.put("sexo",sexo);
                map.put("sobreMascota",descripcion);
                map.put("ubicacion",ubicacion);
                firestore.collection("mascota").document(mAuth.getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        nombreMascota.setFocusable(false);
                        nombreMascota.setTextIsSelectable(false);
                        direccion.setFocusable(false);
                        direccion.setTextIsSelectable(false);
                        sobreMascota.setFocusable(false);
                        sobreMascota.setTextIsSelectable(false);
                        razaMascota.setFocusable(false);
                        razaMascota.setTextIsSelectable(false);
                        edadMascota.setFocusable(false);
                        edadMascota.setTextIsSelectable(false);
                        sexoMascota.setFocusable(false);
                        sexoMascota.setEnabled(false);
                        imageSlider.setVisibility(View.VISIBLE);
                        glFotos.setVisibility(View.GONE);
                        rvFotos.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.GONE);
                        btnactualizar.setVisibility(View.GONE);
                        ibSearch.setVisibility(View.GONE);
                        btnEditar.setVisibility(View.VISIBLE);
                        Toast.makeText(ClienteProfileActivity.this,"Actualizado correctamente",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        setBottomNavigationView();


    }

    public void uploadPhotoFromDocument(View view) {
        if (pbPhoto.getVisibility()==View.VISIBLE){
            Toast.makeText(ClienteProfileActivity.this, "Espera a que se termine de subir la foto", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            launcherPhotoDocument.launch(intent);
        }
    }

    public void uploadPhotoFromCamera(View view) {
        if (pbPhoto.getVisibility()==View.VISIBLE){
            Toast.makeText(ClienteProfileActivity.this, "Espera a que se termine de subir la foto", Toast.LENGTH_SHORT).show();
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


    public void subirImagenAFirebase(byte[] imageBytes) {
        StorageReference photoChild = FirebaseStorage.getInstance().getReference().child("devicephotos/" + "photo_" + Timestamp.now().getSeconds() + ".jpg");
        pbPhoto.setVisibility(View.VISIBLE);
        photoChild.putBytes(imageBytes).addOnSuccessListener(taskSnapshot -> {
            pbPhoto.setVisibility(View.GONE);
            photoChild.getDownloadUrl().addOnSuccessListener(uri -> {
                listFotos.add(uri.toString());
                fotosAdapter.notifyDataSetChanged();
                evaluarEmpty();
            }).addOnFailureListener(e ->{
                Log.d("msg-test", "error",e);
                Toast.makeText(ClienteProfileActivity.this, "Hubo un error al subir la imagen", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            Log.d("msg-test", "error",e);
            pbPhoto.setVisibility(View.GONE);
            Toast.makeText(ClienteProfileActivity.this, "Hubo un error al subir la imagen", Toast.LENGTH_SHORT).show();
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



    public void cerrarSesionCliente(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(ClienteProfileActivity.this, "Has cerrado sesión", Toast.LENGTH_SHORT).show();
        sharedPreferences.edit().remove("user").apply();
        startActivity(new Intent(ClienteProfileActivity.this, LoginActivity.class));
        ActivityCompat.finishAffinity(ClienteProfileActivity.this);
        finish();
    }

    public void setBottomNavigationView(){
        bottomNavigationView = findViewById(R.id.bottomNavProfileCliente);
        bottomNavigationView.clearAnimation();
        bottomNavigationView.setSelectedItemId(R.id.btnNavMenuClienteProfile);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.btnNavMenuClienteHome:
                        startActivity(new Intent(getApplicationContext(), ClienteHomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.btnNavMenuClienteProfile:
                        return true;
                    case R.id.btnNavMenuClienteSolicitud:
                        startActivity(new Intent(getApplicationContext(), ClienteSolicitudesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.btnNavMenuClienteTips:
                        startActivity(new Intent(getApplicationContext(), ClienteTipsActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }


    public void editLocation(String direccion){
        checkPermission();
        if(isPer){
            if(checkGooglePlayService()){
                SupportMapFragment supportMapFragment = new SupportMapFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.containerMap,supportMapFragment).commit();
                supportMapFragment.getMapAsync(this::onMapReady);
            }else{
                Toast.makeText(this,"Ha ocurrido un error.",Toast.LENGTH_SHORT).show();
            }
        }

        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = direccion;
                if(location==null){
                    Toast.makeText(ClienteProfileActivity.this,"Ingrese una ubicación, porfavor.",Toast.LENGTH_SHORT).show();

                }else{
                    Geocoder geocoder = new Geocoder(ClienteProfileActivity.this, Locale.getDefault());
                    try {
                        List<Address> listAdresss = geocoder.getFromLocationName(location+"Perú",1);
                        if(listAdresss.size()>0 && googleMap!=null){

                            LatLng latLng = new LatLng(listAdresss.get(0).getLatitude(),listAdresss.get(0).getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.title("Mi ubicación");
                            markerOptions.position(latLng);
                            googleMap.addMarker(markerOptions);
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,14);
                            googleMap.animateCamera(cameraUpdate);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng latLng = new LatLng(-9.189967,-75.015152);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("Mi ubicación");
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,14);
        googleMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private boolean checkGooglePlayService(){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if(result == ConnectionResult.SUCCESS){
            return true;
        } else if (googleApiAvailability.isUserResolvableError(result)) {
            Dialog dialog = googleApiAvailability.getErrorDialog(this, result, 201, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(ClienteProfileActivity.this,"Canceled",Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        }
        return false;
    }

    private void checkPermission(){

        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPer = true;
                Toast.makeText(ClienteProfileActivity.this,"Permisson",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("packages",getPackageName(),"");
                intent.setData(uri);
                startActivity(intent);

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

}