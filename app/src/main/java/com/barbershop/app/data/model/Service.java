package com.barbershop.app.data.model;

/**
 * Service model class representing a shop service.
 * Maps to the Firebase Shops/{shopId}/services/{serviceId} node.
 */
public class Service {
    
    private String serviceId;
    private String serviceName;
    private String serviceDuration;
    private String servicePrice;
    private String serviceDescription;
    
    public Service() {
        // Default constructor required for Firebase
    }
    
    public Service(String serviceId, String serviceName, String serviceDuration, 
                   String servicePrice, String serviceDescription) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDuration = serviceDuration;
        this.servicePrice = servicePrice;
        this.serviceDescription = serviceDescription;
    }
    
    // Getters and Setters
    public String getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getServiceDuration() {
        return serviceDuration;
    }
    
    public void setServiceDuration(String serviceDuration) {
        this.serviceDuration = serviceDuration;
    }
    
    public String getServicePrice() {
        return servicePrice;
    }
    
    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }
    
    public String getServiceDescription() {
        return serviceDescription;
    }
    
    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }
    
    /**
     * Gets formatted display string for the service.
     * Format: "ServiceName - Duration mins - Price$"
     */
    public String getDisplayString() {
        return serviceName + " - " + serviceDuration + "mins - " + servicePrice + "$";
    }
    
    /**
     * Builder class for creating Service instances.
     */
    public static class Builder {
        private String serviceId;
        private String serviceName;
        private String serviceDuration;
        private String servicePrice;
        private String serviceDescription;
        
        public Builder setServiceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }
        
        public Builder setServiceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }
        
        public Builder setServiceDuration(String serviceDuration) {
            this.serviceDuration = serviceDuration;
            return this;
        }
        
        public Builder setServicePrice(String servicePrice) {
            this.servicePrice = servicePrice;
            return this;
        }
        
        public Builder setServiceDescription(String serviceDescription) {
            this.serviceDescription = serviceDescription;
            return this;
        }
        
        public Service build() {
            return new Service(serviceId, serviceName, serviceDuration, 
                             servicePrice, serviceDescription);
        }
    }
}
