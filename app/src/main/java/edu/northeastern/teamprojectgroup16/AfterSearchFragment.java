package edu.northeastern.teamprojectgroup16;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AfterSearchFragment extends Fragment {

    private TextView textViewUsername;
    private TextView textViewEmail;

    public static AfterSearchFragment newInstance(String username, String email) {
        AfterSearchFragment fragment = new AfterSearchFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("email", email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_after_search, container, false);

        textViewUsername = view.findViewById(R.id.textViewUsername);
        textViewEmail = view.findViewById(R.id.textViewEmail);

        Bundle args = getArguments();
        if (args != null) {
            String username = args.getString("username");
            String email = args.getString("email");

            textViewUsername.setText(username);
            textViewEmail.setText(email);
        }

        return view;
    }
}