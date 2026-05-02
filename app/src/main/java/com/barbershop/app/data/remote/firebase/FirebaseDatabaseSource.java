package com.barbershop.app.data.remote.firebase;

import com.barbershop.app.data.model.Appointment;
import com.barbershop.app.data.model.Service;
import com.barbershop.app.data.model.Shop;
import com.barbershop.app.data.model.User;
import com.barbershop.app.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles all Firebase Realtime Database operations.
 * Acts as the single source for all database interactions.
 */
public class FirebaseDatabaseSource {
    
    private final DatabaseReference databaseReference;
    
    public FirebaseDatabaseSource() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    
    // ==================== USER OPERATIONS ====================
    
    /**
     * Check if user exists in the Users node.
     */
    public void checkUserExists(String uid, UserCheckCallback callback) {
        databaseReference.child(Constants.DB_USERS).child(uid)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    callback.onResult(snapshot.exists());
                }
                
                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onError(error.getMessage());
                }
            });
    }
    
    /**
     * Save user profile to database.
     */
    public void saveUser(String uid, User user, DatabaseCallback callback) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("user_profile_pic", user.getUserProfilePic());
        userData.put("user_name", user.getUserName());
        userData.put("user_mail", user.getUserMail());
        userData.put("user_password", user.getUserPassword());
        userData.put("user_mobile_no", user.getUserMobileNo());
        
        databaseReference.child(Constants.DB_USERS).child(uid).setValue(userData)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError(task.getException() != null 
                        ? task.getException().getMessage() 
                        : "Failed to save user");
                }
            });
    }
    
    /**
     * Get user profile from database.
     */
    public void getUser(String uid, UserCallback callback) {
        databaseReference.child(Constants.DB_USERS).child(uid)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = new User();
                        user.setUserProfilePic(getStringValue(snapshot, "user_profile_pic", "userProfilePic"));
                        user.setUserName(getStringValue(snapshot, "user_name", "userName"));
                        user.setUserMail(getStringValue(snapshot, "user_mail", "userMail"));
                        user.setUserPassword(getStringValue(snapshot, "user_password", "userPassword"));
                        user.setUserMobileNo(getStringValue(snapshot, "user_mobile_no", "userMobileNo"));
                        callback.onSuccess(user);
                    } else {
                        callback.onError("User not found");
                    }
                }
                
                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onError(error.getMessage());
                }
            });
    }
    
    // ==================== SHOP OPERATIONS ====================
    
    /**
     * Check if shop exists in the Shops node.
     */
    public void checkShopExists(String uid, UserCheckCallback callback) {
        databaseReference.child(Constants.DB_SHOPS).child(uid)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    callback.onResult(snapshot.exists());
                }
                
                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onError(error.getMessage());
                }
            });
    }
    
    /**
     * Save shop profile to database.
     */
    public void saveShop(String uid, Shop shop, DatabaseCallback callback) {
        Map<String, Object> shopData = new HashMap<>();
        shopData.put("shop_profile_pic", shop.getShopProfilePic());
        shopData.put("shop_name", shop.getShopName());
        shopData.put("owner_name", shop.getOwnerName());
        shopData.put("shop_mail", shop.getShopMail());
        shopData.put("shop_password", shop.getShopPassword());
        shopData.put("shop_mobile_no", shop.getShopMobileNo());
        shopData.put("shop_address", shop.getShopAddress());
        if (shop.getJoiningYear() != null && !shop.getJoiningYear().trim().isEmpty()) {
            shopData.put("joining_year", shop.getJoiningYear());
        }
        
        databaseReference.child(Constants.DB_SHOPS).child(uid)
            .child(Constants.DB_SHOP_DETAILS).setValue(shopData)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError(task.getException() != null 
                        ? task.getException().getMessage() 
                        : "Failed to save shop");
                }
            });
    }
    
    /**
     * Get shop details from database.
     */
    public void getShop(String shopId, ShopCallback callback) {
        databaseReference.child(Constants.DB_SHOPS).child(shopId)
            .child(Constants.DB_SHOP_DETAILS)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Shop shop = new Shop();
                        shop.setShopProfilePic(getStringValue(snapshot, "shop_profile_pic", "shopProfilePic"));
                        shop.setShopName(getStringValue(snapshot, "shop_name", "shopName"));
                        shop.setOwnerName(getStringValue(snapshot, "owner_name", "ownerName"));
                        shop.setShopMail(getStringValue(snapshot, "shop_mail", "shopMail"));
                        shop.setShopPassword(getStringValue(snapshot, "shop_password", "shopPassword"));
                        shop.setShopMobileNo(getStringValue(snapshot, "shop_mobile_no", "shopMobileNo"));
                        shop.setShopAddress(getStringValue(snapshot, "shop_address", "shopAddress"));
                        shop.setJoiningYear(getStringValue(snapshot, "joining_year", "joiningYear"));
                        callback.onSuccess(shop);
                    } else {
                        callback.onError("Shop not found");
                    }
                }
                
                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onError(error.getMessage());
                }
            });
    }
    
    /**
     * Update shop joining year.
     */
    public void updateShopJoiningYear(String uid, String year, DatabaseCallback callback) {
        databaseReference.child(Constants.DB_SHOPS).child(uid)
            .child(Constants.DB_SHOP_DETAILS)
            .child("joining_year").setValue(year)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError(task.getException() != null 
                        ? task.getException().getMessage() 
                        : "Failed to update joining year");
                }
            });
    }
    
    // ==================== APPOINTMENT OPERATIONS ====================
    
    /**
     * Get all appointments for a user.
     */
    public void getUserAppointments(String userId, AppointmentsCallback callback) {
        databaseReference.child(Constants.DB_USERS).child(userId)
            .child(Constants.DB_APPOINTMENTS)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<Appointment> appointments = new ArrayList<>();
                    
                    for (DataSnapshot shopSnapshot : snapshot.getChildren()) {
                        String shopId = shopSnapshot.getKey();
                        String shopName = shopSnapshot.child("shop_name").getValue(String.class);
                        
                        DataSnapshot datesSnapshot = shopSnapshot.child("appointment_dates");
                        for (DataSnapshot dateSnapshot : datesSnapshot.getChildren()) {
                            Appointment appointment = parseAppointmentSnapshot(
                                shopId, shopName, dateSnapshot);
                            appointments.add(appointment);
                        }
                    }
                    
                    callback.onSuccess(appointments);
                }
                
                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onError(error.getMessage());
                }
            });
    }
    
    /**
     * Book an appointment (saves to both user and shop nodes).
     */
    public void bookAppointment(String userId, String shopId, Appointment appointment,
                               String customerName, DatabaseCallback callback) {
        String date = appointment.getAppointmentDate();
        
        // User side data
        Map<String, Object> userAppointmentData = new HashMap<>();
        userAppointmentData.put("shop_name", appointment.getShopName());
        userAppointmentData.put("appointment_dates/" + date + "/slot", appointment.getSlot());
        userAppointmentData.put("appointment_dates/" + date + "/total_amount", appointment.getTotalAmount());
        userAppointmentData.put("appointment_dates/" + date + "/status", appointment.getStatus());
        
        // Shop side data
        Map<String, Object> shopAppointmentData = new HashMap<>();
        shopAppointmentData.put("slot", appointment.getSlot());
        shopAppointmentData.put("date", date);
        shopAppointmentData.put("status", appointment.getStatus());
        shopAppointmentData.put("Total_amount", appointment.getTotalAmount());
        shopAppointmentData.put("customer_name", customerName);
        
        // Add services
        List<String> services = appointment.getServices();
        if (services != null) {
            for (String service : services) {
                String serviceKey = service.contains("-") ? 
                    service.substring(0, service.indexOf('-')).trim() : service;
                userAppointmentData.put("appointment_dates/" + date + "/services/" + serviceKey, service);
                shopAppointmentData.put("selected_services/" + serviceKey, service);
            }
        }
        
        // Save to user appointments
        databaseReference.child(Constants.DB_USERS).child(userId)
            .child(Constants.DB_APPOINTMENTS).child(shopId)
            .updateChildren(userAppointmentData);
        
        // Save to shop appointments
        databaseReference.child(Constants.DB_SHOPS).child(shopId)
            .child(Constants.DB_APPOINTMENTS).child(userId).child(date)
            .updateChildren(shopAppointmentData)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError(task.getException() != null 
                        ? task.getException().getMessage() 
                        : "Failed to book appointment");
                }
            });
    }
    
    /**
     * Get all appointments for a shop.
     */
    public void getShopAppointments(String shopId, AppointmentsCallback callback) {
        databaseReference.child(Constants.DB_SHOPS).child(shopId)
            .child(Constants.DB_APPOINTMENTS)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<Appointment> appointments = new ArrayList<>();
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        for (DataSnapshot dateSnapshot : userSnapshot.getChildren()) {
                            Appointment appt = parseShopAppointmentSnapshot(
                                userId, dateSnapshot);
                            appointments.add(appt);
                        }
                    }
                    callback.onSuccess(appointments);
                }
                
                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onError(error.getMessage());
                }
            });
    }

    /**
     * Update appointment status.
     */
    public void updateAppointmentStatus(String userId, String shopId, String date, 
                                       String status, DatabaseCallback callback) {
        databaseReference.child(Constants.DB_USERS).child(userId)
            .child(Constants.DB_APPOINTMENTS).child(shopId)
            .child("appointment_dates").child(date).child("status").setValue(status);
            
        databaseReference.child(Constants.DB_SHOPS).child(shopId)
            .child(Constants.DB_APPOINTMENTS).child(userId).child(date)
            .child("status").setValue(status)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError(task.getException() != null ? 
                        task.getException().getMessage() : "Update failed");
                }
            });
    }

    /**
     * Cancel an appointment.
     */
    public void cancelAppointment(String userId, String shopId, String date, 
                                  DatabaseCallback callback) {
        // Remove from user side
        databaseReference.child(Constants.DB_USERS).child(userId)
            .child(Constants.DB_APPOINTMENTS).child(shopId)
            .child("appointment_dates").child(date).removeValue();
        
        // Remove from shop side
        databaseReference.child(Constants.DB_SHOPS).child(shopId)
            .child(Constants.DB_APPOINTMENTS).child(userId).child(date)
            .removeValue((error, ref) -> {
                if (error != null) {
                    callback.onError(error.getMessage());
                } else {
                    callback.onSuccess();
                }
            });
    }
    
    // ==================== SERVICE OPERATIONS ====================
    
    /**
     * Get services for a shop.
     */
    public void getShopServices(String shopId, ServicesCallback callback) {
        databaseReference.child(Constants.DB_SHOPS).child(shopId)
            .child(Constants.DB_SERVICES)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<Service> services = new ArrayList<>();
                    for (DataSnapshot serviceSnapshot : snapshot.getChildren()) {
                        Service service = new Service();
                        service.setServiceId(serviceSnapshot.getKey());
                        service.setServiceName(getStringValue(serviceSnapshot, "service_name", "serviceName"));
                        service.setServiceDuration(getStringValue(serviceSnapshot, "service_duration", "serviceDuration"));
                        service.setServicePrice(getStringValue(serviceSnapshot, "service_price", "servicePrice"));
                        service.setServiceDescription(
                            getStringValue(serviceSnapshot, "service_description", "serviceDescription"));
                        services.add(service);
                    }
                    callback.onSuccess(services);
                }
                
                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onError(error.getMessage());
                }
            });
    }
    
    /**
     * Get available time slots for a shop.
     */
    public void getShopSlots(String shopId, SlotsCallback callback) {
        databaseReference.child(Constants.DB_SHOPS).child(shopId)
            .child(Constants.DB_SHOP_DETAILS)
            .child("slots_for_booking")
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<String> slots = new ArrayList<>();
                    if (snapshot.exists()) {
                        for (DataSnapshot slotSnapshot : snapshot.getChildren()) {
                            String slot = slotSnapshot.getValue(String.class);
                            if (slot != null) {
                                slots.add(slot);
                            }
                        }
                    }
                    callback.onSuccess(slots);
                }
                
                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onError(error.getMessage());
                }
            });
    }
    
    // ==================== HELPER METHODS ====================
    
    private Appointment parseAppointmentSnapshot(String shopId, String shopName, 
                                                  DataSnapshot dateSnapshot) {
        Appointment appointment = new Appointment();
        appointment.setShopId(shopId);
        appointment.setShopName(shopName);
        appointment.setAppointmentDate(dateSnapshot.getKey());
        appointment.setSlot(dateSnapshot.child("slot").getValue(String.class));
        
        Object amountObj = dateSnapshot.child("total_amount").getValue();
        appointment.setTotalAmount(amountObj != null ? amountObj.toString() : "0");
        
        Object statusObj = dateSnapshot.child("status").getValue();
        appointment.setStatus(statusObj != null ? statusObj.toString() : "Pending");
        
        List<String> services = new ArrayList<>();
        DataSnapshot servicesSnapshot = dateSnapshot.child("services");
        for (DataSnapshot serviceSnapshot : servicesSnapshot.getChildren()) {
            String service = serviceSnapshot.getValue(String.class);
            if (service != null) {
                services.add(service);
            }
        }
        appointment.setServices(services);
        
        return appointment;
    }

    private Appointment parseShopAppointmentSnapshot(String userId, DataSnapshot dateSnapshot) {
        Appointment appointment = new Appointment();
        appointment.setUserId(userId);
        appointment.setAppointmentDate(dateSnapshot.child("date").getValue(String.class));
        if (appointment.getAppointmentDate() == null) {
            appointment.setAppointmentDate(dateSnapshot.getKey());
        }
        appointment.setSlot(dateSnapshot.child("slot").getValue(String.class));
        appointment.setCustomerName(dateSnapshot.child("customer_name").getValue(String.class));
        
        Object amountObj = dateSnapshot.child("Total_amount").getValue();
        appointment.setTotalAmount(amountObj != null ? amountObj.toString() : "0");
        
        Object statusObj = dateSnapshot.child("status").getValue();
        appointment.setStatus(statusObj != null ? statusObj.toString() : "Pending");
        
        List<String> services = new ArrayList<>();
        DataSnapshot servicesSnapshot = dateSnapshot.child("selected_services");
        for (DataSnapshot serviceSnapshot : servicesSnapshot.getChildren()) {
            String service = serviceSnapshot.getValue(String.class);
            if (service != null) {
                services.add(service);
            }
        }
        appointment.setServices(services);
        
        return appointment;
    }
    
    private String getStringValue(DataSnapshot snapshot, String legacyKey, String camelCaseKey) {
        String value = snapshot.child(legacyKey).getValue(String.class);
        if (value == null) {
            value = snapshot.child(camelCaseKey).getValue(String.class);
        }
        return value;
    }
    
    /**
     * Get all shops from the database.
     */
    public void getAllShops(ShopsCallback callback) {
        databaseReference.child(Constants.DB_SHOPS)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<Shop> shops = new ArrayList<>();
                    for (DataSnapshot shopSnapshot : snapshot.getChildren()) {
                        DataSnapshot detailsSnapshot = shopSnapshot.child(Constants.DB_SHOP_DETAILS);
                        if (detailsSnapshot.exists()) {
                            Shop shop = new Shop();
                            shop.setShopId(shopSnapshot.getKey());
                            shop.setShopProfilePic(getStringValue(detailsSnapshot, "shop_profile_pic", "shopProfilePic"));
                            shop.setShopName(getStringValue(detailsSnapshot, "shop_name", "shopName"));
                            shop.setOwnerName(getStringValue(detailsSnapshot, "owner_name", "ownerName"));
                            shop.setShopMail(getStringValue(detailsSnapshot, "shop_mail", "shopMail"));
                            shop.setShopPassword(getStringValue(detailsSnapshot, "shop_password", "shopPassword"));
                            shop.setShopMobileNo(getStringValue(detailsSnapshot, "shop_mobile_no", "shopMobileNo"));
                            shop.setShopAddress(getStringValue(detailsSnapshot, "shop_address", "shopAddress"));
                            shop.setJoiningYear(getStringValue(detailsSnapshot, "joining_year", "joiningYear"));
                            
                            // Calculate Ratings
                            DataSnapshot reviewsSnapshot = shopSnapshot.child("Reviews");
                            if (reviewsSnapshot.exists()) {
                                long reviewCount = reviewsSnapshot.getChildrenCount();
                                float totalRating = 0;
                                for (DataSnapshot review : reviewsSnapshot.getChildren()) {
                                    Float r = review.child("ratings").getValue(Float.class);
                                    if (r != null) totalRating += r;
                                }
                                shop.setReviewCount((int) reviewCount);
                                shop.setAverageRating(reviewCount > 0 ? totalRating / reviewCount : 0f);
                            }
                            
                            // Fetch Service Images
                            DataSnapshot imagesSnapshot = shopSnapshot.child("Images").child("Shop_Servces_Images");
                            if (imagesSnapshot.exists()) {
                                List<String> images = new ArrayList<>();
                                for (DataSnapshot img : imagesSnapshot.getChildren()) {
                                    String url = img.getValue(String.class);
                                    if (url != null) images.add(url);
                                }
                                shop.setServiceImages(images);
                            }
                            
                            shops.add(shop);
                        }
                    }
                    callback.onSuccess(shops);
                }
                
                @Override
                public void onCancelled(DatabaseError error) {
                    callback.onError(error.getMessage());
                }
            });
    }
    
    // Callback interfaces
    public interface DatabaseCallback {
        void onSuccess();
        void onError(String errorMessage);
    }
    
    public interface UserCheckCallback {
        void onResult(boolean exists);
        void onError(String error);
    }
    
    public interface UserCallback {
        void onSuccess(User user);
        void onError(String error);
    }
    
    public interface ShopCallback {
        void onSuccess(Shop shop);
        void onError(String error);
    }
    
    public interface ShopsCallback {
        void onSuccess(List<Shop> shops);
        void onError(String error);
    }
    
    public interface AppointmentsCallback {
        void onSuccess(List<Appointment> appointments);
        void onError(String error);
    }
    
    public interface ServicesCallback {
        void onSuccess(List<Service> services);
        void onError(String error);
    }
    
    public interface SlotsCallback {
        void onSuccess(List<String> slots);
        void onError(String error);
    }
}
