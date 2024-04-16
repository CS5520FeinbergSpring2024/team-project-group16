package edu.northeastern.teamprojectgroup16.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.northeastern.teamprojectgroup16.ImageRecFragment;
import edu.northeastern.teamprojectgroup16.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // Tags for fragments
    private static final String TAG_POST_REC_FRAGMENT = "POST_REC_FRAGMENT";
    private static final String TAG_IMAGE_REC_FRAGMENT = "IMAGE_REC_FRAGMENT";

    // TODO: load server data from database using username.
    private int[] circleImages = {R.drawable.ic_add_server}; // Add your circle drawables here
    private ArrayList<String> serverIDArray = new ArrayList<>();
    private int selectedCircleIndex = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PostRecFragment postRecFragment;
    private ImageRecFragment imageRecFragment;
    private FloatingActionButton addCircleFab;
    private NestedScrollView nestedScrollView;
    
    private Button showPost;
    private Button showImage;
    private String currentServerID = null;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(TAG_POST_REC_FRAGMENT, param1);
        args.putString(TAG_IMAGE_REC_FRAGMENT, param2);
        fragment.setArguments(args);
        return fragment;
        // TODO: initialize the post data
        // TODO: Implement datasource change when click image
        // TODO: Implement detailed add circle (detailed info needed for add a server)
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout circleContainer = rootView.findViewById(R.id.circleContainer);
        updateServerBar(circleContainer);

        postRecFragment = new PostRecFragment();
        imageRecFragment = new ImageRecFragment();

        nestedScrollView = rootView.findViewById(R.id.homeScrollView);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    // Scrolling down
                    showHorizontalScrollBar();
                } else {
                    // Scrolling up
                    hideHorizontalScrollBar();
                }
            }
        });

        addCircleFab = rootView.findViewById(R.id.addCircleFab);
        addCircleFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add a new circle
                addCircle(circleContainer, circleImages.length - 1);
            }
        });
        
        showPost = rootView.findViewById(R.id.showPostBtn);
        showImage = rootView.findViewById(R.id.showImageBtn);
        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageRecyclerView();
            }
        });
        showPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPostRecyclerView();
            }
        });

         fetchServer(circleContainer);
    
        return rootView;
    }

    private void fetchServer(LinearLayout circleContainer) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        DatabaseReference serverRef = FirebaseDatabase.getInstance().getReference("users").child(userID).child("serverIDs");
        serverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        serverIDArray.add(dataSnapshot.getKey());
                        ImageView circleImageView = new ImageView(getContext());
                        circleImageView.setImageResource(circleImages[0]);
                        circleImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO:Should be modified to use serverIDArray rather than index.
                                handleCircleClick(dataSnapshot.getKey());
                            }
                        });
                        circleContainer.addView(circleImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showImageRecyclerView() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.recyclerViewContainer, imageRecFragment)
                .commit();
    }

    private void showPostRecyclerView() {
        if (currentServerID != null){
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.recyclerViewContainer, PostRecFragment.withServerIDPostRecFragment(currentServerID))
                    .commit();
        } else {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.recyclerViewContainer, postRecFragment)
                    .commit();
        }

    }

    private void hideHorizontalScrollBar() {
        HorizontalScrollView horizontalScrollView = getView().findViewById(R.id.horizontalScrollView);
        horizontalScrollView.setVisibility(View.GONE);
    }

    private void showHorizontalScrollBar() {
        HorizontalScrollView horizontalScrollView = getView().findViewById(R.id.horizontalScrollView);
        horizontalScrollView.setVisibility(View.VISIBLE);
    }

    private void updateServerBar(LinearLayout circleContainer) {
        // TODO: Fetch data from server
        for (int i = 1; i < circleImages.length; i++) {
            addCircle(circleContainer, i);
        }

    }

    private void addCircle(LinearLayout circleContainer, final int index) {
        Context context = requireContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Invitation Code");

        final EditText codeInput = new EditText(context);
        codeInput.setHint("Enter Code");

        builder.setView(codeInput);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String code = codeInput.getText().toString();
            saveCodeAndPassword(circleContainer, index, code);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    private void saveCodeAndPassword(LinearLayout circleContainer, int index, String code) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference serverRef = FirebaseDatabase.getInstance().getReference("servers");
        serverRef.orderByChild("code").equalTo(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String serverIDToUse = null;
                    for (DataSnapshot serverSnapshot : snapshot.getChildren()) {
                        String serverId = serverSnapshot.getKey();
                        String userId = user.getUid();
                        // Add the user to the server's members list
                        addMemberToServer(serverId, userId);

                        // Add the server to the user's servers list
                        addUserToServer(userId, serverId);
                        serverIDToUse = serverId;

                    }
                    ImageView circleImageView = new ImageView(getContext());
                    circleImageView.setImageResource(circleImages[index]);
                    String finalServerIDToUse = serverIDToUse;
                    circleImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle circle click event
                            handleCircleClick(finalServerIDToUse);
                        }
                    });
                    circleContainer.addView(circleImageView);
                } else {
                    Toast.makeText(getContext(), "Invalid code. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        // TODO: save data to firebase or verify the firebase server accessability

    }

    private static void addMemberToServer(String serverId, String userId) {
        // Get a reference to the server's members list
        DatabaseReference serverMembersRef = FirebaseDatabase.getInstance().getReference()
                .child("servers").child(serverId).child("memberIDs");

        // Add the user ID to the server's members list
        serverMembersRef.child(userId).setValue(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("User added to server: " + serverId);
                    } else {
                        System.out.println("Failed to add user to server: " + task.getException().getMessage());
                    }
                });
    }

    private static void addUserToServer(String userId, String serverId) {
        // Get a reference to the user's servers list
        DatabaseReference userServersRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("serverIDs");

        // Add the server ID to the user's servers list
        userServersRef.child(serverId).setValue(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("Server added to user: " + userId);
                    } else {
                        System.out.println("Failed to add server to user: " + task.getException().getMessage());
                    }
                });
    }

    private void handleCircleClick(String serverID) {
        currentServerID = serverID;
    }
}