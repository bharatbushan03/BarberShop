package com.barbershop.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.barbershop.app.custom_adapters.reviews_list_selectedshop_adapter;
import com.barbershop.app.userdetails.reviewdetail_class;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ownerfragmentreviews#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ownerfragmentreviews extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Float avg_ratings=0f;
    Long no_of_reviews_int=0l;
    Long one_rating_given_customers_count=0l,two_rating_given_customers_count=0l,three_rating_given_customers_count=0l,four_rating_given_customers_count=0l,five_rating_given_customers_count=0l;


    public Ownerfragmentreviews() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ownerfragmentreviews.
     */
    // TODO: Rename and change types and number of parameters
    public static Ownerfragmentreviews newInstance(String param1, String param2) {
        Ownerfragmentreviews fragment = new Ownerfragmentreviews();
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
        View view=inflater.inflate(R.layout.fragment_ownerfragmentreviews, container, false);

        RecyclerView rv=view.findViewById(R.id.reviews_list_of_shop);

        TextView avg_ratingtext,no_of_reviews,reviews_of_5_inpercent,reviews_of_4_inpercent,reviews_of_3_inpercent,reviews_of_2_inpercent,reviews_of_1_inpercent;
        ProgressBar progressBar_1_star,progressBar_2_star,progressBar_3_star,progressBar_4_star,progressBar_5_star;

        avg_ratingtext=view.findViewById(R.id.avg_ratingtext);
        no_of_reviews=view.findViewById(R.id.no_of_reviews);
        reviews_of_1_inpercent=view.findViewById(R.id.reviews_of_1_inpercent);
        reviews_of_2_inpercent=view.findViewById(R.id.reviews_of_2_inpercent);
        reviews_of_3_inpercent=view.findViewById(R.id.reviews_of_3_inpercent);
        reviews_of_4_inpercent=view.findViewById(R.id.reviews_of_4_inpercent);
        reviews_of_5_inpercent=view.findViewById(R.id.reviews_of_5_inpercent);
        progressBar_1_star=view.findViewById(R.id.progressBar_1_star);
        progressBar_2_star=view.findViewById(R.id.progressBar_2_star);
        progressBar_3_star=view.findViewById(R.id.progressBar_3_star);
        progressBar_4_star=view.findViewById(R.id.progressBar_4_star);
        progressBar_5_star=view.findViewById(R.id.progressBar_5_star);

        FirebaseDatabase.getInstance().getReference("Shops").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isAdded()) {
                    return;
                }

                avg_ratings=0f;
                no_of_reviews_int=snapshot.getChildrenCount();
                one_rating_given_customers_count=0l;
                two_rating_given_customers_count=0l;
                three_rating_given_customers_count=0l;
                four_rating_given_customers_count=0l;
                five_rating_given_customers_count=0l;
                ArrayList<reviewdetail_class> reviews_objcts_list = new ArrayList<>();

                for (DataSnapshot datasnapshot1: snapshot.getChildren()
                ) {
                    String cust_name = datasnapshot1.child("cust_name").getValue(String.class);
                    Float ratings = datasnapshot1.child("ratings").getValue(Float.class);
                    Float safeRating = ratings != null ? ratings : 0f;
                    avg_ratings=avg_ratings+safeRating;

                    if(ratings!=null){
                        long rounded = Math.round(ratings);
                        if(rounded==1){
                            one_rating_given_customers_count++;
                        }

                        if(rounded==2){
                            two_rating_given_customers_count++;
                        }

                        if(rounded==3){
                            three_rating_given_customers_count++;
                        }

                        if(rounded==4){
                            four_rating_given_customers_count++;
                        }

                        if(rounded==5){
                            five_rating_given_customers_count++;
                        }}

                    String feedbacktext = datasnapshot1.child("feedback").getValue(String.class);
                    String img1 = "", img2 = "", img3 = "", img4 = "";
                    int i = 1;
                    for (DataSnapshot datasnapshot : datasnapshot1.child("images").getChildren()
                    ) {
                        if (i == 1) {
                            img1 = datasnapshot.getValue(String.class);
                        }
                        if (i == 2) {
                            img2 = datasnapshot.getValue(String.class);
                        }
                        if (i == 3) {
                            img3 = datasnapshot.getValue(String.class);
                        }
                        if (i == 4) {
                            img4 = datasnapshot.getValue(String.class);
                        }
                        i++;
                    }
                    reviewdetail_class obj = new reviewdetail_class(cust_name, img1, img2, img3, img4, feedbacktext, safeRating);
                    reviews_objcts_list.add(obj);
                }
                if(no_of_reviews_int==0){
                    avg_ratingtext.setText("0");
                    no_of_reviews.setText("(0 Reviews)");
                    progressBar_1_star.setProgress(0);
                    progressBar_2_star.setProgress(0);
                    progressBar_3_star.setProgress(0);
                    progressBar_4_star.setProgress(0);
                    progressBar_5_star.setProgress(0);
                    reviews_of_1_inpercent.setText("0%");
                    reviews_of_2_inpercent.setText("0%");
                    reviews_of_3_inpercent.setText("0%");
                    reviews_of_4_inpercent.setText("0%");
                    reviews_of_5_inpercent.setText("0%");

                    reviews_list_selectedshop_adapter ad=new reviews_list_selectedshop_adapter(reviews_objcts_list,getContext());
                    rv.setAdapter(ad);
                    rv.setLayoutManager(new LinearLayoutManager(getContext()));
                    return;
                }

                avg_ratings=avg_ratings/no_of_reviews_int;
                avg_ratingtext.setText(avg_ratings+"");
                no_of_reviews.setText("("+no_of_reviews_int+" Reviews)");
                one_rating_given_customers_count=(one_rating_given_customers_count*100/no_of_reviews_int);
                two_rating_given_customers_count=(two_rating_given_customers_count*100/no_of_reviews_int);
                three_rating_given_customers_count=(three_rating_given_customers_count*100/no_of_reviews_int);
                four_rating_given_customers_count=(four_rating_given_customers_count*100/no_of_reviews_int);
                five_rating_given_customers_count=(five_rating_given_customers_count*100/no_of_reviews_int);
                progressBar_1_star.setProgress(one_rating_given_customers_count.intValue());
                reviews_of_1_inpercent.setText(one_rating_given_customers_count+"%");
                progressBar_2_star.setProgress(two_rating_given_customers_count.intValue());
                reviews_of_2_inpercent.setText(two_rating_given_customers_count+"%");
                progressBar_3_star.setProgress(three_rating_given_customers_count.intValue());
                reviews_of_3_inpercent.setText(three_rating_given_customers_count+"%");
                progressBar_4_star.setProgress(four_rating_given_customers_count.intValue());
                reviews_of_4_inpercent.setText(four_rating_given_customers_count+"%");
                progressBar_5_star.setProgress(five_rating_given_customers_count.intValue());
                reviews_of_5_inpercent.setText(five_rating_given_customers_count+"%");


                reviews_list_selectedshop_adapter ad=new reviews_list_selectedshop_adapter(reviews_objcts_list,getContext());
                rv.setAdapter(ad);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }
}
