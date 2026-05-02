package com.barbershop.app.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.barbershop.app.data.model.Appointment;
import com.barbershop.app.data.model.Service;
import com.barbershop.app.data.model.Shop;
import com.barbershop.app.data.remote.firebase.FirebaseDatabaseSource;
import com.barbershop.app.utils.Resource;

import java.util.List;

/**
 * Repository for appointment and booking operations.
 */
public class AppointmentRepository {
    
    private final FirebaseDatabaseSource databaseSource;
    
    public AppointmentRepository() {
        this.databaseSource = new FirebaseDatabaseSource();
    }
    
    /**
     * Get all appointments for a customer.
     */
    public LiveData<Resource<List<Appointment>>> getUserAppointments(String userId) {
        MutableLiveData<Resource<List<Appointment>>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        databaseSource.getUserAppointments(userId, new FirebaseDatabaseSource.AppointmentsCallback() {
            @Override
            public void onSuccess(List<Appointment> appointments) {
                resultLiveData.setValue(Resource.success(appointments));
            }
            
            @Override
            public void onError(String error) {
                resultLiveData.setValue(Resource.error(error, null));
            }
        });
        
        return resultLiveData;
    }
    
    /**
     * Get all shops for home screen.
     */
    public LiveData<Resource<List<Shop>>> getAllShops() {
        MutableLiveData<Resource<List<Shop>>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        databaseSource.getAllShops(new FirebaseDatabaseSource.ShopsCallback() {
            @Override
            public void onSuccess(List<Shop> shops) {
                resultLiveData.setValue(Resource.success(shops));
            }
            
            @Override
            public void onError(String error) {
                resultLiveData.setValue(Resource.error(error, null));
            }
        });
        
        return resultLiveData;
    }
    
    /**
     * Get shop details for booking flows.
     */
    public LiveData<Resource<Shop>> getShop(String shopId) {
        MutableLiveData<Resource<Shop>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        databaseSource.getShop(shopId, new FirebaseDatabaseSource.ShopCallback() {
            @Override
            public void onSuccess(Shop shop) {
                resultLiveData.setValue(Resource.success(shop));
            }
            
            @Override
            public void onError(String error) {
                resultLiveData.setValue(Resource.error(error, null));
            }
        });
        
        return resultLiveData;
    }
    
    /**
     * Get available services for a shop.
     */
    public LiveData<Resource<List<Service>>> getShopServices(String shopId) {
        MutableLiveData<Resource<List<Service>>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        databaseSource.getShopServices(shopId, new FirebaseDatabaseSource.ServicesCallback() {
            @Override
            public void onSuccess(List<Service> services) {
                resultLiveData.setValue(Resource.success(services));
            }
            
            @Override
            public void onError(String error) {
                resultLiveData.setValue(Resource.error(error, null));
            }
        });
        
        return resultLiveData;
    }
    
    /**
     * Get available time slots for a shop.
     */
    public LiveData<Resource<List<String>>> getShopSlots(String shopId) {
        MutableLiveData<Resource<List<String>>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        databaseSource.getShopSlots(shopId, new FirebaseDatabaseSource.SlotsCallback() {
            @Override
            public void onSuccess(List<String> slots) {
                resultLiveData.setValue(Resource.success(slots));
            }
            
            @Override
            public void onError(String error) {
                resultLiveData.setValue(Resource.error(error, null));
            }
        });
        
        return resultLiveData;
    }
    
    /**
     * Get all appointments for a shop.
     */
    public LiveData<Resource<List<Appointment>>> getShopAppointments(String shopId) {
        MutableLiveData<Resource<List<Appointment>>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        databaseSource.getShopAppointments(shopId, new FirebaseDatabaseSource.AppointmentsCallback() {
            @Override
            public void onSuccess(List<Appointment> appointments) {
                resultLiveData.setValue(Resource.success(appointments));
            }
            
            @Override
            public void onError(String error) {
                resultLiveData.setValue(Resource.error(error, null));
            }
        });
        
        return resultLiveData;
    }

    /**
     * Update appointment status.
     */
    public LiveData<Resource<Boolean>> updateAppointmentStatus(String userId, String shopId, 
                                                              String date, String status) {
        MutableLiveData<Resource<Boolean>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        databaseSource.updateAppointmentStatus(userId, shopId, date, status, 
            new FirebaseDatabaseSource.DatabaseCallback() {
                @Override
                public void onSuccess() {
                    resultLiveData.setValue(Resource.success(true));
                }
                
                @Override
                public void onError(String errorMessage) {
                    resultLiveData.setValue(Resource.error(errorMessage, false));
                }
            });
        
        return resultLiveData;
    }

    /**
     * Book an appointment.
     */
    public LiveData<Resource<Boolean>> bookAppointment(String userId, String shopId,
                                                       Appointment appointment, String customerName) {
        MutableLiveData<Resource<Boolean>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        databaseSource.bookAppointment(userId, shopId, appointment, customerName,
            new FirebaseDatabaseSource.DatabaseCallback() {
                @Override
                public void onSuccess() {
                    resultLiveData.setValue(Resource.success(true));
                }
                
                @Override
                public void onError(String errorMessage) {
                    resultLiveData.setValue(Resource.error(errorMessage, false));
                }
            }
        );
        
        return resultLiveData;
    }
    
    /**
     * Cancel an appointment.
     */
    public LiveData<Resource<Boolean>> cancelAppointment(String userId, String shopId, 
                                                         String date) {
        MutableLiveData<Resource<Boolean>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Resource.loading(null));
        
        databaseSource.cancelAppointment(userId, shopId, date,
            new FirebaseDatabaseSource.DatabaseCallback() {
                @Override
                public void onSuccess() {
                    resultLiveData.setValue(Resource.success(true));
                }
                
                @Override
                public void onError(String errorMessage) {
                    resultLiveData.setValue(Resource.error(errorMessage, false));
                }
            }
        );
        
        return resultLiveData;
    }
    
    /**
     * Calculate total amount for selected services.
     */
    public int calculateTotalAmount(List<Service> selectedServices) {
        int total = 0;
        for (Service service : selectedServices) {
            try {
                total += Integer.parseInt(service.getServicePrice());
            } catch (NumberFormatException e) {
                // Skip invalid price
            }
        }
        return total;
    }
}
