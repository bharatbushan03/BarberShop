package com.barbershop.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barbershop.app.custom_adapters.appointmentlist_ownerside_adapter;
import com.barbershop.app.custom_adapters.appointmentlist_userside_adapter;
import com.barbershop.app.userdetails.appointment_in_userside;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ownerfragmentappointments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ownerfragmentappointments extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Ownerfragmentappointments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ownerfragmentappointments.
     */
    // TODO: Rename and change types and number of parameters
    public static Ownerfragmentappointments newInstance(String param1, String param2) {
        Ownerfragmentappointments fragment = new Ownerfragmentappointments();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_ownerfragmentappointments, container, false);

        RecyclerView appointmentlist_shop_recyclerview=view.findViewById(R.id.shop_appointments_recyclerview);


        FirebaseDatabase.getInstance().getReference("Shops").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("appointments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String custname;
                String apt_date="",slot="",amount="",status="pending";
                Log.d("QQQ","1");

                ArrayList<appointment_in_userside> all_appointments_info_of_user=new ArrayList<>();

                for (DataSnapshot datasnapshot: snapshot.getChildren()
                ) {
                    custname=datasnapshot.child("customer_name").getValue(String.class);
                    for (DataSnapshot datasnapshot1: datasnapshot.getChildren()
                    ) {
                        apt_date=datasnapshot1.getKey();
                        slot=datasnapshot1.child("slot").getValue(String.class);
                        amount=datasnapshot1.child("Total_amount").getValue(String.class)+"";
                        status=datasnapshot1.child("status").getValue(String.class)+"";

                        String s="";
                        for (DataSnapshot datasnapshot2:datasnapshot1.child("selected_services").getChildren()
                        ) {
                            s=s+datasnapshot2.getValue(String.class).substring(0,datasnapshot2.getValue(String.class).indexOf('-')).trim()+"\n";
                            Log.d("QWWQ",datasnapshot1.child("services").getChildrenCount()+""+"s==="+datasnapshot2.getValue(String.class));
                        }

                        appointment_in_userside full_apmt_info=new appointment_in_userside(custname,apt_date,s,slot,amount,status);
                        all_appointments_info_of_user.add(full_apmt_info);
                    }
                }

                appointmentlist_ownerside_adapter ad=new appointmentlist_ownerside_adapter(all_appointments_info_of_user,getContext());
                appointmentlist_shop_recyclerview.setAdapter(ad);
                appointmentlist_shop_recyclerview.setLayoutManager(new GridLayoutManager(getContext(),2));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        return view;
    }
}
