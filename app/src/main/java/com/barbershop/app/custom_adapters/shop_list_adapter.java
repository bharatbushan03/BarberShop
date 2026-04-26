package com.barbershop.app.custom_adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.barbershop.app.Apphomescreen;
import com.barbershop.app.custHomeActivity;
import com.barbershop.app.custom_adapters.shop_list_adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.barbershop.app.R;
import com.barbershop.app.userdetails.Shop;
import com.barbershop.app.userdetails.services;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.Provider;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class shop_list_adapter extends RecyclerView.Adapter<shop_list_adapter.ViewHolder> {

    private ArrayList<Shop> list;
    private Context context;

    public shop_list_adapter(ArrayList<Shop> listt, Context context){
        list=listt;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.shop_card_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("ppl",list.get(position).getShop_name()+" pic="+list.get(position).getShop_profile_pic());
        holder.getShop_name().setText(""+list.get(position).getShop_name()+"");
        holder.getOwner_name().setText(""+list.get(position).getOwner_name());
        Picasso.get().load(list.get(position).getShop_profile_pic()).into(holder.owner_pic);
        //child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Images").child("Shop_Servces_Images").
        FirebaseDatabase.getInstance().getReference("Shops").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot: snapshot.getChildren()) {

                    if(datasnapshot.child("shop_details").child("shop_mail").getValue(String.class).equals(list.get(holder.getAdapterPosition()).getShop_mail())){


                        Log.d("OOOID","="+datasnapshot.getKey());
                        Long no_of_ratings = 0l;
                        Float rating_sum = 0f;
                        no_of_ratings=datasnapshot.child("Reviews").getChildrenCount();
                        for (DataSnapshot datasnapshot2:datasnapshot.child("Reviews").getChildren()
                        ) {
                            if(datasnapshot2.child("ratings").getValue(Float.class)!=null)
                                rating_sum=rating_sum+datasnapshot2.child("ratings").getValue(Float.class);
                        }
                        Float rating_avg=rating_sum/no_of_ratings;
                        holder.getR().setRating(rating_avg);
                        holder.getRatings().setText(rating_avg+"("+no_of_ratings+")");






                        int stop=0;
                        for (DataSnapshot datasnapshot1 :datasnapshot.child("Images/Shop_Servces_Images").getChildren()) {
                            String service_img_uri = datasnapshot1.getValue(String.class);
                            stop++;
                            if (stop == 1)
                                Picasso.get().load(service_img_uri).into(holder.getService_img1());
                            if (stop == 2)
                                Picasso.get().load(service_img_uri).into(holder.getService_img2());
                            if (stop == 3)
                                Picasso.get().load(service_img_uri).into(holder.getService_img3());
                            if (stop == 4)
                                Picasso.get().load(service_img_uri).into(holder.getService_img4());
                            if (stop > 4)
                                break;
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayList<services> arrayList=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Shops").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot: snapshot.getChildren()) {
                    if (datasnapshot.child("shop_details").child("shop_mail").getValue(String.class).equals(list.get(position).getShop_mail())) {
                        for (DataSnapshot datasnapshot1 : datasnapshot.child("services").getChildren()) {

                            //services services getValue(com.barbershop.app.userdetails.services.class);
                            services s = datasnapshot1.getValue(services.class);
                            Log.d("ASDAA", "name=" + s.getService_name());
                            arrayList.add(s);
                        }
                        Log.d("ASDAA",arrayList.size()+"===size");
                        servicelist_adapter_inshopcard_view servicelist_adapter_inshopcard_view = new servicelist_adapter_inshopcard_view(arrayList, Apphomescreen.Loading_box.getContext());
                        holder.getRv().setAdapter(servicelist_adapter_inshopcard_view);
                        holder.getRv().setLayoutManager(new LinearLayoutManager(Apphomescreen.Loading_box.getContext(),LinearLayoutManager.HORIZONTAL,false));

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




       // Picasso.get().load(postSnapshot.getValue(String.class)).into(profileimg);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Apphomescreen.Loading_box.dismiss();
            }
        }, 1000);   //5 seconds


        // holder.getShop_image().setImageURI(Uri.parse(list.get(position).getShop_profile_pic()));
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

        private TextView shop_name,owner_name,ratings;
        private RatingBar r;
        private ImageView service_img1,service_img2,service_img3,service_img4;
        private CircleImageView owner_pic;
        private RecyclerView rv;

        public ViewHolder(View view) {
            super(view);

            shop_name=view.findViewById(R.id.Shop_Name);
            owner_name=view.findViewById(R.id.Shop_Owner_Name);
            ratings=view.findViewById(R.id.Shop_Ratings);
            r=view.findViewById(R.id.ratingBar);
            service_img1=view.findViewById(R.id.service_img1);
            service_img2=view.findViewById(R.id.service_img2);
            service_img3=view.findViewById(R.id.service_img3);
            service_img4=view.findViewById(R.id.service_img4);
            rv=view.findViewById(R.id.rv_for_serviceslist_in_shopcard_view);
            owner_pic=view.findViewById(R.id.owner_image);

            // Define click listener for the ViewHolder's View

            // textView = (TextView) view.findViewById(R.id.textView);
        }

        public TextView getShop_name() {
            return shop_name;
        }

        public TextView getOwner_name() {
            return owner_name;
        }

        public TextView getRatings() {
            return ratings;
        }

        public RatingBar getR() {
            return r;
        }

        public RecyclerView getRv() {
            return rv;
        }

        public ImageView getService_img1() {
            return service_img1;
        }

        public ImageView getService_img2() {
            return service_img2;
        }

        public ImageView getService_img3() {
            return service_img3;
        }

        public ImageView getService_img4() {
            return service_img4;
        }

        public CircleImageView getOwner_pic() {
            return owner_pic;
        }
    }
}
