package com.barbershop.app;

import android.app.AlertDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.barbershop.app.custom_adapters.shop_imgs_adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link showing_listofservices_for_shop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class showing_listofservices_for_shop extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String shop_id;

    public showing_listofservices_for_shop() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment showing_listofservices_for_shop.
     */
    // TODO: Rename and change types and number of parameters
    public static showing_listofservices_for_shop newInstance(String param1, String param2) {
        showing_listofservices_for_shop fragment = new showing_listofservices_for_shop();
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

        shop_id=getArguments().getString("ID");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TextView shop_mail,owner_name,shopname;
        ImageView service_img1,service_img2,service_img3,owner_img;
        //this textviews are of tabs
        TextView service,reviews,about,filltab;
        ImageButton empty_heart,fill_heart,seeall_images_btn_selectedshop,cancelbtn_showshopimg_alertbox,browse_ownerimg_btn_selectedshop;
        AlertDialog.Builder alertdialogBuilder2;
        View show_shop_nd_services_pics;
        AlertDialog showImagesbox;
        RecyclerView shop_images_list_in_alertbox,shop_services_list_in_alertbox;
        Button add_shop_service_image_btn_alertbox,add_shop_images_btn,book_appointment_btn;
        ImageView owner_profile_pic;
        View view;
        view=inflater.inflate(R.layout.fragment_showing_listofservices_for_shop, container, false);
        service=view.findViewById(R.id.service_tab);
        reviews=view.findViewById(R.id.reviews_tab);
        about=view.findViewById(R.id.about_tab);
        RatingBar r=view.findViewById(R.id.ratingBarr);
        TextView ratings=view.findViewById(R.id.Shop_Ratingss);
        filltab=view.findViewById(R.id.filltab);
        empty_heart=view.findViewById(R.id.empty_heart);
        fill_heart=view.findViewById(R.id.fill_heart);
        shop_mail=view.findViewById(R.id.shop_mail_selectedshop);
        owner_name=view.findViewById(R.id.owner_name_selectedshop);
        service_img1=view.findViewById(R.id.profile_image1_selectedshop);
        service_img2=view.findViewById(R.id.profile_image2_selectedshop);
        service_img3=view.findViewById(R.id.profile_image3_selectedshop);
        owner_img=view.findViewById(R.id.owner_image_selectedshop);
        shopname=view.findViewById(R.id.shop_name_of_selected_shop);
        seeall_images_btn_selectedshop=view.findViewById(R.id.seeall_images_btn_selectedshop);


        ImageButton backbtn=view.findViewById(R.id.back_btn_mainscreen_for_selected_shop);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        show_shop_nd_services_pics = layoutInflater.inflate(R.layout.add_shopimages_alertbox, null);
        alertdialogBuilder2 = new AlertDialog.Builder(
                getContext());
        alertdialogBuilder2
                .setCancelable(false);
        alertdialogBuilder2.setView(show_shop_nd_services_pics);
        showImagesbox=alertdialogBuilder2.create();

        shop_services_list_in_alertbox=show_shop_nd_services_pics.findViewById(R.id.shopservicesview_recycler);
        shop_images_list_in_alertbox=show_shop_nd_services_pics.findViewById(R.id.shopimagesview_recycler);
        add_shop_service_image_btn_alertbox=show_shop_nd_services_pics.findViewById(R.id.add_shop_service_img_button);
        add_shop_images_btn=show_shop_nd_services_pics.findViewById(R.id.add_shopimg_button);
        cancelbtn_showshopimg_alertbox=show_shop_nd_services_pics.findViewById(R.id.addimg_alertboc_cancelbtn);
        browse_ownerimg_btn_selectedshop=show_shop_nd_services_pics.findViewById(R.id.browseimg_add_images_alertbox);
        owner_profile_pic=show_shop_nd_services_pics.findViewById(R.id.owner_profile_image);
        cancelbtn_showshopimg_alertbox.setVisibility(View.VISIBLE);
        owner_profile_pic.setVisibility(View.GONE);
        browse_ownerimg_btn_selectedshop.setVisibility(View.GONE);
        add_shop_images_btn.setText("Shop Images");
        add_shop_service_image_btn_alertbox.setText("Services Images");

        seeall_images_btn_selectedshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagesbox.show();
            }
        });
        cancelbtn_showshopimg_alertbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagesbox.dismiss();
            }
        });

        ArrayList<Uri> services_images=new ArrayList<>();
        ArrayList<Uri> shop_images=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Shops").child(shop_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snapshot1=snapshot.child("Images").child("Shop_Servces_Images");
                services_images.clear();
                shop_images.clear();
                for (DataSnapshot serviceimage_snapshot: snapshot1.getChildren()
                ) {
                   services_images.add(Uri.parse(serviceimage_snapshot.getValue(String.class)));
                }

                DataSnapshot snapshot3=snapshot.child("Images").child("Shop_images");
                for (DataSnapshot shopimage_snapshot: snapshot3.getChildren()
                ) {
                    shop_images.add(Uri.parse(shopimage_snapshot.getValue(String.class)));
                }

                shop_imgs_adapter ad1=new shop_imgs_adapter(shop_images,getContext());
                shop_imgs_adapter ad2=new shop_imgs_adapter(services_images,getContext());
                shop_services_list_in_alertbox.setAdapter(ad1);
                shop_services_list_in_alertbox.setLayoutManager(new GridLayoutManager(getContext(),2));
                shop_images_list_in_alertbox.setAdapter(ad2);
                shop_images_list_in_alertbox.setLayoutManager(new GridLayoutManager(getContext(),2));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        empty_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                empty_heart.setVisibility(View.GONE);
                fill_heart.setVisibility(View.VISIBLE);
            }
        });
        fill_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                empty_heart.setVisibility(View.VISIBLE);
                fill_heart.setVisibility(View.GONE);
            }
        });
        filltab.animate().x(0).setDuration(100);
        filltab.setText("Services");
        filltab.setTextColor(Color.parseColor("#FFFFFF"));

        FragmentTransaction fragmentTransaction=getParentFragmentManager().beginTransaction();
        Serviceslist_of_selected_shop f1=new Serviceslist_of_selected_shop();
        Bundle b=new Bundle();
        b.putString("ID",""+shop_id);
        f1.setArguments(b);
        fragmentTransaction.replace(R.id.fragment_container_for_service_review_about,f1).commit();







        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filltab.setWidth(view.getWidth());
                filltab.setHeight(view.getHeight());
                filltab.animate().x(0).setDuration(100);
                filltab.setVisibility(View.VISIBLE);
                filltab.setText("Services");
                filltab.setTextColor(Color.parseColor("#FFFFFF"));

                FragmentTransaction fragmentTransaction=getParentFragmentManager().beginTransaction();
                Serviceslist_of_selected_shop f1=new Serviceslist_of_selected_shop();
                Bundle b=new Bundle();
                b.putString("ID",""+shop_id);
                f1.setArguments(b);
                fragmentTransaction.replace(R.id.fragment_container_for_service_review_about,f1).commit();

            }
        });
        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filltab.setWidth(view.getWidth());
                filltab.setHeight(view.getHeight());
                filltab.animate().x(view.getWidth()).setDuration(100);
                filltab.setVisibility(View.VISIBLE);
                filltab.setText("Reviews");
                filltab.setTextColor(Color.parseColor("#FFFFFF"));

                FragmentTransaction fragmentTransaction=getParentFragmentManager().beginTransaction();
                reviews_of_selected_shop_userside f1=new reviews_of_selected_shop_userside();
                Bundle b=new Bundle();
                b.putString("shop_id",""+shop_id);
                f1.setArguments(b);
                // fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragment_container_for_service_review_about,f1).commit();
                Toast.makeText(getContext(),"view changedd",Toast.LENGTH_SHORT).show();
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filltab.setWidth(view.getWidth());
                filltab.setHeight(view.getHeight());
                filltab.animate().x(view.getWidth()*2).setDuration(100);
                filltab.setVisibility(View.VISIBLE);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        filltab.setText("About");
                    }
                }, 90);

                filltab.setTextColor(Color.parseColor("#FFFFFF"));


                FragmentTransaction fragmentTransaction=getParentFragmentManager().beginTransaction();
               // fragmentTransaction.addToBackStack(null);
                aboutfragment ab1=new aboutfragment();
                Bundle b=new Bundle();
                b.putString("Shop_id",shop_id);
                ab1.setArguments(b);
                fragmentTransaction.replace(R.id.fragment_container_for_service_review_about,ab1);
                fragmentTransaction.commit();
            }
        });

        FirebaseDatabase.getInstance().getReference("Shops").child(shop_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snapshot1=snapshot.child("Images").child("Shop_Servces_Images");
               int only_first_3=1;
                for (DataSnapshot serviceimage: snapshot1.getChildren()
                     ) {
                    String img=serviceimage.getValue(String.class);
                    if(only_first_3==1){
                        Picasso.get().load(img).into(service_img1);
                    }
                    if(only_first_3==2){
                        Picasso.get().load(img).into(service_img2);
                    }
                    if(only_first_3==3){
                        Picasso.get().load(img).into(service_img3);
                    }
                    if(only_first_3>3){
                        break;
                    }
                    only_first_3++;
                }

                DataSnapshot snapshot2=snapshot.child("shop_details");
                String s_name=snapshot2.child("shop_name").getValue(String.class);
                String o_name=snapshot2.child("owner_name").getValue(String.class);
                String s_mail=snapshot2.child("shop_mail").getValue(String.class);
                String o_img=snapshot2.child("shop_profile_pic").getValue(String.class);

                Long no_of_ratings = 0l;
                Float rating_sum = 0f;
                for (DataSnapshot datasnapshot2:snapshot.child("Reviews").getChildren()
                     ) {
                    no_of_ratings=snapshot.child("Reviews").getChildrenCount();
                    if(datasnapshot2.child("ratings").getValue(Float.class)!=null)
                    rating_sum=rating_sum+datasnapshot2.child("ratings").getValue(Float.class);
                }
                Float rating_avg=rating_sum/no_of_ratings;
                r.setRating(rating_avg);
                ratings.setText(rating_avg+"("+no_of_ratings+")");
                shopname.setText(s_name);
                owner_name.setText(o_name);
                shop_mail.setText(s_mail);
                Picasso.get().load(o_img).into(owner_img);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        // Inflate the layout for this fragment
        return view;
    }
}
