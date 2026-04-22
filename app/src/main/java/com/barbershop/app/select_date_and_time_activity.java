package com.barbershop.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yourname.barbershop.userdetails.services;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class select_date_and_time_activity extends AppCompatActivity {
    TextView shop_name_in_selectdate_activity;
    ListView showing_selected_services_listview;

    ArrayList<String> all_service_info;
    String appointment_date="";
    String shopname,shop_upi_id;
    int Total_amount;


    int PAY_REQUEST_CODE = 123;


    CalendarView calendar;
    String shop_id;
    int no_of_slots;
    Button make_payment_btn;
    CheckBox slot1,slot2,slot3,slot4,slot5,slot6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date_and_time);
        Objects.requireNonNull(getSupportActionBar()).hide();

        shop_id=getIntent().getStringExtra("shop_id");


        calendar=findViewById(R.id.calendarView);
        slot1=findViewById(R.id.slot_1);
        slot2=findViewById(R.id.slot_2);
        slot3=findViewById(R.id.slot_3);
        slot4=findViewById(R.id.slot_4);
        slot5=findViewById(R.id.slot_5);
        slot6=findViewById(R.id.slot_6);
        make_payment_btn=findViewById(R.id.make_payment_btn);
        showing_selected_services_listview=findViewById(R.id.showing_selected_services_listview);
        shop_name_in_selectdate_activity=findViewById(R.id.shop_name_in_selectdate_activity);


         all_service_info=new ArrayList<>();

        ArrayList<String> selected_services_list = getIntent().getStringArrayListExtra("selected_services");


calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
        appointment_date=i2+"-"+i1+"-"+i;
    }
});

        FirebaseDatabase.getInstance().getReference("Shops").child(shop_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                all_service_info.clear();
                Total_amount=0;

                shopname=snapshot.child("shop_details").child("shop_name").getValue(String.class);
                for (DataSnapshot snapshot_for_service:snapshot.child("services").getChildren()
                     ) {
                    if(selected_services_list.contains(snapshot_for_service.child("service_name").getValue(String.class))) {
                        all_service_info.add(snapshot_for_service.child("service_name").getValue(String.class) + " - " + snapshot_for_service.child("service_duration").getValue(String.class) + "mins - " + snapshot_for_service.child("service_price").getValue(String.class) + "$");
                        Total_amount=Total_amount+Integer.parseInt(snapshot_for_service.child("service_price").getValue(String.class));

                    }  }

                ArrayAdapter<String> ad=new ArrayAdapter<>(select_date_and_time_activity.this,android.R.layout.simple_list_item_1,all_service_info);
                showing_selected_services_listview.setAdapter(ad);

                shop_name_in_selectdate_activity.setText(shop_name_in_selectdate_activity.getText().toString().replace("Evans Saloon",snapshot.child("shop_details").child("shop_name").getValue(String.class)));

                ArrayList<String> slots_list_ofstrings=new ArrayList<>();
                if(snapshot.child("shop_details").child("slots_for_booking").exists()){
                     no_of_slots= (int) snapshot.child("shop_details").child("slots_for_booking").getChildrenCount();

                    for (DataSnapshot datasnapshot:snapshot.child("shop_details").child("slots_for_booking").getChildren()
                    ) {
                        slots_list_ofstrings.add(datasnapshot.getValue(String.class));
                    }

                    if(no_of_slots==1){
                        slot1.setVisibility(View.VISIBLE);
                        slot1.setText(slots_list_ofstrings.get(0));
                    }
                    if(no_of_slots==2){
                        slot1.setVisibility(View.VISIBLE);
                        slot2.setVisibility(View.VISIBLE);
                        slot1.setText(slots_list_ofstrings.get(0));
                        slot2.setText(slots_list_ofstrings.get(1));
                    }
                    if(no_of_slots==3){
                        slot1.setVisibility(View.VISIBLE);
                        slot2.setVisibility(View.VISIBLE);
                        slot3.setVisibility(View.VISIBLE);
                        slot1.setText(slots_list_ofstrings.get(0));
                        slot2.setText(slots_list_ofstrings.get(1));
                        slot3.setText(slots_list_ofstrings.get(2));
                    }

                    if(no_of_slots==4){
                        slot1.setVisibility(View.VISIBLE);
                        slot2.setVisibility(View.VISIBLE);
                        slot3.setVisibility(View.VISIBLE);
                        slot4.setVisibility(View.VISIBLE);
                        slot1.setText(slots_list_ofstrings.get(0));
                        slot2.setText(slots_list_ofstrings.get(1));
                        slot3.setText(slots_list_ofstrings.get(2));
                        slot4.setText(slots_list_ofstrings.get(3));
                    }
                    if(no_of_slots==5){
                        slot1.setVisibility(View.VISIBLE);
                        slot2.setVisibility(View.VISIBLE);
                        slot3.setVisibility(View.VISIBLE);
                        slot4.setVisibility(View.VISIBLE);
                        slot5.setVisibility(View.VISIBLE);
                        slot1.setText(slots_list_ofstrings.get(0));
                        slot2.setText(slots_list_ofstrings.get(1));
                        slot3.setText(slots_list_ofstrings.get(2));
                        slot4.setText(slots_list_ofstrings.get(3));
                        slot4.setText(slots_list_ofstrings.get(4));
                    }
                    if(no_of_slots==6){
                        slot1.setVisibility(View.VISIBLE);
                        slot2.setVisibility(View.VISIBLE);
                        slot3.setVisibility(View.VISIBLE);
                        slot4.setVisibility(View.VISIBLE);
                        slot5.setVisibility(View.VISIBLE);
                        slot6.setVisibility(View.VISIBLE);
                        slot1.setText(slots_list_ofstrings.get(0));
                        slot2.setText(slots_list_ofstrings.get(1));
                        slot3.setText(slots_list_ofstrings.get(2));
                        slot4.setText(slots_list_ofstrings.get(3));
                        slot4.setText(slots_list_ofstrings.get(4));
                        slot5.setText(slots_list_ofstrings.get(5));
                    }
                   }else {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        calendar=findViewById(R.id.calendarView);
        calendar.setMinDate(calendar.getDate());




        slot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slot2.setChecked(false);
                slot3.setChecked(false);
                slot4.setChecked(false);
                slot5.setChecked(false);
                slot6.setChecked(false);
            }
        });
        slot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slot1.setChecked(false);
                slot3.setChecked(false);
                slot4.setChecked(false);
                slot5.setChecked(false);
                slot6.setChecked(false);
            }
        });
        slot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slot1.setChecked(false);
                slot2.setChecked(false);
                slot4.setChecked(false);
                slot5.setChecked(false);
                slot6.setChecked(false);
            }
        });
        slot4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slot1.setChecked(false);
                slot3.setChecked(false);
                slot2.setChecked(false);
                slot5.setChecked(false);
                slot6.setChecked(false);
            }
        });
        slot5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slot1.setChecked(false);
                slot3.setChecked(false);
                slot2.setChecked(false);
                slot4.setChecked(false);
                slot6.setChecked(false);
            }
        });
        slot6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slot1.setChecked(false);
                slot3.setChecked(false);
                slot2.setChecked(false);
                slot5.setChecked(false);
                slot4.setChecked(false);
            }
        });
        make_payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slot="";
                if(slot6.isChecked())slot=slot6.getText().toString();
                if(slot5.isChecked())slot=slot5.getText().toString();
                if(slot4.isChecked())slot=slot4.getText().toString();
                if(slot3.isChecked())slot=slot3.getText().toString();
                if(slot2.isChecked())slot=slot2.getText().toString();
                if(slot1.isChecked())slot=slot1.getText().toString();
                if(appointment_date.equals("")){
                    final Calendar start_range=Calendar.getInstance();
                    int d=start_range.get(Calendar.DAY_OF_MONTH);
                    int m=start_range.get(Calendar.MONTH);
                    int y=start_range.get(Calendar.YEAR);
                    appointment_date=d+"-"+m+"-"+y;
                }

                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("appointments").child(shop_id).child("shop_name").setValue(shopname);
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("appointments").child(shop_id).child("appointment_dates").child(appointment_date).child("slot").setValue(slot);
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("appointments").child(shop_id).child("appointment_dates").child(appointment_date).child("total_amount").setValue(Total_amount);
                FirebaseDatabase.getInstance().getReference("Shops").child(shop_id).child("appointments").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(appointment_date).child("slot").setValue(slot);
                FirebaseDatabase.getInstance().getReference("Shops").child(shop_id).child("appointments").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(appointment_date).child("date").setValue(appointment_date);
                FirebaseDatabase.getInstance().getReference("Shops").child(shop_id).child("appointments").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(appointment_date).child("status").setValue("Pending");
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("appointments").child(shop_id).child("appointment_dates").child(appointment_date).child("status").setValue("Pending");




                String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";


                String transaction_note="";

                for (String s:all_service_info
                     ) {
                    FirebaseDatabase.getInstance().getReference("Shops").child(shop_id).child("appointments").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(appointment_date).child("selected_services").child(s.substring(0,s.indexOf('-'))).setValue(s);
                    transaction_note=transaction_note+"\n"+s;
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("appointments").child(shop_id).child("appointment_dates").child(appointment_date).child("services").child(s.substring(0,s.indexOf('-'))).setValue(s);
                }

                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user_name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FirebaseDatabase.getInstance().getReference("Shops").child(shop_id).child("appointments").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("customer_name").setValue(snapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                FirebaseDatabase.getInstance().getReference("Shops").child(shop_id).child("appointments").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(appointment_date).child("Total_amount").setValue(Total_amount+"");
                Uri uri =
                        new Uri.Builder()
                                .scheme("upi")
                                .authority("pay")
                                .appendQueryParameter("pa", "desepticon1910@oksbi")
                                .appendQueryParameter("pn", shopname)
                                .appendQueryParameter("tn",transaction_note+"\n\n"+"On Date :"+appointment_date+"\n"+"Slot :"+slot)
                                .appendQueryParameter("am", Total_amount+"")
                                .appendQueryParameter("cu", "INR")
                                .appendQueryParameter("url", "your-transaction-url")
                                .build();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);

                Intent chooser=Intent.createChooser(intent,"Pay to Shop Name With");
                if(chooser.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(intent, PAY_REQUEST_CODE);
                }else {
                    Toast.makeText(select_date_and_time_activity.this, "No upi app Found Please Install!!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


//    private void setNotification(String dateTimeStr,String id)
//    {
//
//
//        SimpleDateFormat formatToCompare = new SimpleDateFormat(
//                "MM/dd/yyyy hh:mm");
//
//        Date dateNotification = null;
//
//        try {
//            dateNotification = formatToCompare
//                    .parse(dateTimeStr);
//        } catch (java.text.ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//
//        Intent intent = null;
//        intent = new Intent(getApplicationContext(), TimeAlarm.class);
//        intent.putExtra("NOTIFICATION", "You have a appointment today Be Ready For that!!");
//        intent.putExtra("ID", Integer.parseInt(id));
//        intent.putExtra("LONG", dateNotification.getTime());
//
//        PendingIntent sender = PendingIntent.getBroadcast(
//                getApplicationContext(),Integer.parseInt(id),
//                intent, 0);
//
//
//
//        AlarmManager am = null;
//        am = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        // am.setRepeating(AlarmManager.RTC_WAKEUP,
//        // dateNotification.getTime(), AlarmManager.INTERVAL_DAY,
//        // sender);
//
//        am.set(AlarmManager.RTC_WAKEUP, dateNotification.getTime(),
//                sender);
//        Toast.makeText(this, "u will recieve reminder at ...", Toast.LENGTH_SHORT).show();
//
//    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(select_date_and_time_activity.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(select_date_and_time_activity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(select_date_and_time_activity.this, "Payment Done Successfylly", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(select_date_and_time_activity.this,custHomeActivity.class));
                finish();

                Log.e("UPI", "Cancelled by user: "+approvalRefNo);
            }
            else {
                Toast.makeText(select_date_and_time_activity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(select_date_and_time_activity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) select_date_and_time_activity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PAY_REQUEST_CODE){
            if ((RESULT_OK == resultCode)) {
                if (data != null) {
                    String trxt = data.getStringExtra("response");
                    Log.e("UPI", "onActivityResult: " + trxt);
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(trxt);
                    upiPaymentDataOperation(dataList);
                } else {
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
            } else {
                //when user simply back without payment
                Log.e("UPI", "onActivityResult: " + "Return data is null");
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("nothing");
                upiPaymentDataOperation(dataList);
            }
            }
        }

    }


