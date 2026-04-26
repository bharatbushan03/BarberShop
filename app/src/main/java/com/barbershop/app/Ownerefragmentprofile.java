package com.barbershop.app;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.OpenableColumns;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.barbershop.app.custom_adapters.RecyclerItemClickListener;
import com.barbershop.app.custom_adapters.shop_imgs_adapter;
import com.barbershop.app.userdetails.Shop;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ownerefragmentprofile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ownerefragmentprofile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int opening_time_hour_as_int,opening_time_min_as_int,closing_time_hour_as_int,closing_time_min_as_int,no_of_slots;

    AlertDialog alertDialog,alertDialog2;
    AlertDialog.Builder alertDialogBuilder;


    private static final int pick_image=1;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage Storage;
    StorageReference reference;

    Button logoutbtn,deletebtn,re_authsendmailbtn,add_shop_service_image_btn_alertbox
            ,select_open_timing_btn,select_closed_timing_btn,add_shop_images_btn
            ,update_business_ours_btn,update_cancellation_policy;
    ImageView profileimg,profileimg2,profileimg3;
    private Activity activity;
    EditText editshopname,editownername,editabout,editaddress,editmobile,editmail,reath_password,reauth_mail,cancellation_policy_text;
    ImageButton browsebtn,editbutton,savebutton,re_authcancelbtn,cancelbtn_addshopimg_alertbox,seeallimages_imagebtn
            ,down_arrow_for_updatebusiness_Hours;
    ActivityResultLauncher<String> launcher;
    ProgressDialog progressDialog;
    ProgressBar Loadimg;
    TextView changepassword,add_location,opentiming_textview,closedtiming_textview,add_cancellation_policy_textview;
    View re_authbox,add_shop_pics_alertbox;
    AlertDialog.Builder alertDialogbuilder,alertdialogBuilder2,  alertDialogBuilder1;;
    AlertDialog resetmailbox,delete_slot_warning;
    RadioButton monday_open,monday_close,sunday_open,sunday_close,tuesday_open,tuesday_close,wednesday_open,wednesday_close,thursday_open,thursday_close,friday_open,friday_close,saturday_open,saturday_close;
    RecyclerView shop_services_list_in_alertbox,shop_images_list_in_alertbox;
    CircleImageView owner_profile_pic;
    String selected_btn_in_alertbox;
    String ref;
    String slot_time="zzz";


    shop_imgs_adapter adapter,adapter2;
    static int i,j;
    private Uri Image_Uri;
    ArrayList<Uri> Shop_images_uri,Shop_services_uri;
    //this is for changing shops email and password

    String mail;

    public Ownerefragmentprofile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ownerefragmentprofile.
     */
    // TODO: Rename and change types and number of parameters
    public static Ownerefragmentprofile newInstance(String param1, String param2) {
        Ownerefragmentprofile fragment = new Ownerefragmentprofile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Getting Info...");
        progressDialog.setMessage("Take a Sip..");
        progressDialog.setCancelable(false);
        progressDialog.show();


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


      mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_ownerefragmentprofile, container, false);

        LayoutInflater li = LayoutInflater.from(getContext());
        re_authbox = li.inflate(R.layout.re_authbox, null);
        alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder
                .setCancelable(false);
        alertDialogBuilder.setView(re_authbox);

        alertDialog = alertDialogBuilder.create();

        re_authcancelbtn = (ImageButton) re_authbox.findViewById(R.id.reauth_cancelbtn);
        re_authsendmailbtn = (Button) re_authbox.findViewById(R.id.reauth_sendmailbtn);
        reath_password = (EditText) re_authbox.findViewById(R.id.reauth_password);
        reauth_mail = (EditText) re_authbox.findViewById(R.id.reauth_mail);


        logoutbtn= (Button) view.findViewById(R.id.logoutbtn);
       deletebtn= (Button) view.findViewById(R.id.deletebtnn);

       add_location=view.findViewById(R.id.add_loc_to_map);

       add_location.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
               if(available== ConnectionResult.SUCCESS){
                   Log.d("map::","is ok working");
                   Intent i1=new Intent(getContext(),MapsActivity.class);
                   i1.putExtra("UserOrOwner","Owner");
                   startActivity(i1);
               }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
                   Log.d("map","error occured but fixable");
                   Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(getActivity(),available, 9001);
                dialog.show();
               }else {
                   Toast.makeText(getContext(),"You cant make map req",Toast.LENGTH_LONG).show();

               }
           }
       });
        editbutton= (ImageButton) view.findViewById(R.id.editbutton);
        savebutton=(ImageButton)view.findViewById(R.id.savebtn);

        editshopname=(EditText) view.findViewById(R.id.editname);

        editownername=(EditText) view.findViewById(R.id.editownername);
        editmail=(EditText) view.findViewById(R.id.editmail);
        editabout=(EditText)view.findViewById(R.id.about_owner_inprofilefrag);

        seeallimages_imagebtn=view.findViewById(R.id.seeall_images_btn);

        editaddress=(EditText) view.findViewById(R.id.editaddress);
        editmobile=(EditText) view.findViewById(R.id.editmobile);
       // editpassword=(EditText) view.findViewById(R.id.editpassword);
        profileimg=(ImageView)view.findViewById(R.id.profile_image1);
        profileimg2=(ImageView)view.findViewById(R.id.profile_image2);
        profileimg3=(ImageView)view.findViewById(R.id.profile_image3);

        LinearLayout businesshours_visible;
        //businesshours views
        businesshours_visible=view.findViewById(R.id.visibility_for_businesshours);
            down_arrow_for_updatebusiness_Hours=view.findViewById(R.id.down_arrow_businesshours_ownerprofile_frag);
            select_open_timing_btn=view.findViewById(R.id.select_open_timeing_businessHours_proffrag);
            select_closed_timing_btn=view.findViewById(R.id.select_close_timeing_businessHours_proffrag);
            opentiming_textview=view.findViewById(R.id.opentime_textview);
            closedtiming_textview=view.findViewById(R.id.closetime_textview);
            monday_open=view.findViewById(R.id.monday_open_radio_btn);
            monday_close=view.findViewById(R.id.monday_clodes_radio_btn);
            sunday_open=view.findViewById(R.id.sunday_open_radio_btn);
            sunday_close=view.findViewById(R.id.sunday_closed_radio_btn);
        tuesday_open=view.findViewById(R.id.tuesday_open_radio_btn);
        tuesday_close=view.findViewById(R.id.tuesday_closed_radio_btn);
        wednesday_open=view.findViewById(R.id.wednesday_open_radio_btn);
        wednesday_close=view.findViewById(R.id.wednesday_closed_radio_btn);
        thursday_open=view.findViewById(R.id.thursday_open_radio_btn);
        thursday_close=view.findViewById(R.id.thursday_closed_radio_btn);
        friday_open=view.findViewById(R.id.friday_open_radio_btn);
        friday_close=view.findViewById(R.id.friday_closed_radio_btn);
        saturday_open=view.findViewById(R.id.saturday_open_radio_btn);
        saturday_close=view.findViewById(R.id.saturday_closed_radio_btn);
        update_business_ours_btn=view.findViewById(R.id.update_businesshours_btn);

        MaterialCardView businesshours_cardview=view.findViewById(R.id.businesshours_card_view);

       Button add_slot=view.findViewById(R.id.add_slots_btn);
        ListView slots_list=view.findViewById(R.id.slots_list);
        Button open_time_slot=view.findViewById(R.id.open_time_slot_btn);
        Button close_time_slot=view.findViewById(R.id.close_time_slot_btn);
        TextView open_time_slottextview=view.findViewById(R.id.show_slot_opentime);
        TextView close_time_slottextview=view.findViewById(R.id.show_slot_closetime);
        ArrayList<String> slots_list_for_adapter=new ArrayList<>();

        slots_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                slots_list.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        slots_list.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        slots_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                alertDialogBuilder1 = new AlertDialog.Builder(getContext())
                        .setTitle("Delete Slot")
                        .setMessage("Are you sure you want to Delete "+slots_list_for_adapter.get(i)+" Slot? Press Ok if yes ")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("slots_for_booking").child(slots_list_for_adapter.get(i)).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        Toast.makeText(getContext(),"Slot Deleted :(",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delete_slot_warning.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert);
                delete_slot_warning=alertDialogBuilder1.create();
                delete_slot_warning.show();
            }

        });
        FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ot=snapshot.child("schedule").child("opening_time").getValue(String.class).replace("AM","");
                ot=ot.replace("PM","");
               String ot1=ot.substring(0,ot.indexOf(':'));
               String ot2=ot.substring(ot.indexOf(':')+1,ot.length()-1);
               Log.d("OTTT","ot=hour="+ot1+"min="+ot2);
               opening_time_hour_as_int=Integer.parseInt(ot1);
                opening_time_min_as_int=Integer.parseInt(ot2);

                String ct=snapshot.child("schedule").child("closing_time").getValue(String.class).replace("AM","");
                ct=ct.replace("PM","");
                String ct1=ct.substring(0,ct.indexOf(':'));
                String ct2=ct.substring(ct.indexOf(':')+1,ct.length()-1);
                Log.d("OTTT","ct=hour="+ct1+"min="+ct2);
                closing_time_hour_as_int=Integer.parseInt(ct1);
                closing_time_min_as_int=Integer.parseInt(ct2);


                //opening_time_as_int=Integer.parseInt();
                //closing_time_as_int=Integer.parseInt(snapshot.child("schedule").child("closing_time").getValue(String.class));

                if(snapshot.child("slots_for_booking").exists()){
                    Log.d("slotlist","database size="+snapshot.child("slots_for_booking").getChildrenCount());
                    no_of_slots= (int) snapshot.child("slots_for_booking").getChildrenCount();

                    slots_list_for_adapter.clear();

                    for (DataSnapshot datasnapshot:snapshot.child("slots_for_booking").getChildren()
                         ) {
                        slots_list_for_adapter.add(datasnapshot.getValue(String.class));
                    }
                    ArrayAdapter<String> ad3=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,slots_list_for_adapter);
                    slots_list.setAdapter(ad3);
                }else {
                    slots_list_for_adapter.clear();
                    ArrayAdapter<String> ad3=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,slots_list_for_adapter);
                    slots_list.setAdapter(ad3);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        add_slot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if(!slots_list_for_adapter.contains(slot_time))
               // {
                   // slots_list_for_adapter.add(slot_time);
                open_time_slottextview.setText("00:00");
                close_time_slottextview.setText("00:00");
                    Log.d("slotlist","size="+slots_list_for_adapter.size());
                    if(!slot_time.equals("") && slot_time.contains("-") && slot_time.length()>9) {
                        if(no_of_slots<6){
                            FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("slots_for_booking").child(slot_time).setValue(slot_time);
                        }else {
                            Toast.makeText(getContext(), "you can Make maximum Upto 6 slots", Toast.LENGTH_LONG).show();
                        }

                    }//}
//                ArrayAdapter<String> ad3=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,slots_list_for_adapter);
//                slots_list.setAdapter(ad3);
                 slot_time="";

            }
        });

        open_time_slot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar start_range=Calendar.getInstance();
                start_range.set(Calendar.HOUR_OF_DAY,opening_time_hour_as_int);
                start_range.set(Calendar.MINUTE,opening_time_min_as_int);
                int y1=start_range.get(Calendar.HOUR_OF_DAY);
                Log.d("Y!!!!","="+y1);
                int d1=start_range.get(Calendar.MINUTE);

                final Calendar end_range=Calendar.getInstance();
                end_range.set(Calendar.HOUR_OF_DAY,closing_time_hour_as_int);
                end_range.set(Calendar.MINUTE,closing_time_min_as_int);
                int y2=end_range.get(Calendar.HOUR_OF_DAY);
                Log.d("Y!!!!ee","="+y2);
                int d2=end_range.get(Calendar.MINUTE);

                final Calendar c=Calendar.getInstance();
                int y=c.get(Calendar.HOUR_OF_DAY);
                int d=c.get(Calendar.MINUTE);

                TimePickerDialog td=new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        final Calendar c3=Calendar.getInstance();
                        c3.set(Calendar.MINUTE,i1);
                        c3.set(Calendar.HOUR_OF_DAY,i);


                        if(c3.getTimeInMillis()>start_range.getTimeInMillis() && c3.getTimeInMillis()< end_range.getTimeInMillis()){
                        String am_pm = (i < 12) ? "AM" : "PM";

                        if(slot_time.equals("zzz")){
                            slot_time="";
                        }

                        if(slot_time.contains("-")){
                            slot_time=i+":"+i1+am_pm+slot_time;
                        }else {
                            slot_time=i+":"+i1+am_pm+slot_time;
                        }
                        open_time_slottextview.setText( i+":"+i1+am_pm);}
                        else {
                            Toast.makeText(getContext(),"enter slot range by considering your shop opening time!!",Toast.LENGTH_SHORT).show();
                        }


                    }
                },y,d,false);



                td.show();
            }
        });
        close_time_slot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar start_range=Calendar.getInstance();
                start_range.set(Calendar.HOUR_OF_DAY,opening_time_hour_as_int);
                start_range.set(Calendar.MINUTE,opening_time_min_as_int);
                int y1=start_range.get(Calendar.HOUR_OF_DAY);
                Log.d("Y!!!!","="+y1);
                int d1=start_range.get(Calendar.MINUTE);

                final Calendar end_range=Calendar.getInstance();
                end_range.set(Calendar.HOUR_OF_DAY,closing_time_hour_as_int);
                end_range.set(Calendar.MINUTE,closing_time_min_as_int);
                int y2=end_range.get(Calendar.HOUR_OF_DAY);
                Log.d("Y!!!!ee","="+y2);
                int d2=end_range.get(Calendar.MINUTE);

                final Calendar c=Calendar.getInstance();
                int y=c.get(Calendar.HOUR_OF_DAY);
                int d=c.get(Calendar.MINUTE);

                TimePickerDialog td=new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        final Calendar c3=Calendar.getInstance();
                        c3.set(Calendar.MINUTE,i1);
                        c3.set(Calendar.HOUR_OF_DAY,i);


                        if(c3.getTimeInMillis()>start_range.getTimeInMillis() && c3.getTimeInMillis()< end_range.getTimeInMillis()){
                        String am_pm = (i < 12) ? "AM" : "PM";
                        if(slot_time.equals("zzz")){
                            slot_time="";
                        }
                        slot_time=slot_time+"-"+ i+":"+i1+am_pm;
                        close_time_slottextview.setText( i+":"+i1+am_pm);}else {
                            Toast.makeText(getContext(),"enter slot range by considering your shop opening time!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                },y,d,false);
                td.show();
            }
        });

        select_open_timing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c=Calendar.getInstance();
                int y=c.get(Calendar.HOUR_OF_DAY);
                int d=c.get(Calendar.MINUTE);
                TimePickerDialog td=new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String am_pm = (i < 12) ? "AM" : "PM";
                       FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("schedule").child("opening_time").setValue(""+i+":"+i1+" "+am_pm);
                        opentiming_textview.setText(i+":"+i1+am_pm);
                    }
                },y,d,false);
                  td.show();
            }
        });
        select_closed_timing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c=Calendar.getInstance();
                int y=c.get(Calendar.HOUR_OF_DAY);
                int d=c.get(Calendar.MINUTE);
                TimePickerDialog td=new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String am_pm = (i < 12) ? "AM" : "PM";
                        FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("schedule").child("closing_time").setValue(""+i+":"+i1+" "+am_pm);
                        closedtiming_textview.setText(i+":"+i1+am_pm);
                    }
                },y,d,false);
                td.show();
            }
        });

        FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("schedule").exists()){
                    for (DataSnapshot datasnapshot:snapshot.child("schedule").getChildren()
                         ) {
                        if(!datasnapshot.getKey().equals("opening_time") && !datasnapshot.getKey().equals("closing_time")){

                            if(datasnapshot.getKey().equals("sunday")){
                               if(datasnapshot.getValue(String.class).equals("Open"))
                                sunday_open.setChecked(true);
                            }
                            if(datasnapshot.getKey().equals("monday")){
                                if(datasnapshot.getValue(String.class).equals("Open"))
                                monday_open.setChecked(true);
                            }
                            if(datasnapshot.getKey().equals("tuesday")){
                                if(datasnapshot.getValue(String.class).equals("Open"))
                                tuesday_open.setChecked(true);
                            }
                            if(datasnapshot.getKey().equals("wednesday")){
                                if(datasnapshot.getValue(String.class).equals("Open"))
                                wednesday_open.setChecked(true);
                            }
                            if(datasnapshot.getKey().equals("thursday")){
                                if(datasnapshot.getValue(String.class).equals("Open"))
                                    thursday_open.setChecked(true);
                            }
                            if(datasnapshot.getKey().equals("friday")){
                                if(datasnapshot.getValue(String.class).equals("Open"))
                                friday_open.setChecked(true);
                            }
                            if(datasnapshot.getKey().equals("saturday")){
                                if(datasnapshot.getValue(String.class).equals("Open"))
                                saturday_open.setChecked(true);
                            }
                        }else {
                            Log.d("RRRR",datasnapshot.getKey());
                            if(datasnapshot.getKey().equals("opening_time")){
                                opentiming_textview.setText(datasnapshot.getValue(String.class));
                            }else {

                                closedtiming_textview.setText(datasnapshot.getValue(String.class));
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       update_business_ours_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("schedule").child("sunday").setValue(sunday_open.isChecked()?"Open":"Closed");
               FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("schedule").child("monday").setValue(monday_open.isChecked()?"Open":"Closed");
               FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("schedule").child("tuesday").setValue(tuesday_open.isChecked()?"Open":"Closed");
               FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("schedule").child("thursday").setValue(thursday_open.isChecked()?"Open":"Closed");
               FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("schedule").child("wednesday").setValue(wednesday_open.isChecked()?"Open":"Closed");
               FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("schedule").child("friday").setValue(friday_open.isChecked()?"Open":"Closed");
               FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("schedule").child("saturday").setValue(saturday_open.isChecked()?"Open":"Closed");
               Toast.makeText(getContext(), "Data Updated Successfully!!", Toast.LENGTH_SHORT).show();
           }
       });

        down_arrow_for_updatebusiness_Hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(businesshours_visible.getVisibility()==View.VISIBLE){

                    businesshours_visible.setVisibility(View.GONE);

                    down_arrow_for_updatebusiness_Hours.setImageResource(R.drawable.down_arrow);


                }else {
                    TransitionManager.beginDelayedTransition(businesshours_cardview,new AutoTransition());
                    businesshours_visible.setVisibility(View.VISIBLE);
                    down_arrow_for_updatebusiness_Hours.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                }
            }
        });
        //businesshours views


        // profileimg.setBackgroundTintList(ColorStateList.valueOf(Color.RED));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        changepassword=(TextView) view.findViewById(R.id.changepassword_owner_profile);
        add_cancellation_policy_textview=view.findViewById(R.id.add_cancellation_policy);
        LinearLayout cancellation_policy_visibility=view.findViewById(R.id.cancellation_policy_visibility);
        cancellation_policy_visibility.setVisibility(View.GONE);
        cancellation_policy_text=view.findViewById(R.id.cancellation_policy_text);
        update_cancellation_policy=view.findViewById(R.id.update_cancellation_policy_btn);
        FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("cancellation_Policy").exists()){
                    cancellation_policy_text.setText(snapshot.child("cancellation_Policy").getValue(String.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        update_cancellation_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("cancellation_Policy").setValue(cancellation_policy_text.getText().toString()+"");
                cancellation_policy_visibility.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Cancellation Policy Has been Successfully Updated!!", Toast.LENGTH_SHORT).show();
            }
        });

        add_cancellation_policy_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancellation_policy_visibility.setVisibility(View.VISIBLE);
                cancellation_policy_text.requestFocus();

            }
        });



Shop_images_uri=new ArrayList<>();
Shop_services_uri=new ArrayList<>();



        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        add_shop_pics_alertbox = layoutInflater.inflate(R.layout.add_shopimages_alertbox, null);
        alertdialogBuilder2 = new AlertDialog.Builder(
                getContext());
        alertdialogBuilder2
                .setCancelable(false);
        alertdialogBuilder2.setView(add_shop_pics_alertbox);
        alertDialog2=alertdialogBuilder2.create();






        seeallimages_imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.show();
            }
        });


        shop_services_list_in_alertbox=add_shop_pics_alertbox.findViewById(R.id.shopservicesview_recycler);
        shop_images_list_in_alertbox=add_shop_pics_alertbox.findViewById(R.id.shopimagesview_recycler);
        add_shop_service_image_btn_alertbox=add_shop_pics_alertbox.findViewById(R.id.add_shop_service_img_button);
        add_shop_images_btn=add_shop_pics_alertbox.findViewById(R.id.add_shopimg_button);
        cancelbtn_addshopimg_alertbox=add_shop_pics_alertbox.findViewById(R.id.addimg_alertboc_cancelbtn);
        browsebtn=add_shop_pics_alertbox.findViewById(R.id.browseimg_add_images_alertbox);
        Loadimg = (ProgressBar) add_shop_pics_alertbox.findViewById(R.id.Loadimg_add_images_alertbox);
        owner_profile_pic=add_shop_pics_alertbox.findViewById(R.id.owner_profile_image);
cancelbtn_addshopimg_alertbox.setVisibility(View.VISIBLE);

        browsebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
                startActivityForResult(intent,pick_image);
                selected_btn_in_alertbox="browse_owner_pic";
            }
        });
        add_shop_images_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
                startActivityForResult(intent,pick_image);
                selected_btn_in_alertbox="add_shop_images_btn";
            }
        });
        add_shop_service_image_btn_alertbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
                startActivityForResult(intent,pick_image);
                selected_btn_in_alertbox="add_shop_service_image_btn_alertbox";
            }
        });


        Task<DataSnapshot> ds= database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("shop_profile_pic").get();
       ds.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
           @Override
           public void onSuccess(DataSnapshot dataSnapshot) {
               String owner_pic=dataSnapshot.getValue(String.class);
               Picasso.get().load(owner_pic).into(owner_profile_pic);
           }
       });


        database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("Images").child("Shop_Servces_Images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int firstshowed3imges=1;
                Log.d("PPOO","7");
                Shop_services_uri.clear();
                i=(int)snapshot.getChildrenCount();
                Log.d("TPPP in data change","i="+i);
                if(i!=0) {

                    Log.d("PPOO","8");
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        if(i>=3) {
                            if (firstshowed3imges <= 3) {
                                if (firstshowed3imges == 1) {
                                    Picasso.get().load(postSnapshot.getValue(String.class)).into(profileimg);
                                }
                                if (firstshowed3imges == 2) {
                                    Picasso.get().load(postSnapshot.getValue(String.class)).into(profileimg2);
                                }
                                if (firstshowed3imges == 3) {
                                    Picasso.get().load(postSnapshot.getValue(String.class)).into(profileimg3);
                                }
                                firstshowed3imges++;
                            }
                        }
                        String img = postSnapshot.getValue(String.class);
                        Shop_services_uri.add(Uri.parse(img));
                        Log.d("PPOO","chedk="+Uri.parse(img));
                    }
                    Log.d("PPOO","10");
                    Log.d("TRTR", "imgs size in ondatachange =" + Shop_services_uri.size());

                }else{
                    Log.d("PPOO","11");
                    alertDialog2.show();

                }

                Log.d("PPOO","12");
                adapter = new shop_imgs_adapter(Shop_services_uri, getContext());
                shop_services_list_in_alertbox.setAdapter(adapter);
                shop_services_list_in_alertbox.setLayoutManager(new GridLayoutManager(getContext(),2));
                progressDialog.dismiss();
                Log.d("PPOO","13");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TRTR", "errrrorrrrrrrrrrrrrrrrrr");
            }
        });
        database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("Images").child("Shop_images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Shop_images_uri.clear();
                j=(int)snapshot.getChildrenCount();
                if(j!=0) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        String img = postSnapshot.getValue(String.class);
                        Shop_images_uri.add(Uri.parse(img));
                        Log.d("PPpp","9");
                    }
                    Log.d("PPpp","10");
                    Log.d("PPpp", "imgs size of shopimg in ondatachange =" + Shop_images_uri.size());

                }else{
                    Log.d("PPpp","11");
                    alertDialog2.show();

                }

                Log.d("PPpp","12");
                adapter2 = new shop_imgs_adapter(Shop_images_uri, getContext());
                shop_images_list_in_alertbox.setAdapter(adapter2);
                shop_images_list_in_alertbox.setLayoutManager(new GridLayoutManager(getContext(),2));
                progressDialog.dismiss();
                Log.d("PPpp","13");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TRTR", "errrrorrrrrrrrrrrrrrrrrr");
            }
        });
        cancelbtn_addshopimg_alertbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i<5||j<3){
                    Toast.makeText(getContext(),"Upload minimum 5 services images and 3 shop images",Toast.LENGTH_LONG).show();
                }else{
                    alertDialog2.dismiss();
                }

            }
        });

        shop_services_list_in_alertbox.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), shop_services_list_in_alertbox, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(),"Image selected i= "+position,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongItemClick(View view, int position) {

                alertDialogBuilder=new AlertDialog.Builder(getContext()).setTitle("Delete Image")
                        .setMessage("Are You sure you want to delete this Image ? ")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Log.d("TPPP","i="+position);


                                FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("Images").child("Shop_Servces_Images").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                                            Log.d("PPpp","childsnapforservices="+childSnapshot.getValue(String.class));
                                            if(childSnapshot.getValue(String.class).equals(Shop_services_uri.get(position).toString())){
                                                ref=childSnapshot.getKey();
                                                Log.d("PPpp","ref==="+ref+"\nlistval[pos]=");
                                            }
                                        }
                                        Log.d("TPPP","REFFF=="+ref);
                           FirebaseStorage.getInstance().getReference().child("Shopimages").child(mAuth.getCurrentUser().getUid()).child(ref).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void unused) {
                                   FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("Images").child("Shop_Servces_Images").child(ref).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void unused) {
                                           Toast.makeText(getContext(),"Image deleted successfully",Toast.LENGTH_LONG).show();
                                       }
                                   });
                               }
                           })     ;
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog=alertDialogBuilder.create();
                alertDialog.show();
            }
        }));
        shop_images_list_in_alertbox.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), shop_images_list_in_alertbox, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(),"Image selected i= "+position,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongItemClick(View view, int position) {

                alertDialogBuilder=new AlertDialog.Builder(getContext()).setTitle("Delete Image")
                        .setMessage("Are You sure you want to delete this Image ? ")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Log.d("TPPP","i="+position);


                                FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("Images").child("Shop_images").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                                            Log.d("PPpp","childsnapforservices="+childSnapshot.getValue(String.class));
                                            if(childSnapshot.getValue(String.class).equals(Shop_images_uri.get(position).toString())){
                                                ref=childSnapshot.getKey();
                                                Log.d("PPpp","ref==="+ref+"\nlistvalofshopimag[pos]=");
                                            }
                                        }
                                        Log.d("TPPP","REFFF=="+ref);
                                        FirebaseStorage.getInstance().getReference().child("Shopimages").child(mAuth.getCurrentUser().getUid()).child(ref).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("Images").child("Shop_images").child(ref).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getContext(),"Image deleted successfully",Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        })     ;
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog=alertDialogBuilder.create();
                alertDialog.show();
            }
        }));


        database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Shop ownerdetail = snapshot.getValue(Shop.class);
                Log.d("aayege","111111");
                assert ownerdetail != null;
                editshopname.setText(ownerdetail.getShop_name());
                editownername.setText(ownerdetail.getOwner_name());
                editaddress.setText(ownerdetail.getShop_address());
                editabout.setText(snapshot.child("about_owner").getValue(String.class));
                editaddress.setMaxLines(5);


                editmail.setText(ownerdetail.getShop_mail());
                mail=editmail.getText().toString();
                Log.d("TAGGGGG",mail);
                editmobile.setText(ownerdetail.getShop_mobile_no());
              //  Picasso.get().load(Uri.parse(ownerdetail.getShop_profile_pic())).into(profileimg);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();



                        progressDialog.dismiss();

                        editshopname.setEnabled(false);
                        editmail.setEnabled(false);
                        editownername.setEnabled(false);
                        editaddress.setEnabled(false);
                       // editpassword.setEnabled(false);
                        editmobile.setEnabled(false);
                        editabout.setEnabled(false);
                    }
                }, 0);   //5 seconds


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        re_authcancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });
        re_authsendmailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!reauth_mail.getText().toString().equals("") && !reath_password.getText().toString().equals("")) {
                    alertDialog.dismiss();
                    mail = reauth_mail.getText().toString();
                    String password = reath_password.getText().toString();
                    mAuth=FirebaseAuth.getInstance();

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(mail, password);

                    Objects.requireNonNull(mAuth.getCurrentUser()).reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Log.d("TAGkk", "User re-authenticated.");
                                        mAuth.sendPasswordResetEmail(editmail.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("TAGkk", "Email sent.");

                                                            progressDialog.setTitle("please Sign-in again...");
                                                            progressDialog.setMessage("mail has been sent.. , checkout inbox");
                                                            progressDialog.setCancelable(false);
                                                            progressDialog.show();

                                                            Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                public void run() {
                                                                    // yourMethod();

                                                                    progressDialog.dismiss();
                                                                    mAuth.signOut();
                                                                    startActivity(new Intent(getContext(), Ownerlogin.class));
                                                                    getActivity().finish();

                                                                }
                                                            }, 3000);   //5 seconds


                                                        }else{
                                                            progressDialog.dismiss();
                                                            Toast.makeText(getContext(),"Password Updation Failed",Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(getContext(), "Invalid Credentials!!", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                } else {
                    Toast.makeText(getContext(), "Enter all Fields!!", Toast.LENGTH_LONG).show();

                }
            }
        });
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("shop_name").setValue(editshopname.getText().toString());
                database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("shop_mail").setValue(editmail.getText().toString());
                database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("shop_mobile_no").setValue(editmobile.getText().toString());
               // database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("shop_password").setValue(editpassword.getText().toString());
                database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("owner_name").setValue(editownername.getText().toString());
                database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("shop_address").setValue(editaddress.getText().toString());
                database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("about_owner").setValue(editabout.getText().toString());

                editshopname.setEnabled(false);
                editmail.setEnabled(false);
               // editpassword.setEnabled(false);
                editmobile.setEnabled(false);
                editownername.setEnabled(false);
                editaddress.setEnabled(false);
                editabout.setEnabled(false);

                FirebaseUser user = mAuth.getCurrentUser();

                if(!mail.equals(editmail.getText().toString())){


                    if(android.util.Patterns.EMAIL_ADDRESS.matcher(editmail.getText().toString()).matches()) {

                        assert user != null;

                        Log.d("TAGkk editmail", editmail.getText().toString());
                        Log.d("TAGkk mail",mail);


                        alertDialogbuilder = new AlertDialog.Builder(getContext())
                                .setTitle("Reset Email")
                                .setMessage("Are you sure you want to Update your E-mail Address ? Press Ok if yes ")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getContext(),"Profile Updated Successfully :)",Toast.LENGTH_SHORT).show();

                                        user.updateEmail(editmail.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("user_mail").setValue(editmail.getText().toString());
                                                            mAuth.signOut();
                                                            startActivity(new Intent(getContext(), Ownerlogin.class));
                                                            Toast.makeText(getContext(), "Sign-in Again", Toast.LENGTH_LONG).show();
                                                            getActivity().finish();
                                                        }else{
                                                            Toast.makeText(getContext(), "Email UPdation Failed", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });


                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        resetmailbox.dismiss();
                                        editmail.setText(mail);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert);
                        resetmailbox=alertDialogbuilder.create();
                        resetmailbox.show();
                    }else {
                        Toast.makeText(getContext(), " Email Format is Invalid", Toast.LENGTH_SHORT).show();
                        editmail.setText(mail);

                    }

                }
            }
        });
        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editshopname.setEnabled(true);
                editmail.setEnabled(true);
               // editpassword.setEnabled(true);
                editabout.setEnabled(true);
                editmobile.setEnabled(true);
                editownername.setEnabled(true);
                editaddress.setEnabled(true);

                mail=editmail.getText().toString();
//                pass=editpassword.getText().toString();

                Toast.makeText(getContext(),"Data is in Edit Mode !!",Toast.LENGTH_SHORT).show();


            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(),"Logging Out",Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                startActivity(new Intent(getActivity(),Ownerlogin.class));
                getActivity().finish();
            }
        });
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Deleting Your Account ");
                progressDialog.setMessage("Thank You for Visit");
                progressDialog.setCancelable(false);
                progressDialog.show();

                database.getReference().child("Shops").child(mAuth.getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        assert user != null;
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "Account Deleted", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getActivity(), Ownerlogin.class));
                                            getActivity().finish();
                                        }else{
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "Account Deleted Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                });
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==pick_image){
            if(resultCode==RESULT_OK) {
                Cursor returnCursor = getActivity().getContentResolver().query(data.getData(), null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();
                Long Image_size = (returnCursor.getLong(sizeIndex) / 1000);
                Log.d("TAGp", "Name:" + returnCursor.getString(nameIndex));
                Log.d("TAGp", "Size: " + Image_size);
                if(Image_size<=500){
                if(selected_btn_in_alertbox.equals("browse_owner_pic")){
                        Loadimg.setVisibility(View.VISIBLE);
                        browsebtn.setVisibility(View.GONE);
                    FirebaseStorage.getInstance().getReference().child("Shopimages").child(mAuth.getCurrentUser().getUid()).child(data.getData().toString().replace("content://com.android.providers.media.documents/document/", "")).putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            FirebaseStorage.getInstance().getReference().child("Shopimages").child(mAuth.getCurrentUser().getUid()).child(data.getData().toString().replace("content://com.android.providers.media.documents/document/", "")).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String img_uri = String.valueOf(uri);
                                    FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("shop_profile_pic").setValue(img_uri);
                                    Toast.makeText(getActivity(), "Image uploaded successfully !!", Toast.LENGTH_LONG).show();
                                    Picasso.get().load(img_uri).into(owner_profile_pic);
                                    Loadimg.setVisibility(View.GONE);
                                    browsebtn.setVisibility(View.VISIBLE);

                                }
                            });
                        }
                    });
                }
                if(selected_btn_in_alertbox.equals("add_shop_service_image_btn_alertbox")){
                    progressDialog.setTitle("Uploading Image...");
                    progressDialog.setMessage("Take a Sip..");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                //Toast.makeText(getActivity(),"Please select minimun 5 Images of your Shop and Services",Toast.LENGTH_LONG).show();
                Log.d("TRTR", "inactires else body i=" + i);

                Log.d("TRTRRRR", data.getData().toString());
                StorageReference Shop_Image_folder = FirebaseStorage.getInstance().getReference().child("Shopimages").child(mAuth.getCurrentUser().getUid());

                Shop_Image_folder.child(data.getData().toString().replace("content://com.android.providers.media.documents/document/", "")).putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Shop_Image_folder.child(data.getData().toString().replace("content://com.android.providers.media.documents/document/", "")).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String img_uri = String.valueOf(uri);
                                FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("Images").child("Shop_Servces_Images").child(data.getData().toString().replace("content://com.android.providers.media.documents/document/", "")).setValue(img_uri);
                                i++;
                                Toast.makeText(getActivity(), "Image uploaded successfully !!", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                //    update_shop_image_list();
                            }
                        });
                    }
                });


            }
                if(selected_btn_in_alertbox.equals("add_shop_images_btn")){
                    progressDialog.setTitle("Uploading Image...");
                    progressDialog.setMessage("Take a Sip..");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    //Toast.makeText(getActivity(),"Please select minimun 5 Images of your Shop and Services",Toast.LENGTH_LONG).show();
                    Log.d("TRTR", "inactires else body i=" + i);

                    Log.d("TRTRRRR", data.getData().toString());
                    StorageReference Shop_Image_folder = FirebaseStorage.getInstance().getReference().child("Shopimages").child(mAuth.getCurrentUser().getUid());

                    Shop_Image_folder.child(data.getData().toString().replace("content://com.android.providers.media.documents/document/", "")).putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Shop_Image_folder.child(data.getData().toString().replace("content://com.android.providers.media.documents/document/", "")).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String img_uri = String.valueOf(uri);
                                    FirebaseDatabase.getInstance().getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("Images").child("Shop_images").child(data.getData().toString().replace("content://com.android.providers.media.documents/document/", "")).setValue(img_uri);
                                    i++;
                                    Toast.makeText(getActivity(), "Image uploaded successfully !!", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    //    update_shop_image_list();
                                }
                            });
                        }
                    });

                }

                }
                else {
                    Toast.makeText(getContext(),"Image Size Must be Less than 500Kb!!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
