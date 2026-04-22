package com.barbershop.app.custom_adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbershop.app.R;
import com.barbershop.app.userdetails.Shop;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class shop_imgs_adapter extends RecyclerView.Adapter<shop_imgs_adapter.ViewHolder> {

    private ArrayList<Uri> list;
    private Context context;

    public shop_imgs_adapter(ArrayList<Uri> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.shop_image_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("TRTR","on bind view holder : List size="+list.size()+" Pos="+position +"link ="+list.get(position));
        Picasso.get().load(list.get(position)).into(holder.getShop_image());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView shop_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shop_image=itemView.findViewById(R.id.shopimageview);

        }

        public ImageView getShop_image() {
            return shop_image;
        }
    }
}
