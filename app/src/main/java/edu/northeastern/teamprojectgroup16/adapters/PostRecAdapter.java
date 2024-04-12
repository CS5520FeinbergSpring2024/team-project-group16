package edu.northeastern.teamprojectgroup16.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import edu.northeastern.teamprojectgroup16.R;
import edu.northeastern.teamprojectgroup16.model.PostModel;

public class PostRecAdapter extends RecyclerView.Adapter<PostRecAdapter.PostViewHolder> {
    private List<PostModel> postList;
    private Context context;
    ImageButton favorite_btn;
    DatabaseReference favoriteRef;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    Boolean favoriteChecker = false;
    DatabaseReference favorite_list_ref;
    PostModel post;

    public PostRecAdapter(List<PostModel> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new PostViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String currentUserId = user.getUid();
        PostModel post = postList.get(position);
        String title = post.getTitle();
        String postId = post.getPostId();
        String serverId = post.getServerId();
        String text = post.getText();
        int likeCount = post.getLikeCount();
        String imageUrl = post.getImageUrl();
        String comment = post.getComment();
        Date time = post.getTimestamp();

        holder.favoriteChecker(postId); // self-defined method below

        // click on the star button
        holder.starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteChecker = true;
                favoriteRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (favoriteChecker.equals(true)) {
                            if (snapshot.child(postId).hasChild(currentUserId)) {
                                favoriteRef.child(postId).child(currentUserId).removeValue();
                                deleteFromDatabase(time); // ?
                                favoriteChecker = false;
                            } else {
                                favoriteRef.child(postId).child(currentUserId).setValue(true);
                                post.setTitle(title);
                                post.setTimestamp(time);
                                post.setComment(comment);
                                post.setImageUrl(imageUrl);
                                post.setServerId(serverId);
                                post.setText(text);
                                post.setLikeCount(likeCount);

                                String id = favorite_list_ref.push().getKey(); //??
                                favorite_list_ref.child(id).setValue(post);
                                favoriteChecker = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        Glide.with(context).load(post.getImageUrl()).into(holder.postImageView);
        holder.titleTextView.setText(post.getTitle());
    }

    private void deleteFromDatabase(Date time) {
        Query query = favorite_list_ref.orderByChild("time").equalTo(String.valueOf(time));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.getRef().removeValue();
                    Toast.makeText(context.getApplicationContext(),
                            "Deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView postImageView;
        ImageButton starButton;
        TextView titleTextView;
        String currentUserId;

        DatabaseReference favorite_ref, favorite_list_ref;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            // Favorite function
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            currentUserId = user.getUid();
            favorite_ref = FirebaseDatabase.getInstance().getReference("favourites");
            favorite_list_ref = FirebaseDatabase.getInstance().getReference("favouriteList").child(currentUserId);
            postImageView = itemView.findViewById(R.id.postImageView);
            starButton = itemView.findViewById(R.id.starButton);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }

        public void favoriteChecker(String postId) {
            favorite_ref = FirebaseDatabase.getInstance().getReference("favourites");
            favorite_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(postId).hasChild(currentUserId)) {
                        starButton.setImageResource(R.drawable.ic_star_filled);
                    } else {
                        starButton.setImageResource(R.drawable.ic_star);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
