package edu.northeastern.teamprojectgroup16.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
        String userId = firebaseUser.getUid();

        DocumentReference userReference = FirebaseFirestore.getInstance().document("users/" + FirebaseAuth.getInstance().getUid());
        userReference.get().addOnSuccessListener(userSnapshot -> {
            UserModel userModel = userSnapshot.toObject(UserModel.class);
            if (userModel != null && userModel.getSaved() != null) {
                List<DocumentReference> savedPosts = userModel.getSaved();
                if (savedPosts.isEmpty()) {
                    Toast.makeText(FavoriteActivity.this, "No saved posts", Toast.LENGTH_SHORT).show();
                } else {
                    for (DocumentReference savedPostReference : savedPosts) {
                        savedPostReference.get().addOnSuccessListener(savedPostSnapshot -> {
                            PostModel post = savedPostSnapshot.toObject(PostModel.class);
                            if (post != null) {
                                favoriteItemAdapter.addPost(post);
                            }
                            // Notify the adapter only once all posts are added
                            if (favoriteItemAdapter.getItemCount() == savedPosts.size()) {
                                favoriteItemAdapter.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(e -> Log.e("FavoriteActivity", "Error loading saved post", e));
                    }
                }
            } else {
                Toast.makeText(FavoriteActivity.this, "Failed to load user data or no saved posts", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> Log.e("FavoriteActivity", "Error fetching user document", e));
    }
}
