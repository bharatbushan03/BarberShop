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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yourname.barbershop.userdetails.user;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private static final int RC_SIGN_IN =65 ;
    TextView textView,t;
    EditText user_password,user_email;
    Button loginbtn;
    ImageButton eyeicon;
    ImageButton googlebtn;
    Boolean flag=true;
    public static final String tag ="zesus";
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    GoogleSignInClient mGoogleSignInClient;
   // dbhelper db;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_password=findViewById(R.id.shop_password);

        user_email=findViewById(R.id.shop_name);
        //Log.d(tag,"1kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");

        //db=new dbhelper(this);
       // Log.d(tag,"2kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");

        Objects.requireNonNull(getSupportActionBar()).hide();
        //Log.d(tag,"3kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");

        loginbtn=findViewById(R.id.loginbtn);
        googlebtn=findViewById(R.id.googlebtn);

        TextView textView = (TextView) findViewById(R.id.linkforregisterr);
        SpannableString content = new SpannableString(textView.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        t=findViewById(R.id.textView2);
        t.setPaintFlags(t.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //Log.d(tag,"4kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");

        progressDialog=new ProgressDialog(Login.this);
//        progressDialog.setContentView(R.layout.progress_bar);
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

       progressDialog.setTitle("Thank You For Sign-in");
        progressDialog.setMessage("Take a Sip..");
        // db=new dbhelper(this);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        //186152236574-d497qd22hpan9fe67mjpleau7sg61l86.apps.googleusercontent.com
        //default_web_client_id

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
                finish();
//                startActivity(new Intent(Login.this,Registration.class));
            }
        });
        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setTitle("Sign-in With Google");
                progressDialog.setMessage("Take a Sip..");
                progressDialog.show();
                signIn();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_email.getText().toString().equals("") || user_password.getText().toString().equals("")) {
                    Toast.makeText(Login.this,"enterrrr all fields",Toast.LENGTH_SHORT).show();

                }else {


                    ///sassssssssssssssssssssssssssssssss

//                    FirebaseDatabase.getInstance().getReference().child("Users")
//                            .addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    int count=0;
//                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                        user user = snapshot.getValue(user.class);
//                                        if (user.getUser_mail().equals(user_email.getText().toString()) ) {
//                                            count++;
//                                            if(user.getUser_password().equals(user_password.getText().toString())) {
//
//                                                Toast.makeText(Login.this, "Log-in Successfully", Toast.LENGTH_SHORT).show();
////                                                Log.d("piooo uidinlogin", mAuth.getCurrentUser().getUid());
//                                                Intent i = new Intent(Login.this, custHomeActivity.class);
//                                                //Log.d("piooo reg uidd mailauth",authwithmail_uid);
//                                                i.putExtra("userid", "");
//                                                startActivity(i);
//                                            }else{
//                                                Toast.makeText(Login.this, "Invalid password!!", Toast.LENGTH_SHORT).show();
//
//                                            }
//                                        }
//                                    }
//                                    if(count==0){
//                                        Toast.makeText(Login.this, "Invalid email!!", Toast.LENGTH_SHORT).show();
//
//                                    }
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//                                }
//                            });


//                                    if (usersBean.getUser_password().equals(user_password.getText().toString())) {
//                                        Toast.makeText(Login.this,"Log-in Successfully",Toast.LENGTH_SHORT).show();
//                                        Log.d("piooo uidinlogin",mAuth.getCurrentUser().getUid());
//                                        Intent i = new Intent(Login.this, custHomeActivity.class);
//                                        //Log.d("piooo reg uidd mailauth",authwithmail_uid);
//                                        i.putExtra("userid","");
//                                        startActivity(i);
                    //saaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    mAuth.signInWithEmailAndPassword(user_email.getText().toString(),user_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {

                                database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Toast.makeText(Login.this,"Log-in Successfully",Toast.LENGTH_SHORT).show();
                                            Log.d("piooo uidinlogin",mAuth.getCurrentUser().getUid());
                                            Intent i = new Intent(Login.this, custHomeActivity.class);
                                            //Log.d("piooo reg uidd mailauth",authwithmail_uid);
                                           // i.putExtra("userid","");
                                            progressDialog.dismiss();
                                            startActivity(i);
                                        }
                                        else {
                                            progressDialog.dismiss();
                                            Toast.makeText(Login.this,"User does not Exist",Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }

                                });

                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });


//                if (username.getText().toString().equals("") || user_password.getText().toString().equals("")) {
//                    Toast.makeText(Login.this, "enterrrr all fields", Toast.LENGTH_SHORT).show();
//                } else {
//                    Boolean checkuser = db.checkusernamepassword(username.getText().toString(), user_password.getText().toString());
//
//                    if (checkuser) {
//                        startActivity(new Intent(Login.this, custHomeActivity.class));
//                    } else {
//                        Toast.makeText(Login.this, "User does not Exists!!!", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
                }
        }});

         if(mAuth.getCurrentUser()!=null){

             database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot snapshot) {
                     if (snapshot.exists()) {
                         Toast.makeText(Login.this,"Log-in Successfully",Toast.LENGTH_SHORT).show();
                       //  Log.d("piooo uidinlogin",mAuth.getCurrentUser().getUid());
                         Intent i = new Intent(Login.this, custHomeActivity.class);
                         //Log.d("piooo reg uidd mailauth",authwithmail_uid);
                         // i.putExtra("userid","");
                         progressDialog.dismiss();
                         startActivity(i);
                     }
                     else {
                        // Toast.makeText(Login.this,"User does not Exist",Toast.LENGTH_SHORT).show();

                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }

             });

            //startActivity(new Intent(Login.this, custHomeActivity.class));
        }

        eyeicon=findViewById(R.id.button2);


        eyeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    user_password.setTransformationMethod( HideReturnsTransformationMethod.getInstance());
                }else{
                    user_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                flag=!flag;
            }
        });




    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                           // Toast.makeText(Login.this,"Signin With Google",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this,custHomeActivity.class));

                            user userr=new user();
                            userr.setUser_name(user.getDisplayName());
                            userr.setUser_mail(user.getEmail());
                            userr.setUser_mobile_no(user.getPhoneNumber());
                            userr.setUser_profile_pic(user.getPhotoUrl().toString());

                            database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).setValue(userr);



                            //  updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                           // updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Login.this,firstscreen.class));
        finish();
    }
}
