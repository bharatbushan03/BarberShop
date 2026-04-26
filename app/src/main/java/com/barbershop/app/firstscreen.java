package com.barbershop.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class firstscreen extends AppCompatActivity {
    ImageButton customerbtn,ownerbtn;
    TextView t;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstscreen);

        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        Objects.requireNonNull(getSupportActionBar()).hide();
        customerbtn=findViewById(R.id.customerbtn);
        ownerbtn=findViewById(R.id.ownerbtn);
        t=findViewById(R.id.textView2);
        t.setPaintFlags(t.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        customerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mAuth.getCurrentUser()!=null){

                    final android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(firstscreen.this);
                    progressDialog.setMessage("Logging in...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            progressDialog.dismiss();
                            if (snapshot.exists()) {
                                Toast.makeText(firstscreen.this,"Log-in Successfully",Toast.LENGTH_SHORT).show();
                                //  Log.d("piooo uidinlogin",mAuth.getCurrentUser().getUid());
                                Intent i = new Intent(firstscreen.this, custHomeActivity.class);
                                //Log.d("piooo reg uidd mailauth",authwithmail_uid);
                                // i.putExtra("userid","");
                              //  progressDialog.dismiss();
                                startActivity(i);
                            }
                            else {
                                startActivity(new Intent(firstscreen.this, Login.class));
                                // Toast.makeText(Login.this,"User does not Exist",Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                        }

                    });

                    //startActivity(new Intent(Login.this, custHomeActivity.class));
                }else {
                    startActivity(new Intent(firstscreen.this, Login.class));

                }

            }
        });

        ownerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mAuth.getCurrentUser()!=null){


                    final android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(firstscreen.this);
                    progressDialog.setMessage("Logging in...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    database.getReference().child("Shops").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            progressDialog.dismiss();
                            if (snapshot.exists()) {
                               // progressDialog.dismiss();
                                Toast.makeText(firstscreen.this,"Log-in Successfully",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(firstscreen.this,OwnerHomeActivity.class));


                            }
                            else {
                                startActivity(new Intent(firstscreen.this,Ownerlogin.class));
                                finish();
                                //Toast.makeText(Ownerlogin.this,"Shop does not Exist",Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                        }

                    });


                    //  startActivity(new Intent(Ownerlogin.this, OwnerHomeActivity.class));
                }else{
                    startActivity(new Intent(firstscreen.this,Ownerlogin.class));
                }




            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
