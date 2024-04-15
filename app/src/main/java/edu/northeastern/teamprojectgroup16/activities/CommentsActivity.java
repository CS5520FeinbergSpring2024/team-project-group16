package edu.northeastern.teamprojectgroup16.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.teamprojectgroup16.R;
import edu.northeastern.teamprojectgroup16.adapters.CommentAdapter;
import edu.northeastern.teamprojectgroup16.model.Comment;
import edu.northeastern.teamprojectgroup16.utilites.DateFormatter;
import edu.northeastern.teamprojectgroup16.utilites.KeyboardManager;

public class CommentsActivity extends AppCompatActivity {
    RecyclerView recyclerViewComments;
    EditText commentText;
    ImageView sendButton, closeButton;
    CommentAdapter commentAdapter;
    List<Comment> comments;
    DatabaseReference commentsReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        recyclerViewComments = findViewById(R.id.recyclerview_comments);
        commentText = findViewById(R.id.et_comment);
        sendButton = findViewById(R.id.btn_send_message);
        closeButton = findViewById(R.id.btn_close);

        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, comments);
        recyclerViewComments.setHasFixedSize(true);
        recyclerViewComments.setAdapter(commentAdapter);

        String postId = getIntent().getExtras().getString("postId");
        assert postId != null;
        commentsReference = FirebaseDatabase.getInstance().getReference()
                .child("comments");

        readComments(); // read comments

        boolean openKeyboard = getIntent().getExtras().getBoolean("openKeyboard");
        if (openKeyboard) {
            commentText.requestFocus();
            KeyboardManager.openKeyboard(this);
        }

        // send comments
        sendButton.setOnClickListener(v -> {
            String commentString = commentText.getText().toString();
            if (commentString.isEmpty()) return;

            // currentID
            String userId = FirebaseAuth.getInstance().getUid();
            if (userId == null) return; // make sure log in

            String commentId = DateFormatter.getCurrentTime();
            Comment comment = new Comment(userId, commentString, commentId);

            KeyboardManager.closeKeyboard(this);
            commentText.clearFocus();
            commentText.setText("");

            // add comments
            commentsReference.child(commentId).setValue(comment)
                    .addOnSuccessListener(unused -> {
                        comments.add(comment);
                        commentAdapter.notifyDataSetChanged();
            }).addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Failed to send comment", Toast.LENGTH_SHORT).show();
                    });
        });

        closeButton.setOnClickListener(view -> finish());

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focused = getCurrentFocus();
        if (focused != null) {
            KeyboardManager.closeKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void readComments() {
        commentsReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                comments.clear();
                for (DataSnapshot commentSnapshot : task.getResult().getChildren()) {
                    comments.add(commentSnapshot.getValue(Comment.class));
                }
                commentAdapter.notifyDataSetChanged();
            }
        });
    }


}
