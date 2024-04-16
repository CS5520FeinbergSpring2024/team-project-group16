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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.teamprojectgroup16.R;
import edu.northeastern.teamprojectgroup16.adapters.PostAdapter;
import edu.northeastern.teamprojectgroup16.model.PostModel;


public class PostRecFragment extends Fragment {
    private static final String ARG_PARAM = "param1";
    private RecyclerView postRecyclerView;
    private PostAdapter postAdapter;
    private List<PostModel> postRecList;
    private DatabaseReference postsRef;

    private String serverID = null;


    public PostRecFragment() {
        // Required empty public constructor
    }

    public static PostRecFragment withServerIDPostRecFragment(String serverID){
        PostRecFragment fragment = new PostRecFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, serverID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            serverID = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData(serverID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_rec, container, false);
        postRecyclerView = rootView.findViewById(R.id.postRecyclerView);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postRecyclerView.setHasFixedSize(true);

        postRecList = new ArrayList<>(); // Create a list of sample posts
        postAdapter = new PostAdapter(postRecList, getContext());
        postRecyclerView.setAdapter(postAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void fetchData(String serverID) {
        if (serverID != null){
            DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");
            Log.e("serverID ", serverID);
            postsRef.orderByChild("serverId").equalTo(serverID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    postRecList.clear();
                    Log.e("Status","get here");
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PostModel post = snapshot.getValue(PostModel.class);
                        Log.e("Status","get here");
                        if (post != null) {
                            Map<String, Boolean> likes = new HashMap<>();
                            for (DataSnapshot child : snapshot.child("likes").getChildren()) {
                                likes.put(child.getKey(), child.getValue(Boolean.class));
                            }
                            post.setLikes(likes);
                        }
                        postRecList.add(post);
                        assert post != null;
                        Log.d("Likes Info", "Post ID: " + post.getPostId() + " Likes: " + post.getLikes().size());

                        // Fetch username for the post
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(post.getUserId()).child("userName");
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // Get the value from the DataSnapshot
                                String userName = snapshot.getValue(String.class);

                                // Set the userName to the post
                                post.setUserName(userName);

                                // Log the username here to ensure it's fetched correctly
                                Log.d("User Name", post.getUserName());

                                // Notify the adapter that the data has changed
                                postAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle the error
                                Log.e("Database", "Error: ", error.toException());
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                    Log.e("PostRecFragment", "Database error: " + databaseError.getMessage());
                    Toast.makeText(getContext(), "Failed to load posts.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");
            postsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    postRecList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PostModel post = snapshot.getValue(PostModel.class);
                        if (post != null) {
                            Map<String, Boolean> likes = new HashMap<>();
                            for (DataSnapshot child : snapshot.child("likes").getChildren()) {
                                likes.put(child.getKey(), child.getValue(Boolean.class));
                            }
                            post.setLikes(likes);
                        }
                        postRecList.add(post);
                        assert post != null;
                        Log.d("Likes Info", "Post ID: " + post.getPostId() + " Likes: " + post.getLikes().size());

                        // Fetch username for the post
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(post.getUserId()).child("userName");
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // Get the value from the DataSnapshot
                                String userName = snapshot.getValue(String.class);

                                // Set the userName to the post
                                post.setUserName(userName);

                                // Log the username here to ensure it's fetched correctly
                                Log.d("User Name", post.getUserName());

                                // Notify the adapter that the data has changed
                                postAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle the error
                                Log.e("Database", "Error: ", error.toException());
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                    Log.e("PostRecFragment", "Database error: " + databaseError.getMessage());
                    Toast.makeText(getContext(), "Failed to load posts.", Toast.LENGTH_SHORT).show();
                }
            });
        }
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