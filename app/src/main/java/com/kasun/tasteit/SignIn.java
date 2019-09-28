package com.kasun.tasteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kasun.tasteit.Common.Common;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
     EditText edtPhone,edtPassword;
     Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //setTitle("Sign In");

        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        //Init Firebase

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_User = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please Waiting...");
                mDialog.show();

               table_User.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       //Check if user not exits in database

                       if(dataSnapshot.child(edtPhone.getText().toString()).exists()) {


                           //Get User Information
                           mDialog.dismiss();

                           User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                           if (user.getPassword().equals(edtPassword.getText().toString())) {
                               {
                                   Intent homeIntent = new Intent(SignIn.this, dashboard.class);
                                   Common.currentUser = user;
                                   startActivity(homeIntent);
                                   finish();
                               }
                           }

                           else {
                               Toast.makeText(SignIn.this, "Sign in Failed!", Toast.LENGTH_SHORT).show();
                           }
                       }

                       else
                       {
                           mDialog.dismiss();
                           Toast.makeText(SignIn.this, "User not exists!", Toast.LENGTH_SHORT).show();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });
            }
        });


    }
}
