package com.kalash.mywhatsapplite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kalash.mywhatsapplite.data.desplayData;
import com.kalash.mywhatsapplite.databinding.ActivityGroupChatBinding;
import com.kalash.mywhatsapplite.databinding.ActivitySettingBinding;
import com.kalash.mywhatsapplite.model.users;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        //instance of FireBase Feature
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //loading cloud picture to setting profile
        database.getReference().child("User").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users Users =snapshot.getValue(users.class);
                Picasso.get().load(Users.getProfilePic())
                        .placeholder(R.drawable.user).into(binding.profile);
                binding.etUserName.setText(Users.getUserName());
                binding.etStatus.setText(Users.getStatus());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // on clicking save Button
        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=binding.etUserName.getText().toString();
                String status=binding.etStatus.getText().toString();

                //changing and uploading data in database
                HashMap <String ,Object> obj=new HashMap<>();
                obj.put("userName",userName);
                obj.put("status",status);
                database.getReference().child("User").child(FirebaseAuth.getInstance().getUid()).updateChildren(obj);
                Toast.makeText(SettingActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

            }
        });


        //on clicking plus icon on profile to add profile pic
        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");   // */* for all type of data
                startActivityForResult(intent, 33);
            }
        });

        //on clicking privacy,help and other button

        binding.privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desplayData obj=new desplayData();
                String content =obj.privacy;
                Intent intent=new Intent(SettingActivity.this,PersonalAndAppDetail.class);

                intent.putExtra("content",content);
                startActivity(intent);
            }
        });
        binding.inviteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desplayData obj=new desplayData();
                String content=obj.inviteMember;
                Intent intent=new Intent(SettingActivity.this,PersonalAndAppDetail.class);
                intent.putExtra("content",content);
                startActivity(intent);
            }
        });
        binding.aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desplayData obj=new desplayData();
                String content=obj.AboutUs;
                Intent intent=new Intent(SettingActivity.this,PersonalAndAppDetail.class);
                intent.putExtra("content",content);
                startActivity(intent);
            }
        });
        binding.help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desplayData obj=new desplayData();
                String content= obj.Help;
                Intent intent=new Intent(SettingActivity.this,PersonalAndAppDetail.class);
                intent.putExtra("content",content);
                startActivity(intent);
            }
        });
        binding.specialThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desplayData obj=new desplayData();
                String content=obj.SpecialThank;
                Intent intent=new Intent(SettingActivity.this,PersonalAndAppDetail.class);
                intent.putExtra("content",content);
                startActivity(intent);
            }
        });

    }

    //uploading picture in cloud storage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            Uri sFile = data.getData();
            binding.profile.setImageURI(sFile);
            final StorageReference reference = storage.getReference().child("Profile_Pics")
                    .child(FirebaseAuth.getInstance().getUid());
            //progress dialog
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Profile Picture");
            progressDialog.setMessage("Wait For A Moment");
            progressDialog.show();
            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("profilePic").setValue(uri.toString());
                            Toast.makeText(SettingActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            });


        }

    }

}