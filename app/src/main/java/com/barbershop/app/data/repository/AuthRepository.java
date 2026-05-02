package com.barbershop.app.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.barbershop.app.data.model.User;
import com.barbershop.app.data.model.Shop;
import com.barbershop.app.data.remote.firebase.FirebaseAuthSource;
import com.barbershop.app.data.remote.firebase.FirebaseDatabaseSource;
import com.barbershop.app.utils.Constants;
import com.barbershop.app.utils.Resource;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import android.app.Activity;

/**
 * Repository for all authentication operations.
 * Acts as single source of truth, abstracting Firebase implementation details.
 */
public class AuthRepository {
    
    private final FirebaseAuthSource authSource;
    private final FirebaseDatabaseSource databaseSource;
    
    public AuthRepository() {
        this.authSource = new FirebaseAuthSource();
        this.databaseSource = new FirebaseDatabaseSource();
    }
    
    // ==================== LOGIN OPERATIONS ====================
    
    /**
     * Login customer with email and password.
     * Verifies user exists in Users node after auth success.
     */
    public LiveData<Resource<LoginResult>> loginCustomer(String email, String password) {
        MutableLiveData<Resource<LoginResult>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        authSource.loginWithEmail(email, password, new FirebaseAuthSource.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                verifyCustomerInDatabase(user.getUid(), resultLiveData);
            }
            
            @Override
            public void onError(String errorMessage) {
                resultLiveData.setValue(Resource.error(errorMessage, null));
            }
        });
        
        return resultLiveData;
    }
    
    /**
     * Login owner with email and password.
     * Verifies shop exists in Shops node after auth success.
     */
    public LiveData<Resource<LoginResult>> loginOwner(String email, String password) {
        MutableLiveData<Resource<LoginResult>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        authSource.loginWithEmail(email, password, new FirebaseAuthSource.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                verifyOwnerInDatabase(user.getUid(), resultLiveData);
            }
            
            @Override
            public void onError(String errorMessage) {
                resultLiveData.setValue(Resource.error(errorMessage, null));
            }
        });
        
        return resultLiveData;
    }
    
    /**
     * Login with Google (for customers).
     */
    public LiveData<Resource<LoginResult>> loginWithGoogle(String idToken) {
        MutableLiveData<Resource<LoginResult>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        authSource.loginWithGoogle(idToken, new FirebaseAuthSource.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                checkAndCreateGoogleUser(user, resultLiveData);
            }
            
            @Override
            public void onError(String errorMessage) {
                resultLiveData.setValue(Resource.error(errorMessage, null));
            }
        });
        
        return resultLiveData;
    }
    
    // ==================== REGISTRATION OPERATIONS ====================
    
    /**
     * Start phone verification for customer registration.
     */
    public void sendCustomerPhoneVerification(String phoneNumber, Activity activity,
                                                PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks) {
        String formattedNumber = "+91" + phoneNumber;
        authSource.sendPhoneVerificationCode(formattedNumber, activity, callbacks);
    }
    
    /**
     * Start phone verification for owner registration.
     */
    public void sendOwnerPhoneVerification(String phoneNumber, Activity activity,
                                           PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks) {
        String formattedNumber = "+91" + phoneNumber;
        authSource.sendPhoneVerificationCode(formattedNumber, activity, callbacks);
    }
    
    /**
     * Complete customer registration with phone credential.
     */
    public LiveData<Resource<LoginResult>> completeCustomerRegistration(
            String verificationId, String code, User user) {
        return completeCustomerRegistration(authSource.verifyPhoneCode(verificationId, code), user);
    }
    
    /**
     * Complete customer registration with an already verified phone credential.
     */
    public LiveData<Resource<LoginResult>> completeCustomerRegistration(
            PhoneAuthCredential credential, User user) {
        
        MutableLiveData<Resource<LoginResult>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        authSource.signInWithPhoneCredential(
            credential,
            new FirebaseAuthSource.AuthCallback() {
                @Override
                public void onSuccess(FirebaseUser firebaseUser) {
                    linkEmailAndSaveCustomer(firebaseUser, user, resultLiveData);
                }
                
                @Override
                public void onError(String errorMessage) {
                    resultLiveData.setValue(Resource.error(errorMessage, null));
                }
            }
        );
        
        return resultLiveData;
    }
    
    /**
     * Complete owner registration with phone credential.
     */
    public LiveData<Resource<LoginResult>> completeOwnerRegistration(
            String verificationId, String code, Shop shop) {
        return completeOwnerRegistration(authSource.verifyPhoneCode(verificationId, code), shop);
    }
    
    /**
     * Complete owner registration with an already verified phone credential.
     */
    public LiveData<Resource<LoginResult>> completeOwnerRegistration(
            PhoneAuthCredential credential, Shop shop) {
        
        MutableLiveData<Resource<LoginResult>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        authSource.signInWithPhoneCredential(
            credential,
            new FirebaseAuthSource.AuthCallback() {
                @Override
                public void onSuccess(FirebaseUser firebaseUser) {
                    linkEmailAndSaveOwner(firebaseUser, shop, resultLiveData);
                }
                
                @Override
                public void onError(String errorMessage) {
                    resultLiveData.setValue(Resource.error(errorMessage, null));
                }
            }
        );
        
        return resultLiveData;
    }
    
    /**
     * Create account without phone verification (fallback).
     */
    public LiveData<Resource<LoginResult>> createCustomerWithoutPhone(User user) {
        MutableLiveData<Resource<LoginResult>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        authSource.createAccount(user.getUserMail(), user.getUserPassword(),
            new FirebaseAuthSource.AuthCallback() {
                @Override
                public void onSuccess(FirebaseUser firebaseUser) {
                    saveCustomerProfile(firebaseUser.getUid(), user, resultLiveData);
                }
                
                @Override
                public void onError(String errorMessage) {
                    resultLiveData.setValue(Resource.error(errorMessage, null));
                }
            }
        );
        
        return resultLiveData;
    }
    
    /**
     * Create owner account without phone verification (fallback).
     */
    public LiveData<Resource<LoginResult>> createOwnerWithoutPhone(Shop shop) {
        MutableLiveData<Resource<LoginResult>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        authSource.createAccount(shop.getShopMail(), shop.getShopPassword(),
            new FirebaseAuthSource.AuthCallback() {
                @Override
                public void onSuccess(FirebaseUser firebaseUser) {
                    saveOwnerProfile(firebaseUser.getUid(), shop, resultLiveData);
                }
                
                @Override
                public void onError(String errorMessage) {
                    resultLiveData.setValue(Resource.error(errorMessage, null));
                }
            }
        );
        
        return resultLiveData;
    }
    
    // ==================== SESSION MANAGEMENT ====================
    
    public boolean isUserLoggedIn() {
        return authSource.isUserLoggedIn();
    }
    
    public String getCurrentUserId() {
        return authSource.getCurrentUserId();
    }
    
    public PhoneAuthCredential verifyPhoneCode(String verificationId, String code) {
        return authSource.verifyPhoneCode(verificationId, code);
    }
    
    public void logout() {
        authSource.logout();
    }

    /**
     * Get user profile details.
     */
    public LiveData<Resource<User>> getUserDetails(String uid) {
        MutableLiveData<Resource<User>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        databaseSource.getUser(uid, new FirebaseDatabaseSource.UserCallback() {
            @Override
            public void onSuccess(User user) {
                resultLiveData.setValue(Resource.success(user));
            }
            
            @Override
            public void onError(String error) {
                resultLiveData.setValue(Resource.error(error, null));
            }
        });
        
        return resultLiveData;
    }
    
    // ==================== PRIVATE HELPER METHODS ====================
    
    private void verifyCustomerInDatabase(String uid, MutableLiveData<Resource<LoginResult>> liveData) {
        databaseSource.checkUserExists(uid, new FirebaseDatabaseSource.UserCheckCallback() {
            @Override
            public void onResult(boolean exists) {
                if (exists) {
                    liveData.setValue(Resource.success(
                        new LoginResult(true, false, true, null)));
                } else {
                    authSource.logout();
                    liveData.setValue(Resource.error(
                        Constants.ERROR_WRONG_ACCOUNT_TYPE,
                        new LoginResult(false, true, false, null)));
                }
            }
            
            @Override
            public void onError(String error) {
                authSource.logout();
                liveData.setValue(Resource.error(error, null));
            }
        });
    }
    
    private void verifyOwnerInDatabase(String uid, MutableLiveData<Resource<LoginResult>> liveData) {
        databaseSource.checkShopExists(uid, new FirebaseDatabaseSource.UserCheckCallback() {
            @Override
            public void onResult(boolean exists) {
                if (exists) {
                    liveData.setValue(Resource.success(
                        new LoginResult(true, false, false, null)));
                } else {
                    authSource.logout();
                    liveData.setValue(Resource.error(
                        Constants.ERROR_WRONG_ACCOUNT_TYPE,
                        new LoginResult(false, true, false, null)));
                }
            }
            
            @Override
            public void onError(String error) {
                authSource.logout();
                liveData.setValue(Resource.error(error, null));
            }
        });
    }
    
    private void checkAndCreateGoogleUser(FirebaseUser firebaseUser,
                                         MutableLiveData<Resource<LoginResult>> liveData) {
        databaseSource.checkUserExists(firebaseUser.getUid(), 
            new FirebaseDatabaseSource.UserCheckCallback() {
                @Override
                public void onResult(boolean exists) {
                    if (exists) {
                        liveData.setValue(Resource.success(
                            new LoginResult(true, false, true, null)));
                    } else {
                        // New Google user - create record
                        User newUser = new User.Builder()
                            .setUserProfilePic(firebaseUser.getPhotoUrl() != null 
                                ? firebaseUser.getPhotoUrl().toString() : "default")
                            .setUserName(firebaseUser.getDisplayName())
                            .setUserMail(firebaseUser.getEmail())
                            .setUserMobileNo(firebaseUser.getPhoneNumber())
                            .build();
                        
                        saveCustomerProfile(firebaseUser.getUid(), newUser, liveData);
                    }
                }
                
                @Override
                public void onError(String error) {
                    liveData.setValue(Resource.error(error, null));
                }
            }
        );
    }
    
    private void linkEmailAndSaveCustomer(FirebaseUser firebaseUser, User user,
                                         MutableLiveData<Resource<LoginResult>> liveData) {
        authSource.linkEmailCredential(user.getUserMail(), user.getUserPassword(),
            new FirebaseAuthSource.LinkCallback() {
                @Override
                public void onSuccess() {
                    saveCustomerProfile(firebaseUser.getUid(), user, liveData);
                }
                
                @Override
                public void onError(Exception exception) {
                    handleLinkError(exception, firebaseUser, liveData);
                }
            }
        );
    }
    
    private void linkEmailAndSaveOwner(FirebaseUser firebaseUser, Shop shop,
                                       MutableLiveData<Resource<LoginResult>> liveData) {
        authSource.linkEmailCredential(shop.getShopMail(), shop.getShopPassword(),
            new FirebaseAuthSource.LinkCallback() {
                @Override
                public void onSuccess() {
                    saveOwnerProfile(firebaseUser.getUid(), shop, liveData);
                }
                
                @Override
                public void onError(Exception exception) {
                    handleLinkError(exception, firebaseUser, liveData);
                }
            }
        );
    }
    
    private void saveCustomerProfile(String uid, User user,
                                    MutableLiveData<Resource<LoginResult>> liveData) {
        databaseSource.saveUser(uid, user, new FirebaseDatabaseSource.DatabaseCallback() {
            @Override
            public void onSuccess() {
                liveData.setValue(Resource.success(
                    new LoginResult(true, false, true, null)));
            }
            
            @Override
            public void onError(String errorMessage) {
                authSource.logout();
                liveData.setValue(Resource.error(errorMessage, null));
            }
        });
    }
    
    private void saveOwnerProfile(String uid, Shop shop,
                                 MutableLiveData<Resource<LoginResult>> liveData) {
        databaseSource.saveShop(uid, shop, new FirebaseDatabaseSource.DatabaseCallback() {
            @Override
            public void onSuccess() {
                // Set joining year
                databaseSource.updateShopJoiningYear(uid, 
                    String.valueOf(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),
                    new FirebaseDatabaseSource.DatabaseCallback() {
                        @Override
                        public void onSuccess() {
                            liveData.setValue(Resource.success(
                                new LoginResult(true, false, false, null)));
                        }
                        
                        @Override
                        public void onError(String error) {
                            // Shop saved but joining year failed - still success
                            liveData.setValue(Resource.success(
                                new LoginResult(true, false, false, null)));
                        }
                    }
                );
            }
            
            @Override
            public void onError(String errorMessage) {
                authSource.logout();
                liveData.setValue(Resource.error(errorMessage, null));
            }
        });
    }
    
    private void handleLinkError(Exception exception, FirebaseUser firebaseUser,
                                MutableLiveData<Resource<LoginResult>> liveData) {
        // Check if it's already linked to same email
        if (isLinkedToSameEmail(firebaseUser, exception)) {
            liveData.setValue(Resource.success(
                new LoginResult(true, false, true, null)));
        } else {
            authSource.logout();
            String errorMsg = getLinkErrorMessage(exception);
            liveData.setValue(Resource.error(errorMsg, null));
        }
    }
    
    private boolean isLinkedToSameEmail(FirebaseUser user, Exception exception) {
        if (exception instanceof com.google.firebase.auth.FirebaseAuthException) {
            String errorCode = ((com.google.firebase.auth.FirebaseAuthException) exception).getErrorCode();
            if ("ERROR_PROVIDER_ALREADY_LINKED".equals(errorCode)) {
                return true;
            }
        }
        return false;
    }
    
    private String getLinkErrorMessage(Exception exception) {
        if (exception instanceof com.google.firebase.auth.FirebaseAuthException) {
            String errorCode = ((com.google.firebase.auth.FirebaseAuthException) exception).getErrorCode();
            if ("ERROR_EMAIL_ALREADY_IN_USE".equals(errorCode) || 
                "ERROR_CREDENTIAL_ALREADY_IN_USE".equals(errorCode)) {
                return "This email is already registered. Please log in instead.";
            }
            if ("ERROR_PROVIDER_ALREADY_LINKED".equals(errorCode)) {
                return "This phone number is already linked to another account.";
            }
        }
        return exception != null && exception.getMessage() != null
            ? exception.getMessage()
            : "Unable to create account.";
    }
    
    // ==================== RESULT CLASS ====================
    
    /**
     * Encapsulates login/registration operation result.
     */
    public static class LoginResult {
        public final boolean success;
        public final boolean isWrongAccountType;
        public final boolean isCustomer;
        public final String userId;
        
        public LoginResult(boolean success, boolean isWrongAccountType, 
                          boolean isCustomer, String userId) {
            this.success = success;
            this.isWrongAccountType = isWrongAccountType;
            this.isCustomer = isCustomer;
            this.userId = userId;
        }
    }
}
