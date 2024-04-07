package edu.northeastern.teamprojectgroup16.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.teamprojectgroup16.R;
import edu.northeastern.teamprojectgroup16.databinding.ItemHomeBinding;
import edu.northeastern.teamprojectgroup16.model.HomeModel;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder>{
    private List<HomeModel> list;

    public HomeAdapter(List<HomeModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeBinding itemHomeBinding = ItemHomeBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new HomeViewHolder(itemHomeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.setHomeData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    static class HomeViewHolder extends RecyclerView.ViewHolder {
        ItemHomeBinding binding;

        HomeViewHolder(ItemHomeBinding itemHomeBinding) {
            super(itemHomeBinding.getRoot());
            binding = itemHomeBinding;
        }

        void setHomeData(HomeModel homeModel) {
            binding.TextName.setText(homeModel.getUserName());
            binding.textTime.setText(homeModel.getTimestamp());
            // like count
            int count = homeModel.getLikeCount();
            if (count == 0) {
                binding.textLikeCount.setVisibility(View.INVISIBLE);
            } else if (count == 1) {
                binding.textLikeCount.setText(String.format("%d %s", count, "like"));
            } else {
                binding.textLikeCount.setText(count + " likes");
            }

            // Handle post image similar to how user image is handled in UserAdapter
            if (homeModel.getPostImage() != null && !homeModel.getPostImage().isEmpty()) {
                Bitmap bitmap = getPostImage(homeModel.getPostImage());
                if (bitmap != null) {
                    binding.imageView.setImageBitmap(bitmap);
                } else {
                    // Set a default image or hide the image view
                    binding.imageView.setImageResource(R.drawable.ic_launcher_foreground); // Or set a default image
                }
            } else {
                // Set a default image or hide the image view
                binding.imageView.setImageResource(R.drawable.ic_launcher_foreground); // Or set a default image
            }
        }

        private Bitmap getPostImage(String encodedImage) {
            try {
                byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } catch (IllegalArgumentException e) {
                Log.e("HomeAdapter", "Error decoding Base64 string: " + e.getMessage());
                return null;
            }
        }
    }
}
