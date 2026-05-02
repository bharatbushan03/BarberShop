package com.barbershop.app;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.barbershop.app.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * Maps Activity handles location display for both customers and owners.
 * Customers see all shops on the map.
 * Owners see their own shop location and can update it.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ActivityMapsBinding binding;
    private Location current_Location;
    private String userRole; // "User" or "Owner"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRole = getIntent().getStringExtra("UserOrOwner");
        if (userRole == null) userRole = "User";

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 1234);
        } else {
            getUserLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1234) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                Toast.makeText(this, "Permission denied. Map might not show your location.", Toast.LENGTH_SHORT).show();
                loadShopsOnMap();
            }
        }
    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            loadShopsOnMap();
            return;
        }

        mMap.setMyLocationEnabled(true);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        
        locationTask.addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                current_Location = task.getResult();
                LatLng currentLatLng = new LatLng(current_Location.getLatitude(), current_Location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14f));
                
                if (userRole.equals("Owner")) {
                    updateOwnerLocation();
                }
            }
            loadShopsOnMap();
        });
    }

    private void updateOwnerLocation() {
        if (current_Location == null) return;
        
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        FirebaseDatabase.getInstance().getReference("Shops").child(uid).child("shop_details").child("location").child("longitude").setValue(current_Location.getLongitude() + "");
        FirebaseDatabase.getInstance().getReference("Shops").child(uid).child("shop_details").child("location").child("latitude").setValue(current_Location.getLatitude() + "");
        
        Toast.makeText(this, "Your current location is saved as your shop location", Toast.LENGTH_SHORT).show();
    }

    private void loadShopsOnMap() {
        FirebaseDatabase.getInstance().getReference("Shops").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot shopSnapshot : snapshot.getChildren()) {
                    try {
                        DataSnapshot details = shopSnapshot.child("shop_details");
                        String shopName = details.child("shop_name").getValue(String.class);
                        DataSnapshot locSnapshot = details.child("location");
                        
                        if (locSnapshot.exists()) {
                            String latStr = locSnapshot.child("latitude").getValue(String.class);
                            String lngStr = locSnapshot.child("longitude").getValue(String.class);
                            
                            if (latStr != null && lngStr != null) {
                                double lat = Double.parseDouble(latStr);
                                double lng = Double.parseDouble(lngStr);
                                LatLng shopLoc = new LatLng(lat, lng);
                                mMap.addMarker(new MarkerOptions().position(shopLoc).title(shopName));
                            }
                        }
                    } catch (Exception e) {
                        Log.e("MapsActivity", "Error parsing shop location: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
