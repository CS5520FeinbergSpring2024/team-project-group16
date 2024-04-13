package edu.northeastern.teamprojectgroup16;

import android.os.Bundle;

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

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.teamprojectgroup16.adapters.PostAdapter;
import edu.northeastern.teamprojectgroup16.model.PostModel;

/**
 * Save all favorite items.
 */
public class FavoriteActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PostAdapter postAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    private List<PostModel> postList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // set recyclerview
        recyclerView = findViewById(R.id.rv_favorite);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // set firebase reference
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String currentUserId = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference("favouriteList").child(currentUserId);

        // set adapter
        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);
        loadData();
    }

    private void loadData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();  // Clear the old list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PostModel post = snapshot.getValue(PostModel.class);
                    postList.add(post);
                }
                postAdapter.notifyDataSetChanged();  // Notify the adapter that data has changed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

}
