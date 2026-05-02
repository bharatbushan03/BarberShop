package com.barbershop.app.ui.auth.ownerlogin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.barbershop.app.data.repository.AuthRepository;
import com.barbershop.app.utils.Constants;
import com.barbershop.app.utils.Resource;
import com.barbershop.app.utils.ValidationUtils;

/**
 * ViewModel for Owner Login screen.
 */
public class OwnerLoginViewModel extends ViewModel {
    
    private final AuthRepository authRepository;
    
    private final MutableLiveData<OwnerLoginUiState> _uiState = new MutableLiveData<>();
    public final LiveData<OwnerLoginUiState> uiState = _uiState;
    
    private final MutableLiveData<Resource<AuthRepository.LoginResult>> _loginResult = 
        new MutableLiveData<>();
    public final LiveData<Resource<AuthRepository.LoginResult>> loginResult = _loginResult;
    
    public OwnerLoginViewModel() {
        this.authRepository = new AuthRepository();
    }
    
    /**
     * Called when owner clicks login button.
     */
    public void onLoginClick(String email, String password) {
        ValidationResult validation = validateInputs(email, password);
        if (!validation.isValid) {
            _uiState.setValue(new OwnerLoginUiState(false, true, validation.errorMessage, false, null));
            return;
        }
        
        _uiState.setValue(new OwnerLoginUiState(true, false, null, false, null));
        
        LiveData<Resource<AuthRepository.LoginResult>> result = 
            authRepository.loginOwner(email.trim(), password);
        
        result.observeForever(resource -> {
            _loginResult.setValue(resource);
            if (resource.isSuccess() && resource.data != null) {
                _uiState.setValue(new OwnerLoginUiState(false, false, null, true, resource.data));
            } else if (resource.isError()) {
                _uiState.setValue(new OwnerLoginUiState(false, true, resource.message, false, null));
            }
        });
    }
    
    /**
     * Check auto-login for owner.
     */
    public boolean checkAutoLogin() {
        return authRepository.isUserLoggedIn() && authRepository.getCurrentUserId() != null;
    }
    
    public String getCurrentUserId() {
        return authRepository.getCurrentUserId();
    }
    
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
    
    public static class OwnerLoginUiState {
        public final boolean isLoading;
        public final boolean showError;
        public final String errorMessage;
        public final boolean isSuccess;
        public final AuthRepository.LoginResult loginResult;
        
        public OwnerLoginUiState(boolean isLoading, boolean showError, String errorMessage,
                                boolean isSuccess, AuthRepository.LoginResult loginResult) {
            this.isLoading = isLoading;
            this.showError = showError;
            this.errorMessage = errorMessage;
            this.isSuccess = isSuccess;
            this.loginResult = loginResult;
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
