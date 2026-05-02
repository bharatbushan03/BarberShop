package com.barbershop.app.data.model;

/**
 * User model class representing customer data.
 * Maps to the Firebase Users/{uid} node.
 */
public class User {
    
    private String userProfilePic;
    private String userName;
    private String userMail;
    private String userPassword;
    private String userMobileNo;
    
    public User() {
        // Default constructor required for Firebase
    }
    
    public User(String userProfilePic, String userName, String userMail, 
                String userPassword, String userMobileNo) {
        this.userProfilePic = userProfilePic;
        this.userName = userName;
        this.userMail = userMail;
        this.userPassword = userPassword;
        this.userMobileNo = userMobileNo;
    }
    
    // Getters and Setters
    public String getUserProfilePic() {
        return userProfilePic;
    }
    
    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserMail() {
        return userMail;
    }
    
    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }
    
    public String getUserPassword() {
        return userPassword;
    }
    
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    
    public String getUserMobileNo() {
        return userMobileNo;
    }
    
    public void setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
    }
    
    /**
     * Builder class for creating User instances.
     */
    public static class Builder {
        private String userProfilePic = "default";
        private String userName;
        private String userMail;
        private String userPassword;
        private String userMobileNo;
        
        public Builder setUserProfilePic(String userProfilePic) {
            this.userProfilePic = userProfilePic;
            return this;
        }
        
        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }
        
        public Builder setUserMail(String userMail) {
            this.userMail = userMail;
            return this;
        }
        
        public Builder setUserPassword(String userPassword) {
            this.userPassword = userPassword;
            return this;
        }
        
        public Builder setUserMobileNo(String userMobileNo) {
            this.userMobileNo = userMobileNo;
            return this;
        }
        
        public User build() {
            return new User(userProfilePic, userName, userMail, userPassword, userMobileNo);
        }
    }
}
