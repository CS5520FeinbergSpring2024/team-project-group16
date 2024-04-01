package edu.northeastern.teamprojectgroup16;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostRecAdapter extends RecyclerView.Adapter<PostRecAdapter.PostViewHolder> {
    private List<PostRec> postList;

    public PostRecAdapter(List<PostRec> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_rec, parent, false);
        return new PostViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostRec post = postList.get(position);
        holder.postImageView.setImageResource(post.getImageResId());
        holder.titleTextView.setText(post.getTitle());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView postImageView;
        public TextView titleTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postImageView = itemView.findViewById(R.id.postImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }
}
