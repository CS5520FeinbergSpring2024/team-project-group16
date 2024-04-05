package edu.northeastern.teamprojectgroup16;

import static android.app.Activity.RESULT_OK;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_PERMISSION = 2;
    ImageView imageViewPost;
    EditText editTextCaption;
    Button buttonPost;
    Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        imageViewPost = view.findViewById(R.id.imageViewPost);
        editTextCaption = view.findViewById(R.id.editTextCaption);
        buttonPost = view.findViewById(R.id.buttonPost);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

        imageViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null && !editTextCaption.getText().toString().isEmpty()) {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (firebaseUser != null) {
                        String userId = firebaseUser.getUid();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("serverIds");

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                List<String> serverIds = new ArrayList<>();
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    if (childSnapshot.getValue(Boolean.class)) {
                                        serverIds.add(childSnapshot.getKey());
                                    }
                                }

                                List<String> serverNames = new ArrayList<>();
                                for (String serverId : serverIds) {
                                    DatabaseReference serverReference = FirebaseDatabase.getInstance().getReference("servers").child(serverId).child("serverName");
                                    serverReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String serverName = snapshot.getValue(String.class);
                                            serverNames.add(serverName);

                                            if (serverNames.size() == serverIds.size()) {
                                                AlertDialog.Builder serverDialog = new AlertDialog.Builder(getActivity());
                                                serverDialog.setTitle("Choose a server");
                                                serverDialog.setItems(serverNames.toArray(new String[0]), (dialogInterface, which) -> {
                                                    String selectedServerName = serverNames.get(which);
                                                    Toast.makeText(getActivity(), "You selected server " + selectedServerName, Toast.LENGTH_SHORT).show();

                                                    // Here you create a new Post with the selectedServerName, imageUri and caption
                                                    //Post post = new Post(selectedServerName, imageUri, editTextCaption.getText().toString());

                                                    // Implement the logic in posting to firebase storage or another backend service
                                                });
                                                serverDialog.show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // Handle error here
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle error here
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "Please select an image and caption before posting", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewPost.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}