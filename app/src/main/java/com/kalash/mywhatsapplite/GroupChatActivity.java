package com.kalash.mywhatsapplite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kalash.mywhatsapplite.Adapter.ChatAdapter;
import com.kalash.mywhatsapplite.Adapter.groupChatAdapter;
import com.kalash.mywhatsapplite.databinding.ActivityGroupChatBinding;
import com.kalash.mywhatsapplite.model.MessagesModel;
import com.kalash.mywhatsapplite.model.groupMessageModel;
import com.kalash.mywhatsapplite.model.users;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {
    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        //back button
        binding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        //call button
        binding.callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GroupChatActivity.this, "This Feature is not available in this moment", Toast.LENGTH_SHORT).show();
            }
        });

        //video call button
        binding.videoCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GroupChatActivity.this, "This Feature is not available in this moment", Toast.LENGTH_SHORT).show();
            }
        });

        //chatting section

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<groupMessageModel> messagesModels = new ArrayList<>();

        final groupChatAdapter adapter = new groupChatAdapter(messagesModels, this);
        binding.chatRecyclerView.setAdapter(adapter);
        //ID of sender
        final String senderId = FirebaseAuth.getInstance().getUid();
        binding.userName.setText(R.string.groupName);
        binding.profileImage.setImageResource(R.drawable.groupicon);
        //working on Layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);


        //getting data from database
        database.getReference().child("Group Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesModels.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    groupMessageModel model=dataSnapshot.getValue(groupMessageModel.class);
                    messagesModels.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //taking name of the sender
        final String[] senderName = new String[1];
        database.getReference().child("User").child(FirebaseAuth.getInstance().getUid().toString()).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderName[0] =snapshot.getValue().toString();
                Log.d("senderN", senderName[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //on clicking send button --> sending message to database
        binding.send.setOnClickListener(new View.OnClickListener() {
            users User;
            @Override
            public void onClick(View view) {
                //if etMessage is empty
                if(binding.etMassege.getText().toString().isEmpty())
                {
                    binding.etMassege.setError("Block Must Not Be Empty");
                    return;
                }

                final String message = binding.etMassege.getText().toString();
                final groupMessageModel model = new groupMessageModel(senderId, message,senderName[0]);
                model.setTimestamp(new Date().getTime());
                binding.etMassege.setText("");
              //  Toast.makeText(GroupChatActivity.this, User.getUserName(), Toast.LENGTH_SHORT).show();

                //Storing data in database
                database.getReference().child("Group Chat").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
            }
        });


    }
}