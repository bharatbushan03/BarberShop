  package com.barbershop.app;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

  public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ActivityMapsBinding binding;
      String name;
      Location current_Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        String whoisthere=getIntent().getStringExtra("UserOrOwner");


            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CALL_PHONE};
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this, permissions, 1234);
            }

            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                assert mapFragment != null;
                mapFragment.getMapAsync(this);
            }


            try {
                Task<Location> location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {
                             current_Location = (Location) task.getResult();
                            LocationManager locationManager = (LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);
                            Criteria criteria = new Criteria();
                           String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

                            //You can still do this if you like, you might get lucky:
//                            current_Location = locationManager.getLastKnownLocation(bestProvider);
//                            if (current_Location != null) {
//
//                              }
//                            else{
//                                //This is what you need:
//                                locationManager.requestLocationUpdates(bestProvider, 1000, 0, new LocationListener() {
//                                    @Override
//                                    public void onLocationChanged(@NonNull Location location) {
//                                        locationManager.removeUpdates(this);
//                                    }
//                                });
//                            }

                            Log.d("MAPPP","le=="+current_Location.getLatitude()+"");
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(current_Location.getLatitude(), current_Location.getLongitude()), 15f));
                            mMap.setMyLocationEnabled(true);
                            if(whoisthere.equals("Owner")) {
                            FirebaseDatabase.getInstance().getReference("Shops").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("shop_details").child("shop_name").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    name = snapshot.getValue(String.class);
                                    Log.d("MMM", "name" + name + "lat" + current_Location.getLatitude() + "log" + current_Location.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(current_Location.getLatitude(), current_Location.getLongitude())).title(name));

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            FirebaseDatabase.getInstance().getReference("Shops").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("shop_details").child("location").child("longitude").setValue(current_Location.getLongitude() + "");
                            FirebaseDatabase.getInstance().getReference("Shops").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("shop_details").child("location").child("latitude").setValue(current_Location.getLatitude() + "");
                            Toast.makeText(MapsActivity.this, "Your Current Location is saved As Shop Location", Toast.LENGTH_LONG).show();

                        }else {
                                FirebaseDatabase.getInstance().getReference("Shops").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot datasnap:snapshot.getChildren()
                                        ) {
                                            Double latitude=Double.parseDouble(Objects.requireNonNull(datasnap.child("shop_details").child("location").child("latitude").getValue(String.class)));
                                            Double longitude=Double.parseDouble(Objects.requireNonNull(datasnap.child("shop_details").child("location").child("longitude").getValue(String.class)));
                                            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(datasnap.child("shop_details").child("shop_name").getValue(String.class)));
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        } catch (Throwable throwable) {
                            Log.d("MAPPP", "getdevice loca exception " + throwable.getMessage());
                            throwable.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (SecurityException e) {
                Log.d("MAPPP", "getdevice location security exception " + e.getMessage());
            }

        }

    }
