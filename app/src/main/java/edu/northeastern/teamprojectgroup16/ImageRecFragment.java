package edu.northeastern.teamprojectgroup16;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.teamprojectgroup16.model.ImageRec;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageRecFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageRecFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private RecyclerView imageRecRecyclerView;
    private ImageRecAdapter imageAdapter;
    private ArrayList<ImageRec> imageRecList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String serverID = null;
    private String mParam2;

    public ImageRecFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageRecFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageRecFragment newInstance(String serverID) {
        ImageRecFragment fragment = new ImageRecFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, serverID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            serverID = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        fetchData(serverID);
    }

    private void fetchData(String serverID) {
        if (serverID != null){
            DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");
            Log.e("serverID ", serverID);
            postsRef.orderByChild("serverId").equalTo(serverID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    imageRecList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        ImageRec image = new ImageRec(dataSnapshot.child("imageUrl").getValue(String.class));
                        imageRecList.add(image);
                    }
                    imageAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_rec, container, false);
        imageRecRecyclerView = rootView.findViewById(R.id.imageRecyclerView);
        imageRecRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        imageRecRecyclerView.setHasFixedSize(true);
        imageAdapter = new ImageRecAdapter(imageRecList, getContext());
        imageRecRecyclerView.setAdapter(imageAdapter);
        // Inflate the layout for this fragment
        return rootView;
    }

}