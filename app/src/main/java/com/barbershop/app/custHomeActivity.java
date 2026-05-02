package com.barbershop.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.barbershop.app.ui.customer.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class custHomeActivity extends AppCompatActivity {
        BottomNavigationView bottomNavigationView;
        FirebaseAuth mAuth;
        String userid;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();

        Objects.requireNonNull(getSupportActionBar()).hide();

        bottomNavigationView=findViewById(R.id.cust_bottomNavigationView);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fm,new HomeFragment()).commit();
        //userid=getIntent().getStringExtra("userid");


        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CALL_PHONE};
        if (ActivityCompat.checkSelfPermission(custHomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(custHomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(custHomeActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(custHomeActivity.this, permissions, 1234);
        }



        // Log.d("piooo cust",userid);
       // Bundle bundle = new Bundle();
        //bundle.putString("userid", userid);



        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override



            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction;

                if (item.getItemId() == R.id.home) {
                    fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    HomeFragment homeFragment=new HomeFragment();

                    fragmentTransaction.replace(R.id.fm,homeFragment).commit();
                }
                if (item.getItemId() == R.id.appointments) {
                   fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fm,new Appointmentscreen()).commit();
                }
                if (item.getItemId() == R.id.profile) {
                   fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    Profilescreen profilescreen=new Profilescreen();
                   // profilescreen.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fm,profilescreen).commit();
                }

                return true;
            }
        });
    }


}
