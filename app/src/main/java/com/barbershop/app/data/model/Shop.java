package com.barbershop.app.data.model;

import java.util.List;

/**
 * Shop model class representing barbershop owner data.
 * Maps to the Firebase Shops/{uid}/shop_details node.
 */
public class Shop {
    
    private String shopId;
    private String shopProfilePic;
    private String shopName;
    private String ownerName;
    private String shopMail;
    private String shopPassword;
    private String shopMobileNo;
    private String shopAddress;
    private String joiningYear;
    private float averageRating;
    private int reviewCount;
    private List<String> serviceImages;
    
    public Shop() {
        // Default constructor required for Firebase
    }
    
    // ... (existing getters/setters)

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public List<String> getServiceImages() {
        return serviceImages;
    }

    public void setServiceImages(List<String> serviceImages) {
        this.serviceImages = serviceImages;
    }
    
    public String getShopId() {
        return shopId;
    }
    
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    
    public Shop(String shopProfilePic, String shopName, String ownerName, 
                String shopMail, String shopPassword, String shopMobileNo, 
                String shopAddress) {
        this.shopProfilePic = shopProfilePic;
        this.shopName = shopName;
        this.ownerName = ownerName;
        this.shopMail = shopMail;
        this.shopPassword = shopPassword;
        this.shopMobileNo = shopMobileNo;
        this.shopAddress = shopAddress;
    }
    
    // Getters and Setters
    public String getShopProfilePic() {
        return shopProfilePic;
    }
    
    public void setShopProfilePic(String shopProfilePic) {
        this.shopProfilePic = shopProfilePic;
    }
    
    public String getShopName() {
        return shopName;
    }
    
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    
    public String getOwnerName() {
        return ownerName;
    }
    
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    
    public String getShopMail() {
        return shopMail;
    }
    
    public void setShopMail(String shopMail) {
        this.shopMail = shopMail;
    }
    
    public String getShopPassword() {
        return shopPassword;
    }
    
    public void setShopPassword(String shopPassword) {
        this.shopPassword = shopPassword;
    }
    
    public String getShopMobileNo() {
        return shopMobileNo;
    }
    
    public void setShopMobileNo(String shopMobileNo) {
        this.shopMobileNo = shopMobileNo;
    }
    
    public String getShopAddress() {
        return shopAddress;
    }
    
    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }
    
    public String getJoiningYear() {
        return joiningYear;
    }
    
    public void setJoiningYear(String joiningYear) {
        this.joiningYear = joiningYear;
    }
    
    /**
     * Builder class for creating Shop instances.
     */
    public static class Builder {
        private String shopProfilePic = "default";
        private String shopName;
        private String ownerName;
        private String shopMail;
        private String shopPassword;
        private String shopMobileNo;
        private String shopAddress;
        
        public Builder setShopProfilePic(String shopProfilePic) {
            this.shopProfilePic = shopProfilePic;
            return this;
        }
        
        public Builder setShopName(String shopName) {
            this.shopName = shopName;
            return this;
        }
        
        public Builder setOwnerName(String ownerName) {
            this.ownerName = ownerName;
            return this;
        }
        
        public Builder setShopMail(String shopMail) {
            this.shopMail = shopMail;
            return this;
        }
        
        public Builder setShopPassword(String shopPassword) {
            this.shopPassword = shopPassword;
            return this;
        }
        
        public Builder setShopMobileNo(String shopMobileNo) {
            this.shopMobileNo = shopMobileNo;
            return this;
        }
        
        public Builder setShopAddress(String shopAddress) {
            this.shopAddress = shopAddress;
            return this;
        }
        
        public Shop build() {
            return new Shop(shopProfilePic, shopName, ownerName, shopMail, 
                          shopPassword, shopMobileNo, shopAddress);
        }
    }
}
