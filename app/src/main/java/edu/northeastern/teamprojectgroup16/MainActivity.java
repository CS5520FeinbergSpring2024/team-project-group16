package edu.northeastern.teamprojectgroup16;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import edu.northeastern.teamprojectgroup16.databinding.ActivityCheckIndividualPostBinding;
import edu.northeastern.teamprojectgroup16.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button btnCreateServer;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnCreateServer = findViewById(R.id.buttonCreateServer);
        mAuth = FirebaseAuth.getInstance();
        btnCreateServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        setListeners();
    }
    private void setListeners() {
        replaceFragment(new HomeFragment());
        // Listeners for navigation buttons
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (id == R.id.search) {
                replaceFragment(new SearchFragment());
            } else if (id == R.id.add) {
                replaceFragment(new AddFragment());
            } else if (id == R.id.message) {
                replaceFragment(new MessageFragment());
            } else if (id == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}