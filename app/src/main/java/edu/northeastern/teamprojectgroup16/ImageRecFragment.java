package edu.northeastern.teamprojectgroup16;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView imageRecRecyclerView;
    private ImageRecAdapter imageAdapter;
    private List<ImageRec> imageRecList;

    // TODO: Rename and change types of parameters
    private String mParam1;
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
    public static ImageRecFragment newInstance(String param1, String param2) {
        ImageRecFragment fragment = new ImageRecFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_rec, container, false);
        imageRecRecyclerView = rootView.findViewById(R.id.imageRecyclerView);
        imageRecRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        imageRecRecyclerView.setHasFixedSize(true);
        List<ImageRec> imageList = new ArrayList<>(); // Create a list of sample images
        imageAdapter = new ImageRecAdapter(imageList, getContext());
        imageRecRecyclerView.setAdapter(imageAdapter);

        FirebaseDatabase.getInstance().getReference("images")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                            ImageRec imageRec = new ImageRec(imageUrl);
                            if (imageRec != null) {
                                imageRecList.add(imageRec);
                            }
                        }
                        imageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors
                    }
                });
        // Inflate the layout for this fragment
        return rootView;
    }

    public void addImageRec(ImageRec imageRec){
        imageRecList.add(imageRec);
        imageAdapter.notifyDataSetChanged();
    }

    public void removeImageRec(int index){
        imageRecList.remove(index);
        imageAdapter.notifyItemRemoved(index);
    }
}