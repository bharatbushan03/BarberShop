package com.barbershop.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yourname.barbershop.custom_adapters.RecyclerItemClickListener;
import com.yourname.barbershop.custom_adapters.appointmentlist_userside_adapter;
import com.yourname.barbershop.userdetails.appointment_in_userside;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Appointmentscreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Appointmentscreen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    ArrayList<appointment_in_userside> all_appointments_info_of_user;
    private String mParam2;

    public Appointmentscreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Appointmentscreen.
     */
    // TODO: Rename and change types and number of parameters
    public static Appointmentscreen newInstance(String param1, String param2) {
        Appointmentscreen fragment = new Appointmentscreen();
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
        View view=inflater.inflate(R.layout.fragment_appointmentscreen, container, false);
        RecyclerView rv=view.findViewById(R.id.appointment_taken_byuser_recyclerview);

        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("appointments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String shopname;
                String apt_date="",slot="",amount="",status="pending";

                all_appointments_info_of_user=new ArrayList<>();
                for (DataSnapshot datasnapshot: snapshot.getChildren()
                     ) {
                    ArrayList<String> services = new ArrayList<>();
                        shopname=datasnapshot.child("shop_name").getValue(String.class);
                    for (DataSnapshot datasnapshot1: datasnapshot.child("appointment_dates").getChildren()
                         ) {
                        apt_date=datasnapshot1.getKey();
                        slot=datasnapshot1.child("slot").getValue(String.class);
                        amount=datasnapshot1.child("total_amount").getValue(Long.class)+"";
                        status=datasnapshot1.child("status").getValue(String.class)+"";


                        services.clear();
                            String s="";
                        for (DataSnapshot datasnapshot2:datasnapshot1.child("services").getChildren()
                             ) {
                            s=s+datasnapshot2.getValue(String.class).substring(0,datasnapshot2.getValue(String.class).indexOf('-')).trim()+"\n";
                            services.add(datasnapshot2.getValue(String.class));
                            Log.d("QWWQ",datasnapshot1.child("services").getChildrenCount()+""+"s==="+datasnapshot2.getValue(String.class));
                        }

                        appointment_in_userside full_apmt_info=new appointment_in_userside(shopname,apt_date,s,slot,amount,status);
                        all_appointments_info_of_user.add(full_apmt_info);
                    }
                }

                appointmentlist_userside_adapter ad=new appointmentlist_userside_adapter(all_appointments_info_of_user,getContext());
                rv.setAdapter(ad);
                rv.setLayoutManager(new GridLayoutManager(getContext(),2));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        rv.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv , new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//              //  if(view.getId()==R.id.cancel_appointment_btn)
//                 //   Toast.makeText(getContext(),all_appointments_info_of_user.get(position).getAppointment_date()+"id=="+view.getId(),Toast.LENGTH_SHORT).show();
//
//                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("appointments").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot datasnapshot: snapshot.getChildren()
//                        ) {
//                            if(datasnapshot.child("shop_name").getValue(String.class).equals(all_appointments_info_of_user.get(position).getShopname())){
//                                Log.d("CHHH","Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+datasnapshot.getKey()+"/appointment_dates/"+all_appointments_info_of_user.get(position).getAppointment_date());
//                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(datasnapshot.getKey()).child("appointment_dates").child(all_appointments_info_of_user.get(position).getAppointment_date()).removeValue(new DatabaseReference.CompletionListener() {
//                                    @Override
//                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                                        if(error!=null){
//                                            Log.d("Firebase", "Error deleting node"+ error.getMessage());
//                                        }else {
//                                            FirebaseDatabase.getInstance().getReference("Shops").child("appointments").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(all_appointments_info_of_user.get(position).getAppointment_date()).removeValue(new DatabaseReference.CompletionListener() {
//                                                @Override
//                                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                                                    if(error!=null){
//                                                        Log.d("EEERRR",""+error.toString());
//                                                    }else {
//                                                        Toast.makeText(view.getContext(), "Your Appointment With "+all_appointments_info_of_user.get(position).getShopname()+" has been cancelled!!",Toast.LENGTH_SHORT).show();
//                                                    }
//
//                                                }
//                                            });
//                                        }
//                                        //  Toast.makeText(view.getContext(), "Your Appointment With "+all_appointments_info_of_user.get(position).getShopname()+" has been cancelled!!",Toast.LENGTH_SHORT).show();
//
//
//                                    }
//                                });
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        throw error.toException();
//                    }
//                });
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));

        return view;
    }
}