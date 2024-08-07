package edu.northeastern.teamprojectgroup16;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SearchFragment extends Fragment {

    private EditText editTextSearch;
    private Button buttonSearch;
    private DatabaseReference usersRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        buttonSearch = view.findViewById(R.id.buttonSearch);
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = editTextSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(searchQuery)) {
                    performSearch(searchQuery);
                } else {
                    Toast.makeText(getActivity(), "Please enter a username", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void performSearch(String searchQuery) {
        Query query = usersRef.orderByChild("userName").equalTo(searchQuery);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserModel userModel = userSnapshot.getValue(UserModel.class);
                    if (userModel != null) {
                        displayUserDetails(userModel);
                        return;
                    }
                }
                // Handle if no matching user found
                // For example: show a toast message or update UI
                Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database query cancellation or error
                Toast.makeText(getContext(), "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUserDetails(UserModel userModel) {
        // Navigate to AfterSearchFragment and pass user details
        AfterSearchFragment afterSearchFragment = AfterSearchFragment.newInstance(userModel.getUserName(), userModel.getUserEmail());
        getParentFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, afterSearchFragment) // Assuming R.id.frameLayout is your fragment container
                .addToBackStack(null)
                .commit();
    }



    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

}
