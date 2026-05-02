package com.barbershop.app.ui.auth.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.barbershop.app.R;
import com.barbershop.app.custHomeActivity;
import com.barbershop.app.data.repository.AuthRepository;
import com.barbershop.app.databinding.ActivityLoginBinding;
import com.barbershop.app.ui.auth.register.RegistrationActivity;
import com.barbershop.app.utils.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

/**
 * Login Activity - MVVM Refactored.
 * 
 * BEFORE: 388 lines with mixed Firebase calls, validation, business logic
 * AFTER: ~140 lines - only UI rendering and event handling
 * 
 * Data Flow: UI → ViewModel → Repository → Firebase Source
 */
public class LoginActivity extends AppCompatActivity {
    
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
    private GoogleSignInClient googleSignInClient;
    private ProgressDialog progressDialog;
    
    private boolean passwordVisible = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        Objects.requireNonNull(getSupportActionBar()).hide();
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        
        setupProgressDialog();
        setupGoogleSignIn();
        setupClickListeners();
        observeViewModel();
        
        // Check auto-login
        if (viewModel.checkAutoLogin()) {
            navigateToHome();
        }
    }
    
    private void setupProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }
    
    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    
    private void setupClickListeners() {
        // Login button
        binding.loginbtn.setOnClickListener(v -> {
            String email = binding.shopName.getText().toString().trim();
            String password = binding.shopPassword.getText().toString();
            viewModel.onLoginClick(email, password);
        });
        
        // Google Sign-In
        binding.googlebtn.setOnClickListener(v -> {
            showProgress("Sign-in With Google", "Take a Sip..");
            startGoogleSignIn();
        });
        
        // Password visibility toggle
        binding.button2.setOnClickListener(v -> togglePasswordVisibility());
        
        // Register link
        binding.linkforregisterr.setOnClickListener(v -> {
            startActivity(new Intent(this, RegistrationActivity.class));
            finish();
        });
    }
    
    private void observeViewModel() {
        // Observe UI State changes
        viewModel.uiState.observe(this, state -> {
            if (state.isLoading) {
                showProgress("Thank You For Sign-in", "Take a Sip..");
            } else {
                hideProgress();
            }
            
            if (state.showError && state.errorMessage != null) {
                Toast.makeText(this, state.errorMessage, Toast.LENGTH_LONG).show();
                viewModel.clearError();
            }
            
            if (state.isSuccess) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                navigateToHome();
            }
        });
        
        // Observe login result (for additional processing if needed)
        viewModel.loginResult.observe(this, result -> {
            // Can handle specific result states here
        });
    }
    
    private void startGoogleSignIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Constants.RC_GOOGLE_SIGN_IN);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == Constants.RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null && account.getIdToken() != null) {
                    viewModel.onGoogleSignIn(account.getIdToken());
                } else {
                    hideProgress();
                    Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_LONG).show();
                }
            } catch (ApiException e) {
                hideProgress();
                Toast.makeText(this, "Google sign-in failed: " + e.getMessage(), 
                              Toast.LENGTH_LONG).show();
            }
        }
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
    
    private void navigateToHome() {
        startActivity(new Intent(this, custHomeActivity.class));
        finish();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgress();
        binding = null;
    }
}
