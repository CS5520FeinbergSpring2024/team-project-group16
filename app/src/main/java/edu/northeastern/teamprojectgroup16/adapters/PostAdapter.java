package edu.northeastern.teamprojectgroup16.adapters;

import android.content.Context;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.northeastern.teamprojectgroup16.R;
import edu.northeastern.teamprojectgroup16.activities.CommentsActivity;
import edu.northeastern.teamprojectgroup16.model.PostModel;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<PostModel> postList;
    DatabaseReference userReference;
    private static Context context;

    public PostAdapter(List<PostModel> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_todo, parent, false);
        userReference = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getUid());
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
        // TODO: ?
        holder.post = postModel;

        Log.e("PostAdapter", postModel.toString());
        Log.e("Post Adapter: title", postModel.getTitle());

        if(postModel.getUserName() != null) {
            Log.d("Post User Name", postModel.getUserName());
            holder.userName.setText(postModel.getUserName());
        }
        //holder.userName.setText(" ");

        // Comment function
        holder.commentButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, CommentsActivity.class);
            intent.putExtra("postId", postModel.getPostId());
            intent.putExtra("openKeyboard", false);
            context.startActivity(intent);
        });

        holder.textName.setText(postModel.getTitle());
        Glide.with(holder.imageView.getContext())
                .load(postModel.getImageUrl())
                .into(holder.imageView);

        holder.checkIfLiked();
        holder.checkIfSaved();

        int likeCount = (postModel.getLikes() != null) ? postModel.getLikes().size() : 0;
        if (likeCount == 0) {
            holder.likeCount.setVisibility(View.INVISIBLE);
        } else {
            holder.likeCount.setVisibility(View.VISIBLE);
            holder.likeCount
                    .setText(String.format("%d %s", likeCount, likeCount > 1 ? "likes" : "like"));
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
        ImageButton commentButton; // comment
        ImageButton filledHeartButton; // liked post
        ImageButton saveBtn;
        ImageButton repostButton;
        TextView likeCount;
        boolean isLiked = false; // track if it's liked
        boolean isSaved = false; // track if it's saved
        public static DatabaseReference userReference;
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
            userName = itemView.findViewById(R.id.UserName);
            commentButton = itemView.findViewById(R.id.commentButton); // comment btn

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
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && post.getLikes() != null) {
                isLiked = post.getLikes().containsKey(user.getUid()); // Check if user's ID is a key in the likes map
                updateLikeUI();
                update();
            }

        }

        private void setupLikeButton() {
            likeButton.setOnClickListener(v -> toggleLike());
            filledHeartButton.setOnClickListener(v -> toggleLike());
        }

        private void toggleLike() {
            isLiked = !isLiked;
            updateLikesData();
            updateLikeUI();
        }

        private void updateLikesData() {
            DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("posts").child(post.getPostId());
            if (isLiked) {
                postReference.child("likes").child(userReference.getKey()).setValue(true)
                        .addOnSuccessListener(aVoid -> {
                            post.addLike(userReference.getKey());  // 确保添加点赞者的ID
                            update();
                        });
            } else {
                postReference.child("likes").child(userReference.getKey()).removeValue()
                        .addOnSuccessListener(aVoid -> {
                            post.removeLike(userReference.getKey());  // 确保移除点赞者的ID
                            update();
                        });
            }
        }

        private void updateLikeUI() {
            if (isLiked) {
                likeButton.setVisibility(View.GONE);
                filledHeartButton.setVisibility(View.VISIBLE);
            } else {
                likeButton.setVisibility(View.VISIBLE);
                filledHeartButton.setVisibility(View.GONE);
            }
            int count = post.getLikeCount();
            if (count == 0) {
                likeCount.setVisibility(View.INVISIBLE);
            } else if (count == 1) {
                likeCount.setText(String.format("%d %s", count, "like"));
            } else {
                likeCount.setText(count + " likes");
            }
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
    }
}
