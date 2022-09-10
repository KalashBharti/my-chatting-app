package com.kalash.mywhatsapplite.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.kalash.mywhatsapplite.R;
import com.kalash.mywhatsapplite.model.MessagesModel;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{
     protected String time(long t) {
        Timestamp timestamp = new Timestamp(t);
        DateFormat dateFormat2 = new SimpleDateFormat("h.mm aa");
        String dateString2 = dateFormat2.format(timestamp).toString().toUpperCase();
        return dateString2;
    }
    ArrayList<MessagesModel> messagesModels;
    Context context;
    String recId;
    int SENDER_VIEW_TYPE =1;
    int RECEIVER_VIEW_TYPE=2;

    public ChatAdapter(ArrayList<MessagesModel> messagesModels, Context context) {
        this.messagesModels = messagesModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessagesModel> messagesModels, Context context, String recId) {
        this.messagesModels = messagesModels;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       //identifying sender and put sample sender bubble chat
        if(viewType == SENDER_VIEW_TYPE)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
        return new  SenderViewHolder(view);
        }
        //identifying sender and put sample receiver bubble chat
        else {
            View view= LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // in if case we assuring that the account logged in considered as sender
        if(messagesModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())) {

            return  SENDER_VIEW_TYPE;
        }
    else
        {
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
MessagesModel messagesModel=messagesModels.get(position);

//to delete the specific message and creating Alert Dialog
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context).setTitle("Delete")
                        .setMessage("Are You Sure You Want To Delete This Message ?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override //by clicking yes
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        String SenderRoom=FirebaseAuth.getInstance().getUid()+ recId;
                        database.getReference().child("chats").child(SenderRoom)
                                .child(messagesModel.getMeesageId())
                                .setValue(null);
                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override //by clicking no
                    public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    }
                }).show();
                return false;
            }
        });

//message showing in recycler view
if(holder.getClass()==SenderViewHolder.class)
{
    ((SenderViewHolder)holder).senderMessage.setText(messagesModel.getMessage());  //showing message in chat bubble recycler view
    ((SenderViewHolder)holder).senderTime.setText(time(messagesModel.getTimestamp()));  //showing message time in bubble recycler view
    Log.d("m1", messagesModel.getMessage());
}
else
{
    ((ReceiverViewHolder)holder).receiverMessage.setText(messagesModel.getMessage());//showing message in chat bubble recycler view
    ((ReceiverViewHolder)holder).receiverTime.setText(time(messagesModel.getTimestamp()));//showing message time in chat bubble recycler view
}
    }

    @Override
    public int getItemCount() {
        return messagesModels.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView receiverMessage,receiverTime;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMessage=itemView.findViewById(R.id.receiverText);
            receiverTime=itemView.findViewById(R.id.receiverTime);
        }
    }

    public class SenderViewHolder extends  RecyclerView.ViewHolder {
        TextView senderMessage,senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage=itemView.findViewById(R.id.senderText);
            senderTime=itemView.findViewById(R.id.senderTime);

        }
    }


}
