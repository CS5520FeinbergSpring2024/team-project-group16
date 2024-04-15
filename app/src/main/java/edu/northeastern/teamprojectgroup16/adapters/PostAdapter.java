package edu.northeastern.teamprojectgroup16.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.northeastern.teamprojectgroup16.R;
import edu.northeastern.teamprojectgroup16.model.PostModel;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<PostModel> postList;
    FirebaseFirestore db;
    DocumentReference userReference;
    private static Context context;

    public PostAdapter(List<PostModel> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_todo, parent, false);
        db = FirebaseFirestore.getInstance();
        userReference = db.collection("users").document(FirebaseAuth.getInstance().getUid());
        PostViewHolder.userReference = userReference;
        return new PostViewHolder(view);
    }

    // helper function to format timestamp
    private String formatDate(Date timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(timestamp);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostModel postModel = postList.get(position);
        if (postModel.getLikes() == null) {
            postModel.setLikes(new ArrayList<>());
        }
        holder.post = postModel;
        Log.e("PostAdapter", postModel.toString());
        Log.e("Post Adapter: title", postModel.getTitle());
//        holder.userName.setText(postModel.getUserName());
        holder.textName.setText(postModel.getTitle());
        Glide.with(holder.imageView.getContext())
                .load(postModel.getImageUrl())
                .into(holder.imageView);
        // TODO

        holder.checkIfLiked();
        holder.checkIfSaved();

        // like count
        int count = postModel.getLikeCount();
        if (count == 0) {
            holder.likeCount.setVisibility(View.INVISIBLE);
        } else if (count == 1) {
            holder.likeCount.setText(String.format("%d %s", count, "like"));
        } else {
            holder.likeCount.setText(count + " likes");
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setPostList(ArrayList<PostModel> list) {
        this.postList = list;
        notifyDataSetChanged();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView textName;
        TextView userName;
        ImageView imageView;
        ImageButton likeButton;
        ImageButton filledHeartButton; // liked post
        ImageButton saveBtn;
        ImageButton repostButton;
        TextView likeCount;
        boolean isLiked = false; // track if it's liked
        boolean isSaved = false; // track if it's saved
        public static DocumentReference userReference;
        public PostModel post;
        private String userId;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            saveBtn = itemView.findViewById(R.id.starButton);
            profileImage = itemView.findViewById(R.id.profileImage);
            userName = itemView.findViewById(R.id.username);
            textName = itemView.findViewById(R.id.TextName);
            imageView = itemView.findViewById(R.id.imageView);
            likeButton = itemView.findViewById(R.id.likeButton);
            filledHeartButton = itemView.findViewById(R.id.likeButton2);
            repostButton = itemView.findViewById(R.id.repostButton);
            likeCount = itemView.findViewById(R.id.textLikeCount);

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            userId = firebaseUser.getUid();
            
            setupLikeButton();
            setupSaveButton();
        }

        private void setupSaveButton() {
            saveBtn.setOnClickListener(v -> toggleSave());
        }

        private void toggleSave() {
            isSaved = !isSaved;
            updateSavedData();
        }

        /**
         * Check if it's liked
         */
        private void checkIfLiked() {
            isLiked = post.getLikes().contains(userReference);
            update();
        }



        private void setupLikeButton() {
            likeButton.setOnClickListener(v -> toggleLike());
            filledHeartButton.setOnClickListener(v -> toggleLike());
        }

        private void toggleLike() {
            isLiked = !isLiked;
            updateLikeUI();
            updateLikesData();
        }

        private void updateLikeUI() {
            if (isLiked) {
                likeButton.setVisibility(View.GONE);
                filledHeartButton.setVisibility(View.VISIBLE);
            } else {
                likeButton.setVisibility(View.VISIBLE);
                filledHeartButton.setVisibility(View.GONE);
            }
        }

        public void checkIfSaved() {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            DatabaseReference savedRef = userRef.child("savedPosts");
            savedRef.orderByValue().equalTo(post.getPostId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            isSaved = dataSnapshot.exists();
                            update();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("FirebaseDB Error", "Error checking if post is saved", databaseError.toException());
                        }
                    });

        }

        private void updateSavedData() {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            if (isSaved) {
                DatabaseReference savedRef = userRef.child("savedPosts").push();
                savedRef.setValue(post.getPostId())
                        .addOnSuccessListener(aVoid -> {
                            Log.d("FirebaseDB", "Post ID added to savedPosts successfully");
                            Toast.makeText(context.getApplicationContext(), "Post saved successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("FirebaseDB Error", "Failed to add post ID to savedPosts", e);
                            Toast.makeText(context, "Failed to save post", Toast.LENGTH_SHORT).show();
                        });
            } else {
                userRef.child("savedPosts").orderByValue().equalTo(post.getPostId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                    childSnapshot.getRef().removeValue()  // 移除该 postId
                                            .addOnSuccessListener(aVoid -> Log.d("FirebaseDB", "Post ID removed from savedPosts successfully"))
                                            .addOnFailureListener(e -> Log.e("FirebaseDB Error", "Failed to remove post ID from savedPosts", e));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("FirebaseDB Error", "Error fetching savedPosts", databaseError.toException());
                            }
                        });
            }
            update();
        }

        /**
         * Update the count of likes
         */
        void updateLikesData() {
            DocumentReference postReference = FirebaseFirestore.getInstance().collection("posts").document(post.getPostId());
            if (isLiked) {
                postReference.update("likes", FieldValue.arrayUnion(userReference));
                Log.e("PostReference", post.toString());
                post.addLike(userReference);
            } else {
                postReference.update("likes", FieldValue.arrayRemove(userReference));
                post.removeLike(userReference);
            }
            updateLikeUI();
            update();
        }

        private void update() {
            int n = post.getLikes().size();
            if (n > 0) {
                likeCount.setVisibility(View.VISIBLE);
                String str = n + (n > 1 ? " likes" : " like");
                likeCount.setText(str);
            } else {
                likeCount.setVisibility(View.GONE);
            }

            if (isSaved) saveBtn.setImageResource(R.drawable.ic_save);
            else saveBtn.setImageResource(R.drawable.ic_save_outlined);
        }


    }
}
