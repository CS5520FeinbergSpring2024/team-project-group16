package edu.northeastern.teamprojectgroup16.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

import edu.northeastern.teamprojectgroup16.R;
import edu.northeastern.teamprojectgroup16.model.Comment;
import edu.northeastern.teamprojectgroup16.model.UserModel;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);

        holder.comment.setText(comment.getText()); // set the comment text

        String id = comment.getPublisherId(); // publisherID

        // TODO: show the profile
        if (id != null) {
            loadImageFromDatabase(id, holder.profileImage);
        }

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        DatabaseReference userRef = usersRef.child(id).child("userName");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get the value from the DataSnapshot
                String userName = snapshot.getValue(String.class);

                // Set the userName to the post
                holder.username.setText(userName);

                // Log the username here to ensure it's fetched correctly
                Log.d("User Name", userName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
                Log.e("Database", "Error: ", error.toException());
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    private void loadImageFromDatabase(String userId, ImageView profileImage) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId).child("imageUrl");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imageUrl = snapshot.getValue(String.class);
                if (imageUrl != null) {
                    Glide.with(context).load(imageUrl).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("PostAdapter", "Failed to read image URL.", error.toException());
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView username, comment;
        View container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            profileImage = itemView.findViewById(R.id.img_profile);
            username = itemView.findViewById(R.id.txt_username);
            comment = itemView.findViewById(R.id.txt_comment);
        }
    }
}
