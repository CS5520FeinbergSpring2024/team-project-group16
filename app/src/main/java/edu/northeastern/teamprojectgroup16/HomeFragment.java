package edu.northeastern.teamprojectgroup16;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.teamprojectgroup16.adapters.HomeAdapter;
import edu.northeastern.teamprojectgroup16.model.HomeModel;
import edu.northeastern.teamprojectgroup16.utilities.Constants;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;

    private HomeAdapter adapter;

    private FirebaseUser user;

    private List<HomeModel> list;

    private ListenerRegistration listenerRegistration;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        list = new ArrayList<>();
        adapter = new HomeAdapter(list);
        recyclerView.setAdapter(adapter);

        loadDataFromFirestore();

    }

    private void loadDataFromFirestore() {
        // sample data
        list.add(new HomeModel("Cecilia", "01/11/2024", null, null, ",", "123", "comments", 12));
        list.add(new HomeModel("Patrick", "01/12/2024", null, null, ",", "124", "comments",  11));
        list.add(new HomeModel("Sofia", "01/14/2024", null, null, ",", "125", "comments",  10));
        list.add(new HomeModel("Daniel", "01/16/2024", null, null, ",", "126", "comments",  19));

        CollectionReference reference = FirebaseFirestore.getInstance().collection(Constants.KEY_COLLECTION_USERS)
                .document(user.getUid())
                .collection(Constants.KEY_COLLECTION_POST_IMAGES);
        // Attach the listener and store the registration
        listenerRegistration = reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore Error", error.getMessage());
                    return;
                }

                // Update UI here based on the changes
                // For example, updating your list and notifying the adapter
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (listenerRegistration!= null) {
            listenerRegistration.remove();
        }
    }

    private void init(View view) {

        recyclerView = view.findViewById(R.id.homeRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
}