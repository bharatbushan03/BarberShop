package com.barbershop.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.barbershop.app.userdetails.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profilescreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profilescreen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    AlertDialog alertDialog;
    AlertDialog.Builder alertDialogBuilder,alertDialogBuilder1;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage Storage;
    StorageReference reference;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Toolbar profilefragment_toolbar;
    Button logoutbtn,deletebtn,re_authsendmailbtn;
    ImageView profileimg;
    private Activity activity;
    EditText editname, editmobile, editmail,reath_password,reauth_mail;
    ImageButton browsebtn, editbutton, savebutton,re_authcancelbtn;
    ActivityResultLauncher<String> launcher;
    ProgressDialog progressDialog;
    ProgressBar Loadimg;
    TextView changepassword;
    View re_authbox;
    AlertDialog.Builder alertDialogbuilder;
    AlertDialog resetmailbox;
    Boolean google_signin=false;

    String userid;
    //this is for changing email and password of user
    String mail;
    private int group1Id = 1;
    int helpid=Menu.FIRST;
    public Profilescreen() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profilescreen.
     */
    // TODO: Rename and change types and number of parameters
    public static Profilescreen newInstance(String param1, String param2) {
        Profilescreen fragment = new Profilescreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.upperrightmenulogout,menu);
        menu.add(group1Id,helpid,helpid, "help").setIcon(R.drawable.ic_baseline_help_24);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.threedots_logout)
        {
            Toast.makeText(getActivity(), "Logging Out", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            startActivity(new Intent(getActivity(), Login.class));
            getActivity().finish();
        }
        if(item.getItemId()==R.id.threedots_deleteaccount){
            progressDialog.setTitle("Deleting Your Account ");
            progressDialog.setMessage("Thank You for Visit");
            progressDialog.setCancelable(false);
            progressDialog.show();

            database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Account Deleted", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getActivity(), Login.class));
                                        getActivity().finish();
                                    }else{
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Account Deletion Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
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

        for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if (user.getProviderId().equals("google.com")) {
                Log.d("TAGkk","User is signed in with google");
                google_signin=true;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_profilescreen, container, false);


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

        logoutbtn = (Button) view.findViewById(R.id.logoutbtn);
        deletebtn = (Button) view.findViewById(R.id.deletebtnn);

        editbutton = (ImageButton) view.findViewById(R.id.editbutton);
        savebutton = (ImageButton) view.findViewById(R.id.savebtn);
        editname = (EditText) view.findViewById(R.id.editname);
        editmail = (EditText) view.findViewById(R.id.editmail);
        editmobile = (EditText) view.findViewById(R.id.editmobile);

        browsebtn = (ImageButton) view.findViewById(R.id.browseimg);
        profileimg = (ImageView) view.findViewById(R.id.profile_image);
        Loadimg = (ProgressBar) view.findViewById(R.id.Loadimg);
        changepassword=(TextView) view.findViewById(R.id.changepassword_customer_profile);


        //for checking if mail is changed or not in savebuttonclicklitsener





        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            profileimg.setImageURI(result);


            Storage = FirebaseStorage.getInstance();

                reference = Storage.getReference().child("images").child(mAuth.getCurrentUser().getUid());
                reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("user_profile_pic").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // progressDialog2.dismiss();
                                        Toast.makeText(getContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                        Loadimg.setVisibility(View.INVISIBLE);
                                        browsebtn.setVisibility(View.VISIBLE);

                                    }
                                });
                            }
                        });
                    }
                });

            });

          //  Log.d("piooo userid inprofile", getArguments().getString("userid"));
//            Log.d("piooo mauthuse profile", mAuth.getCurrentUser().getUid());




                database.getReference("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        //  Log.d("piooogettinfoinprofile", mAuth.getCurrentUser().getUid() + "data:" + snapshot.getValue(user.class).getUser_name());

                        user userdetail = snapshot.getValue(user.class);
                        if (userdetail != null) {
                            editname.setText(userdetail.getUser_name());

                            editmail.setText(userdetail.getUser_mail());
                            mail=editmail.getText().toString();
                            Log.d("TAGkk onviewcreatemail",mail);
                            editmobile.setText(userdetail.getUser_mobile_no());
                                if(userdetail.getUser_profile_pic()!=null)
                            Picasso.get().load(Uri.parse(userdetail.getUser_profile_pic())).into(profileimg);
                            progressDialog.dismiss();
                            editname.setEnabled(false);
                            editmail.setEnabled(false);

                            editmobile.setEnabled(false);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    // yourMethod();

                                    progressDialog.dismiss();


                                }
                            }, 0);   //5 seconds

                        } else {
                           // Toast.makeText(getContext(), "cant find any user", Toast.LENGTH_SHORT).show();
                        }
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

                if(google_signin){
                    changepassword.setVisibility(View.GONE);
                }

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


                    progressDialog.setTitle("please Sign-in again...");
                    progressDialog.setMessage("mail has been sent.. , checkout inbox");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(mail, password);

// Prompt the user to re-provide their sign-in credentials

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



                                                            Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                public void run() {
                                                                    // yourMethod();

                                                                    progressDialog.dismiss();
                                                                    mAuth.signOut();
                                                                    startActivity(new Intent(getContext(), Login.class));
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



                        database.getReference("Users").child(Objects.requireNonNull(mAuth.getCurrentUser().getUid())).child("user_name").setValue(editname.getText().toString());
                        //database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("user_mail").setValue(editmail.getText().toString());
                        database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("user_mobile_no").setValue(editmobile.getText().toString());
                       // database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("user_password").setValue(editpassword.getText().toString());





                    editname.setEnabled(false);
                    editmail.setEnabled(false);

                    editmobile.setEnabled(false);
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
                                                                    startActivity(new Intent(getContext(), Login.class));
                                                                    Toast.makeText(getContext(), "Sign-in Again", Toast.LENGTH_LONG).show();
                                                                    getActivity().finish();
                                                                }
                                                                else{
                                                                    Toast.makeText(getContext(), "Email Updation Failed", Toast.LENGTH_LONG).show();
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
                    editname.setEnabled(true);
                    if(!google_signin){
                        editmail.setEnabled(true);
                    }
                    editmobile.setEnabled(true);

                    mail=editmail.getText().toString();


                    Toast.makeText(getContext(), "Data is in Edit Mode !!", Toast.LENGTH_SHORT).show();


                }
            });
            browsebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launcher.launch("image/*");
                    browsebtn.setVisibility(View.INVISIBLE);
                    Loadimg.setVisibility(View.VISIBLE);

                }
            });
            logoutbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(getActivity(), "Logging Out", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    startActivity(new Intent(getActivity(), Login.class));
                    getActivity().finish();

                }
            });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                alertDialog.setTitle("Are you sure you want to delete this account !!");
//                alertDialog.setMessage("Press ok if yes.");
//                alertDialog.show();
                alertDialogBuilder1 = new AlertDialog.Builder(getContext())
                        .setTitle("Delete Account")
                        .setMessage("Are you sure you want to Delete this account press ok if yes.")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                                progressDialog.setTitle("Deleting Your Account ");
                                progressDialog.setMessage("Thank You for Visit");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        user.delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(getActivity(), "Account Deleted", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(getActivity(), Login.class));
                                                            getActivity().finish();
                                                        }else{
                                                            progressDialog.dismiss();
                                                            Toast.makeText(getActivity(), "Account Deletion Failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                    }
                                });

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog=alertDialogBuilder1.create();
                alertDialog.show();



            }
        });


            return view;

    }

}

































