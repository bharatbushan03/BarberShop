package com.barbershop.app;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Bundle;

import android.content.Intent;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Ownerlogin extends AppCompatActivity {

    private static final int RC_SIGN_IN =69 ;
    TextView textView,t;
    EditText password,shopmail;
    Button loginbtn;
    ImageButton eyeimg;
    Boolean flag=true;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    FirebaseDatabase database;
   // dbhelperforowner db;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownerlogin);

      //  db = new dbhelperforowner(this);


        password = findViewById(R.id.shop_password);
        shopmail=findViewById(R.id.shop_name);
        eyeimg = findViewById(R.id.button2);
        loginbtn = findViewById(R.id.loginbtn);

        Objects.requireNonNull(getSupportActionBar()).hide();

        TextView textView = (TextView) findViewById(R.id.linkforregisterr);
        SpannableString content = new SpannableString(textView.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        t = findViewById(R.id.textView2);
        t.setPaintFlags(t.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        progressDialog=new ProgressDialog(Ownerlogin.this);
        progressDialog.setTitle("Thank You For Sign-in");
        progressDialog.setMessage("Take a Sip..");
        progressDialog.setCancelable(false);

        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Ownerlogin.this, OwenerRegistration.class));
                finish();
            }
        });






        eyeimg.setOnClickListener(new View.OnClickListener() {
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



        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String email = shopmail.getText().toString().trim();
                final String userPassword = password.getText().toString();

                if (email.equals("") || userPassword.equals("")) {
                    Toast.makeText(Ownerlogin.this, "enterrrr all fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(email,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {

                                database.getReference().child("Shops").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(Ownerlogin.this,"Log-in Successfully",Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Ownerlogin.this,OwnerHomeActivity.class));
                                            finish();

                                        }
                                        else {
                                            progressDialog.dismiss();
                                            mAuth.signOut();
                                            Toast.makeText(Ownerlogin.this,"This account is not registered as a shop owner. Use customer login instead.",Toast.LENGTH_LONG).show();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Ownerlogin.this,error.getMessage(),Toast.LENGTH_LONG).show();
                                    }

                                });


                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(Ownerlogin.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                   // startActivity(new Intent(Ownerlogin.this,OwnerHomeActivity.class));
                    //                 else {
//                    Boolean checkuser = db.checkshopnamepassword(shopname.getText().toString(), password.getText().toString());
//
//                    if (checkuser) {
//                        startActivity(new Intent(Ownerlogin.this, OwnerHomeActivity.class));
//                    } else {
//                        Toast.makeText(Ownerlogin.this, "User does not Exists!!!", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
            }
        });
        if(mAuth.getCurrentUser()!=null){


            database.getReference().child("Shops").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        progressDialog.dismiss();
                        Toast.makeText(Ownerlogin.this,"Log-in Successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Ownerlogin.this,OwnerHomeActivity.class));
                        finish();

                    }
                    else {
                        //Toast.makeText(Ownerlogin.this,"Shop does not Exist",Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });


          //  startActivity(new Intent(Ownerlogin.this, OwnerHomeActivity.class));
        }





    }
}
