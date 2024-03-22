package edu.northeastern.teamprojectgroup16;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    // Method to navigate to SignUpActivity
    public void navigateToSignUpActivity(View view) {
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }
}