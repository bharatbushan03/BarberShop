package com.barbershop.app.ui.auth.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.barbershop.app.data.repository.AuthRepository;
import com.barbershop.app.utils.Constants;
import com.barbershop.app.utils.Resource;
import com.barbershop.app.utils.ValidationUtils;

/**
 * ViewModel for Customer Login screen.
 * Handles business logic and exposes UI state via LiveData.
 */
public class LoginViewModel extends ViewModel {
    
    private final AuthRepository authRepository;
    
    // UI State
    private final MutableLiveData<LoginUiState> _uiState = new MutableLiveData<>();
    public final LiveData<LoginUiState> uiState = _uiState;
    
    // Login result
    private final MutableLiveData<Resource<AuthRepository.LoginResult>> _loginResult = 
        new MutableLiveData<>();
    public final LiveData<Resource<AuthRepository.LoginResult>> loginResult = _loginResult;
    
    public LoginViewModel() {
        this.authRepository = new AuthRepository();
    }
    
    /**
     * Called when user clicks login button.
     * Performs validation and triggers authentication.
     */
    public void onLoginClick(String email, String password) {
        // Validate inputs
        ValidationResult validation = validateInputs(email, password);
        if (!validation.isValid) {
            _uiState.setValue(new LoginUiState(false, true, validation.errorMessage, false, null));
            return;
        }
        
        _uiState.setValue(new LoginUiState(true, false, null, false, null));
        
        // Trigger login through repository
        LiveData<Resource<AuthRepository.LoginResult>> result = 
            authRepository.loginCustomer(email.trim(), password);
        
        result.observeForever(resource -> {
            _loginResult.setValue(resource);
            if (resource.isSuccess() && resource.data != null) {
                _uiState.setValue(new LoginUiState(false, false, null, true, resource.data));
            } else if (resource.isError()) {
                _uiState.setValue(new LoginUiState(false, true, resource.message, false, null));
            }
        });
    }
    
    /**
     * Called when Google Sign-In completes.
     */
    public void onGoogleSignIn(String idToken) {
        _uiState.setValue(new LoginUiState(true, false, null, false, null));
        
        LiveData<Resource<AuthRepository.LoginResult>> result = 
            authRepository.loginWithGoogle(idToken);
        
        result.observeForever(resource -> {
            _loginResult.setValue(resource);
            if (resource.isSuccess() && resource.data != null) {
                _uiState.setValue(new LoginUiState(false, false, null, true, resource.data));
            } else if (resource.isError()) {
                _uiState.setValue(new LoginUiState(false, true, resource.message, false, null));
            }
        });
    }
    
    /**
     * Check if user is already logged in (for auto-login).
     */
    public boolean checkAutoLogin() {
        if (authRepository.isUserLoggedIn()) {
            String userId = authRepository.getCurrentUserId();
            // We know a user is logged in but need to verify it's a customer
            // This would typically involve a quick database check
            return userId != null;
        }
        return false;
    }
    
    /**
     * Get current user ID if logged in.
     */
    public String getCurrentUserId() {
        return authRepository.getCurrentUserId();
    }
    
    /**
     * Clear error state.
     */
    public void clearError() {
        _uiState.setValue(new LoginUiState(false, false, null, false, null));
    }
    
    /**
     * Validate login inputs.
     */
    private ValidationResult validateInputs(String email, String password) {
        if (ValidationUtils.hasEmptyFields(email, password)) {
            return new ValidationResult(false, Constants.ERROR_EMPTY_FIELDS);
        }
        
        if (!ValidationUtils.isValidEmail(email)) {
            return new ValidationResult(false, "Please enter a valid email address");
        }
        
        if (!ValidationUtils.isValidPassword(password)) {
            return new ValidationResult(false, 
                "Password must be at least " + Constants.MIN_PASSWORD_LENGTH + " characters");
        }
        
        return new ValidationResult(true, null);
    }
    
    /**
     * UI State data class.
     */
    public static class LoginUiState {
        public final boolean isLoading;
        public final boolean showError;
        public final String errorMessage;
        public final boolean isSuccess;
        public final AuthRepository.LoginResult loginResult;
        
        public LoginUiState(boolean isLoading, boolean showError, String errorMessage,
                           boolean isSuccess, AuthRepository.LoginResult loginResult) {
            this.isLoading = isLoading;
            this.showError = showError;
            this.errorMessage = errorMessage;
            this.isSuccess = isSuccess;
            this.loginResult = loginResult;
        }
    }
    
    /**
     * Validation result helper class.
     */
    private static class ValidationResult {
        final boolean isValid;
        final String errorMessage;
        
        ValidationResult(boolean isValid, String errorMessage) {
            this.isValid = isValid;
            this.errorMessage = errorMessage;
        }
    }
}
