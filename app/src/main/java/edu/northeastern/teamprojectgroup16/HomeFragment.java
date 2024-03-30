package edu.northeastern.teamprojectgroup16;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: load server data from database using username.
    private int[] circleImages = {R.drawable.ic_add_server}; // Add your circle drawables here
    private int selectedCircleIndex = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout circleContainer = rootView.findViewById(R.id.circleContainer);
        updateServerBar(circleContainer);

        NestedScrollView nestedScrollView = rootView.findViewById(R.id.homeScrollView);
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

        FloatingActionButton addCircleFab = rootView.findViewById(R.id.addCircleFab);
        addCircleFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add a new circle
                addCircle(circleContainer, circleImages.length);
            }
        });

        return rootView;
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
        for (int i = 0; i < circleImages.length; i++) {
            addCircle(circleContainer, i);
        }

    }

    private void addCircle(LinearLayout circleContainer, final int index) {
        ImageView circleImageView = new ImageView(getContext());
        circleImageView.setImageResource(circleImages[index]);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle circle click event
                handleCircleClick(index);
            }
        });
        circleContainer.addView(circleImageView);
    }

    private void handleCircleClick(int index) {
        // TODO: change displayed data.
    }
}