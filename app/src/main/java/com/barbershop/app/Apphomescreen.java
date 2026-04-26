package com.barbershop.app;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.barbershop.app.custom_adapters.RecyclerItemClickListener;
import com.barbershop.app.custom_adapters.shop_list_adapter;
import com.barbershop.app.userdetails.Shop;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Apphomescreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Apphomescreen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    FirebaseDatabase database;
    FirebaseAuth mAuth;
    RecyclerView shop_list_recyclerview;
    public static ProgressDialog Loading_box;
    ArrayList<Shop> list=new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Apphomescreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Apphomescreen.
     */
    // TODO: Rename and change types and number of parameters
    public static Apphomescreen newInstance(String param1, String param2) {
        Apphomescreen fragment = new Apphomescreen();
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

        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_apphomescreen, container, false);

        ImageButton g_map_search=view.findViewById(R.id.searchby_location);
        ImageButton backbtn=view.findViewById(R.id.back_btn_apphomescreen);




        g_map_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(getContext(),MapsActivity.class);
                i1.putExtra("UserOrOwner","User");
                startActivity(i1);
            }
        });

        shop_list_recyclerview=view.findViewById(R.id.shop_list);

        Loading_box=new ProgressDialog(getContext());
        Loading_box.setTitle("Loading Shops");
        Loading_box.setMessage("Wait A Moment");
        Loading_box.show();



        database.getReference("Shops").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Shop shop = postSnapshot.child("shop_details").getValue(Shop.class);
                    Log.d("shubham",shop.getShop_name());
                    list.add(shop);
                }

                shop_list_adapter adapter=new shop_list_adapter(list,getContext());
                shop_list_recyclerview.setAdapter(adapter);
                shop_list_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        shop_list_recyclerview.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), shop_list_recyclerview, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String mailid_shop=list.get(position).getShop_mail();

                        FirebaseDatabase.getInstance().getReference("Shops").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot datasnapshot: snapshot.getChildren()
                                     ) {
                                        if(datasnapshot.child("shop_details").child("shop_mail").getValue(String.class).equals(mailid_shop)){
                                            if (!isAdded()) return;
                                            FragmentTransaction fragmentTransaction=getParentFragmentManager().beginTransaction();
                                            showing_listofservices_for_shop f1=new showing_listofservices_for_shop();
                                            Bundle b=new Bundle();
                                            Log.d("ASAAa",datasnapshot.getKey());
                                            b.putString("ID",""+datasnapshot.getKey());
                                            f1.setArguments(b);
                                            g_map_search.setVisibility(View.GONE);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.replace(R.id.fm,f1).commit();
                                            break;
                                        }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );


        return  view;
    }


}
