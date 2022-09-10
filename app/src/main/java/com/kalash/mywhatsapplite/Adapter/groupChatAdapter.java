package com.kalash.mywhatsapplite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.kalash.mywhatsapplite.R;
import com.kalash.mywhatsapplite.model.MessagesModel;
import com.kalash.mywhatsapplite.model.groupMessageModel;

import java.util.ArrayList;

public class groupChatAdapter extends RecyclerView.Adapter {

    ArrayList<groupMessageModel> messagesModels;
    Context context;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public groupChatAdapter(ArrayList<groupMessageModel> messagesModels, Context context) {
        this.messagesModels = messagesModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //identifying sender and put sample sender bubble chat
        if (viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new groupChatAdapter.GroupSenderViewHolder(view);
        }
        //identifying sender and put sample receiver bubble chat
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_group_receiver, parent, false);
            return new groupChatAdapter.GroupReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // in if case we assuring that the account logged in considered as sender
        if (messagesModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())) {

            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        groupMessageModel messagesModel = messagesModels.get(position);
        if (holder.getClass() == GroupSenderViewHolder.class) {
            ((GroupSenderViewHolder) holder).senderMessage.setText(messagesModel.getMessage());
        }
        else
        {
            ((GroupReceiverViewHolder)holder).receiverMessage.setText(messagesModel.getMessage());
           ((GroupReceiverViewHolder)holder).name.setText(messagesModel.getName());
        }
    }

    @Override
    public int getItemCount() {
        return messagesModels.size();
    }

    public class GroupReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMessage, receiverTime, name;

        public GroupReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMessage = itemView.findViewById(R.id.GrpreceiverText);
            receiverTime = itemView.findViewById(R.id.receiverText);
            name = itemView.findViewById(R.id.candidateName);
        }
    }

    public class GroupSenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMessage, senderTime;

        public GroupSenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);

        }
    }

}
