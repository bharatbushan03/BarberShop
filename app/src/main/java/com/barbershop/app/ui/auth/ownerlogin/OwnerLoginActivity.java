package com.barbershop.app.ui.auth.ownerlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.barbershop.app.OwnerHomeActivity;
import com.barbershop.app.R;
import com.barbershop.app.databinding.ActivityOwnerloginBinding;
import com.barbershop.app.ui.auth.ownerlogin.OwnerLoginViewModel;
import com.barbershop.app.ui.auth.register.OwnerRegistrationActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Owner Login Activity - MVVM Refactored.
 * Handles shop owner authentication.
 */
public class OwnerLoginActivity extends AppCompatActivity {

    private ActivityOwnerloginBinding binding;
    private OwnerLoginViewModel viewModel;
    private ProgressDialog progressDialog;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOwnerloginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        viewModel = new ViewModelProvider(this).get(OwnerLoginViewModel.class);

        setupProgressDialog();
        setupClickListeners();
        observeViewModel();

        checkAutoLoginOwner();
    }

    private void checkAutoLoginOwner() {
        String uid = viewModel.getCurrentUserId();
        if (uid == null || uid.trim().isEmpty()) {
            return;
        }

        FirebaseDatabase.getInstance().getReference("Shops").child(uid)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        navigateToOwnerHome();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // No-op: stay on login if the check fails.
                }
            });
    }

    private void setupProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    private void setupClickListeners() {
        // Login button
        binding.loginbtn.setOnClickListener(v -> {
            String email = binding.shopName.getText().toString().trim();
            String password = binding.shopPassword.getText().toString();
            viewModel.onLoginClick(email, password);
        });

        // Password visibility toggle
        binding.button2.setOnClickListener(v -> togglePasswordVisibility());

        // Register link
        binding.linkforregisterr.setOnClickListener(v -> {
            startActivity(new Intent(this, OwnerRegistrationActivity.class));
            finish();
        });
    }

    private void observeViewModel() {
        viewModel.uiState.observe(this, state -> {
            if (state.isLoading) {
                showProgress("Thank You For Sign-in", "Take a Sip..");
            } else {
                hideProgress();
            }

            if (state.showError && state.errorMessage != null) {
                Toast.makeText(this, state.errorMessage, Toast.LENGTH_LONG).show();
            }

            if (state.isSuccess) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                navigateToOwnerHome();
            }
        });
    }

    private void togglePasswordVisibility() {
        if (passwordVisible) {
            binding.shopPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            binding.shopPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        passwordVisible = !passwordVisible;
        binding.shopPassword.setSelection(binding.shopPassword.getText().length());
    }

    private void showProgress(String title, String message) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void navigateToOwnerHome() {
        startActivity(new Intent(this, OwnerHomeActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgress();
        binding = null;
    }
}
