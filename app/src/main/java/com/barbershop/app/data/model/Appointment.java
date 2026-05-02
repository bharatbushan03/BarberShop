package com.barbershop.app.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Appointment model class representing a booking.
 * Maps to the Firebase Users/{uid}/appointments/{shopId} and
 * Shops/{shopId}/appointments/{userId} nodes.
 */
public class Appointment {
    
    private String shopId;
    private String shopName;
    private String userId;
    private String customerName;
    private String appointmentDate;
    private String slot;
    private String totalAmount;
    private String status;
    private List<String> services;
    
    public Appointment() {
        this.services = new ArrayList<>();
    }
    
    public Appointment(String shopId, String shopName, String userId, 
                       String customerName, String appointmentDate, String slot, 
                       String totalAmount, String status, List<String> services) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.userId = userId;
        this.customerName = customerName;
        this.appointmentDate = appointmentDate;
        this.slot = slot;
        this.totalAmount = totalAmount;
        this.status = status;
        this.services = services != null ? services : new ArrayList<>();
    }
    
    // Getters and Setters
    public String getShopId() {
        return shopId;
    }
    
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    
    public String getShopName() {
        return shopName;
    }
    
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getAppointmentDate() {
        return appointmentDate;
    }
    
    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public String getSlot() {
        return slot;
    }
    
    public void setSlot(String slot) {
        this.slot = slot;
    }
    
    public String getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<String> getServices() {
        return services;
    }
    
    public void setServices(List<String> services) {
        this.services = services;
    }
    
    /**
     * Converts this appointment to a simplified display model.
     */
    public AppointmentDisplay toDisplayModel() {
        String servicesDisplay = "";
        if (services != null && !services.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String service : services) {
                if (service.contains("-")) {
                    sb.append(service.substring(0, service.indexOf('-')).trim()).append("\n");
                } else {
                    sb.append(service).append("\n");
                }
            }
            servicesDisplay = sb.toString();
        }
        
        return new AppointmentDisplay(shopName, appointmentDate, servicesDisplay, 
                                     slot, totalAmount, status);
    }
    
    /**
     * Simplified appointment model for UI display.
     */
    public static class AppointmentDisplay {
        private final String shopName;
        private final String appointmentDate;
        private final String services;
        private final String slot;
        private final String amount;
        private final String status;
        
        public AppointmentDisplay(String shopName, String appointmentDate, 
                                  String services, String slot, String amount, 
                                  String status) {
            this.shopName = shopName;
            this.appointmentDate = appointmentDate;
            this.services = services;
            this.slot = slot;
            this.amount = amount;
            this.status = status;
        }
        
        public String getShopName() { return shopName; }
        public String getAppointmentDate() { return appointmentDate; }
        public String getServices() { return services; }
        public String getSlot() { return slot; }
        public String getAmount() { return amount; }
        public String getStatus() { return status; }
    }
}
