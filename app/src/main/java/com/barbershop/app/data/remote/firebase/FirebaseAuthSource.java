package com.barbershop.app.data.remote.firebase;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import android.app.Activity;

/**
 * Handles all Firebase Authentication operations.
 * Isolated from UI, can be mocked for testing.
 */
public class FirebaseAuthSource {
    
    private final FirebaseAuth firebaseAuth;
    
    public FirebaseAuthSource() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }
    
    /**
     * Gets the currently logged in user.
     */
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }
    
    /**
     * Checks if a user is currently logged in.
     */
    public boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }
    
    /**
     * Gets the current user's UID.
     */
    public String getCurrentUserId() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getUid() : null;
    }
    
    /**
     * Login with email and password.
     */
    public void loginWithEmail(String email, String password, AuthCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess(firebaseAuth.getCurrentUser());
                } else {
                    callback.onError(task.getException() != null 
                        ? task.getException().getMessage() 
                        : "Authentication failed");
                }
            });
    }
    
    /**
     * Login with Google ID token.
     */
    public void loginWithGoogle(String idToken, AuthCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess(firebaseAuth.getCurrentUser());
                } else {
                    callback.onError(task.getException() != null 
                        ? task.getException().getMessage() 
                        : "Google sign-in failed");
                }
            });
    }
    
    /**
     * Create account with email and password.
     */
    public void createAccount(String email, String password, AuthCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess(firebaseAuth.getCurrentUser());
                } else {
                    callback.onError(task.getException() != null 
                        ? task.getException().getMessage() 
                        : "Account creation failed");
                }
            });
    }
    
    /**
     * Sign in with phone credential.
     */
    public void signInWithPhoneCredential(PhoneAuthCredential credential, AuthCallback callback) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess(firebaseAuth.getCurrentUser());
                } else {
                    callback.onError(task.getException() != null 
                        ? task.getException().getMessage() 
                        : "Phone verification failed");
                }
            });
    }
    
    /**
     * Link email credential to current user.
     */
    public void linkEmailCredential(String email, String password, LinkCallback callback) {
        FirebaseUser user = getCurrentUser();
        if (user == null) {
            callback.onError(new IllegalStateException("No user logged in"));
            return;
        }
        
        AuthCredential credential = com.google.firebase.auth.EmailAuthProvider.getCredential(email, password);
        user.linkWithCredential(credential)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError(task.getException());
                }
            });
    }
    
    /**
     * Send phone verification code.
     */
    public void sendPhoneVerificationCode(String phoneNumber, Activity activity,
                                          PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    
    /**
     * Verify phone OTP code.
     */
    public PhoneAuthCredential verifyPhoneCode(String verificationId, String code) {
        return PhoneAuthProvider.getCredential(verificationId, code);
    }
    
    /**
     * Sign out current user.
     */
    public void logout() {
        firebaseAuth.signOut();
    }
    
    /**
     * Re-authenticate user (required for sensitive operations).
     */
    public void reauthenticate(String email, String password, AuthCallback callback) {
        FirebaseUser user = getCurrentUser();
        if (user == null) {
            callback.onError("No user logged in");
            return;
        }
        
        AuthCredential credential = com.google.firebase.auth.EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess(user);
                } else {
                    callback.onError(task.getException() != null 
                        ? task.getException().getMessage() 
                        : "Re-authentication failed");
                }
            });
    }
    
    /**
     * Delete current user account.
     */
    public void deleteAccount(AuthCallback callback) {
        FirebaseUser user = getCurrentUser();
        if (user == null) {
            callback.onError("No user logged in");
            return;
        }
        
        user.delete()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(task.getException() != null 
                        ? task.getException().getMessage() 
                        : "Account deletion failed");
                }
            });
    }
    
    // Callback interfaces
    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onError(String errorMessage);
    }
    
    public interface LinkCallback {
        void onSuccess();
        void onError(Exception exception);
    }
}
