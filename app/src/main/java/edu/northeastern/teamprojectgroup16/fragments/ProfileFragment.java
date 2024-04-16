package edu.northeastern.teamprojectgroup16.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.northeastern.teamprojectgroup16.Login;
import edu.northeastern.teamprojectgroup16.R;
import edu.northeastern.teamprojectgroup16.activities.FavoriteActivity;

public class ProfileFragment extends Fragment {

    private TextView textViewUsername;
    private TextView textViewEmail;
    ImageView btnLogOut;
    ImageView favoriteButton;
    ImageButton editProfileBtn;
    ImageView profileView;
    private String encodedImage;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewUsername = rootView.findViewById(R.id.textViewUsername);
        textViewEmail = rootView.findViewById(R.id.textViewEmail);
        favoriteButton = rootView.findViewById(R.id.relates);
        editProfileBtn = rootView.findViewById(R.id.edit_profileImage);
        profileView = rootView.findViewById(R.id.profileImage);

        // Go to the favorite activity
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FavoriteActivity.class);
                startActivity(intent);
            }
        });

        // Retrieve the current user from Firebase Authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String username = user.getDisplayName();
            String email = user.getEmail();

            // Update the TextViews with user's username and email
            textViewUsername.setText("Username: " + username);
            textViewEmail.setText("Email: " + email);
        }

        btnLogOut = rootView.findViewById(R.id.btn_logout);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                // Navigate back to the login screen or perform any other desired action
                // For example, you can navigate back to the login screen if you have one
                Intent intent = new Intent(getActivity(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
                // You may also display a toast or a Snackbar to inform the user that they have been logged out
                Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            }
        });

        // edit profile
        editProfile();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadImageFromDatabase();
    }

    private void editProfile() {
        editProfileBtn.setOnClickListener(v -> {
            pickImage.launch("image/*");
        });
    }

    // pick image
    private final ActivityResultLauncher<String> pickImage = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    uploadImage(uri);
                }
            }
    );

    private void loadImageFromDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) return; // make sure log in
        DatabaseReference myRef = database.getReference("users").child(userId).child("imageUrl");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageUrl = dataSnapshot.getValue(String.class);
                if (imageUrl != null) {
                    // Use Glide or Picasso to load the image
                    Glide.with(getActivity()).load(imageUrl).into(profileView);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ProfileFragment", "Failed to read image URL.", error.toException());
            }
        });
    }

    private void uploadImage(Uri fileUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + fileUri.getLastPathSegment());

        imageRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the URL of the uploaded image
                    imageRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                        // Save image URL to Firebase Realtime Database
                        saveImageUrlToDatabase(downloadUrl.toString());
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle unsuccessful uploads
                    e.printStackTrace();
                });
    }

    private void saveImageUrlToDatabase(String imageUrl) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) return; // make sure log in
        DatabaseReference myRef = database.getReference("users").child(userId).child("imageUrl");

        myRef.setValue(imageUrl)
                .addOnSuccessListener(aVoid -> {
                    // Data saved successfully!
                    Log.d("ProfileFragment", "Image URL saved to database successfully!");
                })
                .addOnFailureListener(e -> {
                    // Failed to save data
                    e.printStackTrace();
                });
    }
}
