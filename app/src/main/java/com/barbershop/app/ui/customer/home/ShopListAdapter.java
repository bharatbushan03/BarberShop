package com.barbershop.app.ui.customer.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbershop.app.R;
import com.barbershop.app.data.model.Shop;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Refactored adapter for shop listing.
 * Optimized to use pre-populated data from ViewModel.
 */
public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ViewHolder> {

    private final List<Shop> shopList;
    private final OnShopClickListener listener;

    public interface OnShopClickListener {
        void onShopClick(Shop shop);
    }

    public ShopListAdapter(List<Shop> shopList, OnShopClickListener listener) {
        this.shopList = shopList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shop shop = shopList.get(position);
        
        holder.shopName.setText(shop.getShopName());
        holder.ownerName.setText(shop.getOwnerName());
        
        // Load profile pic
        if (shop.getShopProfilePic() != null && !shop.getShopProfilePic().equals("default")) {
            Picasso.get().load(shop.getShopProfilePic()).into(holder.ownerPic);
        } else {
            holder.ownerPic.setImageResource(R.drawable.user12); // Fallback
        }
        
        // Set Ratings
        holder.ratingBar.setRating(shop.getAverageRating());
        holder.ratingText.setText(String.format("%.1f(%d)", 
            shop.getAverageRating(), shop.getReviewCount()));
        
        // Load Service Images (up to 4)
        List<String> images = shop.getServiceImages();
        if (images != null) {
            if (images.size() > 0) Picasso.get().load(images.get(0)).into(holder.serviceImg1);
            if (images.size() > 1) Picasso.get().load(images.get(1)).into(holder.serviceImg2);
            if (images.size() > 2) Picasso.get().load(images.get(2)).into(holder.serviceImg3);
            if (images.size() > 3) Picasso.get().load(images.get(3)).into(holder.serviceImg4);
        }
        
        holder.itemView.setOnClickListener(v -> listener.onShopClick(shop));
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView shopName, ownerName, ratingText;
        final RatingBar ratingBar;
        final ImageView serviceImg1, serviceImg2, serviceImg3, serviceImg4;
        final CircleImageView ownerPic;

        public ViewHolder(View view) {
            super(view);
            shopName = view.findViewById(R.id.Shop_Name);
            ownerName = view.findViewById(R.id.Shop_Owner_Name);
            ratingText = view.findViewById(R.id.Shop_Ratings);
            ratingBar = view.findViewById(R.id.ratingBar);
            serviceImg1 = view.findViewById(R.id.service_img1);
            serviceImg2 = view.findViewById(R.id.service_img2);
            serviceImg3 = view.findViewById(R.id.service_img3);
            serviceImg4 = view.findViewById(R.id.service_img4);
            ownerPic = view.findViewById(R.id.owner_image);
        }
    }
}
