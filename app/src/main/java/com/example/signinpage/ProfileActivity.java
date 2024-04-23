package com.example.signinpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName;
    private EditText etAddress;
    private EditText etPhone;
    private Button btnSave;
    private DatabaseReference database;
    private FirebaseAuth mAuth;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        etName= (EditText) findViewById(R.id.etName);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etPhone = (EditText) findViewById(R.id.etPhone);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        database= FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        database.child("users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getValue(User.class);
                etName.setText(dataSnapshot.getValue(User.class).getName());
                etAddress.setText(dataSnapshot.getValue(User.class).getAddress());
                etPhone.setText(dataSnapshot.getValue(User.class).getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnSave.getId()) {
            database.child("users").child(mAuth.getUid()).child("name").setValue(etName.getText().toString());
            database.child("users").child(mAuth.getUid()).child("address").setValue(etAddress.getText().toString());
            database.child("users").child(mAuth.getUid()).child("phone").setValue(etPhone.getText().toString());

            Intent i = new Intent(ProfileActivity.this, MapsActivity.class);
            startActivity(i);
        }
    }
}