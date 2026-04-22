package com.barbershop.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yourname.barbershop.userdetails.Shop;
import com.yourname.barbershop.userdetails.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.security.acl.Owner;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OwenerRegistration extends AppCompatActivity {
    Button registerbtn,verify;
    ImageButton eyebutton;

    TextView t;
    //dbhelperforowner db;
    EditText password,shopname,shopmail,ownername,shopnumber,shopaddress;
    Boolean flag=true;
    ProgressBar Loading;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ImageView check,incorrect_otp;
    String otp;
    AlertDialog alertDialog;
    AlertDialog.Builder alertDialogBuilder;
    View otpinputView;
    String authwithmail_uid;
    String verificationId;
    String codesent ;

    EditText otp1, otp6, otp5, otp4, otp3, otp2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owener_registration);
        Objects.requireNonNull(getSupportActionBar()).hide();
        t = findViewById(R.id.textView2);
        t.setPaintFlags(t.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        password = findViewById(R.id.shop_password);
        shopname = findViewById(R.id.shop_name);
        ownername = findViewById(R.id.owner_name);
        shopmail = findViewById(R.id.shop_mail);
        shopaddress = findViewById(R.id.shop_address);
        shopnumber = findViewById(R.id.shop_mobile);
        check = findViewById(R.id.check);
        incorrect_otp = findViewById(R.id.incorrectotp);
        Loading = findViewById(R.id.progress_loader);
        registerbtn = findViewById(R.id.registrationbtn);
        registerbtn.setEnabled(true);
        eyebutton = findViewById(R.id.eyebutton);
        progressDialog = new ProgressDialog(OwenerRegistration.this);
        progressDialog.setTitle("Thank You For Sign-up");
        progressDialog.setMessage("We're Creating Your Account");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        LayoutInflater li = LayoutInflater.from(OwenerRegistration.this);
        otpinputView = li.inflate(R.layout.otp_input, null);

        alertDialogBuilder = new AlertDialog.Builder(
                OwenerRegistration.this);
        alertDialogBuilder
                .setCancelable(false);
        alertDialogBuilder.setView(otpinputView);

        alertDialog = alertDialogBuilder.create();

        otp1 = (EditText) otpinputView
                .findViewById(R.id.otpinput1);
        otp6 = (EditText) otpinputView
                .findViewById(R.id.otpinput6);
        otp5 = (EditText) otpinputView
                .findViewById(R.id.otpinput5);
        otp4 = (EditText) otpinputView
                .findViewById(R.id.otpinput4);
        otp3 = (EditText) otpinputView
                .findViewById(R.id.otpinput3);
        otp2 = (EditText) otpinputView
                .findViewById(R.id.otpinput2);
        verify = (Button) otpinputView.findViewById(R.id.verify);


        //db=new dbhelperforowner(this);

        eyebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = !flag;
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = !flag;
                }
            }
        });


        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerbtn.setEnabled(false);
                incorrect_otp.setVisibility(View.GONE);



                if (shopname.getText().toString().equals("") || shopmail.getText().toString().equals("") || password.getText().toString().equals("") || ownername.getText().toString().equals("") || shopaddress.getText().toString().equals("") || shopnumber.getText().toString().equals("")) {
                    Toast.makeText(OwenerRegistration.this, "enterrrr all fields", Toast.LENGTH_SHORT).show();
                    registerbtn.setEnabled(true);
                } else {

                    if (shopnumber.getText().toString().length() != 10) {
                        Toast.makeText(OwenerRegistration.this, "Invalid mobile no length", Toast.LENGTH_SHORT).show();
                        registerbtn.setEnabled(true);
                    } else {
                        Loading.setVisibility(View.VISIBLE);
                        progressDialog.show();
                        sendVerificationCode("+91" + shopnumber.getText().toString());
                    }
                }

            }
        });


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!otp1.getText().toString().equals("") && !otp2.getText().toString().equals("") && !otp3.getText().toString().equals("") && !otp4.getText().toString().equals("") && !otp5.getText().toString().equals("") && !otp6.getText().toString().equals("")) {
                    otp = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString() + otp5.getText().toString() + otp6.getText().toString();
                    alertDialog.dismiss();
                    // Log.d("piooo  verifyclick", "");
                    verifyCode(otp);
                } else {
                    Toast.makeText(OwenerRegistration.this, "Enter Full OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        Toast.makeText(OwenerRegistration.this,"Ready To Go",Toast.LENGTH_SHORT).show();
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            mAuth.createUserWithEmailAndPassword(shopmail.getText().toString(), password.getText().toString()).
                                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if (task.isSuccessful()) {
                                                Loading.setVisibility(View.GONE);
                                                check.setVisibility(View.VISIBLE);

                                                authwithmail_uid= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                                //String shop_profile_pic, String shop_name, String owner_name, String shop_mail, String shop_password, String shop_mobile_no, String shop_address
                                                Shop shop=new Shop("default",shopname.getText().toString(),ownername.getText().toString(),shopmail.getText().toString(),password.getText().toString(),shopnumber.getText().toString(),shopaddress.getText().toString());

                                                Log.d("piooo mauthinshubhm",mAuth.getCurrentUser().getUid());
                                                database.getReference().child("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").setValue(shop);
                                                database.getReference("Shops").child(mAuth.getCurrentUser().getUid()).child("shop_details").child("joining_year").setValue(Calendar.getInstance().get(Calendar.YEAR)+"");
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    public void run() {
                                                        Intent i = new Intent(OwenerRegistration.this, OwnerHomeActivity.class);
                                                        Log.d("piooo reg uidd mailauth",authwithmail_uid);
                                                        //i.putExtra("userid",authwithmail_uid);

                                                        startActivity(i);
                                                        finish();


                                                    }
                                                }, 2000);

                                            } else {
                                                Toast.makeText(OwenerRegistration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Log.d("piooo","failll");
                            registerbtn.setEnabled(true);
                            Loading.setVisibility(View.GONE);
                            incorrect_otp.setVisibility(View.VISIBLE);
                            Toast.makeText(OwenerRegistration.this, "Invalid OTP!!! Retry", Toast.LENGTH_LONG).show();
                            // Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        Log.d("piooo", number);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//            // below line is used for getting OTP code
//            // which is sent in phone auth credentials.
//
            Log.d("piooo","verification completed ");
            codesent = phoneAuthCredential.getSmsCode();
            Log.d("piooo  firebasecodesent", codesent);


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Log.d("piooo  fail", "");
            Toast.makeText(OwenerRegistration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(OwenerRegistration.this, "Try Again", Toast.LENGTH_SHORT).show();
            registerbtn.setEnabled(true);
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            Log.d("piooo  code sent", "");
            Log.d("piooo  otp code", s);
            verificationId = s;
            progressDialog.dismiss();
            alertDialog.show();

            otp1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    otp2.requestFocus();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            otp2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    otp3.requestFocus();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            otp3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    otp4.requestFocus();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            otp4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    otp5.requestFocus();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            otp5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    otp6.requestFocus();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

    };

    private void verifyCode(String code) {
        Log.d("piooo  verify code fun", "");
        Log.d("piooo comparison", "code="+code+"codesent="+codesent);

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        codesent=credential.getSmsCode();
        signInWithCredential(credential);

    }
}

