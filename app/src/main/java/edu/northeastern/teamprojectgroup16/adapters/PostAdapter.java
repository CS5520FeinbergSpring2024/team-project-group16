package edu.northeastern.teamprojectgroup16.adapters;

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
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.northeastern.teamprojectgroup16.R;
import edu.northeastern.teamprojectgroup16.model.PostModel;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<PostModel> postList;

    public PostAdapter(List<PostModel> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_todo, parent, false);
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
        Log.e("PostAdapter", postModel.toString());
        Log.e("Post Adapter: title", postModel.getTitle());
//        holder.userName.setText(postModel.getUserName());
        holder.textName.setText(postModel.getTitle());
        Glide.with(holder.imageView.getContext())
                .load(postModel.getImageUrl())
                .into(holder.imageView);
        // TODO
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
        ImageButton starButton;
        ImageButton repostButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            starButton = itemView.findViewById(R.id.starButton);
            profileImage = itemView.findViewById(R.id.profileImage);
            userName = itemView.findViewById(R.id.username);
            textName = itemView.findViewById(R.id.TextName);
            imageView = itemView.findViewById(R.id.imageView);
            likeButton = itemView.findViewById(R.id.likeButton);
            repostButton = itemView.findViewById(R.id.repostButton);
        }
    }
}
