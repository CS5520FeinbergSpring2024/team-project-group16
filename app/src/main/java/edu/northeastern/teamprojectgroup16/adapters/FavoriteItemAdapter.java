package edu.northeastern.teamprojectgroup16.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.northeastern.teamprojectgroup16.R;
import edu.northeastern.teamprojectgroup16.model.PostModel;

public class FavoriteItemAdapter extends RecyclerView.Adapter<FavoriteItemAdapter.FavoriteViewHolder> {
    private List<PostModel> postList;
    private Context context;

    public FavoriteItemAdapter(List<PostModel> postList) {
        this.postList = postList;
    }

    public void addPost(PostModel post) {
        postList.add(post);
        notifyItemInserted(this.postList.size() - 1);
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        PostModel postModel = postList.get(position);
        Log.e("Favorite", postModel.toString());
        holder.textName.setText(postModel.getTitle());
        Glide.with(holder.imageView.getContext())
                .load(postModel.getImageUrl())
                .into(holder.imageView);
        String userId = postModel.getUserId();
        if (userId != null) {
            loadImageFromDatabase(userId, holder.profileImage);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    private void loadImageFromDatabase(String userId, ImageView profileImage) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId).child("imageUrl");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imageUrl = snapshot.getValue(String.class);
                if (imageUrl != null) {
                    Glide.with(profileImage.getContext()).load(imageUrl).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("PostAdapter", "Failed to read image URL.", error.toException());
            }
        });
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView textName;
        ImageView imageView;
        ImageButton likeButton;
        ImageButton saveBtn;
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            saveBtn = itemView.findViewById(R.id.starButton);
            profileImage = itemView.findViewById(R.id.profileImage);
            textName = itemView.findViewById(R.id.TextName);
            imageView = itemView.findViewById(R.id.imageView);
            likeButton = itemView.findViewById(R.id.likeButton);
        }
    }
}
