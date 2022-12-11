package com.example.tinpet.Anonymus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tinpet.Entity.Mascota;
import com.example.tinpet.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RegisterLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    boolean isPer;
    EditText inputlocation;
    Button btnSiguiente;
    ImageButton btnSearchLocation;
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_location);

        inputlocation = findViewById(R.id.etLocationRegister);
        btnSiguiente = findViewById(R.id.btnSiguienteLocation);
        btnSearchLocation = findViewById(R.id.ibSearchMap);

        checkPermission();
        if(isPer){
            if(checkGooglePlayService()){
                SupportMapFragment supportMapFragment = new SupportMapFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.containerMap,supportMapFragment).commit();
                supportMapFragment.getMapAsync(this);
            }else{
                Toast.makeText(this,"Ha ocurrido un error.",Toast.LENGTH_SHORT).show();
            }
        }

        btnSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = inputlocation.getText().toString();
                if(location==null){
                    Toast.makeText(RegisterLocationActivity.this,"Ingrese una ubicación, porfavor.",Toast.LENGTH_SHORT).show();

                }else{
                    Geocoder geocoder = new Geocoder(RegisterLocationActivity.this, Locale.getDefault());
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

        Intent intent = getIntent();
        Mascota mascota = (Mascota) intent.getSerializableExtra("mascota");

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputlocation!=null){
                    mascota.setUbicacion(inputlocation.getText().toString());
                    Intent intent = new Intent(RegisterLocationActivity.this,PasswordActivity.class);
                    intent.putExtra("mascota",mascota);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterLocationActivity.this,"Ingrese una ubiación",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
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
                    Toast.makeText(RegisterLocationActivity.this,"Canceled",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RegisterLocationActivity.this,"Permisson",Toast.LENGTH_SHORT).show();

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