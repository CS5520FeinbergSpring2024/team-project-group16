package edu.northeastern.teamprojectgroup16.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.teamprojectgroup16.R;
import edu.northeastern.teamprojectgroup16.adapters.FavoriteItemAdapter;
import edu.northeastern.teamprojectgroup16.model.PostModel;
import edu.northeastern.teamprojectgroup16.model.UserModel;

/**
 * Save all favorite items.
 */
public class FavoriteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FavoriteItemAdapter favoriteItemAdapter;
    private List<PostModel> postList;
    ImageView closeButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        closeButton = findViewById(R.id.btn_close);
        closeButton.setOnClickListener(view -> finish());

        // set recyclerview
        recyclerView = findViewById(R.id.recyclerview_saved_posts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        favoriteItemAdapter = new FavoriteItemAdapter(postList);
        recyclerView.setAdapter(favoriteItemAdapter);

        loadData();
    }

    private void loadData() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId).child("savedPosts");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> savedPostIds = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String postId = snapshot.getValue(String.class);  // Assuming savedPosts contains post IDs as strings
                        if (postId != null) {
                            savedPostIds.add(postId);
                        }
                    }

                    loadPosts(savedPostIds);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FavoriteActivity", "Error fetching saved posts", databaseError.toException());
                    Toast.makeText(FavoriteActivity.this, "Failed to load saved posts", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void loadPosts(List<String> postIds) {
        if (postIds.isEmpty()) {
            Toast.makeText(FavoriteActivity.this, "No saved posts", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");
        for (String postId : postIds) {
            postsRef.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    PostModel post = dataSnapshot.getValue(PostModel.class);
                    if (post != null) {
                        favoriteItemAdapter.addPost(post);
                    }
                    // Notify the adapter only once all posts are added
                    if (favoriteItemAdapter.getItemCount() == postIds.size()) {
                        favoriteItemAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FavoriteActivity", "Error loading post", databaseError.toException());
                }
            });
        }
    }
}
