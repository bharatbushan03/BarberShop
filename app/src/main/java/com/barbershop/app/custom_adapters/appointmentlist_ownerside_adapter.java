package com.barbershop.app.custom_adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.barbershop.app.R;
import com.barbershop.app.userdetails.appointment_in_userside;
import com.barbershop.app.userdetails.services;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class appointmentlist_ownerside_adapter extends RecyclerView.Adapter<appointmentlist_ownerside_adapter.ViewHolder> {

    private static ArrayList<appointment_in_userside> list;
    private Context context;


    public appointmentlist_ownerside_adapter(ArrayList<appointment_in_userside> listt, Context context){

        list=listt;
        Log.d("ASDAA", "sizeinserv_adap ="+list.size());
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(context)
                .inflate(R.layout.appointment_list_of_cust, parent, false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.getShopname().setText(list.get(position).getShopname());
        holder.getApmt_date().setText(list.get(position).getAppointment_date());
        Log.d("QWWQ","size="+list.get(position).getServices());
        holder.getServices().setText(list.get(position).getServices());
        holder.getSlot().setText(list.get(position).getSlot());
        holder.getAmount().setText(list.get(position).getAmount());


        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!list.get(holder.getAdapterPosition()).getStatus().equals("Pending")){
                        holder.getCompleted_icon().setVisibility(View.VISIBLE);
                    }else {
                        holder.getPendingicon().setVisibility(View.VISIBLE) ;
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
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
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView shopname,apmt_date,services,slot,amount,custname_textview;
        private Button cancel_appointment_btn,complete_appointment_btn;
        private ImageView completed_icon,pendingicon;

        public ViewHolder(View view) {
            super(view);
            shopname=view.findViewById(R.id.shopname_apmt_userside);
            custname_textview=view.findViewById(R.id.custname_textview);
            custname_textview.setText("Customer Name");
            custname_textview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_person_outline_24,0,0,0);
            apmt_date=view.findViewById(R.id.apmt_date_userside);
            services=view.findViewById(R.id.services_apmt_userside);
            slot=view.findViewById(R.id.slot_apmt_userside);
            amount=view.findViewById(R.id.amount_apmt_userside);
            cancel_appointment_btn=view.findViewById(R.id.cancel_appointment_btn);
            completed_icon=view.findViewById(R.id.check_iconimg_appointment_cardview);
            complete_appointment_btn=view.findViewById(R.id.done_appointment_btn);
            pendingicon=view.findViewById(R.id.pending_iconimg_appointment_cardview);
            complete_appointment_btn.setVisibility(View.GONE);
           cancel_appointment_btn.setVisibility(View.GONE);


            // cancel_appointment_btn.setOnClickListener(this);

        }

        public ImageView getPendingicon() {
            return pendingicon;
        }

        public ImageView getCompleted_icon() {
            return completed_icon;
        }

        public Button getComplete_appointment_btn() {
            return complete_appointment_btn;
        }

        public Button getcancelBtn(){
            return cancel_appointment_btn;
        }
        public TextView getShopname() {
            return shopname;
        }
        public TextView getApmt_date() {
            return apmt_date;
        }
        public TextView getServices() {
            return services;
        }
        public TextView getSlot() {
            return slot;
        }
        public TextView getAmount() {
            return amount;
        }

    }
}
