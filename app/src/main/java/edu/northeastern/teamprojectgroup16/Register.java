package edu.northeastern.teamprojectgroup16;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import edu.northeastern.teamprojectgroup16.model.UserModel;

public class Register extends AppCompatActivity {

    EditText editTextFName, editTextLName, editTextUName, editTextEmail, editTextPassword;
    Button buttonSignUp;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextFName = findViewById(R.id.firstName);
        editTextLName = findViewById(R.id.lastName);
        editTextUName = findViewById(R.id.username);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        mAuth = FirebaseAuth.getInstance();

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, firstName, lastName, userName;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                firstName = String.valueOf(editTextFName.getText());
                lastName = String.valueOf(editTextLName.getText());
                userName = String.valueOf(editTextUName.getText());

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Email cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(firstName)) {
                    Toast.makeText(Register.this, "First name cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(lastName)) {
                    Toast.makeText(Register.this, "Last name cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(userName)) {
                    Toast.makeText(Register.this, "User name cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(userName).build();
                                    user.updateProfile(profileUpdates);

                                    UserModel userModel = new UserModel(user.getUid(), userName, email, password, new ArrayList<>());

                                    databaseReference = FirebaseDatabase.getInstance().getReference("users");
                                    databaseReference.child("users");
                                    databaseReference.child(user.getUid()).setValue(userModel);


//
//                                    databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(userModel);

                                    Toast.makeText(Register.this, "Account Created!",
                                            Toast.LENGTH_SHORT).show();

//                                    Intent intent = new Intent(Register.this, MainActivity.class);
//                                    intent.putExtra("User Name", userName);
//                                    startActivity(intent);
//                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
    public void navigateToSignInActivity(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    private void SignUp() {
        super.onStart();

    }
}