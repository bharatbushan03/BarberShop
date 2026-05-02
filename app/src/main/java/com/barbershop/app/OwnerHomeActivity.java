package com.barbershop.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

public class OwnerHomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home);
        Objects.requireNonNull(getSupportActionBar()).hide();
        bottomNavigationView=findViewById(R.id.owner_bottomNavigationView);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fm,new Ownerefragmentprofile()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override



            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction;

                if (item.getItemId() == R.id.profile) {
                    fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fm,new Ownerefragmentprofile()).commit();
                }
                if (item.getItemId() == R.id.appointments) {
                    fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fm,new Ownerfragmentappointments()).commit();
                }
                if (item.getItemId() == R.id.services) {
                    fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fm,new Ownerfragmentservices()).commit();
                }

                if (item.getItemId() == R.id.payments) {
                    fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fm,new Ownerfragmentpayment()).commit();
                }

                if (item.getItemId() == R.id.reviews) {
                    fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fm,new Ownerfragmentreviews()).commit();
                }

                return true;
            }
        });


    }
}
