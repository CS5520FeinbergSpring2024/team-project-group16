package edu.northeastern.teamprojectgroup16.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.northeastern.teamprojectgroup16.R;
import edu.northeastern.teamprojectgroup16.adapters.MessageAdapter;
import edu.northeastern.teamprojectgroup16.adapters.UsersAdapter;
import edu.northeastern.teamprojectgroup16.model.Comment;
import edu.northeastern.teamprojectgroup16.model.Message;
import edu.northeastern.teamprojectgroup16.utilites.DateFormatter;
import edu.northeastern.teamprojectgroup16.utilites.KeyboardManager;

public class ChatActivity extends AppCompatActivity {
    String receiverId, receiverName, senderRoom, receiverRoom;
    String senderId, senderName;
    DatabaseReference dbReferenceSender, dbReferenceReceiver, userReference;
    ImageView sendBtn;
    EditText messageText;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.chatrecycler);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sendBtn = findViewById(R.id.sendMessageIcon);
        messageAdapter = new MessageAdapter(this);


        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        messageText = findViewById(R.id.messageEdit);


        userReference = FirebaseDatabase.getInstance().getReference("users");
        receiverId = getIntent().getStringExtra("id");
        receiverName = getIntent().getStringExtra("name");

        getSupportActionBar().setTitle(receiverName);
        if(receiverId != null) {
            senderRoom = FirebaseAuth.getInstance().getUid() + receiverId;
            receiverRoom = receiverId + FirebaseAuth.getInstance().getUid();

        }


        dbReferenceSender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        dbReferenceReceiver = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);

        dbReferenceSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Message> messages = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messages.add(message);
                }
                messageAdapter.clear();
                for(Message message : messages) {
                    messageAdapter.add(message);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageText.getText().toString();
                if(message.trim().length() > 0) {
                    SendMessage(message);
                    Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChatActivity.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SendMessage(String message) {


        // currentID
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) return; // make sure log in

        String messageId = DateFormatter.getCurrentTime();
        Message messageModel = new Message(messageId, FirebaseAuth.getInstance().getUid(), message);

        KeyboardManager.closeKeyboard(this);
        messageText.clearFocus();
        messageText.setText("");

        // add comments
        dbReferenceSender.child(messageId).setValue(messageModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        messageAdapter.notifyDataSetChanged();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatActivity.this, "Failed to send the message.", Toast.LENGTH_SHORT).show();

                    }
                });
        dbReferenceReceiver.child(messageId).setValue(messageModel);
        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);

        messageText.setText("");


//
//        String messageId = UUID.randomUUID().toString();
//        Message messageModel = new Message(messageId, FirebaseAuth.getInstance().getUid(), message);
//        messageAdapter.add(messageModel);
//
//        dbReferenceSender.child(messageId).setValue(messageModel)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        messageAdapter.notifyDataSetChanged();
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(ChatActivity.this, "Failed to send the message.", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//        dbReferenceReceiver.child(messageId).setValue(messageModel);
//        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
//
//        messageText.setText("");
    }


}