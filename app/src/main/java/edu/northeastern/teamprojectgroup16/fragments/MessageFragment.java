package edu.northeastern.teamprojectgroup16.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import edu.northeastern.teamprojectgroup16.ChatActivity;
import edu.northeastern.teamprojectgroup16.R;
import edu.northeastern.teamprojectgroup16.UserModel;
import edu.northeastern.teamprojectgroup16.adapters.UsersAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public MessageFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment MessageFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static MessageFragment newInstance(String param1, String param2) {
//        MessageFragment fragment = new MessageFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//
//
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
////         Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_message, container, false);
//
//    }
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        // Launch ChatActivity
//        Intent intent = new Intent(getActivity(), ChatActivity.class);
//        startActivity(intent);
//    }

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private String yourName;
    private UsersAdapter usersAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        // Set up toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        String userName = requireActivity().getIntent().getStringExtra("user");
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(userName);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        usersAdapter = new UsersAdapter(requireContext());
        recyclerView.setAdapter(usersAdapter);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersAdapter.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uId = dataSnapshot.getKey();
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    if (userModel != null && userModel.getUserID() != null && userModel.getUserID().equals(FirebaseAuth.getInstance().getUid())) {
                        usersAdapter.add(userModel);
                    }
                }
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}