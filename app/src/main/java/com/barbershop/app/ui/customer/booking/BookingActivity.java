package com.barbershop.app.ui.customer.booking;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.barbershop.app.R;
import com.barbershop.app.custHomeActivity;
import com.barbershop.app.data.model.Service;
import com.barbershop.app.databinding.ActivitySelectDateAndTimeBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Booking Activity - MVVM Refactored (formerly select_date_and_time_activity).
 * 
 * BEFORE: 467 lines with direct Firebase calls, payment logic, slot handling
 * AFTER: ~200 lines - clean UI with ViewModel handling all business logic
 */
public class BookingActivity extends AppCompatActivity {
    
    private static final int PAY_REQUEST_CODE = 123;
    
    private ActivitySelectDateAndTimeBinding binding;
    private BookingViewModel viewModel;
    
    private final CheckBox[] slotCheckBoxes = new CheckBox[6];
    private String shopId;
    private ArrayList<String> selectedServices;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectDateAndTimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        Objects.requireNonNull(getSupportActionBar()).hide();
        
        // Get intent extras
        shopId = getIntent().getStringExtra("shop_id");
        selectedServices = getIntent().getStringArrayListExtra("selected_services");
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(BookingViewModel.class);
        viewModel.init(shopId, selectedServices);
        
        setupSlotCheckboxes();
        setupClickListeners();
        observeViewModel();
        
        // Set default date
        Calendar calendar = Calendar.getInstance();
        binding.calendarView.setMinDate(calendar.getTimeInMillis());
    }
    
    private void setupSlotCheckboxes() {
        slotCheckBoxes[0] = binding.slot1;
        slotCheckBoxes[1] = binding.slot2;
        slotCheckBoxes[2] = binding.slot3;
        slotCheckBoxes[3] = binding.slot4;
        slotCheckBoxes[4] = binding.slot5;
        slotCheckBoxes[5] = binding.slot6;
        
        // Initially hide all
        for (CheckBox checkBox : slotCheckBoxes) {
            checkBox.setVisibility(CheckBox.GONE);
        }
        
        // Setup exclusive selection
        for (int i = 0; i < slotCheckBoxes.length; i++) {
            final int index = i;
            slotCheckBoxes[i].setOnClickListener(v -> {
                clearOtherSlots(index);
                viewModel.selectSlot(slotCheckBoxes[index].getText().toString());
            });
        }
    }
    
    private void clearOtherSlots(int selectedIndex) {
        for (int i = 0; i < slotCheckBoxes.length; i++) {
            if (i != selectedIndex) {
                slotCheckBoxes[i].setChecked(false);
            }
        }
    }
    
    private void setupClickListeners() {
        // Calendar date selection
        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            viewModel.selectDate(dayOfMonth, month, year);
        });
        
        // Make payment/book button
        binding.makePaymentBtn.setOnClickListener(v -> {
            viewModel.makeBooking();
        });
    }
    
    private void observeViewModel() {
        viewModel.uiState.observe(this, state -> {
            // Update shop name
            if (state.shopName != null) {
                binding.shopNameInSelectdateActivity.setText(
                    getString(R.string.appointment_booking_title, state.shopName));
            }
            
            // Update services list
            updateServicesList(state.availableServices);
            
            // Update slots visibility
            updateSlotsVisibility(state.availableSlots);
            
            // Handle loading
            if (state.isLoading) {
                binding.makePaymentBtn.setEnabled(false);
            } else {
                binding.makePaymentBtn.setEnabled(true);
            }
            
            // Handle errors
            if (state.showError && state.errorMessage != null) {
                Toast.makeText(this, state.errorMessage, Toast.LENGTH_LONG).show();
            }
            
            // Handle success - proceed to payment
            if (state.isBookingSuccess) {
                launchUpiPayment();
            }
        });
    }
    
    private void updateServicesList(List<Service> services) {
        List<String> displayStrings = new ArrayList<>();
        for (Service service : services) {
            displayStrings.add(service.getDisplayString());
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this, android.R.layout.simple_list_item_1, displayStrings);
        binding.showingSelectedServicesListview.setAdapter(adapter);
    }
    
    private void updateSlotsVisibility(List<String> slots) {
        // Hide all first
        for (CheckBox checkBox : slotCheckBoxes) {
            checkBox.setVisibility(CheckBox.GONE);
            checkBox.setChecked(false);
            checkBox.setText("");
        }
        
        // Show available slots
        int count = Math.min(slots.size(), slotCheckBoxes.length);
        for (int i = 0; i < count; i++) {
            slotCheckBoxes[i].setVisibility(CheckBox.VISIBLE);
            slotCheckBoxes[i].setText(slots.get(i));
        }
    }
    
    private void launchUpiPayment() {
        String uriString = viewModel.getUpiPaymentUri();
        Uri uri = Uri.parse(uriString);
        
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        
        Intent chooser = Intent.createChooser(intent, "Pay with");
        if (chooser.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooser, PAY_REQUEST_CODE);
        } else {
            Toast.makeText(this, "No UPI app found. Please install one.", 
                          Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == PAY_REQUEST_CODE) {
            handlePaymentResult(resultCode, data);
        }
    }
    
    private void handlePaymentResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            String response = data.getStringExtra("response");
            if (response != null && !response.equals("nothing")) {
                // Parse UPI response
                String status = parseUpiResponse(response);
                
                if (status.equals("success")) {
                    Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show();
                } else {
                    // Payment may have been cancelled or failed
                    // But appointment is already booked - show success
                    Toast.makeText(this, "Appointment booked successfully!", 
                                  Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // User cancelled or no response
            Toast.makeText(this, "Appointment booked successfully!", 
                          Toast.LENGTH_SHORT).show();
        }
        
        // Navigate to home
        startActivity(new Intent(this, custHomeActivity.class));
        finish();
    }
    
    private String parseUpiResponse(String response) {
        String[] responseParams = response.split("&");
        for (String param : responseParams) {
            String[] keyValue = param.split("=");
            if (keyValue.length >= 2 && keyValue[0].equalsIgnoreCase("status")) {
                return keyValue[1].toLowerCase(Locale.ROOT);
            }
        }
        return "unknown";
    }
    
    private boolean isConnectionAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        }
        return false;
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
