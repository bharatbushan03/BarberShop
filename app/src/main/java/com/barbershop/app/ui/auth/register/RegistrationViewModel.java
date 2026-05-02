package com.barbershop.app.ui.auth.register;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.barbershop.app.data.model.User;
import com.barbershop.app.data.repository.AuthRepository;
import com.barbershop.app.utils.Constants;
import com.barbershop.app.utils.Resource;
import com.barbershop.app.utils.ValidationUtils;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;

/**
 * ViewModel for Customer Registration screen.
 * Handles phone verification and account creation.
 */
public class RegistrationViewModel extends ViewModel {
    
    private final AuthRepository authRepository;
    
    private final MutableLiveData<RegistrationUiState> _uiState = new MutableLiveData<>();
    public final LiveData<RegistrationUiState> uiState = _uiState;
    
    private final MutableLiveData<Resource<AuthRepository.LoginResult>> _registrationResult = 
        new MutableLiveData<>();
    public final LiveData<Resource<AuthRepository.LoginResult>> registrationResult = _registrationResult;
    
    // Phone verification state
    private String verificationId;
    private String otpCode;
    
    // User data being registered
    private User pendingUser;
    
    public RegistrationViewModel() {
        this.authRepository = new AuthRepository();
    }
    
    /**
     * Called when user clicks register button.
     * Validates inputs and prepares user data.
     * @return true if inputs are valid
     */
    public boolean onRegisterClick(String name, String email, String mobile, String password) {
        ValidationResult validation = validateInputs(name, email, mobile, password);
        if (!validation.isValid) {
            _uiState.setValue(new RegistrationUiState(false, false, true, validation.errorMessage, 
                                                      false, false, null, null));
            return false;
        }
        
        // Store user data for later
        pendingUser = new User.Builder()
            .setUserName(name)
            .setUserMail(email.trim())
            .setUserMobileNo(mobile)
            .setUserPassword(password)
            .setUserProfilePic("default")
            .build();
        
        _uiState.setValue(new RegistrationUiState(true, false, false, null, false, false, null, null));
        return true;
    }
    
    /**
     * Start phone verification.
     */
    public void startPhoneVerification(Activity activity) {
        if (pendingUser == null) return;
        
        authRepository.sendCustomerPhoneVerification(pendingUser.getUserMobileNo(), activity,
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    otpCode = credential.getSmsCode();
                    _uiState.setValue(new RegistrationUiState(true, false, false, null, 
                                                              false, false, otpCode, null));
                    completeRegistration(credential);
                }
                
                @Override
                public void onVerificationFailed(FirebaseException e) {
                    String message = e.getMessage() != null ? e.getMessage() : "Verification failed";
                    
                    // Check if phone auth is disabled
                    if (message.toLowerCase(Locale.ROOT).contains("provider is disabled")) {
                        // Fall back to email-only registration
                        registerWithoutPhone();
                    } else {
                        _uiState.setValue(new RegistrationUiState(false, false, true, message,
                                                                  false, false, null, null));
                    }
                }
                
                @Override
                public void onCodeSent(String verificationId, 
                                       PhoneAuthProvider.ForceResendingToken token) {
                    RegistrationViewModel.this.verificationId = verificationId;
                    _uiState.setValue(new RegistrationUiState(false, true, false, null,
                                                              false, false, null, null));
                }
            }
        );
    }
    
    /**
     * Verify OTP and complete registration.
     */
    public void verifyOtpAndRegister(String otp) {
        if (verificationId == null || verificationId.trim().isEmpty()) {
            _uiState.setValue(new RegistrationUiState(false, false, true, 
                "OTP session expired. Please try again.", false, false, null, null));
            return;
        }
        
        _uiState.setValue(new RegistrationUiState(true, false, false, null, false, false, null, null));
        
        PhoneAuthCredential credential = authRepository.verifyPhoneCode(verificationId, otp);
        completeRegistration(credential);
    }
    
    /**
     * Complete registration with phone credential.
     */
    private void completeRegistration(PhoneAuthCredential credential) {
        LiveData<Resource<AuthRepository.LoginResult>> result = 
            authRepository.completeCustomerRegistration(credential, pendingUser);
        
        result.observeForever(resource -> {
            _registrationResult.setValue(resource);
            if (resource.isSuccess() && resource.data != null) {
                _uiState.setValue(new RegistrationUiState(false, false, false, null,
                                                          true, false, null, resource.data));
            } else if (resource.isError()) {
                _uiState.setValue(new RegistrationUiState(false, false, true, resource.message,
                                                          false, false, null, null));
            }
        });
    }
    
    /**
     * Register without phone verification (fallback).
     */
    private void registerWithoutPhone() {
        _uiState.setValue(new RegistrationUiState(true, false, false, 
            "Phone verification unavailable. Creating account with email...", false, false, null, null));
        
        LiveData<Resource<AuthRepository.LoginResult>> result = 
            authRepository.createCustomerWithoutPhone(pendingUser);
        
        result.observeForever(resource -> {
            _registrationResult.setValue(resource);
            if (resource.isSuccess() && resource.data != null) {
                _uiState.setValue(new RegistrationUiState(false, false, false, null,
                                                          true, true, null, resource.data));
            } else if (resource.isError()) {
                _uiState.setValue(new RegistrationUiState(false, false, true, resource.message,
                                                          false, false, null, null));
            }
        });
    }
    
    /**
     * Get the user being registered (for OTP auto-fill).
     */
    public User getPendingUser() {
        return pendingUser;
    }
    
    private ValidationResult validateInputs(String name, String email, String mobile, String password) {
        if (ValidationUtils.hasEmptyFields(name, email, mobile, password)) {
            return new ValidationResult(false, Constants.ERROR_EMPTY_FIELDS);
        }
        
        if (!ValidationUtils.isValidEmail(email)) {
            return new ValidationResult(false, "Please enter a valid email address");
        }
        
        if (!ValidationUtils.isValidMobile(mobile)) {
            return new ValidationResult(false, Constants.ERROR_INVALID_MOBILE);
        }
        
        if (!ValidationUtils.isValidPassword(password)) {
            return new ValidationResult(false, 
                "Password must be at least " + Constants.MIN_PASSWORD_LENGTH + " characters");
        }
        
        return new ValidationResult(true, null);
    }
    
    public static class RegistrationUiState {
        public final boolean isLoading;
        public final boolean showOtpDialog;
        public final boolean showError;
        public final String errorMessage;
        public final boolean isSuccess;
        public final boolean isFallbackMode; // True if registered without phone
        public final String autoFillOtp;
        public final AuthRepository.LoginResult result;
        
        public RegistrationUiState(boolean isLoading, boolean showOtpDialog, boolean showError,
                                  String errorMessage, boolean isSuccess, boolean isFallbackMode,
                                  String autoFillOtp, AuthRepository.LoginResult result) {
            this.isLoading = isLoading;
            this.showOtpDialog = showOtpDialog;
            this.showError = showError;
            this.errorMessage = errorMessage;
            this.isSuccess = isSuccess;
            this.isFallbackMode = isFallbackMode;
            this.autoFillOtp = autoFillOtp;
            this.result = result;
        }
    }
    
    private static class ValidationResult {
        final boolean isValid;
        final String errorMessage;
        
        ValidationResult(boolean isValid, String errorMessage) {
            this.isValid = isValid;
            this.errorMessage = errorMessage;
        }
    }
}
