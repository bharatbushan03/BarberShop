package com.barbershop.app.custom_adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbershop.app.R;
import com.barbershop.app.userdetails.services;

import java.util.ArrayList;

public class serviceslist_of_selected_shop_adapter extends RecyclerView.Adapter<serviceslist_of_selected_shop_adapter.ViewHolder> {

    private final ArrayList<services> list;
    public final ArrayList<String> a2 = new ArrayList<>();
    private final LayoutInflater layoutInflater;


    public serviceslist_of_selected_shop_adapter(ArrayList<services> listt, Context context){

        list=listt;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.service_view_of_selectedshop, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       services service = list.get(position);
       String serviceName = service.getService_name();
       holder.getservicename().setText(serviceName);
       holder.getPrice_and_duration().setText(service.getService_price()+"$ and up to "+service.getService_duration()+"mins ");
       holder.getExpandable_description().setText(service.getService_description());

        holder.getService_checkbox().setOnCheckedChangeListener(null);
        holder.getService_checkbox().setChecked(a2.contains(serviceName));
        holder.getService_checkbox().setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked){
                if (!a2.contains(serviceName)) {
                    a2.add(serviceName);
                }
            }
            else {
                a2.remove(serviceName);
            }

            Log.d("CGHE","sizeee="+a2.size()+"added="+serviceName);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder  {
        private TextView servicename,price_and_duration,expandable_description;
        private CheckBox service_checkbox;

        public ViewHolder(View view) {
            super(view);
            servicename=view.findViewById(R.id.servicename_of_selected_shop);
            expandable_description=view.findViewById(R.id.expand_description_selected_shop);
            price_and_duration=view.findViewById(R.id.price_duration_of_selectedshop);
            service_checkbox=view.findViewById(R.id.service_checkbox);
//            expandable_description.setOnClickListener(this);
        }

        public TextView getservicename() {
            return servicename;
        }

        public TextView getPrice_and_duration() {
            return price_and_duration;
        }

        public TextView getExpandable_description() {
            return expandable_description;
        }

        public CheckBox getService_checkbox() {
            return service_checkbox;
        }
    }
}
