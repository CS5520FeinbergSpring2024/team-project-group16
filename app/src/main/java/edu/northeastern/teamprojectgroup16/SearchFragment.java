import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.teamprojectgroup16.UserModel;

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
                if (!searchQuery.isEmpty()) {
                    performSearch(searchQuery);
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
                // Clear previous search results if any
                // Iterate through search results (should be at most one)
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserModel userModel = userSnapshot.getValue(UserModel.class);
                    if (userModel != null) {
                        // Display user details in AfterSearchFragment
                        displayUserDetails(userModel);
                        return; // Stop after finding the first matching user
                    }
                }
                // Handle if no matching user found
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database query cancellation or error
            }
        });
    }

    private void displayUserDetails(UserModel userModel) {
        // Navigate to AfterSearchFragment and pass user details
        AfterSearchFragment afterSearchFragment = AfterSearchFragment.newInstance(userModel.getUserName(), userModel.getUserEmail());
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, afterSearchFragment)
                .addToBackStack(null)
                .commit();
    }
}
