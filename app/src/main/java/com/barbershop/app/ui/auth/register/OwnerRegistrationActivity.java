package com.barbershop.app.ui.auth.register;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.barbershop.app.OwnerHomeActivity;
import com.barbershop.app.R;
import com.barbershop.app.databinding.ActivityOwenerRegistrationBinding;

import java.util.Objects;

/**
 * Owner Registration Activity - MVVM Refactored.
 * Handles shop owner account creation with phone verification.
 */
public class OwnerRegistrationActivity extends AppCompatActivity {

    private ActivityOwenerRegistrationBinding binding;
    private OwnerRegistrationViewModel viewModel;
    private ProgressDialog progressDialog;

    // OTP Dialog views
    private AlertDialog otpDialog;
    private EditText[] otpFields;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOwenerRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        viewModel = new ViewModelProvider(this).get(OwnerRegistrationViewModel.class);

        setupProgressDialog();
        setupOtpDialog();
        setupClickListeners();
        observeViewModel();
    }

    private void setupProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    private void setupOtpDialog() {
        View otpView = LayoutInflater.from(this).inflate(R.layout.otp_input, null);

        otpFields = new EditText[] {
                otpView.findViewById(R.id.otpinput1),
                otpView.findViewById(R.id.otpinput2),
                otpView.findViewById(R.id.otpinput3),
                otpView.findViewById(R.id.otpinput4),
                otpView.findViewById(R.id.otpinput5),
                otpView.findViewById(R.id.otpinput6)
        };

        Button verifyButton = otpView.findViewById(R.id.verify);

        setupOtpFieldNavigation();

        verifyButton.setOnClickListener(v -> {
            String otp = collectOtp();
            if (otp.length() == 6) {
                viewModel.verifyOtpAndRegister(otp);
                otpDialog.dismiss();
            } else {
                Toast.makeText(this, "Enter full OTP", Toast.LENGTH_SHORT).show();
            }
        });

        otpDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(otpView)
                .create();
    }

    private void setupOtpFieldNavigation() {
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            otpFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpFields.length - 1) {
                        otpFields[index + 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void setupClickListeners() {
        // Password visibility toggle
        binding.eyebutton.setOnClickListener(v -> togglePasswordVisibility());

        // Register button
        binding.registrationbtn.setOnClickListener(v -> {
            String shopName = binding.shopName.getText().toString();
            String ownerName = binding.ownerName.getText().toString();
            String email = binding.shopMail.getText().toString();
            String mobile = binding.shopMobile.getText().toString();
            String address = binding.shopAddress.getText().toString();
            String password = binding.shopPassword.getText().toString();

            if (viewModel.onRegisterClick(shopName, ownerName, email, mobile, address, password)) {
                viewModel.startPhoneVerification(this);
            }
        });

        // Back link (if any)
        binding.textView2.setOnClickListener(v -> finish());
    }

    private void observeViewModel() {
        viewModel.uiState.observe(this, state -> {
            if (state.isLoading) {
                showProgress(state.errorMessage != null ? state.errorMessage
                        : "Thank You For Sign-up", "We're Creating Your Account");
            } else {
                hideProgress();
            }

            if (state.showOtpDialog && !otpDialog.isShowing()) {
                clearOtpFields();
                otpDialog.show();
            }

            if (state.autoFillOtp != null && !state.autoFillOtp.isEmpty()) {
                fillOtpFields(state.autoFillOtp);
            }

            if (state.showError && state.errorMessage != null) {
                Toast.makeText(this, state.errorMessage, Toast.LENGTH_LONG).show();
            }

            if (state.isSuccess) {
                String message = state.isFallbackMode
                        ? "Account created (phone verification unavailable)"
                        : "Account created successfully!";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, OwnerHomeActivity.class));
                finish();
            }
        });
    }

    private String collectOtp() {
        StringBuilder otp = new StringBuilder();
        for (EditText field : otpFields) {
            otp.append(field.getText().toString());
        }
        return otp.toString();
    }

    private void fillOtpFields(String code) {
        for (int i = 0; i < code.length() && i < otpFields.length; i++) {
            otpFields[i].setText(String.valueOf(code.charAt(i)));
        }
    }

    private void clearOtpFields() {
        for (EditText field : otpFields) {
            field.setText("");
        }
        otpFields[0].requestFocus();
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
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (otpDialog != null && otpDialog.isShowing()) {
            otpDialog.dismiss();
        }
        hideProgress();
        binding = null;
    }
}
