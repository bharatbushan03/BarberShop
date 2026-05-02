package com.barbershop.app.ui.customer.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.barbershop.app.MapsActivity;
import com.barbershop.app.R;
import com.barbershop.app.data.model.Shop;
import com.barbershop.app.databinding.FragmentApphomescreenBinding;
import com.barbershop.app.showing_listofservices_for_shop;

import java.util.ArrayList;

/**
 * Refactored Home Fragment using MVVM.
 * Replaces Apphomescreen.java.
 */
public class HomeFragment extends Fragment {

    private FragmentApphomescreenBinding binding;
    private HomeViewModel viewModel;
    private ShopListAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentApphomescreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        
        setupRecyclerView();
        setupClickListeners();
        setupProgressDialog();
        observeViewModel();
    }

    private void setupRecyclerView() {
        adapter = new ShopListAdapter(new ArrayList<>(), this::onShopClick);
        binding.shopList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.shopList.setAdapter(adapter);
    }

    private void setupClickListeners() {
        binding.searchbyLocation.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MapsActivity.class);
            intent.putExtra("UserOrOwner", "User");
            startActivity(intent);
        });
    }

    private void setupProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading Shops");
        progressDialog.setMessage("Wait A Moment");
        progressDialog.setCancelable(false);
    }

    private void observeViewModel() {
        viewModel.uiState.observe(getViewLifecycleOwner(), state -> {
            if (state.isLoading) {
                progressDialog.show();
            } else {
                progressDialog.dismiss();
            }

            if (state.shops != null) {
                adapter = new ShopListAdapter(state.shops, this::onShopClick);
                binding.shopList.setAdapter(adapter);
            }
        });
    }

    private void onShopClick(Shop shop) {
        showing_listofservices_for_shop fragment = new showing_listofservices_for_shop();
        Bundle bundle = new Bundle();
        bundle.putString("ID", shop.getShopId());
        fragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fm, fragment)
                .addToBackStack(null)
                .commit();
        
        binding.searchbyLocation.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        binding = null;
    }
}
