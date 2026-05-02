package com.barbershop.app.ui.auth.register;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.barbershop.app.data.model.Shop;
import com.barbershop.app.data.repository.AuthRepository;
import com.barbershop.app.utils.Constants;
import com.barbershop.app.utils.Resource;
import com.barbershop.app.utils.ValidationUtils;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;

/**
 * ViewModel for Owner Registration screen.
 * Handles phone verification and shop account creation.
 */
public class OwnerRegistrationViewModel extends ViewModel {
    
    private final AuthRepository authRepository;
    
    private final MutableLiveData<OwnerRegistrationUiState> _uiState = new MutableLiveData<>();
    public final LiveData<OwnerRegistrationUiState> uiState = _uiState;
    
    private final MutableLiveData<Resource<AuthRepository.LoginResult>> _registrationResult = 
        new MutableLiveData<>();
    public final LiveData<Resource<AuthRepository.LoginResult>> registrationResult = _registrationResult;
    
    // Phone verification state
    private String verificationId;
    private String otpCode;
    
    // Shop data being registered
    private Shop pendingShop;
    
    public OwnerRegistrationViewModel() {
        this.authRepository = new AuthRepository();
    }
    
    /**
     * Called when user clicks register button.
     * Validates inputs and prepares shop data.
     * @return true if inputs are valid
     */
    public boolean onRegisterClick(String shopName, String ownerName, String email, 
                                 String mobile, String address, String password) {
        ValidationResult validation = validateInputs(shopName, ownerName, email, mobile, address, password);
        if (!validation.isValid) {
            _uiState.setValue(new OwnerRegistrationUiState(false, false, true, validation.errorMessage, 
                                                           false, false, null, null));
            return false;
        }
        
        // Store shop data for later
        pendingShop = new Shop.Builder()
            .setShopName(shopName)
            .setOwnerName(ownerName)
            .setShopMail(email.trim())
            .setShopMobileNo(mobile)
            .setShopAddress(address)
            .setShopPassword(password)
            .setShopProfilePic("default")
            .build();
        
        _uiState.setValue(new OwnerRegistrationUiState(true, false, false, null, false, false, null, null));
        return true;
    }
    
    /**
     * Start phone verification.
     */
    public void startPhoneVerification(Activity activity) {
        if (pendingShop == null) return;
        
        authRepository.sendOwnerPhoneVerification(pendingShop.getShopMobileNo(), activity,
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    otpCode = credential.getSmsCode();
                    _uiState.setValue(new OwnerRegistrationUiState(true, false, false, null, 
                                                                   false, false, otpCode, null));
                    completeRegistration(credential);
                }
                
                @Override
                public void onVerificationFailed(FirebaseException e) {
                    String message = e.getMessage() != null ? e.getMessage() : "Verification failed";
                    
                    if (message.toLowerCase(Locale.ROOT).contains("provider is disabled")) {
                        registerWithoutPhone();
                    } else {
                        _uiState.setValue(new OwnerRegistrationUiState(false, false, true, message,
                                                                       false, false, null, null));
                    }
                }
                
                @Override
                public void onCodeSent(String verificationId, 
                                       PhoneAuthProvider.ForceResendingToken token) {
                    OwnerRegistrationViewModel.this.verificationId = verificationId;
                    _uiState.setValue(new OwnerRegistrationUiState(false, true, false, null,
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
            _uiState.setValue(new OwnerRegistrationUiState(false, false, true, 
                "OTP session expired. Please try again.", false, false, null, null));
            return;
        }
        
        _uiState.setValue(new OwnerRegistrationUiState(true, false, false, null, false, false, null, null));
        
        PhoneAuthCredential credential = authRepository.verifyPhoneCode(verificationId, otp);
        completeRegistration(credential);
    }
    
    /**
     * Complete registration with phone credential.
     */
    private void completeRegistration(PhoneAuthCredential credential) {
        LiveData<Resource<AuthRepository.LoginResult>> result = 
            authRepository.completeOwnerRegistration(credential, pendingShop);
        
        result.observeForever(resource -> {
            _registrationResult.setValue(resource);
            if (resource.isSuccess() && resource.data != null) {
                _uiState.setValue(new OwnerRegistrationUiState(false, false, false, null,
                                                               true, false, null, resource.data));
            } else if (resource.isError()) {
                _uiState.setValue(new OwnerRegistrationUiState(false, false, true, resource.message,
                                                               false, false, null, null));
            }
        });
    }
    
    /**
     * Register without phone verification (fallback).
     */
    private void registerWithoutPhone() {
        _uiState.setValue(new OwnerRegistrationUiState(true, false, false, 
            "Phone verification unavailable. Creating account with email...", false, false, null, null));
        
        LiveData<Resource<AuthRepository.LoginResult>> result = 
            authRepository.createOwnerWithoutPhone(pendingShop);
        
        result.observeForever(resource -> {
            _registrationResult.setValue(resource);
            if (resource.isSuccess() && resource.data != null) {
                _uiState.setValue(new OwnerRegistrationUiState(false, false, false, null,
                                                               true, true, null, resource.data));
            } else if (resource.isError()) {
                _uiState.setValue(new OwnerRegistrationUiState(false, false, true, resource.message,
                                                               false, false, null, null));
            }
        });
    }
    
    public Shop getPendingShop() {
        return pendingShop;
    }
    
    private ValidationResult validateInputs(String shopName, String ownerName, String email, 
                                          String mobile, String address, String password) {
        if (ValidationUtils.hasEmptyFields(shopName, ownerName, email, mobile, address, password)) {
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
    
    public static class OwnerRegistrationUiState {
        public final boolean isLoading;
        public final boolean showOtpDialog;
        public final boolean showError;
        public final String errorMessage;
        public final boolean isSuccess;
        public final boolean isFallbackMode;
        public final String autoFillOtp;
        public final AuthRepository.LoginResult result;
        
        public OwnerRegistrationUiState(boolean isLoading, boolean showOtpDialog, boolean showError,
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
