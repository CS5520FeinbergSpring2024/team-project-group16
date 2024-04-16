package edu.northeastern.teamprojectgroup16.adapters;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.teamprojectgroup16.R;
import edu.northeastern.teamprojectgroup16.fragments.MessageFragment;
import edu.northeastern.teamprojectgroup16.model.Message;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECIEVE = 2;
    private Context context;
    private List<Message> messsageList;

    public MessageAdapter(Context context) {
        this.context = context;
        this.messsageList = new ArrayList<>();
    }

    public void add(Message message) {
        messsageList.add(message);
        notifyDataSetChanged();
    }

    public void clear() {
        messsageList.clear();
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = inflater.inflate(R.layout.message_row_sent, parent, false);
        } else {
            view = inflater.inflate(R.layout.message_row_received, parent, false);
        }
        return new MessageAdapter.MyViewHolder(view);

    }

   @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {

        Message message = messsageList.get(position);
        if(message.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            holder.textViewSentMessage.setText(message.getMessage());
        } else {
            holder.textViewReceiveMessage.setText(message.getMessage());
        }
    }




    @Override
    public int getItemCount() {
        return messsageList.size();
    }

    public List<Message> getMesssageList() {
        return messsageList;
    }

    @Override
    public int getItemViewType(int position) {
        if(messsageList.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECIEVE;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewSentMessage, textViewReceiveMessage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSentMessage = itemView.findViewById(R.id.textViewSentMessage);
            textViewReceiveMessage = itemView.findViewById(R.id.textViewReceivedMessage);
        }
    }
}
