package com.barbershop.app.ui.customer.booking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.barbershop.app.data.model.Appointment;
import com.barbershop.app.data.model.Service;
import com.barbershop.app.data.model.Shop;
import com.barbershop.app.data.repository.AppointmentRepository;
import com.barbershop.app.data.repository.AuthRepository;
import com.barbershop.app.utils.Resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * ViewModel for Booking/Select Date and Time screen.
 */
public class BookingViewModel extends ViewModel {
    
    private final AppointmentRepository appointmentRepository;
    private final AuthRepository authRepository;
    
    private final MutableLiveData<BookingUiState> _uiState = new MutableLiveData<>();
    public final LiveData<BookingUiState> uiState = _uiState;
    
    // Data holders
    private String shopId;
    private String shopName;
    private String userName;
    private String selectedDate;
    private String selectedSlot;
    private final List<Service> availableServices = new ArrayList<>();
    private final List<Service> selectedServices = new ArrayList<>();
    private final List<String> availableSlots = new ArrayList<>();
    
    public BookingViewModel() {
        this.appointmentRepository = new AppointmentRepository();
        this.authRepository = new AuthRepository();
    }
    
    /**
     * Initialize with shop ID.
     */
    public void init(String shopId, List<String> preSelectedServices) {
        this.shopId = shopId;
        this.selectedDate = getDefaultDate();
        this.selectedSlot = null;
        this.shopName = null;
        this.userName = null;
        this.availableServices.clear();
        this.selectedServices.clear();
        this.availableSlots.clear();
        
        _uiState.setValue(new BookingUiState(true, false, null, false, false, 
                                           null, new ArrayList<>(), 0, new ArrayList<>()));
        
        loadShopDetails();
        loadUserDetails();
        
        // Load shop services
        loadShopServices(preSelectedServices);
        
        // Load shop slots
        loadShopSlots();
    }
    
    private void loadShopDetails() {
        LiveData<Resource<Shop>> shopResult = appointmentRepository.getShop(shopId);
        
        shopResult.observeForever(resource -> {
            if (resource.isSuccess() && resource.data != null) {
                shopName = resource.data.getShopName();
                updateUiState();
            } else if (resource.isError()) {
                _uiState.setValue(new BookingUiState(false, true, resource.message, false, false,
                                                   shopName, new ArrayList<>(availableServices),
                                                   calculateTotal(), new ArrayList<>(availableSlots)));
            }
        });
    }

    private void loadUserDetails() {
        String userId = authRepository.getCurrentUserId();
        if (userId != null) {
            authRepository.getUserDetails(userId).observeForever(resource -> {
                if (resource.isSuccess() && resource.data != null) {
                    userName = resource.data.getUserName();
                }
            });
        }
    }
    
    /**
     * Load services for the shop.
     */
    private void loadShopServices(List<String> preSelectedServices) {
        LiveData<Resource<List<Service>>> servicesResult = 
            appointmentRepository.getShopServices(shopId);
        
        servicesResult.observeForever(resource -> {
            if (resource.isSuccess() && resource.data != null) {
                availableServices.clear();
                availableServices.addAll(resource.data);
                selectedServices.clear();
                
                // Auto-select pre-selected services
                if (preSelectedServices != null) {
                    for (Service service : availableServices) {
                        if (preSelectedServices.contains(service.getServiceName())) {
                            selectedServices.add(service);
                        }
                    }
                }
                
                updateUiState();
            } else if (resource.isError()) {
                _uiState.setValue(new BookingUiState(false, true, resource.message, false, false,
                                                   shopName, new ArrayList<>(availableServices), 
                                                   calculateTotal(), new ArrayList<>(availableSlots)));
            }
        });
    }
    
    /**
     * Load available time slots.
     */
    private void loadShopSlots() {
        LiveData<Resource<List<String>>> slotsResult = appointmentRepository.getShopSlots(shopId);
        
        slotsResult.observeForever(resource -> {
            availableSlots.clear();
            if (resource.isSuccess() && resource.data != null) {
                availableSlots.addAll(resource.data);
                updateUiState();
            } else if (resource.isError()) {
                _uiState.setValue(new BookingUiState(false, true, resource.message, false, false,
                                                   shopName, new ArrayList<>(availableServices),
                                                   calculateTotal(), new ArrayList<>(availableSlots)));
            }
        });
    }
    
    /**
     * Select or deselect a service.
     */
    public void toggleServiceSelection(Service service) {
        if (selectedServices.contains(service)) {
            selectedServices.remove(service);
        } else {
            selectedServices.add(service);
        }
        updateUiState();
    }
    
    /**
     * Select a date.
     */
    public void selectDate(int day, int month, int year) {
        selectedDate = day + "-" + (month + 1) + "-" + year;
        updateUiState();
    }
    
    /**
     * Select a time slot.
     */
    public void selectSlot(String slot) {
        selectedSlot = slot;
        updateUiState();
    }
    
    /**
     * Make a booking.
     */
    public void makeBooking() {
        if (selectedServices.isEmpty()) {
            _uiState.setValue(new BookingUiState(false, true, "Please select at least one service",
                                               false, false, shopName, availableServices,
                                               calculateTotal(), availableSlots));
            return;
        }
        
        if (selectedSlot == null || selectedSlot.isEmpty()) {
            _uiState.setValue(new BookingUiState(false, true, "Please select a time slot",
                                               false, false, shopName, availableServices,
                                               calculateTotal(), availableSlots));
            return;
        }
        
        if (shopName == null || shopName.trim().isEmpty()) {
            _uiState.setValue(new BookingUiState(false, true,
                                               "Unable to load shop details. Please try again.",
                                               false, false, shopName, availableServices,
                                               calculateTotal(), availableSlots));
            return;
        }
        
        String userId = authRepository.getCurrentUserId();
        if (userId == null || userId.trim().isEmpty()) {
            _uiState.setValue(new BookingUiState(false, true,
                                               "Please log in again before booking an appointment.",
                                               false, false, shopName, availableServices,
                                               calculateTotal(), availableSlots));
            return;
        }
        
        _uiState.setValue(new BookingUiState(true, false, null, false, false,
                                          shopName, availableServices, calculateTotal(), availableSlots));
        
        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setShopId(shopId);
        appointment.setShopName(shopName);
        appointment.setAppointmentDate(selectedDate);
        appointment.setSlot(selectedSlot);
        appointment.setStatus("Pending");
        appointment.setTotalAmount(String.valueOf(calculateTotal()));
        
        List<String> serviceStrings = new ArrayList<>();
        for (Service service : selectedServices) {
            serviceStrings.add(service.getDisplayString());
        }
        appointment.setServices(serviceStrings);
        
        LiveData<Resource<Boolean>> result = appointmentRepository.bookAppointment(
            userId, shopId, appointment, userName != null ? userName : "Customer"
        );
        
        result.observeForever(resource -> {
            if (resource.isSuccess()) {
                _uiState.setValue(new BookingUiState(false, false, null, true, false,
                                                   shopName, availableServices, calculateTotal(), 
                                                   availableSlots));
            } else {
                _uiState.setValue(new BookingUiState(false, true, resource.message, false, false,
                                                   shopName, availableServices, calculateTotal(), 
                                                   availableSlots));
            }
        });
    }
    
    /**
     * Get UPI payment URI.
     */
    public String getUpiPaymentUri() {
        int amount = calculateTotal();
        String transactionNote = buildTransactionNote();
        
        return new android.net.Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa", "desepticon1910@oksbi") // Shop UPI ID
            .appendQueryParameter("pn", shopName)
            .appendQueryParameter("tn", transactionNote)
            .appendQueryParameter("am", String.valueOf(amount))
            .appendQueryParameter("cu", "INR")
            .appendQueryParameter("url", "your-transaction-url")
            .build()
            .toString();
    }
    
    private String buildTransactionNote() {
        StringBuilder note = new StringBuilder();
        for (Service service : selectedServices) {
            note.append(service.getDisplayString()).append("\n");
        }
        note.append("\nOn Date: ").append(selectedDate)
            .append("\nSlot: ").append(selectedSlot);
        return note.toString();
    }
    
    private int calculateTotal() {
        return appointmentRepository.calculateTotalAmount(selectedServices);
    }
    
    private String getDefaultDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH) + "-" + 
               (calendar.get(Calendar.MONTH) + 1) + "-" + 
               calendar.get(Calendar.YEAR);
    }
    
    private void updateUiState() {
        _uiState.setValue(new BookingUiState(false, false, null, false, false,
                                           shopName, new ArrayList<>(availableServices),
                                           calculateTotal(), new ArrayList<>(availableSlots)));
    }
    
    // Getters for current state
    public List<Service> getSelectedServices() {
        return new ArrayList<>(selectedServices);
    }
    
    public String getSelectedDate() {
        return selectedDate;
    }
    
    public String getSelectedSlot() {
        return selectedSlot;
    }
    
    public int getTotalAmount() {
        return calculateTotal();
    }
    
    public static class BookingUiState {
        public final boolean isLoading;
        public final boolean showError;
        public final String errorMessage;
        public final boolean isBookingSuccess;
        public final boolean canProceedToPayment;
        public final String shopName;
        public final List<Service> availableServices;
        public final int totalAmount;
        public final List<String> availableSlots;
        
        public BookingUiState(boolean isLoading, boolean showError, String errorMessage,
                             boolean isBookingSuccess, boolean canProceedToPayment,
                             String shopName, List<Service> availableServices,
                             int totalAmount, List<String> availableSlots) {
            this.isLoading = isLoading;
            this.showError = showError;
            this.errorMessage = errorMessage;
            this.isBookingSuccess = isBookingSuccess;
            this.canProceedToPayment = canProceedToPayment;
            this.shopName = shopName;
            this.availableServices = availableServices;
            this.totalAmount = totalAmount;
            this.availableSlots = availableSlots;
        }
    }
}
