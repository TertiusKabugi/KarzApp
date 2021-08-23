package com.kabugi.karz;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullName,mEmail,mPassword,cPass,mPhone;
    Button mRegisterBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mFullName   = findViewById(R.id.edtFName);
        mEmail      = findViewById(R.id.edtEmail);
        mPassword   = findViewById(R.id.edtPass);
        mPhone      = findViewById(R.id.edtnumber);
        mRegisterBtn= findViewById(R.id.btnReg);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.pBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String cPassword = cPass.getText().toString().trim();
                final String fullName = mFullName.getText().toString();
                final String phone    = mPhone.getText().toString();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                if (!cPassword.equals(password)){
                    cPass.setError("Passwords Don't Match");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // register the user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            // send verification link

                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegistrationActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });

                            Toast.makeText(RegistrationActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",fullName);
                            user.put("email",email);
                            user.put("phone",phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else {
                            Toast.makeText(RegistrationActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}


//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static com.kabugi.karz.R.layout.activity_registration;
//
//public class RegistrationActivity extends AppCompatActivity {
//    public static final String TAG = "TAG";
//    EditText fullName, mail, phoneNumber, pass, cPass;
//    Button btnReg;
//    FirebaseAuth firebaseAuth;
//    ProgressBar progressBar;
//    FirebaseFirestore fStore;
//    String userID;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(activity_registration);
//        fullName = findViewById(R.id.edtEmail);
//        mail = findViewById(R.id.edtMail);
//        pass = findViewById(R.id.edtPass);
//        phoneNumber = findViewById(R.id.edtnumber);
//        cPass = findViewById(R.id.edtCPass);
//        btnReg = findViewById(R.id.btnReg);
//        progressBar = findViewById(R.id.pBar);
//
//
//        // firebaseAuth = FirebaseAuth.getInstance();
//        firebaseAuth = FirebaseAuth.getInstance();
//        fStore = FirebaseFirestore.getInstance();
//
//        //Check if user is registered
//        if (firebaseAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }
//
//        btnReg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email = mail.getText().toString().trim();
//                String password = pass.getText().toString().trim();
//                String cPassword = cPass.getText().toString().trim();
//                String name = fullName.getText().toString();
//                String number = phoneNumber.getText().toString();
//
//                if (TextUtils.isEmpty(email)){
//                    mail.setError("Enter Email");
//                    return;
//                }
//                if (TextUtils.isEmpty(password)){
//                    pass.setError("Enter Password");
//                    return;
//                }
//
//                if (password.length() < 6 ){
//                    pass.setError("Short Password");
//                    return;
//                }
//
//                if (!cPassword.equals(password)){
//                    cPass.setError("Passwords Don't Match");
//                    return;
//                }
//                progressBar.setVisibility(View.VISIBLE);
//
//                //Register User In Firebase
//                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(RegistrationActivity.this, "User Created", Toast.LENGTH_SHORT).show();
//                            userID = firebaseAuth.getCurrentUser().getUid();
//                            DocumentReference documentReference = fStore.collection("users").document(userID);
//                            Map<String,Object> user = new HashMap<>();
//                            user.put("fName",name);
//                            user.put("email", email);
//                            user.put("phoneNumber",number);
//                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    Log.d(TAG, "onSuccess: User profile is created for "+ userID);
//                                }
//                            });
//                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//
//                        }else {
//                            Toast.makeText(RegistrationActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    }
//                });
//            }
//        });
//
//    }
//}
//
//                            // send verification link
//
////                            FirebaseUser fuser = firebaseAuth.getCurrentUser();
////                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
////                                @Override
////                                public void onSuccess(Void aVoid) {
////                                    Toast.makeText(RegistrationActivity.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
////                                }
////                            }).addOnFailureListener(new OnFailureListener() {
////                                @Override
////                                public void onFailure(@NonNull Exception e) {
////                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
////                                }
////                            });
////
////                            Toast.makeText(RegistrationActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
////                            userID = firebaseAuth.getCurrentUser().getUid();
////                            DocumentReference documentReference = fStore.collection("users").document(userID);
////                            Map<String,Object> user = new HashMap<>();
////                            user.put("fName",fullName);
////                            user.put("email",email);
////                            user.put("phone",phoneNumber);
////                            ((DocumentReference) documentReference).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
////                                @Override
////                                public void onSuccess(Void aVoid) {
////                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
////                                }
////                            }).addOnFailureListener(new OnFailureListener() {
////                                @Override
////                                public void onFailure(@NonNull Exception e) {
////                                    Log.d(TAG, "onFailure: " + e.toString());
////                                }
////                            });