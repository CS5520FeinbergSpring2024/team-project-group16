package edu.northeastern.teamprojectgroup16;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostRecFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostRecFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView postRecyclerView;
    private PostRecAdapter postAdapter;
    private List<PostRec> postRecList;

    public PostRecFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostRecFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostRecFragment newInstance(String param1, String param2) {
        PostRecFragment fragment = new PostRecFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_post_rec, container, false);
        postRecyclerView = rootView.findViewById(R.id.postRecyclerView);
        postRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        postRecList = new ArrayList<>(); // Create a list of sample posts
        postAdapter = new PostRecAdapter(postRecList);
        postRecyclerView.setAdapter(postAdapter);
        // Inflate the layout for this fragment
        return rootView;
    }

    public void addPostRec(PostRec postRec){
        this.postRecList.add(postRec);
        postAdapter.notifyAll();
    }

    public void deletePostRec(int index){
        this.postRecList.remove(index);
        postAdapter.notifyItemRemoved(index);
    }
}