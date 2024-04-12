package edu.northeastern.teamprojectgroup16.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.teamprojectgroup16.R;
import edu.northeastern.teamprojectgroup16.adapters.PostRecAdapter;
import edu.northeastern.teamprojectgroup16.model.PostModel;


public class PostRecFragment extends Fragment {


    private RecyclerView postRecyclerView;
    private PostRecAdapter postAdapter;
    private List<PostModel> postRecList;


    public PostRecFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_rec, container, false);
        postRecyclerView = rootView.findViewById(R.id.postRecyclerView);
        postRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        postRecList = new ArrayList<>(); // Create a list of sample posts
        postAdapter = new PostRecAdapter(postRecList, requireContext());
        postRecyclerView.setAdapter(postAdapter);

        FirebaseDatabase.getInstance().getReference("Posts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                    }
                });





        // Inflate the layout for this fragment
        return rootView;
    }

    public void addPostRec(PostModel postModel) {
        this.postRecList.add(postModel);
        postAdapter.notifyAll();
    }


    public void deletePostRec(int index){
        this.postRecList.remove(index);
        postAdapter.notifyItemRemoved(index);
    }
}