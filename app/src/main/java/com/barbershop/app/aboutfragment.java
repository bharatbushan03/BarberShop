package com.barbershop.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.barbershop.app.databinding.FragmentAboutfragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link aboutfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class aboutfragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ImageView mapimageview;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentAboutfragmentBinding fragmentAboutfragmentBinding;
    String Shop_id;

    public aboutfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment aboutfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static aboutfragment newInstance(String param1, String param2) {
        aboutfragment fragment = new aboutfragment();
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
        Shop_id=getArguments().getString("Shop_id");
        Log.d("AWAWAW","id="+Shop_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       fragmentAboutfragmentBinding=FragmentAboutfragmentBinding.inflate(inflater,container,false);
     //  View view=inflater.inflate(R.layout.fragment_aboutfragment, container, false);;
        FragmentTransaction fragmentTransaction=getParentFragmentManager().beginTransaction();
        MapsFragment mf1=new MapsFragment();
        Bundle b=new Bundle();
        Log.d("AWAWAW","id="+Shop_id);
        b.putString("Shop_id",Shop_id);
        mf1.setArguments(b);
        fragmentTransaction.replace(R.id.map_fragment_container_aboutfrag,mf1);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        return fragmentAboutfragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase.getInstance().getReference("Shops").child(Shop_id).child("shop_details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fragmentAboutfragmentBinding.ownerNameAboutFragment.setText(fragmentAboutfragmentBinding.ownerNameAboutFragment.getText().toString().replace("Kay", snapshot.child("owner_name").getValue(String.class)));
                fragmentAboutfragmentBinding.aboutOwnerAboutFrag.setText(snapshot.child("about_owner").getValue(String.class));
                fragmentAboutfragmentBinding.shopNameAboutFrag.setText(snapshot.child("shop_name").getValue(String.class));
                fragmentAboutfragmentBinding.shopAddressAboutFrag.setText(snapshot.child("shop_address").getValue(String.class));
                if (snapshot.child("schedule").exists()) {
                Log.d("RRRR", snapshot.child("schedule").child("sunday").getValue(String.class));
                fragmentAboutfragmentBinding.sundayTimeAboutFrag.setText(snapshot.child("schedule").child("sunday").getValue(String.class).equals("Open") ? (snapshot.child("schedule").child("opening_time").getValue(String.class) + "-" + snapshot.child("schedule").child("closing_time").getValue(String.class)) : "Closed");
                fragmentAboutfragmentBinding.mondayTimingAboutFrag.setText(snapshot.child("schedule").child("monday").getValue(String.class).equals("Open") ? (snapshot.child("schedule").child("opening_time").getValue(String.class) + "-" + snapshot.child("schedule").child("closing_time").getValue(String.class)) : "Closed");
                fragmentAboutfragmentBinding.tuesdayTimingAboutFrag.setText(snapshot.child("schedule").child("tuesday").getValue(String.class).equals("Open") ? (snapshot.child("schedule").child("opening_time").getValue(String.class) + "-" + snapshot.child("schedule").child("closing_time").getValue(String.class)) : "Closed");
                fragmentAboutfragmentBinding.wednesdayTimingAboutFrag.setText(snapshot.child("schedule").child("wednesday").getValue(String.class).equals("Open") ? (snapshot.child("schedule").child("opening_time").getValue(String.class) + "-" + snapshot.child("schedule").child("closing_time").getValue(String.class)) : "Closed");
                fragmentAboutfragmentBinding.thursdayTimingAboutFrag.setText(snapshot.child("schedule").child("thursday").getValue(String.class).equals("Open") ? (snapshot.child("schedule").child("opening_time").getValue(String.class) + "-" + snapshot.child("schedule").child("closing_time").getValue(String.class)) : "Closed");
                fragmentAboutfragmentBinding.fridayTimingAboutFrag.setText(snapshot.child("schedule").child("friday").getValue(String.class).equals("Open") ? (snapshot.child("schedule").child("opening_time").getValue(String.class) + "-" + snapshot.child("schedule").child("closing_time").getValue(String.class)) : "Closed");
                fragmentAboutfragmentBinding.saturdayTimingAboutFrag.setText(snapshot.child("schedule").child("saturday").getValue(String.class).equals("Open") ? (snapshot.child("schedule").child("opening_time").getValue(String.class) + "-" + snapshot.child("schedule").child("closing_time").getValue(String.class)) : "Closed");

            }
                if (snapshot.child("cancellation_Policy").exists()){
                    fragmentAboutfragmentBinding.cancellationPolicyInfoAboutFrag.setText(snapshot.child("cancellation_Policy").exists() ? (snapshot.child("cancellation_Policy").getValue(String.class)) : "This is my Cancellationnnnn");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fragmentAboutfragmentBinding.callAboutFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] permissions={ Manifest.permission.CALL_PHONE};
                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),permissions,1234);
                }

                FirebaseDatabase.getInstance().getReference("Shops").child(Shop_id).child("shop_details").child("shop_mobile_no").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + snapshot.getValue(String.class)));
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        fragmentAboutfragmentBinding.mailAboutFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Shops").child(Shop_id).child("shop_details").child("shop_mail").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{snapshot.getValue(String.class)});
                        i.putExtra(Intent.EXTRA_SUBJECT, "Query regarding shop");
                        i.putExtra(Intent.EXTRA_TEXT   , "Welcome Sir!");
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}