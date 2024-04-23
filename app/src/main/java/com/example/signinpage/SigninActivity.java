package com.example.signinpage;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName;
    private EditText etAddress;
    private EditText etPhone;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnSave;
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private final int REQUEST_CODE = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        etName= (EditText) findViewById(R.id.etName);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference();

        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(SigninActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SigninActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE);
            }
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnSave.getId()) {
            signIn(etEmail.getText().toString(), etPassword.getText().toString());
        }
    }


    private void signIn(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SigninActivity", "createUserWithEmail:success");
                            User user= new User(mAuth.getUid(), etName.getText().toString(),etAddress.getText().toString(),etPhone.getText().toString(),etEmail.getText().toString());
                            database.child("users").child(user.getId()).setValue(user);
                            if(email.equals("admin@gmail.com")){
                                Intent i=new Intent(SigninActivity.this, AdminReportsActivity.class);
                                startActivity(i);
                            }
                            else{
                                Intent i = new Intent(SigninActivity.this, MapsActivity.class);
                                startActivity(i);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SigninActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SigninActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }
}