package com.kalash.mywhatsapplite.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kalash.mywhatsapplite.Adapter.userAdapter;
import com.kalash.mywhatsapplite.R;
import com.kalash.mywhatsapplite.Sign_In;
import com.kalash.mywhatsapplite.Sing_Up;
import com.kalash.mywhatsapplite.databinding.FragmentChatBinding;
import com.kalash.mywhatsapplite.model.users;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    public ChatFragment() {
        // Required empty public constructor
    }
    FragmentChatBinding binding ;
    ArrayList<users> list=new ArrayList<>();
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentChatBinding.inflate(inflater, container, false);
        // chat sections
        userAdapter adapter=new userAdapter(list,getContext());
         binding.chatRecyclerView.setAdapter(adapter);
        database=FirebaseDatabase.getInstance();
        users user=new users();
        String myId=user.getUserId();
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(manager);

        // getting data from the firebase

        database.getReference().child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    users Users =dataSnapshot.getValue(users.class);
//                    assert Users != null;
                    Users.setUserId(dataSnapshot.getKey());
                   // Log.d("uid", FirebaseAuth.getInstance().getUid());
                    Log.d("uid",dataSnapshot.getKey());
                        if(!Users.getUserId().equals(FirebaseAuth.getInstance().getUid())) //dataSnapshot.getKey() can be used in place of Users.getUserId
                        {
                            list.add(Users);
                        }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }
}