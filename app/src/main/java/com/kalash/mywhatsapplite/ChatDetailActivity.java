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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kalash.mywhatsapplite.Adapter.ChatAdapter;
import com.kalash.mywhatsapplite.databinding.ActivityChatDetailBinding;
import com.kalash.mywhatsapplite.model.MessagesModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        getSupportActionBar().hide();
       final String senderId=auth.getUid();          //get user id from firebase authentication

        //taking data from userAdapter --  main class  (tap on profile>--> data to--> chatDetail Activity
        String receiverId = getIntent().getStringExtra("userId"); //getting receiver id from userAdapter class
        String userName=getIntent().getStringExtra("userName");  //getting userName id from userAdapter class
        String profilePic=getIntent().getStringExtra("profilePic"); //getting profilePic id from userAdapter class

        //setting person profile data to chatDetail activity
        binding.userName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.user).into(binding.profileImage);

        //back button
        binding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        //call button
        binding.callbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatDetailActivity.this, "This Feature is not available in this moment", Toast.LENGTH_SHORT).show();
            }
        });

        //video call button
        binding.videoCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatDetailActivity.this, "This Feature is not available in this moment", Toast.LENGTH_SHORT).show();
            }
        });

        //setting chat text in chat recycler view
        final ArrayList<MessagesModel> messagesModels=new ArrayList<>();
        final ChatAdapter chatAdapter=new ChatAdapter(messagesModels,this,receiverId);
        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        final String senderRoom=senderId+receiverId;   //to create child root id to store chats
        final String receiverRoom=receiverId+senderId;

        // showing all the message in recycler view
        database.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messagesModels.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            MessagesModel model=snapshot1.getValue(MessagesModel.class);
                           messagesModels.add(model);
                            model.setMeesageId(snapshot1.getKey());
                        }
                        chatAdapter.notifyDataSetChanged();  //for sudden upload
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        //on clicking send button
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if etMessage is Empty
                if(binding.etMassege.getText().toString().isEmpty())
                {
                    binding.etMassege.setError("Block Must Not Be Empty");
                    return;
                }
                //sending edittext text to data base
                String message=binding.etMassege.getText().toString();
                final MessagesModel model=new MessagesModel(senderId,message);
                model.setTimestamp(new Date().getTime());

                //clear the edittext for new message
                binding.etMassege.setText("");

                database.getReference().child("chats").child(senderRoom).push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats").child(receiverRoom).push()
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
            }
        });


        //on clicking profile pic to open profile detail
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatDetailActivity.this,ProfileDetail.class);
                intent.putExtra("userId",receiverId);
                intent.putExtra("profilePic",profilePic);
                intent.putExtra("userName",userName);

                startActivity(intent);

            }
        });
    }

}