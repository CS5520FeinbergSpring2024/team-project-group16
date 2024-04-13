package edu.northeastern.teamprojectgroup16.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.teamprojectgroup16.R;
import edu.northeastern.teamprojectgroup16.adapters.PostAdapter;
import edu.northeastern.teamprojectgroup16.model.PostModel;


public class PostRecFragment extends Fragment {

    private RecyclerView postRecyclerView;
    private PostAdapter postAdapter;
    private List<PostModel> postRecList;
    private DatabaseReference postsRef;


    public PostRecFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_rec, container, false);
        postRecyclerView = rootView.findViewById(R.id.postRecyclerView);
        postRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        postRecyclerView.setHasFixedSize(true);

        postRecList = new ArrayList<>(); // Create a list of sample posts
        postAdapter = new PostAdapter(postRecList);
        postRecyclerView.setAdapter(postAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void fetchData() {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Posts");
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postRecList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = snapshot.child("title").getValue(String.class);
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    // TODO: fetch other fields
                    PostModel post = new PostModel(null, title, imageUrl, null, null, null, 0, null, null);
                    postRecList.add(post);

                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("PostRecFragment", "Database error: " + databaseError.getMessage());
                Toast.makeText(getContext(), "Failed to load posts.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (postsRef != null) {
            postsRef.removeEventListener((ValueEventListener) this);
        }
    }

    public void addPostRec(PostModel postModel) {
        this.postRecList.add(postModel);
        postAdapter.notifyItemInserted(postRecList.size() - 1);
    }


    public void deletePostRec(int index){
        this.postRecList.remove(index);
        postAdapter.notifyItemRemoved(index);
    }
}