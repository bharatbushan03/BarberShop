package com.barbershop.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.barbershop.app.custom_adapters.RecyclerItemClickListener;
import com.barbershop.app.custom_adapters.services_c_adapter;
import com.barbershop.app.userdetails.Shop;
import com.barbershop.app.userdetails.services;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ownerfragmentservices#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ownerfragmentservices extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageButton add_servicebtn;
    View view;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
   static LinearLayout blurr_background;

   ProgressDialog progressDialog;

   AlertDialog.Builder alertDialogBuilder,alertdialogBuilder2,alertDialogBuilder1;
   AlertDialog alertDialog,alertDialog1,alertDialog2;
   View edit_service,add_service;

   Button    addServicebtn;

   ImageButton cancelAddservicelayout;
    TextInputLayout service_name,service_price,service_description,service_duration,add_serviceName,add_servicePrice,add_serviceDescription,add_serviceDuration;

    Button Update_sevice;
    ImageButton cancel_updateservicelayout;

    public Ownerfragmentservices() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ownerfragmentservices.
     */
    // TODO: Rename and change types and number of parameters
    public static Ownerfragmentservices newInstance(String param1, String param2) {
        Ownerfragmentservices fragment = new Ownerfragmentservices();
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


        progressDialog =new ProgressDialog(getContext());
        progressDialog.setTitle("Loading Services...");
        progressDialog.setMessage("wait a Moment");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_ownerfragmentservices, container, false);

        add_servicebtn= view.findViewById(R.id.add_servicebtn);
        recyclerView=view.findViewById(R.id.listof_services);
        blurr_background=view.findViewById(R.id.burr_background);

        LayoutInflater li = LayoutInflater.from(getContext());
        add_service = li.inflate(R.layout.fragment_add_service, null);
        alertdialogBuilder2 = new AlertDialog.Builder(
                getContext());
        alertdialogBuilder2
                .setCancelable(false);
        alertdialogBuilder2.setView(add_service);
        alertDialog2=alertdialogBuilder2.create();
        cancelAddservicelayout=add_service.findViewById(R.id.cancel_addservicelayout);
        addServicebtn=add_service.findViewById(R.id.add_servicebtn);
        add_serviceName=add_service.findViewById(R.id.add_service_name);
        add_servicePrice=add_service.findViewById(R.id.add_service_price);
        add_serviceDescription=add_service.findViewById(R.id.add_service_duration);
        add_serviceDuration=add_service.findViewById(R.id.add_service_description);




        cancelAddservicelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               alertDialog2.dismiss();
            }
        });

        addServicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                services service=new services(add_serviceName.getEditText().getText().toString(),add_servicePrice.getEditText().getText().toString(),add_serviceDuration.getEditText().getText().toString(),add_serviceDescription.getEditText().getText().toString());
              //  Log.d("TAGGG",serviceName.getEditText().getText().toString());

                if(!add_serviceName.getEditText().getText().toString().equals("") && !add_servicePrice.getEditText().getText().toString().equals("") && !add_serviceDescription.getEditText().getText().toString().equals("") && !add_serviceDuration.getEditText().getText().toString().equals("")) {
                    database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("services").child(add_serviceName.getEditText().getText().toString()).setValue(service);
                  //  FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    if (Build.VERSION.SDK_INT >= 26) {
//                        ft.setReorderingAllowed(false);
//                    }
//                    ft.detach(new Ownerfragmentservices()).attach(new Ownerfragmentservices()).commit();
                    Toast.makeText(getContext(), "Service has been added To your Shop :)", Toast.LENGTH_LONG).show();

                    alertDialog2.dismiss();
                }else{
                    Toast.makeText(getContext(), "Enter All Fields", Toast.LENGTH_LONG).show();

                }
            }
        });

        add_servicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                add_service add_service=new add_service();
//                FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
//
//                transaction.addToBackStack(null);
//                transaction.replace(R.id.services_container,add_service);



                alertDialog2.show();


                //transaction.commit();
            }
        });

        ArrayList<services> list=new ArrayList<>();

        database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("services").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    services service = postSnapshot.getValue(services.class);
                   // Log.d("Fuck",service.getService_name());
                    list.add(service);
//                    Log.d("Fuck sizeinfrag",""+list.size());
                }
                services_c_adapter adapter=new services_c_adapter(list,getContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                progressDialog.dismiss();


//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    public void run() {
//                        // yourMethod();
//
//                    }
//                }, 0);   //5 seconds
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        li = LayoutInflater.from(getContext());
        edit_service = li.inflate(R.layout.edit_service, null);
        alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder
                .setCancelable(false);
        alertDialogBuilder.setView(edit_service);

        service_name=edit_service.findViewById(R.id.service_name);
        service_price=edit_service.findViewById(R.id.service_price);
        service_description=edit_service.findViewById(R.id.service_description);
        service_duration=edit_service.findViewById(R.id.service_duration);
        Update_sevice=edit_service.findViewById(R.id.update_servicebtn);
        cancel_updateservicelayout=edit_service.findViewById(R.id.cancel_updateservicelayout);

        alertDialog = alertDialogBuilder.create();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {

                    @Override public void onItemClick(View view, int position) {

                        alertDialog.show();

                        database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("services").child(list.get(position).getService_name()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                services service=snapshot.getValue(com.barbershop.app.userdetails.services.class);
                                service_name.getEditText().setText(service.getService_name());
                                service_name.setEnabled(false);
                                service_price.getEditText().setText(service.getService_price());
                                service_duration.getEditText().setText(service.getService_duration());
                                service_description.getEditText().setText(service.getService_description());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        alertDialog.show();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        alertDialogBuilder1 = new AlertDialog.Builder(getContext())
                                .setTitle("Delete Service")
                                .setMessage("Are you sure you want to Delete "+list.get(position).getService_name()+" Service ? Press Ok if yes ")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("services").child(list.get(position).getService_name()).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                Toast.makeText(getContext(),"Service Deleted :(",Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        alertDialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert);
                        alertDialog1=alertDialogBuilder1.create();
                        alertDialog1.show();
                    }
                })
        );

        Update_sevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("services").child(service_name.getEditText().getText().toString()).child("service_price").setValue(service_price.getEditText().getText().toString());
                database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("services").child(service_name.getEditText().getText().toString()).child("service_description").setValue(service_description.getEditText().getText().toString());
                database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("services").child(service_name.getEditText().getText().toString()).child("service_duration").setValue(service_duration.getEditText().getText().toString());

                alertDialog.dismiss();

            }
        });


        cancel_updateservicelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });



        return view;
    }
}
