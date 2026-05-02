package com.barbershop.app;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.OpenableColumns;
import android.util.Log;
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


import com.barbershop.app.custom_adapters.reviews_list_selectedshop_adapter;
import com.barbershop.app.userdetails.reviewdetail_class;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link reviews_of_selected_shop_userside#newInstance} factory method to
 * create an instance of this fragment.
 */
public class  reviews_of_selected_shop_userside extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog Feedback_box;
    View give_feedback_alertbox;
    TextInputEditText feedbak_inputtext;
    ImageButton cancel;
    Button submit,giveFeedbackBtn;
    RecyclerView rv;
    ImageButton img1,img2,img3,img4;
    int whichis_clicked=0;
    RatingBar ratingBar;
    String shop_id;


    public reviews_of_selected_shop_userside() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment reviews_of_selected_shop_userside.
     */
    // TODO: Rename and change types and number of parameters
    public static reviews_of_selected_shop_userside newInstance(String param1, String param2) {
        reviews_of_selected_shop_userside fragment = new reviews_of_selected_shop_userside();
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
        shop_id=getArguments().getString("shop_id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view=inflater.inflate(R.layout.fragment_reviews_of_selected_shop_userside, container, false);
        LayoutInflater li;
        li = LayoutInflater.from(getContext());
         give_feedback_alertbox = li.inflate(R.layout.give_feedback_alertbox, null);
         alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder
                .setCancelable(false);
        alertDialogBuilder.setView(give_feedback_alertbox);
       Feedback_box= alertDialogBuilder.create();

       cancel=give_feedback_alertbox.findViewById(R.id.cancel_btn_feedbackalertboc);
       submit=give_feedback_alertbox.findViewById(R.id.submit_feedback_btn);
       feedbak_inputtext=give_feedback_alertbox.findViewById(R.id.feedbak_inputtext);
       img1=give_feedback_alertbox.findViewById(R.id.feedback_img1);
        img2=give_feedback_alertbox.findViewById(R.id.feedback_img2);
        img3=give_feedback_alertbox.findViewById(R.id.feedback_img3);
        img4=give_feedback_alertbox.findViewById(R.id.feedback_img4);
        ratingBar=give_feedback_alertbox.findViewById(R.id.give_ratings);




        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        giveFeedbackBtn=view.findViewById(R.id.give_feedback_btn);
        rv=view.findViewById(R.id.reviews_list_of_selected_shop);





        FirebaseDatabase.getInstance().getReference("Shops").child(shop_id).child("Reviews").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<reviewdetail_class> reviews_objcts_list = new ArrayList<>();

                for (DataSnapshot datasnapshot1: snapshot.getChildren()
                ) {

                String cust_name = datasnapshot1.child("cust_name").getValue(String.class);
                Float ratings = datasnapshot1.child("ratings").getValue(Float.class);


                    Log.d("RAtings",ratings+"");
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


                reviewdetail_class obj = new reviewdetail_class(cust_name, img1, img2, img3, img4, feedbacktext, ratings);
                reviews_objcts_list.add(obj);
            }
                 reviews_list_selectedshop_adapter ad=new reviews_list_selectedshop_adapter(reviews_objcts_list,getContext());
                rv.setAdapter(ad);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        giveFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Feedback_box.show();
            }
        });
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
                startActivityForResult(intent,123);
                whichis_clicked=1;
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
                startActivityForResult(intent,123);
                whichis_clicked=2;
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
                startActivityForResult(intent,123);
                whichis_clicked=3;
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
                startActivityForResult(intent,123);
                whichis_clicked=4;
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("RRRRRR","Shops/"+shop_id+"/Reviews/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/ratings");
                FirebaseDatabase.getInstance().getReference("Shops").child(shop_id).child("Reviews").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ratings").setValue(ratingBar.getRating());
                FirebaseDatabase.getInstance().getReference("Shops").child(shop_id).child("Reviews").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("feedback").setValue(feedbak_inputtext.getText().toString());
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user_name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FirebaseDatabase.getInstance().getReference("Shops").child(shop_id).child("Reviews").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("cust_name").setValue(snapshot.getValue(String.class));
                        Toast.makeText(getContext(),"Your Feedback has been Submitted Successfully!!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Feedback_box.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Feedback_box.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123){
            if(resultCode==RESULT_OK && data != null && data.getData() != null && getActivity() != null) {
                try (Cursor returnCursor = getActivity().getContentResolver().query(data.getData(), null, null, null, null)) {
                    if (returnCursor == null) {
                        Toast.makeText(getActivity(), "Unable to read selected image.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    if (!returnCursor.moveToFirst()) {
                        Toast.makeText(getActivity(), "Unable to read selected image.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Long Image_size = (returnCursor.getLong(sizeIndex) / 1000);
                    Log.d("TAGp", "Name:" + returnCursor.getString(nameIndex));
                    Log.d("TAGp", "Size: " + Image_size);
                    if(Image_size<=500){
                    ProgressDialog progressDialog=new ProgressDialog(getContext());
                        progressDialog.setTitle("Uploading Image...");
                        progressDialog.setMessage("Take a Sip..");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        StorageReference Shop_Image_folder = FirebaseStorage.getInstance().getReference().child("shop_reviews_images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        Shop_Image_folder.child(data.getData().toString().replace("content://com.android.providers.media.documents/document/", "")).putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Shop_Image_folder.child(data.getData().toString().replace("content://com.android.providers.media.documents/document/", "")).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String img_uri = String.valueOf(uri);

                                        if(whichis_clicked==1) {
                                            Picasso.get().load(img_uri).into(img1);
                                        }
                                        if(whichis_clicked==2)
                                            Picasso.get().load(img_uri).into(img2);
                                        if(whichis_clicked==3)
                                            Picasso.get().load(img_uri).into(img3);
                                        if(whichis_clicked==4)
                                            Picasso.get().load(img_uri).into(img4);

                                        FirebaseDatabase.getInstance().getReference("Shops").child(shop_id).child("Reviews").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("images").child(data.getData().toString().replace("content://com.android.providers.media.documents/document/", "")).setValue(img_uri);

                                        Toast.makeText(getActivity(), "Image uploaded successfully !!", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                        //    update_shop_image_list();
                                    }
                                });
                            }
                        });
                    }
                    else {
                        Toast.makeText(getContext(),"Image Size Must be Less than 500Kb!!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
