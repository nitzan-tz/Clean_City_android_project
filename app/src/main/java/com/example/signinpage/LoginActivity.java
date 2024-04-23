package com.example.signinpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail;
    private FirebaseAuth mAuth;

    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogIn);
        btnSignin = (Button) findViewById(R.id.btnSignin);
        btnLogin.setOnClickListener(this);
        btnSignin.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == btnLogin.getId()) {
            logIn(etEmail.getText().toString(), etPassword.getText().toString());

        }
        if (view.getId() == btnSignin.getId()) {
            Intent i=new Intent(LoginActivity.this, SigninActivity.class);
            startActivity(i);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            if(mAuth.getCurrentUser().getEmail().equals("admin@gmail.com")){
                Intent i=new Intent(LoginActivity.this, AdminReportsActivity.class);
                startActivity(i);
            }
            else {
                Intent i = new Intent(LoginActivity.this, MapsActivity.class);
                startActivity(i);
            }
        }
    }
    // [END on_start_check_user]
    private void logIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LoginActivity", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(email.equals("admin@gmail.com")){
                                Intent i=new Intent(LoginActivity.this, AdminReportsActivity.class);
                                startActivity(i);
                            }
                            else {
                                Intent i = new Intent(LoginActivity.this, MapsActivity.class);
                                startActivity(i);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LogninActivity", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END sign_in_with_email]
    }
}