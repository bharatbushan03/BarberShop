package com.barbershop.app.utils;

import android.util.Patterns;

/**
 * Utility class for input validation.
 */
public class ValidationUtils {
    
    /**
     * Validates email format.
     * 
     * @param email Email string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return email != null && !email.trim().isEmpty() && 
               Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    /**
     * Validates mobile number (Indian format - 10 digits).
     * 
     * @param mobile Mobile number string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidMobile(String mobile) {
        return mobile != null && mobile.length() == Constants.MOBILE_NUMBER_LENGTH &&
               mobile.matches("[0-9]+");
    }
    
    /**
     * Validates password length.
     * 
     * @param password Password string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null && !password.isEmpty() &&
               password.length() >= Constants.MIN_PASSWORD_LENGTH;
    }
    
    /**
     * Checks if any of the provided strings are empty.
     * 
     * @param fields Varargs of strings to check
     * @return true if any field is empty, false otherwise
     */
    public static boolean hasEmptyFields(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    private ValidationUtils() {
        // Private constructor to prevent instantiation
    }
}
