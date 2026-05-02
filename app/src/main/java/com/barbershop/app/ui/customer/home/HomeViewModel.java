package com.barbershop.app.ui.customer.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.barbershop.app.data.model.Shop;
import com.barbershop.app.data.repository.AppointmentRepository;
import com.barbershop.app.utils.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for Customer Home screen.
 */
public class HomeViewModel extends ViewModel {
    
    private final AppointmentRepository appointmentRepository;
    
    private final MutableLiveData<HomeUiState> _uiState = new MutableLiveData<>();
    public final LiveData<HomeUiState> uiState = _uiState;
    
    public HomeViewModel() {
        this.appointmentRepository = new AppointmentRepository();
        loadShops();
    }
    
    public void loadShops() {
        _uiState.setValue(new HomeUiState(true, false, null, new ArrayList<>()));
        
        appointmentRepository.getAllShops().observeForever(resource -> {
            if (resource.isSuccess() && resource.data != null) {
                _uiState.setValue(new HomeUiState(false, false, null, resource.data));
            } else if (resource.isError()) {
                _uiState.setValue(new HomeUiState(false, true, resource.message, new ArrayList<>()));
            }
        });
    }
    
    public static class HomeUiState {
        public final boolean isLoading;
        public final boolean showError;
        public final String errorMessage;
        public final List<Shop> shops;
        
        public HomeUiState(boolean isLoading, boolean showError, String errorMessage, List<Shop> shops) {
            this.isLoading = isLoading;
            this.showError = showError;
            this.errorMessage = errorMessage;
            this.shops = shops;
        }
    }
}
