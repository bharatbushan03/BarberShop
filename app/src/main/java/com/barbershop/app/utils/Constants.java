package com.barbershop.app.utils;

/**
 * Centralized constants for the application.
 */
public class Constants {
    
    // Firebase Database Paths
    public static final String DB_USERS = "Users";
    public static final String DB_SHOPS = "Shops";
    public static final String DB_SHOP_DETAILS = "shop_details";
    public static final String DB_APPOINTMENTS = "appointments";
    public static final String DB_SERVICES = "services";
    public static final String DB_REVIEWS = "reviews";
    
    // Request Codes
    public static final int RC_GOOGLE_SIGN_IN = 65;
    public static final int RC_OWNER_GOOGLE_SIGN_IN = 69;
    public static final int RC_PAYMENT = 123;
    
    // Validation
    public static final int MOBILE_NUMBER_LENGTH = 10;
    public static final int MIN_PASSWORD_LENGTH = 6;
    
    // Error Messages
    public static final String ERROR_EMPTY_FIELDS = "Please enter all fields";
    public static final String ERROR_INVALID_MOBILE = "Invalid mobile number. Must be exactly 10 digits";
    public static final String ERROR_WRONG_ACCOUNT_TYPE = "This account is not registered for this login type";
    public static final String ERROR_INVALID_OTP = "Invalid OTP. Please try again";
    
    // Success Messages
    public static final String SUCCESS_LOGIN = "Login successful";
    public static final String SUCCESS_REGISTRATION = "Account created successfully";
    
    private Constants() {
        // Private constructor to prevent instantiation
    }
}
