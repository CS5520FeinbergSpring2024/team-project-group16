package edu.northeastern.teamprojectgroup16;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.northeastern.teamprojectgroup16.databinding.ActivityMainBinding;
import edu.northeastern.teamprojectgroup16.fragments.HomeFragment;
import edu.northeastern.teamprojectgroup16.fragments.ProfileFragment;
import edu.northeastern.teamprojectgroup16.model.ServerModel;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button btnCreateServer;
    ActivityMainBinding binding;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnCreateServer = findViewById(R.id.buttonCreateServer);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            initializeUserData(user);
        }
        btnCreateServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Create Server", "Clicked");
                Context context = MainActivity.this;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Enter the Name and Code of Server");

                final EditText nameInput = new EditText(context);
                nameInput.setHint("Enter the Name");
                final EditText codeInput = new EditText(context);
                codeInput.setHint("Enter the Code");

                final LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(nameInput);
                layout.addView(codeInput);

                builder.setView(layout);

                builder.setPositiveButton("OK", (dialog, which) -> addServer(nameInput.getText().toString(), codeInput.getText().toString()));

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                builder.show();
            }
        });
        setListeners();
    }

    private void initializeUserData(FirebaseUser user) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        userRef.child("savedPosts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) { // 只有当savedPosts不存在时才初始化它
                    userRef.child("savedPosts").setValue(new ArrayList<>())
                            .addOnSuccessListener(aVoid -> Log.d("InitUserData", "User data initialized successfully"))
                            .addOnFailureListener(e -> Log.e("InitUserData", "Failed to initialize user data", e));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("InitUserData", "Failed to check if savedPosts exists", databaseError.toException());
            }
        });

//        Map<String, Object> updates = new HashMap<>();
//        updates.put("savedPosts", new ArrayList<>()); // 初始化 savedPosts 为一个空列表
//
//        userRef.updateChildren(updates)
//                .addOnSuccessListener(aVoid -> Log.d("InitUserData", "User data initialized successfully"))
//                .addOnFailureListener(e -> Log.e("InitUserData", "Failed to initialize user data", e));
    }


    private void addServer(String serverName, String code){

        // Create a ServerModel object
        ServerModel newServer = new ServerModel();
        newServer.setServerName(serverName);
        newServer.setCode(code);

        // Get the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Add current user as a member of the server
            newServer.addMember(user.getUid());
        }

        // Get a reference to the Firebase database
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        // Generate a new serverID
        String serverId = dbRef.child("servers").push().getKey();

        // Set the serverID for the new server
        newServer.setServerID(serverId);

        // Add the new server to the Firebase database
// Add the new server to the Firebase database
        if (serverId != null) {
            dbRef.child("servers").child(serverId).setValue(newServer)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // server added successfully
                            Log.i("ServerAdd", "Server added successfully");

                            // Get a reference to the Firebase database for `users`
                            DatabaseReference dbUsersRef = FirebaseDatabase.getInstance().getReference("users");

                            // Update user document with added server Id
                            dbUsersRef.child(user.getUid()).child("serverIds").child(serverId).setValue(true)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("ServerAdd", "Server Id added to user document successfully");
                                            Toast.makeText(MainActivity.this, "Server added successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ServerAdd", "Error adding server Id to user document", e);
                                            Toast.makeText(MainActivity.this, "Error adding server Id to user document", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors that occur during the process
                            Log.e("ServerAdd", "Error adding server", e);
                        }
                    });
            dbRef.child("servers").child(serverId).child("code").setValue(code);
        }
    }
    private void setListeners() {
        replaceFragment(new HomeFragment());
        // Listeners for navigation buttons
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (id == R.id.search) {
                replaceFragment(new SearchFragment());
            } else if (id == R.id.add) {
                replaceFragment(new AddFragment());
            } else if (id == R.id.message) {
                replaceFragment(new MessageFragment());
            } else if (id == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}