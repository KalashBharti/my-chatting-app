package com.kalash.mywhatsapplite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.kalash.mywhatsapplite.databinding.ActivitySingUpBinding;
import com.kalash.mywhatsapplite.model.users;

public class Sing_Up extends AppCompatActivity {
    ActivitySingUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySingUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();


        progressDialog =new ProgressDialog(Sing_Up.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Creating Your Account");


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {

            // this code for user sign up and google firebase authentication
            @Override
            public void onClick(View view) {
                progressDialog.show();  //starting progress dialog

                auth.createUserWithEmailAndPassword
                        (binding.etEmail.getText().toString(),binding.etPassword.getText().toString()).addOnCompleteListener
                        (new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();  //stopping progress dialog

                        if(task.isSuccessful())
                        {
                            //store UserName,email,password form plan text to users.java class String variables
                            users user=new users(binding.etUserName.getText().toString(),
                                    binding.etEmail.getText().toString(),binding.etPassword.getText().toString());

                            // id is the unique data of the user
                            String id=task.getResult().getUser().getUid();

                            database.getReference().child("User").child(id).setValue(user);

                            Toast.makeText(Sing_Up.this,"User Created SuccessFully",Toast.LENGTH_LONG).show();
                            Intent intent= new Intent(Sing_Up.this,MainActivity.class);
                            startActivity(intent);

                        }
                        else
                        {
                            Toast.makeText(Sing_Up.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });

        binding.tvAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Sing_Up.this,Sign_In.class);
                startActivity(intent);
            }
        });
    }
}